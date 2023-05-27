package com.mealkitary.shop.domain.shop

import com.mealkitary.common.ProductTestData.Companion.defaultProduct
import com.mealkitary.common.ShopTestData.Companion.defaultShop
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
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

    private fun productWithIdAndName(id: Long, name: String) = defaultProduct()
        .withId(id)
        .withName(name)
        .build()
}
