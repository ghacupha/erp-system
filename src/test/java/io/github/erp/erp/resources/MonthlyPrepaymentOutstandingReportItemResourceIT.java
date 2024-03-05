package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem;
import io.github.erp.repository.MonthlyPrepaymentOutstandingReportItemRepository;
import io.github.erp.repository.search.MonthlyPrepaymentOutstandingReportItemSearchRepository;
import io.github.erp.service.mapper.MonthlyPrepaymentOutstandingReportItemMapper;
import io.github.erp.web.rest.MonthlyPrepaymentOutstandingReportItemResource;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MonthlyPrepaymentOutstandingReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
class MonthlyPrepaymentOutstandingReportItemResourceIT {

    private static final LocalDate DEFAULT_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_MONTH_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PREPAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PREPAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMORTISED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMORTISED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMORTISED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_OUTSTANDING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_OUTSTANDING_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS = 1;
    private static final Integer UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS = 2;
    private static final Integer SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/prepayments/fixed-asset/monthly-prepayment-outstanding-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/fixed-asset/_search/monthly-prepayment-outstanding-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository;

    @Autowired
    private MonthlyPrepaymentOutstandingReportItemMapper monthlyPrepaymentOutstandingReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MonthlyPrepaymentOutstandingReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private MonthlyPrepaymentOutstandingReportItemSearchRepository mockMonthlyPrepaymentOutstandingReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonthlyPrepaymentOutstandingReportItemMockMvc;

    private MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyPrepaymentOutstandingReportItem createEntity(EntityManager em) {
        MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem = new MonthlyPrepaymentOutstandingReportItem()
            .fiscalMonthEndDate(DEFAULT_FISCAL_MONTH_END_DATE)
            .totalPrepaymentAmount(DEFAULT_TOTAL_PREPAYMENT_AMOUNT)
            .totalAmortisedAmount(DEFAULT_TOTAL_AMORTISED_AMOUNT)
            .totalOutstandingAmount(DEFAULT_TOTAL_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);
        return monthlyPrepaymentOutstandingReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyPrepaymentOutstandingReportItem createUpdatedEntity(EntityManager em) {
        MonthlyPrepaymentOutstandingReportItem monthlyPrepaymentOutstandingReportItem = new MonthlyPrepaymentOutstandingReportItem()
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE)
            .totalPrepaymentAmount(UPDATED_TOTAL_PREPAYMENT_AMOUNT)
            .totalAmortisedAmount(UPDATED_TOTAL_AMORTISED_AMOUNT)
            .totalOutstandingAmount(UPDATED_TOTAL_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
        return monthlyPrepaymentOutstandingReportItem;
    }

    @BeforeEach
    public void initTest() {
        monthlyPrepaymentOutstandingReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItems() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentOutstandingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrepaymentAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmortisedAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalOutstandingAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)));
    }

    @Test
    @Transactional
    void getMonthlyPrepaymentOutstandingReportItem() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get the monthlyPrepaymentOutstandingReportItem
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, monthlyPrepaymentOutstandingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monthlyPrepaymentOutstandingReportItem.getId().intValue()))
            .andExpect(jsonPath("$.fiscalMonthEndDate").value(DEFAULT_FISCAL_MONTH_END_DATE.toString()))
            .andExpect(jsonPath("$.totalPrepaymentAmount").value(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.totalAmortisedAmount").value(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT)))
            .andExpect(jsonPath("$.totalOutstandingAmount").value(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT)))
            .andExpect(jsonPath("$.numberOfPrepaymentAccounts").value(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS));
    }

    @Test
    @Transactional
    void getMonthlyPrepaymentOutstandingReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        Long id = monthlyPrepaymentOutstandingReportItem.getId();

        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("id.equals=" + id);
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.equals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.equals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate not equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.notEquals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate not equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.notEquals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate in DEFAULT_FISCAL_MONTH_END_DATE or UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "fiscalMonthEndDate.in=" + DEFAULT_FISCAL_MONTH_END_DATE + "," + UPDATED_FISCAL_MONTH_END_DATE
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.in=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is not null
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.specified=true");

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is null
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is greater than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "fiscalMonthEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is greater than or equal to UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "fiscalMonthEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_MONTH_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is less than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is less than or equal to SMALLER_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "fiscalMonthEndDate.lessThanOrEqual=" + SMALLER_FISCAL_MONTH_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is less than DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.lessThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is less than UPDATED_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.lessThan=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByFiscalMonthEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is greater than DEFAULT_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("fiscalMonthEndDate.greaterThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the monthlyPrepaymentOutstandingReportItemList where fiscalMonthEndDate is greater than SMALLER_FISCAL_MONTH_END_DATE
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("fiscalMonthEndDate.greaterThan=" + SMALLER_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount equals to DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalPrepaymentAmount.equals=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount equals to UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalPrepaymentAmount.equals=" + UPDATED_TOTAL_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount not equals to DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalPrepaymentAmount.notEquals=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount not equals to UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalPrepaymentAmount.notEquals=" + UPDATED_TOTAL_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount in DEFAULT_TOTAL_PREPAYMENT_AMOUNT or UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalPrepaymentAmount.in=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT + "," + UPDATED_TOTAL_PREPAYMENT_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount equals to UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalPrepaymentAmount.in=" + UPDATED_TOTAL_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is not null
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalPrepaymentAmount.specified=true");

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is null
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalPrepaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is greater than or equal to DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalPrepaymentAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is greater than or equal to UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalPrepaymentAmount.greaterThanOrEqual=" + UPDATED_TOTAL_PREPAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is less than or equal to DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalPrepaymentAmount.lessThanOrEqual=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is less than or equal to SMALLER_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalPrepaymentAmount.lessThanOrEqual=" + SMALLER_TOTAL_PREPAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is less than DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalPrepaymentAmount.lessThan=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is less than UPDATED_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalPrepaymentAmount.lessThan=" + UPDATED_TOTAL_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalPrepaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is greater than DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalPrepaymentAmount.greaterThan=" + DEFAULT_TOTAL_PREPAYMENT_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalPrepaymentAmount is greater than SMALLER_TOTAL_PREPAYMENT_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalPrepaymentAmount.greaterThan=" + SMALLER_TOTAL_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount equals to DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalAmortisedAmount.equals=" + DEFAULT_TOTAL_AMORTISED_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount equals to UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.equals=" + UPDATED_TOTAL_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount not equals to DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.notEquals=" + DEFAULT_TOTAL_AMORTISED_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount not equals to UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalAmortisedAmount.notEquals=" + UPDATED_TOTAL_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount in DEFAULT_TOTAL_AMORTISED_AMOUNT or UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalAmortisedAmount.in=" + DEFAULT_TOTAL_AMORTISED_AMOUNT + "," + UPDATED_TOTAL_AMORTISED_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount equals to UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.in=" + UPDATED_TOTAL_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is not null
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalAmortisedAmount.specified=true");

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is null
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is greater than or equal to DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalAmortisedAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMORTISED_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is greater than or equal to UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalAmortisedAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMORTISED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is less than or equal to DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalAmortisedAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMORTISED_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is less than or equal to SMALLER_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalAmortisedAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMORTISED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is less than DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.lessThan=" + DEFAULT_TOTAL_AMORTISED_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is less than UPDATED_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalAmortisedAmount.lessThan=" + UPDATED_TOTAL_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalAmortisedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is greater than DEFAULT_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalAmortisedAmount.greaterThan=" + DEFAULT_TOTAL_AMORTISED_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalAmortisedAmount is greater than SMALLER_TOTAL_AMORTISED_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalAmortisedAmount.greaterThan=" + SMALLER_TOTAL_AMORTISED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount equals to DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalOutstandingAmount.equals=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount equals to UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalOutstandingAmount.equals=" + UPDATED_TOTAL_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount not equals to DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalOutstandingAmount.notEquals=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount not equals to UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalOutstandingAmount.notEquals=" + UPDATED_TOTAL_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount in DEFAULT_TOTAL_OUTSTANDING_AMOUNT or UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalOutstandingAmount.in=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT + "," + UPDATED_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount equals to UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalOutstandingAmount.in=" + UPDATED_TOTAL_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is not null
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalOutstandingAmount.specified=true");

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is null
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("totalOutstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is greater than or equal to DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalOutstandingAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is greater than or equal to UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalOutstandingAmount.greaterThanOrEqual=" + UPDATED_TOTAL_OUTSTANDING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is less than or equal to DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalOutstandingAmount.lessThanOrEqual=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is less than or equal to SMALLER_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalOutstandingAmount.lessThanOrEqual=" + SMALLER_TOTAL_OUTSTANDING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is less than DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalOutstandingAmount.lessThan=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is less than UPDATED_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("totalOutstandingAmount.lessThan=" + UPDATED_TOTAL_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByTotalOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is greater than DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "totalOutstandingAmount.greaterThan=" + DEFAULT_TOTAL_OUTSTANDING_AMOUNT
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where totalOutstandingAmount is greater than SMALLER_TOTAL_OUTSTANDING_AMOUNT
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "totalOutstandingAmount.greaterThan=" + SMALLER_TOTAL_OUTSTANDING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts equals to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.equals=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.equals=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts not equals to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.notEquals=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts not equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.notEquals=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsInShouldWork() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts in DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS or UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.in=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS + "," + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts equals to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.in=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsNullOrNotNull() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is not null
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound("numberOfPrepaymentAccounts.specified=true");

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is null
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound("numberOfPrepaymentAccounts.specified=false");
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is greater than or equal to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is greater than or equal to UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is less than or equal to DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is less than or equal to SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.lessThanOrEqual=" + SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsLessThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is less than DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.lessThan=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is less than UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.lessThan=" + UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    @Test
    @Transactional
    void getAllMonthlyPrepaymentOutstandingReportItemsByNumberOfPrepaymentAccountsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is greater than DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(
            "numberOfPrepaymentAccounts.greaterThan=" + DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );

        // Get all the monthlyPrepaymentOutstandingReportItemList where numberOfPrepaymentAccounts is greater than SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(
            "numberOfPrepaymentAccounts.greaterThan=" + SMALLER_NUMBER_OF_PREPAYMENT_ACCOUNTS
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonthlyPrepaymentOutstandingReportItemShouldBeFound(String filter) throws Exception {
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentOutstandingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrepaymentAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmortisedAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalOutstandingAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)));

        // Check, that the count call also returns 1
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonthlyPrepaymentOutstandingReportItemShouldNotBeFound(String filter) throws Exception {
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonthlyPrepaymentOutstandingReportItem() throws Exception {
        // Get the monthlyPrepaymentOutstandingReportItem
        restMonthlyPrepaymentOutstandingReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchMonthlyPrepaymentOutstandingReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        monthlyPrepaymentOutstandingReportItemRepository.saveAndFlush(monthlyPrepaymentOutstandingReportItem);
        when(
            mockMonthlyPrepaymentOutstandingReportItemSearchRepository.search(
                "id:" + monthlyPrepaymentOutstandingReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(monthlyPrepaymentOutstandingReportItem), PageRequest.of(0, 1), 1));

        // Search the monthlyPrepaymentOutstandingReportItem
        restMonthlyPrepaymentOutstandingReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + monthlyPrepaymentOutstandingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyPrepaymentOutstandingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrepaymentAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmortisedAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalOutstandingAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)));
    }
}
