package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import io.github.erp.domain.FxRateType;
import io.github.erp.repository.FxRateTypeRepository;
import io.github.erp.repository.search.FxRateTypeSearchRepository;
import io.github.erp.service.criteria.FxRateTypeCriteria;
import io.github.erp.service.dto.FxRateTypeDTO;
import io.github.erp.service.mapper.FxRateTypeMapper;
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
 * Integration tests for the {@link FxRateTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FxRateTypeResourceIT {

    private static final String DEFAULT_FX_RATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FX_RATE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_RATE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FX_RATE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FX_RATE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FX_RATE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fx-rate-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fx-rate-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FxRateTypeRepository fxRateTypeRepository;

    @Autowired
    private FxRateTypeMapper fxRateTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FxRateTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FxRateTypeSearchRepository mockFxRateTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFxRateTypeMockMvc;

    private FxRateType fxRateType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxRateType createEntity(EntityManager em) {
        FxRateType fxRateType = new FxRateType()
            .fxRateCode(DEFAULT_FX_RATE_CODE)
            .fxRateType(DEFAULT_FX_RATE_TYPE)
            .fxRateDetails(DEFAULT_FX_RATE_DETAILS);
        return fxRateType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FxRateType createUpdatedEntity(EntityManager em) {
        FxRateType fxRateType = new FxRateType()
            .fxRateCode(UPDATED_FX_RATE_CODE)
            .fxRateType(UPDATED_FX_RATE_TYPE)
            .fxRateDetails(UPDATED_FX_RATE_DETAILS);
        return fxRateType;
    }

    @BeforeEach
    public void initTest() {
        fxRateType = createEntity(em);
    }

    @Test
    @Transactional
    void createFxRateType() throws Exception {
        int databaseSizeBeforeCreate = fxRateTypeRepository.findAll().size();
        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);
        restFxRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FxRateType testFxRateType = fxRateTypeList.get(fxRateTypeList.size() - 1);
        assertThat(testFxRateType.getFxRateCode()).isEqualTo(DEFAULT_FX_RATE_CODE);
        assertThat(testFxRateType.getFxRateType()).isEqualTo(DEFAULT_FX_RATE_TYPE);
        assertThat(testFxRateType.getFxRateDetails()).isEqualTo(DEFAULT_FX_RATE_DETAILS);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(1)).save(testFxRateType);
    }

    @Test
    @Transactional
    void createFxRateTypeWithExistingId() throws Exception {
        // Create the FxRateType with an existing ID
        fxRateType.setId(1L);
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        int databaseSizeBeforeCreate = fxRateTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void checkFxRateCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxRateTypeRepository.findAll().size();
        // set the field null
        fxRateType.setFxRateCode(null);

        // Create the FxRateType, which fails.
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        restFxRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFxRateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fxRateTypeRepository.findAll().size();
        // set the field null
        fxRateType.setFxRateType(null);

        // Create the FxRateType, which fails.
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        restFxRateTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFxRateTypes() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxRateCode").value(hasItem(DEFAULT_FX_RATE_CODE)))
            .andExpect(jsonPath("$.[*].fxRateType").value(hasItem(DEFAULT_FX_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxRateDetails").value(hasItem(DEFAULT_FX_RATE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getFxRateType() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get the fxRateType
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fxRateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fxRateType.getId().intValue()))
            .andExpect(jsonPath("$.fxRateCode").value(DEFAULT_FX_RATE_CODE))
            .andExpect(jsonPath("$.fxRateType").value(DEFAULT_FX_RATE_TYPE))
            .andExpect(jsonPath("$.fxRateDetails").value(DEFAULT_FX_RATE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getFxRateTypesByIdFiltering() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        Long id = fxRateType.getId();

        defaultFxRateTypeShouldBeFound("id.equals=" + id);
        defaultFxRateTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFxRateTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFxRateTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFxRateTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFxRateTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode equals to DEFAULT_FX_RATE_CODE
        defaultFxRateTypeShouldBeFound("fxRateCode.equals=" + DEFAULT_FX_RATE_CODE);

        // Get all the fxRateTypeList where fxRateCode equals to UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldNotBeFound("fxRateCode.equals=" + UPDATED_FX_RATE_CODE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode not equals to DEFAULT_FX_RATE_CODE
        defaultFxRateTypeShouldNotBeFound("fxRateCode.notEquals=" + DEFAULT_FX_RATE_CODE);

        // Get all the fxRateTypeList where fxRateCode not equals to UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldBeFound("fxRateCode.notEquals=" + UPDATED_FX_RATE_CODE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode in DEFAULT_FX_RATE_CODE or UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldBeFound("fxRateCode.in=" + DEFAULT_FX_RATE_CODE + "," + UPDATED_FX_RATE_CODE);

        // Get all the fxRateTypeList where fxRateCode equals to UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldNotBeFound("fxRateCode.in=" + UPDATED_FX_RATE_CODE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode is not null
        defaultFxRateTypeShouldBeFound("fxRateCode.specified=true");

        // Get all the fxRateTypeList where fxRateCode is null
        defaultFxRateTypeShouldNotBeFound("fxRateCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeContainsSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode contains DEFAULT_FX_RATE_CODE
        defaultFxRateTypeShouldBeFound("fxRateCode.contains=" + DEFAULT_FX_RATE_CODE);

        // Get all the fxRateTypeList where fxRateCode contains UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldNotBeFound("fxRateCode.contains=" + UPDATED_FX_RATE_CODE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateCode does not contain DEFAULT_FX_RATE_CODE
        defaultFxRateTypeShouldNotBeFound("fxRateCode.doesNotContain=" + DEFAULT_FX_RATE_CODE);

        // Get all the fxRateTypeList where fxRateCode does not contain UPDATED_FX_RATE_CODE
        defaultFxRateTypeShouldBeFound("fxRateCode.doesNotContain=" + UPDATED_FX_RATE_CODE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType equals to DEFAULT_FX_RATE_TYPE
        defaultFxRateTypeShouldBeFound("fxRateType.equals=" + DEFAULT_FX_RATE_TYPE);

        // Get all the fxRateTypeList where fxRateType equals to UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldNotBeFound("fxRateType.equals=" + UPDATED_FX_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType not equals to DEFAULT_FX_RATE_TYPE
        defaultFxRateTypeShouldNotBeFound("fxRateType.notEquals=" + DEFAULT_FX_RATE_TYPE);

        // Get all the fxRateTypeList where fxRateType not equals to UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldBeFound("fxRateType.notEquals=" + UPDATED_FX_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType in DEFAULT_FX_RATE_TYPE or UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldBeFound("fxRateType.in=" + DEFAULT_FX_RATE_TYPE + "," + UPDATED_FX_RATE_TYPE);

        // Get all the fxRateTypeList where fxRateType equals to UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldNotBeFound("fxRateType.in=" + UPDATED_FX_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType is not null
        defaultFxRateTypeShouldBeFound("fxRateType.specified=true");

        // Get all the fxRateTypeList where fxRateType is null
        defaultFxRateTypeShouldNotBeFound("fxRateType.specified=false");
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeContainsSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType contains DEFAULT_FX_RATE_TYPE
        defaultFxRateTypeShouldBeFound("fxRateType.contains=" + DEFAULT_FX_RATE_TYPE);

        // Get all the fxRateTypeList where fxRateType contains UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldNotBeFound("fxRateType.contains=" + UPDATED_FX_RATE_TYPE);
    }

    @Test
    @Transactional
    void getAllFxRateTypesByFxRateTypeNotContainsSomething() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        // Get all the fxRateTypeList where fxRateType does not contain DEFAULT_FX_RATE_TYPE
        defaultFxRateTypeShouldNotBeFound("fxRateType.doesNotContain=" + DEFAULT_FX_RATE_TYPE);

        // Get all the fxRateTypeList where fxRateType does not contain UPDATED_FX_RATE_TYPE
        defaultFxRateTypeShouldBeFound("fxRateType.doesNotContain=" + UPDATED_FX_RATE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFxRateTypeShouldBeFound(String filter) throws Exception {
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxRateCode").value(hasItem(DEFAULT_FX_RATE_CODE)))
            .andExpect(jsonPath("$.[*].fxRateType").value(hasItem(DEFAULT_FX_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxRateDetails").value(hasItem(DEFAULT_FX_RATE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFxRateTypeShouldNotBeFound(String filter) throws Exception {
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFxRateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFxRateType() throws Exception {
        // Get the fxRateType
        restFxRateTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFxRateType() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();

        // Update the fxRateType
        FxRateType updatedFxRateType = fxRateTypeRepository.findById(fxRateType.getId()).get();
        // Disconnect from session so that the updates on updatedFxRateType are not directly saved in db
        em.detach(updatedFxRateType);
        updatedFxRateType.fxRateCode(UPDATED_FX_RATE_CODE).fxRateType(UPDATED_FX_RATE_TYPE).fxRateDetails(UPDATED_FX_RATE_DETAILS);
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(updatedFxRateType);

        restFxRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxRateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxRateType testFxRateType = fxRateTypeList.get(fxRateTypeList.size() - 1);
        assertThat(testFxRateType.getFxRateCode()).isEqualTo(UPDATED_FX_RATE_CODE);
        assertThat(testFxRateType.getFxRateType()).isEqualTo(UPDATED_FX_RATE_TYPE);
        assertThat(testFxRateType.getFxRateDetails()).isEqualTo(UPDATED_FX_RATE_DETAILS);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository).save(testFxRateType);
    }

    @Test
    @Transactional
    void putNonExistingFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fxRateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void partialUpdateFxRateTypeWithPatch() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();

        // Update the fxRateType using partial update
        FxRateType partialUpdatedFxRateType = new FxRateType();
        partialUpdatedFxRateType.setId(fxRateType.getId());

        partialUpdatedFxRateType.fxRateCode(UPDATED_FX_RATE_CODE);

        restFxRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxRateType))
            )
            .andExpect(status().isOk());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxRateType testFxRateType = fxRateTypeList.get(fxRateTypeList.size() - 1);
        assertThat(testFxRateType.getFxRateCode()).isEqualTo(UPDATED_FX_RATE_CODE);
        assertThat(testFxRateType.getFxRateType()).isEqualTo(DEFAULT_FX_RATE_TYPE);
        assertThat(testFxRateType.getFxRateDetails()).isEqualTo(DEFAULT_FX_RATE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFxRateTypeWithPatch() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();

        // Update the fxRateType using partial update
        FxRateType partialUpdatedFxRateType = new FxRateType();
        partialUpdatedFxRateType.setId(fxRateType.getId());

        partialUpdatedFxRateType.fxRateCode(UPDATED_FX_RATE_CODE).fxRateType(UPDATED_FX_RATE_TYPE).fxRateDetails(UPDATED_FX_RATE_DETAILS);

        restFxRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFxRateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFxRateType))
            )
            .andExpect(status().isOk());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);
        FxRateType testFxRateType = fxRateTypeList.get(fxRateTypeList.size() - 1);
        assertThat(testFxRateType.getFxRateCode()).isEqualTo(UPDATED_FX_RATE_CODE);
        assertThat(testFxRateType.getFxRateType()).isEqualTo(UPDATED_FX_RATE_TYPE);
        assertThat(testFxRateType.getFxRateDetails()).isEqualTo(UPDATED_FX_RATE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fxRateTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFxRateType() throws Exception {
        int databaseSizeBeforeUpdate = fxRateTypeRepository.findAll().size();
        fxRateType.setId(count.incrementAndGet());

        // Create the FxRateType
        FxRateTypeDTO fxRateTypeDTO = fxRateTypeMapper.toDto(fxRateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFxRateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fxRateTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FxRateType in the database
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(0)).save(fxRateType);
    }

    @Test
    @Transactional
    void deleteFxRateType() throws Exception {
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);

        int databaseSizeBeforeDelete = fxRateTypeRepository.findAll().size();

        // Delete the fxRateType
        restFxRateTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fxRateType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FxRateType> fxRateTypeList = fxRateTypeRepository.findAll();
        assertThat(fxRateTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FxRateType in Elasticsearch
        verify(mockFxRateTypeSearchRepository, times(1)).deleteById(fxRateType.getId());
    }

    @Test
    @Transactional
    void searchFxRateType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fxRateTypeRepository.saveAndFlush(fxRateType);
        when(mockFxRateTypeSearchRepository.search("id:" + fxRateType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fxRateType), PageRequest.of(0, 1), 1));

        // Search the fxRateType
        restFxRateTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fxRateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fxRateCode").value(hasItem(DEFAULT_FX_RATE_CODE)))
            .andExpect(jsonPath("$.[*].fxRateType").value(hasItem(DEFAULT_FX_RATE_TYPE)))
            .andExpect(jsonPath("$.[*].fxRateDetails").value(hasItem(DEFAULT_FX_RATE_DETAILS.toString())));
    }
}
