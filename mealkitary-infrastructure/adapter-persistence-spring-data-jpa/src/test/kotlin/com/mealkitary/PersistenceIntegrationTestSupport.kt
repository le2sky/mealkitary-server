package com.mealkitary

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
abstract class PersistenceIntegrationTestSupport : AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)
}
