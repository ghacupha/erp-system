package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.CommitteeType;
import io.github.erp.repository.CommitteeTypeRepository;
import io.github.erp.repository.search.CommitteeTypeSearchRepository;
import io.github.erp.service.criteria.CommitteeTypeCriteria;
import io.github.erp.service.dto.CommitteeTypeDTO;
import io.github.erp.service.mapper.CommitteeTypeMapper;
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
 * Integration tests for the {@link CommitteeTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommitteeTypeResourceIT {

    private static final String DEFAULT_COMMITTEE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COMMITTEE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMITTEE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMMITTEE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMITTEE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_COMMITTEE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/committee-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/committee-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommitteeTypeRepository committeeTypeRepository;

    @Autowired
    private CommitteeTypeMapper committeeTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CommitteeTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommitteeTypeSearchRepository mockCommitteeTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeTypeMockMvc;

    private CommitteeType committeeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeType createEntity(EntityManager em) {
        CommitteeType committeeType = new CommitteeType()
            .committeeTypeCode(DEFAULT_COMMITTEE_TYPE_CODE)
            .committeeType(DEFAULT_COMMITTEE_TYPE)
            .committeeTypeDetails(DEFAULT_COMMITTEE_TYPE_DETAILS);
        return committeeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeType createUpdatedEntity(EntityManager em) {
        CommitteeType committeeType = new CommitteeType()
            .committeeTypeCode(UPDATED_COMMITTEE_TYPE_CODE)
            .committeeType(UPDATED_COMMITTEE_TYPE)
            .committeeTypeDetails(UPDATED_COMMITTEE_TYPE_DETAILS);
        return committeeType;
    }

    @BeforeEach
    public void initTest() {
        committeeType = createEntity(em);
    }

    @Test
    @Transactional
    void createCommitteeType() throws Exception {
        int databaseSizeBeforeCreate = committeeTypeRepository.findAll().size();
        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);
        restCommitteeTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeType testCommitteeType = committeeTypeList.get(committeeTypeList.size() - 1);
        assertThat(testCommitteeType.getCommitteeTypeCode()).isEqualTo(DEFAULT_COMMITTEE_TYPE_CODE);
        assertThat(testCommitteeType.getCommitteeType()).isEqualTo(DEFAULT_COMMITTEE_TYPE);
        assertThat(testCommitteeType.getCommitteeTypeDetails()).isEqualTo(DEFAULT_COMMITTEE_TYPE_DETAILS);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(1)).save(testCommitteeType);
    }

    @Test
    @Transactional
    void createCommitteeTypeWithExistingId() throws Exception {
        // Create the CommitteeType with an existing ID
        committeeType.setId(1L);
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        int databaseSizeBeforeCreate = committeeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void checkCommitteeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = committeeTypeRepository.findAll().size();
        // set the field null
        committeeType.setCommitteeTypeCode(null);

        // Create the CommitteeType, which fails.
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        restCommitteeTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommitteeTypes() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].committeeTypeCode").value(hasItem(DEFAULT_COMMITTEE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].committeeType").value(hasItem(DEFAULT_COMMITTEE_TYPE)))
            .andExpect(jsonPath("$.[*].committeeTypeDetails").value(hasItem(DEFAULT_COMMITTEE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCommitteeType() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get the committeeType
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, committeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeType.getId().intValue()))
            .andExpect(jsonPath("$.committeeTypeCode").value(DEFAULT_COMMITTEE_TYPE_CODE))
            .andExpect(jsonPath("$.committeeType").value(DEFAULT_COMMITTEE_TYPE))
            .andExpect(jsonPath("$.committeeTypeDetails").value(DEFAULT_COMMITTEE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCommitteeTypesByIdFiltering() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        Long id = committeeType.getId();

        defaultCommitteeTypeShouldBeFound("id.equals=" + id);
        defaultCommitteeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCommitteeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommitteeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCommitteeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommitteeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode equals to DEFAULT_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.equals=" + DEFAULT_COMMITTEE_TYPE_CODE);

        // Get all the committeeTypeList where committeeTypeCode equals to UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.equals=" + UPDATED_COMMITTEE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode not equals to DEFAULT_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.notEquals=" + DEFAULT_COMMITTEE_TYPE_CODE);

        // Get all the committeeTypeList where committeeTypeCode not equals to UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.notEquals=" + UPDATED_COMMITTEE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode in DEFAULT_COMMITTEE_TYPE_CODE or UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.in=" + DEFAULT_COMMITTEE_TYPE_CODE + "," + UPDATED_COMMITTEE_TYPE_CODE);

        // Get all the committeeTypeList where committeeTypeCode equals to UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.in=" + UPDATED_COMMITTEE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode is not null
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.specified=true");

        // Get all the committeeTypeList where committeeTypeCode is null
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode contains DEFAULT_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.contains=" + DEFAULT_COMMITTEE_TYPE_CODE);

        // Get all the committeeTypeList where committeeTypeCode contains UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.contains=" + UPDATED_COMMITTEE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeTypeCode does not contain DEFAULT_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldNotBeFound("committeeTypeCode.doesNotContain=" + DEFAULT_COMMITTEE_TYPE_CODE);

        // Get all the committeeTypeList where committeeTypeCode does not contain UPDATED_COMMITTEE_TYPE_CODE
        defaultCommitteeTypeShouldBeFound("committeeTypeCode.doesNotContain=" + UPDATED_COMMITTEE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType equals to DEFAULT_COMMITTEE_TYPE
        defaultCommitteeTypeShouldBeFound("committeeType.equals=" + DEFAULT_COMMITTEE_TYPE);

        // Get all the committeeTypeList where committeeType equals to UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldNotBeFound("committeeType.equals=" + UPDATED_COMMITTEE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType not equals to DEFAULT_COMMITTEE_TYPE
        defaultCommitteeTypeShouldNotBeFound("committeeType.notEquals=" + DEFAULT_COMMITTEE_TYPE);

        // Get all the committeeTypeList where committeeType not equals to UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldBeFound("committeeType.notEquals=" + UPDATED_COMMITTEE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType in DEFAULT_COMMITTEE_TYPE or UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldBeFound("committeeType.in=" + DEFAULT_COMMITTEE_TYPE + "," + UPDATED_COMMITTEE_TYPE);

        // Get all the committeeTypeList where committeeType equals to UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldNotBeFound("committeeType.in=" + UPDATED_COMMITTEE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType is not null
        defaultCommitteeTypeShouldBeFound("committeeType.specified=true");

        // Get all the committeeTypeList where committeeType is null
        defaultCommitteeTypeShouldNotBeFound("committeeType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeContainsSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType contains DEFAULT_COMMITTEE_TYPE
        defaultCommitteeTypeShouldBeFound("committeeType.contains=" + DEFAULT_COMMITTEE_TYPE);

        // Get all the committeeTypeList where committeeType contains UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldNotBeFound("committeeType.contains=" + UPDATED_COMMITTEE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommitteeTypesByCommitteeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        // Get all the committeeTypeList where committeeType does not contain DEFAULT_COMMITTEE_TYPE
        defaultCommitteeTypeShouldNotBeFound("committeeType.doesNotContain=" + DEFAULT_COMMITTEE_TYPE);

        // Get all the committeeTypeList where committeeType does not contain UPDATED_COMMITTEE_TYPE
        defaultCommitteeTypeShouldBeFound("committeeType.doesNotContain=" + UPDATED_COMMITTEE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommitteeTypeShouldBeFound(String filter) throws Exception {
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].committeeTypeCode").value(hasItem(DEFAULT_COMMITTEE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].committeeType").value(hasItem(DEFAULT_COMMITTEE_TYPE)))
            .andExpect(jsonPath("$.[*].committeeTypeDetails").value(hasItem(DEFAULT_COMMITTEE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommitteeTypeShouldNotBeFound(String filter) throws Exception {
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommitteeType() throws Exception {
        // Get the committeeType
        restCommitteeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommitteeType() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();

        // Update the committeeType
        CommitteeType updatedCommitteeType = committeeTypeRepository.findById(committeeType.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeType are not directly saved in db
        em.detach(updatedCommitteeType);
        updatedCommitteeType
            .committeeTypeCode(UPDATED_COMMITTEE_TYPE_CODE)
            .committeeType(UPDATED_COMMITTEE_TYPE)
            .committeeTypeDetails(UPDATED_COMMITTEE_TYPE_DETAILS);
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(updatedCommitteeType);

        restCommitteeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, committeeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CommitteeType testCommitteeType = committeeTypeList.get(committeeTypeList.size() - 1);
        assertThat(testCommitteeType.getCommitteeTypeCode()).isEqualTo(UPDATED_COMMITTEE_TYPE_CODE);
        assertThat(testCommitteeType.getCommitteeType()).isEqualTo(UPDATED_COMMITTEE_TYPE);
        assertThat(testCommitteeType.getCommitteeTypeDetails()).isEqualTo(UPDATED_COMMITTEE_TYPE_DETAILS);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository).save(testCommitteeType);
    }

    @Test
    @Transactional
    void putNonExistingCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, committeeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void partialUpdateCommitteeTypeWithPatch() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();

        // Update the committeeType using partial update
        CommitteeType partialUpdatedCommitteeType = new CommitteeType();
        partialUpdatedCommitteeType.setId(committeeType.getId());

        partialUpdatedCommitteeType.committeeType(UPDATED_COMMITTEE_TYPE);

        restCommitteeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommitteeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommitteeType))
            )
            .andExpect(status().isOk());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CommitteeType testCommitteeType = committeeTypeList.get(committeeTypeList.size() - 1);
        assertThat(testCommitteeType.getCommitteeTypeCode()).isEqualTo(DEFAULT_COMMITTEE_TYPE_CODE);
        assertThat(testCommitteeType.getCommitteeType()).isEqualTo(UPDATED_COMMITTEE_TYPE);
        assertThat(testCommitteeType.getCommitteeTypeDetails()).isEqualTo(DEFAULT_COMMITTEE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCommitteeTypeWithPatch() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();

        // Update the committeeType using partial update
        CommitteeType partialUpdatedCommitteeType = new CommitteeType();
        partialUpdatedCommitteeType.setId(committeeType.getId());

        partialUpdatedCommitteeType
            .committeeTypeCode(UPDATED_COMMITTEE_TYPE_CODE)
            .committeeType(UPDATED_COMMITTEE_TYPE)
            .committeeTypeDetails(UPDATED_COMMITTEE_TYPE_DETAILS);

        restCommitteeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommitteeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommitteeType))
            )
            .andExpect(status().isOk());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CommitteeType testCommitteeType = committeeTypeList.get(committeeTypeList.size() - 1);
        assertThat(testCommitteeType.getCommitteeTypeCode()).isEqualTo(UPDATED_COMMITTEE_TYPE_CODE);
        assertThat(testCommitteeType.getCommitteeType()).isEqualTo(UPDATED_COMMITTEE_TYPE);
        assertThat(testCommitteeType.getCommitteeTypeDetails()).isEqualTo(UPDATED_COMMITTEE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, committeeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommitteeType() throws Exception {
        int databaseSizeBeforeUpdate = committeeTypeRepository.findAll().size();
        committeeType.setId(count.incrementAndGet());

        // Create the CommitteeType
        CommitteeTypeDTO committeeTypeDTO = committeeTypeMapper.toDto(committeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitteeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(committeeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommitteeType in the database
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(0)).save(committeeType);
    }

    @Test
    @Transactional
    void deleteCommitteeType() throws Exception {
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);

        int databaseSizeBeforeDelete = committeeTypeRepository.findAll().size();

        // Delete the committeeType
        restCommitteeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, committeeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeType> committeeTypeList = committeeTypeRepository.findAll();
        assertThat(committeeTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommitteeType in Elasticsearch
        verify(mockCommitteeTypeSearchRepository, times(1)).deleteById(committeeType.getId());
    }

    @Test
    @Transactional
    void searchCommitteeType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        committeeTypeRepository.saveAndFlush(committeeType);
        when(mockCommitteeTypeSearchRepository.search("id:" + committeeType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(committeeType), PageRequest.of(0, 1), 1));

        // Search the committeeType
        restCommitteeTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + committeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].committeeTypeCode").value(hasItem(DEFAULT_COMMITTEE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].committeeType").value(hasItem(DEFAULT_COMMITTEE_TYPE)))
            .andExpect(jsonPath("$.[*].committeeTypeDetails").value(hasItem(DEFAULT_COMMITTEE_TYPE_DETAILS.toString())));
    }
}
