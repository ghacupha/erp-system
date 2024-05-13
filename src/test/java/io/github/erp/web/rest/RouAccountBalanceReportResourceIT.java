package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.RouAccountBalanceReport;
import io.github.erp.repository.RouAccountBalanceReportRepository;
import io.github.erp.repository.search.RouAccountBalanceReportSearchRepository;
import io.github.erp.service.criteria.RouAccountBalanceReportCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RouAccountBalanceReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouAccountBalanceReportResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_REPORT_IS_COMPILED = false;
    private static final Boolean UPDATED_REPORT_IS_COMPILED = true;

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

    private static final String ENTITY_API_URL = "/api/rou-account-balance-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-account-balance-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAccountBalanceReportRepository rouAccountBalanceReportRepository;

    @Autowired
    private RouAccountBalanceReportMapper rouAccountBalanceReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAccountBalanceReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAccountBalanceReportSearchRepository mockRouAccountBalanceReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAccountBalanceReportMockMvc;

    private RouAccountBalanceReport rouAccountBalanceReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAccountBalanceReport createEntity(EntityManager em) {
        RouAccountBalanceReport rouAccountBalanceReport = new RouAccountBalanceReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .reportIsCompiled(DEFAULT_REPORT_IS_COMPILED)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return rouAccountBalanceReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAccountBalanceReport createUpdatedEntity(EntityManager em) {
        RouAccountBalanceReport rouAccountBalanceReport = new RouAccountBalanceReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return rouAccountBalanceReport;
    }

    @BeforeEach
    public void initTest() {
        rouAccountBalanceReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeCreate = rouAccountBalanceReportRepository.findAll().size();
        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);
        restRouAccountBalanceReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouAccountBalanceReport testRouAccountBalanceReport = rouAccountBalanceReportList.get(rouAccountBalanceReportList.size() - 1);
        assertThat(testRouAccountBalanceReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAccountBalanceReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAccountBalanceReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouAccountBalanceReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouAccountBalanceReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouAccountBalanceReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouAccountBalanceReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouAccountBalanceReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouAccountBalanceReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(1)).save(testRouAccountBalanceReport);
    }

    @Test
    @Transactional
    void createRouAccountBalanceReportWithExistingId() throws Exception {
        // Create the RouAccountBalanceReport with an existing ID
        rouAccountBalanceReport.setId(1L);
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        int databaseSizeBeforeCreate = rouAccountBalanceReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouAccountBalanceReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouAccountBalanceReportRepository.findAll().size();
        // set the field null
        rouAccountBalanceReport.setRequestId(null);

        // Create the RouAccountBalanceReport, which fails.
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        restRouAccountBalanceReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReports() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getRouAccountBalanceReport() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get the rouAccountBalanceReport
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAccountBalanceReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAccountBalanceReport.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.reportIsCompiled").value(DEFAULT_REPORT_IS_COMPILED.booleanValue()))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getRouAccountBalanceReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        Long id = rouAccountBalanceReport.getId();

        defaultRouAccountBalanceReportShouldBeFound("id.equals=" + id);
        defaultRouAccountBalanceReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouAccountBalanceReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAccountBalanceReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAccountBalanceReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAccountBalanceReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouAccountBalanceReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAccountBalanceReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAccountBalanceReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouAccountBalanceReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAccountBalanceReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouAccountBalanceReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouAccountBalanceReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouAccountBalanceReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAccountBalanceReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where requestId is not null
        defaultRouAccountBalanceReportShouldBeFound("requestId.specified=true");

        // Get all the rouAccountBalanceReportList where requestId is null
        defaultRouAccountBalanceReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest is not null
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouAccountBalanceReportList where timeOfRequest is null
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAccountBalanceReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouAccountBalanceReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportIsCompiledIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportIsCompiled equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldBeFound("reportIsCompiled.equals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouAccountBalanceReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldNotBeFound("reportIsCompiled.equals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportIsCompiledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportIsCompiled not equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldNotBeFound("reportIsCompiled.notEquals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouAccountBalanceReportList where reportIsCompiled not equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldBeFound("reportIsCompiled.notEquals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportIsCompiledIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportIsCompiled in DEFAULT_REPORT_IS_COMPILED or UPDATED_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldBeFound("reportIsCompiled.in=" + DEFAULT_REPORT_IS_COMPILED + "," + UPDATED_REPORT_IS_COMPILED);

        // Get all the rouAccountBalanceReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAccountBalanceReportShouldNotBeFound("reportIsCompiled.in=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportIsCompiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportIsCompiled is not null
        defaultRouAccountBalanceReportShouldBeFound("reportIsCompiled.specified=true");

        // Get all the rouAccountBalanceReportList where reportIsCompiled is null
        defaultRouAccountBalanceReportShouldNotBeFound("reportIsCompiled.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAccountBalanceReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAccountBalanceReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouAccountBalanceReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum is not null
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouAccountBalanceReportList where fileChecksum is null
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAccountBalanceReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAccountBalanceReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouAccountBalanceReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouAccountBalanceReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouAccountBalanceReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAccountBalanceReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouAccountBalanceReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouAccountBalanceReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouAccountBalanceReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouAccountBalanceReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouAccountBalanceReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAccountBalanceReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where tampered is not null
        defaultRouAccountBalanceReportShouldBeFound("tampered.specified=true");

        // Get all the rouAccountBalanceReportList where tampered is null
        defaultRouAccountBalanceReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where filename equals to DEFAULT_FILENAME
        defaultRouAccountBalanceReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouAccountBalanceReportList where filename equals to UPDATED_FILENAME
        defaultRouAccountBalanceReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where filename not equals to DEFAULT_FILENAME
        defaultRouAccountBalanceReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouAccountBalanceReportList where filename not equals to UPDATED_FILENAME
        defaultRouAccountBalanceReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouAccountBalanceReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouAccountBalanceReportList where filename equals to UPDATED_FILENAME
        defaultRouAccountBalanceReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where filename is not null
        defaultRouAccountBalanceReportShouldBeFound("filename.specified=true");

        // Get all the rouAccountBalanceReportList where filename is null
        defaultRouAccountBalanceReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAccountBalanceReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAccountBalanceReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the rouAccountBalanceReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters is not null
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouAccountBalanceReportList where reportParameters is null
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAccountBalanceReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        // Get all the rouAccountBalanceReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAccountBalanceReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouAccountBalanceReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);
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
        rouAccountBalanceReport.setRequestedBy(requestedBy);
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouAccountBalanceReportList where requestedBy equals to requestedById
        defaultRouAccountBalanceReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouAccountBalanceReportList where requestedBy equals to (requestedById + 1)
        defaultRouAccountBalanceReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportsByReportingMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);
        FiscalMonth reportingMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            reportingMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(reportingMonth);
            em.flush();
        } else {
            reportingMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(reportingMonth);
        em.flush();
        rouAccountBalanceReport.setReportingMonth(reportingMonth);
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);
        Long reportingMonthId = reportingMonth.getId();

        // Get all the rouAccountBalanceReportList where reportingMonth equals to reportingMonthId
        defaultRouAccountBalanceReportShouldBeFound("reportingMonthId.equals=" + reportingMonthId);

        // Get all the rouAccountBalanceReportList where reportingMonth equals to (reportingMonthId + 1)
        defaultRouAccountBalanceReportShouldNotBeFound("reportingMonthId.equals=" + (reportingMonthId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAccountBalanceReportShouldBeFound(String filter) throws Exception {
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAccountBalanceReportShouldNotBeFound(String filter) throws Exception {
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAccountBalanceReport() throws Exception {
        // Get the rouAccountBalanceReport
        restRouAccountBalanceReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouAccountBalanceReport() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();

        // Update the rouAccountBalanceReport
        RouAccountBalanceReport updatedRouAccountBalanceReport = rouAccountBalanceReportRepository
            .findById(rouAccountBalanceReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouAccountBalanceReport are not directly saved in db
        em.detach(updatedRouAccountBalanceReport);
        updatedRouAccountBalanceReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(updatedRouAccountBalanceReport);

        restRouAccountBalanceReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAccountBalanceReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);
        RouAccountBalanceReport testRouAccountBalanceReport = rouAccountBalanceReportList.get(rouAccountBalanceReportList.size() - 1);
        assertThat(testRouAccountBalanceReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAccountBalanceReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAccountBalanceReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouAccountBalanceReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAccountBalanceReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAccountBalanceReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAccountBalanceReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAccountBalanceReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAccountBalanceReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository).save(testRouAccountBalanceReport);
    }

    @Test
    @Transactional
    void putNonExistingRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAccountBalanceReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void partialUpdateRouAccountBalanceReportWithPatch() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();

        // Update the rouAccountBalanceReport using partial update
        RouAccountBalanceReport partialUpdatedRouAccountBalanceReport = new RouAccountBalanceReport();
        partialUpdatedRouAccountBalanceReport.setId(rouAccountBalanceReport.getId());

        partialUpdatedRouAccountBalanceReport
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouAccountBalanceReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAccountBalanceReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAccountBalanceReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);
        RouAccountBalanceReport testRouAccountBalanceReport = rouAccountBalanceReportList.get(rouAccountBalanceReportList.size() - 1);
        assertThat(testRouAccountBalanceReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAccountBalanceReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAccountBalanceReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouAccountBalanceReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAccountBalanceReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouAccountBalanceReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouAccountBalanceReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAccountBalanceReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAccountBalanceReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouAccountBalanceReportWithPatch() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();

        // Update the rouAccountBalanceReport using partial update
        RouAccountBalanceReport partialUpdatedRouAccountBalanceReport = new RouAccountBalanceReport();
        partialUpdatedRouAccountBalanceReport.setId(rouAccountBalanceReport.getId());

        partialUpdatedRouAccountBalanceReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouAccountBalanceReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAccountBalanceReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAccountBalanceReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);
        RouAccountBalanceReport testRouAccountBalanceReport = rouAccountBalanceReportList.get(rouAccountBalanceReportList.size() - 1);
        assertThat(testRouAccountBalanceReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAccountBalanceReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAccountBalanceReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouAccountBalanceReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAccountBalanceReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAccountBalanceReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAccountBalanceReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAccountBalanceReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAccountBalanceReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouAccountBalanceReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouAccountBalanceReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAccountBalanceReportRepository.findAll().size();
        rouAccountBalanceReport.setId(count.incrementAndGet());

        // Create the RouAccountBalanceReport
        RouAccountBalanceReportDTO rouAccountBalanceReportDTO = rouAccountBalanceReportMapper.toDto(rouAccountBalanceReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAccountBalanceReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAccountBalanceReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAccountBalanceReport in the database
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(0)).save(rouAccountBalanceReport);
    }

    @Test
    @Transactional
    void deleteRouAccountBalanceReport() throws Exception {
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);

        int databaseSizeBeforeDelete = rouAccountBalanceReportRepository.findAll().size();

        // Delete the rouAccountBalanceReport
        restRouAccountBalanceReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouAccountBalanceReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouAccountBalanceReport> rouAccountBalanceReportList = rouAccountBalanceReportRepository.findAll();
        assertThat(rouAccountBalanceReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouAccountBalanceReport in Elasticsearch
        verify(mockRouAccountBalanceReportSearchRepository, times(1)).deleteById(rouAccountBalanceReport.getId());
    }

    @Test
    @Transactional
    void searchRouAccountBalanceReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAccountBalanceReportRepository.saveAndFlush(rouAccountBalanceReport);
        when(mockRouAccountBalanceReportSearchRepository.search("id:" + rouAccountBalanceReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAccountBalanceReport), PageRequest.of(0, 1), 1));

        // Search the rouAccountBalanceReport
        restRouAccountBalanceReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAccountBalanceReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].reportIsCompiled").value(hasItem(DEFAULT_REPORT_IS_COMPILED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
