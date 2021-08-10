package io.github.erp.repository.search;

import io.github.erp.domain.FileType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FileType} entity.
 */
public interface FileTypeSearchRepository extends ElasticsearchRepository<FileType, Long> {}
