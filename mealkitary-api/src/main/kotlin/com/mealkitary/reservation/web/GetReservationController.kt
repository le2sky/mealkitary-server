package com.mealkitary.reservation.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.reservation.application.port.input.GetReservationQuery
import com.mealkitary.reservation.application.port.input.ReservationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class GetReservationController(
    private val getReservationQuery: GetReservationQuery
) {

    @GetMapping("/{reservationId}")
    fun getOneReservation(@PathVariable("reservationId") reservationId: String) =
        getReservationQuery.loadOneReservationById(UUIDUtils.fromString(reservationId))

    @GetMapping
    fun getAllReservation(@RequestParam("shopId") shopIdParam: Long?): ResponseEntity<List<ReservationResponse>> {
        val shopId = shopIdParam ?: throw IllegalArgumentException("가게 식별자는 필수입니다.")

        return HttpResponseUtils
            .mapToResponseEntity(emptiableList = getReservationQuery.loadAllReservationByShopId(shopId))
    }
}
