package com.mealkitary.common

import com.mealkitary.common.ProductTestData.Companion.defaultProduct
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopStatus
import java.time.LocalTime

class ShopTestData {

    class ShopBuilder(
        private var shopStatus: ShopStatus = ShopStatus.VALID,
        private var reservableTimes: List<LocalTime> = listOf(
            LocalTime.of(9, 0),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0)
        ),
        private var products: List<Product> = listOf(
            defaultProduct().withId(1L).withName("부대찌개").build(),
            defaultProduct().withId(2L).withName("닭볶음탕").build()
        )
    ) {

        fun withReservableTimes(vararg times: LocalTime): ShopBuilder {
            this.reservableTimes = times.toList()
            return this
        }

        fun withStatus(shopStatus: ShopStatus): ShopBuilder {
            this.shopStatus = shopStatus
            return this
        }

        fun withProducts(vararg products: Product): ShopBuilder {
            this.products = products.toList()
            return this
        }

        fun build(): Shop {
            return Shop(
                this.shopStatus,
                this.reservableTimes,
                this.products
            )
        }
    }

    companion object {
        fun defaultShop(): ShopBuilder {
            return ShopBuilder()
        }
    }
}
