package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.domain.enumeration.CompilationStatusTypes;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.service.PrepaymentCompilationRequestService;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.PrepaymentCompilationRequestMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrepaymentCompilationRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentCompilationRequestResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final CompilationStatusTypes DEFAULT_COMPILATION_STATUS = CompilationStatusTypes.STARTED;
    private static final CompilationStatusTypes UPDATED_COMPILATION_STATUS = CompilationStatusTypes.IN_PROGRESS;

    private static final Integer DEFAULT_ITEMS_PROCESSED = 1;
    private static final Integer UPDATED_ITEMS_PROCESSED = 2;

    private static final UUID DEFAULT_COMPILATION_TOKEN = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_TOKEN = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/prepayment-compilation-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-compilation-requests";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository;

    @Mock
    private PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepositoryMock;

    @Autowired
    private PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;

    @Mock
    private PrepaymentCompilationRequestService prepaymentCompilationRequestServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentCompilationRequestSearchRepository mockPrepaymentCompilationRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentCompilationRequestMockMvc;

    private PrepaymentCompilationRequest prepaymentCompilationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentCompilationRequest createEntity(EntityManager em) {
        PrepaymentCompilationRequest prepaymentCompilationRequest = new PrepaymentCompilationRequest()
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .compilationStatus(DEFAULT_COMPILATION_STATUS)
            .itemsProcessed(DEFAULT_ITEMS_PROCESSED)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return prepaymentCompilationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentCompilationRequest createUpdatedEntity(EntityManager em) {
        PrepaymentCompilationRequest prepaymentCompilationRequest = new PrepaymentCompilationRequest()
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .itemsProcessed(UPDATED_ITEMS_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return prepaymentCompilationRequest;
    }

    @BeforeEach
    public void initTest() {
        prepaymentCompilationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeCreate = prepaymentCompilationRequestRepository.findAll().size();
        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );
        restPrepaymentCompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentCompilationRequest testPrepaymentCompilationRequest = prepaymentCompilationRequestList.get(
            prepaymentCompilationRequestList.size() - 1
        );
        assertThat(testPrepaymentCompilationRequest.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testPrepaymentCompilationRequest.getCompilationStatus()).isEqualTo(DEFAULT_COMPILATION_STATUS);
        assertThat(testPrepaymentCompilationRequest.getItemsProcessed()).isEqualTo(DEFAULT_ITEMS_PROCESSED);
        assertThat(testPrepaymentCompilationRequest.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(1)).save(testPrepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void createPrepaymentCompilationRequestWithExistingId() throws Exception {
        // Create the PrepaymentCompilationRequest with an existing ID
        prepaymentCompilationRequest.setId(1L);
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        int databaseSizeBeforeCreate = prepaymentCompilationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentCompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void checkCompilationTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentCompilationRequestRepository.findAll().size();
        // set the field null
        prepaymentCompilationRequest.setCompilationToken(null);

        // Create the PrepaymentCompilationRequest, which fails.
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        restPrepaymentCompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequests() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentCompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemsProcessed").value(hasItem(DEFAULT_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentCompilationRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prepaymentCompilationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentCompilationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentCompilationRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentCompilationRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prepaymentCompilationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentCompilationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentCompilationRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPrepaymentCompilationRequest() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get the prepaymentCompilationRequest
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentCompilationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentCompilationRequest.getId().intValue()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.compilationStatus").value(DEFAULT_COMPILATION_STATUS.toString()))
            .andExpect(jsonPath("$.itemsProcessed").value(DEFAULT_ITEMS_PROCESSED))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentCompilationRequest() throws Exception {
        // Get the prepaymentCompilationRequest
        restPrepaymentCompilationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentCompilationRequest() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();

        // Update the prepaymentCompilationRequest
        PrepaymentCompilationRequest updatedPrepaymentCompilationRequest = prepaymentCompilationRequestRepository
            .findById(prepaymentCompilationRequest.getId())
            .get();
        // Disconnect from session so that the updates on updatedPrepaymentCompilationRequest are not directly saved in db
        em.detach(updatedPrepaymentCompilationRequest);
        updatedPrepaymentCompilationRequest
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .itemsProcessed(UPDATED_ITEMS_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            updatedPrepaymentCompilationRequest
        );

        restPrepaymentCompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentCompilationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentCompilationRequest testPrepaymentCompilationRequest = prepaymentCompilationRequestList.get(
            prepaymentCompilationRequestList.size() - 1
        );
        assertThat(testPrepaymentCompilationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testPrepaymentCompilationRequest.getCompilationStatus()).isEqualTo(UPDATED_COMPILATION_STATUS);
        assertThat(testPrepaymentCompilationRequest.getItemsProcessed()).isEqualTo(UPDATED_ITEMS_PROCESSED);
        assertThat(testPrepaymentCompilationRequest.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository).save(testPrepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentCompilationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentCompilationRequestWithPatch() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();

        // Update the prepaymentCompilationRequest using partial update
        PrepaymentCompilationRequest partialUpdatedPrepaymentCompilationRequest = new PrepaymentCompilationRequest();
        partialUpdatedPrepaymentCompilationRequest.setId(prepaymentCompilationRequest.getId());

        restPrepaymentCompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentCompilationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentCompilationRequest))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentCompilationRequest testPrepaymentCompilationRequest = prepaymentCompilationRequestList.get(
            prepaymentCompilationRequestList.size() - 1
        );
        assertThat(testPrepaymentCompilationRequest.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testPrepaymentCompilationRequest.getCompilationStatus()).isEqualTo(DEFAULT_COMPILATION_STATUS);
        assertThat(testPrepaymentCompilationRequest.getItemsProcessed()).isEqualTo(DEFAULT_ITEMS_PROCESSED);
        assertThat(testPrepaymentCompilationRequest.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentCompilationRequestWithPatch() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();

        // Update the prepaymentCompilationRequest using partial update
        PrepaymentCompilationRequest partialUpdatedPrepaymentCompilationRequest = new PrepaymentCompilationRequest();
        partialUpdatedPrepaymentCompilationRequest.setId(prepaymentCompilationRequest.getId());

        partialUpdatedPrepaymentCompilationRequest
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .itemsProcessed(UPDATED_ITEMS_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restPrepaymentCompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentCompilationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentCompilationRequest))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentCompilationRequest testPrepaymentCompilationRequest = prepaymentCompilationRequestList.get(
            prepaymentCompilationRequestList.size() - 1
        );
        assertThat(testPrepaymentCompilationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testPrepaymentCompilationRequest.getCompilationStatus()).isEqualTo(UPDATED_COMPILATION_STATUS);
        assertThat(testPrepaymentCompilationRequest.getItemsProcessed()).isEqualTo(UPDATED_ITEMS_PROCESSED);
        assertThat(testPrepaymentCompilationRequest.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentCompilationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentCompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentCompilationRequestRepository.findAll().size();
        prepaymentCompilationRequest.setId(count.incrementAndGet());

        // Create the PrepaymentCompilationRequest
        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = prepaymentCompilationRequestMapper.toDto(
            prepaymentCompilationRequest
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentCompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentCompilationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentCompilationRequest in the database
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(0)).save(prepaymentCompilationRequest);
    }

    @Test
    @Transactional
    void deletePrepaymentCompilationRequest() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        int databaseSizeBeforeDelete = prepaymentCompilationRequestRepository.findAll().size();

        // Delete the prepaymentCompilationRequest
        restPrepaymentCompilationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentCompilationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentCompilationRequest> prepaymentCompilationRequestList = prepaymentCompilationRequestRepository.findAll();
        assertThat(prepaymentCompilationRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentCompilationRequest in Elasticsearch
        verify(mockPrepaymentCompilationRequestSearchRepository, times(1)).deleteById(prepaymentCompilationRequest.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentCompilationRequest() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);
        when(mockPrepaymentCompilationRequestSearchRepository.search("id:" + prepaymentCompilationRequest.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentCompilationRequest), PageRequest.of(0, 1), 1));

        // Search the prepaymentCompilationRequest
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentCompilationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentCompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemsProcessed").value(hasItem(DEFAULT_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));
    }
}
