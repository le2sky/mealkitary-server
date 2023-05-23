package com.mealkitary.domain.shop

import com.mealkitary.common.constants.domain.ShopConstants.Validation.ErrorMessage.INVALID_SHOP
import java.time.LocalDateTime
import java.time.LocalTime

class Shop(
    private val status: ShopStatus,
    private val reservableTimes: List<LocalTime>
) {
    fun checkReservableShop() {
        if (status.isInvalidStatus()) {
            throw IllegalArgumentException(INVALID_SHOP.message)
        }
    }

    fun isReservableAt(reserveAt: LocalDateTime): Boolean {
        val match = reservableTimes.filter { it == reserveAt.toLocalTime() }
        return match.isNotEmpty()
    }
}
