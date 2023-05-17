package com.mealkitary.demo.application.service

import com.mealkitary.demo.application.port.`in`.GreetUserQuery
import com.mealkitary.demo.application.port.`in`.GreetUserResponse
import com.mealkitary.demo.application.port.out.LoadUserPort
import org.springframework.stereotype.Service

@Service
internal class GreetUserService(
    private val loadUserPort: LoadUserPort
) : GreetUserQuery {
    override fun execute(userId: Long): GreetUserResponse {
        val user = loadUserPort.loadUserById(1L)

        return GreetUserResponse("hello, ${user.whatIsYourName()}")
    }
}
