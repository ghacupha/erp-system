package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.CrbProductServiceFeeType;
import io.github.erp.repository.CrbProductServiceFeeTypeRepository;
import io.github.erp.repository.search.CrbProductServiceFeeTypeSearchRepository;
import io.github.erp.service.criteria.CrbProductServiceFeeTypeCriteria;
import io.github.erp.service.dto.CrbProductServiceFeeTypeDTO;
import io.github.erp.service.mapper.CrbProductServiceFeeTypeMapper;
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
 * Integration tests for the {@link CrbProductServiceFeeTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbProductServiceFeeTypeResourceIT {

    private static final String DEFAULT_CHARGE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHARGE_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CHARGE_TYPE_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_TYPE_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-product-service-fee-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-product-service-fee-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository;

    @Autowired
    private CrbProductServiceFeeTypeMapper crbProductServiceFeeTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbProductServiceFeeTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbProductServiceFeeTypeSearchRepository mockCrbProductServiceFeeTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbProductServiceFeeTypeMockMvc;

    private CrbProductServiceFeeType crbProductServiceFeeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbProductServiceFeeType createEntity(EntityManager em) {
        CrbProductServiceFeeType crbProductServiceFeeType = new CrbProductServiceFeeType()
            .chargeTypeCode(DEFAULT_CHARGE_TYPE_CODE)
            .chargeTypeDescription(DEFAULT_CHARGE_TYPE_DESCRIPTION)
            .chargeTypeCategory(DEFAULT_CHARGE_TYPE_CATEGORY);
        return crbProductServiceFeeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbProductServiceFeeType createUpdatedEntity(EntityManager em) {
        CrbProductServiceFeeType crbProductServiceFeeType = new CrbProductServiceFeeType()
            .chargeTypeCode(UPDATED_CHARGE_TYPE_CODE)
            .chargeTypeDescription(UPDATED_CHARGE_TYPE_DESCRIPTION)
            .chargeTypeCategory(UPDATED_CHARGE_TYPE_CATEGORY);
        return crbProductServiceFeeType;
    }

    @BeforeEach
    public void initTest() {
        crbProductServiceFeeType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeCreate = crbProductServiceFeeTypeRepository.findAll().size();
        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbProductServiceFeeType testCrbProductServiceFeeType = crbProductServiceFeeTypeList.get(crbProductServiceFeeTypeList.size() - 1);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCode()).isEqualTo(DEFAULT_CHARGE_TYPE_CODE);
        assertThat(testCrbProductServiceFeeType.getChargeTypeDescription()).isEqualTo(DEFAULT_CHARGE_TYPE_DESCRIPTION);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCategory()).isEqualTo(DEFAULT_CHARGE_TYPE_CATEGORY);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(1)).save(testCrbProductServiceFeeType);
    }

    @Test
    @Transactional
    void createCrbProductServiceFeeTypeWithExistingId() throws Exception {
        // Create the CrbProductServiceFeeType with an existing ID
        crbProductServiceFeeType.setId(1L);
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        int databaseSizeBeforeCreate = crbProductServiceFeeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void checkChargeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbProductServiceFeeTypeRepository.findAll().size();
        // set the field null
        crbProductServiceFeeType.setChargeTypeCode(null);

        // Create the CrbProductServiceFeeType, which fails.
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        restCrbProductServiceFeeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChargeTypeCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbProductServiceFeeTypeRepository.findAll().size();
        // set the field null
        crbProductServiceFeeType.setChargeTypeCategory(null);

        // Create the CrbProductServiceFeeType, which fails.
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        restCrbProductServiceFeeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypes() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbProductServiceFeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargeTypeCode").value(hasItem(DEFAULT_CHARGE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].chargeTypeDescription").value(hasItem(DEFAULT_CHARGE_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].chargeTypeCategory").value(hasItem(DEFAULT_CHARGE_TYPE_CATEGORY)));
    }

    @Test
    @Transactional
    void getCrbProductServiceFeeType() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get the crbProductServiceFeeType
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbProductServiceFeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbProductServiceFeeType.getId().intValue()))
            .andExpect(jsonPath("$.chargeTypeCode").value(DEFAULT_CHARGE_TYPE_CODE))
            .andExpect(jsonPath("$.chargeTypeDescription").value(DEFAULT_CHARGE_TYPE_DESCRIPTION))
            .andExpect(jsonPath("$.chargeTypeCategory").value(DEFAULT_CHARGE_TYPE_CATEGORY));
    }

    @Test
    @Transactional
    void getCrbProductServiceFeeTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        Long id = crbProductServiceFeeType.getId();

        defaultCrbProductServiceFeeTypeShouldBeFound("id.equals=" + id);
        defaultCrbProductServiceFeeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbProductServiceFeeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbProductServiceFeeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbProductServiceFeeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbProductServiceFeeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode equals to DEFAULT_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.equals=" + DEFAULT_CHARGE_TYPE_CODE);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode equals to UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.equals=" + UPDATED_CHARGE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode not equals to DEFAULT_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.notEquals=" + DEFAULT_CHARGE_TYPE_CODE);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode not equals to UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.notEquals=" + UPDATED_CHARGE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode in DEFAULT_CHARGE_TYPE_CODE or UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.in=" + DEFAULT_CHARGE_TYPE_CODE + "," + UPDATED_CHARGE_TYPE_CODE);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode equals to UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.in=" + UPDATED_CHARGE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode is not null
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.specified=true");

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode is null
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode contains DEFAULT_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.contains=" + DEFAULT_CHARGE_TYPE_CODE);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode contains UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.contains=" + UPDATED_CHARGE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode does not contain DEFAULT_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCode.doesNotContain=" + DEFAULT_CHARGE_TYPE_CODE);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCode does not contain UPDATED_CHARGE_TYPE_CODE
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCode.doesNotContain=" + UPDATED_CHARGE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription equals to DEFAULT_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeDescription.equals=" + DEFAULT_CHARGE_TYPE_DESCRIPTION);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription equals to UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.equals=" + UPDATED_CHARGE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription not equals to DEFAULT_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.notEquals=" + DEFAULT_CHARGE_TYPE_DESCRIPTION);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription not equals to UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeDescription.notEquals=" + UPDATED_CHARGE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription in DEFAULT_CHARGE_TYPE_DESCRIPTION or UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldBeFound(
            "chargeTypeDescription.in=" + DEFAULT_CHARGE_TYPE_DESCRIPTION + "," + UPDATED_CHARGE_TYPE_DESCRIPTION
        );

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription equals to UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.in=" + UPDATED_CHARGE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription is not null
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeDescription.specified=true");

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription is null
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription contains DEFAULT_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeDescription.contains=" + DEFAULT_CHARGE_TYPE_DESCRIPTION);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription contains UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.contains=" + UPDATED_CHARGE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription does not contain DEFAULT_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeDescription.doesNotContain=" + DEFAULT_CHARGE_TYPE_DESCRIPTION);

        // Get all the crbProductServiceFeeTypeList where chargeTypeDescription does not contain UPDATED_CHARGE_TYPE_DESCRIPTION
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeDescription.doesNotContain=" + UPDATED_CHARGE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory equals to DEFAULT_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCategory.equals=" + DEFAULT_CHARGE_TYPE_CATEGORY);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory equals to UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.equals=" + UPDATED_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory not equals to DEFAULT_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.notEquals=" + DEFAULT_CHARGE_TYPE_CATEGORY);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory not equals to UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCategory.notEquals=" + UPDATED_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory in DEFAULT_CHARGE_TYPE_CATEGORY or UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldBeFound(
            "chargeTypeCategory.in=" + DEFAULT_CHARGE_TYPE_CATEGORY + "," + UPDATED_CHARGE_TYPE_CATEGORY
        );

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory equals to UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.in=" + UPDATED_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory is not null
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCategory.specified=true");

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory is null
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory contains DEFAULT_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCategory.contains=" + DEFAULT_CHARGE_TYPE_CATEGORY);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory contains UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.contains=" + UPDATED_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbProductServiceFeeTypesByChargeTypeCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory does not contain DEFAULT_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldNotBeFound("chargeTypeCategory.doesNotContain=" + DEFAULT_CHARGE_TYPE_CATEGORY);

        // Get all the crbProductServiceFeeTypeList where chargeTypeCategory does not contain UPDATED_CHARGE_TYPE_CATEGORY
        defaultCrbProductServiceFeeTypeShouldBeFound("chargeTypeCategory.doesNotContain=" + UPDATED_CHARGE_TYPE_CATEGORY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbProductServiceFeeTypeShouldBeFound(String filter) throws Exception {
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbProductServiceFeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargeTypeCode").value(hasItem(DEFAULT_CHARGE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].chargeTypeDescription").value(hasItem(DEFAULT_CHARGE_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].chargeTypeCategory").value(hasItem(DEFAULT_CHARGE_TYPE_CATEGORY)));

        // Check, that the count call also returns 1
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbProductServiceFeeTypeShouldNotBeFound(String filter) throws Exception {
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbProductServiceFeeType() throws Exception {
        // Get the crbProductServiceFeeType
        restCrbProductServiceFeeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbProductServiceFeeType() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();

        // Update the crbProductServiceFeeType
        CrbProductServiceFeeType updatedCrbProductServiceFeeType = crbProductServiceFeeTypeRepository
            .findById(crbProductServiceFeeType.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbProductServiceFeeType are not directly saved in db
        em.detach(updatedCrbProductServiceFeeType);
        updatedCrbProductServiceFeeType
            .chargeTypeCode(UPDATED_CHARGE_TYPE_CODE)
            .chargeTypeDescription(UPDATED_CHARGE_TYPE_DESCRIPTION)
            .chargeTypeCategory(UPDATED_CHARGE_TYPE_CATEGORY);
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(updatedCrbProductServiceFeeType);

        restCrbProductServiceFeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbProductServiceFeeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbProductServiceFeeType testCrbProductServiceFeeType = crbProductServiceFeeTypeList.get(crbProductServiceFeeTypeList.size() - 1);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCode()).isEqualTo(UPDATED_CHARGE_TYPE_CODE);
        assertThat(testCrbProductServiceFeeType.getChargeTypeDescription()).isEqualTo(UPDATED_CHARGE_TYPE_DESCRIPTION);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCategory()).isEqualTo(UPDATED_CHARGE_TYPE_CATEGORY);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository).save(testCrbProductServiceFeeType);
    }

    @Test
    @Transactional
    void putNonExistingCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbProductServiceFeeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void partialUpdateCrbProductServiceFeeTypeWithPatch() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();

        // Update the crbProductServiceFeeType using partial update
        CrbProductServiceFeeType partialUpdatedCrbProductServiceFeeType = new CrbProductServiceFeeType();
        partialUpdatedCrbProductServiceFeeType.setId(crbProductServiceFeeType.getId());

        partialUpdatedCrbProductServiceFeeType.chargeTypeCode(UPDATED_CHARGE_TYPE_CODE);

        restCrbProductServiceFeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbProductServiceFeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbProductServiceFeeType))
            )
            .andExpect(status().isOk());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbProductServiceFeeType testCrbProductServiceFeeType = crbProductServiceFeeTypeList.get(crbProductServiceFeeTypeList.size() - 1);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCode()).isEqualTo(UPDATED_CHARGE_TYPE_CODE);
        assertThat(testCrbProductServiceFeeType.getChargeTypeDescription()).isEqualTo(DEFAULT_CHARGE_TYPE_DESCRIPTION);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCategory()).isEqualTo(DEFAULT_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateCrbProductServiceFeeTypeWithPatch() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();

        // Update the crbProductServiceFeeType using partial update
        CrbProductServiceFeeType partialUpdatedCrbProductServiceFeeType = new CrbProductServiceFeeType();
        partialUpdatedCrbProductServiceFeeType.setId(crbProductServiceFeeType.getId());

        partialUpdatedCrbProductServiceFeeType
            .chargeTypeCode(UPDATED_CHARGE_TYPE_CODE)
            .chargeTypeDescription(UPDATED_CHARGE_TYPE_DESCRIPTION)
            .chargeTypeCategory(UPDATED_CHARGE_TYPE_CATEGORY);

        restCrbProductServiceFeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbProductServiceFeeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbProductServiceFeeType))
            )
            .andExpect(status().isOk());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbProductServiceFeeType testCrbProductServiceFeeType = crbProductServiceFeeTypeList.get(crbProductServiceFeeTypeList.size() - 1);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCode()).isEqualTo(UPDATED_CHARGE_TYPE_CODE);
        assertThat(testCrbProductServiceFeeType.getChargeTypeDescription()).isEqualTo(UPDATED_CHARGE_TYPE_DESCRIPTION);
        assertThat(testCrbProductServiceFeeType.getChargeTypeCategory()).isEqualTo(UPDATED_CHARGE_TYPE_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbProductServiceFeeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbProductServiceFeeType() throws Exception {
        int databaseSizeBeforeUpdate = crbProductServiceFeeTypeRepository.findAll().size();
        crbProductServiceFeeType.setId(count.incrementAndGet());

        // Create the CrbProductServiceFeeType
        CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbProductServiceFeeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbProductServiceFeeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbProductServiceFeeType in the database
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(0)).save(crbProductServiceFeeType);
    }

    @Test
    @Transactional
    void deleteCrbProductServiceFeeType() throws Exception {
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);

        int databaseSizeBeforeDelete = crbProductServiceFeeTypeRepository.findAll().size();

        // Delete the crbProductServiceFeeType
        restCrbProductServiceFeeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbProductServiceFeeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbProductServiceFeeType> crbProductServiceFeeTypeList = crbProductServiceFeeTypeRepository.findAll();
        assertThat(crbProductServiceFeeTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbProductServiceFeeType in Elasticsearch
        verify(mockCrbProductServiceFeeTypeSearchRepository, times(1)).deleteById(crbProductServiceFeeType.getId());
    }

    @Test
    @Transactional
    void searchCrbProductServiceFeeType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbProductServiceFeeTypeRepository.saveAndFlush(crbProductServiceFeeType);
        when(mockCrbProductServiceFeeTypeSearchRepository.search("id:" + crbProductServiceFeeType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbProductServiceFeeType), PageRequest.of(0, 1), 1));

        // Search the crbProductServiceFeeType
        restCrbProductServiceFeeTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbProductServiceFeeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbProductServiceFeeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargeTypeCode").value(hasItem(DEFAULT_CHARGE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].chargeTypeDescription").value(hasItem(DEFAULT_CHARGE_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].chargeTypeCategory").value(hasItem(DEFAULT_CHARGE_TYPE_CATEGORY)));
    }
}
