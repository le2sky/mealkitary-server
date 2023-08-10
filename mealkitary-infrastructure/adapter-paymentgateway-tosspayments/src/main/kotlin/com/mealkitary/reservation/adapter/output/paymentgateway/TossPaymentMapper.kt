package com.mealkitary.reservation.adapter.output.paymentgateway

import com.mealkitary.reservation.domain.payment.Payment
import org.springframework.stereotype.Component

private const val ORDER_ID_PREFIX = "mealkitary:toss:reservation:"

@Component
class TossPaymentMapper(
    private val codec: TossPaymentCodec
) {

    fun mapToTossPayment(payment: Payment): TossPayment {
        checkIdExist(payment)

        return TossPayment.of(
            payment.paymentKey,
            removePadding(codec.encode(appendPrefix(payment.reservation.id!!))),
            payment.amount.value
        )
    }

    private fun checkIdExist(payment: Payment) {
        if (payment.reservation.id == null) {
            throw IllegalArgumentException("예약 번호(reservationId)는 반드시 존재해야 합니다.")
        }
    }

    private fun appendPrefix(id: Long): String {
        return "$ORDER_ID_PREFIX$id"
    }

    private fun removePadding(encoded: String): String {
        return encoded.replace("=", "")
    }
}
