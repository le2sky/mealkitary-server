package data

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.ShopAddress
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.ShopStatus
import com.mealkitary.shop.domain.shop.ShopTitle
import data.ProductTestData.Companion.defaultProduct
import java.time.LocalTime

class ShopTestData {

    class ShopBuilder(
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
        ),
        private var shopBusinessNumber: ShopBusinessNumber = ShopBusinessNumber.from("123-45-67890"),
        private var shopAddress: ShopAddress = ShopAddress.of(
            "1234567890",
            Coordinates.of(
                126.99599512792346,
                35.976749396987046
            ),
            Address.of(
                "region1DepthName",
                "region2DepthName",
                "region3DepthName",
                "roadName"
            )
        )
    ) {

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

        fun withBusinessNumber(shopBusinessNumber: ShopBusinessNumber): ShopBuilder {
            this.shopBusinessNumber = shopBusinessNumber
            return this
        }

        fun withAddress(shopAddress: ShopAddress): ShopBuilder {
            this.shopAddress = shopAddress
            return this
        }

        fun build(): Shop {
            return Shop(
                ShopTitle.from(this.title),
                this.shopStatus,
                this.shopBusinessNumber,
                this.shopAddress,
                this.reservableTimes.toMutableList(),
                this.products.toMutableList()
            )
        }
    }

    companion object {
        fun defaultShop(): ShopBuilder {
            return ShopBuilder()
        }
    }
}
