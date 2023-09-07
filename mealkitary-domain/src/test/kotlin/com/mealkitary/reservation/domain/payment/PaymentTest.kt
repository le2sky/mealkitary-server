package com.mealkitary.reservation.domain.payment

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
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
    fun `승인된 결제가 취소되면, 결제의 상태는 canceled로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"
        val payment = Payment.of(paymentKey, reservation, amount)
        payment.approve()

        payment.cancel()

        payment.paymentStatus shouldBe PaymentStatus.CANCELED
    }

    @Test
    fun `정상적으로 생성되지 않은 예약으로 결제를 생성할 수 없다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NONE)
            .build()
        val amount = Money.from(2000)
        val paymentKey = "abc123"

        shouldThrow<IllegalStateException> {
            Payment.of(paymentKey, reservation, amount)
        } shouldHaveMessage "미결제인 상태에서만 이용 가능한 기능입니다."
    }
}
