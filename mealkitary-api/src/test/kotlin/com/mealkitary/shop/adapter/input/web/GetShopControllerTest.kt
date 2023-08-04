package com.mealkitary.shop.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.shop.application.port.input.ShopResponse
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetShopControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllShopTest`() {
        every { getShopQuery.loadAllShop() }.answers {
            listOf(ShopResponse(1L, "집밥뚝딱"))
        }

        mvc.perform(MockMvcRequestBuilders.get("/shops/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].title").value("집밥뚝딱"))
    }
}
