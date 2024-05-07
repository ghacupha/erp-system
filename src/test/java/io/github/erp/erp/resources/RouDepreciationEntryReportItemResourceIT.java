package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.RouDepreciationEntryReportItem;
import io.github.erp.repository.RouDepreciationEntryReportItemRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepository;
import io.github.erp.service.mapper.RouDepreciationEntryReportItemMapper;
import io.github.erp.web.rest.RouDepreciationEntryReportItemResource;
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
 * Integration tests for the {@link RouDepreciationEntryReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouDepreciationEntryReportItemResourceIT {

    private static final String DEFAULT_LEASE_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FISCAL_PERIOD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_PERIOD_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FISCAL_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEBIT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEBIT_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ROU_ASSET_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_ROU_ASSET_IDENTIFIER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/leases/rou-depreciation-entry-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-depreciation-entry-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository;

    @Autowired
    private RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationEntryReportItemSearchRepository mockRouDepreciationEntryReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationEntryReportItemMockMvc;

    private RouDepreciationEntryReportItem rouDepreciationEntryReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntryReportItem createEntity(EntityManager em) {
        RouDepreciationEntryReportItem rouDepreciationEntryReportItem = new RouDepreciationEntryReportItem()
            .leaseContractNumber(DEFAULT_LEASE_CONTRACT_NUMBER)
            .fiscalPeriodCode(DEFAULT_FISCAL_PERIOD_CODE)
            .fiscalPeriodEndDate(DEFAULT_FISCAL_PERIOD_END_DATE)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .debitAccountNumber(DEFAULT_DEBIT_ACCOUNT_NUMBER)
            .creditAccountNumber(DEFAULT_CREDIT_ACCOUNT_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .shortTitle(DEFAULT_SHORT_TITLE)
            .rouAssetIdentifier(DEFAULT_ROU_ASSET_IDENTIFIER)
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT);
        return rouDepreciationEntryReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntryReportItem createUpdatedEntity(EntityManager em) {
        RouDepreciationEntryReportItem rouDepreciationEntryReportItem = new RouDepreciationEntryReportItem()
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .fiscalPeriodCode(UPDATED_FISCAL_PERIOD_CODE)
            .fiscalPeriodEndDate(UPDATED_FISCAL_PERIOD_END_DATE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .debitAccountNumber(UPDATED_DEBIT_ACCOUNT_NUMBER)
            .creditAccountNumber(UPDATED_CREDIT_ACCOUNT_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .shortTitle(UPDATED_SHORT_TITLE)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT);
        return rouDepreciationEntryReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationEntryReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItems() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodCode").value(hasItem(DEFAULT_FISCAL_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].debitAccountNumber").value(hasItem(DEFAULT_DEBIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditAccountNumber").value(hasItem(DEFAULT_CREDIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }

    @Test
    @Transactional
    void getRouDepreciationEntryReportItem() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get the rouDepreciationEntryReportItem
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationEntryReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationEntryReportItem.getId().intValue()))
            .andExpect(jsonPath("$.leaseContractNumber").value(DEFAULT_LEASE_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.fiscalPeriodCode").value(DEFAULT_FISCAL_PERIOD_CODE))
            .andExpect(jsonPath("$.fiscalPeriodEndDate").value(DEFAULT_FISCAL_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.debitAccountNumber").value(DEFAULT_DEBIT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.creditAccountNumber").value(DEFAULT_CREDIT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.shortTitle").value(DEFAULT_SHORT_TITLE))
            .andExpect(jsonPath("$.rouAssetIdentifier").value(DEFAULT_ROU_ASSET_IDENTIFIER))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)));
    }

    @Test
    @Transactional
    void getRouDepreciationEntryReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        Long id = rouDepreciationEntryReportItem.getId();

        defaultRouDepreciationEntryReportItemShouldBeFound("id.equals=" + id);
        defaultRouDepreciationEntryReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationEntryReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationEntryReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationEntryReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationEntryReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("leaseContractNumber.equals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.equals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber not equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.notEquals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber not equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("leaseContractNumber.notEquals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber in DEFAULT_LEASE_CONTRACT_NUMBER or UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "leaseContractNumber.in=" + DEFAULT_LEASE_CONTRACT_NUMBER + "," + UPDATED_LEASE_CONTRACT_NUMBER
        );

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.in=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("leaseContractNumber.specified=true");

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber contains DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("leaseContractNumber.contains=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber contains UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.contains=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByLeaseContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber does not contain DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("leaseContractNumber.doesNotContain=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where leaseContractNumber does not contain UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("leaseContractNumber.doesNotContain=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode equals to DEFAULT_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodCode.equals=" + DEFAULT_FISCAL_PERIOD_CODE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode equals to UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.equals=" + UPDATED_FISCAL_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode not equals to DEFAULT_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.notEquals=" + DEFAULT_FISCAL_PERIOD_CODE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode not equals to UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodCode.notEquals=" + UPDATED_FISCAL_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode in DEFAULT_FISCAL_PERIOD_CODE or UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "fiscalPeriodCode.in=" + DEFAULT_FISCAL_PERIOD_CODE + "," + UPDATED_FISCAL_PERIOD_CODE
        );

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode equals to UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.in=" + UPDATED_FISCAL_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodCode.specified=true");

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode contains DEFAULT_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodCode.contains=" + DEFAULT_FISCAL_PERIOD_CODE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode contains UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.contains=" + UPDATED_FISCAL_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode does not contain DEFAULT_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodCode.doesNotContain=" + DEFAULT_FISCAL_PERIOD_CODE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodCode does not contain UPDATED_FISCAL_PERIOD_CODE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodCode.doesNotContain=" + UPDATED_FISCAL_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.equals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.equals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate not equals to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.notEquals=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate not equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.notEquals=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate in DEFAULT_FISCAL_PERIOD_END_DATE or UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "fiscalPeriodEndDate.in=" + DEFAULT_FISCAL_PERIOD_END_DATE + "," + UPDATED_FISCAL_PERIOD_END_DATE
        );

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate equals to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.in=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.specified=true");

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is greater than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is greater than or equal to UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThanOrEqual=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is less than or equal to DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is less than or equal to SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThanOrEqual=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is less than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.lessThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is less than UPDATED_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.lessThan=" + UPDATED_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByFiscalPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is greater than DEFAULT_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("fiscalPeriodEndDate.greaterThan=" + DEFAULT_FISCAL_PERIOD_END_DATE);

        // Get all the rouDepreciationEntryReportItemList where fiscalPeriodEndDate is greater than SMALLER_FISCAL_PERIOD_END_DATE
        defaultRouDepreciationEntryReportItemShouldBeFound("fiscalPeriodEndDate.greaterThan=" + SMALLER_FISCAL_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldBeFound("assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName not equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.notEquals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName not equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldBeFound("assetCategoryName.notEquals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName in DEFAULT_ASSET_CATEGORY_NAME or UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME
        );

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("assetCategoryName.specified=true");

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName contains DEFAULT_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldBeFound("assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName contains UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName does not contain DEFAULT_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldNotBeFound("assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the rouDepreciationEntryReportItemList where assetCategoryName does not contain UPDATED_ASSET_CATEGORY_NAME
        defaultRouDepreciationEntryReportItemShouldBeFound("assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber equals to DEFAULT_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("debitAccountNumber.equals=" + DEFAULT_DEBIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber equals to UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.equals=" + UPDATED_DEBIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber not equals to DEFAULT_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.notEquals=" + DEFAULT_DEBIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber not equals to UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("debitAccountNumber.notEquals=" + UPDATED_DEBIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber in DEFAULT_DEBIT_ACCOUNT_NUMBER or UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "debitAccountNumber.in=" + DEFAULT_DEBIT_ACCOUNT_NUMBER + "," + UPDATED_DEBIT_ACCOUNT_NUMBER
        );

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber equals to UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.in=" + UPDATED_DEBIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("debitAccountNumber.specified=true");

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber contains DEFAULT_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("debitAccountNumber.contains=" + DEFAULT_DEBIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber contains UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.contains=" + UPDATED_DEBIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDebitAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber does not contain DEFAULT_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("debitAccountNumber.doesNotContain=" + DEFAULT_DEBIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where debitAccountNumber does not contain UPDATED_DEBIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("debitAccountNumber.doesNotContain=" + UPDATED_DEBIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber equals to DEFAULT_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("creditAccountNumber.equals=" + DEFAULT_CREDIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber equals to UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.equals=" + UPDATED_CREDIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber not equals to DEFAULT_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.notEquals=" + DEFAULT_CREDIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber not equals to UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("creditAccountNumber.notEquals=" + UPDATED_CREDIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber in DEFAULT_CREDIT_ACCOUNT_NUMBER or UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "creditAccountNumber.in=" + DEFAULT_CREDIT_ACCOUNT_NUMBER + "," + UPDATED_CREDIT_ACCOUNT_NUMBER
        );

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber equals to UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.in=" + UPDATED_CREDIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("creditAccountNumber.specified=true");

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber contains DEFAULT_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("creditAccountNumber.contains=" + DEFAULT_CREDIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber contains UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.contains=" + UPDATED_CREDIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByCreditAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber does not contain DEFAULT_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("creditAccountNumber.doesNotContain=" + DEFAULT_CREDIT_ACCOUNT_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where creditAccountNumber does not contain UPDATED_CREDIT_ACCOUNT_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("creditAccountNumber.doesNotContain=" + UPDATED_CREDIT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description not equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryReportItemList where description not equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouDepreciationEntryReportItemList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("description.specified=true");

        // Get all the rouDepreciationEntryReportItemList where description is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description contains DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryReportItemList where description contains UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where description does not contain DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryReportItemList where description does not contain UPDATED_DESCRIPTION
        defaultRouDepreciationEntryReportItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle equals to DEFAULT_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.equals=" + DEFAULT_SHORT_TITLE);

        // Get all the rouDepreciationEntryReportItemList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.equals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle not equals to DEFAULT_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.notEquals=" + DEFAULT_SHORT_TITLE);

        // Get all the rouDepreciationEntryReportItemList where shortTitle not equals to UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.notEquals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle in DEFAULT_SHORT_TITLE or UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.in=" + DEFAULT_SHORT_TITLE + "," + UPDATED_SHORT_TITLE);

        // Get all the rouDepreciationEntryReportItemList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.in=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.specified=true");

        // Get all the rouDepreciationEntryReportItemList where shortTitle is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle contains DEFAULT_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.contains=" + DEFAULT_SHORT_TITLE);

        // Get all the rouDepreciationEntryReportItemList where shortTitle contains UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.contains=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByShortTitleNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where shortTitle does not contain DEFAULT_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldNotBeFound("shortTitle.doesNotContain=" + DEFAULT_SHORT_TITLE);

        // Get all the rouDepreciationEntryReportItemList where shortTitle does not contain UPDATED_SHORT_TITLE
        defaultRouDepreciationEntryReportItemShouldBeFound("shortTitle.doesNotContain=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldBeFound("rouAssetIdentifier.equals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.equals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier not equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.notEquals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier not equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldBeFound("rouAssetIdentifier.notEquals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier in DEFAULT_ROU_ASSET_IDENTIFIER or UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "rouAssetIdentifier.in=" + DEFAULT_ROU_ASSET_IDENTIFIER + "," + UPDATED_ROU_ASSET_IDENTIFIER
        );

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.in=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("rouAssetIdentifier.specified=true");

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier contains DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldBeFound("rouAssetIdentifier.contains=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier contains UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.contains=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByRouAssetIdentifierNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier does not contain DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("rouAssetIdentifier.doesNotContain=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryReportItemList where rouAssetIdentifier does not contain UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryReportItemShouldBeFound("rouAssetIdentifier.doesNotContain=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.specified=true");

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryReportItemList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultRouDepreciationEntryReportItemShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.specified=true");

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound(
            "outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT
        );

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is not null
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.specified=true");

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is null
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntryReportItemsByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryReportItemList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryReportItemShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationEntryReportItemShouldBeFound(String filter) throws Exception {
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodCode").value(hasItem(DEFAULT_FISCAL_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].debitAccountNumber").value(hasItem(DEFAULT_DEBIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditAccountNumber").value(hasItem(DEFAULT_CREDIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));

        // Check, that the count call also returns 1
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationEntryReportItemShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationEntryReportItem() throws Exception {
        // Get the rouDepreciationEntryReportItem
        restRouDepreciationEntryReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchRouDepreciationEntryReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationEntryReportItemRepository.saveAndFlush(rouDepreciationEntryReportItem);
        when(
            mockRouDepreciationEntryReportItemSearchRepository.search("id:" + rouDepreciationEntryReportItem.getId(), PageRequest.of(0, 20))
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationEntryReportItem), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationEntryReportItem
        restRouDepreciationEntryReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationEntryReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntryReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].fiscalPeriodCode").value(hasItem(DEFAULT_FISCAL_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].fiscalPeriodEndDate").value(hasItem(DEFAULT_FISCAL_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].debitAccountNumber").value(hasItem(DEFAULT_DEBIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditAccountNumber").value(hasItem(DEFAULT_CREDIT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))));
    }
}
