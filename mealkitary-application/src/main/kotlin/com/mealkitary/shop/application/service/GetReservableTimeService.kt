package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import com.mealkitary.shop.application.port.output.LoadReservableTimePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetReservableTimeService(
    private val loadReservableTimePort: LoadReservableTimePort
) : GetReservableTimeQuery {

    override fun loadAllReservableTimeByShopId(shopId: Long) =
        loadReservableTimePort.loadAllReservableTimeByShopId(shopId)
}
