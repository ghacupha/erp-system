package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.domain.enumeration.NVBCompilationStatus;
import io.github.erp.repository.NbvCompilationJobRepository;
import io.github.erp.repository.search.NbvCompilationJobSearchRepository;
import io.github.erp.service.criteria.NbvCompilationJobCriteria;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.github.erp.service.mapper.NbvCompilationJobMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link NbvCompilationJobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NbvCompilationJobResourceIT {

    private static final UUID DEFAULT_COMPILATION_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_JOB_IDENTIFIER = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_COMPILATION_JOB_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COMPILATION_JOB_TIME_OF_REQUEST = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final Integer DEFAULT_ENTITIES_AFFECTED = 1;
    private static final Integer UPDATED_ENTITIES_AFFECTED = 2;
    private static final Integer SMALLER_ENTITIES_AFFECTED = 1 - 1;

    private static final NVBCompilationStatus DEFAULT_COMPILATION_STATUS = NVBCompilationStatus.STARTED;
    private static final NVBCompilationStatus UPDATED_COMPILATION_STATUS = NVBCompilationStatus.RUNNING;

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_BATCHES = 1;
    private static final Integer UPDATED_NUMBER_OF_BATCHES = 2;
    private static final Integer SMALLER_NUMBER_OF_BATCHES = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_PROCESSED_BATCHES = 1;
    private static final Integer UPDATED_NUMBER_OF_PROCESSED_BATCHES = 2;
    private static final Integer SMALLER_NUMBER_OF_PROCESSED_BATCHES = 1 - 1;

    private static final Duration DEFAULT_PROCESSING_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_PROCESSING_TIME = Duration.ofHours(12);
    private static final Duration SMALLER_PROCESSING_TIME = Duration.ofHours(5);

    private static final String ENTITY_API_URL = "/api/nbv-compilation-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nbv-compilation-jobs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NbvCompilationJobRepository nbvCompilationJobRepository;

    @Autowired
    private NbvCompilationJobMapper nbvCompilationJobMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NbvCompilationJobSearchRepositoryMockConfiguration
     */
    @Autowired
    private NbvCompilationJobSearchRepository mockNbvCompilationJobSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNbvCompilationJobMockMvc;

    private NbvCompilationJob nbvCompilationJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationJob createEntity(EntityManager em) {
        NbvCompilationJob nbvCompilationJob = new NbvCompilationJob()
            .compilationJobIdentifier(DEFAULT_COMPILATION_JOB_IDENTIFIER)
            .compilationJobTimeOfRequest(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST)
            .entitiesAffected(DEFAULT_ENTITIES_AFFECTED)
            .compilationStatus(DEFAULT_COMPILATION_STATUS)
            .jobTitle(DEFAULT_JOB_TITLE)
            .numberOfBatches(DEFAULT_NUMBER_OF_BATCHES)
            .numberOfProcessedBatches(DEFAULT_NUMBER_OF_PROCESSED_BATCHES)
            .processingTime(DEFAULT_PROCESSING_TIME);
        return nbvCompilationJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationJob createUpdatedEntity(EntityManager em) {
        NbvCompilationJob nbvCompilationJob = new NbvCompilationJob()
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationJobTimeOfRequest(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST)
            .entitiesAffected(UPDATED_ENTITIES_AFFECTED)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .jobTitle(UPDATED_JOB_TITLE)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .numberOfProcessedBatches(UPDATED_NUMBER_OF_PROCESSED_BATCHES)
            .processingTime(UPDATED_PROCESSING_TIME);
        return nbvCompilationJob;
    }

    @BeforeEach
    public void initTest() {
        nbvCompilationJob = createEntity(em);
    }

    // @Test
    @Transactional
    void createNbvCompilationJob() throws Exception {
        int databaseSizeBeforeCreate = nbvCompilationJobRepository.findAll().size();
        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);
        restNbvCompilationJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeCreate + 1);
        NbvCompilationJob testNbvCompilationJob = nbvCompilationJobList.get(nbvCompilationJobList.size() - 1);
        assertThat(testNbvCompilationJob.getCompilationJobIdentifier()).isEqualTo(DEFAULT_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNbvCompilationJob.getCompilationJobTimeOfRequest()).isEqualTo(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
        assertThat(testNbvCompilationJob.getEntitiesAffected()).isEqualTo(DEFAULT_ENTITIES_AFFECTED);
        assertThat(testNbvCompilationJob.getCompilationStatus()).isEqualTo(DEFAULT_COMPILATION_STATUS);
        assertThat(testNbvCompilationJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testNbvCompilationJob.getNumberOfBatches()).isEqualTo(DEFAULT_NUMBER_OF_BATCHES);
        assertThat(testNbvCompilationJob.getNumberOfProcessedBatches()).isEqualTo(DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
        assertThat(testNbvCompilationJob.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(1)).save(testNbvCompilationJob);
    }

    @Test
    @Transactional
    void createNbvCompilationJobWithExistingId() throws Exception {
        // Create the NbvCompilationJob with an existing ID
        nbvCompilationJob.setId(1L);
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        int databaseSizeBeforeCreate = nbvCompilationJobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNbvCompilationJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeCreate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void checkCompilationJobIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = nbvCompilationJobRepository.findAll().size();
        // set the field null
        nbvCompilationJob.setCompilationJobIdentifier(null);

        // Create the NbvCompilationJob, which fails.
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        restNbvCompilationJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJobTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = nbvCompilationJobRepository.findAll().size();
        // set the field null
        nbvCompilationJob.setJobTitle(null);

        // Create the NbvCompilationJob, which fails.
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        restNbvCompilationJobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNbvCompilationJobs() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        // Get all the nbvCompilationJobList
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobTimeOfRequest").value(hasItem(sameInstant(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].entitiesAffected").value(hasItem(DEFAULT_ENTITIES_AFFECTED)))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].numberOfProcessedBatches").value(hasItem(DEFAULT_NUMBER_OF_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())));
    }

    @Test
    @Transactional
    void getNbvCompilationJob() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        // Get the nbvCompilationJob
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL_ID, nbvCompilationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nbvCompilationJob.getId().intValue()))
            .andExpect(jsonPath("$.compilationJobIdentifier").value(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationJobTimeOfRequest").value(sameInstant(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.entitiesAffected").value(DEFAULT_ENTITIES_AFFECTED))
            .andExpect(jsonPath("$.compilationStatus").value(DEFAULT_COMPILATION_STATUS.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.numberOfBatches").value(DEFAULT_NUMBER_OF_BATCHES))
            .andExpect(jsonPath("$.numberOfProcessedBatches").value(DEFAULT_NUMBER_OF_PROCESSED_BATCHES))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.toString()));
    }

    @Test
    @Transactional
    void getNbvCompilationJobsByIdFiltering() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        Long id = nbvCompilationJob.getId();

        defaultNbvCompilationJobShouldBeFound("id.equals=" + id);
        defaultNbvCompilationJobShouldNotBeFound("id.notEquals=" + id);

        defaultNbvCompilationJobShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNbvCompilationJobShouldNotBeFound("id.greaterThan=" + id);

        defaultNbvCompilationJobShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNbvCompilationJobShouldNotBeFound("id.lessThan=" + id);
    }

//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobIdentifierIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier equals to DEFAULT_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldBeFound("compilationJobIdentifier.equals=" + DEFAULT_COMPILATION_JOB_IDENTIFIER);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier equals to UPDATED_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobIdentifier.equals=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
//    }

//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobIdentifierIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier not equals to DEFAULT_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobIdentifier.notEquals=" + DEFAULT_COMPILATION_JOB_IDENTIFIER);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier not equals to UPDATED_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldBeFound("compilationJobIdentifier.notEquals=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobIdentifierIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier in DEFAULT_COMPILATION_JOB_IDENTIFIER or UPDATED_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldBeFound(
//            "compilationJobIdentifier.in=" + DEFAULT_COMPILATION_JOB_IDENTIFIER + "," + UPDATED_COMPILATION_JOB_IDENTIFIER
//        );
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier equals to UPDATED_COMPILATION_JOB_IDENTIFIER
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobIdentifier.in=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobIdentifierIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier is not null
//        defaultNbvCompilationJobShouldBeFound("compilationJobIdentifier.specified=true");
//
//        // Get all the nbvCompilationJobList where compilationJobIdentifier is null
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobIdentifier.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest equals to DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.equals=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest equals to UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.equals=" + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest not equals to DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.notEquals=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest not equals to UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.notEquals=" + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest in DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST or UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound(
//            "compilationJobTimeOfRequest.in=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST + "," + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        );
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest equals to UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.in=" + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is not null
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.specified=true");
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is null
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.specified=false");
//    }

//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is greater than or equal to DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.greaterThanOrEqual=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is greater than or equal to UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound(
//            "compilationJobTimeOfRequest.greaterThanOrEqual=" + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        );
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is less than or equal to DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.lessThanOrEqual=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is less than or equal to SMALLER_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.lessThanOrEqual=" + SMALLER_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsLessThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is less than DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.lessThan=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is less than UPDATED_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.lessThan=" + UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationJobTimeOfRequestIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is greater than DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldNotBeFound("compilationJobTimeOfRequest.greaterThan=" + DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST);
//
//        // Get all the nbvCompilationJobList where compilationJobTimeOfRequest is greater than SMALLER_COMPILATION_JOB_TIME_OF_REQUEST
//        defaultNbvCompilationJobShouldBeFound("compilationJobTimeOfRequest.greaterThan=" + SMALLER_COMPILATION_JOB_TIME_OF_REQUEST);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected equals to DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.equals=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected equals to UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.equals=" + UPDATED_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected not equals to DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.notEquals=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected not equals to UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.notEquals=" + UPDATED_ENTITIES_AFFECTED);
//    }

//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected in DEFAULT_ENTITIES_AFFECTED or UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.in=" + DEFAULT_ENTITIES_AFFECTED + "," + UPDATED_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected equals to UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.in=" + UPDATED_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is not null
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.specified=true");
//
//        // Get all the nbvCompilationJobList where entitiesAffected is null
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is greater than or equal to DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.greaterThanOrEqual=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is greater than or equal to UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.greaterThanOrEqual=" + UPDATED_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is less than or equal to DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.lessThanOrEqual=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is less than or equal to SMALLER_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.lessThanOrEqual=" + SMALLER_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsLessThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is less than DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.lessThan=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is less than UPDATED_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.lessThan=" + UPDATED_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByEntitiesAffectedIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is greater than DEFAULT_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldNotBeFound("entitiesAffected.greaterThan=" + DEFAULT_ENTITIES_AFFECTED);
//
//        // Get all the nbvCompilationJobList where entitiesAffected is greater than SMALLER_ENTITIES_AFFECTED
//        defaultNbvCompilationJobShouldBeFound("entitiesAffected.greaterThan=" + SMALLER_ENTITIES_AFFECTED);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationStatusIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationStatus equals to DEFAULT_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldBeFound("compilationStatus.equals=" + DEFAULT_COMPILATION_STATUS);
//
//        // Get all the nbvCompilationJobList where compilationStatus equals to UPDATED_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldNotBeFound("compilationStatus.equals=" + UPDATED_COMPILATION_STATUS);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationStatusIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationStatus not equals to DEFAULT_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldNotBeFound("compilationStatus.notEquals=" + DEFAULT_COMPILATION_STATUS);
//
//        // Get all the nbvCompilationJobList where compilationStatus not equals to UPDATED_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldBeFound("compilationStatus.notEquals=" + UPDATED_COMPILATION_STATUS);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationStatusIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationStatus in DEFAULT_COMPILATION_STATUS or UPDATED_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldBeFound("compilationStatus.in=" + DEFAULT_COMPILATION_STATUS + "," + UPDATED_COMPILATION_STATUS);
//
//        // Get all the nbvCompilationJobList where compilationStatus equals to UPDATED_COMPILATION_STATUS
//        defaultNbvCompilationJobShouldNotBeFound("compilationStatus.in=" + UPDATED_COMPILATION_STATUS);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByCompilationStatusIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where compilationStatus is not null
//        defaultNbvCompilationJobShouldBeFound("compilationStatus.specified=true");
//
//        // Get all the nbvCompilationJobList where compilationStatus is null
//        defaultNbvCompilationJobShouldNotBeFound("compilationStatus.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle equals to DEFAULT_JOB_TITLE
//        defaultNbvCompilationJobShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);
//
//        // Get all the nbvCompilationJobList where jobTitle equals to UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle not equals to DEFAULT_JOB_TITLE
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.notEquals=" + DEFAULT_JOB_TITLE);
//
//        // Get all the nbvCompilationJobList where jobTitle not equals to UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldBeFound("jobTitle.notEquals=" + UPDATED_JOB_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);
//
//        // Get all the nbvCompilationJobList where jobTitle equals to UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle is not null
//        defaultNbvCompilationJobShouldBeFound("jobTitle.specified=true");
//
//        // Get all the nbvCompilationJobList where jobTitle is null
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleContainsSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle contains DEFAULT_JOB_TITLE
//        defaultNbvCompilationJobShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);
//
//        // Get all the nbvCompilationJobList where jobTitle contains UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByJobTitleNotContainsSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where jobTitle does not contain DEFAULT_JOB_TITLE
//        defaultNbvCompilationJobShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);
//
//        // Get all the nbvCompilationJobList where jobTitle does not contain UPDATED_JOB_TITLE
//        defaultNbvCompilationJobShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches equals to DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.equals=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches equals to UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.equals=" + UPDATED_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches not equals to DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.notEquals=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches not equals to UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.notEquals=" + UPDATED_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches in DEFAULT_NUMBER_OF_BATCHES or UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.in=" + DEFAULT_NUMBER_OF_BATCHES + "," + UPDATED_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches equals to UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.in=" + UPDATED_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is not null
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.specified=true");
//
//        // Get all the nbvCompilationJobList where numberOfBatches is null
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is greater than or equal to DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is greater than or equal to UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.greaterThanOrEqual=" + UPDATED_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is less than or equal to DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.lessThanOrEqual=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is less than or equal to SMALLER_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.lessThanOrEqual=" + SMALLER_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsLessThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is less than DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.lessThan=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is less than UPDATED_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.lessThan=" + UPDATED_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfBatchesIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is greater than DEFAULT_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfBatches.greaterThan=" + DEFAULT_NUMBER_OF_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfBatches is greater than SMALLER_NUMBER_OF_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfBatches.greaterThan=" + SMALLER_NUMBER_OF_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches equals to DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.equals=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches equals to UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.equals=" + UPDATED_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches not equals to DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.notEquals=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches not equals to UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.notEquals=" + UPDATED_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches in DEFAULT_NUMBER_OF_PROCESSED_BATCHES or UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound(
//            "numberOfProcessedBatches.in=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES + "," + UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        );
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches equals to UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.in=" + UPDATED_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is not null
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.specified=true");
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is null
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is greater than or equal to DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is greater than or equal to UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is less than or equal to DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is less than or equal to SMALLER_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.lessThanOrEqual=" + SMALLER_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsLessThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is less than DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.lessThan=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is less than UPDATED_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.lessThan=" + UPDATED_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByNumberOfProcessedBatchesIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is greater than DEFAULT_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldNotBeFound("numberOfProcessedBatches.greaterThan=" + DEFAULT_NUMBER_OF_PROCESSED_BATCHES);
//
//        // Get all the nbvCompilationJobList where numberOfProcessedBatches is greater than SMALLER_NUMBER_OF_PROCESSED_BATCHES
//        defaultNbvCompilationJobShouldBeFound("numberOfProcessedBatches.greaterThan=" + SMALLER_NUMBER_OF_PROCESSED_BATCHES);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime equals to DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.equals=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime equals to UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.equals=" + UPDATED_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsNotEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime not equals to DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.notEquals=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime not equals to UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.notEquals=" + UPDATED_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsInShouldWork() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime in DEFAULT_PROCESSING_TIME or UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime equals to UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.in=" + UPDATED_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsNullOrNotNull() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime is not null
//        defaultNbvCompilationJobShouldBeFound("processingTime.specified=true");
//
//        // Get all the nbvCompilationJobList where processingTime is null
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.specified=false");
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime is greater than or equal to DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime is greater than or equal to UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime is less than or equal to DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime is less than or equal to SMALLER_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsLessThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime is less than DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime is less than UPDATED_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.lessThan=" + UPDATED_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByProcessingTimeIsGreaterThanSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//
//        // Get all the nbvCompilationJobList where processingTime is greater than DEFAULT_PROCESSING_TIME
//        defaultNbvCompilationJobShouldNotBeFound("processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME);
//
//        // Get all the nbvCompilationJobList where processingTime is greater than SMALLER_PROCESSING_TIME
//        defaultNbvCompilationJobShouldBeFound("processingTime.greaterThan=" + SMALLER_PROCESSING_TIME);
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByActivePeriodIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//        DepreciationPeriod activePeriod;
//        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
//            activePeriod = DepreciationPeriodResourceIT.createEntity(em);
//            em.persist(activePeriod);
//            em.flush();
//        } else {
//            activePeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
//        }
//        em.persist(activePeriod);
//        em.flush();
//        nbvCompilationJob.setActivePeriod(activePeriod);
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//        Long activePeriodId = activePeriod.getId();
//
//        // Get all the nbvCompilationJobList where activePeriod equals to activePeriodId
//        defaultNbvCompilationJobShouldBeFound("activePeriodId.equals=" + activePeriodId);
//
//        // Get all the nbvCompilationJobList where activePeriod equals to (activePeriodId + 1)
//        defaultNbvCompilationJobShouldNotBeFound("activePeriodId.equals=" + (activePeriodId + 1));
//    }
//
//    @Test
//    @Transactional
//    void getAllNbvCompilationJobsByInitiatedByIsEqualToSomething() throws Exception {
//        // Initialize the database
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//        ApplicationUser initiatedBy;
//        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
//            initiatedBy = ApplicationUserResourceIT.createEntity(em);
//            em.persist(initiatedBy);
//            em.flush();
//        } else {
//            initiatedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
//        }
//        em.persist(initiatedBy);
//        em.flush();
//        nbvCompilationJob.setInitiatedBy(initiatedBy);
//        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
//        Long initiatedById = initiatedBy.getId();
//
//        // Get all the nbvCompilationJobList where initiatedBy equals to initiatedById
//        defaultNbvCompilationJobShouldBeFound("initiatedById.equals=" + initiatedById);
//
//        // Get all the nbvCompilationJobList where initiatedBy equals to (initiatedById + 1)
//        defaultNbvCompilationJobShouldNotBeFound("initiatedById.equals=" + (initiatedById + 1));
//    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNbvCompilationJobShouldBeFound(String filter) throws Exception {
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobTimeOfRequest").value(hasItem(sameInstant(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].entitiesAffected").value(hasItem(DEFAULT_ENTITIES_AFFECTED)))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].numberOfProcessedBatches").value(hasItem(DEFAULT_NUMBER_OF_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())));

        // Check, that the count call also returns 1
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNbvCompilationJobShouldNotBeFound(String filter) throws Exception {
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNbvCompilationJob() throws Exception {
        // Get the nbvCompilationJob
        restNbvCompilationJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNbvCompilationJob() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();

        // Update the nbvCompilationJob
        NbvCompilationJob updatedNbvCompilationJob = nbvCompilationJobRepository.findById(nbvCompilationJob.getId()).get();
        // Disconnect from session so that the updates on updatedNbvCompilationJob are not directly saved in db
        em.detach(updatedNbvCompilationJob);
        updatedNbvCompilationJob
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationJobTimeOfRequest(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST)
            .entitiesAffected(UPDATED_ENTITIES_AFFECTED)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .jobTitle(UPDATED_JOB_TITLE)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .numberOfProcessedBatches(UPDATED_NUMBER_OF_PROCESSED_BATCHES)
            .processingTime(UPDATED_PROCESSING_TIME);
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(updatedNbvCompilationJob);

        restNbvCompilationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvCompilationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationJob testNbvCompilationJob = nbvCompilationJobList.get(nbvCompilationJobList.size() - 1);
        assertThat(testNbvCompilationJob.getCompilationJobIdentifier()).isEqualTo(UPDATED_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNbvCompilationJob.getCompilationJobTimeOfRequest()).isEqualTo(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
        assertThat(testNbvCompilationJob.getEntitiesAffected()).isEqualTo(UPDATED_ENTITIES_AFFECTED);
        assertThat(testNbvCompilationJob.getCompilationStatus()).isEqualTo(UPDATED_COMPILATION_STATUS);
        assertThat(testNbvCompilationJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testNbvCompilationJob.getNumberOfBatches()).isEqualTo(UPDATED_NUMBER_OF_BATCHES);
        assertThat(testNbvCompilationJob.getNumberOfProcessedBatches()).isEqualTo(UPDATED_NUMBER_OF_PROCESSED_BATCHES);
        assertThat(testNbvCompilationJob.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository).save(testNbvCompilationJob);
    }

    @Test
    @Transactional
    void putNonExistingNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvCompilationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void putWithIdMismatchNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void partialUpdateNbvCompilationJobWithPatch() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();

        // Update the nbvCompilationJob using partial update
        NbvCompilationJob partialUpdatedNbvCompilationJob = new NbvCompilationJob();
        partialUpdatedNbvCompilationJob.setId(nbvCompilationJob.getId());

        partialUpdatedNbvCompilationJob
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationJobTimeOfRequest(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST)
            .entitiesAffected(UPDATED_ENTITIES_AFFECTED)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .numberOfProcessedBatches(UPDATED_NUMBER_OF_PROCESSED_BATCHES)
            .processingTime(UPDATED_PROCESSING_TIME);

        restNbvCompilationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvCompilationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvCompilationJob))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationJob testNbvCompilationJob = nbvCompilationJobList.get(nbvCompilationJobList.size() - 1);
        assertThat(testNbvCompilationJob.getCompilationJobIdentifier()).isEqualTo(UPDATED_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNbvCompilationJob.getCompilationJobTimeOfRequest()).isEqualTo(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
        assertThat(testNbvCompilationJob.getEntitiesAffected()).isEqualTo(UPDATED_ENTITIES_AFFECTED);
        assertThat(testNbvCompilationJob.getCompilationStatus()).isEqualTo(DEFAULT_COMPILATION_STATUS);
        assertThat(testNbvCompilationJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testNbvCompilationJob.getNumberOfBatches()).isEqualTo(UPDATED_NUMBER_OF_BATCHES);
        assertThat(testNbvCompilationJob.getNumberOfProcessedBatches()).isEqualTo(UPDATED_NUMBER_OF_PROCESSED_BATCHES);
        assertThat(testNbvCompilationJob.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void fullUpdateNbvCompilationJobWithPatch() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();

        // Update the nbvCompilationJob using partial update
        NbvCompilationJob partialUpdatedNbvCompilationJob = new NbvCompilationJob();
        partialUpdatedNbvCompilationJob.setId(nbvCompilationJob.getId());

        partialUpdatedNbvCompilationJob
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationJobTimeOfRequest(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST)
            .entitiesAffected(UPDATED_ENTITIES_AFFECTED)
            .compilationStatus(UPDATED_COMPILATION_STATUS)
            .jobTitle(UPDATED_JOB_TITLE)
            .numberOfBatches(UPDATED_NUMBER_OF_BATCHES)
            .numberOfProcessedBatches(UPDATED_NUMBER_OF_PROCESSED_BATCHES)
            .processingTime(UPDATED_PROCESSING_TIME);

        restNbvCompilationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvCompilationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvCompilationJob))
            )
            .andExpect(status().isOk());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);
        NbvCompilationJob testNbvCompilationJob = nbvCompilationJobList.get(nbvCompilationJobList.size() - 1);
        assertThat(testNbvCompilationJob.getCompilationJobIdentifier()).isEqualTo(UPDATED_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNbvCompilationJob.getCompilationJobTimeOfRequest()).isEqualTo(UPDATED_COMPILATION_JOB_TIME_OF_REQUEST);
        assertThat(testNbvCompilationJob.getEntitiesAffected()).isEqualTo(UPDATED_ENTITIES_AFFECTED);
        assertThat(testNbvCompilationJob.getCompilationStatus()).isEqualTo(UPDATED_COMPILATION_STATUS);
        assertThat(testNbvCompilationJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testNbvCompilationJob.getNumberOfBatches()).isEqualTo(UPDATED_NUMBER_OF_BATCHES);
        assertThat(testNbvCompilationJob.getNumberOfProcessedBatches()).isEqualTo(UPDATED_NUMBER_OF_PROCESSED_BATCHES);
        assertThat(testNbvCompilationJob.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nbvCompilationJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNbvCompilationJob() throws Exception {
        int databaseSizeBeforeUpdate = nbvCompilationJobRepository.findAll().size();
        nbvCompilationJob.setId(count.incrementAndGet());

        // Create the NbvCompilationJob
        NbvCompilationJobDTO nbvCompilationJobDTO = nbvCompilationJobMapper.toDto(nbvCompilationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvCompilationJobMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvCompilationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvCompilationJob in the database
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(0)).save(nbvCompilationJob);
    }

    @Test
    @Transactional
    void deleteNbvCompilationJob() throws Exception {
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);

        int databaseSizeBeforeDelete = nbvCompilationJobRepository.findAll().size();

        // Delete the nbvCompilationJob
        restNbvCompilationJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, nbvCompilationJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NbvCompilationJob> nbvCompilationJobList = nbvCompilationJobRepository.findAll();
        assertThat(nbvCompilationJobList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NbvCompilationJob in Elasticsearch
        verify(mockNbvCompilationJobSearchRepository, times(1)).deleteById(nbvCompilationJob.getId());
    }

    @Test
    @Transactional
    void searchNbvCompilationJob() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nbvCompilationJobRepository.saveAndFlush(nbvCompilationJob);
        when(mockNbvCompilationJobSearchRepository.search("id:" + nbvCompilationJob.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nbvCompilationJob), PageRequest.of(0, 1), 1));

        // Search the nbvCompilationJob
        restNbvCompilationJobMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nbvCompilationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobTimeOfRequest").value(hasItem(sameInstant(DEFAULT_COMPILATION_JOB_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].entitiesAffected").value(hasItem(DEFAULT_ENTITIES_AFFECTED)))
            .andExpect(jsonPath("$.[*].compilationStatus").value(hasItem(DEFAULT_COMPILATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].numberOfBatches").value(hasItem(DEFAULT_NUMBER_OF_BATCHES)))
            .andExpect(jsonPath("$.[*].numberOfProcessedBatches").value(hasItem(DEFAULT_NUMBER_OF_PROCESSED_BATCHES)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.toString())));
    }
}
