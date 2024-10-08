package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.LeaseLiabilityScheduleReportItem;
import io.github.erp.repository.LeaseLiabilityScheduleReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepository;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportItemMapper;
import io.github.erp.web.rest.LeaseLiabilityScheduleReportItemResource;
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

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LeaseLiabilityScheduleReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseLiabilityScheduleReportItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final BigDecimal DEFAULT_OPENING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPENING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OPENING_BALANCE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CASH_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRINCIPAL_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRINCIPAL_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRINCIPAL_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_BALANCE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYABLE_OPENING = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYABLE_OPENING = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYABLE_OPENING = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_ACCRUED = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_ACCRUED = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_ACCRUED = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYABLE_CLOSING = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYABLE_CLOSING = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYABLE_CLOSING = new BigDecimal(1 - 1);

    private static final Long DEFAULT_AMORTIZATION_SCHEDULE_ID = 1L;
    private static final Long UPDATED_AMORTIZATION_SCHEDULE_ID = 2L;
    private static final Long SMALLER_AMORTIZATION_SCHEDULE_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/leases/lease-liability-schedule-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-liability-schedule-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository;

    @Autowired
    private LeaseLiabilityScheduleReportItemMapper leaseLiabilityScheduleReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityScheduleReportItemSearchRepository mockLeaseLiabilityScheduleReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityScheduleReportItemMockMvc;

    private LeaseLiabilityScheduleReportItem leaseLiabilityScheduleReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleReportItem createEntity(EntityManager em) {
        LeaseLiabilityScheduleReportItem leaseLiabilityScheduleReportItem = new LeaseLiabilityScheduleReportItem()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .openingBalance(DEFAULT_OPENING_BALANCE)
            .cashPayment(DEFAULT_CASH_PAYMENT)
            .principalPayment(DEFAULT_PRINCIPAL_PAYMENT)
            .interestPayment(DEFAULT_INTEREST_PAYMENT)
            .outstandingBalance(DEFAULT_OUTSTANDING_BALANCE)
            .interestPayableOpening(DEFAULT_INTEREST_PAYABLE_OPENING)
            .interestAccrued(DEFAULT_INTEREST_ACCRUED)
            .interestPayableClosing(DEFAULT_INTEREST_PAYABLE_CLOSING)
            .amortizationScheduleId(DEFAULT_AMORTIZATION_SCHEDULE_ID);
        return leaseLiabilityScheduleReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleReportItem createUpdatedEntity(EntityManager em) {
        LeaseLiabilityScheduleReportItem leaseLiabilityScheduleReportItem = new LeaseLiabilityScheduleReportItem()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .cashPayment(UPDATED_CASH_PAYMENT)
            .principalPayment(UPDATED_PRINCIPAL_PAYMENT)
            .interestPayment(UPDATED_INTEREST_PAYMENT)
            .outstandingBalance(UPDATED_OUTSTANDING_BALANCE)
            .interestPayableOpening(UPDATED_INTEREST_PAYABLE_OPENING)
            .interestAccrued(UPDATED_INTEREST_ACCRUED)
            .interestPayableClosing(UPDATED_INTEREST_PAYABLE_CLOSING)
            .amortizationScheduleId(UPDATED_AMORTIZATION_SCHEDULE_ID);
        return leaseLiabilityScheduleReportItem;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityScheduleReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItems() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableClosing").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_CLOSING))))
            .andExpect(jsonPath("$.[*].amortizationScheduleId").value(hasItem(DEFAULT_AMORTIZATION_SCHEDULE_ID.intValue())));
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleReportItem() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get the leaseLiabilityScheduleReportItem
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityScheduleReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityScheduleReportItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.openingBalance").value(sameNumber(DEFAULT_OPENING_BALANCE)))
            .andExpect(jsonPath("$.cashPayment").value(sameNumber(DEFAULT_CASH_PAYMENT)))
            .andExpect(jsonPath("$.principalPayment").value(sameNumber(DEFAULT_PRINCIPAL_PAYMENT)))
            .andExpect(jsonPath("$.interestPayment").value(sameNumber(DEFAULT_INTEREST_PAYMENT)))
            .andExpect(jsonPath("$.outstandingBalance").value(sameNumber(DEFAULT_OUTSTANDING_BALANCE)))
            .andExpect(jsonPath("$.interestPayableOpening").value(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING)))
            .andExpect(jsonPath("$.interestAccrued").value(sameNumber(DEFAULT_INTEREST_ACCRUED)))
            .andExpect(jsonPath("$.interestPayableClosing").value(sameNumber(DEFAULT_INTEREST_PAYABLE_CLOSING)))
            .andExpect(jsonPath("$.amortizationScheduleId").value(DEFAULT_AMORTIZATION_SCHEDULE_ID.intValue()));
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        Long id = leaseLiabilityScheduleReportItem.getId();

        defaultLeaseLiabilityScheduleReportItemShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityScheduleReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityScheduleReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER
        );

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleReportItemList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance equals to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.equals=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.equals=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance not equals to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.notEquals=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance not equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.notEquals=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance in DEFAULT_OPENING_BALANCE or UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "openingBalance.in=" + DEFAULT_OPENING_BALANCE + "," + UPDATED_OPENING_BALANCE
        );

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.in=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is greater than or equal to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.greaterThanOrEqual=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is greater than or equal to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.greaterThanOrEqual=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is less than or equal to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.lessThanOrEqual=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is less than or equal to SMALLER_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.lessThanOrEqual=" + SMALLER_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is less than DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.lessThan=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is less than UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.lessThan=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOpeningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is greater than DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("openingBalance.greaterThan=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where openingBalance is greater than SMALLER_OPENING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("openingBalance.greaterThan=" + SMALLER_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment equals to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.equals=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.equals=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment not equals to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.notEquals=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment not equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.notEquals=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment in DEFAULT_CASH_PAYMENT or UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.in=" + DEFAULT_CASH_PAYMENT + "," + UPDATED_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.in=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is greater than or equal to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.greaterThanOrEqual=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is greater than or equal to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.greaterThanOrEqual=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is less than or equal to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.lessThanOrEqual=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is less than or equal to SMALLER_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.lessThanOrEqual=" + SMALLER_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is less than DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.lessThan=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is less than UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.lessThan=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByCashPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is greater than DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("cashPayment.greaterThan=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where cashPayment is greater than SMALLER_CASH_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("cashPayment.greaterThan=" + SMALLER_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment equals to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.equals=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.equals=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment not equals to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.notEquals=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment not equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.notEquals=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment in DEFAULT_PRINCIPAL_PAYMENT or UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "principalPayment.in=" + DEFAULT_PRINCIPAL_PAYMENT + "," + UPDATED_PRINCIPAL_PAYMENT
        );

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.in=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is greater than or equal to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.greaterThanOrEqual=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is greater than or equal to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.greaterThanOrEqual=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is less than or equal to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.lessThanOrEqual=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is less than or equal to SMALLER_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.lessThanOrEqual=" + SMALLER_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is less than DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.lessThan=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is less than UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.lessThan=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByPrincipalPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is greater than DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("principalPayment.greaterThan=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where principalPayment is greater than SMALLER_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("principalPayment.greaterThan=" + SMALLER_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment equals to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.equals=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.equals=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment not equals to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.notEquals=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment not equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.notEquals=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment in DEFAULT_INTEREST_PAYMENT or UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestPayment.in=" + DEFAULT_INTEREST_PAYMENT + "," + UPDATED_INTEREST_PAYMENT
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.in=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is greater than or equal to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is greater than or equal to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.greaterThanOrEqual=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is less than or equal to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.lessThanOrEqual=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is less than or equal to SMALLER_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.lessThanOrEqual=" + SMALLER_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is less than DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.lessThan=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is less than UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.lessThan=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is greater than DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayment.greaterThan=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayment is greater than SMALLER_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayment.greaterThan=" + SMALLER_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance equals to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.equals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.equals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance not equals to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.notEquals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance not equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.notEquals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance in DEFAULT_OUTSTANDING_BALANCE or UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "outstandingBalance.in=" + DEFAULT_OUTSTANDING_BALANCE + "," + UPDATED_OUTSTANDING_BALANCE
        );

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.in=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is greater than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is greater than or equal to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.greaterThanOrEqual=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is less than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.lessThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is less than or equal to SMALLER_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.lessThanOrEqual=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is less than DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.lessThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is less than UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.lessThan=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByOutstandingBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is greater than DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("outstandingBalance.greaterThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleReportItemList where outstandingBalance is greater than SMALLER_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("outstandingBalance.greaterThan=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening equals to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.equals=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.equals=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening not equals to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.notEquals=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening not equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.notEquals=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening in DEFAULT_INTEREST_PAYABLE_OPENING or UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestPayableOpening.in=" + DEFAULT_INTEREST_PAYABLE_OPENING + "," + UPDATED_INTEREST_PAYABLE_OPENING
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.in=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is greater than or equal to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestPayableOpening.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_OPENING
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is greater than or equal to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "interestPayableOpening.greaterThanOrEqual=" + UPDATED_INTEREST_PAYABLE_OPENING
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is less than or equal to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.lessThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is less than or equal to SMALLER_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "interestPayableOpening.lessThanOrEqual=" + SMALLER_INTEREST_PAYABLE_OPENING
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is less than DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.lessThan=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is less than UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.lessThan=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableOpeningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is greater than DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableOpening.greaterThan=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableOpening is greater than SMALLER_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableOpening.greaterThan=" + SMALLER_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued equals to DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.equals=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued equals to UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.equals=" + UPDATED_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued not equals to DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.notEquals=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued not equals to UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.notEquals=" + UPDATED_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued in DEFAULT_INTEREST_ACCRUED or UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestAccrued.in=" + DEFAULT_INTEREST_ACCRUED + "," + UPDATED_INTEREST_ACCRUED
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued equals to UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.in=" + UPDATED_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is greater than or equal to DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.greaterThanOrEqual=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is greater than or equal to UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.greaterThanOrEqual=" + UPDATED_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is less than or equal to DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.lessThanOrEqual=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is less than or equal to SMALLER_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.lessThanOrEqual=" + SMALLER_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is less than DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.lessThan=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is less than UPDATED_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.lessThan=" + UPDATED_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestAccruedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is greater than DEFAULT_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestAccrued.greaterThan=" + DEFAULT_INTEREST_ACCRUED);

        // Get all the leaseLiabilityScheduleReportItemList where interestAccrued is greater than SMALLER_INTEREST_ACCRUED
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestAccrued.greaterThan=" + SMALLER_INTEREST_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing equals to DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.equals=" + DEFAULT_INTEREST_PAYABLE_CLOSING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing equals to UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.equals=" + UPDATED_INTEREST_PAYABLE_CLOSING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing not equals to DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.notEquals=" + DEFAULT_INTEREST_PAYABLE_CLOSING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing not equals to UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.notEquals=" + UPDATED_INTEREST_PAYABLE_CLOSING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing in DEFAULT_INTEREST_PAYABLE_CLOSING or UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestPayableClosing.in=" + DEFAULT_INTEREST_PAYABLE_CLOSING + "," + UPDATED_INTEREST_PAYABLE_CLOSING
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing equals to UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.in=" + UPDATED_INTEREST_PAYABLE_CLOSING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is greater than or equal to DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "interestPayableClosing.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_CLOSING
        );

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is greater than or equal to UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "interestPayableClosing.greaterThanOrEqual=" + UPDATED_INTEREST_PAYABLE_CLOSING
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is less than or equal to DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.lessThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_CLOSING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is less than or equal to SMALLER_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "interestPayableClosing.lessThanOrEqual=" + SMALLER_INTEREST_PAYABLE_CLOSING
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is less than DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.lessThan=" + DEFAULT_INTEREST_PAYABLE_CLOSING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is less than UPDATED_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.lessThan=" + UPDATED_INTEREST_PAYABLE_CLOSING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByInterestPayableClosingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is greater than DEFAULT_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("interestPayableClosing.greaterThan=" + DEFAULT_INTEREST_PAYABLE_CLOSING);

        // Get all the leaseLiabilityScheduleReportItemList where interestPayableClosing is greater than SMALLER_INTEREST_PAYABLE_CLOSING
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("interestPayableClosing.greaterThan=" + SMALLER_INTEREST_PAYABLE_CLOSING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId equals to DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.equals=" + DEFAULT_AMORTIZATION_SCHEDULE_ID);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId equals to UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.equals=" + UPDATED_AMORTIZATION_SCHEDULE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId not equals to DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.notEquals=" + DEFAULT_AMORTIZATION_SCHEDULE_ID);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId not equals to UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.notEquals=" + UPDATED_AMORTIZATION_SCHEDULE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId in DEFAULT_AMORTIZATION_SCHEDULE_ID or UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "amortizationScheduleId.in=" + DEFAULT_AMORTIZATION_SCHEDULE_ID + "," + UPDATED_AMORTIZATION_SCHEDULE_ID
        );

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId equals to UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.in=" + UPDATED_AMORTIZATION_SCHEDULE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is not null
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.specified=true");

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is null
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is greater than or equal to DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound(
            "amortizationScheduleId.greaterThanOrEqual=" + DEFAULT_AMORTIZATION_SCHEDULE_ID
        );

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is greater than or equal to UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "amortizationScheduleId.greaterThanOrEqual=" + UPDATED_AMORTIZATION_SCHEDULE_ID
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is less than or equal to DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.lessThanOrEqual=" + DEFAULT_AMORTIZATION_SCHEDULE_ID);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is less than or equal to SMALLER_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(
            "amortizationScheduleId.lessThanOrEqual=" + SMALLER_AMORTIZATION_SCHEDULE_ID
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is less than DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.lessThan=" + DEFAULT_AMORTIZATION_SCHEDULE_ID);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is less than UPDATED_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.lessThan=" + UPDATED_AMORTIZATION_SCHEDULE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleReportItemsByAmortizationScheduleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is greater than DEFAULT_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldNotBeFound("amortizationScheduleId.greaterThan=" + DEFAULT_AMORTIZATION_SCHEDULE_ID);

        // Get all the leaseLiabilityScheduleReportItemList where amortizationScheduleId is greater than SMALLER_AMORTIZATION_SCHEDULE_ID
        defaultLeaseLiabilityScheduleReportItemShouldBeFound("amortizationScheduleId.greaterThan=" + SMALLER_AMORTIZATION_SCHEDULE_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityScheduleReportItemShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableClosing").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_CLOSING))))
            .andExpect(jsonPath("$.[*].amortizationScheduleId").value(hasItem(DEFAULT_AMORTIZATION_SCHEDULE_ID.intValue())));

        // Check, that the count call also returns 1
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityScheduleReportItemShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityScheduleReportItem() throws Exception {
        // Get the leaseLiabilityScheduleReportItem
        restLeaseLiabilityScheduleReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityScheduleReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityScheduleReportItemRepository.saveAndFlush(leaseLiabilityScheduleReportItem);
        when(
            mockLeaseLiabilityScheduleReportItemSearchRepository.search(
                "id:" + leaseLiabilityScheduleReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityScheduleReportItem), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityScheduleReportItem
        restLeaseLiabilityScheduleReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityScheduleReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableClosing").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_CLOSING))))
            .andExpect(jsonPath("$.[*].amortizationScheduleId").value(hasItem(DEFAULT_AMORTIZATION_SCHEDULE_ID.intValue())));
    }
}
