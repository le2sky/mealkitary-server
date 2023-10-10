package com.docs.shop

import com.docs.RestDocsSupport
import com.mealkitary.shop.application.port.input.GetShopQuery
import com.mealkitary.shop.application.port.input.ShopResponse
import com.mealkitary.shop.web.GetShopController
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class GetShopControllerDocsTest : RestDocsSupport() {

    private val getShopQuery = mockk<GetShopQuery>()

    @Test
    fun `api docs test - getAllShopTest`() {
        val shopId = UUID.randomUUID()
        every { getShopQuery.loadAllShop() }.answers {
            listOf(ShopResponse(shopId, "집밥뚝딱"))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-list",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("가게 목록 배열"),
                        fieldWithPath("[].id").type(JsonFieldType.STRING).description("가게 식별자"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("가게명")
                    )
                )
            )
    }

    override fun initController() = GetShopController(getShopQuery)
}
