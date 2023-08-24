package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SendAcceptedReservationMessagePort
import com.mealkitary.reservation.domain.reservation.Reservation
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AcceptReservationServiceTest : AnnotationSpec() {

    private val loadReservationPort = mockk<LoadReservationPort>()
    private val sendAcceptedReservationMessagePort = mockk<SendAcceptedReservationMessagePort>()

    private val acceptReservationService =
        AcceptReservationService(loadReservationPort, sendAcceptedReservationMessagePort)

    @Test
    fun `service unit test - 예약이 정상적으로 승인되면 예약의 상태가 승인됨으로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.PAID)
            .build()
        stubbingReservation(reservation)

        acceptReservationService.accept(reservation.id)

        reservation.reservationStatus shouldBe ReservationStatus.RESERVED
    }

    @Test
    fun `service unit test - 예약이 정상적으로 승인되면 예약 승인 알림이 전송된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.PAID)
            .build()
        stubbingReservation(reservation)

        acceptReservationService.accept(reservation.id)

        verify(exactly = 1) { sendAcceptedReservationMessagePort.sendAcceptedReservationMessage() }
    }

    @Test
    fun `service unit test - 미결제 상태의 예약을 승인하면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        stubbingReservation(reservation)

        shouldThrow<IllegalStateException> {
            acceptReservationService.accept(reservation.id)
        } shouldHaveMessage "미결제 상태인 예약은 승인할 수 없습니다."
    }

    @Test
    fun `service unit test - 정상적으로 생성되지 않은 예약을 승인하면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NONE)
            .build()
        stubbingReservation(reservation)

        shouldThrow<IllegalStateException> {
            acceptReservationService.accept(reservation.id)
        } shouldHaveMessage "정상적으로 생성된 예약에 대해서만 수행 가능합니다."
    }

    @Test
    fun `service unit test - 이미 승인된 예약은 다시 승인하면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.RESERVED)
            .build()
        stubbingReservation(reservation)

        shouldThrow<IllegalStateException> {
            acceptReservationService.accept(reservation.id)
        } shouldHaveMessage "이미 승인된 예약입니다."
    }

    @Test
    fun `service unit test - 이미 거절된 예약을 승인하면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.REJECTED)
            .build()
        stubbingReservation(reservation)

        shouldThrow<IllegalStateException> {
            acceptReservationService.accept(reservation.id)
        } shouldHaveMessage "이미 예약 거부된 건에 대해서 승인할 수 없습니다."
    }

    private fun stubbingReservation(reservation: Reservation) {
        every { loadReservationPort.loadOneReservationById(any()) } answers { reservation }
        every { sendAcceptedReservationMessagePort.sendAcceptedReservationMessage() } answers {}
    }
}
