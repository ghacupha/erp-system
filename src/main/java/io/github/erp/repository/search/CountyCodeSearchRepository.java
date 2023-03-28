package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.CountyCode;
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
 * Spring Data Elasticsearch repository for the {@link CountyCode} entity.
 */
public interface CountyCodeSearchRepository extends ElasticsearchRepository<CountyCode, Long>, CountyCodeSearchRepositoryInternal {}

interface CountyCodeSearchRepositoryInternal {
    Page<CountyCode> search(String query, Pageable pageable);
}

class CountyCodeSearchRepositoryInternalImpl implements CountyCodeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CountyCodeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<CountyCode> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<CountyCode> hits = elasticsearchTemplate
            .search(nativeSearchQuery, CountyCode.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
