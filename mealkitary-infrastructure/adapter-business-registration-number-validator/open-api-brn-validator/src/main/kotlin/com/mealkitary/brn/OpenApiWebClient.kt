package com.mealkitary.brn

import com.mealkitary.brn.payload.OpenApiBrnStatusPayload
import com.mealkitary.brn.payload.OpenApiBrnStatusResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Component
class OpenApiWebClient(
    private val webClient: WebClient,
    @Value("\${openapi.brn.serviceKey}")
    private val serviceKey: String
) {

    fun requestStatus(payload: OpenApiBrnStatusPayload, baseUrl: String): OpenApiBrnStatusResponse {
        val openApiBrnStatusResponse = webClient.post()
            .uri("$baseUrl/v1/status?serviceKey=$serviceKey")
            .headers { setHeader(it) }
            .body(Mono.just(payload), OpenApiBrnStatusPayload::class.java)
            .exchangeToMono {
                if (it.statusCode() == HttpStatus.OK) {
                    it.bodyToMono(OpenApiBrnStatusResponse::class.java)
                } else {
                    Mono.error(RuntimeException())
                }
            }
            .block()

        return openApiBrnStatusResponse!!
    }

    private fun setHeader(
        headers: HttpHeaders,
    ) {
        headers.acceptCharset = listOf(StandardCharsets.UTF_8)
        headers.contentType = MediaType.APPLICATION_JSON
    }
}
