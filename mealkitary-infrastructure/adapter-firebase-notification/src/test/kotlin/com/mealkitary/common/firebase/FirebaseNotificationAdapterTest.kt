package com.mealkitary.common.firebase

import com.mealkitary.common.firebase.message.ReservationCreatedMessage
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.util.UUID

class FirebaseNotificationAdapterTest : AnnotationSpec() {

    @Test
    fun `notification adapter unit test - sendNewReservationMessage`() {
        val client = mockk<FirebaseNotificationClient>()
        val adapterUnderTest = FirebaseNotificationAdapter("test-token", client)
        val reservationId = UUID.randomUUID()
        val slot = slot<ReservationCreatedMessage>()
        every { client.send(capture(slot)) } answers {}

        adapterUnderTest.sendNewReservationMessage(reservationId, "부대찌개 외 2건")

        val actual = slot.captured
        actual.token shouldBe "test-token"
        actual.title shouldBe "새로운 예약이 들어왔어요!"
        actual.description shouldBe "부대찌개 외 2건"
        actual.reservationId shouldBe reservationId
    }
}
