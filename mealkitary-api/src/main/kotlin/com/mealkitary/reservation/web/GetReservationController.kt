package com.mealkitary.reservation.web

import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.reservation.application.port.input.GetReservationQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class GetReservationController(
    private val getReservationQuery: GetReservationQuery
) {

    @GetMapping("/{reservationId}")
    fun getOneReservation(@PathVariable("reservationId") reservationId: String) =
        getReservationQuery.loadOneReservationById(UUIDUtils.fromString(reservationId))
}
