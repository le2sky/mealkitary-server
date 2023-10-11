package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.shop.Shop
import java.util.UUID

interface SaveShopPort {

    fun saveOne(shop: Shop): UUID
}
