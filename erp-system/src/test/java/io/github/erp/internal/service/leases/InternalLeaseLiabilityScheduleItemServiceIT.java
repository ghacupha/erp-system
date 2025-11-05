package io.github.erp.internal.service.leases;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.erp.resources.LeaseLiabilityCompilationResourceIT;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.web.rest.LeaseLiabilityScheduleItemResourceIT;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class InternalLeaseLiabilityScheduleItemServiceIT {

    @Autowired
    private InternalLeaseLiabilityScheduleItemService internalLeaseLiabilityScheduleItemService;

    @Autowired
    private LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    @Autowired
    private EntityManager em;

    private LeaseLiabilityCompilation leaseLiabilityCompilation;

    @BeforeEach
    void initTest() {
        leaseLiabilityCompilation = LeaseLiabilityCompilationResourceIT.createEntity(em);
        em.persist(leaseLiabilityCompilation);
        em.flush();
    }

    @Test
    @Transactional
    void updateActivationByCompilationActivatesRows() {
        LeaseLiabilityScheduleItem firstItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        firstItem.setActive(false);
        firstItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        em.persist(firstItem);

        LeaseLiabilityScheduleItem secondItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        secondItem.setActive(false);
        secondItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        em.persist(secondItem);
        em.flush();

        int affected = internalLeaseLiabilityScheduleItemService.updateActivationByCompilation(leaseLiabilityCompilation.getId(), true);

        assertThat(affected).isEqualTo(2);
        em.clear();
        List<LeaseLiabilityScheduleItem> refreshed = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(leaseLiabilityCompilation.getId());
        assertThat(refreshed).hasSize(2);
        assertThat(refreshed).allMatch(item -> Boolean.TRUE.equals(item.getActive()));
    }

    @Test
    @Transactional
    void updateActivationByCompilationDeactivatesRows() {
        LeaseLiabilityScheduleItem firstItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        firstItem.setActive(true);
        firstItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        em.persist(firstItem);

        LeaseLiabilityScheduleItem secondItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        secondItem.setActive(true);
        secondItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        em.persist(secondItem);
        em.flush();

        int affected = internalLeaseLiabilityScheduleItemService.updateActivationByCompilation(leaseLiabilityCompilation.getId(), false);

        assertThat(affected).isEqualTo(2);
        em.clear();
        List<LeaseLiabilityScheduleItem> refreshed = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(leaseLiabilityCompilation.getId());
        assertThat(refreshed).hasSize(2);
        assertThat(refreshed).allMatch(item -> Boolean.FALSE.equals(item.getActive()));
    }
}
