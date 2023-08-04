package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.reservation.adapter.input.web.request.ReserveProductWebRequest
import com.mealkitary.reservation.adapter.input.web.request.ReservedWebProduct
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/reservations/1"))
    }

    @Test
    fun `api integration test - 가게 식별자가 누락된 경우 400 에러를 발생한다`() {
        val reserveProductWebRequest = ReserveProductWebRequest(
            products = listOf(
                ReservedWebProduct(
                    2L,
                    "부대찌개",
                    3000,
                    3
                )
            ),
            reservedAt = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0)
            ).toString()
        )

        mvc.perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("shopId"))
            .andExpect(jsonPath("$..errors[0].reason").value("예약 대상 가게 식별자는 필수입니다."))
    }

    @Test
    fun `api integration test - 예약 시간이 누락된 경우 400 에러를 발생한다`() {
        val reserveProductWebRequest = ReserveProductWebRequest(
            shopId = 2,
            products = listOf(
                ReservedWebProduct(
                    2L,
                    "부대찌개",
                    3000,
                    3
                )
            )
        )

        mvc.perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("reservedAt"))
            .andExpect(jsonPath("$..errors[1].field").value("reservedAt"))
            .andExpect(
                jsonPath("$.errors[?(@.reason == '잘못된 날짜 형식입니다. yyyy-mm-ddThh:mm:ss 형식으로 입력해주세요.')]")
                    .exists()
            )
            .andExpect(jsonPath("$.errors[?(@.reason == '예약 시간은 필수입니다.')]").exists())
    }

    @Test
    fun `api integration test - 예약 상품 목록이 누락된 경우 400 에러를 발생한다`() {
        val reserveProductWebRequest = ReserveProductWebRequest(
            shopId = 2,
            reservedAt = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0)
            ).toString()
        )

        mvc.perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."))
            .andExpect(jsonPath("$..errors[0].field").value("products"))
            .andExpect(jsonPath("$..errors[0].reason").value("예약 상품 목록은 필수입니다."))
    }

    @Test
    fun `api integration test - JSON 형식이 아닌 경우 400 에러가 발생한다`() {
        mvc.perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("}{"))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("JSON 형식이 잘못되었습니다."))
    }
}
