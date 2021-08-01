package io.github.erp.repository.search;

import io.github.erp.domain.PaymentCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentCategory} entity.
 */
public interface PaymentCategorySearchRepository extends ElasticsearchRepository<PaymentCategory, Long> {}
