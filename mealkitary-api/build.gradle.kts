import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks
val snippetsDir by extra { file("build/generated-snippets") }
val asciidoctorExt: Configuration by configurations.creating

bootJar.enabled = true
jar.enabled = false

plugins {
    id("org.asciidoctor.jvm.convert")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":mealkitary-application"))
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-infrastructure:adapter-persistence-spring-data-jpa"))
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
