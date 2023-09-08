package com.docs.reservation

import com.docs.RestDocsSupport
import com.mealkitary.reservation.application.port.input.GetReservationQuery
import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.application.port.input.ReservedProduct
import com.mealkitary.reservation.web.GetReservationController
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

class GetReservationControllerDocsTest : RestDocsSupport() {

    private val getReservationQuery = mockk<GetReservationQuery>()

    @Test
    fun `api docs test - getOneReservation`() {
        val reservationId = UUID.randomUUID()
        val reserveAt = LocalDateTime.now()
        every { getReservationQuery.loadOneReservationById(reservationId) } answers {
            ReservationResponse(
                reservationId,
                "집밥뚝딱 안양점",
                "부대찌개 외 1건",
                reserveAt,
                "PAID",
                listOf(
                    ReservedProduct(
                        1L,
                        "부대찌개",
                        20000,
                        2
                    ),
                    ReservedProduct(
                        2L,
                        "김치찌개",
                        20000,
                        1
                    )
                )
            )
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/reservations/{reservationId}", reservationId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "reservation-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("reservationId").description("조회 대상 예약의 식별자")
                    ),
                    responseFields(
                        fieldWithPath("reservationId").type(JsonFieldType.STRING).description("예약 식별자"),
                        fieldWithPath("shopName").type(JsonFieldType.STRING).description("예약 대상 가게의 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("예약 개요"),
                        fieldWithPath("reserveAt").type(JsonFieldType.STRING).description("예약 시간(yyyy-mm-ddThh:mm:ss)"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("예약 상태"),
                        fieldWithPath("reservedProduct.[].productId").type(JsonFieldType.NUMBER)
                            .description("예약 상품 식별자"),
                        fieldWithPath("reservedProduct.[].name").type(JsonFieldType.STRING)
                            .description("예약 상품명"),
                        fieldWithPath("reservedProduct.[].price").type(JsonFieldType.NUMBER)
                            .description("예약 상품 가격"),
                        fieldWithPath("reservedProduct.[].count").type(JsonFieldType.NUMBER).description("예약 수량")
                    )
                )
            )
    }

    override fun initController() = GetReservationController(getReservationQuery)
}
