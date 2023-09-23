package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import io.mockk.every
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class UpdateShopStatusControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - updateShopStatus`() {
        val shopId = 1L
        every { updateShopStatusUseCase.update(any()) } answers {}

        mvc.perform(
            MockMvcRequestBuilders.post("/shops/{shopId}/status", shopId)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
