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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityScheduleItemService;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import io.github.erp.web.rest.LeaseLiabilityScheduleItemResourceIT;
import io.github.erp.erp.resources.LeaseLiabilityCompilationResourceIT;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class LeaseLiabilityCompilationItemWriterIT {

    @SpyBean
    private InternalLeaseLiabilityScheduleItemService internalLeaseLiabilityScheduleItemService;

    @Autowired
    private LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    @Autowired
    private LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void rerunDeactivatesExistingRowsBeforePersistingReplacementBatches() throws Exception {
        LeaseLiabilityCompilation compilation = LeaseLiabilityCompilationResourceIT.createEntity(em);
        compilation.setActive(true);
        em.persist(compilation);
        em.flush();

        LeaseLiabilityScheduleItem existingFirst = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        existingFirst.setLeaseLiabilityCompilation(compilation);
        existingFirst.setActive(true);
        em.persist(existingFirst);

        LeaseLiabilityScheduleItem existingSecond = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        existingSecond.setLeaseLiabilityCompilation(compilation);
        existingSecond.setActive(true);
        em.persist(existingSecond);
        em.flush();

        Long existingFirstId = existingFirst.getId();
        Long existingSecondId = existingSecond.getId();

        LeaseLiabilityCompilationItemWriter writer =
            new LeaseLiabilityCompilationItemWriter(internalLeaseLiabilityScheduleItemService, compilation.getId());

        LeaseLiabilityScheduleItemDTO firstReplacement = buildScheduleItemDto();
        LeaseLiabilityScheduleItemDTO secondReplacement = buildScheduleItemDto();

        writer.write(List.of(List.of(firstReplacement)));
        writer.write(List.of(List.of(secondReplacement)));

        InOrder interactionOrder = inOrder(internalLeaseLiabilityScheduleItemService);
        interactionOrder.verify(internalLeaseLiabilityScheduleItemService)
            .updateActivationByCompilation(compilation.getId(), false);
        interactionOrder.verify(internalLeaseLiabilityScheduleItemService).saveAll(anyList());
        verify(internalLeaseLiabilityScheduleItemService, times(1))
            .updateActivationByCompilation(compilation.getId(), false);
        verify(internalLeaseLiabilityScheduleItemService, times(2)).saveAll(anyList());

        em.flush();
        em.clear();

        List<LeaseLiabilityScheduleItem> refreshed =
            leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(compilation.getId());

        assertThat(refreshed)
            .filteredOn(item -> item.getId().equals(existingFirstId) || item.getId().equals(existingSecondId))
            .allMatch(item -> Boolean.FALSE.equals(item.getActive()));

        assertThat(refreshed)
            .filteredOn(item -> !item.getId().equals(existingFirstId) && !item.getId().equals(existingSecondId))
            .hasSize(2)
            .allMatch(item -> Boolean.TRUE.equals(item.getActive()));
    }

    private LeaseLiabilityScheduleItemDTO buildScheduleItemDto() {
        LeaseLiabilityScheduleItem scheduleItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        scheduleItem.setLeaseLiabilityCompilation(null);
        scheduleItem.setActive(false);
        LeaseLiabilityScheduleItemDTO dto = leaseLiabilityScheduleItemMapper.toDto(scheduleItem);
        dto.setId(null);
        dto.setActive(Boolean.FALSE);
        dto.setLeaseLiabilityCompilation(null);
        return dto;
    }
}
