# Agent Guidelines

This system is generated with JHipster. Follow these conventions when contributing:

1**Commit Messages**

   - Use a short summary in the imperative mood (max 72 characters).

2. **Documentation**

   - Write a detailed analysis and review of workflows that have been implemented or changed for the report-submission service, and the reasons like in a case study
   - and include in both this system's or the client's or deployment project man_pages folder (if present, create one if note) and the main project's man_pages.
   - If the workflow can also be written as user stories also write a document and place it in the main project's user-stories folder as well as the system's corresponding subfolder (either the client, the server or the deployment project)
   - update the user's manual file (or create one if missing) on the user-pages subfolder for every feature we have, for each we have modified or added and the workflows or procedure changes
   - Always update the main .gitignore file with any sensitive files from the subfolders and even the main project folder
   - create an integrated test, to the extent that it's possible to test workflows in the user stories
   - detailing workflows from the user navigating to the appropriate frontend components and all the way to the related services involved in report submission
   - for workflows that involve custom queries, create an sql file showing the relevant postgresql sql query, and include in the queries forlder under erp-system subfolder, just for review purposes and side tests against the actual database  

3. **Project Structure**
   - Keep files in the standard JHipster layout and naming conventions for the report-submission service.
   - Reflect entity field changes in respective `/.jhipster/*.json` definitions whenever you add or remove fields.
   - Place custom logic, services, repositories, resources and others in `io.github.erp.erp` package with corresponding packages such as services, resources, repository and if possible with the `Extension` suffix (e.g. `ReportSubmissionServiceExtension`) and inject those into resources instead of altering the generated services directly.

Always make sure the project compiles and formatting checks pass before opening a PR.
