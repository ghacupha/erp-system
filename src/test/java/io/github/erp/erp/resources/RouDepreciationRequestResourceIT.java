package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import io.github.erp.repository.RouDepreciationRequestRepository;
import io.github.erp.repository.search.RouDepreciationRequestSearchRepository;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
import io.github.erp.web.rest.RouDepreciationRequestResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RouDepreciationRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouDepreciationRequestResourceIT {

    private static final UUID DEFAULT_REQUISITION_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUISITION_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final depreciationProcessStatusTypes DEFAULT_DEPRECIATION_PROCESS_STATUS = depreciationProcessStatusTypes.STARTED;
    private static final depreciationProcessStatusTypes UPDATED_DEPRECIATION_PROCESS_STATUS = depreciationProcessStatusTypes.RUNNING;

    private static final Integer DEFAULT_NUMBER_OF_ENUMERATED_ITEMS = 1;
    private static final Integer UPDATED_NUMBER_OF_ENUMERATED_ITEMS = 2;
    private static final Integer SMALLER_NUMBER_OF_ENUMERATED_ITEMS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/leases/rou-depreciation-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-depreciation-requests";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationRequestRepository rouDepreciationRequestRepository;

    @Autowired
    private RouDepreciationRequestMapper rouDepreciationRequestMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationRequestSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationRequestSearchRepository mockRouDepreciationRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationRequestMockMvc;

    private RouDepreciationRequest rouDepreciationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationRequest createEntity(EntityManager em) {
        RouDepreciationRequest rouDepreciationRequest = new RouDepreciationRequest()
            .requisitionId(DEFAULT_REQUISITION_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .depreciationProcessStatus(DEFAULT_DEPRECIATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);
        return rouDepreciationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationRequest createUpdatedEntity(EntityManager em) {
        RouDepreciationRequest rouDepreciationRequest = new RouDepreciationRequest()
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .depreciationProcessStatus(UPDATED_DEPRECIATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
        return rouDepreciationRequest;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationRequestRepository.findAll().size();
        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);
        restRouDepreciationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationRequest testRouDepreciationRequest = rouDepreciationRequestList.get(rouDepreciationRequestList.size() - 1);
        assertThat(testRouDepreciationRequest.getRequisitionId()).isEqualTo(DEFAULT_REQUISITION_ID);
        assertThat(testRouDepreciationRequest.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouDepreciationRequest.getDepreciationProcessStatus()).isEqualTo(DEFAULT_DEPRECIATION_PROCESS_STATUS);
        assertThat(testRouDepreciationRequest.getNumberOfEnumeratedItems()).isEqualTo(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(1)).save(testRouDepreciationRequest);
    }

    @Test
    @Transactional
    void createRouDepreciationRequestWithExistingId() throws Exception {
        // Create the RouDepreciationRequest with an existing ID
        rouDepreciationRequest.setId(1L);
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        int databaseSizeBeforeCreate = rouDepreciationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouDepreciationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void checkRequisitionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationRequestRepository.findAll().size();
        // set the field null
        rouDepreciationRequest.setRequisitionId(null);

        // Create the RouDepreciationRequest, which fails.
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        restRouDepreciationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequests() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].depreciationProcessStatus").value(hasItem(DEFAULT_DEPRECIATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)));
    }

    @Test
    @Transactional
    void getRouDepreciationRequest() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get the rouDepreciationRequest
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationRequest.getId().intValue()))
            .andExpect(jsonPath("$.requisitionId").value(DEFAULT_REQUISITION_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.depreciationProcessStatus").value(DEFAULT_DEPRECIATION_PROCESS_STATUS.toString()))
            .andExpect(jsonPath("$.numberOfEnumeratedItems").value(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS));
    }

    @Test
    @Transactional
    void getRouDepreciationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        Long id = rouDepreciationRequest.getId();

        defaultRouDepreciationRequestShouldBeFound("id.equals=" + id);
        defaultRouDepreciationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByRequisitionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where requisitionId equals to DEFAULT_REQUISITION_ID
        defaultRouDepreciationRequestShouldBeFound("requisitionId.equals=" + DEFAULT_REQUISITION_ID);

        // Get all the rouDepreciationRequestList where requisitionId equals to UPDATED_REQUISITION_ID
        defaultRouDepreciationRequestShouldNotBeFound("requisitionId.equals=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByRequisitionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where requisitionId not equals to DEFAULT_REQUISITION_ID
        defaultRouDepreciationRequestShouldNotBeFound("requisitionId.notEquals=" + DEFAULT_REQUISITION_ID);

        // Get all the rouDepreciationRequestList where requisitionId not equals to UPDATED_REQUISITION_ID
        defaultRouDepreciationRequestShouldBeFound("requisitionId.notEquals=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByRequisitionIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where requisitionId in DEFAULT_REQUISITION_ID or UPDATED_REQUISITION_ID
        defaultRouDepreciationRequestShouldBeFound("requisitionId.in=" + DEFAULT_REQUISITION_ID + "," + UPDATED_REQUISITION_ID);

        // Get all the rouDepreciationRequestList where requisitionId equals to UPDATED_REQUISITION_ID
        defaultRouDepreciationRequestShouldNotBeFound("requisitionId.in=" + UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByRequisitionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where requisitionId is not null
        defaultRouDepreciationRequestShouldBeFound("requisitionId.specified=true");

        // Get all the rouDepreciationRequestList where requisitionId is null
        defaultRouDepreciationRequestShouldNotBeFound("requisitionId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest is not null
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouDepreciationRequestList where timeOfRequest is null
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationRequestList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationRequestShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByDepreciationProcessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus equals to DEFAULT_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldBeFound("depreciationProcessStatus.equals=" + DEFAULT_DEPRECIATION_PROCESS_STATUS);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus equals to UPDATED_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldNotBeFound("depreciationProcessStatus.equals=" + UPDATED_DEPRECIATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByDepreciationProcessStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus not equals to DEFAULT_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldNotBeFound("depreciationProcessStatus.notEquals=" + DEFAULT_DEPRECIATION_PROCESS_STATUS);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus not equals to UPDATED_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldBeFound("depreciationProcessStatus.notEquals=" + UPDATED_DEPRECIATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByDepreciationProcessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus in DEFAULT_DEPRECIATION_PROCESS_STATUS or UPDATED_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldBeFound(
            "depreciationProcessStatus.in=" + DEFAULT_DEPRECIATION_PROCESS_STATUS + "," + UPDATED_DEPRECIATION_PROCESS_STATUS
        );

        // Get all the rouDepreciationRequestList where depreciationProcessStatus equals to UPDATED_DEPRECIATION_PROCESS_STATUS
        defaultRouDepreciationRequestShouldNotBeFound("depreciationProcessStatus.in=" + UPDATED_DEPRECIATION_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByDepreciationProcessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where depreciationProcessStatus is not null
        defaultRouDepreciationRequestShouldBeFound("depreciationProcessStatus.specified=true");

        // Get all the rouDepreciationRequestList where depreciationProcessStatus is null
        defaultRouDepreciationRequestShouldNotBeFound("depreciationProcessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems equals to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.equals=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.equals=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems not equals to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.notEquals=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems not equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.notEquals=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems in DEFAULT_NUMBER_OF_ENUMERATED_ITEMS or UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound(
            "numberOfEnumeratedItems.in=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS + "," + UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        );

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems equals to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.in=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is not null
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.specified=true");

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is null
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is greater than or equal to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is greater than or equal to UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is less than or equal to DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is less than or equal to SMALLER_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.lessThanOrEqual=" + SMALLER_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is less than DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.lessThan=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is less than UPDATED_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.lessThan=" + UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByNumberOfEnumeratedItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is greater than DEFAULT_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldNotBeFound("numberOfEnumeratedItems.greaterThan=" + DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);

        // Get all the rouDepreciationRequestList where numberOfEnumeratedItems is greater than SMALLER_NUMBER_OF_ENUMERATED_ITEMS
        defaultRouDepreciationRequestShouldBeFound("numberOfEnumeratedItems.greaterThan=" + SMALLER_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationRequestsByInitiatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);
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
        rouDepreciationRequest.setInitiatedBy(initiatedBy);
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);
        Long initiatedById = initiatedBy.getId();

        // Get all the rouDepreciationRequestList where initiatedBy equals to initiatedById
        defaultRouDepreciationRequestShouldBeFound("initiatedById.equals=" + initiatedById);

        // Get all the rouDepreciationRequestList where initiatedBy equals to (initiatedById + 1)
        defaultRouDepreciationRequestShouldNotBeFound("initiatedById.equals=" + (initiatedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationRequestShouldBeFound(String filter) throws Exception {
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].depreciationProcessStatus").value(hasItem(DEFAULT_DEPRECIATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)));

        // Check, that the count call also returns 1
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationRequestShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationRequest() throws Exception {
        // Get the rouDepreciationRequest
        restRouDepreciationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouDepreciationRequest() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();

        // Update the rouDepreciationRequest
        RouDepreciationRequest updatedRouDepreciationRequest = rouDepreciationRequestRepository
            .findById(rouDepreciationRequest.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouDepreciationRequest are not directly saved in db
        em.detach(updatedRouDepreciationRequest);
        updatedRouDepreciationRequest
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .depreciationProcessStatus(UPDATED_DEPRECIATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(updatedRouDepreciationRequest);

        restRouDepreciationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationRequest testRouDepreciationRequest = rouDepreciationRequestList.get(rouDepreciationRequestList.size() - 1);
        assertThat(testRouDepreciationRequest.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
        assertThat(testRouDepreciationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationRequest.getDepreciationProcessStatus()).isEqualTo(UPDATED_DEPRECIATION_PROCESS_STATUS);
        assertThat(testRouDepreciationRequest.getNumberOfEnumeratedItems()).isEqualTo(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository).save(testRouDepreciationRequest);
    }

    @Test
    @Transactional
    void putNonExistingRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void partialUpdateRouDepreciationRequestWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();

        // Update the rouDepreciationRequest using partial update
        RouDepreciationRequest partialUpdatedRouDepreciationRequest = new RouDepreciationRequest();
        partialUpdatedRouDepreciationRequest.setId(rouDepreciationRequest.getId());

        partialUpdatedRouDepreciationRequest
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .depreciationProcessStatus(UPDATED_DEPRECIATION_PROCESS_STATUS);

        restRouDepreciationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationRequest))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationRequest testRouDepreciationRequest = rouDepreciationRequestList.get(rouDepreciationRequestList.size() - 1);
        assertThat(testRouDepreciationRequest.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
        assertThat(testRouDepreciationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationRequest.getDepreciationProcessStatus()).isEqualTo(UPDATED_DEPRECIATION_PROCESS_STATUS);
        assertThat(testRouDepreciationRequest.getNumberOfEnumeratedItems()).isEqualTo(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void fullUpdateRouDepreciationRequestWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();

        // Update the rouDepreciationRequest using partial update
        RouDepreciationRequest partialUpdatedRouDepreciationRequest = new RouDepreciationRequest();
        partialUpdatedRouDepreciationRequest.setId(rouDepreciationRequest.getId());

        partialUpdatedRouDepreciationRequest
            .requisitionId(UPDATED_REQUISITION_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .depreciationProcessStatus(UPDATED_DEPRECIATION_PROCESS_STATUS)
            .numberOfEnumeratedItems(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);

        restRouDepreciationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationRequest))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationRequest testRouDepreciationRequest = rouDepreciationRequestList.get(rouDepreciationRequestList.size() - 1);
        assertThat(testRouDepreciationRequest.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
        assertThat(testRouDepreciationRequest.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationRequest.getDepreciationProcessStatus()).isEqualTo(UPDATED_DEPRECIATION_PROCESS_STATUS);
        assertThat(testRouDepreciationRequest.getNumberOfEnumeratedItems()).isEqualTo(UPDATED_NUMBER_OF_ENUMERATED_ITEMS);
    }

    @Test
    @Transactional
    void patchNonExistingRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouDepreciationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouDepreciationRequest() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationRequestRepository.findAll().size();
        rouDepreciationRequest.setId(count.incrementAndGet());

        // Create the RouDepreciationRequest
        RouDepreciationRequestDTO rouDepreciationRequestDTO = rouDepreciationRequestMapper.toDto(rouDepreciationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationRequest in the database
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(0)).save(rouDepreciationRequest);
    }

    @Test
    @Transactional
    void deleteRouDepreciationRequest() throws Exception {
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);

        int databaseSizeBeforeDelete = rouDepreciationRequestRepository.findAll().size();

        // Delete the rouDepreciationRequest
        restRouDepreciationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouDepreciationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouDepreciationRequest> rouDepreciationRequestList = rouDepreciationRequestRepository.findAll();
        assertThat(rouDepreciationRequestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouDepreciationRequest in Elasticsearch
        verify(mockRouDepreciationRequestSearchRepository, times(1)).deleteById(rouDepreciationRequest.getId());
    }

    @Test
    @Transactional
    void searchRouDepreciationRequest() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationRequestRepository.saveAndFlush(rouDepreciationRequest);
        when(mockRouDepreciationRequestSearchRepository.search("id:" + rouDepreciationRequest.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationRequest), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationRequest
        restRouDepreciationRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].depreciationProcessStatus").value(hasItem(DEFAULT_DEPRECIATION_PROCESS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfEnumeratedItems").value(hasItem(DEFAULT_NUMBER_OF_ENUMERATED_ITEMS)));
    }
}
