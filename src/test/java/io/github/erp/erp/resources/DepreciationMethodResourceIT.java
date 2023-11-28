package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.enumeration.DepreciationTypes;
import io.github.erp.repository.DepreciationMethodRepository;
import io.github.erp.repository.search.DepreciationMethodSearchRepository;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.mapper.DepreciationMethodMapper;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
 * Integration tests for the DepreciationMethodResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class DepreciationMethodResourceIT {

    private static final String DEFAULT_DEPRECIATION_METHOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_METHOD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DepreciationTypes DEFAULT_DEPRECIATION_TYPE = DepreciationTypes.STRAIGHT_LINE;
    private static final DepreciationTypes UPDATED_DEPRECIATION_TYPE = DepreciationTypes.DECLINING_BALANCE;

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-methods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationMethodRepository depreciationMethodRepository;

    @Mock
    private DepreciationMethodRepository depreciationMethodRepositoryMock;

    @Autowired
    private DepreciationMethodMapper depreciationMethodMapper;

    @Mock
    private DepreciationMethodService depreciationMethodServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationMethodSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationMethodSearchRepository mockDepreciationMethodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationMethodMockMvc;

    private DepreciationMethod depreciationMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationMethod createEntity(EntityManager em) {
        DepreciationMethod depreciationMethod = new DepreciationMethod()
            .depreciationMethodName(DEFAULT_DEPRECIATION_METHOD_NAME)
            .description(DEFAULT_DESCRIPTION)
            .depreciationType(DEFAULT_DEPRECIATION_TYPE);
        return depreciationMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationMethod createUpdatedEntity(EntityManager em) {
        DepreciationMethod depreciationMethod = new DepreciationMethod()
            .depreciationMethodName(UPDATED_DEPRECIATION_METHOD_NAME)
            .description(UPDATED_DESCRIPTION)
            .depreciationType(UPDATED_DEPRECIATION_TYPE);
        return depreciationMethod;
    }

    @BeforeEach
    public void initTest() {
        depreciationMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationMethod() throws Exception {
        int databaseSizeBeforeCreate = depreciationMethodRepository.findAll().size();
        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);
        restDepreciationMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationMethod testDepreciationMethod = depreciationMethodList.get(depreciationMethodList.size() - 1);
        assertThat(testDepreciationMethod.getDepreciationMethodName()).isEqualTo(DEFAULT_DEPRECIATION_METHOD_NAME);
        assertThat(testDepreciationMethod.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepreciationMethod.getDepreciationType()).isEqualTo(DEFAULT_DEPRECIATION_TYPE);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(1)).save(testDepreciationMethod);
    }

    @Test
    @Transactional
    void createDepreciationMethodWithExistingId() throws Exception {
        // Create the DepreciationMethod with an existing ID
        depreciationMethod.setId(1L);
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        int databaseSizeBeforeCreate = depreciationMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void checkDepreciationMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationMethodRepository.findAll().size();
        // set the field null
        depreciationMethod.setDepreciationMethodName(null);

        // Create the DepreciationMethod, which fails.
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        restDepreciationMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepreciationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationMethodRepository.findAll().size();
        // set the field null
        depreciationMethod.setDepreciationType(null);

        // Create the DepreciationMethod, which fails.
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        restDepreciationMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepreciationMethods() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].depreciationMethodName").value(hasItem(DEFAULT_DEPRECIATION_METHOD_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationType").value(hasItem(DEFAULT_DEPRECIATION_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepreciationMethodsWithEagerRelationshipsIsEnabled() throws Exception {
        when(depreciationMethodServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepreciationMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depreciationMethodServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepreciationMethodsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depreciationMethodServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepreciationMethodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depreciationMethodServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDepreciationMethod() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get the depreciationMethod
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationMethod.getId().intValue()))
            .andExpect(jsonPath("$.depreciationMethodName").value(DEFAULT_DEPRECIATION_METHOD_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.depreciationType").value(DEFAULT_DEPRECIATION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getDepreciationMethodsByIdFiltering() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        Long id = depreciationMethod.getId();

        defaultDepreciationMethodShouldBeFound("id.equals=" + id);
        defaultDepreciationMethodShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationMethodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationMethodShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationMethodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationMethodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName equals to DEFAULT_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldBeFound("depreciationMethodName.equals=" + DEFAULT_DEPRECIATION_METHOD_NAME);

        // Get all the depreciationMethodList where depreciationMethodName equals to UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.equals=" + UPDATED_DEPRECIATION_METHOD_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName not equals to DEFAULT_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.notEquals=" + DEFAULT_DEPRECIATION_METHOD_NAME);

        // Get all the depreciationMethodList where depreciationMethodName not equals to UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldBeFound("depreciationMethodName.notEquals=" + UPDATED_DEPRECIATION_METHOD_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName in DEFAULT_DEPRECIATION_METHOD_NAME or UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldBeFound(
            "depreciationMethodName.in=" + DEFAULT_DEPRECIATION_METHOD_NAME + "," + UPDATED_DEPRECIATION_METHOD_NAME
        );

        // Get all the depreciationMethodList where depreciationMethodName equals to UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.in=" + UPDATED_DEPRECIATION_METHOD_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName is not null
        defaultDepreciationMethodShouldBeFound("depreciationMethodName.specified=true");

        // Get all the depreciationMethodList where depreciationMethodName is null
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameContainsSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName contains DEFAULT_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldBeFound("depreciationMethodName.contains=" + DEFAULT_DEPRECIATION_METHOD_NAME);

        // Get all the depreciationMethodList where depreciationMethodName contains UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.contains=" + UPDATED_DEPRECIATION_METHOD_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationMethodNameNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationMethodName does not contain DEFAULT_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldNotBeFound("depreciationMethodName.doesNotContain=" + DEFAULT_DEPRECIATION_METHOD_NAME);

        // Get all the depreciationMethodList where depreciationMethodName does not contain UPDATED_DEPRECIATION_METHOD_NAME
        defaultDepreciationMethodShouldBeFound("depreciationMethodName.doesNotContain=" + UPDATED_DEPRECIATION_METHOD_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description equals to DEFAULT_DESCRIPTION
        defaultDepreciationMethodShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationMethodList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description not equals to DEFAULT_DESCRIPTION
        defaultDepreciationMethodShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationMethodList where description not equals to UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the depreciationMethodList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description is not null
        defaultDepreciationMethodShouldBeFound("description.specified=true");

        // Get all the depreciationMethodList where description is null
        defaultDepreciationMethodShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description contains DEFAULT_DESCRIPTION
        defaultDepreciationMethodShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationMethodList where description contains UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where description does not contain DEFAULT_DESCRIPTION
        defaultDepreciationMethodShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationMethodList where description does not contain UPDATED_DESCRIPTION
        defaultDepreciationMethodShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationType equals to DEFAULT_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldBeFound("depreciationType.equals=" + DEFAULT_DEPRECIATION_TYPE);

        // Get all the depreciationMethodList where depreciationType equals to UPDATED_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldNotBeFound("depreciationType.equals=" + UPDATED_DEPRECIATION_TYPE);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationType not equals to DEFAULT_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldNotBeFound("depreciationType.notEquals=" + DEFAULT_DEPRECIATION_TYPE);

        // Get all the depreciationMethodList where depreciationType not equals to UPDATED_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldBeFound("depreciationType.notEquals=" + UPDATED_DEPRECIATION_TYPE);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationType in DEFAULT_DEPRECIATION_TYPE or UPDATED_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldBeFound("depreciationType.in=" + DEFAULT_DEPRECIATION_TYPE + "," + UPDATED_DEPRECIATION_TYPE);

        // Get all the depreciationMethodList where depreciationType equals to UPDATED_DEPRECIATION_TYPE
        defaultDepreciationMethodShouldNotBeFound("depreciationType.in=" + UPDATED_DEPRECIATION_TYPE);
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByDepreciationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        // Get all the depreciationMethodList where depreciationType is not null
        defaultDepreciationMethodShouldBeFound("depreciationType.specified=true");

        // Get all the depreciationMethodList where depreciationType is null
        defaultDepreciationMethodShouldNotBeFound("depreciationType.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationMethodsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = io.github.erp.erp.resources.PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        depreciationMethod.addPlaceholder(placeholder);
        depreciationMethodRepository.saveAndFlush(depreciationMethod);
        Long placeholderId = placeholder.getId();

        // Get all the depreciationMethodList where placeholder equals to placeholderId
        defaultDepreciationMethodShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the depreciationMethodList where placeholder equals to (placeholderId + 1)
        defaultDepreciationMethodShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationMethodShouldBeFound(String filter) throws Exception {
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].depreciationMethodName").value(hasItem(DEFAULT_DEPRECIATION_METHOD_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationType").value(hasItem(DEFAULT_DEPRECIATION_TYPE.toString())));

        // Check, that the count call also returns 1
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationMethodShouldNotBeFound(String filter) throws Exception {
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationMethod() throws Exception {
        // Get the depreciationMethod
        restDepreciationMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationMethod() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();

        // Update the depreciationMethod
        DepreciationMethod updatedDepreciationMethod = depreciationMethodRepository.findById(depreciationMethod.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationMethod are not directly saved in db
        em.detach(updatedDepreciationMethod);
        updatedDepreciationMethod
            .depreciationMethodName(UPDATED_DEPRECIATION_METHOD_NAME)
            .description(UPDATED_DESCRIPTION)
            .depreciationType(UPDATED_DEPRECIATION_TYPE);
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(updatedDepreciationMethod);

        restDepreciationMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationMethod testDepreciationMethod = depreciationMethodList.get(depreciationMethodList.size() - 1);
        assertThat(testDepreciationMethod.getDepreciationMethodName()).isEqualTo(UPDATED_DEPRECIATION_METHOD_NAME);
        assertThat(testDepreciationMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepreciationMethod.getDepreciationType()).isEqualTo(UPDATED_DEPRECIATION_TYPE);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository).save(testDepreciationMethod);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationMethodWithPatch() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();

        // Update the depreciationMethod using partial update
        DepreciationMethod partialUpdatedDepreciationMethod = new DepreciationMethod();
        partialUpdatedDepreciationMethod.setId(depreciationMethod.getId());

        partialUpdatedDepreciationMethod.description(UPDATED_DESCRIPTION).depreciationType(UPDATED_DEPRECIATION_TYPE);

        restDepreciationMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationMethod))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationMethod testDepreciationMethod = depreciationMethodList.get(depreciationMethodList.size() - 1);
        assertThat(testDepreciationMethod.getDepreciationMethodName()).isEqualTo(DEFAULT_DEPRECIATION_METHOD_NAME);
        assertThat(testDepreciationMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepreciationMethod.getDepreciationType()).isEqualTo(UPDATED_DEPRECIATION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationMethodWithPatch() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();

        // Update the depreciationMethod using partial update
        DepreciationMethod partialUpdatedDepreciationMethod = new DepreciationMethod();
        partialUpdatedDepreciationMethod.setId(depreciationMethod.getId());

        partialUpdatedDepreciationMethod
            .depreciationMethodName(UPDATED_DEPRECIATION_METHOD_NAME)
            .description(UPDATED_DESCRIPTION)
            .depreciationType(UPDATED_DEPRECIATION_TYPE);

        restDepreciationMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationMethod))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationMethod testDepreciationMethod = depreciationMethodList.get(depreciationMethodList.size() - 1);
        assertThat(testDepreciationMethod.getDepreciationMethodName()).isEqualTo(UPDATED_DEPRECIATION_METHOD_NAME);
        assertThat(testDepreciationMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepreciationMethod.getDepreciationType()).isEqualTo(UPDATED_DEPRECIATION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationMethodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationMethod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationMethodRepository.findAll().size();
        depreciationMethod.setId(count.incrementAndGet());

        // Create the DepreciationMethod
        DepreciationMethodDTO depreciationMethodDTO = depreciationMethodMapper.toDto(depreciationMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationMethod in the database
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(0)).save(depreciationMethod);
    }

    @Test
    @Transactional
    void deleteDepreciationMethod() throws Exception {
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);

        int databaseSizeBeforeDelete = depreciationMethodRepository.findAll().size();

        // Delete the depreciationMethod
        restDepreciationMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationMethod> depreciationMethodList = depreciationMethodRepository.findAll();
        assertThat(depreciationMethodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationMethod in Elasticsearch
        verify(mockDepreciationMethodSearchRepository, times(1)).deleteById(depreciationMethod.getId());
    }

    @Test
    @Transactional
    void searchDepreciationMethod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationMethodRepository.saveAndFlush(depreciationMethod);
        when(mockDepreciationMethodSearchRepository.search("id:" + depreciationMethod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationMethod), PageRequest.of(0, 1), 1));

        // Search the depreciationMethod
        restDepreciationMethodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].depreciationMethodName").value(hasItem(DEFAULT_DEPRECIATION_METHOD_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationType").value(hasItem(DEFAULT_DEPRECIATION_TYPE.toString())));
    }
}
