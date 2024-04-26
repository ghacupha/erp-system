package io.github.erp.web.rest;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CollateralType;
import io.github.erp.repository.CollateralTypeRepository;
import io.github.erp.repository.search.CollateralTypeSearchRepository;
import io.github.erp.service.criteria.CollateralTypeCriteria;
import io.github.erp.service.dto.CollateralTypeDTO;
import io.github.erp.service.mapper.CollateralTypeMapper;
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
 * Integration tests for the {@link CollateralTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CollateralTypeResourceIT {

    private static final String DEFAULT_COLLATERAL_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COLLATERAL_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COLLATERAL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COLLATERAL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLLATERAL_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COLLATERAL_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/collateral-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/collateral-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollateralTypeRepository collateralTypeRepository;

    @Autowired
    private CollateralTypeMapper collateralTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CollateralTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollateralTypeSearchRepository mockCollateralTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollateralTypeMockMvc;

    private CollateralType collateralType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollateralType createEntity(EntityManager em) {
        CollateralType collateralType = new CollateralType()
            .collateralTypeCode(DEFAULT_COLLATERAL_TYPE_CODE)
            .collateralType(DEFAULT_COLLATERAL_TYPE)
            .collateralTypeDescription(DEFAULT_COLLATERAL_TYPE_DESCRIPTION);
        return collateralType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollateralType createUpdatedEntity(EntityManager em) {
        CollateralType collateralType = new CollateralType()
            .collateralTypeCode(UPDATED_COLLATERAL_TYPE_CODE)
            .collateralType(UPDATED_COLLATERAL_TYPE)
            .collateralTypeDescription(UPDATED_COLLATERAL_TYPE_DESCRIPTION);
        return collateralType;
    }

    @BeforeEach
    public void initTest() {
        collateralType = createEntity(em);
    }

    @Test
    @Transactional
    void createCollateralType() throws Exception {
        int databaseSizeBeforeCreate = collateralTypeRepository.findAll().size();
        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);
        restCollateralTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CollateralType testCollateralType = collateralTypeList.get(collateralTypeList.size() - 1);
        assertThat(testCollateralType.getCollateralTypeCode()).isEqualTo(DEFAULT_COLLATERAL_TYPE_CODE);
        assertThat(testCollateralType.getCollateralType()).isEqualTo(DEFAULT_COLLATERAL_TYPE);
        assertThat(testCollateralType.getCollateralTypeDescription()).isEqualTo(DEFAULT_COLLATERAL_TYPE_DESCRIPTION);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(1)).save(testCollateralType);
    }

    @Test
    @Transactional
    void createCollateralTypeWithExistingId() throws Exception {
        // Create the CollateralType with an existing ID
        collateralType.setId(1L);
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        int databaseSizeBeforeCreate = collateralTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollateralTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void checkCollateralTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralTypeRepository.findAll().size();
        // set the field null
        collateralType.setCollateralTypeCode(null);

        // Create the CollateralType, which fails.
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        restCollateralTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCollateralTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralTypeRepository.findAll().size();
        // set the field null
        collateralType.setCollateralType(null);

        // Create the CollateralType, which fails.
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        restCollateralTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollateralTypes() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralType.getId().intValue())))
            .andExpect(jsonPath("$.[*].collateralTypeCode").value(hasItem(DEFAULT_COLLATERAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].collateralType").value(hasItem(DEFAULT_COLLATERAL_TYPE)))
            .andExpect(jsonPath("$.[*].collateralTypeDescription").value(hasItem(DEFAULT_COLLATERAL_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCollateralType() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get the collateralType
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, collateralType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collateralType.getId().intValue()))
            .andExpect(jsonPath("$.collateralTypeCode").value(DEFAULT_COLLATERAL_TYPE_CODE))
            .andExpect(jsonPath("$.collateralType").value(DEFAULT_COLLATERAL_TYPE))
            .andExpect(jsonPath("$.collateralTypeDescription").value(DEFAULT_COLLATERAL_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCollateralTypesByIdFiltering() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        Long id = collateralType.getId();

        defaultCollateralTypeShouldBeFound("id.equals=" + id);
        defaultCollateralTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCollateralTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollateralTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCollateralTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollateralTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode equals to DEFAULT_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldBeFound("collateralTypeCode.equals=" + DEFAULT_COLLATERAL_TYPE_CODE);

        // Get all the collateralTypeList where collateralTypeCode equals to UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.equals=" + UPDATED_COLLATERAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode not equals to DEFAULT_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.notEquals=" + DEFAULT_COLLATERAL_TYPE_CODE);

        // Get all the collateralTypeList where collateralTypeCode not equals to UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldBeFound("collateralTypeCode.notEquals=" + UPDATED_COLLATERAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode in DEFAULT_COLLATERAL_TYPE_CODE or UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldBeFound("collateralTypeCode.in=" + DEFAULT_COLLATERAL_TYPE_CODE + "," + UPDATED_COLLATERAL_TYPE_CODE);

        // Get all the collateralTypeList where collateralTypeCode equals to UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.in=" + UPDATED_COLLATERAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode is not null
        defaultCollateralTypeShouldBeFound("collateralTypeCode.specified=true");

        // Get all the collateralTypeList where collateralTypeCode is null
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode contains DEFAULT_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldBeFound("collateralTypeCode.contains=" + DEFAULT_COLLATERAL_TYPE_CODE);

        // Get all the collateralTypeList where collateralTypeCode contains UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.contains=" + UPDATED_COLLATERAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralTypeCode does not contain DEFAULT_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldNotBeFound("collateralTypeCode.doesNotContain=" + DEFAULT_COLLATERAL_TYPE_CODE);

        // Get all the collateralTypeList where collateralTypeCode does not contain UPDATED_COLLATERAL_TYPE_CODE
        defaultCollateralTypeShouldBeFound("collateralTypeCode.doesNotContain=" + UPDATED_COLLATERAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType equals to DEFAULT_COLLATERAL_TYPE
        defaultCollateralTypeShouldBeFound("collateralType.equals=" + DEFAULT_COLLATERAL_TYPE);

        // Get all the collateralTypeList where collateralType equals to UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldNotBeFound("collateralType.equals=" + UPDATED_COLLATERAL_TYPE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType not equals to DEFAULT_COLLATERAL_TYPE
        defaultCollateralTypeShouldNotBeFound("collateralType.notEquals=" + DEFAULT_COLLATERAL_TYPE);

        // Get all the collateralTypeList where collateralType not equals to UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldBeFound("collateralType.notEquals=" + UPDATED_COLLATERAL_TYPE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeIsInShouldWork() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType in DEFAULT_COLLATERAL_TYPE or UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldBeFound("collateralType.in=" + DEFAULT_COLLATERAL_TYPE + "," + UPDATED_COLLATERAL_TYPE);

        // Get all the collateralTypeList where collateralType equals to UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldNotBeFound("collateralType.in=" + UPDATED_COLLATERAL_TYPE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType is not null
        defaultCollateralTypeShouldBeFound("collateralType.specified=true");

        // Get all the collateralTypeList where collateralType is null
        defaultCollateralTypeShouldNotBeFound("collateralType.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeContainsSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType contains DEFAULT_COLLATERAL_TYPE
        defaultCollateralTypeShouldBeFound("collateralType.contains=" + DEFAULT_COLLATERAL_TYPE);

        // Get all the collateralTypeList where collateralType contains UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldNotBeFound("collateralType.contains=" + UPDATED_COLLATERAL_TYPE);
    }

    @Test
    @Transactional
    void getAllCollateralTypesByCollateralTypeNotContainsSomething() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        // Get all the collateralTypeList where collateralType does not contain DEFAULT_COLLATERAL_TYPE
        defaultCollateralTypeShouldNotBeFound("collateralType.doesNotContain=" + DEFAULT_COLLATERAL_TYPE);

        // Get all the collateralTypeList where collateralType does not contain UPDATED_COLLATERAL_TYPE
        defaultCollateralTypeShouldBeFound("collateralType.doesNotContain=" + UPDATED_COLLATERAL_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollateralTypeShouldBeFound(String filter) throws Exception {
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralType.getId().intValue())))
            .andExpect(jsonPath("$.[*].collateralTypeCode").value(hasItem(DEFAULT_COLLATERAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].collateralType").value(hasItem(DEFAULT_COLLATERAL_TYPE)))
            .andExpect(jsonPath("$.[*].collateralTypeDescription").value(hasItem(DEFAULT_COLLATERAL_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollateralTypeShouldNotBeFound(String filter) throws Exception {
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollateralTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollateralType() throws Exception {
        // Get the collateralType
        restCollateralTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollateralType() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();

        // Update the collateralType
        CollateralType updatedCollateralType = collateralTypeRepository.findById(collateralType.getId()).get();
        // Disconnect from session so that the updates on updatedCollateralType are not directly saved in db
        em.detach(updatedCollateralType);
        updatedCollateralType
            .collateralTypeCode(UPDATED_COLLATERAL_TYPE_CODE)
            .collateralType(UPDATED_COLLATERAL_TYPE)
            .collateralTypeDescription(UPDATED_COLLATERAL_TYPE_DESCRIPTION);
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(updatedCollateralType);

        restCollateralTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collateralTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);
        CollateralType testCollateralType = collateralTypeList.get(collateralTypeList.size() - 1);
        assertThat(testCollateralType.getCollateralTypeCode()).isEqualTo(UPDATED_COLLATERAL_TYPE_CODE);
        assertThat(testCollateralType.getCollateralType()).isEqualTo(UPDATED_COLLATERAL_TYPE);
        assertThat(testCollateralType.getCollateralTypeDescription()).isEqualTo(UPDATED_COLLATERAL_TYPE_DESCRIPTION);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository).save(testCollateralType);
    }

    @Test
    @Transactional
    void putNonExistingCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collateralTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void partialUpdateCollateralTypeWithPatch() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();

        // Update the collateralType using partial update
        CollateralType partialUpdatedCollateralType = new CollateralType();
        partialUpdatedCollateralType.setId(collateralType.getId());

        partialUpdatedCollateralType.collateralTypeCode(UPDATED_COLLATERAL_TYPE_CODE);

        restCollateralTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollateralType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollateralType))
            )
            .andExpect(status().isOk());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);
        CollateralType testCollateralType = collateralTypeList.get(collateralTypeList.size() - 1);
        assertThat(testCollateralType.getCollateralTypeCode()).isEqualTo(UPDATED_COLLATERAL_TYPE_CODE);
        assertThat(testCollateralType.getCollateralType()).isEqualTo(DEFAULT_COLLATERAL_TYPE);
        assertThat(testCollateralType.getCollateralTypeDescription()).isEqualTo(DEFAULT_COLLATERAL_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCollateralTypeWithPatch() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();

        // Update the collateralType using partial update
        CollateralType partialUpdatedCollateralType = new CollateralType();
        partialUpdatedCollateralType.setId(collateralType.getId());

        partialUpdatedCollateralType
            .collateralTypeCode(UPDATED_COLLATERAL_TYPE_CODE)
            .collateralType(UPDATED_COLLATERAL_TYPE)
            .collateralTypeDescription(UPDATED_COLLATERAL_TYPE_DESCRIPTION);

        restCollateralTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollateralType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollateralType))
            )
            .andExpect(status().isOk());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);
        CollateralType testCollateralType = collateralTypeList.get(collateralTypeList.size() - 1);
        assertThat(testCollateralType.getCollateralTypeCode()).isEqualTo(UPDATED_COLLATERAL_TYPE_CODE);
        assertThat(testCollateralType.getCollateralType()).isEqualTo(UPDATED_COLLATERAL_TYPE);
        assertThat(testCollateralType.getCollateralTypeDescription()).isEqualTo(UPDATED_COLLATERAL_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collateralTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollateralType() throws Exception {
        int databaseSizeBeforeUpdate = collateralTypeRepository.findAll().size();
        collateralType.setId(count.incrementAndGet());

        // Create the CollateralType
        CollateralTypeDTO collateralTypeDTO = collateralTypeMapper.toDto(collateralType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollateralType in the database
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(0)).save(collateralType);
    }

    @Test
    @Transactional
    void deleteCollateralType() throws Exception {
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);

        int databaseSizeBeforeDelete = collateralTypeRepository.findAll().size();

        // Delete the collateralType
        restCollateralTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, collateralType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollateralType> collateralTypeList = collateralTypeRepository.findAll();
        assertThat(collateralTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CollateralType in Elasticsearch
        verify(mockCollateralTypeSearchRepository, times(1)).deleteById(collateralType.getId());
    }

    @Test
    @Transactional
    void searchCollateralType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        collateralTypeRepository.saveAndFlush(collateralType);
        when(mockCollateralTypeSearchRepository.search("id:" + collateralType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(collateralType), PageRequest.of(0, 1), 1));

        // Search the collateralType
        restCollateralTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + collateralType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralType.getId().intValue())))
            .andExpect(jsonPath("$.[*].collateralTypeCode").value(hasItem(DEFAULT_COLLATERAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].collateralType").value(hasItem(DEFAULT_COLLATERAL_TYPE)))
            .andExpect(jsonPath("$.[*].collateralTypeDescription").value(hasItem(DEFAULT_COLLATERAL_TYPE_DESCRIPTION.toString())));
    }
}
