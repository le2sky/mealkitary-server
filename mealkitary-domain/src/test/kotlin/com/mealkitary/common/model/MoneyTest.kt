package com.mealkitary.common.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class MoneyTest : AnnotationSpec() {

    @Test
    fun `돈이 음수라면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            Money.from(-100)
        } shouldHaveMessage "돈은 음수가 될 수 없습니다."
    }

    @Test
    fun `금액이 같다면 동등하다`() {
        val money = Money.from(1000)
        val targetMoney = Money.from(1000)

        (money == targetMoney).shouldBeTrue()
        (money.equals(targetMoney)).shouldBeTrue()
    }

    @Test
    fun `금액이 다르다면 동등하지 않다`() {
        val money = Money.from(1000)
        val targetMoney = Money.from(2000)

        (money == targetMoney).shouldBeFalse()
        (money.equals(targetMoney)).shouldBeFalse()
    }

    @Test
    fun `해시코드 검증`() {
        val money = Money.from(1000)
        val targetMoney = Money.from(1000)
        val set = setOf(money, targetMoney)
        set.size shouldBe 1
    }

    @Test
    fun `금액을 더한다`() {
        val money = Money.from(1000)
        val targetMoney = Money.from(200)
        (money + targetMoney).value shouldBe 1200
    }

    @Test
    fun `금액을 곱한다`() {
        (Money.from(1000) * 10).value shouldBe 10000
    }

    @Test
    fun `더하기 연산은 불변을 보장한다`() {
        val money = Money.from(1000)
        val sut = money + Money.from(10000)

        (sut === money).shouldBeFalse()
    }

    @Test
    fun `곱하기 연산은 불변을 보장한다`() {
        val money = Money.from(1000)
        val sut = money * 2

        (sut === money).shouldBeFalse()
    }

    @Test
    fun `더 작거나 같은 값인지 확인한다`() {
        val money = Money.from(1000)
        val target1 = Money.from(1000)
        val target2 = Money.from(2000)
        val target3 = Money.from(999)

        money.lessThanEqual(target1).shouldBeTrue()
        money.lessThanEqual(target2).shouldBeTrue()
        money.lessThanEqual(target3).shouldBeFalse()
    }
}
