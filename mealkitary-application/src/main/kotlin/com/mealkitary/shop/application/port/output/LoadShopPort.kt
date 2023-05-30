package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.shop.Shop

interface LoadShopPort {

    fun loadAllShop(): List<Shop>
}
