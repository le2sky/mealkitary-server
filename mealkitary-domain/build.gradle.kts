plugins {
    id("java-test-fixtures")
    kotlin("kapt")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    val kapt by configurations
    val querydslVersion: String by properties
    val ulidCreatorVersion: String by properties
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    implementation("com.github.f4b6a3:ulid-creator:$ulidCreatorVersion")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
}
