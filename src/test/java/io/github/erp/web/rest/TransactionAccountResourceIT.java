package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionAccount;
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
            .notesContentType(DEFAULT_NOTES_CONTENT_TYPE);
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
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE);
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
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))));
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
            .andExpect(jsonPath("$.notes").value(Base64Utils.encodeToString(DEFAULT_NOTES)));
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
    void getAllTransactionAccountsByParentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountRepository.saveAndFlush(transactionAccount);
        TransactionAccount parentAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            parentAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(parentAccount);
            em.flush();
        } else {
            parentAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(parentAccount);
        em.flush();
        transactionAccount.setParentAccount(parentAccount);
        transactionAccountRepository.saveAndFlush(transactionAccount);
        Long parentAccountId = parentAccount.getId();

        // Get all the transactionAccountList where parentAccount equals to parentAccountId
        defaultTransactionAccountShouldBeFound("parentAccountId.equals=" + parentAccountId);

        // Get all the transactionAccountList where parentAccount equals to (parentAccountId + 1)
        defaultTransactionAccountShouldNotBeFound("parentAccountId.equals=" + (parentAccountId + 1));
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
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))));

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
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE);
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

        partialUpdatedTransactionAccount.notes(UPDATED_NOTES).notesContentType(UPDATED_NOTES_CONTENT_TYPE);

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
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE);

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
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))));
    }
}
