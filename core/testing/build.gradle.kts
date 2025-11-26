plugins {
  alias(libs.plugins.kmp.library)
}

android {
  namespace = "com.sermilion.kmpstarter.core.testing"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.common)
      implementation(libs.kotlinx.coroutines.core)
      api(libs.kotlinx.coroutines.test)
      api(libs.kotest.assertions.core)
    }

    androidMain.dependencies {
      api(libs.androidx.junit)
      api(libs.mockk.android)
    }

    jvmMain.dependencies {
      api(libs.mockk.core)
    }
  }
}
