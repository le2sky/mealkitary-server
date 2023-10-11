package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.GetProductQuery
import com.mealkitary.shop.application.port.input.ProductResponse
import com.mealkitary.shop.application.port.output.LoadProductPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class GetProductService(
    private val loadProductPort: LoadProductPort
) : GetProductQuery {

    override fun loadAllProductByShopId(shopId: UUID) = loadProductPort.loadAllProductByShopId(shopId)
        .map { ProductResponse(it.id.id, it.name, it.price.value) }
}
