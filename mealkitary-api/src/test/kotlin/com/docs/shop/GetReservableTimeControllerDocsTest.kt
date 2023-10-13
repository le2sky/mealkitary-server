package com.docs.shop

import com.docs.RestDocsSupport
import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import com.mealkitary.shop.web.GetReservableTimeController
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
import java.time.LocalTime
import java.util.UUID

class GetReservableTimeControllerDocsTest : RestDocsSupport() {

    private val getReservableTimeQuery = mockk<GetReservableTimeQuery>()

    @Test
    fun `api docs test - getAllReservableTimeOfShopTest`() {
        val shopId = UUID.randomUUID()
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(shopId) }.answers {
            listOf(LocalTime.of(6, 30))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/{shopId}/reservable-time", shopId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-reservable-time",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("shopId").description("예약 가능 시간을 조회하려는 대상 가게의 식별자")
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("예약 가능 시간(hh:mm:ss) 목록 배열"),
                    )
                )
            )
    }

    override fun initController() = GetReservableTimeController(getReservableTimeQuery)
}
