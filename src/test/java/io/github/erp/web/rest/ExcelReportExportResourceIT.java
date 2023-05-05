package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Algorithm;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.ExcelReportExport;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportDesign;
import io.github.erp.domain.ReportStatus;
import io.github.erp.domain.SecurityClearance;
import io.github.erp.domain.SystemModule;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.ExcelReportExportRepository;
import io.github.erp.repository.search.ExcelReportExportSearchRepository;
import io.github.erp.service.ExcelReportExportService;
import io.github.erp.service.criteria.ExcelReportExportCriteria;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.mapper.ExcelReportExportMapper;
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
 * Integration tests for the {@link ExcelReportExportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExcelReportExportResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PASSWORD = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_NOTES_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_CHECK_SUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECK_SUM = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_REPORT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REPORT_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REPORT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/excel-report-exports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/excel-report-exports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExcelReportExportRepository excelReportExportRepository;

    @Mock
    private ExcelReportExportRepository excelReportExportRepositoryMock;

    @Autowired
    private ExcelReportExportMapper excelReportExportMapper;

    @Mock
    private ExcelReportExportService excelReportExportServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ExcelReportExportSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExcelReportExportSearchRepository mockExcelReportExportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExcelReportExportMockMvc;

    private ExcelReportExport excelReportExport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcelReportExport createEntity(EntityManager em) {
        ExcelReportExport excelReportExport = new ExcelReportExport()
            .reportName(DEFAULT_REPORT_NAME)
            .reportPassword(DEFAULT_REPORT_PASSWORD)
            .reportNotes(DEFAULT_REPORT_NOTES)
            .reportNotesContentType(DEFAULT_REPORT_NOTES_CONTENT_TYPE)
            .fileCheckSum(DEFAULT_FILE_CHECK_SUM)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .reportTimeStamp(DEFAULT_REPORT_TIME_STAMP)
            .reportId(DEFAULT_REPORT_ID);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        excelReportExport.setSecurityClearance(securityClearance);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        excelReportExport.setReportCreator(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        excelReportExport.setOrganization(dealer);
        // Add required entity
        excelReportExport.setDepartment(dealer);
        // Add required entity
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        excelReportExport.setSystemModule(systemModule);
        // Add required entity
        ReportDesign reportDesign;
        if (TestUtil.findAll(em, ReportDesign.class).isEmpty()) {
            reportDesign = ReportDesignResourceIT.createEntity(em);
            em.persist(reportDesign);
            em.flush();
        } else {
            reportDesign = TestUtil.findAll(em, ReportDesign.class).get(0);
        }
        excelReportExport.setReportDesign(reportDesign);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        excelReportExport.setFileCheckSumAlgorithm(algorithm);
        return excelReportExport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcelReportExport createUpdatedEntity(EntityManager em) {
        ExcelReportExport excelReportExport = new ExcelReportExport()
            .reportName(UPDATED_REPORT_NAME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE)
            .fileCheckSum(UPDATED_FILE_CHECK_SUM)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportTimeStamp(UPDATED_REPORT_TIME_STAMP)
            .reportId(UPDATED_REPORT_ID);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createUpdatedEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        excelReportExport.setSecurityClearance(securityClearance);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        excelReportExport.setReportCreator(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        excelReportExport.setOrganization(dealer);
        // Add required entity
        excelReportExport.setDepartment(dealer);
        // Add required entity
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createUpdatedEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        excelReportExport.setSystemModule(systemModule);
        // Add required entity
        ReportDesign reportDesign;
        if (TestUtil.findAll(em, ReportDesign.class).isEmpty()) {
            reportDesign = ReportDesignResourceIT.createUpdatedEntity(em);
            em.persist(reportDesign);
            em.flush();
        } else {
            reportDesign = TestUtil.findAll(em, ReportDesign.class).get(0);
        }
        excelReportExport.setReportDesign(reportDesign);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createUpdatedEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        excelReportExport.setFileCheckSumAlgorithm(algorithm);
        return excelReportExport;
    }

    @BeforeEach
    public void initTest() {
        excelReportExport = createEntity(em);
    }

    @Test
    @Transactional
    void createExcelReportExport() throws Exception {
        int databaseSizeBeforeCreate = excelReportExportRepository.findAll().size();
        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);
        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeCreate + 1);
        ExcelReportExport testExcelReportExport = excelReportExportList.get(excelReportExportList.size() - 1);
        assertThat(testExcelReportExport.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testExcelReportExport.getReportPassword()).isEqualTo(DEFAULT_REPORT_PASSWORD);
        assertThat(testExcelReportExport.getReportNotes()).isEqualTo(DEFAULT_REPORT_NOTES);
        assertThat(testExcelReportExport.getReportNotesContentType()).isEqualTo(DEFAULT_REPORT_NOTES_CONTENT_TYPE);
        assertThat(testExcelReportExport.getFileCheckSum()).isEqualTo(DEFAULT_FILE_CHECK_SUM);
        assertThat(testExcelReportExport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testExcelReportExport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testExcelReportExport.getReportTimeStamp()).isEqualTo(DEFAULT_REPORT_TIME_STAMP);
        assertThat(testExcelReportExport.getReportId()).isEqualTo(DEFAULT_REPORT_ID);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(1)).save(testExcelReportExport);
    }

    @Test
    @Transactional
    void createExcelReportExportWithExistingId() throws Exception {
        // Create the ExcelReportExport with an existing ID
        excelReportExport.setId(1L);
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        int databaseSizeBeforeCreate = excelReportExportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = excelReportExportRepository.findAll().size();
        // set the field null
        excelReportExport.setReportName(null);

        // Create the ExcelReportExport, which fails.
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = excelReportExportRepository.findAll().size();
        // set the field null
        excelReportExport.setReportPassword(null);

        // Create the ExcelReportExport, which fails.
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = excelReportExportRepository.findAll().size();
        // set the field null
        excelReportExport.setReportTimeStamp(null);

        // Create the ExcelReportExport, which fails.
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = excelReportExportRepository.findAll().size();
        // set the field null
        excelReportExport.setReportId(null);

        // Create the ExcelReportExport, which fails.
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        restExcelReportExportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExcelReportExports() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excelReportExport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))))
            .andExpect(jsonPath("$.[*].fileCheckSum").value(hasItem(DEFAULT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportTimeStamp").value(hasItem(sameInstant(DEFAULT_REPORT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcelReportExportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(excelReportExportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcelReportExportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(excelReportExportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcelReportExportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(excelReportExportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcelReportExportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(excelReportExportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getExcelReportExport() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get the excelReportExport
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL_ID, excelReportExport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excelReportExport.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportPassword").value(DEFAULT_REPORT_PASSWORD))
            .andExpect(jsonPath("$.reportNotesContentType").value(DEFAULT_REPORT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportNotes").value(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES)))
            .andExpect(jsonPath("$.fileCheckSum").value(DEFAULT_FILE_CHECK_SUM.toString()))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.reportTimeStamp").value(sameInstant(DEFAULT_REPORT_TIME_STAMP)))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()));
    }

    @Test
    @Transactional
    void getExcelReportExportsByIdFiltering() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        Long id = excelReportExport.getId();

        defaultExcelReportExportShouldBeFound("id.equals=" + id);
        defaultExcelReportExportShouldNotBeFound("id.notEquals=" + id);

        defaultExcelReportExportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExcelReportExportShouldNotBeFound("id.greaterThan=" + id);

        defaultExcelReportExportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExcelReportExportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName equals to DEFAULT_REPORT_NAME
        defaultExcelReportExportShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the excelReportExportList where reportName equals to UPDATED_REPORT_NAME
        defaultExcelReportExportShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName not equals to DEFAULT_REPORT_NAME
        defaultExcelReportExportShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the excelReportExportList where reportName not equals to UPDATED_REPORT_NAME
        defaultExcelReportExportShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultExcelReportExportShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the excelReportExportList where reportName equals to UPDATED_REPORT_NAME
        defaultExcelReportExportShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName is not null
        defaultExcelReportExportShouldBeFound("reportName.specified=true");

        // Get all the excelReportExportList where reportName is null
        defaultExcelReportExportShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName contains DEFAULT_REPORT_NAME
        defaultExcelReportExportShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the excelReportExportList where reportName contains UPDATED_REPORT_NAME
        defaultExcelReportExportShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportName does not contain DEFAULT_REPORT_NAME
        defaultExcelReportExportShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the excelReportExportList where reportName does not contain UPDATED_REPORT_NAME
        defaultExcelReportExportShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword equals to DEFAULT_REPORT_PASSWORD
        defaultExcelReportExportShouldBeFound("reportPassword.equals=" + DEFAULT_REPORT_PASSWORD);

        // Get all the excelReportExportList where reportPassword equals to UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldNotBeFound("reportPassword.equals=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword not equals to DEFAULT_REPORT_PASSWORD
        defaultExcelReportExportShouldNotBeFound("reportPassword.notEquals=" + DEFAULT_REPORT_PASSWORD);

        // Get all the excelReportExportList where reportPassword not equals to UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldBeFound("reportPassword.notEquals=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword in DEFAULT_REPORT_PASSWORD or UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldBeFound("reportPassword.in=" + DEFAULT_REPORT_PASSWORD + "," + UPDATED_REPORT_PASSWORD);

        // Get all the excelReportExportList where reportPassword equals to UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldNotBeFound("reportPassword.in=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword is not null
        defaultExcelReportExportShouldBeFound("reportPassword.specified=true");

        // Get all the excelReportExportList where reportPassword is null
        defaultExcelReportExportShouldNotBeFound("reportPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordContainsSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword contains DEFAULT_REPORT_PASSWORD
        defaultExcelReportExportShouldBeFound("reportPassword.contains=" + DEFAULT_REPORT_PASSWORD);

        // Get all the excelReportExportList where reportPassword contains UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldNotBeFound("reportPassword.contains=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportPassword does not contain DEFAULT_REPORT_PASSWORD
        defaultExcelReportExportShouldNotBeFound("reportPassword.doesNotContain=" + DEFAULT_REPORT_PASSWORD);

        // Get all the excelReportExportList where reportPassword does not contain UPDATED_REPORT_PASSWORD
        defaultExcelReportExportShouldBeFound("reportPassword.doesNotContain=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp equals to DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.equals=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp equals to UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.equals=" + UPDATED_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp not equals to DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.notEquals=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp not equals to UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.notEquals=" + UPDATED_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp in DEFAULT_REPORT_TIME_STAMP or UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.in=" + DEFAULT_REPORT_TIME_STAMP + "," + UPDATED_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp equals to UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.in=" + UPDATED_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp is not null
        defaultExcelReportExportShouldBeFound("reportTimeStamp.specified=true");

        // Get all the excelReportExportList where reportTimeStamp is null
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.specified=false");
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp is greater than or equal to DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.greaterThanOrEqual=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp is greater than or equal to UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.greaterThanOrEqual=" + UPDATED_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp is less than or equal to DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.lessThanOrEqual=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp is less than or equal to SMALLER_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.lessThanOrEqual=" + SMALLER_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp is less than DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.lessThan=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp is less than UPDATED_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.lessThan=" + UPDATED_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportTimeStamp is greater than DEFAULT_REPORT_TIME_STAMP
        defaultExcelReportExportShouldNotBeFound("reportTimeStamp.greaterThan=" + DEFAULT_REPORT_TIME_STAMP);

        // Get all the excelReportExportList where reportTimeStamp is greater than SMALLER_REPORT_TIME_STAMP
        defaultExcelReportExportShouldBeFound("reportTimeStamp.greaterThan=" + SMALLER_REPORT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportId equals to DEFAULT_REPORT_ID
        defaultExcelReportExportShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the excelReportExportList where reportId equals to UPDATED_REPORT_ID
        defaultExcelReportExportShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportId not equals to DEFAULT_REPORT_ID
        defaultExcelReportExportShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the excelReportExportList where reportId not equals to UPDATED_REPORT_ID
        defaultExcelReportExportShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultExcelReportExportShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the excelReportExportList where reportId equals to UPDATED_REPORT_ID
        defaultExcelReportExportShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        // Get all the excelReportExportList where reportId is not null
        defaultExcelReportExportShouldBeFound("reportId.specified=true");

        // Get all the excelReportExportList where reportId is null
        defaultExcelReportExportShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
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
        excelReportExport.addPlaceholder(placeholder);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long placeholderId = placeholder.getId();

        // Get all the excelReportExportList where placeholder equals to placeholderId
        defaultExcelReportExportShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the excelReportExportList where placeholder equals to (placeholderId + 1)
        defaultExcelReportExportShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        UniversallyUniqueMapping parameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        excelReportExport.addParameters(parameters);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long parametersId = parameters.getId();

        // Get all the excelReportExportList where parameters equals to parametersId
        defaultExcelReportExportShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the excelReportExportList where parameters equals to (parametersId + 1)
        defaultExcelReportExportShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        ReportStatus reportStatus;
        if (TestUtil.findAll(em, ReportStatus.class).isEmpty()) {
            reportStatus = ReportStatusResourceIT.createEntity(em);
            em.persist(reportStatus);
            em.flush();
        } else {
            reportStatus = TestUtil.findAll(em, ReportStatus.class).get(0);
        }
        em.persist(reportStatus);
        em.flush();
        excelReportExport.setReportStatus(reportStatus);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long reportStatusId = reportStatus.getId();

        // Get all the excelReportExportList where reportStatus equals to reportStatusId
        defaultExcelReportExportShouldBeFound("reportStatusId.equals=" + reportStatusId);

        // Get all the excelReportExportList where reportStatus equals to (reportStatusId + 1)
        defaultExcelReportExportShouldNotBeFound("reportStatusId.equals=" + (reportStatusId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        excelReportExport.setSecurityClearance(securityClearance);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long securityClearanceId = securityClearance.getId();

        // Get all the excelReportExportList where securityClearance equals to securityClearanceId
        defaultExcelReportExportShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the excelReportExportList where securityClearance equals to (securityClearanceId + 1)
        defaultExcelReportExportShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        ApplicationUser reportCreator;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            reportCreator = ApplicationUserResourceIT.createEntity(em);
            em.persist(reportCreator);
            em.flush();
        } else {
            reportCreator = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(reportCreator);
        em.flush();
        excelReportExport.setReportCreator(reportCreator);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long reportCreatorId = reportCreator.getId();

        // Get all the excelReportExportList where reportCreator equals to reportCreatorId
        defaultExcelReportExportShouldBeFound("reportCreatorId.equals=" + reportCreatorId);

        // Get all the excelReportExportList where reportCreator equals to (reportCreatorId + 1)
        defaultExcelReportExportShouldNotBeFound("reportCreatorId.equals=" + (reportCreatorId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Dealer organization;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            organization = DealerResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(organization);
        em.flush();
        excelReportExport.setOrganization(organization);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long organizationId = organization.getId();

        // Get all the excelReportExportList where organization equals to organizationId
        defaultExcelReportExportShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the excelReportExportList where organization equals to (organizationId + 1)
        defaultExcelReportExportShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Dealer department;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            department = DealerResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(department);
        em.flush();
        excelReportExport.setDepartment(department);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long departmentId = department.getId();

        // Get all the excelReportExportList where department equals to departmentId
        defaultExcelReportExportShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the excelReportExportList where department equals to (departmentId + 1)
        defaultExcelReportExportShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsBySystemModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        SystemModule systemModule;
        if (TestUtil.findAll(em, SystemModule.class).isEmpty()) {
            systemModule = SystemModuleResourceIT.createEntity(em);
            em.persist(systemModule);
            em.flush();
        } else {
            systemModule = TestUtil.findAll(em, SystemModule.class).get(0);
        }
        em.persist(systemModule);
        em.flush();
        excelReportExport.setSystemModule(systemModule);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long systemModuleId = systemModule.getId();

        // Get all the excelReportExportList where systemModule equals to systemModuleId
        defaultExcelReportExportShouldBeFound("systemModuleId.equals=" + systemModuleId);

        // Get all the excelReportExportList where systemModule equals to (systemModuleId + 1)
        defaultExcelReportExportShouldNotBeFound("systemModuleId.equals=" + (systemModuleId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByReportDesignIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        ReportDesign reportDesign;
        if (TestUtil.findAll(em, ReportDesign.class).isEmpty()) {
            reportDesign = ReportDesignResourceIT.createEntity(em);
            em.persist(reportDesign);
            em.flush();
        } else {
            reportDesign = TestUtil.findAll(em, ReportDesign.class).get(0);
        }
        em.persist(reportDesign);
        em.flush();
        excelReportExport.setReportDesign(reportDesign);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long reportDesignId = reportDesign.getId();

        // Get all the excelReportExportList where reportDesign equals to reportDesignId
        defaultExcelReportExportShouldBeFound("reportDesignId.equals=" + reportDesignId);

        // Get all the excelReportExportList where reportDesign equals to (reportDesignId + 1)
        defaultExcelReportExportShouldNotBeFound("reportDesignId.equals=" + (reportDesignId + 1));
    }

    @Test
    @Transactional
    void getAllExcelReportExportsByFileCheckSumAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Algorithm fileCheckSumAlgorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            fileCheckSumAlgorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(fileCheckSumAlgorithm);
            em.flush();
        } else {
            fileCheckSumAlgorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        em.persist(fileCheckSumAlgorithm);
        em.flush();
        excelReportExport.setFileCheckSumAlgorithm(fileCheckSumAlgorithm);
        excelReportExportRepository.saveAndFlush(excelReportExport);
        Long fileCheckSumAlgorithmId = fileCheckSumAlgorithm.getId();

        // Get all the excelReportExportList where fileCheckSumAlgorithm equals to fileCheckSumAlgorithmId
        defaultExcelReportExportShouldBeFound("fileCheckSumAlgorithmId.equals=" + fileCheckSumAlgorithmId);

        // Get all the excelReportExportList where fileCheckSumAlgorithm equals to (fileCheckSumAlgorithmId + 1)
        defaultExcelReportExportShouldNotBeFound("fileCheckSumAlgorithmId.equals=" + (fileCheckSumAlgorithmId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExcelReportExportShouldBeFound(String filter) throws Exception {
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excelReportExport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))))
            .andExpect(jsonPath("$.[*].fileCheckSum").value(hasItem(DEFAULT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportTimeStamp").value(hasItem(sameInstant(DEFAULT_REPORT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));

        // Check, that the count call also returns 1
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExcelReportExportShouldNotBeFound(String filter) throws Exception {
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExcelReportExportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExcelReportExport() throws Exception {
        // Get the excelReportExport
        restExcelReportExportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExcelReportExport() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();

        // Update the excelReportExport
        ExcelReportExport updatedExcelReportExport = excelReportExportRepository.findById(excelReportExport.getId()).get();
        // Disconnect from session so that the updates on updatedExcelReportExport are not directly saved in db
        em.detach(updatedExcelReportExport);
        updatedExcelReportExport
            .reportName(UPDATED_REPORT_NAME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE)
            .fileCheckSum(UPDATED_FILE_CHECK_SUM)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportTimeStamp(UPDATED_REPORT_TIME_STAMP)
            .reportId(UPDATED_REPORT_ID);
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(updatedExcelReportExport);

        restExcelReportExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excelReportExportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);
        ExcelReportExport testExcelReportExport = excelReportExportList.get(excelReportExportList.size() - 1);
        assertThat(testExcelReportExport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testExcelReportExport.getReportPassword()).isEqualTo(UPDATED_REPORT_PASSWORD);
        assertThat(testExcelReportExport.getReportNotes()).isEqualTo(UPDATED_REPORT_NOTES);
        assertThat(testExcelReportExport.getReportNotesContentType()).isEqualTo(UPDATED_REPORT_NOTES_CONTENT_TYPE);
        assertThat(testExcelReportExport.getFileCheckSum()).isEqualTo(UPDATED_FILE_CHECK_SUM);
        assertThat(testExcelReportExport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testExcelReportExport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testExcelReportExport.getReportTimeStamp()).isEqualTo(UPDATED_REPORT_TIME_STAMP);
        assertThat(testExcelReportExport.getReportId()).isEqualTo(UPDATED_REPORT_ID);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository).save(testExcelReportExport);
    }

    @Test
    @Transactional
    void putNonExistingExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excelReportExportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void putWithIdMismatchExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void partialUpdateExcelReportExportWithPatch() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();

        // Update the excelReportExport using partial update
        ExcelReportExport partialUpdatedExcelReportExport = new ExcelReportExport();
        partialUpdatedExcelReportExport.setId(excelReportExport.getId());

        partialUpdatedExcelReportExport.reportName(UPDATED_REPORT_NAME);

        restExcelReportExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcelReportExport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcelReportExport))
            )
            .andExpect(status().isOk());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);
        ExcelReportExport testExcelReportExport = excelReportExportList.get(excelReportExportList.size() - 1);
        assertThat(testExcelReportExport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testExcelReportExport.getReportPassword()).isEqualTo(DEFAULT_REPORT_PASSWORD);
        assertThat(testExcelReportExport.getReportNotes()).isEqualTo(DEFAULT_REPORT_NOTES);
        assertThat(testExcelReportExport.getReportNotesContentType()).isEqualTo(DEFAULT_REPORT_NOTES_CONTENT_TYPE);
        assertThat(testExcelReportExport.getFileCheckSum()).isEqualTo(DEFAULT_FILE_CHECK_SUM);
        assertThat(testExcelReportExport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testExcelReportExport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testExcelReportExport.getReportTimeStamp()).isEqualTo(DEFAULT_REPORT_TIME_STAMP);
        assertThat(testExcelReportExport.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
    }

    @Test
    @Transactional
    void fullUpdateExcelReportExportWithPatch() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();

        // Update the excelReportExport using partial update
        ExcelReportExport partialUpdatedExcelReportExport = new ExcelReportExport();
        partialUpdatedExcelReportExport.setId(excelReportExport.getId());

        partialUpdatedExcelReportExport
            .reportName(UPDATED_REPORT_NAME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE)
            .fileCheckSum(UPDATED_FILE_CHECK_SUM)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportTimeStamp(UPDATED_REPORT_TIME_STAMP)
            .reportId(UPDATED_REPORT_ID);

        restExcelReportExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcelReportExport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcelReportExport))
            )
            .andExpect(status().isOk());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);
        ExcelReportExport testExcelReportExport = excelReportExportList.get(excelReportExportList.size() - 1);
        assertThat(testExcelReportExport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testExcelReportExport.getReportPassword()).isEqualTo(UPDATED_REPORT_PASSWORD);
        assertThat(testExcelReportExport.getReportNotes()).isEqualTo(UPDATED_REPORT_NOTES);
        assertThat(testExcelReportExport.getReportNotesContentType()).isEqualTo(UPDATED_REPORT_NOTES_CONTENT_TYPE);
        assertThat(testExcelReportExport.getFileCheckSum()).isEqualTo(UPDATED_FILE_CHECK_SUM);
        assertThat(testExcelReportExport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testExcelReportExport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testExcelReportExport.getReportTimeStamp()).isEqualTo(UPDATED_REPORT_TIME_STAMP);
        assertThat(testExcelReportExport.getReportId()).isEqualTo(UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excelReportExportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExcelReportExport() throws Exception {
        int databaseSizeBeforeUpdate = excelReportExportRepository.findAll().size();
        excelReportExport.setId(count.incrementAndGet());

        // Create the ExcelReportExport
        ExcelReportExportDTO excelReportExportDTO = excelReportExportMapper.toDto(excelReportExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcelReportExportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excelReportExportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcelReportExport in the database
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(0)).save(excelReportExport);
    }

    @Test
    @Transactional
    void deleteExcelReportExport() throws Exception {
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);

        int databaseSizeBeforeDelete = excelReportExportRepository.findAll().size();

        // Delete the excelReportExport
        restExcelReportExportMockMvc
            .perform(delete(ENTITY_API_URL_ID, excelReportExport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExcelReportExport> excelReportExportList = excelReportExportRepository.findAll();
        assertThat(excelReportExportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExcelReportExport in Elasticsearch
        verify(mockExcelReportExportSearchRepository, times(1)).deleteById(excelReportExport.getId());
    }

    @Test
    @Transactional
    void searchExcelReportExport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        excelReportExportRepository.saveAndFlush(excelReportExport);
        when(mockExcelReportExportSearchRepository.search("id:" + excelReportExport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(excelReportExport), PageRequest.of(0, 1), 1));

        // Search the excelReportExport
        restExcelReportExportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + excelReportExport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excelReportExport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))))
            .andExpect(jsonPath("$.[*].fileCheckSum").value(hasItem(DEFAULT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportTimeStamp").value(hasItem(sameInstant(DEFAULT_REPORT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }
}
