package com.mealkitary.reservation.application.port.input

interface ReserveProductUseCase {

    fun reserve(reserveProductRequest: ReserveProductRequest): Long
}
