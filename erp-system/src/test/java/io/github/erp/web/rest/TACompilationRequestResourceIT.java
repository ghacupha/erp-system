package io.github.erp.web.rest;

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
import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.TACompilationRequest;
import io.github.erp.domain.enumeration.compilationProcessStatusTypes;
import io.github.erp.repository.TACompilationRequestRepository;
import io.github.erp.repository.search.TACompilationRequestSearchRepository;
import io.github.erp.service.criteria.TACompilationRequestCriteria;
import io.github.erp.service.dto.TACompilationRequestDTO;
import io.github.erp.service.mapper.TACompilationRequestMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TACompilationRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TACompilationRequestResourceIT {

    private static final UUID DEFAULT_REQUISITION_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUISITION_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final compilationProcessStatusTypes DEFAULT_COMPILATION_PROCESS_STATUS = compilationProcessStatusTypes.STARTED;
    private static final compilationProcessStatusTypes UPDATED_COMPILATION_PROCESS_STATUS = compilationProcessStatusTypes.RUNNING;

    private static final Integer DEFAULT_NUMBER_OF_ENUMERATED_ITEMS = 1;
    private static final Integer UPDATED_NUMBER_OF_ENUMERATED_ITEMS = 2;
    private static final Integer SMALLER_NUMBER_OF_ENUMERATED_ITEMS = 1 - 1;

    private static final UUID DEFAULT_BATCH_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_BATCH_JOB_IDENTIFIER = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_COMPILATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPILATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COMPILATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_INVALIDATED = false;
    private static final Boolean UPDATED_INVALIDATED = true;

    private static final String ENTITY_API_URL = "/api/ta-compilation-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-compilation-requests";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TACompilationRequestRepository tACompilationRequestRepository;

    @Autowired
    private TACompilationRequestMapper tACompilationRequestMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TACompilationRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private TACompilationRequestSearchRepository mockTACompilationRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTACompilationRequestMockMvc;

    private TACompilationRequest tACompilationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TACompilationRequest createEntity(EntityManager em) {
        TACompilationRequest tACompilationRequest = new TACompilationRequest()
            .requisitionId(DEFAULT_REQUISITION_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .compilationProcessStatus(DEFAULT_COMPILATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)
            .batchJobIdentifier(DEFAULT_BATCH_JOB_IDENTIFIER)
            .compilationTime(DEFAULT_COMPILATION_TIME)
            .invalidated(DEFAULT_INVALIDATED);
        return tACompilationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TACompilationRequest createUpdatedEntity(EntityManager em) {
        TACompilationRequest tACompilationRequest = new TACompilationRequest()
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationProcessStatus(UPDATED_COMPILATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);
        return tACompilationRequest;
    }

    @BeforeEach
    public void initTest() {
        tACompilationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createTACompilationRequest() throws Exception {
        int databaseSizeBeforeCreate = tACompilationRequestRepository.findAll().size();
        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);
        restTACompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        TACompilationRequest testTACompilationRequest = tACompilationRequestList.get(tACompilationRequestList.size() - 1);
        assertThat(testTACompilationRequest.getRequisitionId()).isEqualTo(DEFAULT_REQUISITION_ID);
        assertThat(testTACompilationRequest.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testTACompilationRequest.getCompilationProcessStatus()).isEqualTo(DEFAULT_COMPILATION_PROCESS_STATUS);
        assertThat(testTACompilationRequest.getNumberOfEnumeratedItems()).isEqualTo(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);
        assertThat(testTACompilationRequest.getBatchJobIdentifier()).isEqualTo(DEFAULT_BATCH_JOB_IDENTIFIER);
        assertThat(testTACompilationRequest.getCompilationTime()).isEqualTo(DEFAULT_COMPILATION_TIME);
        assertThat(testTACompilationRequest.getInvalidated()).isEqualTo(DEFAULT_INVALIDATED);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(1)).save(testTACompilationRequest);
    }

    @Test
    @Transactional
    void createTACompilationRequestWithExistingId() throws Exception {
        // Create the TACompilationRequest with an existing ID
        tACompilationRequest.setId(1L);
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        int databaseSizeBeforeCreate = tACompilationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTACompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void checkRequisitionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tACompilationRequestRepository.findAll().size();
        // set the field null
        tACompilationRequest.setRequisitionId(null);

        // Create the TACompilationRequest, which fails.
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        restTACompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatchJobIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tACompilationRequestRepository.findAll().size();
        // set the field null
        tACompilationRequest.setBatchJobIdentifier(null);

        // Create the TACompilationRequest, which fails.
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        restTACompilationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTACompilationRequests() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tACompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationProcessStatus").value(hasItem(DEFAULT_COMPILATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));
    }

    @Test
    @Transactional
    void getTACompilationRequest() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get the tACompilationRequest
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, tACompilationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tACompilationRequest.getId().intValue()))
            .andExpect(jsonPath("$.requisitionId").value(DEFAULT_REQUISITION_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.compilationProcessStatus").value(DEFAULT_COMPILATION_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.numberOfEnumeratedItems").value(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS))
            .andExpect(jsonPath("$.batchJobIdentifier").value(DEFAULT_BATCH_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationTime").value(sameInstant(DEFAULT_COMPILATION_TIME)))
            .andExpect(jsonPath("$.invalidated").value(DEFAULT_INVALIDATED.booleanValue()));
    }

    @Test
    @Transactional
    void getTACompilationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        Long id = tACompilationRequest.getId();

        defaultTACompilationRequestShouldBeFound("id.equals=" + id);
        defaultTACompilationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultTACompilationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTACompilationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultTACompilationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTACompilationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByRequisitionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where requisitionId equals to DEFAULT_REQUISITION_ID
        defaultTACompilationRequestShouldBeFound("requisitionId.equals=" + DEFAULT_REQUISITION_ID);

        // Get all the tACompilationRequestList where requisitionId equals to UPDATED_REQUISITION_ID
        defaultTACompilationRequestShouldNotBeFound("requisitionId.equals=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByRequisitionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where requisitionId not equals to DEFAULT_REQUISITION_ID
        defaultTACompilationRequestShouldNotBeFound("requisitionId.notEquals=" + DEFAULT_REQUISITION_ID);

        // Get all the tACompilationRequestList where requisitionId not equals to UPDATED_REQUISITION_ID
        defaultTACompilationRequestShouldBeFound("requisitionId.notEquals=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByRequisitionIdIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where requisitionId in DEFAULT_REQUISITION_ID or UPDATED_REQUISITION_ID
        defaultTACompilationRequestShouldBeFound("requisitionId.in=" + DEFAULT_REQUISITION_ID + "," + UPDATED_REQUISITION_ID);

        // Get all the tACompilationRequestList where requisitionId equals to UPDATED_REQUISITION_ID
        defaultTACompilationRequestShouldNotBeFound("requisitionId.in=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByRequisitionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where requisitionId is not null
        defaultTACompilationRequestShouldBeFound("requisitionId.specified=true");

        // Get all the tACompilationRequestList where requisitionId is null
        defaultTACompilationRequestShouldNotBeFound("requisitionId.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest is not null
        defaultTACompilationRequestShouldBeFound("timeOfRequest.specified=true");

        // Get all the tACompilationRequestList where timeOfRequest is null
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultTACompilationRequestShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the tACompilationRequestList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultTACompilationRequestShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationProcessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationProcessStatus equals to DEFAULT_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldBeFound("compilationProcessStatus.equals=" + DEFAULT_COMPILATION_PROCESS_STATUS);

        // Get all the tACompilationRequestList where compilationProcessStatus equals to UPDATED_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldNotBeFound("compilationProcessStatus.equals=" + UPDATED_COMPILATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationProcessStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationProcessStatus not equals to DEFAULT_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldNotBeFound("compilationProcessStatus.notEquals=" + DEFAULT_COMPILATION_PROCESS_STATUS);

        // Get all the tACompilationRequestList where compilationProcessStatus not equals to UPDATED_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldBeFound("compilationProcessStatus.notEquals=" + UPDATED_COMPILATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationProcessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationProcessStatus in DEFAULT_COMPILATION_PROCESS_STATUS or UPDATED_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldBeFound(
            "compilationProcessStatus.in=" + DEFAULT_COMPILATION_PROCESS_STATUS + "," + UPDATED_COMPILATION_PROCESS_STATUS
        );

        // Get all the tACompilationRequestList where compilationProcessStatus equals to UPDATED_COMPILATION_PROCESS_STATUS
        defaultTACompilationRequestShouldNotBeFound("compilationProcessStatus.in=" + UPDATED_COMPILATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationProcessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationProcessStatus is not null
        defaultTACompilationRequestShouldBeFound("compilationProcessStatus.specified=true");

        // Get all the tACompilationRequestList where compilationProcessStatus is null
        defaultTACompilationRequestShouldNotBeFound("compilationProcessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems equals to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.equals=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.equals=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems not equals to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.notEquals=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems not equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.notEquals=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems in DEFAULT_NUMBER_OF_ENUMERATED_ITEMS or UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound(
            "numberOfEnumeratedItems.in=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS + "," + UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        );

        // Get all the tACompilationRequestList where numberOfEnumeratedItems equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.in=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is not null
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.specified=true");

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is null
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is greater than or equal to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is greater than or equal to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is less than or equal to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is less than or equal to SMALLER_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.lessThanOrEqual=" + SMALLER_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is less than DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.lessThan=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is less than UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.lessThan=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByNumberOfEnumeratedItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is greater than DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldNotBeFound("numberOfEnumeratedItems.greaterThan=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the tACompilationRequestList where numberOfEnumeratedItems is greater than SMALLER_NUMBER_OF_ENUMERATED_ITEMS
        defaultTACompilationRequestShouldBeFound("numberOfEnumeratedItems.greaterThan=" + SMALLER_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByBatchJobIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where batchJobIdentifier equals to DEFAULT_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldBeFound("batchJobIdentifier.equals=" + DEFAULT_BATCH_JOB_IDENTIFIER);

        // Get all the tACompilationRequestList where batchJobIdentifier equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldNotBeFound("batchJobIdentifier.equals=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByBatchJobIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where batchJobIdentifier not equals to DEFAULT_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldNotBeFound("batchJobIdentifier.notEquals=" + DEFAULT_BATCH_JOB_IDENTIFIER);

        // Get all the tACompilationRequestList where batchJobIdentifier not equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldBeFound("batchJobIdentifier.notEquals=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByBatchJobIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where batchJobIdentifier in DEFAULT_BATCH_JOB_IDENTIFIER or UPDATED_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldBeFound(
            "batchJobIdentifier.in=" + DEFAULT_BATCH_JOB_IDENTIFIER + "," + UPDATED_BATCH_JOB_IDENTIFIER
        );

        // Get all the tACompilationRequestList where batchJobIdentifier equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultTACompilationRequestShouldNotBeFound("batchJobIdentifier.in=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByBatchJobIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where batchJobIdentifier is not null
        defaultTACompilationRequestShouldBeFound("batchJobIdentifier.specified=true");

        // Get all the tACompilationRequestList where batchJobIdentifier is null
        defaultTACompilationRequestShouldNotBeFound("batchJobIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime equals to DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.equals=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.equals=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime not equals to DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.notEquals=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime not equals to UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.notEquals=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime in DEFAULT_COMPILATION_TIME or UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.in=" + DEFAULT_COMPILATION_TIME + "," + UPDATED_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.in=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime is not null
        defaultTACompilationRequestShouldBeFound("compilationTime.specified=true");

        // Get all the tACompilationRequestList where compilationTime is null
        defaultTACompilationRequestShouldNotBeFound("compilationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime is greater than or equal to DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.greaterThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime is greater than or equal to UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.greaterThanOrEqual=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime is less than or equal to DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.lessThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime is less than or equal to SMALLER_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.lessThanOrEqual=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime is less than DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.lessThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime is less than UPDATED_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.lessThan=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByCompilationTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where compilationTime is greater than DEFAULT_COMPILATION_TIME
        defaultTACompilationRequestShouldNotBeFound("compilationTime.greaterThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the tACompilationRequestList where compilationTime is greater than SMALLER_COMPILATION_TIME
        defaultTACompilationRequestShouldBeFound("compilationTime.greaterThan=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByInvalidatedIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where invalidated equals to DEFAULT_INVALIDATED
        defaultTACompilationRequestShouldBeFound("invalidated.equals=" + DEFAULT_INVALIDATED);

        // Get all the tACompilationRequestList where invalidated equals to UPDATED_INVALIDATED
        defaultTACompilationRequestShouldNotBeFound("invalidated.equals=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByInvalidatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where invalidated not equals to DEFAULT_INVALIDATED
        defaultTACompilationRequestShouldNotBeFound("invalidated.notEquals=" + DEFAULT_INVALIDATED);

        // Get all the tACompilationRequestList where invalidated not equals to UPDATED_INVALIDATED
        defaultTACompilationRequestShouldBeFound("invalidated.notEquals=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByInvalidatedIsInShouldWork() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where invalidated in DEFAULT_INVALIDATED or UPDATED_INVALIDATED
        defaultTACompilationRequestShouldBeFound("invalidated.in=" + DEFAULT_INVALIDATED + "," + UPDATED_INVALIDATED);

        // Get all the tACompilationRequestList where invalidated equals to UPDATED_INVALIDATED
        defaultTACompilationRequestShouldNotBeFound("invalidated.in=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByInvalidatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        // Get all the tACompilationRequestList where invalidated is not null
        defaultTACompilationRequestShouldBeFound("invalidated.specified=true");

        // Get all the tACompilationRequestList where invalidated is null
        defaultTACompilationRequestShouldNotBeFound("invalidated.specified=false");
    }

    @Test
    @Transactional
    void getAllTACompilationRequestsByInitiatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);
        ApplicationUser initiatedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            initiatedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(initiatedBy);
            em.flush();
        } else {
            initiatedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(initiatedBy);
        em.flush();
        tACompilationRequest.setInitiatedBy(initiatedBy);
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);
        Long initiatedById = initiatedBy.getId();

        // Get all the tACompilationRequestList where initiatedBy equals to initiatedById
        defaultTACompilationRequestShouldBeFound("initiatedById.equals=" + initiatedById);

        // Get all the tACompilationRequestList where initiatedBy equals to (initiatedById + 1)
        defaultTACompilationRequestShouldNotBeFound("initiatedById.equals=" + (initiatedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTACompilationRequestShouldBeFound(String filter) throws Exception {
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tACompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationProcessStatus").value(hasItem(DEFAULT_COMPILATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));

        // Check, that the count call also returns 1
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTACompilationRequestShouldNotBeFound(String filter) throws Exception {
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTACompilationRequest() throws Exception {
        // Get the tACompilationRequest
        restTACompilationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTACompilationRequest() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();

        // Update the tACompilationRequest
        TACompilationRequest updatedTACompilationRequest = tACompilationRequestRepository.findById(tACompilationRequest.getId()).get();
        // Disconnect from session so that the updates on updatedTACompilationRequest are not directly saved in db
        em.detach(updatedTACompilationRequest);
        updatedTACompilationRequest
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationProcessStatus(UPDATED_COMPILATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(updatedTACompilationRequest);

        restTACompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tACompilationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        TACompilationRequest testTACompilationRequest = tACompilationRequestList.get(tACompilationRequestList.size() - 1);
        assertThat(testTACompilationRequest.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
        assertThat(testTACompilationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testTACompilationRequest.getCompilationProcessStatus()).isEqualTo(UPDATED_COMPILATION_PROCESS_STATUS);
        assertThat(testTACompilationRequest.getNumberOfEnumeratedItems()).isEqualTo(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
        assertThat(testTACompilationRequest.getBatchJobIdentifier()).isEqualTo(UPDATED_BATCH_JOB_IDENTIFIER);
        assertThat(testTACompilationRequest.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testTACompilationRequest.getInvalidated()).isEqualTo(UPDATED_INVALIDATED);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository).save(testTACompilationRequest);
    }

    @Test
    @Transactional
    void putNonExistingTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tACompilationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void putWithIdMismatchTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void partialUpdateTACompilationRequestWithPatch() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();

        // Update the tACompilationRequest using partial update
        TACompilationRequest partialUpdatedTACompilationRequest = new TACompilationRequest();
        partialUpdatedTACompilationRequest.setId(tACompilationRequest.getId());

        partialUpdatedTACompilationRequest
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationProcessStatus(UPDATED_COMPILATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);

        restTACompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTACompilationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTACompilationRequest))
            )
            .andExpect(status().isOk());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        TACompilationRequest testTACompilationRequest = tACompilationRequestList.get(tACompilationRequestList.size() - 1);
        assertThat(testTACompilationRequest.getRequisitionId()).isEqualTo(DEFAULT_REQUISITION_ID);
        assertThat(testTACompilationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testTACompilationRequest.getCompilationProcessStatus()).isEqualTo(UPDATED_COMPILATION_PROCESS_STATUS);
        assertThat(testTACompilationRequest.getNumberOfEnumeratedItems()).isEqualTo(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
        assertThat(testTACompilationRequest.getBatchJobIdentifier()).isEqualTo(DEFAULT_BATCH_JOB_IDENTIFIER);
        assertThat(testTACompilationRequest.getCompilationTime()).isEqualTo(DEFAULT_COMPILATION_TIME);
        assertThat(testTACompilationRequest.getInvalidated()).isEqualTo(DEFAULT_INVALIDATED);
    }

    @Test
    @Transactional
    void fullUpdateTACompilationRequestWithPatch() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();

        // Update the tACompilationRequest using partial update
        TACompilationRequest partialUpdatedTACompilationRequest = new TACompilationRequest();
        partialUpdatedTACompilationRequest.setId(tACompilationRequest.getId());

        partialUpdatedTACompilationRequest
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .compilationProcessStatus(UPDATED_COMPILATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);

        restTACompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTACompilationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTACompilationRequest))
            )
            .andExpect(status().isOk());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);
        TACompilationRequest testTACompilationRequest = tACompilationRequestList.get(tACompilationRequestList.size() - 1);
        assertThat(testTACompilationRequest.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
        assertThat(testTACompilationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testTACompilationRequest.getCompilationProcessStatus()).isEqualTo(UPDATED_COMPILATION_PROCESS_STATUS);
        assertThat(testTACompilationRequest.getNumberOfEnumeratedItems()).isEqualTo(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
        assertThat(testTACompilationRequest.getBatchJobIdentifier()).isEqualTo(UPDATED_BATCH_JOB_IDENTIFIER);
        assertThat(testTACompilationRequest.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testTACompilationRequest.getInvalidated()).isEqualTo(UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void patchNonExistingTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tACompilationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTACompilationRequest() throws Exception {
        int databaseSizeBeforeUpdate = tACompilationRequestRepository.findAll().size();
        tACompilationRequest.setId(count.incrementAndGet());

        // Create the TACompilationRequest
        TACompilationRequestDTO tACompilationRequestDTO = tACompilationRequestMapper.toDto(tACompilationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTACompilationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tACompilationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TACompilationRequest in the database
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(0)).save(tACompilationRequest);
    }

    @Test
    @Transactional
    void deleteTACompilationRequest() throws Exception {
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);

        int databaseSizeBeforeDelete = tACompilationRequestRepository.findAll().size();

        // Delete the tACompilationRequest
        restTACompilationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, tACompilationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TACompilationRequest> tACompilationRequestList = tACompilationRequestRepository.findAll();
        assertThat(tACompilationRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TACompilationRequest in Elasticsearch
        verify(mockTACompilationRequestSearchRepository, times(1)).deleteById(tACompilationRequest.getId());
    }

    @Test
    @Transactional
    void searchTACompilationRequest() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tACompilationRequestRepository.saveAndFlush(tACompilationRequest);
        when(mockTACompilationRequestSearchRepository.search("id:" + tACompilationRequest.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tACompilationRequest), PageRequest.of(0, 1), 1));

        // Search the tACompilationRequest
        restTACompilationRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tACompilationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tACompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationProcessStatus").value(hasItem(DEFAULT_COMPILATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));
    }
}
