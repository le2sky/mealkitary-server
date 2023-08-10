package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.reservation.application.port.output.SaveReservationPort
import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SpringDataJpaReservationPersistenceAdapter(
    private val reservationRepository: ReservationRepository
) : SaveReservationPort {

    override fun saveOne(reservation: Reservation): UUID {
        reservationRepository.save(reservation)

        return reservation.id
    }
}
