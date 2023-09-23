package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.UpdateShopStatusUseCase
import com.mealkitary.shop.application.port.output.ShopPersistencePort
import com.mealkitary.shop.domain.shop.Shop
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateShopStatusService(
    private val shopPersistencePort: ShopPersistencePort,
) : UpdateShopStatusUseCase {

    override fun update(shopId: Long) {
        val shop = findShopById(shopId)

        if (shop.status.isValidStatus()) {
            checkReservationByShopId(shopId)
        }
        updateStatus(shop)
    }

    private fun findShopById(shopId: Long): Shop {
        return shopPersistencePort.loadOneShopById(shopId)
    }

    private fun updateStatus(shop: Shop) {
        if (shop.status.isValidStatus()) {
            shop.changeStatusInvalid()
        } else {
            shop.changeStatusValid()
        }
    }

    private fun checkReservationByShopId(shopId: Long) {
        if (shopPersistencePort.hasReservations(shopId)) {
            throw IllegalStateException("예약이 존재할 경우 가게 상태를 INVALID로 변경할 수 없습니다.")
        }
    }
}
