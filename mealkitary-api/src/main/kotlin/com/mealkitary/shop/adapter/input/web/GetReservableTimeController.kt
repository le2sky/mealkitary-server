package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shops")
class GetReservableTimeController(
    private val getReservableTimeQuery: GetReservableTimeQuery
) {
    @GetMapping("/{shopId}/reservable-time")
    fun getAllReservableTimeOfShop(@PathVariable("shopId") shopId: Long) =
        ResponseEntity.ok(getReservableTimeQuery.loadAllReservableTimeByShopId(shopId))
}
