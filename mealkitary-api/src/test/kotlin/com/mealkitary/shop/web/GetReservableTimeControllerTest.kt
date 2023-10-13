package com.mealkitary.shop.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import io.mockk.every
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalTime
import java.util.UUID

class GetReservableTimeControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllReservableTimeOfShopTest`() {
        val shopId = UUID.randomUUID()
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(shopId) }.answers {
            listOf(LocalTime.of(6, 30))
        }

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", shopId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0]").value("06:30:00"))

        verify { getReservableTimeQuery.loadAllReservableTimeByShopId(shopId) }
    }

    @Test
    fun `api integration test - 존재하지 않는 가게의 예약 가능 시간 조회 시 404 에러를 발생한다`() {
        val shopId = UUID.randomUUID()
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(shopId) }
            .throws(EntityNotFoundException("존재하지 않는 가게입니다."))

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", shopId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("존재하지 않는 가게입니다."))
    }

    @Test
    fun `api integration test - 가게의 예약 가능 시간이 존재하지 않는다면 204 NoContent를 반환한다`() {
        val shopId = UUID.randomUUID()
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(shopId) }.answers { emptyList() }

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", shopId))
            .andExpect(status().isNoContent())
    }

    @Test
    fun `api integration test - 가게 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", "invalid-uuid-test")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }
}
