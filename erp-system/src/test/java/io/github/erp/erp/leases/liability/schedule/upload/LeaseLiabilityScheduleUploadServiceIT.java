package io.github.erp.erp.leases.liability.schedule.upload;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleFileUpload;
import io.github.erp.erp.resources.LeaseLiabilityResourceIT;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationScheduleService;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.LeaseLiabilityScheduleFileUploadRepository;
import io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepositoryMockConfiguration;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Import(LeaseLiabilityCompilationSearchRepositoryMockConfiguration.class)
class LeaseLiabilityScheduleUploadServiceIT {

    @Autowired
    private LeaseLiabilityScheduleUploadService leaseLiabilityScheduleUploadService;

    @Autowired
    private LeaseLiabilityRepository leaseLiabilityRepository;

    @Autowired
    private LeaseLiabilityScheduleFileUploadRepository leaseLiabilityScheduleFileUploadRepository;

    @Autowired
    private LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;

    @Autowired
    private EntityManager em;

    @MockBean
    private FileStorageService storageService;

    @MockBean
    private LeaseLiabilityScheduleUploadJobLauncher jobLauncher;

    @MockBean
    private InternalLeaseAmortizationScheduleService leaseAmortizationScheduleService;

    private LeaseLiability leaseLiability;

    @BeforeEach
    void init() {
        leaseLiabilityScheduleFileUploadRepository.deleteAll();
        leaseLiabilityCompilationRepository.deleteAll();
        leaseLiabilityRepository.deleteAll();
        em.flush();

        leaseLiability = LeaseLiabilityResourceIT.createEntity(em);
        em.persist(leaseLiability);
        em.flush();
    }

    @Test
    @Transactional
    void handleUploadCreatesActiveCompilationWhenMissing() {
        LeaseAmortizationScheduleDTO scheduleDTO = new LeaseAmortizationScheduleDTO();
        scheduleDTO.setId(100L);
        scheduleDTO.setActive(Boolean.TRUE);
        when(leaseAmortizationScheduleService.save(any())).thenReturn(scheduleDTO);

        MockMultipartFile file = new MockMultipartFile("file", "upload.csv", "text/csv", "a,b\n1,2".getBytes());

        LeaseLiabilityScheduleUploadRequest request = new LeaseLiabilityScheduleUploadRequest();
        request.setLeaseLiabilityId(leaseLiability.getId());
        request.setLaunchBatchImmediately(false);

        leaseLiabilityScheduleUploadService.handleUpload(request, file);

        Optional<LeaseLiabilityCompilation> compilation = leaseLiabilityCompilationRepository.findAll().stream().findFirst();
        assertThat(compilation).isPresent();
        assertThat(compilation.get().getActive()).isTrue();

        Optional<LeaseLiabilityScheduleFileUpload> upload = leaseLiabilityScheduleFileUploadRepository
            .findAll()
            .stream()
            .findFirst();
        assertThat(upload).isPresent();
        assertThat(upload.get().getLeaseLiabilityCompilationId()).isEqualTo(compilation.get().getId());
    }
}

