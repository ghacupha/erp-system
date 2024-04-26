package io.github.erp.web.rest;

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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AccountBalance;
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.AccountBalanceRepository;
import io.github.erp.repository.search.AccountBalanceSearchRepository;
import io.github.erp.service.criteria.AccountBalanceCriteria;
import io.github.erp.service.dto.AccountBalanceDTO;
import io.github.erp.service.mapper.AccountBalanceMapper;
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
 * Integration tests for the {@link AccountBalanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountBalanceResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CONTRACT_NUMBER = "160335545446358";
    private static final String UPDATED_ACCOUNT_CONTRACT_NUMBER = "573672386810492";

    private static final BigDecimal DEFAULT_ACCRUED_INTEREST_BALANCE_FCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCRUED_INTEREST_BALANCE_FCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCRUED_INTEREST_BALANCE_FCY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCRUED_INTEREST_BALANCE_LCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCRUED_INTEREST_BALANCE_LCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCRUED_INTEREST_BALANCE_LCY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE_FCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE_FCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNT_BALANCE_FCY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE_LCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE_LCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNT_BALANCE_LCY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/account-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-balances";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountBalanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountBalanceSearchRepository mockAccountBalanceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountBalanceMockMvc;

    private AccountBalance accountBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBalance createEntity(EntityManager em) {
        AccountBalance accountBalance = new AccountBalance()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .customerId(DEFAULT_CUSTOMER_ID)
            .accountContractNumber(DEFAULT_ACCOUNT_CONTRACT_NUMBER)
            .accruedInterestBalanceFCY(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY)
            .accruedInterestBalanceLCY(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY)
            .accountBalanceFCY(DEFAULT_ACCOUNT_BALANCE_FCY)
            .accountBalanceLCY(DEFAULT_ACCOUNT_BALANCE_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        accountBalance.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        accountBalance.setBranchId(bankBranchCode);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        accountBalance.setCurrencyCode(isoCurrencyCode);
        return accountBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBalance createUpdatedEntity(EntityManager em) {
        AccountBalance accountBalance = new AccountBalance()
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accruedInterestBalanceFCY(UPDATED_ACCRUED_INTEREST_BALANCE_FCY)
            .accruedInterestBalanceLCY(UPDATED_ACCRUED_INTEREST_BALANCE_LCY)
            .accountBalanceFCY(UPDATED_ACCOUNT_BALANCE_FCY)
            .accountBalanceLCY(UPDATED_ACCOUNT_BALANCE_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        accountBalance.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        accountBalance.setBranchId(bankBranchCode);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createUpdatedEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        accountBalance.setCurrencyCode(isoCurrencyCode);
        return accountBalance;
    }

    @BeforeEach
    public void initTest() {
        accountBalance = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = accountBalanceRepository.findAll().size();
        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);
        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        AccountBalance testAccountBalance = accountBalanceList.get(accountBalanceList.size() - 1);
        assertThat(testAccountBalance.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testAccountBalance.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testAccountBalance.getAccountContractNumber()).isEqualTo(DEFAULT_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountBalance.getAccruedInterestBalanceFCY()).isEqualByComparingTo(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);
        assertThat(testAccountBalance.getAccruedInterestBalanceLCY()).isEqualByComparingTo(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);
        assertThat(testAccountBalance.getAccountBalanceFCY()).isEqualByComparingTo(DEFAULT_ACCOUNT_BALANCE_FCY);
        assertThat(testAccountBalance.getAccountBalanceLCY()).isEqualByComparingTo(DEFAULT_ACCOUNT_BALANCE_LCY);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(1)).save(testAccountBalance);
    }

    @Test
    @Transactional
    void createAccountBalanceWithExistingId() throws Exception {
        // Create the AccountBalance with an existing ID
        accountBalance.setId(1L);
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        int databaseSizeBeforeCreate = accountBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setReportingDate(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setCustomerId(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setAccountContractNumber(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccruedInterestBalanceFCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setAccruedInterestBalanceFCY(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccruedInterestBalanceLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setAccruedInterestBalanceLCY(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountBalanceFCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setAccountBalanceFCY(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountBalanceLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountBalanceRepository.findAll().size();
        // set the field null
        accountBalance.setAccountBalanceLCY(null);

        // Create the AccountBalance, which fails.
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        restAccountBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountBalances() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY))))
            .andExpect(jsonPath("$.[*].accountBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accountBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_LCY))));
    }

    @Test
    @Transactional
    void getAccountBalance() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get the accountBalance
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, accountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountBalance.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.accountContractNumber").value(DEFAULT_ACCOUNT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.accruedInterestBalanceFCY").value(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY)))
            .andExpect(jsonPath("$.accruedInterestBalanceLCY").value(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY)))
            .andExpect(jsonPath("$.accountBalanceFCY").value(sameNumber(DEFAULT_ACCOUNT_BALANCE_FCY)))
            .andExpect(jsonPath("$.accountBalanceLCY").value(sameNumber(DEFAULT_ACCOUNT_BALANCE_LCY)));
    }

    @Test
    @Transactional
    void getAccountBalancesByIdFiltering() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        Long id = accountBalance.getId();

        defaultAccountBalanceShouldBeFound("id.equals=" + id);
        defaultAccountBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultAccountBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate is not null
        defaultAccountBalanceShouldBeFound("reportingDate.specified=true");

        // Get all the accountBalanceList where reportingDate is null
        defaultAccountBalanceShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultAccountBalanceShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the accountBalanceList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultAccountBalanceShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultAccountBalanceShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the accountBalanceList where customerId equals to UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId not equals to DEFAULT_CUSTOMER_ID
        defaultAccountBalanceShouldNotBeFound("customerId.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the accountBalanceList where customerId not equals to UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldBeFound("customerId.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the accountBalanceList where customerId equals to UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId is not null
        defaultAccountBalanceShouldBeFound("customerId.specified=true");

        // Get all the accountBalanceList where customerId is null
        defaultAccountBalanceShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId contains DEFAULT_CUSTOMER_ID
        defaultAccountBalanceShouldBeFound("customerId.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the accountBalanceList where customerId contains UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldNotBeFound("customerId.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where customerId does not contain DEFAULT_CUSTOMER_ID
        defaultAccountBalanceShouldNotBeFound("customerId.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the accountBalanceList where customerId does not contain UPDATED_CUSTOMER_ID
        defaultAccountBalanceShouldBeFound("customerId.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber equals to DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldBeFound("accountContractNumber.equals=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountBalanceList where accountContractNumber equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.equals=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber not equals to DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.notEquals=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountBalanceList where accountContractNumber not equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldBeFound("accountContractNumber.notEquals=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber in DEFAULT_ACCOUNT_CONTRACT_NUMBER or UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldBeFound(
            "accountContractNumber.in=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER + "," + UPDATED_ACCOUNT_CONTRACT_NUMBER
        );

        // Get all the accountBalanceList where accountContractNumber equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.in=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber is not null
        defaultAccountBalanceShouldBeFound("accountContractNumber.specified=true");

        // Get all the accountBalanceList where accountContractNumber is null
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberContainsSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber contains DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldBeFound("accountContractNumber.contains=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountBalanceList where accountContractNumber contains UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.contains=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountContractNumber does not contain DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldNotBeFound("accountContractNumber.doesNotContain=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountBalanceList where accountContractNumber does not contain UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountBalanceShouldBeFound("accountContractNumber.doesNotContain=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY equals to DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.equals=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY equals to UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.equals=" + UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY not equals to DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.notEquals=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY not equals to UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.notEquals=" + UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY in DEFAULT_ACCRUED_INTEREST_BALANCE_FCY or UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound(
            "accruedInterestBalanceFCY.in=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY + "," + UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        );

        // Get all the accountBalanceList where accruedInterestBalanceFCY equals to UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.in=" + UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is not null
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.specified=true");

        // Get all the accountBalanceList where accruedInterestBalanceFCY is null
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is greater than or equal to DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.greaterThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is greater than or equal to UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.greaterThanOrEqual=" + UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is less than or equal to DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.lessThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is less than or equal to SMALLER_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.lessThanOrEqual=" + SMALLER_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsLessThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is less than DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.lessThan=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is less than UPDATED_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.lessThan=" + UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceFCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is greater than DEFAULT_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceFCY.greaterThan=" + DEFAULT_ACCRUED_INTEREST_BALANCE_FCY);

        // Get all the accountBalanceList where accruedInterestBalanceFCY is greater than SMALLER_ACCRUED_INTEREST_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceFCY.greaterThan=" + SMALLER_ACCRUED_INTEREST_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY equals to DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.equals=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY equals to UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.equals=" + UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY not equals to DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.notEquals=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY not equals to UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.notEquals=" + UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY in DEFAULT_ACCRUED_INTEREST_BALANCE_LCY or UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound(
            "accruedInterestBalanceLCY.in=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY + "," + UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        );

        // Get all the accountBalanceList where accruedInterestBalanceLCY equals to UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.in=" + UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is not null
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.specified=true");

        // Get all the accountBalanceList where accruedInterestBalanceLCY is null
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is greater than or equal to DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.greaterThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is greater than or equal to UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.greaterThanOrEqual=" + UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is less than or equal to DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.lessThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is less than or equal to SMALLER_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.lessThanOrEqual=" + SMALLER_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is less than DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.lessThan=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is less than UPDATED_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.lessThan=" + UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccruedInterestBalanceLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is greater than DEFAULT_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accruedInterestBalanceLCY.greaterThan=" + DEFAULT_ACCRUED_INTEREST_BALANCE_LCY);

        // Get all the accountBalanceList where accruedInterestBalanceLCY is greater than SMALLER_ACCRUED_INTEREST_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accruedInterestBalanceLCY.greaterThan=" + SMALLER_ACCRUED_INTEREST_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY equals to DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.equals=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY equals to UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.equals=" + UPDATED_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY not equals to DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.notEquals=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY not equals to UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.notEquals=" + UPDATED_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY in DEFAULT_ACCOUNT_BALANCE_FCY or UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.in=" + DEFAULT_ACCOUNT_BALANCE_FCY + "," + UPDATED_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY equals to UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.in=" + UPDATED_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY is not null
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.specified=true");

        // Get all the accountBalanceList where accountBalanceFCY is null
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY is greater than or equal to DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY is greater than or equal to UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY is less than or equal to DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY is less than or equal to SMALLER_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsLessThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY is less than DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.lessThan=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY is less than UPDATED_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.lessThan=" + UPDATED_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceFCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceFCY is greater than DEFAULT_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceFCY.greaterThan=" + DEFAULT_ACCOUNT_BALANCE_FCY);

        // Get all the accountBalanceList where accountBalanceFCY is greater than SMALLER_ACCOUNT_BALANCE_FCY
        defaultAccountBalanceShouldBeFound("accountBalanceFCY.greaterThan=" + SMALLER_ACCOUNT_BALANCE_FCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY equals to DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.equals=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY equals to UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.equals=" + UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY not equals to DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.notEquals=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY not equals to UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.notEquals=" + UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsInShouldWork() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY in DEFAULT_ACCOUNT_BALANCE_LCY or UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.in=" + DEFAULT_ACCOUNT_BALANCE_LCY + "," + UPDATED_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY equals to UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.in=" + UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY is not null
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.specified=true");

        // Get all the accountBalanceList where accountBalanceLCY is null
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY is greater than or equal to DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY is greater than or equal to UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY is less than or equal to DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY is less than or equal to SMALLER_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY is less than DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.lessThan=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY is less than UPDATED_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.lessThan=" + UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByAccountBalanceLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        // Get all the accountBalanceList where accountBalanceLCY is greater than DEFAULT_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldNotBeFound("accountBalanceLCY.greaterThan=" + DEFAULT_ACCOUNT_BALANCE_LCY);

        // Get all the accountBalanceList where accountBalanceLCY is greater than SMALLER_ACCOUNT_BALANCE_LCY
        defaultAccountBalanceShouldBeFound("accountBalanceLCY.greaterThan=" + SMALLER_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void getAllAccountBalancesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);
        InstitutionCode bankCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        accountBalance.setBankCode(bankCode);
        accountBalanceRepository.saveAndFlush(accountBalance);
        Long bankCodeId = bankCode.getId();

        // Get all the accountBalanceList where bankCode equals to bankCodeId
        defaultAccountBalanceShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the accountBalanceList where bankCode equals to (bankCodeId + 1)
        defaultAccountBalanceShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllAccountBalancesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);
        BankBranchCode branchId;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchId = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchId);
            em.flush();
        } else {
            branchId = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchId);
        em.flush();
        accountBalance.setBranchId(branchId);
        accountBalanceRepository.saveAndFlush(accountBalance);
        Long branchIdId = branchId.getId();

        // Get all the accountBalanceList where branchId equals to branchIdId
        defaultAccountBalanceShouldBeFound("branchIdId.equals=" + branchIdId);

        // Get all the accountBalanceList where branchId equals to (branchIdId + 1)
        defaultAccountBalanceShouldNotBeFound("branchIdId.equals=" + (branchIdId + 1));
    }

    @Test
    @Transactional
    void getAllAccountBalancesByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);
        IsoCurrencyCode currencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            currencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(currencyCode);
            em.flush();
        } else {
            currencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        em.persist(currencyCode);
        em.flush();
        accountBalance.setCurrencyCode(currencyCode);
        accountBalanceRepository.saveAndFlush(accountBalance);
        Long currencyCodeId = currencyCode.getId();

        // Get all the accountBalanceList where currencyCode equals to currencyCodeId
        defaultAccountBalanceShouldBeFound("currencyCodeId.equals=" + currencyCodeId);

        // Get all the accountBalanceList where currencyCode equals to (currencyCodeId + 1)
        defaultAccountBalanceShouldNotBeFound("currencyCodeId.equals=" + (currencyCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountBalanceShouldBeFound(String filter) throws Exception {
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY))))
            .andExpect(jsonPath("$.[*].accountBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accountBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_LCY))));

        // Check, that the count call also returns 1
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountBalance() throws Exception {
        // Get the accountBalance
        restAccountBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountBalance() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();

        // Update the accountBalance
        AccountBalance updatedAccountBalance = accountBalanceRepository.findById(accountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedAccountBalance are not directly saved in db
        em.detach(updatedAccountBalance);
        updatedAccountBalance
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accruedInterestBalanceFCY(UPDATED_ACCRUED_INTEREST_BALANCE_FCY)
            .accruedInterestBalanceLCY(UPDATED_ACCRUED_INTEREST_BALANCE_LCY)
            .accountBalanceFCY(UPDATED_ACCOUNT_BALANCE_FCY)
            .accountBalanceLCY(UPDATED_ACCOUNT_BALANCE_LCY);
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(updatedAccountBalance);

        restAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);
        AccountBalance testAccountBalance = accountBalanceList.get(accountBalanceList.size() - 1);
        assertThat(testAccountBalance.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountBalance.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testAccountBalance.getAccountContractNumber()).isEqualTo(UPDATED_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountBalance.getAccruedInterestBalanceFCY()).isEqualTo(UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
        assertThat(testAccountBalance.getAccruedInterestBalanceLCY()).isEqualTo(UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
        assertThat(testAccountBalance.getAccountBalanceFCY()).isEqualTo(UPDATED_ACCOUNT_BALANCE_FCY);
        assertThat(testAccountBalance.getAccountBalanceLCY()).isEqualTo(UPDATED_ACCOUNT_BALANCE_LCY);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository).save(testAccountBalance);
    }

    @Test
    @Transactional
    void putNonExistingAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void partialUpdateAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();

        // Update the accountBalance using partial update
        AccountBalance partialUpdatedAccountBalance = new AccountBalance();
        partialUpdatedAccountBalance.setId(accountBalance.getId());

        partialUpdatedAccountBalance
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accruedInterestBalanceFCY(UPDATED_ACCRUED_INTEREST_BALANCE_FCY)
            .accruedInterestBalanceLCY(UPDATED_ACCRUED_INTEREST_BALANCE_LCY);

        restAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);
        AccountBalance testAccountBalance = accountBalanceList.get(accountBalanceList.size() - 1);
        assertThat(testAccountBalance.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountBalance.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testAccountBalance.getAccountContractNumber()).isEqualTo(UPDATED_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountBalance.getAccruedInterestBalanceFCY()).isEqualByComparingTo(UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
        assertThat(testAccountBalance.getAccruedInterestBalanceLCY()).isEqualByComparingTo(UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
        assertThat(testAccountBalance.getAccountBalanceFCY()).isEqualByComparingTo(DEFAULT_ACCOUNT_BALANCE_FCY);
        assertThat(testAccountBalance.getAccountBalanceLCY()).isEqualByComparingTo(DEFAULT_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void fullUpdateAccountBalanceWithPatch() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();

        // Update the accountBalance using partial update
        AccountBalance partialUpdatedAccountBalance = new AccountBalance();
        partialUpdatedAccountBalance.setId(accountBalance.getId());

        partialUpdatedAccountBalance
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accruedInterestBalanceFCY(UPDATED_ACCRUED_INTEREST_BALANCE_FCY)
            .accruedInterestBalanceLCY(UPDATED_ACCRUED_INTEREST_BALANCE_LCY)
            .accountBalanceFCY(UPDATED_ACCOUNT_BALANCE_FCY)
            .accountBalanceLCY(UPDATED_ACCOUNT_BALANCE_LCY);

        restAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBalance))
            )
            .andExpect(status().isOk());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);
        AccountBalance testAccountBalance = accountBalanceList.get(accountBalanceList.size() - 1);
        assertThat(testAccountBalance.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountBalance.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testAccountBalance.getAccountContractNumber()).isEqualTo(UPDATED_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountBalance.getAccruedInterestBalanceFCY()).isEqualByComparingTo(UPDATED_ACCRUED_INTEREST_BALANCE_FCY);
        assertThat(testAccountBalance.getAccruedInterestBalanceLCY()).isEqualByComparingTo(UPDATED_ACCRUED_INTEREST_BALANCE_LCY);
        assertThat(testAccountBalance.getAccountBalanceFCY()).isEqualByComparingTo(UPDATED_ACCOUNT_BALANCE_FCY);
        assertThat(testAccountBalance.getAccountBalanceLCY()).isEqualByComparingTo(UPDATED_ACCOUNT_BALANCE_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountBalanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = accountBalanceRepository.findAll().size();
        accountBalance.setId(count.incrementAndGet());

        // Create the AccountBalance
        AccountBalanceDTO accountBalanceDTO = accountBalanceMapper.toDto(accountBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBalanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBalance in the database
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(0)).save(accountBalance);
    }

    @Test
    @Transactional
    void deleteAccountBalance() throws Exception {
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);

        int databaseSizeBeforeDelete = accountBalanceRepository.findAll().size();

        // Delete the accountBalance
        restAccountBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountBalance> accountBalanceList = accountBalanceRepository.findAll();
        assertThat(accountBalanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountBalance in Elasticsearch
        verify(mockAccountBalanceSearchRepository, times(1)).deleteById(accountBalance.getId());
    }

    @Test
    @Transactional
    void searchAccountBalance() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountBalanceRepository.saveAndFlush(accountBalance);
        when(mockAccountBalanceSearchRepository.search("id:" + accountBalance.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountBalance), PageRequest.of(0, 1), 1));

        // Search the accountBalance
        restAccountBalanceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accruedInterestBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCRUED_INTEREST_BALANCE_LCY))))
            .andExpect(jsonPath("$.[*].accountBalanceFCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_FCY))))
            .andExpect(jsonPath("$.[*].accountBalanceLCY").value(hasItem(sameNumber(DEFAULT_ACCOUNT_BALANCE_LCY))));
    }
}
