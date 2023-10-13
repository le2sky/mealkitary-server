package com.mealkitary.shop.web

import com.mealkitary.common.utils.HttpResponseUtils
import com.mealkitary.common.utils.UUIDUtils
import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
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
    fun getAllReservableTimeOfShop(@PathVariable("shopId") shopId: String) =
        HttpResponseUtils.mapToResponseEntity(
            emptiableList = getReservableTimeQuery.loadAllReservableTimeByShopId(UUIDUtils.fromString(shopId))
        )
}
