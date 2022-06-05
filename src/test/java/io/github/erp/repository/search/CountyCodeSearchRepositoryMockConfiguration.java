package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CountyCodeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CountyCodeSearchRepositoryMockConfiguration {

    @MockBean
    private CountyCodeSearchRepository mockCountyCodeSearchRepository;
}
