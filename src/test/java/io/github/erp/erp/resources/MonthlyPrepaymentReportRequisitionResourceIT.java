package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.MonthlyPrepaymentReportRequisition;
import io.github.erp.repository.MonthlyPrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.MonthlyPrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.MonthlyPrepaymentReportRequisitionMapper;
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

/**
 * Integration tests for the MonthlyPrepaymentReportRequisitionResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
public class MonthlyPrepaymentReportRequisitionResourceIT {

    private static final String DEFAULT_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/prepayments/monthly-prepayment-report-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/monthly-prepayment-report-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository;

    @Autowired
    private MonthlyPrepaymentReportRequisitionMapper monthlyPrepaymentReportRequisitionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MonthlyPrepaymentReportRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private MonthlyPrepaymentReportRequisitionSearchRepository mockMonthlyPrepaymentReportRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonthlyPrepaymentReportRequisitionMockMvc;

    private MonthlyPrepaymentReportRequisition monthlyPrepaymentReportRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyPrepaymentReportRequisition createEntity(EntityManager em) {
        MonthlyPrepaymentReportRequisition monthlyPrepaymentReportRequisition = new MonthlyPrepaymentReportRequisition()
            .requestId(DEFAULT_REQUEST_ID)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .fileChecksum(DEFAULT_FILE_CHECKSUM)
            .tampered(DEFAULT_TAMPERED)
            .filename(DEFAULT_FILENAME)
            .reportParameters(DEFAULT_REPORT_PARAMETERS)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        monthlyPrepaymentReportRequisition.setFiscalYear(fiscalYear);
        return monthlyPrepaymentReportRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyPrepaymentReportRequisition createUpdatedEntity(EntityManager em) {
        MonthlyPrepaymentReportRequisition monthlyPrepaymentReportRequisition = new MonthlyPrepaymentReportRequisition()
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        monthlyPrepaymentReportRequisition.setFiscalYear(fiscalYear);
        return monthlyPrepaymentReportRequisition;
    }

    @BeforeEach
    public void initTest() {
        monthlyPrepaymentReportRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createMonthlyPrepaymentReportRequisitionWithExistingId() throws Exception {
        // Create the MonthlyPrepaymentReportRequisition with an existing ID
        monthlyPrepaymentReportRequisition.setId(1L);
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        int databaseSizeBeforeCreate = monthlyPrepaymentReportRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        // set the field null
        monthlyPrepaymentReportRequisition.setRequestId(null);

        // Create the MonthlyPrepaymentReportRequisition, which fails.
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        // set the field null
        monthlyPrepaymentReportRequisition.setTimeOfRequisition(null);

        // Create the MonthlyPrepaymentReportRequisition, which fails.
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitions() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID)))
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
    void getMonthlyPrepaymentReportRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        Long id = monthlyPrepaymentReportRequisition.getId();

        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("id.equals=" + id);
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId equals to DEFAULT_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId not equals to DEFAULT_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.notEquals=" + DEFAULT_REQUEST_ID);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId not equals to UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.notEquals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId equals to UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where requestId is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId contains DEFAULT_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.contains=" + DEFAULT_REQUEST_ID);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId contains UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.contains=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestIdNotContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId does not contain DEFAULT_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestId.doesNotContain=" + DEFAULT_REQUEST_ID);

        // Get all the monthlyPrepaymentReportRequisitionList where requestId does not contain UPDATED_REQUEST_ID
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestId.doesNotContain=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the monthlyPrepaymentReportRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum equals to DEFAULT_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.equals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.equals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum not equals to DEFAULT_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.notEquals=" + DEFAULT_FILE_CHECKSUM);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum not equals to UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.notEquals=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum in DEFAULT_FILE_CHECKSUM or UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.in=" + DEFAULT_FILE_CHECKSUM + "," + UPDATED_FILE_CHECKSUM);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum equals to UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.in=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum contains DEFAULT_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.contains=" + DEFAULT_FILE_CHECKSUM);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum contains UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.contains=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum does not contain DEFAULT_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fileChecksum.doesNotContain=" + DEFAULT_FILE_CHECKSUM);

        // Get all the monthlyPrepaymentReportRequisitionList where fileChecksum does not contain UPDATED_FILE_CHECKSUM
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fileChecksum.doesNotContain=" + UPDATED_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered equals to DEFAULT_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("tampered.equals=" + DEFAULT_TAMPERED);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("tampered.equals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered not equals to DEFAULT_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("tampered.notEquals=" + DEFAULT_TAMPERED);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered not equals to UPDATED_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("tampered.notEquals=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered in DEFAULT_TAMPERED or UPDATED_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("tampered.in=" + DEFAULT_TAMPERED + "," + UPDATED_TAMPERED);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered equals to UPDATED_TAMPERED
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("tampered.in=" + UPDATED_TAMPERED);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where tampered is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("tampered.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where tampered is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("tampered.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where filename equals to DEFAULT_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the monthlyPrepaymentReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFilenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where filename not equals to DEFAULT_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("filename.notEquals=" + DEFAULT_FILENAME);

        // Get all the monthlyPrepaymentReportRequisitionList where filename not equals to UPDATED_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("filename.notEquals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the monthlyPrepaymentReportRequisitionList where filename equals to UPDATED_FILENAME
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where filename is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("filename.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where filename is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters equals to DEFAULT_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("reportParameters.equals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.equals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters not equals to DEFAULT_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.notEquals=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters not equals to UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("reportParameters.notEquals=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters in DEFAULT_REPORT_PARAMETERS or UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound(
            "reportParameters.in=" + DEFAULT_REPORT_PARAMETERS + "," + UPDATED_REPORT_PARAMETERS
        );

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters equals to UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.in=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters is not null
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("reportParameters.specified=true");

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters is null
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters contains DEFAULT_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("reportParameters.contains=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters contains UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.contains=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByReportParametersNotContainsSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters does not contain DEFAULT_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("reportParameters.doesNotContain=" + DEFAULT_REPORT_PARAMETERS);

        // Get all the monthlyPrepaymentReportRequisitionList where reportParameters does not contain UPDATED_REPORT_PARAMETERS
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("reportParameters.doesNotContain=" + UPDATED_REPORT_PARAMETERS);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
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
        monthlyPrepaymentReportRequisition.setRequestedBy(requestedBy);
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
        Long requestedById = requestedBy.getId();

        // Get all the monthlyPrepaymentReportRequisitionList where requestedBy equals to requestedById
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the monthlyPrepaymentReportRequisitionList where requestedBy equals to (requestedById + 1)
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
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
        monthlyPrepaymentReportRequisition.setLastAccessedBy(lastAccessedBy);
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the monthlyPrepaymentReportRequisitionList where lastAccessedBy equals to lastAccessedById
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the monthlyPrepaymentReportRequisitionList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentReportRequisitionsByFiscalYearIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        monthlyPrepaymentReportRequisition.setFiscalYear(fiscalYear);
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
        Long fiscalYearId = fiscalYear.getId();

        // Get all the monthlyPrepaymentReportRequisitionList where fiscalYear equals to fiscalYearId
        defaultMonthlyPrepaymentReportRequisitionShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the monthlyPrepaymentReportRequisitionList where fiscalYear equals to (fiscalYearId + 1)
        defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonthlyPrepaymentReportRequisitionShouldBeFound(String filter) throws Exception {
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID)))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));

        // Check, that the count call also returns 1
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonthlyPrepaymentReportRequisitionShouldNotBeFound(String filter) throws Exception {
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonthlyPrepaymentReportRequisition() throws Exception {
        // Get the monthlyPrepaymentReportRequisition
        restMonthlyPrepaymentReportRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNonExistingMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monthlyPrepaymentReportRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void partialUpdateMonthlyPrepaymentReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();

        // Update the monthlyPrepaymentReportRequisition using partial update
        MonthlyPrepaymentReportRequisition partialUpdatedMonthlyPrepaymentReportRequisition = new MonthlyPrepaymentReportRequisition();
        partialUpdatedMonthlyPrepaymentReportRequisition.setId(monthlyPrepaymentReportRequisition.getId());

        partialUpdatedMonthlyPrepaymentReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .filename(UPDATED_FILENAME);

        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthlyPrepaymentReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonthlyPrepaymentReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        MonthlyPrepaymentReportRequisition testMonthlyPrepaymentReportRequisition = monthlyPrepaymentReportRequisitionList.get(
            monthlyPrepaymentReportRequisitionList.size() - 1
        );
        assertThat(testMonthlyPrepaymentReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testMonthlyPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testMonthlyPrepaymentReportRequisition.getFileChecksum()).isEqualTo(DEFAULT_FILE_CHECKSUM);
        assertThat(testMonthlyPrepaymentReportRequisition.getTampered()).isEqualTo(DEFAULT_TAMPERED);
        assertThat(testMonthlyPrepaymentReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportParameters()).isEqualTo(DEFAULT_REPORT_PARAMETERS);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMonthlyPrepaymentReportRequisitionWithPatch() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();

        // Update the monthlyPrepaymentReportRequisition using partial update
        MonthlyPrepaymentReportRequisition partialUpdatedMonthlyPrepaymentReportRequisition = new MonthlyPrepaymentReportRequisition();
        partialUpdatedMonthlyPrepaymentReportRequisition.setId(monthlyPrepaymentReportRequisition.getId());

        partialUpdatedMonthlyPrepaymentReportRequisition
            .requestId(UPDATED_REQUEST_ID)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .fileChecksum(UPDATED_FILE_CHECKSUM)
            .tampered(UPDATED_TAMPERED)
            .filename(UPDATED_FILENAME)
            .reportParameters(UPDATED_REPORT_PARAMETERS)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE);

        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthlyPrepaymentReportRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonthlyPrepaymentReportRequisition))
            )
            .andExpect(status().isOk());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);
        MonthlyPrepaymentReportRequisition testMonthlyPrepaymentReportRequisition = monthlyPrepaymentReportRequisitionList.get(
            monthlyPrepaymentReportRequisitionList.size() - 1
        );
        assertThat(testMonthlyPrepaymentReportRequisition.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testMonthlyPrepaymentReportRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testMonthlyPrepaymentReportRequisition.getFileChecksum()).isEqualTo(UPDATED_FILE_CHECKSUM);
        assertThat(testMonthlyPrepaymentReportRequisition.getTampered()).isEqualTo(UPDATED_TAMPERED);
        assertThat(testMonthlyPrepaymentReportRequisition.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportParameters()).isEqualTo(UPDATED_REPORT_PARAMETERS);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testMonthlyPrepaymentReportRequisition.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monthlyPrepaymentReportRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonthlyPrepaymentReportRequisition() throws Exception {
        int databaseSizeBeforeUpdate = monthlyPrepaymentReportRequisitionRepository.findAll().size();
        monthlyPrepaymentReportRequisition.setId(count.incrementAndGet());

        // Create the MonthlyPrepaymentReportRequisition
        MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionMapper.toDto(
            monthlyPrepaymentReportRequisition
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monthlyPrepaymentReportRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthlyPrepaymentReportRequisition in the database
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(0)).save(monthlyPrepaymentReportRequisition);
    }

    @Test
    @Transactional
    void deleteMonthlyPrepaymentReportRequisition() throws Exception {
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);

        int databaseSizeBeforeDelete = monthlyPrepaymentReportRequisitionRepository.findAll().size();

        // Delete the monthlyPrepaymentReportRequisition
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, monthlyPrepaymentReportRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonthlyPrepaymentReportRequisition> monthlyPrepaymentReportRequisitionList = monthlyPrepaymentReportRequisitionRepository.findAll();
        assertThat(monthlyPrepaymentReportRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MonthlyPrepaymentReportRequisition in Elasticsearch
        verify(mockMonthlyPrepaymentReportRequisitionSearchRepository, times(1)).deleteById(monthlyPrepaymentReportRequisition.getId());
    }

    @Test
    @Transactional
    void searchMonthlyPrepaymentReportRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        monthlyPrepaymentReportRequisitionRepository.saveAndFlush(monthlyPrepaymentReportRequisition);
        when(
            mockMonthlyPrepaymentReportRequisitionSearchRepository.search(
                "id:" + monthlyPrepaymentReportRequisition.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(monthlyPrepaymentReportRequisition), PageRequest.of(0, 1), 1));

        // Search the monthlyPrepaymentReportRequisition
        restMonthlyPrepaymentReportRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + monthlyPrepaymentReportRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentReportRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID)))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].fileChecksum").value(hasItem(DEFAULT_FILE_CHECKSUM)))
            .andExpect(jsonPath("$.[*].tampered").value(hasItem(DEFAULT_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].reportParameters").value(hasItem(DEFAULT_REPORT_PARAMETERS)))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))));
    }
}
