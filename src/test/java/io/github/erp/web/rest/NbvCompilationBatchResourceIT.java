package io.github.erp.web.rest;

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
import io.github.erp.domain.NbvCompilationBatch;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
import io.github.erp.repository.NbvCompilationBatchRepository;
import io.github.erp.repository.search.NbvCompilationBatchSearchRepository;
import io.github.erp.service.criteria.NbvCompilationBatchCriteria;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.service.mapper.NbvCompilationBatchMapper;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link NbvCompilationBatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NbvCompilationBatchResourceIT {

    private static final Integer DEFAULT_START_INDEX = 1;
    private static final Integer UPDATED_START_INDEX = 2;
    private static final Integer SMALLER_START_INDEX = 1 - 1;

    private static final Integer DEFAULT_END_INDEX = 1;
    private static final Integer UPDATED_END_INDEX = 2;
    private static final Integer SMALLER_END_INDEX = 1 - 1;

    private static final CompilationBatchStatusTypes DEFAULT_COMPILATION_BATCH_STATUS = CompilationBatchStatusTypes.CREATED;
    private static final CompilationBatchStatusTypes UPDATED_COMPILATION_BATCH_STATUS = CompilationBatchStatusTypes.RUNNING;

    private static final UUID DEFAULT_COMPILATION_BATCH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_BATCH_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_COMPILATION_JOBIDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_JOBIDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();

    private static final Integer DEFAULT_BATCH_SIZE = 1;
    private static final Integer UPDATED_BATCH_SIZE = 2;
    private static final Integer SMALLER_BATCH_SIZE = 1 - 1;

    private static final Integer DEFAULT_PROCESSED_ITEMS = 1;
    private static final Integer UPDATED_PROCESSED_ITEMS = 2;
    private static final Integer SMALLER_PROCESSED_ITEMS = 1 - 1;

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final Boolean DEFAULT_IS_LAST_BATCH = false;
    private static final Boolean UPDATED_IS_LAST_BATCH = true;

    private static final Duration DEFAULT_PROCESSING_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_PROCESSING_TIME = Duration.ofHours(12);
    private static final Duration SMALLER_PROCESSING_TIME = Duration.ofHours(5);

    private static final Integer DEFAULT_TOTAL_ITEMS = 1;
    private static final Integer UPDATED_TOTAL_ITEMS = 2;
    private static final Integer SMALLER_TOTAL_ITEMS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/nbv-compilation-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nbv-compilation-batches";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NbvCompilationBatchRepository nbvCompilationBatchRepository;

    @Autowired
    private NbvCompilationBatchMapper nbvCompilationBatchMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NbvCompilationBatchSearchRepositoryMockConfiguration
     */
    @Autowired
    private NbvCompilationBatchSearchRepository mockNbvCompilationBatchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNbvCompilationBatchMockMvc;

    private NbvCompilationBatch nbvCompilationBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationBatch createEntity(EntityManager em) {
        NbvCompilationBatch nbvCompilationBatch = new NbvCompilationBatch()
            .startIndex(DEFAULT_START_INDEX)
            .endIndex(DEFAULT_END_INDEX)
            .compilationBatchStatus(DEFAULT_COMPILATION_BATCH_STATUS)
            .compilationBatchIdentifier(DEFAULT_COMPILATION_BATCH_IDENTIFIER)
            .compilationJobidentifier(DEFAULT_COMPILATION_JOBIDENTIFIER)
            .depreciationPeriodIdentifier(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER)
            .fiscalMonthIdentifier(DEFAULT_FISCAL_MONTH_IDENTIFIER)
            .batchSize(DEFAULT_BATCH_SIZE)
            .processedItems(DEFAULT_PROCESSED_ITEMS)
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .isLastBatch(DEFAULT_IS_LAST_BATCH)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .totalItems(DEFAULT_TOTAL_ITEMS);
        return nbvCompilationBatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationBatch createUpdatedEntity(EntityManager em) {
        NbvCompilationBatch nbvCompilationBatch = new NbvCompilationBatch()
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .compilationBatchStatus(UPDATED_COMPILATION_BATCH_STATUS)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .compilationJobidentifier(UPDATED_COMPILATION_JOBIDENTIFIER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .batchSize(UPDATED_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .isLastBatch(UPDATED_IS_LAST_BATCH)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);
        return nbvCompilationBatch;
    }

    @BeforeEach
    public void initTest() {
        nbvCompilationBatch = createEntity(em);
    }

    @Test
    @Transactional
    void createNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeCreate = nbvCompilationBatchRepository.findAll().size();
        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);
        restNbvCompilationBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeCreate + 1);
        NbvCompilationBatch testNbvCompilationBatch = nbvCompilationBatchList.get(nbvCompilationBatchList.size() - 1);
        assertThat(testNbvCompilationBatch.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testNbvCompilationBatch.getEndIndex()).isEqualTo(DEFAULT_END_INDEX);
        assertThat(testNbvCompilationBatch.getCompilationBatchStatus()).isEqualTo(DEFAULT_COMPILATION_BATCH_STATUS);
        assertThat(testNbvCompilationBatch.getCompilationBatchIdentifier()).isEqualTo(DEFAULT_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getCompilationJobidentifier()).isEqualTo(DEFAULT_COMPILATION_JOBIDENTIFIER);
        assertThat(testNbvCompilationBatch.getDepreciationPeriodIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getFiscalMonthIdentifier()).isEqualTo(DEFAULT_FISCAL_MONTH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getBatchSize()).isEqualTo(DEFAULT_BATCH_SIZE);
        assertThat(testNbvCompilationBatch.getProcessedItems()).isEqualTo(DEFAULT_PROCESSED_ITEMS);
        assertThat(testNbvCompilationBatch.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testNbvCompilationBatch.getIsLastBatch()).isEqualTo(DEFAULT_IS_LAST_BATCH);
        assertThat(testNbvCompilationBatch.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);
        assertThat(testNbvCompilationBatch.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(1)).save(testNbvCompilationBatch);
    }

    @Test
    @Transactional
    void createNbvCompilationBatchWithExistingId() throws Exception {
        // Create the NbvCompilationBatch with an existing ID
        nbvCompilationBatch.setId(1L);
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        int databaseSizeBeforeCreate = nbvCompilationBatchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNbvCompilationBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeCreate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatches() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].compilationBatchStatus").value(hasItem(DEFAULT_COMPILATION_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobidentifier").value(hasItem(DEFAULT_COMPILATION_JOBIDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSize").value(hasItem(DEFAULT_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].isLastBatch").value(hasItem(DEFAULT_IS_LAST_BATCH.booleanValue())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));
    }

    @Test
    @Transactional
    void getNbvCompilationBatch() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get the nbvCompilationBatch
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, nbvCompilationBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nbvCompilationBatch.getId().intValue()))
            .andExpect(jsonPath("$.startIndex").value(DEFAULT_START_INDEX))
            .andExpect(jsonPath("$.endIndex").value(DEFAULT_END_INDEX))
            .andExpect(jsonPath("$.compilationBatchStatus").value(DEFAULT_COMPILATION_BATCH_STATUS.toString()))
            .andExpect(jsonPath("$.compilationBatchIdentifier").value(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationJobidentifier").value(DEFAULT_COMPILATION_JOBIDENTIFIER.toString()))
            .andExpect(jsonPath("$.depreciationPeriodIdentifier").value(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalMonthIdentifier").value(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.batchSize").value(DEFAULT_BATCH_SIZE))
            .andExpect(jsonPath("$.processedItems").value(DEFAULT_PROCESSED_ITEMS))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.isLastBatch").value(DEFAULT_IS_LAST_BATCH.booleanValue()))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.toString()))
            .andExpect(jsonPath("$.totalItems").value(DEFAULT_TOTAL_ITEMS));
    }

    @Test
    @Transactional
    void getNbvCompilationBatchesByIdFiltering() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        Long id = nbvCompilationBatch.getId();

        defaultNbvCompilationBatchShouldBeFound("id.equals=" + id);
        defaultNbvCompilationBatchShouldNotBeFound("id.notEquals=" + id);

        defaultNbvCompilationBatchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNbvCompilationBatchShouldNotBeFound("id.greaterThan=" + id);

        defaultNbvCompilationBatchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNbvCompilationBatchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex equals to DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.equals=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex equals to UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.equals=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex not equals to DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.notEquals=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex not equals to UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.notEquals=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex in DEFAULT_START_INDEX or UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.in=" + DEFAULT_START_INDEX + "," + UPDATED_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex equals to UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.in=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex is not null
        defaultNbvCompilationBatchShouldBeFound("startIndex.specified=true");

        // Get all the nbvCompilationBatchList where startIndex is null
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex is greater than or equal to DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.greaterThanOrEqual=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex is greater than or equal to UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.greaterThanOrEqual=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex is less than or equal to DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.lessThanOrEqual=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex is less than or equal to SMALLER_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.lessThanOrEqual=" + SMALLER_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex is less than DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.lessThan=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex is less than UPDATED_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.lessThan=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByStartIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where startIndex is greater than DEFAULT_START_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("startIndex.greaterThan=" + DEFAULT_START_INDEX);

        // Get all the nbvCompilationBatchList where startIndex is greater than SMALLER_START_INDEX
        defaultNbvCompilationBatchShouldBeFound("startIndex.greaterThan=" + SMALLER_START_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex equals to DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.equals=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex equals to UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.equals=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex not equals to DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.notEquals=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex not equals to UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.notEquals=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex in DEFAULT_END_INDEX or UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.in=" + DEFAULT_END_INDEX + "," + UPDATED_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex equals to UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.in=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex is not null
        defaultNbvCompilationBatchShouldBeFound("endIndex.specified=true");

        // Get all the nbvCompilationBatchList where endIndex is null
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex is greater than or equal to DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.greaterThanOrEqual=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex is greater than or equal to UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.greaterThanOrEqual=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex is less than or equal to DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.lessThanOrEqual=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex is less than or equal to SMALLER_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.lessThanOrEqual=" + SMALLER_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex is less than DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.lessThan=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex is less than UPDATED_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.lessThan=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByEndIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where endIndex is greater than DEFAULT_END_INDEX
        defaultNbvCompilationBatchShouldNotBeFound("endIndex.greaterThan=" + DEFAULT_END_INDEX);

        // Get all the nbvCompilationBatchList where endIndex is greater than SMALLER_END_INDEX
        defaultNbvCompilationBatchShouldBeFound("endIndex.greaterThan=" + SMALLER_END_INDEX);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchStatus equals to DEFAULT_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldBeFound("compilationBatchStatus.equals=" + DEFAULT_COMPILATION_BATCH_STATUS);

        // Get all the nbvCompilationBatchList where compilationBatchStatus equals to UPDATED_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchStatus.equals=" + UPDATED_COMPILATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchStatus not equals to DEFAULT_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchStatus.notEquals=" + DEFAULT_COMPILATION_BATCH_STATUS);

        // Get all the nbvCompilationBatchList where compilationBatchStatus not equals to UPDATED_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldBeFound("compilationBatchStatus.notEquals=" + UPDATED_COMPILATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchStatusIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchStatus in DEFAULT_COMPILATION_BATCH_STATUS or UPDATED_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldBeFound(
            "compilationBatchStatus.in=" + DEFAULT_COMPILATION_BATCH_STATUS + "," + UPDATED_COMPILATION_BATCH_STATUS
        );

        // Get all the nbvCompilationBatchList where compilationBatchStatus equals to UPDATED_COMPILATION_BATCH_STATUS
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchStatus.in=" + UPDATED_COMPILATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchStatus is not null
        defaultNbvCompilationBatchShouldBeFound("compilationBatchStatus.specified=true");

        // Get all the nbvCompilationBatchList where compilationBatchStatus is null
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier equals to DEFAULT_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("compilationBatchIdentifier.equals=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchIdentifier.equals=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier not equals to DEFAULT_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchIdentifier.notEquals=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier not equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("compilationBatchIdentifier.notEquals=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier in DEFAULT_COMPILATION_BATCH_IDENTIFIER or UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound(
            "compilationBatchIdentifier.in=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER + "," + UPDATED_COMPILATION_BATCH_IDENTIFIER
        );

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchIdentifier.in=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationBatchIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier is not null
        defaultNbvCompilationBatchShouldBeFound("compilationBatchIdentifier.specified=true");

        // Get all the nbvCompilationBatchList where compilationBatchIdentifier is null
        defaultNbvCompilationBatchShouldNotBeFound("compilationBatchIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationJobidentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationJobidentifier equals to DEFAULT_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("compilationJobidentifier.equals=" + DEFAULT_COMPILATION_JOBIDENTIFIER);

        // Get all the nbvCompilationBatchList where compilationJobidentifier equals to UPDATED_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationJobidentifier.equals=" + UPDATED_COMPILATION_JOBIDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationJobidentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationJobidentifier not equals to DEFAULT_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationJobidentifier.notEquals=" + DEFAULT_COMPILATION_JOBIDENTIFIER);

        // Get all the nbvCompilationBatchList where compilationJobidentifier not equals to UPDATED_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("compilationJobidentifier.notEquals=" + UPDATED_COMPILATION_JOBIDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationJobidentifierIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationJobidentifier in DEFAULT_COMPILATION_JOBIDENTIFIER or UPDATED_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldBeFound(
            "compilationJobidentifier.in=" + DEFAULT_COMPILATION_JOBIDENTIFIER + "," + UPDATED_COMPILATION_JOBIDENTIFIER
        );

        // Get all the nbvCompilationBatchList where compilationJobidentifier equals to UPDATED_COMPILATION_JOBIDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("compilationJobidentifier.in=" + UPDATED_COMPILATION_JOBIDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByCompilationJobidentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where compilationJobidentifier is not null
        defaultNbvCompilationBatchShouldBeFound("compilationJobidentifier.specified=true");

        // Get all the nbvCompilationBatchList where compilationJobidentifier is null
        defaultNbvCompilationBatchShouldNotBeFound("compilationJobidentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByDepreciationPeriodIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier equals to DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("depreciationPeriodIdentifier.equals=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("depreciationPeriodIdentifier.equals=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByDepreciationPeriodIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier not equals to DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("depreciationPeriodIdentifier.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier not equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("depreciationPeriodIdentifier.notEquals=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByDepreciationPeriodIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier in DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER or UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound(
            "depreciationPeriodIdentifier.in=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER + "," + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        );

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("depreciationPeriodIdentifier.in=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByDepreciationPeriodIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier is not null
        defaultNbvCompilationBatchShouldBeFound("depreciationPeriodIdentifier.specified=true");

        // Get all the nbvCompilationBatchList where depreciationPeriodIdentifier is null
        defaultNbvCompilationBatchShouldNotBeFound("depreciationPeriodIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByFiscalMonthIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier equals to DEFAULT_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("fiscalMonthIdentifier.equals=" + DEFAULT_FISCAL_MONTH_IDENTIFIER);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("fiscalMonthIdentifier.equals=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByFiscalMonthIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier not equals to DEFAULT_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("fiscalMonthIdentifier.notEquals=" + DEFAULT_FISCAL_MONTH_IDENTIFIER);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier not equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound("fiscalMonthIdentifier.notEquals=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByFiscalMonthIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier in DEFAULT_FISCAL_MONTH_IDENTIFIER or UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldBeFound(
            "fiscalMonthIdentifier.in=" + DEFAULT_FISCAL_MONTH_IDENTIFIER + "," + UPDATED_FISCAL_MONTH_IDENTIFIER
        );

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultNbvCompilationBatchShouldNotBeFound("fiscalMonthIdentifier.in=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByFiscalMonthIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier is not null
        defaultNbvCompilationBatchShouldBeFound("fiscalMonthIdentifier.specified=true");

        // Get all the nbvCompilationBatchList where fiscalMonthIdentifier is null
        defaultNbvCompilationBatchShouldNotBeFound("fiscalMonthIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize equals to DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.equals=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize equals to UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.equals=" + UPDATED_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize not equals to DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.notEquals=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize not equals to UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.notEquals=" + UPDATED_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize in DEFAULT_BATCH_SIZE or UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.in=" + DEFAULT_BATCH_SIZE + "," + UPDATED_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize equals to UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.in=" + UPDATED_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize is not null
        defaultNbvCompilationBatchShouldBeFound("batchSize.specified=true");

        // Get all the nbvCompilationBatchList where batchSize is null
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize is greater than or equal to DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.greaterThanOrEqual=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize is greater than or equal to UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.greaterThanOrEqual=" + UPDATED_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize is less than or equal to DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.lessThanOrEqual=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize is less than or equal to SMALLER_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.lessThanOrEqual=" + SMALLER_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize is less than DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.lessThan=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize is less than UPDATED_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.lessThan=" + UPDATED_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByBatchSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where batchSize is greater than DEFAULT_BATCH_SIZE
        defaultNbvCompilationBatchShouldNotBeFound("batchSize.greaterThan=" + DEFAULT_BATCH_SIZE);

        // Get all the nbvCompilationBatchList where batchSize is greater than SMALLER_BATCH_SIZE
        defaultNbvCompilationBatchShouldBeFound("batchSize.greaterThan=" + SMALLER_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems equals to DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.equals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.equals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems not equals to DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.notEquals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems not equals to UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.notEquals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems in DEFAULT_PROCESSED_ITEMS or UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.in=" + DEFAULT_PROCESSED_ITEMS + "," + UPDATED_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.in=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems is not null
        defaultNbvCompilationBatchShouldBeFound("processedItems.specified=true");

        // Get all the nbvCompilationBatchList where processedItems is null
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems is greater than or equal to DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.greaterThanOrEqual=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems is greater than or equal to UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.greaterThanOrEqual=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems is less than or equal to DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.lessThanOrEqual=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems is less than or equal to SMALLER_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.lessThanOrEqual=" + SMALLER_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems is less than DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.lessThan=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems is less than UPDATED_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.lessThan=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessedItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processedItems is greater than DEFAULT_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("processedItems.greaterThan=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the nbvCompilationBatchList where processedItems is greater than SMALLER_PROCESSED_ITEMS
        defaultNbvCompilationBatchShouldBeFound("processedItems.greaterThan=" + SMALLER_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber is not null
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.specified=true");

        // Get all the nbvCompilationBatchList where sequenceNumber is null
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the nbvCompilationBatchList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultNbvCompilationBatchShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByIsLastBatchIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where isLastBatch equals to DEFAULT_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldBeFound("isLastBatch.equals=" + DEFAULT_IS_LAST_BATCH);

        // Get all the nbvCompilationBatchList where isLastBatch equals to UPDATED_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldNotBeFound("isLastBatch.equals=" + UPDATED_IS_LAST_BATCH);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByIsLastBatchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where isLastBatch not equals to DEFAULT_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldNotBeFound("isLastBatch.notEquals=" + DEFAULT_IS_LAST_BATCH);

        // Get all the nbvCompilationBatchList where isLastBatch not equals to UPDATED_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldBeFound("isLastBatch.notEquals=" + UPDATED_IS_LAST_BATCH);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByIsLastBatchIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where isLastBatch in DEFAULT_IS_LAST_BATCH or UPDATED_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldBeFound("isLastBatch.in=" + DEFAULT_IS_LAST_BATCH + "," + UPDATED_IS_LAST_BATCH);

        // Get all the nbvCompilationBatchList where isLastBatch equals to UPDATED_IS_LAST_BATCH
        defaultNbvCompilationBatchShouldNotBeFound("isLastBatch.in=" + UPDATED_IS_LAST_BATCH);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByIsLastBatchIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where isLastBatch is not null
        defaultNbvCompilationBatchShouldBeFound("isLastBatch.specified=true");

        // Get all the nbvCompilationBatchList where isLastBatch is null
        defaultNbvCompilationBatchShouldNotBeFound("isLastBatch.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime equals to DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.equals=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime not equals to DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.notEquals=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime not equals to UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.notEquals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime in DEFAULT_PROCESSING_TIME or UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.in=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime is not null
        defaultNbvCompilationBatchShouldBeFound("processingTime.specified=true");

        // Get all the nbvCompilationBatchList where processingTime is null
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime is greater than or equal to DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime is greater than or equal to UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime is less than or equal to DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime is less than or equal to SMALLER_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime is less than DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime is less than UPDATED_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.lessThan=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where processingTime is greater than DEFAULT_PROCESSING_TIME
        defaultNbvCompilationBatchShouldNotBeFound("processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the nbvCompilationBatchList where processingTime is greater than SMALLER_PROCESSING_TIME
        defaultNbvCompilationBatchShouldBeFound("processingTime.greaterThan=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems equals to DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.equals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.equals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems not equals to DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.notEquals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems not equals to UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.notEquals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsInShouldWork() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems in DEFAULT_TOTAL_ITEMS or UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.in=" + DEFAULT_TOTAL_ITEMS + "," + UPDATED_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.in=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems is not null
        defaultNbvCompilationBatchShouldBeFound("totalItems.specified=true");

        // Get all the nbvCompilationBatchList where totalItems is null
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems is greater than or equal to DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems is greater than or equal to UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems is less than or equal to DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems is less than or equal to SMALLER_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems is less than DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.lessThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems is less than UPDATED_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.lessThan=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByTotalItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        // Get all the nbvCompilationBatchList where totalItems is greater than DEFAULT_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldNotBeFound("totalItems.greaterThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the nbvCompilationBatchList where totalItems is greater than SMALLER_TOTAL_ITEMS
        defaultNbvCompilationBatchShouldBeFound("totalItems.greaterThan=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllNbvCompilationBatchesByNbvCompilationJobIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);
        NbvCompilationJob nbvCompilationJob;
        if (TestUtil.findAll(em, NbvCompilationJob.class).isEmpty()) {
            nbvCompilationJob = NbvCompilationJobResourceIT.createEntity(em);
            em.persist(nbvCompilationJob);
            em.flush();
        } else {
            nbvCompilationJob = TestUtil.findAll(em, NbvCompilationJob.class).get(0);
        }
        em.persist(nbvCompilationJob);
        em.flush();
        nbvCompilationBatch.setNbvCompilationJob(nbvCompilationJob);
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);
        Long nbvCompilationJobId = nbvCompilationJob.getId();

        // Get all the nbvCompilationBatchList where nbvCompilationJob equals to nbvCompilationJobId
        defaultNbvCompilationBatchShouldBeFound("nbvCompilationJobId.equals=" + nbvCompilationJobId);

        // Get all the nbvCompilationBatchList where nbvCompilationJob equals to (nbvCompilationJobId + 1)
        defaultNbvCompilationBatchShouldNotBeFound("nbvCompilationJobId.equals=" + (nbvCompilationJobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNbvCompilationBatchShouldBeFound(String filter) throws Exception {
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].compilationBatchStatus").value(hasItem(DEFAULT_COMPILATION_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobidentifier").value(hasItem(DEFAULT_COMPILATION_JOBIDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSize").value(hasItem(DEFAULT_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].isLastBatch").value(hasItem(DEFAULT_IS_LAST_BATCH.booleanValue())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));

        // Check, that the count call also returns 1
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNbvCompilationBatchShouldNotBeFound(String filter) throws Exception {
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNbvCompilationBatch() throws Exception {
        // Get the nbvCompilationBatch
        restNbvCompilationBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNbvCompilationBatch() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();

        // Update the nbvCompilationBatch
        NbvCompilationBatch updatedNbvCompilationBatch = nbvCompilationBatchRepository.findById(nbvCompilationBatch.getId()).get();
        // Disconnect from session so that the updates on updatedNbvCompilationBatch are not directly saved in db
        em.detach(updatedNbvCompilationBatch);
        updatedNbvCompilationBatch
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .compilationBatchStatus(UPDATED_COMPILATION_BATCH_STATUS)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .compilationJobidentifier(UPDATED_COMPILATION_JOBIDENTIFIER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .batchSize(UPDATED_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .isLastBatch(UPDATED_IS_LAST_BATCH)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(updatedNbvCompilationBatch);

        restNbvCompilationBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvCompilationBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationBatch testNbvCompilationBatch = nbvCompilationBatchList.get(nbvCompilationBatchList.size() - 1);
        assertThat(testNbvCompilationBatch.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testNbvCompilationBatch.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testNbvCompilationBatch.getCompilationBatchStatus()).isEqualTo(UPDATED_COMPILATION_BATCH_STATUS);
        assertThat(testNbvCompilationBatch.getCompilationBatchIdentifier()).isEqualTo(UPDATED_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getCompilationJobidentifier()).isEqualTo(UPDATED_COMPILATION_JOBIDENTIFIER);
        assertThat(testNbvCompilationBatch.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getBatchSize()).isEqualTo(UPDATED_BATCH_SIZE);
        assertThat(testNbvCompilationBatch.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testNbvCompilationBatch.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testNbvCompilationBatch.getIsLastBatch()).isEqualTo(UPDATED_IS_LAST_BATCH);
        assertThat(testNbvCompilationBatch.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testNbvCompilationBatch.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository).save(testNbvCompilationBatch);
    }

    @Test
    @Transactional
    void putNonExistingNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvCompilationBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void putWithIdMismatchNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void partialUpdateNbvCompilationBatchWithPatch() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();

        // Update the nbvCompilationBatch using partial update
        NbvCompilationBatch partialUpdatedNbvCompilationBatch = new NbvCompilationBatch();
        partialUpdatedNbvCompilationBatch.setId(nbvCompilationBatch.getId());

        partialUpdatedNbvCompilationBatch
            .endIndex(UPDATED_END_INDEX)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .isLastBatch(UPDATED_IS_LAST_BATCH)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);

        restNbvCompilationBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvCompilationBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvCompilationBatch))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationBatch testNbvCompilationBatch = nbvCompilationBatchList.get(nbvCompilationBatchList.size() - 1);
        assertThat(testNbvCompilationBatch.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testNbvCompilationBatch.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testNbvCompilationBatch.getCompilationBatchStatus()).isEqualTo(DEFAULT_COMPILATION_BATCH_STATUS);
        assertThat(testNbvCompilationBatch.getCompilationBatchIdentifier()).isEqualTo(DEFAULT_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getCompilationJobidentifier()).isEqualTo(DEFAULT_COMPILATION_JOBIDENTIFIER);
        assertThat(testNbvCompilationBatch.getDepreciationPeriodIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getFiscalMonthIdentifier()).isEqualTo(DEFAULT_FISCAL_MONTH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getBatchSize()).isEqualTo(DEFAULT_BATCH_SIZE);
        assertThat(testNbvCompilationBatch.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testNbvCompilationBatch.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testNbvCompilationBatch.getIsLastBatch()).isEqualTo(UPDATED_IS_LAST_BATCH);
        assertThat(testNbvCompilationBatch.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testNbvCompilationBatch.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void fullUpdateNbvCompilationBatchWithPatch() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();

        // Update the nbvCompilationBatch using partial update
        NbvCompilationBatch partialUpdatedNbvCompilationBatch = new NbvCompilationBatch();
        partialUpdatedNbvCompilationBatch.setId(nbvCompilationBatch.getId());

        partialUpdatedNbvCompilationBatch
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .compilationBatchStatus(UPDATED_COMPILATION_BATCH_STATUS)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .compilationJobidentifier(UPDATED_COMPILATION_JOBIDENTIFIER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .batchSize(UPDATED_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .isLastBatch(UPDATED_IS_LAST_BATCH)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);

        restNbvCompilationBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvCompilationBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvCompilationBatch))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationBatch testNbvCompilationBatch = nbvCompilationBatchList.get(nbvCompilationBatchList.size() - 1);
        assertThat(testNbvCompilationBatch.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testNbvCompilationBatch.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testNbvCompilationBatch.getCompilationBatchStatus()).isEqualTo(UPDATED_COMPILATION_BATCH_STATUS);
        assertThat(testNbvCompilationBatch.getCompilationBatchIdentifier()).isEqualTo(UPDATED_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getCompilationJobidentifier()).isEqualTo(UPDATED_COMPILATION_JOBIDENTIFIER);
        assertThat(testNbvCompilationBatch.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testNbvCompilationBatch.getBatchSize()).isEqualTo(UPDATED_BATCH_SIZE);
        assertThat(testNbvCompilationBatch.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testNbvCompilationBatch.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testNbvCompilationBatch.getIsLastBatch()).isEqualTo(UPDATED_IS_LAST_BATCH);
        assertThat(testNbvCompilationBatch.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testNbvCompilationBatch.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void patchNonExistingNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nbvCompilationBatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNbvCompilationBatch() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationBatchRepository.findAll().size();
        nbvCompilationBatch.setId(count.incrementAndGet());

        // Create the NbvCompilationBatch
        NbvCompilationBatchDTO nbvCompilationBatchDTO = nbvCompilationBatchMapper.toDto(nbvCompilationBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationBatchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationBatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvCompilationBatch in the database
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(0)).save(nbvCompilationBatch);
    }

    @Test
    @Transactional
    void deleteNbvCompilationBatch() throws Exception {
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);

        int databaseSizeBeforeDelete = nbvCompilationBatchRepository.findAll().size();

        // Delete the nbvCompilationBatch
        restNbvCompilationBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, nbvCompilationBatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NbvCompilationBatch> nbvCompilationBatchList = nbvCompilationBatchRepository.findAll();
        assertThat(nbvCompilationBatchList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NbvCompilationBatch in Elasticsearch
        verify(mockNbvCompilationBatchSearchRepository, times(1)).deleteById(nbvCompilationBatch.getId());
    }

    @Test
    @Transactional
    void searchNbvCompilationBatch() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nbvCompilationBatchRepository.saveAndFlush(nbvCompilationBatch);
        when(mockNbvCompilationBatchSearchRepository.search("id:" + nbvCompilationBatch.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nbvCompilationBatch), PageRequest.of(0, 1), 1));

        // Search the nbvCompilationBatch
        restNbvCompilationBatchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nbvCompilationBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].compilationBatchStatus").value(hasItem(DEFAULT_COMPILATION_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobidentifier").value(hasItem(DEFAULT_COMPILATION_JOBIDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSize").value(hasItem(DEFAULT_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].isLastBatch").value(hasItem(DEFAULT_IS_LAST_BATCH.booleanValue())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));
    }
}
