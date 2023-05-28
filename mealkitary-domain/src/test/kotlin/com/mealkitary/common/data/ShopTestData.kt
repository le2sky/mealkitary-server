package com.mealkitary.common.data

import com.mealkitary.common.data.ProductTestData.Companion.defaultProduct
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopStatus
import java.time.LocalTime

class ShopTestData {

    class ShopBuilder(
        private var id: Long = 1L,
        private var title: String = "집밥뚝딱 안양점",
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

        fun withId(id: Long): ShopBuilder {
            this.id = id
            return this
        }

        fun withTitle(title: String): ShopBuilder {
            this.title = title
            return this
        }

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
                this.id,
                this.title,
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
