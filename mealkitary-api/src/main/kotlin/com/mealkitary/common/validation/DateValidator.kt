package com.mealkitary.common.validation

import java.time.LocalDateTime
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DateValidator : ConstraintValidator<DateValid, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        try {
            LocalDateTime.parse(value)
        } catch (e: Exception) {
            return false
        }

        return true
    }
}
