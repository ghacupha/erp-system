package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.domain.enumeration.CompilationStatusTypes;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.service.PrepaymentCompilationRequestService;
import io.github.erp.service.criteria.PrepaymentCompilationRequestCriteria;
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
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final CompilationStatusTypes DEFAULT_COMPILATION_STATUS = CompilationStatusTypes.STARTED;
    private static final CompilationStatusTypes UPDATED_COMPILATION_STATUS = CompilationStatusTypes.IN_PROGRESS;

    private static final Integer DEFAULT_ITEMS_PROCESSED = 1;
    private static final Integer UPDATED_ITEMS_PROCESSED = 2;
    private static final Integer SMALLER_ITEMS_PROCESSED = 1 - 1;

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
    void getPrepaymentCompilationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        Long id = prepaymentCompilationRequest.getId();

        defaultPrepaymentCompilationRequestShouldBeFound("id.equals=" + id);
        defaultPrepaymentCompilationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentCompilationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentCompilationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentCompilationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentCompilationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is not null
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.specified=true");

        // Get all the prepaymentCompilationRequestList where timeOfRequest is null
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the prepaymentCompilationRequestList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultPrepaymentCompilationRequestShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationStatus equals to DEFAULT_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldBeFound("compilationStatus.equals=" + DEFAULT_COMPILATION_STATUS);

        // Get all the prepaymentCompilationRequestList where compilationStatus equals to UPDATED_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationStatus.equals=" + UPDATED_COMPILATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationStatus not equals to DEFAULT_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationStatus.notEquals=" + DEFAULT_COMPILATION_STATUS);

        // Get all the prepaymentCompilationRequestList where compilationStatus not equals to UPDATED_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldBeFound("compilationStatus.notEquals=" + UPDATED_COMPILATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationStatus in DEFAULT_COMPILATION_STATUS or UPDATED_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldBeFound(
            "compilationStatus.in=" + DEFAULT_COMPILATION_STATUS + "," + UPDATED_COMPILATION_STATUS
        );

        // Get all the prepaymentCompilationRequestList where compilationStatus equals to UPDATED_COMPILATION_STATUS
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationStatus.in=" + UPDATED_COMPILATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationStatus is not null
        defaultPrepaymentCompilationRequestShouldBeFound("compilationStatus.specified=true");

        // Get all the prepaymentCompilationRequestList where compilationStatus is null
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed equals to DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.equals=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed equals to UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.equals=" + UPDATED_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed not equals to DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.notEquals=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed not equals to UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.notEquals=" + UPDATED_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed in DEFAULT_ITEMS_PROCESSED or UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.in=" + DEFAULT_ITEMS_PROCESSED + "," + UPDATED_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed equals to UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.in=" + UPDATED_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is not null
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.specified=true");

        // Get all the prepaymentCompilationRequestList where itemsProcessed is null
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is greater than or equal to DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.greaterThanOrEqual=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is greater than or equal to UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.greaterThanOrEqual=" + UPDATED_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is less than or equal to DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.lessThanOrEqual=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is less than or equal to SMALLER_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.lessThanOrEqual=" + SMALLER_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is less than DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.lessThan=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is less than UPDATED_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.lessThan=" + UPDATED_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByItemsProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is greater than DEFAULT_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldNotBeFound("itemsProcessed.greaterThan=" + DEFAULT_ITEMS_PROCESSED);

        // Get all the prepaymentCompilationRequestList where itemsProcessed is greater than SMALLER_ITEMS_PROCESSED
        defaultPrepaymentCompilationRequestShouldBeFound("itemsProcessed.greaterThan=" + SMALLER_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the prepaymentCompilationRequestList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the prepaymentCompilationRequestList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldBeFound(
            "compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN
        );

        // Get all the prepaymentCompilationRequestList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);

        // Get all the prepaymentCompilationRequestList where compilationToken is not null
        defaultPrepaymentCompilationRequestShouldBeFound("compilationToken.specified=true");

        // Get all the prepaymentCompilationRequestList where compilationToken is null
        defaultPrepaymentCompilationRequestShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentCompilationRequestsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        prepaymentCompilationRequest.addPlaceholder(placeholder);
        prepaymentCompilationRequestRepository.saveAndFlush(prepaymentCompilationRequest);
        Long placeholderId = placeholder.getId();

        // Get all the prepaymentCompilationRequestList where placeholder equals to placeholderId
        defaultPrepaymentCompilationRequestShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the prepaymentCompilationRequestList where placeholder equals to (placeholderId + 1)
        defaultPrepaymentCompilationRequestShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentCompilationRequestShouldBeFound(String filter) throws Exception {
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentCompilationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].itemsProcessed").value(hasItem(DEFAULT_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));

        // Check, that the count call also returns 1
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentCompilationRequestShouldNotBeFound(String filter) throws Exception {
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentCompilationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
