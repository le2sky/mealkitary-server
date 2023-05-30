package com.mealkitary.shop.application.port.input

import java.time.LocalTime

interface GetReservableTimeQuery {
    fun loadAllReservableTimeByShopId(shopId: Long): List<LocalTime>
}
