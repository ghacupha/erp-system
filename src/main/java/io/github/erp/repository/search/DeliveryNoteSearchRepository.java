package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.DeliveryNote;
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
 * Spring Data Elasticsearch repository for the {@link DeliveryNote} entity.
 */
public interface DeliveryNoteSearchRepository extends ElasticsearchRepository<DeliveryNote, Long>, DeliveryNoteSearchRepositoryInternal {}

interface DeliveryNoteSearchRepositoryInternal {
    Page<DeliveryNote> search(String query, Pageable pageable);
}

class DeliveryNoteSearchRepositoryInternalImpl implements DeliveryNoteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    DeliveryNoteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<DeliveryNote> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<DeliveryNote> hits = elasticsearchTemplate
            .search(nativeSearchQuery, DeliveryNote.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
