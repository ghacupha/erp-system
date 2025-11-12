package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.PrepaymentReportRequisition;
import io.github.erp.repository.PrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentReportRequisitionMapper;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link PrepaymentReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentReportRequisitionResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUISITION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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

    private static final String ENTITY_API_URL = "/api/prepayment-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository;

    @Autowired
    private PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentReportRequisitionSearchRepository mockPrepaymentReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentReportRequisitionMockMvc;

    private PrepaymentReportRequisition prepaymentReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentReportRequisition createEntity(EntityManager em) {
        PrepaymentReportRequisition prepaymentReportRequisition = new PrepaymentReportRequisition()
            .reportName(DEFAULT_REPORT_NAME)
            .reportDate(DEFAULT_REPORT_DATE)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return prepaymentReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentReportRequisition createUpdatedEntity(EntityManager em) {
        PrepaymentReportRequisition prepaymentReportRequisition = new PrepaymentReportRequisition()
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return prepaymentReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        prepaymentReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = prepaymentReportRequisitionRepository.findAll().size();
        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );
        restPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentReportRequisition testPrepaymentReportRequisition = prepaymentReportRequisitionList.get(
            prepaymentReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentReportRequisition.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testPrepaymentReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testPrepaymentReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testPrepaymentReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testPrepaymentReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testPrepaymentReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(1)).save(testPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void createPrepaymentReportRequisitionWithExistingId() throws Exception {
        // Create the PrepaymentReportRequisition with an existing ID
        prepaymentReportRequisition.setId(1L);
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        int databaseSizeBeforeCreate = prepaymentReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentReportRequisition.setReportName(null);

        // Create the PrepaymentReportRequisition, which fails.
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        restPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentReportRequisition.setReportDate(null);

        // Create the PrepaymentReportRequisition, which fails.
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        restPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentReportRequisition.setTimeOfRequisition(null);

        // Create the PrepaymentReportRequisition, which fails.
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        restPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitions() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getPrepaymentReportRequisition() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get the prepaymentReportRequisition
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.timeOfRequisition").value(sameInstant(DEFAULT_TIME_OF_REQUISITION)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getPrepaymentReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        Long id = prepaymentReportRequisition.getId();

        defaultPrepaymentReportRequisitionShouldBeFound("id.equals=" + id);
        defaultPrepaymentReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName equals to DEFAULT_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName not equals to DEFAULT_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentReportRequisitionList where reportName not equals to UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the prepaymentReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName is not null
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.specified=true");

        // Get all the prepaymentReportRequisitionList where reportName is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName contains DEFAULT_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentReportRequisitionList where reportName contains UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportName does not contain DEFAULT_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentReportRequisitionList where reportName does not contain UPDATED_REPORT_NAME
        defaultPrepaymentReportRequisitionShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate is not null
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the prepaymentReportRequisitionList where reportDate is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultPrepaymentReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the prepaymentReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is not null
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the prepaymentReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum is not null
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the prepaymentReportRequisitionList where fileChecksum is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultPrepaymentReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultPrepaymentReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultPrepaymentReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultPrepaymentReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultPrepaymentReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the prepaymentReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where tampered is not null
        defaultPrepaymentReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the prepaymentReportRequisitionList where tampered is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultPrepaymentReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the prepaymentReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the prepaymentReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultPrepaymentReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultPrepaymentReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the prepaymentReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where filename is not null
        defaultPrepaymentReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the prepaymentReportRequisitionList where filename is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the prepaymentReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters is not null
        defaultPrepaymentReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the prepaymentReportRequisitionList where reportParameters is null
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        // Get all the prepaymentReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultPrepaymentReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);
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
        prepaymentReportRequisition.setRequestedBy(requestedBy);
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the prepaymentReportRequisitionList where requestedBy equals to requestedById
        defaultPrepaymentReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the prepaymentReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultPrepaymentReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);
        ApplicationUser lastAccessedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastAccessedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastAccessedBy);
            em.flush();
        } else {
            lastAccessedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastAccessedBy);
        em.flush();
        prepaymentReportRequisition.setLastAccessedBy(lastAccessedBy);
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the prepaymentReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultPrepaymentReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the prepaymentReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultPrepaymentReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentReportRequisitionShouldBeFound(String filter) throws Exception {
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentReportRequisition() throws Exception {
        // Get the prepaymentReportRequisition
        restPrepaymentReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentReportRequisition() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();

        // Update the prepaymentReportRequisition
        PrepaymentReportRequisition updatedPrepaymentReportRequisition = prepaymentReportRequisitionRepository
            .findById(prepaymentReportRequisition.getId())
            .get();
        // Disconnect from session so that the updates on updatedPrepaymentReportRequisition are not directly saved in db
        em.detach(updatedPrepaymentReportRequisition);
        updatedPrepaymentReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            updatedPrepaymentReportRequisition
        );

        restPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentReportRequisition testPrepaymentReportRequisition = prepaymentReportRequisitionList.get(
            prepaymentReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testPrepaymentReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository).save(testPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();

        // Update the prepaymentReportRequisition using partial update
        PrepaymentReportRequisition partialUpdatedPrepaymentReportRequisition = new PrepaymentReportRequisition();
        partialUpdatedPrepaymentReportRequisition.setId(prepaymentReportRequisition.getId());

        partialUpdatedPrepaymentReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .reportParameters(UPDATED_REPORT_PARAMETERS);

        restPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentReportRequisition testPrepaymentReportRequisition = prepaymentReportRequisitionList.get(
            prepaymentReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testPrepaymentReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();

        // Update the prepaymentReportRequisition using partial update
        PrepaymentReportRequisition partialUpdatedPrepaymentReportRequisition = new PrepaymentReportRequisition();
        partialUpdatedPrepaymentReportRequisition.setId(prepaymentReportRequisition.getId());

        partialUpdatedPrepaymentReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .reportDate(UPDATED_REPORT_DATE)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentReportRequisition testPrepaymentReportRequisition = prepaymentReportRequisitionList.get(
            prepaymentReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testPrepaymentReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentReportRequisitionRepository.findAll().size();
        prepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentReportRequisition
        PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO = prepaymentReportRequisitionMapper.toDto(
            prepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentReportRequisition in the database
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(0)).save(prepaymentReportRequisition);
    }

    @Test
    @Transactional
    void deletePrepaymentReportRequisition() throws Exception {
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);

        int databaseSizeBeforeDelete = prepaymentReportRequisitionRepository.findAll().size();

        // Delete the prepaymentReportRequisition
        restPrepaymentReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentReportRequisition> prepaymentReportRequisitionList = prepaymentReportRequisitionRepository.findAll();
        assertThat(prepaymentReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentReportRequisition in Elasticsearch
        verify(mockPrepaymentReportRequisitionSearchRepository, times(1)).deleteById(prepaymentReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentReportRequisitionRepository.saveAndFlush(prepaymentReportRequisition);
        when(mockPrepaymentReportRequisitionSearchRepository.search("id:" + prepaymentReportRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentReportRequisition), PageRequest.of(0, 1), 1));

        // Search the prepaymentReportRequisition
        restPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
