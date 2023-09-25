package com.mealkitary.shop.application.port.input

interface RegisterShopUseCase {

    fun register(registerShopRequest: RegisterShopRequest): Long
}
