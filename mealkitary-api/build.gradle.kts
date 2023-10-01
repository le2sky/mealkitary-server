import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks
val snippetsDir by extra { file("build/generated-snippets") }
val asciidoctorExt: Configuration by configurations.creating

bootJar.enabled = true
jar.enabled = false

plugins {
    id("org.asciidoctor.jvm.convert")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":mealkitary-application"))
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-infrastructure:adapter-persistence-spring-data-jpa"))
    implementation(project(":mealkitary-infrastructure:adapter-paymentgateway-tosspayments"))
    implementation(project(":mealkitary-infrastructure:adapter-firebase-notification"))
    implementation(project(":mealkitary-infrastructure:adapter-configuration"))
    implementation(
        project(
            ":mealkitary-infrastructure:adapter-business-registration-number-validator:open-api-brn-validator",
        )
    )
    implementation(
        project(
            ":mealkitary-infrastructure:adapter-business-registration-number-validator:simple-brn-validator",
        )
    )
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.test {
    outputs.dir(snippetsDir)
}

tasks.asciidoctor {
    configurations("asciidoctorExt")
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
    sources {
        include("**/index.adoc")
    }
    baseDirFollowsSourceFile()
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from("build/docs/asciidoc") {
        into("BOOT-INF/classes/static/docs")
    }
}

tasks.register<Copy>("copyFile") {
    dependsOn(tasks.asciidoctor)
    destinationDir = file("src/main/resources/static")
    delete("src/main/resource/static/docs")
    from("build/docs/asciidoc") {
        this.into("docs")
    }
}

tasks.build {
    dependsOn("copyFile")
}

jib {
    from {
        image = "adoptopenjdk/openjdk11:alpine-jre"
    }

    to {
        image = "leehaneul/mealkitary-api"
        tags = setOf("${project.version}")
    }

    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
        mainClass = "com.mealkitary.MealkitaryApplicationKt"
        jvmFlags = listOf(
            "-Xms512m",
            "-Xmx512m",
            "-Xdebug",
            "-XshowSettings:vm",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+UseContainerSupport",
            "-Dfile.encoding=UTF-8"
        )
        environment = mapOf("SPRING_OUTPUT_ANSI_ENABLED" to "ALWAYS")
        ports = listOf("8080")
    }
}
