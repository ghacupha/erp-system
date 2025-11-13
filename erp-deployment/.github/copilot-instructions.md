## Quick context

This repository is a deployment/orchestration repo for the Erp System (JHipster-based microservices). It mainly contains Docker Compose stacks, runtime configuration and persistent folders (logs, data, indexes). Many services are run from pre-built Docker images (e.g. `ghacupha/erp-system`, `ghacupha/erp-client`).

## Big-picture architecture (what an agent must know)
- Composition: `docker-compose.yml` brings up infrastructure for the ERP (Kafka + Zookeeper, Elasticsearch, JHipster Registry, the ERP server + client UI, plus ancillary stacks under separate yml files).
- Config server: `jhipster-registry` is configured to read local files from `central-server-config/localhost-config/` in dev mode. See `docker-compose.yml` environment keys starting with `SPRING_CLOUD_CONFIG_SERVER_COMPOSITE` for details.
- Messaging: Kafka is used for inter-service communication. The `kafka` service advertises listeners on `kafka:29092` for internal containers; the ERP server uses `SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS=kafka:29092`.
- Persistence & state: persistent volumes/dirs in repo include `postgresql-data/`, `kafka-queue/`, `search-index-directory/`, `logs/`, and `business-documents/` (sample .erp files live here). Avoid destructive edits to these unless intended.

## Developer workflows and important commands
- Primary local start (documented in top-level `README`):
  - `docker-compose pull && docker-compose up -d && docker system prune`
- On Windows use the included scripts: `launch-apps.cmd` or `launch-default.cmd` (these wrap the compose commands in a Windows-friendly way).
- If you need to change service configuration, prefer editing `central-server-config/localhost-config/` (the registry is configured to read that in dev mode) rather than modifying container images.

## Project-specific patterns & conventions
- Images: production/dev images are referenced explicitly (e.g. `ghacupha/erp-system:1.8.3`) — the deployment repo expects images to be published elsewhere; do not assume a local build will overwrite these images unless CI or local build steps are added.
- Environment variables: `docker-compose.yml` uses `${VAR}` placeholders extensively (e.g. `SPRING_DATASOURCE_URL=${LOCAL_PG_SERVER}/${ERP_SYSTEM_PROD_DB}`). Agents should search for variable usage before replacing values and prefer adding to a `.env` or to the caller script instead of hardcoding secrets.
- Network: all services attach to `erp-system-network`. When adding services, ensure the same network name and compatible service names for internal DNS (e.g. `kafka`, `jhipster-registry`).
- Service dependencies: use `depends_on` where appropriate (see `kafka -> zookeeper` and `erp-system-server -> kafka`). Agents editing boot order should respect these dependencies.

## Integration points to watch
- JHipster Registry: `http://admin:admin@jhipster-registry:8761` (service discovery and config). Changing registry config affects many services.
- Kafka topics & storage: `kafka-queue/` holds Kafka data; changes to partitioning/replication must be coordinated with the Kafka image/version.
- Elasticsearch: `erpsystem-elasticsearch` stores indexes under `search-index-directory/`. Reindexing or mapping changes may be triggered by the ERP app (see env keys `ERP_INDEX_*`).

## Concrete examples an agent can act on
- To find service config examples: open `central-server-config/localhost-config/`.
- To add a new microservice to the deployment:
  1. Add a new service block to `docker-compose.yml` using the same `erp-system-network` network.
  2. Use internal hostnames (e.g. `jhipster-registry`) for inter-service env values.
  3. If the service needs config from the config server, add appropriate `SPRING_CLOUD_CONFIG_URI` or place config under `central-server-config/localhost-config/`.

## Rules for agents when modifying this repo
- Preserve existing volume mounts and data directories unless the change explicitly requires them to be removed.
- Never hardcode secrets (passwords, JWT secrets) in repo files — use env variables and document any required `.env` entries.
- When changing service images or tags, also update `README` and `launch-*.cmd` scripts so humans see the change immediately.
- Prefer conservative edits: add a new service in a commented block first and run tests/compose locally (or describe how to run) before enabling in the default stack.

## Where to look next
- `docker-compose.yml` (root) — primary orchestration and env-var usage
- `central-server-config/localhost-config/` — service configuration overrides used by JHipster Registry in dev
- `business-documents/` and `kafka-queue/` — sample data and persistent queues
- `logs/`, `postgresql-data/`, `search-index-directory/` — local persisted runtime state

If any of these sections are unclear or you want me to expand any part (for example: add an explicit `.env` template, or include common troubleshooting commands), tell me which section and I'll update the file.
