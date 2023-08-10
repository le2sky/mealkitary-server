package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.PersistenceIntegrationTestSupport
import com.mealkitary.reservation.domain.reservation.Reservation
import com.mealkitary.shop.adapter.output.persistence.ShopRepository
import data.ReservationTestData
import io.kotest.matchers.shouldBe

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
        em.flush()
        em.clear()

        val find = em.find(Reservation::class.java, saved)
        saved shouldBe find.id
    }
}
