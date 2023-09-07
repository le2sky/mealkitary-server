package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import io.mockk.every
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID

class RejectReservationControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - rejectReservation`() {
        val id = UUID.randomUUID()
        every { rejectReservationUseCase.reject(any()) } answers {}

        mvc.perform(
            MockMvcRequestBuilders.post("/reservations/{reservationId}/reject", id.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `api integration test - 예약 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            MockMvcRequestBuilders.post("/reservations/{reservationId}/reject", "invalid-uuid-test")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }

    @Test
    fun `api integration test - 내부에서 EntityNotFound 에러가 발생하면 404 에러를 발생한다`() {
        val id = UUID.randomUUID()
        every { rejectReservationUseCase.reject(any()) }.throws(EntityNotFoundException("존재하지 않는 예약입니다."))

        mvc.perform(
            MockMvcRequestBuilders.post("/reservations/{reservationId}/reject", id.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("존재하지 않는 예약입니다."))
    }
}
