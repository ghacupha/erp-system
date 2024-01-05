package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.PdfReportRequisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportTemplate;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.repository.PdfReportRequisitionRepository;
import io.github.erp.repository.search.PdfReportRequisitionSearchRepository;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.criteria.PdfReportRequisitionCriteria;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.mapper.PdfReportRequisitionMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link PdfReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PdfReportRequisitionResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_USER_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_USER_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final ReportStatusTypes DEFAULT_REPORT_STATUS = ReportStatusTypes.GENERATING;
    private static final ReportStatusTypes UPDATED_REPORT_STATUS = ReportStatusTypes.SUCCESSFUL;

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/pdf-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/pdf-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PdfReportRequisitionRepository pdfReportRequisitionRepository;

    @Mock
    private PdfReportRequisitionRepository pdfReportRequisitionRepositoryMock;

    @Autowired
    private PdfReportRequisitionMapper pdfReportRequisitionMapper;

    @Mock
    private PdfReportRequisitionService pdfReportRequisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PdfReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PdfReportRequisitionSearchRepository mockPdfReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPdfReportRequisitionMockMvc;

    private PdfReportRequisition pdfReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PdfReportRequisition createEntity(EntityManager em) {
        PdfReportRequisition pdfReportRequisition = new PdfReportRequisition()
            .reportName(DEFAULT_REPORT_NAME)
            .reportDate(DEFAULT_REPORT_DATE)
            .userPassword(DEFAULT_USER_PASSWORD)
            .ownerPassword(DEFAULT_OWNER_PASSWORD)
            .reportFileChecksum(DEFAULT_REPORT_FILE_CHECKSUM)
            .reportStatus(DEFAULT_REPORT_STATUS)
            .reportId(DEFAULT_REPORT_ID);
        // Add required entity
        ReportTemplate reportTemplate;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportTemplate = ReportTemplateResourceIT.createEntity(em);
            em.persist(reportTemplate);
            em.flush();
        } else {
            reportTemplate = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        pdfReportRequisition.setReportTemplate(reportTemplate);
        return pdfReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PdfReportRequisition createUpdatedEntity(EntityManager em) {
        PdfReportRequisition pdfReportRequisition = new PdfReportRequisition()
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
            .ownerPassword(UPDATED_OWNER_PASSWORD)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID);
        // Add required entity
        ReportTemplate reportTemplate;
        if (TestUtil.findAll(em, ReportTemplate.class).isEmpty()) {
            reportTemplate = ReportTemplateResourceIT.createUpdatedEntity(em);
            em.persist(reportTemplate);
            em.flush();
        } else {
            reportTemplate = TestUtil.findAll(em, ReportTemplate.class).get(0);
        }
        pdfReportRequisition.setReportTemplate(reportTemplate);
        return pdfReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        pdfReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createPdfReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = pdfReportRequisitionRepository.findAll().size();
        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);
        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PdfReportRequisition testPdfReportRequisition = pdfReportRequisitionList.get(pdfReportRequisitionList.size() - 1);
        assertThat(testPdfReportRequisition.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testPdfReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testPdfReportRequisition.getUserPassword()).isEqualTo(DEFAULT_USER_PASSWORD);
        assertThat(testPdfReportRequisition.getOwnerPassword()).isEqualTo(DEFAULT_OWNER_PASSWORD);
        assertThat(testPdfReportRequisition.getReportFileChecksum()).isEqualTo(DEFAULT_REPORT_FILE_CHECKSUM);
        assertThat(testPdfReportRequisition.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);
        assertThat(testPdfReportRequisition.getReportId()).isEqualTo(DEFAULT_REPORT_ID);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(1)).save(testPdfReportRequisition);
    }

    @Test
    @Transactional
    void createPdfReportRequisitionWithExistingId() throws Exception {
        // Create the PdfReportRequisition with an existing ID
        pdfReportRequisition.setId(1L);
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        int databaseSizeBeforeCreate = pdfReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pdfReportRequisitionRepository.findAll().size();
        // set the field null
        pdfReportRequisition.setReportName(null);

        // Create the PdfReportRequisition, which fails.
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = pdfReportRequisitionRepository.findAll().size();
        // set the field null
        pdfReportRequisition.setUserPassword(null);

        // Create the PdfReportRequisition, which fails.
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = pdfReportRequisitionRepository.findAll().size();
        // set the field null
        pdfReportRequisition.setOwnerPassword(null);

        // Create the PdfReportRequisition, which fails.
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pdfReportRequisitionRepository.findAll().size();
        // set the field null
        pdfReportRequisition.setReportId(null);

        // Create the PdfReportRequisition, which fails.
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        restPdfReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitions() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pdfReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].ownerPassword").value(hasItem(DEFAULT_OWNER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPdfReportRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pdfReportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPdfReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pdfReportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPdfReportRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pdfReportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPdfReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pdfReportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPdfReportRequisition() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get the pdfReportRequisition
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, pdfReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pdfReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.userPassword").value(DEFAULT_USER_PASSWORD))
            .andExpect(jsonPath("$.ownerPassword").value(DEFAULT_OWNER_PASSWORD))
            .andExpect(jsonPath("$.reportFileChecksum").value(DEFAULT_REPORT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.reportStatus").value(DEFAULT_REPORT_STATUS.toString()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()));
    }

    @Test
    @Transactional
    void getPdfReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        Long id = pdfReportRequisition.getId();

        defaultPdfReportRequisitionShouldBeFound("id.equals=" + id);
        defaultPdfReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultPdfReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPdfReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultPdfReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPdfReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName equals to DEFAULT_REPORT_NAME
        defaultPdfReportRequisitionShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the pdfReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName not equals to DEFAULT_REPORT_NAME
        defaultPdfReportRequisitionShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the pdfReportRequisitionList where reportName not equals to UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the pdfReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName is not null
        defaultPdfReportRequisitionShouldBeFound("reportName.specified=true");

        // Get all the pdfReportRequisitionList where reportName is null
        defaultPdfReportRequisitionShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName contains DEFAULT_REPORT_NAME
        defaultPdfReportRequisitionShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the pdfReportRequisitionList where reportName contains UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportName does not contain DEFAULT_REPORT_NAME
        defaultPdfReportRequisitionShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the pdfReportRequisitionList where reportName does not contain UPDATED_REPORT_NAME
        defaultPdfReportRequisitionShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate is not null
        defaultPdfReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the pdfReportRequisitionList where reportDate is null
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultPdfReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the pdfReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultPdfReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword equals to DEFAULT_USER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("userPassword.equals=" + DEFAULT_USER_PASSWORD);

        // Get all the pdfReportRequisitionList where userPassword equals to UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.equals=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword not equals to DEFAULT_USER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.notEquals=" + DEFAULT_USER_PASSWORD);

        // Get all the pdfReportRequisitionList where userPassword not equals to UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("userPassword.notEquals=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword in DEFAULT_USER_PASSWORD or UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("userPassword.in=" + DEFAULT_USER_PASSWORD + "," + UPDATED_USER_PASSWORD);

        // Get all the pdfReportRequisitionList where userPassword equals to UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.in=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword is not null
        defaultPdfReportRequisitionShouldBeFound("userPassword.specified=true");

        // Get all the pdfReportRequisitionList where userPassword is null
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword contains DEFAULT_USER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("userPassword.contains=" + DEFAULT_USER_PASSWORD);

        // Get all the pdfReportRequisitionList where userPassword contains UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.contains=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByUserPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where userPassword does not contain DEFAULT_USER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("userPassword.doesNotContain=" + DEFAULT_USER_PASSWORD);

        // Get all the pdfReportRequisitionList where userPassword does not contain UPDATED_USER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("userPassword.doesNotContain=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword equals to DEFAULT_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.equals=" + DEFAULT_OWNER_PASSWORD);

        // Get all the pdfReportRequisitionList where ownerPassword equals to UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.equals=" + UPDATED_OWNER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword not equals to DEFAULT_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.notEquals=" + DEFAULT_OWNER_PASSWORD);

        // Get all the pdfReportRequisitionList where ownerPassword not equals to UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.notEquals=" + UPDATED_OWNER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword in DEFAULT_OWNER_PASSWORD or UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.in=" + DEFAULT_OWNER_PASSWORD + "," + UPDATED_OWNER_PASSWORD);

        // Get all the pdfReportRequisitionList where ownerPassword equals to UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.in=" + UPDATED_OWNER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword is not null
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.specified=true");

        // Get all the pdfReportRequisitionList where ownerPassword is null
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword contains DEFAULT_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.contains=" + DEFAULT_OWNER_PASSWORD);

        // Get all the pdfReportRequisitionList where ownerPassword contains UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.contains=" + UPDATED_OWNER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByOwnerPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where ownerPassword does not contain DEFAULT_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldNotBeFound("ownerPassword.doesNotContain=" + DEFAULT_OWNER_PASSWORD);

        // Get all the pdfReportRequisitionList where ownerPassword does not contain UPDATED_OWNER_PASSWORD
        defaultPdfReportRequisitionShouldBeFound("ownerPassword.doesNotContain=" + UPDATED_OWNER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum equals to DEFAULT_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldBeFound("reportFileChecksum.equals=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the pdfReportRequisitionList where reportFileChecksum equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.equals=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum not equals to DEFAULT_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.notEquals=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the pdfReportRequisitionList where reportFileChecksum not equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldBeFound("reportFileChecksum.notEquals=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum in DEFAULT_REPORT_FILE_CHECKSUM or UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldBeFound(
            "reportFileChecksum.in=" + DEFAULT_REPORT_FILE_CHECKSUM + "," + UPDATED_REPORT_FILE_CHECKSUM
        );

        // Get all the pdfReportRequisitionList where reportFileChecksum equals to UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.in=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum is not null
        defaultPdfReportRequisitionShouldBeFound("reportFileChecksum.specified=true");

        // Get all the pdfReportRequisitionList where reportFileChecksum is null
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum contains DEFAULT_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldBeFound("reportFileChecksum.contains=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the pdfReportRequisitionList where reportFileChecksum contains UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.contains=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportFileChecksum does not contain DEFAULT_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldNotBeFound("reportFileChecksum.doesNotContain=" + DEFAULT_REPORT_FILE_CHECKSUM);

        // Get all the pdfReportRequisitionList where reportFileChecksum does not contain UPDATED_REPORT_FILE_CHECKSUM
        defaultPdfReportRequisitionShouldBeFound("reportFileChecksum.doesNotContain=" + UPDATED_REPORT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportStatus equals to DEFAULT_REPORT_STATUS
        defaultPdfReportRequisitionShouldBeFound("reportStatus.equals=" + DEFAULT_REPORT_STATUS);

        // Get all the pdfReportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultPdfReportRequisitionShouldNotBeFound("reportStatus.equals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportStatus not equals to DEFAULT_REPORT_STATUS
        defaultPdfReportRequisitionShouldNotBeFound("reportStatus.notEquals=" + DEFAULT_REPORT_STATUS);

        // Get all the pdfReportRequisitionList where reportStatus not equals to UPDATED_REPORT_STATUS
        defaultPdfReportRequisitionShouldBeFound("reportStatus.notEquals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportStatusIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportStatus in DEFAULT_REPORT_STATUS or UPDATED_REPORT_STATUS
        defaultPdfReportRequisitionShouldBeFound("reportStatus.in=" + DEFAULT_REPORT_STATUS + "," + UPDATED_REPORT_STATUS);

        // Get all the pdfReportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultPdfReportRequisitionShouldNotBeFound("reportStatus.in=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportStatus is not null
        defaultPdfReportRequisitionShouldBeFound("reportStatus.specified=true");

        // Get all the pdfReportRequisitionList where reportStatus is null
        defaultPdfReportRequisitionShouldNotBeFound("reportStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportId equals to DEFAULT_REPORT_ID
        defaultPdfReportRequisitionShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the pdfReportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultPdfReportRequisitionShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportId not equals to DEFAULT_REPORT_ID
        defaultPdfReportRequisitionShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the pdfReportRequisitionList where reportId not equals to UPDATED_REPORT_ID
        defaultPdfReportRequisitionShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultPdfReportRequisitionShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the pdfReportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultPdfReportRequisitionShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        // Get all the pdfReportRequisitionList where reportId is not null
        defaultPdfReportRequisitionShouldBeFound("reportId.specified=true");

        // Get all the pdfReportRequisitionList where reportId is null
        defaultPdfReportRequisitionShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByReportTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
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
        pdfReportRequisition.setReportTemplate(reportTemplate);
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
        Long reportTemplateId = reportTemplate.getId();

        // Get all the pdfReportRequisitionList where reportTemplate equals to reportTemplateId
        defaultPdfReportRequisitionShouldBeFound("reportTemplateId.equals=" + reportTemplateId);

        // Get all the pdfReportRequisitionList where reportTemplate equals to (reportTemplateId + 1)
        defaultPdfReportRequisitionShouldNotBeFound("reportTemplateId.equals=" + (reportTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
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
        pdfReportRequisition.addPlaceholder(placeholder);
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
        Long placeholderId = placeholder.getId();

        // Get all the pdfReportRequisitionList where placeholder equals to placeholderId
        defaultPdfReportRequisitionShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the pdfReportRequisitionList where placeholder equals to (placeholderId + 1)
        defaultPdfReportRequisitionShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPdfReportRequisitionsByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
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
        pdfReportRequisition.addParameters(parameters);
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
        Long parametersId = parameters.getId();

        // Get all the pdfReportRequisitionList where parameters equals to parametersId
        defaultPdfReportRequisitionShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the pdfReportRequisitionList where parameters equals to (parametersId + 1)
        defaultPdfReportRequisitionShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPdfReportRequisitionShouldBeFound(String filter) throws Exception {
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pdfReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].ownerPassword").value(hasItem(DEFAULT_OWNER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));

        // Check, that the count call also returns 1
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPdfReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPdfReportRequisition() throws Exception {
        // Get the pdfReportRequisition
        restPdfReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPdfReportRequisition() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();

        // Update the pdfReportRequisition
        PdfReportRequisition updatedPdfReportRequisition = pdfReportRequisitionRepository.findById(pdfReportRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedPdfReportRequisition are not directly saved in db
        em.detach(updatedPdfReportRequisition);
        updatedPdfReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
            .ownerPassword(UPDATED_OWNER_PASSWORD)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID);
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(updatedPdfReportRequisition);

        restPdfReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pdfReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PdfReportRequisition testPdfReportRequisition = pdfReportRequisitionList.get(pdfReportRequisitionList.size() - 1);
        assertThat(testPdfReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPdfReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPdfReportRequisition.getUserPassword()).isEqualTo(UPDATED_USER_PASSWORD);
        assertThat(testPdfReportRequisition.getOwnerPassword()).isEqualTo(UPDATED_OWNER_PASSWORD);
        assertThat(testPdfReportRequisition.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);
        assertThat(testPdfReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testPdfReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository).save(testPdfReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pdfReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdatePdfReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();

        // Update the pdfReportRequisition using partial update
        PdfReportRequisition partialUpdatedPdfReportRequisition = new PdfReportRequisition();
        partialUpdatedPdfReportRequisition.setId(pdfReportRequisition.getId());

        partialUpdatedPdfReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .ownerPassword(UPDATED_OWNER_PASSWORD)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM)
            .reportStatus(UPDATED_REPORT_STATUS);

        restPdfReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPdfReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPdfReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PdfReportRequisition testPdfReportRequisition = pdfReportRequisitionList.get(pdfReportRequisitionList.size() - 1);
        assertThat(testPdfReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPdfReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPdfReportRequisition.getUserPassword()).isEqualTo(DEFAULT_USER_PASSWORD);
        assertThat(testPdfReportRequisition.getOwnerPassword()).isEqualTo(UPDATED_OWNER_PASSWORD);
        assertThat(testPdfReportRequisition.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);
        assertThat(testPdfReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testPdfReportRequisition.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
    }

    @Test
    @Transactional
    void fullUpdatePdfReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();

        // Update the pdfReportRequisition using partial update
        PdfReportRequisition partialUpdatedPdfReportRequisition = new PdfReportRequisition();
        partialUpdatedPdfReportRequisition.setId(pdfReportRequisition.getId());

        partialUpdatedPdfReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
            .ownerPassword(UPDATED_OWNER_PASSWORD)
            .reportFileChecksum(UPDATED_REPORT_FILE_CHECKSUM)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID);

        restPdfReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPdfReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPdfReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PdfReportRequisition testPdfReportRequisition = pdfReportRequisitionList.get(pdfReportRequisitionList.size() - 1);
        assertThat(testPdfReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPdfReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPdfReportRequisition.getUserPassword()).isEqualTo(UPDATED_USER_PASSWORD);
        assertThat(testPdfReportRequisition.getOwnerPassword()).isEqualTo(UPDATED_OWNER_PASSWORD);
        assertThat(testPdfReportRequisition.getReportFileChecksum()).isEqualTo(UPDATED_REPORT_FILE_CHECKSUM);
        assertThat(testPdfReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testPdfReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pdfReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPdfReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = pdfReportRequisitionRepository.findAll().size();
        pdfReportRequisition.setId(count.incrementAndGet());

        // Create the PdfReportRequisition
        PdfReportRequisitionDTO pdfReportRequisitionDTO = pdfReportRequisitionMapper.toDto(pdfReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPdfReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pdfReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PdfReportRequisition in the database
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(0)).save(pdfReportRequisition);
    }

    @Test
    @Transactional
    void deletePdfReportRequisition() throws Exception {
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);

        int databaseSizeBeforeDelete = pdfReportRequisitionRepository.findAll().size();

        // Delete the pdfReportRequisition
        restPdfReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pdfReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PdfReportRequisition> pdfReportRequisitionList = pdfReportRequisitionRepository.findAll();
        assertThat(pdfReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PdfReportRequisition in Elasticsearch
        verify(mockPdfReportRequisitionSearchRepository, times(1)).deleteById(pdfReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchPdfReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        pdfReportRequisitionRepository.saveAndFlush(pdfReportRequisition);
        when(mockPdfReportRequisitionSearchRepository.search("id:" + pdfReportRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pdfReportRequisition), PageRequest.of(0, 1), 1));

        // Search the pdfReportRequisition
        restPdfReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + pdfReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pdfReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].ownerPassword").value(hasItem(DEFAULT_OWNER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportFileChecksum").value(hasItem(DEFAULT_REPORT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }
}
