plugins {
    application
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Tests
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // AssertJ
    testImplementation("org.assertj:assertj-core:3.25.3")

    // Mockito Core
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")

    // Run JUnit 5
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Application
    implementation(libs.guava)

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    // Lombok for tests
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    // SLF4J API
    implementation("org.slf4j:slf4j-api:2.0.12")

    // Simple logger (console)
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

application {
    mainClass = "com.github.finncker.desktop.Main"
}

javafx {
    version = "22"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    jvmArgs = listOf("-Dnet.bytebuddy.experimental=true")
    enabled = false
}
