package com.mealkitary.brn

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.brn.payload.OpenApiBrnStatus
import com.mealkitary.brn.payload.OpenApiBrnStatusPayload
import com.mealkitary.brn.payload.OpenApiBrnStatusResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class OpenApiWebClientTest : AnnotationSpec() {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var webClient: WebClient
    private lateinit var openApiWebClient: OpenApiWebClient
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

        openApiWebClient = OpenApiWebClient(webClient, "serviceKey")
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `200 OK를 받으면 아무 예외도 발생하지 않는다`() {
        val expectedPath = "/v1/status?serviceKey=serviceKey"
        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(createResponse()))
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        openApiWebClient.requestStatus(OpenApiBrnStatusPayload(listOf("1232323456")), mockWebServer.url("").toString())

        val recordedRequest = mockWebServer.takeRequest()
        recordedRequest.path shouldBe expectedPath
        recordedRequest.method shouldBe "POST"
        recordedRequest.body.toString().contains("1232323456")
    }

    @Test
    fun `200이 아닌 코드는 RuntimeException으로 처리한다`() {
        listOf(300, 400, 401, 500).forAll {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(it)
                    .setBody(objectMapper.writeValueAsString(createResponse()))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )

            shouldThrow<RuntimeException> {
                openApiWebClient.requestStatus(
                    OpenApiBrnStatusPayload(listOf("1232323456")),
                    mockWebServer.url("").toString()
                )
            }
        }
    }

    private fun createResponse() = OpenApiBrnStatusResponse(
        "OK",
        0,
        0,
        listOf(
            OpenApiBrnStatus(
                "", "계속사업자", "", "", "", "", "", "", "", "", ""
            )
        )
    )
}
