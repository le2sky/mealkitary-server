package com.mealkitary.brn

import com.mealkitary.brn.payload.OpenApiBrnStatus
import com.mealkitary.brn.payload.OpenApiBrnStatusPayload
import com.mealkitary.brn.payload.OpenApiBrnStatusResponse
import com.mealkitary.shop.domain.shop.ShopBusinessNumber
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

class OpenApiBrnValidatorTest : AnnotationSpec() {

    private val openApiWebClient = mockk<OpenApiWebClient>()
    private val openApiBrnValidator = OpenApiBrnValidator(openApiWebClient)

    @Test
    fun `Open API 호출할 때, 사업자 등록 번호의 하이픈(-)을 제거한다`() {
        val payloadSlot = slot<OpenApiBrnStatusPayload>()
        every { openApiWebClient.requestStatus(capture(payloadSlot), any()) } answers {
            createResponse("계속사업자")
        }

        openApiBrnValidator.validate(ShopBusinessNumber.from("123-12-12345"))

        val captured = payloadSlot.captured
        captured.b_no.get(0) shouldBe "1231212345"
    }

    @Test
    fun `사업자 번호 조회 결과가 존재하지 않는다면 예외를 발생한다`() {
        every { openApiWebClient.requestStatus(any(), any()) } answers {
            OpenApiBrnStatusResponse(
                "OK",
                0,
                0,
                emptyList()
            )
        }

        shouldThrow<IllegalArgumentException> {
            openApiBrnValidator.validate(ShopBusinessNumber.from("123-12-12345"))
        } shouldHaveMessage "사업자 번호 조회 결과가 없습니다."
    }

    @Test
    fun `사업자 상태가 계속사업자가 아니라면 예외를 발생한다`() {
        every { openApiWebClient.requestStatus(any(), any()) } answers {
            createResponse("휴업자")
        }

        shouldThrow<IllegalArgumentException> {
            openApiBrnValidator.validate(ShopBusinessNumber.from("123-12-12345"))
        } shouldHaveMessage "유효하지 않은 사업자 번호입니다."
    }

    @Test
    fun `HTTP 클라이언트에게 Open API 경로를 전달한다`() {
        every { openApiWebClient.requestStatus(any(), any()) } answers {
            createResponse("계속사업자")
        }

        openApiBrnValidator.validate(ShopBusinessNumber.from("123-12-12345"))

        verify { openApiWebClient.requestStatus(any(), "https://api.odcloud.kr/api/nts-businessman") }
    }

    private fun createResponse(b_stt: String) = OpenApiBrnStatusResponse(
        "OK",
        0,
        0,
        listOf(
            OpenApiBrnStatus(
                "", b_stt, "", "", "", "", "", "", "", "", ""
            )
        )
    )
}
