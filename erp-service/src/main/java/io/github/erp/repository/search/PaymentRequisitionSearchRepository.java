package io.github.erp.repository.search;

import io.github.erp.domain.PaymentRequisition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PaymentRequisition} entity.
 */
public interface PaymentRequisitionSearchRepository extends ElasticsearchRepository<PaymentRequisition, Long> {
}
