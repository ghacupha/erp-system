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
import io.github.erp.domain.TransactionAccountLedger;
import io.github.erp.repository.TransactionAccountLedgerRepository;
import io.github.erp.repository.search.TransactionAccountLedgerSearchRepository;
import io.github.erp.service.TransactionAccountLedgerService;
import io.github.erp.service.criteria.TransactionAccountLedgerCriteria;
import io.github.erp.service.dto.TransactionAccountLedgerDTO;
import io.github.erp.service.mapper.TransactionAccountLedgerMapper;
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
 * Integration tests for the {@link TransactionAccountLedgerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountLedgerResourceIT {

    private static final String DEFAULT_LEDGER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LEDGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-account-ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-ledgers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountLedgerRepository transactionAccountLedgerRepository;

    @Mock
    private TransactionAccountLedgerRepository transactionAccountLedgerRepositoryMock;

    @Autowired
    private TransactionAccountLedgerMapper transactionAccountLedgerMapper;

    @Mock
    private TransactionAccountLedgerService transactionAccountLedgerServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountLedgerSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountLedgerSearchRepository mockTransactionAccountLedgerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountLedgerMockMvc;

    private TransactionAccountLedger transactionAccountLedger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountLedger createEntity(EntityManager em) {
        TransactionAccountLedger transactionAccountLedger = new TransactionAccountLedger()
            .ledgerCode(DEFAULT_LEDGER_CODE)
            .ledgerName(DEFAULT_LEDGER_NAME);
        return transactionAccountLedger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountLedger createUpdatedEntity(EntityManager em) {
        TransactionAccountLedger transactionAccountLedger = new TransactionAccountLedger()
            .ledgerCode(UPDATED_LEDGER_CODE)
            .ledgerName(UPDATED_LEDGER_NAME);
        return transactionAccountLedger;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountLedger = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountLedgerRepository.findAll().size();
        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);
        restTransactionAccountLedgerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccountLedger testTransactionAccountLedger = transactionAccountLedgerList.get(transactionAccountLedgerList.size() - 1);
        assertThat(testTransactionAccountLedger.getLedgerCode()).isEqualTo(DEFAULT_LEDGER_CODE);
        assertThat(testTransactionAccountLedger.getLedgerName()).isEqualTo(DEFAULT_LEDGER_NAME);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(1)).save(testTransactionAccountLedger);
    }

    @Test
    @Transactional
    void createTransactionAccountLedgerWithExistingId() throws Exception {
        // Create the TransactionAccountLedger with an existing ID
        transactionAccountLedger.setId(1L);
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        int databaseSizeBeforeCreate = transactionAccountLedgerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountLedgerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void checkLedgerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountLedgerRepository.findAll().size();
        // set the field null
        transactionAccountLedger.setLedgerCode(null);

        // Create the TransactionAccountLedger, which fails.
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        restTransactionAccountLedgerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLedgerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountLedgerRepository.findAll().size();
        // set the field null
        transactionAccountLedger.setLedgerName(null);

        // Create the TransactionAccountLedger, which fails.
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        restTransactionAccountLedgerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgers() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].ledgerCode").value(hasItem(DEFAULT_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].ledgerName").value(hasItem(DEFAULT_LEDGER_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountLedgersWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionAccountLedgerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountLedgerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountLedgerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountLedgersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionAccountLedgerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountLedgerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountLedgerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionAccountLedger() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get the transactionAccountLedger
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountLedger.getId().intValue()))
            .andExpect(jsonPath("$.ledgerCode").value(DEFAULT_LEDGER_CODE))
            .andExpect(jsonPath("$.ledgerName").value(DEFAULT_LEDGER_NAME));
    }

    @Test
    @Transactional
    void getTransactionAccountLedgersByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        Long id = transactionAccountLedger.getId();

        defaultTransactionAccountLedgerShouldBeFound("id.equals=" + id);
        defaultTransactionAccountLedgerShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountLedgerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountLedgerShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountLedgerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountLedgerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode equals to DEFAULT_LEDGER_CODE
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.equals=" + DEFAULT_LEDGER_CODE);

        // Get all the transactionAccountLedgerList where ledgerCode equals to UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.equals=" + UPDATED_LEDGER_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode not equals to DEFAULT_LEDGER_CODE
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.notEquals=" + DEFAULT_LEDGER_CODE);

        // Get all the transactionAccountLedgerList where ledgerCode not equals to UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.notEquals=" + UPDATED_LEDGER_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode in DEFAULT_LEDGER_CODE or UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.in=" + DEFAULT_LEDGER_CODE + "," + UPDATED_LEDGER_CODE);

        // Get all the transactionAccountLedgerList where ledgerCode equals to UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.in=" + UPDATED_LEDGER_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode is not null
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.specified=true");

        // Get all the transactionAccountLedgerList where ledgerCode is null
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode contains DEFAULT_LEDGER_CODE
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.contains=" + DEFAULT_LEDGER_CODE);

        // Get all the transactionAccountLedgerList where ledgerCode contains UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.contains=" + UPDATED_LEDGER_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerCode does not contain DEFAULT_LEDGER_CODE
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerCode.doesNotContain=" + DEFAULT_LEDGER_CODE);

        // Get all the transactionAccountLedgerList where ledgerCode does not contain UPDATED_LEDGER_CODE
        defaultTransactionAccountLedgerShouldBeFound("ledgerCode.doesNotContain=" + UPDATED_LEDGER_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName equals to DEFAULT_LEDGER_NAME
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.equals=" + DEFAULT_LEDGER_NAME);

        // Get all the transactionAccountLedgerList where ledgerName equals to UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.equals=" + UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName not equals to DEFAULT_LEDGER_NAME
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.notEquals=" + DEFAULT_LEDGER_NAME);

        // Get all the transactionAccountLedgerList where ledgerName not equals to UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.notEquals=" + UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName in DEFAULT_LEDGER_NAME or UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.in=" + DEFAULT_LEDGER_NAME + "," + UPDATED_LEDGER_NAME);

        // Get all the transactionAccountLedgerList where ledgerName equals to UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.in=" + UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName is not null
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.specified=true");

        // Get all the transactionAccountLedgerList where ledgerName is null
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName contains DEFAULT_LEDGER_NAME
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.contains=" + DEFAULT_LEDGER_NAME);

        // Get all the transactionAccountLedgerList where ledgerName contains UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.contains=" + UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByLedgerNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        // Get all the transactionAccountLedgerList where ledgerName does not contain DEFAULT_LEDGER_NAME
        defaultTransactionAccountLedgerShouldNotBeFound("ledgerName.doesNotContain=" + DEFAULT_LEDGER_NAME);

        // Get all the transactionAccountLedgerList where ledgerName does not contain UPDATED_LEDGER_NAME
        defaultTransactionAccountLedgerShouldBeFound("ledgerName.doesNotContain=" + UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountLedgersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);
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
        transactionAccountLedger.addPlaceholder(placeholder);
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);
        Long placeholderId = placeholder.getId();

        // Get all the transactionAccountLedgerList where placeholder equals to placeholderId
        defaultTransactionAccountLedgerShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionAccountLedgerList where placeholder equals to (placeholderId + 1)
        defaultTransactionAccountLedgerShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountLedgerShouldBeFound(String filter) throws Exception {
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].ledgerCode").value(hasItem(DEFAULT_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].ledgerName").value(hasItem(DEFAULT_LEDGER_NAME)));

        // Check, that the count call also returns 1
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountLedgerShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountLedger() throws Exception {
        // Get the transactionAccountLedger
        restTransactionAccountLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccountLedger() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();

        // Update the transactionAccountLedger
        TransactionAccountLedger updatedTransactionAccountLedger = transactionAccountLedgerRepository
            .findById(transactionAccountLedger.getId())
            .get();
        // Disconnect from session so that the updates on updatedTransactionAccountLedger are not directly saved in db
        em.detach(updatedTransactionAccountLedger);
        updatedTransactionAccountLedger.ledgerCode(UPDATED_LEDGER_CODE).ledgerName(UPDATED_LEDGER_NAME);
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(updatedTransactionAccountLedger);

        restTransactionAccountLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountLedger testTransactionAccountLedger = transactionAccountLedgerList.get(transactionAccountLedgerList.size() - 1);
        assertThat(testTransactionAccountLedger.getLedgerCode()).isEqualTo(UPDATED_LEDGER_CODE);
        assertThat(testTransactionAccountLedger.getLedgerName()).isEqualTo(UPDATED_LEDGER_NAME);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository).save(testTransactionAccountLedger);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountLedgerWithPatch() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();

        // Update the transactionAccountLedger using partial update
        TransactionAccountLedger partialUpdatedTransactionAccountLedger = new TransactionAccountLedger();
        partialUpdatedTransactionAccountLedger.setId(transactionAccountLedger.getId());

        restTransactionAccountLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountLedger))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountLedger testTransactionAccountLedger = transactionAccountLedgerList.get(transactionAccountLedgerList.size() - 1);
        assertThat(testTransactionAccountLedger.getLedgerCode()).isEqualTo(DEFAULT_LEDGER_CODE);
        assertThat(testTransactionAccountLedger.getLedgerName()).isEqualTo(DEFAULT_LEDGER_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountLedgerWithPatch() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();

        // Update the transactionAccountLedger using partial update
        TransactionAccountLedger partialUpdatedTransactionAccountLedger = new TransactionAccountLedger();
        partialUpdatedTransactionAccountLedger.setId(transactionAccountLedger.getId());

        partialUpdatedTransactionAccountLedger.ledgerCode(UPDATED_LEDGER_CODE).ledgerName(UPDATED_LEDGER_NAME);

        restTransactionAccountLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountLedger))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountLedger testTransactionAccountLedger = transactionAccountLedgerList.get(transactionAccountLedgerList.size() - 1);
        assertThat(testTransactionAccountLedger.getLedgerCode()).isEqualTo(UPDATED_LEDGER_CODE);
        assertThat(testTransactionAccountLedger.getLedgerName()).isEqualTo(UPDATED_LEDGER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountLedgerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccountLedger() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountLedgerRepository.findAll().size();
        transactionAccountLedger.setId(count.incrementAndGet());

        // Create the TransactionAccountLedger
        TransactionAccountLedgerDTO transactionAccountLedgerDTO = transactionAccountLedgerMapper.toDto(transactionAccountLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountLedgerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountLedger in the database
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(0)).save(transactionAccountLedger);
    }

    @Test
    @Transactional
    void deleteTransactionAccountLedger() throws Exception {
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);

        int databaseSizeBeforeDelete = transactionAccountLedgerRepository.findAll().size();

        // Delete the transactionAccountLedger
        restTransactionAccountLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccountLedger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccountLedger> transactionAccountLedgerList = transactionAccountLedgerRepository.findAll();
        assertThat(transactionAccountLedgerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccountLedger in Elasticsearch
        verify(mockTransactionAccountLedgerSearchRepository, times(1)).deleteById(transactionAccountLedger.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccountLedger() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountLedgerRepository.saveAndFlush(transactionAccountLedger);
        when(mockTransactionAccountLedgerSearchRepository.search("id:" + transactionAccountLedger.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountLedger), PageRequest.of(0, 1), 1));

        // Search the transactionAccountLedger
        restTransactionAccountLedgerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].ledgerCode").value(hasItem(DEFAULT_LEDGER_CODE)))
            .andExpect(jsonPath("$.[*].ledgerName").value(hasItem(DEFAULT_LEDGER_NAME)));
    }
}
