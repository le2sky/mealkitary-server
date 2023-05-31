package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.product.Product

interface LoadProductPort {

    fun loadAllProductByShopId(shopId: Long): List<Product>
}
