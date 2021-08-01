package io.github.erp.repository.search;

import io.github.erp.domain.FixedAssetAcquisition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FixedAssetAcquisition} entity.
 */
public interface FixedAssetAcquisitionSearchRepository extends ElasticsearchRepository<FixedAssetAcquisition, Long> {}
