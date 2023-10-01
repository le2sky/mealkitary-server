package com.mealkitary.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriBuilderFactory
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

private const val TEEN_MEGA_BYTE = 10 * 1024 * 1024

@Configuration(proxyBeanMethods = false)
class WebClientConfig {

    @Bean
    fun webClient() = WebClient.builder()
        .uriBuilderFactory(uriBuilderFactory())
        .exchangeStrategies(exchangeStrategies())
        .clientConnector(ReactorClientHttpConnector(httpClient()))
        .build()

    private fun uriBuilderFactory(): UriBuilderFactory {
        val factory = DefaultUriBuilderFactory()
        factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE

        return factory
    }

    private fun exchangeStrategies() = ExchangeStrategies.builder()
        .codecs { configurer ->
            configurer.defaultCodecs().maxInMemorySize(TEEN_MEGA_BYTE)
        }
        .build()

    private fun httpClient() = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected { connection ->
            connection
                .addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
        }
}
