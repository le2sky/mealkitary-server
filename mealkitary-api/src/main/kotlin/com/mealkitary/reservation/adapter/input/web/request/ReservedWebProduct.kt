package com.mealkitary.reservation.adapter.input.web.request

import com.mealkitary.reservation.application.port.input.ReservedProduct
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ReservedWebProduct(
    @field:NotNull(message = "예약 대상 상품의 식별자는 필수입니다.")
    val productId: Long? = null,

    @field:NotBlank(message = "예약 대상 상품의 이름은 필수입니다.")
    val name: String? = null,

    @field:NotNull(message = "예약 대상 상품의 가격은 필수입니다.")
    val price: Int? = null,

    @field:NotNull(message = "예약 수량은 필수입니다.")
    val count: Int? = null
) {

    fun mapToServiceObject() = ReservedProduct(
        productId!!,
        name!!,
        price!!,
        count!!
    )
}
