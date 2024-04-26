package io.github.erp.web.rest;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.NbvCompilationReportItem;
import io.github.erp.repository.NbvCompilationReportItemRepository;
import io.github.erp.repository.search.NbvCompilationReportItemSearchRepository;
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
 * Integration tests for the {@link NbvCompilationReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NbvCompilationReportItemResourceIT {

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

    private static final String ENTITY_API_URL = "/api/nbv-compilation-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nbv-compilation-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NbvCompilationReportItemRepository nbvCompilationReportItemRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NbvCompilationReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private NbvCompilationReportItemSearchRepository mockNbvCompilationReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNbvCompilationReportItemMockMvc;

    private NbvCompilationReportItem nbvCompilationReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationReportItem createEntity(EntityManager em) {
        NbvCompilationReportItem nbvCompilationReportItem = new NbvCompilationReportItem()
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
        return nbvCompilationReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NbvCompilationReportItem createUpdatedEntity(EntityManager em) {
        NbvCompilationReportItem nbvCompilationReportItem = new NbvCompilationReportItem()
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
        return nbvCompilationReportItem;
    }

    @BeforeEach
    public void initTest() {
        nbvCompilationReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllNbvCompilationReportItems() throws Exception {
        // Initialize the database
        nbvCompilationReportItemRepository.saveAndFlush(nbvCompilationReportItem);

        // Get all the nbvCompilationReportItemList
        restNbvCompilationReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationReportItem.getId().intValue())))
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
    void getNbvCompilationReportItem() throws Exception {
        // Initialize the database
        nbvCompilationReportItemRepository.saveAndFlush(nbvCompilationReportItem);

        // Get the nbvCompilationReportItem
        restNbvCompilationReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, nbvCompilationReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nbvCompilationReportItem.getId().intValue()))
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
    void getNonExistingNbvCompilationReportItem() throws Exception {
        // Get the nbvCompilationReportItem
        restNbvCompilationReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchNbvCompilationReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nbvCompilationReportItemRepository.saveAndFlush(nbvCompilationReportItem);
        when(mockNbvCompilationReportItemSearchRepository.search("id:" + nbvCompilationReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nbvCompilationReportItem), PageRequest.of(0, 1), 1));

        // Search the nbvCompilationReportItem
        restNbvCompilationReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nbvCompilationReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nbvCompilationReportItem.getId().intValue())))
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
