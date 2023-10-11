package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.output.LoadReservableTimePort
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalTime
import java.util.UUID

class GetReservableTimeServiceTest : AnnotationSpec() {

    private val loadReservableTimePort = mockk<LoadReservableTimePort>()
    private val getReservableTimeService = GetReservableTimeService(loadReservableTimePort)

    @Test
    fun `service unit test - 가게 ID에 해당하는 가게의 모든 예약 가능 시간을 조회한다`() {
        every { loadReservableTimePort.loadAllReservableTimeByShopId(any()) } answers {
            listOf(
                LocalTime.of(6, 30),
                LocalTime.of(18, 30)
            )
        }
        val expected = listOf(
            LocalTime.of(6, 30),
            LocalTime.of(18, 30)
        )

        val actual = getReservableTimeService.loadAllReservableTimeByShopId(UUID.randomUUID())

        actual shouldBe expected
        actual.size shouldBe 2
    }
}
