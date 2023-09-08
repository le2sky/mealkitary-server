package com.mealkitary.reservation.web.request

import com.mealkitary.common.validation.DateValid
import com.mealkitary.reservation.application.port.input.ReserveProductRequest
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class ReserveProductWebRequest(
    @field:NotNull(message = "예약 대상 가게 식별자는 필수입니다.")
    val shopId: Long? = null,

    @field:Valid
    @field:NotNull(message = "예약 상품 목록은 필수입니다.")
    val products: List<ReservedWebProduct>? = null,

    @field:DateValid
    @field:NotNull(message = "예약 시간은 필수입니다.")
    val reservedAt: String? = null
) {

    fun mapToServiceRequest(): ReserveProductRequest {
        return ReserveProductRequest(
            shopId!!,
            products!!.map { it.mapToServiceObject() },
            LocalDateTime.parse(reservedAt!!)
        )
    }
}
