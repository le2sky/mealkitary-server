package com.mealkitary.shop.domain

enum class ShopStatus {
    INVALID,
    VALID;

    fun isInvalidStatus(): Boolean {
        return this.equals(INVALID)
    }
}
