package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.PrepaymentAccountReport;
import io.github.erp.repository.PrepaymentAccountReportRepository;
import io.github.erp.repository.search.PrepaymentAccountReportSearchRepository;
import io.github.erp.service.criteria.PrepaymentAccountReportCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportMapper;
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
 * Integration tests for the {@link PrepaymentAccountReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentAccountReportResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREPAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREPAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMORTISED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMORTISED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMORTISED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS = 1;
    private static final Integer UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS = 2;
    private static final Integer SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_AMORTISED_ITEMS = 1;
    private static final Integer UPDATED_NUMBER_OF_AMORTISED_ITEMS = 2;
    private static final Integer SMALLER_NUMBER_OF_AMORTISED_ITEMS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/prepayment-account-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-account-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentAccountReportRepository prepaymentAccountReportRepository;

    @Autowired
    private PrepaymentAccountReportMapper prepaymentAccountReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentAccountReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentAccountReportSearchRepository mockPrepaymentAccountReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentAccountReportMockMvc;

    private PrepaymentAccountReport prepaymentAccountReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccountReport createEntity(EntityManager em) {
        PrepaymentAccountReport prepaymentAccountReport = new PrepaymentAccountReport()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .prepaymentAmount(DEFAULT_PREPAYMENT_AMOUNT)
            .amortisedAmount(DEFAULT_AMORTISED_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)
            .numberOfAmortisedItems(DEFAULT_NUMBER_OF_AMORTISED_ITEMS);
        return prepaymentAccountReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccountReport createUpdatedEntity(EntityManager em) {
        PrepaymentAccountReport prepaymentAccountReport = new PrepaymentAccountReport()
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .amortisedAmount(UPDATED_AMORTISED_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS)
            .numberOfAmortisedItems(UPDATED_NUMBER_OF_AMORTISED_ITEMS);
        return prepaymentAccountReport;
    }

    @BeforeEach
    public void initTest() {
        prepaymentAccountReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReports() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)))
            .andExpect(jsonPath("$.[*].numberOfAmortisedItems").value(hasItem(DEFAULT_NUMBER_OF_AMORTISED_ITEMS)));
    }

    @Test
    @Transactional
    void getPrepaymentAccountReport() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get the prepaymentAccountReport
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentAccountReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentAccountReport.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.prepaymentAmount").value(sameNumber(DEFAULT_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.amortisedAmount").value(sameNumber(DEFAULT_AMORTISED_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)))
            .andExpect(jsonPath("$.numberOfPrepaymentAccounts").value(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS))
            .andExpect(jsonPath("$.numberOfAmortisedItems").value(DEFAULT_NUMBER_OF_AMORTISED_ITEMS));
    }

    @Test
    @Transactional
    void getPrepaymentAccountReportsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        Long id = prepaymentAccountReport.getId();

        defaultPrepaymentAccountReportShouldBeFound("id.equals=" + id);
        defaultPrepaymentAccountReportShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentAccountReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentAccountReportShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentAccountReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentAccountReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the prepaymentAccountReportList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the prepaymentAccountReportList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the prepaymentAccountReportList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName is not null
        defaultPrepaymentAccountReportShouldBeFound("accountName.specified=true");

        // Get all the prepaymentAccountReportList where accountName is null
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the prepaymentAccountReportList where accountName contains UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the prepaymentAccountReportList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultPrepaymentAccountReportShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the prepaymentAccountReportList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the prepaymentAccountReportList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the prepaymentAccountReportList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber is not null
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.specified=true");

        // Get all the prepaymentAccountReportList where accountNumber is null
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the prepaymentAccountReportList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the prepaymentAccountReportList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultPrepaymentAccountReportShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.equals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.equals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount not equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.notEquals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount not equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.notEquals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount in DEFAULT_PREPAYMENT_AMOUNT or UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.in=" + DEFAULT_PREPAYMENT_AMOUNT + "," + UPDATED_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.in=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount is not null
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.specified=true");

        // Get all the prepaymentAccountReportList where prepaymentAmount is null
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount is greater than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.greaterThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount is greater than or equal to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.greaterThanOrEqual=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount is less than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.lessThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount is less than or equal to SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.lessThanOrEqual=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount is less than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.lessThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount is less than UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.lessThan=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByPrepaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where prepaymentAmount is greater than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("prepaymentAmount.greaterThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountReportList where prepaymentAmount is greater than SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("prepaymentAmount.greaterThan=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount equals to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.equals=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.equals=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount not equals to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.notEquals=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount not equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.notEquals=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount in DEFAULT_AMORTISED_AMOUNT or UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.in=" + DEFAULT_AMORTISED_AMOUNT + "," + UPDATED_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.in=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount is not null
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.specified=true");

        // Get all the prepaymentAccountReportList where amortisedAmount is null
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount is greater than or equal to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.greaterThanOrEqual=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount is greater than or equal to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.greaterThanOrEqual=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount is less than or equal to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.lessThanOrEqual=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount is less than or equal to SMALLER_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.lessThanOrEqual=" + SMALLER_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount is less than DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.lessThan=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount is less than UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.lessThan=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByAmortisedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where amortisedAmount is greater than DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("amortisedAmount.greaterThan=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentAccountReportList where amortisedAmount is greater than SMALLER_AMORTISED_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("amortisedAmount.greaterThan=" + SMALLER_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound(
            "outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT
        );

        // Get all the prepaymentAccountReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount is not null
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.specified=true");

        // Get all the prepaymentAccountReportList where outstandingAmount is null
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentAccountReportList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultPrepaymentAccountReportShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts equals to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.equals=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.equals=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts not equals to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.notEquals=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts not equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.notEquals=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts in DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS or UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound(
            "numberOfPrepaymentAccounts.in=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS + "," + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.in=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is not null
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.specified=true");

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is null
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is greater than or equal to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound(
            "numberOfPrepaymentAccounts.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is greater than or equal to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound(
            "numberOfPrepaymentAccounts.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is less than or equal to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is less than or equal to SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound(
            "numberOfPrepaymentAccounts.lessThanOrEqual=" + SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is less than DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.lessThan=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is less than UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.lessThan=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfPrepaymentAccountsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is greater than DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfPrepaymentAccounts.greaterThan=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);

        // Get all the prepaymentAccountReportList where numberOfPrepaymentAccounts is greater than SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultPrepaymentAccountReportShouldBeFound("numberOfPrepaymentAccounts.greaterThan=" + SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems equals to DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.equals=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems equals to UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.equals=" + UPDATED_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems not equals to DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.notEquals=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems not equals to UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.notEquals=" + UPDATED_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems in DEFAULT_NUMBER_OF_AMORTISED_ITEMS or UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound(
            "numberOfAmortisedItems.in=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS + "," + UPDATED_NUMBER_OF_AMORTISED_ITEMS
        );

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems equals to UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.in=" + UPDATED_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is not null
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.specified=true");

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is null
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is greater than or equal to DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is greater than or equal to UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.greaterThanOrEqual=" + UPDATED_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is less than or equal to DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.lessThanOrEqual=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is less than or equal to SMALLER_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.lessThanOrEqual=" + SMALLER_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is less than DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.lessThan=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is less than UPDATED_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.lessThan=" + UPDATED_NUMBER_OF_AMORTISED_ITEMS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountReportsByNumberOfAmortisedItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is greater than DEFAULT_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldNotBeFound("numberOfAmortisedItems.greaterThan=" + DEFAULT_NUMBER_OF_AMORTISED_ITEMS);

        // Get all the prepaymentAccountReportList where numberOfAmortisedItems is greater than SMALLER_NUMBER_OF_AMORTISED_ITEMS
        defaultPrepaymentAccountReportShouldBeFound("numberOfAmortisedItems.greaterThan=" + SMALLER_NUMBER_OF_AMORTISED_ITEMS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentAccountReportShouldBeFound(String filter) throws Exception {
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)))
            .andExpect(jsonPath("$.[*].numberOfAmortisedItems").value(hasItem(DEFAULT_NUMBER_OF_AMORTISED_ITEMS)));

        // Check, that the count call also returns 1
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentAccountReportShouldNotBeFound(String filter) throws Exception {
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentAccountReport() throws Exception {
        // Get the prepaymentAccountReport
        restPrepaymentAccountReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchPrepaymentAccountReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentAccountReportRepository.saveAndFlush(prepaymentAccountReport);
        when(mockPrepaymentAccountReportSearchRepository.search("id:" + prepaymentAccountReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentAccountReport), PageRequest.of(0, 1), 1));

        // Search the prepaymentAccountReport
        restPrepaymentAccountReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentAccountReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccountReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)))
            .andExpect(jsonPath("$.[*].numberOfAmortisedItems").value(hasItem(DEFAULT_NUMBER_OF_AMORTISED_ITEMS)));
    }
}
