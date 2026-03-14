# KMPLibraryStarter Architecture Docs

Start here for repository architecture and contributor conventions.

This documentation is split into focused files so agents and contributors can read the parts they
need without parsing one very large document.

## Reading Order

1. `docs/architecture/overview.md`
2. `docs/architecture/modules.md`
3. `docs/architecture/build-and-tooling.md`
4. `docs/architecture/extending-and-consuming.md` as needed

## Contents

### Overview

- Repository purpose and supported platforms
- Module structure and dependency boundaries
- Model naming conventions
- Key architectural decisions

Read: `docs/architecture/overview.md`

### Modules

- Responsibilities of each core module
- Data, domain, datastore, UI, and testing conventions
- Package structure expectations

Read: `docs/architecture/modules.md`

### Build and Tooling

- Convention plugins and version catalog usage
- Dependency injection setup
- Static analysis, formatting, and coverage
- Development setup and important configuration files

Read: `docs/architecture/build-and-tooling.md`

### Extending and Consuming

- How to add a new module with the current Android-KMP setup
- How consumers integrate the published library on Android, iOS, and JVM

Read: `docs/architecture/extending-and-consuming.md`
