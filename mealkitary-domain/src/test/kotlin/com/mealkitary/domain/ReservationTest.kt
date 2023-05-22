package com.mealkitary.domain

import com.mealkitary.domain.reservation.Reservation
import com.mealkitary.domain.reservation.ReservationLineItem
import com.mealkitary.domain.reservation.Shop
import io.kotest.core.spec.style.AnnotationSpec
import java.time.LocalDateTime

internal class ReservationTest : AnnotationSpec() {

    @Test
    fun `예약 생성 테스트`() {
        Reservation.of(listOf(ReservationLineItem()), Shop(), LocalDateTime.now())
    }
}
