package io.github.erp.web.rest;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LegalStatus;
import io.github.erp.repository.LegalStatusRepository;
import io.github.erp.repository.search.LegalStatusSearchRepository;
import io.github.erp.service.criteria.LegalStatusCriteria;
import io.github.erp.service.dto.LegalStatusDTO;
import io.github.erp.service.mapper.LegalStatusMapper;
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
 * Integration tests for the {@link LegalStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LegalStatusResourceIT {

    private static final String DEFAULT_LEGAL_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_STATUS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_STATUS_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/legal-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/legal-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LegalStatusRepository legalStatusRepository;

    @Autowired
    private LegalStatusMapper legalStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LegalStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private LegalStatusSearchRepository mockLegalStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLegalStatusMockMvc;

    private LegalStatus legalStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalStatus createEntity(EntityManager em) {
        LegalStatus legalStatus = new LegalStatus()
            .legalStatusCode(DEFAULT_LEGAL_STATUS_CODE)
            .legalStatusType(DEFAULT_LEGAL_STATUS_TYPE)
            .legalStatusDescription(DEFAULT_LEGAL_STATUS_DESCRIPTION);
        return legalStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalStatus createUpdatedEntity(EntityManager em) {
        LegalStatus legalStatus = new LegalStatus()
            .legalStatusCode(UPDATED_LEGAL_STATUS_CODE)
            .legalStatusType(UPDATED_LEGAL_STATUS_TYPE)
            .legalStatusDescription(UPDATED_LEGAL_STATUS_DESCRIPTION);
        return legalStatus;
    }

    @BeforeEach
    public void initTest() {
        legalStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createLegalStatus() throws Exception {
        int databaseSizeBeforeCreate = legalStatusRepository.findAll().size();
        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);
        restLegalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeCreate + 1);
        LegalStatus testLegalStatus = legalStatusList.get(legalStatusList.size() - 1);
        assertThat(testLegalStatus.getLegalStatusCode()).isEqualTo(DEFAULT_LEGAL_STATUS_CODE);
        assertThat(testLegalStatus.getLegalStatusType()).isEqualTo(DEFAULT_LEGAL_STATUS_TYPE);
        assertThat(testLegalStatus.getLegalStatusDescription()).isEqualTo(DEFAULT_LEGAL_STATUS_DESCRIPTION);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(1)).save(testLegalStatus);
    }

    @Test
    @Transactional
    void createLegalStatusWithExistingId() throws Exception {
        // Create the LegalStatus with an existing ID
        legalStatus.setId(1L);
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        int databaseSizeBeforeCreate = legalStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLegalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void checkLegalStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalStatusRepository.findAll().size();
        // set the field null
        legalStatus.setLegalStatusCode(null);

        // Create the LegalStatus, which fails.
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        restLegalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLegalStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalStatusRepository.findAll().size();
        // set the field null
        legalStatus.setLegalStatusType(null);

        // Create the LegalStatus, which fails.
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        restLegalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLegalStatuses() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalStatusCode").value(hasItem(DEFAULT_LEGAL_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].legalStatusType").value(hasItem(DEFAULT_LEGAL_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].legalStatusDescription").value(hasItem(DEFAULT_LEGAL_STATUS_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getLegalStatus() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get the legalStatus
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, legalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(legalStatus.getId().intValue()))
            .andExpect(jsonPath("$.legalStatusCode").value(DEFAULT_LEGAL_STATUS_CODE))
            .andExpect(jsonPath("$.legalStatusType").value(DEFAULT_LEGAL_STATUS_TYPE))
            .andExpect(jsonPath("$.legalStatusDescription").value(DEFAULT_LEGAL_STATUS_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getLegalStatusesByIdFiltering() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        Long id = legalStatus.getId();

        defaultLegalStatusShouldBeFound("id.equals=" + id);
        defaultLegalStatusShouldNotBeFound("id.notEquals=" + id);

        defaultLegalStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLegalStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultLegalStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLegalStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode equals to DEFAULT_LEGAL_STATUS_CODE
        defaultLegalStatusShouldBeFound("legalStatusCode.equals=" + DEFAULT_LEGAL_STATUS_CODE);

        // Get all the legalStatusList where legalStatusCode equals to UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldNotBeFound("legalStatusCode.equals=" + UPDATED_LEGAL_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode not equals to DEFAULT_LEGAL_STATUS_CODE
        defaultLegalStatusShouldNotBeFound("legalStatusCode.notEquals=" + DEFAULT_LEGAL_STATUS_CODE);

        // Get all the legalStatusList where legalStatusCode not equals to UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldBeFound("legalStatusCode.notEquals=" + UPDATED_LEGAL_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode in DEFAULT_LEGAL_STATUS_CODE or UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldBeFound("legalStatusCode.in=" + DEFAULT_LEGAL_STATUS_CODE + "," + UPDATED_LEGAL_STATUS_CODE);

        // Get all the legalStatusList where legalStatusCode equals to UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldNotBeFound("legalStatusCode.in=" + UPDATED_LEGAL_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode is not null
        defaultLegalStatusShouldBeFound("legalStatusCode.specified=true");

        // Get all the legalStatusList where legalStatusCode is null
        defaultLegalStatusShouldNotBeFound("legalStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode contains DEFAULT_LEGAL_STATUS_CODE
        defaultLegalStatusShouldBeFound("legalStatusCode.contains=" + DEFAULT_LEGAL_STATUS_CODE);

        // Get all the legalStatusList where legalStatusCode contains UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldNotBeFound("legalStatusCode.contains=" + UPDATED_LEGAL_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusCode does not contain DEFAULT_LEGAL_STATUS_CODE
        defaultLegalStatusShouldNotBeFound("legalStatusCode.doesNotContain=" + DEFAULT_LEGAL_STATUS_CODE);

        // Get all the legalStatusList where legalStatusCode does not contain UPDATED_LEGAL_STATUS_CODE
        defaultLegalStatusShouldBeFound("legalStatusCode.doesNotContain=" + UPDATED_LEGAL_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType equals to DEFAULT_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldBeFound("legalStatusType.equals=" + DEFAULT_LEGAL_STATUS_TYPE);

        // Get all the legalStatusList where legalStatusType equals to UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldNotBeFound("legalStatusType.equals=" + UPDATED_LEGAL_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType not equals to DEFAULT_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldNotBeFound("legalStatusType.notEquals=" + DEFAULT_LEGAL_STATUS_TYPE);

        // Get all the legalStatusList where legalStatusType not equals to UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldBeFound("legalStatusType.notEquals=" + UPDATED_LEGAL_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType in DEFAULT_LEGAL_STATUS_TYPE or UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldBeFound("legalStatusType.in=" + DEFAULT_LEGAL_STATUS_TYPE + "," + UPDATED_LEGAL_STATUS_TYPE);

        // Get all the legalStatusList where legalStatusType equals to UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldNotBeFound("legalStatusType.in=" + UPDATED_LEGAL_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType is not null
        defaultLegalStatusShouldBeFound("legalStatusType.specified=true");

        // Get all the legalStatusList where legalStatusType is null
        defaultLegalStatusShouldNotBeFound("legalStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType contains DEFAULT_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldBeFound("legalStatusType.contains=" + DEFAULT_LEGAL_STATUS_TYPE);

        // Get all the legalStatusList where legalStatusType contains UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldNotBeFound("legalStatusType.contains=" + UPDATED_LEGAL_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllLegalStatusesByLegalStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        // Get all the legalStatusList where legalStatusType does not contain DEFAULT_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldNotBeFound("legalStatusType.doesNotContain=" + DEFAULT_LEGAL_STATUS_TYPE);

        // Get all the legalStatusList where legalStatusType does not contain UPDATED_LEGAL_STATUS_TYPE
        defaultLegalStatusShouldBeFound("legalStatusType.doesNotContain=" + UPDATED_LEGAL_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLegalStatusShouldBeFound(String filter) throws Exception {
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalStatusCode").value(hasItem(DEFAULT_LEGAL_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].legalStatusType").value(hasItem(DEFAULT_LEGAL_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].legalStatusDescription").value(hasItem(DEFAULT_LEGAL_STATUS_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLegalStatusShouldNotBeFound(String filter) throws Exception {
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLegalStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLegalStatus() throws Exception {
        // Get the legalStatus
        restLegalStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLegalStatus() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();

        // Update the legalStatus
        LegalStatus updatedLegalStatus = legalStatusRepository.findById(legalStatus.getId()).get();
        // Disconnect from session so that the updates on updatedLegalStatus are not directly saved in db
        em.detach(updatedLegalStatus);
        updatedLegalStatus
            .legalStatusCode(UPDATED_LEGAL_STATUS_CODE)
            .legalStatusType(UPDATED_LEGAL_STATUS_TYPE)
            .legalStatusDescription(UPDATED_LEGAL_STATUS_DESCRIPTION);
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(updatedLegalStatus);

        restLegalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, legalStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);
        LegalStatus testLegalStatus = legalStatusList.get(legalStatusList.size() - 1);
        assertThat(testLegalStatus.getLegalStatusCode()).isEqualTo(UPDATED_LEGAL_STATUS_CODE);
        assertThat(testLegalStatus.getLegalStatusType()).isEqualTo(UPDATED_LEGAL_STATUS_TYPE);
        assertThat(testLegalStatus.getLegalStatusDescription()).isEqualTo(UPDATED_LEGAL_STATUS_DESCRIPTION);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository).save(testLegalStatus);
    }

    @Test
    @Transactional
    void putNonExistingLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, legalStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(legalStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void partialUpdateLegalStatusWithPatch() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();

        // Update the legalStatus using partial update
        LegalStatus partialUpdatedLegalStatus = new LegalStatus();
        partialUpdatedLegalStatus.setId(legalStatus.getId());

        partialUpdatedLegalStatus.legalStatusCode(UPDATED_LEGAL_STATUS_CODE).legalStatusDescription(UPDATED_LEGAL_STATUS_DESCRIPTION);

        restLegalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLegalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLegalStatus))
            )
            .andExpect(status().isOk());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);
        LegalStatus testLegalStatus = legalStatusList.get(legalStatusList.size() - 1);
        assertThat(testLegalStatus.getLegalStatusCode()).isEqualTo(UPDATED_LEGAL_STATUS_CODE);
        assertThat(testLegalStatus.getLegalStatusType()).isEqualTo(DEFAULT_LEGAL_STATUS_TYPE);
        assertThat(testLegalStatus.getLegalStatusDescription()).isEqualTo(UPDATED_LEGAL_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLegalStatusWithPatch() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();

        // Update the legalStatus using partial update
        LegalStatus partialUpdatedLegalStatus = new LegalStatus();
        partialUpdatedLegalStatus.setId(legalStatus.getId());

        partialUpdatedLegalStatus
            .legalStatusCode(UPDATED_LEGAL_STATUS_CODE)
            .legalStatusType(UPDATED_LEGAL_STATUS_TYPE)
            .legalStatusDescription(UPDATED_LEGAL_STATUS_DESCRIPTION);

        restLegalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLegalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLegalStatus))
            )
            .andExpect(status().isOk());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);
        LegalStatus testLegalStatus = legalStatusList.get(legalStatusList.size() - 1);
        assertThat(testLegalStatus.getLegalStatusCode()).isEqualTo(UPDATED_LEGAL_STATUS_CODE);
        assertThat(testLegalStatus.getLegalStatusType()).isEqualTo(UPDATED_LEGAL_STATUS_TYPE);
        assertThat(testLegalStatus.getLegalStatusDescription()).isEqualTo(UPDATED_LEGAL_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, legalStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLegalStatus() throws Exception {
        int databaseSizeBeforeUpdate = legalStatusRepository.findAll().size();
        legalStatus.setId(count.incrementAndGet());

        // Create the LegalStatus
        LegalStatusDTO legalStatusDTO = legalStatusMapper.toDto(legalStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(legalStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LegalStatus in the database
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(0)).save(legalStatus);
    }

    @Test
    @Transactional
    void deleteLegalStatus() throws Exception {
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);

        int databaseSizeBeforeDelete = legalStatusRepository.findAll().size();

        // Delete the legalStatus
        restLegalStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, legalStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LegalStatus> legalStatusList = legalStatusRepository.findAll();
        assertThat(legalStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LegalStatus in Elasticsearch
        verify(mockLegalStatusSearchRepository, times(1)).deleteById(legalStatus.getId());
    }

    @Test
    @Transactional
    void searchLegalStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        legalStatusRepository.saveAndFlush(legalStatus);
        when(mockLegalStatusSearchRepository.search("id:" + legalStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(legalStatus), PageRequest.of(0, 1), 1));

        // Search the legalStatus
        restLegalStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + legalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalStatusCode").value(hasItem(DEFAULT_LEGAL_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].legalStatusType").value(hasItem(DEFAULT_LEGAL_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].legalStatusDescription").value(hasItem(DEFAULT_LEGAL_STATUS_DESCRIPTION.toString())));
    }
}
