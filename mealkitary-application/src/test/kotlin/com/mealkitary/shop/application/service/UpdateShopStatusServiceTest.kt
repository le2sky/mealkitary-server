package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.output.ShopPersistencePort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UpdateShopStatusServiceTest : AnnotationSpec() {

    private val shopPersistencePort = mockk<ShopPersistencePort>()
    private val updateShopStatusService = UpdateShopStatusService(shopPersistencePort)

    @Test
    fun `service unit test - 가게의 상태를 INVALID로 변경한다`() {
        val shopId = 1L
        val validShop = Shop(
            "제목",
            ShopStatus.VALID,
            mutableListOf(),
            mutableListOf()
        )

        every { shopPersistencePort.loadOneShopById(shopId) } returns validShop
        every { shopPersistencePort.hasReservations(shopId) } returns false

        updateShopStatusService.update(shopId)

        validShop.status shouldBe ShopStatus.INVALID
    }

    @Test
    fun `service unit test - 가게의 상태를 VALID로 변경한다`() {
        val shopId = 1L
        val validShop = Shop(
            "제목",
            ShopStatus.INVALID,
            mutableListOf(),
            mutableListOf()
        )

        every { shopPersistencePort.loadOneShopById(shopId) } returns validShop
        every { shopPersistencePort.hasReservations(shopId) } returns false

        updateShopStatusService.update(shopId)

        validShop.status shouldBe ShopStatus.VALID
    }

    @Test
    fun `service unit test - 가게의 상태를 INVALID로 변경할 때, 예약이 존재하면 예외가 발생한다`() {
        val shopId = 1L
        val validShop = Shop(
            "제목",
            ShopStatus.VALID,
            mutableListOf(),
            mutableListOf()
        )

        every { shopPersistencePort.loadOneShopById(shopId) } returns validShop
        every { shopPersistencePort.hasReservations(shopId) } returns true

        shouldThrow<IllegalStateException> {
            updateShopStatusService.update(shopId)
        }
    }
}
