package io.github.erp.web.rest;

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
import io.github.erp.domain.CrbAccountHolderType;
import io.github.erp.repository.CrbAccountHolderTypeRepository;
import io.github.erp.repository.search.CrbAccountHolderTypeSearchRepository;
import io.github.erp.service.criteria.CrbAccountHolderTypeCriteria;
import io.github.erp.service.dto.CrbAccountHolderTypeDTO;
import io.github.erp.service.mapper.CrbAccountHolderTypeMapper;
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
 * Integration tests for the {@link CrbAccountHolderTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbAccountHolderTypeResourceIT {

    private static final String DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-account-holder-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-account-holder-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbAccountHolderTypeRepository crbAccountHolderTypeRepository;

    @Autowired
    private CrbAccountHolderTypeMapper crbAccountHolderTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbAccountHolderTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbAccountHolderTypeSearchRepository mockCrbAccountHolderTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbAccountHolderTypeMockMvc;

    private CrbAccountHolderType crbAccountHolderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAccountHolderType createEntity(EntityManager em) {
        CrbAccountHolderType crbAccountHolderType = new CrbAccountHolderType()
            .accountHolderCategoryTypeCode(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)
            .accountHolderCategoryType(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);
        return crbAccountHolderType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAccountHolderType createUpdatedEntity(EntityManager em) {
        CrbAccountHolderType crbAccountHolderType = new CrbAccountHolderType()
            .accountHolderCategoryTypeCode(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)
            .accountHolderCategoryType(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
        return crbAccountHolderType;
    }

    @BeforeEach
    public void initTest() {
        crbAccountHolderType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeCreate = crbAccountHolderTypeRepository.findAll().size();
        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);
        restCrbAccountHolderTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbAccountHolderType testCrbAccountHolderType = crbAccountHolderTypeList.get(crbAccountHolderTypeList.size() - 1);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryTypeCode()).isEqualTo(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryType()).isEqualTo(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(1)).save(testCrbAccountHolderType);
    }

    @Test
    @Transactional
    void createCrbAccountHolderTypeWithExistingId() throws Exception {
        // Create the CrbAccountHolderType with an existing ID
        crbAccountHolderType.setId(1L);
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        int databaseSizeBeforeCreate = crbAccountHolderTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbAccountHolderTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void checkAccountHolderCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAccountHolderTypeRepository.findAll().size();
        // set the field null
        crbAccountHolderType.setAccountHolderCategoryTypeCode(null);

        // Create the CrbAccountHolderType, which fails.
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        restCrbAccountHolderTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountHolderCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAccountHolderTypeRepository.findAll().size();
        // set the field null
        crbAccountHolderType.setAccountHolderCategoryType(null);

        // Create the CrbAccountHolderType, which fails.
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        restCrbAccountHolderTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypes() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountHolderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountHolderCategoryTypeCode").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountHolderCategoryType").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE)));
    }

    @Test
    @Transactional
    void getCrbAccountHolderType() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get the crbAccountHolderType
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbAccountHolderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbAccountHolderType.getId().intValue()))
            .andExpect(jsonPath("$.accountHolderCategoryTypeCode").value(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.accountHolderCategoryType").value(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE));
    }

    @Test
    @Transactional
    void getCrbAccountHolderTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        Long id = crbAccountHolderType.getId();

        defaultCrbAccountHolderTypeShouldBeFound("id.equals=" + id);
        defaultCrbAccountHolderTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbAccountHolderTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbAccountHolderTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbAccountHolderTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbAccountHolderTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode equals to DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryTypeCode.equals=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryTypeCode.equals=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode not equals to DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryTypeCode.notEquals=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode not equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryTypeCode.notEquals=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode in DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE or UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldBeFound(
            "accountHolderCategoryTypeCode.in=" +
            DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE +
            "," +
            UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        );

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryTypeCode.in=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode is not null
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryTypeCode.specified=true");

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode is null
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode contains DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryTypeCode.contains=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode contains UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryTypeCode.contains=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode does not contain DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldNotBeFound(
            "accountHolderCategoryTypeCode.doesNotContain=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        );

        // Get all the crbAccountHolderTypeList where accountHolderCategoryTypeCode does not contain UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        defaultCrbAccountHolderTypeShouldBeFound(
            "accountHolderCategoryTypeCode.doesNotContain=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType equals to DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryType.equals=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.equals=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType not equals to DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.notEquals=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType not equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryType.notEquals=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType in DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE or UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldBeFound(
            "accountHolderCategoryType.in=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE + "," + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        );

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType equals to UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.in=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType is not null
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryType.specified=true");

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType is null
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeContainsSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType contains DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryType.contains=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType contains UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.contains=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbAccountHolderTypesByAccountHolderCategoryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType does not contain DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldNotBeFound("accountHolderCategoryType.doesNotContain=" + DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Get all the crbAccountHolderTypeList where accountHolderCategoryType does not contain UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE
        defaultCrbAccountHolderTypeShouldBeFound("accountHolderCategoryType.doesNotContain=" + UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbAccountHolderTypeShouldBeFound(String filter) throws Exception {
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountHolderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountHolderCategoryTypeCode").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountHolderCategoryType").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE)));

        // Check, that the count call also returns 1
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbAccountHolderTypeShouldNotBeFound(String filter) throws Exception {
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbAccountHolderType() throws Exception {
        // Get the crbAccountHolderType
        restCrbAccountHolderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbAccountHolderType() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();

        // Update the crbAccountHolderType
        CrbAccountHolderType updatedCrbAccountHolderType = crbAccountHolderTypeRepository.findById(crbAccountHolderType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbAccountHolderType are not directly saved in db
        em.detach(updatedCrbAccountHolderType);
        updatedCrbAccountHolderType
            .accountHolderCategoryTypeCode(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)
            .accountHolderCategoryType(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(updatedCrbAccountHolderType);

        restCrbAccountHolderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAccountHolderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountHolderType testCrbAccountHolderType = crbAccountHolderTypeList.get(crbAccountHolderTypeList.size() - 1);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryTypeCode()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryType()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository).save(testCrbAccountHolderType);
    }

    @Test
    @Transactional
    void putNonExistingCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAccountHolderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void partialUpdateCrbAccountHolderTypeWithPatch() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();

        // Update the crbAccountHolderType using partial update
        CrbAccountHolderType partialUpdatedCrbAccountHolderType = new CrbAccountHolderType();
        partialUpdatedCrbAccountHolderType.setId(crbAccountHolderType.getId());

        partialUpdatedCrbAccountHolderType
            .accountHolderCategoryTypeCode(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)
            .accountHolderCategoryType(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);

        restCrbAccountHolderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAccountHolderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAccountHolderType))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountHolderType testCrbAccountHolderType = crbAccountHolderTypeList.get(crbAccountHolderTypeList.size() - 1);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryTypeCode()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryType()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCrbAccountHolderTypeWithPatch() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();

        // Update the crbAccountHolderType using partial update
        CrbAccountHolderType partialUpdatedCrbAccountHolderType = new CrbAccountHolderType();
        partialUpdatedCrbAccountHolderType.setId(crbAccountHolderType.getId());

        partialUpdatedCrbAccountHolderType
            .accountHolderCategoryTypeCode(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)
            .accountHolderCategoryType(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);

        restCrbAccountHolderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAccountHolderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAccountHolderType))
            )
            .andExpect(status().isOk());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAccountHolderType testCrbAccountHolderType = crbAccountHolderTypeList.get(crbAccountHolderTypeList.size() - 1);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryTypeCode()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE);
        assertThat(testCrbAccountHolderType.getAccountHolderCategoryType()).isEqualTo(UPDATED_ACCOUNT_HOLDER_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbAccountHolderTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbAccountHolderType() throws Exception {
        int databaseSizeBeforeUpdate = crbAccountHolderTypeRepository.findAll().size();
        crbAccountHolderType.setId(count.incrementAndGet());

        // Create the CrbAccountHolderType
        CrbAccountHolderTypeDTO crbAccountHolderTypeDTO = crbAccountHolderTypeMapper.toDto(crbAccountHolderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAccountHolderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAccountHolderTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAccountHolderType in the database
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(0)).save(crbAccountHolderType);
    }

    @Test
    @Transactional
    void deleteCrbAccountHolderType() throws Exception {
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);

        int databaseSizeBeforeDelete = crbAccountHolderTypeRepository.findAll().size();

        // Delete the crbAccountHolderType
        restCrbAccountHolderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbAccountHolderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbAccountHolderType> crbAccountHolderTypeList = crbAccountHolderTypeRepository.findAll();
        assertThat(crbAccountHolderTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbAccountHolderType in Elasticsearch
        verify(mockCrbAccountHolderTypeSearchRepository, times(1)).deleteById(crbAccountHolderType.getId());
    }

    @Test
    @Transactional
    void searchCrbAccountHolderType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbAccountHolderTypeRepository.saveAndFlush(crbAccountHolderType);
        when(mockCrbAccountHolderTypeSearchRepository.search("id:" + crbAccountHolderType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbAccountHolderType), PageRequest.of(0, 1), 1));

        // Search the crbAccountHolderType
        restCrbAccountHolderTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbAccountHolderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAccountHolderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountHolderCategoryTypeCode").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountHolderCategoryType").value(hasItem(DEFAULT_ACCOUNT_HOLDER_CATEGORY_TYPE)));
    }
}
