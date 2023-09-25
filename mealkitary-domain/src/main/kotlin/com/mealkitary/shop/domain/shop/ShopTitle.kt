package com.mealkitary.shop.domain.shop

import javax.persistence.Column
import javax.persistence.Embeddable

private const val MAX_TITLE_LENGTH = 50
private const val TITLE_FORMAT = "^[a-zA-Z0-9가-힣\\s]*$"

@Embeddable
class ShopTitle(
    @Column(name = "title", nullable = false)
    val value: String
) {
    companion object {
        fun from(title: String): ShopTitle {
            checkIsTitleFormat(title)
            checkIsTitleBlank(title)
            checkTitleLength(title)

            return ShopTitle(title)
        }

        private fun checkIsTitleFormat(title: String) {
            if (!title.matches(getTitleFormatRegex())) {
                throw IllegalArgumentException("올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)")
            }
        }

        private fun checkTitleLength(title: String) {
            if (title.length > MAX_TITLE_LENGTH) {
                throw IllegalArgumentException("가게의 이름은 최대 50글자입니다.")
            }
        }

        private fun checkIsTitleBlank(title: String) {
            if (title.isBlank()) {
                throw IllegalArgumentException("가게 이름을 입력해주세요.")
            }
        }

        private fun getTitleFormatRegex() = Regex(TITLE_FORMAT)
    }
}
