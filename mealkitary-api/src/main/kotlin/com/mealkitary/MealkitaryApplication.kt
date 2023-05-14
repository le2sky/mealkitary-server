package com.mealkitary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class MealkitaryApplication

fun main(args: Array<String>) {
    runApplication<MealkitaryApplication>(*args)
}
