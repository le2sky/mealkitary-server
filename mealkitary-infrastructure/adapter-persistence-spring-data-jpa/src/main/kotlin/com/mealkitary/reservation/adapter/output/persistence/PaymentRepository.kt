package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.reservation.domain.payment.Payment
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface PaymentRepository : JpaRepository<Payment, UUID> {

    @EntityGraph(attributePaths = ["reservation"])
    fun findByReservationId(reservationId: UUID): Optional<Payment>
}
