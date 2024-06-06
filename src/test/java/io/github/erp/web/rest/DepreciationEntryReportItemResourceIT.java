package io.github.erp.web.rest;

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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationEntryReportItem;
import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.repository.search.DepreciationEntryReportItemSearchRepository;
import io.github.erp.service.criteria.DepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.DepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.DepreciationEntryReportItemMapper;
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
 * Integration tests for the {@link DepreciationEntryReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationEntryReportItemResourceIT {

    private static final String DEFAULT_ASSET_REGISTRATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_REGISTRATION_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTED_AT = "AAAAAAAAAA";
    private static final String UPDATED_POSTED_AT = "BBBBBBBBBB";

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String DEFAULT_SERVICE_OUTLET = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_PERIOD = "BBBBBBBBBB";

    private static final String DEFAULT_FISCAL_MONTH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_MONTH_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ASSET_REGISTRATION_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSET_REGISTRATION_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSET_REGISTRATION_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final Long DEFAULT_ELAPSED_MONTHS = 1L;
    private static final Long UPDATED_ELAPSED_MONTHS = 2L;
    private static final Long SMALLER_ELAPSED_MONTHS = 1L - 1L;

    private static final Long DEFAULT_PRIOR_MONTHS = 1L;
    private static final Long UPDATED_PRIOR_MONTHS = 2L;
    private static final Long SMALLER_PRIOR_MONTHS = 1L - 1L;

    private static final BigDecimal DEFAULT_USEFUL_LIFE_YEARS = new BigDecimal(1);
    private static final BigDecimal UPDATED_USEFUL_LIFE_YEARS = new BigDecimal(2);
    private static final BigDecimal SMALLER_USEFUL_LIFE_YEARS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PREVIOUS_NBV = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREVIOUS_NBV = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREVIOUS_NBV = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DEPRECIATION_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_PERIOD_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DEPRECIATION_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/depreciation-entry-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-entry-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationEntryReportItemRepository depreciationEntryReportItemRepository;

    @Autowired
    private DepreciationEntryReportItemMapper depreciationEntryReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationEntryReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationEntryReportItemSearchRepository mockDepreciationEntryReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationEntryReportItemMockMvc;

    private DepreciationEntryReportItem depreciationEntryReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntryReportItem createEntity(EntityManager em) {
        DepreciationEntryReportItem depreciationEntryReportItem = new DepreciationEntryReportItem()
            .assetRegistrationDetails(DEFAULT_ASSET_REGISTRATION_DETAILS)
            .postedAt(DEFAULT_POSTED_AT)
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .serviceOutlet(DEFAULT_SERVICE_OUTLET)
            .assetCategory(DEFAULT_ASSET_CATEGORY)
            .depreciationMethod(DEFAULT_DEPRECIATION_METHOD)
            .depreciationPeriod(DEFAULT_DEPRECIATION_PERIOD)
            .fiscalMonthCode(DEFAULT_FISCAL_MONTH_CODE)
            .assetRegistrationCost(DEFAULT_ASSET_REGISTRATION_COST)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .elapsedMonths(DEFAULT_ELAPSED_MONTHS)
            .priorMonths(DEFAULT_PRIOR_MONTHS)
            .usefulLifeYears(DEFAULT_USEFUL_LIFE_YEARS)
            .previousNBV(DEFAULT_PREVIOUS_NBV)
            .netBookValue(DEFAULT_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(DEFAULT_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(DEFAULT_DEPRECIATION_PERIOD_END_DATE);
        return depreciationEntryReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntryReportItem createUpdatedEntity(EntityManager em) {
        DepreciationEntryReportItem depreciationEntryReportItem = new DepreciationEntryReportItem()
            .assetRegistrationDetails(UPDATED_ASSET_REGISTRATION_DETAILS)
            .postedAt(UPDATED_POSTED_AT)
            .assetNumber(UPDATED_ASSET_NUMBER)
            .serviceOutlet(UPDATED_SERVICE_OUTLET)
            .assetCategory(UPDATED_ASSET_CATEGORY)
            .depreciationMethod(UPDATED_DEPRECIATION_METHOD)
            .depreciationPeriod(UPDATED_DEPRECIATION_PERIOD)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE)
            .assetRegistrationCost(UPDATED_ASSET_REGISTRATION_COST)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .previousNBV(UPDATED_PREVIOUS_NBV)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(UPDATED_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(UPDATED_DEPRECIATION_PERIOD_END_DATE);
        return depreciationEntryReportItem;
    }

    @BeforeEach
    public void initTest() {
        depreciationEntryReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItems() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetRegistrationDetails").value(hasItem(DEFAULT_ASSET_REGISTRATION_DETAILS)))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(DEFAULT_POSTED_AT)))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationMethod").value(hasItem(DEFAULT_DEPRECIATION_METHOD)))
            .andExpect(jsonPath("$.[*].depreciationPeriod").value(hasItem(DEFAULT_DEPRECIATION_PERIOD)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].assetRegistrationCost").value(hasItem(sameNumber(DEFAULT_ASSET_REGISTRATION_COST))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getDepreciationEntryReportItem() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get the depreciationEntryReportItem
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationEntryReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationEntryReportItem.getId().intValue()))
            .andExpect(jsonPath("$.assetRegistrationDetails").value(DEFAULT_ASSET_REGISTRATION_DETAILS))
            .andExpect(jsonPath("$.postedAt").value(DEFAULT_POSTED_AT))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.serviceOutlet").value(DEFAULT_SERVICE_OUTLET))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.depreciationMethod").value(DEFAULT_DEPRECIATION_METHOD))
            .andExpect(jsonPath("$.depreciationPeriod").value(DEFAULT_DEPRECIATION_PERIOD))
            .andExpect(jsonPath("$.fiscalMonthCode").value(DEFAULT_FISCAL_MONTH_CODE))
            .andExpect(jsonPath("$.assetRegistrationCost").value(sameNumber(DEFAULT_ASSET_REGISTRATION_COST)))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.elapsedMonths").value(DEFAULT_ELAPSED_MONTHS.intValue()))
            .andExpect(jsonPath("$.priorMonths").value(DEFAULT_PRIOR_MONTHS.intValue()))
            .andExpect(jsonPath("$.usefulLifeYears").value(sameNumber(DEFAULT_USEFUL_LIFE_YEARS)))
            .andExpect(jsonPath("$.previousNBV").value(sameNumber(DEFAULT_PREVIOUS_NBV)))
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)))
            .andExpect(jsonPath("$.depreciationPeriodStartDate").value(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.depreciationPeriodEndDate").value(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getDepreciationEntryReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        Long id = depreciationEntryReportItem.getId();

        defaultDepreciationEntryReportItemShouldBeFound("id.equals=" + id);
        defaultDepreciationEntryReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationEntryReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationEntryReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationEntryReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationEntryReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails equals to DEFAULT_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationDetails.equals=" + DEFAULT_ASSET_REGISTRATION_DETAILS);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails equals to UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.equals=" + UPDATED_ASSET_REGISTRATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails not equals to DEFAULT_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.notEquals=" + DEFAULT_ASSET_REGISTRATION_DETAILS);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails not equals to UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationDetails.notEquals=" + UPDATED_ASSET_REGISTRATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails in DEFAULT_ASSET_REGISTRATION_DETAILS or UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldBeFound(
            "assetRegistrationDetails.in=" + DEFAULT_ASSET_REGISTRATION_DETAILS + "," + UPDATED_ASSET_REGISTRATION_DETAILS
        );

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails equals to UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.in=" + UPDATED_ASSET_REGISTRATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails is not null
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationDetails.specified=true");

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails is null
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails contains DEFAULT_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationDetails.contains=" + DEFAULT_ASSET_REGISTRATION_DETAILS);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails contains UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.contains=" + UPDATED_ASSET_REGISTRATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails does not contain DEFAULT_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationDetails.doesNotContain=" + DEFAULT_ASSET_REGISTRATION_DETAILS);

        // Get all the depreciationEntryReportItemList where assetRegistrationDetails does not contain UPDATED_ASSET_REGISTRATION_DETAILS
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationDetails.doesNotContain=" + UPDATED_ASSET_REGISTRATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt equals to DEFAULT_POSTED_AT
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.equals=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryReportItemList where postedAt equals to UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.equals=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt not equals to DEFAULT_POSTED_AT
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.notEquals=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryReportItemList where postedAt not equals to UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.notEquals=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt in DEFAULT_POSTED_AT or UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.in=" + DEFAULT_POSTED_AT + "," + UPDATED_POSTED_AT);

        // Get all the depreciationEntryReportItemList where postedAt equals to UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.in=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt is not null
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.specified=true");

        // Get all the depreciationEntryReportItemList where postedAt is null
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt contains DEFAULT_POSTED_AT
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.contains=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryReportItemList where postedAt contains UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.contains=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPostedAtNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where postedAt does not contain DEFAULT_POSTED_AT
        defaultDepreciationEntryReportItemShouldNotBeFound("postedAt.doesNotContain=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryReportItemList where postedAt does not contain UPDATED_POSTED_AT
        defaultDepreciationEntryReportItemShouldBeFound("postedAt.doesNotContain=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber is not null
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.specified=true");

        // Get all the depreciationEntryReportItemList where assetNumber is null
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryReportItemList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultDepreciationEntryReportItemShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet equals to DEFAULT_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.equals=" + DEFAULT_SERVICE_OUTLET);

        // Get all the depreciationEntryReportItemList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.equals=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet not equals to DEFAULT_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.notEquals=" + DEFAULT_SERVICE_OUTLET);

        // Get all the depreciationEntryReportItemList where serviceOutlet not equals to UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.notEquals=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet in DEFAULT_SERVICE_OUTLET or UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.in=" + DEFAULT_SERVICE_OUTLET + "," + UPDATED_SERVICE_OUTLET);

        // Get all the depreciationEntryReportItemList where serviceOutlet equals to UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.in=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet is not null
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.specified=true");

        // Get all the depreciationEntryReportItemList where serviceOutlet is null
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet contains DEFAULT_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.contains=" + DEFAULT_SERVICE_OUTLET);

        // Get all the depreciationEntryReportItemList where serviceOutlet contains UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.contains=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByServiceOutletNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where serviceOutlet does not contain DEFAULT_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldNotBeFound("serviceOutlet.doesNotContain=" + DEFAULT_SERVICE_OUTLET);

        // Get all the depreciationEntryReportItemList where serviceOutlet does not contain UPDATED_SERVICE_OUTLET
        defaultDepreciationEntryReportItemShouldBeFound("serviceOutlet.doesNotContain=" + UPDATED_SERVICE_OUTLET);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the depreciationEntryReportItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the depreciationEntryReportItemList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the depreciationEntryReportItemList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory is not null
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.specified=true");

        // Get all the depreciationEntryReportItemList where assetCategory is null
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the depreciationEntryReportItemList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the depreciationEntryReportItemList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultDepreciationEntryReportItemShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod equals to DEFAULT_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationMethod.equals=" + DEFAULT_DEPRECIATION_METHOD);

        // Get all the depreciationEntryReportItemList where depreciationMethod equals to UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.equals=" + UPDATED_DEPRECIATION_METHOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod not equals to DEFAULT_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.notEquals=" + DEFAULT_DEPRECIATION_METHOD);

        // Get all the depreciationEntryReportItemList where depreciationMethod not equals to UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationMethod.notEquals=" + UPDATED_DEPRECIATION_METHOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod in DEFAULT_DEPRECIATION_METHOD or UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationMethod.in=" + DEFAULT_DEPRECIATION_METHOD + "," + UPDATED_DEPRECIATION_METHOD
        );

        // Get all the depreciationEntryReportItemList where depreciationMethod equals to UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.in=" + UPDATED_DEPRECIATION_METHOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod is not null
        defaultDepreciationEntryReportItemShouldBeFound("depreciationMethod.specified=true");

        // Get all the depreciationEntryReportItemList where depreciationMethod is null
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod contains DEFAULT_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationMethod.contains=" + DEFAULT_DEPRECIATION_METHOD);

        // Get all the depreciationEntryReportItemList where depreciationMethod contains UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.contains=" + UPDATED_DEPRECIATION_METHOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationMethodNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationMethod does not contain DEFAULT_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationMethod.doesNotContain=" + DEFAULT_DEPRECIATION_METHOD);

        // Get all the depreciationEntryReportItemList where depreciationMethod does not contain UPDATED_DEPRECIATION_METHOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationMethod.doesNotContain=" + UPDATED_DEPRECIATION_METHOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod equals to DEFAULT_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriod.equals=" + DEFAULT_DEPRECIATION_PERIOD);

        // Get all the depreciationEntryReportItemList where depreciationPeriod equals to UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.equals=" + UPDATED_DEPRECIATION_PERIOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod not equals to DEFAULT_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.notEquals=" + DEFAULT_DEPRECIATION_PERIOD);

        // Get all the depreciationEntryReportItemList where depreciationPeriod not equals to UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriod.notEquals=" + UPDATED_DEPRECIATION_PERIOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod in DEFAULT_DEPRECIATION_PERIOD or UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriod.in=" + DEFAULT_DEPRECIATION_PERIOD + "," + UPDATED_DEPRECIATION_PERIOD
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriod equals to UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.in=" + UPDATED_DEPRECIATION_PERIOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod is not null
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriod.specified=true");

        // Get all the depreciationEntryReportItemList where depreciationPeriod is null
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod contains DEFAULT_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriod.contains=" + DEFAULT_DEPRECIATION_PERIOD);

        // Get all the depreciationEntryReportItemList where depreciationPeriod contains UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.contains=" + UPDATED_DEPRECIATION_PERIOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriod does not contain DEFAULT_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriod.doesNotContain=" + DEFAULT_DEPRECIATION_PERIOD);

        // Get all the depreciationEntryReportItemList where depreciationPeriod does not contain UPDATED_DEPRECIATION_PERIOD
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriod.doesNotContain=" + UPDATED_DEPRECIATION_PERIOD);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode equals to DEFAULT_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldBeFound("fiscalMonthCode.equals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.equals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode not equals to DEFAULT_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.notEquals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode not equals to UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldBeFound("fiscalMonthCode.notEquals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode in DEFAULT_FISCAL_MONTH_CODE or UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldBeFound(
            "fiscalMonthCode.in=" + DEFAULT_FISCAL_MONTH_CODE + "," + UPDATED_FISCAL_MONTH_CODE
        );

        // Get all the depreciationEntryReportItemList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.in=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode is not null
        defaultDepreciationEntryReportItemShouldBeFound("fiscalMonthCode.specified=true");

        // Get all the depreciationEntryReportItemList where fiscalMonthCode is null
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode contains DEFAULT_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldBeFound("fiscalMonthCode.contains=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode contains UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.contains=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByFiscalMonthCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode does not contain DEFAULT_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldNotBeFound("fiscalMonthCode.doesNotContain=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the depreciationEntryReportItemList where fiscalMonthCode does not contain UPDATED_FISCAL_MONTH_CODE
        defaultDepreciationEntryReportItemShouldBeFound("fiscalMonthCode.doesNotContain=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost equals to DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.equals=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost equals to UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.equals=" + UPDATED_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost not equals to DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.notEquals=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost not equals to UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.notEquals=" + UPDATED_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost in DEFAULT_ASSET_REGISTRATION_COST or UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound(
            "assetRegistrationCost.in=" + DEFAULT_ASSET_REGISTRATION_COST + "," + UPDATED_ASSET_REGISTRATION_COST
        );

        // Get all the depreciationEntryReportItemList where assetRegistrationCost equals to UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.in=" + UPDATED_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is not null
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.specified=true");

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is null
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is greater than or equal to DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.greaterThanOrEqual=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is greater than or equal to UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.greaterThanOrEqual=" + UPDATED_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is less than or equal to DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.lessThanOrEqual=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is less than or equal to SMALLER_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.lessThanOrEqual=" + SMALLER_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is less than DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.lessThan=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is less than UPDATED_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.lessThan=" + UPDATED_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByAssetRegistrationCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is greater than DEFAULT_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldNotBeFound("assetRegistrationCost.greaterThan=" + DEFAULT_ASSET_REGISTRATION_COST);

        // Get all the depreciationEntryReportItemList where assetRegistrationCost is greater than SMALLER_ASSET_REGISTRATION_COST
        defaultDepreciationEntryReportItemShouldBeFound("assetRegistrationCost.greaterThan=" + SMALLER_ASSET_REGISTRATION_COST);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the depreciationEntryReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount is not null
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.specified=true");

        // Get all the depreciationEntryReportItemList where depreciationAmount is null
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryReportItemList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultDepreciationEntryReportItemShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths equals to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.equals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.equals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths not equals to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.notEquals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths not equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.notEquals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths in DEFAULT_ELAPSED_MONTHS or UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.in=" + DEFAULT_ELAPSED_MONTHS + "," + UPDATED_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.in=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths is not null
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.specified=true");

        // Get all the depreciationEntryReportItemList where elapsedMonths is null
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths is greater than or equal to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.greaterThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths is greater than or equal to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.greaterThanOrEqual=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths is less than or equal to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.lessThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths is less than or equal to SMALLER_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.lessThanOrEqual=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths is less than DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.lessThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths is less than UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.lessThan=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByElapsedMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where elapsedMonths is greater than DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("elapsedMonths.greaterThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryReportItemList where elapsedMonths is greater than SMALLER_ELAPSED_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("elapsedMonths.greaterThan=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths equals to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.equals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.equals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths not equals to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.notEquals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths not equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.notEquals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths in DEFAULT_PRIOR_MONTHS or UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.in=" + DEFAULT_PRIOR_MONTHS + "," + UPDATED_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.in=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths is not null
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.specified=true");

        // Get all the depreciationEntryReportItemList where priorMonths is null
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths is greater than or equal to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.greaterThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths is greater than or equal to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.greaterThanOrEqual=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths is less than or equal to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.lessThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths is less than or equal to SMALLER_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.lessThanOrEqual=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths is less than DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.lessThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths is less than UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.lessThan=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPriorMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where priorMonths is greater than DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldNotBeFound("priorMonths.greaterThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryReportItemList where priorMonths is greater than SMALLER_PRIOR_MONTHS
        defaultDepreciationEntryReportItemShouldBeFound("priorMonths.greaterThan=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.equals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.equals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears not equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.notEquals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears not equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.notEquals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears in DEFAULT_USEFUL_LIFE_YEARS or UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound(
            "usefulLifeYears.in=" + DEFAULT_USEFUL_LIFE_YEARS + "," + UPDATED_USEFUL_LIFE_YEARS
        );

        // Get all the depreciationEntryReportItemList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.in=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is not null
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.specified=true");

        // Get all the depreciationEntryReportItemList where usefulLifeYears is null
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is greater than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.greaterThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is greater than or equal to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.greaterThanOrEqual=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is less than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.lessThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is less than or equal to SMALLER_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.lessThanOrEqual=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is less than DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.lessThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is less than UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.lessThan=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByUsefulLifeYearsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is greater than DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldNotBeFound("usefulLifeYears.greaterThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryReportItemList where usefulLifeYears is greater than SMALLER_USEFUL_LIFE_YEARS
        defaultDepreciationEntryReportItemShouldBeFound("usefulLifeYears.greaterThan=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV equals to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.equals=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.equals=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV not equals to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.notEquals=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV not equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.notEquals=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV in DEFAULT_PREVIOUS_NBV or UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.in=" + DEFAULT_PREVIOUS_NBV + "," + UPDATED_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.in=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV is not null
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.specified=true");

        // Get all the depreciationEntryReportItemList where previousNBV is null
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV is greater than or equal to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.greaterThanOrEqual=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV is greater than or equal to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.greaterThanOrEqual=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV is less than or equal to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.lessThanOrEqual=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV is less than or equal to SMALLER_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.lessThanOrEqual=" + SMALLER_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV is less than DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.lessThan=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV is less than UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.lessThan=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByPreviousNBVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where previousNBV is greater than DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldNotBeFound("previousNBV.greaterThan=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryReportItemList where previousNBV is greater than SMALLER_PREVIOUS_NBV
        defaultDepreciationEntryReportItemShouldBeFound("previousNBV.greaterThan=" + SMALLER_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue is not null
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.specified=true");

        // Get all the depreciationEntryReportItemList where netBookValue is null
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryReportItemList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultDepreciationEntryReportItemShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate equals to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodStartDate.equals=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodStartDate.equals=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate not equals to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodStartDate.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate not equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodStartDate.notEquals=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate in DEFAULT_DEPRECIATION_PERIOD_START_DATE or UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodStartDate.in=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE + "," + UPDATED_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodStartDate.in=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is not null
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodStartDate.specified=true");

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is null
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is greater than or equal to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodStartDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is greater than or equal to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodStartDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_PERIOD_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is less than or equal to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodStartDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is less than or equal to SMALLER_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodStartDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_PERIOD_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is less than DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodStartDate.lessThan=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is less than UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodStartDate.lessThan=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is greater than DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodStartDate.greaterThan=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodStartDate is greater than SMALLER_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodStartDate.greaterThan=" + SMALLER_DEPRECIATION_PERIOD_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate equals to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodEndDate.equals=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.equals=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate not equals to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate not equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodEndDate.notEquals=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate in DEFAULT_DEPRECIATION_PERIOD_END_DATE or UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodEndDate.in=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE + "," + UPDATED_DEPRECIATION_PERIOD_END_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.in=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is not null
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodEndDate.specified=true");

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is null
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is greater than or equal to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodEndDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is greater than or equal to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodEndDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_PERIOD_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is less than or equal to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound(
            "depreciationPeriodEndDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE
        );

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is less than or equal to SMALLER_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound(
            "depreciationPeriodEndDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_PERIOD_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is less than DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.lessThan=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is less than UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodEndDate.lessThan=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntryReportItemsByDepreciationPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is greater than DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldNotBeFound("depreciationPeriodEndDate.greaterThan=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryReportItemList where depreciationPeriodEndDate is greater than SMALLER_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryReportItemShouldBeFound("depreciationPeriodEndDate.greaterThan=" + SMALLER_DEPRECIATION_PERIOD_END_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationEntryReportItemShouldBeFound(String filter) throws Exception {
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetRegistrationDetails").value(hasItem(DEFAULT_ASSET_REGISTRATION_DETAILS)))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(DEFAULT_POSTED_AT)))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationMethod").value(hasItem(DEFAULT_DEPRECIATION_METHOD)))
            .andExpect(jsonPath("$.[*].depreciationPeriod").value(hasItem(DEFAULT_DEPRECIATION_PERIOD)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].assetRegistrationCost").value(hasItem(sameNumber(DEFAULT_ASSET_REGISTRATION_COST))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())));

        // Check, that the count call also returns 1
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationEntryReportItemShouldNotBeFound(String filter) throws Exception {
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationEntryReportItem() throws Exception {
        // Get the depreciationEntryReportItem
        restDepreciationEntryReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchDepreciationEntryReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationEntryReportItemRepository.saveAndFlush(depreciationEntryReportItem);
        when(mockDepreciationEntryReportItemSearchRepository.search("id:" + depreciationEntryReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationEntryReportItem), PageRequest.of(0, 1), 1));

        // Search the depreciationEntryReportItem
        restDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationEntryReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetRegistrationDetails").value(hasItem(DEFAULT_ASSET_REGISTRATION_DETAILS)))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(DEFAULT_POSTED_AT)))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutlet").value(hasItem(DEFAULT_SERVICE_OUTLET)))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationMethod").value(hasItem(DEFAULT_DEPRECIATION_METHOD)))
            .andExpect(jsonPath("$.[*].depreciationPeriod").value(hasItem(DEFAULT_DEPRECIATION_PERIOD)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].assetRegistrationCost").value(hasItem(sameNumber(DEFAULT_ASSET_REGISTRATION_COST))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())));
    }
}
