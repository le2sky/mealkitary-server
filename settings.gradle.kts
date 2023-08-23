rootProject.name = "mealkitary"

include(
    "mealkitary-api",
    "mealkitary-application",
    "mealkitary-domain",
    "mealkitary-infrastructure:adapter-persistence-spring-data-jpa",
    "mealkitary-infrastructure:adapter-paymentgateway-tosspayments",
    "mealkitary-infrastructure:adapter-firebase-notification"
)

pluginManagement {
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val asciidoctorVersion: String by settings
    val jibVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "org.jetbrains.kotlin.jvm",
                "org.jetbrains.kotlin.plugin.spring",
                "org.jetbrains.kotlin.kapt",
                "org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
                "org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
                "org.asciidoctor.jvm.convert" -> useVersion(asciidoctorVersion)
                "com.google.cloud.tools.jib" -> useVersion(jibVersion)
            }
        }
    }
}
