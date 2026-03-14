# Feature: agp9-gradle9-modernization
Created: 2026-03-14
Status: Complete
Sources: User conversation, AGP 9 release notes, Android-KMP migration docs, KSP quickstart, repository build files

## Acceptance Criteria
1. Upgrade the Android/Gradle toolchain to AGP 9.x and Gradle 9.x using versions that remain compatible with the repository's Kotlin Multiplatform setup.
2. Migrate shared KMP modules away from the legacy `com.android.library` + `androidTarget` integration to the supported Android-KMP library plugin configuration.
3. Modernize custom convention plugins and root build logic so they no longer depend on AGP's deprecated legacy DSL types or legacy Android/Kotlin wiring.
4. Preserve Android, JVM, and iOS shared-module behavior, including KSP-based code generation, Compose resources, tests, linting, and coverage wiring where supported by the new plugin model.
5. Verify the migration with repository checks, fixing migration-caused failures and documenting any remaining pre-existing blockers that are outside the modernization scope.

## Non-goals
- Product feature development unrelated to the build migration
- Cleaning unrelated user changes already present in the worktree
- Broad refactors outside of what is required for AGP 9 / Gradle 9 readiness

## Open Questions
None. The user requested a full in-place modernization without creating a branch.

## Notes
- This is a large migration because it touches shared convention plugins, the version catalog, the Gradle wrapper, and every shared KMP module.
- The repository currently has unrelated modified and untracked files; the migration must stay surgical and avoid reverting or disturbing them.
