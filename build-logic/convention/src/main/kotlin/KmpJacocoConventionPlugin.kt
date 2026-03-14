import com.sermilion.kmpstarter.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpJacocoConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("org.gradle.jacoco")
      configureJacoco()
    }
  }
}
