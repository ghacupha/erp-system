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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TARecognitionROURule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TARecognitionROURuleRepository;
import io.github.erp.repository.search.TARecognitionROURuleSearchRepository;
import io.github.erp.service.TARecognitionROURuleService;
import io.github.erp.service.criteria.TARecognitionROURuleCriteria;
import io.github.erp.service.dto.TARecognitionROURuleDTO;
import io.github.erp.service.mapper.TARecognitionROURuleMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link TARecognitionROURuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TARecognitionROURuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ta-recognition-rou-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-recognition-rou-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TARecognitionROURuleRepository tARecognitionROURuleRepository;

    @Mock
    private TARecognitionROURuleRepository tARecognitionROURuleRepositoryMock;

    @Autowired
    private TARecognitionROURuleMapper tARecognitionROURuleMapper;

    @Mock
    private TARecognitionROURuleService tARecognitionROURuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TARecognitionROURuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TARecognitionROURuleSearchRepository mockTARecognitionROURuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTARecognitionROURuleMockMvc;

    private TARecognitionROURule tARecognitionROURule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TARecognitionROURule createEntity(EntityManager em) {
        TARecognitionROURule tARecognitionROURule = new TARecognitionROURule().name(DEFAULT_NAME).identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tARecognitionROURule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tARecognitionROURule.setDebit(transactionAccount);
        // Add required entity
        tARecognitionROURule.setCredit(transactionAccount);
        return tARecognitionROURule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TARecognitionROURule createUpdatedEntity(EntityManager em) {
        TARecognitionROURule tARecognitionROURule = new TARecognitionROURule().name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tARecognitionROURule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tARecognitionROURule.setDebit(transactionAccount);
        // Add required entity
        tARecognitionROURule.setCredit(transactionAccount);
        return tARecognitionROURule;
    }

    @BeforeEach
    public void initTest() {
        tARecognitionROURule = createEntity(em);
    }

    @Test
    @Transactional
    void createTARecognitionROURule() throws Exception {
        int databaseSizeBeforeCreate = tARecognitionROURuleRepository.findAll().size();
        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);
        restTARecognitionROURuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeCreate + 1);
        TARecognitionROURule testTARecognitionROURule = tARecognitionROURuleList.get(tARecognitionROURuleList.size() - 1);
        assertThat(testTARecognitionROURule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTARecognitionROURule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(1)).save(testTARecognitionROURule);
    }

    @Test
    @Transactional
    void createTARecognitionROURuleWithExistingId() throws Exception {
        // Create the TARecognitionROURule with an existing ID
        tARecognitionROURule.setId(1L);
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        int databaseSizeBeforeCreate = tARecognitionROURuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTARecognitionROURuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tARecognitionROURuleRepository.findAll().size();
        // set the field null
        tARecognitionROURule.setName(null);

        // Create the TARecognitionROURule, which fails.
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        restTARecognitionROURuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tARecognitionROURuleRepository.findAll().size();
        // set the field null
        tARecognitionROURule.setIdentifier(null);

        // Create the TARecognitionROURule, which fails.
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        restTARecognitionROURuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURules() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tARecognitionROURule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTARecognitionROURulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tARecognitionROURuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTARecognitionROURuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tARecognitionROURuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTARecognitionROURulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tARecognitionROURuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTARecognitionROURuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tARecognitionROURuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTARecognitionROURule() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get the tARecognitionROURule
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tARecognitionROURule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tARecognitionROURule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTARecognitionROURulesByIdFiltering() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        Long id = tARecognitionROURule.getId();

        defaultTARecognitionROURuleShouldBeFound("id.equals=" + id);
        defaultTARecognitionROURuleShouldNotBeFound("id.notEquals=" + id);

        defaultTARecognitionROURuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTARecognitionROURuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTARecognitionROURuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTARecognitionROURuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name equals to DEFAULT_NAME
        defaultTARecognitionROURuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tARecognitionROURuleList where name equals to UPDATED_NAME
        defaultTARecognitionROURuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name not equals to DEFAULT_NAME
        defaultTARecognitionROURuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tARecognitionROURuleList where name not equals to UPDATED_NAME
        defaultTARecognitionROURuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTARecognitionROURuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tARecognitionROURuleList where name equals to UPDATED_NAME
        defaultTARecognitionROURuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name is not null
        defaultTARecognitionROURuleShouldBeFound("name.specified=true");

        // Get all the tARecognitionROURuleList where name is null
        defaultTARecognitionROURuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name contains DEFAULT_NAME
        defaultTARecognitionROURuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tARecognitionROURuleList where name contains UPDATED_NAME
        defaultTARecognitionROURuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where name does not contain DEFAULT_NAME
        defaultTARecognitionROURuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tARecognitionROURuleList where name does not contain UPDATED_NAME
        defaultTARecognitionROURuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTARecognitionROURuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tARecognitionROURuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTARecognitionROURuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTARecognitionROURuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tARecognitionROURuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTARecognitionROURuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTARecognitionROURuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tARecognitionROURuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTARecognitionROURuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        // Get all the tARecognitionROURuleList where identifier is not null
        defaultTARecognitionROURuleShouldBeFound("identifier.specified=true");

        // Get all the tARecognitionROURuleList where identifier is null
        defaultTARecognitionROURuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tARecognitionROURule.getLeaseContract();
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tARecognitionROURuleList where leaseContract equals to leaseContractId
        defaultTARecognitionROURuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tARecognitionROURuleList where leaseContract equals to (leaseContractId + 1)
        defaultTARecognitionROURuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        TransactionAccount debit;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            debit = TransactionAccountResourceIT.createEntity(em);
            em.persist(debit);
            em.flush();
        } else {
            debit = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(debit);
        em.flush();
        tARecognitionROURule.setDebit(debit);
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        Long debitId = debit.getId();

        // Get all the tARecognitionROURuleList where debit equals to debitId
        defaultTARecognitionROURuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tARecognitionROURuleList where debit equals to (debitId + 1)
        defaultTARecognitionROURuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        TransactionAccount credit;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            credit = TransactionAccountResourceIT.createEntity(em);
            em.persist(credit);
            em.flush();
        } else {
            credit = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(credit);
        em.flush();
        tARecognitionROURule.setCredit(credit);
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        Long creditId = credit.getId();

        // Get all the tARecognitionROURuleList where credit equals to creditId
        defaultTARecognitionROURuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tARecognitionROURuleList where credit equals to (creditId + 1)
        defaultTARecognitionROURuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTARecognitionROURulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
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
        tARecognitionROURule.addPlaceholder(placeholder);
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        Long placeholderId = placeholder.getId();

        // Get all the tARecognitionROURuleList where placeholder equals to placeholderId
        defaultTARecognitionROURuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tARecognitionROURuleList where placeholder equals to (placeholderId + 1)
        defaultTARecognitionROURuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTARecognitionROURuleShouldBeFound(String filter) throws Exception {
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tARecognitionROURule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTARecognitionROURuleShouldNotBeFound(String filter) throws Exception {
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTARecognitionROURule() throws Exception {
        // Get the tARecognitionROURule
        restTARecognitionROURuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTARecognitionROURule() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();

        // Update the tARecognitionROURule
        TARecognitionROURule updatedTARecognitionROURule = tARecognitionROURuleRepository.findById(tARecognitionROURule.getId()).get();
        // Disconnect from session so that the updates on updatedTARecognitionROURule are not directly saved in db
        em.detach(updatedTARecognitionROURule);
        updatedTARecognitionROURule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(updatedTARecognitionROURule);

        restTARecognitionROURuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tARecognitionROURuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);
        TARecognitionROURule testTARecognitionROURule = tARecognitionROURuleList.get(tARecognitionROURuleList.size() - 1);
        assertThat(testTARecognitionROURule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTARecognitionROURule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository).save(testTARecognitionROURule);
    }

    @Test
    @Transactional
    void putNonExistingTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tARecognitionROURuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void partialUpdateTARecognitionROURuleWithPatch() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();

        // Update the tARecognitionROURule using partial update
        TARecognitionROURule partialUpdatedTARecognitionROURule = new TARecognitionROURule();
        partialUpdatedTARecognitionROURule.setId(tARecognitionROURule.getId());

        partialUpdatedTARecognitionROURule.identifier(UPDATED_IDENTIFIER);

        restTARecognitionROURuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTARecognitionROURule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTARecognitionROURule))
            )
            .andExpect(status().isOk());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);
        TARecognitionROURule testTARecognitionROURule = tARecognitionROURuleList.get(tARecognitionROURuleList.size() - 1);
        assertThat(testTARecognitionROURule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTARecognitionROURule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTARecognitionROURuleWithPatch() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();

        // Update the tARecognitionROURule using partial update
        TARecognitionROURule partialUpdatedTARecognitionROURule = new TARecognitionROURule();
        partialUpdatedTARecognitionROURule.setId(tARecognitionROURule.getId());

        partialUpdatedTARecognitionROURule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTARecognitionROURuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTARecognitionROURule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTARecognitionROURule))
            )
            .andExpect(status().isOk());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);
        TARecognitionROURule testTARecognitionROURule = tARecognitionROURuleList.get(tARecognitionROURuleList.size() - 1);
        assertThat(testTARecognitionROURule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTARecognitionROURule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tARecognitionROURuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTARecognitionROURule() throws Exception {
        int databaseSizeBeforeUpdate = tARecognitionROURuleRepository.findAll().size();
        tARecognitionROURule.setId(count.incrementAndGet());

        // Create the TARecognitionROURule
        TARecognitionROURuleDTO tARecognitionROURuleDTO = tARecognitionROURuleMapper.toDto(tARecognitionROURule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTARecognitionROURuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tARecognitionROURuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TARecognitionROURule in the database
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(0)).save(tARecognitionROURule);
    }

    @Test
    @Transactional
    void deleteTARecognitionROURule() throws Exception {
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);

        int databaseSizeBeforeDelete = tARecognitionROURuleRepository.findAll().size();

        // Delete the tARecognitionROURule
        restTARecognitionROURuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tARecognitionROURule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TARecognitionROURule> tARecognitionROURuleList = tARecognitionROURuleRepository.findAll();
        assertThat(tARecognitionROURuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TARecognitionROURule in Elasticsearch
        verify(mockTARecognitionROURuleSearchRepository, times(1)).deleteById(tARecognitionROURule.getId());
    }

    @Test
    @Transactional
    void searchTARecognitionROURule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tARecognitionROURuleRepository.saveAndFlush(tARecognitionROURule);
        when(mockTARecognitionROURuleSearchRepository.search("id:" + tARecognitionROURule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tARecognitionROURule), PageRequest.of(0, 1), 1));

        // Search the tARecognitionROURule
        restTARecognitionROURuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tARecognitionROURule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tARecognitionROURule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
