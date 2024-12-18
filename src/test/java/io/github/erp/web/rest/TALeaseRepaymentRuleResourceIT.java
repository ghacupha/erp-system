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
import io.github.erp.domain.TALeaseRepaymentRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TALeaseRepaymentRuleRepository;
import io.github.erp.repository.search.TALeaseRepaymentRuleSearchRepository;
import io.github.erp.service.TALeaseRepaymentRuleService;
import io.github.erp.service.criteria.TALeaseRepaymentRuleCriteria;
import io.github.erp.service.dto.TALeaseRepaymentRuleDTO;
import io.github.erp.service.mapper.TALeaseRepaymentRuleMapper;
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
 * Integration tests for the {@link TALeaseRepaymentRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TALeaseRepaymentRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ta-lease-repayment-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-lease-repayment-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository;

    @Mock
    private TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepositoryMock;

    @Autowired
    private TALeaseRepaymentRuleMapper tALeaseRepaymentRuleMapper;

    @Mock
    private TALeaseRepaymentRuleService tALeaseRepaymentRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TALeaseRepaymentRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TALeaseRepaymentRuleSearchRepository mockTALeaseRepaymentRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTALeaseRepaymentRuleMockMvc;

    private TALeaseRepaymentRule tALeaseRepaymentRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseRepaymentRule createEntity(EntityManager em) {
        TALeaseRepaymentRule tALeaseRepaymentRule = new TALeaseRepaymentRule().name(DEFAULT_NAME).identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseRepaymentRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseRepaymentRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseRepaymentRule.setCredit(transactionAccount);
        return tALeaseRepaymentRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseRepaymentRule createUpdatedEntity(EntityManager em) {
        TALeaseRepaymentRule tALeaseRepaymentRule = new TALeaseRepaymentRule().name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseRepaymentRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseRepaymentRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseRepaymentRule.setCredit(transactionAccount);
        return tALeaseRepaymentRule;
    }

    @BeforeEach
    public void initTest() {
        tALeaseRepaymentRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeCreate = tALeaseRepaymentRuleRepository.findAll().size();
        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);
        restTALeaseRepaymentRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TALeaseRepaymentRule testTALeaseRepaymentRule = tALeaseRepaymentRuleList.get(tALeaseRepaymentRuleList.size() - 1);
        assertThat(testTALeaseRepaymentRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTALeaseRepaymentRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(1)).save(testTALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void createTALeaseRepaymentRuleWithExistingId() throws Exception {
        // Create the TALeaseRepaymentRule with an existing ID
        tALeaseRepaymentRule.setId(1L);
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        int databaseSizeBeforeCreate = tALeaseRepaymentRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTALeaseRepaymentRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseRepaymentRuleRepository.findAll().size();
        // set the field null
        tALeaseRepaymentRule.setName(null);

        // Create the TALeaseRepaymentRule, which fails.
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        restTALeaseRepaymentRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseRepaymentRuleRepository.findAll().size();
        // set the field null
        tALeaseRepaymentRule.setIdentifier(null);

        // Create the TALeaseRepaymentRule, which fails.
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        restTALeaseRepaymentRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRules() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRepaymentRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseRepaymentRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tALeaseRepaymentRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseRepaymentRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseRepaymentRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseRepaymentRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tALeaseRepaymentRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseRepaymentRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseRepaymentRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTALeaseRepaymentRule() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get the tALeaseRepaymentRule
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tALeaseRepaymentRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tALeaseRepaymentRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTALeaseRepaymentRulesByIdFiltering() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        Long id = tALeaseRepaymentRule.getId();

        defaultTALeaseRepaymentRuleShouldBeFound("id.equals=" + id);
        defaultTALeaseRepaymentRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTALeaseRepaymentRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTALeaseRepaymentRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTALeaseRepaymentRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTALeaseRepaymentRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name equals to DEFAULT_NAME
        defaultTALeaseRepaymentRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tALeaseRepaymentRuleList where name equals to UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name not equals to DEFAULT_NAME
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tALeaseRepaymentRuleList where name not equals to UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tALeaseRepaymentRuleList where name equals to UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name is not null
        defaultTALeaseRepaymentRuleShouldBeFound("name.specified=true");

        // Get all the tALeaseRepaymentRuleList where name is null
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name contains DEFAULT_NAME
        defaultTALeaseRepaymentRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tALeaseRepaymentRuleList where name contains UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where name does not contain DEFAULT_NAME
        defaultTALeaseRepaymentRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tALeaseRepaymentRuleList where name does not contain UPDATED_NAME
        defaultTALeaseRepaymentRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseRepaymentRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseRepaymentRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tALeaseRepaymentRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseRepaymentRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        // Get all the tALeaseRepaymentRuleList where identifier is not null
        defaultTALeaseRepaymentRuleShouldBeFound("identifier.specified=true");

        // Get all the tALeaseRepaymentRuleList where identifier is null
        defaultTALeaseRepaymentRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tALeaseRepaymentRule.getLeaseContract();
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tALeaseRepaymentRuleList where leaseContract equals to leaseContractId
        defaultTALeaseRepaymentRuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tALeaseRepaymentRuleList where leaseContract equals to (leaseContractId + 1)
        defaultTALeaseRepaymentRuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
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
        tALeaseRepaymentRule.setDebit(debit);
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
        Long debitId = debit.getId();

        // Get all the tALeaseRepaymentRuleList where debit equals to debitId
        defaultTALeaseRepaymentRuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tALeaseRepaymentRuleList where debit equals to (debitId + 1)
        defaultTALeaseRepaymentRuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
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
        tALeaseRepaymentRule.setCredit(credit);
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
        Long creditId = credit.getId();

        // Get all the tALeaseRepaymentRuleList where credit equals to creditId
        defaultTALeaseRepaymentRuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tALeaseRepaymentRuleList where credit equals to (creditId + 1)
        defaultTALeaseRepaymentRuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRepaymentRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
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
        tALeaseRepaymentRule.addPlaceholder(placeholder);
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
        Long placeholderId = placeholder.getId();

        // Get all the tALeaseRepaymentRuleList where placeholder equals to placeholderId
        defaultTALeaseRepaymentRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tALeaseRepaymentRuleList where placeholder equals to (placeholderId + 1)
        defaultTALeaseRepaymentRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTALeaseRepaymentRuleShouldBeFound(String filter) throws Exception {
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRepaymentRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTALeaseRepaymentRuleShouldNotBeFound(String filter) throws Exception {
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTALeaseRepaymentRule() throws Exception {
        // Get the tALeaseRepaymentRule
        restTALeaseRepaymentRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTALeaseRepaymentRule() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();

        // Update the tALeaseRepaymentRule
        TALeaseRepaymentRule updatedTALeaseRepaymentRule = tALeaseRepaymentRuleRepository.findById(tALeaseRepaymentRule.getId()).get();
        // Disconnect from session so that the updates on updatedTALeaseRepaymentRule are not directly saved in db
        em.detach(updatedTALeaseRepaymentRule);
        updatedTALeaseRepaymentRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(updatedTALeaseRepaymentRule);

        restTALeaseRepaymentRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseRepaymentRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRepaymentRule testTALeaseRepaymentRule = tALeaseRepaymentRuleList.get(tALeaseRepaymentRuleList.size() - 1);
        assertThat(testTALeaseRepaymentRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseRepaymentRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository).save(testTALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void putNonExistingTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseRepaymentRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void partialUpdateTALeaseRepaymentRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();

        // Update the tALeaseRepaymentRule using partial update
        TALeaseRepaymentRule partialUpdatedTALeaseRepaymentRule = new TALeaseRepaymentRule();
        partialUpdatedTALeaseRepaymentRule.setId(tALeaseRepaymentRule.getId());

        partialUpdatedTALeaseRepaymentRule.identifier(UPDATED_IDENTIFIER);

        restTALeaseRepaymentRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseRepaymentRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseRepaymentRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRepaymentRule testTALeaseRepaymentRule = tALeaseRepaymentRuleList.get(tALeaseRepaymentRuleList.size() - 1);
        assertThat(testTALeaseRepaymentRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTALeaseRepaymentRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTALeaseRepaymentRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();

        // Update the tALeaseRepaymentRule using partial update
        TALeaseRepaymentRule partialUpdatedTALeaseRepaymentRule = new TALeaseRepaymentRule();
        partialUpdatedTALeaseRepaymentRule.setId(tALeaseRepaymentRule.getId());

        partialUpdatedTALeaseRepaymentRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTALeaseRepaymentRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseRepaymentRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseRepaymentRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRepaymentRule testTALeaseRepaymentRule = tALeaseRepaymentRuleList.get(tALeaseRepaymentRuleList.size() - 1);
        assertThat(testTALeaseRepaymentRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseRepaymentRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tALeaseRepaymentRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTALeaseRepaymentRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRepaymentRuleRepository.findAll().size();
        tALeaseRepaymentRule.setId(count.incrementAndGet());

        // Create the TALeaseRepaymentRule
        TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleMapper.toDto(tALeaseRepaymentRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRepaymentRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRepaymentRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseRepaymentRule in the database
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(0)).save(tALeaseRepaymentRule);
    }

    @Test
    @Transactional
    void deleteTALeaseRepaymentRule() throws Exception {
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);

        int databaseSizeBeforeDelete = tALeaseRepaymentRuleRepository.findAll().size();

        // Delete the tALeaseRepaymentRule
        restTALeaseRepaymentRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tALeaseRepaymentRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TALeaseRepaymentRule> tALeaseRepaymentRuleList = tALeaseRepaymentRuleRepository.findAll();
        assertThat(tALeaseRepaymentRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TALeaseRepaymentRule in Elasticsearch
        verify(mockTALeaseRepaymentRuleSearchRepository, times(1)).deleteById(tALeaseRepaymentRule.getId());
    }

    @Test
    @Transactional
    void searchTALeaseRepaymentRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tALeaseRepaymentRuleRepository.saveAndFlush(tALeaseRepaymentRule);
        when(mockTALeaseRepaymentRuleSearchRepository.search("id:" + tALeaseRepaymentRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tALeaseRepaymentRule), PageRequest.of(0, 1), 1));

        // Search the tALeaseRepaymentRule
        restTALeaseRepaymentRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tALeaseRepaymentRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRepaymentRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
