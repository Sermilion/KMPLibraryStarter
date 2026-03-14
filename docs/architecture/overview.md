# Architecture Overview

## Overview

KMPLibraryStarter is a starter template for building cross-platform UI libraries using Compose
Multiplatform. It follows Clean Architecture principles with clear separation between data, domain,
and UI/presentation responsibilities.

## Supported Platforms

| Platform | SDK/Version | Notes |
|----------|-------------|-------|
| Android | minSdk 26, compileSdk 36 | Jetpack Compose |
| iOS | arm64, simulatorArm64 | Compose Multiplatform |
| JVM | Java 11 bytecode target | Build with JDK 17 |

## Module Structure

```text
KMPLibraryStarter/
├── build-logic/           # Gradle convention plugins
│   └── convention/        # Shared build configuration
├── core/
│   ├── common/            # Shared utilities and extensions
│   ├── data/              # Data layer (repositories, database, network)
│   ├── datastore/         # User preferences and settings
│   ├── designsystem/      # UI theme, colors, typography
│   ├── domain/            # Business logic and use cases
│   ├── testing/           # Test utilities and doubles
│   └── ui/                # Compose UI components
├── config/                # Detekt configuration
└── gradle/                # Version catalog
```

## Module Dependencies

```text
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

### Dependency Rules

- `api`: exposed to consumers transitively
- `implementation`: internal only

| Module | Dependencies |
|--------|--------------|
| `core:ui` | api: designsystem, impl: domain, common |
| `core:domain` | api: common, paging, serialization |
| `core:data` | api: domain, impl: common, datastore |
| `core:designsystem` | Compose libraries only |
| `core:datastore` | DataStore, Serialization |
| `core:common` | Coroutines, DateTime |
| `core:testing` | api: common, KoTest, Coroutines Test |

## Naming Conventions

### Model Naming

- Data-layer models use the entity name plus `DataModel` suffix, for example `UserDataModel`.
- Domain-layer models use only the entity name, for example `User`.
- Presentation/UI-layer models use the entity name plus `UiModel` suffix, for example `UserUiModel`.

## Key Decisions

1. **Compose Multiplatform** over platform-specific UI for maximum code sharing
2. **kotlin-inject** over runtime DI for compile-time safety and multiplatform support
3. **Room 3** for a coroutine-first multiplatform database API
4. **Ktor** for multiplatform networking
5. **KoTest** for expressive Kotlin-first tests across supported targets
6. **Convention plugins** over duplicated module build logic
