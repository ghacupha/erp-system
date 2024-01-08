package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.DepreciationJob;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
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
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the DepreciationJobResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class DepreciationJobResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_OF_COMMENCEMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_COMMENCEMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_COMMENCEMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final DepreciationJobStatusType DEFAULT_DEPRECIATION_JOB_STATUS = DepreciationJobStatusType.COMPLETE;
    private static final DepreciationJobStatusType UPDATED_DEPRECIATION_JOB_STATUS = DepreciationJobStatusType.RUNNING;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_BATCHES = 1;
    private static final Integer UPDATED_NUMBER_OF_BATCHES = 2;
    private static final Integer SMALLER_NUMBER_OF_BATCHES = 1 - 1;

    private static final Integer DEFAULT_PROCESSED_BATCHES = 1;
    private static final Integer UPDATED_PROCESSED_BATCHES = 2;
    private static final Integer SMALLER_PROCESSED_BATCHES = 1 - 1;

    private static final Integer DEFAULT_LAST_BATCH_SIZE = 1;
    private static final Integer UPDATED_LAST_BATCH_SIZE = 2;
    private static final Integer SMALLER_LAST_BATCH_SIZE = 1 - 1;

    private static final Integer DEFAULT_PROCESSED_ITEMS = 1;
    private static final Integer UPDATED_PROCESSED_ITEMS = 2;
    private static final Integer SMALLER_PROCESSED_ITEMS = 1 - 1;

    private static final Duration DEFAULT_PROCESSING_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_PROCESSING_TIME = Duration.ofHours(12);
    private static final Duration SMALLER_PROCESSING_TIME = Duration.ofHours(5);

    private static final Integer DEFAULT_TOTAL_ITEMS = 1;
    private static final Integer UPDATED_TOTAL_ITEMS = 2;
    private static final Integer SMALLER_TOTAL_ITEMS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-jobs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationJobRepository depreciationJobRepository;

    @Autowired
    private DepreciationJobMapper depreciationJobMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationJobSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationJobSearchRepository mockDepreciationJobSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationJobMockMvc;

    private DepreciationJob depreciationJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJob createEntity(EntityManager em) {
        DepreciationJob depreciationJob = new DepreciationJob()
            .timeOfCommencement(DEFAULT_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(DEFAULT_DEPRECIATION_JOB_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .numberOfBatches(DEFAULT_NUMBER_OF_BATCHES)
            .processedBatches(DEFAULT_PROCESSED_BATCHES)
            .lastBatchSize(DEFAULT_LAST_BATCH_SIZE)
            .processedItems(DEFAULT_PROCESSED_ITEMS)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .totalItems(DEFAULT_TOTAL_ITEMS);
        return depreciationJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJob createUpdatedEntity(EntityManager em) {
        DepreciationJob depreciationJob = new DepreciationJob()
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .processedBatches(UPDATED_PROCESSED_BATCHES)
            .lastBatchSize(UPDATED_LAST_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);
        return depreciationJob;
    }

    @BeforeEach
    public void initTest() {
        depreciationJob = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationJob() throws Exception {
        int databaseSizeBeforeCreate = depreciationJobRepository.findAll().size();
        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);
        restDepreciationJobMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(DEFAULT_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(DEFAULT_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepreciationJob.getNumberOfBatches()).isEqualTo(DEFAULT_NUMBER_OF_BATCHES);
        assertThat(testDepreciationJob.getProcessedBatches()).isEqualTo(DEFAULT_PROCESSED_BATCHES);
        assertThat(testDepreciationJob.getLastBatchSize()).isEqualTo(DEFAULT_LAST_BATCH_SIZE);
        assertThat(testDepreciationJob.getProcessedItems()).isEqualTo(DEFAULT_PROCESSED_ITEMS);
        assertThat(testDepreciationJob.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);
        assertThat(testDepreciationJob.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(1)).save(testDepreciationJob);
    }

    @Test
    @Transactional
    void createDepreciationJobWithExistingId() throws Exception {
        // Create the DepreciationJob with an existing ID
        depreciationJob.setId(1L);
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        int databaseSizeBeforeCreate = depreciationJobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationJobMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void getAllDepreciationJobs() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfCommencement").value(hasItem(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT))))
            .andExpect(jsonPath("$.[*].depreciationJobStatus").value(hasItem(DEFAULT_DEPRECIATION_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].processedBatches").value(hasItem(DEFAULT_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].lastBatchSize").value(hasItem(DEFAULT_LAST_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));
    }

    @Test
    @Transactional
    void getDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get the depreciationJob
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationJob.getId().intValue()))
            .andExpect(jsonPath("$.timeOfCommencement").value(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT)))
            .andExpect(jsonPath("$.depreciationJobStatus").value(DEFAULT_DEPRECIATION_JOB_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.numberOfBatches").value(DEFAULT_NUMBER_OF_BATCHES))
            .andExpect(jsonPath("$.processedBatches").value(DEFAULT_PROCESSED_BATCHES))
            .andExpect(jsonPath("$.lastBatchSize").value(DEFAULT_LAST_BATCH_SIZE))
            .andExpect(jsonPath("$.processedItems").value(DEFAULT_PROCESSED_ITEMS))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.toString()))
            .andExpect(jsonPath("$.totalItems").value(DEFAULT_TOTAL_ITEMS));
    }

    @Test
    @Transactional
    void getDepreciationJobsByIdFiltering() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        Long id = depreciationJob.getId();

        defaultDepreciationJobShouldBeFound("id.equals=" + id);
        defaultDepreciationJobShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationJobShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationJobShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationJobShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationJobShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement equals to DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.equals=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement equals to UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.equals=" + UPDATED_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement not equals to DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.notEquals=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement not equals to UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.notEquals=" + UPDATED_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement in DEFAULT_TIME_OF_COMMENCEMENT or UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.in=" + DEFAULT_TIME_OF_COMMENCEMENT + "," + UPDATED_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement equals to UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.in=" + UPDATED_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement is not null
        defaultDepreciationJobShouldBeFound("timeOfCommencement.specified=true");

        // Get all the depreciationJobList where timeOfCommencement is null
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement is greater than or equal to DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.greaterThanOrEqual=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement is greater than or equal to UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.greaterThanOrEqual=" + UPDATED_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement is less than or equal to DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.lessThanOrEqual=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement is less than or equal to SMALLER_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.lessThanOrEqual=" + SMALLER_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement is less than DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.lessThan=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement is less than UPDATED_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.lessThan=" + UPDATED_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTimeOfCommencementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where timeOfCommencement is greater than DEFAULT_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldNotBeFound("timeOfCommencement.greaterThan=" + DEFAULT_TIME_OF_COMMENCEMENT);

        // Get all the depreciationJobList where timeOfCommencement is greater than SMALLER_TIME_OF_COMMENCEMENT
        defaultDepreciationJobShouldBeFound("timeOfCommencement.greaterThan=" + SMALLER_TIME_OF_COMMENCEMENT);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDepreciationJobStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where depreciationJobStatus equals to DEFAULT_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldBeFound("depreciationJobStatus.equals=" + DEFAULT_DEPRECIATION_JOB_STATUS);

        // Get all the depreciationJobList where depreciationJobStatus equals to UPDATED_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldNotBeFound("depreciationJobStatus.equals=" + UPDATED_DEPRECIATION_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDepreciationJobStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where depreciationJobStatus not equals to DEFAULT_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldNotBeFound("depreciationJobStatus.notEquals=" + DEFAULT_DEPRECIATION_JOB_STATUS);

        // Get all the depreciationJobList where depreciationJobStatus not equals to UPDATED_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldBeFound("depreciationJobStatus.notEquals=" + UPDATED_DEPRECIATION_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDepreciationJobStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where depreciationJobStatus in DEFAULT_DEPRECIATION_JOB_STATUS or UPDATED_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldBeFound(
            "depreciationJobStatus.in=" + DEFAULT_DEPRECIATION_JOB_STATUS + "," + UPDATED_DEPRECIATION_JOB_STATUS
        );

        // Get all the depreciationJobList where depreciationJobStatus equals to UPDATED_DEPRECIATION_JOB_STATUS
        defaultDepreciationJobShouldNotBeFound("depreciationJobStatus.in=" + UPDATED_DEPRECIATION_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDepreciationJobStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where depreciationJobStatus is not null
        defaultDepreciationJobShouldBeFound("depreciationJobStatus.specified=true");

        // Get all the depreciationJobList where depreciationJobStatus is null
        defaultDepreciationJobShouldNotBeFound("depreciationJobStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description equals to DEFAULT_DESCRIPTION
        defaultDepreciationJobShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationJobList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationJobShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description not equals to DEFAULT_DESCRIPTION
        defaultDepreciationJobShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationJobList where description not equals to UPDATED_DESCRIPTION
        defaultDepreciationJobShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDepreciationJobShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the depreciationJobList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationJobShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description is not null
        defaultDepreciationJobShouldBeFound("description.specified=true");

        // Get all the depreciationJobList where description is null
        defaultDepreciationJobShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description contains DEFAULT_DESCRIPTION
        defaultDepreciationJobShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationJobList where description contains UPDATED_DESCRIPTION
        defaultDepreciationJobShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where description does not contain DEFAULT_DESCRIPTION
        defaultDepreciationJobShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationJobList where description does not contain UPDATED_DESCRIPTION
        defaultDepreciationJobShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches equals to DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.equals=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches equals to UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.equals=" + UPDATED_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches not equals to DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.notEquals=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches not equals to UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.notEquals=" + UPDATED_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches in DEFAULT_NUMBER_OF_BATCHES or UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.in=" + DEFAULT_NUMBER_OF_BATCHES + "," + UPDATED_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches equals to UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.in=" + UPDATED_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches is not null
        defaultDepreciationJobShouldBeFound("numberOfBatches.specified=true");

        // Get all the depreciationJobList where numberOfBatches is null
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches is greater than or equal to DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches is greater than or equal to UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.greaterThanOrEqual=" + UPDATED_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches is less than or equal to DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.lessThanOrEqual=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches is less than or equal to SMALLER_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.lessThanOrEqual=" + SMALLER_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches is less than DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.lessThan=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches is less than UPDATED_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.lessThan=" + UPDATED_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByNumberOfBatchesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where numberOfBatches is greater than DEFAULT_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldNotBeFound("numberOfBatches.greaterThan=" + DEFAULT_NUMBER_OF_BATCHES);

        // Get all the depreciationJobList where numberOfBatches is greater than SMALLER_NUMBER_OF_BATCHES
        defaultDepreciationJobShouldBeFound("numberOfBatches.greaterThan=" + SMALLER_NUMBER_OF_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches equals to DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.equals=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches equals to UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.equals=" + UPDATED_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches not equals to DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.notEquals=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches not equals to UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.notEquals=" + UPDATED_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches in DEFAULT_PROCESSED_BATCHES or UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.in=" + DEFAULT_PROCESSED_BATCHES + "," + UPDATED_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches equals to UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.in=" + UPDATED_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches is not null
        defaultDepreciationJobShouldBeFound("processedBatches.specified=true");

        // Get all the depreciationJobList where processedBatches is null
        defaultDepreciationJobShouldNotBeFound("processedBatches.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches is greater than or equal to DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.greaterThanOrEqual=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches is greater than or equal to UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.greaterThanOrEqual=" + UPDATED_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches is less than or equal to DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.lessThanOrEqual=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches is less than or equal to SMALLER_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.lessThanOrEqual=" + SMALLER_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches is less than DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.lessThan=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches is less than UPDATED_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.lessThan=" + UPDATED_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedBatchesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedBatches is greater than DEFAULT_PROCESSED_BATCHES
        defaultDepreciationJobShouldNotBeFound("processedBatches.greaterThan=" + DEFAULT_PROCESSED_BATCHES);

        // Get all the depreciationJobList where processedBatches is greater than SMALLER_PROCESSED_BATCHES
        defaultDepreciationJobShouldBeFound("processedBatches.greaterThan=" + SMALLER_PROCESSED_BATCHES);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize equals to DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.equals=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize equals to UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.equals=" + UPDATED_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize not equals to DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.notEquals=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize not equals to UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.notEquals=" + UPDATED_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize in DEFAULT_LAST_BATCH_SIZE or UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.in=" + DEFAULT_LAST_BATCH_SIZE + "," + UPDATED_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize equals to UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.in=" + UPDATED_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize is not null
        defaultDepreciationJobShouldBeFound("lastBatchSize.specified=true");

        // Get all the depreciationJobList where lastBatchSize is null
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize is greater than or equal to DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.greaterThanOrEqual=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize is greater than or equal to UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.greaterThanOrEqual=" + UPDATED_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize is less than or equal to DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.lessThanOrEqual=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize is less than or equal to SMALLER_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.lessThanOrEqual=" + SMALLER_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize is less than DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.lessThan=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize is less than UPDATED_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.lessThan=" + UPDATED_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByLastBatchSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where lastBatchSize is greater than DEFAULT_LAST_BATCH_SIZE
        defaultDepreciationJobShouldNotBeFound("lastBatchSize.greaterThan=" + DEFAULT_LAST_BATCH_SIZE);

        // Get all the depreciationJobList where lastBatchSize is greater than SMALLER_LAST_BATCH_SIZE
        defaultDepreciationJobShouldBeFound("lastBatchSize.greaterThan=" + SMALLER_LAST_BATCH_SIZE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems equals to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.equals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.equals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems not equals to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.notEquals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems not equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.notEquals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems in DEFAULT_PROCESSED_ITEMS or UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.in=" + DEFAULT_PROCESSED_ITEMS + "," + UPDATED_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.in=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems is not null
        defaultDepreciationJobShouldBeFound("processedItems.specified=true");

        // Get all the depreciationJobList where processedItems is null
        defaultDepreciationJobShouldNotBeFound("processedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems is greater than or equal to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.greaterThanOrEqual=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems is greater than or equal to UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.greaterThanOrEqual=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems is less than or equal to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.lessThanOrEqual=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems is less than or equal to SMALLER_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.lessThanOrEqual=" + SMALLER_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems is less than DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.lessThan=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems is less than UPDATED_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.lessThan=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessedItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processedItems is greater than DEFAULT_PROCESSED_ITEMS
        defaultDepreciationJobShouldNotBeFound("processedItems.greaterThan=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationJobList where processedItems is greater than SMALLER_PROCESSED_ITEMS
        defaultDepreciationJobShouldBeFound("processedItems.greaterThan=" + SMALLER_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime equals to DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.equals=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime not equals to DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.notEquals=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime not equals to UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.notEquals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime in DEFAULT_PROCESSING_TIME or UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.in=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime is not null
        defaultDepreciationJobShouldBeFound("processingTime.specified=true");

        // Get all the depreciationJobList where processingTime is null
        defaultDepreciationJobShouldNotBeFound("processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime is greater than or equal to DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime is greater than or equal to UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime is less than or equal to DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime is less than or equal to SMALLER_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime is less than DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime is less than UPDATED_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.lessThan=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where processingTime is greater than DEFAULT_PROCESSING_TIME
        defaultDepreciationJobShouldNotBeFound("processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the depreciationJobList where processingTime is greater than SMALLER_PROCESSING_TIME
        defaultDepreciationJobShouldBeFound("processingTime.greaterThan=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems equals to DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.equals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.equals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems not equals to DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.notEquals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems not equals to UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.notEquals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems in DEFAULT_TOTAL_ITEMS or UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.in=" + DEFAULT_TOTAL_ITEMS + "," + UPDATED_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.in=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems is not null
        defaultDepreciationJobShouldBeFound("totalItems.specified=true");

        // Get all the depreciationJobList where totalItems is null
        defaultDepreciationJobShouldNotBeFound("totalItems.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems is greater than or equal to DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems is greater than or equal to UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems is less than or equal to DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems is less than or equal to SMALLER_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems is less than DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.lessThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems is less than UPDATED_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.lessThan=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByTotalItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList where totalItems is greater than DEFAULT_TOTAL_ITEMS
        defaultDepreciationJobShouldNotBeFound("totalItems.greaterThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the depreciationJobList where totalItems is greater than SMALLER_TOTAL_ITEMS
        defaultDepreciationJobShouldBeFound("totalItems.greaterThan=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobsByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(depreciationPeriod);
        em.flush();
        depreciationJob.setDepreciationPeriod(depreciationPeriod);
        depreciationJobRepository.saveAndFlush(depreciationJob);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the depreciationJobList where depreciationPeriod equals to depreciationPeriodId
        defaultDepreciationJobShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the depreciationJobList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultDepreciationJobShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationJobShouldBeFound(String filter) throws Exception {
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfCommencement").value(hasItem(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT))))
            .andExpect(jsonPath("$.[*].depreciationJobStatus").value(hasItem(DEFAULT_DEPRECIATION_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].processedBatches").value(hasItem(DEFAULT_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].lastBatchSize").value(hasItem(DEFAULT_LAST_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));

        // Check, that the count call also returns 1
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationJobShouldNotBeFound(String filter) throws Exception {
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationJob() throws Exception {
        // Get the depreciationJob
        restDepreciationJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob
        DepreciationJob updatedDepreciationJob = depreciationJobRepository.findById(depreciationJob.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationJob are not directly saved in db
        em.detach(updatedDepreciationJob);
        updatedDepreciationJob
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .processedBatches(UPDATED_PROCESSED_BATCHES)
            .lastBatchSize(UPDATED_LAST_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(updatedDepreciationJob);

        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(UPDATED_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(UPDATED_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepreciationJob.getNumberOfBatches()).isEqualTo(UPDATED_NUMBER_OF_BATCHES);
        assertThat(testDepreciationJob.getProcessedBatches()).isEqualTo(UPDATED_PROCESSED_BATCHES);
        assertThat(testDepreciationJob.getLastBatchSize()).isEqualTo(UPDATED_LAST_BATCH_SIZE);
        assertThat(testDepreciationJob.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationJob.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testDepreciationJob.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository).save(testDepreciationJob);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationJobWithPatch() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob using partial update
        DepreciationJob partialUpdatedDepreciationJob = new DepreciationJob();
        partialUpdatedDepreciationJob.setId(depreciationJob.getId());

        partialUpdatedDepreciationJob.lastBatchSize(UPDATED_LAST_BATCH_SIZE).processedItems(UPDATED_PROCESSED_ITEMS);

        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJob))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(DEFAULT_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(DEFAULT_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepreciationJob.getNumberOfBatches()).isEqualTo(DEFAULT_NUMBER_OF_BATCHES);
        assertThat(testDepreciationJob.getProcessedBatches()).isEqualTo(DEFAULT_PROCESSED_BATCHES);
        assertThat(testDepreciationJob.getLastBatchSize()).isEqualTo(UPDATED_LAST_BATCH_SIZE);
        assertThat(testDepreciationJob.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationJob.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);
        assertThat(testDepreciationJob.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationJobWithPatch() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob using partial update
        DepreciationJob partialUpdatedDepreciationJob = new DepreciationJob();
        partialUpdatedDepreciationJob.setId(depreciationJob.getId());

        partialUpdatedDepreciationJob
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .processedBatches(UPDATED_PROCESSED_BATCHES)
            .lastBatchSize(UPDATED_LAST_BATCH_SIZE)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .processingTime(UPDATED_PROCESSING_TIME)
            .totalItems(UPDATED_TOTAL_ITEMS);

        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJob))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(UPDATED_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(UPDATED_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepreciationJob.getNumberOfBatches()).isEqualTo(UPDATED_NUMBER_OF_BATCHES);
        assertThat(testDepreciationJob.getProcessedBatches()).isEqualTo(UPDATED_PROCESSED_BATCHES);
        assertThat(testDepreciationJob.getLastBatchSize()).isEqualTo(UPDATED_LAST_BATCH_SIZE);
        assertThat(testDepreciationJob.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationJob.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testDepreciationJob.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void deleteDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeDelete = depreciationJobRepository.findAll().size();

        // Delete the depreciationJob
        restDepreciationJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(1)).deleteById(depreciationJob.getId());
    }

    @Test
    @Transactional
    void searchDepreciationJob() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);
        when(mockDepreciationJobSearchRepository.search("id:" + depreciationJob.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationJob), PageRequest.of(0, 1), 1));

        // Search the depreciationJob
        restDepreciationJobMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfCommencement").value(hasItem(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT))))
            .andExpect(jsonPath("$.[*].depreciationJobStatus").value(hasItem(DEFAULT_DEPRECIATION_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].processedBatches").value(hasItem(DEFAULT_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].lastBatchSize").value(hasItem(DEFAULT_LAST_BATCH_SIZE)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));
    }
}
