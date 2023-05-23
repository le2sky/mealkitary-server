package com.mealkitary.domain.shop

enum class ShopStatus {
    INVALID,
    VALID;

    fun isInvalidStatus(): Boolean {
        return this.equals(INVALID)
    }
}
