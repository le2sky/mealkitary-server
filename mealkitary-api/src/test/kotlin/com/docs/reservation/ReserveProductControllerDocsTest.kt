package com.docs.reservation

import com.docs.RestDocsSupport
import com.mealkitary.reservation.adapter.input.web.ReserveProductController
import com.mealkitary.reservation.adapter.input.web.request.ReserveProductWebRequest
import com.mealkitary.reservation.adapter.input.web.request.ReservedWebProduct
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReserveProductControllerDocsTest : RestDocsSupport() {

    private val reserveProductUseCase = mockk<ReserveProductUseCase>()

    @Test
    fun `api docs test - reserveProduct`() {
        every { reserveProductUseCase.reserve(any()) }.answers {
            1L
        }
        val reserveProductWebRequest = ReserveProductWebRequest(
            1L,
            listOf(
                ReservedWebProduct(
                    2L,
                    "부대찌개",
                    3000,
                    3
                )
            ),
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0)
            ).toString()
        )

        mvc.perform(
            RestDocumentationRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost:8080/reservations/1"))
            .andDo(
                document(
                    "reservation-post",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("예약 대상 가게 식별자"),
                        fieldWithPath("products.[].productId").type(JsonFieldType.NUMBER).description("예약 대상 상품 식별자"),
                        fieldWithPath("products.[].name").type(JsonFieldType.STRING).description("예약 대상 상품명"),
                        fieldWithPath("products.[].price").type(JsonFieldType.NUMBER).description("예약 대상 상품 가격"),
                        fieldWithPath("products.[].count").type(JsonFieldType.NUMBER).description("예약 수량"),
                        fieldWithPath("reservedAt").type(JsonFieldType.STRING).description("예약 시간(yyyy-mm-ddThh:mm:ss)")
                    ),
                    responseHeaders(
                        headerWithName("Location")
                            .description("생성된 예약 리소스 URI")
                    ),
                )
            )
    }

    override fun initController() = ReserveProductController(reserveProductUseCase)
}
