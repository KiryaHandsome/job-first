val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.1"
}

group = "com.job"
version = "0.0.1"

application {
    mainClass = "com.job.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-core")

    implementation(libs.kodein)
    implementation(libs.jackson)
    implementation(libs.jooq)
    implementation(libs.jooq.codegen)
    implementation(libs.jooq.meta)
    implementation(libs.postgresql.r2dbc)
    implementation(libs.postgresql)
    implementation(libs.jackson.time)
    implementation(libs.flyway)
    implementation(libs.flyway.postgresql)
    implementation(libs.jbcrypt)

    // tests
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
