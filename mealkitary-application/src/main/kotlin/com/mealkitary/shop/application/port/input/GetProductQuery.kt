package com.mealkitary.shop.application.port.input

import java.util.UUID

interface GetProductQuery {

    fun loadAllProductByShopId(shopId: UUID): List<ProductResponse>
}
