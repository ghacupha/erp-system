package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.AgriculturalEnterpriseActivityType;
import io.github.erp.repository.AgriculturalEnterpriseActivityTypeRepository;
import io.github.erp.repository.search.AgriculturalEnterpriseActivityTypeSearchRepository;
import io.github.erp.service.dto.AgriculturalEnterpriseActivityTypeDTO;
import io.github.erp.service.mapper.AgriculturalEnterpriseActivityTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.AgriculturalEnterpriseActivityTypeResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Integration tests for the {@link AgriculturalEnterpriseActivityTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class AgriculturalEnterpriseActivityTypeResourceIT {

    private static final String DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/agricultural-enterprise-activity-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/agricultural-enterprise-activity-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository;

    @Autowired
    private AgriculturalEnterpriseActivityTypeMapper agriculturalEnterpriseActivityTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AgriculturalEnterpriseActivityTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AgriculturalEnterpriseActivityTypeSearchRepository mockAgriculturalEnterpriseActivityTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgriculturalEnterpriseActivityTypeMockMvc;

    private AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalEnterpriseActivityType createEntity(EntityManager em) {
        AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityType = new AgriculturalEnterpriseActivityType()
            .agriculturalEnterpriseActivityTypeCode(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE)
            .agriculturalEnterpriseActivityType(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)
            .agriculturalEnterpriseActivityTypeDescription(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);
        return agriculturalEnterpriseActivityType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalEnterpriseActivityType createUpdatedEntity(EntityManager em) {
        AgriculturalEnterpriseActivityType agriculturalEnterpriseActivityType = new AgriculturalEnterpriseActivityType()
            .agriculturalEnterpriseActivityTypeCode(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE)
            .agriculturalEnterpriseActivityType(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)
            .agriculturalEnterpriseActivityTypeDescription(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);
        return agriculturalEnterpriseActivityType;
    }

    @BeforeEach
    public void initTest() {
        agriculturalEnterpriseActivityType = createEntity(em);
    }

    @Test
    @Transactional
    void createAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeCreate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AgriculturalEnterpriseActivityType testAgriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeList.get(
            agriculturalEnterpriseActivityTypeList.size() - 1
        );
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeCode())
            .isEqualTo(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityType())
            .isEqualTo(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeDescription())
            .isEqualTo(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(1)).save(testAgriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void createAgriculturalEnterpriseActivityTypeWithExistingId() throws Exception {
        // Create the AgriculturalEnterpriseActivityType with an existing ID
        agriculturalEnterpriseActivityType.setId(1L);
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        int databaseSizeBeforeCreate = agriculturalEnterpriseActivityTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void checkAgriculturalEnterpriseActivityTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        // set the field null
        agriculturalEnterpriseActivityType.setAgriculturalEnterpriseActivityTypeCode(null);

        // Create the AgriculturalEnterpriseActivityType, which fails.
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgriculturalEnterpriseActivityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        // set the field null
        agriculturalEnterpriseActivityType.setAgriculturalEnterpriseActivityType(null);

        // Create the AgriculturalEnterpriseActivityType, which fails.
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypes() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalEnterpriseActivityType.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeCode").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].agriculturalEnterpriseActivityType").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeDescription")
                    .value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getAgriculturalEnterpriseActivityType() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get the agriculturalEnterpriseActivityType
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, agriculturalEnterpriseActivityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agriculturalEnterpriseActivityType.getId().intValue()))
            .andExpect(jsonPath("$.agriculturalEnterpriseActivityTypeCode").value(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE))
            .andExpect(jsonPath("$.agriculturalEnterpriseActivityType").value(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE))
            .andExpect(
                jsonPath("$.agriculturalEnterpriseActivityTypeDescription")
                    .value(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION.toString())
            );
    }

    @Test
    @Transactional
    void getAgriculturalEnterpriseActivityTypesByIdFiltering() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        Long id = agriculturalEnterpriseActivityType.getId();

        defaultAgriculturalEnterpriseActivityTypeShouldBeFound("id.equals=" + id);
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAgriculturalEnterpriseActivityTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAgriculturalEnterpriseActivityTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode equals to DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityTypeCode.equals=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityTypeCode.equals=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode not equals to DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityTypeCode.notEquals=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode not equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityTypeCode.notEquals=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode in DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE or UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityTypeCode.in=" +
            DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE +
            "," +
            UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityTypeCode.in=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode is not null
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound("agriculturalEnterpriseActivityTypeCode.specified=true");

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode is null
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound("agriculturalEnterpriseActivityTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode contains DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityTypeCode.contains=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode contains UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityTypeCode.contains=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode does not contain DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityTypeCode.doesNotContain=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityTypeCode does not contain UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityTypeCode.doesNotContain=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType equals to DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityType.equals=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityType.equals=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType not equals to DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityType.notEquals=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType not equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityType.notEquals=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType in DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE or UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityType.in=" +
            DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE +
            "," +
            UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType equals to UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityType.in=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType is not null
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound("agriculturalEnterpriseActivityType.specified=true");

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType is null
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound("agriculturalEnterpriseActivityType.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeContainsSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType contains DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityType.contains=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType contains UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityType.contains=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalEnterpriseActivityTypesByAgriculturalEnterpriseActivityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType does not contain DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(
            "agriculturalEnterpriseActivityType.doesNotContain=" + DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );

        // Get all the agriculturalEnterpriseActivityTypeList where agriculturalEnterpriseActivityType does not contain UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        defaultAgriculturalEnterpriseActivityTypeShouldBeFound(
            "agriculturalEnterpriseActivityType.doesNotContain=" + UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgriculturalEnterpriseActivityTypeShouldBeFound(String filter) throws Exception {
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalEnterpriseActivityType.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeCode").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].agriculturalEnterpriseActivityType").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeDescription")
                    .value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgriculturalEnterpriseActivityTypeShouldNotBeFound(String filter) throws Exception {
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgriculturalEnterpriseActivityType() throws Exception {
        // Get the agriculturalEnterpriseActivityType
        restAgriculturalEnterpriseActivityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgriculturalEnterpriseActivityType() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();

        // Update the agriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityType updatedAgriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeRepository
            .findById(agriculturalEnterpriseActivityType.getId())
            .get();
        // Disconnect from session so that the updates on updatedAgriculturalEnterpriseActivityType are not directly saved in db
        em.detach(updatedAgriculturalEnterpriseActivityType);
        updatedAgriculturalEnterpriseActivityType
            .agriculturalEnterpriseActivityTypeCode(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE)
            .agriculturalEnterpriseActivityType(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)
            .agriculturalEnterpriseActivityTypeDescription(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            updatedAgriculturalEnterpriseActivityType
        );

        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalEnterpriseActivityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);
        AgriculturalEnterpriseActivityType testAgriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeList.get(
            agriculturalEnterpriseActivityTypeList.size() - 1
        );
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeCode())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityType())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeDescription())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository).save(testAgriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void putNonExistingAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalEnterpriseActivityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void partialUpdateAgriculturalEnterpriseActivityTypeWithPatch() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();

        // Update the agriculturalEnterpriseActivityType using partial update
        AgriculturalEnterpriseActivityType partialUpdatedAgriculturalEnterpriseActivityType = new AgriculturalEnterpriseActivityType();
        partialUpdatedAgriculturalEnterpriseActivityType.setId(agriculturalEnterpriseActivityType.getId());

        partialUpdatedAgriculturalEnterpriseActivityType
            .agriculturalEnterpriseActivityTypeCode(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE)
            .agriculturalEnterpriseActivityType(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)
            .agriculturalEnterpriseActivityTypeDescription(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);

        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalEnterpriseActivityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgriculturalEnterpriseActivityType))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);
        AgriculturalEnterpriseActivityType testAgriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeList.get(
            agriculturalEnterpriseActivityTypeList.size() - 1
        );
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeCode())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityType())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeDescription())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAgriculturalEnterpriseActivityTypeWithPatch() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();

        // Update the agriculturalEnterpriseActivityType using partial update
        AgriculturalEnterpriseActivityType partialUpdatedAgriculturalEnterpriseActivityType = new AgriculturalEnterpriseActivityType();
        partialUpdatedAgriculturalEnterpriseActivityType.setId(agriculturalEnterpriseActivityType.getId());

        partialUpdatedAgriculturalEnterpriseActivityType
            .agriculturalEnterpriseActivityTypeCode(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE)
            .agriculturalEnterpriseActivityType(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)
            .agriculturalEnterpriseActivityTypeDescription(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);

        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalEnterpriseActivityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgriculturalEnterpriseActivityType))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);
        AgriculturalEnterpriseActivityType testAgriculturalEnterpriseActivityType = agriculturalEnterpriseActivityTypeList.get(
            agriculturalEnterpriseActivityTypeList.size() - 1
        );
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeCode())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityType())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE);
        assertThat(testAgriculturalEnterpriseActivityType.getAgriculturalEnterpriseActivityTypeDescription())
            .isEqualTo(UPDATED_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agriculturalEnterpriseActivityTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgriculturalEnterpriseActivityType() throws Exception {
        int databaseSizeBeforeUpdate = agriculturalEnterpriseActivityTypeRepository.findAll().size();
        agriculturalEnterpriseActivityType.setId(count.incrementAndGet());

        // Create the AgriculturalEnterpriseActivityType
        AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeMapper.toDto(
            agriculturalEnterpriseActivityType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agriculturalEnterpriseActivityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalEnterpriseActivityType in the database
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(0)).save(agriculturalEnterpriseActivityType);
    }

    @Test
    @Transactional
    void deleteAgriculturalEnterpriseActivityType() throws Exception {
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);

        int databaseSizeBeforeDelete = agriculturalEnterpriseActivityTypeRepository.findAll().size();

        // Delete the agriculturalEnterpriseActivityType
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, agriculturalEnterpriseActivityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgriculturalEnterpriseActivityType> agriculturalEnterpriseActivityTypeList = agriculturalEnterpriseActivityTypeRepository.findAll();
        assertThat(agriculturalEnterpriseActivityTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AgriculturalEnterpriseActivityType in Elasticsearch
        verify(mockAgriculturalEnterpriseActivityTypeSearchRepository, times(1)).deleteById(agriculturalEnterpriseActivityType.getId());
    }

    @Test
    @Transactional
    void searchAgriculturalEnterpriseActivityType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        agriculturalEnterpriseActivityTypeRepository.saveAndFlush(agriculturalEnterpriseActivityType);
        when(
            mockAgriculturalEnterpriseActivityTypeSearchRepository.search(
                "id:" + agriculturalEnterpriseActivityType.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(agriculturalEnterpriseActivityType), PageRequest.of(0, 1), 1));

        // Search the agriculturalEnterpriseActivityType
        restAgriculturalEnterpriseActivityTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + agriculturalEnterpriseActivityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalEnterpriseActivityType.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeCode").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].agriculturalEnterpriseActivityType").value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].agriculturalEnterpriseActivityTypeDescription")
                    .value(hasItem(DEFAULT_AGRICULTURAL_ENTERPRISE_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );
    }
}
