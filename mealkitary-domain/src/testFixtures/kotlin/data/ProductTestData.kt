package data

import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.product.ProductId

class ProductTestData {

    class ProductBuilder(
        private var id: ProductId = ProductId(1L),
        private var name: String = "부대찌개",
        private var price: Money = Money.from(1000)
    ) {

        fun withId(id: Long): ProductBuilder {
            this.id = ProductId(id)
            return this
        }

        fun withName(name: String): ProductBuilder {
            this.name = name
            return this
        }

        fun withPrice(price: Money): ProductBuilder {
            this.price = price
            return this
        }

        fun build(): Product {
            return Product(this.id, this.name, this.price)
        }
    }

    companion object {
        fun defaultProduct(): ProductBuilder {
            return ProductBuilder()
        }
    }
}
