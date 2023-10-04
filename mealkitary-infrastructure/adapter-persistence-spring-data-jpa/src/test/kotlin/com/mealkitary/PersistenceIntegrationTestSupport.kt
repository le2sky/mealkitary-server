package com.mealkitary

import com.mealkitary.reservation.application.service.AcceptReservationService
import com.mealkitary.reservation.application.service.PayReservationService
import com.mealkitary.reservation.application.service.RejectReservationService
import com.mealkitary.shop.application.service.RegisterShopService
import com.mealkitary.shop.domain.shop.factory.ShopFactory
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

@SpringBootTest
@Transactional
abstract class PersistenceIntegrationTestSupport : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    protected lateinit var em: EntityManager

    @Autowired
    protected lateinit var emf: EntityManagerFactory

    @MockkBean
    private lateinit var payReservationService: PayReservationService

    @MockkBean
    private lateinit var acceptReservationService: AcceptReservationService

    @MockkBean
    private lateinit var rejectReservationService: RejectReservationService

    @MockkBean
    private lateinit var registerShopService: RegisterShopService

    @MockkBean
    private lateinit var shopFactory: ShopFactory
}
