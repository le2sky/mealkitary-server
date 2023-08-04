package com.mealkitary.common.exception

import org.springframework.validation.BindingResult

data class FieldError(
    val field: String,
    val value: String,
    val reason: String
) {

    companion object {

        fun of(bindingResult: BindingResult) =
            bindingResult.fieldErrors.map {
                FieldError(it.field, (it.rejectedValue ?: "").toString(), it.defaultMessage!!)
            }
    }
}
