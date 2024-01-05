package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import io.github.erp.domain.DerivativeUnderlyingAsset;
import io.github.erp.repository.DerivativeUnderlyingAssetRepository;
import io.github.erp.repository.search.DerivativeUnderlyingAssetSearchRepository;
import io.github.erp.service.criteria.DerivativeUnderlyingAssetCriteria;
import io.github.erp.service.dto.DerivativeUnderlyingAssetDTO;
import io.github.erp.service.mapper.DerivativeUnderlyingAssetMapper;
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
 * Integration tests for the {@link DerivativeUnderlyingAssetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DerivativeUnderlyingAssetResourceIT {

    private static final String DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/derivative-underlying-assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/derivative-underlying-assets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository;

    @Autowired
    private DerivativeUnderlyingAssetMapper derivativeUnderlyingAssetMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DerivativeUnderlyingAssetSearchRepositoryMockConfiguration
     */
    @Autowired
    private DerivativeUnderlyingAssetSearchRepository mockDerivativeUnderlyingAssetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDerivativeUnderlyingAssetMockMvc;

    private DerivativeUnderlyingAsset derivativeUnderlyingAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DerivativeUnderlyingAsset createEntity(EntityManager em) {
        DerivativeUnderlyingAsset derivativeUnderlyingAsset = new DerivativeUnderlyingAsset()
            .derivativeUnderlyingAssetTypeCode(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)
            .financialDerivativeUnderlyingAssetType(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE)
            .derivativeUnderlyingAssetTypeDetails(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);
        return derivativeUnderlyingAsset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DerivativeUnderlyingAsset createUpdatedEntity(EntityManager em) {
        DerivativeUnderlyingAsset derivativeUnderlyingAsset = new DerivativeUnderlyingAsset()
            .derivativeUnderlyingAssetTypeCode(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)
            .financialDerivativeUnderlyingAssetType(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE)
            .derivativeUnderlyingAssetTypeDetails(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);
        return derivativeUnderlyingAsset;
    }

    @BeforeEach
    public void initTest() {
        derivativeUnderlyingAsset = createEntity(em);
    }

    @Test
    @Transactional
    void createDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeCreate = derivativeUnderlyingAssetRepository.findAll().size();
        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeCreate + 1);
        DerivativeUnderlyingAsset testDerivativeUnderlyingAsset = derivativeUnderlyingAssetList.get(
            derivativeUnderlyingAssetList.size() - 1
        );
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeCode())
            .isEqualTo(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE);
        assertThat(testDerivativeUnderlyingAsset.getFinancialDerivativeUnderlyingAssetType())
            .isEqualTo(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE);
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeDetails())
            .isEqualTo(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(1)).save(testDerivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void createDerivativeUnderlyingAssetWithExistingId() throws Exception {
        // Create the DerivativeUnderlyingAsset with an existing ID
        derivativeUnderlyingAsset.setId(1L);
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        int databaseSizeBeforeCreate = derivativeUnderlyingAssetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeCreate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void checkDerivativeUnderlyingAssetTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = derivativeUnderlyingAssetRepository.findAll().size();
        // set the field null
        derivativeUnderlyingAsset.setDerivativeUnderlyingAssetTypeCode(null);

        // Create the DerivativeUnderlyingAsset, which fails.
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        restDerivativeUnderlyingAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinancialDerivativeUnderlyingAssetTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = derivativeUnderlyingAssetRepository.findAll().size();
        // set the field null
        derivativeUnderlyingAsset.setFinancialDerivativeUnderlyingAssetType(null);

        // Create the DerivativeUnderlyingAsset, which fails.
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        restDerivativeUnderlyingAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssets() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeUnderlyingAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].derivativeUnderlyingAssetTypeCode").value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeUnderlyingAssetType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].derivativeUnderlyingAssetTypeDetails")
                    .value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getDerivativeUnderlyingAsset() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get the derivativeUnderlyingAsset
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, derivativeUnderlyingAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(derivativeUnderlyingAsset.getId().intValue()))
            .andExpect(jsonPath("$.derivativeUnderlyingAssetTypeCode").value(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE))
            .andExpect(jsonPath("$.financialDerivativeUnderlyingAssetType").value(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE))
            .andExpect(
                jsonPath("$.derivativeUnderlyingAssetTypeDetails").value(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS.toString())
            );
    }

    @Test
    @Transactional
    void getDerivativeUnderlyingAssetsByIdFiltering() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        Long id = derivativeUnderlyingAsset.getId();

        defaultDerivativeUnderlyingAssetShouldBeFound("id.equals=" + id);
        defaultDerivativeUnderlyingAssetShouldNotBeFound("id.notEquals=" + id);

        defaultDerivativeUnderlyingAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDerivativeUnderlyingAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultDerivativeUnderlyingAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDerivativeUnderlyingAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode equals to DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "derivativeUnderlyingAssetTypeCode.equals=" + DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode equals to UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "derivativeUnderlyingAssetTypeCode.equals=" + UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode not equals to DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "derivativeUnderlyingAssetTypeCode.notEquals=" + DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode not equals to UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "derivativeUnderlyingAssetTypeCode.notEquals=" + UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode in DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE or UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "derivativeUnderlyingAssetTypeCode.in=" +
            DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE +
            "," +
            UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode equals to UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "derivativeUnderlyingAssetTypeCode.in=" + UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode is not null
        defaultDerivativeUnderlyingAssetShouldBeFound("derivativeUnderlyingAssetTypeCode.specified=true");

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode is null
        defaultDerivativeUnderlyingAssetShouldNotBeFound("derivativeUnderlyingAssetTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode contains DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "derivativeUnderlyingAssetTypeCode.contains=" + DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode contains UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "derivativeUnderlyingAssetTypeCode.contains=" + UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByDerivativeUnderlyingAssetTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode does not contain DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "derivativeUnderlyingAssetTypeCode.doesNotContain=" + DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );

        // Get all the derivativeUnderlyingAssetList where derivativeUnderlyingAssetTypeCode does not contain UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "derivativeUnderlyingAssetTypeCode.doesNotContain=" + UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType equals to DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "financialDerivativeUnderlyingAssetType.equals=" + DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType equals to UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "financialDerivativeUnderlyingAssetType.equals=" + UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType not equals to DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "financialDerivativeUnderlyingAssetType.notEquals=" + DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType not equals to UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "financialDerivativeUnderlyingAssetType.notEquals=" + UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeIsInShouldWork() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType in DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE or UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "financialDerivativeUnderlyingAssetType.in=" +
            DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE +
            "," +
            UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType equals to UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "financialDerivativeUnderlyingAssetType.in=" + UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType is not null
        defaultDerivativeUnderlyingAssetShouldBeFound("financialDerivativeUnderlyingAssetType.specified=true");

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType is null
        defaultDerivativeUnderlyingAssetShouldNotBeFound("financialDerivativeUnderlyingAssetType.specified=false");
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeContainsSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType contains DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "financialDerivativeUnderlyingAssetType.contains=" + DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType contains UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "financialDerivativeUnderlyingAssetType.contains=" + UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDerivativeUnderlyingAssetsByFinancialDerivativeUnderlyingAssetTypeNotContainsSomething() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType does not contain DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldNotBeFound(
            "financialDerivativeUnderlyingAssetType.doesNotContain=" + DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );

        // Get all the derivativeUnderlyingAssetList where financialDerivativeUnderlyingAssetType does not contain UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        defaultDerivativeUnderlyingAssetShouldBeFound(
            "financialDerivativeUnderlyingAssetType.doesNotContain=" + UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDerivativeUnderlyingAssetShouldBeFound(String filter) throws Exception {
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeUnderlyingAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].derivativeUnderlyingAssetTypeCode").value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeUnderlyingAssetType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].derivativeUnderlyingAssetTypeDetails")
                    .value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDerivativeUnderlyingAssetShouldNotBeFound(String filter) throws Exception {
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDerivativeUnderlyingAsset() throws Exception {
        // Get the derivativeUnderlyingAsset
        restDerivativeUnderlyingAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDerivativeUnderlyingAsset() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();

        // Update the derivativeUnderlyingAsset
        DerivativeUnderlyingAsset updatedDerivativeUnderlyingAsset = derivativeUnderlyingAssetRepository
            .findById(derivativeUnderlyingAsset.getId())
            .get();
        // Disconnect from session so that the updates on updatedDerivativeUnderlyingAsset are not directly saved in db
        em.detach(updatedDerivativeUnderlyingAsset);
        updatedDerivativeUnderlyingAsset
            .derivativeUnderlyingAssetTypeCode(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)
            .financialDerivativeUnderlyingAssetType(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE)
            .derivativeUnderlyingAssetTypeDetails(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(updatedDerivativeUnderlyingAsset);

        restDerivativeUnderlyingAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, derivativeUnderlyingAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);
        DerivativeUnderlyingAsset testDerivativeUnderlyingAsset = derivativeUnderlyingAssetList.get(
            derivativeUnderlyingAssetList.size() - 1
        );
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeCode())
            .isEqualTo(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE);
        assertThat(testDerivativeUnderlyingAsset.getFinancialDerivativeUnderlyingAssetType())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE);
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeDetails())
            .isEqualTo(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository).save(testDerivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void putNonExistingDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, derivativeUnderlyingAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void putWithIdMismatchDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void partialUpdateDerivativeUnderlyingAssetWithPatch() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();

        // Update the derivativeUnderlyingAsset using partial update
        DerivativeUnderlyingAsset partialUpdatedDerivativeUnderlyingAsset = new DerivativeUnderlyingAsset();
        partialUpdatedDerivativeUnderlyingAsset.setId(derivativeUnderlyingAsset.getId());

        partialUpdatedDerivativeUnderlyingAsset.financialDerivativeUnderlyingAssetType(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE);

        restDerivativeUnderlyingAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDerivativeUnderlyingAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDerivativeUnderlyingAsset))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);
        DerivativeUnderlyingAsset testDerivativeUnderlyingAsset = derivativeUnderlyingAssetList.get(
            derivativeUnderlyingAssetList.size() - 1
        );
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeCode())
            .isEqualTo(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE);
        assertThat(testDerivativeUnderlyingAsset.getFinancialDerivativeUnderlyingAssetType())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE);
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeDetails())
            .isEqualTo(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateDerivativeUnderlyingAssetWithPatch() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();

        // Update the derivativeUnderlyingAsset using partial update
        DerivativeUnderlyingAsset partialUpdatedDerivativeUnderlyingAsset = new DerivativeUnderlyingAsset();
        partialUpdatedDerivativeUnderlyingAsset.setId(derivativeUnderlyingAsset.getId());

        partialUpdatedDerivativeUnderlyingAsset
            .derivativeUnderlyingAssetTypeCode(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)
            .financialDerivativeUnderlyingAssetType(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE)
            .derivativeUnderlyingAssetTypeDetails(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);

        restDerivativeUnderlyingAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDerivativeUnderlyingAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDerivativeUnderlyingAsset))
            )
            .andExpect(status().isOk());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);
        DerivativeUnderlyingAsset testDerivativeUnderlyingAsset = derivativeUnderlyingAssetList.get(
            derivativeUnderlyingAssetList.size() - 1
        );
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeCode())
            .isEqualTo(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE);
        assertThat(testDerivativeUnderlyingAsset.getFinancialDerivativeUnderlyingAssetType())
            .isEqualTo(UPDATED_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE);
        assertThat(testDerivativeUnderlyingAsset.getDerivativeUnderlyingAssetTypeDetails())
            .isEqualTo(UPDATED_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, derivativeUnderlyingAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDerivativeUnderlyingAsset() throws Exception {
        int databaseSizeBeforeUpdate = derivativeUnderlyingAssetRepository.findAll().size();
        derivativeUnderlyingAsset.setId(count.incrementAndGet());

        // Create the DerivativeUnderlyingAsset
        DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetMapper.toDto(derivativeUnderlyingAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDerivativeUnderlyingAssetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(derivativeUnderlyingAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DerivativeUnderlyingAsset in the database
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(0)).save(derivativeUnderlyingAsset);
    }

    @Test
    @Transactional
    void deleteDerivativeUnderlyingAsset() throws Exception {
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);

        int databaseSizeBeforeDelete = derivativeUnderlyingAssetRepository.findAll().size();

        // Delete the derivativeUnderlyingAsset
        restDerivativeUnderlyingAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, derivativeUnderlyingAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DerivativeUnderlyingAsset> derivativeUnderlyingAssetList = derivativeUnderlyingAssetRepository.findAll();
        assertThat(derivativeUnderlyingAssetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DerivativeUnderlyingAsset in Elasticsearch
        verify(mockDerivativeUnderlyingAssetSearchRepository, times(1)).deleteById(derivativeUnderlyingAsset.getId());
    }

    @Test
    @Transactional
    void searchDerivativeUnderlyingAsset() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        derivativeUnderlyingAssetRepository.saveAndFlush(derivativeUnderlyingAsset);
        when(mockDerivativeUnderlyingAssetSearchRepository.search("id:" + derivativeUnderlyingAsset.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(derivativeUnderlyingAsset), PageRequest.of(0, 1), 1));

        // Search the derivativeUnderlyingAsset
        restDerivativeUnderlyingAssetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + derivativeUnderlyingAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(derivativeUnderlyingAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].derivativeUnderlyingAssetTypeCode").value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].financialDerivativeUnderlyingAssetType").value(hasItem(DEFAULT_FINANCIAL_DERIVATIVE_UNDERLYING_ASSET_TYPE))
            )
            .andExpect(
                jsonPath("$.[*].derivativeUnderlyingAssetTypeDetails")
                    .value(hasItem(DEFAULT_DERIVATIVE_UNDERLYING_ASSET_TYPE_DETAILS.toString()))
            );
    }
}
