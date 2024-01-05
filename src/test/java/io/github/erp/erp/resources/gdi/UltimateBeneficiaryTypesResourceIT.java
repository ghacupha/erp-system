package io.github.erp.erp.resources.gdi;

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
import io.github.erp.domain.UltimateBeneficiaryTypes;
import io.github.erp.repository.UltimateBeneficiaryTypesRepository;
import io.github.erp.repository.search.UltimateBeneficiaryTypesSearchRepository;
import io.github.erp.service.dto.UltimateBeneficiaryTypesDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryTypesMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.TestUtil;
import io.github.erp.web.rest.UltimateBeneficiaryTypesResource;
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
 * Integration tests for the {@link UltimateBeneficiaryTypesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class UltimateBeneficiaryTypesResourceIT {

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/ultimate-beneficiary-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/ultimate-beneficiary-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository;

    @Autowired
    private UltimateBeneficiaryTypesMapper ultimateBeneficiaryTypesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.UltimateBeneficiaryTypesSearchRepositoryMockConfiguration
     */
    @Autowired
    private UltimateBeneficiaryTypesSearchRepository mockUltimateBeneficiaryTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUltimateBeneficiaryTypesMockMvc;

    private UltimateBeneficiaryTypes ultimateBeneficiaryTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UltimateBeneficiaryTypes createEntity(EntityManager em) {
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes = new UltimateBeneficiaryTypes()
            .ultimateBeneficiaryTypeCode(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE)
            .ultimateBeneficiaryType(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryTypeDetails(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS);
        return ultimateBeneficiaryTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UltimateBeneficiaryTypes createUpdatedEntity(EntityManager em) {
        UltimateBeneficiaryTypes ultimateBeneficiaryTypes = new UltimateBeneficiaryTypes()
            .ultimateBeneficiaryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);
        return ultimateBeneficiaryTypes;
    }

    @BeforeEach
    public void initTest() {
        ultimateBeneficiaryTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeCreate = ultimateBeneficiaryTypesRepository.findAll().size();
        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeCreate + 1);
        UltimateBeneficiaryTypes testUltimateBeneficiaryTypes = ultimateBeneficiaryTypesList.get(ultimateBeneficiaryTypesList.size() - 1);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeCode()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryType()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeDetails()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(1)).save(testUltimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void createUltimateBeneficiaryTypesWithExistingId() throws Exception {
        // Create the UltimateBeneficiaryTypes with an existing ID
        ultimateBeneficiaryTypes.setId(1L);
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        int databaseSizeBeforeCreate = ultimateBeneficiaryTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeCreate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void checkUltimateBeneficiaryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ultimateBeneficiaryTypesRepository.findAll().size();
        // set the field null
        ultimateBeneficiaryTypes.setUltimateBeneficiaryTypeCode(null);

        // Create the UltimateBeneficiaryTypes, which fails.
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        restUltimateBeneficiaryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUltimateBeneficiaryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ultimateBeneficiaryTypesRepository.findAll().size();
        // set the field null
        ultimateBeneficiaryTypes.setUltimateBeneficiaryType(null);

        // Create the UltimateBeneficiaryTypes, which fails.
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        restUltimateBeneficiaryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypes() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryTypeDetails").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getUltimateBeneficiaryTypes() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get the ultimateBeneficiaryTypes
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, ultimateBeneficiaryTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ultimateBeneficiaryTypes.getId().intValue()))
            .andExpect(jsonPath("$.ultimateBeneficiaryTypeCode").value(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE))
            .andExpect(jsonPath("$.ultimateBeneficiaryType").value(DEFAULT_ULTIMATE_BENEFICIARY_TYPE))
            .andExpect(jsonPath("$.ultimateBeneficiaryTypeDetails").value(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getUltimateBeneficiaryTypesByIdFiltering() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        Long id = ultimateBeneficiaryTypes.getId();

        defaultUltimateBeneficiaryTypesShouldBeFound("id.equals=" + id);
        defaultUltimateBeneficiaryTypesShouldNotBeFound("id.notEquals=" + id);

        defaultUltimateBeneficiaryTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUltimateBeneficiaryTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultUltimateBeneficiaryTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUltimateBeneficiaryTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryTypeCode.equals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryTypeCode.equals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode not equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryTypeCode.notEquals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode not equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryTypeCode.notEquals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode in DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE or UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldBeFound(
            "ultimateBeneficiaryTypeCode.in=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE + "," + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryTypeCode.in=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode is not null
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryTypeCode.specified=true");

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode is null
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode contains DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryTypeCode.contains=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode contains UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryTypeCode.contains=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode does not contain DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldNotBeFound(
            "ultimateBeneficiaryTypeCode.doesNotContain=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryTypeCode does not contain UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        defaultUltimateBeneficiaryTypesShouldBeFound(
            "ultimateBeneficiaryTypeCode.doesNotContain=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryType.equals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.equals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType not equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.notEquals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType not equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryType.notEquals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType in DEFAULT_ULTIMATE_BENEFICIARY_TYPE or UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldBeFound(
            "ultimateBeneficiaryType.in=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE + "," + UPDATED_ULTIMATE_BENEFICIARY_TYPE
        );

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.in=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType is not null
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryType.specified=true");

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType is null
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.specified=false");
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType contains DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryType.contains=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType contains UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.contains=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryTypesByUltimateBeneficiaryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType does not contain DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldNotBeFound("ultimateBeneficiaryType.doesNotContain=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryTypesList where ultimateBeneficiaryType does not contain UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryTypesShouldBeFound("ultimateBeneficiaryType.doesNotContain=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUltimateBeneficiaryTypesShouldBeFound(String filter) throws Exception {
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryTypeDetails").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUltimateBeneficiaryTypesShouldNotBeFound(String filter) throws Exception {
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUltimateBeneficiaryTypes() throws Exception {
        // Get the ultimateBeneficiaryTypes
        restUltimateBeneficiaryTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUltimateBeneficiaryTypes() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();

        // Update the ultimateBeneficiaryTypes
        UltimateBeneficiaryTypes updatedUltimateBeneficiaryTypes = ultimateBeneficiaryTypesRepository
            .findById(ultimateBeneficiaryTypes.getId())
            .get();
        // Disconnect from session so that the updates on updatedUltimateBeneficiaryTypes are not directly saved in db
        em.detach(updatedUltimateBeneficiaryTypes);
        updatedUltimateBeneficiaryTypes
            .ultimateBeneficiaryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(updatedUltimateBeneficiaryTypes);

        restUltimateBeneficiaryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ultimateBeneficiaryTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryTypes testUltimateBeneficiaryTypes = ultimateBeneficiaryTypesList.get(ultimateBeneficiaryTypesList.size() - 1);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeCode()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryType()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeDetails()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository).save(testUltimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void putNonExistingUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ultimateBeneficiaryTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void putWithIdMismatchUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void partialUpdateUltimateBeneficiaryTypesWithPatch() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();

        // Update the ultimateBeneficiaryTypes using partial update
        UltimateBeneficiaryTypes partialUpdatedUltimateBeneficiaryTypes = new UltimateBeneficiaryTypes();
        partialUpdatedUltimateBeneficiaryTypes.setId(ultimateBeneficiaryTypes.getId());

        partialUpdatedUltimateBeneficiaryTypes
            .ultimateBeneficiaryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE)
            .ultimateBeneficiaryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);

        restUltimateBeneficiaryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUltimateBeneficiaryTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUltimateBeneficiaryTypes))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryTypes testUltimateBeneficiaryTypes = ultimateBeneficiaryTypesList.get(ultimateBeneficiaryTypesList.size() - 1);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeCode()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryType()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeDetails()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateUltimateBeneficiaryTypesWithPatch() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();

        // Update the ultimateBeneficiaryTypes using partial update
        UltimateBeneficiaryTypes partialUpdatedUltimateBeneficiaryTypes = new UltimateBeneficiaryTypes();
        partialUpdatedUltimateBeneficiaryTypes.setId(ultimateBeneficiaryTypes.getId());

        partialUpdatedUltimateBeneficiaryTypes
            .ultimateBeneficiaryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);

        restUltimateBeneficiaryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUltimateBeneficiaryTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUltimateBeneficiaryTypes))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryTypes testUltimateBeneficiaryTypes = ultimateBeneficiaryTypesList.get(ultimateBeneficiaryTypesList.size() - 1);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeCode()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryType()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryTypes.getUltimateBeneficiaryTypeDetails()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ultimateBeneficiaryTypesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUltimateBeneficiaryTypes() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryTypesRepository.findAll().size();
        ultimateBeneficiaryTypes.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryTypes
        UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesMapper.toDto(ultimateBeneficiaryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UltimateBeneficiaryTypes in the database
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(0)).save(ultimateBeneficiaryTypes);
    }

    @Test
    @Transactional
    void deleteUltimateBeneficiaryTypes() throws Exception {
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);

        int databaseSizeBeforeDelete = ultimateBeneficiaryTypesRepository.findAll().size();

        // Delete the ultimateBeneficiaryTypes
        restUltimateBeneficiaryTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, ultimateBeneficiaryTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UltimateBeneficiaryTypes> ultimateBeneficiaryTypesList = ultimateBeneficiaryTypesRepository.findAll();
        assertThat(ultimateBeneficiaryTypesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UltimateBeneficiaryTypes in Elasticsearch
        verify(mockUltimateBeneficiaryTypesSearchRepository, times(1)).deleteById(ultimateBeneficiaryTypes.getId());
    }

    @Test
    @Transactional
    void searchUltimateBeneficiaryTypes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ultimateBeneficiaryTypesRepository.saveAndFlush(ultimateBeneficiaryTypes);
        when(mockUltimateBeneficiaryTypesSearchRepository.search("id:" + ultimateBeneficiaryTypes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ultimateBeneficiaryTypes), PageRequest.of(0, 1), 1));

        // Search the ultimateBeneficiaryTypes
        restUltimateBeneficiaryTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ultimateBeneficiaryTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryTypeDetails").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE_DETAILS.toString()))
            );
    }
}
