package com.mealkitary.shop.domain.shop

import com.mealkitary.shop.domain.shop.address.Address
import com.mealkitary.shop.domain.shop.address.Coordinates
import com.mealkitary.shop.domain.shop.address.ShopAddress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class ShopAddressTest : AnnotationSpec() {

    @Test
    fun `올바른 값들을 입력했을 경우 객체를 생성할 수 있다`() {
        val cityCode = "1234567890"
        val coordinates = Coordinates.of(
            -150.653,
            46.492
        )
        val address = Address.of(
            "region1DepthName",
            "region2DepthName",
            "region3DepthName",
            "roadName"
        )

        val shopAddress = ShopAddress.of(
            cityCode,
            coordinates,
            address
        )

        shopAddress.cityCode shouldBe cityCode
        shopAddress.coordinates shouldBe coordinates
        shopAddress.address shouldBe address
    }

    @Test
    fun `지역 코드가 올바르지 않을 경우 예외를 발생한다`() {
        val cityCode = "25231491723109"
        val coordinates = Coordinates.of(
            -150.653,
            46.492
        )
        val address = Address.of(
            "region1DepthName",
            "region2DepthName",
            "region3DepthName",
            "roadName"
        )

        shouldThrow<IllegalArgumentException> {
            ShopAddress.of(
                cityCode,
                coordinates,
                address
            )
        } shouldHaveMessage "올바른 지역 코드가 아닙니다. (행정동 지역 코드는 10자리)"
    }
}
