package com.mealkitary.shop.web.request

import com.mealkitary.shop.application.port.input.RegisterShopRequest
import javax.validation.constraints.NotBlank

data class RegisterShopWebRequest(
    @field:NotBlank(message = "등록 대상 가게의 이름은 필수입니다.")
    val title: String? = null,

    @field:NotBlank(message = "사업자 번호는 필수입니다.")
    val brn: String? = null,

    @field:NotBlank(message = "주소는 필수입니다.")
    val address: String? = null
) {

    fun mapToServiceRequest() = RegisterShopRequest(title!!, brn!!, address!!)
}
