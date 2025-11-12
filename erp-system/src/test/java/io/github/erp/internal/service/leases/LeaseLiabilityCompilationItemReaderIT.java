package io.github.erp.internal.service.leases;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.erp.resources.LeaseLiabilityCompilationResourceIT;
import io.github.erp.erp.resources.LeaseLiabilityResourceIT;
import io.github.erp.erp.resources.LeaseLiabilityScheduleItemResourceIT;
import io.github.erp.internal.service.leases.batch.LeaseLiabilityCompilationItemReader;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class LeaseLiabilityCompilationItemReaderIT {

    @Autowired
    private InternalLeaseLiabilityService internalLeaseLiabilityService;

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
    void readerSkipsPreviouslyScheduledLiabilities() {
        LeaseLiability firstLiability = LeaseLiabilityResourceIT.createEntity(em);
        em.persist(firstLiability);

        LeaseLiability secondLiability = LeaseLiabilityResourceIT.createEntity(em);
        em.persist(secondLiability);

        LeaseLiability thirdLiability = LeaseLiabilityResourceIT.createEntity(em);
        em.persist(thirdLiability);
        em.flush();

        LeaseLiabilityScheduleItem scheduledItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        scheduledItem.setLeaseLiability(firstLiability);
        scheduledItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        em.persist(scheduledItem);
        em.flush();

        Long scheduledLiabilityId = firstLiability.getId();
        Long expectedSecondId = secondLiability.getId();
        Long expectedThirdId = thirdLiability.getId();

        em.clear();

        LeaseLiabilityCompilationItemReader reader = new LeaseLiabilityCompilationItemReader(
            internalLeaseLiabilityService,
            leaseLiabilityCompilation.getId(),
            "batch-idempotent-test"
        );

        List<LeaseLiabilityDTO> results = new ArrayList<>();
        LeaseLiabilityDTO nextItem;
        while ((nextItem = reader.read()) != null) {
            results.add(nextItem);
        }

        assertThat(results).extracting(LeaseLiabilityDTO::getId).containsExactlyInAnyOrder(expectedSecondId, expectedThirdId);
        assertThat(results).extracting(LeaseLiabilityDTO::getId).doesNotContain(scheduledLiabilityId);
    }
}
