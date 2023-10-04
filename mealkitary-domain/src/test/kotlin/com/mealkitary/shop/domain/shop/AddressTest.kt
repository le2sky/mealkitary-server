package com.mealkitary.shop.domain.shop

import com.mealkitary.common.model.Address
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class AddressTest : AnnotationSpec() {

    @Test
    fun `올바른 주소를 입력할 경우 객체를 생성할 수 있다`() {
        val region1DepthName = "서울"
        val region2DepthName = "강남구"
        val region3DepthName = "논현동"
        val roadName = "논현로"

        val address = Address.of(
            region1DepthName,
            region2DepthName,
            region3DepthName,
            roadName
        )

        address.region1DepthName shouldBe region1DepthName
        address.region2DepthName shouldBe region2DepthName
        address.region3DepthName shouldBe region3DepthName
        address.roadName shouldBe roadName
    }
}
