import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":mealkitary-application"))
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-infrastructure:adapter-persistence-spring-data-jpa"))
}

tasks.withType<BootJar> {
    enabled = true
}

tasks.withType<Jar> {
    enabled = false
}
