package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.AgencyNotice;
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
 * Spring Data Elasticsearch repository for the {@link AgencyNotice} entity.
 */
public interface AgencyNoticeSearchRepository extends ElasticsearchRepository<AgencyNotice, Long>, AgencyNoticeSearchRepositoryInternal {}

interface AgencyNoticeSearchRepositoryInternal {
    Page<AgencyNotice> search(String query, Pageable pageable);
}

class AgencyNoticeSearchRepositoryInternalImpl implements AgencyNoticeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AgencyNoticeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AgencyNotice> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AgencyNotice> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AgencyNotice.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
