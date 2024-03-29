package com.mealkitary.shop.domain.product

import com.mealkitary.common.model.Money
import data.ProductTestData.Companion.defaultProduct
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ProductTest : AnnotationSpec() {

    @Test
    fun `id, 이름, 가격이 같다면 동등하다`() {
        val product = defaultProduct().build()
        val targetProduct = defaultProduct().build()

        (product == targetProduct).shouldBeTrue()
        (product.equals(targetProduct)).shouldBeTrue()
    }

    @Test
    fun `id가 다르다면 동등하지 않다`() {
        val product = defaultProduct().withId(1L).build()
        val targetProduct = defaultProduct().withId(2L).build()

        (product == targetProduct).shouldBeFalse()
        (product.equals(targetProduct)).shouldBeFalse()
    }

    @Test
    fun `이름이 다르다면 동등하지 않다`() {
        val product = defaultProduct().withName("target").build()
        val targetProduct = defaultProduct().withName("target!").build()

        (product == targetProduct).shouldBeFalse()
        (product.equals(targetProduct)).shouldBeFalse()
    }

    @Test
    fun `가격이 다르다면 동등하지 않다`() {
        val product = defaultProduct().withPrice(Money.from(1000)).build()
        val targetProduct = defaultProduct().withPrice(Money.from(2000)).build()

        (product == targetProduct).shouldBeFalse()
        (product.equals(targetProduct)).shouldBeFalse()
    }

    @Test
    fun `해시코드 검증`() {
        val product = defaultProduct().build()
        val targetProduct = defaultProduct().build()
        val set = setOf(product, targetProduct)

        set.size shouldBe 1
    }
}
