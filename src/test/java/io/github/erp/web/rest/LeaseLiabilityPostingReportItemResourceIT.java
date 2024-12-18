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
import io.github.erp.domain.LeaseLiabilityPostingReportItem;
import io.github.erp.repository.LeaseLiabilityPostingReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityPostingReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityPostingReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityPostingReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityPostingReportItemMapper;
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
 * Integration tests for the {@link LeaseLiabilityPostingReportItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityPostingReportItemResourceIT {

    private static final String DEFAULT_BOOKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_POSTING = "AAAAAAAAAA";
    private static final String UPDATED_POSTING = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_POSTING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_POSTING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_POSTING_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/lease-liability-posting-report-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-posting-report-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository;

    @Autowired
    private LeaseLiabilityPostingReportItemMapper leaseLiabilityPostingReportItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityPostingReportItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityPostingReportItemSearchRepository mockLeaseLiabilityPostingReportItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityPostingReportItemMockMvc;

    private LeaseLiabilityPostingReportItem leaseLiabilityPostingReportItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityPostingReportItem createEntity(EntityManager em) {
        LeaseLiabilityPostingReportItem leaseLiabilityPostingReportItem = new LeaseLiabilityPostingReportItem()
            .bookingId(DEFAULT_BOOKING_ID)
            .leaseTitle(DEFAULT_LEASE_TITLE)
            .leaseDescription(DEFAULT_LEASE_DESCRIPTION)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .posting(DEFAULT_POSTING)
            .postingAmount(DEFAULT_POSTING_AMOUNT);
        return leaseLiabilityPostingReportItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityPostingReportItem createUpdatedEntity(EntityManager em) {
        LeaseLiabilityPostingReportItem leaseLiabilityPostingReportItem = new LeaseLiabilityPostingReportItem()
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .leaseDescription(UPDATED_LEASE_DESCRIPTION)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .posting(UPDATED_POSTING)
            .postingAmount(UPDATED_POSTING_AMOUNT);
        return leaseLiabilityPostingReportItem;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityPostingReportItem = createEntity(em);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItems() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].posting").value(hasItem(DEFAULT_POSTING)))
            .andExpect(jsonPath("$.[*].postingAmount").value(hasItem(sameNumber(DEFAULT_POSTING_AMOUNT))));
    }

    @Test
    @Transactional
    void getLeaseLiabilityPostingReportItem() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get the leaseLiabilityPostingReportItem
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityPostingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityPostingReportItem.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID))
            .andExpect(jsonPath("$.leaseTitle").value(DEFAULT_LEASE_TITLE))
            .andExpect(jsonPath("$.leaseDescription").value(DEFAULT_LEASE_DESCRIPTION))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.posting").value(DEFAULT_POSTING))
            .andExpect(jsonPath("$.postingAmount").value(sameNumber(DEFAULT_POSTING_AMOUNT)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityPostingReportItemsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        Long id = leaseLiabilityPostingReportItem.getId();

        defaultLeaseLiabilityPostingReportItemShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityPostingReportItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityPostingReportItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId equals to DEFAULT_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityPostingReportItemList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId not equals to DEFAULT_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.notEquals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityPostingReportItemList where bookingId not equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.notEquals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the leaseLiabilityPostingReportItemList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where bookingId is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId contains DEFAULT_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.contains=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityPostingReportItemList where bookingId contains UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.contains=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByBookingIdNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where bookingId does not contain DEFAULT_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("bookingId.doesNotContain=" + DEFAULT_BOOKING_ID);

        // Get all the leaseLiabilityPostingReportItemList where bookingId does not contain UPDATED_BOOKING_ID
        defaultLeaseLiabilityPostingReportItemShouldBeFound("bookingId.doesNotContain=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle equals to DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.equals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.equals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle not equals to DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.notEquals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle not equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.notEquals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle in DEFAULT_LEASE_TITLE or UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.in=" + DEFAULT_LEASE_TITLE + "," + UPDATED_LEASE_TITLE);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.in=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle contains DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.contains=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle contains UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.contains=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseTitleNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle does not contain DEFAULT_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseTitle.doesNotContain=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseLiabilityPostingReportItemList where leaseTitle does not contain UPDATED_LEASE_TITLE
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseTitle.doesNotContain=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription equals to DEFAULT_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseDescription.equals=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription equals to UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.equals=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription not equals to DEFAULT_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.notEquals=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription not equals to UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseDescription.notEquals=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription in DEFAULT_LEASE_DESCRIPTION or UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldBeFound(
            "leaseDescription.in=" + DEFAULT_LEASE_DESCRIPTION + "," + UPDATED_LEASE_DESCRIPTION
        );

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription equals to UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.in=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseDescription.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription contains DEFAULT_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseDescription.contains=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription contains UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.contains=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByLeaseDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription does not contain DEFAULT_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("leaseDescription.doesNotContain=" + DEFAULT_LEASE_DESCRIPTION);

        // Get all the leaseLiabilityPostingReportItemList where leaseDescription does not contain UPDATED_LEASE_DESCRIPTION
        defaultLeaseLiabilityPostingReportItemShouldBeFound("leaseDescription.doesNotContain=" + UPDATED_LEASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where accountNumber is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the leaseLiabilityPostingReportItemList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultLeaseLiabilityPostingReportItemShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting equals to DEFAULT_POSTING
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.equals=" + DEFAULT_POSTING);

        // Get all the leaseLiabilityPostingReportItemList where posting equals to UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.equals=" + UPDATED_POSTING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting not equals to DEFAULT_POSTING
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.notEquals=" + DEFAULT_POSTING);

        // Get all the leaseLiabilityPostingReportItemList where posting not equals to UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.notEquals=" + UPDATED_POSTING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting in DEFAULT_POSTING or UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.in=" + DEFAULT_POSTING + "," + UPDATED_POSTING);

        // Get all the leaseLiabilityPostingReportItemList where posting equals to UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.in=" + UPDATED_POSTING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where posting is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting contains DEFAULT_POSTING
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.contains=" + DEFAULT_POSTING);

        // Get all the leaseLiabilityPostingReportItemList where posting contains UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.contains=" + UPDATED_POSTING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where posting does not contain DEFAULT_POSTING
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("posting.doesNotContain=" + DEFAULT_POSTING);

        // Get all the leaseLiabilityPostingReportItemList where posting does not contain UPDATED_POSTING
        defaultLeaseLiabilityPostingReportItemShouldBeFound("posting.doesNotContain=" + UPDATED_POSTING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount equals to DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.equals=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount equals to UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.equals=" + UPDATED_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount not equals to DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.notEquals=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount not equals to UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.notEquals=" + UPDATED_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount in DEFAULT_POSTING_AMOUNT or UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.in=" + DEFAULT_POSTING_AMOUNT + "," + UPDATED_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount equals to UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.in=" + UPDATED_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is not null
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.specified=true");

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is null
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is greater than or equal to DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.greaterThanOrEqual=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is greater than or equal to UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.greaterThanOrEqual=" + UPDATED_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is less than or equal to DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.lessThanOrEqual=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is less than or equal to SMALLER_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.lessThanOrEqual=" + SMALLER_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is less than DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.lessThan=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is less than UPDATED_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.lessThan=" + UPDATED_POSTING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityPostingReportItemsByPostingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is greater than DEFAULT_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldNotBeFound("postingAmount.greaterThan=" + DEFAULT_POSTING_AMOUNT);

        // Get all the leaseLiabilityPostingReportItemList where postingAmount is greater than SMALLER_POSTING_AMOUNT
        defaultLeaseLiabilityPostingReportItemShouldBeFound("postingAmount.greaterThan=" + SMALLER_POSTING_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityPostingReportItemShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].posting").value(hasItem(DEFAULT_POSTING)))
            .andExpect(jsonPath("$.[*].postingAmount").value(hasItem(sameNumber(DEFAULT_POSTING_AMOUNT))));

        // Check, that the count call also returns 1
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityPostingReportItemShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityPostingReportItem() throws Exception {
        // Get the leaseLiabilityPostingReportItem
        restLeaseLiabilityPostingReportItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityPostingReportItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityPostingReportItemRepository.saveAndFlush(leaseLiabilityPostingReportItem);
        when(
            mockLeaseLiabilityPostingReportItemSearchRepository.search(
                "id:" + leaseLiabilityPostingReportItem.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityPostingReportItem), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityPostingReportItem
        restLeaseLiabilityPostingReportItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityPostingReportItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityPostingReportItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].leaseDescription").value(hasItem(DEFAULT_LEASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].posting").value(hasItem(DEFAULT_POSTING)))
            .andExpect(jsonPath("$.[*].postingAmount").value(hasItem(sameNumber(DEFAULT_POSTING_AMOUNT))));
    }
}
