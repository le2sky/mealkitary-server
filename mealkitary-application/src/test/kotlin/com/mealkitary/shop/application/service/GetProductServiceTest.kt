package com.mealkitary.shop.application.service

import com.mealkitary.common.model.Money
import com.mealkitary.shop.application.port.input.ProductResponse
import com.mealkitary.shop.application.port.output.LoadProductPort
import com.mealkitary.shop.domain.product.Product
import com.mealkitary.shop.domain.product.ProductId
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class GetProductServiceTest : AnnotationSpec() {

    private val loadProductPort = mockk<LoadProductPort>()
    private val getProductService = GetProductService(loadProductPort)

    @Test
    fun `service unit test - 가게 ID에 해당하는 가게의 상품 목록을 조회한다`() {
        every { loadProductPort.loadAllProductByShopId(any()) } answers {
            listOf(Product(ProductId(1L), "부대찌개", Money.from(15000)))
        }
        val expected = listOf(ProductResponse(1L, "부대찌개", 15000))

        val actual = getProductService.loadAllProductByShopId(UUID.randomUUID())

        actual shouldBe expected
    }
}
