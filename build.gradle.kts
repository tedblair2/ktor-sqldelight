val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_ktor: String by project
val hikaricp_version: String by project
val postgres_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("app.cash.sqldelight") version "2.0.2"
}

group = "tedblair2.github.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_ktor")
    //connection pooling
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
    //migration lib
    implementation("org.flywaydb:flyway-core:7.10.0")
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("tedblair2.github.com.db")
            dialect("app.cash.sqldelight:postgresql-dialect:2.0.2")
            deriveSchemaFromMigrations.set(true)
            migrationOutputDirectory = file("src/main/resources/db/migration")
            migrationOutputFileFormat = ".sql"
        }
    }
}

tasks{
    processResources.configure {
        dependsOn("generateMainDatabaseMigrations")
    }
}


