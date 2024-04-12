package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.PrepaymentReport;
import io.github.erp.repository.PrepaymentReportRepository;
import io.github.erp.repository.search.PrepaymentReportSearchRepository;
import io.github.erp.service.criteria.PrepaymentReportCriteria;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.PrepaymentReportMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PrepaymentReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentReportResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREPAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREPAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMORTISED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMORTISED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMORTISED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/prepayment-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentReportRepository prepaymentReportRepository;

    @Autowired
    private PrepaymentReportMapper prepaymentReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentReportSearchRepository mockPrepaymentReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentReportMockMvc;

    private PrepaymentReport prepaymentReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentReport createEntity(EntityManager em) {
        PrepaymentReport prepaymentReport = new PrepaymentReport()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .dealerName(DEFAULT_DEALER_NAME)
            .paymentNumber(DEFAULT_PAYMENT_NUMBER)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .currencyCode(DEFAULT_CURRENCY_CODE)
            .prepaymentAmount(DEFAULT_PREPAYMENT_AMOUNT)
            .amortisedAmount(DEFAULT_AMORTISED_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT);
        return prepaymentReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentReport createUpdatedEntity(EntityManager em) {
        PrepaymentReport prepaymentReport = new PrepaymentReport()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .dealerName(UPDATED_DEALER_NAME)
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .amortisedAmount(UPDATED_AMORTISED_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT);
        return prepaymentReport;
    }

    @BeforeEach
    public void initTest() {
        prepaymentReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllPrepaymentReports() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }

    @Test
    @Transactional
    void getPrepaymentReport() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get the prepaymentReport
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentReport.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.paymentNumber").value(DEFAULT_PAYMENT_NUMBER))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
            .andExpect(jsonPath("$.prepaymentAmount").value(sameNumber(DEFAULT_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.amortisedAmount").value(sameNumber(DEFAULT_AMORTISED_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)));
    }

    @Test
    @Transactional
    void getPrepaymentReportsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        Long id = prepaymentReport.getId();

        defaultPrepaymentReportShouldBeFound("id.equals=" + id);
        defaultPrepaymentReportShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentReportShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentReportList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentReportList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the prepaymentReportList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber is not null
        defaultPrepaymentReportShouldBeFound("catalogueNumber.specified=true");

        // Get all the prepaymentReportList where catalogueNumber is null
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentReportList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentReportList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentReportShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars equals to DEFAULT_PARTICULARS
        defaultPrepaymentReportShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentReportList where particulars equals to UPDATED_PARTICULARS
        defaultPrepaymentReportShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars not equals to DEFAULT_PARTICULARS
        defaultPrepaymentReportShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentReportList where particulars not equals to UPDATED_PARTICULARS
        defaultPrepaymentReportShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultPrepaymentReportShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the prepaymentReportList where particulars equals to UPDATED_PARTICULARS
        defaultPrepaymentReportShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars is not null
        defaultPrepaymentReportShouldBeFound("particulars.specified=true");

        // Get all the prepaymentReportList where particulars is null
        defaultPrepaymentReportShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars contains DEFAULT_PARTICULARS
        defaultPrepaymentReportShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentReportList where particulars contains UPDATED_PARTICULARS
        defaultPrepaymentReportShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where particulars does not contain DEFAULT_PARTICULARS
        defaultPrepaymentReportShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentReportList where particulars does not contain UPDATED_PARTICULARS
        defaultPrepaymentReportShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName equals to DEFAULT_DEALER_NAME
        defaultPrepaymentReportShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the prepaymentReportList where dealerName equals to UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultPrepaymentReportShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the prepaymentReportList where dealerName not equals to UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the prepaymentReportList where dealerName equals to UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName is not null
        defaultPrepaymentReportShouldBeFound("dealerName.specified=true");

        // Get all the prepaymentReportList where dealerName is null
        defaultPrepaymentReportShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName contains DEFAULT_DEALER_NAME
        defaultPrepaymentReportShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the prepaymentReportList where dealerName contains UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultPrepaymentReportShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the prepaymentReportList where dealerName does not contain UPDATED_DEALER_NAME
        defaultPrepaymentReportShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber equals to DEFAULT_PAYMENT_NUMBER
        defaultPrepaymentReportShouldBeFound("paymentNumber.equals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the prepaymentReportList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.equals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber not equals to DEFAULT_PAYMENT_NUMBER
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.notEquals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the prepaymentReportList where paymentNumber not equals to UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldBeFound("paymentNumber.notEquals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber in DEFAULT_PAYMENT_NUMBER or UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldBeFound("paymentNumber.in=" + DEFAULT_PAYMENT_NUMBER + "," + UPDATED_PAYMENT_NUMBER);

        // Get all the prepaymentReportList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.in=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber is not null
        defaultPrepaymentReportShouldBeFound("paymentNumber.specified=true");

        // Get all the prepaymentReportList where paymentNumber is null
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber contains DEFAULT_PAYMENT_NUMBER
        defaultPrepaymentReportShouldBeFound("paymentNumber.contains=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the prepaymentReportList where paymentNumber contains UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.contains=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentNumber does not contain DEFAULT_PAYMENT_NUMBER
        defaultPrepaymentReportShouldNotBeFound("paymentNumber.doesNotContain=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the prepaymentReportList where paymentNumber does not contain UPDATED_PAYMENT_NUMBER
        defaultPrepaymentReportShouldBeFound("paymentNumber.doesNotContain=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate is not null
        defaultPrepaymentReportShouldBeFound("paymentDate.specified=true");

        // Get all the prepaymentReportList where paymentDate is null
        defaultPrepaymentReportShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultPrepaymentReportShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the prepaymentReportList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultPrepaymentReportShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode equals to DEFAULT_CURRENCY_CODE
        defaultPrepaymentReportShouldBeFound("currencyCode.equals=" + DEFAULT_CURRENCY_CODE);

        // Get all the prepaymentReportList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldNotBeFound("currencyCode.equals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode not equals to DEFAULT_CURRENCY_CODE
        defaultPrepaymentReportShouldNotBeFound("currencyCode.notEquals=" + DEFAULT_CURRENCY_CODE);

        // Get all the prepaymentReportList where currencyCode not equals to UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldBeFound("currencyCode.notEquals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode in DEFAULT_CURRENCY_CODE or UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldBeFound("currencyCode.in=" + DEFAULT_CURRENCY_CODE + "," + UPDATED_CURRENCY_CODE);

        // Get all the prepaymentReportList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldNotBeFound("currencyCode.in=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode is not null
        defaultPrepaymentReportShouldBeFound("currencyCode.specified=true");

        // Get all the prepaymentReportList where currencyCode is null
        defaultPrepaymentReportShouldNotBeFound("currencyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode contains DEFAULT_CURRENCY_CODE
        defaultPrepaymentReportShouldBeFound("currencyCode.contains=" + DEFAULT_CURRENCY_CODE);

        // Get all the prepaymentReportList where currencyCode contains UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldNotBeFound("currencyCode.contains=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByCurrencyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where currencyCode does not contain DEFAULT_CURRENCY_CODE
        defaultPrepaymentReportShouldNotBeFound("currencyCode.doesNotContain=" + DEFAULT_CURRENCY_CODE);

        // Get all the prepaymentReportList where currencyCode does not contain UPDATED_CURRENCY_CODE
        defaultPrepaymentReportShouldBeFound("currencyCode.doesNotContain=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.equals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.equals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount not equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.notEquals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount not equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.notEquals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount in DEFAULT_PREPAYMENT_AMOUNT or UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.in=" + DEFAULT_PREPAYMENT_AMOUNT + "," + UPDATED_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.in=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount is not null
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.specified=true");

        // Get all the prepaymentReportList where prepaymentAmount is null
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount is greater than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.greaterThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount is greater than or equal to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.greaterThanOrEqual=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount is less than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.lessThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount is less than or equal to SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.lessThanOrEqual=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount is less than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.lessThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount is less than UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.lessThan=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByPrepaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where prepaymentAmount is greater than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("prepaymentAmount.greaterThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentReportList where prepaymentAmount is greater than SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentReportShouldBeFound("prepaymentAmount.greaterThan=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount equals to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.equals=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.equals=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount not equals to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.notEquals=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount not equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.notEquals=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount in DEFAULT_AMORTISED_AMOUNT or UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.in=" + DEFAULT_AMORTISED_AMOUNT + "," + UPDATED_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount equals to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.in=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount is not null
        defaultPrepaymentReportShouldBeFound("amortisedAmount.specified=true");

        // Get all the prepaymentReportList where amortisedAmount is null
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount is greater than or equal to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.greaterThanOrEqual=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount is greater than or equal to UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.greaterThanOrEqual=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount is less than or equal to DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.lessThanOrEqual=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount is less than or equal to SMALLER_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.lessThanOrEqual=" + SMALLER_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount is less than DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.lessThan=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount is less than UPDATED_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.lessThan=" + UPDATED_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByAmortisedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where amortisedAmount is greater than DEFAULT_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("amortisedAmount.greaterThan=" + DEFAULT_AMORTISED_AMOUNT);

        // Get all the prepaymentReportList where amortisedAmount is greater than SMALLER_AMORTISED_AMOUNT
        defaultPrepaymentReportShouldBeFound("amortisedAmount.greaterThan=" + SMALLER_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount is not null
        defaultPrepaymentReportShouldBeFound("outstandingAmount.specified=true");

        // Get all the prepaymentReportList where outstandingAmount is null
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentReportsByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);

        // Get all the prepaymentReportList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the prepaymentReportList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultPrepaymentReportShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentReportShouldBeFound(String filter) throws Exception {
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));

        // Check, that the count call also returns 1
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentReportShouldNotBeFound(String filter) throws Exception {
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentReport() throws Exception {
        // Get the prepaymentReport
        restPrepaymentReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchPrepaymentReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentReportRepository.saveAndFlush(prepaymentReport);
        when(mockPrepaymentReportSearchRepository.search("id:" + prepaymentReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentReport), PageRequest.of(0, 1), 1));

        // Search the prepaymentReport
        restPrepaymentReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].amortisedAmount").value(hasItem(sameNumber(DEFAULT_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }
}
