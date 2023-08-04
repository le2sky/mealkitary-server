package com.mealkitary.common.validation

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class DateValidatorTest : AnnotationSpec() {

    @Test
    fun `날짜가 null이면 검증에 실패한다`() {
        val sut = DateValidator()

        val result = sut.isValid(null, null)

        result.shouldBeFalse()
    }

    @Test
    fun `날짜 형식이 올바르면 검증에 성공한다(ISO-8601)`() {
        val dateString = "2023-12-31T01:30:00"
        val sut = DateValidator()

        val result = sut.isValid(dateString, null)

        result.shouldBeTrue()
    }
}
