package com.mealkitary.addess

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.address.KakaoApiWebClient
import com.mealkitary.address.payload.Address
import com.mealkitary.address.payload.Document
import com.mealkitary.address.payload.KakaoApiAddressResponse
import com.mealkitary.address.payload.Meta
import com.mealkitary.address.payload.RoadAddress
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

        val actualResponse = kakaoApiWebClient.requestAddress(address, mockWebServer.url("").toString())

        val recordedRequest = mockWebServer.takeRequest()
        recordedRequest.method shouldBe "GET"

        actualResponse.documents[0].address!!.h_code shouldBe response.documents[0].address!!.h_code
        actualResponse.documents[0].x shouldBe response.documents[0].x
        actualResponse.documents[0].y shouldBe response.documents[0].y
        actualResponse.documents[0].address!!.region_1depth_name shouldBe
            response.documents[0].address!!.region_1depth_name
        actualResponse.documents[0].address!!.region_2depth_name shouldBe
            response.documents[0].address!!.region_2depth_name
        actualResponse.documents[0].address!!.region_3depth_name shouldBe
            response.documents[0].address!!.region_3depth_name
        actualResponse.documents[0].road_address!!.road_name shouldBe
            response.documents[0].road_address!!.road_name
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
                kakaoApiWebClient.requestAddress("경기도남양주시다산중앙로82번안길132-12", mockWebServer.url("").toString())
            }
        }
    }

    private fun createResponse() = KakaoApiAddressResponse(
        documents = listOf(
            Document(
                x = "127.166069448936",
                y = "37.6120947950094",
                address = Address(
                    region_1depth_name = "경기",
                    region_2depth_name = "남양주시",
                    region_3depth_name = "다산동",
                    region_3depth_h_name = "",
                    h_code = "4136011200",
                    address_name = "",
                    main_address_no = "",
                    sub_address_no = "",
                    mountain_yn = "",
                    b_code = "",
                    x = "",
                    y = ""
                ),
                road_address = RoadAddress(
                    road_name = "다산중앙로82번안길",
                    address_name = "",
                    x = "",
                    y = "",
                    building_name = "",
                    main_building_no = "",
                    sub_building_no = "",
                    region_1depth_name = "",
                    region_2depth_name = "",
                    region_3depth_name = "",
                    underground_yn = "",
                    zone_no = ""
                ),
                address_name = "",
                address_type = ""
            )
        ),
        meta = Meta(false, 1, 1)
    )
}
