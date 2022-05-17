package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.JobSheet;
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
 * Spring Data Elasticsearch repository for the {@link JobSheet} entity.
 */
public interface JobSheetSearchRepository extends ElasticsearchRepository<JobSheet, Long>, JobSheetSearchRepositoryInternal {}

interface JobSheetSearchRepositoryInternal {
    Page<JobSheet> search(String query, Pageable pageable);
}

class JobSheetSearchRepositoryInternalImpl implements JobSheetSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    JobSheetSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<JobSheet> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<JobSheet> hits = elasticsearchTemplate
            .search(nativeSearchQuery, JobSheet.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
