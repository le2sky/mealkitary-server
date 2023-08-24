package com.mealkitary.common.firebase

import com.mealkitary.common.firebase.message.ReservationCreatedMessage
import com.mealkitary.reservation.application.port.output.SendNewReservationMessagePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FirebaseNotificationAdapter(
    @Value("\${admin.fcm.token}")
    private val token: String,
    private val client: FirebaseNotificationClient
) : SendNewReservationMessagePort {

    override fun sendNewReservationMessage(reservationId: UUID, description: String) {
        client.send(ReservationCreatedMessage("새로운 예약이 들어왔어요!", description, reservationId, token))
    }
}
