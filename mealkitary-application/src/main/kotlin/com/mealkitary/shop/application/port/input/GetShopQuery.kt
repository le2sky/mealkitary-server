package com.mealkitary.shop.application.port.input

interface GetShopQuery {

    fun loadAllShop(): List<ShopResponse>
}
