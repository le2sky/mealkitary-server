package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.reservation.adapter.input.web.request.PayReservationWebRequest
import com.mealkitary.reservation.application.port.input.PayReservationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/reservations")
class PayReservationController(
    private val payReservationUseCase: PayReservationUseCase
) {

    @PostMapping("/{reservationId}/pay")
    fun payReservation(
        @PathVariable("reservationId") reservationId: String,
        @Valid @RequestBody payReservationWebRequest: PayReservationWebRequest
    ): ResponseEntity<Unit> {
        val serviceRequest = payReservationWebRequest.mapToServiceRequest(UUIDUtils.fromString(reservationId))
        val resourceId = payReservationUseCase.pay(serviceRequest)
        val location = HttpResponseUtils.createResourceUri("payments", resourceId)

        return ResponseEntity.created(location).build()
    }
}
