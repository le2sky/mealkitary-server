package com.mealkitary.addess

import com.mealkitary.address.KakaoApiAddressResolver
import com.mealkitary.address.KakaoApiWebClient
import com.mealkitary.address.payload.KakaoApiAddressResponse
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class KakaoApiAddressResolverTest : AnnotationSpec() {

    private val kakaoApiWebClient = mockk<KakaoApiWebClient>()
    private val kakaoApiAddressResolver = KakaoApiAddressResolver(kakaoApiWebClient)

    @Test
    fun `Kakao API를 통해 해당하는 주소 정보를 받아온다`() {
        val address = "경기도 남양주시 다산중앙로82번안길 132-12"
        val response = KakaoApiAddressResponse(
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

        every { kakaoApiWebClient.requestAddress(address) } returns response

        val shopAddress = kakaoApiAddressResolver.resolve(address)

        shopAddress.address.region1DepthName shouldBe "경기"
        shopAddress.address.region2DepthName shouldBe "남양주시"
        shopAddress.address.region3DepthName shouldBe "다산동"
        shopAddress.address.roadName shouldBe "다산중앙로82번안길"
        shopAddress.cityCode shouldBe "4136011200"
        shopAddress.coordinates.longitude shouldBe 127.166069448936
        shopAddress.coordinates.latitude shouldBe 37.6120947950094
    }
}
