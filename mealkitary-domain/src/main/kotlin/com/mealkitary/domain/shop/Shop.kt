package com.mealkitary.domain.shop

import java.time.LocalDateTime
import java.time.LocalTime

class Shop(
    private val status: ShopStatus,
    private val reservableTimes: List<LocalTime>
) {
    fun isInvalid() = status.isInvalidStatus()
    fun isReservableAt(reserveAt: LocalDateTime): Boolean {
        val match = reservableTimes.filter { it == reserveAt.toLocalTime() }
        return match.isNotEmpty()
    }
}
