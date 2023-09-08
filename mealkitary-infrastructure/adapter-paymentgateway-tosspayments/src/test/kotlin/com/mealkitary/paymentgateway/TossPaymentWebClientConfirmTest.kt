package com.mealkitary.paymentgateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.paymentgateway.codec.UrlSafeBase64Codec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.throwable.shouldHaveMessage
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class TossPaymentWebClientConfirmTest : AnnotationSpec() {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var webClient: WebClient
    private lateinit var tossPaymentWebClient: TossPaymentWebClient
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("").toString())
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)
            }
            .defaultHeaders { headers ->
                headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            }
            .build()
        tossPaymentWebClient = TossPaymentWebClient(UrlSafeBase64Codec(), webClient, "secretKey")
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `멱등키는 orderId이다`() {
        val expectedPath = "/v1/payments/confirm"
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        tossPaymentWebClient.requestConfirm(
            TossPayment.of(
                "paymentKey",
                "reservation-01",
                20000
            ),
            mockWebServer.url("").toString()
        )

        val recordedRequest = mockWebServer.takeRequest()
        recordedRequest.method shouldBe "POST"
        recordedRequest.path shouldBe expectedPath
        recordedRequest.getHeader("Idempotency-Key").shouldBe("reservation-01")
    }

    @Test
    fun `200 OK를 받으면 아무 예외도 발생하지 않는다`() {
        val expectedPath = "/v1/payments/confirm"
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        tossPaymentWebClient.requestConfirm(
            TossPayment.of(
                "paymentKey",
                "reservation-01",
                20000
            ),
            mockWebServer.url("").toString()
        )

        val recordedRequest = mockWebServer.takeRequest()
        recordedRequest.method shouldBe "POST"
        recordedRequest.path shouldBe expectedPath
        recordedRequest.getHeader("Authorization").shouldNotBeBlank()
        recordedRequest.getHeader("Idempotency-Key").shouldNotBeBlank()
    }

    @Test
    fun `400 에러가 발생하면 IllegalArgumentException으로 처리한다`() {
        val expectedMessage = "잘못된 요청입니다."
        val body = TossErrorResponse("BAD_REQUEST", expectedMessage)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(body))
                .setResponseCode(400)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        shouldThrow<IllegalArgumentException> {
            tossPaymentWebClient.requestConfirm(
                TossPayment.of(
                    "paymentKey",
                    "reservation-01",
                    20000
                ),
                mockWebServer.url("").toString()
            )
        } shouldHaveMessage expectedMessage
    }

    @Test
    fun `404 에러가 발생하면 EntityNotFoundException으로 처리한다`() {
        val expectedMessage = "존재하지 않는 결제입니다."
        val body = TossErrorResponse("NOT_FOUND", expectedMessage)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(body))
                .setResponseCode(404)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        shouldThrow<EntityNotFoundException> {
            tossPaymentWebClient.requestConfirm(
                TossPayment.of(
                    "paymentKey",
                    "reservation-01",
                    20000
                ),
                mockWebServer.url("").toString()
            )
        } shouldHaveMessage expectedMessage
    }

    // TODO: 401, 403, 500 처리 미흡
    @Test
    fun `401 에러가 발생하면 RuntimeException으로 처리한다`() {
        val body = TossErrorResponse("UNAUTHORIZED_KEY", "인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다.")
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(body))
                .setResponseCode(401)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        shouldThrow<RuntimeException> {
            tossPaymentWebClient.requestConfirm(
                TossPayment.of(
                    "paymentKey",
                    "reservation-01",
                    20000
                ),
                mockWebServer.url("").toString()
            )
        }
    }

    @Test
    fun `403 에러가 발생하면 RuntimeException으로 처리한다`() {
        val body = TossErrorResponse("FORBIDDEN_REQUEST", "허용되지 않은 요청입니다.")
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(body))
                .setResponseCode(403)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        shouldThrow<RuntimeException> {
            tossPaymentWebClient.requestConfirm(
                TossPayment.of(
                    "paymentKey",
                    "reservation-01",
                    20000
                ),
                mockWebServer.url("").toString()
            )
        }
    }

    @Test
    fun `500 에러가 발생하면 RuntimeException으로 처리한다`() {
        val body = TossErrorResponse("UNKNOWN_PAYMENT_ERROR", "결제에 실패했습니다.")
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(body))
                .setResponseCode(500)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        shouldThrow<RuntimeException> {
            tossPaymentWebClient.requestConfirm(
                TossPayment.of(
                    "paymentKey",
                    "reservation-01",
                    20000
                ),
                mockWebServer.url("").toString()
            )
        }
    }
}
