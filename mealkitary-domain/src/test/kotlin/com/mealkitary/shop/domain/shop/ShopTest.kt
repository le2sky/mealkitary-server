package com.mealkitary.shop.domain.shop

import com.mealkitary.common.data.ProductTestData.Companion.defaultProduct
import com.mealkitary.common.data.ShopTestData.Companion.defaultShop
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.throwable.shouldHaveMessage

internal class ShopTest : AnnotationSpec() {

    @Test
    fun `검증하려는 상품이 존재하지 않는다면 예외를 발생한다`() {
        val product = productWithIdAndName(2L, "테스트 밀키트")
        val sut = defaultShop()
            .withProducts(productWithIdAndName(1L, "테스트 밀키트"))
            .build()

        shouldThrow<IllegalArgumentException> {
            sut.checkItem(product)
        } shouldHaveMessage "존재하지 않는 상품입니다."
    }

    @Test
    fun `검증하려는 상품의 이름이 일치하지 않는다면 예외를 발생한다`() {
        val product = productWithIdAndName(1L, "테스트 밀킷")
        val sut = defaultShop()
            .withProducts(productWithIdAndName(1L, "테스트 밀키트"))
            .build()

        shouldThrow<IllegalArgumentException> {
            sut.checkItem(product)
        } shouldHaveMessage "존재하지 않는 상품입니다."
    }

    @Test
    fun `검증하려는 상품이 존재하는 경우 유효성 검사를 통과한다`() {
        val product = defaultProduct().build()
        val sut = defaultShop()
            .withProducts(defaultProduct().build())
            .build()

        sut.checkItem(product)
    }

    @Test
    fun `가게 이름이 빈 문자열일 경우 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            defaultShop().withTitle("").build()
        } shouldHaveMessage "가게 이름을 입력해주세요."
    }

    @Test
    fun `가게 이름이 공백으로만 이루어져 있을 경우, 예외를 발생한다`() {
        val params = listOf(" ", "  ", "   ", "\n", "\t")
        params.forAll {
            shouldThrow<IllegalArgumentException> {
                defaultShop().withTitle(it).build()
            } shouldHaveMessage "가게 이름을 입력해주세요."
        }
    }

    private fun productWithIdAndName(id: Long, name: String) = defaultProduct()
        .withId(id)
        .withName(name)
        .build()
}
