package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.ApplicationUser;
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
 * Spring Data Elasticsearch repository for the {@link ApplicationUser} entity.
 */
public interface ApplicationUserSearchRepository
    extends ElasticsearchRepository<ApplicationUser, Long>, ApplicationUserSearchRepositoryInternal {}

interface ApplicationUserSearchRepositoryInternal {
    Page<ApplicationUser> search(String query, Pageable pageable);
}

class ApplicationUserSearchRepositoryInternalImpl implements ApplicationUserSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ApplicationUserSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ApplicationUser> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ApplicationUser> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ApplicationUser.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
