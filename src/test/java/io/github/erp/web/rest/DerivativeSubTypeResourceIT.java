package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.DerivativeSubType;
import io.github.erp.repository.DerivativeSubTypeRepository;
import io.github.erp.repository.search.DerivativeSubTypeSearchRepository;
import io.github.erp.service.criteria.DerivativeSubTypeCriteria;
import io.github.erp.service.dto.DerivativeSubTypeDTO;
import io.github.erp.service.mapper.DerivativeSubTypeMapper;
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
 * Integration tests for the {@link DerivativeSubTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DerivativeSubTypeResourceIT {

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/derivative-sub-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/derivative-sub-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DerivativeSubTypeRepository derivativeSubTypeRepository;

    @Autowired
    private DerivativeSubTypeMapper derivativeSubTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DerivativeSubTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private DerivativeSubTypeSearchRepository mockDerivativeSubTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDerivativeSubTypeMockMvc;

    private DerivativeSubType derivativeSubType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DerivativeSubType createEntity(EntityManager em) {
        DerivativeSubType derivativeSubType = new DerivativeSubType()
            .financialDerivativeSubTypeCode(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)
            .financialDerivativeSubTye(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE)
            .financialDerivativeSubtypeDetails(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);
        return derivativeSubType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DerivativeSubType createUpdatedEntity(EntityManager em) {
        DerivativeSubType derivativeSubType = new DerivativeSubType()
            .financialDerivativeSubTypeCode(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)
            .financialDerivativeSubTye(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE)
            .financialDerivativeSubtypeDetails(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);
        return derivativeSubType;
    }

    @BeforeEach
    public void initTest() {
        derivativeSubType = createEntity(em);
    }

    @Test
    @Transactional
    void createDerivativeSubType() throws Exception {
        int databaseSizeBeforeCreate = derivativeSubTypeRepository.findAll().size();
        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);
        restDerivativeSubTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DerivativeSubType testDerivativeSubType = derivativeSubTypeList.get(derivativeSubTypeList.size() - 1);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTypeCode()).isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTye()).isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubtypeDetails()).isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(1)).save(testDerivativeSubType);
    }

    @Test
    @Transactional
    void createDerivativeSubTypeWithExistingId() throws Exception {
        // Create the DerivativeSubType with an existing ID
        derivativeSubType.setId(1L);
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        int databaseSizeBeforeCreate = derivativeSubTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDerivativeSubTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void checkFinancialDerivativeSubTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = derivativeSubTypeRepository.findAll().size();
        // set the field null
        derivativeSubType.setFinancialDerivativeSubTypeCode(null);

        // Create the DerivativeSubType, which fails.
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        restDerivativeSubTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinancialDerivativeSubTyeIsRequired() throws Exception {
        int databaseSizeBeforeTest = derivativeSubTypeRepository.findAll().size();
        // set the field null
        derivativeSubType.setFinancialDerivativeSubTye(null);

        // Create the DerivativeSubType, which fails.
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        restDerivativeSubTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypes() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTye").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeSubtypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getDerivativeSubType() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get the derivativeSubType
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, derivativeSubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(derivativeSubType.getId().intValue()))
            .andExpect(jsonPath("$.financialDerivativeSubTypeCode").value(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE))
            .andExpect(jsonPath("$.financialDerivativeSubTye").value(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE))
            .andExpect(jsonPath("$.financialDerivativeSubtypeDetails").value(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getDerivativeSubTypesByIdFiltering() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        Long id = derivativeSubType.getId();

        defaultDerivativeSubTypeShouldBeFound("id.equals=" + id);
        defaultDerivativeSubTypeShouldNotBeFound("id.notEquals=" + id);

        defaultDerivativeSubTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDerivativeSubTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultDerivativeSubTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDerivativeSubTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode equals to DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTypeCode.equals=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTypeCode.equals=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode not equals to DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTypeCode.notEquals=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode not equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTypeCode.notEquals=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode in DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE or UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldBeFound(
            "financialDerivativeSubTypeCode.in=" +
            DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE +
            "," +
            UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        );

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTypeCode.in=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode is not null
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTypeCode.specified=true");

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode is null
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode contains DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTypeCode.contains=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode contains UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTypeCode.contains=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode does not contain DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldNotBeFound(
            "financialDerivativeSubTypeCode.doesNotContain=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        );

        // Get all the derivativeSubTypeList where financialDerivativeSubTypeCode does not contain UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        defaultDerivativeSubTypeShouldBeFound(
            "financialDerivativeSubTypeCode.doesNotContain=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeIsEqualToSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye equals to DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTye.equals=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.equals=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye not equals to DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.notEquals=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye not equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTye.notEquals=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeIsInShouldWork() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye in DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE or UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldBeFound(
            "financialDerivativeSubTye.in=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE + "," + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        );

        // Get all the derivativeSubTypeList where financialDerivativeSubTye equals to UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.in=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeIsNullOrNotNull() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye is not null
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTye.specified=true");

        // Get all the derivativeSubTypeList where financialDerivativeSubTye is null
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.specified=false");
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeContainsSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye contains DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTye.contains=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye contains UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.contains=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
    }

    @Test
    @Transactional
    void getAllDerivativeSubTypesByFinancialDerivativeSubTyeNotContainsSomething() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye does not contain DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldNotBeFound("financialDerivativeSubTye.doesNotContain=" + DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE);

        // Get all the derivativeSubTypeList where financialDerivativeSubTye does not contain UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE
        defaultDerivativeSubTypeShouldBeFound("financialDerivativeSubTye.doesNotContain=" + UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDerivativeSubTypeShouldBeFound(String filter) throws Exception {
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTye").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeSubtypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDerivativeSubTypeShouldNotBeFound(String filter) throws Exception {
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDerivativeSubType() throws Exception {
        // Get the derivativeSubType
        restDerivativeSubTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDerivativeSubType() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();

        // Update the derivativeSubType
        DerivativeSubType updatedDerivativeSubType = derivativeSubTypeRepository.findById(derivativeSubType.getId()).get();
        // Disconnect from session so that the updates on updatedDerivativeSubType are not directly saved in db
        em.detach(updatedDerivativeSubType);
        updatedDerivativeSubType
            .financialDerivativeSubTypeCode(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)
            .financialDerivativeSubTye(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE)
            .financialDerivativeSubtypeDetails(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(updatedDerivativeSubType);

        restDerivativeSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, derivativeSubTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);
        DerivativeSubType testDerivativeSubType = derivativeSubTypeList.get(derivativeSubTypeList.size() - 1);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTye()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubtypeDetails()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository).save(testDerivativeSubType);
    }

    @Test
    @Transactional
    void putNonExistingDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, derivativeSubTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void putWithIdMismatchDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void partialUpdateDerivativeSubTypeWithPatch() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();

        // Update the derivativeSubType using partial update
        DerivativeSubType partialUpdatedDerivativeSubType = new DerivativeSubType();
        partialUpdatedDerivativeSubType.setId(derivativeSubType.getId());

        partialUpdatedDerivativeSubType
            .financialDerivativeSubTypeCode(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)
            .financialDerivativeSubTye(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE)
            .financialDerivativeSubtypeDetails(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);

        restDerivativeSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDerivativeSubType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDerivativeSubType))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);
        DerivativeSubType testDerivativeSubType = derivativeSubTypeList.get(derivativeSubTypeList.size() - 1);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTye()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubtypeDetails()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateDerivativeSubTypeWithPatch() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();

        // Update the derivativeSubType using partial update
        DerivativeSubType partialUpdatedDerivativeSubType = new DerivativeSubType();
        partialUpdatedDerivativeSubType.setId(derivativeSubType.getId());

        partialUpdatedDerivativeSubType
            .financialDerivativeSubTypeCode(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)
            .financialDerivativeSubTye(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE)
            .financialDerivativeSubtypeDetails(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);

        restDerivativeSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDerivativeSubType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDerivativeSubType))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);
        DerivativeSubType testDerivativeSubType = derivativeSubTypeList.get(derivativeSubTypeList.size() - 1);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubTye()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUB_TYE);
        assertThat(testDerivativeSubType.getFinancialDerivativeSubtypeDetails()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, derivativeSubTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDerivativeSubType() throws Exception {
        int databaseSizeBeforeUpdate = derivativeSubTypeRepository.findAll().size();
        derivativeSubType.setId(count.incrementAndGet());

        // Create the DerivativeSubType
        DerivativeSubTypeDTO derivativeSubTypeDTO = derivativeSubTypeMapper.toDto(derivativeSubType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeSubTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DerivativeSubType in the database
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(0)).save(derivativeSubType);
    }

    @Test
    @Transactional
    void deleteDerivativeSubType() throws Exception {
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);

        int databaseSizeBeforeDelete = derivativeSubTypeRepository.findAll().size();

        // Delete the derivativeSubType
        restDerivativeSubTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, derivativeSubType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DerivativeSubType> derivativeSubTypeList = derivativeSubTypeRepository.findAll();
        assertThat(derivativeSubTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DerivativeSubType in Elasticsearch
        verify(mockDerivativeSubTypeSearchRepository, times(1)).deleteById(derivativeSubType.getId());
    }

    @Test
    @Transactional
    void searchDerivativeSubType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        derivativeSubTypeRepository.saveAndFlush(derivativeSubType);
        when(mockDerivativeSubTypeSearchRepository.search("id:" + derivativeSubType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(derivativeSubType), PageRequest.of(0, 1), 1));

        // Search the derivativeSubType
        restDerivativeSubTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + derivativeSubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeSubTye").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUB_TYE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeSubtypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_SUBTYPE_DETAILS.toString()))
            );
    }
}
