package com.mealkitary.paymentgateway

import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.paymentgateway.payload.TossErrorResponse
import com.mealkitary.paymentgateway.payload.TossPayment
import com.mealkitary.paymentgateway.payload.TossPaymentCancelPayload
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
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
            .headers { setHeader(it, payment) }
            .body(Mono.just(payment), TossPayment::class.java)
            .exchangeToMono { exceptionHandler(it) }
            .block()
    }

    fun requestCancel(payment: TossPayment, payload: TossPaymentCancelPayload, baseUrl: String) {
        webClient.post()
            .uri("$baseUrl/v1/payments/${payment.paymentKey}/cancel")
            .headers { setHeader(it, payment) }
            .body(Mono.just(payload), TossPaymentCancelPayload::class.java)
            .exchangeToMono { exceptionHandler(it) }
            .block()
    }

    private fun setHeader(
        headers: HttpHeaders,
        payment: TossPayment
    ) {
        headers.acceptCharset = listOf(StandardCharsets.UTF_8)
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBasicAuth(codec.encode("$secretKey:"))
        headers.set("Idempotency-Key", payment.orderId)
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
