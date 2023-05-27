package com.mealkitary.shop.domain.product

class Product(
    private val id: ProductId,
    private val name: String,
    private val price: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        return price == other.price
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + price
        return result
    }
}
