package com.mealkitary.shop.application.port.output

import com.mealkitary.shop.domain.product.Product
import java.util.UUID

interface LoadProductPort {

    fun loadAllProductByShopId(shopId: UUID): List<Product>
}
