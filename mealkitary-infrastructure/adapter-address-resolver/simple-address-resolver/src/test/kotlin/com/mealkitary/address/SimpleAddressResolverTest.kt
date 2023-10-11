package com.mealkitary.address

import com.mealkitary.SimpleAddressResolver
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class SimpleAddressResolverTest : AnnotationSpec() {

    @Test
    fun `adapter unit test - 문자열 주소값을 받아 주소 객체를 생성한다`() {
        val address = "서울특별시 강남구 역삼동 논현로"
        val resolver = SimpleAddressResolver()

        val shopAddress = resolver.resolve(address)

        shopAddress.cityCode shouldBe "1234567890"
        shopAddress.coordinates.longitude shouldBe 127.0
        shopAddress.coordinates.latitude shouldBe 40.0
        shopAddress.address.region1DepthName shouldBe "서울특별시"
        shopAddress.address.region2DepthName shouldBe "강남구"
        shopAddress.address.region3DepthName shouldBe "역삼동"
        shopAddress.address.roadName shouldBe "논현로"
    }

    @Test
    fun `adapter unit test - 문자열 주소값이 3등분일 경우 3개의 정보를 가진 주소 객체를 생성한다`() {
        val address = "경기도 남양주시 다산동"
        val resolver = SimpleAddressResolver()

        val shopAddress = resolver.resolve(address)

        shopAddress.cityCode shouldBe "1234567890"
        shopAddress.coordinates.longitude shouldBe 127.0
        shopAddress.coordinates.latitude shouldBe 40.0
        shopAddress.address.region1DepthName shouldBe "경기도"
        shopAddress.address.region2DepthName shouldBe "남양주시"
        shopAddress.address.region3DepthName shouldBe "다산동"
        shopAddress.address.roadName shouldBe ""
    }

    @Test
    fun `adapter unit test - 문자열 주소값이 2등분일 경우 2개의 정보를 가진 주소 객체를 생성한다`() {
        val address = "제주특별자치도 한림읍"
        val resolver = SimpleAddressResolver()

        val shopAddress = resolver.resolve(address)

        shopAddress.cityCode shouldBe "1234567890"
        shopAddress.coordinates.longitude shouldBe 127.0
        shopAddress.coordinates.latitude shouldBe 40.0
        shopAddress.address.region1DepthName shouldBe "제주특별자치도"
        shopAddress.address.region2DepthName shouldBe "한림읍"
        shopAddress.address.region3DepthName shouldBe ""
        shopAddress.address.roadName shouldBe ""
    }

    @Test
    fun `adapter unit test - 문자열 주소값이 2등분 이하일 경우 예외를 발생한다`() {
        val address = "제주특별자치도"
        val resolver = SimpleAddressResolver()

        shouldThrow<IllegalArgumentException> {
            resolver.resolve(address)
        } shouldHaveMessage "주소 형식이 올바르지 않습니다."
    }
}
