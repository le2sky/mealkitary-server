dependencies {
    val firebaseAdminVersion: String by properties
    implementation(project(":mealkitary-domain"))
    implementation("com.google.firebase:firebase-admin:$firebaseAdminVersion")
    testImplementation(testFixtures(project(":mealkitary-domain")))
}
