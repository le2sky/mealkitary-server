package com.mealkitary.shop.domain.shop

import data.ShopTestData.Companion.defaultShop
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ShopStatusTest {

    @Test
    fun isInvalidStatus() {
        val shop = defaultShop().withStatus(ShopStatus.VALID).build()

        shop.status.isValidStatus() shouldBe true
    }

    @Test
    fun isValidStatus() {
        val shop = defaultShop().withStatus(ShopStatus.INVALID).build()

        shop.status.isInvalidStatus() shouldBe true
    }
}
