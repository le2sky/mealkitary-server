package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.reservation.adapter.input.web.request.ReserveProductWebRequest
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/reservations")
class ReserveProductController(
    private val reserveProductUseCase: ReserveProductUseCase
) {

    @PostMapping
    fun reserveProduct(@Valid @RequestBody reserveProductWebRequest: ReserveProductWebRequest): ResponseEntity<Unit> {
        val resourceId = reserveProductUseCase.reserve(reserveProductWebRequest.mapToServiceRequest())
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri()

        return ResponseEntity.created(location).build()
    }
}
