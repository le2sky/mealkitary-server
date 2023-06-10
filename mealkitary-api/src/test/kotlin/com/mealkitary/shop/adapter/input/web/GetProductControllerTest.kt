package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetProductQuery
import com.mealkitary.shop.application.port.input.ProductResponse
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

@WebMvcTest(controllers = [GetProductController::class])
@AutoConfigureRestDocs
class GetProductControllerTest : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var getProductQuery: GetProductQuery

    @Test
    fun `api integration test - getAllProductOfShopTest`() {
        every { getProductQuery.loadAllProductByShopId(1L) }.answers {
            listOf(ProductResponse(1L, "부대찌개", 15000))
        }

        mvc.perform(RestDocumentationRequestBuilders.get("/shops/{shopId}/products", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "shop-get-products",
                    pathParameters(
                        parameterWithName("shopId").description("상품을 조회하려는 대상 가게의 식별자.")
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("상품 목록 배열"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격")
                    )
                )
            )
        verify { getProductQuery.loadAllProductByShopId(1L) }
    }
}
