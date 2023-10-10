package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import io.mockk.every
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID

class UpdateShopStatusControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - updateShopStatus`() {
        val shopId = UUID.randomUUID()
        every { updateShopStatusUseCase.update(any()) } answers {}

        mvc.perform(
            MockMvcRequestBuilders.post("/shops/{shopId}/status", shopId)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
