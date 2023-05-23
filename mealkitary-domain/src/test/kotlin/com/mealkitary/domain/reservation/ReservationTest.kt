package com.mealkitary.domain.reservation

import com.mealkitary.common.ReservationTestData.Companion.defaultReservation
import com.mealkitary.common.ShopTestData.Companion.defaultShop
import com.mealkitary.domain.shop.ShopStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.spyk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private const val CHANGE_RESERVATION_STATUS_METHOD_NAME = "changeReservationStatus"

internal class ReservationTest : AnnotationSpec() {

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
                    LocalDate.now().plusDays(1), LocalTime.of(18, 30)
                )
            ).build()
            sut.reserve()
        } shouldHaveMessage "예약하시려는 가게는 해당 시간에 예약을 받지 않습니다."
    }

    @Test
    fun `예약 하려는 가게가 정상 영업 중이 아니라면 예외를 발생한다`() {
        val invalidShop = defaultShop().withStatus(ShopStatus.INVALID).build()
        shouldThrow<IllegalArgumentException> {
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
        val base = defaultReservation().withReserveAt(validTime())
        val paid = base.withReservationStatus(ReservationStatus.PAID).build()
        val reserved = base.withReservationStatus(ReservationStatus.RESERVED).build()
        val rejected = base.withReservationStatus(ReservationStatus.REJECTED).build()
        val sut = listOf(paid, reserved, rejected)

        sut.forAll {
            shouldThrow<IllegalStateException> {
                it.pay()
            } shouldHaveMessage "미결제인 상태에서만 결제 상태를 변경할 수 있습니다."
        }
    }

    private fun paidReservation() =
        defaultReservation()
            .withReservationStatus(ReservationStatus.PAID)
            .withReserveAt(validTime())
            .build()

    private fun notPaidReservation() =
        defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .withReserveAt(validTime())
            .build()

    private fun validTime(): LocalDateTime = LocalDateTime.of(
        LocalDate.now().plusDays(1),
        LocalTime.of(18, 0)
    )

    private fun shouldThrowWhenAcceptBeforeReservationTime(beforeTime: LocalDateTime) {
        shouldThrow<IllegalStateException> {
            defaultReservation().withReserveAt(beforeTime).build()
        } shouldHaveMessage "예약 시간은 적어도 미래 시점이어야 합니다."
    }
}
