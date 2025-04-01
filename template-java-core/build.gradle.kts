plugins {
  kotlin("jvm").version("1.9.22")
}

repositories {
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

// "annotationProcessor" deps go on the classpath prior to compilation.
// "api" deps go on compile and runtime classpaths, exported to consumers.
// "implementation" deps go on the compile and runtime classpaths.
// "compileOnly" deps go on the compile classpath.
// "runtimeOnly" deps go on the runtime classpath.
// "testAnnotationProcessor" deps go on the classpath prior to compilation, during tests.
// "testImplementation" deps go on the compile and runtime classpaths, during tests.
// "testCompileOnly" deps go on the compile classpath, during tests.
// "testRuntimeOnly" deps go on the runtime classpath, during tests.
//
// If you mix up "implementation" and "runtimeOnly", you may introduce some bloat but it's not the end of the world.
//
// If you mix up "testImplementation" and "testRuntimeOnly", you may introduce some bloat but it's not the end of the
// world.
//
// If you mix up "api" and "runtimeOnly", you'll export a dependency to users that may conflict with their own, and
// that may qualify for end of the world.
//
// TLDR: 99% of the time you just want "implementation" and "testImplementation".
dependencies {
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("com.google.guava:guava:33.4.6-jre")
  implementation("com.google.inject:guice:7.0.0")
  implementation("com.ibm.icu:icu4j:77.1")
  implementation("org.slf4j:slf4j-api:2.0.17")

  testImplementation("org.assertj:assertj-core:3.27.3")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.12.1")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
}
