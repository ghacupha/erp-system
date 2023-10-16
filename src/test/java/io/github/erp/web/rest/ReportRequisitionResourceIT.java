package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportContentType;
import io.github.erp.domain.ReportRequisition;
import io.github.erp.domain.ReportTemplate;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.repository.ReportRequisitionRepository;
import io.github.erp.repository.search.ReportRequisitionSearchRepository;
import io.github.erp.service.ReportRequisitionService;
import io.github.erp.service.criteria.ReportRequisitionCriteria;
import io.github.erp.service.dto.ReportRequisitionDTO;
import io.github.erp.service.mapper.ReportRequisitionMapper;
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
 * Integration tests for the {@link ReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportRequisitionResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REPORT_REQUEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REPORT_REQUEST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REPORT_REQUEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_REPORT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PASSWORD = "BBBBBBBBBB";

    private static final ReportStatusTypes DEFAULT_REPORT_STATUS = ReportStatusTypes.GENERATING;
    private static final ReportStatusTypes UPDATED_REPORT_STATUS = ReportStatusTypes.SUCCESSFUL;

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final byte[] DEFAULT_REPORT_FILE_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REPORT_FILE_CHECK_SUM = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_FILE_CHECK_SUM = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_NOTES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRequisitionRepository reportRequisitionRepository;

    @Mock
    private ReportRequisitionRepository reportRequisitionRepositoryMock;

    @Autowired
    private ReportRequisitionMapper reportRequisitionMapper;

    @Mock
    private ReportRequisitionService reportRequisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportRequisitionSearchRepository mockReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportRequisitionMockMvc;

    private ReportRequisition reportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportRequisition createEntity(EntityManager em) {
        ReportRequisition reportRequisition = new ReportRequisition()
            .reportName(DEFAULT_REPORT_NAME)
            .reportRequestTime(DEFAULT_REPORT_REQUEST_TIME)
            .reportPassword(DEFAULT_REPORT_PASSWORD)
            .reportStatus(DEFAULT_REPORT_STATUS)
            .reportId(DEFAULT_REPORT_ID)
            .reportFileAttachment(DEFAULT_REPORT_FILE_ATTACHMENT)
            .reportFileAttachmentContentType(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)
            .reportFileCheckSum(DEFAULT_REPORT_FILE_CHECK_SUM)
            .reportNotes(DEFAULT_REPORT_NOTES)
            .reportNotesContentType(DEFAULT_REPORT_NOTES_CONTENT_TYPE);
        // Add required entity
        ReportTemplate reportTemplate;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportTemplate = ReportTemplateResourceIT.createEntity(em);
            em.persist(reportTemplate);
            em.flush();
        } else {
            reportTemplate = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        reportRequisition.setReportTemplate(reportTemplate);
        // Add required entity
        ReportContentType reportContentType;
        if (TestUtil.findAll(em, ReportContentType.class).isEmpty()) {
            reportContentType = ReportContentTypeResourceIT.createEntity(em);
            em.persist(reportContentType);
            em.flush();
        } else {
            reportContentType = TestUtil.findAll(em, ReportContentType.class).get(0);
        }
        reportRequisition.setReportContentType(reportContentType);
        return reportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportRequisition createUpdatedEntity(EntityManager em) {
        ReportRequisition reportRequisition = new ReportRequisition()
            .reportName(UPDATED_REPORT_NAME)
            .reportRequestTime(UPDATED_REPORT_REQUEST_TIME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID)
            .reportFileAttachment(UPDATED_REPORT_FILE_ATTACHMENT)
            .reportFileAttachmentContentType(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)
            .reportFileCheckSum(UPDATED_REPORT_FILE_CHECK_SUM)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE);
        // Add required entity
        ReportTemplate reportTemplate;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportTemplate = ReportTemplateResourceIT.createUpdatedEntity(em);
            em.persist(reportTemplate);
            em.flush();
        } else {
            reportTemplate = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        reportRequisition.setReportTemplate(reportTemplate);
        // Add required entity
        ReportContentType reportContentType;
        if (TestUtil.findAll(em, ReportContentType.class).isEmpty()) {
            reportContentType = ReportContentTypeResourceIT.createUpdatedEntity(em);
            em.persist(reportContentType);
            em.flush();
        } else {
            reportContentType = TestUtil.findAll(em, ReportContentType.class).get(0);
        }
        reportRequisition.setReportContentType(reportContentType);
        return reportRequisition;
    }

    @BeforeEach
    public void initTest() {
        reportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = reportRequisitionRepository.findAll().size();
        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);
        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        ReportRequisition testReportRequisition = reportRequisitionList.get(reportRequisitionList.size() - 1);
        assertThat(testReportRequisition.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testReportRequisition.getReportRequestTime()).isEqualTo(DEFAULT_REPORT_REQUEST_TIME);
        assertThat(testReportRequisition.getReportPassword()).isEqualTo(DEFAULT_REPORT_PASSWORD);
        assertThat(testReportRequisition.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);
        assertThat(testReportRequisition.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testReportRequisition.getReportFileAttachment()).isEqualTo(DEFAULT_REPORT_FILE_ATTACHMENT);
        assertThat(testReportRequisition.getReportFileAttachmentContentType()).isEqualTo(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE);
        assertThat(testReportRequisition.getReportFileCheckSum()).isEqualTo(DEFAULT_REPORT_FILE_CHECK_SUM);
        assertThat(testReportRequisition.getReportNotes()).isEqualTo(DEFAULT_REPORT_NOTES);
        assertThat(testReportRequisition.getReportNotesContentType()).isEqualTo(DEFAULT_REPORT_NOTES_CONTENT_TYPE);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(1)).save(testReportRequisition);
    }

    @Test
    @Transactional
    void createReportRequisitionWithExistingId() throws Exception {
        // Create the ReportRequisition with an existing ID
        reportRequisition.setId(1L);
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        int databaseSizeBeforeCreate = reportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRequisitionRepository.findAll().size();
        // set the field null
        reportRequisition.setReportName(null);

        // Create the ReportRequisition, which fails.
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportRequestTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRequisitionRepository.findAll().size();
        // set the field null
        reportRequisition.setReportRequestTime(null);

        // Create the ReportRequisition, which fails.
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRequisitionRepository.findAll().size();
        // set the field null
        reportRequisition.setReportPassword(null);

        // Create the ReportRequisition, which fails.
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRequisitionRepository.findAll().size();
        // set the field null
        reportRequisition.setReportId(null);

        // Create the ReportRequisition, which fails.
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        restReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportRequisitions() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportRequestTime").value(hasItem(sameInstant(DEFAULT_REPORT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].reportFileAttachmentContentType").value(hasItem(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFileAttachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].reportFileCheckSum").value(hasItem(DEFAULT_REPORT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReportRequisition() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get the reportRequisition
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, reportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportRequestTime").value(sameInstant(DEFAULT_REPORT_REQUEST_TIME)))
            .andExpect(jsonPath("$.reportPassword").value(DEFAULT_REPORT_PASSWORD))
            .andExpect(jsonPath("$.reportStatus").value(DEFAULT_REPORT_STATUS.toString()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()))
            .andExpect(jsonPath("$.reportFileAttachmentContentType").value(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFileAttachment").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE_ATTACHMENT)))
            .andExpect(jsonPath("$.reportFileCheckSum").value(DEFAULT_REPORT_FILE_CHECK_SUM.toString()))
            .andExpect(jsonPath("$.reportNotesContentType").value(DEFAULT_REPORT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportNotes").value(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES)));
    }

    @Test
    @Transactional
    void getReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        Long id = reportRequisition.getId();

        defaultReportRequisitionShouldBeFound("id.equals=" + id);
        defaultReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName equals to DEFAULT_REPORT_NAME
        defaultReportRequisitionShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the reportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultReportRequisitionShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName not equals to DEFAULT_REPORT_NAME
        defaultReportRequisitionShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the reportRequisitionList where reportName not equals to UPDATED_REPORT_NAME
        defaultReportRequisitionShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultReportRequisitionShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the reportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultReportRequisitionShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName is not null
        defaultReportRequisitionShouldBeFound("reportName.specified=true");

        // Get all the reportRequisitionList where reportName is null
        defaultReportRequisitionShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName contains DEFAULT_REPORT_NAME
        defaultReportRequisitionShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the reportRequisitionList where reportName contains UPDATED_REPORT_NAME
        defaultReportRequisitionShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportName does not contain DEFAULT_REPORT_NAME
        defaultReportRequisitionShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the reportRequisitionList where reportName does not contain UPDATED_REPORT_NAME
        defaultReportRequisitionShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime equals to DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.equals=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime equals to UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.equals=" + UPDATED_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime not equals to DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.notEquals=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime not equals to UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.notEquals=" + UPDATED_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsInShouldWork() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime in DEFAULT_REPORT_REQUEST_TIME or UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.in=" + DEFAULT_REPORT_REQUEST_TIME + "," + UPDATED_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime equals to UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.in=" + UPDATED_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime is not null
        defaultReportRequisitionShouldBeFound("reportRequestTime.specified=true");

        // Get all the reportRequisitionList where reportRequestTime is null
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime is greater than or equal to DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.greaterThanOrEqual=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime is greater than or equal to UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.greaterThanOrEqual=" + UPDATED_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime is less than or equal to DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.lessThanOrEqual=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime is less than or equal to SMALLER_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.lessThanOrEqual=" + SMALLER_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime is less than DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.lessThan=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime is less than UPDATED_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.lessThan=" + UPDATED_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportRequestTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportRequestTime is greater than DEFAULT_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldNotBeFound("reportRequestTime.greaterThan=" + DEFAULT_REPORT_REQUEST_TIME);

        // Get all the reportRequisitionList where reportRequestTime is greater than SMALLER_REPORT_REQUEST_TIME
        defaultReportRequisitionShouldBeFound("reportRequestTime.greaterThan=" + SMALLER_REPORT_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword equals to DEFAULT_REPORT_PASSWORD
        defaultReportRequisitionShouldBeFound("reportPassword.equals=" + DEFAULT_REPORT_PASSWORD);

        // Get all the reportRequisitionList where reportPassword equals to UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldNotBeFound("reportPassword.equals=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword not equals to DEFAULT_REPORT_PASSWORD
        defaultReportRequisitionShouldNotBeFound("reportPassword.notEquals=" + DEFAULT_REPORT_PASSWORD);

        // Get all the reportRequisitionList where reportPassword not equals to UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldBeFound("reportPassword.notEquals=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword in DEFAULT_REPORT_PASSWORD or UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldBeFound("reportPassword.in=" + DEFAULT_REPORT_PASSWORD + "," + UPDATED_REPORT_PASSWORD);

        // Get all the reportRequisitionList where reportPassword equals to UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldNotBeFound("reportPassword.in=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword is not null
        defaultReportRequisitionShouldBeFound("reportPassword.specified=true");

        // Get all the reportRequisitionList where reportPassword is null
        defaultReportRequisitionShouldNotBeFound("reportPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordContainsSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword contains DEFAULT_REPORT_PASSWORD
        defaultReportRequisitionShouldBeFound("reportPassword.contains=" + DEFAULT_REPORT_PASSWORD);

        // Get all the reportRequisitionList where reportPassword contains UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldNotBeFound("reportPassword.contains=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportPassword does not contain DEFAULT_REPORT_PASSWORD
        defaultReportRequisitionShouldNotBeFound("reportPassword.doesNotContain=" + DEFAULT_REPORT_PASSWORD);

        // Get all the reportRequisitionList where reportPassword does not contain UPDATED_REPORT_PASSWORD
        defaultReportRequisitionShouldBeFound("reportPassword.doesNotContain=" + UPDATED_REPORT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportStatus equals to DEFAULT_REPORT_STATUS
        defaultReportRequisitionShouldBeFound("reportStatus.equals=" + DEFAULT_REPORT_STATUS);

        // Get all the reportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultReportRequisitionShouldNotBeFound("reportStatus.equals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportStatus not equals to DEFAULT_REPORT_STATUS
        defaultReportRequisitionShouldNotBeFound("reportStatus.notEquals=" + DEFAULT_REPORT_STATUS);

        // Get all the reportRequisitionList where reportStatus not equals to UPDATED_REPORT_STATUS
        defaultReportRequisitionShouldBeFound("reportStatus.notEquals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportStatusIsInShouldWork() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportStatus in DEFAULT_REPORT_STATUS or UPDATED_REPORT_STATUS
        defaultReportRequisitionShouldBeFound("reportStatus.in=" + DEFAULT_REPORT_STATUS + "," + UPDATED_REPORT_STATUS);

        // Get all the reportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultReportRequisitionShouldNotBeFound("reportStatus.in=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportStatus is not null
        defaultReportRequisitionShouldBeFound("reportStatus.specified=true");

        // Get all the reportRequisitionList where reportStatus is null
        defaultReportRequisitionShouldNotBeFound("reportStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportId equals to DEFAULT_REPORT_ID
        defaultReportRequisitionShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the reportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultReportRequisitionShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportId not equals to DEFAULT_REPORT_ID
        defaultReportRequisitionShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the reportRequisitionList where reportId not equals to UPDATED_REPORT_ID
        defaultReportRequisitionShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultReportRequisitionShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the reportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultReportRequisitionShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        // Get all the reportRequisitionList where reportId is not null
        defaultReportRequisitionShouldBeFound("reportId.specified=true");

        // Get all the reportRequisitionList where reportId is null
        defaultReportRequisitionShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByPlaceholdersIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        Placeholder placeholders;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholders = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholders);
            em.flush();
        } else {
            placeholders = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholders);
        em.flush();
        reportRequisition.addPlaceholders(placeholders);
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        Long placeholdersId = placeholders.getId();

        // Get all the reportRequisitionList where placeholders equals to placeholdersId
        defaultReportRequisitionShouldBeFound("placeholdersId.equals=" + placeholdersId);

        // Get all the reportRequisitionList where placeholders equals to (placeholdersId + 1)
        defaultReportRequisitionShouldNotBeFound("placeholdersId.equals=" + (placeholdersId + 1));
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);
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
        reportRequisition.addParameters(parameters);
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        Long parametersId = parameters.getId();

        // Get all the reportRequisitionList where parameters equals to parametersId
        defaultReportRequisitionShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the reportRequisitionList where parameters equals to (parametersId + 1)
        defaultReportRequisitionShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        ReportTemplate reportTemplate;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportTemplate = ReportTemplateResourceIT.createEntity(em);
            em.persist(reportTemplate);
            em.flush();
        } else {
            reportTemplate = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        em.persist(reportTemplate);
        em.flush();
        reportRequisition.setReportTemplate(reportTemplate);
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        Long reportTemplateId = reportTemplate.getId();

        // Get all the reportRequisitionList where reportTemplate equals to reportTemplateId
        defaultReportRequisitionShouldBeFound("reportTemplateId.equals=" + reportTemplateId);

        // Get all the reportRequisitionList where reportTemplate equals to (reportTemplateId + 1)
        defaultReportRequisitionShouldNotBeFound("reportTemplateId.equals=" + (reportTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllReportRequisitionsByReportContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        ReportContentType reportContentType;
        if (TestUtil.findAll(em, ReportContentType.class).isEmpty()) {
            reportContentType = ReportContentTypeResourceIT.createEntity(em);
            em.persist(reportContentType);
            em.flush();
        } else {
            reportContentType = TestUtil.findAll(em, ReportContentType.class).get(0);
        }
        em.persist(reportContentType);
        em.flush();
        reportRequisition.setReportContentType(reportContentType);
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        Long reportContentTypeId = reportContentType.getId();

        // Get all the reportRequisitionList where reportContentType equals to reportContentTypeId
        defaultReportRequisitionShouldBeFound("reportContentTypeId.equals=" + reportContentTypeId);

        // Get all the reportRequisitionList where reportContentType equals to (reportContentTypeId + 1)
        defaultReportRequisitionShouldNotBeFound("reportContentTypeId.equals=" + (reportContentTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportRequisitionShouldBeFound(String filter) throws Exception {
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportRequestTime").value(hasItem(sameInstant(DEFAULT_REPORT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].reportFileAttachmentContentType").value(hasItem(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFileAttachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].reportFileCheckSum").value(hasItem(DEFAULT_REPORT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))));

        // Check, that the count call also returns 1
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportRequisition() throws Exception {
        // Get the reportRequisition
        restReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportRequisition() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();

        // Update the reportRequisition
        ReportRequisition updatedReportRequisition = reportRequisitionRepository.findById(reportRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedReportRequisition are not directly saved in db
        em.detach(updatedReportRequisition);
        updatedReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportRequestTime(UPDATED_REPORT_REQUEST_TIME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID)
            .reportFileAttachment(UPDATED_REPORT_FILE_ATTACHMENT)
            .reportFileAttachmentContentType(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)
            .reportFileCheckSum(UPDATED_REPORT_FILE_CHECK_SUM)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE);
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(updatedReportRequisition);

        restReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        ReportRequisition testReportRequisition = reportRequisitionList.get(reportRequisitionList.size() - 1);
        assertThat(testReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReportRequisition.getReportRequestTime()).isEqualTo(UPDATED_REPORT_REQUEST_TIME);
        assertThat(testReportRequisition.getReportPassword()).isEqualTo(UPDATED_REPORT_PASSWORD);
        assertThat(testReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportRequisition.getReportFileAttachment()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT);
        assertThat(testReportRequisition.getReportFileAttachmentContentType()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE);
        assertThat(testReportRequisition.getReportFileCheckSum()).isEqualTo(UPDATED_REPORT_FILE_CHECK_SUM);
        assertThat(testReportRequisition.getReportNotes()).isEqualTo(UPDATED_REPORT_NOTES);
        assertThat(testReportRequisition.getReportNotesContentType()).isEqualTo(UPDATED_REPORT_NOTES_CONTENT_TYPE);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository).save(testReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void partialUpdateReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();

        // Update the reportRequisition using partial update
        ReportRequisition partialUpdatedReportRequisition = new ReportRequisition();
        partialUpdatedReportRequisition.setId(reportRequisition.getId());

        partialUpdatedReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportRequestTime(UPDATED_REPORT_REQUEST_TIME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportId(UPDATED_REPORT_ID)
            .reportFileAttachment(UPDATED_REPORT_FILE_ATTACHMENT)
            .reportFileAttachmentContentType(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)
            .reportFileCheckSum(UPDATED_REPORT_FILE_CHECK_SUM);

        restReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        ReportRequisition testReportRequisition = reportRequisitionList.get(reportRequisitionList.size() - 1);
        assertThat(testReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReportRequisition.getReportRequestTime()).isEqualTo(UPDATED_REPORT_REQUEST_TIME);
        assertThat(testReportRequisition.getReportPassword()).isEqualTo(UPDATED_REPORT_PASSWORD);
        assertThat(testReportRequisition.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);
        assertThat(testReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportRequisition.getReportFileAttachment()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT);
        assertThat(testReportRequisition.getReportFileAttachmentContentType()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE);
        assertThat(testReportRequisition.getReportFileCheckSum()).isEqualTo(UPDATED_REPORT_FILE_CHECK_SUM);
        assertThat(testReportRequisition.getReportNotes()).isEqualTo(DEFAULT_REPORT_NOTES);
        assertThat(testReportRequisition.getReportNotesContentType()).isEqualTo(DEFAULT_REPORT_NOTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();

        // Update the reportRequisition using partial update
        ReportRequisition partialUpdatedReportRequisition = new ReportRequisition();
        partialUpdatedReportRequisition.setId(reportRequisition.getId());

        partialUpdatedReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportRequestTime(UPDATED_REPORT_REQUEST_TIME)
            .reportPassword(UPDATED_REPORT_PASSWORD)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID)
            .reportFileAttachment(UPDATED_REPORT_FILE_ATTACHMENT)
            .reportFileAttachmentContentType(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)
            .reportFileCheckSum(UPDATED_REPORT_FILE_CHECK_SUM)
            .reportNotes(UPDATED_REPORT_NOTES)
            .reportNotesContentType(UPDATED_REPORT_NOTES_CONTENT_TYPE);

        restReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        ReportRequisition testReportRequisition = reportRequisitionList.get(reportRequisitionList.size() - 1);
        assertThat(testReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReportRequisition.getReportRequestTime()).isEqualTo(UPDATED_REPORT_REQUEST_TIME);
        assertThat(testReportRequisition.getReportPassword()).isEqualTo(UPDATED_REPORT_PASSWORD);
        assertThat(testReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testReportRequisition.getReportFileAttachment()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT);
        assertThat(testReportRequisition.getReportFileAttachmentContentType()).isEqualTo(UPDATED_REPORT_FILE_ATTACHMENT_CONTENT_TYPE);
        assertThat(testReportRequisition.getReportFileCheckSum()).isEqualTo(UPDATED_REPORT_FILE_CHECK_SUM);
        assertThat(testReportRequisition.getReportNotes()).isEqualTo(UPDATED_REPORT_NOTES);
        assertThat(testReportRequisition.getReportNotesContentType()).isEqualTo(UPDATED_REPORT_NOTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = reportRequisitionRepository.findAll().size();
        reportRequisition.setId(count.incrementAndGet());

        // Create the ReportRequisition
        ReportRequisitionDTO reportRequisitionDTO = reportRequisitionMapper.toDto(reportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportRequisition in the database
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(0)).save(reportRequisition);
    }

    @Test
    @Transactional
    void deleteReportRequisition() throws Exception {
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);

        int databaseSizeBeforeDelete = reportRequisitionRepository.findAll().size();

        // Delete the reportRequisition
        restReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportRequisition> reportRequisitionList = reportRequisitionRepository.findAll();
        assertThat(reportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportRequisition in Elasticsearch
        verify(mockReportRequisitionSearchRepository, times(1)).deleteById(reportRequisition.getId());
    }

    @Test
    @Transactional
    void searchReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportRequisitionRepository.saveAndFlush(reportRequisition);
        when(mockReportRequisitionSearchRepository.search("id:" + reportRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportRequisition), PageRequest.of(0, 1), 1));

        // Search the reportRequisition
        restReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportRequestTime").value(hasItem(sameInstant(DEFAULT_REPORT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].reportPassword").value(hasItem(DEFAULT_REPORT_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].reportFileAttachmentContentType").value(hasItem(DEFAULT_REPORT_FILE_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFileAttachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].reportFileCheckSum").value(hasItem(DEFAULT_REPORT_FILE_CHECK_SUM.toString())))
            .andExpect(jsonPath("$.[*].reportNotesContentType").value(hasItem(DEFAULT_REPORT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_NOTES))));
    }
}
