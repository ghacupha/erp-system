package io.github.erp.erp.resources;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.RouDepreciationEntryReport;
import io.github.erp.repository.RouDepreciationEntryReportRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportSearchRepository;
import io.github.erp.service.dto.RouDepreciationEntryReportDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportMapper;
import io.github.erp.web.rest.RouDepreciationEntryReportResource;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RouDepreciationEntryReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouDepreciationEntryReportResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_REPORT_IS_COMPILED = false;
    private static final Boolean UPDATED_REPORT_IS_COMPILED = true;

    private static final LocalDate DEFAULT_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

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

    private static final String ENTITY_API_URL = "/api/leases/rou-depreciation-entry-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-depreciation-entry-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository;

    @Autowired
    private RouDepreciationEntryReportMapper rouDepreciationEntryReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationEntryReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationEntryReportSearchRepository mockRouDepreciationEntryReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationEntryReportMockMvc;

    private RouDepreciationEntryReport rouDepreciationEntryReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntryReport createEntity(EntityManager em) {
        RouDepreciationEntryReport rouDepreciationEntryReport = new RouDepreciationEntryReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .reportIsCompiled(DEFAULT_REPORT_IS_COMPILED)
            .periodStartDate(DEFAULT_PERIOD_START_DATE)
            .periodEndDate(DEFAULT_PERIOD_END_DATE)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return rouDepreciationEntryReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntryReport createUpdatedEntity(EntityManager em) {
        RouDepreciationEntryReport rouDepreciationEntryReport = new RouDepreciationEntryReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return rouDepreciationEntryReport;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationEntryReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationEntryReportRepository.findAll().size();
        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);
        restRouDepreciationEntryReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationEntryReport testRouDepreciationEntryReport = rouDepreciationEntryReportList.get(
            rouDepreciationEntryReportList.size() - 1
        );
        assertThat(testRouDepreciationEntryReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouDepreciationEntryReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouDepreciationEntryReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationEntryReport.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testRouDepreciationEntryReport.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testRouDepreciationEntryReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouDepreciationEntryReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouDepreciationEntryReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouDepreciationEntryReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouDepreciationEntryReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouDepreciationEntryReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(1)).save(testRouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void createRouDepreciationEntryReportWithExistingId() throws Exception {
        // Create the RouDepreciationEntryReport with an existing ID
        rouDepreciationEntryReport.setId(1L);
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        int databaseSizeBeforeCreate = rouDepreciationEntryReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouDepreciationEntryReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryReportRepository.findAll().size();
        // set the field null
        rouDepreciationEntryReport.setRequestId(null);

        // Create the RouDepreciationEntryReport, which fails.
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        restRouDepreciationEntryReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReports() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getRouDepreciationEntryReport() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get the rouDepreciationEntryReport
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationEntryReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationEntryReport.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.reportIsCompiled").value(DEFAULT_REPORT_IS_COMPILED.booleanValue()))
            .andExpect(jsonPath("$.periodStartDate").value(DEFAULT_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.periodEndDate").value(DEFAULT_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getRouDepreciationEntryReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        Long id = rouDepreciationEntryReport.getId();

        defaultRouDepreciationEntryReportShouldBeFound("id.equals=" + id);
        defaultRouDepreciationEntryReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationEntryReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationEntryReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationEntryReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationEntryReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouDepreciationEntryReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouDepreciationEntryReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouDepreciationEntryReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouDepreciationEntryReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouDepreciationEntryReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouDepreciationEntryReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouDepreciationEntryReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouDepreciationEntryReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouDepreciationEntryReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where requestId is not null
        defaultRouDepreciationEntryReportShouldBeFound("requestId.specified=true");

        // Get all the rouDepreciationEntryReportList where requestId is null
        defaultRouDepreciationEntryReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is not null
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouDepreciationEntryReportList where timeOfRequest is null
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationEntryReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationEntryReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportIsCompiledIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldBeFound("reportIsCompiled.equals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldNotBeFound("reportIsCompiled.equals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportIsCompiledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled not equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldNotBeFound("reportIsCompiled.notEquals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled not equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldBeFound("reportIsCompiled.notEquals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportIsCompiledIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled in DEFAULT_REPORT_IS_COMPILED or UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldBeFound(
            "reportIsCompiled.in=" + DEFAULT_REPORT_IS_COMPILED + "," + UPDATED_REPORT_IS_COMPILED
        );

        // Get all the rouDepreciationEntryReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationEntryReportShouldNotBeFound("reportIsCompiled.in=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportIsCompiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportIsCompiled is not null
        defaultRouDepreciationEntryReportShouldBeFound("reportIsCompiled.specified=true");

        // Get all the rouDepreciationEntryReportList where reportIsCompiled is null
        defaultRouDepreciationEntryReportShouldNotBeFound("reportIsCompiled.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate equals to DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.equals=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate equals to UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.equals=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate not equals to DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.notEquals=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate not equals to UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.notEquals=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate in DEFAULT_PERIOD_START_DATE or UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.in=" + DEFAULT_PERIOD_START_DATE + "," + UPDATED_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate equals to UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.in=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate is not null
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.specified=true");

        // Get all the rouDepreciationEntryReportList where periodStartDate is null
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate is greater than or equal to DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.greaterThanOrEqual=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate is greater than or equal to UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.greaterThanOrEqual=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate is less than or equal to DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.lessThanOrEqual=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate is less than or equal to SMALLER_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.lessThanOrEqual=" + SMALLER_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate is less than DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.lessThan=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate is less than UPDATED_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.lessThan=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodStartDate is greater than DEFAULT_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodStartDate.greaterThan=" + DEFAULT_PERIOD_START_DATE);

        // Get all the rouDepreciationEntryReportList where periodStartDate is greater than SMALLER_PERIOD_START_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodStartDate.greaterThan=" + SMALLER_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate equals to DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.equals=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate equals to UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.equals=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate not equals to DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.notEquals=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate not equals to UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.notEquals=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate in DEFAULT_PERIOD_END_DATE or UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.in=" + DEFAULT_PERIOD_END_DATE + "," + UPDATED_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate equals to UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.in=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate is not null
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.specified=true");

        // Get all the rouDepreciationEntryReportList where periodEndDate is null
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate is greater than or equal to DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.greaterThanOrEqual=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate is greater than or equal to UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.greaterThanOrEqual=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate is less than or equal to DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.lessThanOrEqual=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate is less than or equal to SMALLER_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.lessThanOrEqual=" + SMALLER_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate is less than DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.lessThan=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate is less than UPDATED_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.lessThan=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where periodEndDate is greater than DEFAULT_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldNotBeFound("periodEndDate.greaterThan=" + DEFAULT_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportList where periodEndDate is greater than SMALLER_PERIOD_END_DATE
        defaultRouDepreciationEntryReportShouldBeFound("periodEndDate.greaterThan=" + SMALLER_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationEntryReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationEntryReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouDepreciationEntryReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum is not null
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouDepreciationEntryReportList where fileChecksum is null
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationEntryReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationEntryReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouDepreciationEntryReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouDepreciationEntryReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouDepreciationEntryReportList where tampered equals to UPDATED_TAMPERED
        defaultRouDepreciationEntryReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouDepreciationEntryReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouDepreciationEntryReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouDepreciationEntryReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouDepreciationEntryReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouDepreciationEntryReportList where tampered equals to UPDATED_TAMPERED
        defaultRouDepreciationEntryReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where tampered is not null
        defaultRouDepreciationEntryReportShouldBeFound("tampered.specified=true");

        // Get all the rouDepreciationEntryReportList where tampered is null
        defaultRouDepreciationEntryReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where filename equals to DEFAULT_FILENAME
        defaultRouDepreciationEntryReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouDepreciationEntryReportList where filename equals to UPDATED_FILENAME
        defaultRouDepreciationEntryReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where filename not equals to DEFAULT_FILENAME
        defaultRouDepreciationEntryReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouDepreciationEntryReportList where filename not equals to UPDATED_FILENAME
        defaultRouDepreciationEntryReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouDepreciationEntryReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouDepreciationEntryReportList where filename equals to UPDATED_FILENAME
        defaultRouDepreciationEntryReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where filename is not null
        defaultRouDepreciationEntryReportShouldBeFound("filename.specified=true");

        // Get all the rouDepreciationEntryReportList where filename is null
        defaultRouDepreciationEntryReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationEntryReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationEntryReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the rouDepreciationEntryReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters is not null
        defaultRouDepreciationEntryReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouDepreciationEntryReportList where reportParameters is null
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationEntryReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        // Get all the rouDepreciationEntryReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationEntryReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationEntryReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);
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
        rouDepreciationEntryReport.setRequestedBy(requestedBy);
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouDepreciationEntryReportList where requestedBy equals to requestedById
        defaultRouDepreciationEntryReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouDepreciationEntryReportList where requestedBy equals to (requestedById + 1)
        defaultRouDepreciationEntryReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationEntryReportShouldBeFound(String filter) throws Exception {
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationEntryReportShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationEntryReport() throws Exception {
        // Get the rouDepreciationEntryReport
        restRouDepreciationEntryReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouDepreciationEntryReport() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();

        // Update the rouDepreciationEntryReport
        RouDepreciationEntryReport updatedRouDepreciationEntryReport = rouDepreciationEntryReportRepository
            .findById(rouDepreciationEntryReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouDepreciationEntryReport are not directly saved in db
        em.detach(updatedRouDepreciationEntryReport);
        updatedRouDepreciationEntryReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(
            updatedRouDepreciationEntryReport
        );

        restRouDepreciationEntryReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntryReport testRouDepreciationEntryReport = rouDepreciationEntryReportList.get(
            rouDepreciationEntryReportList.size() - 1
        );
        assertThat(testRouDepreciationEntryReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationEntryReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationEntryReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationEntryReport.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testRouDepreciationEntryReport.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testRouDepreciationEntryReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouDepreciationEntryReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationEntryReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouDepreciationEntryReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouDepreciationEntryReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouDepreciationEntryReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository).save(testRouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void putNonExistingRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void partialUpdateRouDepreciationEntryReportWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();

        // Update the rouDepreciationEntryReport using partial update
        RouDepreciationEntryReport partialUpdatedRouDepreciationEntryReport = new RouDepreciationEntryReport();
        partialUpdatedRouDepreciationEntryReport.setId(rouDepreciationEntryReport.getId());

        partialUpdatedRouDepreciationEntryReport
            .requestId(UPDATED_REQUEST_ID)
            .tampered(UPDATED_TAMPERED)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouDepreciationEntryReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntryReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntryReport))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntryReport testRouDepreciationEntryReport = rouDepreciationEntryReportList.get(
            rouDepreciationEntryReportList.size() - 1
        );
        assertThat(testRouDepreciationEntryReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationEntryReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouDepreciationEntryReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationEntryReport.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testRouDepreciationEntryReport.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testRouDepreciationEntryReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouDepreciationEntryReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationEntryReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouDepreciationEntryReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouDepreciationEntryReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouDepreciationEntryReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouDepreciationEntryReportWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();

        // Update the rouDepreciationEntryReport using partial update
        RouDepreciationEntryReport partialUpdatedRouDepreciationEntryReport = new RouDepreciationEntryReport();
        partialUpdatedRouDepreciationEntryReport.setId(rouDepreciationEntryReport.getId());

        partialUpdatedRouDepreciationEntryReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouDepreciationEntryReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntryReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntryReport))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntryReport testRouDepreciationEntryReport = rouDepreciationEntryReportList.get(
            rouDepreciationEntryReportList.size() - 1
        );
        assertThat(testRouDepreciationEntryReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationEntryReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationEntryReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationEntryReport.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testRouDepreciationEntryReport.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testRouDepreciationEntryReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouDepreciationEntryReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationEntryReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouDepreciationEntryReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouDepreciationEntryReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouDepreciationEntryReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouDepreciationEntryReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouDepreciationEntryReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryReportRepository.findAll().size();
        rouDepreciationEntryReport.setId(count.incrementAndGet());

        // Create the RouDepreciationEntryReport
        RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO = rouDepreciationEntryReportMapper.toDto(rouDepreciationEntryReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntryReport in the database
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(0)).save(rouDepreciationEntryReport);
    }

    @Test
    @Transactional
    void deleteRouDepreciationEntryReport() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);

        int databaseSizeBeforeDelete = rouDepreciationEntryReportRepository.findAll().size();

        // Delete the rouDepreciationEntryReport
        restRouDepreciationEntryReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouDepreciationEntryReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouDepreciationEntryReport> rouDepreciationEntryReportList = rouDepreciationEntryReportRepository.findAll();
        assertThat(rouDepreciationEntryReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouDepreciationEntryReport in Elasticsearch
        verify(mockRouDepreciationEntryReportSearchRepository, times(1)).deleteById(rouDepreciationEntryReport.getId());
    }

    @Test
    @Transactional
    void searchRouDepreciationEntryReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationEntryReportRepository.saveAndFlush(rouDepreciationEntryReport);
        when(mockRouDepreciationEntryReportSearchRepository.search("id:" + rouDepreciationEntryReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationEntryReport), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationEntryReport
        restRouDepreciationEntryReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationEntryReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
