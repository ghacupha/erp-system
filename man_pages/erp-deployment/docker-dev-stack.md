# Docker Dev Stack

## Summary

The ERP deployment now has a dedicated development compose file that runs the backend, frontend, registry, Kafka, ZooKeeper, and Elasticsearch inside Docker. This avoids clashes with the production stack and removes the need to rely on the developer's local Node.js installation for the client build.

## Workflow

* `erp-deployment/docker-compose-dev.yml` builds the backend and frontend from the repository source tree.
* The dev registry uses `erp-deployment/central-server-dev-config`.
* Kafka, ZooKeeper, and Elasticsearch keep their isolated dev ports so the production compose stack can remain online.
* The new root `scripts/ErpDevBuildUp.ps1` launcher starts the full stack with `docker-compose -f erp-deployment/docker-compose-dev.yml up --build -d`.

## Port Assumptions

* Backend: `8982`
* Frontend: `8983`
* Registry: `8771`
* Kafka external listener: `9870`
* Elasticsearch: `8840`

## Notes

The backend container still expects PostgreSQL to be available through the configured `SPRING_DATASOURCE_URL`. This change only moves the ERP application runtime into Docker; it does not introduce a new database container.
