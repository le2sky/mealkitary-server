package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.shop.Shop

interface SaveShopPort {

    fun saveOne(shop: Shop): Long
}
