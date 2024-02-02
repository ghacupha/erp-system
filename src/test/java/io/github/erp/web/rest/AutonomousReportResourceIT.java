package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.AutonomousReport;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.AutonomousReportRepository;
import io.github.erp.repository.search.AutonomousReportSearchRepository;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.criteria.AutonomousReportCriteria;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.mapper.AutonomousReportMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
 * Integration tests for the {@link AutonomousReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AutonomousReportResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_REPORT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_FILENAME = UUID.randomUUID();

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REPORT_TAMPERED = false;
    private static final Boolean UPDATED_REPORT_TAMPERED = true;

    private static final String ENTITY_API_URL = "/api/autonomous-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/autonomous-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AutonomousReportRepository autonomousReportRepository;

    @Mock
    private AutonomousReportRepository autonomousReportRepositoryMock;

    @Autowired
    private AutonomousReportMapper autonomousReportMapper;

    @Mock
    private AutonomousReportService autonomousReportServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AutonomousReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private AutonomousReportSearchRepository mockAutonomousReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutonomousReportMockMvc;

    private AutonomousReport autonomousReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutonomousReport createEntity(EntityManager em) {
        AutonomousReport autonomousReport = new AutonomousReport()
            .reportName(DEFAULT_REPORT_NAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .createdAt(DEFAULT_CREATED_AT)
            .reportFilename(DEFAULT_REPORT_FILENAME)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .reportTampered(DEFAULT_REPORT_TAMPERED);
        return autonomousReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutonomousReport createUpdatedEntity(EntityManager em) {
        AutonomousReport autonomousReport = new AutonomousReport()
            .reportName(UPDATED_REPORT_NAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .createdAt(UPDATED_CREATED_AT)
            .reportFilename(UPDATED_REPORT_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportTampered(UPDATED_REPORT_TAMPERED);
        return autonomousReport;
    }

    @BeforeEach
    public void initTest() {
        autonomousReport = createEntity(em);
    }

    @Test
    @Transactional
    void createAutonomousReport() throws Exception {
        int databaseSizeBeforeCreate = autonomousReportRepository.findAll().size();
        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);
        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeCreate + 1);
        AutonomousReport testAutonomousReport = autonomousReportList.get(autonomousReportList.size() - 1);
        assertThat(testAutonomousReport.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testAutonomousReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testAutonomousReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAutonomousReport.getReportFilename()).isEqualTo(DEFAULT_REPORT_FILENAME);
        assertThat(testAutonomousReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testAutonomousReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testAutonomousReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testAutonomousReport.getReportTampered()).isEqualTo(DEFAULT_REPORT_TAMPERED);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(1)).save(testAutonomousReport);
    }

    @Test
    @Transactional
    void createAutonomousReportWithExistingId() throws Exception {
        // Create the AutonomousReport with an existing ID
        autonomousReport.setId(1L);
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        int databaseSizeBeforeCreate = autonomousReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = autonomousReportRepository.findAll().size();
        // set the field null
        autonomousReport.setReportName(null);

        // Create the AutonomousReport, which fails.
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = autonomousReportRepository.findAll().size();
        // set the field null
        autonomousReport.setCreatedAt(null);

        // Create the AutonomousReport, which fails.
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = autonomousReportRepository.findAll().size();
        // set the field null
        autonomousReport.setReportFilename(null);

        // Create the AutonomousReport, which fails.
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileChecksumIsRequired() throws Exception {
        int databaseSizeBeforeTest = autonomousReportRepository.findAll().size();
        // set the field null
        autonomousReport.setFileChecksum(null);

        // Create the AutonomousReport, which fails.
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        restAutonomousReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutonomousReports() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autonomousReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].reportFilename").value(hasItem(DEFAULT_REPORT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportTampered").value(hasItem(DEFAULT_REPORT_TAMPERED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutonomousReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(autonomousReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutonomousReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(autonomousReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutonomousReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(autonomousReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutonomousReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(autonomousReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAutonomousReport() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get the autonomousReport
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL_ID, autonomousReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autonomousReport.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.reportFilename").value(DEFAULT_REPORT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.reportTampered").value(DEFAULT_REPORT_TAMPERED.booleanValue()));
    }

    @Test
    @Transactional
    void getAutonomousReportsByIdFiltering() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        Long id = autonomousReport.getId();

        defaultAutonomousReportShouldBeFound("id.equals=" + id);
        defaultAutonomousReportShouldNotBeFound("id.notEquals=" + id);

        defaultAutonomousReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAutonomousReportShouldNotBeFound("id.greaterThan=" + id);

        defaultAutonomousReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAutonomousReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName equals to DEFAULT_REPORT_NAME
        defaultAutonomousReportShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the autonomousReportList where reportName equals to UPDATED_REPORT_NAME
        defaultAutonomousReportShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName not equals to DEFAULT_REPORT_NAME
        defaultAutonomousReportShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the autonomousReportList where reportName not equals to UPDATED_REPORT_NAME
        defaultAutonomousReportShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultAutonomousReportShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the autonomousReportList where reportName equals to UPDATED_REPORT_NAME
        defaultAutonomousReportShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName is not null
        defaultAutonomousReportShouldBeFound("reportName.specified=true");

        // Get all the autonomousReportList where reportName is null
        defaultAutonomousReportShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName contains DEFAULT_REPORT_NAME
        defaultAutonomousReportShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the autonomousReportList where reportName contains UPDATED_REPORT_NAME
        defaultAutonomousReportShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportName does not contain DEFAULT_REPORT_NAME
        defaultAutonomousReportShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the autonomousReportList where reportName does not contain UPDATED_REPORT_NAME
        defaultAutonomousReportShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultAutonomousReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the autonomousReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultAutonomousReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the autonomousReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the autonomousReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters is not null
        defaultAutonomousReportShouldBeFound("reportParameters.specified=true");

        // Get all the autonomousReportList where reportParameters is null
        defaultAutonomousReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultAutonomousReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the autonomousReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultAutonomousReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the autonomousReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultAutonomousReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt equals to DEFAULT_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt equals to UPDATED_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt not equals to DEFAULT_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt not equals to UPDATED_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the autonomousReportList where createdAt equals to UPDATED_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt is not null
        defaultAutonomousReportShouldBeFound("createdAt.specified=true");

        // Get all the autonomousReportList where createdAt is null
        defaultAutonomousReportShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt is less than DEFAULT_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt is less than UPDATED_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where createdAt is greater than DEFAULT_CREATED_AT
        defaultAutonomousReportShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the autonomousReportList where createdAt is greater than SMALLER_CREATED_AT
        defaultAutonomousReportShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportFilename equals to DEFAULT_REPORT_FILENAME
        defaultAutonomousReportShouldBeFound("reportFilename.equals=" + DEFAULT_REPORT_FILENAME);

        // Get all the autonomousReportList where reportFilename equals to UPDATED_REPORT_FILENAME
        defaultAutonomousReportShouldNotBeFound("reportFilename.equals=" + UPDATED_REPORT_FILENAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportFilename not equals to DEFAULT_REPORT_FILENAME
        defaultAutonomousReportShouldNotBeFound("reportFilename.notEquals=" + DEFAULT_REPORT_FILENAME);

        // Get all the autonomousReportList where reportFilename not equals to UPDATED_REPORT_FILENAME
        defaultAutonomousReportShouldBeFound("reportFilename.notEquals=" + UPDATED_REPORT_FILENAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportFilename in DEFAULT_REPORT_FILENAME or UPDATED_REPORT_FILENAME
        defaultAutonomousReportShouldBeFound("reportFilename.in=" + DEFAULT_REPORT_FILENAME + "," + UPDATED_REPORT_FILENAME);

        // Get all the autonomousReportList where reportFilename equals to UPDATED_REPORT_FILENAME
        defaultAutonomousReportShouldNotBeFound("reportFilename.in=" + UPDATED_REPORT_FILENAME);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportFilename is not null
        defaultAutonomousReportShouldBeFound("reportFilename.specified=true");

        // Get all the autonomousReportList where reportFilename is null
        defaultAutonomousReportShouldNotBeFound("reportFilename.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultAutonomousReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the autonomousReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultAutonomousReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the autonomousReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the autonomousReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum is not null
        defaultAutonomousReportShouldBeFound("fileChecksum.specified=true");

        // Get all the autonomousReportList where fileChecksum is null
        defaultAutonomousReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultAutonomousReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the autonomousReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultAutonomousReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the autonomousReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultAutonomousReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportTampered equals to DEFAULT_REPORT_TAMPERED
        defaultAutonomousReportShouldBeFound("reportTampered.equals=" + DEFAULT_REPORT_TAMPERED);

        // Get all the autonomousReportList where reportTampered equals to UPDATED_REPORT_TAMPERED
        defaultAutonomousReportShouldNotBeFound("reportTampered.equals=" + UPDATED_REPORT_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportTampered not equals to DEFAULT_REPORT_TAMPERED
        defaultAutonomousReportShouldNotBeFound("reportTampered.notEquals=" + DEFAULT_REPORT_TAMPERED);

        // Get all the autonomousReportList where reportTampered not equals to UPDATED_REPORT_TAMPERED
        defaultAutonomousReportShouldBeFound("reportTampered.notEquals=" + UPDATED_REPORT_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportTampered in DEFAULT_REPORT_TAMPERED or UPDATED_REPORT_TAMPERED
        defaultAutonomousReportShouldBeFound("reportTampered.in=" + DEFAULT_REPORT_TAMPERED + "," + UPDATED_REPORT_TAMPERED);

        // Get all the autonomousReportList where reportTampered equals to UPDATED_REPORT_TAMPERED
        defaultAutonomousReportShouldNotBeFound("reportTampered.in=" + UPDATED_REPORT_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        // Get all the autonomousReportList where reportTampered is not null
        defaultAutonomousReportShouldBeFound("reportTampered.specified=true");

        // Get all the autonomousReportList where reportTampered is null
        defaultAutonomousReportShouldNotBeFound("reportTampered.specified=false");
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByReportMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);
        UniversallyUniqueMapping reportMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            reportMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(reportMapping);
            em.flush();
        } else {
            reportMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(reportMapping);
        em.flush();
        autonomousReport.addReportMapping(reportMapping);
        autonomousReportRepository.saveAndFlush(autonomousReport);
        Long reportMappingId = reportMapping.getId();

        // Get all the autonomousReportList where reportMapping equals to reportMappingId
        defaultAutonomousReportShouldBeFound("reportMappingId.equals=" + reportMappingId);

        // Get all the autonomousReportList where reportMapping equals to (reportMappingId + 1)
        defaultAutonomousReportShouldNotBeFound("reportMappingId.equals=" + (reportMappingId + 1));
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        autonomousReport.addPlaceholder(placeholder);
        autonomousReportRepository.saveAndFlush(autonomousReport);
        Long placeholderId = placeholder.getId();

        // Get all the autonomousReportList where placeholder equals to placeholderId
        defaultAutonomousReportShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the autonomousReportList where placeholder equals to (placeholderId + 1)
        defaultAutonomousReportShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAutonomousReportsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);
        ApplicationUser createdBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            createdBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(createdBy);
            em.flush();
        } else {
            createdBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        autonomousReport.setCreatedBy(createdBy);
        autonomousReportRepository.saveAndFlush(autonomousReport);
        Long createdById = createdBy.getId();

        // Get all the autonomousReportList where createdBy equals to createdById
        defaultAutonomousReportShouldBeFound("createdById.equals=" + createdById);

        // Get all the autonomousReportList where createdBy equals to (createdById + 1)
        defaultAutonomousReportShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutonomousReportShouldBeFound(String filter) throws Exception {
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autonomousReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].reportFilename").value(hasItem(DEFAULT_REPORT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportTampered").value(hasItem(DEFAULT_REPORT_TAMPERED.booleanValue())));

        // Check, that the count call also returns 1
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutonomousReportShouldNotBeFound(String filter) throws Exception {
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutonomousReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAutonomousReport() throws Exception {
        // Get the autonomousReport
        restAutonomousReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAutonomousReport() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();

        // Update the autonomousReport
        AutonomousReport updatedAutonomousReport = autonomousReportRepository.findById(autonomousReport.getId()).get();
        // Disconnect from session so that the updates on updatedAutonomousReport are not directly saved in db
        em.detach(updatedAutonomousReport);
        updatedAutonomousReport
            .reportName(UPDATED_REPORT_NAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .createdAt(UPDATED_CREATED_AT)
            .reportFilename(UPDATED_REPORT_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportTampered(UPDATED_REPORT_TAMPERED);
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(updatedAutonomousReport);

        restAutonomousReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autonomousReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);
        AutonomousReport testAutonomousReport = autonomousReportList.get(autonomousReportList.size() - 1);
        assertThat(testAutonomousReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testAutonomousReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAutonomousReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAutonomousReport.getReportFilename()).isEqualTo(UPDATED_REPORT_FILENAME);
        assertThat(testAutonomousReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAutonomousReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testAutonomousReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAutonomousReport.getReportTampered()).isEqualTo(UPDATED_REPORT_TAMPERED);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository).save(testAutonomousReport);
    }

    @Test
    @Transactional
    void putNonExistingAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autonomousReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void partialUpdateAutonomousReportWithPatch() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();

        // Update the autonomousReport using partial update
        AutonomousReport partialUpdatedAutonomousReport = new AutonomousReport();
        partialUpdatedAutonomousReport.setId(autonomousReport.getId());

        partialUpdatedAutonomousReport.reportName(UPDATED_REPORT_NAME).createdAt(UPDATED_CREATED_AT);

        restAutonomousReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutonomousReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutonomousReport))
            )
            .andExpect(status().isOk());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);
        AutonomousReport testAutonomousReport = autonomousReportList.get(autonomousReportList.size() - 1);
        assertThat(testAutonomousReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testAutonomousReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testAutonomousReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAutonomousReport.getReportFilename()).isEqualTo(DEFAULT_REPORT_FILENAME);
        assertThat(testAutonomousReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testAutonomousReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testAutonomousReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testAutonomousReport.getReportTampered()).isEqualTo(DEFAULT_REPORT_TAMPERED);
    }

    @Test
    @Transactional
    void fullUpdateAutonomousReportWithPatch() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();

        // Update the autonomousReport using partial update
        AutonomousReport partialUpdatedAutonomousReport = new AutonomousReport();
        partialUpdatedAutonomousReport.setId(autonomousReport.getId());

        partialUpdatedAutonomousReport
            .reportName(UPDATED_REPORT_NAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .createdAt(UPDATED_CREATED_AT)
            .reportFilename(UPDATED_REPORT_FILENAME)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportTampered(UPDATED_REPORT_TAMPERED);

        restAutonomousReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutonomousReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutonomousReport))
            )
            .andExpect(status().isOk());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);
        AutonomousReport testAutonomousReport = autonomousReportList.get(autonomousReportList.size() - 1);
        assertThat(testAutonomousReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testAutonomousReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAutonomousReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAutonomousReport.getReportFilename()).isEqualTo(UPDATED_REPORT_FILENAME);
        assertThat(testAutonomousReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAutonomousReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testAutonomousReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAutonomousReport.getReportTampered()).isEqualTo(UPDATED_REPORT_TAMPERED);
    }

    @Test
    @Transactional
    void patchNonExistingAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autonomousReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutonomousReport() throws Exception {
        int databaseSizeBeforeUpdate = autonomousReportRepository.findAll().size();
        autonomousReport.setId(count.incrementAndGet());

        // Create the AutonomousReport
        AutonomousReportDTO autonomousReportDTO = autonomousReportMapper.toDto(autonomousReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutonomousReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autonomousReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutonomousReport in the database
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(0)).save(autonomousReport);
    }

    @Test
    @Transactional
    void deleteAutonomousReport() throws Exception {
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);

        int databaseSizeBeforeDelete = autonomousReportRepository.findAll().size();

        // Delete the autonomousReport
        restAutonomousReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, autonomousReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutonomousReport> autonomousReportList = autonomousReportRepository.findAll();
        assertThat(autonomousReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AutonomousReport in Elasticsearch
        verify(mockAutonomousReportSearchRepository, times(1)).deleteById(autonomousReport.getId());
    }

    @Test
    @Transactional
    void searchAutonomousReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        autonomousReportRepository.saveAndFlush(autonomousReport);
        when(mockAutonomousReportSearchRepository.search("id:" + autonomousReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(autonomousReport), PageRequest.of(0, 1), 1));

        // Search the autonomousReport
        restAutonomousReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + autonomousReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autonomousReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].reportFilename").value(hasItem(DEFAULT_REPORT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportTampered").value(hasItem(DEFAULT_REPORT_TAMPERED.booleanValue())));
    }
}
