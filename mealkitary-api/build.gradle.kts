import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = false

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":mealkitary-application"))
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-infrastructure:adapter-persistence-spring-data-jpa"))
}
