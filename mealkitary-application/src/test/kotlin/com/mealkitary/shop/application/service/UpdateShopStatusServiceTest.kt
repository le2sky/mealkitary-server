package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.output.CheckExistenceShopPort
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UpdateShopStatusServiceTest : AnnotationSpec() {

    private val checkExistenceShopPort = mockk<CheckExistenceShopPort>()
    private val loadShopPort = mockk<LoadShopPort>()
    private val updateShopStatusService = UpdateShopStatusService(checkExistenceShopPort, loadShopPort)

    @Test
    fun `service unit test - 가게의 상태를 INVALID로 변경한다`() {
        val shopId = 1L
        val validShop = Shop(
            ShopTitle.from("제목"),
            ShopStatus.VALID,
            ShopBusinessNumber.from("123-12-12345"),
            mutableListOf(),
            mutableListOf()
        )

        every { loadShopPort.loadOneShopById(shopId) } returns validShop
        every { checkExistenceShopPort.hasReservations(shopId) } returns false

        updateShopStatusService.update(shopId)

        validShop.status shouldBe ShopStatus.INVALID
    }

    @Test
    fun `service unit test - 가게의 상태를 VALID로 변경한다`() {
        val shopId = 1L
        val validShop = Shop(
            ShopTitle.from("제목"),
            ShopStatus.INVALID,
            ShopBusinessNumber.from("123-12-12345"),
            mutableListOf(),
            mutableListOf()
        )

        every { loadShopPort.loadOneShopById(shopId) } returns validShop
        every { checkExistenceShopPort.hasReservations(shopId) } returns false

        updateShopStatusService.update(shopId)

        validShop.status shouldBe ShopStatus.VALID
    }

    @Test
    fun `service unit test - 가게의 상태를 INVALID로 변경할 때, 예약이 존재하면 예외가 발생한다`() {
        val shopId = 1L
        val validShop = Shop(
            ShopTitle.from("제목"),
            ShopStatus.VALID,
            ShopBusinessNumber.from("123-12-12345"),
            mutableListOf(),
            mutableListOf()
        )

        every { loadShopPort.loadOneShopById(shopId) } returns validShop
        every { checkExistenceShopPort.hasReservations(shopId) } returns true

        shouldThrow<IllegalStateException> {
            updateShopStatusService.update(shopId)
        }
    }
}
