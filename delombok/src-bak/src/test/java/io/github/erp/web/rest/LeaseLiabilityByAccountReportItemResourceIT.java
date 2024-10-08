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
import io.github.erp.domain.LeaseLiabilityByAccountReportItem;
import io.github.erp.repository.LeaseLiabilityByAccountReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportItemMapper;
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
 * Integration tests for the {@link LeaseLiabilityByAccountReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityByAccountReportItemResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNT_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/lease-liability-by-account-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-by-account-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository;

    @Autowired
    private LeaseLiabilityByAccountReportItemMapper leaseLiabilityByAccountReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityByAccountReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityByAccountReportItemSearchRepository mockLeaseLiabilityByAccountReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityByAccountReportItemMockMvc;

    private LeaseLiabilityByAccountReportItem leaseLiabilityByAccountReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityByAccountReportItem createEntity(EntityManager em) {
        LeaseLiabilityByAccountReportItem leaseLiabilityByAccountReportItem = new LeaseLiabilityByAccountReportItem()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE);
        return leaseLiabilityByAccountReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityByAccountReportItem createUpdatedEntity(EntityManager em) {
        LeaseLiabilityByAccountReportItem leaseLiabilityByAccountReportItem = new LeaseLiabilityByAccountReportItem()
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .accountBalance(UPDATED_ACCOUNT_BALANCE);
        return leaseLiabilityByAccountReportItem;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityByAccountReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItems() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityByAccountReportItem() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get the leaseLiabilityByAccountReportItem
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityByAccountReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityByAccountReportItem.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.accountBalance").value(sameNumber(DEFAULT_ACCOUNT_BALANCE)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityByAccountReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        Long id = leaseLiabilityByAccountReportItem.getId();

        defaultLeaseLiabilityByAccountReportItemShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityByAccountReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityByAccountReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the leaseLiabilityByAccountReportItemList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the leaseLiabilityByAccountReportItemList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the leaseLiabilityByAccountReportItemList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName is not null
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.specified=true");

        // Get all the leaseLiabilityByAccountReportItemList where accountName is null
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the leaseLiabilityByAccountReportItemList where accountName contains UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the leaseLiabilityByAccountReportItemList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber is not null
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.specified=true");

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber is null
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityByAccountReportItemList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description equals to DEFAULT_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseLiabilityByAccountReportItemList where description equals to UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description not equals to DEFAULT_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseLiabilityByAccountReportItemList where description not equals to UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leaseLiabilityByAccountReportItemList where description equals to UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description is not null
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.specified=true");

        // Get all the leaseLiabilityByAccountReportItemList where description is null
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description contains DEFAULT_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leaseLiabilityByAccountReportItemList where description contains UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where description does not contain DEFAULT_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leaseLiabilityByAccountReportItemList where description does not contain UPDATED_DESCRIPTION
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance not equals to DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.notEquals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance not equals to UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.notEquals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound(
            "accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE
        );

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is not null
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.specified=true");

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is null
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is greater than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is greater than or equal to UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is less than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is less than or equal to SMALLER_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is less than DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.lessThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is less than UPDATED_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.lessThan=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityByAccountReportItemsByAccountBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is greater than DEFAULT_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldNotBeFound("accountBalance.greaterThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the leaseLiabilityByAccountReportItemList where accountBalance is greater than SMALLER_ACCOUNT_BALANCE
        defaultLeaseLiabilityByAccountReportItemShouldBeFound("accountBalance.greaterThan=" + SMALLER_ACCOUNT_BALANCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityByAccountReportItemShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityByAccountReportItemShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityByAccountReportItem() throws Exception {
        // Get the leaseLiabilityByAccountReportItem
        restLeaseLiabilityByAccountReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityByAccountReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityByAccountReportItemRepository.saveAndFlush(leaseLiabilityByAccountReportItem);
        when(
            mockLeaseLiabilityByAccountReportItemSearchRepository.search(
                "id:" + leaseLiabilityByAccountReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityByAccountReportItem), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityByAccountReportItem
        restLeaseLiabilityByAccountReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityByAccountReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityByAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));
    }
}
