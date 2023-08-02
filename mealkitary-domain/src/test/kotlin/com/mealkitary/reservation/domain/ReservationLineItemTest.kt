package com.mealkitary.reservation.domain

import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.ProductId
import data.ProductTestData.Companion.defaultProduct
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class ReservationLineItemTest : AnnotationSpec() {

    @Test
    fun `개별 아이템의 수량은 1개 이상이 아니라면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            ReservationLineItem.of(ProductId(1L), "부대찌개", Money.from(1000), 0)
        } shouldHaveMessage "개별 항목의 수량은 적어도 한 개 이상이어야 합니다."
    }

    @Test
    fun `매핑한 프로덕트와 동등성 검증`() {
        val product = defaultProduct().build()
        val item = ReservationLineItem.of(ProductId(1L), "부대찌개", Money.from(1000), 2)

        (product == item.mapToProduct()).shouldBeTrue()
        (product.equals(item.mapToProduct())).shouldBeTrue()
    }

    @Test
    fun `개별 아이템의 가격은 수량과 단가의 곱이다`() {
        val item = ReservationLineItem.of(ProductId(1L), "부대찌개", Money.from(10000), 3)

        item.calculateEachItemTotalPrice() shouldBe Money.from(30000)
    }
}
