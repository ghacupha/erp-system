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
import io.github.erp.domain.LeaseLiabilityReport;
import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeaseLiabilityReportRepository;
import io.github.erp.repository.search.LeaseLiabilityReportSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityReportMapper;
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
 * Integration tests for the {@link LeaseLiabilityReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/lease-liability-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityReportRepository leaseLiabilityReportRepository;

    @Autowired
    private LeaseLiabilityReportMapper leaseLiabilityReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityReportSearchRepository mockLeaseLiabilityReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityReportMockMvc;

    private LeaseLiabilityReport leaseLiabilityReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityReport createEntity(EntityManager em) {
        LeaseLiabilityReport leaseLiabilityReport = new LeaseLiabilityReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        leaseLiabilityReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityReport createUpdatedEntity(EntityManager em) {
        LeaseLiabilityReport leaseLiabilityReport = new LeaseLiabilityReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createUpdatedEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        leaseLiabilityReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityReport;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityReportRepository.findAll().size();
        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);
        restLeaseLiabilityReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityReport testLeaseLiabilityReport = leaseLiabilityReportList.get(leaseLiabilityReportList.size() - 1);
        assertThat(testLeaseLiabilityReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testLeaseLiabilityReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testLeaseLiabilityReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testLeaseLiabilityReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(1)).save(testLeaseLiabilityReport);
    }

    @Test
    @Transactional
    void createLeaseLiabilityReportWithExistingId() throws Exception {
        // Create the LeaseLiabilityReport with an existing ID
        leaseLiabilityReport.setId(1L);
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        int databaseSizeBeforeCreate = leaseLiabilityReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityReportRepository.findAll().size();
        // set the field null
        leaseLiabilityReport.setRequestId(null);

        // Create the LeaseLiabilityReport, which fails.
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        restLeaseLiabilityReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReports() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReport.getId().intValue())))
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
    void getLeaseLiabilityReport() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get the leaseLiabilityReport
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityReport.getId().intValue()))
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
    void getLeaseLiabilityReportsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        Long id = leaseLiabilityReport.getId();

        defaultLeaseLiabilityReportShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityReportShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityReportShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultLeaseLiabilityReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the leaseLiabilityReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where requestId is not null
        defaultLeaseLiabilityReportShouldBeFound("requestId.specified=true");

        // Get all the leaseLiabilityReportList where requestId is null
        defaultLeaseLiabilityReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest is not null
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the leaseLiabilityReportList where timeOfRequest is null
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the leaseLiabilityReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum is not null
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.specified=true");

        // Get all the leaseLiabilityReportList where fileChecksum is null
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where tampered equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where tampered not equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityReportList where tampered not equals to UPDATED_TAMPERED
        defaultLeaseLiabilityReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultLeaseLiabilityReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the leaseLiabilityReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where tampered is not null
        defaultLeaseLiabilityReportShouldBeFound("tampered.specified=true");

        // Get all the leaseLiabilityReportList where tampered is null
        defaultLeaseLiabilityReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where filename equals to DEFAULT_FILENAME
        defaultLeaseLiabilityReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where filename not equals to DEFAULT_FILENAME
        defaultLeaseLiabilityReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityReportList where filename not equals to UPDATED_FILENAME
        defaultLeaseLiabilityReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultLeaseLiabilityReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the leaseLiabilityReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where filename is not null
        defaultLeaseLiabilityReportShouldBeFound("filename.specified=true");

        // Get all the leaseLiabilityReportList where filename is null
        defaultLeaseLiabilityReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the leaseLiabilityReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters is not null
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.specified=true");

        // Get all the leaseLiabilityReportList where reportParameters is null
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        // Get all the leaseLiabilityReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);
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
        leaseLiabilityReport.setRequestedBy(requestedBy);
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);
        Long requestedById = requestedBy.getId();

        // Get all the leaseLiabilityReportList where requestedBy equals to requestedById
        defaultLeaseLiabilityReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the leaseLiabilityReportList where requestedBy equals to (requestedById + 1)
        defaultLeaseLiabilityReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportsByLeasePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        em.persist(leasePeriod);
        em.flush();
        leaseLiabilityReport.setLeasePeriod(leasePeriod);
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);
        Long leasePeriodId = leasePeriod.getId();

        // Get all the leaseLiabilityReportList where leasePeriod equals to leasePeriodId
        defaultLeaseLiabilityReportShouldBeFound("leasePeriodId.equals=" + leasePeriodId);

        // Get all the leaseLiabilityReportList where leasePeriod equals to (leasePeriodId + 1)
        defaultLeaseLiabilityReportShouldNotBeFound("leasePeriodId.equals=" + (leasePeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityReportShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityReportShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityReport() throws Exception {
        // Get the leaseLiabilityReport
        restLeaseLiabilityReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityReport() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();

        // Update the leaseLiabilityReport
        LeaseLiabilityReport updatedLeaseLiabilityReport = leaseLiabilityReportRepository.findById(leaseLiabilityReport.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityReport are not directly saved in db
        em.detach(updatedLeaseLiabilityReport);
        updatedLeaseLiabilityReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(updatedLeaseLiabilityReport);

        restLeaseLiabilityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityReport testLeaseLiabilityReport = leaseLiabilityReportList.get(leaseLiabilityReportList.size() - 1);
        assertThat(testLeaseLiabilityReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository).save(testLeaseLiabilityReport);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();

        // Update the leaseLiabilityReport using partial update
        LeaseLiabilityReport partialUpdatedLeaseLiabilityReport = new LeaseLiabilityReport();
        partialUpdatedLeaseLiabilityReport.setId(leaseLiabilityReport.getId());

        partialUpdatedLeaseLiabilityReport
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityReport testLeaseLiabilityReport = leaseLiabilityReportList.get(leaseLiabilityReportList.size() - 1);
        assertThat(testLeaseLiabilityReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testLeaseLiabilityReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();

        // Update the leaseLiabilityReport using partial update
        LeaseLiabilityReport partialUpdatedLeaseLiabilityReport = new LeaseLiabilityReport();
        partialUpdatedLeaseLiabilityReport.setId(leaseLiabilityReport.getId());

        partialUpdatedLeaseLiabilityReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityReport testLeaseLiabilityReport = leaseLiabilityReportList.get(leaseLiabilityReportList.size() - 1);
        assertThat(testLeaseLiabilityReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityReportRepository.findAll().size();
        leaseLiabilityReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityReport
        LeaseLiabilityReportDTO leaseLiabilityReportDTO = leaseLiabilityReportMapper.toDto(leaseLiabilityReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityReport in the database
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(0)).save(leaseLiabilityReport);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityReport() throws Exception {
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);

        int databaseSizeBeforeDelete = leaseLiabilityReportRepository.findAll().size();

        // Delete the leaseLiabilityReport
        restLeaseLiabilityReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityReport> leaseLiabilityReportList = leaseLiabilityReportRepository.findAll();
        assertThat(leaseLiabilityReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityReport in Elasticsearch
        verify(mockLeaseLiabilityReportSearchRepository, times(1)).deleteById(leaseLiabilityReport.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityReportRepository.saveAndFlush(leaseLiabilityReport);
        when(mockLeaseLiabilityReportSearchRepository.search("id:" + leaseLiabilityReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityReport), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityReport
        restLeaseLiabilityReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReport.getId().intValue())))
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
