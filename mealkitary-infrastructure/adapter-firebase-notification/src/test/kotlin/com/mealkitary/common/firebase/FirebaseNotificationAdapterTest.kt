package com.mealkitary.common.firebase

import com.mealkitary.common.firebase.message.ReservationCreatedMessage
import com.mealkitary.common.firebase.message.ReservationStatusChangedMessage
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.util.UUID

class FirebaseNotificationAdapterTest : AnnotationSpec() {

    private val client = mockk<FirebaseNotificationClient>()
    private val adapterUnderTest = FirebaseNotificationAdapter("test-admin-token", "test-client-token", client)

    @Test
    fun `notification adapter unit test - sendNewReservationMessage`() {
        val reservationId = UUID.randomUUID()
        val slot = slot<ReservationCreatedMessage>()
        every { client.send(capture(slot)) } answers {}

        adapterUnderTest.sendNewReservationMessage(reservationId, "부대찌개 외 2건")

        val actual = slot.captured
        actual.token shouldBe "test-admin-token"
        actual.title shouldBe "새로운 예약이 들어왔어요!"
        actual.description shouldBe "부대찌개 외 2건"
        actual.reservationId shouldBe reservationId
    }

    @Test
    fun `notification adapter unit test - sendAcceptedReservationMessage`() {
        val slot = slot<ReservationStatusChangedMessage>()
        every { client.send(capture(slot)) } answers {}

        adapterUnderTest.sendAcceptedReservationMessage()

        val actual = slot.captured
        actual.token shouldBe "test-client-token"
        actual.title shouldBe "예약이 승인됐어요!"
    }

    @Test
    fun `notification adapter unit test - sendRejectedReservationMessage`() {
        val slot = slot<ReservationStatusChangedMessage>()
        every { client.send(capture(slot)) } answers {}

        adapterUnderTest.sendRejectedReservationMessage()

        val actual = slot.captured
        actual.token shouldBe "test-client-token"
        actual.title shouldBe "죄송합니다. 예약하신 가게의 사정으로 인해 예약이 거절됐어요."
    }
}
