package io.github.erp.erp.resources;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.LeaseAmortizationSchedule;
import io.github.erp.domain.LeaseLiabilityScheduleReport;
import io.github.erp.repository.LeaseLiabilityScheduleReportRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportSearchRepository;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportMapper;
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
 * Integration tests for the LeaseLiabilityScheduleReportResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseLiabilityScheduleReportResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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

    private static final String ENTITY_API_URL = "/api/leases/lease-liability-schedule-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-liability-schedule-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository;

    @Autowired
    private LeaseLiabilityScheduleReportMapper leaseLiabilityScheduleReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityScheduleReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityScheduleReportSearchRepository mockLeaseLiabilityScheduleReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityScheduleReportMockMvc;

    private LeaseLiabilityScheduleReport leaseLiabilityScheduleReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleReport createEntity(EntityManager em) {
        LeaseLiabilityScheduleReport leaseLiabilityScheduleReport = new LeaseLiabilityScheduleReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeaseAmortizationSchedule leaseAmortizationSchedule;
        if (TestUtil.findAll(em, LeaseAmortizationSchedule.class).isEmpty()) {
            leaseAmortizationSchedule = LeaseAmortizationScheduleResourceIT.createEntity(em);
            em.persist(leaseAmortizationSchedule);
            em.flush();
        } else {
            leaseAmortizationSchedule = TestUtil.findAll(em, LeaseAmortizationSchedule.class).get(0);
        }
        leaseLiabilityScheduleReport.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
        return leaseLiabilityScheduleReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleReport createUpdatedEntity(EntityManager em) {
        LeaseLiabilityScheduleReport leaseLiabilityScheduleReport = new LeaseLiabilityScheduleReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeaseAmortizationSchedule leaseAmortizationSchedule;
        if (TestUtil.findAll(em, LeaseAmortizationSchedule.class).isEmpty()) {
            leaseAmortizationSchedule = LeaseAmortizationScheduleResourceIT.createUpdatedEntity(em);
            em.persist(leaseAmortizationSchedule);
            em.flush();
        } else {
            leaseAmortizationSchedule = TestUtil.findAll(em, LeaseAmortizationSchedule.class).get(0);
        }
        leaseLiabilityScheduleReport.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
        return leaseLiabilityScheduleReport;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityScheduleReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityScheduleReportRepository.findAll().size();
        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityScheduleReport testLeaseLiabilityScheduleReport = leaseLiabilityScheduleReportList.get(
            leaseLiabilityScheduleReportList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityScheduleReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityScheduleReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityScheduleReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testLeaseLiabilityScheduleReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testLeaseLiabilityScheduleReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityScheduleReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testLeaseLiabilityScheduleReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(1)).save(testLeaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void createLeaseLiabilityScheduleReportWithExistingId() throws Exception {
        // Create the LeaseLiabilityScheduleReport with an existing ID
        leaseLiabilityScheduleReport.setId(1L);
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        int databaseSizeBeforeCreate = leaseLiabilityScheduleReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityScheduleReportRepository.findAll().size();
        // set the field null
        leaseLiabilityScheduleReport.setRequestId(null);

        // Create the LeaseLiabilityScheduleReport, which fails.
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityScheduleReportRepository.findAll().size();
        // set the field null
        leaseLiabilityScheduleReport.setTimeOfRequest(null);

        // Create the LeaseLiabilityScheduleReport, which fails.
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReports() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleReport() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get the leaseLiabilityScheduleReport
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityScheduleReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityScheduleReport.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleReportsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        Long id = leaseLiabilityScheduleReport.getId();

        defaultLeaseLiabilityScheduleReportShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityScheduleReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityScheduleReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityScheduleReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityScheduleReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the leaseLiabilityScheduleReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where requestId is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("requestId.specified=true");

        // Get all the leaseLiabilityScheduleReportList where requestId is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityScheduleReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityScheduleReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.specified=true");

        // Get all the leaseLiabilityScheduleReportList where fileChecksum is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityScheduleReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityScheduleReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where tampered equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityScheduleReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where tampered not equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityScheduleReportList where tampered not equals to UPDATED_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the leaseLiabilityScheduleReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where tampered is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("tampered.specified=true");

        // Get all the leaseLiabilityScheduleReportList where tampered is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where filename equals to DEFAULT_FILENAME
        defaultLeaseLiabilityScheduleReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityScheduleReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where filename not equals to DEFAULT_FILENAME
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityScheduleReportList where filename not equals to UPDATED_FILENAME
        defaultLeaseLiabilityScheduleReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultLeaseLiabilityScheduleReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the leaseLiabilityScheduleReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where filename is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("filename.specified=true");

        // Get all the leaseLiabilityScheduleReportList where filename is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityScheduleReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityScheduleReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the leaseLiabilityScheduleReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters is not null
        defaultLeaseLiabilityScheduleReportShouldBeFound("reportParameters.specified=true");

        // Get all the leaseLiabilityScheduleReportList where reportParameters is null
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityScheduleReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        // Get all the leaseLiabilityScheduleReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityScheduleReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityScheduleReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);
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
        leaseLiabilityScheduleReport.setRequestedBy(requestedBy);
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);
        Long requestedById = requestedBy.getId();

        // Get all the leaseLiabilityScheduleReportList where requestedBy equals to requestedById
        defaultLeaseLiabilityScheduleReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the leaseLiabilityScheduleReportList where requestedBy equals to (requestedById + 1)
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportsByLeaseAmortizationScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);
        LeaseAmortizationSchedule leaseAmortizationSchedule;
        if (TestUtil.findAll(em, LeaseAmortizationSchedule.class).isEmpty()) {
            leaseAmortizationSchedule = LeaseAmortizationScheduleResourceIT.createEntity(em);
            em.persist(leaseAmortizationSchedule);
            em.flush();
        } else {
            leaseAmortizationSchedule = TestUtil.findAll(em, LeaseAmortizationSchedule.class).get(0);
        }
        em.persist(leaseAmortizationSchedule);
        em.flush();
        leaseLiabilityScheduleReport.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);
        Long leaseAmortizationScheduleId = leaseAmortizationSchedule.getId();

        // Get all the leaseLiabilityScheduleReportList where leaseAmortizationSchedule equals to leaseAmortizationScheduleId
        defaultLeaseLiabilityScheduleReportShouldBeFound("leaseAmortizationScheduleId.equals=" + leaseAmortizationScheduleId);

        // Get all the leaseLiabilityScheduleReportList where leaseAmortizationSchedule equals to (leaseAmortizationScheduleId + 1)
        defaultLeaseLiabilityScheduleReportShouldNotBeFound("leaseAmortizationScheduleId.equals=" + (leaseAmortizationScheduleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityScheduleReportShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityScheduleReportShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityScheduleReport() throws Exception {
        // Get the leaseLiabilityScheduleReport
        restLeaseLiabilityScheduleReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityScheduleReport() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();

        // Update the leaseLiabilityScheduleReport
        LeaseLiabilityScheduleReport updatedLeaseLiabilityScheduleReport = leaseLiabilityScheduleReportRepository
            .findById(leaseLiabilityScheduleReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityScheduleReport are not directly saved in db
        em.detach(updatedLeaseLiabilityScheduleReport);
        updatedLeaseLiabilityScheduleReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            updatedLeaseLiabilityScheduleReport
        );

        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityScheduleReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleReport testLeaseLiabilityScheduleReport = leaseLiabilityScheduleReportList.get(
            leaseLiabilityScheduleReportList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityScheduleReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityScheduleReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityScheduleReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityScheduleReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityScheduleReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityScheduleReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityScheduleReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository).save(testLeaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityScheduleReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityScheduleReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();

        // Update the leaseLiabilityScheduleReport using partial update
        LeaseLiabilityScheduleReport partialUpdatedLeaseLiabilityScheduleReport = new LeaseLiabilityScheduleReport();
        partialUpdatedLeaseLiabilityScheduleReport.setId(leaseLiabilityScheduleReport.getId());

        partialUpdatedLeaseLiabilityScheduleReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityScheduleReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityScheduleReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleReport testLeaseLiabilityScheduleReport = leaseLiabilityScheduleReportList.get(
            leaseLiabilityScheduleReportList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityScheduleReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityScheduleReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityScheduleReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityScheduleReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityScheduleReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityScheduleReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityScheduleReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityScheduleReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();

        // Update the leaseLiabilityScheduleReport using partial update
        LeaseLiabilityScheduleReport partialUpdatedLeaseLiabilityScheduleReport = new LeaseLiabilityScheduleReport();
        partialUpdatedLeaseLiabilityScheduleReport.setId(leaseLiabilityScheduleReport.getId());

        partialUpdatedLeaseLiabilityScheduleReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityScheduleReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityScheduleReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleReport testLeaseLiabilityScheduleReport = leaseLiabilityScheduleReportList.get(
            leaseLiabilityScheduleReportList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityScheduleReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityScheduleReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityScheduleReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityScheduleReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityScheduleReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityScheduleReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityScheduleReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityScheduleReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityScheduleReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleReportRepository.findAll().size();
        leaseLiabilityScheduleReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleReport
        LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportMapper.toDto(
            leaseLiabilityScheduleReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityScheduleReport in the database
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(0)).save(leaseLiabilityScheduleReport);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityScheduleReport() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);

        int databaseSizeBeforeDelete = leaseLiabilityScheduleReportRepository.findAll().size();

        // Delete the leaseLiabilityScheduleReport
        restLeaseLiabilityScheduleReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityScheduleReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityScheduleReport> leaseLiabilityScheduleReportList = leaseLiabilityScheduleReportRepository.findAll();
        assertThat(leaseLiabilityScheduleReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityScheduleReport in Elasticsearch
        verify(mockLeaseLiabilityScheduleReportSearchRepository, times(1)).deleteById(leaseLiabilityScheduleReport.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityScheduleReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityScheduleReportRepository.saveAndFlush(leaseLiabilityScheduleReport);
        when(mockLeaseLiabilityScheduleReportSearchRepository.search("id:" + leaseLiabilityScheduleReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityScheduleReport), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityScheduleReport
        restLeaseLiabilityScheduleReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityScheduleReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
