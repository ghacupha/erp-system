package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.AssetAccessory;
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
 * Spring Data Elasticsearch repository for the {@link AssetAccessory} entity.
 */
public interface AssetAccessorySearchRepository
    extends ElasticsearchRepository<AssetAccessory, Long>, AssetAccessorySearchRepositoryInternal {}

interface AssetAccessorySearchRepositoryInternal {
    Page<AssetAccessory> search(String query, Pageable pageable);
}

class AssetAccessorySearchRepositoryInternalImpl implements AssetAccessorySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AssetAccessorySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AssetAccessory> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AssetAccessory> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AssetAccessory.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
