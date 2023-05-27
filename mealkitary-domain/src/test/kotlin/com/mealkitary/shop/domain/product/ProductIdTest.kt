package com.mealkitary.shop.domain.product

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ProductIdTest : AnnotationSpec() {

    @Test
    fun `id 내부 값이 같다면 동등하다`() {
        val productId = ProductId(1L)
        val targetId = ProductId(1L)
        (productId == targetId).shouldBeTrue()
    }

    @Test
    fun `id 내부 값이 다르다면 동등하지 않다`() {
        val productId = ProductId(1L)
        val targetId = ProductId(2L)
        (productId == targetId).shouldBeFalse()
    }

    @Test
    fun `해시코드 검증`() {
        val productId = ProductId(1L)
        val targetId = ProductId(1L)
        val set = setOf(productId, targetId)
        set.size shouldBe 1
    }
}
