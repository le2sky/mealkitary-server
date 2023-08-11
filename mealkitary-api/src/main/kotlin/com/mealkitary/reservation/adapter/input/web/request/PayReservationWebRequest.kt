package com.mealkitary.reservation.adapter.input.web.request

import com.mealkitary.reservation.application.port.input.PayReservationRequest
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PayReservationWebRequest(
    @field:NotBlank(message = "결제 식별자는 필수입니다.")
    val paymentKey: String? = null,

    @field:NotNull(message = "결제 금액은 필수입니다.")
    val amount: Int? = null,
) {

    fun mapToServiceRequest(reservationId: UUID): PayReservationRequest {
        return PayReservationRequest(
            paymentKey!!,
            reservationId,
            amount!!
        )
    }
}
