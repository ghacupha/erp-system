package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.RouAssetListReport;
import io.github.erp.repository.RouAssetListReportRepository;
import io.github.erp.repository.search.RouAssetListReportSearchRepository;
import io.github.erp.service.criteria.RouAssetListReportCriteria;
import io.github.erp.service.dto.RouAssetListReportDTO;
import io.github.erp.service.mapper.RouAssetListReportMapper;
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
 * Integration tests for the {@link RouAssetListReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouAssetListReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/rou-asset-list-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-asset-list-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAssetListReportRepository rouAssetListReportRepository;

    @Autowired
    private RouAssetListReportMapper rouAssetListReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAssetListReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAssetListReportSearchRepository mockRouAssetListReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAssetListReportMockMvc;

    private RouAssetListReport rouAssetListReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetListReport createEntity(EntityManager em) {
        RouAssetListReport rouAssetListReport = new RouAssetListReport()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return rouAssetListReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAssetListReport createUpdatedEntity(EntityManager em) {
        RouAssetListReport rouAssetListReport = new RouAssetListReport()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return rouAssetListReport;
    }

    @BeforeEach
    public void initTest() {
        rouAssetListReport = createEntity(em);
    }

    @Test
    @Transactional
    void createRouAssetListReport() throws Exception {
        int databaseSizeBeforeCreate = rouAssetListReportRepository.findAll().size();
        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);
        restRouAssetListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeCreate + 1);
        RouAssetListReport testRouAssetListReport = rouAssetListReportList.get(rouAssetListReportList.size() - 1);
        assertThat(testRouAssetListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAssetListReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAssetListReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouAssetListReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouAssetListReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouAssetListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouAssetListReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouAssetListReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(1)).save(testRouAssetListReport);
    }

    @Test
    @Transactional
    void createRouAssetListReportWithExistingId() throws Exception {
        // Create the RouAssetListReport with an existing ID
        rouAssetListReport.setId(1L);
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        int databaseSizeBeforeCreate = rouAssetListReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouAssetListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouAssetListReportRepository.findAll().size();
        // set the field null
        rouAssetListReport.setRequestId(null);

        // Create the RouAssetListReport, which fails.
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        restRouAssetListReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouAssetListReports() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReport.getId().intValue())))
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
    void getRouAssetListReport() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get the rouAssetListReport
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAssetListReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAssetListReport.getId().intValue()))
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
    void getRouAssetListReportsByIdFiltering() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        Long id = rouAssetListReport.getId();

        defaultRouAssetListReportShouldBeFound("id.equals=" + id);
        defaultRouAssetListReportShouldNotBeFound("id.notEquals=" + id);

        defaultRouAssetListReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAssetListReportShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAssetListReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAssetListReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultRouAssetListReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAssetListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAssetListReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultRouAssetListReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the rouAssetListReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultRouAssetListReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultRouAssetListReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the rouAssetListReportList where requestId equals to UPDATED_REQUEST_ID
        defaultRouAssetListReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where requestId is not null
        defaultRouAssetListReportShouldBeFound("requestId.specified=true");

        // Get all the rouAssetListReportList where requestId is null
        defaultRouAssetListReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest is not null
        defaultRouAssetListReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the rouAssetListReportList where timeOfRequest is null
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultRouAssetListReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the rouAssetListReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultRouAssetListReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultRouAssetListReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetListReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the rouAssetListReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum is not null
        defaultRouAssetListReportShouldBeFound("fileChecksum.specified=true");

        // Get all the rouAssetListReportList where fileChecksum is null
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultRouAssetListReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetListReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultRouAssetListReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the rouAssetListReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultRouAssetListReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where tampered equals to DEFAULT_TAMPERED
        defaultRouAssetListReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the rouAssetListReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAssetListReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where tampered not equals to DEFAULT_TAMPERED
        defaultRouAssetListReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the rouAssetListReportList where tampered not equals to UPDATED_TAMPERED
        defaultRouAssetListReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultRouAssetListReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the rouAssetListReportList where tampered equals to UPDATED_TAMPERED
        defaultRouAssetListReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where tampered is not null
        defaultRouAssetListReportShouldBeFound("tampered.specified=true");

        // Get all the rouAssetListReportList where tampered is null
        defaultRouAssetListReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where filename equals to DEFAULT_FILENAME
        defaultRouAssetListReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the rouAssetListReportList where filename equals to UPDATED_FILENAME
        defaultRouAssetListReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where filename not equals to DEFAULT_FILENAME
        defaultRouAssetListReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the rouAssetListReportList where filename not equals to UPDATED_FILENAME
        defaultRouAssetListReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultRouAssetListReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the rouAssetListReportList where filename equals to UPDATED_FILENAME
        defaultRouAssetListReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where filename is not null
        defaultRouAssetListReportShouldBeFound("filename.specified=true");

        // Get all the rouAssetListReportList where filename is null
        defaultRouAssetListReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAssetListReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultRouAssetListReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetListReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the rouAssetListReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters is not null
        defaultRouAssetListReportShouldBeFound("reportParameters.specified=true");

        // Get all the rouAssetListReportList where reportParameters is null
        defaultRouAssetListReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultRouAssetListReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetListReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        // Get all the rouAssetListReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultRouAssetListReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the rouAssetListReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultRouAssetListReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllRouAssetListReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);
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
        rouAssetListReport.setRequestedBy(requestedBy);
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);
        Long requestedById = requestedBy.getId();

        // Get all the rouAssetListReportList where requestedBy equals to requestedById
        defaultRouAssetListReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the rouAssetListReportList where requestedBy equals to (requestedById + 1)
        defaultRouAssetListReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAssetListReportShouldBeFound(String filter) throws Exception {
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAssetListReportShouldNotBeFound(String filter) throws Exception {
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAssetListReport() throws Exception {
        // Get the rouAssetListReport
        restRouAssetListReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouAssetListReport() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();

        // Update the rouAssetListReport
        RouAssetListReport updatedRouAssetListReport = rouAssetListReportRepository.findById(rouAssetListReport.getId()).get();
        // Disconnect from session so that the updates on updatedRouAssetListReport are not directly saved in db
        em.detach(updatedRouAssetListReport);
        updatedRouAssetListReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(updatedRouAssetListReport);

        restRouAssetListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAssetListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetListReport testRouAssetListReport = rouAssetListReportList.get(rouAssetListReportList.size() - 1);
        assertThat(testRouAssetListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAssetListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAssetListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAssetListReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAssetListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAssetListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAssetListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAssetListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository).save(testRouAssetListReport);
    }

    @Test
    @Transactional
    void putNonExistingRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouAssetListReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void partialUpdateRouAssetListReportWithPatch() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();

        // Update the rouAssetListReport using partial update
        RouAssetListReport partialUpdatedRouAssetListReport = new RouAssetListReport();
        partialUpdatedRouAssetListReport.setId(rouAssetListReport.getId());

        restRouAssetListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAssetListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAssetListReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetListReport testRouAssetListReport = rouAssetListReportList.get(rouAssetListReportList.size() - 1);
        assertThat(testRouAssetListReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRouAssetListReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testRouAssetListReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testRouAssetListReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testRouAssetListReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testRouAssetListReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testRouAssetListReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testRouAssetListReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRouAssetListReportWithPatch() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();

        // Update the rouAssetListReport using partial update
        RouAssetListReport partialUpdatedRouAssetListReport = new RouAssetListReport();
        partialUpdatedRouAssetListReport.setId(rouAssetListReport.getId());

        partialUpdatedRouAssetListReport
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restRouAssetListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouAssetListReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouAssetListReport))
            )
            .andExpect(status().isOk());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);
        RouAssetListReport testRouAssetListReport = rouAssetListReportList.get(rouAssetListReportList.size() - 1);
        assertThat(testRouAssetListReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRouAssetListReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testRouAssetListReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testRouAssetListReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testRouAssetListReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testRouAssetListReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testRouAssetListReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testRouAssetListReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouAssetListReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouAssetListReport() throws Exception {
        int databaseSizeBeforeUpdate = rouAssetListReportRepository.findAll().size();
        rouAssetListReport.setId(count.incrementAndGet());

        // Create the RouAssetListReport
        RouAssetListReportDTO rouAssetListReportDTO = rouAssetListReportMapper.toDto(rouAssetListReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouAssetListReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouAssetListReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouAssetListReport in the database
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(0)).save(rouAssetListReport);
    }

    @Test
    @Transactional
    void deleteRouAssetListReport() throws Exception {
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);

        int databaseSizeBeforeDelete = rouAssetListReportRepository.findAll().size();

        // Delete the rouAssetListReport
        restRouAssetListReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouAssetListReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouAssetListReport> rouAssetListReportList = rouAssetListReportRepository.findAll();
        assertThat(rouAssetListReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouAssetListReport in Elasticsearch
        verify(mockRouAssetListReportSearchRepository, times(1)).deleteById(rouAssetListReport.getId());
    }

    @Test
    @Transactional
    void searchRouAssetListReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAssetListReportRepository.saveAndFlush(rouAssetListReport);
        when(mockRouAssetListReportSearchRepository.search("id:" + rouAssetListReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAssetListReport), PageRequest.of(0, 1), 1));

        // Search the rouAssetListReport
        restRouAssetListReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAssetListReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAssetListReport.getId().intValue())))
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
