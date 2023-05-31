package com.mealkitary.reservation.domain

import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.AT_LEAST_ONE_ITEM_COUNT_REQUIRED
import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.product.ProductId

class ReservationLineItem private constructor(
    private val itemId: ProductId,
    private val name: String,
    private val price: Money,
    private val count: Int
) {

    fun mapToProduct(): Product {
        return Product(
            this.itemId,
            this.name,
            this.price
        )
    }

    fun calculateEachItemTotalPrice() = price * count

    companion object {
        fun of(itemId: ProductId, name: String, price: Money, count: Int): ReservationLineItem {
            checkCount(count)
            return ReservationLineItem(itemId, name, price, count)
        }

        private fun checkCount(count: Int) {
            if (count < 1) {
                throw IllegalArgumentException(AT_LEAST_ONE_ITEM_COUNT_REQUIRED.message)
            }
        }
    }
}
