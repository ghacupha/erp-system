package io.github.erp.erp.resources;

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

}
