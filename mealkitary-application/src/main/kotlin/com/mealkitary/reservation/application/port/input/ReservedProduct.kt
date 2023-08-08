package com.mealkitary.reservation.application.port.input

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.ReservationLineItem
import com.mealkitary.shop.domain.product.ProductId

data class ReservedProduct(
    val productId: Long,
    val name: String,
    val price: Int,
    val count: Int
) {

    fun mapToDomainEntity() = ReservationLineItem.of(
        ProductId(productId),
        name,
        Money.from(price),
        count
    )
}
