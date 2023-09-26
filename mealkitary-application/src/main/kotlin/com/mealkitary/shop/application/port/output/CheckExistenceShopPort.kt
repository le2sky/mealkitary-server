package com.mealkitary.shop.application.port.output

interface CheckExistenceShopPort {

    fun hasReservations(shopId: Long): Boolean
}
