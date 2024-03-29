package com.docs.shop

import com.docs.RestDocsSupport
import com.mealkitary.shop.application.port.input.GetProductQuery
import com.mealkitary.shop.application.port.input.ProductResponse
import com.mealkitary.shop.web.GetProductController
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
import java.util.UUID

class GetProductControllerDocsTest : RestDocsSupport() {

    private val getProductQuery = mockk<GetProductQuery>()

    @Test
    fun `api docs test - getAllProductOfShopTest`() {
        val shopId = UUID.randomUUID()
        every { getProductQuery.loadAllProductByShopId(shopId) }.answers {
            listOf(ProductResponse(1L, "부대찌개", 15000))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/{shopId}/products", shopId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-products",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("shopId").description("상품을 조회하려는 대상 가게의 식별자")
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("상품 목록 배열"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격")
                    )
                )
            )
    }

    override fun initController() = GetProductController(getProductQuery)
}
