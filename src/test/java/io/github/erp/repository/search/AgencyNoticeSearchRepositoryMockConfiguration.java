package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AgencyNoticeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AgencyNoticeSearchRepositoryMockConfiguration {

    @MockBean
    private AgencyNoticeSearchRepository mockAgencyNoticeSearchRepository;
}
