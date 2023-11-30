package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.CrbCreditFacilityType;
import io.github.erp.repository.CrbCreditFacilityTypeRepository;
import io.github.erp.repository.search.CrbCreditFacilityTypeSearchRepository;
import io.github.erp.service.criteria.CrbCreditFacilityTypeCriteria;
import io.github.erp.service.dto.CrbCreditFacilityTypeDTO;
import io.github.erp.service.mapper.CrbCreditFacilityTypeMapper;
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
 * Integration tests for the {@link CrbCreditFacilityTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbCreditFacilityTypeResourceIT {

    private static final String DEFAULT_CREDIT_FACILITY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_FACILITY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_FACILITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_FACILITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_FACILITY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_FACILITY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-credit-facility-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-credit-facility-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository;

    @Autowired
    private CrbCreditFacilityTypeMapper crbCreditFacilityTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbCreditFacilityTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbCreditFacilityTypeSearchRepository mockCrbCreditFacilityTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbCreditFacilityTypeMockMvc;

    private CrbCreditFacilityType crbCreditFacilityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCreditFacilityType createEntity(EntityManager em) {
        CrbCreditFacilityType crbCreditFacilityType = new CrbCreditFacilityType()
            .creditFacilityTypeCode(DEFAULT_CREDIT_FACILITY_TYPE_CODE)
            .creditFacilityType(DEFAULT_CREDIT_FACILITY_TYPE)
            .creditFacilityDescription(DEFAULT_CREDIT_FACILITY_DESCRIPTION);
        return crbCreditFacilityType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCreditFacilityType createUpdatedEntity(EntityManager em) {
        CrbCreditFacilityType crbCreditFacilityType = new CrbCreditFacilityType()
            .creditFacilityTypeCode(UPDATED_CREDIT_FACILITY_TYPE_CODE)
            .creditFacilityType(UPDATED_CREDIT_FACILITY_TYPE)
            .creditFacilityDescription(UPDATED_CREDIT_FACILITY_DESCRIPTION);
        return crbCreditFacilityType;
    }

    @BeforeEach
    public void initTest() {
        crbCreditFacilityType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeCreate = crbCreditFacilityTypeRepository.findAll().size();
        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);
        restCrbCreditFacilityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbCreditFacilityType testCrbCreditFacilityType = crbCreditFacilityTypeList.get(crbCreditFacilityTypeList.size() - 1);
        assertThat(testCrbCreditFacilityType.getCreditFacilityTypeCode()).isEqualTo(DEFAULT_CREDIT_FACILITY_TYPE_CODE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityType()).isEqualTo(DEFAULT_CREDIT_FACILITY_TYPE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityDescription()).isEqualTo(DEFAULT_CREDIT_FACILITY_DESCRIPTION);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(1)).save(testCrbCreditFacilityType);
    }

    @Test
    @Transactional
    void createCrbCreditFacilityTypeWithExistingId() throws Exception {
        // Create the CrbCreditFacilityType with an existing ID
        crbCreditFacilityType.setId(1L);
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        int databaseSizeBeforeCreate = crbCreditFacilityTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbCreditFacilityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void checkCreditFacilityTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCreditFacilityTypeRepository.findAll().size();
        // set the field null
        crbCreditFacilityType.setCreditFacilityTypeCode(null);

        // Create the CrbCreditFacilityType, which fails.
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        restCrbCreditFacilityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditFacilityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCreditFacilityTypeRepository.findAll().size();
        // set the field null
        crbCreditFacilityType.setCreditFacilityType(null);

        // Create the CrbCreditFacilityType, which fails.
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        restCrbCreditFacilityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypes() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditFacilityTypeCode").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditFacilityType").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE)))
            .andExpect(jsonPath("$.[*].creditFacilityDescription").value(hasItem(DEFAULT_CREDIT_FACILITY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCrbCreditFacilityType() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get the crbCreditFacilityType
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbCreditFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbCreditFacilityType.getId().intValue()))
            .andExpect(jsonPath("$.creditFacilityTypeCode").value(DEFAULT_CREDIT_FACILITY_TYPE_CODE))
            .andExpect(jsonPath("$.creditFacilityType").value(DEFAULT_CREDIT_FACILITY_TYPE))
            .andExpect(jsonPath("$.creditFacilityDescription").value(DEFAULT_CREDIT_FACILITY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCrbCreditFacilityTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        Long id = crbCreditFacilityType.getId();

        defaultCrbCreditFacilityTypeShouldBeFound("id.equals=" + id);
        defaultCrbCreditFacilityTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbCreditFacilityTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbCreditFacilityTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbCreditFacilityTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbCreditFacilityTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode equals to DEFAULT_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityTypeCode.equals=" + DEFAULT_CREDIT_FACILITY_TYPE_CODE);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode equals to UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.equals=" + UPDATED_CREDIT_FACILITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode not equals to DEFAULT_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.notEquals=" + DEFAULT_CREDIT_FACILITY_TYPE_CODE);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode not equals to UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityTypeCode.notEquals=" + UPDATED_CREDIT_FACILITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode in DEFAULT_CREDIT_FACILITY_TYPE_CODE or UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldBeFound(
            "creditFacilityTypeCode.in=" + DEFAULT_CREDIT_FACILITY_TYPE_CODE + "," + UPDATED_CREDIT_FACILITY_TYPE_CODE
        );

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode equals to UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.in=" + UPDATED_CREDIT_FACILITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode is not null
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityTypeCode.specified=true");

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode is null
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode contains DEFAULT_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityTypeCode.contains=" + DEFAULT_CREDIT_FACILITY_TYPE_CODE);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode contains UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.contains=" + UPDATED_CREDIT_FACILITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode does not contain DEFAULT_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityTypeCode.doesNotContain=" + DEFAULT_CREDIT_FACILITY_TYPE_CODE);

        // Get all the crbCreditFacilityTypeList where creditFacilityTypeCode does not contain UPDATED_CREDIT_FACILITY_TYPE_CODE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityTypeCode.doesNotContain=" + UPDATED_CREDIT_FACILITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType equals to DEFAULT_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityType.equals=" + DEFAULT_CREDIT_FACILITY_TYPE);

        // Get all the crbCreditFacilityTypeList where creditFacilityType equals to UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.equals=" + UPDATED_CREDIT_FACILITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType not equals to DEFAULT_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.notEquals=" + DEFAULT_CREDIT_FACILITY_TYPE);

        // Get all the crbCreditFacilityTypeList where creditFacilityType not equals to UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityType.notEquals=" + UPDATED_CREDIT_FACILITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType in DEFAULT_CREDIT_FACILITY_TYPE or UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldBeFound(
            "creditFacilityType.in=" + DEFAULT_CREDIT_FACILITY_TYPE + "," + UPDATED_CREDIT_FACILITY_TYPE
        );

        // Get all the crbCreditFacilityTypeList where creditFacilityType equals to UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.in=" + UPDATED_CREDIT_FACILITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType is not null
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityType.specified=true");

        // Get all the crbCreditFacilityTypeList where creditFacilityType is null
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeContainsSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType contains DEFAULT_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityType.contains=" + DEFAULT_CREDIT_FACILITY_TYPE);

        // Get all the crbCreditFacilityTypeList where creditFacilityType contains UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.contains=" + UPDATED_CREDIT_FACILITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCreditFacilityTypesByCreditFacilityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        // Get all the crbCreditFacilityTypeList where creditFacilityType does not contain DEFAULT_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldNotBeFound("creditFacilityType.doesNotContain=" + DEFAULT_CREDIT_FACILITY_TYPE);

        // Get all the crbCreditFacilityTypeList where creditFacilityType does not contain UPDATED_CREDIT_FACILITY_TYPE
        defaultCrbCreditFacilityTypeShouldBeFound("creditFacilityType.doesNotContain=" + UPDATED_CREDIT_FACILITY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbCreditFacilityTypeShouldBeFound(String filter) throws Exception {
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditFacilityTypeCode").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditFacilityType").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE)))
            .andExpect(jsonPath("$.[*].creditFacilityDescription").value(hasItem(DEFAULT_CREDIT_FACILITY_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbCreditFacilityTypeShouldNotBeFound(String filter) throws Exception {
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbCreditFacilityType() throws Exception {
        // Get the crbCreditFacilityType
        restCrbCreditFacilityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbCreditFacilityType() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();

        // Update the crbCreditFacilityType
        CrbCreditFacilityType updatedCrbCreditFacilityType = crbCreditFacilityTypeRepository.findById(crbCreditFacilityType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbCreditFacilityType are not directly saved in db
        em.detach(updatedCrbCreditFacilityType);
        updatedCrbCreditFacilityType
            .creditFacilityTypeCode(UPDATED_CREDIT_FACILITY_TYPE_CODE)
            .creditFacilityType(UPDATED_CREDIT_FACILITY_TYPE)
            .creditFacilityDescription(UPDATED_CREDIT_FACILITY_DESCRIPTION);
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(updatedCrbCreditFacilityType);

        restCrbCreditFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCreditFacilityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditFacilityType testCrbCreditFacilityType = crbCreditFacilityTypeList.get(crbCreditFacilityTypeList.size() - 1);
        assertThat(testCrbCreditFacilityType.getCreditFacilityTypeCode()).isEqualTo(UPDATED_CREDIT_FACILITY_TYPE_CODE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityType()).isEqualTo(UPDATED_CREDIT_FACILITY_TYPE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityDescription()).isEqualTo(UPDATED_CREDIT_FACILITY_DESCRIPTION);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository).save(testCrbCreditFacilityType);
    }

    @Test
    @Transactional
    void putNonExistingCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCreditFacilityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void partialUpdateCrbCreditFacilityTypeWithPatch() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();

        // Update the crbCreditFacilityType using partial update
        CrbCreditFacilityType partialUpdatedCrbCreditFacilityType = new CrbCreditFacilityType();
        partialUpdatedCrbCreditFacilityType.setId(crbCreditFacilityType.getId());

        partialUpdatedCrbCreditFacilityType.creditFacilityTypeCode(UPDATED_CREDIT_FACILITY_TYPE_CODE);

        restCrbCreditFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCreditFacilityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCreditFacilityType))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditFacilityType testCrbCreditFacilityType = crbCreditFacilityTypeList.get(crbCreditFacilityTypeList.size() - 1);
        assertThat(testCrbCreditFacilityType.getCreditFacilityTypeCode()).isEqualTo(UPDATED_CREDIT_FACILITY_TYPE_CODE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityType()).isEqualTo(DEFAULT_CREDIT_FACILITY_TYPE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityDescription()).isEqualTo(DEFAULT_CREDIT_FACILITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbCreditFacilityTypeWithPatch() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();

        // Update the crbCreditFacilityType using partial update
        CrbCreditFacilityType partialUpdatedCrbCreditFacilityType = new CrbCreditFacilityType();
        partialUpdatedCrbCreditFacilityType.setId(crbCreditFacilityType.getId());

        partialUpdatedCrbCreditFacilityType
            .creditFacilityTypeCode(UPDATED_CREDIT_FACILITY_TYPE_CODE)
            .creditFacilityType(UPDATED_CREDIT_FACILITY_TYPE)
            .creditFacilityDescription(UPDATED_CREDIT_FACILITY_DESCRIPTION);

        restCrbCreditFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCreditFacilityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCreditFacilityType))
            )
            .andExpect(status().isOk());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCreditFacilityType testCrbCreditFacilityType = crbCreditFacilityTypeList.get(crbCreditFacilityTypeList.size() - 1);
        assertThat(testCrbCreditFacilityType.getCreditFacilityTypeCode()).isEqualTo(UPDATED_CREDIT_FACILITY_TYPE_CODE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityType()).isEqualTo(UPDATED_CREDIT_FACILITY_TYPE);
        assertThat(testCrbCreditFacilityType.getCreditFacilityDescription()).isEqualTo(UPDATED_CREDIT_FACILITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbCreditFacilityTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbCreditFacilityType() throws Exception {
        int databaseSizeBeforeUpdate = crbCreditFacilityTypeRepository.findAll().size();
        crbCreditFacilityType.setId(count.incrementAndGet());

        // Create the CrbCreditFacilityType
        CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCreditFacilityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCreditFacilityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCreditFacilityType in the database
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(0)).save(crbCreditFacilityType);
    }

    @Test
    @Transactional
    void deleteCrbCreditFacilityType() throws Exception {
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);

        int databaseSizeBeforeDelete = crbCreditFacilityTypeRepository.findAll().size();

        // Delete the crbCreditFacilityType
        restCrbCreditFacilityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbCreditFacilityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbCreditFacilityType> crbCreditFacilityTypeList = crbCreditFacilityTypeRepository.findAll();
        assertThat(crbCreditFacilityTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbCreditFacilityType in Elasticsearch
        verify(mockCrbCreditFacilityTypeSearchRepository, times(1)).deleteById(crbCreditFacilityType.getId());
    }

    @Test
    @Transactional
    void searchCrbCreditFacilityType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbCreditFacilityTypeRepository.saveAndFlush(crbCreditFacilityType);
        when(mockCrbCreditFacilityTypeSearchRepository.search("id:" + crbCreditFacilityType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbCreditFacilityType), PageRequest.of(0, 1), 1));

        // Search the crbCreditFacilityType
        restCrbCreditFacilityTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbCreditFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCreditFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditFacilityTypeCode").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditFacilityType").value(hasItem(DEFAULT_CREDIT_FACILITY_TYPE)))
            .andExpect(jsonPath("$.[*].creditFacilityDescription").value(hasItem(DEFAULT_CREDIT_FACILITY_DESCRIPTION.toString())));
    }
}
