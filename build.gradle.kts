plugins {
    id("java-library")
    id("maven-publish")
    id("com.diffplug.spotless") version "8.0.0"
}

group = "io.github.snz"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("primitive-di") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

spotless {
    java {
        palantirJavaFormat()
        removeUnusedImports()
    }
}

tasks.test {
    useJUnitPlatform()
}