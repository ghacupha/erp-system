package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.FraudType;
import io.github.erp.repository.FraudTypeRepository;
import io.github.erp.repository.search.FraudTypeSearchRepository;
import io.github.erp.service.criteria.FraudTypeCriteria;
import io.github.erp.service.dto.FraudTypeDTO;
import io.github.erp.service.mapper.FraudTypeMapper;
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
 * Integration tests for the {@link FraudTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FraudTypeResourceIT {

    private static final String DEFAULT_FRAUD_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FRAUD_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FRAUD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FRAUD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FRAUD_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FRAUD_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fraud-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fraud-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraudTypeRepository fraudTypeRepository;

    @Autowired
    private FraudTypeMapper fraudTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FraudTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FraudTypeSearchRepository mockFraudTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraudTypeMockMvc;

    private FraudType fraudType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudType createEntity(EntityManager em) {
        FraudType fraudType = new FraudType()
            .fraudTypeCode(DEFAULT_FRAUD_TYPE_CODE)
            .fraudType(DEFAULT_FRAUD_TYPE)
            .fraudTypeDetails(DEFAULT_FRAUD_TYPE_DETAILS);
        return fraudType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudType createUpdatedEntity(EntityManager em) {
        FraudType fraudType = new FraudType()
            .fraudTypeCode(UPDATED_FRAUD_TYPE_CODE)
            .fraudType(UPDATED_FRAUD_TYPE)
            .fraudTypeDetails(UPDATED_FRAUD_TYPE_DETAILS);
        return fraudType;
    }

    @BeforeEach
    public void initTest() {
        fraudType = createEntity(em);
    }

    @Test
    @Transactional
    void createFraudType() throws Exception {
        int databaseSizeBeforeCreate = fraudTypeRepository.findAll().size();
        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);
        restFraudTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FraudType testFraudType = fraudTypeList.get(fraudTypeList.size() - 1);
        assertThat(testFraudType.getFraudTypeCode()).isEqualTo(DEFAULT_FRAUD_TYPE_CODE);
        assertThat(testFraudType.getFraudType()).isEqualTo(DEFAULT_FRAUD_TYPE);
        assertThat(testFraudType.getFraudTypeDetails()).isEqualTo(DEFAULT_FRAUD_TYPE_DETAILS);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(1)).save(testFraudType);
    }

    @Test
    @Transactional
    void createFraudTypeWithExistingId() throws Exception {
        // Create the FraudType with an existing ID
        fraudType.setId(1L);
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        int databaseSizeBeforeCreate = fraudTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraudTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void checkFraudTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraudTypeRepository.findAll().size();
        // set the field null
        fraudType.setFraudTypeCode(null);

        // Create the FraudType, which fails.
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        restFraudTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFraudTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraudTypeRepository.findAll().size();
        // set the field null
        fraudType.setFraudType(null);

        // Create the FraudType, which fails.
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        restFraudTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFraudTypes() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fraudTypeCode").value(hasItem(DEFAULT_FRAUD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fraudType").value(hasItem(DEFAULT_FRAUD_TYPE)))
            .andExpect(jsonPath("$.[*].fraudTypeDetails").value(hasItem(DEFAULT_FRAUD_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getFraudType() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get the fraudType
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fraudType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fraudType.getId().intValue()))
            .andExpect(jsonPath("$.fraudTypeCode").value(DEFAULT_FRAUD_TYPE_CODE))
            .andExpect(jsonPath("$.fraudType").value(DEFAULT_FRAUD_TYPE))
            .andExpect(jsonPath("$.fraudTypeDetails").value(DEFAULT_FRAUD_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getFraudTypesByIdFiltering() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        Long id = fraudType.getId();

        defaultFraudTypeShouldBeFound("id.equals=" + id);
        defaultFraudTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFraudTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFraudTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFraudTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFraudTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode equals to DEFAULT_FRAUD_TYPE_CODE
        defaultFraudTypeShouldBeFound("fraudTypeCode.equals=" + DEFAULT_FRAUD_TYPE_CODE);

        // Get all the fraudTypeList where fraudTypeCode equals to UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.equals=" + UPDATED_FRAUD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode not equals to DEFAULT_FRAUD_TYPE_CODE
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.notEquals=" + DEFAULT_FRAUD_TYPE_CODE);

        // Get all the fraudTypeList where fraudTypeCode not equals to UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldBeFound("fraudTypeCode.notEquals=" + UPDATED_FRAUD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode in DEFAULT_FRAUD_TYPE_CODE or UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldBeFound("fraudTypeCode.in=" + DEFAULT_FRAUD_TYPE_CODE + "," + UPDATED_FRAUD_TYPE_CODE);

        // Get all the fraudTypeList where fraudTypeCode equals to UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.in=" + UPDATED_FRAUD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode is not null
        defaultFraudTypeShouldBeFound("fraudTypeCode.specified=true");

        // Get all the fraudTypeList where fraudTypeCode is null
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode contains DEFAULT_FRAUD_TYPE_CODE
        defaultFraudTypeShouldBeFound("fraudTypeCode.contains=" + DEFAULT_FRAUD_TYPE_CODE);

        // Get all the fraudTypeList where fraudTypeCode contains UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.contains=" + UPDATED_FRAUD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudTypeCode does not contain DEFAULT_FRAUD_TYPE_CODE
        defaultFraudTypeShouldNotBeFound("fraudTypeCode.doesNotContain=" + DEFAULT_FRAUD_TYPE_CODE);

        // Get all the fraudTypeList where fraudTypeCode does not contain UPDATED_FRAUD_TYPE_CODE
        defaultFraudTypeShouldBeFound("fraudTypeCode.doesNotContain=" + UPDATED_FRAUD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType equals to DEFAULT_FRAUD_TYPE
        defaultFraudTypeShouldBeFound("fraudType.equals=" + DEFAULT_FRAUD_TYPE);

        // Get all the fraudTypeList where fraudType equals to UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldNotBeFound("fraudType.equals=" + UPDATED_FRAUD_TYPE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType not equals to DEFAULT_FRAUD_TYPE
        defaultFraudTypeShouldNotBeFound("fraudType.notEquals=" + DEFAULT_FRAUD_TYPE);

        // Get all the fraudTypeList where fraudType not equals to UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldBeFound("fraudType.notEquals=" + UPDATED_FRAUD_TYPE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType in DEFAULT_FRAUD_TYPE or UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldBeFound("fraudType.in=" + DEFAULT_FRAUD_TYPE + "," + UPDATED_FRAUD_TYPE);

        // Get all the fraudTypeList where fraudType equals to UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldNotBeFound("fraudType.in=" + UPDATED_FRAUD_TYPE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType is not null
        defaultFraudTypeShouldBeFound("fraudType.specified=true");

        // Get all the fraudTypeList where fraudType is null
        defaultFraudTypeShouldNotBeFound("fraudType.specified=false");
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeContainsSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType contains DEFAULT_FRAUD_TYPE
        defaultFraudTypeShouldBeFound("fraudType.contains=" + DEFAULT_FRAUD_TYPE);

        // Get all the fraudTypeList where fraudType contains UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldNotBeFound("fraudType.contains=" + UPDATED_FRAUD_TYPE);
    }

    @Test
    @Transactional
    void getAllFraudTypesByFraudTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        // Get all the fraudTypeList where fraudType does not contain DEFAULT_FRAUD_TYPE
        defaultFraudTypeShouldNotBeFound("fraudType.doesNotContain=" + DEFAULT_FRAUD_TYPE);

        // Get all the fraudTypeList where fraudType does not contain UPDATED_FRAUD_TYPE
        defaultFraudTypeShouldBeFound("fraudType.doesNotContain=" + UPDATED_FRAUD_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFraudTypeShouldBeFound(String filter) throws Exception {
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fraudTypeCode").value(hasItem(DEFAULT_FRAUD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fraudType").value(hasItem(DEFAULT_FRAUD_TYPE)))
            .andExpect(jsonPath("$.[*].fraudTypeDetails").value(hasItem(DEFAULT_FRAUD_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFraudTypeShouldNotBeFound(String filter) throws Exception {
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFraudTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFraudType() throws Exception {
        // Get the fraudType
        restFraudTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFraudType() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();

        // Update the fraudType
        FraudType updatedFraudType = fraudTypeRepository.findById(fraudType.getId()).get();
        // Disconnect from session so that the updates on updatedFraudType are not directly saved in db
        em.detach(updatedFraudType);
        updatedFraudType.fraudTypeCode(UPDATED_FRAUD_TYPE_CODE).fraudType(UPDATED_FRAUD_TYPE).fraudTypeDetails(UPDATED_FRAUD_TYPE_DETAILS);
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(updatedFraudType);

        restFraudTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);
        FraudType testFraudType = fraudTypeList.get(fraudTypeList.size() - 1);
        assertThat(testFraudType.getFraudTypeCode()).isEqualTo(UPDATED_FRAUD_TYPE_CODE);
        assertThat(testFraudType.getFraudType()).isEqualTo(UPDATED_FRAUD_TYPE);
        assertThat(testFraudType.getFraudTypeDetails()).isEqualTo(UPDATED_FRAUD_TYPE_DETAILS);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository).save(testFraudType);
    }

    @Test
    @Transactional
    void putNonExistingFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void partialUpdateFraudTypeWithPatch() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();

        // Update the fraudType using partial update
        FraudType partialUpdatedFraudType = new FraudType();
        partialUpdatedFraudType.setId(fraudType.getId());

        restFraudTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraudType))
            )
            .andExpect(status().isOk());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);
        FraudType testFraudType = fraudTypeList.get(fraudTypeList.size() - 1);
        assertThat(testFraudType.getFraudTypeCode()).isEqualTo(DEFAULT_FRAUD_TYPE_CODE);
        assertThat(testFraudType.getFraudType()).isEqualTo(DEFAULT_FRAUD_TYPE);
        assertThat(testFraudType.getFraudTypeDetails()).isEqualTo(DEFAULT_FRAUD_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFraudTypeWithPatch() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();

        // Update the fraudType using partial update
        FraudType partialUpdatedFraudType = new FraudType();
        partialUpdatedFraudType.setId(fraudType.getId());

        partialUpdatedFraudType
            .fraudTypeCode(UPDATED_FRAUD_TYPE_CODE)
            .fraudType(UPDATED_FRAUD_TYPE)
            .fraudTypeDetails(UPDATED_FRAUD_TYPE_DETAILS);

        restFraudTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraudType))
            )
            .andExpect(status().isOk());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);
        FraudType testFraudType = fraudTypeList.get(fraudTypeList.size() - 1);
        assertThat(testFraudType.getFraudTypeCode()).isEqualTo(UPDATED_FRAUD_TYPE_CODE);
        assertThat(testFraudType.getFraudType()).isEqualTo(UPDATED_FRAUD_TYPE);
        assertThat(testFraudType.getFraudTypeDetails()).isEqualTo(UPDATED_FRAUD_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraudTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFraudType() throws Exception {
        int databaseSizeBeforeUpdate = fraudTypeRepository.findAll().size();
        fraudType.setId(count.incrementAndGet());

        // Create the FraudType
        FraudTypeDTO fraudTypeDTO = fraudTypeMapper.toDto(fraudType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fraudTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudType in the database
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(0)).save(fraudType);
    }

    @Test
    @Transactional
    void deleteFraudType() throws Exception {
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);

        int databaseSizeBeforeDelete = fraudTypeRepository.findAll().size();

        // Delete the fraudType
        restFraudTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fraudType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FraudType> fraudTypeList = fraudTypeRepository.findAll();
        assertThat(fraudTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FraudType in Elasticsearch
        verify(mockFraudTypeSearchRepository, times(1)).deleteById(fraudType.getId());
    }

    @Test
    @Transactional
    void searchFraudType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fraudTypeRepository.saveAndFlush(fraudType);
        when(mockFraudTypeSearchRepository.search("id:" + fraudType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fraudType), PageRequest.of(0, 1), 1));

        // Search the fraudType
        restFraudTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fraudType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fraudTypeCode").value(hasItem(DEFAULT_FRAUD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fraudType").value(hasItem(DEFAULT_FRAUD_TYPE)))
            .andExpect(jsonPath("$.[*].fraudTypeDetails").value(hasItem(DEFAULT_FRAUD_TYPE_DETAILS.toString())));
    }
}
