package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.BusinessStamp;
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
 * Spring Data Elasticsearch repository for the {@link BusinessStamp} entity.
 */
public interface BusinessStampSearchRepository
    extends ElasticsearchRepository<BusinessStamp, Long>, BusinessStampSearchRepositoryInternal {}

interface BusinessStampSearchRepositoryInternal {
    Page<BusinessStamp> search(String query, Pageable pageable);
}

class BusinessStampSearchRepositoryInternalImpl implements BusinessStampSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    BusinessStampSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<BusinessStamp> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<BusinessStamp> hits = elasticsearchTemplate
            .search(nativeSearchQuery, BusinessStamp.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
