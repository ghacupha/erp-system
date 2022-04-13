package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.ServiceOutlet;
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
 * Spring Data Elasticsearch repository for the {@link ServiceOutlet} entity.
 */
public interface ServiceOutletSearchRepository
    extends ElasticsearchRepository<ServiceOutlet, Long>, ServiceOutletSearchRepositoryInternal {}

interface ServiceOutletSearchRepositoryInternal {
    Page<ServiceOutlet> search(String query, Pageable pageable);
}

class ServiceOutletSearchRepositoryInternalImpl implements ServiceOutletSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ServiceOutletSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ServiceOutlet> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ServiceOutlet> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ServiceOutlet.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
