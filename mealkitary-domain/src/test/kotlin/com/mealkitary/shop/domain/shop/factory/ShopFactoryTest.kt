package com.mealkitary.shop.domain.shop.factory

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk

class ShopFactoryTest : AnnotationSpec() {

    private val shopBusinessNumberValidator = mockk<ShopBusinessNumberValidator>()
    private val shopFactory = ShopFactory(shopBusinessNumberValidator)

    @Test
    fun `사업자번호가 유효하지 않으면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            shopFactory.createOne("집밥뚝딱 안양점", "32-12-3221")
        } shouldHaveMessage "올바른 사업자번호 형식이 아닙니다."
    }

    @Test
    fun `실제로 유효한 사업자번호와 가게이름이라면 가게를 생성한다`() {
        every { shopBusinessNumberValidator.validate(any()) } answers { }

        val shop = shopFactory.createOne("집밥뚝딱 안양점", "321-23-12345")

        shop.title.value shouldBe "집밥뚝딱 안양점"
        shop.businessNumber.value shouldBe "321-23-12345"
    }

    @Test
    fun `가게 이름이 유효하지 않으면 예외를 발생한다`() {
        every { shopBusinessNumberValidator.validate(any()) } answers { }

        shouldThrow<IllegalArgumentException> {
            shopFactory.createOne("집밥뚝딱 ! 안양점", "321-23-12345")
        } shouldHaveMessage "올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)"
    }
}
