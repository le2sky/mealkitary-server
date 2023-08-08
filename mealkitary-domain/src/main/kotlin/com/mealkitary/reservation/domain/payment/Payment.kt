package com.mealkitary.reservation.domain.payment

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.Reservation
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class Payment private constructor(
    paymentKey: String,
    reservation: Reservation,
    amount: Money
) {

    @Id
    @Column(name = "payment_id")
    val paymentKey: String = paymentKey

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    val reservation: Reservation = reservation

    @Embedded
    val amount: Money = amount

    companion object {
        fun of(paymentKey: String, reservation: Reservation, amount: Money): Payment {
            checkAmount(reservation, amount)

            return Payment(paymentKey, reservation, amount)
        }

        private fun checkAmount(
            reservation: Reservation,
            amount: Money
        ) {
            if (amount.lessThanEqual(Money.from(0))) {
                throw IllegalArgumentException("결제 금액은 최소 0원 이상이어야 합니다.")
            }

            if (amount != reservation.calculateTotalPrice()) {
                throw IllegalArgumentException("예약 금액과 결제 금액이 일치하지 않습니다.")
            }
        }
    }
}
