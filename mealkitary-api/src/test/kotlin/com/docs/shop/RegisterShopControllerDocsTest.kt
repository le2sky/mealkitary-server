package com.docs.shop

import com.docs.RestDocsSupport
import com.mealkitary.shop.application.port.input.RegisterShopUseCase
import com.mealkitary.shop.web.RegisterShopController
import com.mealkitary.shop.web.request.RegisterShopWebRequest
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RegisterShopControllerDocsTest : RestDocsSupport() {

    private val registerShopUseCase = mockk<RegisterShopUseCase>()

    @Test
    fun `api docs test - registerShop`() {
        every { registerShopUseCase.register(any()) } answers { 1L }

        val registerShopWebRequest = RegisterShopWebRequest("집밥뚝딱 안양점", "123-23-12345")

        mvc.perform(
            RestDocumentationRequestBuilders.post("/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerShopWebRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "http://localhost/shops/1"))
            .andDo(
                MockMvcRestDocumentation.document(
                    "shop-post",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("등록 대상 가게 이름"),
                        fieldWithPath("brn").type(JsonFieldType.STRING).description("사업자 번호"),
                    ),
                    responseHeaders(headerWithName("Location").description("생성된 가게 리소스 URI")),
                )
            )
    }

    override fun initController() = RegisterShopController(registerShopUseCase)
}
