package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.LoanRepaymentFrequency;
import io.github.erp.repository.LoanRepaymentFrequencyRepository;
import io.github.erp.repository.search.LoanRepaymentFrequencySearchRepository;
import io.github.erp.service.dto.LoanRepaymentFrequencyDTO;
import io.github.erp.service.mapper.LoanRepaymentFrequencyMapper;
import io.github.erp.web.rest.LoanRepaymentFrequencyResource;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LoanRepaymentFrequencyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class LoanRepaymentFrequencyResourceIT {

    private static final String DEFAULT_FREQUENCY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/loan-repayment-frequencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/loan-repayment-frequencies";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository;

    @Autowired
    private LoanRepaymentFrequencyMapper loanRepaymentFrequencyMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanRepaymentFrequencySearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanRepaymentFrequencySearchRepository mockLoanRepaymentFrequencySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanRepaymentFrequencyMockMvc;

    private LoanRepaymentFrequency loanRepaymentFrequency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRepaymentFrequency createEntity(EntityManager em) {
        LoanRepaymentFrequency loanRepaymentFrequency = new LoanRepaymentFrequency()
            .frequencyTypeCode(DEFAULT_FREQUENCY_TYPE_CODE)
            .frequencyType(DEFAULT_FREQUENCY_TYPE)
            .frequencyTypeDetails(DEFAULT_FREQUENCY_TYPE_DETAILS);
        return loanRepaymentFrequency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRepaymentFrequency createUpdatedEntity(EntityManager em) {
        LoanRepaymentFrequency loanRepaymentFrequency = new LoanRepaymentFrequency()
            .frequencyTypeCode(UPDATED_FREQUENCY_TYPE_CODE)
            .frequencyType(UPDATED_FREQUENCY_TYPE)
            .frequencyTypeDetails(UPDATED_FREQUENCY_TYPE_DETAILS);
        return loanRepaymentFrequency;
    }

    @BeforeEach
    public void initTest() {
        loanRepaymentFrequency = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeCreate = loanRepaymentFrequencyRepository.findAll().size();
        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);
        restLoanRepaymentFrequencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeCreate + 1);
        LoanRepaymentFrequency testLoanRepaymentFrequency = loanRepaymentFrequencyList.get(loanRepaymentFrequencyList.size() - 1);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeCode()).isEqualTo(DEFAULT_FREQUENCY_TYPE_CODE);
        assertThat(testLoanRepaymentFrequency.getFrequencyType()).isEqualTo(DEFAULT_FREQUENCY_TYPE);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeDetails()).isEqualTo(DEFAULT_FREQUENCY_TYPE_DETAILS);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(1)).save(testLoanRepaymentFrequency);
    }

    @Test
    @Transactional
    void createLoanRepaymentFrequencyWithExistingId() throws Exception {
        // Create the LoanRepaymentFrequency with an existing ID
        loanRepaymentFrequency.setId(1L);
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        int databaseSizeBeforeCreate = loanRepaymentFrequencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanRepaymentFrequencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void checkFrequencyTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepaymentFrequencyRepository.findAll().size();
        // set the field null
        loanRepaymentFrequency.setFrequencyTypeCode(null);

        // Create the LoanRepaymentFrequency, which fails.
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        restLoanRepaymentFrequencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepaymentFrequencyRepository.findAll().size();
        // set the field null
        loanRepaymentFrequency.setFrequencyType(null);

        // Create the LoanRepaymentFrequency, which fails.
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        restLoanRepaymentFrequencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequencies() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRepaymentFrequency.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequencyTypeCode").value(hasItem(DEFAULT_FREQUENCY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].frequencyType").value(hasItem(DEFAULT_FREQUENCY_TYPE)))
            .andExpect(jsonPath("$.[*].frequencyTypeDetails").value(hasItem(DEFAULT_FREQUENCY_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanRepaymentFrequency() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get the loanRepaymentFrequency
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL_ID, loanRepaymentFrequency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanRepaymentFrequency.getId().intValue()))
            .andExpect(jsonPath("$.frequencyTypeCode").value(DEFAULT_FREQUENCY_TYPE_CODE))
            .andExpect(jsonPath("$.frequencyType").value(DEFAULT_FREQUENCY_TYPE))
            .andExpect(jsonPath("$.frequencyTypeDetails").value(DEFAULT_FREQUENCY_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanRepaymentFrequenciesByIdFiltering() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        Long id = loanRepaymentFrequency.getId();

        defaultLoanRepaymentFrequencyShouldBeFound("id.equals=" + id);
        defaultLoanRepaymentFrequencyShouldNotBeFound("id.notEquals=" + id);

        defaultLoanRepaymentFrequencyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanRepaymentFrequencyShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanRepaymentFrequencyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanRepaymentFrequencyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode equals to DEFAULT_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyTypeCode.equals=" + DEFAULT_FREQUENCY_TYPE_CODE);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode equals to UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.equals=" + UPDATED_FREQUENCY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode not equals to DEFAULT_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.notEquals=" + DEFAULT_FREQUENCY_TYPE_CODE);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode not equals to UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyTypeCode.notEquals=" + UPDATED_FREQUENCY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode in DEFAULT_FREQUENCY_TYPE_CODE or UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldBeFound(
            "frequencyTypeCode.in=" + DEFAULT_FREQUENCY_TYPE_CODE + "," + UPDATED_FREQUENCY_TYPE_CODE
        );

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode equals to UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.in=" + UPDATED_FREQUENCY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode is not null
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyTypeCode.specified=true");

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode is null
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode contains DEFAULT_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyTypeCode.contains=" + DEFAULT_FREQUENCY_TYPE_CODE);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode contains UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.contains=" + UPDATED_FREQUENCY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode does not contain DEFAULT_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyTypeCode.doesNotContain=" + DEFAULT_FREQUENCY_TYPE_CODE);

        // Get all the loanRepaymentFrequencyList where frequencyTypeCode does not contain UPDATED_FREQUENCY_TYPE_CODE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyTypeCode.doesNotContain=" + UPDATED_FREQUENCY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType equals to DEFAULT_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.equals=" + DEFAULT_FREQUENCY_TYPE);

        // Get all the loanRepaymentFrequencyList where frequencyType equals to UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.equals=" + UPDATED_FREQUENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType not equals to DEFAULT_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.notEquals=" + DEFAULT_FREQUENCY_TYPE);

        // Get all the loanRepaymentFrequencyList where frequencyType not equals to UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.notEquals=" + UPDATED_FREQUENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType in DEFAULT_FREQUENCY_TYPE or UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.in=" + DEFAULT_FREQUENCY_TYPE + "," + UPDATED_FREQUENCY_TYPE);

        // Get all the loanRepaymentFrequencyList where frequencyType equals to UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.in=" + UPDATED_FREQUENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType is not null
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.specified=true");

        // Get all the loanRepaymentFrequencyList where frequencyType is null
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeContainsSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType contains DEFAULT_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.contains=" + DEFAULT_FREQUENCY_TYPE);

        // Get all the loanRepaymentFrequencyList where frequencyType contains UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.contains=" + UPDATED_FREQUENCY_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRepaymentFrequenciesByFrequencyTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        // Get all the loanRepaymentFrequencyList where frequencyType does not contain DEFAULT_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldNotBeFound("frequencyType.doesNotContain=" + DEFAULT_FREQUENCY_TYPE);

        // Get all the loanRepaymentFrequencyList where frequencyType does not contain UPDATED_FREQUENCY_TYPE
        defaultLoanRepaymentFrequencyShouldBeFound("frequencyType.doesNotContain=" + UPDATED_FREQUENCY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanRepaymentFrequencyShouldBeFound(String filter) throws Exception {
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRepaymentFrequency.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequencyTypeCode").value(hasItem(DEFAULT_FREQUENCY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].frequencyType").value(hasItem(DEFAULT_FREQUENCY_TYPE)))
            .andExpect(jsonPath("$.[*].frequencyTypeDetails").value(hasItem(DEFAULT_FREQUENCY_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanRepaymentFrequencyShouldNotBeFound(String filter) throws Exception {
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanRepaymentFrequency() throws Exception {
        // Get the loanRepaymentFrequency
        restLoanRepaymentFrequencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanRepaymentFrequency() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();

        // Update the loanRepaymentFrequency
        LoanRepaymentFrequency updatedLoanRepaymentFrequency = loanRepaymentFrequencyRepository
            .findById(loanRepaymentFrequency.getId())
            .get();
        // Disconnect from session so that the updates on updatedLoanRepaymentFrequency are not directly saved in db
        em.detach(updatedLoanRepaymentFrequency);
        updatedLoanRepaymentFrequency
            .frequencyTypeCode(UPDATED_FREQUENCY_TYPE_CODE)
            .frequencyType(UPDATED_FREQUENCY_TYPE)
            .frequencyTypeDetails(UPDATED_FREQUENCY_TYPE_DETAILS);
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(updatedLoanRepaymentFrequency);

        restLoanRepaymentFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRepaymentFrequencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);
        LoanRepaymentFrequency testLoanRepaymentFrequency = loanRepaymentFrequencyList.get(loanRepaymentFrequencyList.size() - 1);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeCode()).isEqualTo(UPDATED_FREQUENCY_TYPE_CODE);
        assertThat(testLoanRepaymentFrequency.getFrequencyType()).isEqualTo(UPDATED_FREQUENCY_TYPE);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeDetails()).isEqualTo(UPDATED_FREQUENCY_TYPE_DETAILS);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository).save(testLoanRepaymentFrequency);
    }

    @Test
    @Transactional
    void putNonExistingLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRepaymentFrequencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void partialUpdateLoanRepaymentFrequencyWithPatch() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();

        // Update the loanRepaymentFrequency using partial update
        LoanRepaymentFrequency partialUpdatedLoanRepaymentFrequency = new LoanRepaymentFrequency();
        partialUpdatedLoanRepaymentFrequency.setId(loanRepaymentFrequency.getId());

        partialUpdatedLoanRepaymentFrequency
            .frequencyTypeCode(UPDATED_FREQUENCY_TYPE_CODE)
            .frequencyType(UPDATED_FREQUENCY_TYPE)
            .frequencyTypeDetails(UPDATED_FREQUENCY_TYPE_DETAILS);

        restLoanRepaymentFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRepaymentFrequency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRepaymentFrequency))
            )
            .andExpect(status().isOk());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);
        LoanRepaymentFrequency testLoanRepaymentFrequency = loanRepaymentFrequencyList.get(loanRepaymentFrequencyList.size() - 1);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeCode()).isEqualTo(UPDATED_FREQUENCY_TYPE_CODE);
        assertThat(testLoanRepaymentFrequency.getFrequencyType()).isEqualTo(UPDATED_FREQUENCY_TYPE);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeDetails()).isEqualTo(UPDATED_FREQUENCY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanRepaymentFrequencyWithPatch() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();

        // Update the loanRepaymentFrequency using partial update
        LoanRepaymentFrequency partialUpdatedLoanRepaymentFrequency = new LoanRepaymentFrequency();
        partialUpdatedLoanRepaymentFrequency.setId(loanRepaymentFrequency.getId());

        partialUpdatedLoanRepaymentFrequency
            .frequencyTypeCode(UPDATED_FREQUENCY_TYPE_CODE)
            .frequencyType(UPDATED_FREQUENCY_TYPE)
            .frequencyTypeDetails(UPDATED_FREQUENCY_TYPE_DETAILS);

        restLoanRepaymentFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRepaymentFrequency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRepaymentFrequency))
            )
            .andExpect(status().isOk());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);
        LoanRepaymentFrequency testLoanRepaymentFrequency = loanRepaymentFrequencyList.get(loanRepaymentFrequencyList.size() - 1);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeCode()).isEqualTo(UPDATED_FREQUENCY_TYPE_CODE);
        assertThat(testLoanRepaymentFrequency.getFrequencyType()).isEqualTo(UPDATED_FREQUENCY_TYPE);
        assertThat(testLoanRepaymentFrequency.getFrequencyTypeDetails()).isEqualTo(UPDATED_FREQUENCY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanRepaymentFrequencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanRepaymentFrequency() throws Exception {
        int databaseSizeBeforeUpdate = loanRepaymentFrequencyRepository.findAll().size();
        loanRepaymentFrequency.setId(count.incrementAndGet());

        // Create the LoanRepaymentFrequency
        LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO = loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRepaymentFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRepaymentFrequencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRepaymentFrequency in the database
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(0)).save(loanRepaymentFrequency);
    }

    @Test
    @Transactional
    void deleteLoanRepaymentFrequency() throws Exception {
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);

        int databaseSizeBeforeDelete = loanRepaymentFrequencyRepository.findAll().size();

        // Delete the loanRepaymentFrequency
        restLoanRepaymentFrequencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanRepaymentFrequency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanRepaymentFrequency> loanRepaymentFrequencyList = loanRepaymentFrequencyRepository.findAll();
        assertThat(loanRepaymentFrequencyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanRepaymentFrequency in Elasticsearch
        verify(mockLoanRepaymentFrequencySearchRepository, times(1)).deleteById(loanRepaymentFrequency.getId());
    }

    @Test
    @Transactional
    void searchLoanRepaymentFrequency() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanRepaymentFrequencyRepository.saveAndFlush(loanRepaymentFrequency);
        when(mockLoanRepaymentFrequencySearchRepository.search("id:" + loanRepaymentFrequency.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanRepaymentFrequency), PageRequest.of(0, 1), 1));

        // Search the loanRepaymentFrequency
        restLoanRepaymentFrequencyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanRepaymentFrequency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRepaymentFrequency.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequencyTypeCode").value(hasItem(DEFAULT_FREQUENCY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].frequencyType").value(hasItem(DEFAULT_FREQUENCY_TYPE)))
            .andExpect(jsonPath("$.[*].frequencyTypeDetails").value(hasItem(DEFAULT_FREQUENCY_TYPE_DETAILS.toString())));
    }
}
