package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.TransactionAccountReportItem;
import io.github.erp.repository.TransactionAccountReportItemRepository;
import io.github.erp.repository.search.TransactionAccountReportItemSearchRepository;
import io.github.erp.service.criteria.TransactionAccountReportItemCriteria;
import io.github.erp.service.dto.TransactionAccountReportItemDTO;
import io.github.erp.service.mapper.TransactionAccountReportItemMapper;
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
 * Integration tests for the {@link TransactionAccountReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountReportItemResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNT_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/transaction-account-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountReportItemRepository transactionAccountReportItemRepository;

    @Autowired
    private TransactionAccountReportItemMapper transactionAccountReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountReportItemSearchRepository mockTransactionAccountReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountReportItemMockMvc;

    private TransactionAccountReportItem transactionAccountReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountReportItem createEntity(EntityManager em) {
        TransactionAccountReportItem transactionAccountReportItem = new TransactionAccountReportItem()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE);
        return transactionAccountReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountReportItem createUpdatedEntity(EntityManager em) {
        TransactionAccountReportItem transactionAccountReportItem = new TransactionAccountReportItem()
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountBalance(UPDATED_ACCOUNT_BALANCE);
        return transactionAccountReportItem;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItems() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));
    }

    @Test
    @Transactional
    void getTransactionAccountReportItem() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get the transactionAccountReportItem
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountReportItem.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.accountBalance").value(sameNumber(DEFAULT_ACCOUNT_BALANCE)));
    }

    @Test
    @Transactional
    void getTransactionAccountReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        Long id = transactionAccountReportItem.getId();

        defaultTransactionAccountReportItemShouldBeFound("id.equals=" + id);
        defaultTransactionAccountReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountReportItemList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountReportItemList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the transactionAccountReportItemList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName is not null
        defaultTransactionAccountReportItemShouldBeFound("accountName.specified=true");

        // Get all the transactionAccountReportItemList where accountName is null
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountReportItemList where accountName contains UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountReportItemList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultTransactionAccountReportItemShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountReportItemList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the transactionAccountReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber is not null
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.specified=true");

        // Get all the transactionAccountReportItemList where accountNumber is null
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountReportItemList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountReportItemList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountReportItemShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance not equals to DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.notEquals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance not equals to UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.notEquals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance is not null
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.specified=true");

        // Get all the transactionAccountReportItemList where accountBalance is null
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance is greater than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance is greater than or equal to UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance is less than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance is less than or equal to SMALLER_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance is less than DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.lessThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance is less than UPDATED_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.lessThan=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountReportItemsByAccountBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);

        // Get all the transactionAccountReportItemList where accountBalance is greater than DEFAULT_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldNotBeFound("accountBalance.greaterThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the transactionAccountReportItemList where accountBalance is greater than SMALLER_ACCOUNT_BALANCE
        defaultTransactionAccountReportItemShouldBeFound("accountBalance.greaterThan=" + SMALLER_ACCOUNT_BALANCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountReportItemShouldBeFound(String filter) throws Exception {
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));

        // Check, that the count call also returns 1
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountReportItemShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountReportItem() throws Exception {
        // Get the transactionAccountReportItem
        restTransactionAccountReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchTransactionAccountReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountReportItemRepository.saveAndFlush(transactionAccountReportItem);
        when(mockTransactionAccountReportItemSearchRepository.search("id:" + transactionAccountReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountReportItem), PageRequest.of(0, 1), 1));

        // Search the transactionAccountReportItem
        restTransactionAccountReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE))));
    }
}
