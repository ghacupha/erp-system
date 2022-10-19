package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.BusinessDocument;
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
 * Spring Data Elasticsearch repository for the {@link BusinessDocument} entity.
 */
public interface BusinessDocumentSearchRepository
    extends ElasticsearchRepository<BusinessDocument, Long>, BusinessDocumentSearchRepositoryInternal {}

interface BusinessDocumentSearchRepositoryInternal {
    Page<BusinessDocument> search(String query, Pageable pageable);
}

class BusinessDocumentSearchRepositoryInternalImpl implements BusinessDocumentSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    BusinessDocumentSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<BusinessDocument> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<BusinessDocument> hits = elasticsearchTemplate
            .search(nativeSearchQuery, BusinessDocument.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
