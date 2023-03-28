package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.ReportTemplate;
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
 * Spring Data Elasticsearch repository for the {@link ReportTemplate} entity.
 */
public interface ReportTemplateSearchRepository
    extends ElasticsearchRepository<ReportTemplate, Long>, ReportTemplateSearchRepositoryInternal {}

interface ReportTemplateSearchRepositoryInternal {
    Page<ReportTemplate> search(String query, Pageable pageable);
}

class ReportTemplateSearchRepositoryInternalImpl implements ReportTemplateSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ReportTemplateSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ReportTemplate> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ReportTemplate> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ReportTemplate.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
