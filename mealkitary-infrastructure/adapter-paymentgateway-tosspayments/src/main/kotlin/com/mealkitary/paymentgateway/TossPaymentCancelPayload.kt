package com.mealkitary.paymentgateway

private const val CANCEL_REASON_LIMIT_LENGTH = 200

data class TossPaymentCancelPayload(
    val cancelReason: String
) {

    init {
        if (cancelReason.length > CANCEL_REASON_LIMIT_LENGTH) {
            throw IllegalArgumentException("취소 사유의 최대 길이는 ${CANCEL_REASON_LIMIT_LENGTH}자입니다.")
        }
    }
}
