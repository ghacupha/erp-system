package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

