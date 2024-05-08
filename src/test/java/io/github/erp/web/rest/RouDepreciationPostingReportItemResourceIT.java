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
import io.github.erp.domain.RouDepreciationPostingReportItem;
import io.github.erp.repository.RouDepreciationPostingReportItemRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepository;
import io.github.erp.service.criteria.RouDepreciationPostingReportItemCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportItemMapper;
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
 * Integration tests for the {@link RouDepreciationPostingReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouDepreciationPostingReportItemResourceIT {

    private static final String DEFAULT_LEASE_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FISCAL_MONTH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_MONTH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_FOR_CREDIT = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_FOR_CREDIT = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_FOR_DEBIT = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_FOR_DEBIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/rou-depreciation-posting-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-depreciation-posting-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository;

    @Autowired
    private RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationPostingReportItemSearchRepository mockRouDepreciationPostingReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationPostingReportItemMockMvc;

    private RouDepreciationPostingReportItem rouDepreciationPostingReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationPostingReportItem createEntity(EntityManager em) {
        RouDepreciationPostingReportItem rouDepreciationPostingReportItem = new RouDepreciationPostingReportItem()
            .leaseContractNumber(DEFAULT_LEASE_CONTRACT_NUMBER)
            .leaseDescription(DEFAULT_LEASE_DESCRIPTION)
            .fiscalMonthCode(DEFAULT_FISCAL_MONTH_CODE)
            .accountForCredit(DEFAULT_ACCOUNT_FOR_CREDIT)
            .accountForDebit(DEFAULT_ACCOUNT_FOR_DEBIT)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT);
        return rouDepreciationPostingReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationPostingReportItem createUpdatedEntity(EntityManager em) {
        RouDepreciationPostingReportItem rouDepreciationPostingReportItem = new RouDepreciationPostingReportItem()
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .leaseDescription(UPDATED_LEASE_DESCRIPTION)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE)
            .accountForCredit(UPDATED_ACCOUNT_FOR_CREDIT)
            .accountForDebit(UPDATED_ACCOUNT_FOR_DEBIT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT);
        return rouDepreciationPostingReportItem;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationPostingReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItems() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].accountForCredit").value(hasItem(DEFAULT_ACCOUNT_FOR_CREDIT)))
            .andExpect(jsonPath("$.[*].accountForDebit").value(hasItem(DEFAULT_ACCOUNT_FOR_DEBIT)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))));
    }

    @Test
    @Transactional
    void getRouDepreciationPostingReportItem() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get the rouDepreciationPostingReportItem
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationPostingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationPostingReportItem.getId().intValue()))
            .andExpect(jsonPath("$.leaseContractNumber").value(DEFAULT_LEASE_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.leaseDescription").value(DEFAULT_LEASE_DESCRIPTION))
            .andExpect(jsonPath("$.fiscalMonthCode").value(DEFAULT_FISCAL_MONTH_CODE))
            .andExpect(jsonPath("$.accountForCredit").value(DEFAULT_ACCOUNT_FOR_CREDIT))
            .andExpect(jsonPath("$.accountForDebit").value(DEFAULT_ACCOUNT_FOR_DEBIT))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)));
    }

    @Test
    @Transactional
    void getRouDepreciationPostingReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        Long id = rouDepreciationPostingReportItem.getId();

        defaultRouDepreciationPostingReportItemShouldBeFound("id.equals=" + id);
        defaultRouDepreciationPostingReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationPostingReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationPostingReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationPostingReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationPostingReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseContractNumber.equals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.equals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber not equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.notEquals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber not equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseContractNumber.notEquals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber in DEFAULT_LEASE_CONTRACT_NUMBER or UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "leaseContractNumber.in=" + DEFAULT_LEASE_CONTRACT_NUMBER + "," + UPDATED_LEASE_CONTRACT_NUMBER
        );

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.in=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseContractNumber.specified=true");

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber contains DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseContractNumber.contains=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber contains UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.contains=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber does not contain DEFAULT_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseContractNumber.doesNotContain=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the rouDepreciationPostingReportItemList where leaseContractNumber does not contain UPDATED_LEASE_CONTRACT_NUMBER
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseContractNumber.doesNotContain=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription equals to DEFAULT_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseDescription.equals=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription equals to UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.equals=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription not equals to DEFAULT_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.notEquals=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription not equals to UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseDescription.notEquals=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription in DEFAULT_LEASE_DESCRIPTION or UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "leaseDescription.in=" + DEFAULT_LEASE_DESCRIPTION + "," + UPDATED_LEASE_DESCRIPTION
        );

        // Get all the rouDepreciationPostingReportItemList where leaseDescription equals to UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.in=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseDescription.specified=true");

        // Get all the rouDepreciationPostingReportItemList where leaseDescription is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription contains DEFAULT_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseDescription.contains=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription contains UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.contains=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByLeaseDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription does not contain DEFAULT_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldNotBeFound("leaseDescription.doesNotContain=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the rouDepreciationPostingReportItemList where leaseDescription does not contain UPDATED_LEASE_DESCRIPTION
        defaultRouDepreciationPostingReportItemShouldBeFound("leaseDescription.doesNotContain=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode equals to DEFAULT_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldBeFound("fiscalMonthCode.equals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.equals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode not equals to DEFAULT_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.notEquals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode not equals to UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldBeFound("fiscalMonthCode.notEquals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode in DEFAULT_FISCAL_MONTH_CODE or UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "fiscalMonthCode.in=" + DEFAULT_FISCAL_MONTH_CODE + "," + UPDATED_FISCAL_MONTH_CODE
        );

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.in=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("fiscalMonthCode.specified=true");

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode contains DEFAULT_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldBeFound("fiscalMonthCode.contains=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode contains UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.contains=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByFiscalMonthCodeNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode does not contain DEFAULT_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldNotBeFound("fiscalMonthCode.doesNotContain=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the rouDepreciationPostingReportItemList where fiscalMonthCode does not contain UPDATED_FISCAL_MONTH_CODE
        defaultRouDepreciationPostingReportItemShouldBeFound("fiscalMonthCode.doesNotContain=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit equals to DEFAULT_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForCredit.equals=" + DEFAULT_ACCOUNT_FOR_CREDIT);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit equals to UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.equals=" + UPDATED_ACCOUNT_FOR_CREDIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit not equals to DEFAULT_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.notEquals=" + DEFAULT_ACCOUNT_FOR_CREDIT);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit not equals to UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForCredit.notEquals=" + UPDATED_ACCOUNT_FOR_CREDIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit in DEFAULT_ACCOUNT_FOR_CREDIT or UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "accountForCredit.in=" + DEFAULT_ACCOUNT_FOR_CREDIT + "," + UPDATED_ACCOUNT_FOR_CREDIT
        );

        // Get all the rouDepreciationPostingReportItemList where accountForCredit equals to UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.in=" + UPDATED_ACCOUNT_FOR_CREDIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForCredit.specified=true");

        // Get all the rouDepreciationPostingReportItemList where accountForCredit is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit contains DEFAULT_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForCredit.contains=" + DEFAULT_ACCOUNT_FOR_CREDIT);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit contains UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.contains=" + UPDATED_ACCOUNT_FOR_CREDIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForCreditNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit does not contain DEFAULT_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForCredit.doesNotContain=" + DEFAULT_ACCOUNT_FOR_CREDIT);

        // Get all the rouDepreciationPostingReportItemList where accountForCredit does not contain UPDATED_ACCOUNT_FOR_CREDIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForCredit.doesNotContain=" + UPDATED_ACCOUNT_FOR_CREDIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit equals to DEFAULT_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForDebit.equals=" + DEFAULT_ACCOUNT_FOR_DEBIT);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit equals to UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.equals=" + UPDATED_ACCOUNT_FOR_DEBIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit not equals to DEFAULT_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.notEquals=" + DEFAULT_ACCOUNT_FOR_DEBIT);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit not equals to UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForDebit.notEquals=" + UPDATED_ACCOUNT_FOR_DEBIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit in DEFAULT_ACCOUNT_FOR_DEBIT or UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "accountForDebit.in=" + DEFAULT_ACCOUNT_FOR_DEBIT + "," + UPDATED_ACCOUNT_FOR_DEBIT
        );

        // Get all the rouDepreciationPostingReportItemList where accountForDebit equals to UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.in=" + UPDATED_ACCOUNT_FOR_DEBIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForDebit.specified=true");

        // Get all the rouDepreciationPostingReportItemList where accountForDebit is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit contains DEFAULT_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForDebit.contains=" + DEFAULT_ACCOUNT_FOR_DEBIT);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit contains UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.contains=" + UPDATED_ACCOUNT_FOR_DEBIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByAccountForDebitNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit does not contain DEFAULT_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("accountForDebit.doesNotContain=" + DEFAULT_ACCOUNT_FOR_DEBIT);

        // Get all the rouDepreciationPostingReportItemList where accountForDebit does not contain UPDATED_ACCOUNT_FOR_DEBIT
        defaultRouDepreciationPostingReportItemShouldBeFound("accountForDebit.doesNotContain=" + UPDATED_ACCOUNT_FOR_DEBIT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is not null
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.specified=true");

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is null
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationPostingReportItemsByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationPostingReportItemList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationPostingReportItemShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationPostingReportItemShouldBeFound(String filter) throws Exception {
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].accountForCredit").value(hasItem(DEFAULT_ACCOUNT_FOR_CREDIT)))
            .andExpect(jsonPath("$.[*].accountForDebit").value(hasItem(DEFAULT_ACCOUNT_FOR_DEBIT)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))));

        // Check, that the count call also returns 1
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationPostingReportItemShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationPostingReportItem() throws Exception {
        // Get the rouDepreciationPostingReportItem
        restRouDepreciationPostingReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchRouDepreciationPostingReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationPostingReportItemRepository.saveAndFlush(rouDepreciationPostingReportItem);
        when(
            mockRouDepreciationPostingReportItemSearchRepository.search(
                "id:" + rouDepreciationPostingReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationPostingReportItem), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationPostingReportItem
        restRouDepreciationPostingReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationPostingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)))
            .andExpect(jsonPath("$.[*].accountForCredit").value(hasItem(DEFAULT_ACCOUNT_FOR_CREDIT)))
            .andExpect(jsonPath("$.[*].accountForDebit").value(hasItem(DEFAULT_ACCOUNT_FOR_DEBIT)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))));
    }
}
