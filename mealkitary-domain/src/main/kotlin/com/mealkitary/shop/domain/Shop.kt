package com.mealkitary.shop.domain

import java.time.LocalDateTime
import java.time.LocalTime

class Shop(
    private val status: ShopStatus,
    private val reservableTimes: List<LocalTime>
) {
    fun checkReservableShop() {
        if (status.isInvalidStatus()) {
            throw IllegalArgumentException("유효하지 않은 가게입니다.")
        }
    }

    fun isReservableAt(reserveAt: LocalDateTime): Boolean {
        val match = reservableTimes.filter { it == reserveAt.toLocalTime() }
        return match.isNotEmpty()
    }
}
