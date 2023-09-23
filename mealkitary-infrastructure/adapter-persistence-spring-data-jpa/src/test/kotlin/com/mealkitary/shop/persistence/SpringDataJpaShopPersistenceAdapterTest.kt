package com.mealkitary.shop.persistence

import com.mealkitary.PersistenceIntegrationTestSupport
import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.common.model.Money
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.LocalTime

class SpringDataJpaShopPersistenceAdapterTest(
    private val adapterUnderTest: SpringDataJpaShopPersistenceAdapter,
) : PersistenceIntegrationTestSupport() {

    @Test
    fun `db integration test - 가게에 예약이 존재하는지 확인한다`() {
        val existsReservation = adapterUnderTest.hasReservations(1L)

        existsReservation.shouldBeTrue()
    }

    @Test
    fun `db integration test - 모든 가게를 조회한다`() {
        val shops = adapterUnderTest.loadAllShop()

        shops.size shouldBe 3
        shops.get(0).title shouldBe "집밥뚝딱 철산점"
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 상품 목록을 조회한다`() {
        val products = adapterUnderTest.loadAllProductByShopId(1L)

        products.size shouldBe 3
        products.get(0).name shouldBe "부대찌개"
        products.get(0).price shouldBe Money.from(15800)
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 상품 목록을 조회할 때, 해당 가게가 존재하지 않는 경우 예외를 발생한다`() {
        shouldThrow<EntityNotFoundException> {
            adapterUnderTest.loadAllProductByShopId(99L)
        } shouldHaveMessage "존재하지 않는 가게입니다."
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게를 조회하는데, 프로덕트도 함께 로드된다`() {
        val shop = adapterUnderTest.loadOneShopById(1L)

        emf.persistenceUnitUtil.isLoaded(shop.products).shouldBeTrue()
        shop.title shouldBe "집밥뚝딱 철산점"
        shop.products.size shouldBe 3
        shop.reservableTimes.size shouldBe 4
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게를 조회할 때, 해당 가게가 존재하지 않는 경우 예외를 발생한다`() {
        shouldThrow<EntityNotFoundException> {
            adapterUnderTest.loadOneShopById(99L)
        } shouldHaveMessage "존재하지 않는 가게입니다."
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 모든 예약 가능 시간을 조회한다`() {
        val reservableTimes = adapterUnderTest.loadAllReservableTimeByShopId(1L)

        reservableTimes.size shouldBe 4
        reservableTimes.get(0) shouldBe LocalTime.of(6, 30)
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 모든 예약 가능 시간을 조회할 때, 해당 가게가 존재하지 않는 경우 예외를 발생한다`() {
        shouldThrow<EntityNotFoundException> {
            adapterUnderTest.loadAllReservableTimeByShopId(99L)
        } shouldHaveMessage "존재하지 않는 가게입니다."
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 예약 시간이 없는 경우 빈 배열을 반환한다`() {
        val shop = adapterUnderTest.loadOneShopById(1L)
        shop.reservableTimes.clear()
        em.flush()
        em.clear()

        val reservableTimes = adapterUnderTest.loadAllReservableTimeByShopId(1L)

        reservableTimes.shouldBeEmpty()
    }

    @Test
    fun `db integration test - 가게 ID에 해당하는 가게의 상품이 없는 경우 빈 배열을 반환한다`() {
        em.createQuery("delete from Product p")
            .executeUpdate()

        val products = adapterUnderTest.loadAllProductByShopId(1L)

        products.shouldBeEmpty()
    }

    @Test
    fun `db integration test - 가게가 하나도 존재하지 않는 경우 빈 배열을 반환한다 `() {
        em.createQuery("delete from Product p")
            .executeUpdate()
        em.createNativeQuery("delete from reservable_time")
            .executeUpdate()
        em.createQuery("delete from Shop s")
            .executeUpdate()

        val shops = adapterUnderTest.loadAllShop()

        shops.shouldBeEmpty()
    }
}
