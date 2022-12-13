package io.github.erp.repository.search;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FixedAssetAcquisitionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FixedAssetAcquisitionSearchRepositoryMockConfiguration {

    @MockBean
    private FixedAssetAcquisitionSearchRepository mockFixedAssetAcquisitionSearchRepository;
}
