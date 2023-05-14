package com.mealkitary.demo.adapter.out.persistence

import com.mealkitary.demo.application.port.out.LoadUserPort
import com.mealkitary.demo.domain.User
import org.springframework.stereotype.Repository

@Repository
internal class UserPersistenceAdapter(
    private val userRepository: UserRepository
) : LoadUserPort {

    override fun loadUserById(userId: Long): User {
        val user = userRepository.findById(userId)
        if (user.isPresent) return user.get()
        val newUser = User(name = "leesky")
        userRepository.save(newUser)
        return newUser
    }
}