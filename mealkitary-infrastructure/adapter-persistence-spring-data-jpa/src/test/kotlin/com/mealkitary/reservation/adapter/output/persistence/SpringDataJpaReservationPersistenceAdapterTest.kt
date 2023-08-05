package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.PersistenceIntegrationTestSupport
import com.mealkitary.shop.adapter.output.persistence.ShopRepository
import data.ReservationTestData
import io.kotest.matchers.longs.shouldBeGreaterThan

class SpringDataJpaReservationPersistenceAdapterTest(
    private val adapterUnderTest: SpringDataJpaReservationPersistenceAdapter,
    private val shopRepository: ShopRepository
) : PersistenceIntegrationTestSupport() {

    @Test
    fun `db integration test - 신규 예약을 저장한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withShop(shopRepository.findOneWithProductsById(1L).orElseThrow())
            .build()

        val saved = adapterUnderTest.saveOne(reservation)

        saved shouldBeGreaterThan 0
    }
}
