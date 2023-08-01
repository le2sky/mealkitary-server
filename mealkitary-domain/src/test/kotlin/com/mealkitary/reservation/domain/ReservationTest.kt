package com.mealkitary.reservation.domain

import com.mealkitary.common.data.ReservationTestData.Companion.defaultReservation
import com.mealkitary.common.data.ShopTestData.Companion.defaultShop
import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.ProductId
import com.mealkitary.shop.domain.shop.ShopStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.spyk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private const val CHANGE_RESERVATION_STATUS_METHOD_NAME = "changeReservationStatus"

class ReservationTest : AnnotationSpec() {

    @Test
    fun `예약 상품이 하나도 존재하지 않는다면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            defaultReservation().withLineItems().build()
        } shouldHaveMessage "예약 상품은 적어도 한 개 이상이어야 합니다."
    }

    @Test
    fun `예약 시간이 현재 시간보다 과거라면 예외를 발생한다`() {
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusDays(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusHours(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusYears(1))
    }

    @Test
    fun `예약 하려는 가게가 제공하는 예약 시간이 아니라면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            val sut = defaultReservation().withReserveAt(
                LocalDateTime.of(
                    LocalDate.now().plusDays(1),
                    LocalTime.of(18, 30)
                )
            ).build()
            sut.reserve()
        } shouldHaveMessage "예약 대상 가게는 해당 시간에 예약을 받지 않습니다."
    }

    @Test
    fun `예약 하려는 가게가 정상 영업 중이 아니라면 예외를 발생한다`() {
        val invalidShop = defaultShop().withStatus(ShopStatus.INVALID).build()

        shouldThrow<IllegalStateException> {
            val sut = defaultReservation().withShop(invalidShop).build()
            sut.reserve()
        } shouldHaveMessage "유효하지 않은 가게입니다."
    }

    @Test
    fun `결제 상태인 예약은 점주가 승인할 수 있다`() {
        val sut = spyk(
            objToCopy = paidReservation(),
            recordPrivateCalls = true
        )

        sut.accept()

        verify { sut[CHANGE_RESERVATION_STATUS_METHOD_NAME](ReservationStatus.RESERVED) }
    }

    @Test
    fun `미결제 상태인 예약을 승인 시도할 경우 예외를 발생한다`() {
        val sut = notPaidReservation()

        shouldThrow<IllegalStateException> {
            sut.accept()
        } shouldHaveMessage "미결제 상태인 예약은 승인할 수 없습니다."
    }

    @Test
    fun `결제 상태인 예약은 점주가 거부할 수 있다`() {
        val sut = spyk(
            objToCopy = paidReservation(),
            recordPrivateCalls = true
        )

        sut.reject()

        verify { sut[CHANGE_RESERVATION_STATUS_METHOD_NAME](ReservationStatus.REJECTED) }
    }

    @Test
    fun `미결제 상태인 예약을 거부 시도할 경우 예외를 발생한다`() {
        val sut = notPaidReservation()

        shouldThrow<IllegalStateException> {
            sut.reject()
        } shouldHaveMessage "미결제 상태인 예약은 거부할 수 없습니다."
    }

    @Test
    fun `예약 확정인 상태에서 점주가 예약을 거부할 경우 예외를 발생한다 `() {
        val sut = paidReservation()
        sut.accept()

        shouldThrow<IllegalStateException> {
            sut.reject()
        } shouldHaveMessage "이미 예약 확정된 건에 대해서 거부할 수 없습니다."
    }

    @Test
    fun `예약 거부 상태에서 점주가 예약을 승인할 경우 예외를 발생한다`() {
        val sut = paidReservation()
        sut.reject()

        shouldThrow<IllegalStateException> {
            sut.accept()
        } shouldHaveMessage "이미 예약 거부된 건에 대해서 승인할 수 없습니다."
    }

    @Test
    fun `사용자가 결제를 하면 예약의 상태를 결제됨으로 변경한다`() {
        val sut = spyk(
            objToCopy = notPaidReservation(),
            recordPrivateCalls = true
        )

        sut.pay()

        verify { sut[CHANGE_RESERVATION_STATUS_METHOD_NAME](ReservationStatus.PAID) }
    }

    @Test
    fun `미결제 상태가 아닌 다른 상태에서 결제를 시도하면 예외를 발생한다`() {
        val base = defaultReservation()
        val none = base.withReservationStatus(ReservationStatus.NONE).build()
        val paid = base.withReservationStatus(ReservationStatus.PAID).build()
        val reserved = base.withReservationStatus(ReservationStatus.RESERVED).build()
        val rejected = base.withReservationStatus(ReservationStatus.REJECTED).build()
        val sut = listOf(none, paid, reserved, rejected)

        sut.forAll {
            shouldThrow<IllegalStateException> {
                it.pay()
            } shouldHaveMessage "미결제인 상태에서만 이용 가능한 기능입니다."
        }
    }

    @Test
    fun `유효하지 않은 상품이 하나라도 존재한다면 예외를 발생한다`() {
        val sut = defaultReservation()
            .withLineItems(
                ReservationLineItem.of(ProductId(1L), "부대찌개", Money.from(1000), 2),
                ReservationLineItem.of(ProductId(2L), "닯보끔탕", Money.from(2000), 2)
            ).build()

        shouldThrow<IllegalArgumentException> {
            sut.reserve()
        } shouldHaveMessage "존재하지 않는 상품입니다."
    }

    @Test
    fun `모든 상품과 가게, 시간이 유효하다면 예약이 가능하다`() {
        val sut = defaultReservation().build()
        sut.reserve()
    }

    @Test
    fun `정상적으로 생성되지 않은 예약은 예약 거부할 수 없다`() {
        val sut = defaultReservation().build()

        shouldThrow<IllegalStateException> {
            sut.reject()
        } shouldHaveMessage "정상적으로 생성된 예약에 대해서만 수행 가능합니다."
    }

    @Test
    fun `정상적으로 생성되지 않은 예약은 수락할 수 없다`() {
        val sut = defaultReservation().build()

        shouldThrow<IllegalStateException> {
            sut.accept()
        } shouldHaveMessage "정상적으로 생성된 예약에 대해서만 수행 가능합니다."
    }

    @Test
    fun `이미 처리하고 있는 예약은 다시 예약 요청할 수 없다`() {
        val sut = paidReservation()

        shouldThrow<IllegalStateException> {
            sut.reserve()
        } shouldHaveMessage "이미 처리하고 있는 예약입니다."
    }

    @Test
    fun `예약 생성 요청을 해야 예약의 상태가 미결제로 변경된다`() {
        val sut = spyk(
            objToCopy = defaultReservation().build(),
            recordPrivateCalls = true
        )

        sut.reserve()

        verify { sut[CHANGE_RESERVATION_STATUS_METHOD_NAME](ReservationStatus.NOTPAID) }
    }

    @Test
    fun `미결제 상태인 상태가 아닌 경우 가격 계산을 할 수 없다`() {
        val base = defaultReservation()
        val none = base.withReservationStatus(ReservationStatus.NONE).build()
        val paid = base.withReservationStatus(ReservationStatus.PAID).build()
        val reserved = base.withReservationStatus(ReservationStatus.RESERVED).build()
        val rejected = base.withReservationStatus(ReservationStatus.REJECTED).build()
        val sut = listOf(none, paid, reserved, rejected)

        sut.forAll {
            shouldThrow<IllegalStateException> {
                it.calculateTotalPrice()
            } shouldHaveMessage "미결제인 상태에서만 이용 가능한 기능입니다."
        }
    }

    @Test
    fun `총 상품의 가격을 계산한다`() {
        val totalPrice = defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .withLineItems(
                ReservationLineItem.of(ProductId(1L), "a", Money.from(1000), 10),
                ReservationLineItem.of(ProductId(2L), "b", Money.from(9000), 2),
                ReservationLineItem.of(ProductId(3L), "c", Money.from(3000), 3)
            ).build()
            .calculateTotalPrice()

        totalPrice shouldBe Money.from(37000)
    }

    private fun paidReservation() =
        defaultReservation()
            .withReservationStatus(ReservationStatus.PAID)
            .build()

    private fun notPaidReservation() =
        defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()

    private fun shouldThrowWhenAcceptBeforeReservationTime(beforeTime: LocalDateTime) {
        shouldThrow<IllegalStateException> {
            defaultReservation().withReserveAt(beforeTime).build()
        } shouldHaveMessage "예약 시간은 적어도 미래 시점이어야 합니다."
    }
}
