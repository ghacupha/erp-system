package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.WorkInProgressOverview;
import io.github.erp.repository.WorkInProgressOverviewRepository;
import io.github.erp.repository.search.WorkInProgressOverviewSearchRepository;
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
 * Integration tests for the {@link WorkInProgressOverviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkInProgressOverviewResourceIT {

    private static final Long DEFAULT_NUMBER_OF_ITEMS = 1L;
    private static final Long UPDATED_NUMBER_OF_ITEMS = 2L;

    private static final BigDecimal DEFAULT_INSTALMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSTALMENT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TRANSFER_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-in-progress-overviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-overviews";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressOverviewRepository workInProgressOverviewRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressOverviewSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressOverviewSearchRepository mockWorkInProgressOverviewSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressOverviewMockMvc;

    private WorkInProgressOverview workInProgressOverview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOverview createEntity(EntityManager em) {
        WorkInProgressOverview workInProgressOverview = new WorkInProgressOverview()
            .numberOfItems(DEFAULT_NUMBER_OF_ITEMS)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .totalTransferAmount(DEFAULT_TOTAL_TRANSFER_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT);
        return workInProgressOverview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressOverview createUpdatedEntity(EntityManager em) {
        WorkInProgressOverview workInProgressOverview = new WorkInProgressOverview()
            .numberOfItems(UPDATED_NUMBER_OF_ITEMS)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .totalTransferAmount(UPDATED_TOTAL_TRANSFER_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT);
        return workInProgressOverview;
    }

    @BeforeEach
    public void initTest() {
        workInProgressOverview = createEntity(em);
    }

    @Test
    @Transactional
    void getAllWorkInProgressOverviews() throws Exception {
        // Initialize the database
        workInProgressOverviewRepository.saveAndFlush(workInProgressOverview);

        // Get all the workInProgressOverviewList
        restWorkInProgressOverviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOverview.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberOfItems").value(hasItem(DEFAULT_NUMBER_OF_ITEMS.intValue())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTransferAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }

    @Test
    @Transactional
    void getWorkInProgressOverview() throws Exception {
        // Initialize the database
        workInProgressOverviewRepository.saveAndFlush(workInProgressOverview);

        // Get the workInProgressOverview
        restWorkInProgressOverviewMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressOverview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressOverview.getId().intValue()))
            .andExpect(jsonPath("$.numberOfItems").value(DEFAULT_NUMBER_OF_ITEMS.intValue()))
            .andExpect(jsonPath("$.instalmentAmount").value(sameNumber(DEFAULT_INSTALMENT_AMOUNT)))
            .andExpect(jsonPath("$.totalTransferAmount").value(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressOverview() throws Exception {
        // Get the workInProgressOverview
        restWorkInProgressOverviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchWorkInProgressOverview() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressOverviewRepository.saveAndFlush(workInProgressOverview);
        when(mockWorkInProgressOverviewSearchRepository.search("id:" + workInProgressOverview.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressOverview), PageRequest.of(0, 1), 1));

        // Search the workInProgressOverview
        restWorkInProgressOverviewMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressOverview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressOverview.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberOfItems").value(hasItem(DEFAULT_NUMBER_OF_ITEMS.intValue())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalTransferAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TRANSFER_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }
}
