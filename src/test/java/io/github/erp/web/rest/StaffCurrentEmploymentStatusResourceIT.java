package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.StaffCurrentEmploymentStatus;
import io.github.erp.repository.StaffCurrentEmploymentStatusRepository;
import io.github.erp.repository.search.StaffCurrentEmploymentStatusSearchRepository;
import io.github.erp.service.criteria.StaffCurrentEmploymentStatusCriteria;
import io.github.erp.service.dto.StaffCurrentEmploymentStatusDTO;
import io.github.erp.service.mapper.StaffCurrentEmploymentStatusMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link StaffCurrentEmploymentStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StaffCurrentEmploymentStatusResourceIT {

    private static final String DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/staff-current-employment-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/staff-current-employment-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository;

    @Autowired
    private StaffCurrentEmploymentStatusMapper staffCurrentEmploymentStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.StaffCurrentEmploymentStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private StaffCurrentEmploymentStatusSearchRepository mockStaffCurrentEmploymentStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffCurrentEmploymentStatusMockMvc;

    private StaffCurrentEmploymentStatus staffCurrentEmploymentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffCurrentEmploymentStatus createEntity(EntityManager em) {
        StaffCurrentEmploymentStatus staffCurrentEmploymentStatus = new StaffCurrentEmploymentStatus()
            .staffCurrentEmploymentStatusTypeCode(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE)
            .staffCurrentEmploymentStatusType(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)
            .staffCurrentEmploymentStatusTypeDetails(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);
        return staffCurrentEmploymentStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffCurrentEmploymentStatus createUpdatedEntity(EntityManager em) {
        StaffCurrentEmploymentStatus staffCurrentEmploymentStatus = new StaffCurrentEmploymentStatus()
            .staffCurrentEmploymentStatusTypeCode(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE)
            .staffCurrentEmploymentStatusType(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)
            .staffCurrentEmploymentStatusTypeDetails(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);
        return staffCurrentEmploymentStatus;
    }

    @BeforeEach
    public void initTest() {
        staffCurrentEmploymentStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeCreate = staffCurrentEmploymentStatusRepository.findAll().size();
        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeCreate + 1);
        StaffCurrentEmploymentStatus testStaffCurrentEmploymentStatus = staffCurrentEmploymentStatusList.get(
            staffCurrentEmploymentStatusList.size() - 1
        );
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeCode())
            .isEqualTo(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusType())
            .isEqualTo(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeDetails())
            .isEqualTo(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(1)).save(testStaffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void createStaffCurrentEmploymentStatusWithExistingId() throws Exception {
        // Create the StaffCurrentEmploymentStatus with an existing ID
        staffCurrentEmploymentStatus.setId(1L);
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        int databaseSizeBeforeCreate = staffCurrentEmploymentStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void checkStaffCurrentEmploymentStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffCurrentEmploymentStatusRepository.findAll().size();
        // set the field null
        staffCurrentEmploymentStatus.setStaffCurrentEmploymentStatusTypeCode(null);

        // Create the StaffCurrentEmploymentStatus, which fails.
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStaffCurrentEmploymentStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffCurrentEmploymentStatusRepository.findAll().size();
        // set the field null
        staffCurrentEmploymentStatus.setStaffCurrentEmploymentStatusType(null);

        // Create the StaffCurrentEmploymentStatus, which fails.
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatuses() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffCurrentEmploymentStatus.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeCode").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].staffCurrentEmploymentStatusType").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeDetails")
                    .value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getStaffCurrentEmploymentStatus() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get the staffCurrentEmploymentStatus
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, staffCurrentEmploymentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffCurrentEmploymentStatus.getId().intValue()))
            .andExpect(jsonPath("$.staffCurrentEmploymentStatusTypeCode").value(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.staffCurrentEmploymentStatusType").value(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE))
            .andExpect(
                jsonPath("$.staffCurrentEmploymentStatusTypeDetails").value(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS.toString())
            );
    }

    @Test
    @Transactional
    void getStaffCurrentEmploymentStatusesByIdFiltering() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        Long id = staffCurrentEmploymentStatus.getId();

        defaultStaffCurrentEmploymentStatusShouldBeFound("id.equals=" + id);
        defaultStaffCurrentEmploymentStatusShouldNotBeFound("id.notEquals=" + id);

        defaultStaffCurrentEmploymentStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStaffCurrentEmploymentStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultStaffCurrentEmploymentStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStaffCurrentEmploymentStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode equals to DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusTypeCode.equals=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusTypeCode.equals=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode not equals to DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusTypeCode.notEquals=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode not equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusTypeCode.notEquals=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode in DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE or UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusTypeCode.in=" +
            DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE +
            "," +
            UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusTypeCode.in=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode is not null
        defaultStaffCurrentEmploymentStatusShouldBeFound("staffCurrentEmploymentStatusTypeCode.specified=true");

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode is null
        defaultStaffCurrentEmploymentStatusShouldNotBeFound("staffCurrentEmploymentStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode contains DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusTypeCode.contains=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode contains UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusTypeCode.contains=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode does not contain DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusTypeCode.doesNotContain=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusTypeCode does not contain UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusTypeCode.doesNotContain=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType equals to DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusType.equals=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusType.equals=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType not equals to DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusType.notEquals=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType not equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusType.notEquals=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType in DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE or UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusType.in=" +
            DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE +
            "," +
            UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType equals to UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusType.in=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType is not null
        defaultStaffCurrentEmploymentStatusShouldBeFound("staffCurrentEmploymentStatusType.specified=true");

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType is null
        defaultStaffCurrentEmploymentStatusShouldNotBeFound("staffCurrentEmploymentStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType contains DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusType.contains=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType contains UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusType.contains=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllStaffCurrentEmploymentStatusesByStaffCurrentEmploymentStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType does not contain DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldNotBeFound(
            "staffCurrentEmploymentStatusType.doesNotContain=" + DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );

        // Get all the staffCurrentEmploymentStatusList where staffCurrentEmploymentStatusType does not contain UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        defaultStaffCurrentEmploymentStatusShouldBeFound(
            "staffCurrentEmploymentStatusType.doesNotContain=" + UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaffCurrentEmploymentStatusShouldBeFound(String filter) throws Exception {
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffCurrentEmploymentStatus.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeCode").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].staffCurrentEmploymentStatusType").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeDetails")
                    .value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaffCurrentEmploymentStatusShouldNotBeFound(String filter) throws Exception {
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStaffCurrentEmploymentStatus() throws Exception {
        // Get the staffCurrentEmploymentStatus
        restStaffCurrentEmploymentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaffCurrentEmploymentStatus() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();

        // Update the staffCurrentEmploymentStatus
        StaffCurrentEmploymentStatus updatedStaffCurrentEmploymentStatus = staffCurrentEmploymentStatusRepository
            .findById(staffCurrentEmploymentStatus.getId())
            .get();
        // Disconnect from session so that the updates on updatedStaffCurrentEmploymentStatus are not directly saved in db
        em.detach(updatedStaffCurrentEmploymentStatus);
        updatedStaffCurrentEmploymentStatus
            .staffCurrentEmploymentStatusTypeCode(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE)
            .staffCurrentEmploymentStatusType(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)
            .staffCurrentEmploymentStatusTypeDetails(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            updatedStaffCurrentEmploymentStatus
        );

        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffCurrentEmploymentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);
        StaffCurrentEmploymentStatus testStaffCurrentEmploymentStatus = staffCurrentEmploymentStatusList.get(
            staffCurrentEmploymentStatusList.size() - 1
        );
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeCode())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusType())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeDetails())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository).save(testStaffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void putNonExistingStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffCurrentEmploymentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void partialUpdateStaffCurrentEmploymentStatusWithPatch() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();

        // Update the staffCurrentEmploymentStatus using partial update
        StaffCurrentEmploymentStatus partialUpdatedStaffCurrentEmploymentStatus = new StaffCurrentEmploymentStatus();
        partialUpdatedStaffCurrentEmploymentStatus.setId(staffCurrentEmploymentStatus.getId());

        partialUpdatedStaffCurrentEmploymentStatus.staffCurrentEmploymentStatusType(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE);

        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffCurrentEmploymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffCurrentEmploymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);
        StaffCurrentEmploymentStatus testStaffCurrentEmploymentStatus = staffCurrentEmploymentStatusList.get(
            staffCurrentEmploymentStatusList.size() - 1
        );
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeCode())
            .isEqualTo(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusType())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeDetails())
            .isEqualTo(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateStaffCurrentEmploymentStatusWithPatch() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();

        // Update the staffCurrentEmploymentStatus using partial update
        StaffCurrentEmploymentStatus partialUpdatedStaffCurrentEmploymentStatus = new StaffCurrentEmploymentStatus();
        partialUpdatedStaffCurrentEmploymentStatus.setId(staffCurrentEmploymentStatus.getId());

        partialUpdatedStaffCurrentEmploymentStatus
            .staffCurrentEmploymentStatusTypeCode(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE)
            .staffCurrentEmploymentStatusType(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)
            .staffCurrentEmploymentStatusTypeDetails(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);

        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffCurrentEmploymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffCurrentEmploymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);
        StaffCurrentEmploymentStatus testStaffCurrentEmploymentStatus = staffCurrentEmploymentStatusList.get(
            staffCurrentEmploymentStatusList.size() - 1
        );
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeCode())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusType())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE);
        assertThat(testStaffCurrentEmploymentStatus.getStaffCurrentEmploymentStatusTypeDetails())
            .isEqualTo(UPDATED_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffCurrentEmploymentStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaffCurrentEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = staffCurrentEmploymentStatusRepository.findAll().size();
        staffCurrentEmploymentStatus.setId(count.incrementAndGet());

        // Create the StaffCurrentEmploymentStatus
        StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusMapper.toDto(
            staffCurrentEmploymentStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffCurrentEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffCurrentEmploymentStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffCurrentEmploymentStatus in the database
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(0)).save(staffCurrentEmploymentStatus);
    }

    @Test
    @Transactional
    void deleteStaffCurrentEmploymentStatus() throws Exception {
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);

        int databaseSizeBeforeDelete = staffCurrentEmploymentStatusRepository.findAll().size();

        // Delete the staffCurrentEmploymentStatus
        restStaffCurrentEmploymentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, staffCurrentEmploymentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaffCurrentEmploymentStatus> staffCurrentEmploymentStatusList = staffCurrentEmploymentStatusRepository.findAll();
        assertThat(staffCurrentEmploymentStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StaffCurrentEmploymentStatus in Elasticsearch
        verify(mockStaffCurrentEmploymentStatusSearchRepository, times(1)).deleteById(staffCurrentEmploymentStatus.getId());
    }

    @Test
    @Transactional
    void searchStaffCurrentEmploymentStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        staffCurrentEmploymentStatusRepository.saveAndFlush(staffCurrentEmploymentStatus);
        when(mockStaffCurrentEmploymentStatusSearchRepository.search("id:" + staffCurrentEmploymentStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(staffCurrentEmploymentStatus), PageRequest.of(0, 1), 1));

        // Search the staffCurrentEmploymentStatus
        restStaffCurrentEmploymentStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + staffCurrentEmploymentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffCurrentEmploymentStatus.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeCode").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].staffCurrentEmploymentStatusType").value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].staffCurrentEmploymentStatusTypeDetails")
                    .value(hasItem(DEFAULT_STAFF_CURRENT_EMPLOYMENT_STATUS_TYPE_DETAILS.toString()))
            );
    }
}
