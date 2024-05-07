package io.github.erp.erp.index;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.times;

public class AssetRegistryIndexingServiceIT {

// TODO
//    @Test
//    public void testIndexerSequenceCallsProcessInBatchesOf() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        // Create mocks for dependencies
//        AssetRegistryIndexingService assetService = mock(AssetRegistryIndexingService.class);
//        AssetRegistrationSearchRepository searchRepository = mock(AssetRegistrationSearchRepository.class);
//        AssetRegistrationMapper mapper = mock(AssetRegistrationMapper.class);
//        AssetRegistrationService service = mock(AssetRegistrationService.class);
//        IndexProperties indexProperties = new IndexProperties();
//        indexProperties.setEnabled(true);
//        IndexProperties.Rebuild rebuildEnabled = new IndexProperties.Rebuild();
//        rebuildEnabled.setEnabled(true);
//        indexProperties.setRebuild(rebuildEnabled);
//
//        // Create an instance of the subclass to test
//        AssetRegistryIndexingService batchProcessor = new AssetRegistryIndexingService(indexProperties, mapper, service, searchRepository);
//
//        // Spy on the subclass to verify method invocation
//        AssetRegistryIndexingService spyBatchProcessor = spy(batchProcessor);
//
//        // Call the method to test
//        spyBatchProcessor.index();
//
//        // Verify that processInBatchesOf method is called
//        Mockito.verify(spyBatchProcessor, atLeastOnce()).processInBatchesOf(anyInt());
//    }

}
