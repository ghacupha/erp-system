# Elasticsearch connection timeouts caused by shard limits

## Context
The production ERP server timed out while instantiating `SimpleElasticsearchRepository`. The REST client stack trace was followed by a `cluster_block_exception` stating that adding the requested shards would exceed the cluster limit (`this cluster currently has [998]/[1000] maximum shards open`). Once Elasticsearch rejected the index existence check, the HTTP request waited for the full 30-second socket timeout and the Spring bean failed to initialise.

## Investigation
* The service was already pointing at the correct cluster (`erpsystem-elasticsearch:9200`), so the host mapping was not the culprit.
* Elasticsearch runs as a single node with the default `cluster.max_shards_per_node=1000`. Historic test indexes filled the quota, so even the implicit index existence probe for Spring Data repositories was blocked.
* Because the REST call never received a successful response, the repository constructor surfaced as a connection timeout even though the underlying cause was a shard limit guardrail.

## Resolution
* Increase the shard budget for the single-node cluster by setting `cluster.max_shards_per_node=3000` on all Elasticsearch services (production and development Compose files). This prevents routine index operations from being blocked when many legacy indexes exist.
* After raising the limit, remove obsolete indexes or lower shard counts on new ones as part of regular maintenance to avoid hitting the ceiling again.
