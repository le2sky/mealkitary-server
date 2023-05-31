package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetShopQuery
import com.mealkitary.shop.application.port.input.ShopResponse
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [GetShopController::class])
class GetShopControllerTest : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var getShopQuery: GetShopQuery

    @Test
    fun `api integration test - getAllShopTest`() {
        every { getShopQuery.loadAllShop() }.answers {
            listOf(ShopResponse(1L, "집밥뚝딱"))
        }
        mvc.perform(get("/shops/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
}
