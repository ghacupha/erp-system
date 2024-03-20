package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.RouMonthlyDepreciationReport;
import io.github.erp.repository.RouMonthlyDepreciationReportRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepository;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportMapper;
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
 * Integration tests for the {@link RouMonthlyDepreciationReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouMonthlyDepreciationReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/rou-monthly-depreciation-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-monthly-depreciation-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository;

    @Autowired
    private RouMonthlyDepreciationReportMapper rouMonthlyDepreciationReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouMonthlyDepreciationReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouMonthlyDepreciationReportSearchRepository mockRouMonthlyDepreciationReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouMonthlyDepreciationReportMockMvc;

    private RouMonthlyDepreciationReport rouMonthlyDepreciationReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouMonthlyDepreciationReport createEntity(EntityManager em) {
        RouMonthlyDepreciationReport rouMonthlyDepreciationReport = new RouMonthlyDepreciationReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .reportIsCompiled(DEFAULT_REPORT_IS_COMPILED)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        rouMonthlyDepreciationReport.setReportingYear(fiscalYear);
        return rouMonthlyDepreciationReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouMonthlyDepreciationReport createUpdatedEntity(EntityManager em) {
        RouMonthlyDepreciationReport rouMonthlyDepreciationReport = new RouMonthlyDepreciationReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        rouMonthlyDepreciationReport.setReportingYear(fiscalYear);
        return rouMonthlyDepreciationReport;
    }

    @BeforeEach
    public void initTest() {
        rouMonthlyDepreciationReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeCreate = rouMonthlyDepreciationReportRepository.findAll().size();
        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouMonthlyDepreciationReport testRouMonthlyDepreciationReport = rouMonthlyDepreciationReportList.get(
            rouMonthlyDepreciationReportList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouMonthlyDepreciationReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouMonthlyDepreciationReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouMonthlyDepreciationReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouMonthlyDepreciationReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouMonthlyDepreciationReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouMonthlyDepreciationReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouMonthlyDepreciationReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouMonthlyDepreciationReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(1)).save(testRouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void createRouMonthlyDepreciationReportWithExistingId() throws Exception {
        // Create the RouMonthlyDepreciationReport with an existing ID
        rouMonthlyDepreciationReport.setId(1L);
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        int databaseSizeBeforeCreate = rouMonthlyDepreciationReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouMonthlyDepreciationReportRepository.findAll().size();
        // set the field null
        rouMonthlyDepreciationReport.setRequestId(null);

        // Create the RouMonthlyDepreciationReport, which fails.
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        restRouMonthlyDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouMonthlyDepreciationReportRepository.findAll().size();
        // set the field null
        rouMonthlyDepreciationReport.setTimeOfRequest(null);

        // Create the RouMonthlyDepreciationReport, which fails.
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        restRouMonthlyDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReports() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReport.getId().intValue())))
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
    void getRouMonthlyDepreciationReport() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get the rouMonthlyDepreciationReport
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouMonthlyDepreciationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouMonthlyDepreciationReport.getId().intValue()))
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
    void getRouMonthlyDepreciationReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        Long id = rouMonthlyDepreciationReport.getId();

        defaultRouMonthlyDepreciationReportShouldBeFound("id.equals=" + id);
        defaultRouMonthlyDepreciationReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouMonthlyDepreciationReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouMonthlyDepreciationReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouMonthlyDepreciationReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouMonthlyDepreciationReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouMonthlyDepreciationReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouMonthlyDepreciationReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouMonthlyDepreciationReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouMonthlyDepreciationReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where requestId is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("requestId.specified=true");

        // Get all the rouMonthlyDepreciationReportList where requestId is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouMonthlyDepreciationReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouMonthlyDepreciationReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportIsCompiledIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldBeFound("reportIsCompiled.equals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportIsCompiled.equals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportIsCompiledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled not equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportIsCompiled.notEquals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled not equals to UPDATED_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldBeFound("reportIsCompiled.notEquals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportIsCompiledIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled in DEFAULT_REPORT_IS_COMPILED or UPDATED_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldBeFound(
            "reportIsCompiled.in=" + DEFAULT_REPORT_IS_COMPILED + "," + UPDATED_REPORT_IS_COMPILED
        );

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportIsCompiled.in=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportIsCompiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("reportIsCompiled.specified=true");

        // Get all the rouMonthlyDepreciationReportList where reportIsCompiled is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportIsCompiled.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouMonthlyDepreciationReportList where fileChecksum is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouMonthlyDepreciationReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouMonthlyDepreciationReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouMonthlyDepreciationReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouMonthlyDepreciationReportList where tampered equals to UPDATED_TAMPERED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouMonthlyDepreciationReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouMonthlyDepreciationReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouMonthlyDepreciationReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouMonthlyDepreciationReportList where tampered equals to UPDATED_TAMPERED
        defaultRouMonthlyDepreciationReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where tampered is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("tampered.specified=true");

        // Get all the rouMonthlyDepreciationReportList where tampered is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where filename equals to DEFAULT_FILENAME
        defaultRouMonthlyDepreciationReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouMonthlyDepreciationReportList where filename equals to UPDATED_FILENAME
        defaultRouMonthlyDepreciationReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where filename not equals to DEFAULT_FILENAME
        defaultRouMonthlyDepreciationReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouMonthlyDepreciationReportList where filename not equals to UPDATED_FILENAME
        defaultRouMonthlyDepreciationReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouMonthlyDepreciationReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouMonthlyDepreciationReportList where filename equals to UPDATED_FILENAME
        defaultRouMonthlyDepreciationReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where filename is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("filename.specified=true");

        // Get all the rouMonthlyDepreciationReportList where filename is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouMonthlyDepreciationReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouMonthlyDepreciationReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the rouMonthlyDepreciationReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters is not null
        defaultRouMonthlyDepreciationReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouMonthlyDepreciationReportList where reportParameters is null
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouMonthlyDepreciationReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        // Get all the rouMonthlyDepreciationReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouMonthlyDepreciationReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouMonthlyDepreciationReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);
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
        rouMonthlyDepreciationReport.setRequestedBy(requestedBy);
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouMonthlyDepreciationReportList where requestedBy equals to requestedById
        defaultRouMonthlyDepreciationReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouMonthlyDepreciationReportList where requestedBy equals to (requestedById + 1)
        defaultRouMonthlyDepreciationReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllRouMonthlyDepreciationReportsByReportingYearIsEqualToSomething() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);
        FiscalYear reportingYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            reportingYear = FiscalYearResourceIT.createEntity(em);
            em.persist(reportingYear);
            em.flush();
        } else {
            reportingYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(reportingYear);
        em.flush();
        rouMonthlyDepreciationReport.setReportingYear(reportingYear);
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);
        Long reportingYearId = reportingYear.getId();

        // Get all the rouMonthlyDepreciationReportList where reportingYear equals to reportingYearId
        defaultRouMonthlyDepreciationReportShouldBeFound("reportingYearId.equals=" + reportingYearId);

        // Get all the rouMonthlyDepreciationReportList where reportingYear equals to (reportingYearId + 1)
        defaultRouMonthlyDepreciationReportShouldNotBeFound("reportingYearId.equals=" + (reportingYearId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouMonthlyDepreciationReportShouldBeFound(String filter) throws Exception {
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReport.getId().intValue())))
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
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouMonthlyDepreciationReportShouldNotBeFound(String filter) throws Exception {
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouMonthlyDepreciationReport() throws Exception {
        // Get the rouMonthlyDepreciationReport
        restRouMonthlyDepreciationReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouMonthlyDepreciationReport() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();

        // Update the rouMonthlyDepreciationReport
        RouMonthlyDepreciationReport updatedRouMonthlyDepreciationReport = rouMonthlyDepreciationReportRepository
            .findById(rouMonthlyDepreciationReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouMonthlyDepreciationReport are not directly saved in db
        em.detach(updatedRouMonthlyDepreciationReport);
        updatedRouMonthlyDepreciationReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            updatedRouMonthlyDepreciationReport
        );

        restRouMonthlyDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouMonthlyDepreciationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReport testRouMonthlyDepreciationReport = rouMonthlyDepreciationReportList.get(
            rouMonthlyDepreciationReportList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouMonthlyDepreciationReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouMonthlyDepreciationReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouMonthlyDepreciationReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouMonthlyDepreciationReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouMonthlyDepreciationReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouMonthlyDepreciationReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouMonthlyDepreciationReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouMonthlyDepreciationReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository).save(testRouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void putNonExistingRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouMonthlyDepreciationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void partialUpdateRouMonthlyDepreciationReportWithPatch() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();

        // Update the rouMonthlyDepreciationReport using partial update
        RouMonthlyDepreciationReport partialUpdatedRouMonthlyDepreciationReport = new RouMonthlyDepreciationReport();
        partialUpdatedRouMonthlyDepreciationReport.setId(rouMonthlyDepreciationReport.getId());

        partialUpdatedRouMonthlyDepreciationReport
            .requestId(UPDATED_REQUEST_ID)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .tampered(UPDATED_TAMPERED)
            .reportParameters(UPDATED_REPORT_PARAMETERS);

        restRouMonthlyDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouMonthlyDepreciationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouMonthlyDepreciationReport))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReport testRouMonthlyDepreciationReport = rouMonthlyDepreciationReportList.get(
            rouMonthlyDepreciationReportList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouMonthlyDepreciationReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouMonthlyDepreciationReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouMonthlyDepreciationReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouMonthlyDepreciationReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouMonthlyDepreciationReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouMonthlyDepreciationReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouMonthlyDepreciationReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouMonthlyDepreciationReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouMonthlyDepreciationReportWithPatch() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();

        // Update the rouMonthlyDepreciationReport using partial update
        RouMonthlyDepreciationReport partialUpdatedRouMonthlyDepreciationReport = new RouMonthlyDepreciationReport();
        partialUpdatedRouMonthlyDepreciationReport.setId(rouMonthlyDepreciationReport.getId());

        partialUpdatedRouMonthlyDepreciationReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouMonthlyDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouMonthlyDepreciationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouMonthlyDepreciationReport))
            )
            .andExpect(status().isOk());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);
        RouMonthlyDepreciationReport testRouMonthlyDepreciationReport = rouMonthlyDepreciationReportList.get(
            rouMonthlyDepreciationReportList.size() - 1
        );
        assertThat(testRouMonthlyDepreciationReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouMonthlyDepreciationReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouMonthlyDepreciationReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouMonthlyDepreciationReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouMonthlyDepreciationReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouMonthlyDepreciationReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouMonthlyDepreciationReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouMonthlyDepreciationReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouMonthlyDepreciationReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouMonthlyDepreciationReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouMonthlyDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = rouMonthlyDepreciationReportRepository.findAll().size();
        rouMonthlyDepreciationReport.setId(count.incrementAndGet());

        // Create the RouMonthlyDepreciationReport
        RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportMapper.toDto(
            rouMonthlyDepreciationReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouMonthlyDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouMonthlyDepreciationReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouMonthlyDepreciationReport in the database
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(0)).save(rouMonthlyDepreciationReport);
    }

    @Test
    @Transactional
    void deleteRouMonthlyDepreciationReport() throws Exception {
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);

        int databaseSizeBeforeDelete = rouMonthlyDepreciationReportRepository.findAll().size();

        // Delete the rouMonthlyDepreciationReport
        restRouMonthlyDepreciationReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouMonthlyDepreciationReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouMonthlyDepreciationReport> rouMonthlyDepreciationReportList = rouMonthlyDepreciationReportRepository.findAll();
        assertThat(rouMonthlyDepreciationReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouMonthlyDepreciationReport in Elasticsearch
        verify(mockRouMonthlyDepreciationReportSearchRepository, times(1)).deleteById(rouMonthlyDepreciationReport.getId());
    }

    @Test
    @Transactional
    void searchRouMonthlyDepreciationReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouMonthlyDepreciationReportRepository.saveAndFlush(rouMonthlyDepreciationReport);
        when(mockRouMonthlyDepreciationReportSearchRepository.search("id:" + rouMonthlyDepreciationReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouMonthlyDepreciationReport), PageRequest.of(0, 1), 1));

        // Search the rouMonthlyDepreciationReport
        restRouMonthlyDepreciationReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouMonthlyDepreciationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouMonthlyDepreciationReport.getId().intValue())))
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
