package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CrbFileTransmissionStatus;
import io.github.erp.domain.enumeration.SubmittedFileStatusTypes;
import io.github.erp.repository.CrbFileTransmissionStatusRepository;
import io.github.erp.repository.search.CrbFileTransmissionStatusSearchRepository;
import io.github.erp.service.criteria.CrbFileTransmissionStatusCriteria;
import io.github.erp.service.dto.CrbFileTransmissionStatusDTO;
import io.github.erp.service.mapper.CrbFileTransmissionStatusMapper;
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
 * Integration tests for the {@link CrbFileTransmissionStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbFileTransmissionStatusResourceIT {

    private static final String DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final SubmittedFileStatusTypes DEFAULT_SUBMITTED_FILE_STATUS_TYPE = SubmittedFileStatusTypes.CORRECT;
    private static final SubmittedFileStatusTypes UPDATED_SUBMITTED_FILE_STATUS_TYPE = SubmittedFileStatusTypes.INCORRECT;

    private static final String DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-file-transmission-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-file-transmission-statuses";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository;

    @Autowired
    private CrbFileTransmissionStatusMapper crbFileTransmissionStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbFileTransmissionStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbFileTransmissionStatusSearchRepository mockCrbFileTransmissionStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbFileTransmissionStatusMockMvc;

    private CrbFileTransmissionStatus crbFileTransmissionStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbFileTransmissionStatus createEntity(EntityManager em) {
        CrbFileTransmissionStatus crbFileTransmissionStatus = new CrbFileTransmissionStatus()
            .submittedFileStatusTypeCode(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE)
            .submittedFileStatusType(DEFAULT_SUBMITTED_FILE_STATUS_TYPE)
            .submittedFileStatusTypeDescription(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);
        return crbFileTransmissionStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbFileTransmissionStatus createUpdatedEntity(EntityManager em) {
        CrbFileTransmissionStatus crbFileTransmissionStatus = new CrbFileTransmissionStatus()
            .submittedFileStatusTypeCode(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE)
            .submittedFileStatusType(UPDATED_SUBMITTED_FILE_STATUS_TYPE)
            .submittedFileStatusTypeDescription(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);
        return crbFileTransmissionStatus;
    }

    @BeforeEach
    public void initTest() {
        crbFileTransmissionStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeCreate = crbFileTransmissionStatusRepository.findAll().size();
        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);
        restCrbFileTransmissionStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CrbFileTransmissionStatus testCrbFileTransmissionStatus = crbFileTransmissionStatusList.get(
            crbFileTransmissionStatusList.size() - 1
        );
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeCode()).isEqualTo(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusType()).isEqualTo(DEFAULT_SUBMITTED_FILE_STATUS_TYPE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeDescription())
            .isEqualTo(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(1)).save(testCrbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void createCrbFileTransmissionStatusWithExistingId() throws Exception {
        // Create the CrbFileTransmissionStatus with an existing ID
        crbFileTransmissionStatus.setId(1L);
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        int databaseSizeBeforeCreate = crbFileTransmissionStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbFileTransmissionStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void checkSubmittedFileStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbFileTransmissionStatusRepository.findAll().size();
        // set the field null
        crbFileTransmissionStatus.setSubmittedFileStatusTypeCode(null);

        // Create the CrbFileTransmissionStatus, which fails.
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        restCrbFileTransmissionStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmittedFileStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbFileTransmissionStatusRepository.findAll().size();
        // set the field null
        crbFileTransmissionStatus.setSubmittedFileStatusType(null);

        // Create the CrbFileTransmissionStatus, which fails.
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        restCrbFileTransmissionStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatuses() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbFileTransmissionStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].submittedFileStatusTypeCode").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].submittedFileStatusType").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].submittedFileStatusTypeDescription")
                    .value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION))
            );
    }

    @Test
    @Transactional
    void getCrbFileTransmissionStatus() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get the crbFileTransmissionStatus
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, crbFileTransmissionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbFileTransmissionStatus.getId().intValue()))
            .andExpect(jsonPath("$.submittedFileStatusTypeCode").value(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.submittedFileStatusType").value(DEFAULT_SUBMITTED_FILE_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.submittedFileStatusTypeDescription").value(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCrbFileTransmissionStatusesByIdFiltering() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        Long id = crbFileTransmissionStatus.getId();

        defaultCrbFileTransmissionStatusShouldBeFound("id.equals=" + id);
        defaultCrbFileTransmissionStatusShouldNotBeFound("id.notEquals=" + id);

        defaultCrbFileTransmissionStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbFileTransmissionStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbFileTransmissionStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbFileTransmissionStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode equals to DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusTypeCode.equals=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusTypeCode.equals=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode not equals to DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldNotBeFound(
            "submittedFileStatusTypeCode.notEquals=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        );

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode not equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusTypeCode.notEquals=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode in DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE or UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldBeFound(
            "submittedFileStatusTypeCode.in=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE + "," + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        );

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusTypeCode.in=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode is not null
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusTypeCode.specified=true");

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode is null
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode contains DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusTypeCode.contains=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode contains UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusTypeCode.contains=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode does not contain DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldNotBeFound(
            "submittedFileStatusTypeCode.doesNotContain=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE
        );

        // Get all the crbFileTransmissionStatusList where submittedFileStatusTypeCode does not contain UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        defaultCrbFileTransmissionStatusShouldBeFound(
            "submittedFileStatusTypeCode.doesNotContain=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType equals to DEFAULT_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusType.equals=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusType.equals=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType not equals to DEFAULT_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusType.notEquals=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType not equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusType.notEquals=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType in DEFAULT_SUBMITTED_FILE_STATUS_TYPE or UPDATED_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldBeFound(
            "submittedFileStatusType.in=" + DEFAULT_SUBMITTED_FILE_STATUS_TYPE + "," + UPDATED_SUBMITTED_FILE_STATUS_TYPE
        );

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType equals to UPDATED_SUBMITTED_FILE_STATUS_TYPE
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusType.in=" + UPDATED_SUBMITTED_FILE_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbFileTransmissionStatusesBySubmittedFileStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType is not null
        defaultCrbFileTransmissionStatusShouldBeFound("submittedFileStatusType.specified=true");

        // Get all the crbFileTransmissionStatusList where submittedFileStatusType is null
        defaultCrbFileTransmissionStatusShouldNotBeFound("submittedFileStatusType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbFileTransmissionStatusShouldBeFound(String filter) throws Exception {
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbFileTransmissionStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].submittedFileStatusTypeCode").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].submittedFileStatusType").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].submittedFileStatusTypeDescription")
                    .value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION))
            );

        // Check, that the count call also returns 1
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbFileTransmissionStatusShouldNotBeFound(String filter) throws Exception {
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbFileTransmissionStatus() throws Exception {
        // Get the crbFileTransmissionStatus
        restCrbFileTransmissionStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbFileTransmissionStatus() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();

        // Update the crbFileTransmissionStatus
        CrbFileTransmissionStatus updatedCrbFileTransmissionStatus = crbFileTransmissionStatusRepository
            .findById(crbFileTransmissionStatus.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbFileTransmissionStatus are not directly saved in db
        em.detach(updatedCrbFileTransmissionStatus);
        updatedCrbFileTransmissionStatus
            .submittedFileStatusTypeCode(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE)
            .submittedFileStatusType(UPDATED_SUBMITTED_FILE_STATUS_TYPE)
            .submittedFileStatusTypeDescription(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(updatedCrbFileTransmissionStatus);

        restCrbFileTransmissionStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbFileTransmissionStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbFileTransmissionStatus testCrbFileTransmissionStatus = crbFileTransmissionStatusList.get(
            crbFileTransmissionStatusList.size() - 1
        );
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeCode()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusType()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeDescription())
            .isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository).save(testCrbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void putNonExistingCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbFileTransmissionStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void partialUpdateCrbFileTransmissionStatusWithPatch() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();

        // Update the crbFileTransmissionStatus using partial update
        CrbFileTransmissionStatus partialUpdatedCrbFileTransmissionStatus = new CrbFileTransmissionStatus();
        partialUpdatedCrbFileTransmissionStatus.setId(crbFileTransmissionStatus.getId());

        partialUpdatedCrbFileTransmissionStatus
            .submittedFileStatusTypeCode(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE)
            .submittedFileStatusType(UPDATED_SUBMITTED_FILE_STATUS_TYPE)
            .submittedFileStatusTypeDescription(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);

        restCrbFileTransmissionStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbFileTransmissionStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbFileTransmissionStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbFileTransmissionStatus testCrbFileTransmissionStatus = crbFileTransmissionStatusList.get(
            crbFileTransmissionStatusList.size() - 1
        );
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeCode()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusType()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeDescription())
            .isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbFileTransmissionStatusWithPatch() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();

        // Update the crbFileTransmissionStatus using partial update
        CrbFileTransmissionStatus partialUpdatedCrbFileTransmissionStatus = new CrbFileTransmissionStatus();
        partialUpdatedCrbFileTransmissionStatus.setId(crbFileTransmissionStatus.getId());

        partialUpdatedCrbFileTransmissionStatus
            .submittedFileStatusTypeCode(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE)
            .submittedFileStatusType(UPDATED_SUBMITTED_FILE_STATUS_TYPE)
            .submittedFileStatusTypeDescription(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);

        restCrbFileTransmissionStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbFileTransmissionStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbFileTransmissionStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbFileTransmissionStatus testCrbFileTransmissionStatus = crbFileTransmissionStatusList.get(
            crbFileTransmissionStatusList.size() - 1
        );
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeCode()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_CODE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusType()).isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE);
        assertThat(testCrbFileTransmissionStatus.getSubmittedFileStatusTypeDescription())
            .isEqualTo(UPDATED_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbFileTransmissionStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbFileTransmissionStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbFileTransmissionStatusRepository.findAll().size();
        crbFileTransmissionStatus.setId(count.incrementAndGet());

        // Create the CrbFileTransmissionStatus
        CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO = crbFileTransmissionStatusMapper.toDto(crbFileTransmissionStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbFileTransmissionStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbFileTransmissionStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbFileTransmissionStatus in the database
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(0)).save(crbFileTransmissionStatus);
    }

    @Test
    @Transactional
    void deleteCrbFileTransmissionStatus() throws Exception {
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);

        int databaseSizeBeforeDelete = crbFileTransmissionStatusRepository.findAll().size();

        // Delete the crbFileTransmissionStatus
        restCrbFileTransmissionStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbFileTransmissionStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbFileTransmissionStatus> crbFileTransmissionStatusList = crbFileTransmissionStatusRepository.findAll();
        assertThat(crbFileTransmissionStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbFileTransmissionStatus in Elasticsearch
        verify(mockCrbFileTransmissionStatusSearchRepository, times(1)).deleteById(crbFileTransmissionStatus.getId());
    }

    @Test
    @Transactional
    void searchCrbFileTransmissionStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbFileTransmissionStatusRepository.saveAndFlush(crbFileTransmissionStatus);
        when(mockCrbFileTransmissionStatusSearchRepository.search("id:" + crbFileTransmissionStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbFileTransmissionStatus), PageRequest.of(0, 1), 1));

        // Search the crbFileTransmissionStatus
        restCrbFileTransmissionStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbFileTransmissionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbFileTransmissionStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].submittedFileStatusTypeCode").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].submittedFileStatusType").value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].submittedFileStatusTypeDescription")
                    .value(hasItem(DEFAULT_SUBMITTED_FILE_STATUS_TYPE_DESCRIPTION))
            );
    }
}
