package com.mealkitary.common.constants.domain

internal object ShopConstants {

    object Validation {
        enum class ErrorMessage(val message: String) {
            INVALID_SHOP("유효하지 않은 가게입니다.")
        }
    }
}
