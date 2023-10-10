package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.GetReservationQuery
import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class GetReservationService(
    private val loadReservationPort: LoadReservationPort
) : GetReservationQuery {

    override fun loadOneReservationById(reservationId: UUID) =
        loadReservationPort.queryOneReservationById(reservationId)

    override fun loadAllReservationByShopId(shopId: UUID): List<ReservationResponse> =
        loadReservationPort.queryAllReservationByShopId(shopId)
}
