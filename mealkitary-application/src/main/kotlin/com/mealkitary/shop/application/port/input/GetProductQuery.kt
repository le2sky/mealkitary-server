package com.mealkitary.shop.application.port.input

interface GetProductQuery {

    fun loadAllProductByShopId(shopId: Long): List<ProductResponse>
}
