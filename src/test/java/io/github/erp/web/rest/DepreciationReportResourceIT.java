package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.DepreciationReport;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.DepreciationReportRepository;
import io.github.erp.repository.search.DepreciationReportSearchRepository;
import io.github.erp.service.criteria.DepreciationReportCriteria;
import io.github.erp.service.dto.DepreciationReportDTO;
import io.github.erp.service.mapper.DepreciationReportMapper;
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
 * Integration tests for the {@link DepreciationReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationReportResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME_OF_REPORT_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REPORT_REQUEST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REPORT_REQUEST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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

    private static final String ENTITY_API_URL = "/api/depreciation-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationReportRepository depreciationReportRepository;

    @Autowired
    private DepreciationReportMapper depreciationReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationReportSearchRepository mockDepreciationReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationReportMockMvc;

    private DepreciationReport depreciationReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationReport createEntity(EntityManager em) {
        DepreciationReport depreciationReport = new DepreciationReport()
            .reportName(DEFAULT_REPORT_NAME)
            .timeOfReportRequest(DEFAULT_TIME_OF_REPORT_REQUEST)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        depreciationReport.setDepreciationPeriod(depreciationPeriod);
        return depreciationReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationReport createUpdatedEntity(EntityManager em) {
        DepreciationReport depreciationReport = new DepreciationReport()
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        depreciationReport.setDepreciationPeriod(depreciationPeriod);
        return depreciationReport;
    }

    @BeforeEach
    public void initTest() {
        depreciationReport = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationReport() throws Exception {
        int databaseSizeBeforeCreate = depreciationReportRepository.findAll().size();
        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);
        restDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationReport testDepreciationReport = depreciationReportList.get(depreciationReportList.size() - 1);
        assertThat(testDepreciationReport.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testDepreciationReport.getTimeOfReportRequest()).isEqualTo(DEFAULT_TIME_OF_REPORT_REQUEST);
        assertThat(testDepreciationReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testDepreciationReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testDepreciationReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testDepreciationReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testDepreciationReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testDepreciationReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(1)).save(testDepreciationReport);
    }

    @Test
    @Transactional
    void createDepreciationReportWithExistingId() throws Exception {
        // Create the DepreciationReport with an existing ID
        depreciationReport.setId(1L);
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        int databaseSizeBeforeCreate = depreciationReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationReportRepository.findAll().size();
        // set the field null
        depreciationReport.setReportName(null);

        // Create the DepreciationReport, which fails.
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        restDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfReportRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationReportRepository.findAll().size();
        // set the field null
        depreciationReport.setTimeOfReportRequest(null);

        // Create the DepreciationReport, which fails.
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        restDepreciationReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepreciationReports() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfReportRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getDepreciationReport() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get the depreciationReport
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationReport.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.timeOfReportRequest").value(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getDepreciationReportsByIdFiltering() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        Long id = depreciationReport.getId();

        defaultDepreciationReportShouldBeFound("id.equals=" + id);
        defaultDepreciationReportShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationReportShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName equals to DEFAULT_REPORT_NAME
        defaultDepreciationReportShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the depreciationReportList where reportName equals to UPDATED_REPORT_NAME
        defaultDepreciationReportShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName not equals to DEFAULT_REPORT_NAME
        defaultDepreciationReportShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the depreciationReportList where reportName not equals to UPDATED_REPORT_NAME
        defaultDepreciationReportShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultDepreciationReportShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the depreciationReportList where reportName equals to UPDATED_REPORT_NAME
        defaultDepreciationReportShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName is not null
        defaultDepreciationReportShouldBeFound("reportName.specified=true");

        // Get all the depreciationReportList where reportName is null
        defaultDepreciationReportShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName contains DEFAULT_REPORT_NAME
        defaultDepreciationReportShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the depreciationReportList where reportName contains UPDATED_REPORT_NAME
        defaultDepreciationReportShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportName does not contain DEFAULT_REPORT_NAME
        defaultDepreciationReportShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the depreciationReportList where reportName does not contain UPDATED_REPORT_NAME
        defaultDepreciationReportShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest equals to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.equals=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.equals=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest not equals to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.notEquals=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest not equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.notEquals=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest in DEFAULT_TIME_OF_REPORT_REQUEST or UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound(
            "timeOfReportRequest.in=" + DEFAULT_TIME_OF_REPORT_REQUEST + "," + UPDATED_TIME_OF_REPORT_REQUEST
        );

        // Get all the depreciationReportList where timeOfReportRequest equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.in=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest is not null
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.specified=true");

        // Get all the depreciationReportList where timeOfReportRequest is null
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest is greater than or equal to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest is greater than or equal to UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest is less than or equal to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest is less than or equal to SMALLER_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest is less than DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.lessThan=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest is less than UPDATED_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.lessThan=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTimeOfReportRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where timeOfReportRequest is greater than DEFAULT_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldNotBeFound("timeOfReportRequest.greaterThan=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the depreciationReportList where timeOfReportRequest is greater than SMALLER_TIME_OF_REPORT_REQUEST
        defaultDepreciationReportShouldBeFound("timeOfReportRequest.greaterThan=" + SMALLER_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultDepreciationReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the depreciationReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultDepreciationReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the depreciationReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the depreciationReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum is not null
        defaultDepreciationReportShouldBeFound("fileChecksum.specified=true");

        // Get all the depreciationReportList where fileChecksum is null
        defaultDepreciationReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultDepreciationReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the depreciationReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultDepreciationReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the depreciationReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultDepreciationReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where tampered equals to DEFAULT_TAMPERED
        defaultDepreciationReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the depreciationReportList where tampered equals to UPDATED_TAMPERED
        defaultDepreciationReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where tampered not equals to DEFAULT_TAMPERED
        defaultDepreciationReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the depreciationReportList where tampered not equals to UPDATED_TAMPERED
        defaultDepreciationReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultDepreciationReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the depreciationReportList where tampered equals to UPDATED_TAMPERED
        defaultDepreciationReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where tampered is not null
        defaultDepreciationReportShouldBeFound("tampered.specified=true");

        // Get all the depreciationReportList where tampered is null
        defaultDepreciationReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where filename equals to DEFAULT_FILENAME
        defaultDepreciationReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the depreciationReportList where filename equals to UPDATED_FILENAME
        defaultDepreciationReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where filename not equals to DEFAULT_FILENAME
        defaultDepreciationReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the depreciationReportList where filename not equals to UPDATED_FILENAME
        defaultDepreciationReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultDepreciationReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the depreciationReportList where filename equals to UPDATED_FILENAME
        defaultDepreciationReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where filename is not null
        defaultDepreciationReportShouldBeFound("filename.specified=true");

        // Get all the depreciationReportList where filename is null
        defaultDepreciationReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultDepreciationReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the depreciationReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultDepreciationReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the depreciationReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the depreciationReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters is not null
        defaultDepreciationReportShouldBeFound("reportParameters.specified=true");

        // Get all the depreciationReportList where reportParameters is null
        defaultDepreciationReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultDepreciationReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the depreciationReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        // Get all the depreciationReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultDepreciationReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the depreciationReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultDepreciationReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);
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
        depreciationReport.setRequestedBy(requestedBy);
        depreciationReportRepository.saveAndFlush(depreciationReport);
        Long requestedById = requestedBy.getId();

        // Get all the depreciationReportList where requestedBy equals to requestedById
        defaultDepreciationReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the depreciationReportList where requestedBy equals to (requestedById + 1)
        defaultDepreciationReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(depreciationPeriod);
        em.flush();
        depreciationReport.setDepreciationPeriod(depreciationPeriod);
        depreciationReportRepository.saveAndFlush(depreciationReport);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the depreciationReportList where depreciationPeriod equals to depreciationPeriodId
        defaultDepreciationReportShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the depreciationReportList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultDepreciationReportShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(serviceOutlet);
        em.flush();
        depreciationReport.setServiceOutlet(serviceOutlet);
        depreciationReportRepository.saveAndFlush(depreciationReport);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the depreciationReportList where serviceOutlet equals to serviceOutletId
        defaultDepreciationReportShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the depreciationReportList where serviceOutlet equals to (serviceOutletId + 1)
        defaultDepreciationReportShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationReportsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        em.persist(assetCategory);
        em.flush();
        depreciationReport.setAssetCategory(assetCategory);
        depreciationReportRepository.saveAndFlush(depreciationReport);
        Long assetCategoryId = assetCategory.getId();

        // Get all the depreciationReportList where assetCategory equals to assetCategoryId
        defaultDepreciationReportShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the depreciationReportList where assetCategory equals to (assetCategoryId + 1)
        defaultDepreciationReportShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationReportShouldBeFound(String filter) throws Exception {
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfReportRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationReportShouldNotBeFound(String filter) throws Exception {
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationReport() throws Exception {
        // Get the depreciationReport
        restDepreciationReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationReport() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();

        // Update the depreciationReport
        DepreciationReport updatedDepreciationReport = depreciationReportRepository.findById(depreciationReport.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationReport are not directly saved in db
        em.detach(updatedDepreciationReport);
        updatedDepreciationReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(updatedDepreciationReport);

        restDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);
        DepreciationReport testDepreciationReport = depreciationReportList.get(depreciationReportList.size() - 1);
        assertThat(testDepreciationReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testDepreciationReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testDepreciationReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testDepreciationReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testDepreciationReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testDepreciationReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testDepreciationReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testDepreciationReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository).save(testDepreciationReport);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationReportWithPatch() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();

        // Update the depreciationReport using partial update
        DepreciationReport partialUpdatedDepreciationReport = new DepreciationReport();
        partialUpdatedDepreciationReport.setId(depreciationReport.getId());

        partialUpdatedDepreciationReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationReport))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);
        DepreciationReport testDepreciationReport = depreciationReportList.get(depreciationReportList.size() - 1);
        assertThat(testDepreciationReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testDepreciationReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testDepreciationReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testDepreciationReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testDepreciationReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testDepreciationReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testDepreciationReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testDepreciationReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationReportWithPatch() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();

        // Update the depreciationReport using partial update
        DepreciationReport partialUpdatedDepreciationReport = new DepreciationReport();
        partialUpdatedDepreciationReport.setId(depreciationReport.getId());

        partialUpdatedDepreciationReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationReport))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);
        DepreciationReport testDepreciationReport = depreciationReportList.get(depreciationReportList.size() - 1);
        assertThat(testDepreciationReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testDepreciationReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testDepreciationReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testDepreciationReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testDepreciationReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testDepreciationReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testDepreciationReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testDepreciationReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationReport() throws Exception {
        int databaseSizeBeforeUpdate = depreciationReportRepository.findAll().size();
        depreciationReport.setId(count.incrementAndGet());

        // Create the DepreciationReport
        DepreciationReportDTO depreciationReportDTO = depreciationReportMapper.toDto(depreciationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationReport in the database
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(0)).save(depreciationReport);
    }

    @Test
    @Transactional
    void deleteDepreciationReport() throws Exception {
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);

        int databaseSizeBeforeDelete = depreciationReportRepository.findAll().size();

        // Delete the depreciationReport
        restDepreciationReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationReport> depreciationReportList = depreciationReportRepository.findAll();
        assertThat(depreciationReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationReport in Elasticsearch
        verify(mockDepreciationReportSearchRepository, times(1)).deleteById(depreciationReport.getId());
    }

    @Test
    @Transactional
    void searchDepreciationReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationReportRepository.saveAndFlush(depreciationReport);
        when(mockDepreciationReportSearchRepository.search("id:" + depreciationReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationReport), PageRequest.of(0, 1), 1));

        // Search the depreciationReport
        restDepreciationReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfReportRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
