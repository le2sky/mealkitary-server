package com.mealkitary.brn

import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.factory.ShopBusinessNumberValidator
import org.springframework.stereotype.Component

@Component
class SimpleBrnValidator : ShopBusinessNumberValidator {

    override fun validate(brn: ShopBusinessNumber) {}
}
