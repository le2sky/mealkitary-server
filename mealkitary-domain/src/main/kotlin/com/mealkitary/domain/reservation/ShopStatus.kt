package com.mealkitary.domain.reservation

enum class ShopStatus {
    INVALID,
    VALID;

    fun isInvalidStatus(): Boolean {
        return this.equals(INVALID)
    }
}
