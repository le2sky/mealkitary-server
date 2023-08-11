package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.reservation.adapter.input.web.request.PayReservationWebRequest
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class PayReservationControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - payReservation`() {
        val id = UUID.randomUUID()
        every { payReservationUseCase.pay(any()) }.answers { id }
        val payReservationWebRequest = PayReservationWebRequest(
            "paymentKey",
            2000
        )

        mvc.perform(
            post("/reservations/{reservationId}/pay", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/payments/$id"))
    }

    @Test
    fun `api integration test - 예약 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        val id = "invalid-uuid-test"
        val payReservationWebRequest = PayReservationWebRequest(
            "paymentKey",
            2000
        )

        mvc.perform(
            post("/reservations/{reservationId}/pay", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }

    @Test
    fun `api integration test - JSON 형식이 아닌 경우 400 에러가 발생한다`() {
        mvc.perform(
            post("/reservations/{reservationId}/pay", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("}{"))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("JSON 형식이 잘못되었습니다."))
    }

    @Test
    fun `api integration test - 결제 식별자가 누락된 경우 400 에러가 발생한다`() {
        val payReservationWebRequest = PayReservationWebRequest(
            amount = 2000
        )

        mvc.perform(
            post("/reservations/{reservationId}/pay", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("paymentKey"))
            .andExpect(jsonPath("$..errors[0].reason").value("결제 식별자는 필수입니다."))
    }

    @Test
    fun `api integration test - 결제 금액이 누락된 경우 400 에러가 발생한다`() {
        val payReservationWebRequest = PayReservationWebRequest(
            paymentKey = "paymentKey"
        )

        mvc.perform(
            post("/reservations/{reservationId}/pay", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("amount"))
            .andExpect(jsonPath("$..errors[0].reason").value("결제 금액은 필수입니다."))
    }

    @Test
    fun `api integration test - 결제 금액과 결제 식별자 모두 누락된 경우 400 에러가 발생한다`() {
        val id = UUID.randomUUID()

        mvc.perform(
            post("/reservations/{reservationId}/pay", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PayReservationWebRequest()))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$.errors[?(@.field == 'amount')]").exists())
            .andExpect(jsonPath("$.errors[?(@.field == 'paymentKey')]").exists())
            .andExpect(jsonPath("$.errors[?(@.reason == '결제 금액은 필수입니다.')]").exists())
            .andExpect(jsonPath("$.errors[?(@.reason == '결제 식별자는 필수입니다.')]").exists())
    }

    @Test
    fun `api integration test - 내부에서 EntityNotFound 에러가 발생하면 404 에러를 발생한다`() {
        val id = UUID.randomUUID()
        every { payReservationUseCase.pay(any()) }.throws(EntityNotFoundException("존재하지 않는 예약입니다."))
        val payReservationWebRequest = PayReservationWebRequest(
            "paymentKey",
            2000
        )

        mvc.perform(
            post("/reservations/{reservationId}/pay", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value("404"))
            .andExpect(jsonPath("$.message").value("존재하지 않는 예약입니다."))
    }
}
