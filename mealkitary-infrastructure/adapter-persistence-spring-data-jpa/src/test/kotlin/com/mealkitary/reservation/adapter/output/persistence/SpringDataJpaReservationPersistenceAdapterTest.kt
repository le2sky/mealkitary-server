package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.PersistenceIntegrationTestSupport

class SpringDataJpaReservationPersistenceAdapterTest(
    private val adapterUnderTest: SpringDataJpaReservationPersistenceAdapter
) : PersistenceIntegrationTestSupport() {

    @Test
    fun `db integration test - 신규 예약을 저장한다`() {
    }
}
