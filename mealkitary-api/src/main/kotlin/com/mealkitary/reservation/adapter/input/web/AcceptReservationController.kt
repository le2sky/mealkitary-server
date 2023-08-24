package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.reservation.application.port.input.AcceptReservationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class AcceptReservationController(
    private val acceptReservationUseCase: AcceptReservationUseCase
) {

    @PostMapping("/{reservationId}/accept")
    fun acceptReservation(@PathVariable("reservationId") reservationId: String): ResponseEntity<Unit> {
        acceptReservationUseCase.accept(UUIDUtils.fromString(reservationId))

        return ResponseEntity.noContent().build()
    }
}
