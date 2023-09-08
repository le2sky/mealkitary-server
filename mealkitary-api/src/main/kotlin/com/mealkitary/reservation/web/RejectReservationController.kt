package com.mealkitary.reservation.web

import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.reservation.application.port.input.RejectReservationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class RejectReservationController(
    private val rejectReservationUseCase: RejectReservationUseCase
) {

    @PostMapping("/{reservationId}/reject")
    fun rejectReservation(@PathVariable("reservationId") reservationId: String): ResponseEntity<Unit> {
        rejectReservationUseCase.reject(UUIDUtils.fromString(reservationId))

        return ResponseEntity.noContent().build()
    }
}
