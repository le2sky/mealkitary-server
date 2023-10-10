package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.shop.ShopAddress

interface AddressResolver {

    fun resolveAddress(address: String): ShopAddress
}
