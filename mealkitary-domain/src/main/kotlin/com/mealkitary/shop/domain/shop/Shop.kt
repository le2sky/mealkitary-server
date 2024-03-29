package com.mealkitary.shop.domain.shop

import com.mealkitary.common.model.UUIDBaseEntity
import com.mealkitary.shop.domain.product.Product
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.CascadeType
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "shop")
class Shop(
    title: ShopTitle,
    status: ShopStatus,
    businessNumber: ShopBusinessNumber,
    address: ShopAddress,
    reservableTimes: MutableList<LocalTime>,
    products: MutableList<Product>
) : UUIDBaseEntity() {

    @Column(nullable = false)
    var title: ShopTitle = title
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ShopStatus = status
        protected set

    @ElementCollection
    @CollectionTable(
        name = "reservable_time",
        joinColumns = [JoinColumn(name = "shop_id")]
    )
    @Column(name = "reservable_time", nullable = false)
    var reservableTimes: MutableList<LocalTime> = reservableTimes
        protected set

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "shop_id", nullable = false)
    var products: MutableList<Product> = products
        protected set

    val businessNumber: ShopBusinessNumber = businessNumber

    val address: ShopAddress = address

    fun checkReservableShop() {
        if (status.isInvalidStatus()) {
            throw IllegalStateException("유효하지 않은 가게입니다.")
        }
    }

    fun checkItem(product: Product) {
        val match = products.filter { it == product }
        if (match.isEmpty()) {
            throw IllegalArgumentException("존재하지 않는 상품입니다.")
        }
    }

    fun isReservableAt(reserveAt: LocalDateTime): Boolean {
        val match = reservableTimes.filter { it == reserveAt.toLocalTime() }
        return match.isNotEmpty()
    }

    fun changeStatusValid() {
        status = ShopStatus.VALID
    }

    fun changeStatusInvalid() {
        status = ShopStatus.INVALID
    }
}
