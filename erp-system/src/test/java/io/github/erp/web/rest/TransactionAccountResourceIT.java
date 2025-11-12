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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ReportingEntity;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionAccountCategory;
import io.github.erp.domain.TransactionAccountLedger;
import io.github.erp.domain.enumeration.AccountSubTypes;
import io.github.erp.domain.enumeration.AccountTypes;
import io.github.erp.repository.TransactionAccountRepository;
import io.github.erp.repository.search.TransactionAccountSearchRepository;
import io.github.erp.service.TransactionAccountService;
import io.github.erp.service.criteria.TransactionAccountCriteria;
import io.github.erp.service.dto.TransactionAccountDTO;
import io.github.erp.service.mapper.TransactionAccountMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TransactionAccountResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NOTES_CONTENT_TYPE = "image/png";

    private static final AccountTypes DEFAULT_ACCOUNT_TYPE = AccountTypes.ASSET;
    private static final AccountTypes UPDATED_ACCOUNT_TYPE = AccountTypes.LIABILITY;

    private static final AccountSubTypes DEFAULT_ACCOUNT_SUB_TYPE = AccountSubTypes.SETTLEMENT_ASSET;
    private static final AccountSubTypes UPDATED_ACCOUNT_SUB_TYPE = AccountSubTypes.ACCOUNT_RECEIVABLE;

    private static final Boolean DEFAULT_DUMMY_ACCOUNT = false;
    private static final Boolean UPDATED_DUMMY_ACCOUNT = true;

    private static final String ENTITY_API_URL = "/api/transaction-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-accounts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountRepository transactionAccountRepository;

    @Mock
    private TransactionAccountRepository transactionAccountRepositoryMock;

    @Autowired
    private TransactionAccountMapper transactionAccountMapper;

    @Mock
    private TransactionAccountService transactionAccountServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountSearchRepository mockTransactionAccountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountMockMvc;

    private TransactionAccount transactionAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccount createEntity(EntityManager em) {
        TransactionAccount transactionAccount = new TransactionAccount()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .notes(DEFAULT_NOTES)
            .notesContentType(DEFAULT_NOTES_CONTENT_TYPE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountSubType(DEFAULT_ACCOUNT_SUB_TYPE)
            .dummyAccount(DEFAULT_DUMMY_ACCOUNT);
        // Add required entity
        TransactionAccountLedger transactionAccountLedger;
        if (TestUtil.findAll(em, TransactionAccountLedger.class).isEmpty()) {
            transactionAccountLedger = TransactionAccountLedgerResourceIT.createEntity(em);
            em.persist(transactionAccountLedger);
            em.flush();
        } else {
            transactionAccountLedger = TestUtil.findAll(em, TransactionAccountLedger.class).get(0);
        }
        transactionAccount.setAccountLedger(transactionAccountLedger);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccount.setAccountCategory(transactionAccountCategory);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        transactionAccount.setServiceOutlet(serviceOutlet);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        transactionAccount.setSettlementCurrency(settlementCurrency);
        // Add required entity
        ReportingEntity reportingEntity;
        if (TestUtil.findAll(em, ReportingEntity.class).isEmpty()) {
            reportingEntity = ReportingEntityResourceIT.createEntity(em);
            em.persist(reportingEntity);
            em.flush();
        } else {
            reportingEntity = TestUtil.findAll(em, ReportingEntity.class).get(0);
        }
        transactionAccount.setInstitution(reportingEntity);
        return transactionAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccount createUpdatedEntity(EntityManager em) {
        TransactionAccount transactionAccount = new TransactionAccount()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountSubType(UPDATED_ACCOUNT_SUB_TYPE)
            .dummyAccount(UPDATED_DUMMY_ACCOUNT);
        // Add required entity
        TransactionAccountLedger transactionAccountLedger;
        if (TestUtil.findAll(em, TransactionAccountLedger.class).isEmpty()) {
            transactionAccountLedger = TransactionAccountLedgerResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccountLedger);
            em.flush();
        } else {
            transactionAccountLedger = TestUtil.findAll(em, TransactionAccountLedger.class).get(0);
        }
        transactionAccount.setAccountLedger(transactionAccountLedger);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccount.setAccountCategory(transactionAccountCategory);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createUpdatedEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        transactionAccount.setServiceOutlet(serviceOutlet);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createUpdatedEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        transactionAccount.setSettlementCurrency(settlementCurrency);
        // Add required entity
        ReportingEntity reportingEntity;
        if (TestUtil.findAll(em, ReportingEntity.class).isEmpty()) {
            reportingEntity = ReportingEntityResourceIT.createUpdatedEntity(em);
            em.persist(reportingEntity);
            em.flush();
        } else {
            reportingEntity = TestUtil.findAll(em, ReportingEntity.class).get(0);
        }
        transactionAccount.setInstitution(reportingEntity);
        return transactionAccount;
    }

    @BeforeEach
    public void initTest() {
        transactionAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccount() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountRepository.findAll().size();
        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);
        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccount testTransactionAccount = transactionAccountList.get(transactionAccountList.size() - 1);
        assertThat(testTransactionAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testTransactionAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testTransactionAccount.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTransactionAccount.getNotesContentType()).isEqualTo(DEFAULT_NOTES_CONTENT_TYPE);
        assertThat(testTransactionAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testTransactionAccount.getAccountSubType()).isEqualTo(DEFAULT_ACCOUNT_SUB_TYPE);
        assertThat(testTransactionAccount.getDummyAccount()).isEqualTo(DEFAULT_DUMMY_ACCOUNT);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(1)).save(testTransactionAccount);
    }

    @Test
    @Transactional
    void createTransactionAccountWithExistingId() throws Exception {
        // Create the TransactionAccount with an existing ID
        transactionAccount.setId(1L);
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        int databaseSizeBeforeCreate = transactionAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountRepository.findAll().size();
        // set the field null
        transactionAccount.setAccountNumber(null);

        // Create the TransactionAccount, which fails.
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountRepository.findAll().size();
        // set the field null
        transactionAccount.setAccountName(null);

        // Create the TransactionAccount, which fails.
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountRepository.findAll().size();
        // set the field null
        transactionAccount.setAccountType(null);

        // Create the TransactionAccount, which fails.
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountSubTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountRepository.findAll().size();
        // set the field null
        transactionAccount.setAccountSubType(null);

        // Create the TransactionAccount, which fails.
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        restTransactionAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccounts() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountSubType").value(hasItem(DEFAULT_ACCOUNT_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dummyAccount").value(hasItem(DEFAULT_DUMMY_ACCOUNT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionAccount() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get the transactionAccount
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.notesContentType").value(DEFAULT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.notes").value(Base64Utils.encodeToString(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountSubType").value(DEFAULT_ACCOUNT_SUB_TYPE.toString()))
            .andExpect(jsonPath("$.dummyAccount").value(DEFAULT_DUMMY_ACCOUNT.booleanValue()));
    }

    @Test
    @Transactional
    void getTransactionAccountsByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        Long id = transactionAccount.getId();

        defaultTransactionAccountShouldBeFound("id.equals=" + id);
        defaultTransactionAccountShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the transactionAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber is not null
        defaultTransactionAccountShouldBeFound("accountNumber.specified=true");

        // Get all the transactionAccountList where accountNumber is null
        defaultTransactionAccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultTransactionAccountShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the transactionAccountList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultTransactionAccountShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the transactionAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName is not null
        defaultTransactionAccountShouldBeFound("accountName.specified=true");

        // Get all the transactionAccountList where accountName is null
        defaultTransactionAccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountList where accountName contains UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultTransactionAccountShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the transactionAccountList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultTransactionAccountShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountType equals to DEFAULT_ACCOUNT_TYPE
        defaultTransactionAccountShouldBeFound("accountType.equals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the transactionAccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultTransactionAccountShouldNotBeFound("accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountType not equals to DEFAULT_ACCOUNT_TYPE
        defaultTransactionAccountShouldNotBeFound("accountType.notEquals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the transactionAccountList where accountType not equals to UPDATED_ACCOUNT_TYPE
        defaultTransactionAccountShouldBeFound("accountType.notEquals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountType in DEFAULT_ACCOUNT_TYPE or UPDATED_ACCOUNT_TYPE
        defaultTransactionAccountShouldBeFound("accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE);

        // Get all the transactionAccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultTransactionAccountShouldNotBeFound("accountType.in=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountType is not null
        defaultTransactionAccountShouldBeFound("accountType.specified=true");

        // Get all the transactionAccountList where accountType is null
        defaultTransactionAccountShouldNotBeFound("accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountSubTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountSubType equals to DEFAULT_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldBeFound("accountSubType.equals=" + DEFAULT_ACCOUNT_SUB_TYPE);

        // Get all the transactionAccountList where accountSubType equals to UPDATED_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldNotBeFound("accountSubType.equals=" + UPDATED_ACCOUNT_SUB_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountSubTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountSubType not equals to DEFAULT_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldNotBeFound("accountSubType.notEquals=" + DEFAULT_ACCOUNT_SUB_TYPE);

        // Get all the transactionAccountList where accountSubType not equals to UPDATED_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldBeFound("accountSubType.notEquals=" + UPDATED_ACCOUNT_SUB_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountSubTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountSubType in DEFAULT_ACCOUNT_SUB_TYPE or UPDATED_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldBeFound("accountSubType.in=" + DEFAULT_ACCOUNT_SUB_TYPE + "," + UPDATED_ACCOUNT_SUB_TYPE);

        // Get all the transactionAccountList where accountSubType equals to UPDATED_ACCOUNT_SUB_TYPE
        defaultTransactionAccountShouldNotBeFound("accountSubType.in=" + UPDATED_ACCOUNT_SUB_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountSubTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where accountSubType is not null
        defaultTransactionAccountShouldBeFound("accountSubType.specified=true");

        // Get all the transactionAccountList where accountSubType is null
        defaultTransactionAccountShouldNotBeFound("accountSubType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByDummyAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where dummyAccount equals to DEFAULT_DUMMY_ACCOUNT
        defaultTransactionAccountShouldBeFound("dummyAccount.equals=" + DEFAULT_DUMMY_ACCOUNT);

        // Get all the transactionAccountList where dummyAccount equals to UPDATED_DUMMY_ACCOUNT
        defaultTransactionAccountShouldNotBeFound("dummyAccount.equals=" + UPDATED_DUMMY_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByDummyAccountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where dummyAccount not equals to DEFAULT_DUMMY_ACCOUNT
        defaultTransactionAccountShouldNotBeFound("dummyAccount.notEquals=" + DEFAULT_DUMMY_ACCOUNT);

        // Get all the transactionAccountList where dummyAccount not equals to UPDATED_DUMMY_ACCOUNT
        defaultTransactionAccountShouldBeFound("dummyAccount.notEquals=" + UPDATED_DUMMY_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByDummyAccountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where dummyAccount in DEFAULT_DUMMY_ACCOUNT or UPDATED_DUMMY_ACCOUNT
        defaultTransactionAccountShouldBeFound("dummyAccount.in=" + DEFAULT_DUMMY_ACCOUNT + "," + UPDATED_DUMMY_ACCOUNT);

        // Get all the transactionAccountList where dummyAccount equals to UPDATED_DUMMY_ACCOUNT
        defaultTransactionAccountShouldNotBeFound("dummyAccount.in=" + UPDATED_DUMMY_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByDummyAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList where dummyAccount is not null
        defaultTransactionAccountShouldBeFound("dummyAccount.specified=true");

        // Get all the transactionAccountList where dummyAccount is null
        defaultTransactionAccountShouldNotBeFound("dummyAccount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountLedgerIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        TransactionAccountLedger accountLedger;
        if (TestUtil.findAll(em, TransactionAccountLedger.class).isEmpty()) {
            accountLedger = TransactionAccountLedgerResourceIT.createEntity(em);
            em.persist(accountLedger);
            em.flush();
        } else {
            accountLedger = TestUtil.findAll(em, TransactionAccountLedger.class).get(0);
        }
        em.persist(accountLedger);
        em.flush();
        transactionAccount.setAccountLedger(accountLedger);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long accountLedgerId = accountLedger.getId();

        // Get all the transactionAccountList where accountLedger equals to accountLedgerId
        defaultTransactionAccountShouldBeFound("accountLedgerId.equals=" + accountLedgerId);

        // Get all the transactionAccountList where accountLedger equals to (accountLedgerId + 1)
        defaultTransactionAccountShouldNotBeFound("accountLedgerId.equals=" + (accountLedgerId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByAccountCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        TransactionAccountCategory accountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            accountCategory = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(accountCategory);
            em.flush();
        } else {
            accountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        em.persist(accountCategory);
        em.flush();
        transactionAccount.setAccountCategory(accountCategory);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long accountCategoryId = accountCategory.getId();

        // Get all the transactionAccountList where accountCategory equals to accountCategoryId
        defaultTransactionAccountShouldBeFound("accountCategoryId.equals=" + accountCategoryId);

        // Get all the transactionAccountList where accountCategory equals to (accountCategoryId + 1)
        defaultTransactionAccountShouldNotBeFound("accountCategoryId.equals=" + (accountCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
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
        transactionAccount.addPlaceholder(placeholder);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long placeholderId = placeholder.getId();

        // Get all the transactionAccountList where placeholder equals to placeholderId
        defaultTransactionAccountShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionAccountList where placeholder equals to (placeholderId + 1)
        defaultTransactionAccountShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(serviceOutlet);
        em.flush();
        transactionAccount.setServiceOutlet(serviceOutlet);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the transactionAccountList where serviceOutlet equals to serviceOutletId
        defaultTransactionAccountShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the transactionAccountList where serviceOutlet equals to (serviceOutletId + 1)
        defaultTransactionAccountShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        transactionAccount.setSettlementCurrency(settlementCurrency);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the transactionAccountList where settlementCurrency equals to settlementCurrencyId
        defaultTransactionAccountShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the transactionAccountList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultTransactionAccountShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountsByInstitutionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        ReportingEntity institution;
        if (TestUtil.findAll(em, ReportingEntity.class).isEmpty()) {
            institution = ReportingEntityResourceIT.createEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, ReportingEntity.class).get(0);
        }
        em.persist(institution);
        em.flush();
        transactionAccount.setInstitution(institution);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long institutionId = institution.getId();

        // Get all the transactionAccountList where institution equals to institutionId
        defaultTransactionAccountShouldBeFound("institutionId.equals=" + institutionId);

        // Get all the transactionAccountList where institution equals to (institutionId + 1)
        defaultTransactionAccountShouldNotBeFound("institutionId.equals=" + (institutionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountShouldBeFound(String filter) throws Exception {
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountSubType").value(hasItem(DEFAULT_ACCOUNT_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dummyAccount").value(hasItem(DEFAULT_DUMMY_ACCOUNT.booleanValue())));

        // Check, that the count call also returns 1
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccount() throws Exception {
        // Get the transactionAccount
        restTransactionAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccount() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();

        // Update the transactionAccount
        TransactionAccount updatedTransactionAccount = transactionAccountRepository.findById(transactionAccount.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionAccount are not directly saved in db
        em.detach(updatedTransactionAccount);
        updatedTransactionAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountSubType(UPDATED_ACCOUNT_SUB_TYPE)
            .dummyAccount(UPDATED_DUMMY_ACCOUNT);
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(updatedTransactionAccount);

        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccount testTransactionAccount = transactionAccountList.get(transactionAccountList.size() - 1);
        assertThat(testTransactionAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testTransactionAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testTransactionAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTransactionAccount.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testTransactionAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testTransactionAccount.getAccountSubType()).isEqualTo(UPDATED_ACCOUNT_SUB_TYPE);
        assertThat(testTransactionAccount.getDummyAccount()).isEqualTo(UPDATED_DUMMY_ACCOUNT);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository).save(testTransactionAccount);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountWithPatch() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();

        // Update the transactionAccount using partial update
        TransactionAccount partialUpdatedTransactionAccount = new TransactionAccount();
        partialUpdatedTransactionAccount.setId(transactionAccount.getId());

        partialUpdatedTransactionAccount
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .accountSubType(UPDATED_ACCOUNT_SUB_TYPE)
            .dummyAccount(UPDATED_DUMMY_ACCOUNT);

        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccount))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccount testTransactionAccount = transactionAccountList.get(transactionAccountList.size() - 1);
        assertThat(testTransactionAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testTransactionAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testTransactionAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTransactionAccount.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testTransactionAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testTransactionAccount.getAccountSubType()).isEqualTo(UPDATED_ACCOUNT_SUB_TYPE);
        assertThat(testTransactionAccount.getDummyAccount()).isEqualTo(UPDATED_DUMMY_ACCOUNT);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountWithPatch() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();

        // Update the transactionAccount using partial update
        TransactionAccount partialUpdatedTransactionAccount = new TransactionAccount();
        partialUpdatedTransactionAccount.setId(transactionAccount.getId());

        partialUpdatedTransactionAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountSubType(UPDATED_ACCOUNT_SUB_TYPE)
            .dummyAccount(UPDATED_DUMMY_ACCOUNT);

        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccount))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccount testTransactionAccount = transactionAccountList.get(transactionAccountList.size() - 1);
        assertThat(testTransactionAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testTransactionAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testTransactionAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTransactionAccount.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testTransactionAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testTransactionAccount.getAccountSubType()).isEqualTo(UPDATED_ACCOUNT_SUB_TYPE);
        assertThat(testTransactionAccount.getDummyAccount()).isEqualTo(UPDATED_DUMMY_ACCOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccount() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountRepository.findAll().size();
        transactionAccount.setId(count.incrementAndGet());

        // Create the TransactionAccount
        TransactionAccountDTO transactionAccountDTO = transactionAccountMapper.toDto(transactionAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccount in the database
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(0)).save(transactionAccount);
    }

    @Test
    @Transactional
    void deleteTransactionAccount() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);

        int databaseSizeBeforeDelete = transactionAccountRepository.findAll().size();

        // Delete the transactionAccount
        restTransactionAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccount> transactionAccountList = transactionAccountRepository.findAll();
        assertThat(transactionAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccount in Elasticsearch
        verify(mockTransactionAccountSearchRepository, times(1)).deleteById(transactionAccount.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccount() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        when(mockTransactionAccountSearchRepository.search("id:" + transactionAccount.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccount), PageRequest.of(0, 1), 1));

        // Search the transactionAccount
        restTransactionAccountMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountSubType").value(hasItem(DEFAULT_ACCOUNT_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dummyAccount").value(hasItem(DEFAULT_DUMMY_ACCOUNT.booleanValue())));
    }
}
