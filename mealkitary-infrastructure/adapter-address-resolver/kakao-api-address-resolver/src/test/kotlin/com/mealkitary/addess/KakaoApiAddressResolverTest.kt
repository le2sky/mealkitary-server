package com.mealkitary.addess

import com.mealkitary.address.KakaoApiAddressResolver
import com.mealkitary.address.KakaoApiWebClient
import com.mealkitary.address.payload.Address
import com.mealkitary.address.payload.Document
import com.mealkitary.address.payload.KakaoApiAddressResponse
import com.mealkitary.address.payload.Meta
import com.mealkitary.address.payload.RoadAddress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class KakaoApiAddressResolverTest : AnnotationSpec() {

    private val kakaoApiWebClient = mockk<KakaoApiWebClient>()
    private val kakaoApiAddressResolver = KakaoApiAddressResolver(kakaoApiWebClient)

    @Test
    fun `카카오 API를 통해 주소 정보를 받아온다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument(
                        "ROAD_ADDR",
                        createRoadAddress(
                            region1depthName = "경기",
                            region2depthName = "안양시 만안구",
                            region3depthName = "안양동",
                            roadName = "병목안로",
                            mainBuildingNo = "123",
                            subBuildingNo = "23"
                        ),
                        createAddress(hCode = "4117158100"), x = "126.909658990799", y = "37.3921505309817"
                    )
                ),
                createMeta(1)
            )
        }

        val shopAddress = kakaoApiAddressResolver.resolve("경기도 안양시 만안구 병목안로 123-23")

        shopAddress.address.region1DepthName shouldBe "경기"
        shopAddress.address.region2DepthName shouldBe "안양시 만안구"
        shopAddress.address.region3DepthName shouldBe "안양동"
        shopAddress.address.roadName shouldBe "병목안로 123-23"
        shopAddress.coordinates.longitude shouldBe 126.909658990799
        shopAddress.coordinates.latitude shouldBe 37.3921505309817
        shopAddress.cityCode shouldBe "4117158100"
    }

    @Test
    fun `meta의 검색 결과가 존재하지 않는 경우, 예외를 발생한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(createDocument("LOAD_ADDR", createRoadAddress(), createAddress())), createMeta(0)
            )
        }

        shouldThrow<IllegalArgumentException> {
            kakaoApiAddressResolver.resolve("경기도 안양시")
        } shouldHaveMessage "올바른 도로명 주소를 입력해주세요."
    }

    @Test
    fun `document가 하나도 포함되어 있지 않다면, 예외를 발생한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(emptyList(), createMeta(10))
        }

        shouldThrow<IllegalArgumentException> {
            kakaoApiAddressResolver.resolve("경기도 안양시")
        } shouldHaveMessage "올바른 도로명 주소를 입력해주세요."
    }

    @Test
    fun `검색 결과의 첫 번째 결과의 주소 타입이 ROAD_ADDR이 아닌 경우 예외를 발생한다`() {
        listOf("REGION", "ROAD", "REGION_ADDR").forAll { addrType ->
            every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
                KakaoApiAddressResponse(
                    listOf(
                        createDocument(addrType, createRoadAddress(), createAddress()),
                        createDocument("ROAD_ADDR", createRoadAddress(), createAddress())
                    ),
                    createMeta(1)
                )
            }

            shouldThrow<IllegalArgumentException> {
                kakaoApiAddressResolver.resolve("경기도 안양시")
            } shouldHaveMessage "올바른 도로명 주소를 입력해주세요."
        }
    }

    @Test
    fun `해당하는 도로명 주소(RoadAddress)가 존재하지 않는 경우, 예외를 발생한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument("ROAD_ADDR", null, createAddress())
                ),
                createMeta(1)
            )
        }

        shouldThrow<IllegalArgumentException> {
            kakaoApiAddressResolver.resolve("경기도 안양시")
        } shouldHaveMessage "올바른 도로명 주소를 입력해주세요."
    }

    @Test
    fun `해당하는 주소(Address)가 존재하지 않는 경우, 예외를 발생한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument("ROAD_ADDR", createRoadAddress(), null)
                ),
                createMeta(1)
            )
        }

        shouldThrow<IllegalArgumentException> {
            kakaoApiAddressResolver.resolve("경기도 안양시")
        } shouldHaveMessage "올바른 도로명 주소를 입력해주세요."
    }

    @Test
    fun `HTTP 클라이언트에게 카카오 API 경로를 전달한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument("ROAD_ADDR", createRoadAddress(), createAddress())
                ),
                createMeta(1)
            )
        }

        kakaoApiAddressResolver.resolve("경기도 안양시 만안구")

        verify { kakaoApiWebClient.requestAddress(any(), "https://dapi.kakao.com") }
    }

    @Test
    fun `도로명 주소에 서브 빌딩 번호가 존재하지 않는다면 메인 빌딩 번호만 포함해야한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument(
                        "ROAD_ADDR",
                        createRoadAddress(
                            region1depthName = "경기",
                            region2depthName = "안양시 만안구",
                            region3depthName = "안양동",
                            roadName = "병목안로",
                            mainBuildingNo = "123"
                        ),
                        createAddress()
                    )
                ),
                createMeta(1)
            )
        }

        val shopAddress = kakaoApiAddressResolver.resolve("경기도 안양시 만안구 병목안로 123")

        shopAddress.address.region1DepthName shouldBe "경기"
        shopAddress.address.region2DepthName shouldBe "안양시 만안구"
        shopAddress.address.region3DepthName shouldBe "안양동"
        shopAddress.address.roadName shouldBe "병목안로 123"
    }

    @Test
    fun `도로명 주소에 서브 빌딩 번호가 존재한다면 하이픈을 구분자로 포함해야한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument(
                        "ROAD_ADDR",
                        createRoadAddress(
                            region1depthName = "경기",
                            region2depthName = "안양시 만안구",
                            region3depthName = "안양동",
                            roadName = "병목안로",
                            mainBuildingNo = "123",
                            subBuildingNo = "23"
                        ),
                        createAddress()
                    )
                ),
                createMeta(1)
            )
        }

        val shopAddress = kakaoApiAddressResolver.resolve("경기도 안양시 만안구 병목안로 123-23")

        shopAddress.address.region1DepthName shouldBe "경기"
        shopAddress.address.region2DepthName shouldBe "안양시 만안구"
        shopAddress.address.region3DepthName shouldBe "안양동"
        shopAddress.address.roadName shouldBe "병목안로 123-23"
    }

    @Test
    fun `유효하지 않는 좌표값을 전달받으면 예외를 발생한다`() {
        every { kakaoApiWebClient.requestAddress(any(), any()) } answers {
            KakaoApiAddressResponse(
                listOf(
                    createDocument(
                        "ROAD_ADDR",
                        createRoadAddress(
                            region1depthName = "경기",
                            region2depthName = "안양시 만안구",
                            region3depthName = "안양동",
                            roadName = "병목안로",
                            mainBuildingNo = "123",
                            subBuildingNo = "23"
                        ),
                        createAddress(), x = "", y = ""
                    )
                ),
                createMeta(1)
            )
        }

        shouldThrow<IllegalArgumentException> {
            kakaoApiAddressResolver.resolve("경기도 안양시 만안구 병목안로 129-27")
        } shouldHaveMessage "유효하지 않은 좌표 범위입니다."
    }

    fun createMeta(totalCount: Int) = Meta(
        false, 10, totalCount
    )

    fun createDocument(
        addrType: String,
        roadAddress: RoadAddress?,
        address: Address?,
        x: String = "123",
        y: String = "32"
    ) = Document(
        "경기도 안양시 만안구 병목안로 123-23", addrType, address, roadAddress, x, y
    )

    fun createAddress(hCode: String = "1234567890") = Address(
        "", "", hCode, "", "", "", "", "", "", "", "", ""
    )

    fun createRoadAddress(
        region1depthName: String = "",
        region2depthName: String = "",
        region3depthName: String = "",
        roadName: String = "",
        mainBuildingNo: String = "",
        subBuildingNo: String = "",
    ) = RoadAddress(
        "",
        "",
        mainBuildingNo,
        region1depthName,
        region2depthName,
        region3depthName,
        roadName,
        subBuildingNo,
        "",
        "",
        "",
        ""
    )
}
