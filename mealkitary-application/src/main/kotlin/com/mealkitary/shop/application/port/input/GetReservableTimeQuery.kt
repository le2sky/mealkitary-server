package com.mealkitary.shop.application.port.input

import java.time.LocalTime
import java.util.UUID

interface GetReservableTimeQuery {

    fun loadAllReservableTimeByShopId(shopId: UUID): List<LocalTime>
}
