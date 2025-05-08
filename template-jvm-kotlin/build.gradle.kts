plugins {
  kotlin("jvm") version "1.9.24"
}

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    showStandardStreams = true
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.1")
  testImplementation("io.kotest:kotest-runner-junit5:6.0.0.M4")
  testImplementation("io.kotest:kotest-assertions-core:6.0.0.M4")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.1")
}
