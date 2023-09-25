package com.mealkitary.shop.domain.shop

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class ShopTitleTest : AnnotationSpec() {

    @Test
    fun `가게 이름이 빈 문자열일 경우 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            ShopTitle.from("")
        } shouldHaveMessage "가게 이름을 입력해주세요."
    }

    @Test
    fun `가게 이름이 공백으로만 이루어져 있을 경우, 예외를 발생한다`() {
        val params = listOf(" ", "  ", "   ", "\n", "\t")

        params.forAll {
            shouldThrow<IllegalArgumentException> {
                ShopTitle.from(it)
            } shouldHaveMessage "가게 이름을 입력해주세요."
        }
    }

    @Test
    fun `가게의 이름은 최대 50글자이다`() {
        val title = "가".repeat(51)

        shouldThrow<IllegalArgumentException> {
            ShopTitle.from(title)
        } shouldHaveMessage "가게의 이름은 최대 50글자입니다."
    }

    @Test
    fun `가게의 이름이 올바른 형식이아니라면 예외를 발생한다`() {
        listOf(
            "!@#집밥뚝딱,",
            "   집밥 @ 뚝딱",
            "집밥뚝@#@!"
        ).forAll {
            shouldThrow<IllegalArgumentException> {
                ShopTitle.from(it)
            } shouldHaveMessage "올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)"
        }
    }

    @Test
    fun `가게 이름의 조건을 모두 만족하면 가게 이름 객체를 반환한다`() {
        val shopTitle = ShopTitle.from("집밥뚝딱 안양점")

        shopTitle.value shouldBe "집밥뚝딱 안양점"
    }
}
