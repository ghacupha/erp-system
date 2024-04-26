package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.PrepaymentReportTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

// @ExtendWith(MockitoExtension.class)
public class InternalPrepaymentReportRepositoryTest {

    // @Mock
    private InternalPrepaymentReportRepository repository;

//    @Test // TODO
//    public void testFindAllByReportDate() {
//        // Arrange - Create test data
//        LocalDate testDate = LocalDate.of(2023, 11, 30);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        // Act - Mock repository call
//        Mockito.when(repository.findAllByReportDate(any(Specification.class), eq(testDate), eq(pageable)))
//            .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
//
//        // Assert - Check the result
//        Page<PrepaymentReportTuple> result = repository.findAllByReportDate(null, testDate, pageable);
//        assertNotNull(result);
//        assertEquals(0, result.getTotalElements());
//    }

}

