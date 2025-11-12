package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TAAmortizationRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TAAmortizationRuleRepository;
import io.github.erp.repository.search.TAAmortizationRuleSearchRepository;
import io.github.erp.service.TAAmortizationRuleService;
import io.github.erp.service.dto.TAAmortizationRuleDTO;
import io.github.erp.service.mapper.TAAmortizationRuleMapper;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the TAAmortizationRuleResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"BOOK_KEEPING", "FIXED_ASSETS_USER"})
class TAAmortizationRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/accounts/ta-amortization-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/accounts/_search/ta-amortization-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TAAmortizationRuleRepository tAAmortizationRuleRepository;

    @Mock
    private TAAmortizationRuleRepository tAAmortizationRuleRepositoryMock;

    @Autowired
    private TAAmortizationRuleMapper tAAmortizationRuleMapper;

    @Mock
    private TAAmortizationRuleService tAAmortizationRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TAAmortizationRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TAAmortizationRuleSearchRepository mockTAAmortizationRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTAAmortizationRuleMockMvc;

    private TAAmortizationRule tAAmortizationRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TAAmortizationRule createEntity(EntityManager em) {
        TAAmortizationRule tAAmortizationRule = new TAAmortizationRule().name(DEFAULT_NAME).identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tAAmortizationRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tAAmortizationRule.setDebit(transactionAccount);
        // Add required entity
        tAAmortizationRule.setCredit(transactionAccount);
        return tAAmortizationRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TAAmortizationRule createUpdatedEntity(EntityManager em) {
        TAAmortizationRule tAAmortizationRule = new TAAmortizationRule().name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tAAmortizationRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tAAmortizationRule.setDebit(transactionAccount);
        // Add required entity
        tAAmortizationRule.setCredit(transactionAccount);
        return tAAmortizationRule;
    }

    @BeforeEach
    public void initTest() {
        tAAmortizationRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTAAmortizationRule() throws Exception {
        int databaseSizeBeforeCreate = tAAmortizationRuleRepository.findAll().size();
        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);
        restTAAmortizationRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TAAmortizationRule testTAAmortizationRule = tAAmortizationRuleList.get(tAAmortizationRuleList.size() - 1);
        assertThat(testTAAmortizationRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTAAmortizationRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(1)).save(testTAAmortizationRule);
    }

    @Test
    @Transactional
    void createTAAmortizationRuleWithExistingId() throws Exception {
        // Create the TAAmortizationRule with an existing ID
        tAAmortizationRule.setId(1L);
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        int databaseSizeBeforeCreate = tAAmortizationRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTAAmortizationRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tAAmortizationRuleRepository.findAll().size();
        // set the field null
        tAAmortizationRule.setName(null);

        // Create the TAAmortizationRule, which fails.
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        restTAAmortizationRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tAAmortizationRuleRepository.findAll().size();
        // set the field null
        tAAmortizationRule.setIdentifier(null);

        // Create the TAAmortizationRule, which fails.
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        restTAAmortizationRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRules() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAAmortizationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTAAmortizationRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tAAmortizationRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTAAmortizationRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tAAmortizationRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTAAmortizationRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tAAmortizationRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTAAmortizationRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tAAmortizationRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTAAmortizationRule() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get the tAAmortizationRule
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tAAmortizationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tAAmortizationRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTAAmortizationRulesByIdFiltering() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        Long id = tAAmortizationRule.getId();

        defaultTAAmortizationRuleShouldBeFound("id.equals=" + id);
        defaultTAAmortizationRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTAAmortizationRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTAAmortizationRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTAAmortizationRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTAAmortizationRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name equals to DEFAULT_NAME
        defaultTAAmortizationRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tAAmortizationRuleList where name equals to UPDATED_NAME
        defaultTAAmortizationRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name not equals to DEFAULT_NAME
        defaultTAAmortizationRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tAAmortizationRuleList where name not equals to UPDATED_NAME
        defaultTAAmortizationRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTAAmortizationRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tAAmortizationRuleList where name equals to UPDATED_NAME
        defaultTAAmortizationRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name is not null
        defaultTAAmortizationRuleShouldBeFound("name.specified=true");

        // Get all the tAAmortizationRuleList where name is null
        defaultTAAmortizationRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name contains DEFAULT_NAME
        defaultTAAmortizationRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tAAmortizationRuleList where name contains UPDATED_NAME
        defaultTAAmortizationRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where name does not contain DEFAULT_NAME
        defaultTAAmortizationRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tAAmortizationRuleList where name does not contain UPDATED_NAME
        defaultTAAmortizationRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTAAmortizationRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tAAmortizationRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTAAmortizationRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTAAmortizationRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tAAmortizationRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTAAmortizationRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTAAmortizationRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tAAmortizationRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTAAmortizationRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        // Get all the tAAmortizationRuleList where identifier is not null
        defaultTAAmortizationRuleShouldBeFound("identifier.specified=true");

        // Get all the tAAmortizationRuleList where identifier is null
        defaultTAAmortizationRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tAAmortizationRule.getLeaseContract();
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tAAmortizationRuleList where leaseContract equals to leaseContractId
        defaultTAAmortizationRuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tAAmortizationRuleList where leaseContract equals to (leaseContractId + 1)
        defaultTAAmortizationRuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
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
        tAAmortizationRule.setDebit(debit);
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
        Long debitId = debit.getId();

        // Get all the tAAmortizationRuleList where debit equals to debitId
        defaultTAAmortizationRuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tAAmortizationRuleList where debit equals to (debitId + 1)
        defaultTAAmortizationRuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
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
        tAAmortizationRule.setCredit(credit);
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
        Long creditId = credit.getId();

        // Get all the tAAmortizationRuleList where credit equals to creditId
        defaultTAAmortizationRuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tAAmortizationRuleList where credit equals to (creditId + 1)
        defaultTAAmortizationRuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTAAmortizationRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
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
        tAAmortizationRule.addPlaceholder(placeholder);
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
        Long placeholderId = placeholder.getId();

        // Get all the tAAmortizationRuleList where placeholder equals to placeholderId
        defaultTAAmortizationRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tAAmortizationRuleList where placeholder equals to (placeholderId + 1)
        defaultTAAmortizationRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTAAmortizationRuleShouldBeFound(String filter) throws Exception {
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAAmortizationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTAAmortizationRuleShouldNotBeFound(String filter) throws Exception {
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTAAmortizationRule() throws Exception {
        // Get the tAAmortizationRule
        restTAAmortizationRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTAAmortizationRule() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();

        // Update the tAAmortizationRule
        TAAmortizationRule updatedTAAmortizationRule = tAAmortizationRuleRepository.findById(tAAmortizationRule.getId()).get();
        // Disconnect from session so that the updates on updatedTAAmortizationRule are not directly saved in db
        em.detach(updatedTAAmortizationRule);
        updatedTAAmortizationRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(updatedTAAmortizationRule);

        restTAAmortizationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tAAmortizationRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);
        TAAmortizationRule testTAAmortizationRule = tAAmortizationRuleList.get(tAAmortizationRuleList.size() - 1);
        assertThat(testTAAmortizationRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTAAmortizationRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository).save(testTAAmortizationRule);
    }

    @Test
    @Transactional
    void putNonExistingTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tAAmortizationRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void partialUpdateTAAmortizationRuleWithPatch() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();

        // Update the tAAmortizationRule using partial update
        TAAmortizationRule partialUpdatedTAAmortizationRule = new TAAmortizationRule();
        partialUpdatedTAAmortizationRule.setId(tAAmortizationRule.getId());

        restTAAmortizationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTAAmortizationRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTAAmortizationRule))
            )
            .andExpect(status().isOk());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);
        TAAmortizationRule testTAAmortizationRule = tAAmortizationRuleList.get(tAAmortizationRuleList.size() - 1);
        assertThat(testTAAmortizationRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTAAmortizationRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTAAmortizationRuleWithPatch() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();

        // Update the tAAmortizationRule using partial update
        TAAmortizationRule partialUpdatedTAAmortizationRule = new TAAmortizationRule();
        partialUpdatedTAAmortizationRule.setId(tAAmortizationRule.getId());

        partialUpdatedTAAmortizationRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTAAmortizationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTAAmortizationRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTAAmortizationRule))
            )
            .andExpect(status().isOk());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);
        TAAmortizationRule testTAAmortizationRule = tAAmortizationRuleList.get(tAAmortizationRuleList.size() - 1);
        assertThat(testTAAmortizationRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTAAmortizationRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tAAmortizationRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTAAmortizationRule() throws Exception {
        int databaseSizeBeforeUpdate = tAAmortizationRuleRepository.findAll().size();
        tAAmortizationRule.setId(count.incrementAndGet());

        // Create the TAAmortizationRule
        TAAmortizationRuleDTO tAAmortizationRuleDTO = tAAmortizationRuleMapper.toDto(tAAmortizationRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAAmortizationRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAAmortizationRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TAAmortizationRule in the database
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(0)).save(tAAmortizationRule);
    }

    @Test
    @Transactional
    void deleteTAAmortizationRule() throws Exception {
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);

        int databaseSizeBeforeDelete = tAAmortizationRuleRepository.findAll().size();

        // Delete the tAAmortizationRule
        restTAAmortizationRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tAAmortizationRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TAAmortizationRule> tAAmortizationRuleList = tAAmortizationRuleRepository.findAll();
        assertThat(tAAmortizationRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TAAmortizationRule in Elasticsearch
        verify(mockTAAmortizationRuleSearchRepository, times(1)).deleteById(tAAmortizationRule.getId());
    }

    @Test
    @Transactional
    void searchTAAmortizationRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tAAmortizationRuleRepository.saveAndFlush(tAAmortizationRule);
        when(mockTAAmortizationRuleSearchRepository.search("id:" + tAAmortizationRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tAAmortizationRule), PageRequest.of(0, 1), 1));

        // Search the tAAmortizationRule
        restTAAmortizationRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tAAmortizationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAAmortizationRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
