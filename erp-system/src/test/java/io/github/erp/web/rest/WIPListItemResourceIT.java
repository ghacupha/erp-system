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
import io.github.erp.domain.WIPListItem;
import io.github.erp.repository.WIPListItemRepository;
import io.github.erp.repository.search.WIPListItemSearchRepository;
import io.github.erp.service.criteria.WIPListItemCriteria;
import io.github.erp.service.dto.WIPListItemDTO;
import io.github.erp.service.mapper.WIPListItemMapper;
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
 * Integration tests for the {@link WIPListItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WIPListItemResourceIT {

    private static final String DEFAULT_SEQUENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INSTALMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSTALMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INSTALMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INSTALMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSTALMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INSTALMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_SETTLEMENT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_SETTLEMENT_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SETTLEMENT_TRANSACTION = "AAAAAAAAAA";
    private static final String UPDATED_SETTLEMENT_TRANSACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SETTLEMENT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SETTLEMENT_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SETTLEMENT_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_WORK_PROJECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wip-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/wip-list-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WIPListItemRepository wIPListItemRepository;

    @Autowired
    private WIPListItemMapper wIPListItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WIPListItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private WIPListItemSearchRepository mockWIPListItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWIPListItemMockMvc;

    private WIPListItem wIPListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPListItem createEntity(EntityManager em) {
        WIPListItem wIPListItem = new WIPListItem()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .instalmentDate(DEFAULT_INSTALMENT_DATE)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .settlementCurrency(DEFAULT_SETTLEMENT_CURRENCY)
            .outletCode(DEFAULT_OUTLET_CODE)
            .settlementTransaction(DEFAULT_SETTLEMENT_TRANSACTION)
            .settlementTransactionDate(DEFAULT_SETTLEMENT_TRANSACTION_DATE)
            .dealerName(DEFAULT_DEALER_NAME)
            .workProject(DEFAULT_WORK_PROJECT);
        return wIPListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WIPListItem createUpdatedEntity(EntityManager em) {
        WIPListItem wIPListItem = new WIPListItem()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .settlementCurrency(UPDATED_SETTLEMENT_CURRENCY)
            .outletCode(UPDATED_OUTLET_CODE)
            .settlementTransaction(UPDATED_SETTLEMENT_TRANSACTION)
            .settlementTransactionDate(UPDATED_SETTLEMENT_TRANSACTION_DATE)
            .dealerName(UPDATED_DEALER_NAME)
            .workProject(UPDATED_WORK_PROJECT);
        return wIPListItem;
    }

    @BeforeEach
    public void initTest() {
        wIPListItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllWIPListItems() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY)))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].settlementTransaction").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION)))
            .andExpect(jsonPath("$.[*].settlementTransactionDate").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));
    }

    @Test
    @Transactional
    void getWIPListItem() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get the wIPListItem
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, wIPListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wIPListItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.instalmentDate").value(DEFAULT_INSTALMENT_DATE.toString()))
            .andExpect(jsonPath("$.instalmentAmount").value(sameNumber(DEFAULT_INSTALMENT_AMOUNT)))
            .andExpect(jsonPath("$.settlementCurrency").value(DEFAULT_SETTLEMENT_CURRENCY))
            .andExpect(jsonPath("$.outletCode").value(DEFAULT_OUTLET_CODE))
            .andExpect(jsonPath("$.settlementTransaction").value(DEFAULT_SETTLEMENT_TRANSACTION))
            .andExpect(jsonPath("$.settlementTransactionDate").value(DEFAULT_SETTLEMENT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.workProject").value(DEFAULT_WORK_PROJECT));
    }

    @Test
    @Transactional
    void getWIPListItemsByIdFiltering() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        Long id = wIPListItem.getId();

        defaultWIPListItemShouldBeFound("id.equals=" + id);
        defaultWIPListItemShouldNotBeFound("id.notEquals=" + id);

        defaultWIPListItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWIPListItemShouldNotBeFound("id.greaterThan=" + id);

        defaultWIPListItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWIPListItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultWIPListItemShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the wIPListItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultWIPListItemShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the wIPListItemList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the wIPListItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber is not null
        defaultWIPListItemShouldBeFound("sequenceNumber.specified=true");

        // Get all the wIPListItemList where sequenceNumber is null
        defaultWIPListItemShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber contains DEFAULT_SEQUENCE_NUMBER
        defaultWIPListItemShouldBeFound("sequenceNumber.contains=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the wIPListItemList where sequenceNumber contains UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldNotBeFound("sequenceNumber.contains=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySequenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where sequenceNumber does not contain DEFAULT_SEQUENCE_NUMBER
        defaultWIPListItemShouldNotBeFound("sequenceNumber.doesNotContain=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the wIPListItemList where sequenceNumber does not contain UPDATED_SEQUENCE_NUMBER
        defaultWIPListItemShouldBeFound("sequenceNumber.doesNotContain=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars equals to DEFAULT_PARTICULARS
        defaultWIPListItemShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the wIPListItemList where particulars equals to UPDATED_PARTICULARS
        defaultWIPListItemShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars not equals to DEFAULT_PARTICULARS
        defaultWIPListItemShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the wIPListItemList where particulars not equals to UPDATED_PARTICULARS
        defaultWIPListItemShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultWIPListItemShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the wIPListItemList where particulars equals to UPDATED_PARTICULARS
        defaultWIPListItemShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars is not null
        defaultWIPListItemShouldBeFound("particulars.specified=true");

        // Get all the wIPListItemList where particulars is null
        defaultWIPListItemShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars contains DEFAULT_PARTICULARS
        defaultWIPListItemShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the wIPListItemList where particulars contains UPDATED_PARTICULARS
        defaultWIPListItemShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where particulars does not contain DEFAULT_PARTICULARS
        defaultWIPListItemShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the wIPListItemList where particulars does not contain UPDATED_PARTICULARS
        defaultWIPListItemShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate equals to DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.equals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.equals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate not equals to DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.notEquals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate not equals to UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.notEquals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate in DEFAULT_INSTALMENT_DATE or UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.in=" + DEFAULT_INSTALMENT_DATE + "," + UPDATED_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.in=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate is not null
        defaultWIPListItemShouldBeFound("instalmentDate.specified=true");

        // Get all the wIPListItemList where instalmentDate is null
        defaultWIPListItemShouldNotBeFound("instalmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate is greater than or equal to DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.greaterThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate is greater than or equal to UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.greaterThanOrEqual=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate is less than or equal to DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.lessThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate is less than or equal to SMALLER_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.lessThanOrEqual=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate is less than DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.lessThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate is less than UPDATED_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.lessThan=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentDate is greater than DEFAULT_INSTALMENT_DATE
        defaultWIPListItemShouldNotBeFound("instalmentDate.greaterThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the wIPListItemList where instalmentDate is greater than SMALLER_INSTALMENT_DATE
        defaultWIPListItemShouldBeFound("instalmentDate.greaterThan=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.equals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.equals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount not equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.notEquals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount not equals to UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.notEquals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount in DEFAULT_INSTALMENT_AMOUNT or UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.in=" + DEFAULT_INSTALMENT_AMOUNT + "," + UPDATED_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.in=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount is not null
        defaultWIPListItemShouldBeFound("instalmentAmount.specified=true");

        // Get all the wIPListItemList where instalmentAmount is null
        defaultWIPListItemShouldNotBeFound("instalmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount is greater than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.greaterThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount is greater than or equal to UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.greaterThanOrEqual=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount is less than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.lessThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount is less than or equal to SMALLER_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.lessThanOrEqual=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount is less than DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.lessThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount is less than UPDATED_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.lessThan=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByInstalmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where instalmentAmount is greater than DEFAULT_INSTALMENT_AMOUNT
        defaultWIPListItemShouldNotBeFound("instalmentAmount.greaterThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the wIPListItemList where instalmentAmount is greater than SMALLER_INSTALMENT_AMOUNT
        defaultWIPListItemShouldBeFound("instalmentAmount.greaterThan=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency equals to DEFAULT_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldBeFound("settlementCurrency.equals=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the wIPListItemList where settlementCurrency equals to UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldNotBeFound("settlementCurrency.equals=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency not equals to DEFAULT_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldNotBeFound("settlementCurrency.notEquals=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the wIPListItemList where settlementCurrency not equals to UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldBeFound("settlementCurrency.notEquals=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency in DEFAULT_SETTLEMENT_CURRENCY or UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldBeFound("settlementCurrency.in=" + DEFAULT_SETTLEMENT_CURRENCY + "," + UPDATED_SETTLEMENT_CURRENCY);

        // Get all the wIPListItemList where settlementCurrency equals to UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldNotBeFound("settlementCurrency.in=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency is not null
        defaultWIPListItemShouldBeFound("settlementCurrency.specified=true");

        // Get all the wIPListItemList where settlementCurrency is null
        defaultWIPListItemShouldNotBeFound("settlementCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency contains DEFAULT_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldBeFound("settlementCurrency.contains=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the wIPListItemList where settlementCurrency contains UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldNotBeFound("settlementCurrency.contains=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementCurrency does not contain DEFAULT_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldNotBeFound("settlementCurrency.doesNotContain=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the wIPListItemList where settlementCurrency does not contain UPDATED_SETTLEMENT_CURRENCY
        defaultWIPListItemShouldBeFound("settlementCurrency.doesNotContain=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode equals to DEFAULT_OUTLET_CODE
        defaultWIPListItemShouldBeFound("outletCode.equals=" + DEFAULT_OUTLET_CODE);

        // Get all the wIPListItemList where outletCode equals to UPDATED_OUTLET_CODE
        defaultWIPListItemShouldNotBeFound("outletCode.equals=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode not equals to DEFAULT_OUTLET_CODE
        defaultWIPListItemShouldNotBeFound("outletCode.notEquals=" + DEFAULT_OUTLET_CODE);

        // Get all the wIPListItemList where outletCode not equals to UPDATED_OUTLET_CODE
        defaultWIPListItemShouldBeFound("outletCode.notEquals=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode in DEFAULT_OUTLET_CODE or UPDATED_OUTLET_CODE
        defaultWIPListItemShouldBeFound("outletCode.in=" + DEFAULT_OUTLET_CODE + "," + UPDATED_OUTLET_CODE);

        // Get all the wIPListItemList where outletCode equals to UPDATED_OUTLET_CODE
        defaultWIPListItemShouldNotBeFound("outletCode.in=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode is not null
        defaultWIPListItemShouldBeFound("outletCode.specified=true");

        // Get all the wIPListItemList where outletCode is null
        defaultWIPListItemShouldNotBeFound("outletCode.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode contains DEFAULT_OUTLET_CODE
        defaultWIPListItemShouldBeFound("outletCode.contains=" + DEFAULT_OUTLET_CODE);

        // Get all the wIPListItemList where outletCode contains UPDATED_OUTLET_CODE
        defaultWIPListItemShouldNotBeFound("outletCode.contains=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where outletCode does not contain DEFAULT_OUTLET_CODE
        defaultWIPListItemShouldNotBeFound("outletCode.doesNotContain=" + DEFAULT_OUTLET_CODE);

        // Get all the wIPListItemList where outletCode does not contain UPDATED_OUTLET_CODE
        defaultWIPListItemShouldBeFound("outletCode.doesNotContain=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction equals to DEFAULT_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldBeFound("settlementTransaction.equals=" + DEFAULT_SETTLEMENT_TRANSACTION);

        // Get all the wIPListItemList where settlementTransaction equals to UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldNotBeFound("settlementTransaction.equals=" + UPDATED_SETTLEMENT_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction not equals to DEFAULT_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldNotBeFound("settlementTransaction.notEquals=" + DEFAULT_SETTLEMENT_TRANSACTION);

        // Get all the wIPListItemList where settlementTransaction not equals to UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldBeFound("settlementTransaction.notEquals=" + UPDATED_SETTLEMENT_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction in DEFAULT_SETTLEMENT_TRANSACTION or UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldBeFound(
            "settlementTransaction.in=" + DEFAULT_SETTLEMENT_TRANSACTION + "," + UPDATED_SETTLEMENT_TRANSACTION
        );

        // Get all the wIPListItemList where settlementTransaction equals to UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldNotBeFound("settlementTransaction.in=" + UPDATED_SETTLEMENT_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction is not null
        defaultWIPListItemShouldBeFound("settlementTransaction.specified=true");

        // Get all the wIPListItemList where settlementTransaction is null
        defaultWIPListItemShouldNotBeFound("settlementTransaction.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction contains DEFAULT_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldBeFound("settlementTransaction.contains=" + DEFAULT_SETTLEMENT_TRANSACTION);

        // Get all the wIPListItemList where settlementTransaction contains UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldNotBeFound("settlementTransaction.contains=" + UPDATED_SETTLEMENT_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransaction does not contain DEFAULT_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldNotBeFound("settlementTransaction.doesNotContain=" + DEFAULT_SETTLEMENT_TRANSACTION);

        // Get all the wIPListItemList where settlementTransaction does not contain UPDATED_SETTLEMENT_TRANSACTION
        defaultWIPListItemShouldBeFound("settlementTransaction.doesNotContain=" + UPDATED_SETTLEMENT_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate equals to DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.equals=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate equals to UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.equals=" + UPDATED_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate not equals to DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.notEquals=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate not equals to UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.notEquals=" + UPDATED_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate in DEFAULT_SETTLEMENT_TRANSACTION_DATE or UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound(
            "settlementTransactionDate.in=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE + "," + UPDATED_SETTLEMENT_TRANSACTION_DATE
        );

        // Get all the wIPListItemList where settlementTransactionDate equals to UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.in=" + UPDATED_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate is not null
        defaultWIPListItemShouldBeFound("settlementTransactionDate.specified=true");

        // Get all the wIPListItemList where settlementTransactionDate is null
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate is greater than or equal to DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.greaterThanOrEqual=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate is greater than or equal to UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.greaterThanOrEqual=" + UPDATED_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate is less than or equal to DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.lessThanOrEqual=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate is less than or equal to SMALLER_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.lessThanOrEqual=" + SMALLER_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate is less than DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.lessThan=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate is less than UPDATED_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.lessThan=" + UPDATED_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsBySettlementTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where settlementTransactionDate is greater than DEFAULT_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldNotBeFound("settlementTransactionDate.greaterThan=" + DEFAULT_SETTLEMENT_TRANSACTION_DATE);

        // Get all the wIPListItemList where settlementTransactionDate is greater than SMALLER_SETTLEMENT_TRANSACTION_DATE
        defaultWIPListItemShouldBeFound("settlementTransactionDate.greaterThan=" + SMALLER_SETTLEMENT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName equals to DEFAULT_DEALER_NAME
        defaultWIPListItemShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the wIPListItemList where dealerName equals to UPDATED_DEALER_NAME
        defaultWIPListItemShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultWIPListItemShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the wIPListItemList where dealerName not equals to UPDATED_DEALER_NAME
        defaultWIPListItemShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultWIPListItemShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the wIPListItemList where dealerName equals to UPDATED_DEALER_NAME
        defaultWIPListItemShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName is not null
        defaultWIPListItemShouldBeFound("dealerName.specified=true");

        // Get all the wIPListItemList where dealerName is null
        defaultWIPListItemShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName contains DEFAULT_DEALER_NAME
        defaultWIPListItemShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the wIPListItemList where dealerName contains UPDATED_DEALER_NAME
        defaultWIPListItemShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultWIPListItemShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the wIPListItemList where dealerName does not contain UPDATED_DEALER_NAME
        defaultWIPListItemShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject equals to DEFAULT_WORK_PROJECT
        defaultWIPListItemShouldBeFound("workProject.equals=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPListItemList where workProject equals to UPDATED_WORK_PROJECT
        defaultWIPListItemShouldNotBeFound("workProject.equals=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject not equals to DEFAULT_WORK_PROJECT
        defaultWIPListItemShouldNotBeFound("workProject.notEquals=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPListItemList where workProject not equals to UPDATED_WORK_PROJECT
        defaultWIPListItemShouldBeFound("workProject.notEquals=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectIsInShouldWork() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject in DEFAULT_WORK_PROJECT or UPDATED_WORK_PROJECT
        defaultWIPListItemShouldBeFound("workProject.in=" + DEFAULT_WORK_PROJECT + "," + UPDATED_WORK_PROJECT);

        // Get all the wIPListItemList where workProject equals to UPDATED_WORK_PROJECT
        defaultWIPListItemShouldNotBeFound("workProject.in=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject is not null
        defaultWIPListItemShouldBeFound("workProject.specified=true");

        // Get all the wIPListItemList where workProject is null
        defaultWIPListItemShouldNotBeFound("workProject.specified=false");
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject contains DEFAULT_WORK_PROJECT
        defaultWIPListItemShouldBeFound("workProject.contains=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPListItemList where workProject contains UPDATED_WORK_PROJECT
        defaultWIPListItemShouldNotBeFound("workProject.contains=" + UPDATED_WORK_PROJECT);
    }

    @Test
    @Transactional
    void getAllWIPListItemsByWorkProjectNotContainsSomething() throws Exception {
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);

        // Get all the wIPListItemList where workProject does not contain DEFAULT_WORK_PROJECT
        defaultWIPListItemShouldNotBeFound("workProject.doesNotContain=" + DEFAULT_WORK_PROJECT);

        // Get all the wIPListItemList where workProject does not contain UPDATED_WORK_PROJECT
        defaultWIPListItemShouldBeFound("workProject.doesNotContain=" + UPDATED_WORK_PROJECT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWIPListItemShouldBeFound(String filter) throws Exception {
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY)))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].settlementTransaction").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION)))
            .andExpect(jsonPath("$.[*].settlementTransactionDate").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));

        // Check, that the count call also returns 1
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWIPListItemShouldNotBeFound(String filter) throws Exception {
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWIPListItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWIPListItem() throws Exception {
        // Get the wIPListItem
        restWIPListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchWIPListItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        wIPListItemRepository.saveAndFlush(wIPListItem);
        when(mockWIPListItemSearchRepository.search("id:" + wIPListItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(wIPListItem), PageRequest.of(0, 1), 1));

        // Search the wIPListItem
        restWIPListItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + wIPListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wIPListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY)))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].settlementTransaction").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION)))
            .andExpect(jsonPath("$.[*].settlementTransactionDate").value(hasItem(DEFAULT_SETTLEMENT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].workProject").value(hasItem(DEFAULT_WORK_PROJECT)));
    }
}
