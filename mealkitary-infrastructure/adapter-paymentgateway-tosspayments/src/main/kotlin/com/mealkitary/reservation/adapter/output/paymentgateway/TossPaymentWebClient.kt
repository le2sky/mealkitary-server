package com.mealkitary.reservation.adapter.output.paymentgateway

import com.mealkitary.common.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Component
class TossPaymentWebClient(
    private val codec: TossPaymentCodec,
    private val webClient: WebClient,
    @Value("\${pg.toss.secretKey}")
    private val secretKey: String
) {

    fun requestConfirm(payment: TossPayment, baseUrl: String) {
        webClient.post()
            .uri("$baseUrl/v1/payments/confirm")
            .headers {
                it.acceptCharset = listOf(StandardCharsets.UTF_8)
                it.contentType = MediaType.APPLICATION_JSON
                it.setBasicAuth(codec.encode("$secretKey:"))
                it.set("Idempotency-Key", payment.orderId)
            }
            .body(Mono.just(payment), TossPayment::class.java)
            .exchangeToMono { exceptionHandler(it) }
            .block()
    }

    private fun exceptionHandler(response: ClientResponse): Mono<Throwable> {
        return when (response.statusCode()) {
            HttpStatus.OK -> Mono.empty()

            HttpStatus.BAD_REQUEST -> response.bodyToMono(TossErrorResponse::class.java).flatMap {
                Mono.error(IllegalArgumentException(it.message))
            }

            HttpStatus.NOT_FOUND -> response.bodyToMono(TossErrorResponse::class.java).flatMap {
                Mono.error(EntityNotFoundException(it.message))
            }

            else -> Mono.error(RuntimeException())
        }
    }
}
