package com.mealkitary.domain

import com.mealkitary.domain.reservation.Reservation
import com.mealkitary.domain.reservation.ReservationLineItem
import com.mealkitary.domain.reservation.Shop
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.LocalDateTime

internal class ReservationTest : AnnotationSpec() {

    @Test
    fun `예약 생성 테스트`() {
        Reservation.of(listOf(ReservationLineItem()), Shop(), LocalDateTime.now())
    }

    @Test
    fun `예약 상품이 하나도 존재하지 않는다면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            Reservation.of(emptyList(), Shop(), LocalDateTime.now())
        } shouldHaveMessage "예약 상품은 적어도 한 개 이상이어야 합니다."
    }

    @Test
    fun `예약 시간이 현재 시간보다 과거라면 예외를 발생한다`() {
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusDays(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusHours(1))
        shouldThrowWhenAcceptBeforeReservationTime(LocalDateTime.now().minusYears(1))
    }

    private fun shouldThrowWhenAcceptBeforeReservationTime(beforeOneDays: LocalDateTime) {
        shouldThrow<IllegalStateException> {
            Reservation.of(listOf(ReservationLineItem()), Shop(), beforeOneDays)
        } shouldHaveMessage "예약 시간은 적어도 미래 시점이어야 합니다."
    }
}
