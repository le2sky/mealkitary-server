package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.ShopResponse
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetShopServiceTest : AnnotationSpec() {

    private val loadShopPort = mockk<LoadShopPort>()
    private val getShopService = GetShopService(loadShopPort)

    @Test
    fun `service unit test - 모든 가게를 조회한다`() {
        every { loadShopPort.loadAllShop() } answers {
            listOf(
                Shop(
                    ShopTitle.from("집밥뚝딱"),
                    ShopStatus.VALID,
                    ShopBusinessNumber.from("123-45-67890"),
                    mutableListOf(),
                    mutableListOf()
                )
            )
        }
        val expected = ShopResponse(null, "집밥뚝딱")

        val actual = getShopService.loadAllShop()

        actual shouldBe listOf(expected)
    }
}
