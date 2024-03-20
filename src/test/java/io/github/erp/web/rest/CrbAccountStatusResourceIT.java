package io.github.erp.web.rest;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CrbAccountStatus;
import io.github.erp.repository.CrbAccountStatusRepository;
import io.github.erp.repository.search.CrbAccountStatusSearchRepository;
import io.github.erp.service.criteria.CrbAccountStatusCriteria;
import io.github.erp.service.dto.CrbAccountStatusDTO;
import io.github.erp.service.mapper.CrbAccountStatusMapper;
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
 * Integration tests for the {@link CrbAccountStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbAccountStatusResourceIT {

    private static final String DEFAULT_ACCOUNT_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-account-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-account-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbAccountStatusRepository crbAccountStatusRepository;

    @Autowired
    private CrbAccountStatusMapper crbAccountStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbAccountStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbAccountStatusSearchRepository mockCrbAccountStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbAccountStatusMockMvc;

    private CrbAccountStatus crbAccountStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAccountStatus createEntity(EntityManager em) {
        CrbAccountStatus crbAccountStatus = new CrbAccountStatus()
            .accountStatusTypeCode(DEFAULT_ACCOUNT_STATUS_TYPE_CODE)
            .accountStatusType(DEFAULT_ACCOUNT_STATUS_TYPE)
            .accountStatusTypeDetails(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS);
        return crbAccountStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAccountStatus createUpdatedEntity(EntityManager em) {
        CrbAccountStatus crbAccountStatus = new CrbAccountStatus()
            .accountStatusTypeCode(UPDATED_ACCOUNT_STATUS_TYPE_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusTypeDetails(UPDATED_ACCOUNT_STATUS_TYPE_DETAILS);
        return crbAccountStatus;
    }

    @BeforeEach
    public void initTest() {
        crbAccountStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbAccountStatus() throws Exception {
        int databaseSizeBeforeCreate = crbAccountStatusRepository.findAll().size();
        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);
        restCrbAccountStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CrbAccountStatus testCrbAccountStatus = crbAccountStatusList.get(crbAccountStatusList.size() - 1);
        assertThat(testCrbAccountStatus.getAccountStatusTypeCode()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE_CODE);
        assertThat(testCrbAccountStatus.getAccountStatusType()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE);
        assertThat(testCrbAccountStatus.getAccountStatusTypeDetails()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(1)).save(testCrbAccountStatus);
    }

    @Test
    @Transactional
    void createCrbAccountStatusWithExistingId() throws Exception {
        // Create the CrbAccountStatus with an existing ID
        crbAccountStatus.setId(1L);
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        int databaseSizeBeforeCreate = crbAccountStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbAccountStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void checkAccountStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAccountStatusRepository.findAll().size();
        // set the field null
        crbAccountStatus.setAccountStatusTypeCode(null);

        // Create the CrbAccountStatus, which fails.
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        restCrbAccountStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAccountStatusRepository.findAll().size();
        // set the field null
        crbAccountStatus.setAccountStatusType(null);

        // Create the CrbAccountStatus, which fails.
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        restCrbAccountStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatuses() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusTypeCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].accountStatusTypeDetails").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbAccountStatus() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get the crbAccountStatus
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, crbAccountStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbAccountStatus.getId().intValue()))
            .andExpect(jsonPath("$.accountStatusTypeCode").value(DEFAULT_ACCOUNT_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.accountStatusType").value(DEFAULT_ACCOUNT_STATUS_TYPE))
            .andExpect(jsonPath("$.accountStatusTypeDetails").value(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbAccountStatusesByIdFiltering() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        Long id = crbAccountStatus.getId();

        defaultCrbAccountStatusShouldBeFound("id.equals=" + id);
        defaultCrbAccountStatusShouldNotBeFound("id.notEquals=" + id);

        defaultCrbAccountStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbAccountStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbAccountStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbAccountStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode equals to DEFAULT_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldBeFound("accountStatusTypeCode.equals=" + DEFAULT_ACCOUNT_STATUS_TYPE_CODE);

        // Get all the crbAccountStatusList where accountStatusTypeCode equals to UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.equals=" + UPDATED_ACCOUNT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode not equals to DEFAULT_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.notEquals=" + DEFAULT_ACCOUNT_STATUS_TYPE_CODE);

        // Get all the crbAccountStatusList where accountStatusTypeCode not equals to UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldBeFound("accountStatusTypeCode.notEquals=" + UPDATED_ACCOUNT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode in DEFAULT_ACCOUNT_STATUS_TYPE_CODE or UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldBeFound(
            "accountStatusTypeCode.in=" + DEFAULT_ACCOUNT_STATUS_TYPE_CODE + "," + UPDATED_ACCOUNT_STATUS_TYPE_CODE
        );

        // Get all the crbAccountStatusList where accountStatusTypeCode equals to UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.in=" + UPDATED_ACCOUNT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode is not null
        defaultCrbAccountStatusShouldBeFound("accountStatusTypeCode.specified=true");

        // Get all the crbAccountStatusList where accountStatusTypeCode is null
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode contains DEFAULT_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldBeFound("accountStatusTypeCode.contains=" + DEFAULT_ACCOUNT_STATUS_TYPE_CODE);

        // Get all the crbAccountStatusList where accountStatusTypeCode contains UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.contains=" + UPDATED_ACCOUNT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusTypeCode does not contain DEFAULT_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusTypeCode.doesNotContain=" + DEFAULT_ACCOUNT_STATUS_TYPE_CODE);

        // Get all the crbAccountStatusList where accountStatusTypeCode does not contain UPDATED_ACCOUNT_STATUS_TYPE_CODE
        defaultCrbAccountStatusShouldBeFound("accountStatusTypeCode.doesNotContain=" + UPDATED_ACCOUNT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType equals to DEFAULT_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldBeFound("accountStatusType.equals=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the crbAccountStatusList where accountStatusType equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.equals=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType not equals to DEFAULT_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.notEquals=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the crbAccountStatusList where accountStatusType not equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldBeFound("accountStatusType.notEquals=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType in DEFAULT_ACCOUNT_STATUS_TYPE or UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldBeFound("accountStatusType.in=" + DEFAULT_ACCOUNT_STATUS_TYPE + "," + UPDATED_ACCOUNT_STATUS_TYPE);

        // Get all the crbAccountStatusList where accountStatusType equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.in=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType is not null
        defaultCrbAccountStatusShouldBeFound("accountStatusType.specified=true");

        // Get all the crbAccountStatusList where accountStatusType is null
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType contains DEFAULT_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldBeFound("accountStatusType.contains=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the crbAccountStatusList where accountStatusType contains UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.contains=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountStatusesByAccountStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        // Get all the crbAccountStatusList where accountStatusType does not contain DEFAULT_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldNotBeFound("accountStatusType.doesNotContain=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the crbAccountStatusList where accountStatusType does not contain UPDATED_ACCOUNT_STATUS_TYPE
        defaultCrbAccountStatusShouldBeFound("accountStatusType.doesNotContain=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbAccountStatusShouldBeFound(String filter) throws Exception {
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusTypeCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].accountStatusTypeDetails").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbAccountStatusShouldNotBeFound(String filter) throws Exception {
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbAccountStatus() throws Exception {
        // Get the crbAccountStatus
        restCrbAccountStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbAccountStatus() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();

        // Update the crbAccountStatus
        CrbAccountStatus updatedCrbAccountStatus = crbAccountStatusRepository.findById(crbAccountStatus.getId()).get();
        // Disconnect from session so that the updates on updatedCrbAccountStatus are not directly saved in db
        em.detach(updatedCrbAccountStatus);
        updatedCrbAccountStatus
            .accountStatusTypeCode(UPDATED_ACCOUNT_STATUS_TYPE_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusTypeDetails(UPDATED_ACCOUNT_STATUS_TYPE_DETAILS);
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(updatedCrbAccountStatus);

        restCrbAccountStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAccountStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountStatus testCrbAccountStatus = crbAccountStatusList.get(crbAccountStatusList.size() - 1);
        assertThat(testCrbAccountStatus.getAccountStatusTypeCode()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE_CODE);
        assertThat(testCrbAccountStatus.getAccountStatusType()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE);
        assertThat(testCrbAccountStatus.getAccountStatusTypeDetails()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE_DETAILS);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository).save(testCrbAccountStatus);
    }

    @Test
    @Transactional
    void putNonExistingCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAccountStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void partialUpdateCrbAccountStatusWithPatch() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();

        // Update the crbAccountStatus using partial update
        CrbAccountStatus partialUpdatedCrbAccountStatus = new CrbAccountStatus();
        partialUpdatedCrbAccountStatus.setId(crbAccountStatus.getId());

        partialUpdatedCrbAccountStatus
            .accountStatusTypeCode(UPDATED_ACCOUNT_STATUS_TYPE_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE);

        restCrbAccountStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAccountStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAccountStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountStatus testCrbAccountStatus = crbAccountStatusList.get(crbAccountStatusList.size() - 1);
        assertThat(testCrbAccountStatus.getAccountStatusTypeCode()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE_CODE);
        assertThat(testCrbAccountStatus.getAccountStatusType()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE);
        assertThat(testCrbAccountStatus.getAccountStatusTypeDetails()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbAccountStatusWithPatch() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();

        // Update the crbAccountStatus using partial update
        CrbAccountStatus partialUpdatedCrbAccountStatus = new CrbAccountStatus();
        partialUpdatedCrbAccountStatus.setId(crbAccountStatus.getId());

        partialUpdatedCrbAccountStatus
            .accountStatusTypeCode(UPDATED_ACCOUNT_STATUS_TYPE_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusTypeDetails(UPDATED_ACCOUNT_STATUS_TYPE_DETAILS);

        restCrbAccountStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAccountStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAccountStatus))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountStatus testCrbAccountStatus = crbAccountStatusList.get(crbAccountStatusList.size() - 1);
        assertThat(testCrbAccountStatus.getAccountStatusTypeCode()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE_CODE);
        assertThat(testCrbAccountStatus.getAccountStatusType()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE);
        assertThat(testCrbAccountStatus.getAccountStatusTypeDetails()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbAccountStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbAccountStatus() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountStatusRepository.findAll().size();
        crbAccountStatus.setId(count.incrementAndGet());

        // Create the CrbAccountStatus
        CrbAccountStatusDTO crbAccountStatusDTO = crbAccountStatusMapper.toDto(crbAccountStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAccountStatus in the database
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(0)).save(crbAccountStatus);
    }

    @Test
    @Transactional
    void deleteCrbAccountStatus() throws Exception {
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);

        int databaseSizeBeforeDelete = crbAccountStatusRepository.findAll().size();

        // Delete the crbAccountStatus
        restCrbAccountStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbAccountStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbAccountStatus> crbAccountStatusList = crbAccountStatusRepository.findAll();
        assertThat(crbAccountStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbAccountStatus in Elasticsearch
        verify(mockCrbAccountStatusSearchRepository, times(1)).deleteById(crbAccountStatus.getId());
    }

    @Test
    @Transactional
    void searchCrbAccountStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbAccountStatusRepository.saveAndFlush(crbAccountStatus);
        when(mockCrbAccountStatusSearchRepository.search("id:" + crbAccountStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbAccountStatus), PageRequest.of(0, 1), 1));

        // Search the crbAccountStatus
        restCrbAccountStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbAccountStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusTypeCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].accountStatusTypeDetails").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE_DETAILS.toString())));
    }
}
