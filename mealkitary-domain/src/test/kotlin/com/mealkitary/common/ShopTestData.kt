package com.mealkitary.common

import com.mealkitary.domain.shop.Shop
import com.mealkitary.domain.shop.ShopStatus
import java.time.LocalTime

class ShopTestData {

    class ShopBuilder(
        private var shopStatus: ShopStatus = ShopStatus.VALID,
        private var reservableTimes: List<LocalTime> = listOf(
            LocalTime.of(9, 0),
            LocalTime.of(12, 0),
            LocalTime.of(18, 0)
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

        fun build(): Shop {
            return Shop(
                this.shopStatus,
                this.reservableTimes
            )
        }
    }

    companion object {
        fun defaultShop(): ShopBuilder {
            return ShopBuilder()
        }
    }
}