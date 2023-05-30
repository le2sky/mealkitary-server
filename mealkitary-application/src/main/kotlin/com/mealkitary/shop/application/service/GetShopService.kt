package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.GetShopQuery
import com.mealkitary.shop.application.port.input.ShopResponse
import com.mealkitary.shop.application.port.output.LoadShopPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetShopService(
    private val loadShopPort: LoadShopPort
) : GetShopQuery {

    override fun loadAllShop() = loadShopPort.loadAllShop().map { ShopResponse(it.id, it.title) }
}
