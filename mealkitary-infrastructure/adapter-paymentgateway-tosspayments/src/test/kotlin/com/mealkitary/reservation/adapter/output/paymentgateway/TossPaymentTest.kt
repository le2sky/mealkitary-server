package com.mealkitary.reservation.adapter.output.paymentgateway

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.throwable.shouldHaveMessage

class TossPaymentTest : AnnotationSpec() {

    @Test
    fun `결제의 키 값(paymentKey)의 최대 길이는 200자이다`() {
        val paymentKey = "*".repeat(201)

        shouldThrow<IllegalArgumentException> {
            TossPayment.of(
                paymentKey,
                "123456",
                20000
            )
        } shouldHaveMessage "결제 키 값(paymentKey)은 최대 200글자입니다."
    }

    @Test
    fun `결제 대상의 주문 번호(orderId)는 6자 이상 64자 이하의 문자열이다`() {
        shouldThrow<IllegalArgumentException> {
            TossPayment.of(
                "paymentKey",
                "1".repeat(5),
                20000
            )
        } shouldHaveMessage "주문 번호(orderId)는 6자 이상 64자 이하의 문자열입니다."

        shouldThrow<IllegalArgumentException> {
            TossPayment.of(
                "paymentKey",
                "1".repeat(65),
                20000
            )
        } shouldHaveMessage "주문 번호(orderId)는 6자 이상 64자 이하의 문자열입니다."
    }

    @Test
    fun `결제 대상의 주문 번호(orderId)는 영문 대소문자, 숫자, 특수문자 -, _로 이루어져 있다`() {
        val source = listOf(
            "1q2w3e4r!Q@R$$",
            " ",
            "  ",
            "\n",
            "\t",
            "rt1()!@#$%^(*&^%$"
        )

        source.forAll {
            shouldThrow<IllegalArgumentException> {
                TossPayment.of(
                    "paymentKey",
                    it,
                    20000
                )
            } shouldHaveMessage "주문 번호(orderId)는 영문 대소문자, 숫자, 특수문자 -, _로 이루어져야 합니다."
        }
    }

    @Test
    fun `결제 금액(amount)은 0보다 커야한다`() {
        shouldThrow<IllegalArgumentException> {
            TossPayment.of(
                "paymentKey",
                "111111",
                0
            )
        } shouldHaveMessage "결제 금액(amount)은 0보다 커야합니다."
    }
}
