package com.mealkitary.demo.application.port.out

import com.mealkitary.demo.domain.User

interface LoadUserPort {

    fun loadUserById(userId: Long): User
}