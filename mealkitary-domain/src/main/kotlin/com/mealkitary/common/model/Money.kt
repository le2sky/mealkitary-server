package com.mealkitary.common.model

data class Money private constructor(val value: Int) {

    operator fun times(target: Int): Money {
        return of(value * target)
    }

    operator fun plus(target: Money): Money {
        return of(value + target.value)
    }

    companion object {
        fun of(value: Int): Money {
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

