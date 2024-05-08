package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.FxTransactionRateType;
import io.github.erp.repository.FxTransactionRateTypeRepository;
import io.github.erp.repository.search.FxTransactionRateTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionRateTypeCriteria;
import io.github.erp.service.dto.FxTransactionRateTypeDTO;
import io.github.erp.service.mapper.FxTransactionRateTypeMapper;
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
 * Integration tests for the {@link FxTransactionRateTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxTransactionRateTypeResourceIT {

    private static final String DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_RATE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_TRANSACTION_RATE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_RATE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-transaction-rate-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-transaction-rate-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxTransactionRateTypeRepository fxTransactionRateTypeRepository;

    @Autowired
    private FxTransactionRateTypeMapper fxTransactionRateTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxTransactionRateTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxTransactionRateTypeSearchRepository mockFxTransactionRateTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxTransactionRateTypeMockMvc;

    private FxTransactionRateType fxTransactionRateType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionRateType createEntity(EntityManager em) {
        FxTransactionRateType fxTransactionRateType = new FxTransactionRateType()
            .fxTransactionRateTypeCode(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE)
            .fxTransactionRateType(DEFAULT_FX_TRANSACTION_RATE_TYPE)
            .fxTransactionRateTypeDetails(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS);
        return fxTransactionRateType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionRateType createUpdatedEntity(EntityManager em) {
        FxTransactionRateType fxTransactionRateType = new FxTransactionRateType()
            .fxTransactionRateTypeCode(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE)
            .fxTransactionRateType(UPDATED_FX_TRANSACTION_RATE_TYPE)
            .fxTransactionRateTypeDetails(UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS);
        return fxTransactionRateType;
    }

    @BeforeEach
    public void initTest() {
        fxTransactionRateType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxTransactionRateType() throws Exception {
        int databaseSizeBeforeCreate = fxTransactionRateTypeRepository.findAll().size();
        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);
        restFxTransactionRateTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxTransactionRateType testFxTransactionRateType = fxTransactionRateTypeList.get(fxTransactionRateTypeList.size() - 1);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeCode()).isEqualTo(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE);
        assertThat(testFxTransactionRateType.getFxTransactionRateType()).isEqualTo(DEFAULT_FX_TRANSACTION_RATE_TYPE);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeDetails()).isEqualTo(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(1)).save(testFxTransactionRateType);
    }

    @Test
    @Transactional
    void createFxTransactionRateTypeWithExistingId() throws Exception {
        // Create the FxTransactionRateType with an existing ID
        fxTransactionRateType.setId(1L);
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        int databaseSizeBeforeCreate = fxTransactionRateTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxTransactionRateTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void checkFxTransactionRateTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionRateTypeRepository.findAll().size();
        // set the field null
        fxTransactionRateType.setFxTransactionRateTypeCode(null);

        // Create the FxTransactionRateType, which fails.
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        restFxTransactionRateTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFxTransactionRateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionRateTypeRepository.findAll().size();
        // set the field null
        fxTransactionRateType.setFxTransactionRateType(null);

        // Create the FxTransactionRateType, which fails.
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        restFxTransactionRateTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypes() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateType").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeDetails").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getFxTransactionRateType() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get the fxTransactionRateType
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxTransactionRateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxTransactionRateType.getId().intValue()))
            .andExpect(jsonPath("$.fxTransactionRateTypeCode").value(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE))
            .andExpect(jsonPath("$.fxTransactionRateType").value(DEFAULT_FX_TRANSACTION_RATE_TYPE))
            .andExpect(jsonPath("$.fxTransactionRateTypeDetails").value(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getFxTransactionRateTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        Long id = fxTransactionRateType.getId();

        defaultFxTransactionRateTypeShouldBeFound("id.equals=" + id);
        defaultFxTransactionRateTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxTransactionRateTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxTransactionRateTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxTransactionRateTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxTransactionRateTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode equals to DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateTypeCode.equals=" + DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode equals to UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.equals=" + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode not equals to DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.notEquals=" + DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode not equals to UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateTypeCode.notEquals=" + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode in DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE or UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldBeFound(
            "fxTransactionRateTypeCode.in=" + DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE + "," + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        );

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode equals to UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.in=" + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode is not null
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateTypeCode.specified=true");

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode is null
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode contains DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateTypeCode.contains=" + DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode contains UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.contains=" + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode does not contain DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateTypeCode.doesNotContain=" + DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateTypeCode does not contain UPDATED_FX_TRANSACTION_RATE_TYPE_CODE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateTypeCode.doesNotContain=" + UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType equals to DEFAULT_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateType.equals=" + DEFAULT_FX_TRANSACTION_RATE_TYPE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType equals to UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.equals=" + UPDATED_FX_TRANSACTION_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType not equals to DEFAULT_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.notEquals=" + DEFAULT_FX_TRANSACTION_RATE_TYPE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType not equals to UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateType.notEquals=" + UPDATED_FX_TRANSACTION_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType in DEFAULT_FX_TRANSACTION_RATE_TYPE or UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldBeFound(
            "fxTransactionRateType.in=" + DEFAULT_FX_TRANSACTION_RATE_TYPE + "," + UPDATED_FX_TRANSACTION_RATE_TYPE
        );

        // Get all the fxTransactionRateTypeList where fxTransactionRateType equals to UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.in=" + UPDATED_FX_TRANSACTION_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType is not null
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateType.specified=true");

        // Get all the fxTransactionRateTypeList where fxTransactionRateType is null
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType contains DEFAULT_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateType.contains=" + DEFAULT_FX_TRANSACTION_RATE_TYPE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType contains UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.contains=" + UPDATED_FX_TRANSACTION_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionRateTypesByFxTransactionRateTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType does not contain DEFAULT_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldNotBeFound("fxTransactionRateType.doesNotContain=" + DEFAULT_FX_TRANSACTION_RATE_TYPE);

        // Get all the fxTransactionRateTypeList where fxTransactionRateType does not contain UPDATED_FX_TRANSACTION_RATE_TYPE
        defaultFxTransactionRateTypeShouldBeFound("fxTransactionRateType.doesNotContain=" + UPDATED_FX_TRANSACTION_RATE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxTransactionRateTypeShouldBeFound(String filter) throws Exception {
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateType").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeDetails").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxTransactionRateTypeShouldNotBeFound(String filter) throws Exception {
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxTransactionRateType() throws Exception {
        // Get the fxTransactionRateType
        restFxTransactionRateTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxTransactionRateType() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();

        // Update the fxTransactionRateType
        FxTransactionRateType updatedFxTransactionRateType = fxTransactionRateTypeRepository.findById(fxTransactionRateType.getId()).get();
        // Disconnect from session so that the updates on updatedFxTransactionRateType are not directly saved in db
        em.detach(updatedFxTransactionRateType);
        updatedFxTransactionRateType
            .fxTransactionRateTypeCode(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE)
            .fxTransactionRateType(UPDATED_FX_TRANSACTION_RATE_TYPE)
            .fxTransactionRateTypeDetails(UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS);
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(updatedFxTransactionRateType);

        restFxTransactionRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionRateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionRateType testFxTransactionRateType = fxTransactionRateTypeList.get(fxTransactionRateTypeList.size() - 1);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
        assertThat(testFxTransactionRateType.getFxTransactionRateType()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeDetails()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository).save(testFxTransactionRateType);
    }

    @Test
    @Transactional
    void putNonExistingFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionRateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void partialUpdateFxTransactionRateTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();

        // Update the fxTransactionRateType using partial update
        FxTransactionRateType partialUpdatedFxTransactionRateType = new FxTransactionRateType();
        partialUpdatedFxTransactionRateType.setId(fxTransactionRateType.getId());

        partialUpdatedFxTransactionRateType
            .fxTransactionRateTypeCode(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE)
            .fxTransactionRateType(UPDATED_FX_TRANSACTION_RATE_TYPE);

        restFxTransactionRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionRateType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionRateType testFxTransactionRateType = fxTransactionRateTypeList.get(fxTransactionRateTypeList.size() - 1);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
        assertThat(testFxTransactionRateType.getFxTransactionRateType()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeDetails()).isEqualTo(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFxTransactionRateTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();

        // Update the fxTransactionRateType using partial update
        FxTransactionRateType partialUpdatedFxTransactionRateType = new FxTransactionRateType();
        partialUpdatedFxTransactionRateType.setId(fxTransactionRateType.getId());

        partialUpdatedFxTransactionRateType
            .fxTransactionRateTypeCode(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE)
            .fxTransactionRateType(UPDATED_FX_TRANSACTION_RATE_TYPE)
            .fxTransactionRateTypeDetails(UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS);

        restFxTransactionRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionRateType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionRateType testFxTransactionRateType = fxTransactionRateTypeList.get(fxTransactionRateTypeList.size() - 1);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE_CODE);
        assertThat(testFxTransactionRateType.getFxTransactionRateType()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE);
        assertThat(testFxTransactionRateType.getFxTransactionRateTypeDetails()).isEqualTo(UPDATED_FX_TRANSACTION_RATE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxTransactionRateTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxTransactionRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionRateTypeRepository.findAll().size();
        fxTransactionRateType.setId(count.incrementAndGet());

        // Create the FxTransactionRateType
        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionRateTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionRateType in the database
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(0)).save(fxTransactionRateType);
    }

    @Test
    @Transactional
    void deleteFxTransactionRateType() throws Exception {
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);

        int databaseSizeBeforeDelete = fxTransactionRateTypeRepository.findAll().size();

        // Delete the fxTransactionRateType
        restFxTransactionRateTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxTransactionRateType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxTransactionRateType> fxTransactionRateTypeList = fxTransactionRateTypeRepository.findAll();
        assertThat(fxTransactionRateTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxTransactionRateType in Elasticsearch
        verify(mockFxTransactionRateTypeSearchRepository, times(1)).deleteById(fxTransactionRateType.getId());
    }

    @Test
    @Transactional
    void searchFxTransactionRateType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxTransactionRateTypeRepository.saveAndFlush(fxTransactionRateType);
        when(mockFxTransactionRateTypeSearchRepository.search("id:" + fxTransactionRateType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxTransactionRateType), PageRequest.of(0, 1), 1));

        // Search the fxTransactionRateType
        restFxTransactionRateTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxTransactionRateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateType").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionRateTypeDetails").value(hasItem(DEFAULT_FX_TRANSACTION_RATE_TYPE_DETAILS.toString())));
    }
}
