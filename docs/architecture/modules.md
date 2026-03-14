# Module Responsibilities and Conventions

## Core Modules

### core:common

Shared utilities available to all modules.

**Key Components:**

```kotlin
// Extension functions
fun Int?.orZero(): Int
fun <T> T?.require(): T
fun <T> Flow<T?>?.orEmpty(): Flow<T?>

// Coroutine dispatchers (interface + expect/actual)
interface DispatcherProviderContract {
  fun io(): CoroutineDispatcher
  fun main(): CoroutineDispatcher
  fun default(): CoroutineDispatcher
}

expect class DispatcherProvider() : DispatcherProviderContract
```

**Platform Implementations:**

| Platform | io() | main() | default() |
|----------|------|--------|-----------|
| Android | Dispatchers.IO | Dispatchers.Main | Dispatchers.Default |
| iOS | Dispatchers.Default | Dispatchers.Main | Dispatchers.Default |
| JVM | Dispatchers.IO | Dispatchers.Main | Dispatchers.Default |

### core:designsystem

Material 3 design system with custom theming.

**Theme Usage:**

```kotlin
@Composable
fun MyApp() {
  KmpMaterialTheme {
    val colors = KmpTheme.colors
    val kmpColors = KmpTheme.kmpColors
    val typography = KmpTheme.typography
    val shapes = KmpTheme.shapes
  }
}
```

**Typography Scale:**

- Display: Large, Medium, Small
- Headline: Large, Medium, Small
- Title: Large, Medium, Small
- Body: Large, Medium, Small
- Label: Large, Medium, Small

### core:domain

Business logic layer.

**Conventions:**

- Define repository interfaces here
- Implement use cases as single-responsibility classes
- Use `@Inject` for dependency injection
- Keep domain models framework-independent and free of data-layer persistence details

### core:data

Data layer implementation.

**Conventions:**

- Create separate repositories for distinct domain concepts instead of growing unrelated APIs on an
  existing repository
- Prefer explicit mapping between `*DataModel` types and bare domain models
- Prefer bulk insert/update operations for batch writes instead of per-item loops from callers

**Database (Room 3):**

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

**Platform Setup:**

| Platform | Builder Input |
|----------|---------------|
| Android | `Context.getDatabasePath(...)` |
| iOS | `NSFileManager` document directory |
| JVM | File path in the host JVM filesystem |

All targets use Room 3 with `BundledSQLiteDriver` for consistent SQLite behavior.

**Networking (Ktor):**

| Platform | HTTP Engine |
|----------|-------------|
| Android | OkHttp |
| iOS | Darwin |
| JVM | OkHttp |

### core:datastore

User preferences using AndroidX DataStore.

**Conventions:**

- Treat DataStore file renames as data migrations; renaming a file path without migration planning
  can strand existing persisted data

```kotlin
@Serializable
data class UserPreferences(
  val isLoggedIn: Boolean = false,
  val userId: String? = null,
)
```

### core:ui

Compose Multiplatform UI components.

**Dependencies:**

- Compose: runtime, foundation, material3, ui, resources
- Navigation: `androidx.navigation.compose`
- Image loading: Coil 3
- Paging: AndroidX Paging + Compose
- ViewModel: JetBrains Lifecycle

**Conventions:**

- Keep ViewModel-owning composables thin and hoist rendering into content composables that accept
  plain UI state
- Prefer passing stable IDs through UI events and looking up full objects from the source of truth
  instead of storing duplicated objects in presentation state
- When adding previews, prefer reusable content previews and `PreviewParameterProvider` for
  multiple states

### core:testing

Test utilities and doubles.

```kotlin
class TestDispatcherProvider(
  private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : DispatcherProviderContract {
  override fun io(): CoroutineDispatcher = testDispatcher
  override fun main(): CoroutineDispatcher = testDispatcher
  override fun default(): CoroutineDispatcher = testDispatcher
}
```

**Testing Stack:**

- Framework: KoTest (FunSpec)
- Mocking: MockK
- Coroutines: `kotlinx-coroutines-test`

**Testing Conventions:**

- Prefer Kotest assertions for readability
- Prefer MockK annotations over ad-hoc mocks where practical
- Avoid `relaxed = true`; use `relaxedUnitFun = true` only when needed

## Package Structure

```text
com.sermilion.kmpstarter/
├── common/
│   └── coroutines/
│       ├── DispatcherProviderContract
│       ├── DispatcherProvider
│       └── Flows
├── core/
│   ├── data/
│   │   └── db/
│   ├── datastore/
│   ├── designsystem/
│   │   └── theme/
│   ├── domain/
│   ├── testing/
│   └── ui/
```
