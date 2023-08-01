package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.Algorithm;
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
 * Spring Data Elasticsearch repository for the {@link Algorithm} entity.
 */
public interface AlgorithmSearchRepository extends ElasticsearchRepository<Algorithm, Long>, AlgorithmSearchRepositoryInternal {}

interface AlgorithmSearchRepositoryInternal {
    Page<Algorithm> search(String query, Pageable pageable);
}

class AlgorithmSearchRepositoryInternalImpl implements AlgorithmSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AlgorithmSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Algorithm> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Algorithm> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Algorithm.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
