# ERP Project Guidelines

These guidelines are foundational mandates for development within this workspace. They apply to both the `erp-system` (back-end) and `erp-client` (front-end) projects.

- [ERP Client Guidelines (erp-client/GEMINI.md)](./erp-client/GEMINI.md)
- [ERP System Guidelines (erp-system/GEMINI.md)](./erp-system/GEMINI.md)

## Architecture & Frameworks
- **Framework:** Both modules are generated with JHipster.
- **Backend:** Spring Boot, Java.
- **Frontend:** Angular, TypeScript.
- **Database:** PostgreSQL.

## Coding Standards & Patterns
- **Standard JHipster Layout:** Adhere strictly to the generated project structure and naming conventions.
- **Custom Code Separation:** 
    - NEVER modify generated service classes directly. 
    - Implement custom logic in classes suffixed with `Extension` (e.g., `ReportSubmissionServiceExtension`).
    - Place these in appropriate subpackages (`service`, `repository`, `web.rest`, etc.) under the `io.github.erp.erp` namespace for the backend.
    - For the frontend, custom code resides in the `erp` subfolder under `app`.
    - Inject extension classes into resources. A resource that is a copy of the generated resource (named with the `Prod` suffix) should be used for production code.
- **Dependency Management:** Do not modify generated code unless absolutely necessary, including `package-lock.json`, to ensure version consistency.

## Documentation Workflow
Every significant feature must be documented in two ways:

### 1. Technical Analysis (`man_pages/`)
- **Content:** Workflow analysis, design decisions, business purpose, key components, and development notes.
- **Location:** 
    - `man_pages/` at the project root.
    - `man_pages/` within the specific module (e.g., `erp-system/man_pages/`).
- **Organization:** Subfolders based on topic (e.g., `report-submission/`, `lease-liability/`).

### 2. User Stories & Manuals
- **User Stories (`user-stories/`):** 
    - **Content:** Personas, UI steps (pages, buttons), and expected outcomes.
    - **Location:** `user-stories/` at the root and within the module's `user-stories/` folder.
- **User Manual (`user-pages/`):** 
    - **Content:** How-to guides for using features in practice.
    - **Action:** Update or create a page in `user-pages/` for every modified or introduced feature.

## Development Tasks
- **Entity Definitions:** If fields are added, removed, or renamed on a JHipster entity, update the corresponding `.jhipster/*.json` file.
- **Custom SQL:** Place PostgreSQL scripts in `erp-system/queries/` for review and manual testing.
- **Security:** Update the top-level `.gitignore` for any new sensitive configuration or credentials.

## Quality Assurance & Testing
- **Local Verification:** Run unit tests, formatting, and linting before submitting changes.
- **Integration Tests:** Write tests that follow the user stories, covering the full workflow from frontend to backend.
- **Limitations:** If full test coverage is not feasible, document the limitations alongside the user stories.

## Commit Message Standards
- **Format:** Short imperative summary (max 72 characters) followed by an optional body.
- **Body:** Explain the rationale and reference relevant documentation (`man_pages`, `user-stories`, etc.).
- **Example:** `Add audit log to report submission`
