# ERP Client Guidelines

These guidelines are scoped to the `erp-client` project. For repo-wide mandates, see the root [GEMINI.md](../GEMINI.md).

## Frontend Architecture
- **Framework:** Angular.
- **Language:** TypeScript.
- **Custom Code:** Resides in the `erp` subfolder under `src/app`.

## Documentation
- **Technical Analysis:** `erp-client/man_pages/`.
- **User Stories:** `erp-client/user-stories/`.
- **User Manual:** `user-pages/` (at project root).

## Standards
- **Generated Code:** NEVER modify generated components or services directly.
- **Extensions:** Follow the same extension pattern as the backend where applicable (e.g., wrapper components or services).
- **Dependencies:** Avoid modifying `package-lock.json` unless necessary.
