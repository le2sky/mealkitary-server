package com.mealkitary.shop.application.service

import com.mealkitary.shop.application.port.input.RegisterShopRequest
import com.mealkitary.shop.application.port.output.SaveShopPort
import com.mealkitary.shop.domain.shop.Shop
import com.mealkitary.shop.domain.shop.factory.ShopBusinessNumberValidator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class RegisterShopServiceTest : AnnotationSpec() {

    private val saveShopPort = mockk<SaveShopPort>()
    private val shopBusinessNumberValidator = mockk<ShopBusinessNumberValidator>()
    private val registerShopService = RegisterShopService(saveShopPort, shopBusinessNumberValidator)

    @Test
    fun `service unit test - 신규 가게를 등록한다`() {
        val shopSlot = slot<Shop>()
        val request = RegisterShopRequest("집밥뚝딱 안양점", "123-23-12345")
        every { saveShopPort.saveOne(capture(shopSlot)) } answers { 1L }
        every { shopBusinessNumberValidator.validate(any()) } answers {}

        val result = registerShopService.register(request)

        val capturedShop = shopSlot.captured
        result shouldBe 1L
        capturedShop.businessNumber.value shouldBe "123-23-12345"
        capturedShop.title.value shouldBe "집밥뚝딱 안양점"
        capturedShop.products.shouldBeEmpty()
        capturedShop.reservableTimes.shouldBeEmpty()
    }

    @Test
    fun `service unit test - 가게 이름 형식에 맞지 않으면 예외를 발생한다`() {
        val request = RegisterShopRequest("invalid!#@", "123-23-12345")
        every { saveShopPort.saveOne(any()) } answers { 1L }
        every { shopBusinessNumberValidator.validate(any()) } answers {}

        shouldThrow<IllegalArgumentException> {
            registerShopService.register(request)
        } shouldHaveMessage "올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)"
    }

    @Test
    fun `service unit test - 사업자 번호 형식에 맞지 않으면 예외를 발생한다`() {
        val request = RegisterShopRequest("집밥뚝딱 안양점", "invalid-brn")
        every { saveShopPort.saveOne(any()) } answers { 1L }
        every { shopBusinessNumberValidator.validate(any()) } answers {}

        shouldThrow<IllegalArgumentException> {
            registerShopService.register(request)
        } shouldHaveMessage "올바른 사업자번호 형식이 아닙니다."
    }
}
