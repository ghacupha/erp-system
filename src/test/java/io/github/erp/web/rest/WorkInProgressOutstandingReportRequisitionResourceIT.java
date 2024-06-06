package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.WorkInProgressOutstandingReportRequisition;
import io.github.erp.repository.WorkInProgressOutstandingReportRequisitionRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepository;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportRequisitionCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportRequisitionMapper;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link WorkInProgressOutstandingReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkInProgressOutstandingReportRequisitionResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUISITION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TAMPERED = false;
    private static final Boolean UPDATED_TAMPERED = true;

    private static final UUID DEFAULT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_FILENAME = UUID.randomUUID();

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/work-in-progress-outstanding-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-outstanding-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository;

    @Autowired
    private WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressOutstandingReportRequisitionSearchRepository mockWorkInProgressOutstandingReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressOutstandingReportRequisitionMockMvc;

    private WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOutstandingReportRequisition createEntity(EntityManager em) {
        WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition = new WorkInProgressOutstandingReportRequisition()
            .requestId(DEFAULT_REQUEST_ID)
            .reportDate(DEFAULT_REPORT_DATE)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return workInProgressOutstandingReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOutstandingReportRequisition createUpdatedEntity(EntityManager em) {
        WorkInProgressOutstandingReportRequisition workInProgressOutstandingReportRequisition = new WorkInProgressOutstandingReportRequisition()
            .requestId(UPDATED_REQUEST_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return workInProgressOutstandingReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        workInProgressOutstandingReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        WorkInProgressOutstandingReportRequisition testWorkInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionList.get(
            workInProgressOutstandingReportRequisitionList.size() - 1
        );
        assertThat(testWorkInProgressOutstandingReportRequisition.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(1))
            .save(testWorkInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void createWorkInProgressOutstandingReportRequisitionWithExistingId() throws Exception {
        // Create the WorkInProgressOutstandingReportRequisition with an existing ID
        workInProgressOutstandingReportRequisition.setId(1L);
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        int databaseSizeBeforeCreate = workInProgressOutstandingReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        // set the field null
        workInProgressOutstandingReportRequisition.setRequestId(null);

        // Create the WorkInProgressOutstandingReportRequisition, which fails.
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        // set the field null
        workInProgressOutstandingReportRequisition.setReportDate(null);

        // Create the WorkInProgressOutstandingReportRequisition, which fails.
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        // set the field null
        workInProgressOutstandingReportRequisition.setTimeOfRequisition(null);

        // Create the WorkInProgressOutstandingReportRequisition, which fails.
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitions() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getWorkInProgressOutstandingReportRequisition() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get the workInProgressOutstandingReportRequisition
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressOutstandingReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressOutstandingReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.timeOfRequisition").value(sameInstant(DEFAULT_TIME_OF_REQUISITION)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getWorkInProgressOutstandingReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        Long id = workInProgressOutstandingReportRequisition.getId();

        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("id.equals=" + id);
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId equals to DEFAULT_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId not equals to DEFAULT_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId not equals to UPDATED_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where requestId is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("requestId.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where requestId is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the workInProgressOutstandingReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound(
            "timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION
        );

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound(
            "timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION
        );
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound(
            "timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION
        );
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the workInProgressOutstandingReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound(
            "fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM
        );

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the workInProgressOutstandingReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where tampered is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where tampered is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the workInProgressOutstandingReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the workInProgressOutstandingReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the workInProgressOutstandingReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where filename is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where filename is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters is not null
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters is null
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the workInProgressOutstandingReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);
        ApplicationUser requestedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            requestedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(requestedBy);
            em.flush();
        } else {
            requestedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(requestedBy);
        em.flush();
        workInProgressOutstandingReportRequisition.setRequestedBy(requestedBy);
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the workInProgressOutstandingReportRequisitionList where requestedBy equals to requestedById
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the workInProgressOutstandingReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);
        ApplicationUser lastAccessedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastAccessedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastAccessedBy);
            em.flush();
        } else {
            lastAccessedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastAccessedBy);
        em.flush();
        workInProgressOutstandingReportRequisition.setLastAccessedBy(lastAccessedBy);
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the workInProgressOutstandingReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultWorkInProgressOutstandingReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the workInProgressOutstandingReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkInProgressOutstandingReportRequisitionShouldBeFound(String filter) throws Exception {
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkInProgressOutstandingReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressOutstandingReportRequisition() throws Exception {
        // Get the workInProgressOutstandingReportRequisition
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkInProgressOutstandingReportRequisition() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();

        // Update the workInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisition updatedWorkInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionRepository
            .findById(workInProgressOutstandingReportRequisition.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkInProgressOutstandingReportRequisition are not directly saved in db
        em.detach(updatedWorkInProgressOutstandingReportRequisition);
        updatedWorkInProgressOutstandingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            updatedWorkInProgressOutstandingReportRequisition
        );

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressOutstandingReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressOutstandingReportRequisition testWorkInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionList.get(
            workInProgressOutstandingReportRequisitionList.size() - 1
        );
        assertThat(testWorkInProgressOutstandingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository).save(testWorkInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressOutstandingReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdateWorkInProgressOutstandingReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();

        // Update the workInProgressOutstandingReportRequisition using partial update
        WorkInProgressOutstandingReportRequisition partialUpdatedWorkInProgressOutstandingReportRequisition = new WorkInProgressOutstandingReportRequisition();
        partialUpdatedWorkInProgressOutstandingReportRequisition.setId(workInProgressOutstandingReportRequisition.getId());

        partialUpdatedWorkInProgressOutstandingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressOutstandingReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressOutstandingReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressOutstandingReportRequisition testWorkInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionList.get(
            workInProgressOutstandingReportRequisitionList.size() - 1
        );
        assertThat(testWorkInProgressOutstandingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkInProgressOutstandingReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();

        // Update the workInProgressOutstandingReportRequisition using partial update
        WorkInProgressOutstandingReportRequisition partialUpdatedWorkInProgressOutstandingReportRequisition = new WorkInProgressOutstandingReportRequisition();
        partialUpdatedWorkInProgressOutstandingReportRequisition.setId(workInProgressOutstandingReportRequisition.getId());

        partialUpdatedWorkInProgressOutstandingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressOutstandingReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressOutstandingReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressOutstandingReportRequisition testWorkInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisitionList.get(
            workInProgressOutstandingReportRequisitionList.size() - 1
        );
        assertThat(testWorkInProgressOutstandingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWorkInProgressOutstandingReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWorkInProgressOutstandingReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWorkInProgressOutstandingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workInProgressOutstandingReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkInProgressOutstandingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressOutstandingReportRequisitionRepository.findAll().size();
        workInProgressOutstandingReportRequisition.setId(count.incrementAndGet());

        // Create the WorkInProgressOutstandingReportRequisition
        WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressOutstandingReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressOutstandingReportRequisition in the database
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(0)).save(workInProgressOutstandingReportRequisition);
    }

    @Test
    @Transactional
    void deleteWorkInProgressOutstandingReportRequisition() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);

        int databaseSizeBeforeDelete = workInProgressOutstandingReportRequisitionRepository.findAll().size();

        // Delete the workInProgressOutstandingReportRequisition
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, workInProgressOutstandingReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkInProgressOutstandingReportRequisition> workInProgressOutstandingReportRequisitionList = workInProgressOutstandingReportRequisitionRepository.findAll();
        assertThat(workInProgressOutstandingReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkInProgressOutstandingReportRequisition in Elasticsearch
        verify(mockWorkInProgressOutstandingReportRequisitionSearchRepository, times(1))
            .deleteById(workInProgressOutstandingReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchWorkInProgressOutstandingReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressOutstandingReportRequisitionRepository.saveAndFlush(workInProgressOutstandingReportRequisition);
        when(
            mockWorkInProgressOutstandingReportRequisitionSearchRepository.search(
                "id:" + workInProgressOutstandingReportRequisition.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressOutstandingReportRequisition), PageRequest.of(0, 1), 1));

        // Search the workInProgressOutstandingReportRequisition
        restWorkInProgressOutstandingReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressOutstandingReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
