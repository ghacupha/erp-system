package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.SystemModule;
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
 * Spring Data Elasticsearch repository for the {@link SystemModule} entity.
 */
public interface SystemModuleSearchRepository extends ElasticsearchRepository<SystemModule, Long>, SystemModuleSearchRepositoryInternal {}

interface SystemModuleSearchRepositoryInternal {
    Page<SystemModule> search(String query, Pageable pageable);
}

class SystemModuleSearchRepositoryInternalImpl implements SystemModuleSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    SystemModuleSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<SystemModule> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<SystemModule> hits = elasticsearchTemplate
            .search(nativeSearchQuery, SystemModule.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
