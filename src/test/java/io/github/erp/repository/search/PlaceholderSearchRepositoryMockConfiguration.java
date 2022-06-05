package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PlaceholderSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlaceholderSearchRepositoryMockConfiguration {

    @MockBean
    private PlaceholderSearchRepository mockPlaceholderSearchRepository;
}
