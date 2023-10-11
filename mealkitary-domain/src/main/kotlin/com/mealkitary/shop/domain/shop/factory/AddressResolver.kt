package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.shop.ShopAddress

interface AddressResolver {

    fun resolve(fullAddress: String): ShopAddress
}
