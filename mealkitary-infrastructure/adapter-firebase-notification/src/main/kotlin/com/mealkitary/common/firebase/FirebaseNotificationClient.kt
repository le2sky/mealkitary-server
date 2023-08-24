package com.mealkitary.common.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.mealkitary.common.firebase.message.ReservationAcceptedMessage
import com.mealkitary.common.firebase.message.ReservationCreatedMessage
import org.springframework.stereotype.Component

@Component
class FirebaseNotificationClient {

    fun send(reservationCreatedMessage: ReservationCreatedMessage) {
        val message = Message.builder()
            .putData("title", reservationCreatedMessage.title)
            .putData("description", reservationCreatedMessage.description)
            .putData("reservationId", reservationCreatedMessage.reservationId.toString())
            .setToken(reservationCreatedMessage.token)
            .build()

        sendToFcm(message)
    }

    fun send(reservationAcceptedMessage: ReservationAcceptedMessage) {
        val message = Message.builder()
            .putData("title", reservationAcceptedMessage.title)
            .setToken(reservationAcceptedMessage.token)
            .build()

        sendToFcm(message)
    }

    private fun sendToFcm(message: Message) {
        FirebaseMessaging.getInstance().sendAsync(message)
    }
}
