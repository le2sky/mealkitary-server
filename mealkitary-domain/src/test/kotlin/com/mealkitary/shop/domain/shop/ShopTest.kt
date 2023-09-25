package com.mealkitary.shop.domain.shop

import data.ProductTestData.Companion.defaultProduct
import data.ShopTestData.Companion.defaultShop
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.throwable.shouldHaveMessage

class ShopTest : AnnotationSpec() {

    @Test
    fun `유효하지 않은 가게라면 예외를 발생한다`() {
        val sut = defaultShop().withStatus(ShopStatus.INVALID)
            .build()

        shouldThrow<IllegalStateException> {
            sut.checkReservableShop()
        } shouldHaveMessage "유효하지 않은 가게입니다."
    }

    @Test
    fun `유효한 가게라면 검사에 통과한다`() {
        val sut = defaultShop().withStatus(ShopStatus.VALID)
            .build()

        sut.checkReservableShop()
    }

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
