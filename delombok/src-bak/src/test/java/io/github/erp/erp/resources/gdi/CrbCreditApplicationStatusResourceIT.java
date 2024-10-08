package io.github.erp.erp.resources.gdi;

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
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CrbCreditApplicationStatus;
import io.github.erp.repository.CrbCreditApplicationStatusRepository;
import io.github.erp.repository.search.CrbCreditApplicationStatusSearchRepository;
import io.github.erp.service.dto.CrbCreditApplicationStatusDTO;
import io.github.erp.service.mapper.CrbCreditApplicationStatusMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CrbCreditApplicationStatusResource;
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

/**
 * Integration tests for the {@link CrbCreditApplicationStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CrbCreditApplicationStatusResourceIT {

    private static final String DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/crb-credit-application-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/crb-credit-application-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository;

    @Autowired
    private CrbCreditApplicationStatusMapper crbCreditApplicationStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbCreditApplicationStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbCreditApplicationStatusSearchRepository mockCrbCreditApplicationStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbCreditApplicationStatusMockMvc;

    private CrbCreditApplicationStatus crbCreditApplicationStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCreditApplicationStatus createEntity(EntityManager em) {
        CrbCreditApplicationStatus crbCreditApplicationStatus = new CrbCreditApplicationStatus()
            .crbCreditApplicationStatusTypeCode(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)
            .crbCreditApplicationStatusType(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE)
            .crbCreditApplicationStatusDetails(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS);
        return crbCreditApplicationStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCreditApplicationStatus createUpdatedEntity(EntityManager em) {
        CrbCreditApplicationStatus crbCreditApplicationStatus = new CrbCreditApplicationStatus()
            .crbCreditApplicationStatusTypeCode(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)
            .crbCreditApplicationStatusType(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE)
            .crbCreditApplicationStatusDetails(UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS);
        return crbCreditApplicationStatus;
    }

    @BeforeEach
    public void initTest() {
        crbCreditApplicationStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeCreate = crbCreditApplicationStatusRepository.findAll().size();
        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);
        restCrbCreditApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CrbCreditApplicationStatus testCrbCreditApplicationStatus = crbCreditApplicationStatusList.get(
            crbCreditApplicationStatusList.size() - 1
        );
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusTypeCode())
            .isEqualTo(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusType())
            .isEqualTo(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusDetails())
            .isEqualTo(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(1)).save(testCrbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void createCrbCreditApplicationStatusWithExistingId() throws Exception {
        // Create the CrbCreditApplicationStatus with an existing ID
        crbCreditApplicationStatus.setId(1L);
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        int databaseSizeBeforeCreate = crbCreditApplicationStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbCreditApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void checkCrbCreditApplicationStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCreditApplicationStatusRepository.findAll().size();
        // set the field null
        crbCreditApplicationStatus.setCrbCreditApplicationStatusTypeCode(null);

        // Create the CrbCreditApplicationStatus, which fails.
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        restCrbCreditApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCrbCreditApplicationStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCreditApplicationStatusRepository.findAll().size();
        // set the field null
        crbCreditApplicationStatus.setCrbCreditApplicationStatusType(null);

        // Create the CrbCreditApplicationStatus, which fails.
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        restCrbCreditApplicationStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatuses() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusTypeCode").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusType").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].crbCreditApplicationStatusDetails").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getCrbCreditApplicationStatus() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get the crbCreditApplicationStatus
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, crbCreditApplicationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbCreditApplicationStatus.getId().intValue()))
            .andExpect(jsonPath("$.crbCreditApplicationStatusTypeCode").value(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.crbCreditApplicationStatusType").value(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE))
            .andExpect(jsonPath("$.crbCreditApplicationStatusDetails").value(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbCreditApplicationStatusesByIdFiltering() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        Long id = crbCreditApplicationStatus.getId();

        defaultCrbCreditApplicationStatusShouldBeFound("id.equals=" + id);
        defaultCrbCreditApplicationStatusShouldNotBeFound("id.notEquals=" + id);

        defaultCrbCreditApplicationStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbCreditApplicationStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbCreditApplicationStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbCreditApplicationStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode equals to DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusTypeCode.equals=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusTypeCode.equals=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode not equals to DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusTypeCode.notEquals=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode not equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusTypeCode.notEquals=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode in DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE or UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusTypeCode.in=" +
            DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE +
            "," +
            UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusTypeCode.in=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode is not null
        defaultCrbCreditApplicationStatusShouldBeFound("crbCreditApplicationStatusTypeCode.specified=true");

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode is null
        defaultCrbCreditApplicationStatusShouldNotBeFound("crbCreditApplicationStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode contains DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusTypeCode.contains=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode contains UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusTypeCode.contains=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode does not contain DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusTypeCode.doesNotContain=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusTypeCode does not contain UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusTypeCode.doesNotContain=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType equals to DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusType.equals=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusType.equals=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType not equals to DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusType.notEquals=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType not equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusType.notEquals=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType in DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE or UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusType.in=" +
            DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE +
            "," +
            UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType equals to UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusType.in=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType is not null
        defaultCrbCreditApplicationStatusShouldBeFound("crbCreditApplicationStatusType.specified=true");

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType is null
        defaultCrbCreditApplicationStatusShouldNotBeFound("crbCreditApplicationStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType contains DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusType.contains=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType contains UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusType.contains=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbCreditApplicationStatusesByCrbCreditApplicationStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType does not contain DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldNotBeFound(
            "crbCreditApplicationStatusType.doesNotContain=" + DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );

        // Get all the crbCreditApplicationStatusList where crbCreditApplicationStatusType does not contain UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        defaultCrbCreditApplicationStatusShouldBeFound(
            "crbCreditApplicationStatusType.doesNotContain=" + UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbCreditApplicationStatusShouldBeFound(String filter) throws Exception {
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusTypeCode").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusType").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].crbCreditApplicationStatusDetails").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbCreditApplicationStatusShouldNotBeFound(String filter) throws Exception {
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbCreditApplicationStatus() throws Exception {
        // Get the crbCreditApplicationStatus
        restCrbCreditApplicationStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbCreditApplicationStatus() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();

        // Update the crbCreditApplicationStatus
        CrbCreditApplicationStatus updatedCrbCreditApplicationStatus = crbCreditApplicationStatusRepository
            .findById(crbCreditApplicationStatus.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbCreditApplicationStatus are not directly saved in db
        em.detach(updatedCrbCreditApplicationStatus);
        updatedCrbCreditApplicationStatus
            .crbCreditApplicationStatusTypeCode(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)
            .crbCreditApplicationStatusType(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE)
            .crbCreditApplicationStatusDetails(UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS);
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(
            updatedCrbCreditApplicationStatus
        );

        restCrbCreditApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCreditApplicationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditApplicationStatus testCrbCreditApplicationStatus = crbCreditApplicationStatusList.get(
            crbCreditApplicationStatusList.size() - 1
        );
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusTypeCode())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusType())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusDetails())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository).save(testCrbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void putNonExistingCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCreditApplicationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void partialUpdateCrbCreditApplicationStatusWithPatch() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();

        // Update the crbCreditApplicationStatus using partial update
        CrbCreditApplicationStatus partialUpdatedCrbCreditApplicationStatus = new CrbCreditApplicationStatus();
        partialUpdatedCrbCreditApplicationStatus.setId(crbCreditApplicationStatus.getId());

        partialUpdatedCrbCreditApplicationStatus
            .crbCreditApplicationStatusTypeCode(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)
            .crbCreditApplicationStatusType(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE);

        restCrbCreditApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCreditApplicationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCreditApplicationStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditApplicationStatus testCrbCreditApplicationStatus = crbCreditApplicationStatusList.get(
            crbCreditApplicationStatusList.size() - 1
        );
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusTypeCode())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusType())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusDetails())
            .isEqualTo(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbCreditApplicationStatusWithPatch() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();

        // Update the crbCreditApplicationStatus using partial update
        CrbCreditApplicationStatus partialUpdatedCrbCreditApplicationStatus = new CrbCreditApplicationStatus();
        partialUpdatedCrbCreditApplicationStatus.setId(crbCreditApplicationStatus.getId());

        partialUpdatedCrbCreditApplicationStatus
            .crbCreditApplicationStatusTypeCode(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)
            .crbCreditApplicationStatusType(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE)
            .crbCreditApplicationStatusDetails(UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS);

        restCrbCreditApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCreditApplicationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCreditApplicationStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditApplicationStatus testCrbCreditApplicationStatus = crbCreditApplicationStatusList.get(
            crbCreditApplicationStatusList.size() - 1
        );
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusTypeCode())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusType())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_TYPE);
        assertThat(testCrbCreditApplicationStatus.getCrbCreditApplicationStatusDetails())
            .isEqualTo(UPDATED_CRB_CREDIT_APPLICATION_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbCreditApplicationStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbCreditApplicationStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditApplicationStatusRepository.findAll().size();
        crbCreditApplicationStatus.setId(count.incrementAndGet());

        // Create the CrbCreditApplicationStatus
        CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO = crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditApplicationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditApplicationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCreditApplicationStatus in the database
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(0)).save(crbCreditApplicationStatus);
    }

    @Test
    @Transactional
    void deleteCrbCreditApplicationStatus() throws Exception {
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);

        int databaseSizeBeforeDelete = crbCreditApplicationStatusRepository.findAll().size();

        // Delete the crbCreditApplicationStatus
        restCrbCreditApplicationStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbCreditApplicationStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbCreditApplicationStatus> crbCreditApplicationStatusList = crbCreditApplicationStatusRepository.findAll();
        assertThat(crbCreditApplicationStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbCreditApplicationStatus in Elasticsearch
        verify(mockCrbCreditApplicationStatusSearchRepository, times(1)).deleteById(crbCreditApplicationStatus.getId());
    }

    @Test
    @Transactional
    void searchCrbCreditApplicationStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbCreditApplicationStatusRepository.saveAndFlush(crbCreditApplicationStatus);
        when(mockCrbCreditApplicationStatusSearchRepository.search("id:" + crbCreditApplicationStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbCreditApplicationStatus), PageRequest.of(0, 1), 1));

        // Search the crbCreditApplicationStatus
        restCrbCreditApplicationStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbCreditApplicationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditApplicationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusTypeCode").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].crbCreditApplicationStatusType").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].crbCreditApplicationStatusDetails").value(hasItem(DEFAULT_CRB_CREDIT_APPLICATION_STATUS_DETAILS.toString()))
            );
    }
}
