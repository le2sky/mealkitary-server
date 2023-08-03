package com.mealkitary.common.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.validation.BindingResult

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: List<FieldError>
) {
    companion object {

        fun badRequest(bindingResult: BindingResult) =
            ErrorResponse(BAD_REQUEST.value(), "잘못된 입력값입니다.", FieldError.of(bindingResult))

        fun badRequest(message: String) = ErrorResponse(BAD_REQUEST.value(), message, emptyList())

        fun unprocessableEntity(message: String) = ErrorResponse(UNPROCESSABLE_ENTITY.value(), message, emptyList())

        fun methodNotAllowed() =
            ErrorResponse(METHOD_NOT_ALLOWED.value(), "지원하지 않는 메서드입니다.", emptyList())

        fun internalServerError() =
            ErrorResponse(INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러입니다. 관리자에게 문의하세요.", emptyList())
    }
}
