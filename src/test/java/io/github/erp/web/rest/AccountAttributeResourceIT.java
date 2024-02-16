package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.AccountAttribute;
import io.github.erp.domain.AccountOwnershipType;
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.repository.AccountAttributeRepository;
import io.github.erp.repository.search.AccountAttributeSearchRepository;
import io.github.erp.service.criteria.AccountAttributeCriteria;
import io.github.erp.service.dto.AccountAttributeDTO;
import io.github.erp.service.mapper.AccountAttributeMapper;
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
 * Integration tests for the {@link AccountAttributeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountAttributeResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CUSTOMER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACCOUNT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACCOUNT_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACCOUNT_CLOSING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_CLOSING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACCOUNT_CLOSING_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_DEBIT_INTEREST_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT_INTEREST_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEBIT_INTEREST_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CREDIT_INTEREST_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_INTEREST_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CREDIT_INTEREST_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SANCTIONED_ACCOUNT_LIMIT_FCY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SANCTIONED_ACCOUNT_LIMIT_LCY = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_ACCOUNT_STATUS_CHANGE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_STATUS_CHANGE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACCOUNT_STATUS_CHANGE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/account-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-attributes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountAttributeRepository accountAttributeRepository;

    @Autowired
    private AccountAttributeMapper accountAttributeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountAttributeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountAttributeSearchRepository mockAccountAttributeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountAttributeMockMvc;

    private AccountAttribute accountAttribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountAttribute createEntity(EntityManager em) {
        AccountAttribute accountAttribute = new AccountAttribute()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .customerNumber(DEFAULT_CUSTOMER_NUMBER)
            .accountContractNumber(DEFAULT_ACCOUNT_CONTRACT_NUMBER)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountOpeningDate(DEFAULT_ACCOUNT_OPENING_DATE)
            .accountClosingDate(DEFAULT_ACCOUNT_CLOSING_DATE)
            .debitInterestRate(DEFAULT_DEBIT_INTEREST_RATE)
            .creditInterestRate(DEFAULT_CREDIT_INTEREST_RATE)
            .sanctionedAccountLimitFcy(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY)
            .sanctionedAccountLimitLcy(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY)
            .accountStatusChangeDate(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        accountAttribute.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        accountAttribute.setBranchCode(bankBranchCode);
        // Add required entity
        AccountOwnershipType accountOwnershipType;
        if (TestUtil.findAll(em, AccountOwnershipType.class).isEmpty()) {
            accountOwnershipType = AccountOwnershipTypeResourceIT.createEntity(em);
            em.persist(accountOwnershipType);
            em.flush();
        } else {
            accountOwnershipType = TestUtil.findAll(em, AccountOwnershipType.class).get(0);
        }
        accountAttribute.setAccountOwnershipType(accountOwnershipType);
        return accountAttribute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountAttribute createUpdatedEntity(EntityManager em) {
        AccountAttribute accountAttribute = new AccountAttribute()
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountOpeningDate(UPDATED_ACCOUNT_OPENING_DATE)
            .accountClosingDate(UPDATED_ACCOUNT_CLOSING_DATE)
            .debitInterestRate(UPDATED_DEBIT_INTEREST_RATE)
            .creditInterestRate(UPDATED_CREDIT_INTEREST_RATE)
            .sanctionedAccountLimitFcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY)
            .sanctionedAccountLimitLcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY)
            .accountStatusChangeDate(UPDATED_ACCOUNT_STATUS_CHANGE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        accountAttribute.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        accountAttribute.setBranchCode(bankBranchCode);
        // Add required entity
        AccountOwnershipType accountOwnershipType;
        if (TestUtil.findAll(em, AccountOwnershipType.class).isEmpty()) {
            accountOwnershipType = AccountOwnershipTypeResourceIT.createUpdatedEntity(em);
            em.persist(accountOwnershipType);
            em.flush();
        } else {
            accountOwnershipType = TestUtil.findAll(em, AccountOwnershipType.class).get(0);
        }
        accountAttribute.setAccountOwnershipType(accountOwnershipType);
        return accountAttribute;
    }

    @BeforeEach
    public void initTest() {
        accountAttribute = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountAttribute() throws Exception {
        int databaseSizeBeforeCreate = accountAttributeRepository.findAll().size();
        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);
        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        AccountAttribute testAccountAttribute = accountAttributeList.get(accountAttributeList.size() - 1);
        assertThat(testAccountAttribute.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testAccountAttribute.getCustomerNumber()).isEqualTo(DEFAULT_CUSTOMER_NUMBER);
        assertThat(testAccountAttribute.getAccountContractNumber()).isEqualTo(DEFAULT_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountAttribute.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testAccountAttribute.getAccountOpeningDate()).isEqualTo(DEFAULT_ACCOUNT_OPENING_DATE);
        assertThat(testAccountAttribute.getAccountClosingDate()).isEqualTo(DEFAULT_ACCOUNT_CLOSING_DATE);
        assertThat(testAccountAttribute.getDebitInterestRate()).isEqualByComparingTo(DEFAULT_DEBIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getCreditInterestRate()).isEqualByComparingTo(DEFAULT_CREDIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getSanctionedAccountLimitFcy()).isEqualByComparingTo(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);
        assertThat(testAccountAttribute.getSanctionedAccountLimitLcy()).isEqualByComparingTo(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);
        assertThat(testAccountAttribute.getAccountStatusChangeDate()).isEqualTo(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);
        assertThat(testAccountAttribute.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(1)).save(testAccountAttribute);
    }

    @Test
    @Transactional
    void createAccountAttributeWithExistingId() throws Exception {
        // Create the AccountAttribute with an existing ID
        accountAttribute.setId(1L);
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        int databaseSizeBeforeCreate = accountAttributeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setReportingDate(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setCustomerNumber(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setAccountContractNumber(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setAccountName(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDebitInterestRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setDebitInterestRate(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditInterestRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setCreditInterestRate(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSanctionedAccountLimitFcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setSanctionedAccountLimitFcy(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSanctionedAccountLimitLcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeRepository.findAll().size();
        // set the field null
        accountAttribute.setSanctionedAccountLimitLcy(null);

        // Create the AccountAttribute, which fails.
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        restAccountAttributeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountAttributes() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerNumber").value(hasItem(DEFAULT_CUSTOMER_NUMBER)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountClosingDate").value(hasItem(DEFAULT_ACCOUNT_CLOSING_DATE.toString())))
            .andExpect(jsonPath("$.[*].debitInterestRate").value(hasItem(sameNumber(DEFAULT_DEBIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].creditInterestRate").value(hasItem(sameNumber(DEFAULT_CREDIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitFcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitLcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY))))
            .andExpect(jsonPath("$.[*].accountStatusChangeDate").value(hasItem(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }

    @Test
    @Transactional
    void getAccountAttribute() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get the accountAttribute
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL_ID, accountAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountAttribute.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.customerNumber").value(DEFAULT_CUSTOMER_NUMBER))
            .andExpect(jsonPath("$.accountContractNumber").value(DEFAULT_ACCOUNT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountOpeningDate").value(DEFAULT_ACCOUNT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.accountClosingDate").value(DEFAULT_ACCOUNT_CLOSING_DATE.toString()))
            .andExpect(jsonPath("$.debitInterestRate").value(sameNumber(DEFAULT_DEBIT_INTEREST_RATE)))
            .andExpect(jsonPath("$.creditInterestRate").value(sameNumber(DEFAULT_CREDIT_INTEREST_RATE)))
            .andExpect(jsonPath("$.sanctionedAccountLimitFcy").value(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY)))
            .andExpect(jsonPath("$.sanctionedAccountLimitLcy").value(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY)))
            .andExpect(jsonPath("$.accountStatusChangeDate").value(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getAccountAttributesByIdFiltering() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        Long id = accountAttribute.getId();

        defaultAccountAttributeShouldBeFound("id.equals=" + id);
        defaultAccountAttributeShouldNotBeFound("id.notEquals=" + id);

        defaultAccountAttributeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountAttributeShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountAttributeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountAttributeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate is not null
        defaultAccountAttributeShouldBeFound("reportingDate.specified=true");

        // Get all the accountAttributeList where reportingDate is null
        defaultAccountAttributeShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultAccountAttributeShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the accountAttributeList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultAccountAttributeShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber equals to DEFAULT_CUSTOMER_NUMBER
        defaultAccountAttributeShouldBeFound("customerNumber.equals=" + DEFAULT_CUSTOMER_NUMBER);

        // Get all the accountAttributeList where customerNumber equals to UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldNotBeFound("customerNumber.equals=" + UPDATED_CUSTOMER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber not equals to DEFAULT_CUSTOMER_NUMBER
        defaultAccountAttributeShouldNotBeFound("customerNumber.notEquals=" + DEFAULT_CUSTOMER_NUMBER);

        // Get all the accountAttributeList where customerNumber not equals to UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldBeFound("customerNumber.notEquals=" + UPDATED_CUSTOMER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber in DEFAULT_CUSTOMER_NUMBER or UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldBeFound("customerNumber.in=" + DEFAULT_CUSTOMER_NUMBER + "," + UPDATED_CUSTOMER_NUMBER);

        // Get all the accountAttributeList where customerNumber equals to UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldNotBeFound("customerNumber.in=" + UPDATED_CUSTOMER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber is not null
        defaultAccountAttributeShouldBeFound("customerNumber.specified=true");

        // Get all the accountAttributeList where customerNumber is null
        defaultAccountAttributeShouldNotBeFound("customerNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber contains DEFAULT_CUSTOMER_NUMBER
        defaultAccountAttributeShouldBeFound("customerNumber.contains=" + DEFAULT_CUSTOMER_NUMBER);

        // Get all the accountAttributeList where customerNumber contains UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldNotBeFound("customerNumber.contains=" + UPDATED_CUSTOMER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCustomerNumberNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where customerNumber does not contain DEFAULT_CUSTOMER_NUMBER
        defaultAccountAttributeShouldNotBeFound("customerNumber.doesNotContain=" + DEFAULT_CUSTOMER_NUMBER);

        // Get all the accountAttributeList where customerNumber does not contain UPDATED_CUSTOMER_NUMBER
        defaultAccountAttributeShouldBeFound("customerNumber.doesNotContain=" + UPDATED_CUSTOMER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber equals to DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldBeFound("accountContractNumber.equals=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountAttributeList where accountContractNumber equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.equals=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber not equals to DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.notEquals=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountAttributeList where accountContractNumber not equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldBeFound("accountContractNumber.notEquals=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber in DEFAULT_ACCOUNT_CONTRACT_NUMBER or UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldBeFound(
            "accountContractNumber.in=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER + "," + UPDATED_ACCOUNT_CONTRACT_NUMBER
        );

        // Get all the accountAttributeList where accountContractNumber equals to UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.in=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber is not null
        defaultAccountAttributeShouldBeFound("accountContractNumber.specified=true");

        // Get all the accountAttributeList where accountContractNumber is null
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber contains DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldBeFound("accountContractNumber.contains=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountAttributeList where accountContractNumber contains UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.contains=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountContractNumber does not contain DEFAULT_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldNotBeFound("accountContractNumber.doesNotContain=" + DEFAULT_ACCOUNT_CONTRACT_NUMBER);

        // Get all the accountAttributeList where accountContractNumber does not contain UPDATED_ACCOUNT_CONTRACT_NUMBER
        defaultAccountAttributeShouldBeFound("accountContractNumber.doesNotContain=" + UPDATED_ACCOUNT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultAccountAttributeShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the accountAttributeList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultAccountAttributeShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the accountAttributeList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the accountAttributeList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName is not null
        defaultAccountAttributeShouldBeFound("accountName.specified=true");

        // Get all the accountAttributeList where accountName is null
        defaultAccountAttributeShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultAccountAttributeShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the accountAttributeList where accountName contains UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultAccountAttributeShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the accountAttributeList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultAccountAttributeShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate equals to DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.equals=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.equals=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate not equals to DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.notEquals=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate not equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.notEquals=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate in DEFAULT_ACCOUNT_OPENING_DATE or UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.in=" + DEFAULT_ACCOUNT_OPENING_DATE + "," + UPDATED_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.in=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate is not null
        defaultAccountAttributeShouldBeFound("accountOpeningDate.specified=true");

        // Get all the accountAttributeList where accountOpeningDate is null
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate is greater than or equal to DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.greaterThanOrEqual=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate is greater than or equal to UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.greaterThanOrEqual=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate is less than or equal to DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.lessThanOrEqual=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate is less than or equal to SMALLER_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.lessThanOrEqual=" + SMALLER_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate is less than DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.lessThan=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate is less than UPDATED_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.lessThan=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountOpeningDate is greater than DEFAULT_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldNotBeFound("accountOpeningDate.greaterThan=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the accountAttributeList where accountOpeningDate is greater than SMALLER_ACCOUNT_OPENING_DATE
        defaultAccountAttributeShouldBeFound("accountOpeningDate.greaterThan=" + SMALLER_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate equals to DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.equals=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate equals to UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.equals=" + UPDATED_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate not equals to DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.notEquals=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate not equals to UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.notEquals=" + UPDATED_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate in DEFAULT_ACCOUNT_CLOSING_DATE or UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.in=" + DEFAULT_ACCOUNT_CLOSING_DATE + "," + UPDATED_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate equals to UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.in=" + UPDATED_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate is not null
        defaultAccountAttributeShouldBeFound("accountClosingDate.specified=true");

        // Get all the accountAttributeList where accountClosingDate is null
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate is greater than or equal to DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.greaterThanOrEqual=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate is greater than or equal to UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.greaterThanOrEqual=" + UPDATED_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate is less than or equal to DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.lessThanOrEqual=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate is less than or equal to SMALLER_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.lessThanOrEqual=" + SMALLER_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate is less than DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.lessThan=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate is less than UPDATED_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.lessThan=" + UPDATED_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountClosingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountClosingDate is greater than DEFAULT_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldNotBeFound("accountClosingDate.greaterThan=" + DEFAULT_ACCOUNT_CLOSING_DATE);

        // Get all the accountAttributeList where accountClosingDate is greater than SMALLER_ACCOUNT_CLOSING_DATE
        defaultAccountAttributeShouldBeFound("accountClosingDate.greaterThan=" + SMALLER_ACCOUNT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate equals to DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.equals=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate equals to UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.equals=" + UPDATED_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate not equals to DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.notEquals=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate not equals to UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.notEquals=" + UPDATED_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate in DEFAULT_DEBIT_INTEREST_RATE or UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.in=" + DEFAULT_DEBIT_INTEREST_RATE + "," + UPDATED_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate equals to UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.in=" + UPDATED_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate is not null
        defaultAccountAttributeShouldBeFound("debitInterestRate.specified=true");

        // Get all the accountAttributeList where debitInterestRate is null
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate is greater than or equal to DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.greaterThanOrEqual=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate is greater than or equal to UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.greaterThanOrEqual=" + UPDATED_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate is less than or equal to DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.lessThanOrEqual=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate is less than or equal to SMALLER_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.lessThanOrEqual=" + SMALLER_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate is less than DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.lessThan=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate is less than UPDATED_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.lessThan=" + UPDATED_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByDebitInterestRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where debitInterestRate is greater than DEFAULT_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("debitInterestRate.greaterThan=" + DEFAULT_DEBIT_INTEREST_RATE);

        // Get all the accountAttributeList where debitInterestRate is greater than SMALLER_DEBIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("debitInterestRate.greaterThan=" + SMALLER_DEBIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate equals to DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.equals=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate equals to UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.equals=" + UPDATED_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate not equals to DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.notEquals=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate not equals to UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.notEquals=" + UPDATED_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate in DEFAULT_CREDIT_INTEREST_RATE or UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.in=" + DEFAULT_CREDIT_INTEREST_RATE + "," + UPDATED_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate equals to UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.in=" + UPDATED_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate is not null
        defaultAccountAttributeShouldBeFound("creditInterestRate.specified=true");

        // Get all the accountAttributeList where creditInterestRate is null
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate is greater than or equal to DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.greaterThanOrEqual=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate is greater than or equal to UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.greaterThanOrEqual=" + UPDATED_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate is less than or equal to DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.lessThanOrEqual=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate is less than or equal to SMALLER_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.lessThanOrEqual=" + SMALLER_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate is less than DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.lessThan=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate is less than UPDATED_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.lessThan=" + UPDATED_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByCreditInterestRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where creditInterestRate is greater than DEFAULT_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldNotBeFound("creditInterestRate.greaterThan=" + DEFAULT_CREDIT_INTEREST_RATE);

        // Get all the accountAttributeList where creditInterestRate is greater than SMALLER_CREDIT_INTEREST_RATE
        defaultAccountAttributeShouldBeFound("creditInterestRate.greaterThan=" + SMALLER_CREDIT_INTEREST_RATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy equals to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.equals=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.equals=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy not equals to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.notEquals=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy not equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.notEquals=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy in DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY or UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound(
            "sanctionedAccountLimitFcy.in=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY + "," + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        );

        // Get all the accountAttributeList where sanctionedAccountLimitFcy equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.in=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is not null
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.specified=true");

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is null
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is greater than or equal to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.greaterThanOrEqual=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is greater than or equal to UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.greaterThanOrEqual=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is less than or equal to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.lessThanOrEqual=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is less than or equal to SMALLER_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.lessThanOrEqual=" + SMALLER_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is less than DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.lessThan=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is less than UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.lessThan=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitFcyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is greater than DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitFcy.greaterThan=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY);

        // Get all the accountAttributeList where sanctionedAccountLimitFcy is greater than SMALLER_SANCTIONED_ACCOUNT_LIMIT_FCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitFcy.greaterThan=" + SMALLER_SANCTIONED_ACCOUNT_LIMIT_FCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy equals to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.equals=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.equals=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy not equals to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.notEquals=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy not equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.notEquals=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy in DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY or UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound(
            "sanctionedAccountLimitLcy.in=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY + "," + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        );

        // Get all the accountAttributeList where sanctionedAccountLimitLcy equals to UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.in=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is not null
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.specified=true");

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is null
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is greater than or equal to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.greaterThanOrEqual=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is greater than or equal to UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.greaterThanOrEqual=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is less than or equal to DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.lessThanOrEqual=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is less than or equal to SMALLER_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.lessThanOrEqual=" + SMALLER_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is less than DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.lessThan=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is less than UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.lessThan=" + UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesBySanctionedAccountLimitLcyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is greater than DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldNotBeFound("sanctionedAccountLimitLcy.greaterThan=" + DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY);

        // Get all the accountAttributeList where sanctionedAccountLimitLcy is greater than SMALLER_SANCTIONED_ACCOUNT_LIMIT_LCY
        defaultAccountAttributeShouldBeFound("sanctionedAccountLimitLcy.greaterThan=" + SMALLER_SANCTIONED_ACCOUNT_LIMIT_LCY);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate equals to DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.equals=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate equals to UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.equals=" + UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate not equals to DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.notEquals=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate not equals to UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.notEquals=" + UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate in DEFAULT_ACCOUNT_STATUS_CHANGE_DATE or UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound(
            "accountStatusChangeDate.in=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE + "," + UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        );

        // Get all the accountAttributeList where accountStatusChangeDate equals to UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.in=" + UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate is not null
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.specified=true");

        // Get all the accountAttributeList where accountStatusChangeDate is null
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate is greater than or equal to DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.greaterThanOrEqual=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate is greater than or equal to UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.greaterThanOrEqual=" + UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate is less than or equal to DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.lessThanOrEqual=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate is less than or equal to SMALLER_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.lessThanOrEqual=" + SMALLER_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate is less than DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.lessThan=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate is less than UPDATED_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.lessThan=" + UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountStatusChangeDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where accountStatusChangeDate is greater than DEFAULT_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldNotBeFound("accountStatusChangeDate.greaterThan=" + DEFAULT_ACCOUNT_STATUS_CHANGE_DATE);

        // Get all the accountAttributeList where accountStatusChangeDate is greater than SMALLER_ACCOUNT_STATUS_CHANGE_DATE
        defaultAccountAttributeShouldBeFound("accountStatusChangeDate.greaterThan=" + SMALLER_ACCOUNT_STATUS_CHANGE_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate equals to DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.equals=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate not equals to DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.notEquals=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate not equals to UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.notEquals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate in DEFAULT_EXPIRY_DATE or UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate is not null
        defaultAccountAttributeShouldBeFound("expiryDate.specified=true");

        // Get all the accountAttributeList where expiryDate is null
        defaultAccountAttributeShouldNotBeFound("expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate is greater than or equal to DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.greaterThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate is greater than or equal to UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.greaterThanOrEqual=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate is less than or equal to DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.lessThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate is less than or equal to SMALLER_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.lessThanOrEqual=" + SMALLER_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate is less than DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate is less than UPDATED_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        // Get all the accountAttributeList where expiryDate is greater than DEFAULT_EXPIRY_DATE
        defaultAccountAttributeShouldNotBeFound("expiryDate.greaterThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the accountAttributeList where expiryDate is greater than SMALLER_EXPIRY_DATE
        defaultAccountAttributeShouldBeFound("expiryDate.greaterThan=" + SMALLER_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAccountAttributesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);
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
        accountAttribute.setBankCode(bankCode);
        accountAttributeRepository.saveAndFlush(accountAttribute);
        Long bankCodeId = bankCode.getId();

        // Get all the accountAttributeList where bankCode equals to bankCodeId
        defaultAccountAttributeShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the accountAttributeList where bankCode equals to (bankCodeId + 1)
        defaultAccountAttributeShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllAccountAttributesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);
        BankBranchCode branchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchCode);
            em.flush();
        } else {
            branchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchCode);
        em.flush();
        accountAttribute.setBranchCode(branchCode);
        accountAttributeRepository.saveAndFlush(accountAttribute);
        Long branchCodeId = branchCode.getId();

        // Get all the accountAttributeList where branchCode equals to branchCodeId
        defaultAccountAttributeShouldBeFound("branchCodeId.equals=" + branchCodeId);

        // Get all the accountAttributeList where branchCode equals to (branchCodeId + 1)
        defaultAccountAttributeShouldNotBeFound("branchCodeId.equals=" + (branchCodeId + 1));
    }

    @Test
    @Transactional
    void getAllAccountAttributesByAccountOwnershipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);
        AccountOwnershipType accountOwnershipType;
        if (TestUtil.findAll(em, AccountOwnershipType.class).isEmpty()) {
            accountOwnershipType = AccountOwnershipTypeResourceIT.createEntity(em);
            em.persist(accountOwnershipType);
            em.flush();
        } else {
            accountOwnershipType = TestUtil.findAll(em, AccountOwnershipType.class).get(0);
        }
        em.persist(accountOwnershipType);
        em.flush();
        accountAttribute.setAccountOwnershipType(accountOwnershipType);
        accountAttributeRepository.saveAndFlush(accountAttribute);
        Long accountOwnershipTypeId = accountOwnershipType.getId();

        // Get all the accountAttributeList where accountOwnershipType equals to accountOwnershipTypeId
        defaultAccountAttributeShouldBeFound("accountOwnershipTypeId.equals=" + accountOwnershipTypeId);

        // Get all the accountAttributeList where accountOwnershipType equals to (accountOwnershipTypeId + 1)
        defaultAccountAttributeShouldNotBeFound("accountOwnershipTypeId.equals=" + (accountOwnershipTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountAttributeShouldBeFound(String filter) throws Exception {
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerNumber").value(hasItem(DEFAULT_CUSTOMER_NUMBER)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountClosingDate").value(hasItem(DEFAULT_ACCOUNT_CLOSING_DATE.toString())))
            .andExpect(jsonPath("$.[*].debitInterestRate").value(hasItem(sameNumber(DEFAULT_DEBIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].creditInterestRate").value(hasItem(sameNumber(DEFAULT_CREDIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitFcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitLcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY))))
            .andExpect(jsonPath("$.[*].accountStatusChangeDate").value(hasItem(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));

        // Check, that the count call also returns 1
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountAttributeShouldNotBeFound(String filter) throws Exception {
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountAttributeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountAttribute() throws Exception {
        // Get the accountAttribute
        restAccountAttributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountAttribute() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();

        // Update the accountAttribute
        AccountAttribute updatedAccountAttribute = accountAttributeRepository.findById(accountAttribute.getId()).get();
        // Disconnect from session so that the updates on updatedAccountAttribute are not directly saved in db
        em.detach(updatedAccountAttribute);
        updatedAccountAttribute
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountOpeningDate(UPDATED_ACCOUNT_OPENING_DATE)
            .accountClosingDate(UPDATED_ACCOUNT_CLOSING_DATE)
            .debitInterestRate(UPDATED_DEBIT_INTEREST_RATE)
            .creditInterestRate(UPDATED_CREDIT_INTEREST_RATE)
            .sanctionedAccountLimitFcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY)
            .sanctionedAccountLimitLcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY)
            .accountStatusChangeDate(UPDATED_ACCOUNT_STATUS_CHANGE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(updatedAccountAttribute);

        restAccountAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountAttributeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);
        AccountAttribute testAccountAttribute = accountAttributeList.get(accountAttributeList.size() - 1);
        assertThat(testAccountAttribute.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountAttribute.getCustomerNumber()).isEqualTo(UPDATED_CUSTOMER_NUMBER);
        assertThat(testAccountAttribute.getAccountContractNumber()).isEqualTo(UPDATED_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountAttribute.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testAccountAttribute.getAccountOpeningDate()).isEqualTo(UPDATED_ACCOUNT_OPENING_DATE);
        assertThat(testAccountAttribute.getAccountClosingDate()).isEqualTo(UPDATED_ACCOUNT_CLOSING_DATE);
        assertThat(testAccountAttribute.getDebitInterestRate()).isEqualTo(UPDATED_DEBIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getCreditInterestRate()).isEqualTo(UPDATED_CREDIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getSanctionedAccountLimitFcy()).isEqualTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
        assertThat(testAccountAttribute.getSanctionedAccountLimitLcy()).isEqualTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
        assertThat(testAccountAttribute.getAccountStatusChangeDate()).isEqualTo(UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
        assertThat(testAccountAttribute.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository).save(testAccountAttribute);
    }

    @Test
    @Transactional
    void putNonExistingAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountAttributeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void partialUpdateAccountAttributeWithPatch() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();

        // Update the accountAttribute using partial update
        AccountAttribute partialUpdatedAccountAttribute = new AccountAttribute();
        partialUpdatedAccountAttribute.setId(accountAttribute.getId());

        partialUpdatedAccountAttribute
            .reportingDate(UPDATED_REPORTING_DATE)
            .debitInterestRate(UPDATED_DEBIT_INTEREST_RATE)
            .creditInterestRate(UPDATED_CREDIT_INTEREST_RATE)
            .sanctionedAccountLimitFcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY)
            .sanctionedAccountLimitLcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY)
            .accountStatusChangeDate(UPDATED_ACCOUNT_STATUS_CHANGE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);

        restAccountAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountAttribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountAttribute))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);
        AccountAttribute testAccountAttribute = accountAttributeList.get(accountAttributeList.size() - 1);
        assertThat(testAccountAttribute.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountAttribute.getCustomerNumber()).isEqualTo(DEFAULT_CUSTOMER_NUMBER);
        assertThat(testAccountAttribute.getAccountContractNumber()).isEqualTo(DEFAULT_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountAttribute.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testAccountAttribute.getAccountOpeningDate()).isEqualTo(DEFAULT_ACCOUNT_OPENING_DATE);
        assertThat(testAccountAttribute.getAccountClosingDate()).isEqualTo(DEFAULT_ACCOUNT_CLOSING_DATE);
        assertThat(testAccountAttribute.getDebitInterestRate()).isEqualByComparingTo(UPDATED_DEBIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getCreditInterestRate()).isEqualByComparingTo(UPDATED_CREDIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getSanctionedAccountLimitFcy()).isEqualByComparingTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
        assertThat(testAccountAttribute.getSanctionedAccountLimitLcy()).isEqualByComparingTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
        assertThat(testAccountAttribute.getAccountStatusChangeDate()).isEqualTo(UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
        assertThat(testAccountAttribute.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAccountAttributeWithPatch() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();

        // Update the accountAttribute using partial update
        AccountAttribute partialUpdatedAccountAttribute = new AccountAttribute();
        partialUpdatedAccountAttribute.setId(accountAttribute.getId());

        partialUpdatedAccountAttribute
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .accountContractNumber(UPDATED_ACCOUNT_CONTRACT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountOpeningDate(UPDATED_ACCOUNT_OPENING_DATE)
            .accountClosingDate(UPDATED_ACCOUNT_CLOSING_DATE)
            .debitInterestRate(UPDATED_DEBIT_INTEREST_RATE)
            .creditInterestRate(UPDATED_CREDIT_INTEREST_RATE)
            .sanctionedAccountLimitFcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY)
            .sanctionedAccountLimitLcy(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY)
            .accountStatusChangeDate(UPDATED_ACCOUNT_STATUS_CHANGE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);

        restAccountAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountAttribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountAttribute))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);
        AccountAttribute testAccountAttribute = accountAttributeList.get(accountAttributeList.size() - 1);
        assertThat(testAccountAttribute.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAccountAttribute.getCustomerNumber()).isEqualTo(UPDATED_CUSTOMER_NUMBER);
        assertThat(testAccountAttribute.getAccountContractNumber()).isEqualTo(UPDATED_ACCOUNT_CONTRACT_NUMBER);
        assertThat(testAccountAttribute.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testAccountAttribute.getAccountOpeningDate()).isEqualTo(UPDATED_ACCOUNT_OPENING_DATE);
        assertThat(testAccountAttribute.getAccountClosingDate()).isEqualTo(UPDATED_ACCOUNT_CLOSING_DATE);
        assertThat(testAccountAttribute.getDebitInterestRate()).isEqualByComparingTo(UPDATED_DEBIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getCreditInterestRate()).isEqualByComparingTo(UPDATED_CREDIT_INTEREST_RATE);
        assertThat(testAccountAttribute.getSanctionedAccountLimitFcy()).isEqualByComparingTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_FCY);
        assertThat(testAccountAttribute.getSanctionedAccountLimitLcy()).isEqualByComparingTo(UPDATED_SANCTIONED_ACCOUNT_LIMIT_LCY);
        assertThat(testAccountAttribute.getAccountStatusChangeDate()).isEqualTo(UPDATED_ACCOUNT_STATUS_CHANGE_DATE);
        assertThat(testAccountAttribute.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountAttributeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountAttribute() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeRepository.findAll().size();
        accountAttribute.setId(count.incrementAndGet());

        // Create the AccountAttribute
        AccountAttributeDTO accountAttributeDTO = accountAttributeMapper.toDto(accountAttribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountAttribute in the database
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(0)).save(accountAttribute);
    }

    @Test
    @Transactional
    void deleteAccountAttribute() throws Exception {
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);

        int databaseSizeBeforeDelete = accountAttributeRepository.findAll().size();

        // Delete the accountAttribute
        restAccountAttributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountAttribute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountAttribute> accountAttributeList = accountAttributeRepository.findAll();
        assertThat(accountAttributeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountAttribute in Elasticsearch
        verify(mockAccountAttributeSearchRepository, times(1)).deleteById(accountAttribute.getId());
    }

    @Test
    @Transactional
    void searchAccountAttribute() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountAttributeRepository.saveAndFlush(accountAttribute);
        when(mockAccountAttributeSearchRepository.search("id:" + accountAttribute.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountAttribute), PageRequest.of(0, 1), 1));

        // Search the accountAttribute
        restAccountAttributeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerNumber").value(hasItem(DEFAULT_CUSTOMER_NUMBER)))
            .andExpect(jsonPath("$.[*].accountContractNumber").value(hasItem(DEFAULT_ACCOUNT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountClosingDate").value(hasItem(DEFAULT_ACCOUNT_CLOSING_DATE.toString())))
            .andExpect(jsonPath("$.[*].debitInterestRate").value(hasItem(sameNumber(DEFAULT_DEBIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].creditInterestRate").value(hasItem(sameNumber(DEFAULT_CREDIT_INTEREST_RATE))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitFcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_FCY))))
            .andExpect(jsonPath("$.[*].sanctionedAccountLimitLcy").value(hasItem(sameNumber(DEFAULT_SANCTIONED_ACCOUNT_LIMIT_LCY))))
            .andExpect(jsonPath("$.[*].accountStatusChangeDate").value(hasItem(DEFAULT_ACCOUNT_STATUS_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }
}
