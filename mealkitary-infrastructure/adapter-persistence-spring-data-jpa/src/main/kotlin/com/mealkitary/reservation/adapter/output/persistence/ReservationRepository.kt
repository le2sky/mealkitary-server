package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long>
