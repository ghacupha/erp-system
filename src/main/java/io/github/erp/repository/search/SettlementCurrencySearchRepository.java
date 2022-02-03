package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.SettlementCurrency;
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
 * Spring Data Elasticsearch repository for the {@link SettlementCurrency} entity.
 */
public interface SettlementCurrencySearchRepository
    extends ElasticsearchRepository<SettlementCurrency, Long>, SettlementCurrencySearchRepositoryInternal {}

interface SettlementCurrencySearchRepositoryInternal {
    Page<SettlementCurrency> search(String query, Pageable pageable);
}

class SettlementCurrencySearchRepositoryInternalImpl implements SettlementCurrencySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    SettlementCurrencySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<SettlementCurrency> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<SettlementCurrency> hits = elasticsearchTemplate
            .search(nativeSearchQuery, SettlementCurrency.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
