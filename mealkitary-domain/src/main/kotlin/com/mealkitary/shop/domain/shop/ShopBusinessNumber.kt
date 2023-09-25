package com.mealkitary.shop.domain.shop

import javax.persistence.Column
import javax.persistence.Embeddable

// TODO : 매번 REGEX 객체를 생성하는 것이 성능에 얼마나 영향을 미치는지 확인하고 개선
private const val BRN_FORMAT = "([0-9]{3})-?([0-9]{2})-?([0-9]{5})"
private const val INVALID_BRN_FORMAT_MESSAGE = "올바른 사업자번호 형식이 아닙니다."

@Embeddable
class ShopBusinessNumber private constructor(
    @Column(name = "brn", nullable = false)
    val value: String
) {
    companion object {
        fun from(brn: String): ShopBusinessNumber {
            checkIsBrnBlank(brn)
            checkIsBrnFormat(brn)

            return ShopBusinessNumber(brn)
        }

        private fun checkIsBrnBlank(brn: String) {
            if (brn.isBlank()) {
                throw IllegalArgumentException(INVALID_BRN_FORMAT_MESSAGE)
            }
        }

        private fun checkIsBrnFormat(brn: String) {
            if (!brn.matches(getBRNFormatRegex())) {
                throw IllegalArgumentException(INVALID_BRN_FORMAT_MESSAGE)
            }
        }

        private fun getBRNFormatRegex() = Regex(BRN_FORMAT)
    }
}
