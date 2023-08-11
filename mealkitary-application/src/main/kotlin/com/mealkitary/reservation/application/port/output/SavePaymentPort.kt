package com.mealkitary.reservation.application.port.output

import com.mealkitary.reservation.domain.payment.Payment
import java.util.UUID

interface SavePaymentPort {

    fun saveOne(payment: Payment): UUID
}
