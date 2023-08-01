package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalTime

@WebMvcTest(controllers = [GetReservableTimeController::class])
@AutoConfigureRestDocs
class GetReservableTimeControllerTest : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var getReservableTimeQuery: GetReservableTimeQuery

    @Test
    fun `api integration test - getAllReservableTimeOfShopTest`() {
        every { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }.answers {
            listOf(LocalTime.of(6, 30))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/{shopId}/reservable-time", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-reservable-time",
                    pathParameters(
                        parameterWithName("shopId").description("예약 가능 시간을 조회하려는 대상 가게의 식별자.")
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("예약 가능 시간(HH:mm:ss) 목록 배열")
                    )
                )
            )

        verify { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }
    }
}
