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
import io.github.erp.domain.FxCustomerType;
import io.github.erp.repository.FxCustomerTypeRepository;
import io.github.erp.repository.search.FxCustomerTypeSearchRepository;
import io.github.erp.service.criteria.FxCustomerTypeCriteria;
import io.github.erp.service.dto.FxCustomerTypeDTO;
import io.github.erp.service.mapper.FxCustomerTypeMapper;
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

/**
 * Integration tests for the {@link FxCustomerTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxCustomerTypeResourceIT {

    private static final String DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FOREIGN_CUSTOMER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FOREIGN_CUSTOMER_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-customer-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-customer-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxCustomerTypeRepository fxCustomerTypeRepository;

    @Autowired
    private FxCustomerTypeMapper fxCustomerTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxCustomerTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxCustomerTypeSearchRepository mockFxCustomerTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxCustomerTypeMockMvc;

    private FxCustomerType fxCustomerType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxCustomerType createEntity(EntityManager em) {
        FxCustomerType fxCustomerType = new FxCustomerType()
            .foreignExchangeCustomerTypeCode(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)
            .foreignCustomerType(DEFAULT_FOREIGN_CUSTOMER_TYPE);
        return fxCustomerType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxCustomerType createUpdatedEntity(EntityManager em) {
        FxCustomerType fxCustomerType = new FxCustomerType()
            .foreignExchangeCustomerTypeCode(UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)
            .foreignCustomerType(UPDATED_FOREIGN_CUSTOMER_TYPE);
        return fxCustomerType;
    }

    @BeforeEach
    public void initTest() {
        fxCustomerType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxCustomerType() throws Exception {
        int databaseSizeBeforeCreate = fxCustomerTypeRepository.findAll().size();
        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);
        restFxCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxCustomerType testFxCustomerType = fxCustomerTypeList.get(fxCustomerTypeList.size() - 1);
        assertThat(testFxCustomerType.getForeignExchangeCustomerTypeCode()).isEqualTo(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
        assertThat(testFxCustomerType.getForeignCustomerType()).isEqualTo(DEFAULT_FOREIGN_CUSTOMER_TYPE);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(1)).save(testFxCustomerType);
    }

    @Test
    @Transactional
    void createFxCustomerTypeWithExistingId() throws Exception {
        // Create the FxCustomerType with an existing ID
        fxCustomerType.setId(1L);
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        int databaseSizeBeforeCreate = fxCustomerTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void checkForeignExchangeCustomerTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxCustomerTypeRepository.findAll().size();
        // set the field null
        fxCustomerType.setForeignExchangeCustomerTypeCode(null);

        // Create the FxCustomerType, which fails.
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        restFxCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkForeignCustomerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxCustomerTypeRepository.findAll().size();
        // set the field null
        fxCustomerType.setForeignCustomerType(null);

        // Create the FxCustomerType, which fails.
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        restFxCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypes() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].foreignExchangeCustomerTypeCode").value(hasItem(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].foreignCustomerType").value(hasItem(DEFAULT_FOREIGN_CUSTOMER_TYPE)));
    }

    @Test
    @Transactional
    void getFxCustomerType() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get the fxCustomerType
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxCustomerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxCustomerType.getId().intValue()))
            .andExpect(jsonPath("$.foreignExchangeCustomerTypeCode").value(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE))
            .andExpect(jsonPath("$.foreignCustomerType").value(DEFAULT_FOREIGN_CUSTOMER_TYPE));
    }

    @Test
    @Transactional
    void getFxCustomerTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        Long id = fxCustomerType.getId();

        defaultFxCustomerTypeShouldBeFound("id.equals=" + id);
        defaultFxCustomerTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxCustomerTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxCustomerTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxCustomerTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxCustomerTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode equals to DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldBeFound("foreignExchangeCustomerTypeCode.equals=" + DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode equals to UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldNotBeFound("foreignExchangeCustomerTypeCode.equals=" + UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode not equals to DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldNotBeFound("foreignExchangeCustomerTypeCode.notEquals=" + DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode not equals to UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldBeFound("foreignExchangeCustomerTypeCode.notEquals=" + UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode in DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE or UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldBeFound(
            "foreignExchangeCustomerTypeCode.in=" +
            DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE +
            "," +
            UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        );

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode equals to UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldNotBeFound("foreignExchangeCustomerTypeCode.in=" + UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode is not null
        defaultFxCustomerTypeShouldBeFound("foreignExchangeCustomerTypeCode.specified=true");

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode is null
        defaultFxCustomerTypeShouldNotBeFound("foreignExchangeCustomerTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode contains DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldBeFound("foreignExchangeCustomerTypeCode.contains=" + DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode contains UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldNotBeFound("foreignExchangeCustomerTypeCode.contains=" + UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignExchangeCustomerTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode does not contain DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldNotBeFound(
            "foreignExchangeCustomerTypeCode.doesNotContain=" + DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        );

        // Get all the fxCustomerTypeList where foreignExchangeCustomerTypeCode does not contain UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE
        defaultFxCustomerTypeShouldBeFound("foreignExchangeCustomerTypeCode.doesNotContain=" + UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType equals to DEFAULT_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.equals=" + DEFAULT_FOREIGN_CUSTOMER_TYPE);

        // Get all the fxCustomerTypeList where foreignCustomerType equals to UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.equals=" + UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType not equals to DEFAULT_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.notEquals=" + DEFAULT_FOREIGN_CUSTOMER_TYPE);

        // Get all the fxCustomerTypeList where foreignCustomerType not equals to UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.notEquals=" + UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType in DEFAULT_FOREIGN_CUSTOMER_TYPE or UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.in=" + DEFAULT_FOREIGN_CUSTOMER_TYPE + "," + UPDATED_FOREIGN_CUSTOMER_TYPE);

        // Get all the fxCustomerTypeList where foreignCustomerType equals to UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.in=" + UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType is not null
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.specified=true");

        // Get all the fxCustomerTypeList where foreignCustomerType is null
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeContainsSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType contains DEFAULT_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.contains=" + DEFAULT_FOREIGN_CUSTOMER_TYPE);

        // Get all the fxCustomerTypeList where foreignCustomerType contains UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.contains=" + UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllFxCustomerTypesByForeignCustomerTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        // Get all the fxCustomerTypeList where foreignCustomerType does not contain DEFAULT_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldNotBeFound("foreignCustomerType.doesNotContain=" + DEFAULT_FOREIGN_CUSTOMER_TYPE);

        // Get all the fxCustomerTypeList where foreignCustomerType does not contain UPDATED_FOREIGN_CUSTOMER_TYPE
        defaultFxCustomerTypeShouldBeFound("foreignCustomerType.doesNotContain=" + UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxCustomerTypeShouldBeFound(String filter) throws Exception {
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].foreignExchangeCustomerTypeCode").value(hasItem(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].foreignCustomerType").value(hasItem(DEFAULT_FOREIGN_CUSTOMER_TYPE)));

        // Check, that the count call also returns 1
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxCustomerTypeShouldNotBeFound(String filter) throws Exception {
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxCustomerType() throws Exception {
        // Get the fxCustomerType
        restFxCustomerTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxCustomerType() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();

        // Update the fxCustomerType
        FxCustomerType updatedFxCustomerType = fxCustomerTypeRepository.findById(fxCustomerType.getId()).get();
        // Disconnect from session so that the updates on updatedFxCustomerType are not directly saved in db
        em.detach(updatedFxCustomerType);
        updatedFxCustomerType
            .foreignExchangeCustomerTypeCode(UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)
            .foreignCustomerType(UPDATED_FOREIGN_CUSTOMER_TYPE);
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(updatedFxCustomerType);

        restFxCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxCustomerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        FxCustomerType testFxCustomerType = fxCustomerTypeList.get(fxCustomerTypeList.size() - 1);
        assertThat(testFxCustomerType.getForeignExchangeCustomerTypeCode()).isEqualTo(UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
        assertThat(testFxCustomerType.getForeignCustomerType()).isEqualTo(UPDATED_FOREIGN_CUSTOMER_TYPE);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository).save(testFxCustomerType);
    }

    @Test
    @Transactional
    void putNonExistingFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxCustomerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void partialUpdateFxCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();

        // Update the fxCustomerType using partial update
        FxCustomerType partialUpdatedFxCustomerType = new FxCustomerType();
        partialUpdatedFxCustomerType.setId(fxCustomerType.getId());

        restFxCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        FxCustomerType testFxCustomerType = fxCustomerTypeList.get(fxCustomerTypeList.size() - 1);
        assertThat(testFxCustomerType.getForeignExchangeCustomerTypeCode()).isEqualTo(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
        assertThat(testFxCustomerType.getForeignCustomerType()).isEqualTo(DEFAULT_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFxCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();

        // Update the fxCustomerType using partial update
        FxCustomerType partialUpdatedFxCustomerType = new FxCustomerType();
        partialUpdatedFxCustomerType.setId(fxCustomerType.getId());

        partialUpdatedFxCustomerType
            .foreignExchangeCustomerTypeCode(UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)
            .foreignCustomerType(UPDATED_FOREIGN_CUSTOMER_TYPE);

        restFxCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        FxCustomerType testFxCustomerType = fxCustomerTypeList.get(fxCustomerTypeList.size() - 1);
        assertThat(testFxCustomerType.getForeignExchangeCustomerTypeCode()).isEqualTo(UPDATED_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE);
        assertThat(testFxCustomerType.getForeignCustomerType()).isEqualTo(UPDATED_FOREIGN_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxCustomerTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = fxCustomerTypeRepository.findAll().size();
        fxCustomerType.setId(count.incrementAndGet());

        // Create the FxCustomerType
        FxCustomerTypeDTO fxCustomerTypeDTO = fxCustomerTypeMapper.toDto(fxCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxCustomerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxCustomerType in the database
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(0)).save(fxCustomerType);
    }

    @Test
    @Transactional
    void deleteFxCustomerType() throws Exception {
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);

        int databaseSizeBeforeDelete = fxCustomerTypeRepository.findAll().size();

        // Delete the fxCustomerType
        restFxCustomerTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxCustomerType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxCustomerType> fxCustomerTypeList = fxCustomerTypeRepository.findAll();
        assertThat(fxCustomerTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxCustomerType in Elasticsearch
        verify(mockFxCustomerTypeSearchRepository, times(1)).deleteById(fxCustomerType.getId());
    }

    @Test
    @Transactional
    void searchFxCustomerType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxCustomerTypeRepository.saveAndFlush(fxCustomerType);
        when(mockFxCustomerTypeSearchRepository.search("id:" + fxCustomerType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxCustomerType), PageRequest.of(0, 1), 1));

        // Search the fxCustomerType
        restFxCustomerTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxCustomerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].foreignExchangeCustomerTypeCode").value(hasItem(DEFAULT_FOREIGN_EXCHANGE_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].foreignCustomerType").value(hasItem(DEFAULT_FOREIGN_CUSTOMER_TYPE)));
    }
}
