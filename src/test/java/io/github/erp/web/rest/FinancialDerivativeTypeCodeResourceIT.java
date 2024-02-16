package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.FinancialDerivativeTypeCode;
import io.github.erp.repository.FinancialDerivativeTypeCodeRepository;
import io.github.erp.repository.search.FinancialDerivativeTypeCodeSearchRepository;
import io.github.erp.service.criteria.FinancialDerivativeTypeCodeCriteria;
import io.github.erp.service.dto.FinancialDerivativeTypeCodeDTO;
import io.github.erp.service.mapper.FinancialDerivativeTypeCodeMapper;
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
 * Integration tests for the {@link FinancialDerivativeTypeCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FinancialDerivativeTypeCodeResourceIT {

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/financial-derivative-type-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/financial-derivative-type-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository;

    @Autowired
    private FinancialDerivativeTypeCodeMapper financialDerivativeTypeCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FinancialDerivativeTypeCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FinancialDerivativeTypeCodeSearchRepository mockFinancialDerivativeTypeCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancialDerivativeTypeCodeMockMvc;

    private FinancialDerivativeTypeCode financialDerivativeTypeCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialDerivativeTypeCode createEntity(EntityManager em) {
        FinancialDerivativeTypeCode financialDerivativeTypeCode = new FinancialDerivativeTypeCode()
            .financialDerivativeTypeCode(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE)
            .financialDerivativeType(DEFAULT_FINANCIAL_DERIVATIVE_TYPE)
            .financialDerivativeTypeDetails(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS);
        return financialDerivativeTypeCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialDerivativeTypeCode createUpdatedEntity(EntityManager em) {
        FinancialDerivativeTypeCode financialDerivativeTypeCode = new FinancialDerivativeTypeCode()
            .financialDerivativeTypeCode(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE)
            .financialDerivativeType(UPDATED_FINANCIAL_DERIVATIVE_TYPE)
            .financialDerivativeTypeDetails(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);
        return financialDerivativeTypeCode;
    }

    @BeforeEach
    public void initTest() {
        financialDerivativeTypeCode = createEntity(em);
    }

    @Test
    @Transactional
    void createFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeCreate = financialDerivativeTypeCodeRepository.findAll().size();
        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeCreate + 1);
        FinancialDerivativeTypeCode testFinancialDerivativeTypeCode = financialDerivativeTypeCodeList.get(
            financialDerivativeTypeCodeList.size() - 1
        );
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeCode()).isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeType()).isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_TYPE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeDetails())
            .isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(1)).save(testFinancialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void createFinancialDerivativeTypeCodeWithExistingId() throws Exception {
        // Create the FinancialDerivativeTypeCode with an existing ID
        financialDerivativeTypeCode.setId(1L);
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        int databaseSizeBeforeCreate = financialDerivativeTypeCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void checkFinancialDerivativeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialDerivativeTypeCodeRepository.findAll().size();
        // set the field null
        financialDerivativeTypeCode.setFinancialDerivativeTypeCode(null);

        // Create the FinancialDerivativeTypeCode, which fails.
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinancialDerivativeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialDerivativeTypeCodeRepository.findAll().size();
        // set the field null
        financialDerivativeTypeCode.setFinancialDerivativeType(null);

        // Create the FinancialDerivativeTypeCode, which fails.
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodes() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialDerivativeTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeTypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getFinancialDerivativeTypeCode() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get the financialDerivativeTypeCode
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, financialDerivativeTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialDerivativeTypeCode.getId().intValue()))
            .andExpect(jsonPath("$.financialDerivativeTypeCode").value(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE))
            .andExpect(jsonPath("$.financialDerivativeType").value(DEFAULT_FINANCIAL_DERIVATIVE_TYPE))
            .andExpect(jsonPath("$.financialDerivativeTypeDetails").value(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getFinancialDerivativeTypeCodesByIdFiltering() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        Long id = financialDerivativeTypeCode.getId();

        defaultFinancialDerivativeTypeCodeShouldBeFound("id.equals=" + id);
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("id.notEquals=" + id);

        defaultFinancialDerivativeTypeCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultFinancialDerivativeTypeCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode equals to DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeTypeCode.equals=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeTypeCode.equals=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode not equals to DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound(
            "financialDerivativeTypeCode.notEquals=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        );

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode not equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeTypeCode.notEquals=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode in DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE or UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldBeFound(
            "financialDerivativeTypeCode.in=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE + "," + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        );

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeTypeCode.in=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode is not null
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeTypeCode.specified=true");

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode is null
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode contains DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeTypeCode.contains=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode contains UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound(
            "financialDerivativeTypeCode.contains=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode does not contain DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound(
            "financialDerivativeTypeCode.doesNotContain=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE
        );

        // Get all the financialDerivativeTypeCodeList where financialDerivativeTypeCode does not contain UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        defaultFinancialDerivativeTypeCodeShouldBeFound(
            "financialDerivativeTypeCode.doesNotContain=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType equals to DEFAULT_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeType.equals=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.equals=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType not equals to DEFAULT_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.notEquals=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType not equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeType.notEquals=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType in DEFAULT_FINANCIAL_DERIVATIVE_TYPE or UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldBeFound(
            "financialDerivativeType.in=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE + "," + UPDATED_FINANCIAL_DERIVATIVE_TYPE
        );

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType equals to UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.in=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType is not null
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeType.specified=true");

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType is null
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeContainsSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType contains DEFAULT_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeType.contains=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType contains UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.contains=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE);
    }

    @Test
    @Transactional
    void getAllFinancialDerivativeTypeCodesByFinancialDerivativeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType does not contain DEFAULT_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldNotBeFound("financialDerivativeType.doesNotContain=" + DEFAULT_FINANCIAL_DERIVATIVE_TYPE);

        // Get all the financialDerivativeTypeCodeList where financialDerivativeType does not contain UPDATED_FINANCIAL_DERIVATIVE_TYPE
        defaultFinancialDerivativeTypeCodeShouldBeFound("financialDerivativeType.doesNotContain=" + UPDATED_FINANCIAL_DERIVATIVE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinancialDerivativeTypeCodeShouldBeFound(String filter) throws Exception {
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialDerivativeTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeTypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinancialDerivativeTypeCodeShouldNotBeFound(String filter) throws Exception {
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFinancialDerivativeTypeCode() throws Exception {
        // Get the financialDerivativeTypeCode
        restFinancialDerivativeTypeCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFinancialDerivativeTypeCode() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();

        // Update the financialDerivativeTypeCode
        FinancialDerivativeTypeCode updatedFinancialDerivativeTypeCode = financialDerivativeTypeCodeRepository
            .findById(financialDerivativeTypeCode.getId())
            .get();
        // Disconnect from session so that the updates on updatedFinancialDerivativeTypeCode are not directly saved in db
        em.detach(updatedFinancialDerivativeTypeCode);
        updatedFinancialDerivativeTypeCode
            .financialDerivativeTypeCode(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE)
            .financialDerivativeType(UPDATED_FINANCIAL_DERIVATIVE_TYPE)
            .financialDerivativeTypeDetails(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            updatedFinancialDerivativeTypeCode
        );

        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialDerivativeTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        FinancialDerivativeTypeCode testFinancialDerivativeTypeCode = financialDerivativeTypeCodeList.get(
            financialDerivativeTypeCodeList.size() - 1
        );
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeType()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeDetails())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository).save(testFinancialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void putNonExistingFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialDerivativeTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void partialUpdateFinancialDerivativeTypeCodeWithPatch() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();

        // Update the financialDerivativeTypeCode using partial update
        FinancialDerivativeTypeCode partialUpdatedFinancialDerivativeTypeCode = new FinancialDerivativeTypeCode();
        partialUpdatedFinancialDerivativeTypeCode.setId(financialDerivativeTypeCode.getId());

        partialUpdatedFinancialDerivativeTypeCode
            .financialDerivativeTypeCode(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE)
            .financialDerivativeType(UPDATED_FINANCIAL_DERIVATIVE_TYPE)
            .financialDerivativeTypeDetails(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);

        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialDerivativeTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialDerivativeTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        FinancialDerivativeTypeCode testFinancialDerivativeTypeCode = financialDerivativeTypeCodeList.get(
            financialDerivativeTypeCodeList.size() - 1
        );
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeType()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeDetails())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFinancialDerivativeTypeCodeWithPatch() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();

        // Update the financialDerivativeTypeCode using partial update
        FinancialDerivativeTypeCode partialUpdatedFinancialDerivativeTypeCode = new FinancialDerivativeTypeCode();
        partialUpdatedFinancialDerivativeTypeCode.setId(financialDerivativeTypeCode.getId());

        partialUpdatedFinancialDerivativeTypeCode
            .financialDerivativeTypeCode(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE)
            .financialDerivativeType(UPDATED_FINANCIAL_DERIVATIVE_TYPE)
            .financialDerivativeTypeDetails(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);

        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialDerivativeTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialDerivativeTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        FinancialDerivativeTypeCode testFinancialDerivativeTypeCode = financialDerivativeTypeCodeList.get(
            financialDerivativeTypeCodeList.size() - 1
        );
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeCode()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_CODE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeType()).isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE);
        assertThat(testFinancialDerivativeTypeCode.getFinancialDerivativeTypeDetails())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialDerivativeTypeCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancialDerivativeTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = financialDerivativeTypeCodeRepository.findAll().size();
        financialDerivativeTypeCode.setId(count.incrementAndGet());

        // Create the FinancialDerivativeTypeCode
        FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeMapper.toDto(
            financialDerivativeTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialDerivativeTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialDerivativeTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialDerivativeTypeCode in the database
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(0)).save(financialDerivativeTypeCode);
    }

    @Test
    @Transactional
    void deleteFinancialDerivativeTypeCode() throws Exception {
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);

        int databaseSizeBeforeDelete = financialDerivativeTypeCodeRepository.findAll().size();

        // Delete the financialDerivativeTypeCode
        restFinancialDerivativeTypeCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialDerivativeTypeCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinancialDerivativeTypeCode> financialDerivativeTypeCodeList = financialDerivativeTypeCodeRepository.findAll();
        assertThat(financialDerivativeTypeCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FinancialDerivativeTypeCode in Elasticsearch
        verify(mockFinancialDerivativeTypeCodeSearchRepository, times(1)).deleteById(financialDerivativeTypeCode.getId());
    }

    @Test
    @Transactional
    void searchFinancialDerivativeTypeCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        financialDerivativeTypeCodeRepository.saveAndFlush(financialDerivativeTypeCode);
        when(mockFinancialDerivativeTypeCodeSearchRepository.search("id:" + financialDerivativeTypeCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(financialDerivativeTypeCode), PageRequest.of(0, 1), 1));

        // Search the financialDerivativeTypeCode
        restFinancialDerivativeTypeCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + financialDerivativeTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialDerivativeTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].financialDerivativeTypeCode").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].financialDerivativeType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeTypeDetails").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_TYPE_DETAILS.toString()))
            );
    }
}
