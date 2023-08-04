package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.reservation.adapter.input.web.request.ReserveProductWebRequest
import com.mealkitary.reservation.adapter.input.web.request.ReservedWebProduct
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReserveProductControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - reserveProduct`() {
        every { reserveProductUseCase.reserve(any()) }.answers {
            1L
        }
        val reserveProductWebRequest = ReserveProductWebRequest(
            1L,
            listOf(
                ReservedWebProduct(
                    2L,
                    "부대찌개",
                    3000,
                    3
                )
            ),
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0)
            ).toString()
        )

        mvc.perform(
            MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/reservations/1"))
    }
}
