package com.mealkitary.reservation.adapter.input.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.reservation.application.port.input.ReserveProductRequest
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import com.mealkitary.reservation.application.port.input.ReservedProduct
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@WebMvcTest(controllers = [ReserveProductController::class])
@AutoConfigureRestDocs
class ReserveProductControllerTest : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var reserveProductUseCase: ReserveProductUseCase

    @Test
    fun `api integration test - reserveProduct`() {
        every { reserveProductUseCase.reserve(any()) }.answers {
            1L
        }
        val reserveProductRequest = ReserveProductRequest(
            1L,
            listOf(
                ReservedProduct(
                    2L,
                    "부대찌개",
                    3000,
                    3
                )
            ),
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(16, 0)
            )
        )

        mvc.perform(
            RestDocumentationRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserveProductRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost:8080/reservations/1"))
            .andDo(
                document(
                    "reservation-post",
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
                            .description("생성된 예약 리소스의 URI입니다.")
                    ),
                )
            )
    }
}
