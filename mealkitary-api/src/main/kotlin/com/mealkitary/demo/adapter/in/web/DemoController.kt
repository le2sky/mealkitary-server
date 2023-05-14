package com.mealkitary.demo.adapter.`in`.web

import com.mealkitary.demo.adapter.out.persistence.UserRepository
import com.mealkitary.demo.application.port.`in`.GreetUserQuery
import com.mealkitary.demo.application.port.`in`.GreetUserResponse
import com.mealkitary.demo.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
internal class DemoController(
    val greetUserQuery: GreetUserQuery
) {

    @GetMapping("/demo/greet/{userId}")
    fun greet(@PathVariable("userId") userId: Long): ResponseEntity<GreetUserResponse> {
        return ResponseEntity.ok(greetUserQuery.execute(userId))
    }
}