package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.LeaseContract;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.domain.LeaseModelMetadata;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleItemService;
import io.github.erp.service.criteria.LeaseLiabilityScheduleItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link LeaseLiabilityScheduleItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseLiabilityScheduleItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final Boolean DEFAULT_PERIOD_INCLUDED = false;
    private static final Boolean UPDATED_PERIOD_INCLUDED = true;

    private static final LocalDate DEFAULT_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_OPENING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPENING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OPENING_BALANCE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CASH_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRINCIPAL_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRINCIPAL_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRINCIPAL_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYMENT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OUTSTANDING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_OUTSTANDING_BALANCE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYABLE_OPENING = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYABLE_OPENING = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYABLE_OPENING = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_EXPENSE_ACCRUED = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_EXPENSE_ACCRUED = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_EXPENSE_ACCRUED = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_PAYABLE_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PAYABLE_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PAYABLE_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/lease-liability-schedule-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-liability-schedule-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    @Mock
    private LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepositoryMock;

    @Autowired
    private LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    @Mock
    private LeaseLiabilityScheduleItemService leaseLiabilityScheduleItemServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilityScheduleItemSearchRepository mockLeaseLiabilityScheduleItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityScheduleItemMockMvc;

    private LeaseLiabilityScheduleItem leaseLiabilityScheduleItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleItem createEntity(EntityManager em) {
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem = new LeaseLiabilityScheduleItem()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .periodIncluded(DEFAULT_PERIOD_INCLUDED)
            .periodStartDate(DEFAULT_PERIOD_START_DATE)
            .periodEndDate(DEFAULT_PERIOD_END_DATE)
            .openingBalance(DEFAULT_OPENING_BALANCE)
            .cashPayment(DEFAULT_CASH_PAYMENT)
            .principalPayment(DEFAULT_PRINCIPAL_PAYMENT)
            .interestPayment(DEFAULT_INTEREST_PAYMENT)
            .outstandingBalance(DEFAULT_OUTSTANDING_BALANCE)
            .interestPayableOpening(DEFAULT_INTEREST_PAYABLE_OPENING)
            .interestExpenseAccrued(DEFAULT_INTEREST_EXPENSE_ACCRUED)
            .interestPayableBalance(DEFAULT_INTEREST_PAYABLE_BALANCE);
        // Add required entity
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        leaseLiabilityScheduleItem.setLeaseContract(leaseContract);
        return leaseLiabilityScheduleItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiabilityScheduleItem createUpdatedEntity(EntityManager em) {
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem = new LeaseLiabilityScheduleItem()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .periodIncluded(UPDATED_PERIOD_INCLUDED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .cashPayment(UPDATED_CASH_PAYMENT)
            .principalPayment(UPDATED_PRINCIPAL_PAYMENT)
            .interestPayment(UPDATED_INTEREST_PAYMENT)
            .outstandingBalance(UPDATED_OUTSTANDING_BALANCE)
            .interestPayableOpening(UPDATED_INTEREST_PAYABLE_OPENING)
            .interestExpenseAccrued(UPDATED_INTEREST_EXPENSE_ACCRUED)
            .interestPayableBalance(UPDATED_INTEREST_PAYABLE_BALANCE);
        // Add required entity
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        leaseLiabilityScheduleItem.setLeaseContract(leaseContract);
        return leaseLiabilityScheduleItem;
    }

    @BeforeEach
    public void initTest() {
        leaseLiabilityScheduleItem = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityScheduleItemRepository.findAll().size();
        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiabilityScheduleItem testLeaseLiabilityScheduleItem = leaseLiabilityScheduleItemList.get(
            leaseLiabilityScheduleItemList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleItem.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodIncluded()).isEqualTo(DEFAULT_PERIOD_INCLUDED);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getOpeningBalance()).isEqualByComparingTo(DEFAULT_OPENING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getCashPayment()).isEqualByComparingTo(DEFAULT_CASH_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getPrincipalPayment()).isEqualByComparingTo(DEFAULT_PRINCIPAL_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayment()).isEqualByComparingTo(DEFAULT_INTEREST_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getOutstandingBalance()).isEqualByComparingTo(DEFAULT_OUTSTANDING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableOpening()).isEqualByComparingTo(DEFAULT_INTEREST_PAYABLE_OPENING);
        assertThat(testLeaseLiabilityScheduleItem.getInterestExpenseAccrued()).isEqualByComparingTo(DEFAULT_INTEREST_EXPENSE_ACCRUED);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableBalance()).isEqualByComparingTo(DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(1)).save(testLeaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void createLeaseLiabilityScheduleItemWithExistingId() throws Exception {
        // Create the LeaseLiabilityScheduleItem with an existing ID
        leaseLiabilityScheduleItem.setId(1L);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        int databaseSizeBeforeCreate = leaseLiabilityScheduleItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItems() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].periodIncluded").value(hasItem(DEFAULT_PERIOD_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestExpenseAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_EXPENSE_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableBalance").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_BALANCE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseLiabilityScheduleItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaseLiabilityScheduleItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseLiabilityScheduleItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseLiabilityScheduleItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseLiabilityScheduleItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaseLiabilityScheduleItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseLiabilityScheduleItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseLiabilityScheduleItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleItem() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get the leaseLiabilityScheduleItem
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiabilityScheduleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiabilityScheduleItem.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.periodIncluded").value(DEFAULT_PERIOD_INCLUDED.booleanValue()))
            .andExpect(jsonPath("$.periodStartDate").value(DEFAULT_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.periodEndDate").value(DEFAULT_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.openingBalance").value(sameNumber(DEFAULT_OPENING_BALANCE)))
            .andExpect(jsonPath("$.cashPayment").value(sameNumber(DEFAULT_CASH_PAYMENT)))
            .andExpect(jsonPath("$.principalPayment").value(sameNumber(DEFAULT_PRINCIPAL_PAYMENT)))
            .andExpect(jsonPath("$.interestPayment").value(sameNumber(DEFAULT_INTEREST_PAYMENT)))
            .andExpect(jsonPath("$.outstandingBalance").value(sameNumber(DEFAULT_OUTSTANDING_BALANCE)))
            .andExpect(jsonPath("$.interestPayableOpening").value(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING)))
            .andExpect(jsonPath("$.interestExpenseAccrued").value(sameNumber(DEFAULT_INTEREST_EXPENSE_ACCRUED)))
            .andExpect(jsonPath("$.interestPayableBalance").value(sameNumber(DEFAULT_INTEREST_PAYABLE_BALANCE)));
    }

    @Test
    @Transactional
    void getLeaseLiabilityScheduleItemsByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        Long id = leaseLiabilityScheduleItem.getId();

        defaultLeaseLiabilityScheduleItemShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityScheduleItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityScheduleItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.specified=true");

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseLiabilityScheduleItemList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultLeaseLiabilityScheduleItemShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodIncludedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded equals to DEFAULT_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodIncluded.equals=" + DEFAULT_PERIOD_INCLUDED);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded equals to UPDATED_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodIncluded.equals=" + UPDATED_PERIOD_INCLUDED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodIncludedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded not equals to DEFAULT_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodIncluded.notEquals=" + DEFAULT_PERIOD_INCLUDED);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded not equals to UPDATED_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodIncluded.notEquals=" + UPDATED_PERIOD_INCLUDED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodIncludedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded in DEFAULT_PERIOD_INCLUDED or UPDATED_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodIncluded.in=" + DEFAULT_PERIOD_INCLUDED + "," + UPDATED_PERIOD_INCLUDED);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded equals to UPDATED_PERIOD_INCLUDED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodIncluded.in=" + UPDATED_PERIOD_INCLUDED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodIncludedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodIncluded is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodIncluded.specified=true");

        // Get all the leaseLiabilityScheduleItemList where periodIncluded is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodIncluded.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate equals to DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.equals=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate equals to UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.equals=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate not equals to DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.notEquals=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate not equals to UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.notEquals=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate in DEFAULT_PERIOD_START_DATE or UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.in=" + DEFAULT_PERIOD_START_DATE + "," + UPDATED_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate equals to UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.in=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.specified=true");

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is greater than or equal to DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.greaterThanOrEqual=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is greater than or equal to UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.greaterThanOrEqual=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is less than or equal to DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.lessThanOrEqual=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is less than or equal to SMALLER_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.lessThanOrEqual=" + SMALLER_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is less than DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.lessThan=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is less than UPDATED_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.lessThan=" + UPDATED_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is greater than DEFAULT_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodStartDate.greaterThan=" + DEFAULT_PERIOD_START_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodStartDate is greater than SMALLER_PERIOD_START_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodStartDate.greaterThan=" + SMALLER_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate equals to DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.equals=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate equals to UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.equals=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate not equals to DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.notEquals=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate not equals to UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.notEquals=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate in DEFAULT_PERIOD_END_DATE or UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.in=" + DEFAULT_PERIOD_END_DATE + "," + UPDATED_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate equals to UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.in=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.specified=true");

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is greater than or equal to DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.greaterThanOrEqual=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is greater than or equal to UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.greaterThanOrEqual=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is less than or equal to DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.lessThanOrEqual=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is less than or equal to SMALLER_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.lessThanOrEqual=" + SMALLER_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is less than DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.lessThan=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is less than UPDATED_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.lessThan=" + UPDATED_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is greater than DEFAULT_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("periodEndDate.greaterThan=" + DEFAULT_PERIOD_END_DATE);

        // Get all the leaseLiabilityScheduleItemList where periodEndDate is greater than SMALLER_PERIOD_END_DATE
        defaultLeaseLiabilityScheduleItemShouldBeFound("periodEndDate.greaterThan=" + SMALLER_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance equals to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.equals=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.equals=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance not equals to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.notEquals=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance not equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.notEquals=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance in DEFAULT_OPENING_BALANCE or UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.in=" + DEFAULT_OPENING_BALANCE + "," + UPDATED_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance equals to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.in=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.specified=true");

        // Get all the leaseLiabilityScheduleItemList where openingBalance is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is greater than or equal to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.greaterThanOrEqual=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is greater than or equal to UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.greaterThanOrEqual=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is less than or equal to DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.lessThanOrEqual=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is less than or equal to SMALLER_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.lessThanOrEqual=" + SMALLER_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is less than DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.lessThan=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is less than UPDATED_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.lessThan=" + UPDATED_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOpeningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is greater than DEFAULT_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("openingBalance.greaterThan=" + DEFAULT_OPENING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where openingBalance is greater than SMALLER_OPENING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("openingBalance.greaterThan=" + SMALLER_OPENING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment equals to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.equals=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.equals=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment not equals to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.notEquals=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment not equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.notEquals=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment in DEFAULT_CASH_PAYMENT or UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.in=" + DEFAULT_CASH_PAYMENT + "," + UPDATED_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment equals to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.in=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.specified=true");

        // Get all the leaseLiabilityScheduleItemList where cashPayment is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is greater than or equal to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.greaterThanOrEqual=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is greater than or equal to UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.greaterThanOrEqual=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is less than or equal to DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.lessThanOrEqual=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is less than or equal to SMALLER_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.lessThanOrEqual=" + SMALLER_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is less than DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.lessThan=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is less than UPDATED_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.lessThan=" + UPDATED_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByCashPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is greater than DEFAULT_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("cashPayment.greaterThan=" + DEFAULT_CASH_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where cashPayment is greater than SMALLER_CASH_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("cashPayment.greaterThan=" + SMALLER_CASH_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment equals to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.equals=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.equals=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment not equals to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.notEquals=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment not equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.notEquals=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment in DEFAULT_PRINCIPAL_PAYMENT or UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound(
            "principalPayment.in=" + DEFAULT_PRINCIPAL_PAYMENT + "," + UPDATED_PRINCIPAL_PAYMENT
        );

        // Get all the leaseLiabilityScheduleItemList where principalPayment equals to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.in=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.specified=true");

        // Get all the leaseLiabilityScheduleItemList where principalPayment is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is greater than or equal to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.greaterThanOrEqual=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is greater than or equal to UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.greaterThanOrEqual=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is less than or equal to DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.lessThanOrEqual=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is less than or equal to SMALLER_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.lessThanOrEqual=" + SMALLER_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is less than DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.lessThan=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is less than UPDATED_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.lessThan=" + UPDATED_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPrincipalPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is greater than DEFAULT_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("principalPayment.greaterThan=" + DEFAULT_PRINCIPAL_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where principalPayment is greater than SMALLER_PRINCIPAL_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("principalPayment.greaterThan=" + SMALLER_PRINCIPAL_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment equals to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.equals=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.equals=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment not equals to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.notEquals=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment not equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.notEquals=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment in DEFAULT_INTEREST_PAYMENT or UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.in=" + DEFAULT_INTEREST_PAYMENT + "," + UPDATED_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment equals to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.in=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.specified=true");

        // Get all the leaseLiabilityScheduleItemList where interestPayment is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is greater than or equal to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is greater than or equal to UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.greaterThanOrEqual=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is less than or equal to DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.lessThanOrEqual=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is less than or equal to SMALLER_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.lessThanOrEqual=" + SMALLER_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is less than DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.lessThan=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is less than UPDATED_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.lessThan=" + UPDATED_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPaymentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is greater than DEFAULT_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayment.greaterThan=" + DEFAULT_INTEREST_PAYMENT);

        // Get all the leaseLiabilityScheduleItemList where interestPayment is greater than SMALLER_INTEREST_PAYMENT
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayment.greaterThan=" + SMALLER_INTEREST_PAYMENT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance equals to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.equals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.equals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance not equals to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.notEquals=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance not equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.notEquals=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance in DEFAULT_OUTSTANDING_BALANCE or UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound(
            "outstandingBalance.in=" + DEFAULT_OUTSTANDING_BALANCE + "," + UPDATED_OUTSTANDING_BALANCE
        );

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance equals to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.in=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.specified=true");

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is greater than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is greater than or equal to UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.greaterThanOrEqual=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is less than or equal to DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.lessThanOrEqual=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is less than or equal to SMALLER_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.lessThanOrEqual=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is less than DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.lessThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is less than UPDATED_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.lessThan=" + UPDATED_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByOutstandingBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is greater than DEFAULT_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("outstandingBalance.greaterThan=" + DEFAULT_OUTSTANDING_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where outstandingBalance is greater than SMALLER_OUTSTANDING_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("outstandingBalance.greaterThan=" + SMALLER_OUTSTANDING_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening equals to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.equals=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.equals=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening not equals to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.notEquals=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening not equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.notEquals=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening in DEFAULT_INTEREST_PAYABLE_OPENING or UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound(
            "interestPayableOpening.in=" + DEFAULT_INTEREST_PAYABLE_OPENING + "," + UPDATED_INTEREST_PAYABLE_OPENING
        );

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening equals to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.in=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.specified=true");

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is greater than or equal to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is greater than or equal to UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.greaterThanOrEqual=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is less than or equal to DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.lessThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is less than or equal to SMALLER_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.lessThanOrEqual=" + SMALLER_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is less than DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.lessThan=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is less than UPDATED_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.lessThan=" + UPDATED_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableOpeningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is greater than DEFAULT_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableOpening.greaterThan=" + DEFAULT_INTEREST_PAYABLE_OPENING);

        // Get all the leaseLiabilityScheduleItemList where interestPayableOpening is greater than SMALLER_INTEREST_PAYABLE_OPENING
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableOpening.greaterThan=" + SMALLER_INTEREST_PAYABLE_OPENING);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued equals to DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.equals=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued equals to UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.equals=" + UPDATED_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued not equals to DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.notEquals=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued not equals to UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.notEquals=" + UPDATED_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued in DEFAULT_INTEREST_EXPENSE_ACCRUED or UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound(
            "interestExpenseAccrued.in=" + DEFAULT_INTEREST_EXPENSE_ACCRUED + "," + UPDATED_INTEREST_EXPENSE_ACCRUED
        );

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued equals to UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.in=" + UPDATED_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.specified=true");

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is greater than or equal to DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.greaterThanOrEqual=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is greater than or equal to UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.greaterThanOrEqual=" + UPDATED_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is less than or equal to DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.lessThanOrEqual=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is less than or equal to SMALLER_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.lessThanOrEqual=" + SMALLER_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is less than DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.lessThan=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is less than UPDATED_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.lessThan=" + UPDATED_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestExpenseAccruedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is greater than DEFAULT_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestExpenseAccrued.greaterThan=" + DEFAULT_INTEREST_EXPENSE_ACCRUED);

        // Get all the leaseLiabilityScheduleItemList where interestExpenseAccrued is greater than SMALLER_INTEREST_EXPENSE_ACCRUED
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestExpenseAccrued.greaterThan=" + SMALLER_INTEREST_EXPENSE_ACCRUED);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance equals to DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.equals=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance equals to UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.equals=" + UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance not equals to DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.notEquals=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance not equals to UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.notEquals=" + UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance in DEFAULT_INTEREST_PAYABLE_BALANCE or UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound(
            "interestPayableBalance.in=" + DEFAULT_INTEREST_PAYABLE_BALANCE + "," + UPDATED_INTEREST_PAYABLE_BALANCE
        );

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance equals to UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.in=" + UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is not null
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.specified=true");

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is null
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is greater than or equal to DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.greaterThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is greater than or equal to UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.greaterThanOrEqual=" + UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is less than or equal to DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.lessThanOrEqual=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is less than or equal to SMALLER_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.lessThanOrEqual=" + SMALLER_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is less than DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.lessThan=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is less than UPDATED_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.lessThan=" + UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByInterestPayableBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is greater than DEFAULT_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("interestPayableBalance.greaterThan=" + DEFAULT_INTEREST_PAYABLE_BALANCE);

        // Get all the leaseLiabilityScheduleItemList where interestPayableBalance is greater than SMALLER_INTEREST_PAYABLE_BALANCE
        defaultLeaseLiabilityScheduleItemShouldBeFound("interestPayableBalance.greaterThan=" + SMALLER_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        leaseLiabilityScheduleItem.addPlaceholder(placeholder);
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        Long placeholderId = placeholder.getId();

        // Get all the leaseLiabilityScheduleItemList where placeholder equals to placeholderId
        defaultLeaseLiabilityScheduleItemShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the leaseLiabilityScheduleItemList where placeholder equals to (placeholderId + 1)
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByLeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        em.persist(leaseContract);
        em.flush();
        leaseLiabilityScheduleItem.setLeaseContract(leaseContract);
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        Long leaseContractId = leaseContract.getId();

        // Get all the leaseLiabilityScheduleItemList where leaseContract equals to leaseContractId
        defaultLeaseLiabilityScheduleItemShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the leaseLiabilityScheduleItemList where leaseContract equals to (leaseContractId + 1)
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByLeaseModelMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        LeaseModelMetadata leaseModelMetadata;
        if (TestUtil.findAll(em, LeaseModelMetadata.class).isEmpty()) {
            leaseModelMetadata = LeaseModelMetadataResourceIT.createEntity(em);
            em.persist(leaseModelMetadata);
            em.flush();
        } else {
            leaseModelMetadata = TestUtil.findAll(em, LeaseModelMetadata.class).get(0);
        }
        em.persist(leaseModelMetadata);
        em.flush();
        leaseLiabilityScheduleItem.setLeaseModelMetadata(leaseModelMetadata);
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        Long leaseModelMetadataId = leaseModelMetadata.getId();

        // Get all the leaseLiabilityScheduleItemList where leaseModelMetadata equals to leaseModelMetadataId
        defaultLeaseLiabilityScheduleItemShouldBeFound("leaseModelMetadataId.equals=" + leaseModelMetadataId);

        // Get all the leaseLiabilityScheduleItemList where leaseModelMetadata equals to (leaseModelMetadataId + 1)
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("leaseModelMetadataId.equals=" + (leaseModelMetadataId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilityScheduleItemsByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        UniversallyUniqueMapping universallyUniqueMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            universallyUniqueMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(universallyUniqueMapping);
            em.flush();
        } else {
            universallyUniqueMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(universallyUniqueMapping);
        em.flush();
        leaseLiabilityScheduleItem.addUniversallyUniqueMapping(universallyUniqueMapping);
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the leaseLiabilityScheduleItemList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultLeaseLiabilityScheduleItemShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the leaseLiabilityScheduleItemList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultLeaseLiabilityScheduleItemShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityScheduleItemShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].periodIncluded").value(hasItem(DEFAULT_PERIOD_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestExpenseAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_EXPENSE_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableBalance").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_BALANCE))));

        // Check, that the count call also returns 1
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityScheduleItemShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiabilityScheduleItem() throws Exception {
        // Get the leaseLiabilityScheduleItem
        restLeaseLiabilityScheduleItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiabilityScheduleItem() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();

        // Update the leaseLiabilityScheduleItem
        LeaseLiabilityScheduleItem updatedLeaseLiabilityScheduleItem = leaseLiabilityScheduleItemRepository
            .findById(leaseLiabilityScheduleItem.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseLiabilityScheduleItem are not directly saved in db
        em.detach(updatedLeaseLiabilityScheduleItem);
        updatedLeaseLiabilityScheduleItem
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .periodIncluded(UPDATED_PERIOD_INCLUDED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .cashPayment(UPDATED_CASH_PAYMENT)
            .principalPayment(UPDATED_PRINCIPAL_PAYMENT)
            .interestPayment(UPDATED_INTEREST_PAYMENT)
            .outstandingBalance(UPDATED_OUTSTANDING_BALANCE)
            .interestPayableOpening(UPDATED_INTEREST_PAYABLE_OPENING)
            .interestExpenseAccrued(UPDATED_INTEREST_EXPENSE_ACCRUED)
            .interestPayableBalance(UPDATED_INTEREST_PAYABLE_BALANCE);
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(
            updatedLeaseLiabilityScheduleItem
        );

        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityScheduleItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleItem testLeaseLiabilityScheduleItem = leaseLiabilityScheduleItemList.get(
            leaseLiabilityScheduleItemList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleItem.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodIncluded()).isEqualTo(UPDATED_PERIOD_INCLUDED);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getOpeningBalance()).isEqualTo(UPDATED_OPENING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getCashPayment()).isEqualTo(UPDATED_CASH_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getPrincipalPayment()).isEqualTo(UPDATED_PRINCIPAL_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayment()).isEqualTo(UPDATED_INTEREST_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getOutstandingBalance()).isEqualTo(UPDATED_OUTSTANDING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableOpening()).isEqualTo(UPDATED_INTEREST_PAYABLE_OPENING);
        assertThat(testLeaseLiabilityScheduleItem.getInterestExpenseAccrued()).isEqualTo(UPDATED_INTEREST_EXPENSE_ACCRUED);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableBalance()).isEqualTo(UPDATED_INTEREST_PAYABLE_BALANCE);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository).save(testLeaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityScheduleItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityScheduleItemWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();

        // Update the leaseLiabilityScheduleItem using partial update
        LeaseLiabilityScheduleItem partialUpdatedLeaseLiabilityScheduleItem = new LeaseLiabilityScheduleItem();
        partialUpdatedLeaseLiabilityScheduleItem.setId(leaseLiabilityScheduleItem.getId());

        partialUpdatedLeaseLiabilityScheduleItem
            .periodIncluded(UPDATED_PERIOD_INCLUDED)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .cashPayment(UPDATED_CASH_PAYMENT)
            .interestPayableBalance(UPDATED_INTEREST_PAYABLE_BALANCE);

        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityScheduleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityScheduleItem))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleItem testLeaseLiabilityScheduleItem = leaseLiabilityScheduleItemList.get(
            leaseLiabilityScheduleItemList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleItem.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodIncluded()).isEqualTo(UPDATED_PERIOD_INCLUDED);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getOpeningBalance()).isEqualByComparingTo(UPDATED_OPENING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getCashPayment()).isEqualByComparingTo(UPDATED_CASH_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getPrincipalPayment()).isEqualByComparingTo(DEFAULT_PRINCIPAL_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayment()).isEqualByComparingTo(DEFAULT_INTEREST_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getOutstandingBalance()).isEqualByComparingTo(DEFAULT_OUTSTANDING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableOpening()).isEqualByComparingTo(DEFAULT_INTEREST_PAYABLE_OPENING);
        assertThat(testLeaseLiabilityScheduleItem.getInterestExpenseAccrued()).isEqualByComparingTo(DEFAULT_INTEREST_EXPENSE_ACCRUED);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableBalance()).isEqualByComparingTo(UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityScheduleItemWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();

        // Update the leaseLiabilityScheduleItem using partial update
        LeaseLiabilityScheduleItem partialUpdatedLeaseLiabilityScheduleItem = new LeaseLiabilityScheduleItem();
        partialUpdatedLeaseLiabilityScheduleItem.setId(leaseLiabilityScheduleItem.getId());

        partialUpdatedLeaseLiabilityScheduleItem
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .periodIncluded(UPDATED_PERIOD_INCLUDED)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .cashPayment(UPDATED_CASH_PAYMENT)
            .principalPayment(UPDATED_PRINCIPAL_PAYMENT)
            .interestPayment(UPDATED_INTEREST_PAYMENT)
            .outstandingBalance(UPDATED_OUTSTANDING_BALANCE)
            .interestPayableOpening(UPDATED_INTEREST_PAYABLE_OPENING)
            .interestExpenseAccrued(UPDATED_INTEREST_EXPENSE_ACCRUED)
            .interestPayableBalance(UPDATED_INTEREST_PAYABLE_BALANCE);

        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiabilityScheduleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiabilityScheduleItem))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiabilityScheduleItem testLeaseLiabilityScheduleItem = leaseLiabilityScheduleItemList.get(
            leaseLiabilityScheduleItemList.size() - 1
        );
        assertThat(testLeaseLiabilityScheduleItem.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodIncluded()).isEqualTo(UPDATED_PERIOD_INCLUDED);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testLeaseLiabilityScheduleItem.getOpeningBalance()).isEqualByComparingTo(UPDATED_OPENING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getCashPayment()).isEqualByComparingTo(UPDATED_CASH_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getPrincipalPayment()).isEqualByComparingTo(UPDATED_PRINCIPAL_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayment()).isEqualByComparingTo(UPDATED_INTEREST_PAYMENT);
        assertThat(testLeaseLiabilityScheduleItem.getOutstandingBalance()).isEqualByComparingTo(UPDATED_OUTSTANDING_BALANCE);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableOpening()).isEqualByComparingTo(UPDATED_INTEREST_PAYABLE_OPENING);
        assertThat(testLeaseLiabilityScheduleItem.getInterestExpenseAccrued()).isEqualByComparingTo(UPDATED_INTEREST_EXPENSE_ACCRUED);
        assertThat(testLeaseLiabilityScheduleItem.getInterestPayableBalance()).isEqualByComparingTo(UPDATED_INTEREST_PAYABLE_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityScheduleItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiabilityScheduleItem() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityScheduleItemRepository.findAll().size();
        leaseLiabilityScheduleItem.setId(count.incrementAndGet());

        // Create the LeaseLiabilityScheduleItem
        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityScheduleItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityScheduleItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiabilityScheduleItem in the database
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(0)).save(leaseLiabilityScheduleItem);
    }

    @Test
    @Transactional
    void deleteLeaseLiabilityScheduleItem() throws Exception {
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);

        int databaseSizeBeforeDelete = leaseLiabilityScheduleItemRepository.findAll().size();

        // Delete the leaseLiabilityScheduleItem
        restLeaseLiabilityScheduleItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiabilityScheduleItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItemList = leaseLiabilityScheduleItemRepository.findAll();
        assertThat(leaseLiabilityScheduleItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiabilityScheduleItem in Elasticsearch
        verify(mockLeaseLiabilityScheduleItemSearchRepository, times(1)).deleteById(leaseLiabilityScheduleItem.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiabilityScheduleItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityScheduleItemRepository.saveAndFlush(leaseLiabilityScheduleItem);
        when(mockLeaseLiabilityScheduleItemSearchRepository.search("id:" + leaseLiabilityScheduleItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiabilityScheduleItem), PageRequest.of(0, 1), 1));

        // Search the leaseLiabilityScheduleItem
        restLeaseLiabilityScheduleItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiabilityScheduleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiabilityScheduleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].periodIncluded").value(hasItem(DEFAULT_PERIOD_INCLUDED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodEndDate").value(hasItem(DEFAULT_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(sameNumber(DEFAULT_OPENING_BALANCE))))
            .andExpect(jsonPath("$.[*].cashPayment").value(hasItem(sameNumber(DEFAULT_CASH_PAYMENT))))
            .andExpect(jsonPath("$.[*].principalPayment").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAYMENT))))
            .andExpect(jsonPath("$.[*].interestPayment").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYMENT))))
            .andExpect(jsonPath("$.[*].outstandingBalance").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_BALANCE))))
            .andExpect(jsonPath("$.[*].interestPayableOpening").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_OPENING))))
            .andExpect(jsonPath("$.[*].interestExpenseAccrued").value(hasItem(sameNumber(DEFAULT_INTEREST_EXPENSE_ACCRUED))))
            .andExpect(jsonPath("$.[*].interestPayableBalance").value(hasItem(sameNumber(DEFAULT_INTEREST_PAYABLE_BALANCE))));
    }
}
