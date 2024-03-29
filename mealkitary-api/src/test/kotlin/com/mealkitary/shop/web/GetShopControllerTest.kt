package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.shop.application.port.input.ShopResponse
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class GetShopControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllShopTest`() {
        val shopId = UUID.randomUUID()
        every { getShopQuery.loadAllShop() }.answers {
            listOf(ShopResponse(shopId, "집밥뚝딱"))
        }

        mvc.perform(MockMvcRequestBuilders.get("/shops/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(shopId.toString()))
            .andExpect(jsonPath("$.[0].title").value("집밥뚝딱"))
    }

    @Test
    fun `api integration test - 어떠한 가게도 존재하지 않는다면 204 NoContent를 반환한다`() {
        every { getShopQuery.loadAllShop() }.answers { emptyList() }

        mvc.perform(MockMvcRequestBuilders.get("/shops/"))
            .andExpect(status().isNoContent())
    }
}
