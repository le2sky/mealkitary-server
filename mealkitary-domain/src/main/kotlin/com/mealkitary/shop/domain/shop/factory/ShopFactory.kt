package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import java.time.LocalTime

class ShopFactory(
    private val shopBusinessNumberValidator: ShopBusinessNumberValidator
) {

    fun createOne(title: String, brn: String): Shop {
        val shopBusinessNumber = ShopBusinessNumber.from(brn)

        shopBusinessNumberValidator.validate(shopBusinessNumber)

        return Shop(
            ShopTitle.from(title),
            ShopStatus.VALID,
            shopBusinessNumber,
            emptyList<LocalTime>().toMutableList(),
            emptyList<Product>().toMutableList(),
        )
    }
}
