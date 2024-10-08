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
import io.github.erp.domain.LeaseLiabilityReportItem;
import io.github.erp.repository.LeaseLiabilityReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityReportItemMapper;
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
 * Integration tests for the {@link LeaseLiabilityReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityReportItemResourceIT {

    private static final String DEFAULT_BOOKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIABILITY_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LIABILITY_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LIABILITY_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIABILITY_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LIABILITY_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INTEREST_PAYABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/lease-liability-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository;

    @Autowired
    private LeaseLiabilityReportItemMapper leaseLiabilityReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityReportItemSearchRepository mockLeaseLiabilityReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityReportItemMockMvc;

    private LeaseLiabilityReportItem leaseLiabilityReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityReportItem createEntity(EntityManager em) {
        LeaseLiabilityReportItem leaseLiabilityReportItem = new LeaseLiabilityReportItem()
            .bookingId(DEFAULT_BOOKING_ID)
            .leaseTitle(DEFAULT_LEASE_TITLE)
            .liabilityAccountNumber(DEFAULT_LIABILITY_ACCOUNT_NUMBER)
            .liabilityAmount(DEFAULT_LIABILITY_AMOUNT)
            .interestPayableAccountNumber(DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER)
            .interestPayableAmount(DEFAULT_INTEREST_PAYABLE_AMOUNT);
        return leaseLiabilityReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityReportItem createUpdatedEntity(EntityManager em) {
        LeaseLiabilityReportItem leaseLiabilityReportItem = new LeaseLiabilityReportItem()
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .liabilityAccountNumber(UPDATED_LIABILITY_ACCOUNT_NUMBER)
            .liabilityAmount(UPDATED_LIABILITY_AMOUNT)
            .interestPayableAccountNumber(UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER)
            .interestPayableAmount(UPDATED_INTEREST_PAYABLE_AMOUNT);
        return leaseLiabilityReportItem;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItems() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestPayableAccountNumber").value(hasItem(DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestPayableAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_AMOUNT))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityReportItem() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get the leaseLiabilityReportItem
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityReportItem.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID))
            .andExpect(jsonPath("$.leaseTitle").value(DEFAULT_LEASE_TITLE))
            .andExpect(jsonPath("$.liabilityAccountNumber").value(DEFAULT_LIABILITY_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.liabilityAmount").value(sameNumber(DEFAULT_LIABILITY_AMOUNT)))
            .andExpect(jsonPath("$.interestPayableAccountNumber").value(DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.interestPayableAmount").value(sameNumber(DEFAULT_INTEREST_PAYABLE_AMOUNT)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        Long id = leaseLiabilityReportItem.getId();

        defaultLeaseLiabilityReportItemShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId equals to DEFAULT_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityReportItemList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId not equals to DEFAULT_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.notEquals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityReportItemList where bookingId not equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.notEquals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the leaseLiabilityReportItemList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId is not null
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.specified=true");

        // Get all the leaseLiabilityReportItemList where bookingId is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId contains DEFAULT_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.contains=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityReportItemList where bookingId contains UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.contains=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByBookingIdNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where bookingId does not contain DEFAULT_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldNotBeFound("bookingId.doesNotContain=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityReportItemList where bookingId does not contain UPDATED_BOOKING_ID
        defaultLeaseLiabilityReportItemShouldBeFound("bookingId.doesNotContain=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle equals to DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.equals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityReportItemList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.equals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle not equals to DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.notEquals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityReportItemList where leaseTitle not equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.notEquals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle in DEFAULT_LEASE_TITLE or UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.in=" + DEFAULT_LEASE_TITLE + "," + UPDATED_LEASE_TITLE);

        // Get all the leaseLiabilityReportItemList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.in=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle is not null
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.specified=true");

        // Get all the leaseLiabilityReportItemList where leaseTitle is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle contains DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.contains=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityReportItemList where leaseTitle contains UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.contains=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLeaseTitleNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where leaseTitle does not contain DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldNotBeFound("leaseTitle.doesNotContain=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityReportItemList where leaseTitle does not contain UPDATED_LEASE_TITLE
        defaultLeaseLiabilityReportItemShouldBeFound("leaseTitle.doesNotContain=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber equals to DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAccountNumber.equals=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.equals=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber not equals to DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.notEquals=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber not equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAccountNumber.notEquals=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber in DEFAULT_LIABILITY_ACCOUNT_NUMBER or UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound(
            "liabilityAccountNumber.in=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER + "," + UPDATED_LIABILITY_ACCOUNT_NUMBER
        );

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.in=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber is not null
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAccountNumber.specified=true");

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber contains DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAccountNumber.contains=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber contains UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.contains=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber does not contain DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAccountNumber.doesNotContain=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where liabilityAccountNumber does not contain UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAccountNumber.doesNotContain=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount equals to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.equals=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.equals=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount not equals to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.notEquals=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount not equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.notEquals=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount in DEFAULT_LIABILITY_AMOUNT or UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.in=" + DEFAULT_LIABILITY_AMOUNT + "," + UPDATED_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.in=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is not null
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.specified=true");

        // Get all the leaseLiabilityReportItemList where liabilityAmount is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is greater than or equal to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.greaterThanOrEqual=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is greater than or equal to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.greaterThanOrEqual=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is less than or equal to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.lessThanOrEqual=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is less than or equal to SMALLER_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.lessThanOrEqual=" + SMALLER_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is less than DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.lessThan=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is less than UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.lessThan=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByLiabilityAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is greater than DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("liabilityAmount.greaterThan=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityReportItemList where liabilityAmount is greater than SMALLER_LIABILITY_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("liabilityAmount.greaterThan=" + SMALLER_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber equals to DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAccountNumber.equals=" + DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber equals to UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAccountNumber.equals=" + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber not equals to DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound(
            "interestPayableAccountNumber.notEquals=" + DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        );

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber not equals to UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAccountNumber.notEquals=" + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber in DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER or UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound(
            "interestPayableAccountNumber.in=" + DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER + "," + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        );

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber equals to UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAccountNumber.in=" + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber is not null
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAccountNumber.specified=true");

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAccountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber contains DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAccountNumber.contains=" + DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber contains UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAccountNumber.contains=" + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber does not contain DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldNotBeFound(
            "interestPayableAccountNumber.doesNotContain=" + DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER
        );

        // Get all the leaseLiabilityReportItemList where interestPayableAccountNumber does not contain UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        defaultLeaseLiabilityReportItemShouldBeFound(
            "interestPayableAccountNumber.doesNotContain=" + UPDATED_INTEREST_PAYABLE_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount equals to DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.equals=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount equals to UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.equals=" + UPDATED_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount not equals to DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.notEquals=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount not equals to UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.notEquals=" + UPDATED_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount in DEFAULT_INTEREST_PAYABLE_AMOUNT or UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound(
            "interestPayableAmount.in=" + DEFAULT_INTEREST_PAYABLE_AMOUNT + "," + UPDATED_INTEREST_PAYABLE_AMOUNT
        );

        // Get all the leaseLiabilityReportItemList where interestPayableAmount equals to UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.in=" + UPDATED_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is not null
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.specified=true");

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is null
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is greater than or equal to DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is greater than or equal to UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.greaterThanOrEqual=" + UPDATED_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is less than or equal to DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.lessThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is less than or equal to SMALLER_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.lessThanOrEqual=" + SMALLER_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is less than DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.lessThan=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is less than UPDATED_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.lessThan=" + UPDATED_INTEREST_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityReportItemsByInterestPayableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is greater than DEFAULT_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldNotBeFound("interestPayableAmount.greaterThan=" + DEFAULT_INTEREST_PAYABLE_AMOUNT);

        // Get all the leaseLiabilityReportItemList where interestPayableAmount is greater than SMALLER_INTEREST_PAYABLE_AMOUNT
        defaultLeaseLiabilityReportItemShouldBeFound("interestPayableAmount.greaterThan=" + SMALLER_INTEREST_PAYABLE_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityReportItemShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestPayableAccountNumber").value(hasItem(DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestPayableAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_AMOUNT))));

        // Check, that the count call also returns 1
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityReportItemShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityReportItem() throws Exception {
        // Get the leaseLiabilityReportItem
        restLeaseLiabilityReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityReportItemRepository.saveAndFlush(leaseLiabilityReportItem);
        when(mockLeaseLiabilityReportItemSearchRepository.search("id:" + leaseLiabilityReportItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityReportItem), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityReportItem
        restLeaseLiabilityReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestPayableAccountNumber").value(hasItem(DEFAULT_INTEREST_PAYABLE_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestPayableAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_AMOUNT))));
    }
}
