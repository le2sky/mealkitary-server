package com.mealkitary.reservation.adapter.output.persistence

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(SpringDataJpaReservationPersistenceAdapter::class)
class SpringDataJpaReservationPersistenceAdapterTest(
    private val adapterUnderTest: SpringDataJpaReservationPersistenceAdapter
) : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Test
    fun `db integration test - 신규 예약을 저장한다`() {
    }
}
