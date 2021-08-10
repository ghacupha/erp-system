package io.github.erp.repository.search;

import io.github.erp.domain.TaxRule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TaxRule} entity.
 */
public interface TaxRuleSearchRepository extends ElasticsearchRepository<TaxRule, Long> {}
