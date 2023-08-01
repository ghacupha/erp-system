package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.PrepaymentAccount;
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
 * Spring Data Elasticsearch repository for the {@link PrepaymentAccount} entity.
 */
public interface PrepaymentAccountSearchRepository
    extends ElasticsearchRepository<PrepaymentAccount, Long>, PrepaymentAccountSearchRepositoryInternal {}

interface PrepaymentAccountSearchRepositoryInternal {
    Page<PrepaymentAccount> search(String query, Pageable pageable);
}

class PrepaymentAccountSearchRepositoryInternalImpl implements PrepaymentAccountSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PrepaymentAccountSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<PrepaymentAccount> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<PrepaymentAccount> hits = elasticsearchTemplate
            .search(nativeSearchQuery, PrepaymentAccount.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
