plugins {
    id("java-library")
    id("com.diffplug.spotless") version "6.25.0"
}

group = "io.github.snz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    java {
        palantirJavaFormat()
    }
}

tasks.test {
    useJUnitPlatform()
}