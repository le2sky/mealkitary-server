dependencies {
    val querydslVersion: String by properties
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    implementation("com.h2database:h2")
    implementation(project(":mealkitary-domain"))
    implementation(project(":mealkitary-application"))
}
