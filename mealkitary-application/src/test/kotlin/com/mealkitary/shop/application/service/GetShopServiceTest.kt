package com.mealkitary.shop.application.service

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.application.port.input.ShopResponse
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopAddress
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
        val shop = Shop(
            ShopTitle.from("집밥뚝딱"),
            ShopStatus.VALID,
            ShopBusinessNumber.from("123-45-67890"),
            ShopAddress.of(
                "1234567890",
                Coordinates.of(
                    126.99599512792346,
                    35.976749396987046
                ),
                Address.of(
                    "region1DepthName",
                    "region2DepthName",
                    "region3DepthName",
                    "roadName"
                )
            ),
            mutableListOf(),
            mutableListOf()
        )

        every { loadShopPort.loadAllShop() } answers {
            listOf(
                shop
            )
        }
        val expected = ShopResponse(shop.id, "집밥뚝딱")

        val actual = getShopService.loadAllShop()

        actual shouldBe listOf(expected)
    }
}
