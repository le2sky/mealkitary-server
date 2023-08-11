package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SavePaymentPort
import com.mealkitary.reservation.application.port.output.SaveReservationPort
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.stereotype.Repository
import java.util.UUID

private const val NOT_FOUND_RESERVATION_MESSAGE = "존재하지 않는 예약입니다."

@Repository
class SpringDataJpaReservationPersistenceAdapter(
    private val reservationRepository: ReservationRepository,
    private val paymentRepository: PaymentRepository
) : SaveReservationPort, SavePaymentPort, LoadReservationPort {

    override fun saveOne(reservation: Reservation): UUID {
        reservationRepository.save(reservation)

        return reservation.id
    }

    override fun saveOne(payment: Payment): UUID {
        paymentRepository.save(payment)

        return payment.id
    }

    override fun loadOneReservationById(reservationId: UUID): Reservation {
        return reservationRepository.findById(reservationId)
            .orElseThrow { throw EntityNotFoundException(NOT_FOUND_RESERVATION_MESSAGE) }
    }
}
