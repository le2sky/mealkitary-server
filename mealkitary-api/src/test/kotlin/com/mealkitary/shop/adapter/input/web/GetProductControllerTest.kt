package com.mealkitary.shop.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.shop.application.port.input.ProductResponse
import io.mockk.every
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetProductControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllProductOfShopTest`() {
        every { getProductQuery.loadAllProductByShopId(1L) }.answers {
            listOf(ProductResponse(1L, "부대찌개", 15000))
        }

        mvc.perform(get("/shops/{shopId}/products", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].name").value("부대찌개"))
            .andExpect(jsonPath("$.[0].price").value(15000))

        verify { getProductQuery.loadAllProductByShopId(1L) }
    }
}
