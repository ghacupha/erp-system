package io.github.erp.internal.service.leases.batch;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.erp.internal.service.leases.LeaseAmortizationCompilationService;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseLiabilityCompilationItemProcessorTest {

    private static final long COMPILATION_ID = 31L;

    @Mock
    private LeaseAmortizationCompilationService leaseAmortizationCompilationService;

    private LeaseLiabilityCompilationItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new LeaseLiabilityCompilationItemProcessor("batch-token", COMPILATION_ID, leaseAmortizationCompilationService);
    }

    @Test
    void processDelegatesToAmortizationServiceWithCompilationIdentifier() throws Exception {
        LeaseLiabilityDTO model = new LeaseLiabilityDTO();
        model.setId(99L);
        List<LeaseLiabilityScheduleItemDTO> expected = List.of(new LeaseLiabilityScheduleItemDTO());
        when(leaseAmortizationCompilationService.generateAmortizationSchedule(99L, COMPILATION_ID)).thenReturn(expected);

        List<LeaseLiabilityScheduleItemDTO> actual = processor.process(model);

        assertThat(actual).isSameAs(expected);
        verify(leaseAmortizationCompilationService).generateAmortizationSchedule(99L, COMPILATION_ID);
    }
}
