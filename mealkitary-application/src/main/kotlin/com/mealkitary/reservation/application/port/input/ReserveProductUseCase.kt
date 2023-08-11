package com.mealkitary.reservation.application.port.input

import java.util.UUID

interface ReserveProductUseCase {

    fun reserve(reserveProductRequest: ReserveProductRequest): UUID
}
