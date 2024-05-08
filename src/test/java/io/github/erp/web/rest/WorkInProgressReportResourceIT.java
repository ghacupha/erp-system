package io.github.erp.web.rest;

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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.WorkInProgressReport;
import io.github.erp.repository.WorkInProgressReportRepository;
import io.github.erp.repository.search.WorkInProgressReportSearchRepository;
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
 * Integration tests for the {@link WorkInProgressReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkInProgressReportResourceIT {

    private static final String DEFAULT_PROJECT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_ITEMS = 1;
    private static final Integer UPDATED_NUMBER_OF_ITEMS = 2;

    private static final BigDecimal DEFAULT_INSTALMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSTALMENT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSFER_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-in-progress-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressReportRepository workInProgressReportRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressReportSearchRepository mockWorkInProgressReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressReportMockMvc;

    private WorkInProgressReport workInProgressReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressReport createEntity(EntityManager em) {
        WorkInProgressReport workInProgressReport = new WorkInProgressReport()
            .projectTitle(DEFAULT_PROJECT_TITLE)
            .dealerName(DEFAULT_DEALER_NAME)
            .numberOfItems(DEFAULT_NUMBER_OF_ITEMS)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .transferAmount(DEFAULT_TRANSFER_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT);
        return workInProgressReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressReport createUpdatedEntity(EntityManager em) {
        WorkInProgressReport workInProgressReport = new WorkInProgressReport()
            .projectTitle(UPDATED_PROJECT_TITLE)
            .dealerName(UPDATED_DEALER_NAME)
            .numberOfItems(UPDATED_NUMBER_OF_ITEMS)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .transferAmount(UPDATED_TRANSFER_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT);
        return workInProgressReport;
    }

    @BeforeEach
    public void initTest() {
        workInProgressReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllWorkInProgressReports() throws Exception {
        // Initialize the database
        workInProgressReportRepository.saveAndFlush(workInProgressReport);

        // Get all the workInProgressReportList
        restWorkInProgressReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].numberOfItems").value(hasItem(DEFAULT_NUMBER_OF_ITEMS)))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(sameNumber(DEFAULT_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }

    @Test
    @Transactional
    void getWorkInProgressReport() throws Exception {
        // Initialize the database
        workInProgressReportRepository.saveAndFlush(workInProgressReport);

        // Get the workInProgressReport
        restWorkInProgressReportMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressReport.getId().intValue()))
            .andExpect(jsonPath("$.projectTitle").value(DEFAULT_PROJECT_TITLE))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.numberOfItems").value(DEFAULT_NUMBER_OF_ITEMS))
            .andExpect(jsonPath("$.instalmentAmount").value(sameNumber(DEFAULT_INSTALMENT_AMOUNT)))
            .andExpect(jsonPath("$.transferAmount").value(sameNumber(DEFAULT_TRANSFER_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressReport() throws Exception {
        // Get the workInProgressReport
        restWorkInProgressReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchWorkInProgressReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressReportRepository.saveAndFlush(workInProgressReport);
        when(mockWorkInProgressReportSearchRepository.search("id:" + workInProgressReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressReport), PageRequest.of(0, 1), 1));

        // Search the workInProgressReport
        restWorkInProgressReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE)))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].numberOfItems").value(hasItem(DEFAULT_NUMBER_OF_ITEMS)))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(sameNumber(DEFAULT_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }
}
