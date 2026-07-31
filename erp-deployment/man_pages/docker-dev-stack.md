# Docker Dev Stack

## Summary

This compose layout gives the ERP project a Docker-native development path while preserving the existing production deployment file. It lets the team build and run the app containers with Docker Compose instead of depending on locally installed Maven and npm versions.

## What Runs Where

* `docker-compose.yml` remains the production-oriented stack.
* `docker-compose-dev.yml` is the dev overlay and includes the dev registry plus the isolated Kafka, ZooKeeper, and Elasticsearch services.
* The backend and frontend are built from `erp-system/Dockerfile.dev` and `erp-client/Dockerfile.dev`.
* The recommended launchers live in the repository root `scripts/` directory:
  * `ErpDevBuildUp.ps1`
  * `ErpDevUp.ps1`
  * `ErpUp.ps1`
  * `ErpDevDown.ps1`
  * `ErpDown.ps1`

## Operational Assumptions

* Dev services use their own host ports and do not replace the production services.
* The backend still connects to PostgreSQL through the configured datasource URL.
* The dev registry reads config from `central-server-dev-config`.
