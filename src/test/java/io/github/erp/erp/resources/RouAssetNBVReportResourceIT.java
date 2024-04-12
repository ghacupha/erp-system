package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.RouAssetNBVReport;
import io.github.erp.repository.RouAssetNBVReportRepository;
import io.github.erp.repository.search.RouAssetNBVReportSearchRepository;
import io.github.erp.service.dto.RouAssetNBVReportDTO;
import io.github.erp.service.mapper.RouAssetNBVReportMapper;
import io.github.erp.web.rest.RouAssetNBVReportResource;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RouAssetNBVReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouAssetNBVReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/leases/rou-asset-nbv-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-asset-nbv-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAssetNBVReportRepository rouAssetNBVReportRepository;

    @Autowired
    private RouAssetNBVReportMapper rouAssetNBVReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAssetNBVReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAssetNBVReportSearchRepository mockRouAssetNBVReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAssetNBVReportMockMvc;

    private RouAssetNBVReport rouAssetNBVReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetNBVReport createEntity(EntityManager em) {
        RouAssetNBVReport rouAssetNBVReport = new RouAssetNBVReport()
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
        rouAssetNBVReport.setFiscalReportingMonth(fiscalMonth);
        return rouAssetNBVReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetNBVReport createUpdatedEntity(EntityManager em) {
        RouAssetNBVReport rouAssetNBVReport = new RouAssetNBVReport()
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
        rouAssetNBVReport.setFiscalReportingMonth(fiscalMonth);
        return rouAssetNBVReport;
    }

    @BeforeEach
    public void initTest() {
        rouAssetNBVReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeCreate = rouAssetNBVReportRepository.findAll().size();
        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);
        restRouAssetNBVReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouAssetNBVReport testRouAssetNBVReport = rouAssetNBVReportList.get(rouAssetNBVReportList.size() - 1);
        assertThat(testRouAssetNBVReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAssetNBVReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAssetNBVReport.getReportIsCompiled()).isEqualTo(DEFAULT_REPORT_IS_COMPILED);
        assertThat(testRouAssetNBVReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouAssetNBVReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouAssetNBVReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouAssetNBVReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouAssetNBVReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouAssetNBVReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(1)).save(testRouAssetNBVReport);
    }

    @Test
    @Transactional
    void createRouAssetNBVReportWithExistingId() throws Exception {
        // Create the RouAssetNBVReport with an existing ID
        rouAssetNBVReport.setId(1L);
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        int databaseSizeBeforeCreate = rouAssetNBVReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouAssetNBVReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouAssetNBVReportRepository.findAll().size();
        // set the field null
        rouAssetNBVReport.setRequestId(null);

        // Create the RouAssetNBVReport, which fails.
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        restRouAssetNBVReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReports() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReport.getId().intValue())))
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
    void getRouAssetNBVReport() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get the rouAssetNBVReport
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAssetNBVReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAssetNBVReport.getId().intValue()))
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
    void getRouAssetNBVReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        Long id = rouAssetNBVReport.getId();

        defaultRouAssetNBVReportShouldBeFound("id.equals=" + id);
        defaultRouAssetNBVReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouAssetNBVReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAssetNBVReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAssetNBVReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAssetNBVReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouAssetNBVReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAssetNBVReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAssetNBVReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouAssetNBVReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAssetNBVReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouAssetNBVReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouAssetNBVReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouAssetNBVReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAssetNBVReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where requestId is not null
        defaultRouAssetNBVReportShouldBeFound("requestId.specified=true");

        // Get all the rouAssetNBVReportList where requestId is null
        defaultRouAssetNBVReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest is not null
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouAssetNBVReportList where timeOfRequest is null
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetNBVReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouAssetNBVReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportIsCompiledIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportIsCompiled equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldBeFound("reportIsCompiled.equals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouAssetNBVReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldNotBeFound("reportIsCompiled.equals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportIsCompiledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportIsCompiled not equals to DEFAULT_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldNotBeFound("reportIsCompiled.notEquals=" + DEFAULT_REPORT_IS_COMPILED);

        // Get all the rouAssetNBVReportList where reportIsCompiled not equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldBeFound("reportIsCompiled.notEquals=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportIsCompiledIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportIsCompiled in DEFAULT_REPORT_IS_COMPILED or UPDATED_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldBeFound("reportIsCompiled.in=" + DEFAULT_REPORT_IS_COMPILED + "," + UPDATED_REPORT_IS_COMPILED);

        // Get all the rouAssetNBVReportList where reportIsCompiled equals to UPDATED_REPORT_IS_COMPILED
        defaultRouAssetNBVReportShouldNotBeFound("reportIsCompiled.in=" + UPDATED_REPORT_IS_COMPILED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportIsCompiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportIsCompiled is not null
        defaultRouAssetNBVReportShouldBeFound("reportIsCompiled.specified=true");

        // Get all the rouAssetNBVReportList where reportIsCompiled is null
        defaultRouAssetNBVReportShouldNotBeFound("reportIsCompiled.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetNBVReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetNBVReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouAssetNBVReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum is not null
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouAssetNBVReportList where fileChecksum is null
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetNBVReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetNBVReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouAssetNBVReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouAssetNBVReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouAssetNBVReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAssetNBVReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouAssetNBVReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouAssetNBVReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouAssetNBVReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouAssetNBVReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouAssetNBVReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAssetNBVReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where tampered is not null
        defaultRouAssetNBVReportShouldBeFound("tampered.specified=true");

        // Get all the rouAssetNBVReportList where tampered is null
        defaultRouAssetNBVReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where filename equals to DEFAULT_FILENAME
        defaultRouAssetNBVReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouAssetNBVReportList where filename equals to UPDATED_FILENAME
        defaultRouAssetNBVReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where filename not equals to DEFAULT_FILENAME
        defaultRouAssetNBVReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouAssetNBVReportList where filename not equals to UPDATED_FILENAME
        defaultRouAssetNBVReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouAssetNBVReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouAssetNBVReportList where filename equals to UPDATED_FILENAME
        defaultRouAssetNBVReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where filename is not null
        defaultRouAssetNBVReportShouldBeFound("filename.specified=true");

        // Get all the rouAssetNBVReportList where filename is null
        defaultRouAssetNBVReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetNBVReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetNBVReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the rouAssetNBVReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters is not null
        defaultRouAssetNBVReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouAssetNBVReportList where reportParameters is null
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetNBVReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        // Get all the rouAssetNBVReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetNBVReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouAssetNBVReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByFiscalReportingMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);
        FiscalMonth fiscalReportingMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalReportingMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalReportingMonth);
            em.flush();
        } else {
            fiscalReportingMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(fiscalReportingMonth);
        em.flush();
        rouAssetNBVReport.setFiscalReportingMonth(fiscalReportingMonth);
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);
        Long fiscalReportingMonthId = fiscalReportingMonth.getId();

        // Get all the rouAssetNBVReportList where fiscalReportingMonth equals to fiscalReportingMonthId
        defaultRouAssetNBVReportShouldBeFound("fiscalReportingMonthId.equals=" + fiscalReportingMonthId);

        // Get all the rouAssetNBVReportList where fiscalReportingMonth equals to (fiscalReportingMonthId + 1)
        defaultRouAssetNBVReportShouldNotBeFound("fiscalReportingMonthId.equals=" + (fiscalReportingMonthId + 1));
    }

    @Test
    @Transactional
    void getAllRouAssetNBVReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);
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
        rouAssetNBVReport.setRequestedBy(requestedBy);
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouAssetNBVReportList where requestedBy equals to requestedById
        defaultRouAssetNBVReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouAssetNBVReportList where requestedBy equals to (requestedById + 1)
        defaultRouAssetNBVReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAssetNBVReportShouldBeFound(String filter) throws Exception {
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReport.getId().intValue())))
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
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAssetNBVReportShouldNotBeFound(String filter) throws Exception {
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAssetNBVReport() throws Exception {
        // Get the rouAssetNBVReport
        restRouAssetNBVReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouAssetNBVReport() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();

        // Update the rouAssetNBVReport
        RouAssetNBVReport updatedRouAssetNBVReport = rouAssetNBVReportRepository.findById(rouAssetNBVReport.getId()).get();
        // Disconnect from session so that the updates on updatedRouAssetNBVReport are not directly saved in db
        em.detach(updatedRouAssetNBVReport);
        updatedRouAssetNBVReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(updatedRouAssetNBVReport);

        restRouAssetNBVReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAssetNBVReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetNBVReport testRouAssetNBVReport = rouAssetNBVReportList.get(rouAssetNBVReportList.size() - 1);
        assertThat(testRouAssetNBVReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAssetNBVReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAssetNBVReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouAssetNBVReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAssetNBVReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAssetNBVReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAssetNBVReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAssetNBVReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAssetNBVReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository).save(testRouAssetNBVReport);
    }

    @Test
    @Transactional
    void putNonExistingRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAssetNBVReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void partialUpdateRouAssetNBVReportWithPatch() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();

        // Update the rouAssetNBVReport using partial update
        RouAssetNBVReport partialUpdatedRouAssetNBVReport = new RouAssetNBVReport();
        partialUpdatedRouAssetNBVReport.setId(rouAssetNBVReport.getId());

        partialUpdatedRouAssetNBVReport
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouAssetNBVReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAssetNBVReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAssetNBVReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetNBVReport testRouAssetNBVReport = rouAssetNBVReportList.get(rouAssetNBVReportList.size() - 1);
        assertThat(testRouAssetNBVReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAssetNBVReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAssetNBVReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouAssetNBVReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouAssetNBVReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAssetNBVReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAssetNBVReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAssetNBVReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAssetNBVReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouAssetNBVReportWithPatch() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();

        // Update the rouAssetNBVReport using partial update
        RouAssetNBVReport partialUpdatedRouAssetNBVReport = new RouAssetNBVReport();
        partialUpdatedRouAssetNBVReport.setId(rouAssetNBVReport.getId());

        partialUpdatedRouAssetNBVReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportIsCompiled(UPDATED_REPORT_IS_COMPILED)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouAssetNBVReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAssetNBVReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAssetNBVReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetNBVReport testRouAssetNBVReport = rouAssetNBVReportList.get(rouAssetNBVReportList.size() - 1);
        assertThat(testRouAssetNBVReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAssetNBVReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAssetNBVReport.getReportIsCompiled()).isEqualTo(UPDATED_REPORT_IS_COMPILED);
        assertThat(testRouAssetNBVReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAssetNBVReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAssetNBVReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAssetNBVReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAssetNBVReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAssetNBVReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouAssetNBVReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouAssetNBVReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetNBVReportRepository.findAll().size();
        rouAssetNBVReport.setId(count.incrementAndGet());

        // Create the RouAssetNBVReport
        RouAssetNBVReportDTO rouAssetNBVReportDTO = rouAssetNBVReportMapper.toDto(rouAssetNBVReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetNBVReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetNBVReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAssetNBVReport in the database
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(0)).save(rouAssetNBVReport);
    }

    @Test
    @Transactional
    void deleteRouAssetNBVReport() throws Exception {
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);

        int databaseSizeBeforeDelete = rouAssetNBVReportRepository.findAll().size();

        // Delete the rouAssetNBVReport
        restRouAssetNBVReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouAssetNBVReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouAssetNBVReport> rouAssetNBVReportList = rouAssetNBVReportRepository.findAll();
        assertThat(rouAssetNBVReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouAssetNBVReport in Elasticsearch
        verify(mockRouAssetNBVReportSearchRepository, times(1)).deleteById(rouAssetNBVReport.getId());
    }

    @Test
    @Transactional
    void searchRouAssetNBVReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAssetNBVReportRepository.saveAndFlush(rouAssetNBVReport);
        when(mockRouAssetNBVReportSearchRepository.search("id:" + rouAssetNBVReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAssetNBVReport), PageRequest.of(0, 1), 1));

        // Search the rouAssetNBVReport
        restRouAssetNBVReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAssetNBVReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetNBVReport.getId().intValue())))
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
