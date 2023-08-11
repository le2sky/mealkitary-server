package com.mealkitary.reservation.adapter.output.persistence

import com.mealkitary.PersistenceIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.reservation.Reservation
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import com.mealkitary.shop.adapter.output.persistence.ShopRepository
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.util.UUID

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

    @Test
    fun `db integration test - 신규 결제를 저장한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .withShop(shopRepository.findOneWithProductsById(1L).orElseThrow())
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        adapterUnderTest.saveOne(reservation)

        val saved = adapterUnderTest.saveOne(payment)
        em.flush()
        em.clear()

        val find = em.find(Payment::class.java, saved)
        saved shouldBe find.id
        find.amount shouldBe Money.from(2000)
        find.paymentKey shouldBe "paymentKey"
    }

    @Test
    fun `db integration test - 예약을 조회한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .withShop(shopRepository.findOneWithProductsById(1L).orElseThrow())
            .build()
        val saved = adapterUnderTest.saveOne(reservation)
        em.flush()
        em.clear()

        val result = adapterUnderTest.loadOneReservationById(saved)

        result.id shouldBe saved
        result.reservationStatus shouldBe ReservationStatus.NOTPAID
    }

    @Test
    fun `db integration test - 예약 ID에 해당하는 예약을 조회할 때, 해당 예약이 존재하지 않으면 예외를 발생한다`() {
        shouldThrow<EntityNotFoundException> {
            adapterUnderTest.loadOneReservationById(UUID.randomUUID())
        } shouldHaveMessage "존재하지 않는 예약입니다."
    }
}
