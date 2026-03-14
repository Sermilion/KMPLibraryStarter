# KMPLibraryStarter

A production-ready Kotlin Multiplatform library starter template with Compose Multiplatform UI for Android, iOS, and JVM platforms.

## Overview

KMPLibraryStarter provides a solid foundation for building cross-platform libraries with shared UI components. It follows Clean Architecture principles and includes modern tooling for code quality, testing, and dependency injection.

## Features

- **Kotlin Multiplatform** - Share code across Android, iOS, and JVM
- **Compose Multiplatform** - Build shared UI with Jetpack Compose
- **Clean Architecture** - Clear separation of data, domain, and UI/presentation layers
- **Convention Plugins** - Consistent build configuration across modules
- **Dependency Injection** - Compile-time safe DI with kotlin-inject
- **Database** - Room 3 with bundled SQLite for multiplatform persistence
- **Networking** - Ktor client with platform-specific engines
- **Testing** - KoTest framework with MockK
- **Code Quality** - Detekt, ktlint, and aggregated Jacoco coverage
- **Automation** - GitHub Actions check workflow and Dependabot updates
- **Agent Guidance** - Root-level `AGENTS.md` and `CLAUDE.md` for AI-assisted maintenance

## Supported Platforms

| Platform | Min Version | Notes |
|----------|-------------|-------|
| Android | API 26 | Jetpack Compose |
| iOS | arm64, simulatorArm64 | Compose Multiplatform |
| JVM | Java 11 bytecode target | Build with JDK 17 |

## Project Structure

```
KMPLibraryStarter/
├── build-logic/
│   └── convention/          # Gradle convention plugins
├── core/
│   ├── common/              # Shared utilities, dispatchers, extensions
│   ├── data/                # Repositories, database, network clients
│   ├── datastore/           # User preferences (DataStore)
│   ├── designsystem/        # Theme, colors, typography, shapes
│   ├── domain/              # Use cases, repository interfaces
│   ├── testing/             # Test utilities and doubles
│   └── ui/                  # Compose UI components
├── config/
│   └── detekt/              # Static analysis configuration
└── gradle/
    └── libs.versions.toml   # Dependency version catalog
```

## Module Dependencies

```
                    ┌─────────────┐
                    │   core:ui   │
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               ▼               ▼
   ┌──────────────┐ ┌─────────────┐ ┌─────────────┐
   │designsystem  │ │   domain    │ │   common    │
   └──────────────┘ └──────┬──────┘ └─────────────┘
                           │               ▲
                           ▼               │
                    ┌─────────────┐        │
                    │    data     │────────┘
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │  datastore  │
                    └─────────────┘
```

## Getting Started

### Prerequisites

- JDK 17
- Latest stable Android Studio or IntelliJ IDEA
- Latest stable Xcode (required for iOS framework builds)
- The build runs on JDK 17, while Android and JVM artifacts stay on Java 11 bytecode for broader consumer compatibility

### Clone and Build

```bash
git clone https://github.com/Sermilion/KMPLibraryStarter.git
cd KMPLibraryStarter

# Build all modules
./gradlew build

# Run tests
./gradlew check

# Format code
./gradlew spotlessApply
```

### Starter Maintenance

- Read `AGENTS.md` before making structural, build, or release-related changes.
- Use `./gradlew check` as the default validation gate for local changes and CI.
- Update shared dependency versions through `gradle/libs.versions.toml` instead of hardcoding them in module scripts.

### IDE Setup

1. Open the project in Android Studio or IntelliJ IDEA
2. Wait for Gradle sync to complete
3. For iOS development, open `iosApp/` in Xcode

### iOS Setup

#### Building iOS Framework

To build the iOS framework for your KMP library:

```bash
# Build currently configured iOS targets
./gradlew :core:ui:linkIosArm64            # Physical device
./gradlew :core:ui:linkIosSimulatorArm64   # Simulator (Apple Silicon)
```

Artifacts are generated under `core/ui/build/bin/ios*/`

#### Consuming in iOS Project

**Option 1: CocoaPods** (Recommended for published libraries)

Create a `podspec` file for your library:

```ruby
Pod::Spec.new do |spec|
  spec.name                     = 'KmpLibraryStarter'
  spec.version                  = '1.0.0'
  spec.homepage                 = 'https://github.com/Sermilion/KMPLibraryStarter'
  spec.source                   = { :http=> ''}
  spec.authors                  = ''
  spec.license                  = ''
  spec.summary                  = 'KMP Library Starter'
  spec.vendored_frameworks      = 'build/cocoapods/framework/KmpLibraryStarter.framework'
  spec.ios.deployment_target    = '14.0'
end
```

In your iOS project's `Podfile`:

```ruby
target 'YourApp' do
  use_frameworks!
  pod 'KmpLibraryStarter', :path => '../path/to/KMPLibraryStarter'
end
```

**Option 2: Swift Package Manager (SPM)**

Create a `Package.swift` for your library (requires XCFramework):

1. Build XCFramework for all targets:
```bash
./gradlew assembleXCFramework
```

2. Add to SPM:
```swift
// Package.swift
let package = Package(
  name: "KmpLibraryStarter",
  products: [
    .library(name: "KmpLibraryStarter", targets: ["KmpLibraryStarter"])
  ],
  targets: [
    .binaryTarget(
      name: "KmpLibraryStarter",
      path: "./KmpLibraryStarter.xcframework"
    )
  ]
)
```

**Option 3: Direct Framework Integration** (For local development)

1. Build the framework as shown above
2. In Xcode, drag the `.framework` file to your project
3. Add to "Frameworks, Libraries, and Embedded Content"
4. Set "Embed" to "Embed & Sign"

#### iOS Testing Considerations

- MockK is not supported on iOS (Kotlin Native limitation)
- Use Mokkery for multiplatform mocking, or create manual test doubles
- KoTest works on all platforms including iOS

## Convention Plugins

The project uses convention plugins for consistent build configuration:

| Plugin | Purpose |
|--------|---------|
| `kmp.library` | Base KMP library setup with Android-KMP, iOS, and JVM targets |
| `kmp.compose` | KMP library with Compose Multiplatform |
| `kmp.jacoco` | Aggregated code coverage reporting for JVM and Android host tests |
| `kmp.lint` | Android lint configuration |
| `kmp.detekt` | Static analysis with Detekt |

### Usage

```kotlin
// build.gradle.kts
plugins {
  alias(libs.plugins.kmp.library)    // For non-UI modules
  alias(libs.plugins.kmp.compose)    // For Compose modules
  alias(libs.plugins.kmp.jacoco)     // For coverage
}
```

## Core Modules

### common

Shared utilities available to all modules:

```kotlin
// Extension functions
fun Int?.orZero(): Int
fun <T> T?.require(): T
fun <T> Flow<T?>?.orEmpty(): Flow<T?>

// Coroutine dispatchers with testable interface
interface DispatcherProviderContract {
  fun io(): CoroutineDispatcher
  fun main(): CoroutineDispatcher
  fun default(): CoroutineDispatcher
}
```

### designsystem

Material 3 design system with custom theming:

```kotlin
@Composable
fun MyScreen() {
  KmpMaterialTheme {
    val colors = KmpTheme.colors           // M3 ColorScheme
    val kmpColors = KmpTheme.kmpColors     // Custom colors
    val typography = KmpTheme.typography
    val shapes = KmpTheme.shapes

    // Your UI here
  }
}
```

### domain

Business logic layer with repository interfaces and use cases:

```kotlin
interface UserRepository {
  fun getUsers(): Flow<List<User>>
  suspend fun getUserById(id: String): User?
}

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  operator fun invoke(id: String): Flow<User?> =
    repository.getUsers().map { users -> users.find { it.id == id } }
}
```

### data

Data layer with Room 3 database and Ktor networking:

```kotlin
@Entity(tableName = "users")
data class UserDataModel(
  @PrimaryKey val id: String,
  val name: String,
  val email: String?,
  val createdAt: Long,
)

@Dao
interface UserDao {
  @Query("SELECT * FROM users ORDER BY createdAt DESC")
  fun observeUsers(): Flow<List<UserDataModel>>

  @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
  suspend fun getById(id: String): UserDataModel?

  @Upsert
  suspend fun upsert(user: UserDataModel)
}
```

### datastore

User preferences with AndroidX DataStore:

```kotlin
@Serializable
data class UserPreferences(
  val isLoggedIn: Boolean = false,
  val userId: String? = null,
)
```

### testing

Test utilities and doubles:

```kotlin
class TestDispatcherProvider(
  private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : DispatcherProviderContract {
  override fun io() = testDispatcher
  override fun main() = testDispatcher
  override fun default() = testDispatcher
}
```

## Dependency Injection

Uses kotlin-inject for compile-time safe DI:

```kotlin
@Inject
class UserRepositoryImpl(
  private val database: UserDatabase,
  private val dispatchers: DispatcherProviderContract,
) : UserRepository {
  // Implementation
}
```

## Testing

The project uses KoTest with FunSpec style:

```kotlin
class UserRepositoryTest : FunSpec({
  val testDispatcher = TestDispatcherProvider()

  test("getUsers returns empty list when database is empty") {
    // Arrange
    val repository = createRepository()

    // Act
    val result = repository.getUsers().first()

    // Assert
    result.shouldBeEmpty()
  }
})
```

Run tests:

```bash
# All tests
./gradlew check

# Specific module
./gradlew :core:data:check

# JVM tests only
./gradlew jvmTest

# Android unit tests
./gradlew testDebugUnitTest
```

## Code Quality

### Detekt (Static Analysis)

```bash
./gradlew detekt
```

Configuration: `config/detekt/detekt.yml`

### Spotless (Formatting)

```bash
# Check formatting
./gradlew spotlessCheck

# Apply formatting
./gradlew spotlessApply
```

### Code Coverage

```bash
./gradlew jacocoTestReport
```

Reports: `build/reports/jacoco/`

The report aggregates the enabled JVM and Android host test tasks for each module that applies `kmp.jacoco`.

## Adding a New Module

1. Create the module directory:

```
core/
└── newmodule/
    ├── build.gradle.kts
    └── src/
        ├── commonMain/kotlin/
        ├── androidMain/kotlin/
        ├── iosMain/kotlin/
        └── jvmMain/kotlin/
```

2. Add to `settings.gradle.kts`:

```kotlin
include(":core:newmodule")
```

3. Configure `build.gradle.kts`:

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

## Customization

### Package Name

To change the package name from `com.sermilion.kmpstarter`:

1. Update `namespace` in all module `build.gradle.kts` files
2. Rename package directories in `src/*/kotlin/`
3. Update imports in all Kotlin files
4. Update Room entities, DAO, and builder packages in `core/data/src/*/kotlin/`

### Theme Colors

Modify `core/designsystem/src/commonMain/kotlin/.../theme/Color.kt`:

```kotlin
val Green500 = Color(0xFFA2C617)  // Primary color
val Green600 = Color(0xFF92B215)  // Primary variant
// Add your brand colors
```

### Typography

Modify `core/designsystem/src/commonMain/kotlin/.../theme/Typography.kt`:

```kotlin
val KmpTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = FontFamily.Default,  // Replace with custom font
    fontWeight = FontWeight.Normal,
    fontSize = 57.sp,
  ),
  // ...
)
```

## Tech Stack

| Category | Library |
|----------|---------|
| Language | Kotlin 2.3.10 |
| UI | Compose Multiplatform 1.10.2 |
| Async | Kotlin Coroutines 1.10.2 |
| DI | kotlin-inject |
| Database | Room 3.0.0-alpha01 |
| Network | Ktor 3.3.2 |
| Preferences | AndroidX DataStore |
| Images | Coil 3 |
| Paging | AndroidX Paging 3 |
| Testing | KoTest 5.9.1, MockK |
| Static Analysis | Detekt 1.23.8 |
| Formatting | Spotless + ktlint |

## License

```
MIT License

Copyright (c) 2024

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

Before making substantive changes, read `AGENTS.md` and `docs/ARCHITECTURE.md`, then the focused
documents under `docs/architecture/`, for repository-specific build, architecture, naming, and
testing conventions.

## Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Room KMP](https://developer.android.com/kotlin/multiplatform/room)
- [Ktor Client](https://ktor.io/docs/client.html)
- [kotlin-inject](https://github.com/evant/kotlin-inject)
