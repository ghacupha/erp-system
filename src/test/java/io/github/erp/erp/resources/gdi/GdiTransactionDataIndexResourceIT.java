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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.DatasetBehaviorTypes;
import io.github.erp.domain.enumeration.UpdateFrequencyTypes;
import io.github.erp.erp.resources.BusinessDocumentResourceIT;
import io.github.erp.erp.resources.PlaceholderResourceIT;
import io.github.erp.repository.GdiTransactionDataIndexRepository;
import io.github.erp.repository.search.GdiTransactionDataIndexSearchRepository;
import io.github.erp.service.GdiTransactionDataIndexService;
import io.github.erp.service.dto.GdiTransactionDataIndexDTO;
import io.github.erp.service.mapper.GdiTransactionDataIndexMapper;
import io.github.erp.web.rest.GdiTransactionDataIndexResource;
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
 * Integration tests for the {@link GdiTransactionDataIndexResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class GdiTransactionDataIndexResourceIT {

    private static final String DEFAULT_DATASET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATASET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATABASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATABASE_NAME = "BBBBBBBBBB";

    private static final UpdateFrequencyTypes DEFAULT_UPDATE_FREQUENCY = UpdateFrequencyTypes.DAILY;
    private static final UpdateFrequencyTypes UPDATED_UPDATE_FREQUENCY = UpdateFrequencyTypes.INTRA_DAY;

    private static final DatasetBehaviorTypes DEFAULT_DATASET_BEHAVIOR = DatasetBehaviorTypes.INSERT_AND_UPDATE;
    private static final DatasetBehaviorTypes UPDATED_DATASET_BEHAVIOR = DatasetBehaviorTypes.INSERT;

    private static final Integer DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST = 1;
    private static final Integer UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST = 2;
    private static final Integer SMALLER_MINIMUM_DATA_ROWS_PER_REQUEST = 1 - 1;

    private static final Integer DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST = 1;
    private static final Integer UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST = 2;
    private static final Integer SMALLER_MAXIMUM_DATA_ROWS_PER_REQUEST = 1 - 1;

    private static final String DEFAULT_DATASET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DATASET_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DATA_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/gdi-transaction-data-indices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/gdi-transaction-data-indices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository;

    @Mock
    private GdiTransactionDataIndexRepository gdiTransactionDataIndexRepositoryMock;

    @Autowired
    private GdiTransactionDataIndexMapper gdiTransactionDataIndexMapper;

    @Mock
    private GdiTransactionDataIndexService gdiTransactionDataIndexServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.GdiTransactionDataIndexSearchRepositoryMockConfiguration
     */
    @Autowired
    private GdiTransactionDataIndexSearchRepository mockGdiTransactionDataIndexSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGdiTransactionDataIndexMockMvc;

    private GdiTransactionDataIndex gdiTransactionDataIndex;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GdiTransactionDataIndex createEntity(EntityManager em) {
        GdiTransactionDataIndex gdiTransactionDataIndex = new GdiTransactionDataIndex()
            .datasetName(DEFAULT_DATASET_NAME)
            .databaseName(DEFAULT_DATABASE_NAME)
            .updateFrequency(DEFAULT_UPDATE_FREQUENCY)
            .datasetBehavior(DEFAULT_DATASET_BEHAVIOR)
            .minimumDataRowsPerRequest(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST)
            .maximumDataRowsPerRequest(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST)
            .datasetDescription(DEFAULT_DATASET_DESCRIPTION)
            .dataPath(DEFAULT_DATA_PATH);
        return gdiTransactionDataIndex;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GdiTransactionDataIndex createUpdatedEntity(EntityManager em) {
        GdiTransactionDataIndex gdiTransactionDataIndex = new GdiTransactionDataIndex()
            .datasetName(UPDATED_DATASET_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .updateFrequency(UPDATED_UPDATE_FREQUENCY)
            .datasetBehavior(UPDATED_DATASET_BEHAVIOR)
            .minimumDataRowsPerRequest(UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST)
            .maximumDataRowsPerRequest(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST)
            .datasetDescription(UPDATED_DATASET_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);
        return gdiTransactionDataIndex;
    }

    @BeforeEach
    public void initTest() {
        gdiTransactionDataIndex = createEntity(em);
    }

    @Test
    @Transactional
    void createGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeCreate = gdiTransactionDataIndexRepository.findAll().size();
        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);
        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeCreate + 1);
        GdiTransactionDataIndex testGdiTransactionDataIndex = gdiTransactionDataIndexList.get(gdiTransactionDataIndexList.size() - 1);
        assertThat(testGdiTransactionDataIndex.getDatasetName()).isEqualTo(DEFAULT_DATASET_NAME);
        assertThat(testGdiTransactionDataIndex.getDatabaseName()).isEqualTo(DEFAULT_DATABASE_NAME);
        assertThat(testGdiTransactionDataIndex.getUpdateFrequency()).isEqualTo(DEFAULT_UPDATE_FREQUENCY);
        assertThat(testGdiTransactionDataIndex.getDatasetBehavior()).isEqualTo(DEFAULT_DATASET_BEHAVIOR);
        assertThat(testGdiTransactionDataIndex.getMinimumDataRowsPerRequest()).isEqualTo(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getMaximumDataRowsPerRequest()).isEqualTo(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getDatasetDescription()).isEqualTo(DEFAULT_DATASET_DESCRIPTION);
        assertThat(testGdiTransactionDataIndex.getDataPath()).isEqualTo(DEFAULT_DATA_PATH);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(1)).save(testGdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void createGdiTransactionDataIndexWithExistingId() throws Exception {
        // Create the GdiTransactionDataIndex with an existing ID
        gdiTransactionDataIndex.setId(1L);
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        int databaseSizeBeforeCreate = gdiTransactionDataIndexRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeCreate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void checkDatasetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiTransactionDataIndexRepository.findAll().size();
        // set the field null
        gdiTransactionDataIndex.setDatasetName(null);

        // Create the GdiTransactionDataIndex, which fails.
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatabaseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiTransactionDataIndexRepository.findAll().size();
        // set the field null
        gdiTransactionDataIndex.setDatabaseName(null);

        // Create the GdiTransactionDataIndex, which fails.
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdateFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiTransactionDataIndexRepository.findAll().size();
        // set the field null
        gdiTransactionDataIndex.setUpdateFrequency(null);

        // Create the GdiTransactionDataIndex, which fails.
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatasetBehaviorIsRequired() throws Exception {
        int databaseSizeBeforeTest = gdiTransactionDataIndexRepository.findAll().size();
        // set the field null
        gdiTransactionDataIndex.setDatasetBehavior(null);

        // Create the GdiTransactionDataIndex, which fails.
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        restGdiTransactionDataIndexMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndices() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiTransactionDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].datasetName").value(hasItem(DEFAULT_DATASET_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].updateFrequency").value(hasItem(DEFAULT_UPDATE_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].datasetBehavior").value(hasItem(DEFAULT_DATASET_BEHAVIOR.toString())))
            .andExpect(jsonPath("$.[*].minimumDataRowsPerRequest").value(hasItem(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].maximumDataRowsPerRequest").value(hasItem(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].datasetDescription").value(hasItem(DEFAULT_DATASET_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGdiTransactionDataIndicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(gdiTransactionDataIndexServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGdiTransactionDataIndexMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gdiTransactionDataIndexServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGdiTransactionDataIndicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gdiTransactionDataIndexServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGdiTransactionDataIndexMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gdiTransactionDataIndexServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGdiTransactionDataIndex() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get the gdiTransactionDataIndex
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL_ID, gdiTransactionDataIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gdiTransactionDataIndex.getId().intValue()))
            .andExpect(jsonPath("$.datasetName").value(DEFAULT_DATASET_NAME))
            .andExpect(jsonPath("$.databaseName").value(DEFAULT_DATABASE_NAME))
            .andExpect(jsonPath("$.updateFrequency").value(DEFAULT_UPDATE_FREQUENCY.toString()))
            .andExpect(jsonPath("$.datasetBehavior").value(DEFAULT_DATASET_BEHAVIOR.toString()))
            .andExpect(jsonPath("$.minimumDataRowsPerRequest").value(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST))
            .andExpect(jsonPath("$.maximumDataRowsPerRequest").value(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST))
            .andExpect(jsonPath("$.datasetDescription").value(DEFAULT_DATASET_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dataPath").value(DEFAULT_DATA_PATH));
    }

    @Test
    @Transactional
    void getGdiTransactionDataIndicesByIdFiltering() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        Long id = gdiTransactionDataIndex.getId();

        defaultGdiTransactionDataIndexShouldBeFound("id.equals=" + id);
        defaultGdiTransactionDataIndexShouldNotBeFound("id.notEquals=" + id);

        defaultGdiTransactionDataIndexShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGdiTransactionDataIndexShouldNotBeFound("id.greaterThan=" + id);

        defaultGdiTransactionDataIndexShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGdiTransactionDataIndexShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName equals to DEFAULT_DATASET_NAME
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.equals=" + DEFAULT_DATASET_NAME);

        // Get all the gdiTransactionDataIndexList where datasetName equals to UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.equals=" + UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName not equals to DEFAULT_DATASET_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.notEquals=" + DEFAULT_DATASET_NAME);

        // Get all the gdiTransactionDataIndexList where datasetName not equals to UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.notEquals=" + UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName in DEFAULT_DATASET_NAME or UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.in=" + DEFAULT_DATASET_NAME + "," + UPDATED_DATASET_NAME);

        // Get all the gdiTransactionDataIndexList where datasetName equals to UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.in=" + UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName is not null
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.specified=true");

        // Get all the gdiTransactionDataIndexList where datasetName is null
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName contains DEFAULT_DATASET_NAME
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.contains=" + DEFAULT_DATASET_NAME);

        // Get all the gdiTransactionDataIndexList where datasetName contains UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.contains=" + UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetNameNotContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetName does not contain DEFAULT_DATASET_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetName.doesNotContain=" + DEFAULT_DATASET_NAME);

        // Get all the gdiTransactionDataIndexList where datasetName does not contain UPDATED_DATASET_NAME
        defaultGdiTransactionDataIndexShouldBeFound("datasetName.doesNotContain=" + UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName equals to DEFAULT_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.equals=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiTransactionDataIndexList where databaseName equals to UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.equals=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName not equals to DEFAULT_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.notEquals=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiTransactionDataIndexList where databaseName not equals to UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.notEquals=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName in DEFAULT_DATABASE_NAME or UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.in=" + DEFAULT_DATABASE_NAME + "," + UPDATED_DATABASE_NAME);

        // Get all the gdiTransactionDataIndexList where databaseName equals to UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.in=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName is not null
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.specified=true");

        // Get all the gdiTransactionDataIndexList where databaseName is null
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName contains DEFAULT_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.contains=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiTransactionDataIndexList where databaseName contains UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.contains=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatabaseNameNotContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where databaseName does not contain DEFAULT_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldNotBeFound("databaseName.doesNotContain=" + DEFAULT_DATABASE_NAME);

        // Get all the gdiTransactionDataIndexList where databaseName does not contain UPDATED_DATABASE_NAME
        defaultGdiTransactionDataIndexShouldBeFound("databaseName.doesNotContain=" + UPDATED_DATABASE_NAME);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByUpdateFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where updateFrequency equals to DEFAULT_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldBeFound("updateFrequency.equals=" + DEFAULT_UPDATE_FREQUENCY);

        // Get all the gdiTransactionDataIndexList where updateFrequency equals to UPDATED_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldNotBeFound("updateFrequency.equals=" + UPDATED_UPDATE_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByUpdateFrequencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where updateFrequency not equals to DEFAULT_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldNotBeFound("updateFrequency.notEquals=" + DEFAULT_UPDATE_FREQUENCY);

        // Get all the gdiTransactionDataIndexList where updateFrequency not equals to UPDATED_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldBeFound("updateFrequency.notEquals=" + UPDATED_UPDATE_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByUpdateFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where updateFrequency in DEFAULT_UPDATE_FREQUENCY or UPDATED_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldBeFound("updateFrequency.in=" + DEFAULT_UPDATE_FREQUENCY + "," + UPDATED_UPDATE_FREQUENCY);

        // Get all the gdiTransactionDataIndexList where updateFrequency equals to UPDATED_UPDATE_FREQUENCY
        defaultGdiTransactionDataIndexShouldNotBeFound("updateFrequency.in=" + UPDATED_UPDATE_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByUpdateFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where updateFrequency is not null
        defaultGdiTransactionDataIndexShouldBeFound("updateFrequency.specified=true");

        // Get all the gdiTransactionDataIndexList where updateFrequency is null
        defaultGdiTransactionDataIndexShouldNotBeFound("updateFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetBehaviorIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetBehavior equals to DEFAULT_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldBeFound("datasetBehavior.equals=" + DEFAULT_DATASET_BEHAVIOR);

        // Get all the gdiTransactionDataIndexList where datasetBehavior equals to UPDATED_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetBehavior.equals=" + UPDATED_DATASET_BEHAVIOR);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetBehaviorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetBehavior not equals to DEFAULT_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetBehavior.notEquals=" + DEFAULT_DATASET_BEHAVIOR);

        // Get all the gdiTransactionDataIndexList where datasetBehavior not equals to UPDATED_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldBeFound("datasetBehavior.notEquals=" + UPDATED_DATASET_BEHAVIOR);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetBehaviorIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetBehavior in DEFAULT_DATASET_BEHAVIOR or UPDATED_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldBeFound("datasetBehavior.in=" + DEFAULT_DATASET_BEHAVIOR + "," + UPDATED_DATASET_BEHAVIOR);

        // Get all the gdiTransactionDataIndexList where datasetBehavior equals to UPDATED_DATASET_BEHAVIOR
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetBehavior.in=" + UPDATED_DATASET_BEHAVIOR);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDatasetBehaviorIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where datasetBehavior is not null
        defaultGdiTransactionDataIndexShouldBeFound("datasetBehavior.specified=true");

        // Get all the gdiTransactionDataIndexList where datasetBehavior is null
        defaultGdiTransactionDataIndexShouldNotBeFound("datasetBehavior.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest equals to DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.equals=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest equals to UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.equals=" + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest not equals to DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.notEquals=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest not equals to UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.notEquals=" + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest in DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST or UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound(
            "minimumDataRowsPerRequest.in=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST + "," + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        );

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest equals to UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.in=" + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is not null
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.specified=true");

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is null
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is greater than or equal to DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound(
            "minimumDataRowsPerRequest.greaterThanOrEqual=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        );

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is greater than or equal to UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound(
            "minimumDataRowsPerRequest.greaterThanOrEqual=" + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        );
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is less than or equal to DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.lessThanOrEqual=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is less than or equal to SMALLER_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound(
            "minimumDataRowsPerRequest.lessThanOrEqual=" + SMALLER_MINIMUM_DATA_ROWS_PER_REQUEST
        );
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is less than DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.lessThan=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is less than UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.lessThan=" + UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMinimumDataRowsPerRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is greater than DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("minimumDataRowsPerRequest.greaterThan=" + DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where minimumDataRowsPerRequest is greater than SMALLER_MINIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("minimumDataRowsPerRequest.greaterThan=" + SMALLER_MINIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest equals to DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.equals=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest equals to UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.equals=" + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest not equals to DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.notEquals=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest not equals to UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.notEquals=" + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest in DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST or UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound(
            "maximumDataRowsPerRequest.in=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST + "," + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        );

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest equals to UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.in=" + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is not null
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.specified=true");

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is null
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is greater than or equal to DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound(
            "maximumDataRowsPerRequest.greaterThanOrEqual=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        );

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is greater than or equal to UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound(
            "maximumDataRowsPerRequest.greaterThanOrEqual=" + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        );
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is less than or equal to DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.lessThanOrEqual=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is less than or equal to SMALLER_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound(
            "maximumDataRowsPerRequest.lessThanOrEqual=" + SMALLER_MAXIMUM_DATA_ROWS_PER_REQUEST
        );
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is less than DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.lessThan=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is less than UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.lessThan=" + UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMaximumDataRowsPerRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is greater than DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldNotBeFound("maximumDataRowsPerRequest.greaterThan=" + DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST);

        // Get all the gdiTransactionDataIndexList where maximumDataRowsPerRequest is greater than SMALLER_MAXIMUM_DATA_ROWS_PER_REQUEST
        defaultGdiTransactionDataIndexShouldBeFound("maximumDataRowsPerRequest.greaterThan=" + SMALLER_MAXIMUM_DATA_ROWS_PER_REQUEST);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath equals to DEFAULT_DATA_PATH
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.equals=" + DEFAULT_DATA_PATH);

        // Get all the gdiTransactionDataIndexList where dataPath equals to UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.equals=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath not equals to DEFAULT_DATA_PATH
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.notEquals=" + DEFAULT_DATA_PATH);

        // Get all the gdiTransactionDataIndexList where dataPath not equals to UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.notEquals=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathIsInShouldWork() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath in DEFAULT_DATA_PATH or UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.in=" + DEFAULT_DATA_PATH + "," + UPDATED_DATA_PATH);

        // Get all the gdiTransactionDataIndexList where dataPath equals to UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.in=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath is not null
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.specified=true");

        // Get all the gdiTransactionDataIndexList where dataPath is null
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.specified=false");
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath contains DEFAULT_DATA_PATH
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.contains=" + DEFAULT_DATA_PATH);

        // Get all the gdiTransactionDataIndexList where dataPath contains UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.contains=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataPathNotContainsSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        // Get all the gdiTransactionDataIndexList where dataPath does not contain DEFAULT_DATA_PATH
        defaultGdiTransactionDataIndexShouldNotBeFound("dataPath.doesNotContain=" + DEFAULT_DATA_PATH);

        // Get all the gdiTransactionDataIndexList where dataPath does not contain UPDATED_DATA_PATH
        defaultGdiTransactionDataIndexShouldBeFound("dataPath.doesNotContain=" + UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByMasterDataItemIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        GdiMasterDataIndex masterDataItem;
        if (TestUtil.findAll(em, GdiMasterDataIndex.class).isEmpty()) {
            masterDataItem = GdiMasterDataIndexResourceIT.createEntity(em);
            em.persist(masterDataItem);
            em.flush();
        } else {
            masterDataItem = TestUtil.findAll(em, GdiMasterDataIndex.class).get(0);
        }
        em.persist(masterDataItem);
        em.flush();
        gdiTransactionDataIndex.addMasterDataItem(masterDataItem);
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        Long masterDataItemId = masterDataItem.getId();

        // Get all the gdiTransactionDataIndexList where masterDataItem equals to masterDataItemId
        defaultGdiTransactionDataIndexShouldBeFound("masterDataItemId.equals=" + masterDataItemId);

        // Get all the gdiTransactionDataIndexList where masterDataItem equals to (masterDataItemId + 1)
        defaultGdiTransactionDataIndexShouldNotBeFound("masterDataItemId.equals=" + (masterDataItemId + 1));
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByBusinessTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        BusinessTeam businessTeam;
        if (TestUtil.findAll(em, BusinessTeam.class).isEmpty()) {
            businessTeam = BusinessTeamResourceIT.createEntity(em);
            em.persist(businessTeam);
            em.flush();
        } else {
            businessTeam = TestUtil.findAll(em, BusinessTeam.class).get(0);
        }
        em.persist(businessTeam);
        em.flush();
        gdiTransactionDataIndex.setBusinessTeam(businessTeam);
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        Long businessTeamId = businessTeam.getId();

        // Get all the gdiTransactionDataIndexList where businessTeam equals to businessTeamId
        defaultGdiTransactionDataIndexShouldBeFound("businessTeamId.equals=" + businessTeamId);

        // Get all the gdiTransactionDataIndexList where businessTeam equals to (businessTeamId + 1)
        defaultGdiTransactionDataIndexShouldNotBeFound("businessTeamId.equals=" + (businessTeamId + 1));
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByDataSetTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        BusinessDocument dataSetTemplate;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            dataSetTemplate = BusinessDocumentResourceIT.createEntity(em);
            em.persist(dataSetTemplate);
            em.flush();
        } else {
            dataSetTemplate = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(dataSetTemplate);
        em.flush();
        gdiTransactionDataIndex.setDataSetTemplate(dataSetTemplate);
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        Long dataSetTemplateId = dataSetTemplate.getId();

        // Get all the gdiTransactionDataIndexList where dataSetTemplate equals to dataSetTemplateId
        defaultGdiTransactionDataIndexShouldBeFound("dataSetTemplateId.equals=" + dataSetTemplateId);

        // Get all the gdiTransactionDataIndexList where dataSetTemplate equals to (dataSetTemplateId + 1)
        defaultGdiTransactionDataIndexShouldNotBeFound("dataSetTemplateId.equals=" + (dataSetTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllGdiTransactionDataIndicesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
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
        gdiTransactionDataIndex.addPlaceholder(placeholder);
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        Long placeholderId = placeholder.getId();

        // Get all the gdiTransactionDataIndexList where placeholder equals to placeholderId
        defaultGdiTransactionDataIndexShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the gdiTransactionDataIndexList where placeholder equals to (placeholderId + 1)
        defaultGdiTransactionDataIndexShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGdiTransactionDataIndexShouldBeFound(String filter) throws Exception {
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiTransactionDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].datasetName").value(hasItem(DEFAULT_DATASET_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].updateFrequency").value(hasItem(DEFAULT_UPDATE_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].datasetBehavior").value(hasItem(DEFAULT_DATASET_BEHAVIOR.toString())))
            .andExpect(jsonPath("$.[*].minimumDataRowsPerRequest").value(hasItem(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].maximumDataRowsPerRequest").value(hasItem(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].datasetDescription").value(hasItem(DEFAULT_DATASET_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));

        // Check, that the count call also returns 1
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGdiTransactionDataIndexShouldNotBeFound(String filter) throws Exception {
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGdiTransactionDataIndex() throws Exception {
        // Get the gdiTransactionDataIndex
        restGdiTransactionDataIndexMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGdiTransactionDataIndex() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();

        // Update the gdiTransactionDataIndex
        GdiTransactionDataIndex updatedGdiTransactionDataIndex = gdiTransactionDataIndexRepository
            .findById(gdiTransactionDataIndex.getId())
            .get();
        // Disconnect from session so that the updates on updatedGdiTransactionDataIndex are not directly saved in db
        em.detach(updatedGdiTransactionDataIndex);
        updatedGdiTransactionDataIndex
            .datasetName(UPDATED_DATASET_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .updateFrequency(UPDATED_UPDATE_FREQUENCY)
            .datasetBehavior(UPDATED_DATASET_BEHAVIOR)
            .minimumDataRowsPerRequest(UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST)
            .maximumDataRowsPerRequest(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST)
            .datasetDescription(UPDATED_DATASET_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(updatedGdiTransactionDataIndex);

        restGdiTransactionDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gdiTransactionDataIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isOk());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiTransactionDataIndex testGdiTransactionDataIndex = gdiTransactionDataIndexList.get(gdiTransactionDataIndexList.size() - 1);
        assertThat(testGdiTransactionDataIndex.getDatasetName()).isEqualTo(UPDATED_DATASET_NAME);
        assertThat(testGdiTransactionDataIndex.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testGdiTransactionDataIndex.getUpdateFrequency()).isEqualTo(UPDATED_UPDATE_FREQUENCY);
        assertThat(testGdiTransactionDataIndex.getDatasetBehavior()).isEqualTo(UPDATED_DATASET_BEHAVIOR);
        assertThat(testGdiTransactionDataIndex.getMinimumDataRowsPerRequest()).isEqualTo(UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getMaximumDataRowsPerRequest()).isEqualTo(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getDatasetDescription()).isEqualTo(UPDATED_DATASET_DESCRIPTION);
        assertThat(testGdiTransactionDataIndex.getDataPath()).isEqualTo(UPDATED_DATA_PATH);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository).save(testGdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void putNonExistingGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gdiTransactionDataIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void putWithIdMismatchGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void partialUpdateGdiTransactionDataIndexWithPatch() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();

        // Update the gdiTransactionDataIndex using partial update
        GdiTransactionDataIndex partialUpdatedGdiTransactionDataIndex = new GdiTransactionDataIndex();
        partialUpdatedGdiTransactionDataIndex.setId(gdiTransactionDataIndex.getId());

        partialUpdatedGdiTransactionDataIndex
            .datasetName(UPDATED_DATASET_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .updateFrequency(UPDATED_UPDATE_FREQUENCY)
            .maximumDataRowsPerRequest(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST)
            .datasetDescription(UPDATED_DATASET_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);

        restGdiTransactionDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGdiTransactionDataIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGdiTransactionDataIndex))
            )
            .andExpect(status().isOk());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiTransactionDataIndex testGdiTransactionDataIndex = gdiTransactionDataIndexList.get(gdiTransactionDataIndexList.size() - 1);
        assertThat(testGdiTransactionDataIndex.getDatasetName()).isEqualTo(UPDATED_DATASET_NAME);
        assertThat(testGdiTransactionDataIndex.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testGdiTransactionDataIndex.getUpdateFrequency()).isEqualTo(UPDATED_UPDATE_FREQUENCY);
        assertThat(testGdiTransactionDataIndex.getDatasetBehavior()).isEqualTo(DEFAULT_DATASET_BEHAVIOR);
        assertThat(testGdiTransactionDataIndex.getMinimumDataRowsPerRequest()).isEqualTo(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getMaximumDataRowsPerRequest()).isEqualTo(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getDatasetDescription()).isEqualTo(UPDATED_DATASET_DESCRIPTION);
        assertThat(testGdiTransactionDataIndex.getDataPath()).isEqualTo(UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void fullUpdateGdiTransactionDataIndexWithPatch() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();

        // Update the gdiTransactionDataIndex using partial update
        GdiTransactionDataIndex partialUpdatedGdiTransactionDataIndex = new GdiTransactionDataIndex();
        partialUpdatedGdiTransactionDataIndex.setId(gdiTransactionDataIndex.getId());

        partialUpdatedGdiTransactionDataIndex
            .datasetName(UPDATED_DATASET_NAME)
            .databaseName(UPDATED_DATABASE_NAME)
            .updateFrequency(UPDATED_UPDATE_FREQUENCY)
            .datasetBehavior(UPDATED_DATASET_BEHAVIOR)
            .minimumDataRowsPerRequest(UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST)
            .maximumDataRowsPerRequest(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST)
            .datasetDescription(UPDATED_DATASET_DESCRIPTION)
            .dataPath(UPDATED_DATA_PATH);

        restGdiTransactionDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGdiTransactionDataIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGdiTransactionDataIndex))
            )
            .andExpect(status().isOk());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);
        GdiTransactionDataIndex testGdiTransactionDataIndex = gdiTransactionDataIndexList.get(gdiTransactionDataIndexList.size() - 1);
        assertThat(testGdiTransactionDataIndex.getDatasetName()).isEqualTo(UPDATED_DATASET_NAME);
        assertThat(testGdiTransactionDataIndex.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testGdiTransactionDataIndex.getUpdateFrequency()).isEqualTo(UPDATED_UPDATE_FREQUENCY);
        assertThat(testGdiTransactionDataIndex.getDatasetBehavior()).isEqualTo(UPDATED_DATASET_BEHAVIOR);
        assertThat(testGdiTransactionDataIndex.getMinimumDataRowsPerRequest()).isEqualTo(UPDATED_MINIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getMaximumDataRowsPerRequest()).isEqualTo(UPDATED_MAXIMUM_DATA_ROWS_PER_REQUEST);
        assertThat(testGdiTransactionDataIndex.getDatasetDescription()).isEqualTo(UPDATED_DATASET_DESCRIPTION);
        assertThat(testGdiTransactionDataIndex.getDataPath()).isEqualTo(UPDATED_DATA_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gdiTransactionDataIndexDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGdiTransactionDataIndex() throws Exception {
        int databaseSizeBeforeUpdate = gdiTransactionDataIndexRepository.findAll().size();
        gdiTransactionDataIndex.setId(count.incrementAndGet());

        // Create the GdiTransactionDataIndex
        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGdiTransactionDataIndexMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gdiTransactionDataIndexDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GdiTransactionDataIndex in the database
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(0)).save(gdiTransactionDataIndex);
    }

    @Test
    @Transactional
    void deleteGdiTransactionDataIndex() throws Exception {
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);

        int databaseSizeBeforeDelete = gdiTransactionDataIndexRepository.findAll().size();

        // Delete the gdiTransactionDataIndex
        restGdiTransactionDataIndexMockMvc
            .perform(delete(ENTITY_API_URL_ID, gdiTransactionDataIndex.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GdiTransactionDataIndex> gdiTransactionDataIndexList = gdiTransactionDataIndexRepository.findAll();
        assertThat(gdiTransactionDataIndexList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GdiTransactionDataIndex in Elasticsearch
        verify(mockGdiTransactionDataIndexSearchRepository, times(1)).deleteById(gdiTransactionDataIndex.getId());
    }

    @Test
    @Transactional
    void searchGdiTransactionDataIndex() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        gdiTransactionDataIndexRepository.saveAndFlush(gdiTransactionDataIndex);
        when(mockGdiTransactionDataIndexSearchRepository.search("id:" + gdiTransactionDataIndex.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gdiTransactionDataIndex), PageRequest.of(0, 1), 1));

        // Search the gdiTransactionDataIndex
        restGdiTransactionDataIndexMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + gdiTransactionDataIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gdiTransactionDataIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].datasetName").value(hasItem(DEFAULT_DATASET_NAME)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].updateFrequency").value(hasItem(DEFAULT_UPDATE_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].datasetBehavior").value(hasItem(DEFAULT_DATASET_BEHAVIOR.toString())))
            .andExpect(jsonPath("$.[*].minimumDataRowsPerRequest").value(hasItem(DEFAULT_MINIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].maximumDataRowsPerRequest").value(hasItem(DEFAULT_MAXIMUM_DATA_ROWS_PER_REQUEST)))
            .andExpect(jsonPath("$.[*].datasetDescription").value(hasItem(DEFAULT_DATASET_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dataPath").value(hasItem(DEFAULT_DATA_PATH)));
    }
}
