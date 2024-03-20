package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.ManagementMemberType;
import io.github.erp.repository.ManagementMemberTypeRepository;
import io.github.erp.repository.search.ManagementMemberTypeSearchRepository;
import io.github.erp.service.criteria.ManagementMemberTypeCriteria;
import io.github.erp.service.dto.ManagementMemberTypeDTO;
import io.github.erp.service.mapper.ManagementMemberTypeMapper;
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
 * Integration tests for the {@link ManagementMemberTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ManagementMemberTypeResourceIT {

    private static final String DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MANAGEMENT_MEMBER_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGEMENT_MEMBER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MANAGEMENT_MEMBER_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/management-member-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/management-member-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManagementMemberTypeRepository managementMemberTypeRepository;

    @Autowired
    private ManagementMemberTypeMapper managementMemberTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ManagementMemberTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ManagementMemberTypeSearchRepository mockManagementMemberTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManagementMemberTypeMockMvc;

    private ManagementMemberType managementMemberType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagementMemberType createEntity(EntityManager em) {
        ManagementMemberType managementMemberType = new ManagementMemberType()
            .managementMemberTypeCode(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE)
            .managementMemberType(DEFAULT_MANAGEMENT_MEMBER_TYPE);
        return managementMemberType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagementMemberType createUpdatedEntity(EntityManager em) {
        ManagementMemberType managementMemberType = new ManagementMemberType()
            .managementMemberTypeCode(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE)
            .managementMemberType(UPDATED_MANAGEMENT_MEMBER_TYPE);
        return managementMemberType;
    }

    @BeforeEach
    public void initTest() {
        managementMemberType = createEntity(em);
    }

    @Test
    @Transactional
    void createManagementMemberType() throws Exception {
        int databaseSizeBeforeCreate = managementMemberTypeRepository.findAll().size();
        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);
        restManagementMemberTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ManagementMemberType testManagementMemberType = managementMemberTypeList.get(managementMemberTypeList.size() - 1);
        assertThat(testManagementMemberType.getManagementMemberTypeCode()).isEqualTo(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE);
        assertThat(testManagementMemberType.getManagementMemberType()).isEqualTo(DEFAULT_MANAGEMENT_MEMBER_TYPE);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(1)).save(testManagementMemberType);
    }

    @Test
    @Transactional
    void createManagementMemberTypeWithExistingId() throws Exception {
        // Create the ManagementMemberType with an existing ID
        managementMemberType.setId(1L);
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        int databaseSizeBeforeCreate = managementMemberTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagementMemberTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void checkManagementMemberTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = managementMemberTypeRepository.findAll().size();
        // set the field null
        managementMemberType.setManagementMemberTypeCode(null);

        // Create the ManagementMemberType, which fails.
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        restManagementMemberTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManagementMemberTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = managementMemberTypeRepository.findAll().size();
        // set the field null
        managementMemberType.setManagementMemberType(null);

        // Create the ManagementMemberType, which fails.
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        restManagementMemberTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypes() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managementMemberType.getId().intValue())))
            .andExpect(jsonPath("$.[*].managementMemberTypeCode").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].managementMemberType").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE)));
    }

    @Test
    @Transactional
    void getManagementMemberType() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get the managementMemberType
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, managementMemberType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(managementMemberType.getId().intValue()))
            .andExpect(jsonPath("$.managementMemberTypeCode").value(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE))
            .andExpect(jsonPath("$.managementMemberType").value(DEFAULT_MANAGEMENT_MEMBER_TYPE));
    }

    @Test
    @Transactional
    void getManagementMemberTypesByIdFiltering() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        Long id = managementMemberType.getId();

        defaultManagementMemberTypeShouldBeFound("id.equals=" + id);
        defaultManagementMemberTypeShouldNotBeFound("id.notEquals=" + id);

        defaultManagementMemberTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManagementMemberTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultManagementMemberTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManagementMemberTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode equals to DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldBeFound("managementMemberTypeCode.equals=" + DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE);

        // Get all the managementMemberTypeList where managementMemberTypeCode equals to UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.equals=" + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode not equals to DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.notEquals=" + DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE);

        // Get all the managementMemberTypeList where managementMemberTypeCode not equals to UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldBeFound("managementMemberTypeCode.notEquals=" + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode in DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE or UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldBeFound(
            "managementMemberTypeCode.in=" + DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE + "," + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        );

        // Get all the managementMemberTypeList where managementMemberTypeCode equals to UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.in=" + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode is not null
        defaultManagementMemberTypeShouldBeFound("managementMemberTypeCode.specified=true");

        // Get all the managementMemberTypeList where managementMemberTypeCode is null
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode contains DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldBeFound("managementMemberTypeCode.contains=" + DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE);

        // Get all the managementMemberTypeList where managementMemberTypeCode contains UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.contains=" + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberTypeCode does not contain DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberTypeCode.doesNotContain=" + DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE);

        // Get all the managementMemberTypeList where managementMemberTypeCode does not contain UPDATED_MANAGEMENT_MEMBER_TYPE_CODE
        defaultManagementMemberTypeShouldBeFound("managementMemberTypeCode.doesNotContain=" + UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType equals to DEFAULT_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldBeFound("managementMemberType.equals=" + DEFAULT_MANAGEMENT_MEMBER_TYPE);

        // Get all the managementMemberTypeList where managementMemberType equals to UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.equals=" + UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType not equals to DEFAULT_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.notEquals=" + DEFAULT_MANAGEMENT_MEMBER_TYPE);

        // Get all the managementMemberTypeList where managementMemberType not equals to UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldBeFound("managementMemberType.notEquals=" + UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeIsInShouldWork() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType in DEFAULT_MANAGEMENT_MEMBER_TYPE or UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldBeFound(
            "managementMemberType.in=" + DEFAULT_MANAGEMENT_MEMBER_TYPE + "," + UPDATED_MANAGEMENT_MEMBER_TYPE
        );

        // Get all the managementMemberTypeList where managementMemberType equals to UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.in=" + UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType is not null
        defaultManagementMemberTypeShouldBeFound("managementMemberType.specified=true");

        // Get all the managementMemberTypeList where managementMemberType is null
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.specified=false");
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeContainsSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType contains DEFAULT_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldBeFound("managementMemberType.contains=" + DEFAULT_MANAGEMENT_MEMBER_TYPE);

        // Get all the managementMemberTypeList where managementMemberType contains UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.contains=" + UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllManagementMemberTypesByManagementMemberTypeNotContainsSomething() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        // Get all the managementMemberTypeList where managementMemberType does not contain DEFAULT_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldNotBeFound("managementMemberType.doesNotContain=" + DEFAULT_MANAGEMENT_MEMBER_TYPE);

        // Get all the managementMemberTypeList where managementMemberType does not contain UPDATED_MANAGEMENT_MEMBER_TYPE
        defaultManagementMemberTypeShouldBeFound("managementMemberType.doesNotContain=" + UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManagementMemberTypeShouldBeFound(String filter) throws Exception {
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managementMemberType.getId().intValue())))
            .andExpect(jsonPath("$.[*].managementMemberTypeCode").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].managementMemberType").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE)));

        // Check, that the count call also returns 1
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManagementMemberTypeShouldNotBeFound(String filter) throws Exception {
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingManagementMemberType() throws Exception {
        // Get the managementMemberType
        restManagementMemberTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManagementMemberType() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();

        // Update the managementMemberType
        ManagementMemberType updatedManagementMemberType = managementMemberTypeRepository.findById(managementMemberType.getId()).get();
        // Disconnect from session so that the updates on updatedManagementMemberType are not directly saved in db
        em.detach(updatedManagementMemberType);
        updatedManagementMemberType
            .managementMemberTypeCode(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE)
            .managementMemberType(UPDATED_MANAGEMENT_MEMBER_TYPE);
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(updatedManagementMemberType);

        restManagementMemberTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, managementMemberTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);
        ManagementMemberType testManagementMemberType = managementMemberTypeList.get(managementMemberTypeList.size() - 1);
        assertThat(testManagementMemberType.getManagementMemberTypeCode()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
        assertThat(testManagementMemberType.getManagementMemberType()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository).save(testManagementMemberType);
    }

    @Test
    @Transactional
    void putNonExistingManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, managementMemberTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void putWithIdMismatchManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void partialUpdateManagementMemberTypeWithPatch() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();

        // Update the managementMemberType using partial update
        ManagementMemberType partialUpdatedManagementMemberType = new ManagementMemberType();
        partialUpdatedManagementMemberType.setId(managementMemberType.getId());

        partialUpdatedManagementMemberType
            .managementMemberTypeCode(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE)
            .managementMemberType(UPDATED_MANAGEMENT_MEMBER_TYPE);

        restManagementMemberTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManagementMemberType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManagementMemberType))
            )
            .andExpect(status().isOk());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);
        ManagementMemberType testManagementMemberType = managementMemberTypeList.get(managementMemberTypeList.size() - 1);
        assertThat(testManagementMemberType.getManagementMemberTypeCode()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
        assertThat(testManagementMemberType.getManagementMemberType()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateManagementMemberTypeWithPatch() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();

        // Update the managementMemberType using partial update
        ManagementMemberType partialUpdatedManagementMemberType = new ManagementMemberType();
        partialUpdatedManagementMemberType.setId(managementMemberType.getId());

        partialUpdatedManagementMemberType
            .managementMemberTypeCode(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE)
            .managementMemberType(UPDATED_MANAGEMENT_MEMBER_TYPE);

        restManagementMemberTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManagementMemberType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManagementMemberType))
            )
            .andExpect(status().isOk());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);
        ManagementMemberType testManagementMemberType = managementMemberTypeList.get(managementMemberTypeList.size() - 1);
        assertThat(testManagementMemberType.getManagementMemberTypeCode()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE_CODE);
        assertThat(testManagementMemberType.getManagementMemberType()).isEqualTo(UPDATED_MANAGEMENT_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, managementMemberTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManagementMemberType() throws Exception {
        int databaseSizeBeforeUpdate = managementMemberTypeRepository.findAll().size();
        managementMemberType.setId(count.incrementAndGet());

        // Create the ManagementMemberType
        ManagementMemberTypeDTO managementMemberTypeDTO = managementMemberTypeMapper.toDto(managementMemberType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagementMemberTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(managementMemberTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManagementMemberType in the database
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(0)).save(managementMemberType);
    }

    @Test
    @Transactional
    void deleteManagementMemberType() throws Exception {
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);

        int databaseSizeBeforeDelete = managementMemberTypeRepository.findAll().size();

        // Delete the managementMemberType
        restManagementMemberTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, managementMemberType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManagementMemberType> managementMemberTypeList = managementMemberTypeRepository.findAll();
        assertThat(managementMemberTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ManagementMemberType in Elasticsearch
        verify(mockManagementMemberTypeSearchRepository, times(1)).deleteById(managementMemberType.getId());
    }

    @Test
    @Transactional
    void searchManagementMemberType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        managementMemberTypeRepository.saveAndFlush(managementMemberType);
        when(mockManagementMemberTypeSearchRepository.search("id:" + managementMemberType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(managementMemberType), PageRequest.of(0, 1), 1));

        // Search the managementMemberType
        restManagementMemberTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + managementMemberType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managementMemberType.getId().intValue())))
            .andExpect(jsonPath("$.[*].managementMemberTypeCode").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].managementMemberType").value(hasItem(DEFAULT_MANAGEMENT_MEMBER_TYPE)));
    }
}
