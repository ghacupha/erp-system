package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.NbvReportRepository;
import io.github.erp.repository.search.NbvReportSearchRepository;
import io.github.erp.service.dto.NbvReportDTO;
import io.github.erp.service.mapper.NbvReportMapper;
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
 * Integration tests for the NbvReportResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class NbvReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/fixed-asset/nbv-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/nbv-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NbvReportRepository nbvReportRepository;

    @Autowired
    private NbvReportMapper nbvReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NbvReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private NbvReportSearchRepository mockNbvReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNbvReportMockMvc;

    private NbvReport nbvReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvReport createEntity(EntityManager em) {
        NbvReport nbvReport = new NbvReport()
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
        nbvReport.setDepreciationPeriod(depreciationPeriod);
        return nbvReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvReport createUpdatedEntity(EntityManager em) {
        NbvReport nbvReport = new NbvReport()
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
        nbvReport.setDepreciationPeriod(depreciationPeriod);
        return nbvReport;
    }

    @BeforeEach
    public void initTest() {
        nbvReport = createEntity(em);
    }

    // @Test
    @Transactional
    void createNbvReport() throws Exception {
        int databaseSizeBeforeCreate = nbvReportRepository.findAll().size();
        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);
        restNbvReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvReportDTO)))
            .andExpect(status().isCreated());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeCreate + 1);
        NbvReport testNbvReport = nbvReportList.get(nbvReportList.size() - 1);
        assertThat(testNbvReport.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testNbvReport.getTimeOfReportRequest()).isEqualTo(DEFAULT_TIME_OF_REPORT_REQUEST);
        assertThat(testNbvReport.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testNbvReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testNbvReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testNbvReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testNbvReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testNbvReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(1)).save(testNbvReport);
    }

    @Test
    @Transactional
    void createNbvReportWithExistingId() throws Exception {
        // Create the NbvReport with an existing ID
        nbvReport.setId(1L);
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        int databaseSizeBeforeCreate = nbvReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNbvReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nbvReportRepository.findAll().size();
        // set the field null
        nbvReport.setReportName(null);

        // Create the NbvReport, which fails.
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        restNbvReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvReportDTO)))
            .andExpect(status().isBadRequest());

        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfReportRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = nbvReportRepository.findAll().size();
        // set the field null
        nbvReport.setTimeOfReportRequest(null);

        // Create the NbvReport, which fails.
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        restNbvReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvReportDTO)))
            .andExpect(status().isBadRequest());

        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNbvReports() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfReportRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    // @Test
    @Transactional
    void getNbvReport() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get the nbvReport
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL_ID, nbvReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nbvReport.getId().intValue()))
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
    void getNbvReportsByIdFiltering() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        Long id = nbvReport.getId();

        defaultNbvReportShouldBeFound("id.equals=" + id);
        defaultNbvReportShouldNotBeFound("id.notEquals=" + id);

        defaultNbvReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNbvReportShouldNotBeFound("id.greaterThan=" + id);

        defaultNbvReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNbvReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName equals to DEFAULT_REPORT_NAME
        defaultNbvReportShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the nbvReportList where reportName equals to UPDATED_REPORT_NAME
        defaultNbvReportShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName not equals to DEFAULT_REPORT_NAME
        defaultNbvReportShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the nbvReportList where reportName not equals to UPDATED_REPORT_NAME
        defaultNbvReportShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultNbvReportShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the nbvReportList where reportName equals to UPDATED_REPORT_NAME
        defaultNbvReportShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName is not null
        defaultNbvReportShouldBeFound("reportName.specified=true");

        // Get all the nbvReportList where reportName is null
        defaultNbvReportShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName contains DEFAULT_REPORT_NAME
        defaultNbvReportShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the nbvReportList where reportName contains UPDATED_REPORT_NAME
        defaultNbvReportShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportName does not contain DEFAULT_REPORT_NAME
        defaultNbvReportShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the nbvReportList where reportName does not contain UPDATED_REPORT_NAME
        defaultNbvReportShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest equals to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.equals=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.equals=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest not equals to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.notEquals=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest not equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.notEquals=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest in DEFAULT_TIME_OF_REPORT_REQUEST or UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.in=" + DEFAULT_TIME_OF_REPORT_REQUEST + "," + UPDATED_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest equals to UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.in=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest is not null
        defaultNbvReportShouldBeFound("timeOfReportRequest.specified=true");

        // Get all the nbvReportList where timeOfReportRequest is null
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest is greater than or equal to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.greaterThanOrEqual=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest is greater than or equal to UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.greaterThanOrEqual=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest is less than or equal to DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.lessThanOrEqual=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest is less than or equal to SMALLER_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.lessThanOrEqual=" + SMALLER_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsLessThanSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest is less than DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.lessThan=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest is less than UPDATED_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.lessThan=" + UPDATED_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTimeOfReportRequestIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where timeOfReportRequest is greater than DEFAULT_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldNotBeFound("timeOfReportRequest.greaterThan=" + DEFAULT_TIME_OF_REPORT_REQUEST);

        // Get all the nbvReportList where timeOfReportRequest is greater than SMALLER_TIME_OF_REPORT_REQUEST
        defaultNbvReportShouldBeFound("timeOfReportRequest.greaterThan=" + SMALLER_TIME_OF_REPORT_REQUEST);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultNbvReportShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the nbvReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultNbvReportShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the nbvReportList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the nbvReportList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum is not null
        defaultNbvReportShouldBeFound("fileChecksum.specified=true");

        // Get all the nbvReportList where fileChecksum is null
        defaultNbvReportShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultNbvReportShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the nbvReportList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultNbvReportShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the nbvReportList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultNbvReportShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where tampered equals to DEFAULT_TAMPERED
        defaultNbvReportShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the nbvReportList where tampered equals to UPDATED_TAMPERED
        defaultNbvReportShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where tampered not equals to DEFAULT_TAMPERED
        defaultNbvReportShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the nbvReportList where tampered not equals to UPDATED_TAMPERED
        defaultNbvReportShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultNbvReportShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the nbvReportList where tampered equals to UPDATED_TAMPERED
        defaultNbvReportShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllNbvReportsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where tampered is not null
        defaultNbvReportShouldBeFound("tampered.specified=true");

        // Get all the nbvReportList where tampered is null
        defaultNbvReportShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where filename equals to DEFAULT_FILENAME
        defaultNbvReportShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the nbvReportList where filename equals to UPDATED_FILENAME
        defaultNbvReportShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where filename not equals to DEFAULT_FILENAME
        defaultNbvReportShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the nbvReportList where filename not equals to UPDATED_FILENAME
        defaultNbvReportShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultNbvReportShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the nbvReportList where filename equals to UPDATED_FILENAME
        defaultNbvReportShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllNbvReportsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where filename is not null
        defaultNbvReportShouldBeFound("filename.specified=true");

        // Get all the nbvReportList where filename is null
        defaultNbvReportShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultNbvReportShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the nbvReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultNbvReportShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the nbvReportList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldBeFound("reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS);

        // Get all the nbvReportList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters is not null
        defaultNbvReportShouldBeFound("reportParameters.specified=true");

        // Get all the nbvReportList where reportParameters is null
        defaultNbvReportShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultNbvReportShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the nbvReportList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllNbvReportsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        // Get all the nbvReportList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultNbvReportShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the nbvReportList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultNbvReportShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllNbvReportsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);
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
        nbvReport.setRequestedBy(requestedBy);
        nbvReportRepository.saveAndFlush(nbvReport);
        Long requestedById = requestedBy.getId();

        // Get all the nbvReportList where requestedBy equals to requestedById
        defaultNbvReportShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the nbvReportList where requestedBy equals to (requestedById + 1)
        defaultNbvReportShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllNbvReportsByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);
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
        nbvReport.setDepreciationPeriod(depreciationPeriod);
        nbvReportRepository.saveAndFlush(nbvReport);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the nbvReportList where depreciationPeriod equals to depreciationPeriodId
        defaultNbvReportShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the nbvReportList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultNbvReportShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllNbvReportsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);
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
        nbvReport.setServiceOutlet(serviceOutlet);
        nbvReportRepository.saveAndFlush(nbvReport);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the nbvReportList where serviceOutlet equals to serviceOutletId
        defaultNbvReportShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the nbvReportList where serviceOutlet equals to (serviceOutletId + 1)
        defaultNbvReportShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllNbvReportsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);
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
        nbvReport.setAssetCategory(assetCategory);
        nbvReportRepository.saveAndFlush(nbvReport);
        Long assetCategoryId = assetCategory.getId();

        // Get all the nbvReportList where assetCategory equals to assetCategoryId
        defaultNbvReportShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the nbvReportList where assetCategory equals to (assetCategoryId + 1)
        defaultNbvReportShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNbvReportShouldBeFound(String filter) throws Exception {
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfReportRequest").value(hasItem(sameInstant(DEFAULT_TIME_OF_REPORT_REQUEST))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNbvReportShouldNotBeFound(String filter) throws Exception {
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNbvReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNbvReport() throws Exception {
        // Get the nbvReport
        restNbvReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNbvReport() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();

        // Update the nbvReport
        NbvReport updatedNbvReport = nbvReportRepository.findById(nbvReport.getId()).get();
        // Disconnect from session so that the updates on updatedNbvReport are not directly saved in db
        em.detach(updatedNbvReport);
        updatedNbvReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(updatedNbvReport);

        restNbvReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);
        NbvReport testNbvReport = nbvReportList.get(nbvReportList.size() - 1);
        assertThat(testNbvReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testNbvReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testNbvReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testNbvReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testNbvReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testNbvReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testNbvReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testNbvReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository).save(testNbvReport);
    }

    @Test
    @Transactional
    void putNonExistingNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nbvReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void putWithIdMismatchNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nbvReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void partialUpdateNbvReportWithPatch() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();

        // Update the nbvReport using partial update
        NbvReport partialUpdatedNbvReport = new NbvReport();
        partialUpdatedNbvReport.setId(nbvReport.getId());

        partialUpdatedNbvReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM);

        restNbvReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvReport))
            )
            .andExpect(status().isOk());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);
        NbvReport testNbvReport = nbvReportList.get(nbvReportList.size() - 1);
        assertThat(testNbvReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testNbvReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testNbvReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testNbvReport.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testNbvReport.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testNbvReport.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testNbvReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testNbvReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateNbvReportWithPatch() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();

        // Update the nbvReport using partial update
        NbvReport partialUpdatedNbvReport = new NbvReport();
        partialUpdatedNbvReport.setId(nbvReport.getId());

        partialUpdatedNbvReport
            .reportName(UPDATED_REPORT_NAME)
            .timeOfReportRequest(UPDATED_TIME_OF_REPORT_REQUEST)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restNbvReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNbvReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNbvReport))
            )
            .andExpect(status().isOk());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);
        NbvReport testNbvReport = nbvReportList.get(nbvReportList.size() - 1);
        assertThat(testNbvReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testNbvReport.getTimeOfReportRequest()).isEqualTo(UPDATED_TIME_OF_REPORT_REQUEST);
        assertThat(testNbvReport.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testNbvReport.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testNbvReport.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testNbvReport.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testNbvReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testNbvReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nbvReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNbvReport() throws Exception {
        int databaseSizeBeforeUpdate = nbvReportRepository.findAll().size();
        nbvReport.setId(count.incrementAndGet());

        // Create the NbvReport
        NbvReportDTO nbvReportDTO = nbvReportMapper.toDto(nbvReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNbvReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nbvReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NbvReport in the database
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(0)).save(nbvReport);
    }

    @Test
    @Transactional
    void deleteNbvReport() throws Exception {
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);

        int databaseSizeBeforeDelete = nbvReportRepository.findAll().size();

        // Delete the nbvReport
        restNbvReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, nbvReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NbvReport> nbvReportList = nbvReportRepository.findAll();
        assertThat(nbvReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NbvReport in Elasticsearch
        verify(mockNbvReportSearchRepository, times(1)).deleteById(nbvReport.getId());
    }

    @Test
    @Transactional
    void searchNbvReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nbvReportRepository.saveAndFlush(nbvReport);
        when(mockNbvReportSearchRepository.search("id:" + nbvReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nbvReport), PageRequest.of(0, 1), 1));

        // Search the nbvReport
        restNbvReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nbvReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvReport.getId().intValue())))
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
