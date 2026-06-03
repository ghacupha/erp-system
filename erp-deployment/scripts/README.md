# erp-deployment/scripts

PowerShell scripts for operating the ERP deployment. All scripts are designed to be run from the `erp-deployment/` directory unless otherwise noted.

---

## ERPBackup.ps1

Backs up the ERP system's persistent state into a timestamped zip archive.

Two tiers:
- **Light** (default) — PostgreSQL dump + JHipster Registry config directories + `.env` files. No downtime. Run frequently (e.g. nightly).
- **Full** — Everything in Light, plus Elasticsearch index, Kafka queue, and Zookeeper data. Stops and restarts the Docker stack for a consistent snapshot. Run occasionally (e.g. weekly or before upgrades).

```powershell
# Light backup of the production stack (default)
.\scripts\ERPBackup.ps1

# Full backup of the production stack
.\scripts\ERPBackup.ps1 -Mode Full

# Dev stack, custom backup root
.\scripts\ERPBackup.ps1 -Stack Dev -BackupRoot 'E:\backups'

# Full dev backup
.\scripts\ERPBackup.ps1 -Mode Full -Stack Dev
```

| Parameter | Default | Description |
|---|---|---|
| `-Mode` | `Light` | `Light` or `Full` |
| `-Stack` | `Prod` | `Prod` (uses `docker-compose.yml`) or `Dev` (uses `services-dev.yml`) |
| `-BackupRoot` | `D:\backups\erp` | Root directory for archive files |
| `-PgHost` | `localhost` | PostgreSQL host |
| `-PgPort` | `5432` | PostgreSQL port |

Archives land in `<BackupRoot>/<stack>/erp-backup-<stack>-<mode>-<timestamp>.zip`.
Retention: 7 light backups, 3 full backups per stack.

**Prerequisite:** `pg_dump` must be on `PATH`.

See `man_pages/erp-backup.md` for full technical details.

---

## liquibase-compose.ps1 / liquibase-compose.cmd

Runs Liquibase database migration commands against the ERP database, resolving credentials from the running Docker container or from environment variables.

```powershell
# Show pending changesets
.\scripts\liquibase-compose.ps1 -Command status

# Apply all pending changesets (production profile)
.\scripts\liquibase-compose.ps1 -Command update -Profile prod

# Apply against dev database
.\scripts\liquibase-compose.ps1 -Command update -Profile dev

# Preview SQL without applying
.\scripts\liquibase-compose.ps1 -Command updateSQL -Profile prod
```

| Parameter | Default | Description |
|---|---|---|
| `-Command` | `status` | Liquibase goal: `status`, `update`, `updateSQL`, `validate`, `history`, `releaseLocks`, `clearCheckSums`, `changelogSync`, `changelogSyncSQL` |
| `-Profile` | `prod` | `prod` or `dev` — selects database credentials |
| `-ComposeFile` | `docker-compose.yml` | Path to the compose file used to locate the running container |
| `-ComposeService` | `erp-system-server` | Service name to inspect for credentials |
| `-Contexts` | same as `-Profile` | Liquibase context filter |
| `-DryRun` | false | Print the Maven command without executing it |

`liquibase-compose.cmd` is a thin CMD wrapper that delegates to the `.ps1` file, for use in environments where PowerShell execution policy is restrictive.

---

## normal-startup.ps1

Sets environment variables in the current PowerShell session to disable cache rebuilding and index rebuilding on application startup. Use this before starting the application for a routine restart where no data refresh is needed.

```powershell
. .\scripts\normal-startup.ps1   # dot-source to apply to current session
```

Sets:
- `ERP_CACHE_REBUILD_ENABLED=FALSE`
- `ERP_CACHE_TEARDOWN_REBUILD_ENABLED=FALSE`
- `ERP_INDEX_ENABLED=FALSE`
- `ERP_INDEX_REBUILD_ENABLED=FALSE`

---

## enable-index-refresh-startup.ps1

Sets environment variables in the current PowerShell session to enable full cache and index rebuilding on the next application startup. Use this after a bulk data import, a reindex operation, or when the search index is suspected to be stale.

```powershell
. .\scripts\enable-index-refresh-startup.ps1
```

Sets:
- `ERP_CACHE_REBUILD_ENABLED=TRUE`
- `ERP_CACHE_TEARDOWN_REBUILD_ENABLED=TRUE`
- `ERP_INDEX_ENABLED=TRUE`
- `ERP_INDEX_REBUILD_ENABLED=TRUE`

---

## show-startup-mode.ps1

Prints the current values of the four startup-mode flags in the current session. Useful for confirming which mode is active before starting the application.

```powershell
. .\scripts\show-startup-mode.ps1
```

---

---

## Notes

### Angular client — charting library

The prepayments dashboard (added in `erp-client/src/main/webapp/app/erp/erp-prepayments/prepayments-dashboard/`) depends on `ng2-charts` and `chart.js`. These are listed in `erp-client/package.json` but must be installed before the client will build:

```powershell
cd erp-client
npm install
```

---

## Related scripts (erp-system/scripts/)

Scripts for developer workflows live in `erp-system/scripts/`:

| Script | Purpose |
|---|---|
| `ERPDevUp.ps1` | Start the full dev environment: Docker infrastructure, Maven (Spring Boot), and Angular client |
| `generate-entity.ps1` | Scaffold a new JHipster entity (JSON + code generation) |
| `remove-entity.ps1` | Remove a JHipster entity and its generated files |
| `liquibase-compose.ps1` | Same as the deployment version — also present in the system module |
| `build-it.cmd` | Build the Spring Boot application |
| `maintain-entities.cmd` | Batch entity maintenance helper |
| `rollback-liquibase-changelog` | Roll back the most recent Liquibase changeset |
