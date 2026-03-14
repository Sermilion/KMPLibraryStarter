package com.sermilion.kmpstarter

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

private val coverageExclusions = listOf(
  "**/R.class",
  "**/R\$*.class",
  "**/BuildConfig.*",
  "**/Manifest*.*"
)

private fun File.classEntries(): Set<String> =
  if (!exists()) {
    emptySet()
  } else {
    walkTopDown()
      .filter { it.isFile && it.extension == "class" }
      .map { it.relativeTo(this).invariantSeparatorsPath }
      .toSet()
  }

internal fun Project.configureJacoco() {
  extensions.configure<JacocoPluginExtension> {
    toolVersion = libs.findVersion("jacoco").get().toString()
  }

  tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
      destinationFile = layout.buildDirectory.file("jacoco/$name.exec").get().asFile
      isIncludeNoLocationClasses = true
      excludes = listOf("jdk.internal.*")
    }
  }

  tasks.register("jacocoTestReport", JacocoReport::class) {
    val testTasks = tasks.withType<Test>()
    val buildDirectory = layout.buildDirectory.get().asFile

    dependsOn(testTasks)

    reports {
      xml.required.set(true)
      html.required.set(true)
    }

    classDirectories.setFrom(
      provider {
        val jvmMainDir = buildDirectory.resolve("classes/kotlin/jvm/main")
        val jvmJavaMainDir = buildDirectory.resolve("classes/java/jvm/main")
        val androidMainDir = buildDirectory.resolve("classes/kotlin/android/main")
        val androidJavaMainDir = buildDirectory.resolve("classes/java/android/main")
        val canonicalClassEntries = jvmMainDir.classEntries() + jvmJavaMainDir.classEntries()

        files(
          fileTree(jvmMainDir) {
            include("**/*.class")
            exclude(coverageExclusions)
          },
          fileTree(jvmJavaMainDir) {
            include("**/*.class")
            exclude(coverageExclusions)
          },
          fileTree(androidMainDir) {
            include("**/*.class")
            exclude(coverageExclusions + canonicalClassEntries)
          },
          fileTree(androidJavaMainDir) {
            include("**/*.class")
            exclude(coverageExclusions + canonicalClassEntries)
          },
        )
      }
    )

    sourceDirectories.setFrom(
      files(
        "$projectDir/src/commonMain/kotlin",
        "$projectDir/src/commonMain/java",
        "$projectDir/src/androidMain/kotlin",
        "$projectDir/src/androidMain/java",
        "$projectDir/src/jvmMain/kotlin",
        "$projectDir/src/jvmMain/java",
      )
    )

    executionData.setFrom(
      fileTree(layout.buildDirectory) {
        include(
          "jacoco/*.exec",
          "outputs/unit_test_code_coverage/**/*.ec",
        )
      }
    )
  }
}
