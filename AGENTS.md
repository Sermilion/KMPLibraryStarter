# Agent Guidelines for Working with This Repository

This starter is meant to stay reusable and production-ready. Treat build logic, version catalog changes,
documentation, and generated library surfaces as template-level decisions rather than one-off app changes.

## Essential Build Commands

### Build and Verification

- Run the main quality gate: `./gradlew check`
- Build all modules: `./gradlew build`
- Format Kotlin and Gradle files: `./gradlew spotlessApply`
- Run static analysis only: `./gradlew detekt`

### Platform-Specific Tasks

- Build the shared UI framework for iOS simulator on Apple Silicon:
  `./gradlew :core:ui:linkIosSimulatorArm64`
- Build the shared UI framework for iOS device:
  `./gradlew :core:ui:linkIosArm64`
- Build the Android debug variant of a module:
  `./gradlew :core:ui:assembleAndroidMain`

## Project Architecture

### Module Structure

The repository is organized as a Kotlin Multiplatform starter with shared convention plugins:

- `build-logic/convention`: reusable Gradle convention plugins
- `core:common`: shared utilities, dispatchers, extensions
- `core:domain`: domain contracts and business logic
- `core:data`: Room 3, networking, and repository implementations
- `core:datastore`: preference and settings persistence
- `core:designsystem`: shared Compose theme and tokens
- `core:ui`: Compose Multiplatform UI surface
- `core:testing`: shared test helpers

### Architectural Expectations

- Keep module boundaries clean: `domain` should stay framework-light, `data` should implement contracts,
  and `ui` should depend on shared abstractions rather than platform-specific shortcuts.
- Prefer shared configuration in `build-logic` and `gradle/libs.versions.toml` over duplicated per-module setup.
- Keep starter code generic and reusable. Avoid product-specific naming, secrets, endpoints, or workflows.

## Dependency and Build Guidance

- Manage dependency versions through `gradle/libs.versions.toml`.
- Keep plugin and library versions aligned with Kotlin/Compose compatibility requirements.
- Build the repository with JDK 17, but keep published Android/JVM bytecode targets on Java 11 unless you are deliberately raising the starter's minimum runtime requirement.
- Prefer stable repositories only. Avoid adding `mavenLocal()`, JitPack, or preview feeds unless a dependency
  truly requires them and the reason is documented.
- If build logic changes, re-run `./gradlew check` from the repository root.

## Testing and Quality Standards

- `./gradlew check` is the default release gate for this starter.
- Do not add suppressions to hide build, lint, or test failures unless the template intentionally documents them.
- MockK is available for Android and JVM tests. For iOS tests, prefer manual fakes or wire in Mokkery only when
  the starter actually needs multiplatform mocking coverage.
- Prefer Kotest assertions and MockK annotations over ad-hoc `mockk()` setup where practical. Avoid `relaxed = true`;
  use `relaxedUnitFun = true` only when the test genuinely needs it.
- When adding repository APIs, prefer both single-item and bulk write operations instead of forcing callers into
  item-by-item loops.
- Treat DataStore file renames as migrations. Renaming a file without a migration plan can strand existing user data.
- When changing docs that affect contributor workflows, update `README.md` and this file together.

## Automation

- CI is defined in `.github/workflows/check.yml` and should stay fast and deterministic.
- CI should continue verifying both the main Gradle quality gate and at least one iOS framework
  link task on macOS so multiplatform breakages are caught early.
- Dependabot is configured for Gradle and GitHub Actions updates in `.github/dependabot.yml`.
- Keep workflows template-safe: no organization-specific secrets, deployment steps, or proprietary checks.

## Agent Workflow Expectations

- Read this file before making structural changes.
- Read all Markdown files under `docs/` before making substantive repository changes. Treat `docs/ARCHITECTURE.md` as the required entry point, then read the focused architecture files under `docs/architecture/` for architecture, module-boundary, and design decisions.
- Explain and document template-wide decisions, especially dependency upgrades and repository policy changes.
- Prefer passing stable IDs instead of full objects through UI events and presentation state when a deeper layer remains
  the source of truth.
- For Compose UI, keep ViewModel-backed entry points thin and hoist render state into previewable content composables
  where practical.
- Prefer surgical changes over broad churn, but complete the root fix when you touch build logic or starter docs.
