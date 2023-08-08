package com.mealkitary.reservation.domain.reservation

import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.AT_LEAST_ONE_ITEM_COUNT_REQUIRED
import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.product.ProductId
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class ReservationLineItem private constructor(
    itemId: ProductId,
    name: String,
    price: Money,
    count: Int
) {

    @Embedded
    private val itemId: ProductId = itemId

    @Column(nullable = false)
    private val name: String = name

    @Embedded
    private val price: Money = price

    @Column(nullable = false)
    private val count: Int = count

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