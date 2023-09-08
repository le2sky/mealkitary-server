package com.mealkitary.paymentgateway

interface TossPaymentCodec {

    fun encode(input: String): String

    fun decode(input: String): String
}
