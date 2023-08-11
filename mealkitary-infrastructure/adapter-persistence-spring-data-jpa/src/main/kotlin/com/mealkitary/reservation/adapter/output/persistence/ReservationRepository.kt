package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReservationRepository : JpaRepository<Reservation, UUID>
