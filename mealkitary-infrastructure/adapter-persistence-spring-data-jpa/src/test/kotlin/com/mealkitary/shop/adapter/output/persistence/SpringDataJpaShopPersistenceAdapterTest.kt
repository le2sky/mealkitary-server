package com.mealkitary.shop.adapter.output.persistence

import com.mealkitary.common.model.Money
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalTime

@DataJpaTest
@Import(SpringDataJpaShopPersistenceAdapter::class)
class SpringDataJpaShopPersistenceAdapterTest(
    private val adapterUnderTest: SpringDataJpaShopPersistenceAdapter
) : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

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
    fun `db integration test - 가게 ID에 해당하는 가게의 모든 예약 가능 시간을 조회한다`() {
        val reservableTimes = adapterUnderTest.loadAllReservableTimeByShopId(1L)

        reservableTimes.size shouldBe 4
        reservableTimes.get(0) shouldBe LocalTime.of(6, 30)
    }
}
