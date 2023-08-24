dependencies {
    val firebaseAdminVersion: String by properties
    implementation(project(":mealkitary-application"))
    implementation("com.google.firebase:firebase-admin:$firebaseAdminVersion")
}
