package io.github.erp.web.rest;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ExcelReportExport;
import io.github.erp.domain.Placeholder;
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
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD.toString())))
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
            .andExpect(jsonPath("$.reportPassword").value(DEFAULT_REPORT_PASSWORD.toString()))
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
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD.toString())))
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
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))))
            .andExpect(jsonPath("$.[*].fileCheckSum").value(hasItem(DEFAULT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportTimeStamp").value(hasItem(sameInstant(DEFAULT_REPORT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }
}
