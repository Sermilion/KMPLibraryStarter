import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLintConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.withPlugin("com.android.application") {
        configure<ApplicationExtension> { lint(Lint::configure) }
      }

      pluginManager.withPlugin("com.android.library") {
        configure<LibraryExtension> { lint(Lint::configure) }
      }

      pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
        extensions.getByType<KotlinMultiplatformExtension>()
          .targets
          .withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
          .configureEach {
            lint(Lint::configure)
          }
      }
    }
  }
}

private fun Lint.configure() {
  xmlReport = true
  checkDependencies = true
}
