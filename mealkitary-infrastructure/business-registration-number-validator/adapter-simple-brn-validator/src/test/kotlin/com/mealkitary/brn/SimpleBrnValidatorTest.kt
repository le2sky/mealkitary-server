package com.mealkitary.brn

import io.kotest.core.spec.style.AnnotationSpec

class SimpleBrnValidatorTest : AnnotationSpec() {

    @Test
    fun `adapter unit test - 심플 BRN 검사기는 모든 BRN을 통과 시킨다`() {
        val adapterUnderTest = SimpleBrnValidator()

        adapterUnderTest.validate("any")
    }
}
