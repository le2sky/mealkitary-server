package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.shop.ShopBusinessNumber

interface ShopBusinessNumberValidator {

    fun validate(brn: ShopBusinessNumber)
}
