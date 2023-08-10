package com.mealkitary.reservation.adapter.output.paymentgateway

private const val MAX_PAYMENT_KEY_LENGTH = 200
private const val MIN_ORDER_ID_LENGTH = 6
private const val MAX_ORDER_ID_LENGTH = 64
private const val ORDER_ID_FORMAT = "^([0-9a-zA-Z\\-_]+)"

class TossPayment private constructor(
    val paymentKey: String,
    val orderId: String,
    val amount: Int
) {

    companion object {
        fun of(paymentKey: String, orderId: String, amount: Int): TossPayment {
            checkPaymentKeyLength(paymentKey)
            checkAmount(amount)
            checkOrderIdFormat(orderId)
            checkOrderIdLengthInRange(orderId)

            return TossPayment(paymentKey, orderId, amount)
        }

        private fun checkPaymentKeyLength(paymentKey: String) {
            if (paymentKey.length > MAX_PAYMENT_KEY_LENGTH) {
                throw IllegalArgumentException("결제 키 값(paymentKey)은 최대 200글자입니다.")
            }
        }

        private fun checkAmount(amount: Int) {
            if (amount <= 0) {
                throw IllegalArgumentException("결제 금액(amount)은 0보다 커야합니다.")
            }
        }

        private fun checkOrderIdFormat(orderId: String) {
            if (!orderId.matches(getOrderIdFormatRegex())) {
                throw IllegalArgumentException("주문 번호(orderId)는 영문 대소문자, 숫자, 특수문자 -, _로 이루어져야 합니다.")
            }
        }

        private fun getOrderIdFormatRegex() = Regex(ORDER_ID_FORMAT)

        private fun checkOrderIdLengthInRange(orderId: String) {
            if (orderId.length < MIN_ORDER_ID_LENGTH || orderId.length > MAX_ORDER_ID_LENGTH) {
                throw IllegalArgumentException("주문 번호(orderId)는 6자 이상 64자 이하의 문자열입니다.")
            }
        }
    }
}
