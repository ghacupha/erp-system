package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.WorkInProgressOutstandingReport;
import io.github.erp.repository.WorkInProgressOutstandingReportRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepository;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link WorkInProgressOutstandingReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkInProgressOutstandingReportResourceIT {

    private static final String DEFAULT_SEQUENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_4217_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ISO_4217_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INSTALMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSTALMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INSTALMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TRANSFER_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_TRANSFER_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/work-in-progress-outstanding-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-outstanding-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository;

    @Autowired
    private WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressOutstandingReportSearchRepository mockWorkInProgressOutstandingReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressOutstandingReportMockMvc;

    private WorkInProgressOutstandingReport workInProgressOutstandingReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOutstandingReport createEntity(EntityManager em) {
        WorkInProgressOutstandingReport workInProgressOutstandingReport = new WorkInProgressOutstandingReport()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .dealerName(DEFAULT_DEALER_NAME)
            .iso4217Code(DEFAULT_ISO_4217_CODE)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .totalTransferAmount(DEFAULT_TOTAL_TRANSFER_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT);
        return workInProgressOutstandingReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOutstandingReport createUpdatedEntity(EntityManager em) {
        WorkInProgressOutstandingReport workInProgressOutstandingReport = new WorkInProgressOutstandingReport()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .dealerName(UPDATED_DEALER_NAME)
            .iso4217Code(UPDATED_ISO_4217_CODE)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .totalTransferAmount(UPDATED_TOTAL_TRANSFER_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT);
        return workInProgressOutstandingReport;
    }

    @BeforeEach
    public void initTest() {
        workInProgressOutstandingReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReports() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].iso4217Code").value(hasItem(DEFAULT_ISO_4217_CODE)))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTransferAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }

    @Test
    @Transactional
    void getWorkInProgressOutstandingReport() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get the workInProgressOutstandingReport
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressOutstandingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressOutstandingReport.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.iso4217Code").value(DEFAULT_ISO_4217_CODE))
            .andExpect(jsonPath("$.instalmentAmount").value(sameNumber(DEFAULT_INSTALMENT_AMOUNT)))
            .andExpect(jsonPath("$.totalTransferAmount").value(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)));
    }

    @Test
    @Transactional
    void getWorkInProgressOutstandingReportsByIdFiltering() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        Long id = workInProgressOutstandingReport.getId();

        defaultWorkInProgressOutstandingReportShouldBeFound("id.equals=" + id);
        defaultWorkInProgressOutstandingReportShouldNotBeFound("id.notEquals=" + id);

        defaultWorkInProgressOutstandingReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkInProgressOutstandingReportShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkInProgressOutstandingReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkInProgressOutstandingReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressOutstandingReportList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressOutstandingReportList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the workInProgressOutstandingReportList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.specified=true");

        // Get all the workInProgressOutstandingReportList where sequenceNumber is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber contains DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.contains=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressOutstandingReportList where sequenceNumber contains UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.contains=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsBySequenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where sequenceNumber does not contain DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldNotBeFound("sequenceNumber.doesNotContain=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressOutstandingReportList where sequenceNumber does not contain UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressOutstandingReportShouldBeFound("sequenceNumber.doesNotContain=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars equals to DEFAULT_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressOutstandingReportList where particulars equals to UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars not equals to DEFAULT_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressOutstandingReportList where particulars not equals to UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the workInProgressOutstandingReportList where particulars equals to UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.specified=true");

        // Get all the workInProgressOutstandingReportList where particulars is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars contains DEFAULT_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressOutstandingReportList where particulars contains UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where particulars does not contain DEFAULT_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressOutstandingReportList where particulars does not contain UPDATED_PARTICULARS
        defaultWorkInProgressOutstandingReportShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName equals to DEFAULT_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the workInProgressOutstandingReportList where dealerName equals to UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the workInProgressOutstandingReportList where dealerName not equals to UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the workInProgressOutstandingReportList where dealerName equals to UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.specified=true");

        // Get all the workInProgressOutstandingReportList where dealerName is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName contains DEFAULT_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the workInProgressOutstandingReportList where dealerName contains UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the workInProgressOutstandingReportList where dealerName does not contain UPDATED_DEALER_NAME
        defaultWorkInProgressOutstandingReportShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code equals to DEFAULT_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.equals=" + DEFAULT_ISO_4217_CODE);

        // Get all the workInProgressOutstandingReportList where iso4217Code equals to UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.equals=" + UPDATED_ISO_4217_CODE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code not equals to DEFAULT_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.notEquals=" + DEFAULT_ISO_4217_CODE);

        // Get all the workInProgressOutstandingReportList where iso4217Code not equals to UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.notEquals=" + UPDATED_ISO_4217_CODE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code in DEFAULT_ISO_4217_CODE or UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.in=" + DEFAULT_ISO_4217_CODE + "," + UPDATED_ISO_4217_CODE);

        // Get all the workInProgressOutstandingReportList where iso4217Code equals to UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.in=" + UPDATED_ISO_4217_CODE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.specified=true");

        // Get all the workInProgressOutstandingReportList where iso4217Code is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code contains DEFAULT_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.contains=" + DEFAULT_ISO_4217_CODE);

        // Get all the workInProgressOutstandingReportList where iso4217Code contains UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.contains=" + UPDATED_ISO_4217_CODE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByIso4217CodeNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where iso4217Code does not contain DEFAULT_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldNotBeFound("iso4217Code.doesNotContain=" + DEFAULT_ISO_4217_CODE);

        // Get all the workInProgressOutstandingReportList where iso4217Code does not contain UPDATED_ISO_4217_CODE
        defaultWorkInProgressOutstandingReportShouldBeFound("iso4217Code.doesNotContain=" + UPDATED_ISO_4217_CODE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.equals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.equals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount not equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.notEquals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount not equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.notEquals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount in DEFAULT_INSTALMENT_AMOUNT or UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound(
            "instalmentAmount.in=" + DEFAULT_INSTALMENT_AMOUNT + "," + UPDATED_INSTALMENT_AMOUNT
        );

        // Get all the workInProgressOutstandingReportList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.in=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.specified=true");

        // Get all the workInProgressOutstandingReportList where instalmentAmount is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is greater than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.greaterThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is greater than or equal to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.greaterThanOrEqual=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is less than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.lessThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is less than or equal to SMALLER_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.lessThanOrEqual=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is less than DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.lessThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is less than UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.lessThan=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByInstalmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is greater than DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("instalmentAmount.greaterThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressOutstandingReportList where instalmentAmount is greater than SMALLER_INSTALMENT_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("instalmentAmount.greaterThan=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount equals to DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.equals=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount equals to UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.equals=" + UPDATED_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount not equals to DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.notEquals=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount not equals to UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.notEquals=" + UPDATED_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount in DEFAULT_TOTAL_TRANSFER_AMOUNT or UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound(
            "totalTransferAmount.in=" + DEFAULT_TOTAL_TRANSFER_AMOUNT + "," + UPDATED_TOTAL_TRANSFER_AMOUNT
        );

        // Get all the workInProgressOutstandingReportList where totalTransferAmount equals to UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.in=" + UPDATED_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.specified=true");

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is greater than or equal to DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is greater than or equal to UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.greaterThanOrEqual=" + UPDATED_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is less than or equal to DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.lessThanOrEqual=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is less than or equal to SMALLER_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.lessThanOrEqual=" + SMALLER_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is less than DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.lessThan=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is less than UPDATED_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.lessThan=" + UPDATED_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByTotalTransferAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is greater than DEFAULT_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("totalTransferAmount.greaterThan=" + DEFAULT_TOTAL_TRANSFER_AMOUNT);

        // Get all the workInProgressOutstandingReportList where totalTransferAmount is greater than SMALLER_TOTAL_TRANSFER_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("totalTransferAmount.greaterThan=" + SMALLER_TOTAL_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound(
            "outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT
        );

        // Get all the workInProgressOutstandingReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is not null
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.specified=true");

        // Get all the workInProgressOutstandingReportList where outstandingAmount is null
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOutstandingReportsByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the workInProgressOutstandingReportList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultWorkInProgressOutstandingReportShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkInProgressOutstandingReportShouldBeFound(String filter) throws Exception {
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].iso4217Code").value(hasItem(DEFAULT_ISO_4217_CODE)))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTransferAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));

        // Check, that the count call also returns 1
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkInProgressOutstandingReportShouldNotBeFound(String filter) throws Exception {
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressOutstandingReport() throws Exception {
        // Get the workInProgressOutstandingReport
        restWorkInProgressOutstandingReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchWorkInProgressOutstandingReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressOutstandingReportRepository.saveAndFlush(workInProgressOutstandingReport);
        when(
            mockWorkInProgressOutstandingReportSearchRepository.search(
                "id:" + workInProgressOutstandingReport.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressOutstandingReport), PageRequest.of(0, 1), 1));

        // Search the workInProgressOutstandingReport
        restWorkInProgressOutstandingReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressOutstandingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOutstandingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].iso4217Code").value(hasItem(DEFAULT_ISO_4217_CODE)))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTransferAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }
}
