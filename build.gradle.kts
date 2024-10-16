plugins {
  id("org.jetbrains.intellij") version "1.17.4"
  id("org.jetbrains.grammarkit") version "2022.3.2.2"
  id("java")
}

group = "com.github.intellij.gno"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2023.1")
  type.set("IC")
  plugins.set(listOf("java"))
  pluginName.set("Gno")

  plugins.set(listOf(/* Plugin Dependencies */))
}

sourceSets {
  main {
    java {
      srcDirs("src/main/java")
    }
  }
}

dependencies {
//  implementation("com.jetbrains.intellij.java:java-psi-api:213.0.0")
  implementation("org.jetbrains:annotations:26.0.1")
}

//grammarKit {
//  jflexRelease.set("1.7.0-1")
//  grammarKitRelease.set("2021.1.2")
//}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }

  patchPluginXml {
    sinceBuild.set("231")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}