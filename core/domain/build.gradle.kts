plugins {
  alias(libs.plugins.kmp.library)
  alias(libs.plugins.kmp.jacoco)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  android {
    namespace = "com.sermilion.kmpstarter.core.domain"
  }

  sourceSets {
    commonMain.dependencies {
      api(projects.core.common)
      api(libs.paging.common)
      api(libs.serialization.json)
      api(libs.kotlin.inject.runtime)
      implementation(libs.kermit)
    }

    commonTest.dependencies {
      implementation(projects.core.testing)
    }

    androidMain.dependencies {
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.paging.runtime)
      implementation(libs.paging.compose)
    }
  }

  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
}

dependencies {
  add("kspAndroid", libs.kotlin.inject.compiler)
  add("kspIosArm64", libs.kotlin.inject.compiler)
  add("kspIosSimulatorArm64", libs.kotlin.inject.compiler)
  add("kspJvm", libs.kotlin.inject.compiler)
}
