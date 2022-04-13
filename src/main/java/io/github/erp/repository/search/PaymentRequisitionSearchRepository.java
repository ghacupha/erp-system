package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.PaymentRequisition;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentRequisition} entity.
 */
public interface PaymentRequisitionSearchRepository
    extends ElasticsearchRepository<PaymentRequisition, Long>, PaymentRequisitionSearchRepositoryInternal {}

interface PaymentRequisitionSearchRepositoryInternal {
    Page<PaymentRequisition> search(String query, Pageable pageable);
}

class PaymentRequisitionSearchRepositoryInternalImpl implements PaymentRequisitionSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PaymentRequisitionSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<PaymentRequisition> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<PaymentRequisition> hits = elasticsearchTemplate
            .search(nativeSearchQuery, PaymentRequisition.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
