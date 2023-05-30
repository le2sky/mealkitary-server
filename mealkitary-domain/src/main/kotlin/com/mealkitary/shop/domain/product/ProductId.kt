package com.mealkitary.shop.domain.product

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ProductId(@Column(name = "product_id") var id: Long) : Serializable {

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
