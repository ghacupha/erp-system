package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PrepaymentAmortizationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PrepaymentAmortizationSearchRepositoryMockConfiguration {

    @MockBean
    private PrepaymentAmortizationSearchRepository mockPrepaymentAmortizationSearchRepository;
}
