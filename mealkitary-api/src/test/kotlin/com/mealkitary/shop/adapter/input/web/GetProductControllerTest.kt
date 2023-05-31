package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetProductQuery
import com.mealkitary.shop.application.port.input.ProductResponse
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [GetProductController::class])
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

        mvc.perform(MockMvcRequestBuilders.get("/shops/1/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        verify { getProductQuery.loadAllProductByShopId(1L) }
    }
}
