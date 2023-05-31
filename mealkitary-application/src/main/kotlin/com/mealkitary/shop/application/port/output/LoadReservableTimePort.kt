package com.mealkitary.shop.application.port.output

import java.time.LocalTime

interface LoadReservableTimePort {

    fun loadAllReservableTimeByShopId(shopId: Long): List<LocalTime>
}
