# Elasticsearch connection timeout on production deployments

## Context
Production startup failed with 30-second socket timeouts when the application instantiated Elasticsearch repositories. The REST client attempted to reach `http://localhost:8840`, which is only configured for development, causing repeated connection failures in Docker.

## Investigation
* The Docker environment for the ERP server provides `SPRING_ELASTICSEARCH_REST_URIS=http://erpsystem-elasticsearch:9200`.
* `application-prod.yml` bound `spring.elasticsearch.rest.uris` to `SPRING_DATA_JEST_URI`, whose default is `http://localhost:8840` from the development profile.
* Because the production value was ignored, the client tried to contact a host that does not exist in the production container network.

## Resolution
The production configuration now reads `SPRING_ELASTICSEARCH_REST_URIS` first and falls back to `SPRING_DATA_JEST_URI` only when necessary. This aligns the application with the Docker Compose configuration and prevents connection timeouts.
