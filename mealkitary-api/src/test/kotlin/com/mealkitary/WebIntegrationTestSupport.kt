package com.mealkitary

import com.fasterxml.jackson.databind.ObjectMapper
import com.mealkitary.reservation.adapter.input.web.AcceptReservationController
import com.mealkitary.reservation.adapter.input.web.GetReservationController
import com.mealkitary.reservation.adapter.input.web.PayReservationController
import com.mealkitary.reservation.adapter.input.web.RejectReservationController
import com.mealkitary.reservation.adapter.input.web.ReserveProductController
import com.mealkitary.reservation.application.port.input.AcceptReservationUseCase
import com.mealkitary.reservation.application.port.input.GetReservationQuery
import com.mealkitary.reservation.application.port.input.PayReservationUseCase
import com.mealkitary.reservation.application.port.input.RejectReservationUseCase
import com.mealkitary.reservation.application.port.input.ReserveProductUseCase
import com.mealkitary.shop.application.port.input.GetProductQuery
import com.mealkitary.shop.application.port.input.GetReservableTimeQuery
import com.mealkitary.shop.application.port.input.GetShopQuery
import com.mealkitary.shop.web.GetProductController
import com.mealkitary.shop.web.GetReservableTimeController
import com.mealkitary.shop.web.GetShopController
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    controllers = [
        ReserveProductController::class,
        PayReservationController::class,
        AcceptReservationController::class,
        RejectReservationController::class,
        GetReservationController::class,
        GetShopController::class,
        GetReservableTimeController::class,
        GetProductController::class
    ]
)
abstract class WebIntegrationTestSupport : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var reserveProductUseCase: ReserveProductUseCase

    @MockkBean
    protected lateinit var payReservationUseCase: PayReservationUseCase

    @MockkBean
    protected lateinit var acceptReservationUseCase: AcceptReservationUseCase

    @MockkBean
    protected lateinit var rejectReservationUseCase: RejectReservationUseCase

    @MockkBean
    protected lateinit var getReservationQuery: GetReservationQuery

    @MockkBean
    protected lateinit var getShopQuery: GetShopQuery

    @MockkBean
    protected lateinit var getReservableTimeQuery: GetReservableTimeQuery

    @MockkBean
    protected lateinit var getProductQuery: GetProductQuery
}
