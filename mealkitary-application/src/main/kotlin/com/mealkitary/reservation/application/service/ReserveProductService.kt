package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.ReserveProductRequest
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import com.mealkitary.reservation.application.port.output.SaveReservationPort
import com.mealkitary.reservation.domain.reservation.Reservation
import com.mealkitary.shop.application.port.output.LoadShopPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReserveProductService(
    private val loadShopPort: LoadShopPort,
    private val saveReservationPort: SaveReservationPort
) : ReserveProductUseCase {

    @Transactional
    override fun reserve(reserveProductRequest: ReserveProductRequest): UUID {
        val shop = loadShopPort.loadOneShopById(reserveProductRequest.shopId)
        val reservationLineItem = reserveProductRequest.products.map { it.mapToDomainEntity() }
        val reservation = Reservation.of(
            lineItems = reservationLineItem,
            shop = shop,
            reserveAt = reserveProductRequest.reservedAt
        )

        reservation.reserve()

        return saveReservationPort.saveOne(reservation)
    }
}
