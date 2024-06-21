package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeaseAmortizationCalculation;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeasePayment;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
import io.github.erp.web.rest.TestUtil;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the LeaseLiabilityResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseLiabilityResourceIT {

    private static final String DEFAULT_LEASE_ID = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LIABILITY_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LIABILITY_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_LIABILITY_AMOUNT = new BigDecimal(0 - 1);

    private static final Float DEFAULT_INTEREST_RATE = 1F;
    private static final Float UPDATED_INTEREST_RATE = 2F;
    private static final Float SMALLER_INTEREST_RATE = 1F - 1F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/leases/lease-liabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-liabilities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseLiabilityRepository leaseLiabilityRepository;

    @Autowired
    private LeaseLiabilityMapper leaseLiabilityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseLiabilitySearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseLiabilitySearchRepository mockLeaseLiabilitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseLiabilityMockMvc;

    private LeaseLiability leaseLiability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiability createEntity(EntityManager em) {
        LeaseLiability leaseLiability = new LeaseLiability()
            .leaseId(DEFAULT_LEASE_ID)
            .liabilityAmount(DEFAULT_LIABILITY_AMOUNT)
            .interestRate(DEFAULT_INTEREST_RATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseLiability.setLeaseContract(iFRS16LeaseContract);
        return leaseLiability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseLiability createUpdatedEntity(EntityManager em) {
        LeaseLiability leaseLiability = new LeaseLiability()
            .leaseId(UPDATED_LEASE_ID)
            .liabilityAmount(UPDATED_LIABILITY_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseLiability.setLeaseContract(iFRS16LeaseContract);
        return leaseLiability;
    }

    @BeforeEach
    public void initTest() {
        leaseLiability = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseLiability() throws Exception {
        int databaseSizeBeforeCreate = leaseLiabilityRepository.findAll().size();
        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);
        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseLiability testLeaseLiability = leaseLiabilityList.get(leaseLiabilityList.size() - 1);
        assertThat(testLeaseLiability.getLeaseId()).isEqualTo(DEFAULT_LEASE_ID);
        assertThat(testLeaseLiability.getLiabilityAmount()).isEqualByComparingTo(DEFAULT_LIABILITY_AMOUNT);
        assertThat(testLeaseLiability.getInterestRate()).isEqualTo(DEFAULT_INTEREST_RATE);
        assertThat(testLeaseLiability.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaseLiability.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(1)).save(testLeaseLiability);
    }

    @Test
    @Transactional
    void createLeaseLiabilityWithExistingId() throws Exception {
        // Create the LeaseLiability with an existing ID
        leaseLiability.setId(1L);
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        int databaseSizeBeforeCreate = leaseLiabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void checkLeaseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityRepository.findAll().size();
        // set the field null
        leaseLiability.setLeaseId(null);

        // Create the LeaseLiability, which fails.
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLiabilityAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityRepository.findAll().size();
        // set the field null
        leaseLiability.setLiabilityAmount(null);

        // Create the LeaseLiability, which fails.
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInterestRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityRepository.findAll().size();
        // set the field null
        leaseLiability.setInterestRate(null);

        // Create the LeaseLiability, which fails.
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityRepository.findAll().size();
        // set the field null
        leaseLiability.setStartDate(null);

        // Create the LeaseLiability, which fails.
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseLiabilityRepository.findAll().size();
        // set the field null
        leaseLiability.setEndDate(null);

        // Create the LeaseLiability, which fails.
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilities() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiability.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseId").value(hasItem(DEFAULT_LEASE_ID)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getLeaseLiability() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get the leaseLiability
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseLiability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseLiability.getId().intValue()))
            .andExpect(jsonPath("$.leaseId").value(DEFAULT_LEASE_ID))
            .andExpect(jsonPath("$.liabilityAmount").value(sameNumber(DEFAULT_LIABILITY_AMOUNT)))
            .andExpect(jsonPath("$.interestRate").value(DEFAULT_INTEREST_RATE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getLeaseLiabilitiesByIdFiltering() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        Long id = leaseLiability.getId();

        defaultLeaseLiabilityShouldBeFound("id.equals=" + id);
        defaultLeaseLiabilityShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseLiabilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseLiabilityShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseLiabilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseLiabilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId equals to DEFAULT_LEASE_ID
        defaultLeaseLiabilityShouldBeFound("leaseId.equals=" + DEFAULT_LEASE_ID);

        // Get all the leaseLiabilityList where leaseId equals to UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldNotBeFound("leaseId.equals=" + UPDATED_LEASE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId not equals to DEFAULT_LEASE_ID
        defaultLeaseLiabilityShouldNotBeFound("leaseId.notEquals=" + DEFAULT_LEASE_ID);

        // Get all the leaseLiabilityList where leaseId not equals to UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldBeFound("leaseId.notEquals=" + UPDATED_LEASE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId in DEFAULT_LEASE_ID or UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldBeFound("leaseId.in=" + DEFAULT_LEASE_ID + "," + UPDATED_LEASE_ID);

        // Get all the leaseLiabilityList where leaseId equals to UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldNotBeFound("leaseId.in=" + UPDATED_LEASE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId is not null
        defaultLeaseLiabilityShouldBeFound("leaseId.specified=true");

        // Get all the leaseLiabilityList where leaseId is null
        defaultLeaseLiabilityShouldNotBeFound("leaseId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId contains DEFAULT_LEASE_ID
        defaultLeaseLiabilityShouldBeFound("leaseId.contains=" + DEFAULT_LEASE_ID);

        // Get all the leaseLiabilityList where leaseId contains UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldNotBeFound("leaseId.contains=" + UPDATED_LEASE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseIdNotContainsSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where leaseId does not contain DEFAULT_LEASE_ID
        defaultLeaseLiabilityShouldNotBeFound("leaseId.doesNotContain=" + DEFAULT_LEASE_ID);

        // Get all the leaseLiabilityList where leaseId does not contain UPDATED_LEASE_ID
        defaultLeaseLiabilityShouldBeFound("leaseId.doesNotContain=" + UPDATED_LEASE_ID);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount equals to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.equals=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.equals=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount not equals to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.notEquals=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount not equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.notEquals=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount in DEFAULT_LIABILITY_AMOUNT or UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.in=" + DEFAULT_LIABILITY_AMOUNT + "," + UPDATED_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount equals to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.in=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount is not null
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.specified=true");

        // Get all the leaseLiabilityList where liabilityAmount is null
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount is greater than or equal to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.greaterThanOrEqual=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount is greater than or equal to UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.greaterThanOrEqual=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount is less than or equal to DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.lessThanOrEqual=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount is less than or equal to SMALLER_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.lessThanOrEqual=" + SMALLER_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount is less than DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.lessThan=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount is less than UPDATED_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.lessThan=" + UPDATED_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLiabilityAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where liabilityAmount is greater than DEFAULT_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldNotBeFound("liabilityAmount.greaterThan=" + DEFAULT_LIABILITY_AMOUNT);

        // Get all the leaseLiabilityList where liabilityAmount is greater than SMALLER_LIABILITY_AMOUNT
        defaultLeaseLiabilityShouldBeFound("liabilityAmount.greaterThan=" + SMALLER_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate equals to DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.equals=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate equals to UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.equals=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate not equals to DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.notEquals=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate not equals to UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.notEquals=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate in DEFAULT_INTEREST_RATE or UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.in=" + DEFAULT_INTEREST_RATE + "," + UPDATED_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate equals to UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.in=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate is not null
        defaultLeaseLiabilityShouldBeFound("interestRate.specified=true");

        // Get all the leaseLiabilityList where interestRate is null
        defaultLeaseLiabilityShouldNotBeFound("interestRate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate is greater than or equal to DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.greaterThanOrEqual=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate is greater than or equal to UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.greaterThanOrEqual=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate is less than or equal to DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.lessThanOrEqual=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate is less than or equal to SMALLER_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.lessThanOrEqual=" + SMALLER_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate is less than DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.lessThan=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate is less than UPDATED_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.lessThan=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByInterestRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where interestRate is greater than DEFAULT_INTEREST_RATE
        defaultLeaseLiabilityShouldNotBeFound("interestRate.greaterThan=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseLiabilityList where interestRate is greater than SMALLER_INTEREST_RATE
        defaultLeaseLiabilityShouldBeFound("interestRate.greaterThan=" + SMALLER_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate equals to DEFAULT_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate equals to UPDATED_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate not equals to DEFAULT_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate not equals to UPDATED_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the leaseLiabilityList where startDate equals to UPDATED_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate is not null
        defaultLeaseLiabilityShouldBeFound("startDate.specified=true");

        // Get all the leaseLiabilityList where startDate is null
        defaultLeaseLiabilityShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate is greater than or equal to UPDATED_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate is less than or equal to DEFAULT_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate is less than or equal to SMALLER_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate is less than DEFAULT_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate is less than UPDATED_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where startDate is greater than DEFAULT_START_DATE
        defaultLeaseLiabilityShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the leaseLiabilityList where startDate is greater than SMALLER_START_DATE
        defaultLeaseLiabilityShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate equals to DEFAULT_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate equals to UPDATED_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate not equals to DEFAULT_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate not equals to UPDATED_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leaseLiabilityList where endDate equals to UPDATED_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate is not null
        defaultLeaseLiabilityShouldBeFound("endDate.specified=true");

        // Get all the leaseLiabilityList where endDate is null
        defaultLeaseLiabilityShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate is greater than or equal to UPDATED_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate is less than or equal to DEFAULT_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate is less than or equal to SMALLER_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate is less than DEFAULT_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate is less than UPDATED_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        // Get all the leaseLiabilityList where endDate is greater than DEFAULT_END_DATE
        defaultLeaseLiabilityShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the leaseLiabilityList where endDate is greater than SMALLER_END_DATE
        defaultLeaseLiabilityShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseAmortizationCalculationIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        LeaseAmortizationCalculation leaseAmortizationCalculation;
        if (TestUtil.findAll(em, LeaseAmortizationCalculation.class).isEmpty()) {
            leaseAmortizationCalculation = LeaseAmortizationCalculationResourceIT.createEntity(em);
            em.persist(leaseAmortizationCalculation);
            em.flush();
        } else {
            leaseAmortizationCalculation = TestUtil.findAll(em, LeaseAmortizationCalculation.class).get(0);
        }
        em.persist(leaseAmortizationCalculation);
        em.flush();
        leaseLiability.setLeaseAmortizationCalculation(leaseAmortizationCalculation);
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        Long leaseAmortizationCalculationId = leaseAmortizationCalculation.getId();

        // Get all the leaseLiabilityList where leaseAmortizationCalculation equals to leaseAmortizationCalculationId
        defaultLeaseLiabilityShouldBeFound("leaseAmortizationCalculationId.equals=" + leaseAmortizationCalculationId);

        // Get all the leaseLiabilityList where leaseAmortizationCalculation equals to (leaseAmortizationCalculationId + 1)
        defaultLeaseLiabilityShouldNotBeFound("leaseAmortizationCalculationId.equals=" + (leaseAmortizationCalculationId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeasePaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        LeasePayment leasePayment;
        if (TestUtil.findAll(em, LeasePayment.class).isEmpty()) {
            leasePayment = LeasePaymentResourceIT.createEntity(em);
            em.persist(leasePayment);
            em.flush();
        } else {
            leasePayment = TestUtil.findAll(em, LeasePayment.class).get(0);
        }
        em.persist(leasePayment);
        em.flush();
        leaseLiability.addLeasePayment(leasePayment);
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        Long leasePaymentId = leasePayment.getId();

        // Get all the leaseLiabilityList where leasePayment equals to leasePaymentId
        defaultLeaseLiabilityShouldBeFound("leasePaymentId.equals=" + leasePaymentId);

        // Get all the leaseLiabilityList where leasePayment equals to (leasePaymentId + 1)
        defaultLeaseLiabilityShouldNotBeFound("leasePaymentId.equals=" + (leasePaymentId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseLiabilitiesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = leaseLiability.getLeaseContract();
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        Long leaseContractId = leaseContract.getId();

        // Get all the leaseLiabilityList where leaseContract equals to leaseContractId
        defaultLeaseLiabilityShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the leaseLiabilityList where leaseContract equals to (leaseContractId + 1)
        defaultLeaseLiabilityShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseLiabilityShouldBeFound(String filter) throws Exception {
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiability.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseId").value(hasItem(DEFAULT_LEASE_ID)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseLiabilityShouldNotBeFound(String filter) throws Exception {
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseLiability() throws Exception {
        // Get the leaseLiability
        restLeaseLiabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseLiability() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();

        // Update the leaseLiability
        LeaseLiability updatedLeaseLiability = leaseLiabilityRepository.findById(leaseLiability.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseLiability are not directly saved in db
        em.detach(updatedLeaseLiability);
        updatedLeaseLiability
            .leaseId(UPDATED_LEASE_ID)
            .liabilityAmount(UPDATED_LIABILITY_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(updatedLeaseLiability);

        restLeaseLiabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiability testLeaseLiability = leaseLiabilityList.get(leaseLiabilityList.size() - 1);
        assertThat(testLeaseLiability.getLeaseId()).isEqualTo(UPDATED_LEASE_ID);
        assertThat(testLeaseLiability.getLiabilityAmount()).isEqualTo(UPDATED_LIABILITY_AMOUNT);
        assertThat(testLeaseLiability.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseLiability.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaseLiability.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository).save(testLeaseLiability);
    }

    @Test
    @Transactional
    void putNonExistingLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseLiabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void partialUpdateLeaseLiabilityWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();

        // Update the leaseLiability using partial update
        LeaseLiability partialUpdatedLeaseLiability = new LeaseLiability();
        partialUpdatedLeaseLiability.setId(leaseLiability.getId());

        partialUpdatedLeaseLiability.liabilityAmount(UPDATED_LIABILITY_AMOUNT).interestRate(UPDATED_INTEREST_RATE);

        restLeaseLiabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiability))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiability testLeaseLiability = leaseLiabilityList.get(leaseLiabilityList.size() - 1);
        assertThat(testLeaseLiability.getLeaseId()).isEqualTo(DEFAULT_LEASE_ID);
        assertThat(testLeaseLiability.getLiabilityAmount()).isEqualByComparingTo(UPDATED_LIABILITY_AMOUNT);
        assertThat(testLeaseLiability.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseLiability.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaseLiability.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseLiabilityWithPatch() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();

        // Update the leaseLiability using partial update
        LeaseLiability partialUpdatedLeaseLiability = new LeaseLiability();
        partialUpdatedLeaseLiability.setId(leaseLiability.getId());

        partialUpdatedLeaseLiability
            .leaseId(UPDATED_LEASE_ID)
            .liabilityAmount(UPDATED_LIABILITY_AMOUNT)
            .interestRate(UPDATED_INTEREST_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restLeaseLiabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseLiability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseLiability))
            )
            .andExpect(status().isOk());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);
        LeaseLiability testLeaseLiability = leaseLiabilityList.get(leaseLiabilityList.size() - 1);
        assertThat(testLeaseLiability.getLeaseId()).isEqualTo(UPDATED_LEASE_ID);
        assertThat(testLeaseLiability.getLiabilityAmount()).isEqualByComparingTo(UPDATED_LIABILITY_AMOUNT);
        assertThat(testLeaseLiability.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseLiability.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaseLiability.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseLiabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseLiability() throws Exception {
        int databaseSizeBeforeUpdate = leaseLiabilityRepository.findAll().size();
        leaseLiability.setId(count.incrementAndGet());

        // Create the LeaseLiability
        LeaseLiabilityDTO leaseLiabilityDTO = leaseLiabilityMapper.toDto(leaseLiability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseLiabilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseLiabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseLiability in the database
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(0)).save(leaseLiability);
    }

    @Test
    @Transactional
    void deleteLeaseLiability() throws Exception {
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);

        int databaseSizeBeforeDelete = leaseLiabilityRepository.findAll().size();

        // Delete the leaseLiability
        restLeaseLiabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseLiability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseLiability> leaseLiabilityList = leaseLiabilityRepository.findAll();
        assertThat(leaseLiabilityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseLiability in Elasticsearch
        verify(mockLeaseLiabilitySearchRepository, times(1)).deleteById(leaseLiability.getId());
    }

    @Test
    @Transactional
    void searchLeaseLiability() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseLiabilityRepository.saveAndFlush(leaseLiability);
        when(mockLeaseLiabilitySearchRepository.search("id:" + leaseLiability.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseLiability), PageRequest.of(0, 1), 1));

        // Search the leaseLiability
        restLeaseLiabilityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseLiability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseLiability.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseId").value(hasItem(DEFAULT_LEASE_ID)))
            .andExpect(jsonPath("$.[*].liabilityAmount").value(hasItem(sameNumber(DEFAULT_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
}
