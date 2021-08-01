package io.github.erp.repository.search;

import io.github.erp.domain.TaxReference;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TaxReference} entity.
 */
public interface TaxReferenceSearchRepository extends ElasticsearchRepository<TaxReference, Long> {}
