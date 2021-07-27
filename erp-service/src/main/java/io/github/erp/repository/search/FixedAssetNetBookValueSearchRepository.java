package io.github.erp.repository.search;

import io.github.erp.domain.FixedAssetNetBookValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link FixedAssetNetBookValue} entity.
 */
public interface FixedAssetNetBookValueSearchRepository extends ElasticsearchRepository<FixedAssetNetBookValue, Long> {
}
