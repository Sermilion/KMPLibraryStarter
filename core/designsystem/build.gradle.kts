plugins {
  alias(libs.plugins.kmp.library)
  alias(libs.plugins.kmp.jacoco)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.sermilion.kmpstarter.core.designsystem"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.common)
      api(libs.compose.foundation)
      api(libs.compose.material3)
      api(libs.compose.material.icons.extended)
      api(libs.compose.runtime)
      api(libs.compose.ui)
      api(libs.compose.animation)
      api(libs.compose.resources)
      api(libs.kotlinx.collections.immutable)
    }

    androidMain.dependencies {
      api(libs.androidx.activity.compose)
      implementation(libs.androidx.core.ktx)
      api(libs.coil.kt.compose)
    }

    iosMain.dependencies {
    }

    jvmMain.dependencies {
    }
  }
}
