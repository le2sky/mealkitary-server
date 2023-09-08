package com.mealkitary.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.mealkitary.firebase.message.ReservationCreatedMessage
import com.mealkitary.firebase.message.ReservationStatusChangedMessage
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

    fun send(reservationStatusChangedMessage: ReservationStatusChangedMessage) {
        val message = Message.builder()
            .putData("title", reservationStatusChangedMessage.title)
            .setToken(reservationStatusChangedMessage.token)
            .build()

        sendToFcm(message)
    }

    private fun sendToFcm(message: Message) {
        FirebaseMessaging.getInstance().sendAsync(message)
    }
}
