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
import io.github.erp.domain.PrepaymentByAccountReportRequisition;
import io.github.erp.repository.PrepaymentByAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentByAccountReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentByAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentByAccountReportRequisitionMapper;
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
 * Integration tests for the {@link PrepaymentByAccountReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentByAccountReportRequisitionResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUISITION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final UUID DEFAULT_FILENAME = UUID.randomUUID();
    private static final UUID UPDATED_FILENAME = UUID.randomUUID();

    private static final String DEFAULT_REPORT_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_PARAMETERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_TAMPERED = false;
    private static final Boolean UPDATED_TAMPERED = true;

    private static final String ENTITY_API_URL = "/api/prepayment-by-account-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-by-account-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository;

    @Autowired
    private PrepaymentByAccountReportRequisitionMapper prepaymentByAccountReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentByAccountReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentByAccountReportRequisitionSearchRepository mockPrepaymentByAccountReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentByAccountReportRequisitionMockMvc;

    private PrepaymentByAccountReportRequisition prepaymentByAccountReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentByAccountReportRequisition createEntity(EntityManager em) {
        PrepaymentByAccountReportRequisition prepaymentByAccountReportRequisition = new PrepaymentByAccountReportRequisition()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .reportDate(DEFAULT_REPORT_DATE)
            .tampered(DEFAULT_TAMPERED);
        return prepaymentByAccountReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentByAccountReportRequisition createUpdatedEntity(EntityManager em) {
        PrepaymentByAccountReportRequisition prepaymentByAccountReportRequisition = new PrepaymentByAccountReportRequisition()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .tampered(UPDATED_TAMPERED);
        return prepaymentByAccountReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        prepaymentByAccountReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentByAccountReportRequisition testPrepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionList.get(
            prepaymentByAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentByAccountReportRequisition.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testPrepaymentByAccountReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testPrepaymentByAccountReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testPrepaymentByAccountReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentByAccountReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testPrepaymentByAccountReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(1)).save(testPrepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void createPrepaymentByAccountReportRequisitionWithExistingId() throws Exception {
        // Create the PrepaymentByAccountReportRequisition with an existing ID
        prepaymentByAccountReportRequisition.setId(1L);
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        int databaseSizeBeforeCreate = prepaymentByAccountReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentByAccountReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentByAccountReportRequisition.setTimeOfRequisition(null);

        // Create the PrepaymentByAccountReportRequisition, which fails.
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentByAccountReportRequisitionRepository.findAll().size();
        // set the field null
        prepaymentByAccountReportRequisition.setReportDate(null);

        // Create the PrepaymentByAccountReportRequisition, which fails.
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitions() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentByAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())));
    }

    @Test
    @Transactional
    void getPrepaymentByAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get the prepaymentByAccountReportRequisition
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentByAccountReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentByAccountReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
            .andExpect(jsonPath("$.timeOfRequisition").value(sameInstant(DEFAULT_TIME_OF_REQUISITION)))
            .andExpect(jsonPath("$.fileChecksum").value(DEFAULT_FILE_CHECKSUM))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.reportParameters").value(DEFAULT_REPORT_PARAMETERS))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.tampered").value(DEFAULT_TAMPERED.booleanValue()));
    }

    @Test
    @Transactional
    void getPrepaymentByAccountReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        Long id = prepaymentByAccountReportRequisition.getId();

        defaultPrepaymentByAccountReportRequisitionShouldBeFound("id.equals=" + id);
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentByAccountReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentByAccountReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where requestId equals to DEFAULT_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the prepaymentByAccountReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where requestId not equals to DEFAULT_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the prepaymentByAccountReportRequisitionList where requestId not equals to UPDATED_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the prepaymentByAccountReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where requestId is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("requestId.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where requestId is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the prepaymentByAccountReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the prepaymentByAccountReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the prepaymentByAccountReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the prepaymentByAccountReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the prepaymentByAccountReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where filename is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where filename is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the prepaymentByAccountReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate equals to DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate not equals to DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.notEquals=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate not equals to UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.notEquals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate equals to UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is greater than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.greaterThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is greater than or equal to UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.greaterThanOrEqual=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is less than or equal to DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.lessThanOrEqual=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is less than or equal to SMALLER_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.lessThanOrEqual=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is less than DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is less than UPDATED_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByReportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is greater than DEFAULT_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("reportDate.greaterThan=" + DEFAULT_REPORT_DATE);

        // Get all the prepaymentByAccountReportRequisitionList where reportDate is greater than SMALLER_REPORT_DATE
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("reportDate.greaterThan=" + SMALLER_REPORT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentByAccountReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the prepaymentByAccountReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the prepaymentByAccountReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        // Get all the prepaymentByAccountReportRequisitionList where tampered is not null
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the prepaymentByAccountReportRequisitionList where tampered is null
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);
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
        prepaymentByAccountReportRequisition.setRequestedBy(requestedBy);
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the prepaymentByAccountReportRequisitionList where requestedBy equals to requestedById
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the prepaymentByAccountReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentByAccountReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);
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
        prepaymentByAccountReportRequisition.setLastAccessedBy(lastAccessedBy);
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the prepaymentByAccountReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultPrepaymentByAccountReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the prepaymentByAccountReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultPrepaymentByAccountReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentByAccountReportRequisitionShouldBeFound(String filter) throws Exception {
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentByAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())));

        // Check, that the count call also returns 1
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentByAccountReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentByAccountReportRequisition() throws Exception {
        // Get the prepaymentByAccountReportRequisition
        restPrepaymentByAccountReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentByAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisition updatedPrepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionRepository
            .findById(prepaymentByAccountReportRequisition.getId())
            .get();
        // Disconnect from session so that the updates on updatedPrepaymentByAccountReportRequisition are not directly saved in db
        em.detach(updatedPrepaymentByAccountReportRequisition);
        updatedPrepaymentByAccountReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .tampered(UPDATED_TAMPERED);
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            updatedPrepaymentByAccountReportRequisition
        );

        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentByAccountReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentByAccountReportRequisition testPrepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionList.get(
            prepaymentByAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentByAccountReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testPrepaymentByAccountReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentByAccountReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentByAccountReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentByAccountReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentByAccountReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository).save(testPrepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentByAccountReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentByAccountReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentByAccountReportRequisition using partial update
        PrepaymentByAccountReportRequisition partialUpdatedPrepaymentByAccountReportRequisition = new PrepaymentByAccountReportRequisition();
        partialUpdatedPrepaymentByAccountReportRequisition.setId(prepaymentByAccountReportRequisition.getId());

        partialUpdatedPrepaymentByAccountReportRequisition
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportDate(UPDATED_REPORT_DATE);

        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentByAccountReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentByAccountReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentByAccountReportRequisition testPrepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionList.get(
            prepaymentByAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentByAccountReportRequisition.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testPrepaymentByAccountReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentByAccountReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testPrepaymentByAccountReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testPrepaymentByAccountReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentByAccountReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentByAccountReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();

        // Update the prepaymentByAccountReportRequisition using partial update
        PrepaymentByAccountReportRequisition partialUpdatedPrepaymentByAccountReportRequisition = new PrepaymentByAccountReportRequisition();
        partialUpdatedPrepaymentByAccountReportRequisition.setId(prepaymentByAccountReportRequisition.getId());

        partialUpdatedPrepaymentByAccountReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .reportDate(UPDATED_REPORT_DATE)
            .tampered(UPDATED_TAMPERED);

        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentByAccountReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentByAccountReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentByAccountReportRequisition testPrepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionList.get(
            prepaymentByAccountReportRequisitionList.size() - 1
        );
        assertThat(testPrepaymentByAccountReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testPrepaymentByAccountReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testPrepaymentByAccountReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testPrepaymentByAccountReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testPrepaymentByAccountReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testPrepaymentByAccountReportRequisition.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testPrepaymentByAccountReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentByAccountReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentByAccountReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentByAccountReportRequisitionRepository.findAll().size();
        prepaymentByAccountReportRequisition.setId(count.incrementAndGet());

        // Create the PrepaymentByAccountReportRequisition
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentByAccountReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentByAccountReportRequisition in the database
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(0)).save(prepaymentByAccountReportRequisition);
    }

    @Test
    @Transactional
    void deletePrepaymentByAccountReportRequisition() throws Exception {
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);

        int databaseSizeBeforeDelete = prepaymentByAccountReportRequisitionRepository.findAll().size();

        // Delete the prepaymentByAccountReportRequisition
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentByAccountReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentByAccountReportRequisition> prepaymentByAccountReportRequisitionList = prepaymentByAccountReportRequisitionRepository.findAll();
        assertThat(prepaymentByAccountReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentByAccountReportRequisition in Elasticsearch
        verify(mockPrepaymentByAccountReportRequisitionSearchRepository, times(1)).deleteById(prepaymentByAccountReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentByAccountReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentByAccountReportRequisitionRepository.saveAndFlush(prepaymentByAccountReportRequisition);
        when(
            mockPrepaymentByAccountReportRequisitionSearchRepository.search(
                "id:" + prepaymentByAccountReportRequisition.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentByAccountReportRequisition), PageRequest.of(0, 1), 1));

        // Search the prepaymentByAccountReportRequisition
        restPrepaymentByAccountReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentByAccountReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentByAccountReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())));
    }
}
