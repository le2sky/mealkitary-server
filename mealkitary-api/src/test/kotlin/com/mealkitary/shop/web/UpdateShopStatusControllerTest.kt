package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import io.mockk.every
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class UpdateShopStatusControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - updateShopStatus`() {
        val shopId = UUID.randomUUID()
        every { updateShopStatusUseCase.update(any()) } answers {}

        mvc.perform(
            post("/shops/{shopId}/status", shopId)
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `api integration test - 가게 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            post("/shops/{shopId}/status", "invalid-uuid-test")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }
}
