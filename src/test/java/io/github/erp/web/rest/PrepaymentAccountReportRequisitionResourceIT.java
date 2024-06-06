package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.PrepaymentAccountReportRequisition;
import io.github.erp.repository.PrepaymentAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentAccountReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportRequisitionMapper;
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
 * Integration tests for the {@link PrepaymentAccountReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentAccountReportRequisitionResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUISITION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TAMPERED = false;
    private static final Boolean UPDATED_TAMPERED = true;

    private static final UUID DEFAULT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_FILENAME = UUID.randomUUID();

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/prepayment-account-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-account-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository;

    @Autowired
    private PrepaymentAccountReportRequisitionMapper prepaymentAccountReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentAccountReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentAccountReportRequisitionSearchRepository mockPrepaymentAccountReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentAccountReportRequisitionMockMvc;

    private PrepaymentAccountReportRequisition prepaymentAccountReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccountReportRequisition createEntity(EntityManager em) {
        PrepaymentAccountReportRequisition prepaymentAccountReportRequisition = new PrepaymentAccountReportRequisition()
            .reportName(DEFAULT_REPORT_NAME)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportDate(DEFAULT_REPORT_DATE)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        return prepaymentAccountReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccountReportRequisition createUpdatedEntity(EntityManager em) {
        PrepaymentAccountReportRequisition prepaymentAccountReportRequisition = new PrepaymentAccountReportRequisition()
            .reportName(UPDATED_REPORT_NAME)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportDate(UPDATED_REPORT_DATE)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        return prepaymentAccountReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        prepaymentAccountReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = prepaymentAccountReportRequisitionRepository.findAll().size();
        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentAccountReportRequisition testPrepaymentAccountReportRequisition = prepaymentAccountReportRequisitionList.get(
            prepaymentAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentAccountReportRequisition.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testPrepaymentAccountReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testPrepaymentAccountReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testPrepaymentAccountReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testPrepaymentAccountReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentAccountReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testPrepaymentAccountReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testPrepaymentAccountReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testPrepaymentAccountReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(1)).save(testPrepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void createPrepaymentAccountReportRequisitionWithExistingId() throws Exception {
        // Create the PrepaymentAccountReportRequisition with an existing ID
        prepaymentAccountReportRequisition.setId(1L);
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        int databaseSizeBeforeCreate = prepaymentAccountReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void checkReportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentAccountReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentAccountReportRequisition.setReportName(null);

        // Create the PrepaymentAccountReportRequisition, which fails.
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentAccountReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentAccountReportRequisition.setTimeOfRequisition(null);

        // Create the PrepaymentAccountReportRequisition, which fails.
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentAccountReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentAccountReportRequisition.setReportDate(null);

        // Create the PrepaymentAccountReportRequisition, which fails.
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitions() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }

    @Test
    @Transactional
    void getPrepaymentAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get the prepaymentAccountReportRequisition
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentAccountReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentAccountReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.timeOfRequisition").value(sameInstant(DEFAULT_TIME_OF_REQUISITION)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)));
    }

    @Test
    @Transactional
    void getPrepaymentAccountReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        Long id = prepaymentAccountReportRequisition.getId();

        defaultPrepaymentAccountReportRequisitionShouldBeFound("id.equals=" + id);
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentAccountReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentAccountReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName equals to DEFAULT_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentAccountReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName not equals to DEFAULT_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentAccountReportRequisitionList where reportName not equals to UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the prepaymentAccountReportRequisitionList where reportName equals to UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where reportName is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName contains DEFAULT_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentAccountReportRequisitionList where reportName contains UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportName does not contain DEFAULT_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the prepaymentAccountReportRequisitionList where reportName does not contain UPDATED_REPORT_NAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentAccountReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentAccountReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentAccountReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultPrepaymentAccountReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentAccountReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentAccountReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the prepaymentAccountReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where tampered is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where tampered is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the prepaymentAccountReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the prepaymentAccountReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the prepaymentAccountReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where filename is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where filename is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where reportDate is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentAccountReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the prepaymentAccountReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters is not null
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the prepaymentAccountReportRequisitionList where reportParameters is null
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentAccountReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultPrepaymentAccountReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);
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
        prepaymentAccountReportRequisition.setRequestedBy(requestedBy);
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the prepaymentAccountReportRequisitionList where requestedBy equals to requestedById
        defaultPrepaymentAccountReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the prepaymentAccountReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);
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
        prepaymentAccountReportRequisition.setLastAccessedBy(lastAccessedBy);
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the prepaymentAccountReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultPrepaymentAccountReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the prepaymentAccountReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultPrepaymentAccountReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentAccountReportRequisitionShouldBeFound(String filter) throws Exception {
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentAccountReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentAccountReportRequisition() throws Exception {
        // Get the prepaymentAccountReportRequisition
        restPrepaymentAccountReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentAccountReportRequisition
        PrepaymentAccountReportRequisition updatedPrepaymentAccountReportRequisition = prepaymentAccountReportRequisitionRepository
            .findById(prepaymentAccountReportRequisition.getId())
            .get();
        // Disconnect from session so that the updates on updatedPrepaymentAccountReportRequisition are not directly saved in db
        em.detach(updatedPrepaymentAccountReportRequisition);
        updatedPrepaymentAccountReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportDate(UPDATED_REPORT_DATE)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            updatedPrepaymentAccountReportRequisition
        );

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAccountReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccountReportRequisition testPrepaymentAccountReportRequisition = prepaymentAccountReportRequisitionList.get(
            prepaymentAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentAccountReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentAccountReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentAccountReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentAccountReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testPrepaymentAccountReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentAccountReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentAccountReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentAccountReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository).save(testPrepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAccountReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentAccountReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentAccountReportRequisition using partial update
        PrepaymentAccountReportRequisition partialUpdatedPrepaymentAccountReportRequisition = new PrepaymentAccountReportRequisition();
        partialUpdatedPrepaymentAccountReportRequisition.setId(prepaymentAccountReportRequisition.getId());

        partialUpdatedPrepaymentAccountReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .reportDate(UPDATED_REPORT_DATE)
            .reportParameters(UPDATED_REPORT_PARAMETERS);

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAccountReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAccountReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccountReportRequisition testPrepaymentAccountReportRequisition = prepaymentAccountReportRequisitionList.get(
            prepaymentAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentAccountReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentAccountReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testPrepaymentAccountReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentAccountReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testPrepaymentAccountReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentAccountReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentAccountReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testPrepaymentAccountReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentAccountReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentAccountReportRequisition using partial update
        PrepaymentAccountReportRequisition partialUpdatedPrepaymentAccountReportRequisition = new PrepaymentAccountReportRequisition();
        partialUpdatedPrepaymentAccountReportRequisition.setId(prepaymentAccountReportRequisition.getId());

        partialUpdatedPrepaymentAccountReportRequisition
            .reportName(UPDATED_REPORT_NAME)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportDate(UPDATED_REPORT_DATE)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAccountReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAccountReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccountReportRequisition testPrepaymentAccountReportRequisition = prepaymentAccountReportRequisitionList.get(
            prepaymentAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentAccountReportRequisition.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testPrepaymentAccountReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentAccountReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentAccountReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testPrepaymentAccountReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentAccountReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentAccountReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentAccountReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentAccountReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountReportRequisitionRepository.findAll().size();
        prepaymentAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentAccountReportRequisition
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionMapper.toDto(
            prepaymentAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAccountReportRequisition in the database
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(0)).save(prepaymentAccountReportRequisition);
    }

    @Test
    @Transactional
    void deletePrepaymentAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);

        int databaseSizeBeforeDelete = prepaymentAccountReportRequisitionRepository.findAll().size();

        // Delete the prepaymentAccountReportRequisition
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentAccountReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentAccountReportRequisition> prepaymentAccountReportRequisitionList = prepaymentAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentAccountReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentAccountReportRequisitionSearchRepository, times(1)).deleteById(prepaymentAccountReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentAccountReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentAccountReportRequisitionRepository.saveAndFlush(prepaymentAccountReportRequisition);
        when(
            mockPrepaymentAccountReportRequisitionSearchRepository.search(
                "id:" + prepaymentAccountReportRequisition.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentAccountReportRequisition), PageRequest.of(0, 1), 1));

        // Search the prepaymentAccountReportRequisition
        restPrepaymentAccountReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentAccountReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
