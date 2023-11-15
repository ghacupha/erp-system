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
import io.github.erp.domain.FxTransactionType;
import io.github.erp.repository.FxTransactionTypeRepository;
import io.github.erp.repository.search.FxTransactionTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionTypeCriteria;
import io.github.erp.service.dto.FxTransactionTypeDTO;
import io.github.erp.service.mapper.FxTransactionTypeMapper;
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
 * Integration tests for the {@link FxTransactionTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxTransactionTypeResourceIT {

    private static final String DEFAULT_FX_TRANSACTION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-transaction-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxTransactionTypeRepository fxTransactionTypeRepository;

    @Autowired
    private FxTransactionTypeMapper fxTransactionTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxTransactionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxTransactionTypeSearchRepository mockFxTransactionTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxTransactionTypeMockMvc;

    private FxTransactionType fxTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionType createEntity(EntityManager em) {
        FxTransactionType fxTransactionType = new FxTransactionType()
            .fxTransactionTypeCode(DEFAULT_FX_TRANSACTION_TYPE_CODE)
            .fxTransactionType(DEFAULT_FX_TRANSACTION_TYPE)
            .fxTransactionTypeDescription(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION);
        return fxTransactionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxTransactionType createUpdatedEntity(EntityManager em) {
        FxTransactionType fxTransactionType = new FxTransactionType()
            .fxTransactionTypeCode(UPDATED_FX_TRANSACTION_TYPE_CODE)
            .fxTransactionType(UPDATED_FX_TRANSACTION_TYPE)
            .fxTransactionTypeDescription(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);
        return fxTransactionType;
    }

    @BeforeEach
    public void initTest() {
        fxTransactionType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxTransactionType() throws Exception {
        int databaseSizeBeforeCreate = fxTransactionTypeRepository.findAll().size();
        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);
        restFxTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxTransactionType testFxTransactionType = fxTransactionTypeList.get(fxTransactionTypeList.size() - 1);
        assertThat(testFxTransactionType.getFxTransactionTypeCode()).isEqualTo(DEFAULT_FX_TRANSACTION_TYPE_CODE);
        assertThat(testFxTransactionType.getFxTransactionType()).isEqualTo(DEFAULT_FX_TRANSACTION_TYPE);
        assertThat(testFxTransactionType.getFxTransactionTypeDescription()).isEqualTo(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(1)).save(testFxTransactionType);
    }

    @Test
    @Transactional
    void createFxTransactionTypeWithExistingId() throws Exception {
        // Create the FxTransactionType with an existing ID
        fxTransactionType.setId(1L);
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        int databaseSizeBeforeCreate = fxTransactionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void checkFxTransactionTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionTypeRepository.findAll().size();
        // set the field null
        fxTransactionType.setFxTransactionTypeCode(null);

        // Create the FxTransactionType, which fails.
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        restFxTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFxTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxTransactionTypeRepository.findAll().size();
        // set the field null
        fxTransactionType.setFxTransactionType(null);

        // Create the FxTransactionType, which fails.
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        restFxTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypes() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionType").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionTypeDescription").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getFxTransactionType() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get the fxTransactionType
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxTransactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxTransactionType.getId().intValue()))
            .andExpect(jsonPath("$.fxTransactionTypeCode").value(DEFAULT_FX_TRANSACTION_TYPE_CODE))
            .andExpect(jsonPath("$.fxTransactionType").value(DEFAULT_FX_TRANSACTION_TYPE))
            .andExpect(jsonPath("$.fxTransactionTypeDescription").value(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getFxTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        Long id = fxTransactionType.getId();

        defaultFxTransactionTypeShouldBeFound("id.equals=" + id);
        defaultFxTransactionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxTransactionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxTransactionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxTransactionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxTransactionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode equals to DEFAULT_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldBeFound("fxTransactionTypeCode.equals=" + DEFAULT_FX_TRANSACTION_TYPE_CODE);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode equals to UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.equals=" + UPDATED_FX_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode not equals to DEFAULT_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.notEquals=" + DEFAULT_FX_TRANSACTION_TYPE_CODE);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode not equals to UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldBeFound("fxTransactionTypeCode.notEquals=" + UPDATED_FX_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode in DEFAULT_FX_TRANSACTION_TYPE_CODE or UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldBeFound(
            "fxTransactionTypeCode.in=" + DEFAULT_FX_TRANSACTION_TYPE_CODE + "," + UPDATED_FX_TRANSACTION_TYPE_CODE
        );

        // Get all the fxTransactionTypeList where fxTransactionTypeCode equals to UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.in=" + UPDATED_FX_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode is not null
        defaultFxTransactionTypeShouldBeFound("fxTransactionTypeCode.specified=true");

        // Get all the fxTransactionTypeList where fxTransactionTypeCode is null
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode contains DEFAULT_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldBeFound("fxTransactionTypeCode.contains=" + DEFAULT_FX_TRANSACTION_TYPE_CODE);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode contains UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.contains=" + UPDATED_FX_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode does not contain DEFAULT_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionTypeCode.doesNotContain=" + DEFAULT_FX_TRANSACTION_TYPE_CODE);

        // Get all the fxTransactionTypeList where fxTransactionTypeCode does not contain UPDATED_FX_TRANSACTION_TYPE_CODE
        defaultFxTransactionTypeShouldBeFound("fxTransactionTypeCode.doesNotContain=" + UPDATED_FX_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType equals to DEFAULT_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.equals=" + DEFAULT_FX_TRANSACTION_TYPE);

        // Get all the fxTransactionTypeList where fxTransactionType equals to UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.equals=" + UPDATED_FX_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType not equals to DEFAULT_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.notEquals=" + DEFAULT_FX_TRANSACTION_TYPE);

        // Get all the fxTransactionTypeList where fxTransactionType not equals to UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.notEquals=" + UPDATED_FX_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType in DEFAULT_FX_TRANSACTION_TYPE or UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.in=" + DEFAULT_FX_TRANSACTION_TYPE + "," + UPDATED_FX_TRANSACTION_TYPE);

        // Get all the fxTransactionTypeList where fxTransactionType equals to UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.in=" + UPDATED_FX_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType is not null
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.specified=true");

        // Get all the fxTransactionTypeList where fxTransactionType is null
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType contains DEFAULT_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.contains=" + DEFAULT_FX_TRANSACTION_TYPE);

        // Get all the fxTransactionTypeList where fxTransactionType contains UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.contains=" + UPDATED_FX_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllFxTransactionTypesByFxTransactionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        // Get all the fxTransactionTypeList where fxTransactionType does not contain DEFAULT_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldNotBeFound("fxTransactionType.doesNotContain=" + DEFAULT_FX_TRANSACTION_TYPE);

        // Get all the fxTransactionTypeList where fxTransactionType does not contain UPDATED_FX_TRANSACTION_TYPE
        defaultFxTransactionTypeShouldBeFound("fxTransactionType.doesNotContain=" + UPDATED_FX_TRANSACTION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxTransactionTypeShouldBeFound(String filter) throws Exception {
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionType").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionTypeDescription").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxTransactionType() throws Exception {
        // Get the fxTransactionType
        restFxTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxTransactionType() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();

        // Update the fxTransactionType
        FxTransactionType updatedFxTransactionType = fxTransactionTypeRepository.findById(fxTransactionType.getId()).get();
        // Disconnect from session so that the updates on updatedFxTransactionType are not directly saved in db
        em.detach(updatedFxTransactionType);
        updatedFxTransactionType
            .fxTransactionTypeCode(UPDATED_FX_TRANSACTION_TYPE_CODE)
            .fxTransactionType(UPDATED_FX_TRANSACTION_TYPE)
            .fxTransactionTypeDescription(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(updatedFxTransactionType);

        restFxTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionType testFxTransactionType = fxTransactionTypeList.get(fxTransactionTypeList.size() - 1);
        assertThat(testFxTransactionType.getFxTransactionTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_CODE);
        assertThat(testFxTransactionType.getFxTransactionType()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE);
        assertThat(testFxTransactionType.getFxTransactionTypeDescription()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository).save(testFxTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxTransactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void partialUpdateFxTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();

        // Update the fxTransactionType using partial update
        FxTransactionType partialUpdatedFxTransactionType = new FxTransactionType();
        partialUpdatedFxTransactionType.setId(fxTransactionType.getId());

        partialUpdatedFxTransactionType
            .fxTransactionTypeCode(UPDATED_FX_TRANSACTION_TYPE_CODE)
            .fxTransactionType(UPDATED_FX_TRANSACTION_TYPE)
            .fxTransactionTypeDescription(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);

        restFxTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionType testFxTransactionType = fxTransactionTypeList.get(fxTransactionTypeList.size() - 1);
        assertThat(testFxTransactionType.getFxTransactionTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_CODE);
        assertThat(testFxTransactionType.getFxTransactionType()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE);
        assertThat(testFxTransactionType.getFxTransactionTypeDescription()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFxTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();

        // Update the fxTransactionType using partial update
        FxTransactionType partialUpdatedFxTransactionType = new FxTransactionType();
        partialUpdatedFxTransactionType.setId(fxTransactionType.getId());

        partialUpdatedFxTransactionType
            .fxTransactionTypeCode(UPDATED_FX_TRANSACTION_TYPE_CODE)
            .fxTransactionType(UPDATED_FX_TRANSACTION_TYPE)
            .fxTransactionTypeDescription(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);

        restFxTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        FxTransactionType testFxTransactionType = fxTransactionTypeList.get(fxTransactionTypeList.size() - 1);
        assertThat(testFxTransactionType.getFxTransactionTypeCode()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_CODE);
        assertThat(testFxTransactionType.getFxTransactionType()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE);
        assertThat(testFxTransactionType.getFxTransactionTypeDescription()).isEqualTo(UPDATED_FX_TRANSACTION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxTransactionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = fxTransactionTypeRepository.findAll().size();
        fxTransactionType.setId(count.incrementAndGet());

        // Create the FxTransactionType
        FxTransactionTypeDTO fxTransactionTypeDTO = fxTransactionTypeMapper.toDto(fxTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxTransactionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxTransactionType in the database
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(0)).save(fxTransactionType);
    }

    @Test
    @Transactional
    void deleteFxTransactionType() throws Exception {
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);

        int databaseSizeBeforeDelete = fxTransactionTypeRepository.findAll().size();

        // Delete the fxTransactionType
        restFxTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxTransactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxTransactionType> fxTransactionTypeList = fxTransactionTypeRepository.findAll();
        assertThat(fxTransactionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxTransactionType in Elasticsearch
        verify(mockFxTransactionTypeSearchRepository, times(1)).deleteById(fxTransactionType.getId());
    }

    @Test
    @Transactional
    void searchFxTransactionType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxTransactionTypeRepository.saveAndFlush(fxTransactionType);
        when(mockFxTransactionTypeSearchRepository.search("id:" + fxTransactionType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxTransactionType), PageRequest.of(0, 1), 1));

        // Search the fxTransactionType
        restFxTransactionTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxTransactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxTransactionTypeCode").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].fxTransactionType").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].fxTransactionTypeDescription").value(hasItem(DEFAULT_FX_TRANSACTION_TYPE_DESCRIPTION.toString())));
    }
}
