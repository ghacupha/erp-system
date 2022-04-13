package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.FixedAssetDepreciation;
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
 * Spring Data Elasticsearch repository for the {@link FixedAssetDepreciation} entity.
 */
public interface FixedAssetDepreciationSearchRepository
    extends ElasticsearchRepository<FixedAssetDepreciation, Long>, FixedAssetDepreciationSearchRepositoryInternal {}

interface FixedAssetDepreciationSearchRepositoryInternal {
    Page<FixedAssetDepreciation> search(String query, Pageable pageable);
}

class FixedAssetDepreciationSearchRepositoryInternalImpl implements FixedAssetDepreciationSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    FixedAssetDepreciationSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<FixedAssetDepreciation> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<FixedAssetDepreciation> hits = elasticsearchTemplate
            .search(nativeSearchQuery, FixedAssetDepreciation.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
