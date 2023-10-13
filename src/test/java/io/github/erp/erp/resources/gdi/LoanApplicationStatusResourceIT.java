package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.LoanApplicationStatus;
import io.github.erp.repository.LoanApplicationStatusRepository;
import io.github.erp.repository.search.LoanApplicationStatusSearchRepository;
import io.github.erp.service.dto.LoanApplicationStatusDTO;
import io.github.erp.service.mapper.LoanApplicationStatusMapper;
import io.github.erp.web.rest.LoanApplicationStatusResource;
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
 * Integration tests for the {@link LoanApplicationStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class LoanApplicationStatusResourceIT {

    private static final String DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_APPLICATION_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_APPLICATION_STATUS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_STATUS_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/loan-application-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/loan-application-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanApplicationStatusRepository loanApplicationStatusRepository;

    @Autowired
    private LoanApplicationStatusMapper loanApplicationStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanApplicationStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanApplicationStatusSearchRepository mockLoanApplicationStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanApplicationStatusMockMvc;

    private LoanApplicationStatus loanApplicationStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanApplicationStatus createEntity(EntityManager em) {
        LoanApplicationStatus loanApplicationStatus = new LoanApplicationStatus()
            .loanApplicationStatusTypeCode(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE)
            .loanApplicationStatusType(DEFAULT_LOAN_APPLICATION_STATUS_TYPE)
            .loanApplicationStatusDetails(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS);
        return loanApplicationStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanApplicationStatus createUpdatedEntity(EntityManager em) {
        LoanApplicationStatus loanApplicationStatus = new LoanApplicationStatus()
            .loanApplicationStatusTypeCode(UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE)
            .loanApplicationStatusType(UPDATED_LOAN_APPLICATION_STATUS_TYPE)
            .loanApplicationStatusDetails(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);
        return loanApplicationStatus;
    }

    @BeforeEach
    public void initTest() {
        loanApplicationStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeCreate = loanApplicationStatusRepository.findAll().size();
        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);
        restLoanApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeCreate + 1);
        LoanApplicationStatus testLoanApplicationStatus = loanApplicationStatusList.get(loanApplicationStatusList.size() - 1);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusTypeCode()).isEqualTo(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusType()).isEqualTo(DEFAULT_LOAN_APPLICATION_STATUS_TYPE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusDetails()).isEqualTo(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(1)).save(testLoanApplicationStatus);
    }

    @Test
    @Transactional
    void createLoanApplicationStatusWithExistingId() throws Exception {
        // Create the LoanApplicationStatus with an existing ID
        loanApplicationStatus.setId(1L);
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        int databaseSizeBeforeCreate = loanApplicationStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void checkLoanApplicationStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanApplicationStatusRepository.findAll().size();
        // set the field null
        loanApplicationStatus.setLoanApplicationStatusTypeCode(null);

        // Create the LoanApplicationStatus, which fails.
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        restLoanApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanApplicationStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanApplicationStatusRepository.findAll().size();
        // set the field null
        loanApplicationStatus.setLoanApplicationStatusType(null);

        // Create the LoanApplicationStatus, which fails.
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        restLoanApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatuses() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationStatusTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusType").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanApplicationStatus() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get the loanApplicationStatus
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, loanApplicationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanApplicationStatus.getId().intValue()))
            .andExpect(jsonPath("$.loanApplicationStatusTypeCode").value(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.loanApplicationStatusType").value(DEFAULT_LOAN_APPLICATION_STATUS_TYPE))
            .andExpect(jsonPath("$.loanApplicationStatusDetails").value(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanApplicationStatusesByIdFiltering() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        Long id = loanApplicationStatus.getId();

        defaultLoanApplicationStatusShouldBeFound("id.equals=" + id);
        defaultLoanApplicationStatusShouldNotBeFound("id.notEquals=" + id);

        defaultLoanApplicationStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanApplicationStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanApplicationStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanApplicationStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode equals to DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusTypeCode.equals=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusTypeCode.equals=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode not equals to DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldNotBeFound(
            "loanApplicationStatusTypeCode.notEquals=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode not equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusTypeCode.notEquals=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode in DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE or UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldBeFound(
            "loanApplicationStatusTypeCode.in=" +
            DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE +
            "," +
            UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusTypeCode.in=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode is not null
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusTypeCode.specified=true");

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode is null
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode contains DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusTypeCode.contains=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode contains UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusTypeCode.contains=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode does not contain DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldNotBeFound(
            "loanApplicationStatusTypeCode.doesNotContain=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the loanApplicationStatusList where loanApplicationStatusTypeCode does not contain UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        defaultLoanApplicationStatusShouldBeFound(
            "loanApplicationStatusTypeCode.doesNotContain=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType equals to DEFAULT_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusType.equals=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE);

        // Get all the loanApplicationStatusList where loanApplicationStatusType equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.equals=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType not equals to DEFAULT_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.notEquals=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE);

        // Get all the loanApplicationStatusList where loanApplicationStatusType not equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusType.notEquals=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType in DEFAULT_LOAN_APPLICATION_STATUS_TYPE or UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldBeFound(
            "loanApplicationStatusType.in=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE + "," + UPDATED_LOAN_APPLICATION_STATUS_TYPE
        );

        // Get all the loanApplicationStatusList where loanApplicationStatusType equals to UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.in=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType is not null
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusType.specified=true");

        // Get all the loanApplicationStatusList where loanApplicationStatusType is null
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType contains DEFAULT_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusType.contains=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE);

        // Get all the loanApplicationStatusList where loanApplicationStatusType contains UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.contains=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationStatusesByLoanApplicationStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        // Get all the loanApplicationStatusList where loanApplicationStatusType does not contain DEFAULT_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldNotBeFound("loanApplicationStatusType.doesNotContain=" + DEFAULT_LOAN_APPLICATION_STATUS_TYPE);

        // Get all the loanApplicationStatusList where loanApplicationStatusType does not contain UPDATED_LOAN_APPLICATION_STATUS_TYPE
        defaultLoanApplicationStatusShouldBeFound("loanApplicationStatusType.doesNotContain=" + UPDATED_LOAN_APPLICATION_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanApplicationStatusShouldBeFound(String filter) throws Exception {
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationStatusTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusType").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanApplicationStatusShouldNotBeFound(String filter) throws Exception {
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanApplicationStatus() throws Exception {
        // Get the loanApplicationStatus
        restLoanApplicationStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanApplicationStatus() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();

        // Update the loanApplicationStatus
        LoanApplicationStatus updatedLoanApplicationStatus = loanApplicationStatusRepository.findById(loanApplicationStatus.getId()).get();
        // Disconnect from session so that the updates on updatedLoanApplicationStatus are not directly saved in db
        em.detach(updatedLoanApplicationStatus);
        updatedLoanApplicationStatus
            .loanApplicationStatusTypeCode(UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE)
            .loanApplicationStatusType(UPDATED_LOAN_APPLICATION_STATUS_TYPE)
            .loanApplicationStatusDetails(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(updatedLoanApplicationStatus);

        restLoanApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanApplicationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationStatus testLoanApplicationStatus = loanApplicationStatusList.get(loanApplicationStatusList.size() - 1);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusTypeCode()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusType()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_TYPE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusDetails()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository).save(testLoanApplicationStatus);
    }

    @Test
    @Transactional
    void putNonExistingLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanApplicationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void partialUpdateLoanApplicationStatusWithPatch() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();

        // Update the loanApplicationStatus using partial update
        LoanApplicationStatus partialUpdatedLoanApplicationStatus = new LoanApplicationStatus();
        partialUpdatedLoanApplicationStatus.setId(loanApplicationStatus.getId());

        partialUpdatedLoanApplicationStatus
            .loanApplicationStatusType(UPDATED_LOAN_APPLICATION_STATUS_TYPE)
            .loanApplicationStatusDetails(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);

        restLoanApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanApplicationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanApplicationStatus))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationStatus testLoanApplicationStatus = loanApplicationStatusList.get(loanApplicationStatusList.size() - 1);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusTypeCode()).isEqualTo(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusType()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_TYPE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusDetails()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanApplicationStatusWithPatch() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();

        // Update the loanApplicationStatus using partial update
        LoanApplicationStatus partialUpdatedLoanApplicationStatus = new LoanApplicationStatus();
        partialUpdatedLoanApplicationStatus.setId(loanApplicationStatus.getId());

        partialUpdatedLoanApplicationStatus
            .loanApplicationStatusTypeCode(UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE)
            .loanApplicationStatusType(UPDATED_LOAN_APPLICATION_STATUS_TYPE)
            .loanApplicationStatusDetails(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);

        restLoanApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanApplicationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanApplicationStatus))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationStatus testLoanApplicationStatus = loanApplicationStatusList.get(loanApplicationStatusList.size() - 1);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusTypeCode()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusType()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_TYPE);
        assertThat(testLoanApplicationStatus.getLoanApplicationStatusDetails()).isEqualTo(UPDATED_LOAN_APPLICATION_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanApplicationStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationStatusRepository.findAll().size();
        loanApplicationStatus.setId(count.incrementAndGet());

        // Create the LoanApplicationStatus
        LoanApplicationStatusDTO loanApplicationStatusDTO = loanApplicationStatusMapper.toDto(loanApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanApplicationStatus in the database
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(0)).save(loanApplicationStatus);
    }

    @Test
    @Transactional
    void deleteLoanApplicationStatus() throws Exception {
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);

        int databaseSizeBeforeDelete = loanApplicationStatusRepository.findAll().size();

        // Delete the loanApplicationStatus
        restLoanApplicationStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanApplicationStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanApplicationStatus> loanApplicationStatusList = loanApplicationStatusRepository.findAll();
        assertThat(loanApplicationStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanApplicationStatus in Elasticsearch
        verify(mockLoanApplicationStatusSearchRepository, times(1)).deleteById(loanApplicationStatus.getId());
    }

    @Test
    @Transactional
    void searchLoanApplicationStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanApplicationStatusRepository.saveAndFlush(loanApplicationStatus);
        when(mockLoanApplicationStatusSearchRepository.search("id:" + loanApplicationStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanApplicationStatus), PageRequest.of(0, 1), 1));

        // Search the loanApplicationStatus
        restLoanApplicationStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanApplicationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationStatusTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusType").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationStatusDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_STATUS_DETAILS.toString())));
    }
}
