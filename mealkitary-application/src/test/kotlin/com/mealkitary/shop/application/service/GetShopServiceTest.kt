package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.ShopResponse
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopStatus
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
            listOf(Shop("집밥뚝딱", ShopStatus.VALID, mutableListOf(), mutableListOf()))
        }
        val actual = getShopService.loadAllShop()
        val expected = ShopResponse(null, "집밥뚝딱")
        actual shouldBe listOf(expected)
    }
}
