package com.mealkitary.demo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String
) {

    fun whatIsYourName(): String {
        return name
    }
}
