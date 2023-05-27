package com.mealkitary.shop.domain.product

class ProductId(val id: Long) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductId

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
