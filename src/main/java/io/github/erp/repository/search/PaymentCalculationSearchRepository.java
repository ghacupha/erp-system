package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.PaymentCalculation;
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
 * Spring Data Elasticsearch repository for the {@link PaymentCalculation} entity.
 */
public interface PaymentCalculationSearchRepository
    extends ElasticsearchRepository<PaymentCalculation, Long>, PaymentCalculationSearchRepositoryInternal {}

interface PaymentCalculationSearchRepositoryInternal {
    Page<PaymentCalculation> search(String query, Pageable pageable);
}

class PaymentCalculationSearchRepositoryInternalImpl implements PaymentCalculationSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PaymentCalculationSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<PaymentCalculation> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<PaymentCalculation> hits = elasticsearchTemplate
            .search(nativeSearchQuery, PaymentCalculation.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
