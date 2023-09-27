package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.RegisterShopRequest
import com.mealkitary.shop.application.port.input.RegisterShopUseCase
import com.mealkitary.shop.application.port.output.SaveShopPort
import com.mealkitary.shop.domain.shop.factory.ShopBusinessNumberValidator
import com.mealkitary.shop.domain.shop.factory.ShopFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RegisterShopService(
    private val saveShopPort: SaveShopPort,
    shopBusinessNumberValidator: ShopBusinessNumberValidator
) : RegisterShopUseCase {

    private val shopFactory = ShopFactory(shopBusinessNumberValidator)

    @Transactional
    override fun register(registerShopRequest: RegisterShopRequest): Long {
        val shop = shopFactory.createOne(registerShopRequest.title, registerShopRequest.brn)

        return saveShopPort.saveOne(shop)
    }
}
