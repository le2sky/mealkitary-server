package com.docs.shop

import com.docs.RestDocsSupport
import com.mealkitary.shop.application.port.input.UpdateShopStatusUseCase
import com.mealkitary.shop.web.UpdateShopStatusController
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UpdateShopStatusControllerDocsTest : RestDocsSupport() {

    private val updateShopStatsUseCase = mockk<UpdateShopStatusUseCase>()

    @Test
    fun `api docs test - updateShopStatus`() {
        val shopId = 1L
        every { updateShopStatsUseCase.update(shopId) }.answers { }

        mvc.perform(
            RestDocumentationRequestBuilders.post("/shops/{shopId}/status", shopId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "shop-post-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("shopId").description("상태 변경 가게의 식별자")
                    ),
                )
            )
    }

    override fun initController() = UpdateShopStatusController(updateShopStatsUseCase)
}
