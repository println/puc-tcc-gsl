import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    `java-library`
}

group = "boaentrega.gsl"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.apache.avro:avro:1.11.1")

    //spring
    implementation("org.springframework.boot:spring-boot:2.6.7")
    implementation("org.springframework:spring-context:5.3.19")
    implementation("org.springframework.amqp:spring-rabbit:2.4.4")

    //jpa-spring
    implementation("org.springframework.data:spring-data-commons:2.6.4")
    implementation("org.springframework.data:spring-data-jpa:2.6.4")
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
    implementation("org.hibernate:hibernate-core:5.6.8.Final")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("com.fleshgrinder.kotlin:case-format:0.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
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

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
                "Implementation-Version" to project.version))
    }
}

java {
    withSourcesJar()
}