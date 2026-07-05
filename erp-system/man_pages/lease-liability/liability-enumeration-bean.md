# Liability enumeration bean availability

## Context
- Environment was started with profiles `dev` and `api-docs`.
- Startup failed while creating `liabilityEnumerationResourceProd` due to a missing `LiabilityEnumerationProcessor` bean.
- Logs also showed Liquibase retrying database connections because the application context never finished.

## Root cause
`LiabilityEnumerationProcessor` was only provided via a `@Bean` method inside `LiabilityEnumerationConfiguration`. The configuration was not being picked up during component scanning, so the processor bean was never registered and the REST resource failed to start.

## Resolution
- Registered `LiabilityEnumerationProcessor` as a Spring component so it is picked up by component scanning.
- Removed the redundant `@Bean` definition to avoid duplicate bean declarations while keeping the custom job launcher configuration intact.

## Notes
No user-facing behaviour changed; this update only ensures the service can start successfully when invoking liability enumeration endpoints.
