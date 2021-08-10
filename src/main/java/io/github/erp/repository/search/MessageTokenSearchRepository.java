package io.github.erp.repository.search;

import io.github.erp.domain.MessageToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MessageToken} entity.
 */
public interface MessageTokenSearchRepository extends ElasticsearchRepository<MessageToken, Long> {}
