package com.mealkitary.shop.domain.shop.factory

import com.mealkitary.common.model.Address
import com.mealkitary.common.model.Coordinates
import com.mealkitary.shop.domain.shop.address.ShopAddress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk

class ShopFactoryTest : AnnotationSpec() {

    private val shopBusinessNumberValidator = mockk<ShopBusinessNumberValidator>()
    private val addressResolver = mockk<AddressResolver>()
    private val shopFactory = ShopFactory(shopBusinessNumberValidator, addressResolver)

    @Test
    fun `사업자번호가 유효하지 않으면 예외를 발생한다`() {
        shouldThrow<IllegalArgumentException> {
            shopFactory.createOne("집밥뚝딱 안양점", "32-12-3221", "경기도 안양시 동안구 벌말로 40")
        } shouldHaveMessage "올바른 사업자번호 형식이 아닙니다."
    }

    @Test
    fun `실제로 유효한 사업자번호와 가게이름, 주소라면 가게를 생성한다`() {
        val expectedShopAddress =
            ShopAddress.of("1234567890", Coordinates.of(0.0, 0.0), Address.of("경기도", "안양시 동안구", "벌말로", "40"))

        every { shopBusinessNumberValidator.validate(any()) } answers { }
        every { addressResolver.resolveAddress("경기도 안양시 동안구 벌말로 40") } returns expectedShopAddress

        val shop = shopFactory.createOne("집밥뚝딱 안양점", "321-23-12345", "경기도 안양시 동안구 벌말로 40")

        shop.title.value shouldBe "집밥뚝딱 안양점"
        shop.businessNumber.value shouldBe "321-23-12345"
        shop.address shouldBe expectedShopAddress
    }

    @Test
    fun `가게 이름이 유효하지 않으면 예외를 발생한다`() {
        val expectedShopAddress =
            ShopAddress.of("1234567890", Coordinates.of(0.0, 0.0), Address.of("경기도", "안양시 동안구", "벌말로", "40"))

        every { shopBusinessNumberValidator.validate(any()) } answers { }
        every { addressResolver.resolveAddress("경기도 안양시 동안구 벌말로 40") } returns expectedShopAddress

        shouldThrow<IllegalArgumentException> {
            shopFactory.createOne("집밥뚝딱 ! 안양점", "321-23-12345", "경기도 안양시 동안구 벌말로 40")
        } shouldHaveMessage "올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)"
    }

    @Test
    fun `도로명 주소를 받아 주소 객체를 생선한다`() {
        val address = "경기도 안양시 동안구 벌말로"
        val shopAddress = ShopAddress.of(
            "1234567890",
            Coordinates.of(
                127.0,
                40.0
            ),
            Address.of(
                "경기도",
                "안양시",
                "동안구",
                "벌말로"
            )
        )

        every { shopBusinessNumberValidator.validate(any()) } answers { }
        every { addressResolver.resolveAddress(address) } answers { shopAddress }

        shouldThrow<IllegalArgumentException> {
            shopFactory.createOne("집밥뚝딱 ! 안양점", "321-23-12345", "경기도 안양시 동안구 벌말로 40")
        } shouldHaveMessage "올바른 가게 이름 형식이 아닙니다.(한글, 영문, 공백, 숫자만 포함 가능)"
    }
}
