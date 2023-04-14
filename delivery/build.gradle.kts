import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.5.0"
    id ("org.sonarqube") version "3.4.0.2513"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "boaentrega.gsl"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    gradlePluginPortal()
}

sourceSets {
    create("testIntegration") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val testIntegrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val testIntegrationRuntimeOnly: Configuration by configurations.getting {
    extendsFrom(configurations.testRuntimeOnly.get())
}

configurations["testIntegrationImplementation"].extendsFrom(configurations.runtimeOnly.get())
configurations["testIntegrationRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
//    implementation(project(":shared:support"))

    implementation("org.apache.avro:avro:1.11.1")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-avro:2.14.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("com.fleshgrinder.kotlin:case-format:0.2.0")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    runtimeOnly("org.postgresql:postgresql:42.5.0")
    testIntegrationRuntimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test")
    testIntegrationImplementation("org.jeasy:easy-random-core:4.0.0")
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["testIntegration"].output.classesDirs
    classpath = sourceSets["testIntegration"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.withType<ProcessResources> {
    filesMatching("application.yml") {
        expand(project.properties)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }

}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.getByName<Test>("integrationTest") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

tasks.check {
    //dependsOn(gradle.includedBuild(":shared:support").task(":app:build"))
    dependsOn(integrationTest)
}

avro {
    isCreateSetters.set(true)
    isCreateOptionalGetters.set(false)
    isGettersReturnOptional.set(false)
    isOptionalGettersForNullableFieldsOnly.set(false)
    fieldVisibility.set("PRIVATE")
    outputCharacterEncoding.set("UTF-8")
    stringType.set("String")
    templateDirectory.set(null as String?)
    isEnableDecimalLogicalType.set(true)
}

tasks.getByName<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvroJava") {
    source("src/main/resources/schemas/avro")
}
