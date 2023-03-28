package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.SettlementRequisition;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SettlementRequisition} entity.
 */
public interface SettlementRequisitionSearchRepository
    extends ElasticsearchRepository<SettlementRequisition, Long>, SettlementRequisitionSearchRepositoryInternal {}

interface SettlementRequisitionSearchRepositoryInternal {
    Page<SettlementRequisition> search(String query, Pageable pageable);
}

class SettlementRequisitionSearchRepositoryInternalImpl implements SettlementRequisitionSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    SettlementRequisitionSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<SettlementRequisition> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<SettlementRequisition> hits = elasticsearchTemplate
            .search(nativeSearchQuery, SettlementRequisition.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
