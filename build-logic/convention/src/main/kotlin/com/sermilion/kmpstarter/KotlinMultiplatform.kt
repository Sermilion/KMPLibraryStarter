package com.sermilion.kmpstarter

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinMultiplatform(
  extension: KotlinMultiplatformExtension
) {
  extension.apply {
    compilerOptions {
      freeCompilerArgs.addAll(
        listOf(
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=kotlinx.coroutines.FlowPreview",
          "-opt-in=kotlin.ExperimentalMultiplatform",
          "-Xexpect-actual-classes",
        )
      )
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
      commonMain {
        dependencies {
        }
      }

      commonTest {
        dependencies {
        }
      }
    }
  }

  extension.targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach(::configureAndroidTarget)

  configureKotlin()
}

internal fun Project.configureKotlinMultiplatformCompose(
  extension: KotlinMultiplatformExtension
) {
  extension.apply {
    compilerOptions {
      freeCompilerArgs.addAll(
        listOf(
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=kotlinx.coroutines.FlowPreview",
          "-opt-in=kotlin.ExperimentalMultiplatform",
          "-Xexpect-actual-classes",
        )
      )
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
      commonMain {
        dependencies {
        }
      }

      androidMain {
        dependencies {
        }
      }

      iosMain {
        dependencies {
        }
      }
    }
  }

  extension.targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach(::configureAndroidTarget)

  configureKotlin()
}

private fun Project.configureKotlin() {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
      val warningsAsErrors: String? by project
      allWarningsAsErrors.set(warningsAsErrors.toBoolean())
      freeCompilerArgs.addAll(
        listOf(
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=kotlinx.coroutines.FlowPreview",
          "-opt-in=kotlin.ExperimentalMultiplatform",
          "-Xexpect-actual-classes",
        )
      )
    }
  }
}

private fun Project.findVersion(alias: String): String {
  return libs.findVersion(alias).get().requiredVersion
}

private fun Project.configureAndroidTarget(target: KotlinMultiplatformAndroidLibraryTarget) {
  with(target) {
    compileSdk = findVersion("compileSdk").toInt()
    minSdk = findVersion("minSdk").toInt()
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
    packaging {
      resources {
        excludes.addAll(
          listOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "META-INF/versions/**",
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md",
          )
        )
      }
    }
  }
}
