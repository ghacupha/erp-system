package io.github.erp.repository.search;

import io.github.erp.domain.Dealer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Dealer} entity.
 */
public interface DealerSearchRepository extends ElasticsearchRepository<Dealer, Long> {
}
