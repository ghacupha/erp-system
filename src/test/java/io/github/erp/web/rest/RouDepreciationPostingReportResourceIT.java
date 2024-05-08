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
import io.github.erp.domain.RouDepreciationPostingReport;
import io.github.erp.repository.RouDepreciationPostingReportRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportSearchRepository;
import io.github.erp.service.criteria.RouDepreciationPostingReportCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportMapper;
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
 * Integration tests for the {@link RouDepreciationPostingReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouDepreciationPostingReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/rou-depreciation-posting-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-depreciation-posting-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository;

    @Autowired
    private RouDepreciationPostingReportMapper rouDepreciationPostingReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationPostingReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationPostingReportSearchRepository mockRouDepreciationPostingReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationPostingReportMockMvc;

    private RouDepreciationPostingReport rouDepreciationPostingReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationPostingReport createEntity(EntityManager em) {
        RouDepreciationPostingReport rouDepreciationPostingReport = new RouDepreciationPostingReport()
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
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        rouDepreciationPostingReport.setFiscalMonth(fiscalMonth);
        return rouDepreciationPostingReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationPostingReport createUpdatedEntity(EntityManager em) {
        RouDepreciationPostingReport rouDepreciationPostingReport = new RouDepreciationPostingReport()
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
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        rouDepreciationPostingReport.setFiscalMonth(fiscalMonth);
        return rouDepreciationPostingReport;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationPostingReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationPostingReportRepository.findAll().size();
        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );
        restRouDepreciationPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationPostingReport testRouDepreciationPostingReport = rouDepreciationPostingReportList.get(
            rouDepreciationPostingReportList.size() - 1
        );
        assertThat(testRouDepreciationPostingReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouDepreciationPostingReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouDepreciationPostingReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationPostingReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouDepreciationPostingReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouDepreciationPostingReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouDepreciationPostingReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouDepreciationPostingReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouDepreciationPostingReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(1)).save(testRouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void createRouDepreciationPostingReportWithExistingId() throws Exception {
        // Create the RouDepreciationPostingReport with an existing ID
        rouDepreciationPostingReport.setId(1L);
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        int databaseSizeBeforeCreate = rouDepreciationPostingReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouDepreciationPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationPostingReportRepository.findAll().size();
        // set the field null
        rouDepreciationPostingReport.setRequestId(null);

        // Create the RouDepreciationPostingReport, which fails.
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        restRouDepreciationPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReports() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReport.getId().intValue())))
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
    void getRouDepreciationPostingReport() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get the rouDepreciationPostingReport
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationPostingReport.getId().intValue()))
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
    void getRouDepreciationPostingReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        Long id = rouDepreciationPostingReport.getId();

        defaultRouDepreciationPostingReportShouldBeFound("id.equals=" + id);
        defaultRouDepreciationPostingReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationPostingReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationPostingReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationPostingReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationPostingReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouDepreciationPostingReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouDepreciationPostingReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouDepreciationPostingReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouDepreciationPostingReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouDepreciationPostingReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouDepreciationPostingReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouDepreciationPostingReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouDepreciationPostingReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouDepreciationPostingReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where requestId is not null
        defaultRouDepreciationPostingReportShouldBeFound("requestId.specified=true");

        // Get all the rouDepreciationPostingReportList where requestId is null
        defaultRouDepreciationPostingReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is not null
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouDepreciationPostingReportList where timeOfRequest is null
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouDepreciationPostingReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouDepreciationPostingReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportIsCompiledIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldBeFound("reportIsCompiled.equals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldNotBeFound("reportIsCompiled.equals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportIsCompiledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled not equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldNotBeFound("reportIsCompiled.notEquals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled not equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldBeFound("reportIsCompiled.notEquals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportIsCompiledIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled in DEFAULT_REPORT_IS_COMPILED or UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldBeFound(
            "reportIsCompiled.in=" + DEFAULT_REPORT_IS_COMPILED + "," + UPDATED_REPORT_IS_COMPILED
        );

        // Get all the rouDepreciationPostingReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouDepreciationPostingReportShouldNotBeFound("reportIsCompiled.in=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportIsCompiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportIsCompiled is not null
        defaultRouDepreciationPostingReportShouldBeFound("reportIsCompiled.specified=true");

        // Get all the rouDepreciationPostingReportList where reportIsCompiled is null
        defaultRouDepreciationPostingReportShouldNotBeFound("reportIsCompiled.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationPostingReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationPostingReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouDepreciationPostingReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum is not null
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouDepreciationPostingReportList where fileChecksum is null
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationPostingReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouDepreciationPostingReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouDepreciationPostingReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouDepreciationPostingReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouDepreciationPostingReportList where tampered equals to UPDATED_TAMPERED
        defaultRouDepreciationPostingReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouDepreciationPostingReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouDepreciationPostingReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouDepreciationPostingReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouDepreciationPostingReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouDepreciationPostingReportList where tampered equals to UPDATED_TAMPERED
        defaultRouDepreciationPostingReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where tampered is not null
        defaultRouDepreciationPostingReportShouldBeFound("tampered.specified=true");

        // Get all the rouDepreciationPostingReportList where tampered is null
        defaultRouDepreciationPostingReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where filename equals to DEFAULT_FILENAME
        defaultRouDepreciationPostingReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouDepreciationPostingReportList where filename equals to UPDATED_FILENAME
        defaultRouDepreciationPostingReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where filename not equals to DEFAULT_FILENAME
        defaultRouDepreciationPostingReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouDepreciationPostingReportList where filename not equals to UPDATED_FILENAME
        defaultRouDepreciationPostingReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouDepreciationPostingReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouDepreciationPostingReportList where filename equals to UPDATED_FILENAME
        defaultRouDepreciationPostingReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where filename is not null
        defaultRouDepreciationPostingReportShouldBeFound("filename.specified=true");

        // Get all the rouDepreciationPostingReportList where filename is null
        defaultRouDepreciationPostingReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationPostingReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationPostingReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the rouDepreciationPostingReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters is not null
        defaultRouDepreciationPostingReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouDepreciationPostingReportList where reportParameters is null
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationPostingReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        // Get all the rouDepreciationPostingReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouDepreciationPostingReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouDepreciationPostingReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(fiscalMonth);
        em.flush();
        rouDepreciationPostingReport.setFiscalMonth(fiscalMonth);
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the rouDepreciationPostingReportList where fiscalMonth equals to fiscalMonthId
        defaultRouDepreciationPostingReportShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the rouDepreciationPostingReportList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultRouDepreciationPostingReportShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);
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
        rouDepreciationPostingReport.setRequestedBy(requestedBy);
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouDepreciationPostingReportList where requestedBy equals to requestedById
        defaultRouDepreciationPostingReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouDepreciationPostingReportList where requestedBy equals to (requestedById + 1)
        defaultRouDepreciationPostingReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationPostingReportShouldBeFound(String filter) throws Exception {
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReport.getId().intValue())))
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
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationPostingReportShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationPostingReport() throws Exception {
        // Get the rouDepreciationPostingReport
        restRouDepreciationPostingReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouDepreciationPostingReport() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();

        // Update the rouDepreciationPostingReport
        RouDepreciationPostingReport updatedRouDepreciationPostingReport = rouDepreciationPostingReportRepository
            .findById(rouDepreciationPostingReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedRouDepreciationPostingReport are not directly saved in db
        em.detach(updatedRouDepreciationPostingReport);
        updatedRouDepreciationPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            updatedRouDepreciationPostingReport
        );

        restRouDepreciationPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationPostingReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationPostingReport testRouDepreciationPostingReport = rouDepreciationPostingReportList.get(
            rouDepreciationPostingReportList.size() - 1
        );
        assertThat(testRouDepreciationPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationPostingReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationPostingReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationPostingReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouDepreciationPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationPostingReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouDepreciationPostingReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouDepreciationPostingReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouDepreciationPostingReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository).save(testRouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void putNonExistingRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationPostingReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void partialUpdateRouDepreciationPostingReportWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();

        // Update the rouDepreciationPostingReport using partial update
        RouDepreciationPostingReport partialUpdatedRouDepreciationPostingReport = new RouDepreciationPostingReport();
        partialUpdatedRouDepreciationPostingReport.setId(rouDepreciationPostingReport.getId());

        partialUpdatedRouDepreciationPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .tampered(UPDATED_TAMPERED)
            .reportParameters(UPDATED_REPORT_PARAMETERS);

        restRouDepreciationPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationPostingReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationPostingReport))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationPostingReport testRouDepreciationPostingReport = rouDepreciationPostingReportList.get(
            rouDepreciationPostingReportList.size() - 1
        );
        assertThat(testRouDepreciationPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationPostingReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouDepreciationPostingReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationPostingReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouDepreciationPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationPostingReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouDepreciationPostingReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouDepreciationPostingReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouDepreciationPostingReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouDepreciationPostingReportWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();

        // Update the rouDepreciationPostingReport using partial update
        RouDepreciationPostingReport partialUpdatedRouDepreciationPostingReport = new RouDepreciationPostingReport();
        partialUpdatedRouDepreciationPostingReport.setId(rouDepreciationPostingReport.getId());

        partialUpdatedRouDepreciationPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouDepreciationPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationPostingReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationPostingReport))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationPostingReport testRouDepreciationPostingReport = rouDepreciationPostingReportList.get(
            rouDepreciationPostingReportList.size() - 1
        );
        assertThat(testRouDepreciationPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouDepreciationPostingReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouDepreciationPostingReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouDepreciationPostingReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouDepreciationPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouDepreciationPostingReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouDepreciationPostingReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouDepreciationPostingReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouDepreciationPostingReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouDepreciationPostingReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouDepreciationPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationPostingReportRepository.findAll().size();
        rouDepreciationPostingReport.setId(count.incrementAndGet());

        // Create the RouDepreciationPostingReport
        RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO = rouDepreciationPostingReportMapper.toDto(
            rouDepreciationPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationPostingReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationPostingReport in the database
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(0)).save(rouDepreciationPostingReport);
    }

    @Test
    @Transactional
    void deleteRouDepreciationPostingReport() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);

        int databaseSizeBeforeDelete = rouDepreciationPostingReportRepository.findAll().size();

        // Delete the rouDepreciationPostingReport
        restRouDepreciationPostingReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouDepreciationPostingReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouDepreciationPostingReport> rouDepreciationPostingReportList = rouDepreciationPostingReportRepository.findAll();
        assertThat(rouDepreciationPostingReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouDepreciationPostingReport in Elasticsearch
        verify(mockRouDepreciationPostingReportSearchRepository, times(1)).deleteById(rouDepreciationPostingReport.getId());
    }

    @Test
    @Transactional
    void searchRouDepreciationPostingReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationPostingReportRepository.saveAndFlush(rouDepreciationPostingReport);
        when(mockRouDepreciationPostingReportSearchRepository.search("id:" + rouDepreciationPostingReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationPostingReport), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationPostingReport
        restRouDepreciationPostingReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReport.getId().intValue())))
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
