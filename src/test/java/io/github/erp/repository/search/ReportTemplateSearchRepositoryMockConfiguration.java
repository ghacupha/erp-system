package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ReportTemplateSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReportTemplateSearchRepositoryMockConfiguration {

    @MockBean
    private ReportTemplateSearchRepository mockReportTemplateSearchRepository;
}
