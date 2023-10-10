package com.mealkitary.reservation.application.service

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.application.port.input.ReserveProductRequest
import com.mealkitary.reservation.application.port.input.ReservedProduct
import com.mealkitary.reservation.application.port.output.SaveReservationPort
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.ShopStatus
import data.ProductTestData
import data.ShopTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class ReserveProductServiceTest : AnnotationSpec() {

    private val loadShopPort = mockk<LoadShopPort>()
    private val saveReservationPort = mockk<SaveReservationPort>()
    private val reserveProductService = ReserveProductService(loadShopPort, saveReservationPort)

    @Test
    fun `service unit test - 신규 예약을 생성한다`() {
        givenShop()
        val id = UUID.randomUUID()
        every { saveReservationPort.saveOne(any()) } answers { id }
        val reserveProductRequest = createReserveProductRequest()

        val result = reserveProductService.reserve(reserveProductRequest)

        result shouldBe id
    }

    @Test
    fun `service unit test - 예약 상품이 존재하지 않다면 예외를 발생한다`() {
        givenShop()
        val reserveProductRequest = createReserveProductRequest(reservedProducts = listOf())

        shouldThrow<IllegalArgumentException> {
            reserveProductService.reserve(reserveProductRequest)
        } shouldHaveMessage "예약 상품은 적어도 한 개 이상이어야 합니다."
    }

    @Test
    fun `service unit test - 예약 하려는 가게가 제공하는 예약 시간이 아니라면 예외를 발생한다`() {
        givenShop()
        val reserveProductRequest = createReserveProductRequest(
            reservedAt = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(15, 0)
            )
        )

        shouldThrow<IllegalArgumentException> {
            reserveProductService.reserve(reserveProductRequest)
        } shouldHaveMessage "예약 대상 가게는 해당 시간에 예약을 받지 않습니다."
    }

    @Test
    fun `service unit test - 예약 시간이 현재 시간보다 과거라면 예외를 발생한다`() {
        givenShop()
        val reserveProductRequest = createReserveProductRequest(
            reservedAt = LocalDateTime.of(
                LocalDate.now().minusDays(1),
                LocalTime.of(15, 0)
            )
        )

        shouldThrow<IllegalStateException> {
            reserveProductService.reserve(reserveProductRequest)
        } shouldHaveMessage "예약 시간은 적어도 미래 시점이어야 합니다."
    }

    @Test
    fun `service unit test - 검증하려는 상품이 존재하지 않는다면 예외를 발생한다`() {
        givenShop()
        val reserveProductRequest = createReserveProductRequest(
            reservedProducts = listOf(
                ReservedProduct(
                    2L,
                    "존재하지 않는 상품",
                    3000,
                    3
                )
            )
        )

        shouldThrow<IllegalArgumentException> {
            reserveProductService.reserve(reserveProductRequest)
        } shouldHaveMessage "존재하지 않는 상품입니다."
    }

    @Test
    fun `service unit test - 유효하지 않은 가게라면 예외를 발생한다`() {
        givenInvalidShop()
        val reserveProductRequest = createReserveProductRequest()

        shouldThrow<IllegalStateException> {
            reserveProductService.reserve(reserveProductRequest)
        } shouldHaveMessage "유효하지 않은 가게입니다."
    }

    private fun givenInvalidShop() {
        every { loadShopPort.loadOneShopById(any()) } answers {
            ShopTestData.defaultShop()
                .withStatus(ShopStatus.INVALID)
                .build()
        }
    }

    private fun givenShop() {
        every { loadShopPort.loadOneShopById(any()) } answers {
            ShopTestData.defaultShop()
                .withReservableTimes(
                    LocalTime.of(16, 0)
                )
                .withProducts(
                    ProductTestData.defaultProduct()
                        .withId(2L)
                        .withName("부대찌개")
                        .withPrice(Money.from(3000))
                        .build()
                )
                .build()
        }
    }

    private fun createReserveProductRequest(
        reservedProducts: List<ReservedProduct> = listOf(
            ReservedProduct(
                2L,
                "부대찌개",
                3000,
                3
            )
        ),
        reservedAt: LocalDateTime = LocalDateTime.of(
            LocalDate.now().plusDays(1),
            LocalTime.of(16, 0)
        )
    ) = ReserveProductRequest(
        UUID.randomUUID(),
        reservedProducts,
        reservedAt
    )
}
