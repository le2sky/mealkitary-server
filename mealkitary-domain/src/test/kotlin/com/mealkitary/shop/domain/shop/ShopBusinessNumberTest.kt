package com.mealkitary.shop.domain.shop

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class ShopBusinessNumberTest : AnnotationSpec() {

    @Test
    fun `사업자 번호는 빈 문자열이 될 수 없다`() {
        listOf(" ", "  ", "").forAll {
            shouldThrow<IllegalArgumentException> {
                ShopBusinessNumber.from(it)
            } shouldHaveMessage "올바른 사업자번호 형식이 아닙니다."
        }
    }

    @Test
    fun `사업자 번호의 형식이 아니면 예외를 발생한다`() {
        listOf("010-1234-5678 ", "inv-al-lidbn", " 123-23-12345 ").forAll {
            shouldThrow<IllegalArgumentException> {
                ShopBusinessNumber.from(it)
            } shouldHaveMessage "올바른 사업자번호 형식이 아닙니다."
        }
    }

    @Test
    fun `올바른 사업자번호 형식을 입력하는 경우에만 객체를 생성할 수 있다`() {
        val shopBusinessNumber = ShopBusinessNumber.from("132-32-32112")

        shopBusinessNumber.value shouldBe "132-32-32112"
    }
}
