package com.mealkitary.shop.persistence

import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.shop.application.port.output.LoadProductPort
import com.mealkitary.shop.application.port.output.LoadReservableTimePort
import com.mealkitary.shop.application.port.output.LoadShopPort
import com.mealkitary.shop.application.port.output.SaveShopPort
import com.mealkitary.shop.domain.shop.Shop
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.function.Function

private const val NOT_FOUND_SHOP_MESSAGE = "존재하지 않는 가게입니다."

@Repository
class SpringDataJpaShopPersistenceAdapter(
    private val shopRepository: ShopRepository,
) : SaveShopPort, LoadShopPort, LoadProductPort, LoadReservableTimePort {

    override fun saveOne(shop: Shop): Long {
        shopRepository.save(shop)

        return shop.id!!
    }

    override fun loadAllShop(): List<Shop> = shopRepository.findAll()

    override fun loadOneShopById(shopId: Long) = getShopOrThrow(shopRepository::findOneWithProductsById, shopId)

    override fun loadAllProductByShopId(shopId: Long) =
        getShopOrThrow(shopRepository::findOneWithProductsById, shopId).products

    override fun loadAllReservableTimeByShopId(shopId: Long) =
        getShopOrThrow(shopRepository::findOneWithReservableTimesById, shopId).reservableTimes

    private fun getShopOrThrow(queryMethod: Function<Long, Optional<Shop>>, shopId: Long): Shop {
        return (queryMethod.apply(shopId).orElseThrow { throw EntityNotFoundException(NOT_FOUND_SHOP_MESSAGE) })
    }
}
