package com.mealkitary.domain.reservation

class Shop(
    private val status: ShopStatus
) {
    fun isInvalid() = status.isInvalidStatus()
}
