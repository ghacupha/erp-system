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
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.erp.resources.LeaseLiabilityCompilationResourceIT;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.web.rest.LeaseLiabilityScheduleItemResourceIT;
import io.github.erp.web.rest.LeaseRepaymentPeriodResourceIT;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
    private LeaseLiabilityRepository leaseLiabilityRepository;

    @Autowired
    private LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

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
        LeaseLiabilityCompilation refreshedCompilation = em.find(LeaseLiabilityCompilation.class, leaseLiabilityCompilation.getId());
        assertThat(refreshedCompilation.getActive()).isTrue();
        List<LeaseLiabilityScheduleItem> refreshed = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(leaseLiabilityCompilation.getId());
        assertThat(refreshed).hasSize(2);
        assertThat(refreshed).allMatch(item -> Boolean.TRUE.equals(item.getActive()));
    }

    @Test
    @Transactional
    void updateActivationByCompilationDeactivatesRows() {
        leaseLiabilityCompilation.setActive(true);
        leaseLiabilityCompilation = em.merge(leaseLiabilityCompilation);
        em.flush();

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
        LeaseLiabilityCompilation refreshedCompilation = em.find(LeaseLiabilityCompilation.class, leaseLiabilityCompilation.getId());
        assertThat(refreshedCompilation.getActive()).isFalse();
        List<LeaseLiabilityScheduleItem> refreshed = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(leaseLiabilityCompilation.getId());
        assertThat(refreshed).hasSize(2);
        assertThat(refreshed).allMatch(item -> Boolean.FALSE.equals(item.getActive()));
    }

    @Test
    @Transactional
    void saveAllReusesExistingRowsForCompilation() {
        LeaseLiabilityScheduleItem firstItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        firstItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);

        LeaseLiabilityScheduleItem secondItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
        secondItem.setLeaseLiabilityCompilation(leaseLiabilityCompilation);
        LeaseRepaymentPeriod nextPeriod = LeaseRepaymentPeriodResourceIT.createUpdatedEntity(em);
        em.persist(nextPeriod);
        em.flush();
        secondItem.setLeasePeriod(nextPeriod);
        secondItem.setSequenceNumber(2);

        List<LeaseLiabilityScheduleItemDTO> initialDtos = List.of(
            leaseLiabilityScheduleItemMapper.toDto(firstItem),
            leaseLiabilityScheduleItemMapper.toDto(secondItem)
        );

        internalLeaseLiabilityScheduleItemService.saveAll(initialDtos);
        Set<Long> processedLiabilityIds = initialDtos
            .stream()
            .map(LeaseLiabilityScheduleItemDTO::getLeaseLiability)
            .filter(Objects::nonNull)
            .map(LeaseLiabilityDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        em.flush();
        em.clear();

        List<LeaseLiability> flaggedLiabilities = leaseLiabilityRepository.findAllById(processedLiabilityIds);
        assertThat(flaggedLiabilities).hasSize(processedLiabilityIds.size());
        assertThat(flaggedLiabilities).allMatch(liability -> Boolean.TRUE.equals(liability.getHasBeenFullyAmortised()));

        List<LeaseLiabilityScheduleItem> persisted = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(
            leaseLiabilityCompilation.getId()
        );
        assertThat(persisted).hasSize(initialDtos.size());
        Map<String, BigDecimal> previousCashByKey = persisted
            .stream()
            .collect(Collectors.toMap(this::businessKey, LeaseLiabilityScheduleItem::getCashPayment));

        List<LeaseLiabilityScheduleItemDTO> rerunDtos = persisted
            .stream()
            .map(leaseLiabilityScheduleItemMapper::toDto)
            .collect(Collectors.toList());
        rerunDtos.forEach(dto -> {
            dto.setId(null);
            BigDecimal currentCash = dto.getCashPayment() != null ? dto.getCashPayment() : BigDecimal.ZERO;
            dto.setCashPayment(currentCash.add(BigDecimal.ONE));
        });

        internalLeaseLiabilityScheduleItemService.saveAll(rerunDtos);
        em.flush();
        em.clear();

        List<LeaseLiabilityScheduleItem> refreshed = leaseLiabilityScheduleItemRepository.findByLeaseLiabilityCompilationId(
            leaseLiabilityCompilation.getId()
        );
        assertThat(refreshed).hasSize(persisted.size());
        Map<String, BigDecimal> refreshedCashByKey = refreshed
            .stream()
            .collect(Collectors.toMap(this::businessKey, LeaseLiabilityScheduleItem::getCashPayment));

        refreshedCashByKey.forEach((key, amount) -> {
            BigDecimal previous = previousCashByKey.get(key);
            assertThat(previous).isNotNull();
            assertThat(amount).isEqualByComparingTo(previous.add(BigDecimal.ONE));
        });

        List<LeaseLiability> refreshedLiabilities = leaseLiabilityRepository.findAllById(processedLiabilityIds);
        assertThat(refreshedLiabilities).allMatch(liability -> Boolean.TRUE.equals(liability.getHasBeenFullyAmortised()));
    }

    private String businessKey(LeaseLiabilityScheduleItem item) {
        Long liabilityId = item.getLeaseLiability() != null ? item.getLeaseLiability().getId() : null;
        Long periodId = item.getLeasePeriod() != null ? item.getLeasePeriod().getId() : null;
        if (liabilityId == null || periodId == null) {
            throw new IllegalStateException("Lease liability schedule item is missing a liability or period");
        }
        return liabilityId + ":" + periodId;
    }
}
