package com.mealkitary.reservation.domain.payment

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class PaymentTest : AnnotationSpec() {

    @Test
    fun `주어진 결제 금액과 예약 결제 금액이 동일해야 한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(20000)
        val paymentKey = "abc123"

        shouldThrow<IllegalArgumentException> {
            Payment.of(paymentKey, reservation, amount)
        } shouldHaveMessage "예약 금액과 결제 금액이 일치하지 않습니다."
    }

    @Test
    fun `결제 금액은 0원 이상이어야 한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(0)
        val paymentKey = "abc123"

        shouldThrow<IllegalArgumentException> {
            Payment.of(paymentKey, reservation, amount)
        } shouldHaveMessage "결제 금액은 최소 0원 이상이어야 합니다."
    }

    @Test
    fun `결제가 승인되면, 예약의 상태는 paid로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"
        val payment = Payment.of(paymentKey, reservation, amount)

        payment.approve()

        reservation.reservationStatus shouldBe ReservationStatus.PAID
    }

    @Test
    fun `결제가 승인되면, 결제의 상태는 approved로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"
        val payment = Payment.of(paymentKey, reservation, amount)

        payment.approve()

        payment.paymentStatus shouldBe PaymentStatus.APPROVED
    }

    @Test
    fun `이미 승인된 결제는 다시 승인될 수 없다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"
        val payment = Payment.of(paymentKey, reservation, amount)
        payment.approve()

        shouldThrow<IllegalStateException> {
            payment.approve()
        } shouldHaveMessage "이미 승인된 결제는 다시 승인될 수 없습니다."
    }

    @Test
    fun `결제가 이미 승인되어 있는지 조회한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"
        val payment = Payment.of(paymentKey, reservation, amount)

        payment.isApproved().shouldBeFalse()
        payment.approve()
        payment.isApproved().shouldBeTrue()
    }
}
