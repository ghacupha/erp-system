package io.github.erp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FixedAssetNetBookValueSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FixedAssetNetBookValueSearchRepositoryMockConfiguration {

    @MockBean
    private FixedAssetNetBookValueSearchRepository mockFixedAssetNetBookValueSearchRepository;

}
