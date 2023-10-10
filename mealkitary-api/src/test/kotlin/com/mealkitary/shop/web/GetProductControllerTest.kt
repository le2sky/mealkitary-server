package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.shop.application.port.input.ProductResponse
import io.mockk.every
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class GetProductControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllProductOfShopTest`() {
        val shopId = UUID.randomUUID()
        every { getProductQuery.loadAllProductByShopId(shopId) }.answers {
            listOf(ProductResponse(1L, "부대찌개", 15000))
        }

        mvc.perform(get("/shops/{shopId}/products", shopId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].name").value("부대찌개"))
            .andExpect(jsonPath("$.[0].price").value(15000))

        verify { getProductQuery.loadAllProductByShopId(shopId) }
    }

    @Test
    fun `api integration test - 존재하지 않는 가게의 상품 조회 시 404 에러를 발생한다`() {
        val shopId = UUID.randomUUID()
        every { getProductQuery.loadAllProductByShopId(shopId) }
            .throws(EntityNotFoundException("존재하지 않는 가게입니다."))

        mvc.perform(get("/shops/{shopId}/products", shopId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("존재하지 않는 가게입니다."))
    }

    @Test
    fun `api integration test - 가게의 상품이 존재하지 않는다면 204 NoContent를 반환한다`() {
        val shopId = UUID.randomUUID()
        every { getProductQuery.loadAllProductByShopId(shopId) } answers { emptyList() }

        mvc.perform(get("/shops/{shopId}/products", shopId))
            .andExpect(status().isNoContent())
    }

    @Test
    fun `api integration test - 가게 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            get("/shops/{shopId}/products", "invalid-uuid-test")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }
}
