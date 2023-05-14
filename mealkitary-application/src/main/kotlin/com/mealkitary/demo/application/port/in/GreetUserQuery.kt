package com.mealkitary.demo.application.port.`in`

interface GreetUserQuery {
    fun execute(userId: Long): GreetUserResponse
}