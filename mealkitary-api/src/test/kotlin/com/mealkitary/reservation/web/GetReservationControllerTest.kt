package com.mealkitary.reservation.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.application.port.input.ReservedProduct
import io.kotest.inspectors.forAll
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class GetReservationControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getOneReservation`() {
        val reservationId = UUID.randomUUID()
        val reserveAt = LocalDateTime.of(
            LocalDate.of(2023, 6, 23), LocalTime.of(6, 30)
        )
        every { getReservationQuery.loadOneReservationById(reservationId) } answers {
            ReservationResponse(
                reservationId,
                "집밥뚝딱 안양점",
                "부대찌개 외 1건",
                reserveAt,
                "PAID",
                listOf(
                    ReservedProduct(
                        1L,
                        "부대찌개",
                        20000,
                        2
                    ),
                    ReservedProduct(
                        2L,
                        "김치찌개",
                        20000,
                        1
                    )
                )
            )
        }

        mvc.perform(get("/reservations/{reservationId}", reservationId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.reservationId").value(reservationId.toString()))
            .andExpect(jsonPath("$.shopName").value("집밥뚝딱 안양점"))
            .andExpect(jsonPath("$.reserveAt").value(reserveAt.format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.status").value("PAID"))
            .andExpect(jsonPath("$.reservedProduct[0].productId").value(1L))
            .andExpect(jsonPath("$.reservedProduct[0].name").value("부대찌개"))
            .andExpect(jsonPath("$.reservedProduct[0].price").value(20000))
            .andExpect(jsonPath("$.reservedProduct[0].count").value(2))
            .andExpect(jsonPath("$.reservedProduct[1].productId").value(2L))
            .andExpect(jsonPath("$.reservedProduct[1].name").value("김치찌개"))
            .andExpect(jsonPath("$.reservedProduct[1].price").value(20000))
            .andExpect(jsonPath("$.reservedProduct[1].count").value(1))
    }

    @Test
    fun `api integration test - getAllReservation`() {
        val reservationId = UUID.randomUUID()
        val shopId = UUID.randomUUID()
        val reserveAt = LocalDateTime.of(
            LocalDate.of(2023, 6, 23), LocalTime.of(6, 30)
        )
        every { getReservationQuery.loadAllReservationByShopId(any()) } answers {
            listOf(
                ReservationResponse(
                    reservationId,
                    "집밥뚝딱 안양점",
                    "부대찌개 외 1건",
                    reserveAt,
                    "PAID",
                    listOf(
                        ReservedProduct(
                            1L,
                            "부대찌개",
                            20000,
                            2
                        ),
                        ReservedProduct(
                            2L,
                            "김치찌개",
                            20000,
                            1
                        )
                    )
                )
            )
        }

        mvc.perform(get("/reservations?shopId=$shopId"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].reservationId").value(reservationId.toString()))
            .andExpect(jsonPath("$.[0].shopName").value("집밥뚝딱 안양점"))
            .andExpect(jsonPath("$.[0].reserveAt").value(reserveAt.format(DateTimeFormatter.ISO_DATE_TIME)))
            .andExpect(jsonPath("$.[0].status").value("PAID"))
            .andExpect(jsonPath("$.[0].reservedProduct[0].productId").value(1L))
            .andExpect(jsonPath("$.[0].reservedProduct[0].name").value("부대찌개"))
            .andExpect(jsonPath("$.[0].reservedProduct[0].price").value(20000))
            .andExpect(jsonPath("$.[0].reservedProduct[0].count").value(2))
            .andExpect(jsonPath("$.[0].reservedProduct[1].productId").value(2L))
            .andExpect(jsonPath("$.[0].reservedProduct[1].name").value("김치찌개"))
            .andExpect(jsonPath("$.[0].reservedProduct[1].price").value(20000))
            .andExpect(jsonPath("$.[0].reservedProduct[1].count").value(1))
    }

    @Test
    fun `api integration test - getAllReservation - 쿼리 파라미터에 가게 식별자가 누락된 경우 400 에러를 발생한다`() {
        every { getReservationQuery.loadAllReservationByShopId(any()) } answers { emptyList() }

        listOf("/reservations?shopId=", "/reservations", "/reservations?shopId")
            .forAll {
                mvc.perform(get(it))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("가게 식별자는 필수입니다."))
            }
    }

    @Test
    fun `api integration test - getAllReservation - 해당 가게의 예약이 존재하지 않으면 204 NoContent를 반환한다`() {
        every { getReservationQuery.loadAllReservationByShopId(any()) } answers { emptyList() }

        mvc.perform(get("/reservations?shopId=${UUID.randomUUID()}"))
            .andExpect(status().isNoContent())
    }

    @Test
    fun `api integration test - getOneReservation - 예약 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            get("/reservations/{reservationId}", "invalid-uuid-test")
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }

    @Test
    fun `api integration test - 가게 식별자가 UUID 형태가 아니라면 400 에러를 발생한다`() {
        mvc.perform(
            get("/reservations?shopId=invalid-uuid-test")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 UUID 형식입니다."))
    }

    @Test
    fun `api integration test - getOneReservation - 내부에서 EntityNotFound 에러가 발생하면 404 에러를 발생한다`() {
        every { getReservationQuery.loadOneReservationById(any()) }
            .throws(EntityNotFoundException("존재하지 않는 예약입니다."))

        mvc.perform(get("/reservations/{reservationId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("존재하지 않는 예약입니다."))
    }
}
