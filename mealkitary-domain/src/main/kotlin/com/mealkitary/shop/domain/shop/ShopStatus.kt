package com.mealkitary.shop.domain.shop

enum class ShopStatus {
    INVALID,
    VALID;

    fun isInvalidStatus(): Boolean {
        return this.equals(INVALID)
    }
}
