package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.AssetAdditionsReport;
import io.github.erp.repository.AssetAdditionsReportRepository;
import io.github.erp.repository.search.AssetAdditionsReportSearchRepository;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
import io.github.erp.service.mapper.AssetAdditionsReportMapper;
import io.github.erp.web.rest.AssetAdditionsReportResource;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AssetAdditionsReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class AssetAdditionsReportResourceIT {

    private static final LocalDate DEFAULT_TIME_OF_REQUEST = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_OF_REQUEST = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TIME_OF_REQUEST = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_REPORT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_REPORT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_END_DATE = LocalDate.ofEpochDay(-1L);

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

    private static final String ENTITY_API_URL = "/api/fixed-asset/report/asset-additions-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/report/_search/asset-additions-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetAdditionsReportRepository assetAdditionsReportRepository;

    @Autowired
    private AssetAdditionsReportMapper assetAdditionsReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetAdditionsReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetAdditionsReportSearchRepository mockAssetAdditionsReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetAdditionsReportMockMvc;

    private AssetAdditionsReport assetAdditionsReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAdditionsReport createEntity(EntityManager em) {
        AssetAdditionsReport assetAdditionsReport = new AssetAdditionsReport()
            .timeOfRequest(DEFAULT_TIME_OF_REQUEST)
            .reportStartDate(DEFAULT_REPORT_START_DATE)
            .reportEndDate(DEFAULT_REPORT_END_DATE)
            .requestId(DEFAULT_REQUEST_ID)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return assetAdditionsReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAdditionsReport createUpdatedEntity(EntityManager em) {
        AssetAdditionsReport assetAdditionsReport = new AssetAdditionsReport()
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportStartDate(UPDATED_REPORT_START_DATE)
            .reportEndDate(UPDATED_REPORT_END_DATE)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return assetAdditionsReport;
    }

    @BeforeEach
    public void initTest() {
        assetAdditionsReport = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeCreate = assetAdditionsReportRepository.findAll().size();
        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);
        restAssetAdditionsReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeCreate + 1);
        AssetAdditionsReport testAssetAdditionsReport = assetAdditionsReportList.get(assetAdditionsReportList.size() - 1);
        assertThat(testAssetAdditionsReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testAssetAdditionsReport.getReportStartDate()).isEqualTo(DEFAULT_REPORT_START_DATE);
        assertThat(testAssetAdditionsReport.getReportEndDate()).isEqualTo(DEFAULT_REPORT_END_DATE);
        assertThat(testAssetAdditionsReport.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        // assertThat(testAssetAdditionsReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testAssetAdditionsReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        // assertThat(testAssetAdditionsReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        // assertThat(testAssetAdditionsReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testAssetAdditionsReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testAssetAdditionsReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the AssetAdditionsReport in Elasticsearch
        // verify(mockAssetAdditionsReportSearchRepository, times(1)).save(testAssetAdditionsReport);
    }

    @Test
    @Transactional
    void createAssetAdditionsReportWithExistingId() throws Exception {
        // Create the AssetAdditionsReport with an existing ID
        assetAdditionsReport.setId(1L);
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        int databaseSizeBeforeCreate = assetAdditionsReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetAdditionsReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReports() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(DEFAULT_TIME_OF_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].reportStartDate").value(hasItem(DEFAULT_REPORT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportEndDate").value(hasItem(DEFAULT_REPORT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    // @Test
    @Transactional
    void getAssetAdditionsReport() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get the assetAdditionsReport
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL_ID, assetAdditionsReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetAdditionsReport.getId().intValue()))
            .andExpect(jsonPath("$.timeOfRequest").value(DEFAULT_TIME_OF_REQUEST.toString()))
            .andExpect(jsonPath("$.reportStartDate").value(DEFAULT_REPORT_START_DATE.toString()))
            .andExpect(jsonPath("$.reportEndDate").value(DEFAULT_REPORT_END_DATE.toString()))
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
    void getAssetAdditionsReportsByIdFiltering() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        Long id = assetAdditionsReport.getId();

        defaultAssetAdditionsReportShouldBeFound("id.equals=" + id);
        defaultAssetAdditionsReportShouldNotBeFound("id.notEquals=" + id);

        defaultAssetAdditionsReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetAdditionsReportShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetAdditionsReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetAdditionsReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest equals to DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.equals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.equals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest not equals to DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.notEquals=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest not equals to UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.notEquals=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest in DEFAULT_TIME_OF_REQUEST or UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.in=" + DEFAULT_TIME_OF_REQUEST + "," + UPDATED_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest equals to UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.in=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest is not null
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.specified=true");

        // Get all the assetAdditionsReportList where timeOfRequest is null
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest is greater than or equal to DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest is greater than or equal to UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest is less than or equal to DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest is less than or equal to SMALLER_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest is less than DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.lessThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest is less than UPDATED_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.lessThan=" + UPDATED_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTimeOfRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where timeOfRequest is greater than DEFAULT_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldNotBeFound("timeOfRequest.greaterThan=" + DEFAULT_TIME_OF_REQUEST);

        // Get all the assetAdditionsReportList where timeOfRequest is greater than SMALLER_TIME_OF_REQUEST
        defaultAssetAdditionsReportShouldBeFound("timeOfRequest.greaterThan=" + SMALLER_TIME_OF_REQUEST);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate equals to DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.equals=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate equals to UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.equals=" + UPDATED_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate not equals to DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.notEquals=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate not equals to UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.notEquals=" + UPDATED_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate in DEFAULT_REPORT_START_DATE or UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.in=" + DEFAULT_REPORT_START_DATE + "," + UPDATED_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate equals to UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.in=" + UPDATED_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate is not null
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.specified=true");

        // Get all the assetAdditionsReportList where reportStartDate is null
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate is greater than or equal to DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.greaterThanOrEqual=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate is greater than or equal to UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.greaterThanOrEqual=" + UPDATED_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate is less than or equal to DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.lessThanOrEqual=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate is less than or equal to SMALLER_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.lessThanOrEqual=" + SMALLER_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate is less than DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.lessThan=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate is less than UPDATED_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.lessThan=" + UPDATED_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportStartDate is greater than DEFAULT_REPORT_START_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportStartDate.greaterThan=" + DEFAULT_REPORT_START_DATE);

        // Get all the assetAdditionsReportList where reportStartDate is greater than SMALLER_REPORT_START_DATE
        defaultAssetAdditionsReportShouldBeFound("reportStartDate.greaterThan=" + SMALLER_REPORT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate equals to DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.equals=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate equals to UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.equals=" + UPDATED_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate not equals to DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.notEquals=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate not equals to UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.notEquals=" + UPDATED_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate in DEFAULT_REPORT_END_DATE or UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.in=" + DEFAULT_REPORT_END_DATE + "," + UPDATED_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate equals to UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.in=" + UPDATED_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate is not null
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.specified=true");

        // Get all the assetAdditionsReportList where reportEndDate is null
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate is greater than or equal to DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.greaterThanOrEqual=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate is greater than or equal to UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.greaterThanOrEqual=" + UPDATED_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate is less than or equal to DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.lessThanOrEqual=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate is less than or equal to SMALLER_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.lessThanOrEqual=" + SMALLER_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate is less than DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.lessThan=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate is less than UPDATED_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.lessThan=" + UPDATED_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportEndDate is greater than DEFAULT_REPORT_END_DATE
        defaultAssetAdditionsReportShouldNotBeFound("reportEndDate.greaterThan=" + DEFAULT_REPORT_END_DATE);

        // Get all the assetAdditionsReportList where reportEndDate is greater than SMALLER_REPORT_END_DATE
        defaultAssetAdditionsReportShouldBeFound("reportEndDate.greaterThan=" + SMALLER_REPORT_END_DATE);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where requestId equals to DEFAULT_REQUEST_ID
        defaultAssetAdditionsReportShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the assetAdditionsReportList where requestId equals to UPDATED_REQUEST_ID
        defaultAssetAdditionsReportShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where requestId not equals to DEFAULT_REQUEST_ID
        defaultAssetAdditionsReportShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the assetAdditionsReportList where requestId not equals to UPDATED_REQUEST_ID
        defaultAssetAdditionsReportShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultAssetAdditionsReportShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the assetAdditionsReportList where requestId equals to UPDATED_REQUEST_ID
        defaultAssetAdditionsReportShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where requestId is not null
        defaultAssetAdditionsReportShouldBeFound("requestId.specified=true");

        // Get all the assetAdditionsReportList where requestId is null
        defaultAssetAdditionsReportShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the assetAdditionsReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the assetAdditionsReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the assetAdditionsReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum is not null
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.specified=true");

        // Get all the assetAdditionsReportList where fileChecksum is null
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the assetAdditionsReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the assetAdditionsReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultAssetAdditionsReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where tampered equals to DEFAULT_TAMPERED
        defaultAssetAdditionsReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the assetAdditionsReportList where tampered equals to UPDATED_TAMPERED
        defaultAssetAdditionsReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where tampered not equals to DEFAULT_TAMPERED
        defaultAssetAdditionsReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the assetAdditionsReportList where tampered not equals to UPDATED_TAMPERED
        defaultAssetAdditionsReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultAssetAdditionsReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the assetAdditionsReportList where tampered equals to UPDATED_TAMPERED
        defaultAssetAdditionsReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where tampered is not null
        defaultAssetAdditionsReportShouldBeFound("tampered.specified=true");

        // Get all the assetAdditionsReportList where tampered is null
        defaultAssetAdditionsReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where filename equals to DEFAULT_FILENAME
        defaultAssetAdditionsReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the assetAdditionsReportList where filename equals to UPDATED_FILENAME
        defaultAssetAdditionsReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where filename not equals to DEFAULT_FILENAME
        defaultAssetAdditionsReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the assetAdditionsReportList where filename not equals to UPDATED_FILENAME
        defaultAssetAdditionsReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultAssetAdditionsReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the assetAdditionsReportList where filename equals to UPDATED_FILENAME
        defaultAssetAdditionsReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where filename is not null
        defaultAssetAdditionsReportShouldBeFound("filename.specified=true");

        // Get all the assetAdditionsReportList where filename is null
        defaultAssetAdditionsReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the assetAdditionsReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the assetAdditionsReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the assetAdditionsReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters is not null
        defaultAssetAdditionsReportShouldBeFound("reportParameters.specified=true");

        // Get all the assetAdditionsReportList where reportParameters is null
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the assetAdditionsReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        // Get all the assetAdditionsReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the assetAdditionsReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultAssetAdditionsReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAssetAdditionsReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);
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
        assetAdditionsReport.setRequestedBy(requestedBy);
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);
        Long requestedById = requestedBy.getId();

        // Get all the assetAdditionsReportList where requestedBy equals to requestedById
        defaultAssetAdditionsReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the assetAdditionsReportList where requestedBy equals to (requestedById + 1)
        defaultAssetAdditionsReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetAdditionsReportShouldBeFound(String filter) throws Exception {
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(DEFAULT_TIME_OF_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].reportStartDate").value(hasItem(DEFAULT_REPORT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportEndDate").value(hasItem(DEFAULT_REPORT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetAdditionsReportShouldNotBeFound(String filter) throws Exception {
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetAdditionsReport() throws Exception {
        // Get the assetAdditionsReport
        restAssetAdditionsReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetAdditionsReport() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();

        // Update the assetAdditionsReport
        AssetAdditionsReport updatedAssetAdditionsReport = assetAdditionsReportRepository.findById(assetAdditionsReport.getId()).get();
        // Disconnect from session so that the updates on updatedAssetAdditionsReport are not directly saved in db
        em.detach(updatedAssetAdditionsReport);
        updatedAssetAdditionsReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportStartDate(UPDATED_REPORT_START_DATE)
            .reportEndDate(UPDATED_REPORT_END_DATE)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(updatedAssetAdditionsReport);

        restAssetAdditionsReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAdditionsReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);
        AssetAdditionsReport testAssetAdditionsReport = assetAdditionsReportList.get(assetAdditionsReportList.size() - 1);
        assertThat(testAssetAdditionsReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testAssetAdditionsReport.getReportStartDate()).isEqualTo(UPDATED_REPORT_START_DATE);
        assertThat(testAssetAdditionsReport.getReportEndDate()).isEqualTo(UPDATED_REPORT_END_DATE);
        assertThat(testAssetAdditionsReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAssetAdditionsReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAssetAdditionsReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testAssetAdditionsReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAssetAdditionsReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAssetAdditionsReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAssetAdditionsReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository).save(testAssetAdditionsReport);
    }

    @Test
    @Transactional
    void putNonExistingAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAdditionsReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void partialUpdateAssetAdditionsReportWithPatch() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();

        // Update the assetAdditionsReport using partial update
        AssetAdditionsReport partialUpdatedAssetAdditionsReport = new AssetAdditionsReport();
        partialUpdatedAssetAdditionsReport.setId(assetAdditionsReport.getId());

        partialUpdatedAssetAdditionsReport
            .reportEndDate(UPDATED_REPORT_END_DATE)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .filename(UPDATED_FILENAME);

        restAssetAdditionsReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAdditionsReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAdditionsReport))
            )
            .andExpect(status().isOk());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);
        AssetAdditionsReport testAssetAdditionsReport = assetAdditionsReportList.get(assetAdditionsReportList.size() - 1);
        assertThat(testAssetAdditionsReport.getTimeOfRequest()).isEqualTo(DEFAULT_TIME_OF_REQUEST);
        assertThat(testAssetAdditionsReport.getReportStartDate()).isEqualTo(DEFAULT_REPORT_START_DATE);
        assertThat(testAssetAdditionsReport.getReportEndDate()).isEqualTo(UPDATED_REPORT_END_DATE);
        assertThat(testAssetAdditionsReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAssetAdditionsReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAssetAdditionsReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testAssetAdditionsReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAssetAdditionsReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testAssetAdditionsReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testAssetAdditionsReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAssetAdditionsReportWithPatch() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();

        // Update the assetAdditionsReport using partial update
        AssetAdditionsReport partialUpdatedAssetAdditionsReport = new AssetAdditionsReport();
        partialUpdatedAssetAdditionsReport.setId(assetAdditionsReport.getId());

        partialUpdatedAssetAdditionsReport
            .timeOfRequest(UPDATED_TIME_OF_REQUEST)
            .reportStartDate(UPDATED_REPORT_START_DATE)
            .reportEndDate(UPDATED_REPORT_END_DATE)
            .requestId(UPDATED_REQUEST_ID)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restAssetAdditionsReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAdditionsReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAdditionsReport))
            )
            .andExpect(status().isOk());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);
        AssetAdditionsReport testAssetAdditionsReport = assetAdditionsReportList.get(assetAdditionsReportList.size() - 1);
        assertThat(testAssetAdditionsReport.getTimeOfRequest()).isEqualTo(UPDATED_TIME_OF_REQUEST);
        assertThat(testAssetAdditionsReport.getReportStartDate()).isEqualTo(UPDATED_REPORT_START_DATE);
        assertThat(testAssetAdditionsReport.getReportEndDate()).isEqualTo(UPDATED_REPORT_END_DATE);
        assertThat(testAssetAdditionsReport.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAssetAdditionsReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAssetAdditionsReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testAssetAdditionsReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAssetAdditionsReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAssetAdditionsReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAssetAdditionsReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetAdditionsReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetAdditionsReport() throws Exception {
        int databaseSizeBeforeUpdate = assetAdditionsReportRepository.findAll().size();
        assetAdditionsReport.setId(count.incrementAndGet());

        // Create the AssetAdditionsReport
        AssetAdditionsReportDTO assetAdditionsReportDTO = assetAdditionsReportMapper.toDto(assetAdditionsReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAdditionsReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAdditionsReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAdditionsReport in the database
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(0)).save(assetAdditionsReport);
    }

    @Test
    @Transactional
    void deleteAssetAdditionsReport() throws Exception {
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);

        int databaseSizeBeforeDelete = assetAdditionsReportRepository.findAll().size();

        // Delete the assetAdditionsReport
        restAssetAdditionsReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetAdditionsReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetAdditionsReport> assetAdditionsReportList = assetAdditionsReportRepository.findAll();
        assertThat(assetAdditionsReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetAdditionsReport in Elasticsearch
        verify(mockAssetAdditionsReportSearchRepository, times(1)).deleteById(assetAdditionsReport.getId());
    }

    @Test
    @Transactional
    void searchAssetAdditionsReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetAdditionsReportRepository.saveAndFlush(assetAdditionsReport);
        when(mockAssetAdditionsReportSearchRepository.search("id:" + assetAdditionsReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetAdditionsReport), PageRequest.of(0, 1), 1));

        // Search the assetAdditionsReport
        restAssetAdditionsReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetAdditionsReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAdditionsReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfRequest").value(hasItem(DEFAULT_TIME_OF_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].reportStartDate").value(hasItem(DEFAULT_REPORT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportEndDate").value(hasItem(DEFAULT_REPORT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
