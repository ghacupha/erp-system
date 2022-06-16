package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.DepreciationMethod;
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
 * Spring Data Elasticsearch repository for the {@link DepreciationMethod} entity.
 */
public interface DepreciationMethodSearchRepository
    extends ElasticsearchRepository<DepreciationMethod, Long>, DepreciationMethodSearchRepositoryInternal {}

interface DepreciationMethodSearchRepositoryInternal {
    Page<DepreciationMethod> search(String query, Pageable pageable);
}

class DepreciationMethodSearchRepositoryInternalImpl implements DepreciationMethodSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    DepreciationMethodSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<DepreciationMethod> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<DepreciationMethod> hits = elasticsearchTemplate
            .search(nativeSearchQuery, DepreciationMethod.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
