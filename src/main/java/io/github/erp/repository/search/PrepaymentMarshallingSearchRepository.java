package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.PrepaymentMarshalling;
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
 * Spring Data Elasticsearch repository for the {@link PrepaymentMarshalling} entity.
 */
public interface PrepaymentMarshallingSearchRepository
    extends ElasticsearchRepository<PrepaymentMarshalling, Long>, PrepaymentMarshallingSearchRepositoryInternal {}

interface PrepaymentMarshallingSearchRepositoryInternal {
    Page<PrepaymentMarshalling> search(String query, Pageable pageable);
}

class PrepaymentMarshallingSearchRepositoryInternalImpl implements PrepaymentMarshallingSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PrepaymentMarshallingSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<PrepaymentMarshalling> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<PrepaymentMarshalling> hits = elasticsearchTemplate
            .search(nativeSearchQuery, PrepaymentMarshalling.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
