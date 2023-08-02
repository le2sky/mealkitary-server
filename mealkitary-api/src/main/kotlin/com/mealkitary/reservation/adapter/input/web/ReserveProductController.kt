package com.mealkitary.reservation.adapter.input.web

import com.mealkitary.reservation.application.port.input.ReserveProductRequest
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/reservations")
class ReserveProductController(
    private val reserveProductUseCase: ReserveProductUseCase
) {

    @PostMapping
    fun reserveProduct(@RequestBody reserveProductRequest: ReserveProductRequest): ResponseEntity<Unit> {
        val resourceId = reserveProductUseCase.reserve(reserveProductRequest)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri();

        return ResponseEntity.created(location).build()
    }
}


