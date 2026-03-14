# Extending and Consuming the Starter

## Adding a New Module

1. Create a directory under `core/`:

```text
core/
└── newmodule/
    ├── build.gradle.kts
    └── src/
        ├── commonMain/kotlin/
        ├── androidMain/kotlin/
        ├── iosMain/kotlin/
        └── jvmMain/kotlin/
```

2. Add it to `settings.gradle.kts`:

```kotlin
include(":core:newmodule")
```

3. Configure `build.gradle.kts` with the current Android-KMP structure:

```kotlin
plugins {
  alias(libs.plugins.kmp.library)
}

kotlin {
  android {
    namespace = "com.sermilion.kmpstarter.core.newmodule"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.common)
    }
  }
}
```

Use `alias(libs.plugins.kmp.compose)` instead when the module owns Compose UI.

## Consuming the Library

### Android

```kotlin
// build.gradle.kts
dependencies {
  implementation("com.sermilion:kmpstarter-core-ui:VERSION")
}
```

### iOS

Consume the published framework from your iOS project after publishing the library artifacts.

### JVM

```kotlin
dependencies {
  implementation("com.sermilion:kmpstarter-core-ui-jvm:VERSION")
}
```
