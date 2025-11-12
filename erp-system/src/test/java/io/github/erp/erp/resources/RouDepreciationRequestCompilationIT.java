package io.github.erp.erp.resources;

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
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationJob;
import io.github.erp.repository.RouDepreciationRequestRepository;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
import io.github.erp.web.rest.TestUtil;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouDepreciationRequestCompilationIT {

    private static final String ENTITY_API_URL = "/api/leases/rou-depreciation-requests";

    @Autowired
    private MockMvc restMockMvc;

    @Autowired
    private RouDepreciationRequestRepository rouDepreciationRequestRepository;

    @Autowired
    private RouDepreciationRequestMapper rouDepreciationRequestMapper;

    @MockBean
    private ROUDepreciationEntryCompilationJob rouDepreciationEntryCompilationJob;

    @BeforeEach
    void setUp() {
        reset(rouDepreciationEntryCompilationJob);
    }

    @Test
    @Transactional
    void creatingRequestShouldTriggerCompilationJob() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationRequestRepository.findAll().size();

        RouDepreciationRequest request = new RouDepreciationRequest().requisitionId(UUID.randomUUID());
        RouDepreciationRequestDTO requestDTO = rouDepreciationRequestMapper.toDto(request);
        requestDTO.setId(null);

        restMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestDTO))
            )
            .andExpect(status().isCreated());

        List<RouDepreciationRequest> allRequests = rouDepreciationRequestRepository.findAll();
        assertThat(allRequests).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationRequest persisted = allRequests.get(allRequests.size() - 1);

        ArgumentCaptor<RouDepreciationRequestDTO> captor = ArgumentCaptor.forClass(RouDepreciationRequestDTO.class);
        verify(rouDepreciationEntryCompilationJob, times(1)).compileROUDepreciationEntries(captor.capture());
        RouDepreciationRequestDTO intercepted = captor.getValue();
        assertThat(intercepted.getId()).isEqualTo(persisted.getId());
        assertThat(intercepted.getRequisitionId()).isEqualTo(persisted.getRequisitionId());
    }

    @Test
    @Transactional
    void creatingRequestWithExistingIdShouldFailAndNotTriggerCompilationJob() throws Exception {
        RouDepreciationRequest request = new RouDepreciationRequest().requisitionId(UUID.randomUUID());
        request.setId(1L);
        RouDepreciationRequestDTO requestDTO = rouDepreciationRequestMapper.toDto(request);

        restMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        verifyNoInteractions(rouDepreciationEntryCompilationJob);
    }

    @Test
    @Transactional
    void creatingRequestWithoutRequisitionIdShouldFailValidation() throws Exception {
        RouDepreciationRequestDTO requestDTO = new RouDepreciationRequestDTO();

        restMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        verifyNoInteractions(rouDepreciationEntryCompilationJob);
    }
}
