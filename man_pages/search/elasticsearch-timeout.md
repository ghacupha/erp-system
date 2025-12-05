# Elasticsearch connection timeout on production deployments

## Context
The production ERP server was timing out while instantiating `SimpleElasticsearchRepository`. Logs showed 30-second socket timeouts when checking index existence via the REST client. The container logs indicated the application attempted to reach Elasticsearch at `http://localhost:8840`, which is only exposed by the development cluster.

## Investigation
* `SPRING_DATA_JEST_URI` is defined as `http://localhost:8840` in the deployment environment variables.
* `application-prod.yml` configured `spring.elasticsearch.rest.uris` to read from `SPRING_DATA_JEST_URI`, ignoring the `SPRING_ELASTICSEARCH_REST_URIS` variable supplied in Docker Compose (`http://erpsystem-elasticsearch:9200`).
* As a result, the production application attempted to connect to a non-existent local endpoint and timed out on every Elasticsearch call.

## Resolution
The production configuration now prioritises `SPRING_ELASTICSEARCH_REST_URIS` (the Docker Compose value). If it is absent, the legacy `SPRING_DATA_JEST_URI` remains a fallback. This allows the containerised server to connect to the correct Elasticsearch host while preserving compatibility with older settings.
