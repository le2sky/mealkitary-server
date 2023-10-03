package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import com.mealkitary.shop.domain.shop.address.ShopAddress
import java.time.LocalTime

class ShopFactory(
    private val shopBusinessNumberValidator: ShopBusinessNumberValidator,
    private val addressResolver: AddressResolver
) {

    fun createOne(title: String, brn: String, address: String): Shop {
        val shopBusinessNumber = ShopBusinessNumber.from(brn)

        val shopAddress: ShopAddress = addressResolver.resolveAddress(address)

        shopBusinessNumberValidator.validate(shopBusinessNumber)

        return Shop(
            ShopTitle.from(title),
            ShopStatus.VALID,
            shopBusinessNumber,
            shopAddress,
            emptyList<LocalTime>().toMutableList(),
            emptyList<Product>().toMutableList(),
        )
    }
}
