dependencies {
    val mockWebServerVersion: String by properties
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-application"))
    testImplementation(testFixtures(project(":mealkitary-domain")))
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockWebServerVersion")
    testImplementation("io.projectreactor:reactor-test")
}
