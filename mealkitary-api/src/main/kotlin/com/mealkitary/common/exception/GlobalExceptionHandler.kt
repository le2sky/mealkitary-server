package com.mealkitary.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)!!

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error("handleMethodArgumentNotValidException", e)

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.badRequest(bindingResult = e.bindingResult))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.error("handleHttpMessageNotReadableException", e)

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.badRequest(message = "JSON 형식이 잘못되었습니다."))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException):
        ResponseEntity<ErrorResponse> {
        logger.error("handleHttpRequestMethodNotSupportedException", e)

        return ResponseEntity(ErrorResponse.methodNotAllowed(), HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.error("handleIllegalArgumentException", e)

        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.badRequest(e.message!!))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorResponse> {
        logger.error("handleIllegalStateException", e)

        return ResponseEntity
            .unprocessableEntity()
            .body(ErrorResponse.unprocessableEntity(e.message!!))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("handleEntityNotFoundException", e)

        return ResponseEntity(ErrorResponse.notFound(e.message!!), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("handleUnexpectedException", e)

        return ResponseEntity
            .internalServerError()
            .body(ErrorResponse.internalServerError())
    }
}
