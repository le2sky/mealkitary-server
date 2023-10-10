package com.mealkitary.shop.persistence

import com.mealkitary.shop.domain.shop.Shop
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface ShopRepository : JpaRepository<Shop, Long> {

    @EntityGraph(attributePaths = ["products"])
    fun findOneWithProductsById(shopId: UUID): Optional<Shop>

    @EntityGraph(attributePaths = ["reservableTimes"])
    fun findOneWithReservableTimesById(shopId: UUID): Optional<Shop>
}
