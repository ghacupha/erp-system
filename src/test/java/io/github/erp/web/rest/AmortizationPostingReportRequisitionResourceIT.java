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
import io.github.erp.domain.AmortizationPeriod;
import io.github.erp.domain.AmortizationPostingReportRequisition;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.repository.AmortizationPostingReportRequisitionRepository;
import io.github.erp.repository.search.AmortizationPostingReportRequisitionSearchRepository;
import io.github.erp.service.criteria.AmortizationPostingReportRequisitionCriteria;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import io.github.erp.service.mapper.AmortizationPostingReportRequisitionMapper;
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
 * Integration tests for the {@link AmortizationPostingReportRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AmortizationPostingReportRequisitionResourceIT {

    private static final UUID DEFAULT_REQUEST_ID = UUID.randomUUID();
    private static final UUID UPDATED_REQUEST_ID = UUID.randomUUID();

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

    private static final String ENTITY_API_URL = "/api/amortization-posting-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/amortization-posting-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmortizationPostingReportRequisitionRepository amortizationPostingReportRequisitionRepository;

    @Autowired
    private AmortizationPostingReportRequisitionMapper amortizationPostingReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AmortizationPostingReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmortizationPostingReportRequisitionSearchRepository mockAmortizationPostingReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmortizationPostingReportRequisitionMockMvc;

    private AmortizationPostingReportRequisition amortizationPostingReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPostingReportRequisition createEntity(EntityManager em) {
        AmortizationPostingReportRequisition amortizationPostingReportRequisition = new AmortizationPostingReportRequisition()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        AmortizationPeriod amortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            amortizationPeriod = AmortizationPeriodResourceIT.createEntity(em);
            em.persist(amortizationPeriod);
            em.flush();
        } else {
            amortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        amortizationPostingReportRequisition.setAmortizationPeriod(amortizationPeriod);
        return amortizationPostingReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPostingReportRequisition createUpdatedEntity(EntityManager em) {
        AmortizationPostingReportRequisition amortizationPostingReportRequisition = new AmortizationPostingReportRequisition()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        AmortizationPeriod amortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            amortizationPeriod = AmortizationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(amortizationPeriod);
            em.flush();
        } else {
            amortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        amortizationPostingReportRequisition.setAmortizationPeriod(amortizationPeriod);
        return amortizationPostingReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        amortizationPostingReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeCreate = amortizationPostingReportRequisitionRepository.findAll().size();
        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        AmortizationPostingReportRequisition testAmortizationPostingReportRequisition = amortizationPostingReportRequisitionList.get(
            amortizationPostingReportRequisitionList.size() - 1
        );
        assertThat(testAmortizationPostingReportRequisition.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testAmortizationPostingReportRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testAmortizationPostingReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testAmortizationPostingReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testAmortizationPostingReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAmortizationPostingReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testAmortizationPostingReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testAmortizationPostingReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(1)).save(testAmortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void createAmortizationPostingReportRequisitionWithExistingId() throws Exception {
        // Create the AmortizationPostingReportRequisition with an existing ID
        amortizationPostingReportRequisition.setId(1L);
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        int databaseSizeBeforeCreate = amortizationPostingReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPostingReportRequisitionRepository.findAll().size();
        // set the field null
        amortizationPostingReportRequisition.setRequestId(null);

        // Create the AmortizationPostingReportRequisition, which fails.
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPostingReportRequisitionRepository.findAll().size();
        // set the field null
        amortizationPostingReportRequisition.setTimeOfRequisition(null);

        // Create the AmortizationPostingReportRequisition, which fails.
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitions() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
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
    void getAmortizationPostingReportRequisition() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get the amortizationPostingReportRequisition
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, amortizationPostingReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amortizationPostingReportRequisition.getId().intValue()))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID.toString()))
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
    void getAmortizationPostingReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        Long id = amortizationPostingReportRequisition.getId();

        defaultAmortizationPostingReportRequisitionShouldBeFound("id.equals=" + id);
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultAmortizationPostingReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultAmortizationPostingReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where requestId equals to DEFAULT_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the amortizationPostingReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where requestId not equals to DEFAULT_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the amortizationPostingReportRequisitionList where requestId not equals to UPDATED_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the amortizationPostingReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where requestId is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("requestId.specified=true");

        // Get all the amortizationPostingReportRequisitionList where requestId is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the amortizationPostingReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultAmortizationPostingReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the amortizationPostingReportRequisitionList where fileChecksum is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the amortizationPostingReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultAmortizationPostingReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the amortizationPostingReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the amortizationPostingReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the amortizationPostingReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where tampered is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the amortizationPostingReportRequisitionList where tampered is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultAmortizationPostingReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the amortizationPostingReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the amortizationPostingReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultAmortizationPostingReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultAmortizationPostingReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the amortizationPostingReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where filename is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the amortizationPostingReportRequisitionList where filename is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the amortizationPostingReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the amortizationPostingReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the amortizationPostingReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters is not null
        defaultAmortizationPostingReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the amortizationPostingReportRequisitionList where reportParameters is null
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the amortizationPostingReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        // Get all the amortizationPostingReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the amortizationPostingReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultAmortizationPostingReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByAmortizationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
        AmortizationPeriod amortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            amortizationPeriod = AmortizationPeriodResourceIT.createEntity(em);
            em.persist(amortizationPeriod);
            em.flush();
        } else {
            amortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        em.persist(amortizationPeriod);
        em.flush();
        amortizationPostingReportRequisition.setAmortizationPeriod(amortizationPeriod);
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
        Long amortizationPeriodId = amortizationPeriod.getId();

        // Get all the amortizationPostingReportRequisitionList where amortizationPeriod equals to amortizationPeriodId
        defaultAmortizationPostingReportRequisitionShouldBeFound("amortizationPeriodId.equals=" + amortizationPeriodId);

        // Get all the amortizationPostingReportRequisitionList where amortizationPeriod equals to (amortizationPeriodId + 1)
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("amortizationPeriodId.equals=" + (amortizationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
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
        amortizationPostingReportRequisition.setRequestedBy(requestedBy);
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the amortizationPostingReportRequisitionList where requestedBy equals to requestedById
        defaultAmortizationPostingReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the amortizationPostingReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
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
        amortizationPostingReportRequisition.setLastAccessedBy(lastAccessedBy);
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the amortizationPostingReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultAmortizationPostingReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the amortizationPostingReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultAmortizationPostingReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmortizationPostingReportRequisitionShouldBeFound(String filter) throws Exception {
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmortizationPostingReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmortizationPostingReportRequisition() throws Exception {
        // Get the amortizationPostingReportRequisition
        restAmortizationPostingReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmortizationPostingReportRequisition() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();

        // Update the amortizationPostingReportRequisition
        AmortizationPostingReportRequisition updatedAmortizationPostingReportRequisition = amortizationPostingReportRequisitionRepository
            .findById(amortizationPostingReportRequisition.getId())
            .get();
        // Disconnect from session so that the updates on updatedAmortizationPostingReportRequisition are not directly saved in db
        em.detach(updatedAmortizationPostingReportRequisition);
        updatedAmortizationPostingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            updatedAmortizationPostingReportRequisition
        );

        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationPostingReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPostingReportRequisition testAmortizationPostingReportRequisition = amortizationPostingReportRequisitionList.get(
            amortizationPostingReportRequisitionList.size() - 1
        );
        assertThat(testAmortizationPostingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAmortizationPostingReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testAmortizationPostingReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAmortizationPostingReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testAmortizationPostingReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAmortizationPostingReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAmortizationPostingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAmortizationPostingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository).save(testAmortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void putNonExistingAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationPostingReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdateAmortizationPostingReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();

        // Update the amortizationPostingReportRequisition using partial update
        AmortizationPostingReportRequisition partialUpdatedAmortizationPostingReportRequisition = new AmortizationPostingReportRequisition();
        partialUpdatedAmortizationPostingReportRequisition.setId(amortizationPostingReportRequisition.getId());

        partialUpdatedAmortizationPostingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationPostingReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationPostingReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPostingReportRequisition testAmortizationPostingReportRequisition = amortizationPostingReportRequisitionList.get(
            amortizationPostingReportRequisitionList.size() - 1
        );
        assertThat(testAmortizationPostingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAmortizationPostingReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testAmortizationPostingReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testAmortizationPostingReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testAmortizationPostingReportRequisition.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAmortizationPostingReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAmortizationPostingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAmortizationPostingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAmortizationPostingReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();

        // Update the amortizationPostingReportRequisition using partial update
        AmortizationPostingReportRequisition partialUpdatedAmortizationPostingReportRequisition = new AmortizationPostingReportRequisition();
        partialUpdatedAmortizationPostingReportRequisition.setId(amortizationPostingReportRequisition.getId());

        partialUpdatedAmortizationPostingReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationPostingReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationPostingReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPostingReportRequisition testAmortizationPostingReportRequisition = amortizationPostingReportRequisitionList.get(
            amortizationPostingReportRequisitionList.size() - 1
        );
        assertThat(testAmortizationPostingReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testAmortizationPostingReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testAmortizationPostingReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testAmortizationPostingReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testAmortizationPostingReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAmortizationPostingReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testAmortizationPostingReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testAmortizationPostingReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amortizationPostingReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmortizationPostingReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPostingReportRequisitionRepository.findAll().size();
        amortizationPostingReportRequisition.setId(count.incrementAndGet());

        // Create the AmortizationPostingReportRequisition
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPostingReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPostingReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationPostingReportRequisition in the database
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(0)).save(amortizationPostingReportRequisition);
    }

    @Test
    @Transactional
    void deleteAmortizationPostingReportRequisition() throws Exception {
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);

        int databaseSizeBeforeDelete = amortizationPostingReportRequisitionRepository.findAll().size();

        // Delete the amortizationPostingReportRequisition
        restAmortizationPostingReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, amortizationPostingReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AmortizationPostingReportRequisition> amortizationPostingReportRequisitionList = amortizationPostingReportRequisitionRepository.findAll();
        assertThat(amortizationPostingReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AmortizationPostingReportRequisition in Elasticsearch
        verify(mockAmortizationPostingReportRequisitionSearchRepository, times(1)).deleteById(amortizationPostingReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchAmortizationPostingReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        amortizationPostingReportRequisitionRepository.saveAndFlush(amortizationPostingReportRequisition);
        when(
            mockAmortizationPostingReportRequisitionSearchRepository.search(
                "id:" + amortizationPostingReportRequisition.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(amortizationPostingReportRequisition), PageRequest.of(0, 1), 1));

        // Search the amortizationPostingReportRequisition
        restAmortizationPostingReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + amortizationPostingReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
