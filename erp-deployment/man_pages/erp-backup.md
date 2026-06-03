# ERPBackup — Backup Script Technical Analysis

## Purpose

`ERPBackup.ps1` provides two-tier backup coverage for the ERP system's persistent state:
- **Light** backups capture the relational database and configuration files. They are fast, run without downtime, and should be scheduled frequently (e.g. nightly).
- **Full** backups add the Elasticsearch index, Kafka message queue, and Zookeeper coordination data. They require a brief service outage and are run occasionally (e.g. weekly or before major upgrades).

---

## Components Backed Up

### Light mode

| Item | Source |
|---|---|
| PostgreSQL dump | `pg_dump -F c` against the local PG instance |
| JHipster Registry config (prod) | `erp-deployment/central-server-config/` |
| JHipster Registry config (dev) | `erp-deployment/central-server-dev-config/` |
| Deployment env | `erp-deployment/.env` |
| System env | `erp-system/.env` |

Both config directories are always included regardless of `-Stack`, because they are small and together represent the full configuration surface.

### Full mode (adds the following)

| Item | Prod path | Dev path |
|---|---|---|
| Elasticsearch data | `search-index-directory/` | `search-index-dev-directory/` |
| Kafka data | `kafka-queue/` | `kafka-dev-queue/` |
| Zookeeper data | `zookeeper-data/` | `zookeeper-dev-data/` |
| Zookeeper logs | `zookeeper-logs/` | `zookeeper-dev-logs/` |

---

## Parameters

| Parameter | Default | Description |
|---|---|---|
| `-Mode` | `Light` | `Light` or `Full` — selects backup tier |
| `-Stack` | `Prod` | `Prod` (uses `docker-compose.yml`) or `Dev` (uses `services-dev.yml`) |
| `-BackupRoot` | `D:\backups\erp` | Root directory for all archive files |
| `-PgHost` | `localhost` | PostgreSQL host (the local server, not Docker) |
| `-PgPort` | `5432` | PostgreSQL port |

---

## Execution Flow

### Light backup

1. Load `.env` files (deployment, then system).
2. Run `pg_dump -F c` (custom format) with credentials from env vars. Sets `PGPASSWORD` only for the duration of the call and clears it immediately after.
3. Copy both config directories and both `.env` files into a temp staging folder.
4. Compress the staging folder into a timestamped zip under `<BackupRoot>/<stack>/`.
5. Prune archives beyond the retention limit (keep 7 most recent light backups for the selected stack).

### Full backup

Steps 1–3 from Light, then:

4. Run `docker-compose -f <composeFile> down` to stop the selected stack (brief downtime).
5. Inside a `try/finally` block, copy ES, Kafka, and Zookeeper data directories. The `finally` block ensures `docker-compose up -d` is always called to restart services even if a copy step fails.
6. Compress to zip, prune (keep 3 most recent full backups for the selected stack).

---

## Compose file mapping

| `-Stack` | Compose file | Notes |
|---|---|---|
| `Prod` | `docker-compose.yml` | Full production stack — Zookeeper, Kafka, Registry, ES, app containers |
| `Dev` | `services-dev.yml` | Dev infrastructure only — no app container |

---

## Archive naming and layout

```
D:\backups\erp\
  prod\
    erp-backup-prod-light-20250602_020000.zip
    erp-backup-prod-full-20250525_030000.zip
  dev\
    erp-backup-dev-light-20250602_020000.zip
```

Each zip contains:
```
db/
  erpSystem_<timestamp>.dump   (or erpSystemDev_<timestamp>.dump for Dev)
config/
  central-server-config/
  central-server-dev-config/
  deployment.env
  system.env
volumes/                        (Full mode only)
  elasticsearch/
  kafka/
  zookeeper-data/
  zookeeper-logs/
```

---

## Retention policy

| Mode | Archives kept |
|---|---|
| Light | 7 most recent per stack |
| Full | 3 most recent per stack |

Pruning runs after each successful backup. Archives are sorted by `LastWriteTime` descending; those beyond the limit are deleted.

---

## Prerequisites

- `pg_dump` must be on `PATH` (install PostgreSQL client tools).
- Docker and `docker-compose` must be available for Full mode.
- The `.env` file in `erp-deployment/` must contain `PG_DATABASE_PROD_USER`, `PG_DATABASE_PROD_PASSWORD`, and `ERP_SYSTEM_PROD_DB` (or their `DEV` equivalents for the Dev stack).

---

## Usage examples

```powershell
# Nightly light backup of the production stack
.\scripts\ERPBackup.ps1

# Weekly full backup of production
.\scripts\ERPBackup.ps1 -Mode Full

# Light backup of the dev stack to a network share
.\scripts\ERPBackup.ps1 -Stack Dev -BackupRoot '\\fileserver\erp-backups'

# Full dev backup with non-default PG port
.\scripts\ERPBackup.ps1 -Mode Full -Stack Dev -PgPort 5433
```

---

## Scheduling

To run nightly light backups automatically, register a Windows Scheduled Task:

```powershell
$action  = New-ScheduledTaskAction -Execute 'pwsh' `
               -Argument '-NonInteractive -File "D:\labs\erp\erp-deployment\scripts\ERPBackup.ps1"' `
               -WorkingDirectory 'D:\labs\erp\erp-deployment'
$trigger = New-ScheduledTaskTrigger -Daily -At '02:00'
Register-ScheduledTask -TaskName 'ERPLightBackup' -Action $action -Trigger $trigger -RunLevel Highest
```

---

## Design decisions

**Why `pg_dump -F c` (custom format)?** Custom format is compressed, supports parallel restore via `pg_restore`, and is more reliable than plain SQL for large databases.

**Why stop all services for Full mode?** Elasticsearch and Kafka maintain open write-ahead logs and segment files. A filesystem copy of a running instance risks copying partially-flushed pages, producing a corrupt archive. A brief outage is the safe path. PostgreSQL is exempted because `pg_dump` is a logical dump that produces a consistent snapshot regardless of concurrent writes.

**Why `try/finally` around the copy step?** If a copy fails halfway (e.g. disk full), the finally block guarantees services are restarted rather than left stopped.

**Why two separate `.env` files?** `erp-deployment/.env` holds infrastructure and Docker-compose variables; `erp-system/.env` holds Spring Boot runtime variables including the Elasticsearch password. Both are needed for a complete restore.
