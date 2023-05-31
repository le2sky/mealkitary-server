package com.mealkitary.shop.adapter.input.web

import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalTime

@WebMvcTest(controllers = [GetReservableTimeController::class])
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
        mvc.perform(get("/shops/1/reservable-time"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        verify { getReservableTimeQuery.loadAllReservableTimeByShopId(1L) }
    }
}
