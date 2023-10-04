package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.shop.web.request.RegisterShopWebRequest
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RegisterShopControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - registerShop`() {
        every { registerShopUseCase.register(any()) } answers { 1L }

        val registerShopWebRequest = RegisterShopWebRequest("집밥뚝딱 안양점", "123-23-12345", "경기도 안양시 동안구 벌말로 40")

        mvc.perform(
            MockMvcRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerShopWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/shops/1"))
    }

    @Test
    fun `api integration test - 가게 이름이 누락된 경우 400 에러를 발생한다`() {
        val registerShopWebRequest = RegisterShopWebRequest(brn = "123-23-12345", address = "경기도 안양시 동안구 벌말로 40")

        mvc.perform(
            MockMvcRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerShopWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("title"))
            .andExpect(jsonPath("$..errors[0].reason").value("등록 대상 가게의 이름은 필수입니다."))
    }

    @Test
    fun `api integration test - 사업자 번호가 누락된 경우 400 에러를 발생한다`() {
        val registerShopWebRequest = RegisterShopWebRequest(title = "집밥뚝딱 안양점", address = "경기도 안양시 동안구 벌말로 40")

        mvc.perform(
            MockMvcRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerShopWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("brn"))
            .andExpect(jsonPath("$..errors[0].reason").value("사업자 번호는 필수입니다."))
    }

    @Test
    fun `api integration test - 주소가 누락된 경우 400 에러를 발생한다`() {
        val registerShopWebRequest = RegisterShopWebRequest(title = "집밥뚝딱 안양점", brn = "123-23-12345")

        mvc.perform(
            MockMvcRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerShopWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("address"))
            .andExpect(jsonPath("$..errors[0].reason").value("주소는 필수입니다."))
    }

    @Test
    fun `api integration test - JSON 형식이 아닌 경우 400 에러가 발생한다`() {
        mvc.perform(
            MockMvcRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("}{"))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("JSON 형식이 잘못되었습니다."))
    }
}
