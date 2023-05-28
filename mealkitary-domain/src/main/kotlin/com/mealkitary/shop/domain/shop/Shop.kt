package com.mealkitary.shop.domain.shop

import com.mealkitary.shop.domain.product.Product
import java.time.LocalDateTime
import java.time.LocalTime

class Shop(
    private val id: Long,
    private val title: String,
    private val status: ShopStatus,
    private val reservableTimes: List<LocalTime>,
    private val products: List<Product>
) {
    fun checkReservableShop() {
        if (status.isInvalidStatus()) {
            throw IllegalArgumentException("유효하지 않은 가게입니다.")
        }
    }

    fun checkItem(product: Product) {
        val match = products.filter { it == product }
        if (match.isEmpty()) {
            throw IllegalArgumentException("존재하지 않는 상품입니다.")
        }
    }

    fun isReservableAt(reserveAt: LocalDateTime): Boolean {
        val match = reservableTimes.filter { it == reserveAt.toLocalTime() }
        return match.isNotEmpty()
    }
}
