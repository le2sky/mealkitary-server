package com.docs.reservation

import com.docs.RestDocsSupport
import com.mealkitary.reservation.application.port.input.PayReservationUseCase
import com.mealkitary.reservation.web.PayReservationController
import com.mealkitary.reservation.web.request.PayReservationWebRequest
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class PayReservationControllerDocsTest : RestDocsSupport() {

    private val payReservationUseCase = mockk<PayReservationUseCase>()

    @Test
    fun `api docs test - payReservation`() {
        val id = UUID.randomUUID()
        every { payReservationUseCase.pay(any()) }.answers { id }
        val payReservationWebRequest = PayReservationWebRequest(
            "dsaDAW3fa21aVad12A",
            20000,
        )

        mvc.perform(
            RestDocumentationRequestBuilders.post("/reservations/{reservationId}/pay", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payReservationWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/payments/$id"))
            .andDo(
                document(
                    "reservation-post-pay",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("reservationId").description("결제 대상 예약의 식별자")
                    ),
                    requestFields(
                        fieldWithPath("paymentKey").type(JsonFieldType.STRING).description("결제 식별자"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("결제 금액")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 결제 리소스 URI")
                    ),
                )
            )
    }

    override fun initController() = PayReservationController(payReservationUseCase)
}
