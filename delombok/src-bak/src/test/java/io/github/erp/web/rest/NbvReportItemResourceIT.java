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
import io.github.erp.domain.NbvReportItem;
import io.github.erp.repository.NbvReportItemRepository;
import io.github.erp.repository.search.NbvReportItemSearchRepository;
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
 * Integration tests for the {@link NbvReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NbvReportItemResourceIT {

    private static final String DEFAULT_ASSET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_NBV_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_NBV_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PREVIOUS_NBV_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREVIOUS_NBV_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BATCH_SEQUENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_SEQUENCE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROCESSED_ITEMS = 1;
    private static final Integer UPDATED_PROCESSED_ITEMS = 2;

    private static final Integer DEFAULT_TOTAL_ITEMS_PROCESSED = 1;
    private static final Integer UPDATED_TOTAL_ITEMS_PROCESSED = 2;

    private static final Integer DEFAULT_ELAPSED_MONTHS = 1;
    private static final Integer UPDATED_ELAPSED_MONTHS = 2;

    private static final Integer DEFAULT_PRIOR_MONTHS = 1;
    private static final Integer UPDATED_PRIOR_MONTHS = 2;

    private static final BigDecimal DEFAULT_USEFUL_LIFE_YEARS = new BigDecimal(1);
    private static final BigDecimal UPDATED_USEFUL_LIFE_YEARS = new BigDecimal(2);

    private static final String DEFAULT_CAPITALIZATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CAPITALIZATION_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_PERIOD_START_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_PERIOD_START_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_PERIOD_END_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_PERIOD_END_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_PERIOD_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_PERIOD_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_JOB_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nbv-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nbv-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NbvReportItemRepository nbvReportItemRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NbvReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private NbvReportItemSearchRepository mockNbvReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNbvReportItemMockMvc;

    private NbvReportItem nbvReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvReportItem createEntity(EntityManager em) {
        NbvReportItem nbvReportItem = new NbvReportItem()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .nbvAmount(DEFAULT_NBV_AMOUNT)
            .previousNBVAmount(DEFAULT_PREVIOUS_NBV_AMOUNT)
            .assetDescription(DEFAULT_ASSET_DESCRIPTION)
            .batchSequenceNumber(DEFAULT_BATCH_SEQUENCE_NUMBER)
            .processedItems(DEFAULT_PROCESSED_ITEMS)
            .totalItemsProcessed(DEFAULT_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(DEFAULT_ELAPSED_MONTHS)
            .priorMonths(DEFAULT_PRIOR_MONTHS)
            .usefulLifeYears(DEFAULT_USEFUL_LIFE_YEARS)
            .capitalizationDate(DEFAULT_CAPITALIZATION_DATE)
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .depreciationPeriodStartDate(DEFAULT_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(DEFAULT_DEPRECIATION_PERIOD_END_DATE)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .depreciationPeriodTitle(DEFAULT_DEPRECIATION_PERIOD_TITLE)
            .compilationJobTitle(DEFAULT_COMPILATION_JOB_TITLE);
        return nbvReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvReportItem createUpdatedEntity(EntityManager em) {
        NbvReportItem nbvReportItem = new NbvReportItem()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .nbvAmount(UPDATED_NBV_AMOUNT)
            .previousNBVAmount(UPDATED_PREVIOUS_NBV_AMOUNT)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .batchSequenceNumber(UPDATED_BATCH_SEQUENCE_NUMBER)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .totalItemsProcessed(UPDATED_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE)
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .depreciationPeriodStartDate(UPDATED_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(UPDATED_DEPRECIATION_PERIOD_END_DATE)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .depreciationPeriodTitle(UPDATED_DEPRECIATION_PERIOD_TITLE)
            .compilationJobTitle(UPDATED_COMPILATION_JOB_TITLE);
        return nbvReportItem;
    }

    @BeforeEach
    public void initTest() {
        nbvReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllNbvReportItems() throws Exception {
        // Initialize the database
        nbvReportItemRepository.saveAndFlush(nbvReportItem);

        // Get all the nbvReportItemList
        restNbvReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].nbvAmount").value(hasItem(sameNumber(DEFAULT_NBV_AMOUNT))))
            .andExpect(jsonPath("$.[*].previousNBVAmount").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].batchSequenceNumber").value(hasItem(DEFAULT_BATCH_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].totalItemsProcessed").value(hasItem(DEFAULT_TOTAL_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS)))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS)))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE)))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE)))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].depreciationPeriodTitle").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_TITLE)))
            .andExpect(jsonPath("$.[*].compilationJobTitle").value(hasItem(DEFAULT_COMPILATION_JOB_TITLE)));
    }

    @Test
    @Transactional
    void getNbvReportItem() throws Exception {
        // Initialize the database
        nbvReportItemRepository.saveAndFlush(nbvReportItem);

        // Get the nbvReportItem
        restNbvReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, nbvReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nbvReportItem.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER))
            .andExpect(jsonPath("$.nbvAmount").value(sameNumber(DEFAULT_NBV_AMOUNT)))
            .andExpect(jsonPath("$.previousNBVAmount").value(sameNumber(DEFAULT_PREVIOUS_NBV_AMOUNT)))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.batchSequenceNumber").value(DEFAULT_BATCH_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.processedItems").value(DEFAULT_PROCESSED_ITEMS))
            .andExpect(jsonPath("$.totalItemsProcessed").value(DEFAULT_TOTAL_ITEMS_PROCESSED))
            .andExpect(jsonPath("$.elapsedMonths").value(DEFAULT_ELAPSED_MONTHS))
            .andExpect(jsonPath("$.priorMonths").value(DEFAULT_PRIOR_MONTHS))
            .andExpect(jsonPath("$.usefulLifeYears").value(sameNumber(DEFAULT_USEFUL_LIFE_YEARS)))
            .andExpect(jsonPath("$.capitalizationDate").value(DEFAULT_CAPITALIZATION_DATE))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE))
            .andExpect(jsonPath("$.depreciationPeriodStartDate").value(DEFAULT_DEPRECIATION_PERIOD_START_DATE))
            .andExpect(jsonPath("$.depreciationPeriodEndDate").value(DEFAULT_DEPRECIATION_PERIOD_END_DATE))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.depreciationPeriodTitle").value(DEFAULT_DEPRECIATION_PERIOD_TITLE))
            .andExpect(jsonPath("$.compilationJobTitle").value(DEFAULT_COMPILATION_JOB_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingNbvReportItem() throws Exception {
        // Get the nbvReportItem
        restNbvReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchNbvReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nbvReportItemRepository.saveAndFlush(nbvReportItem);
        when(mockNbvReportItemSearchRepository.search("id:" + nbvReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nbvReportItem), PageRequest.of(0, 1), 1));

        // Search the nbvReportItem
        restNbvReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nbvReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].nbvAmount").value(hasItem(sameNumber(DEFAULT_NBV_AMOUNT))))
            .andExpect(jsonPath("$.[*].previousNBVAmount").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].batchSequenceNumber").value(hasItem(DEFAULT_BATCH_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].totalItemsProcessed").value(hasItem(DEFAULT_TOTAL_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS)))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS)))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE)))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE)))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].depreciationPeriodTitle").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_TITLE)))
            .andExpect(jsonPath("$.[*].compilationJobTitle").value(hasItem(DEFAULT_COMPILATION_JOB_TITLE)));
    }
}
