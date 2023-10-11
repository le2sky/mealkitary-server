package com.mealkitary.shop.application.port.input

import java.util.UUID

interface UpdateShopStatusUseCase {

    fun update(shopId: UUID)
}
