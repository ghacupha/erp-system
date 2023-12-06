package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.LoanDeclineReason;
import io.github.erp.repository.LoanDeclineReasonRepository;
import io.github.erp.repository.search.LoanDeclineReasonSearchRepository;
import io.github.erp.service.dto.LoanDeclineReasonDTO;
import io.github.erp.service.mapper.LoanDeclineReasonMapper;
import io.github.erp.web.rest.LoanDeclineReasonResource;
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
 * Integration tests for the {@link LoanDeclineReasonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class LoanDeclineReasonResourceIT {

    private static final String DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_DECLINE_REASON_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_DECLINE_REASON_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_DECLINE_REASON_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_DECLINE_REASON_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_DECLINE_REASON_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/loan-decline-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/loan-decline-reasons";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanDeclineReasonRepository loanDeclineReasonRepository;

    @Autowired
    private LoanDeclineReasonMapper loanDeclineReasonMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanDeclineReasonSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanDeclineReasonSearchRepository mockLoanDeclineReasonSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanDeclineReasonMockMvc;

    private LoanDeclineReason loanDeclineReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanDeclineReason createEntity(EntityManager em) {
        LoanDeclineReason loanDeclineReason = new LoanDeclineReason()
            .loanDeclineReasonTypeCode(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE)
            .loanDeclineReasonType(DEFAULT_LOAN_DECLINE_REASON_TYPE)
            .loanDeclineReasonDetails(DEFAULT_LOAN_DECLINE_REASON_DETAILS);
        return loanDeclineReason;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanDeclineReason createUpdatedEntity(EntityManager em) {
        LoanDeclineReason loanDeclineReason = new LoanDeclineReason()
            .loanDeclineReasonTypeCode(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE)
            .loanDeclineReasonType(UPDATED_LOAN_DECLINE_REASON_TYPE)
            .loanDeclineReasonDetails(UPDATED_LOAN_DECLINE_REASON_DETAILS);
        return loanDeclineReason;
    }

    @BeforeEach
    public void initTest() {
        loanDeclineReason = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanDeclineReason() throws Exception {
        int databaseSizeBeforeCreate = loanDeclineReasonRepository.findAll().size();
        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);
        restLoanDeclineReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeCreate + 1);
        LoanDeclineReason testLoanDeclineReason = loanDeclineReasonList.get(loanDeclineReasonList.size() - 1);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonTypeCode()).isEqualTo(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonType()).isEqualTo(DEFAULT_LOAN_DECLINE_REASON_TYPE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonDetails()).isEqualTo(DEFAULT_LOAN_DECLINE_REASON_DETAILS);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(1)).save(testLoanDeclineReason);
    }

    @Test
    @Transactional
    void createLoanDeclineReasonWithExistingId() throws Exception {
        // Create the LoanDeclineReason with an existing ID
        loanDeclineReason.setId(1L);
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        int databaseSizeBeforeCreate = loanDeclineReasonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanDeclineReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void checkLoanDeclineReasonTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanDeclineReasonRepository.findAll().size();
        // set the field null
        loanDeclineReason.setLoanDeclineReasonTypeCode(null);

        // Create the LoanDeclineReason, which fails.
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        restLoanDeclineReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanDeclineReasonTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanDeclineReasonRepository.findAll().size();
        // set the field null
        loanDeclineReason.setLoanDeclineReasonType(null);

        // Create the LoanDeclineReason, which fails.
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        restLoanDeclineReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasons() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanDeclineReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanDeclineReasonTypeCode").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonType").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonDetails").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanDeclineReason() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get the loanDeclineReason
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, loanDeclineReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanDeclineReason.getId().intValue()))
            .andExpect(jsonPath("$.loanDeclineReasonTypeCode").value(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE))
            .andExpect(jsonPath("$.loanDeclineReasonType").value(DEFAULT_LOAN_DECLINE_REASON_TYPE))
            .andExpect(jsonPath("$.loanDeclineReasonDetails").value(DEFAULT_LOAN_DECLINE_REASON_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanDeclineReasonsByIdFiltering() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        Long id = loanDeclineReason.getId();

        defaultLoanDeclineReasonShouldBeFound("id.equals=" + id);
        defaultLoanDeclineReasonShouldNotBeFound("id.notEquals=" + id);

        defaultLoanDeclineReasonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanDeclineReasonShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanDeclineReasonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanDeclineReasonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode equals to DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonTypeCode.equals=" + DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode equals to UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.equals=" + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode not equals to DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.notEquals=" + DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode not equals to UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonTypeCode.notEquals=" + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode in DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE or UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldBeFound(
            "loanDeclineReasonTypeCode.in=" + DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE + "," + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        );

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode equals to UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.in=" + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode is not null
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonTypeCode.specified=true");

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode is null
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode contains DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonTypeCode.contains=" + DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode contains UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.contains=" + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode does not contain DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonTypeCode.doesNotContain=" + DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE);

        // Get all the loanDeclineReasonList where loanDeclineReasonTypeCode does not contain UPDATED_LOAN_DECLINE_REASON_TYPE_CODE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonTypeCode.doesNotContain=" + UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType equals to DEFAULT_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonType.equals=" + DEFAULT_LOAN_DECLINE_REASON_TYPE);

        // Get all the loanDeclineReasonList where loanDeclineReasonType equals to UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.equals=" + UPDATED_LOAN_DECLINE_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType not equals to DEFAULT_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.notEquals=" + DEFAULT_LOAN_DECLINE_REASON_TYPE);

        // Get all the loanDeclineReasonList where loanDeclineReasonType not equals to UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonType.notEquals=" + UPDATED_LOAN_DECLINE_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType in DEFAULT_LOAN_DECLINE_REASON_TYPE or UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldBeFound(
            "loanDeclineReasonType.in=" + DEFAULT_LOAN_DECLINE_REASON_TYPE + "," + UPDATED_LOAN_DECLINE_REASON_TYPE
        );

        // Get all the loanDeclineReasonList where loanDeclineReasonType equals to UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.in=" + UPDATED_LOAN_DECLINE_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType is not null
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonType.specified=true");

        // Get all the loanDeclineReasonList where loanDeclineReasonType is null
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeContainsSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType contains DEFAULT_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonType.contains=" + DEFAULT_LOAN_DECLINE_REASON_TYPE);

        // Get all the loanDeclineReasonList where loanDeclineReasonType contains UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.contains=" + UPDATED_LOAN_DECLINE_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanDeclineReasonsByLoanDeclineReasonTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        // Get all the loanDeclineReasonList where loanDeclineReasonType does not contain DEFAULT_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldNotBeFound("loanDeclineReasonType.doesNotContain=" + DEFAULT_LOAN_DECLINE_REASON_TYPE);

        // Get all the loanDeclineReasonList where loanDeclineReasonType does not contain UPDATED_LOAN_DECLINE_REASON_TYPE
        defaultLoanDeclineReasonShouldBeFound("loanDeclineReasonType.doesNotContain=" + UPDATED_LOAN_DECLINE_REASON_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanDeclineReasonShouldBeFound(String filter) throws Exception {
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanDeclineReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanDeclineReasonTypeCode").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonType").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonDetails").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanDeclineReasonShouldNotBeFound(String filter) throws Exception {
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanDeclineReason() throws Exception {
        // Get the loanDeclineReason
        restLoanDeclineReasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanDeclineReason() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();

        // Update the loanDeclineReason
        LoanDeclineReason updatedLoanDeclineReason = loanDeclineReasonRepository.findById(loanDeclineReason.getId()).get();
        // Disconnect from session so that the updates on updatedLoanDeclineReason are not directly saved in db
        em.detach(updatedLoanDeclineReason);
        updatedLoanDeclineReason
            .loanDeclineReasonTypeCode(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE)
            .loanDeclineReasonType(UPDATED_LOAN_DECLINE_REASON_TYPE)
            .loanDeclineReasonDetails(UPDATED_LOAN_DECLINE_REASON_DETAILS);
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(updatedLoanDeclineReason);

        restLoanDeclineReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanDeclineReasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);
        LoanDeclineReason testLoanDeclineReason = loanDeclineReasonList.get(loanDeclineReasonList.size() - 1);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonTypeCode()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonType()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_TYPE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonDetails()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_DETAILS);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository).save(testLoanDeclineReason);
    }

    @Test
    @Transactional
    void putNonExistingLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanDeclineReasonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void partialUpdateLoanDeclineReasonWithPatch() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();

        // Update the loanDeclineReason using partial update
        LoanDeclineReason partialUpdatedLoanDeclineReason = new LoanDeclineReason();
        partialUpdatedLoanDeclineReason.setId(loanDeclineReason.getId());

        partialUpdatedLoanDeclineReason
            .loanDeclineReasonTypeCode(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE)
            .loanDeclineReasonDetails(UPDATED_LOAN_DECLINE_REASON_DETAILS);

        restLoanDeclineReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanDeclineReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanDeclineReason))
            )
            .andExpect(status().isOk());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);
        LoanDeclineReason testLoanDeclineReason = loanDeclineReasonList.get(loanDeclineReasonList.size() - 1);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonTypeCode()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonType()).isEqualTo(DEFAULT_LOAN_DECLINE_REASON_TYPE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonDetails()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanDeclineReasonWithPatch() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();

        // Update the loanDeclineReason using partial update
        LoanDeclineReason partialUpdatedLoanDeclineReason = new LoanDeclineReason();
        partialUpdatedLoanDeclineReason.setId(loanDeclineReason.getId());

        partialUpdatedLoanDeclineReason
            .loanDeclineReasonTypeCode(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE)
            .loanDeclineReasonType(UPDATED_LOAN_DECLINE_REASON_TYPE)
            .loanDeclineReasonDetails(UPDATED_LOAN_DECLINE_REASON_DETAILS);

        restLoanDeclineReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanDeclineReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanDeclineReason))
            )
            .andExpect(status().isOk());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);
        LoanDeclineReason testLoanDeclineReason = loanDeclineReasonList.get(loanDeclineReasonList.size() - 1);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonTypeCode()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_TYPE_CODE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonType()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_TYPE);
        assertThat(testLoanDeclineReason.getLoanDeclineReasonDetails()).isEqualTo(UPDATED_LOAN_DECLINE_REASON_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanDeclineReasonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanDeclineReason() throws Exception {
        int databaseSizeBeforeUpdate = loanDeclineReasonRepository.findAll().size();
        loanDeclineReason.setId(count.incrementAndGet());

        // Create the LoanDeclineReason
        LoanDeclineReasonDTO loanDeclineReasonDTO = loanDeclineReasonMapper.toDto(loanDeclineReason);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanDeclineReasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanDeclineReasonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanDeclineReason in the database
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(0)).save(loanDeclineReason);
    }

    @Test
    @Transactional
    void deleteLoanDeclineReason() throws Exception {
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);

        int databaseSizeBeforeDelete = loanDeclineReasonRepository.findAll().size();

        // Delete the loanDeclineReason
        restLoanDeclineReasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanDeclineReason.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanDeclineReason> loanDeclineReasonList = loanDeclineReasonRepository.findAll();
        assertThat(loanDeclineReasonList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanDeclineReason in Elasticsearch
        verify(mockLoanDeclineReasonSearchRepository, times(1)).deleteById(loanDeclineReason.getId());
    }

    @Test
    @Transactional
    void searchLoanDeclineReason() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanDeclineReasonRepository.saveAndFlush(loanDeclineReason);
        when(mockLoanDeclineReasonSearchRepository.search("id:" + loanDeclineReason.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanDeclineReason), PageRequest.of(0, 1), 1));

        // Search the loanDeclineReason
        restLoanDeclineReasonMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanDeclineReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanDeclineReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanDeclineReasonTypeCode").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonType").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].loanDeclineReasonDetails").value(hasItem(DEFAULT_LOAN_DECLINE_REASON_DETAILS.toString())));
    }
}
