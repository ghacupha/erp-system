package io.github.erp.repository.search;

import io.github.erp.domain.PaymentCalculation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentCalculation} entity.
 */
public interface PaymentCalculationSearchRepository extends ElasticsearchRepository<PaymentCalculation, Long> {}
