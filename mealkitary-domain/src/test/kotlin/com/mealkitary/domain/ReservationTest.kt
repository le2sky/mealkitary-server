package com.mealkitary.domain

import com.mealkitary.domain.reservation.Reservation
import com.mealkitary.domain.reservation.ReservationLineItem
import com.mealkitary.domain.reservation.Shop
import com.mealkitary.domain.reservation.ShopStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class ReservationTest : AnnotationSpec() {

    @Test
    fun `예약 생성 테스트`() {
        Reservation.of(
            listOf(ReservationLineItem()),
            Shop(ShopStatus.VALID, emptyList()),
            LocalDateTime.now().plusDays(1)
        )
    }

    @Test
    fun `예약 상품이 하나도 존재하지 않는다면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            Reservation.of(
                emptyList(),
                Shop(ShopStatus.VALID, emptyList()),
                LocalDateTime.now().plusDays(1)
            )
        } shouldHaveMessage "예약 상품은 적어도 한 개 이상이어야 합니다."
    }

    @Test
    fun `예약 시간이 현재 시간보다 과거라면 예외를 발생한다`() {
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusDays(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusHours(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusYears(1))
    }

    @Test
    fun `예약 하려는 가게가 제공하는 예약 시간이 아니라면 예외를 발생한다`() {
        val shop = Shop(
            ShopStatus.VALID,
            listOf(
                LocalTime.of(6, 30),
                LocalTime.of(9, 30),
                LocalTime.of(12, 30)
            )
        )
        shouldThrow<IllegalArgumentException> {
            val sut = Reservation.of(
                listOf(ReservationLineItem()),
                shop,
                LocalDateTime.of(
                    LocalDate.now().plusDays(1),
                    LocalTime.of(18, 30)
                )
            )
            sut.reserve()
        } shouldHaveMessage "예약하시려는 가게는 해당 시간에 예약을 받지 않습니다."
    }

    @Test
    fun `예약하려는 가게가 제공하는 예약 시간을 포함한다면 성공적으로 예약이 된다`() {
        val shop = Shop(
            ShopStatus.VALID,
            listOf(
                LocalTime.of(6, 30),
                LocalTime.of(9, 30),
                LocalTime.of(12, 30)
            )
        )
        val sut = Reservation.of(
            listOf(ReservationLineItem()),
            shop,
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 30)
            )
        )
        sut.reserve()
    }

    @Test
    fun `예약 하려는 가게가 정상 영업 중이 아니라면 예외를 발생한다`() {
        val invalidShop = Shop(ShopStatus.INVALID, emptyList())
        shouldThrow<IllegalArgumentException> {
            val sut = Reservation.of(
                listOf(ReservationLineItem()),
                invalidShop,
                LocalDateTime.now().plusDays(1)
            )
            sut.reserve()
        } shouldHaveMessage "유효하지 않은 가게입니다."
    }

    private fun shouldThrowWhenAcceptBeforeReservationTime(beforeOneDays: LocalDateTime) {
        shouldThrow<IllegalStateException> {
            Reservation.of(
                listOf(ReservationLineItem()),
                Shop(ShopStatus.VALID, emptyList()),
                beforeOneDays
            )
        } shouldHaveMessage "예약 시간은 적어도 미래 시점이어야 합니다."
    }
}
