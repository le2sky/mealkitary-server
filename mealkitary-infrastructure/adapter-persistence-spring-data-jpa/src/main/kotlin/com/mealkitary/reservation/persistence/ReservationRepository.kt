package com.mealkitary.reservation.persistence

import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface ReservationRepository : JpaRepository<Reservation, UUID> {

    @EntityGraph(attributePaths = ["shop"])
    fun findOneWithShopById(reservationId: UUID): Optional<Reservation>

    fun existsReservationByShopId(shopId: Long): Boolean
}
