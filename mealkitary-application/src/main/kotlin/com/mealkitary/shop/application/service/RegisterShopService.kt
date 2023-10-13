package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.RegisterShopRequest
import com.mealkitary.shop.application.port.input.RegisterShopUseCase
import com.mealkitary.shop.application.port.output.SaveShopPort
import com.mealkitary.shop.domain.shop.factory.ShopFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class RegisterShopService(
    private val saveShopPort: SaveShopPort,
    private val shopFactory: ShopFactory
) : RegisterShopUseCase {

    override fun register(registerShopRequest: RegisterShopRequest): UUID {
        val shop = shopFactory.createOne(
            registerShopRequest.title,
            registerShopRequest.brn,
            registerShopRequest.address
        )

        return saveShopPort.saveOne(shop)
    }
}
