package com.mealkitary.addess

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.address.KakaoApiWebClient
import com.mealkitary.address.payload.KakaoApiAddressResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.lang.RuntimeException

class KakaoApiWebClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var webClient: WebClient
    private lateinit var kakaoApiWebClient: KakaoApiWebClient
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

        kakaoApiWebClient = KakaoApiWebClient(webClient, "serviceKey")
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `200 OK를 받으면 아무 예외도 발생하지 않는다`() {
        val address = "경기도 남양주시 다산중앙로82번안길 132-12"
        val response = createResponse()

        mockWebServer.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(response))
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )

        val actualResponse = kakaoApiWebClient.requestAddress(address)

        val recordedRequest = mockWebServer.takeRequest()
        recordedRequest.method shouldBe "GET"

        actualResponse.document.road_address.h_code shouldBe response.document.road_address.h_code
        actualResponse.document.x shouldBe response.document.x
        actualResponse.document.y shouldBe response.document.y
        actualResponse.document.address.region_1depth_name shouldBe response.document.address.region_1depth_name
        actualResponse.document.address.region_2depth_name shouldBe response.document.address.region_2depth_name
        actualResponse.document.address.region_3depth_name shouldBe response.document.address.region_3depth_name
        actualResponse.document.road_address.road_name shouldBe response.document.road_address.road_name
    }

    @Test
    fun `200이 아닌 코드는 RuntimeException으로 처리한다`() {
        listOf(400, 401, 500).forAll {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(it)
                    .setBody(objectMapper.writeValueAsString(createResponse()))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
            shouldThrow<RuntimeException> {
                kakaoApiWebClient.requestAddress("경기도남양주시다산중앙로82번안길132-12")
            }
        }
    }

    private fun createResponse() = KakaoApiAddressResponse(
        document = KakaoApiAddressResponse.Document(
            x = "127.166069448936",
            y = "37.6120947950094",
            address = KakaoApiAddressResponse.Address(
                region_1depth_name = "경기",
                region_2depth_name = "남양주시",
                region_3depth_name = "다산동"
            ),
            road_address = KakaoApiAddressResponse.RoadAddress(
                road_name = "다산중앙로82번안길",
                h_code = "4136011200"
            )
        )
    )
}
