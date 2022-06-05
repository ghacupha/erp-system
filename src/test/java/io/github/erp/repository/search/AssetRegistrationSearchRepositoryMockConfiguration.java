package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AssetRegistrationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AssetRegistrationSearchRepositoryMockConfiguration {

    @MockBean
    private AssetRegistrationSearchRepository mockAssetRegistrationSearchRepository;
}
