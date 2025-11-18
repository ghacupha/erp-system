# Development service isolation (deployment)

This document summarises the new development-only Kafka/ZooKeeper/Elasticsearch/JHipster Registry stack added to the deployment project to prevent interference with production services.

## Rationale

The previous setup pointed both `application-dev` and `application-prod` at the same Kafka broker and Elasticsearch cluster. Running the development server while the production containers were up risked writing test data into production topics and indices.

## Implementation

* Introduced `erp-deployment/services-dev.yml` to start isolated brokers on ports 2182 (ZooKeeper), 9870/29093 (Kafka), 9771 (JHipster Registry), and 8840/8841 (Elasticsearch) with their own data directories.
* Updated the central configuration for the development profile so Kafka bootstrap servers use `localhost:9870`, Eureka points to the dev registry port, and `SPRING_DATA_JEST_URI` targets the dev Elasticsearch instance.

## Usage

1. Start the dev services: `docker-compose -f services-dev.yml up -d` (from `erp-deployment`).
2. Run the ERP server in development mode (e.g. via Maven) and ensure it reads the config from the registry or exports `SPRING_DATA_JEST_URI=http://localhost:8840`.
3. Run the production stack with the original `docker-compose.yml` simultaneously; port separation keeps the data isolated.