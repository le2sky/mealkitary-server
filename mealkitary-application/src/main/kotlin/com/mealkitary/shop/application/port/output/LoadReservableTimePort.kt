package com.mealkitary.shop.application.port.output

import java.time.LocalTime
import java.util.UUID

interface LoadReservableTimePort {

    fun loadAllReservableTimeByShopId(shopId: UUID): List<LocalTime>
}
