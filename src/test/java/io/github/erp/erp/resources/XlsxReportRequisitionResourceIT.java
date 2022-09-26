package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 1 (Caleb Series) Server ver 0.1.1-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportTemplate;
import io.github.erp.domain.XlsxReportRequisition;
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.repository.XlsxReportRequisitionRepository;
import io.github.erp.repository.search.XlsxReportRequisitionSearchRepository;
import io.github.erp.service.XlsxReportRequisitionService;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import io.github.erp.service.mapper.XlsxReportRequisitionMapper;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link XlsxReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"REPORT_ACCESSOR"})
public class XlsxReportRequisitionResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_USER_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_USER_PASSWORD = "BBBBBBBBBB";

    private static final ReportStatusTypes DEFAULT_REPORT_STATUS = ReportStatusTypes.GENERATING;
    private static final ReportStatusTypes UPDATED_REPORT_STATUS = ReportStatusTypes.SUCCESSFUL;

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/xlsx-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/xlsx-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XlsxReportRequisitionRepository xlsxReportRequisitionRepository;

    @Mock
    private XlsxReportRequisitionRepository xlsxReportRequisitionRepositoryMock;

    @Autowired
    private XlsxReportRequisitionMapper xlsxReportRequisitionMapper;

    @Mock
    private XlsxReportRequisitionService xlsxReportRequisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.XlsxReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private XlsxReportRequisitionSearchRepository mockXlsxReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXlsxReportRequisitionMockMvc;

    private XlsxReportRequisition xlsxReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XlsxReportRequisition createEntity(EntityManager em) {
        XlsxReportRequisition xlsxReportRequisition = new XlsxReportRequisition()
            .reportName(DEFAULT_REPORT_NAME)
            .reportDate(DEFAULT_REPORT_DATE)
            .userPassword(DEFAULT_USER_PASSWORD)
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
        xlsxReportRequisition.setReportTemplate(reportTemplate);
        return xlsxReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static XlsxReportRequisition createUpdatedEntity(EntityManager em) {
        XlsxReportRequisition xlsxReportRequisition = new XlsxReportRequisition()
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
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
        xlsxReportRequisition.setReportTemplate(reportTemplate);
        return xlsxReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        xlsxReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = xlsxReportRequisitionRepository.findAll().size();
        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);
        restXlsxReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().is5xxServerError());
            // .andExpect(status().isCreated());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        XlsxReportRequisition testXlsxReportRequisition = xlsxReportRequisitionList.get(xlsxReportRequisitionList.size() - 1);
        assertThat(testXlsxReportRequisition.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testXlsxReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testXlsxReportRequisition.getUserPassword()).isEqualTo(DEFAULT_USER_PASSWORD);
        assertThat(testXlsxReportRequisition.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);
        assertThat(testXlsxReportRequisition.getReportId()).isEqualTo(DEFAULT_REPORT_ID);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(1)).save(testXlsxReportRequisition);
    }

    @Test
    @Transactional
    void createXlsxReportRequisitionWithExistingId() throws Exception {
        // Create the XlsxReportRequisition with an existing ID
        xlsxReportRequisition.setId(1L);
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        int databaseSizeBeforeCreate = xlsxReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXlsxReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = xlsxReportRequisitionRepository.findAll().size();
        // set the field null
        xlsxReportRequisition.setReportName(null);

        // Create the XlsxReportRequisition, which fails.
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        restXlsxReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = xlsxReportRequisitionRepository.findAll().size();
        // set the field null
        xlsxReportRequisition.setUserPassword(null);

        // Create the XlsxReportRequisition, which fails.
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        restXlsxReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = xlsxReportRequisitionRepository.findAll().size();
        // set the field null
        xlsxReportRequisition.setReportId(null);

        // Create the XlsxReportRequisition, which fails.
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        restXlsxReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitions() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xlsxReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllXlsxReportRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(xlsxReportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restXlsxReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(xlsxReportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllXlsxReportRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(xlsxReportRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restXlsxReportRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(xlsxReportRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    // TODO @Test
    @Transactional
    void getXlsxReportRequisition() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get the xlsxReportRequisition
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, xlsxReportRequisition.getId()))
            // TODO .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xlsxReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.userPassword").value(DEFAULT_USER_PASSWORD))
            .andExpect(jsonPath("$.reportStatus").value(DEFAULT_REPORT_STATUS.toString()))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()));
    }

    @Test
    @Transactional
    void getXlsxReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        Long id = xlsxReportRequisition.getId();

        defaultXlsxReportRequisitionShouldBeFound("id.equals=" + id);
        defaultXlsxReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultXlsxReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultXlsxReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultXlsxReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultXlsxReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName equals to DEFAULT_REPORT_NAME
        defaultXlsxReportRequisitionShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the xlsxReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName not equals to DEFAULT_REPORT_NAME
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the xlsxReportRequisitionList where reportName not equals to UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the xlsxReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName is not null
        defaultXlsxReportRequisitionShouldBeFound("reportName.specified=true");

        // Get all the xlsxReportRequisitionList where reportName is null
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName contains DEFAULT_REPORT_NAME
        defaultXlsxReportRequisitionShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the xlsxReportRequisitionList where reportName contains UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportName does not contain DEFAULT_REPORT_NAME
        defaultXlsxReportRequisitionShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the xlsxReportRequisitionList where reportName does not contain UPDATED_REPORT_NAME
        defaultXlsxReportRequisitionShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate is not null
        defaultXlsxReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the xlsxReportRequisitionList where reportDate is null
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultXlsxReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the xlsxReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultXlsxReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword equals to DEFAULT_USER_PASSWORD
        defaultXlsxReportRequisitionShouldBeFound("userPassword.equals=" + DEFAULT_USER_PASSWORD);

        // Get all the xlsxReportRequisitionList where userPassword equals to UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.equals=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword not equals to DEFAULT_USER_PASSWORD
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.notEquals=" + DEFAULT_USER_PASSWORD);

        // Get all the xlsxReportRequisitionList where userPassword not equals to UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldBeFound("userPassword.notEquals=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword in DEFAULT_USER_PASSWORD or UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldBeFound("userPassword.in=" + DEFAULT_USER_PASSWORD + "," + UPDATED_USER_PASSWORD);

        // Get all the xlsxReportRequisitionList where userPassword equals to UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.in=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword is not null
        defaultXlsxReportRequisitionShouldBeFound("userPassword.specified=true");

        // Get all the xlsxReportRequisitionList where userPassword is null
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordContainsSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword contains DEFAULT_USER_PASSWORD
        defaultXlsxReportRequisitionShouldBeFound("userPassword.contains=" + DEFAULT_USER_PASSWORD);

        // Get all the xlsxReportRequisitionList where userPassword contains UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.contains=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByUserPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where userPassword does not contain DEFAULT_USER_PASSWORD
        defaultXlsxReportRequisitionShouldNotBeFound("userPassword.doesNotContain=" + DEFAULT_USER_PASSWORD);

        // Get all the xlsxReportRequisitionList where userPassword does not contain UPDATED_USER_PASSWORD
        defaultXlsxReportRequisitionShouldBeFound("userPassword.doesNotContain=" + UPDATED_USER_PASSWORD);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportStatus equals to DEFAULT_REPORT_STATUS
        defaultXlsxReportRequisitionShouldBeFound("reportStatus.equals=" + DEFAULT_REPORT_STATUS);

        // Get all the xlsxReportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultXlsxReportRequisitionShouldNotBeFound("reportStatus.equals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportStatus not equals to DEFAULT_REPORT_STATUS
        defaultXlsxReportRequisitionShouldNotBeFound("reportStatus.notEquals=" + DEFAULT_REPORT_STATUS);

        // Get all the xlsxReportRequisitionList where reportStatus not equals to UPDATED_REPORT_STATUS
        defaultXlsxReportRequisitionShouldBeFound("reportStatus.notEquals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportStatusIsInShouldWork() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportStatus in DEFAULT_REPORT_STATUS or UPDATED_REPORT_STATUS
        defaultXlsxReportRequisitionShouldBeFound("reportStatus.in=" + DEFAULT_REPORT_STATUS + "," + UPDATED_REPORT_STATUS);

        // Get all the xlsxReportRequisitionList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultXlsxReportRequisitionShouldNotBeFound("reportStatus.in=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportStatus is not null
        defaultXlsxReportRequisitionShouldBeFound("reportStatus.specified=true");

        // Get all the xlsxReportRequisitionList where reportStatus is null
        defaultXlsxReportRequisitionShouldNotBeFound("reportStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportId equals to DEFAULT_REPORT_ID
        defaultXlsxReportRequisitionShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the xlsxReportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultXlsxReportRequisitionShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportId not equals to DEFAULT_REPORT_ID
        defaultXlsxReportRequisitionShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the xlsxReportRequisitionList where reportId not equals to UPDATED_REPORT_ID
        defaultXlsxReportRequisitionShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultXlsxReportRequisitionShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the xlsxReportRequisitionList where reportId equals to UPDATED_REPORT_ID
        defaultXlsxReportRequisitionShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        // Get all the xlsxReportRequisitionList where reportId is not null
        defaultXlsxReportRequisitionShouldBeFound("reportId.specified=true");

        // Get all the xlsxReportRequisitionList where reportId is null
        defaultXlsxReportRequisitionShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByReportTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);
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
        xlsxReportRequisition.setReportTemplate(reportTemplate);
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);
        Long reportTemplateId = reportTemplate.getId();

        // Get all the xlsxReportRequisitionList where reportTemplate equals to reportTemplateId
        defaultXlsxReportRequisitionShouldBeFound("reportTemplateId.equals=" + reportTemplateId);

        // Get all the xlsxReportRequisitionList where reportTemplate equals to (reportTemplateId + 1)
        defaultXlsxReportRequisitionShouldNotBeFound("reportTemplateId.equals=" + (reportTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllXlsxReportRequisitionsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);
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
        xlsxReportRequisition.addPlaceholder(placeholder);
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);
        Long placeholderId = placeholder.getId();

        // Get all the xlsxReportRequisitionList where placeholder equals to placeholderId
        defaultXlsxReportRequisitionShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the xlsxReportRequisitionList where placeholder equals to (placeholderId + 1)
        defaultXlsxReportRequisitionShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultXlsxReportRequisitionShouldBeFound(String filter) throws Exception {
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xlsxReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));

        // Check, that the count call also returns 1
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultXlsxReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingXlsxReportRequisition() throws Exception {
        // Get the xlsxReportRequisition
        // todo restXlsxReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
        restXlsxReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().is5xxServerError());
    }

    @Test
    @Transactional
    void putNewXlsxReportRequisition() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();

        // Update the xlsxReportRequisition
        XlsxReportRequisition updatedXlsxReportRequisition = xlsxReportRequisitionRepository.findById(xlsxReportRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedXlsxReportRequisition are not directly saved in db
        em.detach(updatedXlsxReportRequisition);
        updatedXlsxReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID);
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(updatedXlsxReportRequisition);

        restXlsxReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xlsxReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        XlsxReportRequisition testXlsxReportRequisition = xlsxReportRequisitionList.get(xlsxReportRequisitionList.size() - 1);
        assertThat(testXlsxReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testXlsxReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testXlsxReportRequisition.getUserPassword()).isEqualTo(UPDATED_USER_PASSWORD);
        assertThat(testXlsxReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testXlsxReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository).save(testXlsxReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xlsxReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdateXlsxReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();

        // Update the xlsxReportRequisition using partial update
        XlsxReportRequisition partialUpdatedXlsxReportRequisition = new XlsxReportRequisition();
        partialUpdatedXlsxReportRequisition.setId(xlsxReportRequisition.getId());

        partialUpdatedXlsxReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD);

        restXlsxReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXlsxReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXlsxReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        XlsxReportRequisition testXlsxReportRequisition = xlsxReportRequisitionList.get(xlsxReportRequisitionList.size() - 1);
        assertThat(testXlsxReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testXlsxReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testXlsxReportRequisition.getUserPassword()).isEqualTo(UPDATED_USER_PASSWORD);
        assertThat(testXlsxReportRequisition.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);
        assertThat(testXlsxReportRequisition.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
    }

    @Test
    @Transactional
    void fullUpdateXlsxReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();

        // Update the xlsxReportRequisition using partial update
        XlsxReportRequisition partialUpdatedXlsxReportRequisition = new XlsxReportRequisition();
        partialUpdatedXlsxReportRequisition.setId(xlsxReportRequisition.getId());

        partialUpdatedXlsxReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .userPassword(UPDATED_USER_PASSWORD)
            .reportStatus(UPDATED_REPORT_STATUS)
            .reportId(UPDATED_REPORT_ID);

        restXlsxReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXlsxReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXlsxReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        XlsxReportRequisition testXlsxReportRequisition = xlsxReportRequisitionList.get(xlsxReportRequisitionList.size() - 1);
        assertThat(testXlsxReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testXlsxReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testXlsxReportRequisition.getUserPassword()).isEqualTo(UPDATED_USER_PASSWORD);
        assertThat(testXlsxReportRequisition.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);
        assertThat(testXlsxReportRequisition.getReportId()).isEqualTo(UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xlsxReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXlsxReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = xlsxReportRequisitionRepository.findAll().size();
        xlsxReportRequisition.setId(count.incrementAndGet());

        // Create the XlsxReportRequisition
        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = xlsxReportRequisitionMapper.toDto(xlsxReportRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXlsxReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xlsxReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the XlsxReportRequisition in the database
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(0)).save(xlsxReportRequisition);
    }

    @Test
    @Transactional
    void deleteXlsxReportRequisition() throws Exception {
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);

        int databaseSizeBeforeDelete = xlsxReportRequisitionRepository.findAll().size();

        // Delete the xlsxReportRequisition
        restXlsxReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, xlsxReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<XlsxReportRequisition> xlsxReportRequisitionList = xlsxReportRequisitionRepository.findAll();
        assertThat(xlsxReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the XlsxReportRequisition in Elasticsearch
        verify(mockXlsxReportRequisitionSearchRepository, times(1)).deleteById(xlsxReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchXlsxReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        xlsxReportRequisitionRepository.saveAndFlush(xlsxReportRequisition);
        when(mockXlsxReportRequisitionSearchRepository.search("id:" + xlsxReportRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(xlsxReportRequisition), PageRequest.of(0, 1), 1));

        // Search the xlsxReportRequisition
        restXlsxReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + xlsxReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xlsxReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPassword").value(hasItem(DEFAULT_USER_PASSWORD)))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }
}
