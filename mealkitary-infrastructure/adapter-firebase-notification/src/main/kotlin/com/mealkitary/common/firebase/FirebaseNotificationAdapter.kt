package com.mealkitary.common.firebase

import com.mealkitary.common.firebase.message.ReservationAcceptedMessage
import com.mealkitary.common.firebase.message.ReservationCreatedMessage
import com.mealkitary.reservation.application.port.output.SendAcceptedReservationMessagePort
import com.mealkitary.reservation.application.port.output.SendNewReservationMessagePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FirebaseNotificationAdapter(
    @Value("\${admin.fcm.token}")
    private val adminToken: String,
    @Value("\${client.fcm.token}")
    private val clientToken: String,
    private val client: FirebaseNotificationClient
) : SendNewReservationMessagePort, SendAcceptedReservationMessagePort {

    override fun sendNewReservationMessage(reservationId: UUID, description: String) {
        client.send(ReservationCreatedMessage("새로운 예약이 들어왔어요!", description, reservationId, adminToken))
    }

    override fun sendAcceptedReservationMessage() {
        client.send(ReservationAcceptedMessage("예약이 승인됐어요!", clientToken))
    }
}
