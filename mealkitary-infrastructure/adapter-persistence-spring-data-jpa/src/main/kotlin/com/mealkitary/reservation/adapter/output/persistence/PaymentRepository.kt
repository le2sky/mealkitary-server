package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.reservation.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentRepository : JpaRepository<Payment, UUID>
