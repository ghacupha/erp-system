package io.github.erp.repository.search;

import io.github.erp.domain.FixedAssetDepreciation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FixedAssetDepreciation} entity.
 */
public interface FixedAssetDepreciationSearchRepository extends ElasticsearchRepository<FixedAssetDepreciation, Long> {}
