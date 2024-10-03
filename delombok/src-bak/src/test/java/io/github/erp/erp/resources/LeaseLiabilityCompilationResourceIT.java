package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepository;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.mapper.LeaseLiabilityCompilationMapper;
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
 * Integration tests for the LeaseLiabilityCompilationResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseLiabilityCompilationResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/leases/lease-liability-compilations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-liability-compilations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;

    @Autowired
    private LeaseLiabilityCompilationMapper leaseLiabilityCompilationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityCompilationSearchRepository mockLeaseLiabilityCompilationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityCompilationMockMvc;

    private LeaseLiabilityCompilation leaseLiabilityCompilation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityCompilation createEntity(EntityManager em) {
        LeaseLiabilityCompilation leaseLiabilityCompilation = new LeaseLiabilityCompilation()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST);
        return leaseLiabilityCompilation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityCompilation createUpdatedEntity(EntityManager em) {
        LeaseLiabilityCompilation leaseLiabilityCompilation = new LeaseLiabilityCompilation()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST);
        return leaseLiabilityCompilation;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityCompilation = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityCompilationRepository.findAll().size();
        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);
        restLeaseLiabilityCompilationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityCompilation testLeaseLiabilityCompilation = leaseLiabilityCompilationList.get(
            leaseLiabilityCompilationList.size() - 1
        );
        assertThat(testLeaseLiabilityCompilation.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityCompilation.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(1)).save(testLeaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void createLeaseLiabilityCompilationWithExistingId() throws Exception {
        // Create the LeaseLiabilityCompilation with an existing ID
        leaseLiabilityCompilation.setId(1L);
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        int databaseSizeBeforeCreate = leaseLiabilityCompilationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityCompilationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityCompilationRepository.findAll().size();
        // set the field null
        leaseLiabilityCompilation.setRequestId(null);

        // Create the LeaseLiabilityCompilation, which fails.
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        restLeaseLiabilityCompilationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityCompilationRepository.findAll().size();
        // set the field null
        leaseLiabilityCompilation.setTimeOfRequest(null);

        // Create the LeaseLiabilityCompilation, which fails.
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        restLeaseLiabilityCompilationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilations() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityCompilation.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityCompilation() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get the leaseLiabilityCompilation
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityCompilation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityCompilation.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityCompilationsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        Long id = leaseLiabilityCompilation.getId();

        defaultLeaseLiabilityCompilationShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityCompilationShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityCompilationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityCompilationShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityCompilationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityCompilationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where requestId equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityCompilationList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where requestId not equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityCompilationList where requestId not equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the leaseLiabilityCompilationList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityCompilationShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where requestId is not null
        defaultLeaseLiabilityCompilationShouldBeFound("requestId.specified=true");

        // Get all the leaseLiabilityCompilationList where requestId is null
        defaultLeaseLiabilityCompilationShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is not null
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.specified=true");

        // Get all the leaseLiabilityCompilationList where timeOfRequest is null
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityCompilationList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityCompilationShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityCompilationsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);
        ApplicationUser requestedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            requestedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(requestedBy);
            em.flush();
        } else {
            requestedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(requestedBy);
        em.flush();
        leaseLiabilityCompilation.setRequestedBy(requestedBy);
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);
        Long requestedById = requestedBy.getId();

        // Get all the leaseLiabilityCompilationList where requestedBy equals to requestedById
        defaultLeaseLiabilityCompilationShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the leaseLiabilityCompilationList where requestedBy equals to (requestedById + 1)
        defaultLeaseLiabilityCompilationShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityCompilationShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityCompilation.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))));

        // Check, that the count call also returns 1
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityCompilationShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityCompilation() throws Exception {
        // Get the leaseLiabilityCompilation
        restLeaseLiabilityCompilationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityCompilation() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();

        // Update the leaseLiabilityCompilation
        LeaseLiabilityCompilation updatedLeaseLiabilityCompilation = leaseLiabilityCompilationRepository
            .findById(leaseLiabilityCompilation.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityCompilation are not directly saved in db
        em.detach(updatedLeaseLiabilityCompilation);
        updatedLeaseLiabilityCompilation.requestId(UPDATED_REQUEST_ID).timeOfRequest(UPDATED_TIME_OF_REQUEST);
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(updatedLeaseLiabilityCompilation);

        restLeaseLiabilityCompilationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityCompilationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityCompilation testLeaseLiabilityCompilation = leaseLiabilityCompilationList.get(
            leaseLiabilityCompilationList.size() - 1
        );
        assertThat(testLeaseLiabilityCompilation.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityCompilation.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository).save(testLeaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityCompilationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityCompilationWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();

        // Update the leaseLiabilityCompilation using partial update
        LeaseLiabilityCompilation partialUpdatedLeaseLiabilityCompilation = new LeaseLiabilityCompilation();
        partialUpdatedLeaseLiabilityCompilation.setId(leaseLiabilityCompilation.getId());

        partialUpdatedLeaseLiabilityCompilation.requestId(UPDATED_REQUEST_ID);

        restLeaseLiabilityCompilationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityCompilation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityCompilation))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityCompilation testLeaseLiabilityCompilation = leaseLiabilityCompilationList.get(
            leaseLiabilityCompilationList.size() - 1
        );
        assertThat(testLeaseLiabilityCompilation.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityCompilation.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityCompilationWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();

        // Update the leaseLiabilityCompilation using partial update
        LeaseLiabilityCompilation partialUpdatedLeaseLiabilityCompilation = new LeaseLiabilityCompilation();
        partialUpdatedLeaseLiabilityCompilation.setId(leaseLiabilityCompilation.getId());

        partialUpdatedLeaseLiabilityCompilation.requestId(UPDATED_REQUEST_ID).timeOfRequest(UPDATED_TIME_OF_REQUEST);

        restLeaseLiabilityCompilationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityCompilation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityCompilation))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityCompilation testLeaseLiabilityCompilation = leaseLiabilityCompilationList.get(
            leaseLiabilityCompilationList.size() - 1
        );
        assertThat(testLeaseLiabilityCompilation.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityCompilation.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityCompilationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityCompilation() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityCompilationRepository.findAll().size();
        leaseLiabilityCompilation.setId(count.incrementAndGet());

        // Create the LeaseLiabilityCompilation
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityCompilationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityCompilationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityCompilation in the database
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(0)).save(leaseLiabilityCompilation);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityCompilation() throws Exception {
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);

        int databaseSizeBeforeDelete = leaseLiabilityCompilationRepository.findAll().size();

        // Delete the leaseLiabilityCompilation
        restLeaseLiabilityCompilationMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityCompilation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityCompilation> leaseLiabilityCompilationList = leaseLiabilityCompilationRepository.findAll();
        assertThat(leaseLiabilityCompilationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityCompilation in Elasticsearch
        verify(mockLeaseLiabilityCompilationSearchRepository, times(1)).deleteById(leaseLiabilityCompilation.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityCompilation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityCompilationRepository.saveAndFlush(leaseLiabilityCompilation);
        when(mockLeaseLiabilityCompilationSearchRepository.search("id:" + leaseLiabilityCompilation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityCompilation), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityCompilation
        restLeaseLiabilityCompilationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityCompilation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityCompilation.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))));
    }
}
