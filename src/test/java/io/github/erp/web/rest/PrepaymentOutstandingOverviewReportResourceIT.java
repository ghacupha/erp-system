package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.PrepaymentOutstandingOverviewReport;
import io.github.erp.repository.PrepaymentOutstandingOverviewReportRepository;
import io.github.erp.repository.search.PrepaymentOutstandingOverviewReportSearchRepository;
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
 * Integration tests for the {@link PrepaymentOutstandingOverviewReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentOutstandingOverviewReportResourceIT {

    private static final BigDecimal DEFAULT_TOTAL_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PREPAYMENT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AMORTISED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMORTISED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_OUTSTANDING_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/prepayment-outstanding-overview-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-outstanding-overview-reports";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentOutstandingOverviewReportRepository prepaymentOutstandingOverviewReportRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentOutstandingOverviewReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentOutstandingOverviewReportSearchRepository mockPrepaymentOutstandingOverviewReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentOutstandingOverviewReportMockMvc;

    private PrepaymentOutstandingOverviewReport prepaymentOutstandingOverviewReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentOutstandingOverviewReport createEntity(EntityManager em) {
        PrepaymentOutstandingOverviewReport prepaymentOutstandingOverviewReport = new PrepaymentOutstandingOverviewReport()
            .totalPrepaymentAmount(DEFAULT_TOTAL_PREPAYMENT_AMOUNT)
            .totalAmortisedAmount(DEFAULT_TOTAL_AMORTISED_AMOUNT)
            .totalOutstandingAmount(DEFAULT_TOTAL_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS);
        return prepaymentOutstandingOverviewReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentOutstandingOverviewReport createUpdatedEntity(EntityManager em) {
        PrepaymentOutstandingOverviewReport prepaymentOutstandingOverviewReport = new PrepaymentOutstandingOverviewReport()
            .totalPrepaymentAmount(UPDATED_TOTAL_PREPAYMENT_AMOUNT)
            .totalAmortisedAmount(UPDATED_TOTAL_AMORTISED_AMOUNT)
            .totalOutstandingAmount(UPDATED_TOTAL_OUTSTANDING_AMOUNT)
            .numberOfPrepaymentAccounts(UPDATED_NUMBER_OF_PREPAYMENT_ACCOUNTS);
        return prepaymentOutstandingOverviewReport;
    }

    @BeforeEach
    public void initTest() {
        prepaymentOutstandingOverviewReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllPrepaymentOutstandingOverviewReports() throws Exception {
        // Initialize the database
        prepaymentOutstandingOverviewReportRepository.saveAndFlush(prepaymentOutstandingOverviewReport);

        // Get all the prepaymentOutstandingOverviewReportList
        restPrepaymentOutstandingOverviewReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentOutstandingOverviewReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrepaymentAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmortisedAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalOutstandingAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(sameNumber(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS))));
    }

    @Test
    @Transactional
    void getPrepaymentOutstandingOverviewReport() throws Exception {
        // Initialize the database
        prepaymentOutstandingOverviewReportRepository.saveAndFlush(prepaymentOutstandingOverviewReport);

        // Get the prepaymentOutstandingOverviewReport
        restPrepaymentOutstandingOverviewReportMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentOutstandingOverviewReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentOutstandingOverviewReport.getId().intValue()))
            .andExpect(jsonPath("$.totalPrepaymentAmount").value(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.totalAmortisedAmount").value(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT)))
            .andExpect(jsonPath("$.totalOutstandingAmount").value(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT)))
            .andExpect(jsonPath("$.numberOfPrepaymentAccounts").value(sameNumber(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS)));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentOutstandingOverviewReport() throws Exception {
        // Get the prepaymentOutstandingOverviewReport
        restPrepaymentOutstandingOverviewReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchPrepaymentOutstandingOverviewReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentOutstandingOverviewReportRepository.saveAndFlush(prepaymentOutstandingOverviewReport);
        when(
            mockPrepaymentOutstandingOverviewReportSearchRepository.search(
                "id:" + prepaymentOutstandingOverviewReport.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentOutstandingOverviewReport), PageRequest.of(0, 1), 1));

        // Search the prepaymentOutstandingOverviewReport
        restPrepaymentOutstandingOverviewReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentOutstandingOverviewReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentOutstandingOverviewReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrepaymentAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmortisedAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMORTISED_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalOutstandingAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPrepaymentAccounts").value(hasItem(sameNumber(DEFAULT_NUMBER_OF_PREPAYMENT_ACCOUNTS))));
    }
}
