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
import io.github.erp.domain.LeaseLiabilityPostingReport;
import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeaseLiabilityPostingReportRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityPostingReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityPostingReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityPostingReportMapper;
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
 * Integration tests for the {@link LeaseLiabilityPostingReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityPostingReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/lease-liability-posting-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-posting-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityPostingReportRepository leaseLiabilityPostingReportRepository;

    @Autowired
    private LeaseLiabilityPostingReportMapper leaseLiabilityPostingReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityPostingReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityPostingReportSearchRepository mockLeaseLiabilityPostingReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityPostingReportMockMvc;

    private LeaseLiabilityPostingReport leaseLiabilityPostingReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityPostingReport createEntity(EntityManager em) {
        LeaseLiabilityPostingReport leaseLiabilityPostingReport = new LeaseLiabilityPostingReport()
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
        leaseLiabilityPostingReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityPostingReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityPostingReport createUpdatedEntity(EntityManager em) {
        LeaseLiabilityPostingReport leaseLiabilityPostingReport = new LeaseLiabilityPostingReport()
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
        leaseLiabilityPostingReport.setLeasePeriod(leasePeriod);
        return leaseLiabilityPostingReport;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityPostingReport = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityPostingReportRepository.findAll().size();
        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityPostingReport testLeaseLiabilityPostingReport = leaseLiabilityPostingReportList.get(
            leaseLiabilityPostingReportList.size() - 1
        );
        assertThat(testLeaseLiabilityPostingReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testLeaseLiabilityPostingReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityPostingReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityPostingReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testLeaseLiabilityPostingReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testLeaseLiabilityPostingReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityPostingReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testLeaseLiabilityPostingReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(1)).save(testLeaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void createLeaseLiabilityPostingReportWithExistingId() throws Exception {
        // Create the LeaseLiabilityPostingReport with an existing ID
        leaseLiabilityPostingReport.setId(1L);
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        int databaseSizeBeforeCreate = leaseLiabilityPostingReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityPostingReportRepository.findAll().size();
        // set the field null
        leaseLiabilityPostingReport.setRequestId(null);

        // Create the LeaseLiabilityPostingReport, which fails.
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        restLeaseLiabilityPostingReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReports() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReport.getId().intValue())))
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
    void getLeaseLiabilityPostingReport() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get the leaseLiabilityPostingReport
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityPostingReport.getId().intValue()))
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
    void getLeaseLiabilityPostingReportsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        Long id = leaseLiabilityPostingReport.getId();

        defaultLeaseLiabilityPostingReportShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityPostingReportShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityPostingReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityPostingReportShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityPostingReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityPostingReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityPostingReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the leaseLiabilityPostingReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the leaseLiabilityPostingReportList where requestId equals to UPDATED_REQUEST_ID
        defaultLeaseLiabilityPostingReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where requestId is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("requestId.specified=true");

        // Get all the leaseLiabilityPostingReportList where requestId is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the leaseLiabilityPostingReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultLeaseLiabilityPostingReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityPostingReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityPostingReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the leaseLiabilityPostingReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.specified=true");

        // Get all the leaseLiabilityPostingReportList where fileChecksum is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityPostingReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the leaseLiabilityPostingReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultLeaseLiabilityPostingReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where tampered equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityPostingReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityPostingReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityPostingReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where tampered not equals to DEFAULT_TAMPERED
        defaultLeaseLiabilityPostingReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the leaseLiabilityPostingReportList where tampered not equals to UPDATED_TAMPERED
        defaultLeaseLiabilityPostingReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultLeaseLiabilityPostingReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the leaseLiabilityPostingReportList where tampered equals to UPDATED_TAMPERED
        defaultLeaseLiabilityPostingReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where tampered is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("tampered.specified=true");

        // Get all the leaseLiabilityPostingReportList where tampered is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where filename equals to DEFAULT_FILENAME
        defaultLeaseLiabilityPostingReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityPostingReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityPostingReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where filename not equals to DEFAULT_FILENAME
        defaultLeaseLiabilityPostingReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the leaseLiabilityPostingReportList where filename not equals to UPDATED_FILENAME
        defaultLeaseLiabilityPostingReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultLeaseLiabilityPostingReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the leaseLiabilityPostingReportList where filename equals to UPDATED_FILENAME
        defaultLeaseLiabilityPostingReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where filename is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("filename.specified=true");

        // Get all the leaseLiabilityPostingReportList where filename is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityPostingReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityPostingReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the leaseLiabilityPostingReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters is not null
        defaultLeaseLiabilityPostingReportShouldBeFound("reportParameters.specified=true");

        // Get all the leaseLiabilityPostingReportList where reportParameters is null
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityPostingReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        // Get all the leaseLiabilityPostingReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the leaseLiabilityPostingReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultLeaseLiabilityPostingReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);
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
        leaseLiabilityPostingReport.setRequestedBy(requestedBy);
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);
        Long requestedById = requestedBy.getId();

        // Get all the leaseLiabilityPostingReportList where requestedBy equals to requestedById
        defaultLeaseLiabilityPostingReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the leaseLiabilityPostingReportList where requestedBy equals to (requestedById + 1)
        defaultLeaseLiabilityPostingReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportsByLeasePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);
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
        leaseLiabilityPostingReport.setLeasePeriod(leasePeriod);
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);
        Long leasePeriodId = leasePeriod.getId();

        // Get all the leaseLiabilityPostingReportList where leasePeriod equals to leasePeriodId
        defaultLeaseLiabilityPostingReportShouldBeFound("leasePeriodId.equals=" + leasePeriodId);

        // Get all the leaseLiabilityPostingReportList where leasePeriod equals to (leasePeriodId + 1)
        defaultLeaseLiabilityPostingReportShouldNotBeFound("leasePeriodId.equals=" + (leasePeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityPostingReportShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityPostingReportShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityPostingReport() throws Exception {
        // Get the leaseLiabilityPostingReport
        restLeaseLiabilityPostingReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityPostingReport() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();

        // Update the leaseLiabilityPostingReport
        LeaseLiabilityPostingReport updatedLeaseLiabilityPostingReport = leaseLiabilityPostingReportRepository
            .findById(leaseLiabilityPostingReport.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityPostingReport are not directly saved in db
        em.detach(updatedLeaseLiabilityPostingReport);
        updatedLeaseLiabilityPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            updatedLeaseLiabilityPostingReport
        );

        restLeaseLiabilityPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityPostingReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityPostingReport testLeaseLiabilityPostingReport = leaseLiabilityPostingReportList.get(
            leaseLiabilityPostingReportList.size() - 1
        );
        assertThat(testLeaseLiabilityPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityPostingReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityPostingReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityPostingReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityPostingReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityPostingReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityPostingReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository).save(testLeaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityPostingReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityPostingReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();

        // Update the leaseLiabilityPostingReport using partial update
        LeaseLiabilityPostingReport partialUpdatedLeaseLiabilityPostingReport = new LeaseLiabilityPostingReport();
        partialUpdatedLeaseLiabilityPostingReport.setId(leaseLiabilityPostingReport.getId());

        partialUpdatedLeaseLiabilityPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityPostingReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityPostingReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityPostingReport testLeaseLiabilityPostingReport = leaseLiabilityPostingReportList.get(
            leaseLiabilityPostingReportList.size() - 1
        );
        assertThat(testLeaseLiabilityPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityPostingReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityPostingReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityPostingReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityPostingReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityPostingReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityPostingReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityPostingReportWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();

        // Update the leaseLiabilityPostingReport using partial update
        LeaseLiabilityPostingReport partialUpdatedLeaseLiabilityPostingReport = new LeaseLiabilityPostingReport();
        partialUpdatedLeaseLiabilityPostingReport.setId(leaseLiabilityPostingReport.getId());

        partialUpdatedLeaseLiabilityPostingReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restLeaseLiabilityPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityPostingReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityPostingReport))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityPostingReport testLeaseLiabilityPostingReport = leaseLiabilityPostingReportList.get(
            leaseLiabilityPostingReportList.size() - 1
        );
        assertThat(testLeaseLiabilityPostingReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testLeaseLiabilityPostingReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testLeaseLiabilityPostingReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testLeaseLiabilityPostingReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testLeaseLiabilityPostingReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testLeaseLiabilityPostingReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testLeaseLiabilityPostingReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testLeaseLiabilityPostingReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityPostingReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityPostingReport() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityPostingReportRepository.findAll().size();
        leaseLiabilityPostingReport.setId(count.incrementAndGet());

        // Create the LeaseLiabilityPostingReport
        LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportMapper.toDto(
            leaseLiabilityPostingReport
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityPostingReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityPostingReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityPostingReport in the database
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(0)).save(leaseLiabilityPostingReport);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityPostingReport() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);

        int databaseSizeBeforeDelete = leaseLiabilityPostingReportRepository.findAll().size();

        // Delete the leaseLiabilityPostingReport
        restLeaseLiabilityPostingReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityPostingReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityPostingReport> leaseLiabilityPostingReportList = leaseLiabilityPostingReportRepository.findAll();
        assertThat(leaseLiabilityPostingReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityPostingReport in Elasticsearch
        verify(mockLeaseLiabilityPostingReportSearchRepository, times(1)).deleteById(leaseLiabilityPostingReport.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityPostingReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityPostingReportRepository.saveAndFlush(leaseLiabilityPostingReport);
        when(mockLeaseLiabilityPostingReportSearchRepository.search("id:" + leaseLiabilityPostingReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityPostingReport), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityPostingReport
        restLeaseLiabilityPostingReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReport.getId().intValue())))
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
