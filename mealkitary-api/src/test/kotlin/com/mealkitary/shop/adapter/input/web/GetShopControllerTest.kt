package com.mealkitary.shop.adapter.input.web

import com.mealkitary.WebIntegrationTestSupport
import com.mealkitary.shop.application.port.input.ShopResponse
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetShopControllerTest : WebIntegrationTestSupport() {

    @Test
    fun `api integration test - getAllShopTest`() {
        every { getShopQuery.loadAllShop() }.answers {
            listOf(ShopResponse(1L, "집밥뚝딱"))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-list",
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("가게 목록 배열"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 식별자"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("가게명")
                    )
                )
            )
    }
}
