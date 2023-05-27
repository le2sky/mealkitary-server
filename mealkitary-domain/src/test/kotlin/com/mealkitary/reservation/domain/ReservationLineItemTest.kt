package com.mealkitary.reservation.domain

import com.mealkitary.common.data.ProductTestData.Companion.defaultProduct
import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.ProductId
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue

class ReservationLineItemTest : AnnotationSpec() {

    @Test
    fun `매핑한 프로덕트와 동등성 검증`() {
        val product = defaultProduct().build()
        val reservationLineItem = ReservationLineItem(ProductId(1L), "부대찌개", Money.of(1000), 2)

        (product == reservationLineItem.mapToProduct()).shouldBeTrue()
        (product.equals(reservationLineItem.mapToProduct())).shouldBeTrue()
    }
}
