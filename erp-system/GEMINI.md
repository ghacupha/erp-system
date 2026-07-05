# ERP System Guidelines

These guidelines are scoped to the `erp-system` project. For repo-wide mandates, see the root [GEMINI.md](../GEMINI.md).

## Backend Architecture
- **Framework:** Spring Boot (JHipster).
- **Language:** Java.
- **Namespace:** `io.github.erp.erp`.

## Custom Code Separation
- **Extension Pattern:** 
    - NEVER modify generated service classes.
    - Use `Extension` suffix (e.g., `ReportSubmissionServiceExtension`).
    - Use `Prod` suffix for resources that override generated ones.
- **Custom SQL:** Place scripts in `erp-system/queries/`.

## Documentation
- **Technical Analysis:** `erp-system/man_pages/`.
- **User Stories:** `erp-system/user-stories/`.

## Entities
- **Updates:** Update `.jhipster/*.json` files for any entity schema changes.
