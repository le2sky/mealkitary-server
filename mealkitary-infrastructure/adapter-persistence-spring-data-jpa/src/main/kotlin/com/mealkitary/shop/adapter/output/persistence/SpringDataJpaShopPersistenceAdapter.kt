package com.mealkitary.shop.adapter.output.persistence

import com.mealkitary.shop.application.port.output.LoadProductPort
import com.mealkitary.shop.application.port.output.LoadReservableTimePort
import com.mealkitary.shop.application.port.output.LoadShopPort
import org.springframework.stereotype.Repository

@Repository
class SpringDataJpaShopPersistenceAdapter(
    private val shopRepository: ShopRepository,
) : LoadShopPort, LoadProductPort, LoadReservableTimePort {

    override fun loadAllShop() = shopRepository.findAll()

    override fun loadAllProductByShopId(shopId: Long) = shopRepository.findOneWithProductsById(shopId)
        .products

    override fun loadAllReservableTimeByShopId(shopId: Long) = shopRepository.findOneWithReservableTimesById(shopId)
        .reservableTimes
}
