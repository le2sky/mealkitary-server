package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopAddress
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class ShopFactory(
    private val shopBusinessNumberValidator: ShopBusinessNumberValidator,
    private val addressResolver: AddressResolver
) {

    fun createOne(title: String, brn: String, fullAddress: String): Shop {
        val shopBusinessNumber = ShopBusinessNumber.from(brn)

        val shopAddress: ShopAddress = addressResolver.resolve(fullAddress)

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
