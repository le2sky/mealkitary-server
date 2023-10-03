package com.mealkitary.shop.domain.shop

import com.mealkitary.shop.domain.shop.address.Coordinates
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class CoordinatesTest : AnnotationSpec() {

    @Test
    fun `범위를 벗어나는 좌표일 경우 예외를 발생한다`() {
        val longitude = -188.023
        val latitude = 999.7412

        shouldThrow<IllegalArgumentException> {
            Coordinates.of(longitude, latitude)
        } shouldHaveMessage "유효하지 않은 좌표 범위입니다."
    }

    @Test
    fun `올바른 좌표를 입력했을 경우 객체를 생성한다`() {
        val longitude = -150.653
        val latitude = 46.492

        val coordinates = Coordinates.of(longitude, latitude)

        coordinates.longitude shouldBe longitude
        coordinates.latitude shouldBe latitude
    }
}
