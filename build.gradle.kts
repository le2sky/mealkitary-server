import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.7.11" apply false
	id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21" apply false
	kotlin("plugin.jpa") version "1.6.21" apply false
}

allprojects {
	group = "com.mealkitary"
	version = "0.0.1-SNAPSHOT"

	tasks {
		withType<JavaCompile> {
			sourceCompatibility = "11"
			targetCompatibility = "11"
		}

		withType<KotlinCompile> {
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "11"
			}
		}

		withType<BootJar> {
			enabled = false
		}

		withType<Jar> {
			enabled = true
		}

		withType<Test> {
			useJUnitPlatform()
		}
	}

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}