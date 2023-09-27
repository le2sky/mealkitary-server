package com.mealkitary.brn

import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.factory.ShopBusinessNumberValidator
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class SimpleBrnValidator : ShopBusinessNumberValidator {

    override fun validate(brn: ShopBusinessNumber) {}
}
