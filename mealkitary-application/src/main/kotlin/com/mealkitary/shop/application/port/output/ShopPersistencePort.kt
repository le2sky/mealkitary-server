package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.shop.Shop

interface ShopPersistencePort {

    fun loadOneShopById(shopId: Long): Shop

    fun hasReservations(shopId: Long): Boolean
}
