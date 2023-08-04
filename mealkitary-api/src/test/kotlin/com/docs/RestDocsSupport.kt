package com.docs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

abstract class RestDocsSupport : AnnotationSpec() {

    protected lateinit var mvc: MockMvc
    protected val objectMapper = ObjectMapper()
    private val restDocumentation = ManualRestDocumentation()
    private val messageConverter = MappingJackson2HttpMessageConverter()

    @BeforeAll
    fun beforeAll() {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        messageConverter.supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)
        messageConverter.objectMapper = objectMapper
    }

    @BeforeEach
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(initController())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .setMessageConverters(messageConverter).build()

        restDocumentation.beforeTest(javaClass, javaClass.simpleName)
    }

    @AfterEach
    fun tearDown() {
        restDocumentation.afterTest()
    }

    protected abstract fun initController(): Any
}
