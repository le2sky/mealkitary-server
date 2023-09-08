package com.mealkitary.firebase

import com.mealkitary.firebase.message.ReservationCreatedMessage
import com.mealkitary.firebase.message.ReservationStatusChangedMessage
import com.mealkitary.reservation.application.port.output.SendAcceptedReservationMessagePort
import com.mealkitary.reservation.application.port.output.SendNewReservationMessagePort
import com.mealkitary.reservation.application.port.output.SendRejectedReservationMessagePort
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
) : SendNewReservationMessagePort, SendAcceptedReservationMessagePort, SendRejectedReservationMessagePort {

    override fun sendNewReservationMessage(reservationId: UUID, description: String) {
        client.send(ReservationCreatedMessage("새로운 예약이 들어왔어요!", description, reservationId, adminToken))
    }

    override fun sendAcceptedReservationMessage() {
        client.send(ReservationStatusChangedMessage("예약이 승인됐어요!", clientToken))
    }

    override fun sendRejectedReservationMessage() {
        client.send(ReservationStatusChangedMessage("죄송합니다. 예약하신 가게의 사정으로 인해 예약이 거절됐어요.", clientToken))
    }
}
