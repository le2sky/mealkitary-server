package com.mealkitary.reservation.domain.payment

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
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
}
