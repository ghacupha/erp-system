# Compose Environment Liquibase Runner

## Purpose

Production Liquibase checks should use the same database URL, database name, username, and password that the production container receives from Docker Compose. The runner script in `erp-system/scripts/liquibase-compose.cmd` inspects the `erp-system-server` service environment, resolves the Liquibase connection, and invokes the Maven Liquibase plugin with the project changelog.

## Usage

Run from either `erp-system` or `erp-deployment`:

```cmd
scripts\liquibase-compose.cmd -Command status -Profile prod -DryRun
scripts\liquibase-compose.cmd -Command status -Profile prod
scripts\liquibase-compose.cmd -Command updateSQL -Profile prod
scripts\liquibase-compose.cmd -Command releaseLocks -Profile prod
```

The deployment copy of the script still locates `erp-system` and runs Maven there, because the Liquibase changelog and Maven wrapper live in the server module.

The script prefers `SPRING_LIQUIBASE_URL`, then `SPRING_DATASOURCE_URL`, then `LOCAL_PG_SERVER` plus `ERP_SYSTEM_PROD_DB` or `ERP_SYSTEM_DEV_DB`. Credentials are resolved from Spring Liquibase, Spring datasource, or the profile-specific PostgreSQL variables used by the compose file.

## Safety Notes

Use `-DryRun` before mutating commands to confirm the resolved URL and username. The script writes credentials to a temporary Liquibase properties file and removes it after the Maven command exits, which avoids exposing the password as a Maven command-line argument.

`releaseLocks` should be used only after checking that no ERP server instance is currently migrating the same database.
