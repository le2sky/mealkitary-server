package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.AcceptReservationUseCase
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SendAcceptedReservationMessagePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AcceptReservationService(
    private val loadReservationPort: LoadReservationPort,
    private val sendAcceptedReservationMessagePort: SendAcceptedReservationMessagePort
) : AcceptReservationUseCase {

    @Transactional
    override fun accept(reservationId: UUID) {
        val reservation = loadReservationPort.loadOneReservationById(reservationId)

        reservation.accept()

        sendAcceptedReservationMessagePort.sendAcceptedReservationMessage()
    }
}
