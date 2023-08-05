package com.mealkitary.shop.adapter.input.web

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

class GetReservableTimeControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllReservableTimeOfShopTest`() {
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }.answers {
            listOf(LocalTime.of(6, 30))
        }

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0]").value("06:30:00"))

        verify { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }
    }

    @Test
    fun `api integration test - 존재하지 않는 가게의 예약 가능 시간 조회 시 404 에러를 발생한다`() {
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }
            .throws(EntityNotFoundException("존재하지 않는 가게입니다."))

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", 1))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("존재하지 않는 가게입니다."))
    }

    @Test
    fun `api integration test - 가게의 예약 가능 시간이 존재하지 않는다면 204 NoContent를 반환한다`() {
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }.answers { emptyList() }

        mvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/reservable-time", 1))
            .andExpect(status().isNoContent())
    }
}
