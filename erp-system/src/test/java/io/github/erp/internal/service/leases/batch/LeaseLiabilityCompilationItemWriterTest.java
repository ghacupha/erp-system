package io.github.erp.internal.service.leases.batch;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.erp.internal.service.leases.InternalLeaseLiabilityScheduleItemService;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseLiabilityCompilationItemWriterTest {

    private static final long COMPILATION_ID = 417L;

    @Mock
    private InternalLeaseLiabilityScheduleItemService internalLeaseLiabilityScheduleItemService;

    private LeaseLiabilityCompilationItemWriter writer;

    @BeforeEach
    void setUp() {
        writer = new LeaseLiabilityCompilationItemWriter(internalLeaseLiabilityScheduleItemService, COMPILATION_ID);
    }

    @Test
    void writeSkipsEmptyBatchesWithoutDeactivation() throws Exception {
        writer.write(
            List.of(
                Collections.<LeaseLiabilityScheduleItemDTO>emptyList(),
                Collections.<LeaseLiabilityScheduleItemDTO>emptyList()
            )
        );

        verify(internalLeaseLiabilityScheduleItemService, never()).updateActivationByCompilation(anyLong(), anyBoolean());
        verify(internalLeaseLiabilityScheduleItemService, never()).saveAll(anyList());
    }

    @Test
    void writeDeactivatesExistingSchedulesOnceBeforeSavingFirstBatch() throws Exception {
        LeaseLiabilityScheduleItemDTO first = scheduleItem();
        LeaseLiabilityScheduleItemDTO second = scheduleItem();

        writer.write(
            Arrays.asList(
                Collections.<LeaseLiabilityScheduleItemDTO>emptyList(),
                List.of(first),
                List.of(second)
            )
        );

        InOrder order = inOrder(internalLeaseLiabilityScheduleItemService);
        order.verify(internalLeaseLiabilityScheduleItemService).updateActivationByCompilation(COMPILATION_ID, false);
        order.verify(internalLeaseLiabilityScheduleItemService).saveAll(anyList());
        verify(internalLeaseLiabilityScheduleItemService, times(1))
            .updateActivationByCompilation(COMPILATION_ID, false);
        verify(internalLeaseLiabilityScheduleItemService, times(2)).saveAll(anyList());
    }

    @Test
    void writeActivatesItemsAndBackfillsCompilationWhenMissing() throws Exception {
        LeaseLiabilityScheduleItemDTO dto = scheduleItem();
        writer.write(List.of(List.of(dto)));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<LeaseLiabilityScheduleItemDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(internalLeaseLiabilityScheduleItemService).saveAll(captor.capture());

        LeaseLiabilityScheduleItemDTO persisted = captor.getValue().get(0);
        assertThat(persisted.getActive()).isTrue();
        assertThat(persisted.getLeaseLiabilityCompilation()).isNotNull();
        assertThat(persisted.getLeaseLiabilityCompilation().getId()).isEqualTo(COMPILATION_ID);
        verify(internalLeaseLiabilityScheduleItemService).updateActivationByCompilation(COMPILATION_ID, false);
    }

    @Test
    void writeBackfillsCompilationIdWhenContainerHasNullIdentifier() throws Exception {
        LeaseLiabilityScheduleItemDTO dto = scheduleItem();
        dto.setLeaseLiabilityCompilation(new LeaseLiabilityCompilationDTO());

        writer.write(List.of(List.of(dto)));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<LeaseLiabilityScheduleItemDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(internalLeaseLiabilityScheduleItemService).saveAll(captor.capture());
        LeaseLiabilityScheduleItemDTO persisted = captor.getValue().get(0);
        assertThat(persisted.getLeaseLiabilityCompilation()).isNotNull();
        assertThat(persisted.getLeaseLiabilityCompilation().getId()).isEqualTo(COMPILATION_ID);
    }

    @Test
    void writePreservesExistingCompilationIdentifier() throws Exception {
        LeaseLiabilityScheduleItemDTO dto = scheduleItem();
        LeaseLiabilityCompilationDTO compilation = new LeaseLiabilityCompilationDTO();
        compilation.setId(921L);
        dto.setLeaseLiabilityCompilation(compilation);

        writer.write(List.of(List.of(dto)));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<LeaseLiabilityScheduleItemDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(internalLeaseLiabilityScheduleItemService).saveAll(captor.capture());
        LeaseLiabilityScheduleItemDTO persisted = captor.getValue().get(0);
        assertThat(persisted.getLeaseLiabilityCompilation()).isNotNull();
        assertThat(persisted.getLeaseLiabilityCompilation().getId()).isEqualTo(921L);
        verify(internalLeaseLiabilityScheduleItemService).updateActivationByCompilation(COMPILATION_ID, false);
    }

    private LeaseLiabilityScheduleItemDTO scheduleItem() {
        LeaseLiabilityScheduleItemDTO dto = new LeaseLiabilityScheduleItemDTO();
        dto.setActive(Boolean.FALSE);
        return dto;
    }
}
