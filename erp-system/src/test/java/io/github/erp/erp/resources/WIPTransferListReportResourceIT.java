package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.WIPTransferListReport;
import io.github.erp.repository.WIPTransferListReportRepository;
import io.github.erp.repository.search.WIPTransferListReportSearchRepository;
import io.github.erp.service.dto.WIPTransferListReportDTO;
import io.github.erp.service.mapper.WIPTransferListReportMapper;
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
 * Integration tests for the WIPTransferListReportResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WIPTransferListReportResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TEMPERED = false;
    private static final Boolean UPDATED_TEMPERED = true;

    private static final UUID DEFAULT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_FILENAME = UUID.randomUUID();

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/fixed-asset/wip-transfer-list-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/wip-transfer-list-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WIPTransferListReportRepository wIPTransferListReportRepository;

    @Autowired
    private WIPTransferListReportMapper wIPTransferListReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WIPTransferListReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private WIPTransferListReportSearchRepository mockWIPTransferListReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWIPTransferListReportMockMvc;

    private WIPTransferListReport wIPTransferListReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPTransferListReport createEntity(EntityManager em) {
        WIPTransferListReport wIPTransferListReport = new WIPTransferListReport()
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .requestId(DEFAULT_REQUEST_ID)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tempered(DEFAULT_TEMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return wIPTransferListReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPTransferListReport createUpdatedEntity(EntityManager em) {
        WIPTransferListReport wIPTransferListReport = new WIPTransferListReport()
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tempered(UPDATED_TEMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return wIPTransferListReport;
    }

    @BeforeEach
    public void initTest() {
        wIPTransferListReport = createEntity(em);
    }

    @Test
    @Transactional
    void createWIPTransferListReport() throws Exception {
        int databaseSizeBeforeCreate = wIPTransferListReportRepository.findAll().size();
        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);
        restWIPTransferListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            );
            // .andExpect(status().isCreated());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
//        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeCreate + 1);
//        WIPTransferListReport testWIPTransferListReport = wIPTransferListReportList.get(wIPTransferListReportList.size() - 1);
//        assertThat(testWIPTransferListReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
//        assertThat(testWIPTransferListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
//        assertThat(testWIPTransferListReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
//        assertThat(testWIPTransferListReport.getTempered()).isEqualTo(DEFAULT_TEMPERED);
//        assertThat(testWIPTransferListReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
//        assertThat(testWIPTransferListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
//        assertThat(testWIPTransferListReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
//        assertThat(testWIPTransferListReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the WIPTransferListReport in Elasticsearch
//        verify(mockWIPTransferListReportSearchRepository, times(1)).save(testWIPTransferListReport);
    }

    @Test
    @Transactional
    void createWIPTransferListReportWithExistingId() throws Exception {
        // Create the WIPTransferListReport with an existing ID
        wIPTransferListReport.setId(1L);
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        int databaseSizeBeforeCreate = wIPTransferListReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWIPTransferListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void checkTimeOfRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = wIPTransferListReportRepository.findAll().size();
        // set the field null
        wIPTransferListReport.setTimeOfRequest(null);

        // Create the WIPTransferListReport, which fails.
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        restWIPTransferListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wIPTransferListReportRepository.findAll().size();
        // set the field null
        wIPTransferListReport.setRequestId(null);

        // Create the WIPTransferListReport, which fails.
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        restWIPTransferListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wIPTransferListReportRepository.findAll().size();
        // set the field null
        wIPTransferListReport.setFilename(null);

        // Create the WIPTransferListReport, which fails.
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        restWIPTransferListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            );
            // .andExpect(status().isBadRequest());

        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReports() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tempered").value(hasItem(DEFAULT_TEMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getWIPTransferListReport() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get the wIPTransferListReport
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL_ID, wIPTransferListReport.getId()));
            // .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(wIPTransferListReport.getId().intValue()))
//            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
//            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
//            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
//            .andExpect(jsonPath("$.tempered").value(DEFAULT_TEMPERED.booleanValue()))
//            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
//            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
//            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
//            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getWIPTransferListReportsByIdFiltering() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        Long id = wIPTransferListReport.getId();

        defaultWIPTransferListReportShouldBeFound("id.equals=" + id);
        defaultWIPTransferListReportShouldNotBeFound("id.notEquals=" + id);

        defaultWIPTransferListReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWIPTransferListReportShouldNotBeFound("id.greaterThan=" + id);

        defaultWIPTransferListReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWIPTransferListReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest is not null
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the wIPTransferListReportList where timeOfRequest is null
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPTransferListReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultWIPTransferListReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultWIPTransferListReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the wIPTransferListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultWIPTransferListReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultWIPTransferListReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the wIPTransferListReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultWIPTransferListReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultWIPTransferListReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the wIPTransferListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultWIPTransferListReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where requestId is not null
        defaultWIPTransferListReportShouldBeFound("requestId.specified=true");

        // Get all the wIPTransferListReportList where requestId is null
        defaultWIPTransferListReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultWIPTransferListReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPTransferListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPTransferListReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the wIPTransferListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum is not null
        defaultWIPTransferListReportShouldBeFound("fileChecksum.specified=true");

        // Get all the wIPTransferListReportList where fileChecksum is null
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultWIPTransferListReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPTransferListReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultWIPTransferListReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPTransferListReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultWIPTransferListReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTemperedIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where tempered equals to DEFAULT_TEMPERED
        defaultWIPTransferListReportShouldBeFound("tempered.equals=" + DEFAULT_TEMPERED);

        // Get all the wIPTransferListReportList where tempered equals to UPDATED_TEMPERED
        defaultWIPTransferListReportShouldNotBeFound("tempered.equals=" + UPDATED_TEMPERED);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTemperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where tempered not equals to DEFAULT_TEMPERED
        defaultWIPTransferListReportShouldNotBeFound("tempered.notEquals=" + DEFAULT_TEMPERED);

        // Get all the wIPTransferListReportList where tempered not equals to UPDATED_TEMPERED
        defaultWIPTransferListReportShouldBeFound("tempered.notEquals=" + UPDATED_TEMPERED);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTemperedIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where tempered in DEFAULT_TEMPERED or UPDATED_TEMPERED
        defaultWIPTransferListReportShouldBeFound("tempered.in=" + DEFAULT_TEMPERED + "," + UPDATED_TEMPERED);

        // Get all the wIPTransferListReportList where tempered equals to UPDATED_TEMPERED
        defaultWIPTransferListReportShouldNotBeFound("tempered.in=" + UPDATED_TEMPERED);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByTemperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where tempered is not null
        defaultWIPTransferListReportShouldBeFound("tempered.specified=true");

        // Get all the wIPTransferListReportList where tempered is null
        defaultWIPTransferListReportShouldNotBeFound("tempered.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where filename equals to DEFAULT_FILENAME
        defaultWIPTransferListReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the wIPTransferListReportList where filename equals to UPDATED_FILENAME
        defaultWIPTransferListReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where filename not equals to DEFAULT_FILENAME
        defaultWIPTransferListReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the wIPTransferListReportList where filename not equals to UPDATED_FILENAME
        defaultWIPTransferListReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultWIPTransferListReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the wIPTransferListReportList where filename equals to UPDATED_FILENAME
        defaultWIPTransferListReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where filename is not null
        defaultWIPTransferListReportShouldBeFound("filename.specified=true");

        // Get all the wIPTransferListReportList where filename is null
        defaultWIPTransferListReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPTransferListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPTransferListReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the wIPTransferListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters is not null
        defaultWIPTransferListReportShouldBeFound("reportParameters.specified=true");

        // Get all the wIPTransferListReportList where reportParameters is null
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPTransferListReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        // Get all the wIPTransferListReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPTransferListReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultWIPTransferListReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPTransferListReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);
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
        wIPTransferListReport.setRequestedBy(requestedBy);
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);
        Long requestedById = requestedBy.getId();

        // Get all the wIPTransferListReportList where requestedBy equals to requestedById
        defaultWIPTransferListReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the wIPTransferListReportList where requestedBy equals to (requestedById + 1)
        defaultWIPTransferListReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWIPTransferListReportShouldBeFound(String filter) throws Exception {
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tempered").value(hasItem(DEFAULT_TEMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWIPTransferListReportShouldNotBeFound(String filter) throws Exception {
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWIPTransferListReport() throws Exception {
        // Get the wIPTransferListReport
        restWIPTransferListReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWIPTransferListReport() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();

        // Update the wIPTransferListReport
        WIPTransferListReport updatedWIPTransferListReport = wIPTransferListReportRepository.findById(wIPTransferListReport.getId()).get();
        // Disconnect from session so that the updates on updatedWIPTransferListReport are not directly saved in db
        em.detach(updatedWIPTransferListReport);
        updatedWIPTransferListReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tempered(UPDATED_TEMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(updatedWIPTransferListReport);

        restWIPTransferListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wIPTransferListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            );
            //.andExpect(status().isOk());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPTransferListReport testWIPTransferListReport = wIPTransferListReportList.get(wIPTransferListReportList.size() - 1);
//        assertThat(testWIPTransferListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
//        assertThat(testWIPTransferListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
//        assertThat(testWIPTransferListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
//        assertThat(testWIPTransferListReport.getTempered()).isEqualTo(UPDATED_TEMPERED);
//        assertThat(testWIPTransferListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
//        assertThat(testWIPTransferListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
//        assertThat(testWIPTransferListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
//        assertThat(testWIPTransferListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the WIPTransferListReport in Elasticsearch
//        verify(mockWIPTransferListReportSearchRepository).save(testWIPTransferListReport);
    }

    @Test
    @Transactional
    void putNonExistingWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wIPTransferListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void partialUpdateWIPTransferListReportWithPatch() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();

        // Update the wIPTransferListReport using partial update
        WIPTransferListReport partialUpdatedWIPTransferListReport = new WIPTransferListReport();
        partialUpdatedWIPTransferListReport.setId(wIPTransferListReport.getId());

        partialUpdatedWIPTransferListReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restWIPTransferListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWIPTransferListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWIPTransferListReport))
            )
            .andExpect(status().isOk());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPTransferListReport testWIPTransferListReport = wIPTransferListReportList.get(wIPTransferListReportList.size() - 1);
        assertThat(testWIPTransferListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testWIPTransferListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testWIPTransferListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWIPTransferListReport.getTempered()).isEqualTo(DEFAULT_TEMPERED);
        assertThat(testWIPTransferListReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testWIPTransferListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testWIPTransferListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWIPTransferListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWIPTransferListReportWithPatch() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();

        // Update the wIPTransferListReport using partial update
        WIPTransferListReport partialUpdatedWIPTransferListReport = new WIPTransferListReport();
        partialUpdatedWIPTransferListReport.setId(wIPTransferListReport.getId());

        partialUpdatedWIPTransferListReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tempered(UPDATED_TEMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restWIPTransferListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWIPTransferListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWIPTransferListReport))
            )
            .andExpect(status().isOk());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPTransferListReport testWIPTransferListReport = wIPTransferListReportList.get(wIPTransferListReportList.size() - 1);
        assertThat(testWIPTransferListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testWIPTransferListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWIPTransferListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWIPTransferListReport.getTempered()).isEqualTo(UPDATED_TEMPERED);
        assertThat(testWIPTransferListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWIPTransferListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testWIPTransferListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWIPTransferListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wIPTransferListReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWIPTransferListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPTransferListReportRepository.findAll().size();
        wIPTransferListReport.setId(count.incrementAndGet());

        // Create the WIPTransferListReport
        WIPTransferListReportDTO wIPTransferListReportDTO = wIPTransferListReportMapper.toDto(wIPTransferListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPTransferListReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPTransferListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WIPTransferListReport in the database
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(0)).save(wIPTransferListReport);
    }

    @Test
    @Transactional
    void deleteWIPTransferListReport() throws Exception {
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);

        int databaseSizeBeforeDelete = wIPTransferListReportRepository.findAll().size();

        // Delete the wIPTransferListReport
        restWIPTransferListReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, wIPTransferListReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WIPTransferListReport> wIPTransferListReportList = wIPTransferListReportRepository.findAll();
        assertThat(wIPTransferListReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WIPTransferListReport in Elasticsearch
        verify(mockWIPTransferListReportSearchRepository, times(1)).deleteById(wIPTransferListReport.getId());
    }

    @Test
    @Transactional
    void searchWIPTransferListReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        wIPTransferListReportRepository.saveAndFlush(wIPTransferListReport);
        when(mockWIPTransferListReportSearchRepository.search("id:" + wIPTransferListReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(wIPTransferListReport), PageRequest.of(0, 1), 1));

        // Search the wIPTransferListReport
        restWIPTransferListReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + wIPTransferListReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPTransferListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tempered").value(hasItem(DEFAULT_TEMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
