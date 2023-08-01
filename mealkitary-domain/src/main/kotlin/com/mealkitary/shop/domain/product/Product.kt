package com.mealkitary.shop.domain.product

import com.mealkitary.common.model.Money
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "product")
class Product(
    id: ProductId,
    name: String,
    price: Money
) {

    @EmbeddedId
    var id: ProductId = id
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Embedded
    var price: Money = price
        protected set

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
        result = 31 * result + price.hashCode()
        return result
    }
}
