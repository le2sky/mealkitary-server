package com.mealkitary.reservation.domain

import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.product.ProductId

class ReservationLineItem(
    private val itemId: ProductId,
    private val name: String,
    private val price: Int,
    private val count: Int
) {
    fun mapToProduct(): Product {
        return Product(
            this.itemId,
            this.name,
            this.price
        )
    }
}
