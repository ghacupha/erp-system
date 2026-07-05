# ERPBackup — Backup Script Technical Analysis

See the primary copy at `erp-deployment/man_pages/erp-backup.md`.

## Summary

`erp-deployment/scripts/ERPBackup.ps1` provides two-tier backup for the ERP system:

- **Light** (frequent, no downtime): PostgreSQL dump + config files + `.env` files → timestamped zip. Keeps 7 most recent archives per stack.
- **Full** (occasional, brief downtime): Light + Elasticsearch index + Kafka queue + Zookeeper data → timestamped zip. Keeps 3 most recent archives per stack.

The `-Stack [Prod|Dev]` parameter switches between `docker-compose.yml` (prod) and `services-dev.yml` (dev) paths and credentials.

Default backup root: `D:\backups\erp\<stack>\`.
