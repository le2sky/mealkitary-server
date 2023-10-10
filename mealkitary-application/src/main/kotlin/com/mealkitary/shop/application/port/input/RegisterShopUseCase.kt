package com.mealkitary.shop.application.port.input

import java.util.UUID

interface RegisterShopUseCase {

    fun register(registerShopRequest: RegisterShopRequest): UUID
}
