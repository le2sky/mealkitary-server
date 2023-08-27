package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.util.UUID

class GetReservationServiceTest : AnnotationSpec() {

    private val loadReservationPort = mockk<LoadReservationPort>()
    private val getReservationService = GetReservationService(loadReservationPort)

    @Test
    fun `service unit test - 예약의 상세 정보를 조회한다`() {
        val reservationId = UUID.randomUUID()
        every { loadReservationPort.queryOneReservationById(any()) } answers {
            ReservationResponse(
                reservationId,
                "집밥뚝딱 안양점",
                "부대찌개 외 1건",
                LocalDateTime.now(),
                "PAID",
                emptyList()
            )
        }

        val result = getReservationService.loadOneReservationById(reservationId)

        result.reservationId shouldBe reservationId
        result.shopName shouldBe "집밥뚝딱 안양점"
        result.status shouldBe "PAID"
        result.reservedProduct.isEmpty().shouldBeTrue()
    }
}
