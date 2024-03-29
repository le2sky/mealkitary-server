package com.mealkitary.reservation.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.reservation.web.request.ReserveProductWebRequest
import com.mealkitary.reservation.web.request.ReservedWebProduct
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class ReserveProductControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - reserveProduct`() {
        val id = UUID.randomUUID()
        val shopId = UUID.randomUUID()
        every { reserveProductUseCase.reserve(any()) }.answers {
            id
        }
        val reserveProductWebRequest = ReserveProductWebRequest(
            shopId.toString(),
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
            .andExpect(header().string("Location", "http://localhost/reservations/$id"))
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
            shopId = UUID.randomUUID().toString(),
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
            shopId = UUID.randomUUID().toString(),
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

    @Test
    fun `api integration test - 가게 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        val reserveProductWebRequest = ReserveProductWebRequest(
            shopId = "invalid-uuid-test",
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
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }

    @Test
    fun `api integration test - 예약 대상 가게가 존재하지 않는 경우 404 에러를 발생한다`() {
        every { reserveProductUseCase.reserve(any()) }
            .throws(EntityNotFoundException("존재하지 않는 가게입니다."))
        val reserveProductWebRequest = ReserveProductWebRequest(
            UUID.randomUUID().toString(),
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
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("존재하지 않는 가게입니다."))
    }
}
