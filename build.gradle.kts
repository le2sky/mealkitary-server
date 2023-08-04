import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    kotlin("jvm")
    jacoco
}

allprojects {
    val projectGroup: String by project
    val applicationVersion: String by project
    val jvmVersion: String by project

    group = projectGroup
    version = applicationVersion

    tasks {
        withType<JavaCompile> {
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = jvmVersion
            }
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    jar.enabled = true
    bootJar.enabled = false

    jacoco {
        val jacocoVersion: String by properties
        toolVersion = jacocoVersion
    }


    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
        }
        finalizedBy(tasks.jacocoTestCoverageVerification)

        classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                exclude(
                    "**.Q*",
                    "**.*Application*",
                    "**.exception.*"
                )
            }
        )
    }

    tasks.jacocoTestCoverageVerification {
        val limitOfLineCoverage: String by properties

        dependsOn(tasks.jacocoTestReport)
        violationRules {
            rule {
                enabled = true
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = limitOfLineCoverage.toBigDecimal()
                }

                excludes = listOf(
                    "**.Q*",
                    "**.*Application*",
                    "**.exception.*"
                )
            }
        }
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
            finalizedBy(jacocoTestReport)
        }
    }

    dependencies {
        val kotestVersion: String by properties
        val mockkVersion: String by properties
        val springmockkVersion: String by properties

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")
        testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
    }
}
