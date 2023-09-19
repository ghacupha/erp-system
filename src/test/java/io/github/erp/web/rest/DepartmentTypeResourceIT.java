package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.DepartmentType;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.DepartmentTypeRepository;
import io.github.erp.repository.search.DepartmentTypeSearchRepository;
import io.github.erp.service.DepartmentTypeService;
import io.github.erp.service.criteria.DepartmentTypeCriteria;
import io.github.erp.service.dto.DepartmentTypeDTO;
import io.github.erp.service.mapper.DepartmentTypeMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link DepartmentTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepartmentTypeResourceIT {

    private static final String DEFAULT_DEPARTMENT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/department-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/department-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartmentTypeRepository departmentTypeRepository;

    @Mock
    private DepartmentTypeRepository departmentTypeRepositoryMock;

    @Autowired
    private DepartmentTypeMapper departmentTypeMapper;

    @Mock
    private DepartmentTypeService departmentTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepartmentTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentTypeSearchRepository mockDepartmentTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentTypeMockMvc;

    private DepartmentType departmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentType createEntity(EntityManager em) {
        DepartmentType departmentType = new DepartmentType()
            .departmentTypeCode(DEFAULT_DEPARTMENT_TYPE_CODE)
            .departmentType(DEFAULT_DEPARTMENT_TYPE)
            .departmentTypeDetails(DEFAULT_DEPARTMENT_TYPE_DETAILS);
        return departmentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentType createUpdatedEntity(EntityManager em) {
        DepartmentType departmentType = new DepartmentType()
            .departmentTypeCode(UPDATED_DEPARTMENT_TYPE_CODE)
            .departmentType(UPDATED_DEPARTMENT_TYPE)
            .departmentTypeDetails(UPDATED_DEPARTMENT_TYPE_DETAILS);
        return departmentType;
    }

    @BeforeEach
    public void initTest() {
        departmentType = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartmentType() throws Exception {
        int databaseSizeBeforeCreate = departmentTypeRepository.findAll().size();
        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);
        restDepartmentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentType testDepartmentType = departmentTypeList.get(departmentTypeList.size() - 1);
        assertThat(testDepartmentType.getDepartmentTypeCode()).isEqualTo(DEFAULT_DEPARTMENT_TYPE_CODE);
        assertThat(testDepartmentType.getDepartmentType()).isEqualTo(DEFAULT_DEPARTMENT_TYPE);
        assertThat(testDepartmentType.getDepartmentTypeDetails()).isEqualTo(DEFAULT_DEPARTMENT_TYPE_DETAILS);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(1)).save(testDepartmentType);
    }

    @Test
    @Transactional
    void createDepartmentTypeWithExistingId() throws Exception {
        // Create the DepartmentType with an existing ID
        departmentType.setId(1L);
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        int databaseSizeBeforeCreate = departmentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void checkDepartmentTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentTypeRepository.findAll().size();
        // set the field null
        departmentType.setDepartmentTypeCode(null);

        // Create the DepartmentType, which fails.
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        restDepartmentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentTypeRepository.findAll().size();
        // set the field null
        departmentType.setDepartmentType(null);

        // Create the DepartmentType, which fails.
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        restDepartmentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartmentTypes() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentTypeCode").value(hasItem(DEFAULT_DEPARTMENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].departmentType").value(hasItem(DEFAULT_DEPARTMENT_TYPE)))
            .andExpect(jsonPath("$.[*].departmentTypeDetails").value(hasItem(DEFAULT_DEPARTMENT_TYPE_DETAILS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(departmentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartmentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departmentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departmentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartmentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departmentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDepartmentType() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get the departmentType
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, departmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentType.getId().intValue()))
            .andExpect(jsonPath("$.departmentTypeCode").value(DEFAULT_DEPARTMENT_TYPE_CODE))
            .andExpect(jsonPath("$.departmentType").value(DEFAULT_DEPARTMENT_TYPE))
            .andExpect(jsonPath("$.departmentTypeDetails").value(DEFAULT_DEPARTMENT_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getDepartmentTypesByIdFiltering() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        Long id = departmentType.getId();

        defaultDepartmentTypeShouldBeFound("id.equals=" + id);
        defaultDepartmentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode equals to DEFAULT_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.equals=" + DEFAULT_DEPARTMENT_TYPE_CODE);

        // Get all the departmentTypeList where departmentTypeCode equals to UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.equals=" + UPDATED_DEPARTMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode not equals to DEFAULT_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.notEquals=" + DEFAULT_DEPARTMENT_TYPE_CODE);

        // Get all the departmentTypeList where departmentTypeCode not equals to UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.notEquals=" + UPDATED_DEPARTMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode in DEFAULT_DEPARTMENT_TYPE_CODE or UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.in=" + DEFAULT_DEPARTMENT_TYPE_CODE + "," + UPDATED_DEPARTMENT_TYPE_CODE);

        // Get all the departmentTypeList where departmentTypeCode equals to UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.in=" + UPDATED_DEPARTMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode is not null
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.specified=true");

        // Get all the departmentTypeList where departmentTypeCode is null
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode contains DEFAULT_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.contains=" + DEFAULT_DEPARTMENT_TYPE_CODE);

        // Get all the departmentTypeList where departmentTypeCode contains UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.contains=" + UPDATED_DEPARTMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentTypeCode does not contain DEFAULT_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldNotBeFound("departmentTypeCode.doesNotContain=" + DEFAULT_DEPARTMENT_TYPE_CODE);

        // Get all the departmentTypeList where departmentTypeCode does not contain UPDATED_DEPARTMENT_TYPE_CODE
        defaultDepartmentTypeShouldBeFound("departmentTypeCode.doesNotContain=" + UPDATED_DEPARTMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType equals to DEFAULT_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldBeFound("departmentType.equals=" + DEFAULT_DEPARTMENT_TYPE);

        // Get all the departmentTypeList where departmentType equals to UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldNotBeFound("departmentType.equals=" + UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType not equals to DEFAULT_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldNotBeFound("departmentType.notEquals=" + DEFAULT_DEPARTMENT_TYPE);

        // Get all the departmentTypeList where departmentType not equals to UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldBeFound("departmentType.notEquals=" + UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType in DEFAULT_DEPARTMENT_TYPE or UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldBeFound("departmentType.in=" + DEFAULT_DEPARTMENT_TYPE + "," + UPDATED_DEPARTMENT_TYPE);

        // Get all the departmentTypeList where departmentType equals to UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldNotBeFound("departmentType.in=" + UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType is not null
        defaultDepartmentTypeShouldBeFound("departmentType.specified=true");

        // Get all the departmentTypeList where departmentType is null
        defaultDepartmentTypeShouldNotBeFound("departmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeContainsSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType contains DEFAULT_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldBeFound("departmentType.contains=" + DEFAULT_DEPARTMENT_TYPE);

        // Get all the departmentTypeList where departmentType contains UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldNotBeFound("departmentType.contains=" + UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByDepartmentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        // Get all the departmentTypeList where departmentType does not contain DEFAULT_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldNotBeFound("departmentType.doesNotContain=" + DEFAULT_DEPARTMENT_TYPE);

        // Get all the departmentTypeList where departmentType does not contain UPDATED_DEPARTMENT_TYPE
        defaultDepartmentTypeShouldBeFound("departmentType.doesNotContain=" + UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        departmentType.addPlaceholder(placeholder);
        departmentTypeRepository.saveAndFlush(departmentType);
        Long placeholderId = placeholder.getId();

        // Get all the departmentTypeList where placeholder equals to placeholderId
        defaultDepartmentTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the departmentTypeList where placeholder equals to (placeholderId + 1)
        defaultDepartmentTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentTypeShouldBeFound(String filter) throws Exception {
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentTypeCode").value(hasItem(DEFAULT_DEPARTMENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].departmentType").value(hasItem(DEFAULT_DEPARTMENT_TYPE)))
            .andExpect(jsonPath("$.[*].departmentTypeDetails").value(hasItem(DEFAULT_DEPARTMENT_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentTypeShouldNotBeFound(String filter) throws Exception {
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartmentType() throws Exception {
        // Get the departmentType
        restDepartmentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepartmentType() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();

        // Update the departmentType
        DepartmentType updatedDepartmentType = departmentTypeRepository.findById(departmentType.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentType are not directly saved in db
        em.detach(updatedDepartmentType);
        updatedDepartmentType
            .departmentTypeCode(UPDATED_DEPARTMENT_TYPE_CODE)
            .departmentType(UPDATED_DEPARTMENT_TYPE)
            .departmentTypeDetails(UPDATED_DEPARTMENT_TYPE_DETAILS);
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(updatedDepartmentType);

        restDepartmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);
        DepartmentType testDepartmentType = departmentTypeList.get(departmentTypeList.size() - 1);
        assertThat(testDepartmentType.getDepartmentTypeCode()).isEqualTo(UPDATED_DEPARTMENT_TYPE_CODE);
        assertThat(testDepartmentType.getDepartmentType()).isEqualTo(UPDATED_DEPARTMENT_TYPE);
        assertThat(testDepartmentType.getDepartmentTypeDetails()).isEqualTo(UPDATED_DEPARTMENT_TYPE_DETAILS);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository).save(testDepartmentType);
    }

    @Test
    @Transactional
    void putNonExistingDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentTypeWithPatch() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();

        // Update the departmentType using partial update
        DepartmentType partialUpdatedDepartmentType = new DepartmentType();
        partialUpdatedDepartmentType.setId(departmentType.getId());

        partialUpdatedDepartmentType.departmentTypeCode(UPDATED_DEPARTMENT_TYPE_CODE).departmentType(UPDATED_DEPARTMENT_TYPE);

        restDepartmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartmentType))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);
        DepartmentType testDepartmentType = departmentTypeList.get(departmentTypeList.size() - 1);
        assertThat(testDepartmentType.getDepartmentTypeCode()).isEqualTo(UPDATED_DEPARTMENT_TYPE_CODE);
        assertThat(testDepartmentType.getDepartmentType()).isEqualTo(UPDATED_DEPARTMENT_TYPE);
        assertThat(testDepartmentType.getDepartmentTypeDetails()).isEqualTo(DEFAULT_DEPARTMENT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentTypeWithPatch() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();

        // Update the departmentType using partial update
        DepartmentType partialUpdatedDepartmentType = new DepartmentType();
        partialUpdatedDepartmentType.setId(departmentType.getId());

        partialUpdatedDepartmentType
            .departmentTypeCode(UPDATED_DEPARTMENT_TYPE_CODE)
            .departmentType(UPDATED_DEPARTMENT_TYPE)
            .departmentTypeDetails(UPDATED_DEPARTMENT_TYPE_DETAILS);

        restDepartmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartmentType))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);
        DepartmentType testDepartmentType = departmentTypeList.get(departmentTypeList.size() - 1);
        assertThat(testDepartmentType.getDepartmentTypeCode()).isEqualTo(UPDATED_DEPARTMENT_TYPE_CODE);
        assertThat(testDepartmentType.getDepartmentType()).isEqualTo(UPDATED_DEPARTMENT_TYPE);
        assertThat(testDepartmentType.getDepartmentTypeDetails()).isEqualTo(UPDATED_DEPARTMENT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartmentType() throws Exception {
        int databaseSizeBeforeUpdate = departmentTypeRepository.findAll().size();
        departmentType.setId(count.incrementAndGet());

        // Create the DepartmentType
        DepartmentTypeDTO departmentTypeDTO = departmentTypeMapper.toDto(departmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentType in the database
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(0)).save(departmentType);
    }

    @Test
    @Transactional
    void deleteDepartmentType() throws Exception {
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);

        int databaseSizeBeforeDelete = departmentTypeRepository.findAll().size();

        // Delete the departmentType
        restDepartmentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, departmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentType> departmentTypeList = departmentTypeRepository.findAll();
        assertThat(departmentTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentType in Elasticsearch
        verify(mockDepartmentTypeSearchRepository, times(1)).deleteById(departmentType.getId());
    }

    @Test
    @Transactional
    void searchDepartmentType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        departmentTypeRepository.saveAndFlush(departmentType);
        when(mockDepartmentTypeSearchRepository.search("id:" + departmentType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(departmentType), PageRequest.of(0, 1), 1));

        // Search the departmentType
        restDepartmentTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + departmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentTypeCode").value(hasItem(DEFAULT_DEPARTMENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].departmentType").value(hasItem(DEFAULT_DEPARTMENT_TYPE)))
            .andExpect(jsonPath("$.[*].departmentTypeDetails").value(hasItem(DEFAULT_DEPARTMENT_TYPE_DETAILS.toString())));
    }
}
