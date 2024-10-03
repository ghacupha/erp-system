package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.FxTransactionChannelType;
import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionChannelTypeCriteria;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
import io.github.erp.service.mapper.FxTransactionChannelTypeMapper;
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
 * Integration tests for the {@link FxTransactionChannelTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxTransactionChannelTypeResourceIT {

    private static final String DEFAULT_FX_TRANSACTION_CHANNEL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_CHANNEL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_TRANSACTION_CHANNEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_CHANNEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_CHANNEL_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FX_CHANNEL_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-transaction-channel-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-transaction-channel-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    @Autowired
    private FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxTransactionChannelTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxTransactionChannelTypeSearchRepository mockFxTransactionChannelTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxTransactionChannelTypeMockMvc;

    private FxTransactionChannelType fxTransactionChannelType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionChannelType createEntity(EntityManager em) {
        FxTransactionChannelType fxTransactionChannelType = new FxTransactionChannelType()
            .fxTransactionChannelCode(DEFAULT_FX_TRANSACTION_CHANNEL_CODE)
            .fxTransactionChannelType(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE)
            .fxChannelTypeDetails(DEFAULT_FX_CHANNEL_TYPE_DETAILS);
        return fxTransactionChannelType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionChannelType createUpdatedEntity(EntityManager em) {
        FxTransactionChannelType fxTransactionChannelType = new FxTransactionChannelType()
            .fxTransactionChannelCode(UPDATED_FX_TRANSACTION_CHANNEL_CODE)
            .fxTransactionChannelType(UPDATED_FX_TRANSACTION_CHANNEL_TYPE)
            .fxChannelTypeDetails(UPDATED_FX_CHANNEL_TYPE_DETAILS);
        return fxTransactionChannelType;
    }

    @BeforeEach
    public void initTest() {
        fxTransactionChannelType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeCreate = fxTransactionChannelTypeRepository.findAll().size();
        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);
        restFxTransactionChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxTransactionChannelType testFxTransactionChannelType = fxTransactionChannelTypeList.get(fxTransactionChannelTypeList.size() - 1);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelCode()).isEqualTo(DEFAULT_FX_TRANSACTION_CHANNEL_CODE);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelType()).isEqualTo(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE);
        assertThat(testFxTransactionChannelType.getFxChannelTypeDetails()).isEqualTo(DEFAULT_FX_CHANNEL_TYPE_DETAILS);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(1)).save(testFxTransactionChannelType);
    }

    @Test
    @Transactional
    void createFxTransactionChannelTypeWithExistingId() throws Exception {
        // Create the FxTransactionChannelType with an existing ID
        fxTransactionChannelType.setId(1L);
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        int databaseSizeBeforeCreate = fxTransactionChannelTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxTransactionChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void checkFxTransactionChannelCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionChannelTypeRepository.findAll().size();
        // set the field null
        fxTransactionChannelType.setFxTransactionChannelCode(null);

        // Create the FxTransactionChannelType, which fails.
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        restFxTransactionChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFxTransactionChannelTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionChannelTypeRepository.findAll().size();
        // set the field null
        fxTransactionChannelType.setFxTransactionChannelType(null);

        // Create the FxTransactionChannelType, which fails.
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        restFxTransactionChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypes() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionChannelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionChannelCode").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionChannelType").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].fxChannelTypeDetails").value(hasItem(DEFAULT_FX_CHANNEL_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getFxTransactionChannelType() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get the fxTransactionChannelType
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxTransactionChannelType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxTransactionChannelType.getId().intValue()))
            .andExpect(jsonPath("$.fxTransactionChannelCode").value(DEFAULT_FX_TRANSACTION_CHANNEL_CODE))
            .andExpect(jsonPath("$.fxTransactionChannelType").value(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE))
            .andExpect(jsonPath("$.fxChannelTypeDetails").value(DEFAULT_FX_CHANNEL_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getFxTransactionChannelTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        Long id = fxTransactionChannelType.getId();

        defaultFxTransactionChannelTypeShouldBeFound("id.equals=" + id);
        defaultFxTransactionChannelTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxTransactionChannelTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxTransactionChannelTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxTransactionChannelTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxTransactionChannelTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode equals to DEFAULT_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelCode.equals=" + DEFAULT_FX_TRANSACTION_CHANNEL_CODE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode equals to UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.equals=" + UPDATED_FX_TRANSACTION_CHANNEL_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode not equals to DEFAULT_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.notEquals=" + DEFAULT_FX_TRANSACTION_CHANNEL_CODE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode not equals to UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelCode.notEquals=" + UPDATED_FX_TRANSACTION_CHANNEL_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode in DEFAULT_FX_TRANSACTION_CHANNEL_CODE or UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldBeFound(
            "fxTransactionChannelCode.in=" + DEFAULT_FX_TRANSACTION_CHANNEL_CODE + "," + UPDATED_FX_TRANSACTION_CHANNEL_CODE
        );

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode equals to UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.in=" + UPDATED_FX_TRANSACTION_CHANNEL_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode is not null
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelCode.specified=true");

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode is null
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode contains DEFAULT_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelCode.contains=" + DEFAULT_FX_TRANSACTION_CHANNEL_CODE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode contains UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.contains=" + UPDATED_FX_TRANSACTION_CHANNEL_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode does not contain DEFAULT_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelCode.doesNotContain=" + DEFAULT_FX_TRANSACTION_CHANNEL_CODE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelCode does not contain UPDATED_FX_TRANSACTION_CHANNEL_CODE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelCode.doesNotContain=" + UPDATED_FX_TRANSACTION_CHANNEL_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType equals to DEFAULT_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelType.equals=" + DEFAULT_FX_TRANSACTION_CHANNEL_TYPE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType equals to UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.equals=" + UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType not equals to DEFAULT_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.notEquals=" + DEFAULT_FX_TRANSACTION_CHANNEL_TYPE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType not equals to UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelType.notEquals=" + UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType in DEFAULT_FX_TRANSACTION_CHANNEL_TYPE or UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldBeFound(
            "fxTransactionChannelType.in=" + DEFAULT_FX_TRANSACTION_CHANNEL_TYPE + "," + UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        );

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType equals to UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.in=" + UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType is not null
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelType.specified=true");

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType is null
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType contains DEFAULT_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelType.contains=" + DEFAULT_FX_TRANSACTION_CHANNEL_TYPE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType contains UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.contains=" + UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionChannelTypesByFxTransactionChannelTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType does not contain DEFAULT_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldNotBeFound("fxTransactionChannelType.doesNotContain=" + DEFAULT_FX_TRANSACTION_CHANNEL_TYPE);

        // Get all the fxTransactionChannelTypeList where fxTransactionChannelType does not contain UPDATED_FX_TRANSACTION_CHANNEL_TYPE
        defaultFxTransactionChannelTypeShouldBeFound("fxTransactionChannelType.doesNotContain=" + UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxTransactionChannelTypeShouldBeFound(String filter) throws Exception {
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionChannelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionChannelCode").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionChannelType").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].fxChannelTypeDetails").value(hasItem(DEFAULT_FX_CHANNEL_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxTransactionChannelTypeShouldNotBeFound(String filter) throws Exception {
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxTransactionChannelType() throws Exception {
        // Get the fxTransactionChannelType
        restFxTransactionChannelTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxTransactionChannelType() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();

        // Update the fxTransactionChannelType
        FxTransactionChannelType updatedFxTransactionChannelType = fxTransactionChannelTypeRepository
            .findById(fxTransactionChannelType.getId())
            .get();
        // Disconnect from session so that the updates on updatedFxTransactionChannelType are not directly saved in db
        em.detach(updatedFxTransactionChannelType);
        updatedFxTransactionChannelType
            .fxTransactionChannelCode(UPDATED_FX_TRANSACTION_CHANNEL_CODE)
            .fxTransactionChannelType(UPDATED_FX_TRANSACTION_CHANNEL_TYPE)
            .fxChannelTypeDetails(UPDATED_FX_CHANNEL_TYPE_DETAILS);
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(updatedFxTransactionChannelType);

        restFxTransactionChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionChannelTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionChannelType testFxTransactionChannelType = fxTransactionChannelTypeList.get(fxTransactionChannelTypeList.size() - 1);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelCode()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_CODE);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelType()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
        assertThat(testFxTransactionChannelType.getFxChannelTypeDetails()).isEqualTo(UPDATED_FX_CHANNEL_TYPE_DETAILS);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository).save(testFxTransactionChannelType);
    }

    @Test
    @Transactional
    void putNonExistingFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionChannelTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void partialUpdateFxTransactionChannelTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();

        // Update the fxTransactionChannelType using partial update
        FxTransactionChannelType partialUpdatedFxTransactionChannelType = new FxTransactionChannelType();
        partialUpdatedFxTransactionChannelType.setId(fxTransactionChannelType.getId());

        partialUpdatedFxTransactionChannelType
            .fxTransactionChannelCode(UPDATED_FX_TRANSACTION_CHANNEL_CODE)
            .fxTransactionChannelType(UPDATED_FX_TRANSACTION_CHANNEL_TYPE);

        restFxTransactionChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionChannelType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionChannelType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionChannelType testFxTransactionChannelType = fxTransactionChannelTypeList.get(fxTransactionChannelTypeList.size() - 1);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelCode()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_CODE);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelType()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
        assertThat(testFxTransactionChannelType.getFxChannelTypeDetails()).isEqualTo(DEFAULT_FX_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFxTransactionChannelTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();

        // Update the fxTransactionChannelType using partial update
        FxTransactionChannelType partialUpdatedFxTransactionChannelType = new FxTransactionChannelType();
        partialUpdatedFxTransactionChannelType.setId(fxTransactionChannelType.getId());

        partialUpdatedFxTransactionChannelType
            .fxTransactionChannelCode(UPDATED_FX_TRANSACTION_CHANNEL_CODE)
            .fxTransactionChannelType(UPDATED_FX_TRANSACTION_CHANNEL_TYPE)
            .fxChannelTypeDetails(UPDATED_FX_CHANNEL_TYPE_DETAILS);

        restFxTransactionChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionChannelType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionChannelType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionChannelType testFxTransactionChannelType = fxTransactionChannelTypeList.get(fxTransactionChannelTypeList.size() - 1);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelCode()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_CODE);
        assertThat(testFxTransactionChannelType.getFxTransactionChannelType()).isEqualTo(UPDATED_FX_TRANSACTION_CHANNEL_TYPE);
        assertThat(testFxTransactionChannelType.getFxChannelTypeDetails()).isEqualTo(UPDATED_FX_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxTransactionChannelTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxTransactionChannelType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionChannelTypeRepository.findAll().size();
        fxTransactionChannelType.setId(count.incrementAndGet());

        // Create the FxTransactionChannelType
        FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionChannelTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionChannelType in the database
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(0)).save(fxTransactionChannelType);
    }

    @Test
    @Transactional
    void deleteFxTransactionChannelType() throws Exception {
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);

        int databaseSizeBeforeDelete = fxTransactionChannelTypeRepository.findAll().size();

        // Delete the fxTransactionChannelType
        restFxTransactionChannelTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxTransactionChannelType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxTransactionChannelType> fxTransactionChannelTypeList = fxTransactionChannelTypeRepository.findAll();
        assertThat(fxTransactionChannelTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxTransactionChannelType in Elasticsearch
        verify(mockFxTransactionChannelTypeSearchRepository, times(1)).deleteById(fxTransactionChannelType.getId());
    }

    @Test
    @Transactional
    void searchFxTransactionChannelType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxTransactionChannelTypeRepository.saveAndFlush(fxTransactionChannelType);
        when(mockFxTransactionChannelTypeSearchRepository.search("id:" + fxTransactionChannelType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxTransactionChannelType), PageRequest.of(0, 1), 1));

        // Search the fxTransactionChannelType
        restFxTransactionChannelTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxTransactionChannelType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionChannelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionChannelCode").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionChannelType").value(hasItem(DEFAULT_FX_TRANSACTION_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].fxChannelTypeDetails").value(hasItem(DEFAULT_FX_CHANNEL_TYPE_DETAILS.toString())));
    }
}
