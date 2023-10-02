package com.mealkitary.brn

import com.mealkitary.brn.payload.OpenApiBrnStatusPayload
import com.mealkitary.brn.payload.OpenApiBrnStatusResponse
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import com.mealkitary.shop.domain.shop.factory.ShopBusinessNumberValidator
import org.springframework.stereotype.Component

@Component
class OpenApiBrnValidator(
    private val openApiWebClient: OpenApiWebClient
) : ShopBusinessNumberValidator {

    override fun validate(brn: ShopBusinessNumber) {
        val brnValue = removeDelimiter(brn.value)

        val openApiStatusResponse = openApiWebClient.requestStatus(
            OpenApiBrnStatusPayload(listOf(brnValue)),
            "https://api.odcloud.kr/api/nts-businessman"
        )

        checkHasResult(openApiStatusResponse)
        checkBrnStatus(openApiStatusResponse)
    }

    private fun removeDelimiter(brn: String) = brn.replace("-", "")

    private fun checkHasResult(openApiBrnStatusResponse: OpenApiBrnStatusResponse) {
        if (openApiBrnStatusResponse.data.isEmpty()) {
            throw IllegalArgumentException("사업자 번호 조회 결과가 없습니다.")
        }
    }

    private fun checkBrnStatus(openApiBrnStatusResponse: OpenApiBrnStatusResponse) {
        val result = openApiBrnStatusResponse.data[0]
        if (result.b_stt != "계속사업자") {
            throw IllegalArgumentException("유효하지 않은 사업자 번호입니다.")
        }
    }
}
