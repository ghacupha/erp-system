package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.WorkProjectRegister;
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
 * Spring Data Elasticsearch repository for the {@link WorkProjectRegister} entity.
 */
public interface WorkProjectRegisterSearchRepository
    extends ElasticsearchRepository<WorkProjectRegister, Long>, WorkProjectRegisterSearchRepositoryInternal {}

interface WorkProjectRegisterSearchRepositoryInternal {
    Page<WorkProjectRegister> search(String query, Pageable pageable);
}

class WorkProjectRegisterSearchRepositoryInternalImpl implements WorkProjectRegisterSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkProjectRegisterSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<WorkProjectRegister> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<WorkProjectRegister> hits = elasticsearchTemplate
            .search(nativeSearchQuery, WorkProjectRegister.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
