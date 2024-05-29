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
import io.github.erp.domain.RouAccountBalanceReportItem;
import io.github.erp.repository.RouAccountBalanceReportItemRepository;
import io.github.erp.repository.search.RouAccountBalanceReportItemSearchRepository;
import io.github.erp.service.criteria.RouAccountBalanceReportItemCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportItemMapper;
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
 * Integration tests for the {@link RouAccountBalanceReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouAccountBalanceReportItemResourceIT {

    private static final String DEFAULT_ASSET_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_LEASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_LEASE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_LEASE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCRUED_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCRUED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCRUED_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_PERIOD_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/rou-account-balance-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-account-balance-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository;

    @Autowired
    private RouAccountBalanceReportItemMapper rouAccountBalanceReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouAccountBalanceReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouAccountBalanceReportItemSearchRepository mockRouAccountBalanceReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouAccountBalanceReportItemMockMvc;

    private RouAccountBalanceReportItem rouAccountBalanceReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAccountBalanceReportItem createEntity(EntityManager em) {
        RouAccountBalanceReportItem rouAccountBalanceReportItem = new RouAccountBalanceReportItem()
            .assetAccountName(DEFAULT_ASSET_ACCOUNT_NAME)
            .assetAccountNumber(DEFAULT_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)
            .totalLeaseAmount(DEFAULT_TOTAL_LEASE_AMOUNT)
            .accruedDepreciationAmount(DEFAULT_ACCRUED_DEPRECIATION_AMOUNT)
            .currentPeriodDepreciationAmount(DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT)
            .netBookValue(DEFAULT_NET_BOOK_VALUE)
            .fiscalPeriodEndDate(DEFAULT_FISCAL_PERIOD_END_DATE);
        return rouAccountBalanceReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouAccountBalanceReportItem createUpdatedEntity(EntityManager em) {
        RouAccountBalanceReportItem rouAccountBalanceReportItem = new RouAccountBalanceReportItem()
            .assetAccountName(UPDATED_ASSET_ACCOUNT_NAME)
            .assetAccountNumber(UPDATED_ASSET_ACCOUNT_NUMBER)
            .depreciationAccountNumber(UPDATED_DEPRECIATION_ACCOUNT_NUMBER)
            .totalLeaseAmount(UPDATED_TOTAL_LEASE_AMOUNT)
            .accruedDepreciationAmount(UPDATED_ACCRUED_DEPRECIATION_AMOUNT)
            .currentPeriodDepreciationAmount(UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .fiscalPeriodEndDate(UPDATED_FISCAL_PERIOD_END_DATE);
        return rouAccountBalanceReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouAccountBalanceReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItems() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetAccountName").value(hasItem(DEFAULT_ASSET_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].totalLeaseAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].accruedDepreciationAmount").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION_AMOUNT))))
            .andExpect(
                jsonPath("$.[*].currentPeriodDepreciationAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT)))
            )
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getRouAccountBalanceReportItem() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get the rouAccountBalanceReportItem
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouAccountBalanceReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouAccountBalanceReportItem.getId().intValue()))
            .andExpect(jsonPath("$.assetAccountName").value(DEFAULT_ASSET_ACCOUNT_NAME))
            .andExpect(jsonPath("$.assetAccountNumber").value(DEFAULT_ASSET_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.depreciationAccountNumber").value(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.totalLeaseAmount").value(sameNumber(DEFAULT_TOTAL_LEASE_AMOUNT)))
            .andExpect(jsonPath("$.accruedDepreciationAmount").value(sameNumber(DEFAULT_ACCRUED_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.currentPeriodDepreciationAmount").value(sameNumber(DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)))
            .andExpect(jsonPath("$.fiscalPeriodEndDate").value(DEFAULT_FISCAL_PERIOD_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getRouAccountBalanceReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        Long id = rouAccountBalanceReportItem.getId();

        defaultRouAccountBalanceReportItemShouldBeFound("id.equals=" + id);
        defaultRouAccountBalanceReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouAccountBalanceReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouAccountBalanceReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouAccountBalanceReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouAccountBalanceReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName equals to DEFAULT_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountName.equals=" + DEFAULT_ASSET_ACCOUNT_NAME);

        // Get all the rouAccountBalanceReportItemList where assetAccountName equals to UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.equals=" + UPDATED_ASSET_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName not equals to DEFAULT_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.notEquals=" + DEFAULT_ASSET_ACCOUNT_NAME);

        // Get all the rouAccountBalanceReportItemList where assetAccountName not equals to UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountName.notEquals=" + UPDATED_ASSET_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName in DEFAULT_ASSET_ACCOUNT_NAME or UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldBeFound(
            "assetAccountName.in=" + DEFAULT_ASSET_ACCOUNT_NAME + "," + UPDATED_ASSET_ACCOUNT_NAME
        );

        // Get all the rouAccountBalanceReportItemList where assetAccountName equals to UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.in=" + UPDATED_ASSET_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName is not null
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountName.specified=true");

        // Get all the rouAccountBalanceReportItemList where assetAccountName is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName contains DEFAULT_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountName.contains=" + DEFAULT_ASSET_ACCOUNT_NAME);

        // Get all the rouAccountBalanceReportItemList where assetAccountName contains UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.contains=" + UPDATED_ASSET_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountName does not contain DEFAULT_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountName.doesNotContain=" + DEFAULT_ASSET_ACCOUNT_NAME);

        // Get all the rouAccountBalanceReportItemList where assetAccountName does not contain UPDATED_ASSET_ACCOUNT_NAME
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountName.doesNotContain=" + UPDATED_ASSET_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountNumber.equals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.equals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber not equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.notEquals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber not equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountNumber.notEquals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber in DEFAULT_ASSET_ACCOUNT_NUMBER or UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound(
            "assetAccountNumber.in=" + DEFAULT_ASSET_ACCOUNT_NUMBER + "," + UPDATED_ASSET_ACCOUNT_NUMBER
        );

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.in=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber is not null
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountNumber.specified=true");

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber contains DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountNumber.contains=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber contains UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.contains=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAssetAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber does not contain DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("assetAccountNumber.doesNotContain=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where assetAccountNumber does not contain UPDATED_ASSET_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("assetAccountNumber.doesNotContain=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("depreciationAccountNumber.equals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("depreciationAccountNumber.equals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber not equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("depreciationAccountNumber.notEquals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber not equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("depreciationAccountNumber.notEquals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber in DEFAULT_DEPRECIATION_ACCOUNT_NUMBER or UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound(
            "depreciationAccountNumber.in=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER + "," + UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("depreciationAccountNumber.in=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber is not null
        defaultRouAccountBalanceReportItemShouldBeFound("depreciationAccountNumber.specified=true");

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("depreciationAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber contains DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("depreciationAccountNumber.contains=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber contains UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound("depreciationAccountNumber.contains=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByDepreciationAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber does not contain DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "depreciationAccountNumber.doesNotContain=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        );

        // Get all the rouAccountBalanceReportItemList where depreciationAccountNumber does not contain UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultRouAccountBalanceReportItemShouldBeFound("depreciationAccountNumber.doesNotContain=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount equals to DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.equals=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount equals to UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.equals=" + UPDATED_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount not equals to DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.notEquals=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount not equals to UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.notEquals=" + UPDATED_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount in DEFAULT_TOTAL_LEASE_AMOUNT or UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "totalLeaseAmount.in=" + DEFAULT_TOTAL_LEASE_AMOUNT + "," + UPDATED_TOTAL_LEASE_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount equals to UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.in=" + UPDATED_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is not null
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.specified=true");

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is greater than or equal to DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is greater than or equal to UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.greaterThanOrEqual=" + UPDATED_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is less than or equal to DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.lessThanOrEqual=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is less than or equal to SMALLER_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.lessThanOrEqual=" + SMALLER_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is less than DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.lessThan=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is less than UPDATED_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.lessThan=" + UPDATED_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByTotalLeaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is greater than DEFAULT_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("totalLeaseAmount.greaterThan=" + DEFAULT_TOTAL_LEASE_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where totalLeaseAmount is greater than SMALLER_TOTAL_LEASE_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("totalLeaseAmount.greaterThan=" + SMALLER_TOTAL_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount equals to DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.equals=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount equals to UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.equals=" + UPDATED_ACCRUED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount not equals to DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.notEquals=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount not equals to UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.notEquals=" + UPDATED_ACCRUED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount in DEFAULT_ACCRUED_DEPRECIATION_AMOUNT or UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "accruedDepreciationAmount.in=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT + "," + UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount equals to UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.in=" + UPDATED_ACCRUED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is not null
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.specified=true");

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is greater than or equal to DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "accruedDepreciationAmount.greaterThanOrEqual=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is greater than or equal to UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "accruedDepreciationAmount.greaterThanOrEqual=" + UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is less than or equal to DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.lessThanOrEqual=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is less than or equal to SMALLER_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "accruedDepreciationAmount.lessThanOrEqual=" + SMALLER_ACCRUED_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is less than DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.lessThan=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is less than UPDATED_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.lessThan=" + UPDATED_ACCRUED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByAccruedDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is greater than DEFAULT_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound("accruedDepreciationAmount.greaterThan=" + DEFAULT_ACCRUED_DEPRECIATION_AMOUNT);

        // Get all the rouAccountBalanceReportItemList where accruedDepreciationAmount is greater than SMALLER_ACCRUED_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound("accruedDepreciationAmount.greaterThan=" + SMALLER_ACCRUED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount equals to DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.equals=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount equals to UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.equals=" + UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount not equals to DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.notEquals=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount not equals to UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.notEquals=" + UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount in DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT or UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.in=" +
            DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT +
            "," +
            UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount equals to UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.in=" + UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is not null
        defaultRouAccountBalanceReportItemShouldBeFound("currentPeriodDepreciationAmount.specified=true");

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("currentPeriodDepreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is greater than or equal to DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.greaterThanOrEqual=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is greater than or equal to UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.greaterThanOrEqual=" + UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is less than or equal to DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.lessThanOrEqual=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is less than or equal to SMALLER_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.lessThanOrEqual=" + SMALLER_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is less than DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.lessThan=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is less than UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.lessThan=" + UPDATED_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByCurrentPeriodDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is greater than DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldNotBeFound(
            "currentPeriodDepreciationAmount.greaterThan=" + DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );

        // Get all the rouAccountBalanceReportItemList where currentPeriodDepreciationAmount is greater than SMALLER_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        defaultRouAccountBalanceReportItemShouldBeFound(
            "currentPeriodDepreciationAmount.greaterThan=" + SMALLER_CURRENT_PERIOD_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue is not null
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.specified=true");

        // Get all the rouAccountBalanceReportItemList where netBookValue is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the rouAccountBalanceReportItemList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultRouAccountBalanceReportItemShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.equals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.equals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate not equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.notEquals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate not equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.notEquals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate in DEFAULT_FISCAL_PERIOD_END_DATE or UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound(
            "fiscalPeriodEndDate.in=" + DEFAULT_FISCAL_PERIOD_END_DATE + "," + UPDATED_FISCAL_PERIOD_END_DATE
        );

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.in=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is not null
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.specified=true");

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is greater than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is greater than or equal to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is less than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is less than or equal to SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is less than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is less than UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.lessThan=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is greater than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalPeriodEndDate is greater than SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalPeriodEndDate.greaterThan=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouAccountBalanceReportItemShouldBeFound(String filter) throws Exception {
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetAccountName").value(hasItem(DEFAULT_ASSET_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].totalLeaseAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].accruedDepreciationAmount").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION_AMOUNT))))
            .andExpect(
                jsonPath("$.[*].currentPeriodDepreciationAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT)))
            )
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())));

        // Check, that the count call also returns 1
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouAccountBalanceReportItemShouldNotBeFound(String filter) throws Exception {
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouAccountBalanceReportItem() throws Exception {
        // Get the rouAccountBalanceReportItem
        restRouAccountBalanceReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchRouAccountBalanceReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);
        when(mockRouAccountBalanceReportItemSearchRepository.search("id:" + rouAccountBalanceReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouAccountBalanceReportItem), PageRequest.of(0, 1), 1));

        // Search the rouAccountBalanceReportItem
        restRouAccountBalanceReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouAccountBalanceReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouAccountBalanceReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetAccountName").value(hasItem(DEFAULT_ASSET_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].totalLeaseAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].accruedDepreciationAmount").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION_AMOUNT))))
            .andExpect(
                jsonPath("$.[*].currentPeriodDepreciationAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_PERIOD_DEPRECIATION_AMOUNT)))
            )
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())));
    }
}
