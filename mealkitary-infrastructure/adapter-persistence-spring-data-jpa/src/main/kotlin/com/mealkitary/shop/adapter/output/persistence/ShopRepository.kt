package com.mealkitary.shop.adapter.output.persistence

import com.mealkitary.shop.domain.shop.Shop
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ShopRepository : JpaRepository<Shop, Long> {

    @EntityGraph(attributePaths = ["products"])
    fun findOneWithProductsById(shopId: Long): Shop

    @EntityGraph(attributePaths = ["reservableTimes"])
    fun findOneWithReservableTimesById(shopId: Long): Shop
}
