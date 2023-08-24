package com.docs.reservation

import com.docs.RestDocsSupport
import com.mealkitary.reservation.adapter.input.web.AcceptReservationController
import com.mealkitary.reservation.application.port.input.AcceptReservationUseCase
import io.mockk.every
import io.mockk.mockk
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class AcceptReservationControllerDocsTest : RestDocsSupport() {

    private val acceptReservationUseCase = mockk<AcceptReservationUseCase>()

    @Test
    fun `api docs test - acceptReservation`() {
        val id = UUID.randomUUID()
        every { acceptReservationUseCase.accept(any()) }.answers { }

        mvc.perform(
            RestDocumentationRequestBuilders.post("/reservations/{reservationId}/accept", id)
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "reservation-post-accept",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("reservationId").description("승인 대상 예약의 식별자")
                    ),
                )
            )
    }

    override fun initController() = AcceptReservationController(acceptReservationUseCase)
}
