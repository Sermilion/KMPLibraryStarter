# Build, Tooling, and Development Setup

## Build System

### Convention Plugins

Located in `build-logic/convention/`, these provide consistent configuration:

| Plugin ID | Purpose |
|-----------|---------|
| `kmp.library` | Base KMP library setup via the Android-KMP library plugin |
| `kmp.compose` | KMP + Compose Multiplatform |
| `kmp.jacoco` | Aggregated code coverage reporting |
| `kmp.lint` | Android lint configuration |
| `kmp.detekt` | Static analysis |

### Usage in Modules

```kotlin
plugins {
  alias(libs.plugins.kmp.library)
  // OR
  alias(libs.plugins.kmp.compose)
}
```

### Version Catalog

All dependencies are centralized in `gradle/libs.versions.toml`.

```toml
[versions]
kotlin = "2.3.10"
compose-multiplatform = "1.10.2"
coroutines = "1.10.2"
```

## Dependency Injection

Uses **kotlin-inject** for compile-time DI.

```kotlin
@Inject
class MyRepository(
  private val database: UserDatabase,
  private val dispatchers: DispatcherProviderContract,
)
```

KSP processors are configured for all supported targets:

- `kspAndroid`
- `kspIosArm64`
- `kspIosSimulatorArm64`
- `kspJvm`

## Code Quality

### Static Analysis (Detekt)

Configuration: `config/detekt/detekt.yml`

Key thresholds:

- Cyclomatic complexity: 15
- Large class: 600 lines
- Long method: 60 lines
- Long parameter list: 6 (functions), 7 (constructors)

### Formatting (Spotless + KtLint)

- Max line length: 100 characters
- Trailing commas: allowed
- Wildcard imports: disabled

### Code Coverage (Jacoco)

Aggregated JVM and Android host test coverage reports are generated via `kmp.jacoco`.

## Development Setup

CI runs on macOS and should validate both `./gradlew check` and at least one iOS link task to catch
multiplatform regressions early.

### Prerequisites

- JDK 17
- Latest stable Android Studio or IntelliJ IDEA
- Latest stable Xcode for iOS development
- The build uses JDK 17, while Android and JVM artifacts remain on Java 11 bytecode unless you
  intentionally migrate the published API level

### Build Commands

```bash
# Build all modules
./gradlew build

# Run the main quality gate
./gradlew check

# Android-specific
./gradlew :core:ui:assembleAndroidMain

# iOS framework
./gradlew :core:ui:linkIosArm64

# Code coverage
./gradlew :core:data:jacocoTestReport
```

### IDE Setup

1. Open the project in Android Studio or IntelliJ
2. Sync Gradle
3. For iOS: open `iosApp/` in Xcode

## Configuration Files

| File | Purpose |
|------|---------|
| `gradle.properties` | JVM args, Kotlin settings |
| `gradle/libs.versions.toml` | Dependency versions |
| `config/detekt/detekt.yml` | Static analysis rules |
| `build-logic/` | Convention plugins |
