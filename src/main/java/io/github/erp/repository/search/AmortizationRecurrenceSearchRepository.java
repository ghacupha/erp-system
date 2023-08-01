package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.AmortizationRecurrence;
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
 * Spring Data Elasticsearch repository for the {@link AmortizationRecurrence} entity.
 */
public interface AmortizationRecurrenceSearchRepository
    extends ElasticsearchRepository<AmortizationRecurrence, Long>, AmortizationRecurrenceSearchRepositoryInternal {}

interface AmortizationRecurrenceSearchRepositoryInternal {
    Page<AmortizationRecurrence> search(String query, Pageable pageable);
}

class AmortizationRecurrenceSearchRepositoryInternalImpl implements AmortizationRecurrenceSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AmortizationRecurrenceSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AmortizationRecurrence> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AmortizationRecurrence> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AmortizationRecurrence.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
