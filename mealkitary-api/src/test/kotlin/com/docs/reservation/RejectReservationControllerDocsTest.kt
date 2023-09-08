package com.docs.reservation

import com.docs.RestDocsSupport
import com.mealkitary.reservation.adapter.input.web.RejectReservationController
import com.mealkitary.reservation.application.port.input.RejectReservationUseCase
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

class RejectReservationControllerDocsTest : RestDocsSupport() {

    private val rejectReservationUseCase = mockk<RejectReservationUseCase>()

    @Test
    fun `api docs test - rejectReservation`() {
        val id = UUID.randomUUID()
        every { rejectReservationUseCase.reject(any()) }.answers { }

        mvc.perform(
            RestDocumentationRequestBuilders.post("/reservations/{reservationId}/reject", id)
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "reservation-post-reject",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("reservationId").description("거절 대상 예약의 식별자")
                    ),
                )
            )
    }

    override fun initController() = RejectReservationController(rejectReservationUseCase)
}
