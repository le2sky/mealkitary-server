package com.mealkitary.common.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Money private constructor(
    @Column(name = "price")
    val value: Int
) {

    operator fun times(target: Int): Money {
        return from(value * target)
    }

    operator fun plus(target: Money): Money {
        return from(value + target.value)
    }

    companion object {
        fun from(value: Int): Money {
            checkValue(value)

            return Money(value)
        }

        private fun checkValue(value: Int) {
            if (value < 0) {
                throw IllegalArgumentException("돈은 음수가 될 수 없습니다.")
            }
        }
    }
}
