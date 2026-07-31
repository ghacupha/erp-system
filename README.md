# ERP System

A full-stack enterprise resource planning platform built with [JHipster](https://www.jhipster.tech/) 7.3.1.  
Back-end: Spring Boot 2.6 · Front-end: Angular 13 · Database: PostgreSQL · Search: Elasticsearch 7.13 · Messaging: Kafka.

---

## Repository layout

```
erp-system/       Spring Boot back-end (Maven, Java)
erp-client/       Angular front-end (Node / npm)
erp-deployment/   Docker Compose files, infrastructure config, and operational scripts
man_pages/        Technical write-ups and design decisions
user-stories/     End-user workflow descriptions
user-pages/       User-facing how-to guides
```

---

## Prerequisites

| Tool | Minimum version | Notes |
|---|---|---|
| Java (JDK) | 11 | Set `JAVA_HOME` |
| Node.js | 16 | Only needed for local Angular development |
| npm | 8 | Only needed for local Angular development |
| Docker Desktop | 4.x | Runs PostgreSQL-adjacent services |
| docker-compose | 1.29+ | Bundled with Docker Desktop |
| PostgreSQL client | 13+ | `pg_dump` / `psql` must be on `PATH` |
| PowerShell | 7+ (`pwsh`) | Required for all `.ps1` scripts |

---

## Development quick start

1. **Copy and fill in the environment files**

   ```powershell
   Copy-Item erp-deployment\.env-sample erp-deployment\.env
   Copy-Item erp-system\.env-sample    erp-system\.env   # if present
   ```

   Edit both files and set real values for all credentials (database passwords, JWT secret, Elasticsearch password, etc.).

2. **Optional: install Angular dependencies for local frontend work**

   ```powershell
   cd erp-client
   npm install
   cd ..
   ```

   > `chart.js` and `ng2-charts` are included in `package.json` and will be installed by this command. You only need this step if you also plan to run or build the frontend outside Docker.

3. **Start everything in Docker**

   ```powershell
   .\scripts\ErpDevBuildUp.ps1
   ```

   This will:
   - Build the backend image with Maven Jib and the frontend image from its Dockerfile
   - Start the dev Docker stack defined in `erp-deployment/docker-compose-dev.yml`
   - Run the dev JHipster Registry, Elasticsearch, Kafka, ZooKeeper, backend, and frontend in containers

   If you only need to restart containers after a previous build, use:

   ```powershell
   .\scripts\ErpDevUp.ps1
   ```

4. **Access the application**

   | Service | URL |
   |---|---|
   | Angular UI | http://localhost:8983 |
   | Spring Boot API | http://localhost:8982 |
   | JHipster Registry | http://localhost:8771 |
   | Elasticsearch | http://localhost:8840 |

---

## Production deployment

The production stack is defined in `erp-deployment/docker-compose.yml`.

1. **Populate `erp-deployment/.env`** with production credentials (copy from `.env-sample`).

2. **Start all services**

   ```powershell
   .\scripts\ErpUp.ps1
   ```

   The stack includes: JHipster Registry, Elasticsearch, Kafka, Zookeeper, the Spring Boot server (`erp-system-server`), and the Angular client container (`erp-client-web`).  
   PostgreSQL runs on the host; the application connects via `LOCAL_PG_SERVER` from the `.env` file.

3. **Apply database migrations** (first run or after upgrades)

   ```powershell
   .\scripts\liquibase-compose.ps1 -Command update -Profile prod
   ```

---

## Configuration

Runtime configuration is served by the JHipster Registry acting as a Spring Cloud Config server.

| File | Purpose |
|---|---|
| `erp-deployment/central-server-config/localhost-config/application-dev.yml` | Config served to Spring apps requesting the `dev` profile |
| `erp-deployment/central-server-config/localhost-config/application-prod.yml` | Config served to Spring apps requesting the `prod` profile |
| `erp-deployment/central-server-dev-config/localhost-config/application-dev.yml` | Config served by the dev-only Registry container |
| `erp-system/src/main/resources/config/bootstrap.yml` | Spring Cloud Config client bootstrap (dev) — points to Registry |
| `erp-system/src/main/resources/config/bootstrap-prod.yml` | Spring Cloud Config client bootstrap (prod) |

The JHipster Registry port defaults to **8771** (dev) and **8761** (prod). Override via `JHIPSTER_REGISTRY_PORT` in the `.env` file.

---

## Backup

`erp-deployment/scripts/ERPBackup.ps1` provides two-tier backup.

### Light backup — frequent, no downtime

Backs up the PostgreSQL database (logical dump) and all configuration files.

```powershell
# From erp-deployment/
.\scripts\ERPBackup.ps1
```

### Full backup — occasional, brief downtime

Stops the Docker stack, adds Elasticsearch, Kafka, and Zookeeper data to the archive, then restarts.

```powershell
.\scripts\ERPBackup.ps1 -Mode Full
```

### Common options

| Parameter | Default | Description |
|---|---|---|
| `-Mode` | `Light` | `Light` or `Full` |
| `-Stack` | `Prod` | `Prod` (`docker-compose.yml`) or `Dev` (`services-dev.yml`) |
| `-BackupRoot` | `D:\backups\erp` | Destination directory for archives |
| `-PgHost` | `localhost` | PostgreSQL host |
| `-PgPort` | `5432` | PostgreSQL port |

Archives are written to `<BackupRoot>/<stack>/erp-backup-<stack>-<mode>-<timestamp>.zip`.  
Retention: 7 light backups and 3 full backups per stack are kept; older archives are pruned automatically.

### Scheduling nightly light backups

```powershell
$action  = New-ScheduledTaskAction `
               -Execute 'pwsh' `
               -Argument '-NonInteractive -File "D:\labs\erp\erp-deployment\scripts\ERPBackup.ps1"' `
               -WorkingDirectory 'D:\labs\erp\erp-deployment'
$trigger = New-ScheduledTaskTrigger -Daily -At '02:00'
Register-ScheduledTask -TaskName 'ERPLightBackup' -Action $action -Trigger $trigger -RunLevel Highest
```

See `erp-deployment/scripts/README.md` for all scripts and `erp-deployment/man_pages/erp-backup.md` for full backup design notes.

---

## Startup modes

Before starting `erp-system-server`, dot-source one of these scripts in the same PowerShell session to control cache and index behaviour:

```powershell
# Routine restart — no rebuild
. .\erp-deployment\scripts\normal-startup.ps1

# After bulk import or suspected stale index — rebuild everything
. .\erp-deployment\scripts\enable-index-refresh-startup.ps1

# Check current flags
. .\erp-deployment\scripts\show-startup-mode.ps1
```

---

## Developer scripts

| Script | Purpose |
|---|---|
| `scripts/ErpDevBuildUp.ps1` | Build and start the full dev Docker environment |
| `scripts/ErpDevUp.ps1` | Start the dev Docker environment without rebuilding |
| `scripts/ErpUp.ps1` | Start the production Docker environment |
| `scripts/ErpDevDown.ps1` | Stop the dev Docker environment |
| `scripts/ErpDown.ps1` | Stop the production Docker environment |
| `generate-entity.ps1` | Scaffold a new JHipster entity |
| `remove-entity.ps1` | Remove an entity and its generated files |
| `liquibase-compose.ps1` | Run Liquibase commands against the dev or prod database |
| `rollback-liquibase-changelog` | Roll back the most recent Liquibase changeset |

---

## Documentation

| Location | Contents |
|---|---|
| `man_pages/` | Technical analysis and design decision write-ups |
| `user-stories/` | End-user workflow descriptions by feature |
| `user-pages/` | How-to guides for end users |
| `erp-deployment/man_pages/` | Deployment and infrastructure write-ups |
| `erp-system/man_pages/` | Back-end module write-ups |
| `erp-client/man_pages/` | Front-end module write-ups |

---

## License

GNU General Public License v3.0 — see individual source files for the full notice.
