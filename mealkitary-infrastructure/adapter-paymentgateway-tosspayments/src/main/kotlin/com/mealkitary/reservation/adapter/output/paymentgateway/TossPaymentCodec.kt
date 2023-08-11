package com.mealkitary.reservation.adapter.output.paymentgateway

interface TossPaymentCodec {

    fun encode(input: String): String

    fun decode(input: String): String
}
