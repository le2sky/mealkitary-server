package com.mealkitary.demo.adapter.out.persistence

import com.mealkitary.demo.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}
