package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.shop.Shop
import java.util.UUID

interface LoadShopPort {

    fun loadAllShop(): List<Shop>
    fun loadOneShopById(shopId: UUID): Shop
}
