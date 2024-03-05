package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.StaffRoleType;
import io.github.erp.repository.StaffRoleTypeRepository;
import io.github.erp.repository.search.StaffRoleTypeSearchRepository;
import io.github.erp.service.dto.StaffRoleTypeDTO;
import io.github.erp.service.mapper.StaffRoleTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.StaffRoleTypeResource;
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
 * Integration tests for the {@link StaffRoleTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class StaffRoleTypeResourceIT {

    private static final String DEFAULT_STAFF_ROLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_ROLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STAFF_ROLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_ROLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STAFF_ROLE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_ROLE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/staff-role-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/staff-role-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffRoleTypeRepository staffRoleTypeRepository;

    @Autowired
    private StaffRoleTypeMapper staffRoleTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.StaffRoleTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private StaffRoleTypeSearchRepository mockStaffRoleTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffRoleTypeMockMvc;

    private StaffRoleType staffRoleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffRoleType createEntity(EntityManager em) {
        StaffRoleType staffRoleType = new StaffRoleType()
            .staffRoleTypeCode(DEFAULT_STAFF_ROLE_TYPE_CODE)
            .staffRoleType(DEFAULT_STAFF_ROLE_TYPE)
            .staffRoleTypeDetails(DEFAULT_STAFF_ROLE_TYPE_DETAILS);
        return staffRoleType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffRoleType createUpdatedEntity(EntityManager em) {
        StaffRoleType staffRoleType = new StaffRoleType()
            .staffRoleTypeCode(UPDATED_STAFF_ROLE_TYPE_CODE)
            .staffRoleType(UPDATED_STAFF_ROLE_TYPE)
            .staffRoleTypeDetails(UPDATED_STAFF_ROLE_TYPE_DETAILS);
        return staffRoleType;
    }

    @BeforeEach
    public void initTest() {
        staffRoleType = createEntity(em);
    }

    @Test
    @Transactional
    void createStaffRoleType() throws Exception {
        int databaseSizeBeforeCreate = staffRoleTypeRepository.findAll().size();
        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);
        restStaffRoleTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        StaffRoleType testStaffRoleType = staffRoleTypeList.get(staffRoleTypeList.size() - 1);
        assertThat(testStaffRoleType.getStaffRoleTypeCode()).isEqualTo(DEFAULT_STAFF_ROLE_TYPE_CODE);
        assertThat(testStaffRoleType.getStaffRoleType()).isEqualTo(DEFAULT_STAFF_ROLE_TYPE);
        assertThat(testStaffRoleType.getStaffRoleTypeDetails()).isEqualTo(DEFAULT_STAFF_ROLE_TYPE_DETAILS);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(1)).save(testStaffRoleType);
    }

    @Test
    @Transactional
    void createStaffRoleTypeWithExistingId() throws Exception {
        // Create the StaffRoleType with an existing ID
        staffRoleType.setId(1L);
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        int databaseSizeBeforeCreate = staffRoleTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffRoleTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void checkStaffRoleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRoleTypeRepository.findAll().size();
        // set the field null
        staffRoleType.setStaffRoleTypeCode(null);

        // Create the StaffRoleType, which fails.
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        restStaffRoleTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStaffRoleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRoleTypeRepository.findAll().size();
        // set the field null
        staffRoleType.setStaffRoleType(null);

        // Create the StaffRoleType, which fails.
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        restStaffRoleTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypes() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffRoleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffRoleTypeCode").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].staffRoleType").value(hasItem(DEFAULT_STAFF_ROLE_TYPE)))
            .andExpect(jsonPath("$.[*].staffRoleTypeDetails").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getStaffRoleType() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get the staffRoleType
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, staffRoleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffRoleType.getId().intValue()))
            .andExpect(jsonPath("$.staffRoleTypeCode").value(DEFAULT_STAFF_ROLE_TYPE_CODE))
            .andExpect(jsonPath("$.staffRoleType").value(DEFAULT_STAFF_ROLE_TYPE))
            .andExpect(jsonPath("$.staffRoleTypeDetails").value(DEFAULT_STAFF_ROLE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getStaffRoleTypesByIdFiltering() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        Long id = staffRoleType.getId();

        defaultStaffRoleTypeShouldBeFound("id.equals=" + id);
        defaultStaffRoleTypeShouldNotBeFound("id.notEquals=" + id);

        defaultStaffRoleTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStaffRoleTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultStaffRoleTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStaffRoleTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode equals to DEFAULT_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.equals=" + DEFAULT_STAFF_ROLE_TYPE_CODE);

        // Get all the staffRoleTypeList where staffRoleTypeCode equals to UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.equals=" + UPDATED_STAFF_ROLE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode not equals to DEFAULT_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.notEquals=" + DEFAULT_STAFF_ROLE_TYPE_CODE);

        // Get all the staffRoleTypeList where staffRoleTypeCode not equals to UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.notEquals=" + UPDATED_STAFF_ROLE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode in DEFAULT_STAFF_ROLE_TYPE_CODE or UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.in=" + DEFAULT_STAFF_ROLE_TYPE_CODE + "," + UPDATED_STAFF_ROLE_TYPE_CODE);

        // Get all the staffRoleTypeList where staffRoleTypeCode equals to UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.in=" + UPDATED_STAFF_ROLE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode is not null
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.specified=true");

        // Get all the staffRoleTypeList where staffRoleTypeCode is null
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode contains DEFAULT_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.contains=" + DEFAULT_STAFF_ROLE_TYPE_CODE);

        // Get all the staffRoleTypeList where staffRoleTypeCode contains UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.contains=" + UPDATED_STAFF_ROLE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleTypeCode does not contain DEFAULT_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleTypeCode.doesNotContain=" + DEFAULT_STAFF_ROLE_TYPE_CODE);

        // Get all the staffRoleTypeList where staffRoleTypeCode does not contain UPDATED_STAFF_ROLE_TYPE_CODE
        defaultStaffRoleTypeShouldBeFound("staffRoleTypeCode.doesNotContain=" + UPDATED_STAFF_ROLE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType equals to DEFAULT_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldBeFound("staffRoleType.equals=" + DEFAULT_STAFF_ROLE_TYPE);

        // Get all the staffRoleTypeList where staffRoleType equals to UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.equals=" + UPDATED_STAFF_ROLE_TYPE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType not equals to DEFAULT_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.notEquals=" + DEFAULT_STAFF_ROLE_TYPE);

        // Get all the staffRoleTypeList where staffRoleType not equals to UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldBeFound("staffRoleType.notEquals=" + UPDATED_STAFF_ROLE_TYPE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType in DEFAULT_STAFF_ROLE_TYPE or UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldBeFound("staffRoleType.in=" + DEFAULT_STAFF_ROLE_TYPE + "," + UPDATED_STAFF_ROLE_TYPE);

        // Get all the staffRoleTypeList where staffRoleType equals to UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.in=" + UPDATED_STAFF_ROLE_TYPE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType is not null
        defaultStaffRoleTypeShouldBeFound("staffRoleType.specified=true");

        // Get all the staffRoleTypeList where staffRoleType is null
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeContainsSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType contains DEFAULT_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldBeFound("staffRoleType.contains=" + DEFAULT_STAFF_ROLE_TYPE);

        // Get all the staffRoleTypeList where staffRoleType contains UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.contains=" + UPDATED_STAFF_ROLE_TYPE);
    }

    @Test
    @Transactional
    void getAllStaffRoleTypesByStaffRoleTypeNotContainsSomething() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        // Get all the staffRoleTypeList where staffRoleType does not contain DEFAULT_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldNotBeFound("staffRoleType.doesNotContain=" + DEFAULT_STAFF_ROLE_TYPE);

        // Get all the staffRoleTypeList where staffRoleType does not contain UPDATED_STAFF_ROLE_TYPE
        defaultStaffRoleTypeShouldBeFound("staffRoleType.doesNotContain=" + UPDATED_STAFF_ROLE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaffRoleTypeShouldBeFound(String filter) throws Exception {
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffRoleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffRoleTypeCode").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].staffRoleType").value(hasItem(DEFAULT_STAFF_ROLE_TYPE)))
            .andExpect(jsonPath("$.[*].staffRoleTypeDetails").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaffRoleTypeShouldNotBeFound(String filter) throws Exception {
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStaffRoleType() throws Exception {
        // Get the staffRoleType
        restStaffRoleTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaffRoleType() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();

        // Update the staffRoleType
        StaffRoleType updatedStaffRoleType = staffRoleTypeRepository.findById(staffRoleType.getId()).get();
        // Disconnect from session so that the updates on updatedStaffRoleType are not directly saved in db
        em.detach(updatedStaffRoleType);
        updatedStaffRoleType
            .staffRoleTypeCode(UPDATED_STAFF_ROLE_TYPE_CODE)
            .staffRoleType(UPDATED_STAFF_ROLE_TYPE)
            .staffRoleTypeDetails(UPDATED_STAFF_ROLE_TYPE_DETAILS);
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(updatedStaffRoleType);

        restStaffRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffRoleTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);
        StaffRoleType testStaffRoleType = staffRoleTypeList.get(staffRoleTypeList.size() - 1);
        assertThat(testStaffRoleType.getStaffRoleTypeCode()).isEqualTo(UPDATED_STAFF_ROLE_TYPE_CODE);
        assertThat(testStaffRoleType.getStaffRoleType()).isEqualTo(UPDATED_STAFF_ROLE_TYPE);
        assertThat(testStaffRoleType.getStaffRoleTypeDetails()).isEqualTo(UPDATED_STAFF_ROLE_TYPE_DETAILS);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository).save(testStaffRoleType);
    }

    @Test
    @Transactional
    void putNonExistingStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffRoleTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void partialUpdateStaffRoleTypeWithPatch() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();

        // Update the staffRoleType using partial update
        StaffRoleType partialUpdatedStaffRoleType = new StaffRoleType();
        partialUpdatedStaffRoleType.setId(staffRoleType.getId());

        partialUpdatedStaffRoleType.staffRoleTypeCode(UPDATED_STAFF_ROLE_TYPE_CODE);

        restStaffRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffRoleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffRoleType))
            )
            .andExpect(status().isOk());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);
        StaffRoleType testStaffRoleType = staffRoleTypeList.get(staffRoleTypeList.size() - 1);
        assertThat(testStaffRoleType.getStaffRoleTypeCode()).isEqualTo(UPDATED_STAFF_ROLE_TYPE_CODE);
        assertThat(testStaffRoleType.getStaffRoleType()).isEqualTo(DEFAULT_STAFF_ROLE_TYPE);
        assertThat(testStaffRoleType.getStaffRoleTypeDetails()).isEqualTo(DEFAULT_STAFF_ROLE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateStaffRoleTypeWithPatch() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();

        // Update the staffRoleType using partial update
        StaffRoleType partialUpdatedStaffRoleType = new StaffRoleType();
        partialUpdatedStaffRoleType.setId(staffRoleType.getId());

        partialUpdatedStaffRoleType
            .staffRoleTypeCode(UPDATED_STAFF_ROLE_TYPE_CODE)
            .staffRoleType(UPDATED_STAFF_ROLE_TYPE)
            .staffRoleTypeDetails(UPDATED_STAFF_ROLE_TYPE_DETAILS);

        restStaffRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffRoleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffRoleType))
            )
            .andExpect(status().isOk());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);
        StaffRoleType testStaffRoleType = staffRoleTypeList.get(staffRoleTypeList.size() - 1);
        assertThat(testStaffRoleType.getStaffRoleTypeCode()).isEqualTo(UPDATED_STAFF_ROLE_TYPE_CODE);
        assertThat(testStaffRoleType.getStaffRoleType()).isEqualTo(UPDATED_STAFF_ROLE_TYPE);
        assertThat(testStaffRoleType.getStaffRoleTypeDetails()).isEqualTo(UPDATED_STAFF_ROLE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffRoleTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaffRoleType() throws Exception {
        int databaseSizeBeforeUpdate = staffRoleTypeRepository.findAll().size();
        staffRoleType.setId(count.incrementAndGet());

        // Create the StaffRoleType
        StaffRoleTypeDTO staffRoleTypeDTO = staffRoleTypeMapper.toDto(staffRoleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffRoleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffRoleTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffRoleType in the database
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(0)).save(staffRoleType);
    }

    @Test
    @Transactional
    void deleteStaffRoleType() throws Exception {
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);

        int databaseSizeBeforeDelete = staffRoleTypeRepository.findAll().size();

        // Delete the staffRoleType
        restStaffRoleTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, staffRoleType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaffRoleType> staffRoleTypeList = staffRoleTypeRepository.findAll();
        assertThat(staffRoleTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StaffRoleType in Elasticsearch
        verify(mockStaffRoleTypeSearchRepository, times(1)).deleteById(staffRoleType.getId());
    }

    @Test
    @Transactional
    void searchStaffRoleType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        staffRoleTypeRepository.saveAndFlush(staffRoleType);
        when(mockStaffRoleTypeSearchRepository.search("id:" + staffRoleType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(staffRoleType), PageRequest.of(0, 1), 1));

        // Search the staffRoleType
        restStaffRoleTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + staffRoleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffRoleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffRoleTypeCode").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].staffRoleType").value(hasItem(DEFAULT_STAFF_ROLE_TYPE)))
            .andExpect(jsonPath("$.[*].staffRoleTypeDetails").value(hasItem(DEFAULT_STAFF_ROLE_TYPE_DETAILS.toString())));
    }
}
