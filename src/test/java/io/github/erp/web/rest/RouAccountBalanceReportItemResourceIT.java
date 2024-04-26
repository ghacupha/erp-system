package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_MONTH_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_MONTH_END_DATE = LocalDate.ofEpochDay(-1L);

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
            .netBookValue(DEFAULT_NET_BOOK_VALUE)
            .fiscalMonthEndDate(DEFAULT_FISCAL_MONTH_END_DATE);
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
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .fiscalMonthEndDate(UPDATED_FISCAL_MONTH_END_DATE);
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
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())));
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
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)))
            .andExpect(jsonPath("$.fiscalMonthEndDate").value(DEFAULT_FISCAL_MONTH_END_DATE.toString()));
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
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.equals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.equals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate not equals to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.notEquals=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate not equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.notEquals=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate in DEFAULT_FISCAL_MONTH_END_DATE or UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound(
            "fiscalMonthEndDate.in=" + DEFAULT_FISCAL_MONTH_END_DATE + "," + UPDATED_FISCAL_MONTH_END_DATE
        );

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate equals to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.in=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is not null
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.specified=true");

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is null
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is greater than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is greater than or equal to UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is less than or equal to DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is less than or equal to SMALLER_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.lessThanOrEqual=" + SMALLER_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is less than DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.lessThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is less than UPDATED_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.lessThan=" + UPDATED_FISCAL_MONTH_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouAccountBalanceReportItemsByFiscalMonthEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouAccountBalanceReportItemRepository.saveAndFlush(rouAccountBalanceReportItem);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is greater than DEFAULT_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldNotBeFound("fiscalMonthEndDate.greaterThan=" + DEFAULT_FISCAL_MONTH_END_DATE);

        // Get all the rouAccountBalanceReportItemList where fiscalMonthEndDate is greater than SMALLER_FISCAL_MONTH_END_DATE
        defaultRouAccountBalanceReportItemShouldBeFound("fiscalMonthEndDate.greaterThan=" + SMALLER_FISCAL_MONTH_END_DATE);
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
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())));

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
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].fiscalMonthEndDate").value(hasItem(DEFAULT_FISCAL_MONTH_END_DATE.toString())));
    }
}
