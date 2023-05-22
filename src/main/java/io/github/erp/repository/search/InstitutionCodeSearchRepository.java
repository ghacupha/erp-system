package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.InstitutionCode;
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
 * Spring Data Elasticsearch repository for the {@link InstitutionCode} entity.
 */
public interface InstitutionCodeSearchRepository
    extends ElasticsearchRepository<InstitutionCode, Long>, InstitutionCodeSearchRepositoryInternal {}

interface InstitutionCodeSearchRepositoryInternal {
    Page<InstitutionCode> search(String query, Pageable pageable);
}

class InstitutionCodeSearchRepositoryInternalImpl implements InstitutionCodeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    InstitutionCodeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<InstitutionCode> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<InstitutionCode> hits = elasticsearchTemplate
            .search(nativeSearchQuery, InstitutionCode.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
