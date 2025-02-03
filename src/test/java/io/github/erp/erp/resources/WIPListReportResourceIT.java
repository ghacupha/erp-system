package io.github.erp.erp.resources;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.WIPListReport;
import io.github.erp.repository.WIPListReportRepository;
import io.github.erp.repository.search.WIPListReportSearchRepository;
import io.github.erp.service.dto.WIPListReportDTO;
import io.github.erp.service.mapper.WIPListReportMapper;
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
 * Integration tests for the WIPListReportResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class WIPListReportResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

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

    private static final String ENTITY_API_URL = "/api/fixed-asset/wip-list-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/wip-list-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WIPListReportRepository wIPListReportRepository;

    @Autowired
    private WIPListReportMapper wIPListReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WIPListReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private WIPListReportSearchRepository mockWIPListReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWIPListReportMockMvc;

    private WIPListReport wIPListReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPListReport createEntity(EntityManager em) {
        WIPListReport wIPListReport = new WIPListReport()
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .requestId(DEFAULT_REQUEST_ID)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return wIPListReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPListReport createUpdatedEntity(EntityManager em) {
        WIPListReport wIPListReport = new WIPListReport()
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return wIPListReport;
    }

    @BeforeEach
    public void initTest() {
        wIPListReport = createEntity(em);
    }

    @Test
    @Transactional
    void createWIPListReport() throws Exception {
        int databaseSizeBeforeCreate = wIPListReportRepository.findAll().size();
        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);
        restWIPListReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeCreate + 1);
        WIPListReport testWIPListReport = wIPListReportList.get(wIPListReportList.size() - 1);
        assertThat(testWIPListReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testWIPListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testWIPListReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testWIPListReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testWIPListReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testWIPListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testWIPListReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testWIPListReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(1)).save(testWIPListReport);
    }

    @Test
    @Transactional
    void createWIPListReportWithExistingId() throws Exception {
        // Create the WIPListReport with an existing ID
        wIPListReport.setId(1L);
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        int databaseSizeBeforeCreate = wIPListReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWIPListReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void checkTimeOfRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = wIPListReportRepository.findAll().size();
        // set the field null
        wIPListReport.setTimeOfRequest(null);

        // Create the WIPListReport, which fails.
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        restWIPListReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wIPListReportRepository.findAll().size();
        // set the field null
        wIPListReport.setRequestId(null);

        // Create the WIPListReport, which fails.
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        restWIPListReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWIPListReports() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getWIPListReport() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get the wIPListReport
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL_ID, wIPListReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wIPListReport.getId().intValue()))
            .andExpect(jsonPath("$.timeOfRequest").value(sameInstant(DEFAULT_TIME_OF_REQUEST)))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getWIPListReportsByIdFiltering() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        Long id = wIPListReport.getId();

        defaultWIPListReportShouldBeFound("id.equals=" + id);
        defaultWIPListReportShouldNotBeFound("id.notEquals=" + id);

        defaultWIPListReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWIPListReportShouldNotBeFound("id.greaterThan=" + id);

        defaultWIPListReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWIPListReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest is not null
        defaultWIPListReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the wIPListReportList where timeOfRequest is null
        defaultWIPListReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultWIPListReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the wIPListReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultWIPListReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultWIPListReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the wIPListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultWIPListReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultWIPListReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the wIPListReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultWIPListReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultWIPListReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the wIPListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultWIPListReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where requestId is not null
        defaultWIPListReportShouldBeFound("requestId.specified=true");

        // Get all the wIPListReportList where requestId is null
        defaultWIPListReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultWIPListReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultWIPListReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPListReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the wIPListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum is not null
        defaultWIPListReportShouldBeFound("fileChecksum.specified=true");

        // Get all the wIPListReportList where fileChecksum is null
        defaultWIPListReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultWIPListReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPListReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultWIPListReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the wIPListReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultWIPListReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where tampered equals to DEFAULT_TAMPERED
        defaultWIPListReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the wIPListReportList where tampered equals to UPDATED_TAMPERED
        defaultWIPListReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where tampered not equals to DEFAULT_TAMPERED
        defaultWIPListReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the wIPListReportList where tampered not equals to UPDATED_TAMPERED
        defaultWIPListReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultWIPListReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the wIPListReportList where tampered equals to UPDATED_TAMPERED
        defaultWIPListReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where tampered is not null
        defaultWIPListReportShouldBeFound("tampered.specified=true");

        // Get all the wIPListReportList where tampered is null
        defaultWIPListReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where filename equals to DEFAULT_FILENAME
        defaultWIPListReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the wIPListReportList where filename equals to UPDATED_FILENAME
        defaultWIPListReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where filename not equals to DEFAULT_FILENAME
        defaultWIPListReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the wIPListReportList where filename not equals to UPDATED_FILENAME
        defaultWIPListReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultWIPListReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the wIPListReportList where filename equals to UPDATED_FILENAME
        defaultWIPListReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where filename is not null
        defaultWIPListReportShouldBeFound("filename.specified=true");

        // Get all the wIPListReportList where filename is null
        defaultWIPListReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultWIPListReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultWIPListReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPListReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the wIPListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters is not null
        defaultWIPListReportShouldBeFound("reportParameters.specified=true");

        // Get all the wIPListReportList where reportParameters is null
        defaultWIPListReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultWIPListReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPListReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        // Get all the wIPListReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultWIPListReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the wIPListReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultWIPListReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllWIPListReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);
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
        wIPListReport.setRequestedBy(requestedBy);
        wIPListReportRepository.saveAndFlush(wIPListReport);
        Long requestedById = requestedBy.getId();

        // Get all the wIPListReportList where requestedBy equals to requestedById
        defaultWIPListReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the wIPListReportList where requestedBy equals to (requestedById + 1)
        defaultWIPListReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWIPListReportShouldBeFound(String filter) throws Exception {
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWIPListReportShouldNotBeFound(String filter) throws Exception {
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWIPListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWIPListReport() throws Exception {
        // Get the wIPListReport
        restWIPListReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWIPListReport() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();

        // Update the wIPListReport
        WIPListReport updatedWIPListReport = wIPListReportRepository.findById(wIPListReport.getId()).get();
        // Disconnect from session so that the updates on updatedWIPListReport are not directly saved in db
        em.detach(updatedWIPListReport);
        updatedWIPListReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(updatedWIPListReport);

        restWIPListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wIPListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPListReport testWIPListReport = wIPListReportList.get(wIPListReportList.size() - 1);
        assertThat(testWIPListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testWIPListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWIPListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWIPListReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWIPListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWIPListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testWIPListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWIPListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository).save(testWIPListReport);
    }

    @Test
    @Transactional
    void putNonExistingWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wIPListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void partialUpdateWIPListReportWithPatch() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();

        // Update the wIPListReport using partial update
        WIPListReport partialUpdatedWIPListReport = new WIPListReport();
        partialUpdatedWIPListReport.setId(wIPListReport.getId());

        partialUpdatedWIPListReport.timeOfRequest(UPDATED_TIME_OF_REQUEST).tampered(UPDATED_TAMPERED).filename(UPDATED_FILENAME);

        restWIPListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWIPListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWIPListReport))
            )
            .andExpect(status().isOk());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPListReport testWIPListReport = wIPListReportList.get(wIPListReportList.size() - 1);
        assertThat(testWIPListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testWIPListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testWIPListReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testWIPListReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWIPListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWIPListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testWIPListReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testWIPListReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWIPListReportWithPatch() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();

        // Update the wIPListReport using partial update
        WIPListReport partialUpdatedWIPListReport = new WIPListReport();
        partialUpdatedWIPListReport.setId(wIPListReport.getId());

        partialUpdatedWIPListReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restWIPListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWIPListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWIPListReport))
            )
            .andExpect(status().isOk());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);
        WIPListReport testWIPListReport = wIPListReportList.get(wIPListReportList.size() - 1);
        assertThat(testWIPListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testWIPListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testWIPListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testWIPListReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testWIPListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testWIPListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testWIPListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testWIPListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wIPListReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWIPListReport() throws Exception {
        int databaseSizeBeforeUpdate = wIPListReportRepository.findAll().size();
        wIPListReport.setId(count.incrementAndGet());

        // Create the WIPListReport
        WIPListReportDTO wIPListReportDTO = wIPListReportMapper.toDto(wIPListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWIPListReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wIPListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WIPListReport in the database
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(0)).save(wIPListReport);
    }

    @Test
    @Transactional
    void deleteWIPListReport() throws Exception {
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);

        int databaseSizeBeforeDelete = wIPListReportRepository.findAll().size();

        // Delete the wIPListReport
        restWIPListReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, wIPListReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WIPListReport> wIPListReportList = wIPListReportRepository.findAll();
        assertThat(wIPListReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WIPListReport in Elasticsearch
        verify(mockWIPListReportSearchRepository, times(1)).deleteById(wIPListReport.getId());
    }

    @Test
    @Transactional
    void searchWIPListReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        wIPListReportRepository.saveAndFlush(wIPListReport);
        when(mockWIPListReportSearchRepository.search("id:" + wIPListReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(wIPListReport), PageRequest.of(0, 1), 1));

        // Search the wIPListReport
        restWIPListReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + wIPListReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
