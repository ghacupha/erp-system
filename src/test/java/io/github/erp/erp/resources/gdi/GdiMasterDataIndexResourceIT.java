package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.domain.GdiMasterDataIndex;
import io.github.erp.repository.GdiMasterDataIndexRepository;
import io.github.erp.repository.search.GdiMasterDataIndexSearchRepository;
import io.github.erp.service.dto.GdiMasterDataIndexDTO;
import io.github.erp.service.mapper.GdiMasterDataIndexMapper;
import io.github.erp.web.rest.GdiMasterDataIndexResource;
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
 * Integration tests for the {@link GdiMasterDataIndexResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class GdiMasterDataIndexResourceIT {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATABASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATABASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DATA_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/gdi-master-data-indices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/gdi-master-data-indices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GdiMasterDataIndexRepository gdiMasterDataIndexRepository;

    @Autowired
    private GdiMasterDataIndexMapper gdiMasterDataIndexMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.GdiMasterDataIndexSearchRepositoryMockConfiguration
     */
    @Autowired
    private GdiMasterDataIndexSearchRepository mockGdiMasterDataIndexSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGdiMasterDataIndexMockMvc;

    private GdiMasterDataIndex gdiMasterDataIndex;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GdiMasterDataIndex createEntity(EntityManager em) {
        GdiMasterDataIndex gdiMasterDataIndex = new GdiMasterDataIndex()
            .entityName(DEFAULT_ENTITY_NAME)
            .databaseName(DEFAULT_DATABASE_NAME)
            .businessDescription(DEFAULT_BUSINESS_DESCRIPTION)
            .dataPath(DEFAULT_DATA_PATH);
        return gdiMasterDataIndex;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GdiMasterDataIndex createUpdatedEntity(EntityManager em) {
        GdiMasterDataIndex gdiMasterDataIndex = new GdiMasterDataIndex()
            .entityName(UPDATED_ENTITY_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .businessDescription(UPDATED_BUSINESS_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);
        return gdiMasterDataIndex;
    }

    @BeforeEach
    public void initTest() {
        gdiMasterDataIndex = createEntity(em);
    }

    @Test
    @Transactional
    void createGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeCreate = gdiMasterDataIndexRepository.findAll().size();
        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);
        restGdiMasterDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeCreate + 1);
        GdiMasterDataIndex testGdiMasterDataIndex = gdiMasterDataIndexList.get(gdiMasterDataIndexList.size() - 1);
        assertThat(testGdiMasterDataIndex.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testGdiMasterDataIndex.getDatabaseName()).isEqualTo(DEFAULT_DATABASE_NAME);
        assertThat(testGdiMasterDataIndex.getBusinessDescription()).isEqualTo(DEFAULT_BUSINESS_DESCRIPTION);
        assertThat(testGdiMasterDataIndex.getDataPath()).isEqualTo(DEFAULT_DATA_PATH);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(1)).save(testGdiMasterDataIndex);
    }

    @Test
    @Transactional
    void createGdiMasterDataIndexWithExistingId() throws Exception {
        // Create the GdiMasterDataIndex with an existing ID
        gdiMasterDataIndex.setId(1L);
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        int databaseSizeBeforeCreate = gdiMasterDataIndexRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGdiMasterDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeCreate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiMasterDataIndexRepository.findAll().size();
        // set the field null
        gdiMasterDataIndex.setEntityName(null);

        // Create the GdiMasterDataIndex, which fails.
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        restGdiMasterDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatabaseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiMasterDataIndexRepository.findAll().size();
        // set the field null
        gdiMasterDataIndex.setDatabaseName(null);

        // Create the GdiMasterDataIndex, which fails.
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        restGdiMasterDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndices() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiMasterDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].businessDescription").value(hasItem(DEFAULT_BUSINESS_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));
    }

    @Test
    @Transactional
    void getGdiMasterDataIndex() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get the gdiMasterDataIndex
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL_ID, gdiMasterDataIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gdiMasterDataIndex.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.databaseName").value(DEFAULT_DATABASE_NAME))
            .andExpect(jsonPath("$.businessDescription").value(DEFAULT_BUSINESS_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dataPath").value(DEFAULT_DATA_PATH));
    }

    @Test
    @Transactional
    void getGdiMasterDataIndicesByIdFiltering() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        Long id = gdiMasterDataIndex.getId();

        defaultGdiMasterDataIndexShouldBeFound("id.equals=" + id);
        defaultGdiMasterDataIndexShouldNotBeFound("id.notEquals=" + id);

        defaultGdiMasterDataIndexShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGdiMasterDataIndexShouldNotBeFound("id.greaterThan=" + id);

        defaultGdiMasterDataIndexShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGdiMasterDataIndexShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName equals to DEFAULT_ENTITY_NAME
        defaultGdiMasterDataIndexShouldBeFound("entityName.equals=" + DEFAULT_ENTITY_NAME);

        // Get all the gdiMasterDataIndexList where entityName equals to UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName not equals to DEFAULT_ENTITY_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.notEquals=" + DEFAULT_ENTITY_NAME);

        // Get all the gdiMasterDataIndexList where entityName not equals to UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldBeFound("entityName.notEquals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName in DEFAULT_ENTITY_NAME or UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldBeFound("entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME);

        // Get all the gdiMasterDataIndexList where entityName equals to UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.in=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName is not null
        defaultGdiMasterDataIndexShouldBeFound("entityName.specified=true");

        // Get all the gdiMasterDataIndexList where entityName is null
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName contains DEFAULT_ENTITY_NAME
        defaultGdiMasterDataIndexShouldBeFound("entityName.contains=" + DEFAULT_ENTITY_NAME);

        // Get all the gdiMasterDataIndexList where entityName contains UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where entityName does not contain DEFAULT_ENTITY_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);

        // Get all the gdiMasterDataIndexList where entityName does not contain UPDATED_ENTITY_NAME
        defaultGdiMasterDataIndexShouldBeFound("entityName.doesNotContain=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName equals to DEFAULT_DATABASE_NAME
        defaultGdiMasterDataIndexShouldBeFound("databaseName.equals=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiMasterDataIndexList where databaseName equals to UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.equals=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName not equals to DEFAULT_DATABASE_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.notEquals=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiMasterDataIndexList where databaseName not equals to UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldBeFound("databaseName.notEquals=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameIsInShouldWork() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName in DEFAULT_DATABASE_NAME or UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldBeFound("databaseName.in=" + DEFAULT_DATABASE_NAME + "," + UPDATED_DATABASE_NAME);

        // Get all the gdiMasterDataIndexList where databaseName equals to UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.in=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName is not null
        defaultGdiMasterDataIndexShouldBeFound("databaseName.specified=true");

        // Get all the gdiMasterDataIndexList where databaseName is null
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName contains DEFAULT_DATABASE_NAME
        defaultGdiMasterDataIndexShouldBeFound("databaseName.contains=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiMasterDataIndexList where databaseName contains UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.contains=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDatabaseNameNotContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where databaseName does not contain DEFAULT_DATABASE_NAME
        defaultGdiMasterDataIndexShouldNotBeFound("databaseName.doesNotContain=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiMasterDataIndexList where databaseName does not contain UPDATED_DATABASE_NAME
        defaultGdiMasterDataIndexShouldBeFound("databaseName.doesNotContain=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath equals to DEFAULT_DATA_PATH
        defaultGdiMasterDataIndexShouldBeFound("dataPath.equals=" + DEFAULT_DATA_PATH);

        // Get all the gdiMasterDataIndexList where dataPath equals to UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.equals=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath not equals to DEFAULT_DATA_PATH
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.notEquals=" + DEFAULT_DATA_PATH);

        // Get all the gdiMasterDataIndexList where dataPath not equals to UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldBeFound("dataPath.notEquals=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathIsInShouldWork() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath in DEFAULT_DATA_PATH or UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldBeFound("dataPath.in=" + DEFAULT_DATA_PATH + "," + UPDATED_DATA_PATH);

        // Get all the gdiMasterDataIndexList where dataPath equals to UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.in=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath is not null
        defaultGdiMasterDataIndexShouldBeFound("dataPath.specified=true");

        // Get all the gdiMasterDataIndexList where dataPath is null
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath contains DEFAULT_DATA_PATH
        defaultGdiMasterDataIndexShouldBeFound("dataPath.contains=" + DEFAULT_DATA_PATH);

        // Get all the gdiMasterDataIndexList where dataPath contains UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.contains=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiMasterDataIndicesByDataPathNotContainsSomething() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        // Get all the gdiMasterDataIndexList where dataPath does not contain DEFAULT_DATA_PATH
        defaultGdiMasterDataIndexShouldNotBeFound("dataPath.doesNotContain=" + DEFAULT_DATA_PATH);

        // Get all the gdiMasterDataIndexList where dataPath does not contain UPDATED_DATA_PATH
        defaultGdiMasterDataIndexShouldBeFound("dataPath.doesNotContain=" + UPDATED_DATA_PATH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGdiMasterDataIndexShouldBeFound(String filter) throws Exception {
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiMasterDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].businessDescription").value(hasItem(DEFAULT_BUSINESS_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));

        // Check, that the count call also returns 1
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGdiMasterDataIndexShouldNotBeFound(String filter) throws Exception {
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGdiMasterDataIndex() throws Exception {
        // Get the gdiMasterDataIndex
        restGdiMasterDataIndexMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGdiMasterDataIndex() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();

        // Update the gdiMasterDataIndex
        GdiMasterDataIndex updatedGdiMasterDataIndex = gdiMasterDataIndexRepository.findById(gdiMasterDataIndex.getId()).get();
        // Disconnect from session so that the updates on updatedGdiMasterDataIndex are not directly saved in db
        em.detach(updatedGdiMasterDataIndex);
        updatedGdiMasterDataIndex
            .entityName(UPDATED_ENTITY_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .businessDescription(UPDATED_BUSINESS_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(updatedGdiMasterDataIndex);

        restGdiMasterDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gdiMasterDataIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isOk());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiMasterDataIndex testGdiMasterDataIndex = gdiMasterDataIndexList.get(gdiMasterDataIndexList.size() - 1);
        assertThat(testGdiMasterDataIndex.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testGdiMasterDataIndex.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testGdiMasterDataIndex.getBusinessDescription()).isEqualTo(UPDATED_BUSINESS_DESCRIPTION);
        assertThat(testGdiMasterDataIndex.getDataPath()).isEqualTo(UPDATED_DATA_PATH);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository).save(testGdiMasterDataIndex);
    }

    @Test
    @Transactional
    void putNonExistingGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gdiMasterDataIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void putWithIdMismatchGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void partialUpdateGdiMasterDataIndexWithPatch() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();

        // Update the gdiMasterDataIndex using partial update
        GdiMasterDataIndex partialUpdatedGdiMasterDataIndex = new GdiMasterDataIndex();
        partialUpdatedGdiMasterDataIndex.setId(gdiMasterDataIndex.getId());

        partialUpdatedGdiMasterDataIndex.entityName(UPDATED_ENTITY_NAME).businessDescription(UPDATED_BUSINESS_DESCRIPTION);

        restGdiMasterDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGdiMasterDataIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGdiMasterDataIndex))
            )
            .andExpect(status().isOk());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiMasterDataIndex testGdiMasterDataIndex = gdiMasterDataIndexList.get(gdiMasterDataIndexList.size() - 1);
        assertThat(testGdiMasterDataIndex.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testGdiMasterDataIndex.getDatabaseName()).isEqualTo(DEFAULT_DATABASE_NAME);
        assertThat(testGdiMasterDataIndex.getBusinessDescription()).isEqualTo(UPDATED_BUSINESS_DESCRIPTION);
        assertThat(testGdiMasterDataIndex.getDataPath()).isEqualTo(DEFAULT_DATA_PATH);
    }

    @Test
    @Transactional
    void fullUpdateGdiMasterDataIndexWithPatch() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();

        // Update the gdiMasterDataIndex using partial update
        GdiMasterDataIndex partialUpdatedGdiMasterDataIndex = new GdiMasterDataIndex();
        partialUpdatedGdiMasterDataIndex.setId(gdiMasterDataIndex.getId());

        partialUpdatedGdiMasterDataIndex
            .entityName(UPDATED_ENTITY_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .businessDescription(UPDATED_BUSINESS_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);

        restGdiMasterDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGdiMasterDataIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGdiMasterDataIndex))
            )
            .andExpect(status().isOk());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiMasterDataIndex testGdiMasterDataIndex = gdiMasterDataIndexList.get(gdiMasterDataIndexList.size() - 1);
        assertThat(testGdiMasterDataIndex.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testGdiMasterDataIndex.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testGdiMasterDataIndex.getBusinessDescription()).isEqualTo(UPDATED_BUSINESS_DESCRIPTION);
        assertThat(testGdiMasterDataIndex.getDataPath()).isEqualTo(UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gdiMasterDataIndexDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGdiMasterDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiMasterDataIndexRepository.findAll().size();
        gdiMasterDataIndex.setId(count.incrementAndGet());

        // Create the GdiMasterDataIndex
        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = gdiMasterDataIndexMapper.toDto(gdiMasterDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiMasterDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiMasterDataIndexDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GdiMasterDataIndex in the database
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(0)).save(gdiMasterDataIndex);
    }

    @Test
    @Transactional
    void deleteGdiMasterDataIndex() throws Exception {
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);

        int databaseSizeBeforeDelete = gdiMasterDataIndexRepository.findAll().size();

        // Delete the gdiMasterDataIndex
        restGdiMasterDataIndexMockMvc
            .perform(delete(ENTITY_API_URL_ID, gdiMasterDataIndex.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GdiMasterDataIndex> gdiMasterDataIndexList = gdiMasterDataIndexRepository.findAll();
        assertThat(gdiMasterDataIndexList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GdiMasterDataIndex in Elasticsearch
        verify(mockGdiMasterDataIndexSearchRepository, times(1)).deleteById(gdiMasterDataIndex.getId());
    }

    @Test
    @Transactional
    void searchGdiMasterDataIndex() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        gdiMasterDataIndexRepository.saveAndFlush(gdiMasterDataIndex);
        when(mockGdiMasterDataIndexSearchRepository.search("id:" + gdiMasterDataIndex.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gdiMasterDataIndex), PageRequest.of(0, 1), 1));

        // Search the gdiMasterDataIndex
        restGdiMasterDataIndexMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + gdiMasterDataIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiMasterDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].businessDescription").value(hasItem(DEFAULT_BUSINESS_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));
    }
}
