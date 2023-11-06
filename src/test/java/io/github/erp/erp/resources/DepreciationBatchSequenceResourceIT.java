package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.domain.DepreciationJob;
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.search.DepreciationBatchSequenceSearchRepository;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.mapper.DepreciationBatchSequenceMapper;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the DepreciationBatchSequenceResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class DepreciationBatchSequenceResourceIT {

    private static final Integer DEFAULT_START_INDEX = 1;
    private static final Integer UPDATED_START_INDEX = 2;
    private static final Integer SMALLER_START_INDEX = 1 - 1;

    private static final Integer DEFAULT_END_INDEX = 1;
    private static final Integer UPDATED_END_INDEX = 2;
    private static final Integer SMALLER_END_INDEX = 1 - 1;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final DepreciationBatchStatusType DEFAULT_DEPRECIATION_BATCH_STATUS = DepreciationBatchStatusType.CREATED;
    private static final DepreciationBatchStatusType UPDATED_DEPRECIATION_BATCH_STATUS = DepreciationBatchStatusType.RUNNING;

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-batch-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-batch-sequences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;

    @Autowired
    private DepreciationBatchSequenceMapper depreciationBatchSequenceMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationBatchSequenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationBatchSequenceSearchRepository mockDepreciationBatchSequenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationBatchSequenceMockMvc;

    private DepreciationBatchSequence depreciationBatchSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationBatchSequence createEntity(EntityManager em) {
        DepreciationBatchSequence depreciationBatchSequence = new DepreciationBatchSequence()
            .startIndex(DEFAULT_START_INDEX)
            .endIndex(DEFAULT_END_INDEX)
            .createdAt(DEFAULT_CREATED_AT)
            .depreciationBatchStatus(DEFAULT_DEPRECIATION_BATCH_STATUS);
        return depreciationBatchSequence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationBatchSequence createUpdatedEntity(EntityManager em) {
        DepreciationBatchSequence depreciationBatchSequence = new DepreciationBatchSequence()
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .createdAt(UPDATED_CREATED_AT)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS);
        return depreciationBatchSequence;
    }

    @BeforeEach
    public void initTest() {
        depreciationBatchSequence = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeCreate = depreciationBatchSequenceRepository.findAll().size();
        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);
        restDepreciationBatchSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(DEFAULT_END_INDEX);
        assertThat(testDepreciationBatchSequence.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(DEFAULT_DEPRECIATION_BATCH_STATUS);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(1)).save(testDepreciationBatchSequence);
    }

    @Test
    @Transactional
    void createDepreciationBatchSequenceWithExistingId() throws Exception {
        // Create the DepreciationBatchSequence with an existing ID
        depreciationBatchSequence.setId(1L);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        int databaseSizeBeforeCreate = depreciationBatchSequenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationBatchSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequences() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationBatchSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].depreciationBatchStatus").value(hasItem(DEFAULT_DEPRECIATION_BATCH_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationBatchSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationBatchSequence.getId().intValue()))
            .andExpect(jsonPath("$.startIndex").value(DEFAULT_START_INDEX))
            .andExpect(jsonPath("$.endIndex").value(DEFAULT_END_INDEX))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.depreciationBatchStatus").value(DEFAULT_DEPRECIATION_BATCH_STATUS.toString()));
    }

    @Test
    @Transactional
    void getDepreciationBatchSequencesByIdFiltering() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        Long id = depreciationBatchSequence.getId();

        defaultDepreciationBatchSequenceShouldBeFound("id.equals=" + id);
        defaultDepreciationBatchSequenceShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationBatchSequenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationBatchSequenceShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationBatchSequenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationBatchSequenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex equals to DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.equals=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex equals to UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.equals=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex not equals to DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.notEquals=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex not equals to UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.notEquals=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex in DEFAULT_START_INDEX or UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.in=" + DEFAULT_START_INDEX + "," + UPDATED_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex equals to UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.in=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex is not null
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.specified=true");

        // Get all the depreciationBatchSequenceList where startIndex is null
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex is greater than or equal to DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.greaterThanOrEqual=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex is greater than or equal to UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.greaterThanOrEqual=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex is less than or equal to DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.lessThanOrEqual=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex is less than or equal to SMALLER_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.lessThanOrEqual=" + SMALLER_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex is less than DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.lessThan=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex is less than UPDATED_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.lessThan=" + UPDATED_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByStartIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where startIndex is greater than DEFAULT_START_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("startIndex.greaterThan=" + DEFAULT_START_INDEX);

        // Get all the depreciationBatchSequenceList where startIndex is greater than SMALLER_START_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("startIndex.greaterThan=" + SMALLER_START_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex equals to DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.equals=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex equals to UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.equals=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex not equals to DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.notEquals=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex not equals to UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.notEquals=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex in DEFAULT_END_INDEX or UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.in=" + DEFAULT_END_INDEX + "," + UPDATED_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex equals to UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.in=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex is not null
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.specified=true");

        // Get all the depreciationBatchSequenceList where endIndex is null
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex is greater than or equal to DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.greaterThanOrEqual=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex is greater than or equal to UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.greaterThanOrEqual=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex is less than or equal to DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.lessThanOrEqual=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex is less than or equal to SMALLER_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.lessThanOrEqual=" + SMALLER_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex is less than DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.lessThan=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex is less than UPDATED_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.lessThan=" + UPDATED_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByEndIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where endIndex is greater than DEFAULT_END_INDEX
        defaultDepreciationBatchSequenceShouldNotBeFound("endIndex.greaterThan=" + DEFAULT_END_INDEX);

        // Get all the depreciationBatchSequenceList where endIndex is greater than SMALLER_END_INDEX
        defaultDepreciationBatchSequenceShouldBeFound("endIndex.greaterThan=" + SMALLER_END_INDEX);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt equals to DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt equals to UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt not equals to DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt not equals to UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt equals to UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt is not null
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.specified=true");

        // Get all the depreciationBatchSequenceList where createdAt is null
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt is less than DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt is less than UPDATED_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where createdAt is greater than DEFAULT_CREATED_AT
        defaultDepreciationBatchSequenceShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the depreciationBatchSequenceList where createdAt is greater than SMALLER_CREATED_AT
        defaultDepreciationBatchSequenceShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByDepreciationBatchStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus equals to DEFAULT_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldBeFound("depreciationBatchStatus.equals=" + DEFAULT_DEPRECIATION_BATCH_STATUS);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus equals to UPDATED_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldNotBeFound("depreciationBatchStatus.equals=" + UPDATED_DEPRECIATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByDepreciationBatchStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus not equals to DEFAULT_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldNotBeFound("depreciationBatchStatus.notEquals=" + DEFAULT_DEPRECIATION_BATCH_STATUS);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus not equals to UPDATED_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldBeFound("depreciationBatchStatus.notEquals=" + UPDATED_DEPRECIATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByDepreciationBatchStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus in DEFAULT_DEPRECIATION_BATCH_STATUS or UPDATED_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldBeFound(
            "depreciationBatchStatus.in=" + DEFAULT_DEPRECIATION_BATCH_STATUS + "," + UPDATED_DEPRECIATION_BATCH_STATUS
        );

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus equals to UPDATED_DEPRECIATION_BATCH_STATUS
        defaultDepreciationBatchSequenceShouldNotBeFound("depreciationBatchStatus.in=" + UPDATED_DEPRECIATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByDepreciationBatchStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus is not null
        defaultDepreciationBatchSequenceShouldBeFound("depreciationBatchStatus.specified=true");

        // Get all the depreciationBatchSequenceList where depreciationBatchStatus is null
        defaultDepreciationBatchSequenceShouldNotBeFound("depreciationBatchStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequencesByDepreciationJobIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);
        DepreciationJob depreciationJob;
        if (TestUtil.findAll(em, DepreciationJob.class).isEmpty()) {
            depreciationJob = DepreciationJobResourceIT.createEntity(em);
            em.persist(depreciationJob);
            em.flush();
        } else {
            depreciationJob = TestUtil.findAll(em, DepreciationJob.class).get(0);
        }
        em.persist(depreciationJob);
        em.flush();
        depreciationBatchSequence.setDepreciationJob(depreciationJob);
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);
        Long depreciationJobId = depreciationJob.getId();

        // Get all the depreciationBatchSequenceList where depreciationJob equals to depreciationJobId
        defaultDepreciationBatchSequenceShouldBeFound("depreciationJobId.equals=" + depreciationJobId);

        // Get all the depreciationBatchSequenceList where depreciationJob equals to (depreciationJobId + 1)
        defaultDepreciationBatchSequenceShouldNotBeFound("depreciationJobId.equals=" + (depreciationJobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationBatchSequenceShouldBeFound(String filter) throws Exception {
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationBatchSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].depreciationBatchStatus").value(hasItem(DEFAULT_DEPRECIATION_BATCH_STATUS.toString())));

        // Check, that the count call also returns 1
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationBatchSequenceShouldNotBeFound(String filter) throws Exception {
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationBatchSequence() throws Exception {
        // Get the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence
        DepreciationBatchSequence updatedDepreciationBatchSequence = depreciationBatchSequenceRepository
            .findById(depreciationBatchSequence.getId())
            .get();
        // Disconnect from session so that the updates on updatedDepreciationBatchSequence are not directly saved in db
        em.detach(updatedDepreciationBatchSequence);
        updatedDepreciationBatchSequence
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .createdAt(UPDATED_CREATED_AT)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(updatedDepreciationBatchSequence);

        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testDepreciationBatchSequence.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(UPDATED_DEPRECIATION_BATCH_STATUS);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository).save(testDepreciationBatchSequence);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationBatchSequenceWithPatch() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence using partial update
        DepreciationBatchSequence partialUpdatedDepreciationBatchSequence = new DepreciationBatchSequence();
        partialUpdatedDepreciationBatchSequence.setId(depreciationBatchSequence.getId());

        partialUpdatedDepreciationBatchSequence.depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS);

        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationBatchSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationBatchSequence))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(DEFAULT_END_INDEX);
        assertThat(testDepreciationBatchSequence.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(UPDATED_DEPRECIATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationBatchSequenceWithPatch() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence using partial update
        DepreciationBatchSequence partialUpdatedDepreciationBatchSequence = new DepreciationBatchSequence();
        partialUpdatedDepreciationBatchSequence.setId(depreciationBatchSequence.getId());

        partialUpdatedDepreciationBatchSequence
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .createdAt(UPDATED_CREATED_AT)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS);

        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationBatchSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationBatchSequence))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testDepreciationBatchSequence.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(UPDATED_DEPRECIATION_BATCH_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void deleteDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeDelete = depreciationBatchSequenceRepository.findAll().size();

        // Delete the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationBatchSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(1)).deleteById(depreciationBatchSequence.getId());
    }

    @Test
    @Transactional
    void searchDepreciationBatchSequence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);
        when(mockDepreciationBatchSequenceSearchRepository.search("id:" + depreciationBatchSequence.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationBatchSequence), PageRequest.of(0, 1), 1));

        // Search the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationBatchSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationBatchSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].depreciationBatchStatus").value(hasItem(DEFAULT_DEPRECIATION_BATCH_STATUS.toString())));
    }
}
