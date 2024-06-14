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
import io.github.erp.repository.LeaseAmortizationCalculationRepository;
import io.github.erp.repository.search.LeaseAmortizationCalculationSearchRepository;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.mapper.LeaseAmortizationCalculationMapper;
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
 * Integration tests for the LeaseAmortizationCalculationResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseAmortizationCalculationResourceIT {

    private static final Float DEFAULT_INTEREST_RATE = 1F;
    private static final Float UPDATED_INTEREST_RATE = 2F;
    private static final Float SMALLER_INTEREST_RATE = 1F - 1F;

    private static final String DEFAULT_PERIODICITY = "AAAAAAAAAA";
    private static final String UPDATED_PERIODICITY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LEASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEASE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LEASE_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUMBER_OF_PERIODS = 1;
    private static final Integer UPDATED_NUMBER_OF_PERIODS = 2;
    private static final Integer SMALLER_NUMBER_OF_PERIODS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/leases/lease-amortization-calculations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-amortization-calculations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository;

    @Autowired
    private LeaseAmortizationCalculationMapper leaseAmortizationCalculationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseAmortizationCalculationSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseAmortizationCalculationSearchRepository mockLeaseAmortizationCalculationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseAmortizationCalculationMockMvc;

    private LeaseAmortizationCalculation leaseAmortizationCalculation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseAmortizationCalculation createEntity(EntityManager em) {
        LeaseAmortizationCalculation leaseAmortizationCalculation = new LeaseAmortizationCalculation()
            .interestRate(DEFAULT_INTEREST_RATE)
            .periodicity(DEFAULT_PERIODICITY)
            .leaseAmount(DEFAULT_LEASE_AMOUNT)
            .numberOfPeriods(DEFAULT_NUMBER_OF_PERIODS);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseAmortizationCalculation.setIFRS16LeaseContract(iFRS16LeaseContract);
        return leaseAmortizationCalculation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseAmortizationCalculation createUpdatedEntity(EntityManager em) {
        LeaseAmortizationCalculation leaseAmortizationCalculation = new LeaseAmortizationCalculation()
            .interestRate(UPDATED_INTEREST_RATE)
            .periodicity(UPDATED_PERIODICITY)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .numberOfPeriods(UPDATED_NUMBER_OF_PERIODS);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseAmortizationCalculation.setIFRS16LeaseContract(iFRS16LeaseContract);
        return leaseAmortizationCalculation;
    }

    @BeforeEach
    public void initTest() {
        leaseAmortizationCalculation = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeCreate = leaseAmortizationCalculationRepository.findAll().size();
        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );
        restLeaseAmortizationCalculationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseAmortizationCalculation testLeaseAmortizationCalculation = leaseAmortizationCalculationList.get(
            leaseAmortizationCalculationList.size() - 1
        );
        assertThat(testLeaseAmortizationCalculation.getInterestRate()).isEqualTo(DEFAULT_INTEREST_RATE);
        assertThat(testLeaseAmortizationCalculation.getPeriodicity()).isEqualTo(DEFAULT_PERIODICITY);
        assertThat(testLeaseAmortizationCalculation.getLeaseAmount()).isEqualByComparingTo(DEFAULT_LEASE_AMOUNT);
        assertThat(testLeaseAmortizationCalculation.getNumberOfPeriods()).isEqualTo(DEFAULT_NUMBER_OF_PERIODS);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(1)).save(testLeaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void createLeaseAmortizationCalculationWithExistingId() throws Exception {
        // Create the LeaseAmortizationCalculation with an existing ID
        leaseAmortizationCalculation.setId(1L);
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        int databaseSizeBeforeCreate = leaseAmortizationCalculationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseAmortizationCalculationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculations() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicity").value(hasItem(DEFAULT_PERIODICITY)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPeriods").value(hasItem(DEFAULT_NUMBER_OF_PERIODS)));
    }

    @Test
    @Transactional
    void getLeaseAmortizationCalculation() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get the leaseAmortizationCalculation
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseAmortizationCalculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseAmortizationCalculation.getId().intValue()))
            .andExpect(jsonPath("$.interestRate").value(DEFAULT_INTEREST_RATE.doubleValue()))
            .andExpect(jsonPath("$.periodicity").value(DEFAULT_PERIODICITY))
            .andExpect(jsonPath("$.leaseAmount").value(sameNumber(DEFAULT_LEASE_AMOUNT)))
            .andExpect(jsonPath("$.numberOfPeriods").value(DEFAULT_NUMBER_OF_PERIODS));
    }

    @Test
    @Transactional
    void getLeaseAmortizationCalculationsByIdFiltering() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        Long id = leaseAmortizationCalculation.getId();

        defaultLeaseAmortizationCalculationShouldBeFound("id.equals=" + id);
        defaultLeaseAmortizationCalculationShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseAmortizationCalculationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseAmortizationCalculationShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseAmortizationCalculationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseAmortizationCalculationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate equals to DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.equals=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate equals to UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.equals=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate not equals to DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.notEquals=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate not equals to UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.notEquals=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate in DEFAULT_INTEREST_RATE or UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.in=" + DEFAULT_INTEREST_RATE + "," + UPDATED_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate equals to UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.in=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate is not null
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.specified=true");

        // Get all the leaseAmortizationCalculationList where interestRate is null
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate is greater than or equal to DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.greaterThanOrEqual=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate is greater than or equal to UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.greaterThanOrEqual=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate is less than or equal to DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.lessThanOrEqual=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate is less than or equal to SMALLER_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.lessThanOrEqual=" + SMALLER_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate is less than DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.lessThan=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate is less than UPDATED_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.lessThan=" + UPDATED_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByInterestRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where interestRate is greater than DEFAULT_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldNotBeFound("interestRate.greaterThan=" + DEFAULT_INTEREST_RATE);

        // Get all the leaseAmortizationCalculationList where interestRate is greater than SMALLER_INTEREST_RATE
        defaultLeaseAmortizationCalculationShouldBeFound("interestRate.greaterThan=" + SMALLER_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity equals to DEFAULT_PERIODICITY
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.equals=" + DEFAULT_PERIODICITY);

        // Get all the leaseAmortizationCalculationList where periodicity equals to UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.equals=" + UPDATED_PERIODICITY);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity not equals to DEFAULT_PERIODICITY
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.notEquals=" + DEFAULT_PERIODICITY);

        // Get all the leaseAmortizationCalculationList where periodicity not equals to UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.notEquals=" + UPDATED_PERIODICITY);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityIsInShouldWork() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity in DEFAULT_PERIODICITY or UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.in=" + DEFAULT_PERIODICITY + "," + UPDATED_PERIODICITY);

        // Get all the leaseAmortizationCalculationList where periodicity equals to UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.in=" + UPDATED_PERIODICITY);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity is not null
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.specified=true");

        // Get all the leaseAmortizationCalculationList where periodicity is null
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityContainsSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity contains DEFAULT_PERIODICITY
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.contains=" + DEFAULT_PERIODICITY);

        // Get all the leaseAmortizationCalculationList where periodicity contains UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.contains=" + UPDATED_PERIODICITY);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByPeriodicityNotContainsSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where periodicity does not contain DEFAULT_PERIODICITY
        defaultLeaseAmortizationCalculationShouldNotBeFound("periodicity.doesNotContain=" + DEFAULT_PERIODICITY);

        // Get all the leaseAmortizationCalculationList where periodicity does not contain UPDATED_PERIODICITY
        defaultLeaseAmortizationCalculationShouldBeFound("periodicity.doesNotContain=" + UPDATED_PERIODICITY);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount equals to DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.equals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.equals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount not equals to DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.notEquals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount not equals to UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.notEquals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount in DEFAULT_LEASE_AMOUNT or UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.in=" + DEFAULT_LEASE_AMOUNT + "," + UPDATED_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.in=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount is not null
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.specified=true");

        // Get all the leaseAmortizationCalculationList where leaseAmount is null
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount is greater than or equal to DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.greaterThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount is greater than or equal to UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.greaterThanOrEqual=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount is less than or equal to DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.lessThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount is less than or equal to SMALLER_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.lessThanOrEqual=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount is less than DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.lessThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount is less than UPDATED_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.lessThan=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByLeaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where leaseAmount is greater than DEFAULT_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldNotBeFound("leaseAmount.greaterThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the leaseAmortizationCalculationList where leaseAmount is greater than SMALLER_LEASE_AMOUNT
        defaultLeaseAmortizationCalculationShouldBeFound("leaseAmount.greaterThan=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods equals to DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.equals=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods equals to UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.equals=" + UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods not equals to DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.notEquals=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods not equals to UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.notEquals=" + UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods in DEFAULT_NUMBER_OF_PERIODS or UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound(
            "numberOfPeriods.in=" + DEFAULT_NUMBER_OF_PERIODS + "," + UPDATED_NUMBER_OF_PERIODS
        );

        // Get all the leaseAmortizationCalculationList where numberOfPeriods equals to UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.in=" + UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is not null
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.specified=true");

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is null
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is greater than or equal to DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is greater than or equal to UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is less than or equal to DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is less than or equal to SMALLER_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.lessThanOrEqual=" + SMALLER_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is less than DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.lessThan=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is less than UPDATED_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.lessThan=" + UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByNumberOfPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is greater than DEFAULT_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldNotBeFound("numberOfPeriods.greaterThan=" + DEFAULT_NUMBER_OF_PERIODS);

        // Get all the leaseAmortizationCalculationList where numberOfPeriods is greater than SMALLER_NUMBER_OF_PERIODS
        defaultLeaseAmortizationCalculationShouldBeFound("numberOfPeriods.greaterThan=" + SMALLER_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationCalculationsByIFRS16LeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract iFRS16LeaseContract = leaseAmortizationCalculation.getIFRS16LeaseContract();
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);
        Long iFRS16LeaseContractId = iFRS16LeaseContract.getId();

        // Get all the leaseAmortizationCalculationList where iFRS16LeaseContract equals to iFRS16LeaseContractId
        defaultLeaseAmortizationCalculationShouldBeFound("iFRS16LeaseContractId.equals=" + iFRS16LeaseContractId);

        // Get all the leaseAmortizationCalculationList where iFRS16LeaseContract equals to (iFRS16LeaseContractId + 1)
        defaultLeaseAmortizationCalculationShouldNotBeFound("iFRS16LeaseContractId.equals=" + (iFRS16LeaseContractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseAmortizationCalculationShouldBeFound(String filter) throws Exception {
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicity").value(hasItem(DEFAULT_PERIODICITY)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPeriods").value(hasItem(DEFAULT_NUMBER_OF_PERIODS)));

        // Check, that the count call also returns 1
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseAmortizationCalculationShouldNotBeFound(String filter) throws Exception {
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseAmortizationCalculation() throws Exception {
        // Get the leaseAmortizationCalculation
        restLeaseAmortizationCalculationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseAmortizationCalculation() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();

        // Update the leaseAmortizationCalculation
        LeaseAmortizationCalculation updatedLeaseAmortizationCalculation = leaseAmortizationCalculationRepository
            .findById(leaseAmortizationCalculation.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseAmortizationCalculation are not directly saved in db
        em.detach(updatedLeaseAmortizationCalculation);
        updatedLeaseAmortizationCalculation
            .interestRate(UPDATED_INTEREST_RATE)
            .periodicity(UPDATED_PERIODICITY)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .numberOfPeriods(UPDATED_NUMBER_OF_PERIODS);
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            updatedLeaseAmortizationCalculation
        );

        restLeaseAmortizationCalculationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseAmortizationCalculationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationCalculation testLeaseAmortizationCalculation = leaseAmortizationCalculationList.get(
            leaseAmortizationCalculationList.size() - 1
        );
        assertThat(testLeaseAmortizationCalculation.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseAmortizationCalculation.getPeriodicity()).isEqualTo(UPDATED_PERIODICITY);
        assertThat(testLeaseAmortizationCalculation.getLeaseAmount()).isEqualTo(UPDATED_LEASE_AMOUNT);
        assertThat(testLeaseAmortizationCalculation.getNumberOfPeriods()).isEqualTo(UPDATED_NUMBER_OF_PERIODS);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository).save(testLeaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void putNonExistingLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseAmortizationCalculationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void partialUpdateLeaseAmortizationCalculationWithPatch() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();

        // Update the leaseAmortizationCalculation using partial update
        LeaseAmortizationCalculation partialUpdatedLeaseAmortizationCalculation = new LeaseAmortizationCalculation();
        partialUpdatedLeaseAmortizationCalculation.setId(leaseAmortizationCalculation.getId());

        partialUpdatedLeaseAmortizationCalculation
            .interestRate(UPDATED_INTEREST_RATE)
            .periodicity(UPDATED_PERIODICITY)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .numberOfPeriods(UPDATED_NUMBER_OF_PERIODS);

        restLeaseAmortizationCalculationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseAmortizationCalculation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseAmortizationCalculation))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationCalculation testLeaseAmortizationCalculation = leaseAmortizationCalculationList.get(
            leaseAmortizationCalculationList.size() - 1
        );
        assertThat(testLeaseAmortizationCalculation.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseAmortizationCalculation.getPeriodicity()).isEqualTo(UPDATED_PERIODICITY);
        assertThat(testLeaseAmortizationCalculation.getLeaseAmount()).isEqualByComparingTo(UPDATED_LEASE_AMOUNT);
        assertThat(testLeaseAmortizationCalculation.getNumberOfPeriods()).isEqualTo(UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void fullUpdateLeaseAmortizationCalculationWithPatch() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();

        // Update the leaseAmortizationCalculation using partial update
        LeaseAmortizationCalculation partialUpdatedLeaseAmortizationCalculation = new LeaseAmortizationCalculation();
        partialUpdatedLeaseAmortizationCalculation.setId(leaseAmortizationCalculation.getId());

        partialUpdatedLeaseAmortizationCalculation
            .interestRate(UPDATED_INTEREST_RATE)
            .periodicity(UPDATED_PERIODICITY)
            .leaseAmount(UPDATED_LEASE_AMOUNT)
            .numberOfPeriods(UPDATED_NUMBER_OF_PERIODS);

        restLeaseAmortizationCalculationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseAmortizationCalculation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseAmortizationCalculation))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationCalculation testLeaseAmortizationCalculation = leaseAmortizationCalculationList.get(
            leaseAmortizationCalculationList.size() - 1
        );
        assertThat(testLeaseAmortizationCalculation.getInterestRate()).isEqualTo(UPDATED_INTEREST_RATE);
        assertThat(testLeaseAmortizationCalculation.getPeriodicity()).isEqualTo(UPDATED_PERIODICITY);
        assertThat(testLeaseAmortizationCalculation.getLeaseAmount()).isEqualByComparingTo(UPDATED_LEASE_AMOUNT);
        assertThat(testLeaseAmortizationCalculation.getNumberOfPeriods()).isEqualTo(UPDATED_NUMBER_OF_PERIODS);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseAmortizationCalculationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseAmortizationCalculation() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationCalculationRepository.findAll().size();
        leaseAmortizationCalculation.setId(count.incrementAndGet());

        // Create the LeaseAmortizationCalculation
        LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO = leaseAmortizationCalculationMapper.toDto(
            leaseAmortizationCalculation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationCalculationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationCalculationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseAmortizationCalculation in the database
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(0)).save(leaseAmortizationCalculation);
    }

    @Test
    @Transactional
    void deleteLeaseAmortizationCalculation() throws Exception {
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);

        int databaseSizeBeforeDelete = leaseAmortizationCalculationRepository.findAll().size();

        // Delete the leaseAmortizationCalculation
        restLeaseAmortizationCalculationMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseAmortizationCalculation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseAmortizationCalculation> leaseAmortizationCalculationList = leaseAmortizationCalculationRepository.findAll();
        assertThat(leaseAmortizationCalculationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseAmortizationCalculation in Elasticsearch
        verify(mockLeaseAmortizationCalculationSearchRepository, times(1)).deleteById(leaseAmortizationCalculation.getId());
    }

    @Test
    @Transactional
    void searchLeaseAmortizationCalculation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseAmortizationCalculationRepository.saveAndFlush(leaseAmortizationCalculation);
        when(mockLeaseAmortizationCalculationSearchRepository.search("id:" + leaseAmortizationCalculation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseAmortizationCalculation), PageRequest.of(0, 1), 1));

        // Search the leaseAmortizationCalculation
        restLeaseAmortizationCalculationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseAmortizationCalculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicity").value(hasItem(DEFAULT_PERIODICITY)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].numberOfPeriods").value(hasItem(DEFAULT_NUMBER_OF_PERIODS)));
    }
}
