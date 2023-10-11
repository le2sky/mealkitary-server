package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.UpdateShopStatusUseCase
import com.mealkitary.shop.application.port.output.CheckExistenceShopPort
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.domain.shop.Shop
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateShopStatusService(
    private val checkExistenceShopPort: CheckExistenceShopPort,
    private val loadShopPort: LoadShopPort
) : UpdateShopStatusUseCase {

    override fun update(shopId: UUID) {
        val shop = loadShopPort.loadOneShopById(shopId)

        if (shop.status.isValidStatus()) {
            checkReservationByShopId(shopId)
        }
        updateStatus(shop)
    }

    private fun checkReservationByShopId(shopId: UUID) {
        if (checkExistenceShopPort.hasReservations(shopId)) {
            throw IllegalStateException("예약이 존재할 경우 가게 상태를 INVALID로 변경할 수 없습니다.")
        }
    }

    private fun updateStatus(shop: Shop) {
        if (shop.status.isValidStatus()) {
            shop.changeStatusInvalid()
        } else {
            shop.changeStatusValid()
        }
    }
}
