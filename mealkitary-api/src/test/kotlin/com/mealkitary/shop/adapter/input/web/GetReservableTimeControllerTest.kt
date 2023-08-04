package com.mealkitary.shop.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import io.mockk.every
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
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

        verify { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }
    }
}
