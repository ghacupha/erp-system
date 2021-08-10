package io.github.erp.repository.search;

import io.github.erp.domain.FileUpload;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FileUpload} entity.
 */
public interface FileUploadSearchRepository extends ElasticsearchRepository<FileUpload, Long> {}
