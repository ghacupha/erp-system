package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.ExecutiveCategoryType;
import io.github.erp.repository.ExecutiveCategoryTypeRepository;
import io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepository;
import io.github.erp.service.dto.ExecutiveCategoryTypeDTO;
import io.github.erp.service.mapper.ExecutiveCategoryTypeMapper;
import io.github.erp.web.rest.ExecutiveCategoryTypeResource;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExecutiveCategoryTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class ExecutiveCategoryTypeResourceIT {

    private static final String DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR_CATEGORY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR_CATEGORY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/executive-category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/executive-category-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExecutiveCategoryTypeRepository executiveCategoryTypeRepository;

    @Autowired
    private ExecutiveCategoryTypeMapper executiveCategoryTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExecutiveCategoryTypeSearchRepository mockExecutiveCategoryTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExecutiveCategoryTypeMockMvc;

    private ExecutiveCategoryType executiveCategoryType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutiveCategoryType createEntity(EntityManager em) {
        ExecutiveCategoryType executiveCategoryType = new ExecutiveCategoryType()
            .directorCategoryTypeCode(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE)
            .directorCategoryType(DEFAULT_DIRECTOR_CATEGORY_TYPE)
            .directorCategoryTypeDetails(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS);
        return executiveCategoryType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutiveCategoryType createUpdatedEntity(EntityManager em) {
        ExecutiveCategoryType executiveCategoryType = new ExecutiveCategoryType()
            .directorCategoryTypeCode(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE)
            .directorCategoryType(UPDATED_DIRECTOR_CATEGORY_TYPE)
            .directorCategoryTypeDetails(UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS);
        return executiveCategoryType;
    }

    @BeforeEach
    public void initTest() {
        executiveCategoryType = createEntity(em);
    }

    @Test
    @Transactional
    void createExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeCreate = executiveCategoryTypeRepository.findAll().size();
        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);
        restExecutiveCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ExecutiveCategoryType testExecutiveCategoryType = executiveCategoryTypeList.get(executiveCategoryTypeList.size() - 1);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeCode()).isEqualTo(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryType()).isEqualTo(DEFAULT_DIRECTOR_CATEGORY_TYPE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeDetails()).isEqualTo(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(1)).save(testExecutiveCategoryType);
    }

    @Test
    @Transactional
    void createExecutiveCategoryTypeWithExistingId() throws Exception {
        // Create the ExecutiveCategoryType with an existing ID
        executiveCategoryType.setId(1L);
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        int databaseSizeBeforeCreate = executiveCategoryTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutiveCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void checkDirectorCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = executiveCategoryTypeRepository.findAll().size();
        // set the field null
        executiveCategoryType.setDirectorCategoryTypeCode(null);

        // Create the ExecutiveCategoryType, which fails.
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        restExecutiveCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectorCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = executiveCategoryTypeRepository.findAll().size();
        // set the field null
        executiveCategoryType.setDirectorCategoryType(null);

        // Create the ExecutiveCategoryType, which fails.
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        restExecutiveCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypes() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executiveCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].directorCategoryTypeCode").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].directorCategoryType").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE)))
            .andExpect(jsonPath("$.[*].directorCategoryTypeDetails").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getExecutiveCategoryType() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get the executiveCategoryType
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, executiveCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(executiveCategoryType.getId().intValue()))
            .andExpect(jsonPath("$.directorCategoryTypeCode").value(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.directorCategoryType").value(DEFAULT_DIRECTOR_CATEGORY_TYPE))
            .andExpect(jsonPath("$.directorCategoryTypeDetails").value(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getExecutiveCategoryTypesByIdFiltering() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        Long id = executiveCategoryType.getId();

        defaultExecutiveCategoryTypeShouldBeFound("id.equals=" + id);
        defaultExecutiveCategoryTypeShouldNotBeFound("id.notEquals=" + id);

        defaultExecutiveCategoryTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExecutiveCategoryTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultExecutiveCategoryTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExecutiveCategoryTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode equals to DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryTypeCode.equals=" + DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode equals to UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.equals=" + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode not equals to DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.notEquals=" + DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode not equals to UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryTypeCode.notEquals=" + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode in DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE or UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldBeFound(
            "directorCategoryTypeCode.in=" + DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE + "," + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        );

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode equals to UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.in=" + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode is not null
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryTypeCode.specified=true");

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode is null
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode contains DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryTypeCode.contains=" + DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode contains UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.contains=" + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode does not contain DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryTypeCode.doesNotContain=" + DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE);

        // Get all the executiveCategoryTypeList where directorCategoryTypeCode does not contain UPDATED_DIRECTOR_CATEGORY_TYPE_CODE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryTypeCode.doesNotContain=" + UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType equals to DEFAULT_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryType.equals=" + DEFAULT_DIRECTOR_CATEGORY_TYPE);

        // Get all the executiveCategoryTypeList where directorCategoryType equals to UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.equals=" + UPDATED_DIRECTOR_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType not equals to DEFAULT_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.notEquals=" + DEFAULT_DIRECTOR_CATEGORY_TYPE);

        // Get all the executiveCategoryTypeList where directorCategoryType not equals to UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryType.notEquals=" + UPDATED_DIRECTOR_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType in DEFAULT_DIRECTOR_CATEGORY_TYPE or UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldBeFound(
            "directorCategoryType.in=" + DEFAULT_DIRECTOR_CATEGORY_TYPE + "," + UPDATED_DIRECTOR_CATEGORY_TYPE
        );

        // Get all the executiveCategoryTypeList where directorCategoryType equals to UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.in=" + UPDATED_DIRECTOR_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType is not null
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryType.specified=true");

        // Get all the executiveCategoryTypeList where directorCategoryType is null
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.specified=false");
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeContainsSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType contains DEFAULT_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryType.contains=" + DEFAULT_DIRECTOR_CATEGORY_TYPE);

        // Get all the executiveCategoryTypeList where directorCategoryType contains UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.contains=" + UPDATED_DIRECTOR_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllExecutiveCategoryTypesByDirectorCategoryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        // Get all the executiveCategoryTypeList where directorCategoryType does not contain DEFAULT_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldNotBeFound("directorCategoryType.doesNotContain=" + DEFAULT_DIRECTOR_CATEGORY_TYPE);

        // Get all the executiveCategoryTypeList where directorCategoryType does not contain UPDATED_DIRECTOR_CATEGORY_TYPE
        defaultExecutiveCategoryTypeShouldBeFound("directorCategoryType.doesNotContain=" + UPDATED_DIRECTOR_CATEGORY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExecutiveCategoryTypeShouldBeFound(String filter) throws Exception {
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executiveCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].directorCategoryTypeCode").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].directorCategoryType").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE)))
            .andExpect(jsonPath("$.[*].directorCategoryTypeDetails").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExecutiveCategoryTypeShouldNotBeFound(String filter) throws Exception {
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExecutiveCategoryType() throws Exception {
        // Get the executiveCategoryType
        restExecutiveCategoryTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExecutiveCategoryType() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();

        // Update the executiveCategoryType
        ExecutiveCategoryType updatedExecutiveCategoryType = executiveCategoryTypeRepository.findById(executiveCategoryType.getId()).get();
        // Disconnect from session so that the updates on updatedExecutiveCategoryType are not directly saved in db
        em.detach(updatedExecutiveCategoryType);
        updatedExecutiveCategoryType
            .directorCategoryTypeCode(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE)
            .directorCategoryType(UPDATED_DIRECTOR_CATEGORY_TYPE)
            .directorCategoryTypeDetails(UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS);
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(updatedExecutiveCategoryType);

        restExecutiveCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, executiveCategoryTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        ExecutiveCategoryType testExecutiveCategoryType = executiveCategoryTypeList.get(executiveCategoryTypeList.size() - 1);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeCode()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryType()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeDetails()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository).save(testExecutiveCategoryType);
    }

    @Test
    @Transactional
    void putNonExistingExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, executiveCategoryTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void putWithIdMismatchExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void partialUpdateExecutiveCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();

        // Update the executiveCategoryType using partial update
        ExecutiveCategoryType partialUpdatedExecutiveCategoryType = new ExecutiveCategoryType();
        partialUpdatedExecutiveCategoryType.setId(executiveCategoryType.getId());

        partialUpdatedExecutiveCategoryType.directorCategoryTypeCode(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);

        restExecutiveCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExecutiveCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExecutiveCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        ExecutiveCategoryType testExecutiveCategoryType = executiveCategoryTypeList.get(executiveCategoryTypeList.size() - 1);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeCode()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryType()).isEqualTo(DEFAULT_DIRECTOR_CATEGORY_TYPE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeDetails()).isEqualTo(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateExecutiveCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();

        // Update the executiveCategoryType using partial update
        ExecutiveCategoryType partialUpdatedExecutiveCategoryType = new ExecutiveCategoryType();
        partialUpdatedExecutiveCategoryType.setId(executiveCategoryType.getId());

        partialUpdatedExecutiveCategoryType
            .directorCategoryTypeCode(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE)
            .directorCategoryType(UPDATED_DIRECTOR_CATEGORY_TYPE)
            .directorCategoryTypeDetails(UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS);

        restExecutiveCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExecutiveCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExecutiveCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        ExecutiveCategoryType testExecutiveCategoryType = executiveCategoryTypeList.get(executiveCategoryTypeList.size() - 1);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeCode()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE_CODE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryType()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE);
        assertThat(testExecutiveCategoryType.getDirectorCategoryTypeDetails()).isEqualTo(UPDATED_DIRECTOR_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, executiveCategoryTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExecutiveCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = executiveCategoryTypeRepository.findAll().size();
        executiveCategoryType.setId(count.incrementAndGet());

        // Create the ExecutiveCategoryType
        ExecutiveCategoryTypeDTO executiveCategoryTypeDTO = executiveCategoryTypeMapper.toDto(executiveCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutiveCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(executiveCategoryTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExecutiveCategoryType in the database
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(0)).save(executiveCategoryType);
    }

    @Test
    @Transactional
    void deleteExecutiveCategoryType() throws Exception {
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);

        int databaseSizeBeforeDelete = executiveCategoryTypeRepository.findAll().size();

        // Delete the executiveCategoryType
        restExecutiveCategoryTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, executiveCategoryType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExecutiveCategoryType> executiveCategoryTypeList = executiveCategoryTypeRepository.findAll();
        assertThat(executiveCategoryTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExecutiveCategoryType in Elasticsearch
        verify(mockExecutiveCategoryTypeSearchRepository, times(1)).deleteById(executiveCategoryType.getId());
    }

    @Test
    @Transactional
    void searchExecutiveCategoryType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        executiveCategoryTypeRepository.saveAndFlush(executiveCategoryType);
        when(mockExecutiveCategoryTypeSearchRepository.search("id:" + executiveCategoryType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(executiveCategoryType), PageRequest.of(0, 1), 1));

        // Search the executiveCategoryType
        restExecutiveCategoryTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + executiveCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executiveCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].directorCategoryTypeCode").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].directorCategoryType").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE)))
            .andExpect(jsonPath("$.[*].directorCategoryTypeDetails").value(hasItem(DEFAULT_DIRECTOR_CATEGORY_TYPE_DETAILS.toString())));
    }
}
