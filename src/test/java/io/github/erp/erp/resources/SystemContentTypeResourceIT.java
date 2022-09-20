package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.1-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SystemContentType;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.SystemContentTypeAvailability;
import io.github.erp.repository.SystemContentTypeRepository;
import io.github.erp.repository.search.SystemContentTypeSearchRepository;
import io.github.erp.service.SystemContentTypeService;
import io.github.erp.service.dto.SystemContentTypeDTO;
import io.github.erp.service.mapper.SystemContentTypeMapper;
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
 * Integration tests for the {@link SystemContentTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
class SystemContentTypeResourceIT {

    private static final String DEFAULT_CONTENT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE_HEADER = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE_HEADER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final SystemContentTypeAvailability DEFAULT_AVAILABILITY = SystemContentTypeAvailability.SUPPORTED;
    private static final SystemContentTypeAvailability UPDATED_AVAILABILITY = SystemContentTypeAvailability.NOT_SUPPORTED;

    private static final String ENTITY_API_URL = "/api/dev/system-content-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/system-content-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemContentTypeRepository systemContentTypeRepository;

    @Mock
    private SystemContentTypeRepository systemContentTypeRepositoryMock;

    @Autowired
    private SystemContentTypeMapper systemContentTypeMapper;

    @Mock
    private SystemContentTypeService systemContentTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SystemContentTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemContentTypeSearchRepository mockSystemContentTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemContentTypeMockMvc;

    private SystemContentType systemContentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemContentType createEntity(EntityManager em) {
        SystemContentType systemContentType = new SystemContentType()
            .contentTypeName(DEFAULT_CONTENT_TYPE_NAME)
            .contentTypeHeader(DEFAULT_CONTENT_TYPE_HEADER)
            .comments(DEFAULT_COMMENTS)
            .availability(DEFAULT_AVAILABILITY);
        return systemContentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemContentType createUpdatedEntity(EntityManager em) {
        SystemContentType systemContentType = new SystemContentType()
            .contentTypeName(UPDATED_CONTENT_TYPE_NAME)
            .contentTypeHeader(UPDATED_CONTENT_TYPE_HEADER)
            .comments(UPDATED_COMMENTS)
            .availability(UPDATED_AVAILABILITY);
        return systemContentType;
    }

    @BeforeEach
    public void initTest() {
        systemContentType = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemContentType() throws Exception {
        int databaseSizeBeforeCreate = systemContentTypeRepository.findAll().size();
        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);
        restSystemContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SystemContentType testSystemContentType = systemContentTypeList.get(systemContentTypeList.size() - 1);
        assertThat(testSystemContentType.getContentTypeName()).isEqualTo(DEFAULT_CONTENT_TYPE_NAME);
        assertThat(testSystemContentType.getContentTypeHeader()).isEqualTo(DEFAULT_CONTENT_TYPE_HEADER);
        assertThat(testSystemContentType.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testSystemContentType.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(1)).save(testSystemContentType);
    }

    @Test
    @Transactional
    void createSystemContentTypeWithExistingId() throws Exception {
        // Create the SystemContentType with an existing ID
        systemContentType.setId(1L);
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        int databaseSizeBeforeCreate = systemContentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void checkContentTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemContentTypeRepository.findAll().size();
        // set the field null
        systemContentType.setContentTypeName(null);

        // Create the SystemContentType, which fails.
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        restSystemContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentTypeHeaderIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemContentTypeRepository.findAll().size();
        // set the field null
        systemContentType.setContentTypeHeader(null);

        // Create the SystemContentType, which fails.
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        restSystemContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemContentTypeRepository.findAll().size();
        // set the field null
        systemContentType.setAvailability(null);

        // Create the SystemContentType, which fails.
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        restSystemContentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemContentTypes() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentTypeName").value(hasItem(DEFAULT_CONTENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contentTypeHeader").value(hasItem(DEFAULT_CONTENT_TYPE_HEADER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemContentTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(systemContentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemContentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemContentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemContentTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(systemContentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemContentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemContentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSystemContentType() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get the systemContentType
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, systemContentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemContentType.getId().intValue()))
            .andExpect(jsonPath("$.contentTypeName").value(DEFAULT_CONTENT_TYPE_NAME))
            .andExpect(jsonPath("$.contentTypeHeader").value(DEFAULT_CONTENT_TYPE_HEADER))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()));
    }

    @Test
    @Transactional
    void getSystemContentTypesByIdFiltering() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        Long id = systemContentType.getId();

        defaultSystemContentTypeShouldBeFound("id.equals=" + id);
        defaultSystemContentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSystemContentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemContentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemContentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemContentTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName equals to DEFAULT_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldBeFound("contentTypeName.equals=" + DEFAULT_CONTENT_TYPE_NAME);

        // Get all the systemContentTypeList where contentTypeName equals to UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.equals=" + UPDATED_CONTENT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName not equals to DEFAULT_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.notEquals=" + DEFAULT_CONTENT_TYPE_NAME);

        // Get all the systemContentTypeList where contentTypeName not equals to UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldBeFound("contentTypeName.notEquals=" + UPDATED_CONTENT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName in DEFAULT_CONTENT_TYPE_NAME or UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldBeFound("contentTypeName.in=" + DEFAULT_CONTENT_TYPE_NAME + "," + UPDATED_CONTENT_TYPE_NAME);

        // Get all the systemContentTypeList where contentTypeName equals to UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.in=" + UPDATED_CONTENT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName is not null
        defaultSystemContentTypeShouldBeFound("contentTypeName.specified=true");

        // Get all the systemContentTypeList where contentTypeName is null
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameContainsSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName contains DEFAULT_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldBeFound("contentTypeName.contains=" + DEFAULT_CONTENT_TYPE_NAME);

        // Get all the systemContentTypeList where contentTypeName contains UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.contains=" + UPDATED_CONTENT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeName does not contain DEFAULT_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldNotBeFound("contentTypeName.doesNotContain=" + DEFAULT_CONTENT_TYPE_NAME);

        // Get all the systemContentTypeList where contentTypeName does not contain UPDATED_CONTENT_TYPE_NAME
        defaultSystemContentTypeShouldBeFound("contentTypeName.doesNotContain=" + UPDATED_CONTENT_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderIsEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader equals to DEFAULT_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.equals=" + DEFAULT_CONTENT_TYPE_HEADER);

        // Get all the systemContentTypeList where contentTypeHeader equals to UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.equals=" + UPDATED_CONTENT_TYPE_HEADER);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader not equals to DEFAULT_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.notEquals=" + DEFAULT_CONTENT_TYPE_HEADER);

        // Get all the systemContentTypeList where contentTypeHeader not equals to UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.notEquals=" + UPDATED_CONTENT_TYPE_HEADER);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderIsInShouldWork() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader in DEFAULT_CONTENT_TYPE_HEADER or UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.in=" + DEFAULT_CONTENT_TYPE_HEADER + "," + UPDATED_CONTENT_TYPE_HEADER);

        // Get all the systemContentTypeList where contentTypeHeader equals to UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.in=" + UPDATED_CONTENT_TYPE_HEADER);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader is not null
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.specified=true");

        // Get all the systemContentTypeList where contentTypeHeader is null
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderContainsSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader contains DEFAULT_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.contains=" + DEFAULT_CONTENT_TYPE_HEADER);

        // Get all the systemContentTypeList where contentTypeHeader contains UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.contains=" + UPDATED_CONTENT_TYPE_HEADER);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByContentTypeHeaderNotContainsSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where contentTypeHeader does not contain DEFAULT_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldNotBeFound("contentTypeHeader.doesNotContain=" + DEFAULT_CONTENT_TYPE_HEADER);

        // Get all the systemContentTypeList where contentTypeHeader does not contain UPDATED_CONTENT_TYPE_HEADER
        defaultSystemContentTypeShouldBeFound("contentTypeHeader.doesNotContain=" + UPDATED_CONTENT_TYPE_HEADER);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByAvailabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where availability equals to DEFAULT_AVAILABILITY
        defaultSystemContentTypeShouldBeFound("availability.equals=" + DEFAULT_AVAILABILITY);

        // Get all the systemContentTypeList where availability equals to UPDATED_AVAILABILITY
        defaultSystemContentTypeShouldNotBeFound("availability.equals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByAvailabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where availability not equals to DEFAULT_AVAILABILITY
        defaultSystemContentTypeShouldNotBeFound("availability.notEquals=" + DEFAULT_AVAILABILITY);

        // Get all the systemContentTypeList where availability not equals to UPDATED_AVAILABILITY
        defaultSystemContentTypeShouldBeFound("availability.notEquals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByAvailabilityIsInShouldWork() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where availability in DEFAULT_AVAILABILITY or UPDATED_AVAILABILITY
        defaultSystemContentTypeShouldBeFound("availability.in=" + DEFAULT_AVAILABILITY + "," + UPDATED_AVAILABILITY);

        // Get all the systemContentTypeList where availability equals to UPDATED_AVAILABILITY
        defaultSystemContentTypeShouldNotBeFound("availability.in=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByAvailabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        // Get all the systemContentTypeList where availability is not null
        defaultSystemContentTypeShouldBeFound("availability.specified=true");

        // Get all the systemContentTypeList where availability is null
        defaultSystemContentTypeShouldNotBeFound("availability.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemContentTypesByPlaceholdersIsEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);
        Placeholder placeholders;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholders = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholders);
            em.flush();
        } else {
            placeholders = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholders);
        em.flush();
        systemContentType.addPlaceholders(placeholders);
        systemContentTypeRepository.saveAndFlush(systemContentType);
        Long placeholdersId = placeholders.getId();

        // Get all the systemContentTypeList where placeholders equals to placeholdersId
        defaultSystemContentTypeShouldBeFound("placeholdersId.equals=" + placeholdersId);

        // Get all the systemContentTypeList where placeholders equals to (placeholdersId + 1)
        defaultSystemContentTypeShouldNotBeFound("placeholdersId.equals=" + (placeholdersId + 1));
    }

    @Test
    @Transactional
    void getAllSystemContentTypesBySysMapsIsEqualToSomething() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);
        UniversallyUniqueMapping sysMaps;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            sysMaps = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(sysMaps);
            em.flush();
        } else {
            sysMaps = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(sysMaps);
        em.flush();
        systemContentType.addSysMaps(sysMaps);
        systemContentTypeRepository.saveAndFlush(systemContentType);
        Long sysMapsId = sysMaps.getId();

        // Get all the systemContentTypeList where sysMaps equals to sysMapsId
        defaultSystemContentTypeShouldBeFound("sysMapsId.equals=" + sysMapsId);

        // Get all the systemContentTypeList where sysMaps equals to (sysMapsId + 1)
        defaultSystemContentTypeShouldNotBeFound("sysMapsId.equals=" + (sysMapsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemContentTypeShouldBeFound(String filter) throws Exception {
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentTypeName").value(hasItem(DEFAULT_CONTENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contentTypeHeader").value(hasItem(DEFAULT_CONTENT_TYPE_HEADER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())));

        // Check, that the count call also returns 1
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemContentTypeShouldNotBeFound(String filter) throws Exception {
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemContentType() throws Exception {
        // Get the systemContentType
        restSystemContentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemContentType() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();

        // Update the systemContentType
        SystemContentType updatedSystemContentType = systemContentTypeRepository.findById(systemContentType.getId()).get();
        // Disconnect from session so that the updates on updatedSystemContentType are not directly saved in db
        em.detach(updatedSystemContentType);
        updatedSystemContentType
            .contentTypeName(UPDATED_CONTENT_TYPE_NAME)
            .contentTypeHeader(UPDATED_CONTENT_TYPE_HEADER)
            .comments(UPDATED_COMMENTS)
            .availability(UPDATED_AVAILABILITY);
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(updatedSystemContentType);

        restSystemContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemContentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);
        SystemContentType testSystemContentType = systemContentTypeList.get(systemContentTypeList.size() - 1);
        assertThat(testSystemContentType.getContentTypeName()).isEqualTo(UPDATED_CONTENT_TYPE_NAME);
        assertThat(testSystemContentType.getContentTypeHeader()).isEqualTo(UPDATED_CONTENT_TYPE_HEADER);
        assertThat(testSystemContentType.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testSystemContentType.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository).save(testSystemContentType);
    }

    @Test
    @Transactional
    void putNonExistingSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemContentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void partialUpdateSystemContentTypeWithPatch() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();

        // Update the systemContentType using partial update
        SystemContentType partialUpdatedSystemContentType = new SystemContentType();
        partialUpdatedSystemContentType.setId(systemContentType.getId());

        partialUpdatedSystemContentType.availability(UPDATED_AVAILABILITY);

        restSystemContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemContentType))
            )
            .andExpect(status().isOk());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);
        SystemContentType testSystemContentType = systemContentTypeList.get(systemContentTypeList.size() - 1);
        assertThat(testSystemContentType.getContentTypeName()).isEqualTo(DEFAULT_CONTENT_TYPE_NAME);
        assertThat(testSystemContentType.getContentTypeHeader()).isEqualTo(DEFAULT_CONTENT_TYPE_HEADER);
        assertThat(testSystemContentType.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testSystemContentType.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void fullUpdateSystemContentTypeWithPatch() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();

        // Update the systemContentType using partial update
        SystemContentType partialUpdatedSystemContentType = new SystemContentType();
        partialUpdatedSystemContentType.setId(systemContentType.getId());

        partialUpdatedSystemContentType
            .contentTypeName(UPDATED_CONTENT_TYPE_NAME)
            .contentTypeHeader(UPDATED_CONTENT_TYPE_HEADER)
            .comments(UPDATED_COMMENTS)
            .availability(UPDATED_AVAILABILITY);

        restSystemContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemContentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemContentType))
            )
            .andExpect(status().isOk());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);
        SystemContentType testSystemContentType = systemContentTypeList.get(systemContentTypeList.size() - 1);
        assertThat(testSystemContentType.getContentTypeName()).isEqualTo(UPDATED_CONTENT_TYPE_NAME);
        assertThat(testSystemContentType.getContentTypeHeader()).isEqualTo(UPDATED_CONTENT_TYPE_HEADER);
        assertThat(testSystemContentType.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testSystemContentType.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void patchNonExistingSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemContentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemContentType() throws Exception {
        int databaseSizeBeforeUpdate = systemContentTypeRepository.findAll().size();
        systemContentType.setId(count.incrementAndGet());

        // Create the SystemContentType
        SystemContentTypeDTO systemContentTypeDTO = systemContentTypeMapper.toDto(systemContentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemContentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemContentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemContentType in the database
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(0)).save(systemContentType);
    }

    @Test
    @Transactional
    void deleteSystemContentType() throws Exception {
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);

        int databaseSizeBeforeDelete = systemContentTypeRepository.findAll().size();

        // Delete the systemContentType
        restSystemContentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemContentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemContentType> systemContentTypeList = systemContentTypeRepository.findAll();
        assertThat(systemContentTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SystemContentType in Elasticsearch
        verify(mockSystemContentTypeSearchRepository, times(1)).deleteById(systemContentType.getId());
    }

    @Test
    @Transactional
    void searchSystemContentType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        systemContentTypeRepository.saveAndFlush(systemContentType);
        when(mockSystemContentTypeSearchRepository.search("id:" + systemContentType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(systemContentType), PageRequest.of(0, 1), 1));

        // Search the systemContentType
        restSystemContentTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + systemContentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemContentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentTypeName").value(hasItem(DEFAULT_CONTENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].contentTypeHeader").value(hasItem(DEFAULT_CONTENT_TYPE_HEADER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())));
    }
}
