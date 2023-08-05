package com.mealkitary

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
}
