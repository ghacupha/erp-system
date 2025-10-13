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
import io.github.erp.domain.TALeaseInterestAccrualRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TALeaseInterestAccrualRuleRepository;
import io.github.erp.repository.search.TALeaseInterestAccrualRuleSearchRepository;
import io.github.erp.service.TALeaseInterestAccrualRuleService;
import io.github.erp.service.criteria.TALeaseInterestAccrualRuleCriteria;
import io.github.erp.service.dto.TALeaseInterestAccrualRuleDTO;
import io.github.erp.service.mapper.TALeaseInterestAccrualRuleMapper;
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
 * Integration tests for the {@link TALeaseInterestAccrualRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TALeaseInterestAccrualRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ta-lease-interest-accrual-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-lease-interest-accrual-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository;

    @Mock
    private TALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepositoryMock;

    @Autowired
    private TALeaseInterestAccrualRuleMapper tALeaseInterestAccrualRuleMapper;

    @Mock
    private TALeaseInterestAccrualRuleService tALeaseInterestAccrualRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TALeaseInterestAccrualRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TALeaseInterestAccrualRuleSearchRepository mockTALeaseInterestAccrualRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTALeaseInterestAccrualRuleMockMvc;

    private TALeaseInterestAccrualRule tALeaseInterestAccrualRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseInterestAccrualRule createEntity(EntityManager em) {
        TALeaseInterestAccrualRule tALeaseInterestAccrualRule = new TALeaseInterestAccrualRule()
            .name(DEFAULT_NAME)
            .identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseInterestAccrualRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseInterestAccrualRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseInterestAccrualRule.setCredit(transactionAccount);
        return tALeaseInterestAccrualRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseInterestAccrualRule createUpdatedEntity(EntityManager em) {
        TALeaseInterestAccrualRule tALeaseInterestAccrualRule = new TALeaseInterestAccrualRule()
            .name(UPDATED_NAME)
            .identifier(UPDATED_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseInterestAccrualRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseInterestAccrualRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseInterestAccrualRule.setCredit(transactionAccount);
        return tALeaseInterestAccrualRule;
    }

    @BeforeEach
    public void initTest() {
        tALeaseInterestAccrualRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeCreate = tALeaseInterestAccrualRuleRepository.findAll().size();
        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TALeaseInterestAccrualRule testTALeaseInterestAccrualRule = tALeaseInterestAccrualRuleList.get(
            tALeaseInterestAccrualRuleList.size() - 1
        );
        assertThat(testTALeaseInterestAccrualRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTALeaseInterestAccrualRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(1)).save(testTALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void createTALeaseInterestAccrualRuleWithExistingId() throws Exception {
        // Create the TALeaseInterestAccrualRule with an existing ID
        tALeaseInterestAccrualRule.setId(1L);
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        int databaseSizeBeforeCreate = tALeaseInterestAccrualRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseInterestAccrualRuleRepository.findAll().size();
        // set the field null
        tALeaseInterestAccrualRule.setName(null);

        // Create the TALeaseInterestAccrualRule, which fails.
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseInterestAccrualRuleRepository.findAll().size();
        // set the field null
        tALeaseInterestAccrualRule.setIdentifier(null);

        // Create the TALeaseInterestAccrualRule, which fails.
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRules() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseInterestAccrualRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseInterestAccrualRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tALeaseInterestAccrualRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseInterestAccrualRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseInterestAccrualRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseInterestAccrualRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tALeaseInterestAccrualRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseInterestAccrualRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseInterestAccrualRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTALeaseInterestAccrualRule() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get the tALeaseInterestAccrualRule
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tALeaseInterestAccrualRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tALeaseInterestAccrualRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTALeaseInterestAccrualRulesByIdFiltering() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        Long id = tALeaseInterestAccrualRule.getId();

        defaultTALeaseInterestAccrualRuleShouldBeFound("id.equals=" + id);
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTALeaseInterestAccrualRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTALeaseInterestAccrualRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name equals to DEFAULT_NAME
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tALeaseInterestAccrualRuleList where name equals to UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name not equals to DEFAULT_NAME
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tALeaseInterestAccrualRuleList where name not equals to UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tALeaseInterestAccrualRuleList where name equals to UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name is not null
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.specified=true");

        // Get all the tALeaseInterestAccrualRuleList where name is null
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name contains DEFAULT_NAME
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tALeaseInterestAccrualRuleList where name contains UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where name does not contain DEFAULT_NAME
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tALeaseInterestAccrualRuleList where name does not contain UPDATED_NAME
        defaultTALeaseInterestAccrualRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseInterestAccrualRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseInterestAccrualRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tALeaseInterestAccrualRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        // Get all the tALeaseInterestAccrualRuleList where identifier is not null
        defaultTALeaseInterestAccrualRuleShouldBeFound("identifier.specified=true");

        // Get all the tALeaseInterestAccrualRuleList where identifier is null
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tALeaseInterestAccrualRule.getLeaseContract();
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tALeaseInterestAccrualRuleList where leaseContract equals to leaseContractId
        defaultTALeaseInterestAccrualRuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tALeaseInterestAccrualRuleList where leaseContract equals to (leaseContractId + 1)
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
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
        tALeaseInterestAccrualRule.setDebit(debit);
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
        Long debitId = debit.getId();

        // Get all the tALeaseInterestAccrualRuleList where debit equals to debitId
        defaultTALeaseInterestAccrualRuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tALeaseInterestAccrualRuleList where debit equals to (debitId + 1)
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
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
        tALeaseInterestAccrualRule.setCredit(credit);
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
        Long creditId = credit.getId();

        // Get all the tALeaseInterestAccrualRuleList where credit equals to creditId
        defaultTALeaseInterestAccrualRuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tALeaseInterestAccrualRuleList where credit equals to (creditId + 1)
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseInterestAccrualRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
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
        tALeaseInterestAccrualRule.addPlaceholder(placeholder);
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
        Long placeholderId = placeholder.getId();

        // Get all the tALeaseInterestAccrualRuleList where placeholder equals to placeholderId
        defaultTALeaseInterestAccrualRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tALeaseInterestAccrualRuleList where placeholder equals to (placeholderId + 1)
        defaultTALeaseInterestAccrualRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTALeaseInterestAccrualRuleShouldBeFound(String filter) throws Exception {
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseInterestAccrualRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTALeaseInterestAccrualRuleShouldNotBeFound(String filter) throws Exception {
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTALeaseInterestAccrualRule() throws Exception {
        // Get the tALeaseInterestAccrualRule
        restTALeaseInterestAccrualRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTALeaseInterestAccrualRule() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();

        // Update the tALeaseInterestAccrualRule
        TALeaseInterestAccrualRule updatedTALeaseInterestAccrualRule = tALeaseInterestAccrualRuleRepository
            .findById(tALeaseInterestAccrualRule.getId())
            .get();
        // Disconnect from session so that the updates on updatedTALeaseInterestAccrualRule are not directly saved in db
        em.detach(updatedTALeaseInterestAccrualRule);
        updatedTALeaseInterestAccrualRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(
            updatedTALeaseInterestAccrualRule
        );

        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseInterestAccrualRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseInterestAccrualRule testTALeaseInterestAccrualRule = tALeaseInterestAccrualRuleList.get(
            tALeaseInterestAccrualRuleList.size() - 1
        );
        assertThat(testTALeaseInterestAccrualRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseInterestAccrualRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository).save(testTALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void putNonExistingTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseInterestAccrualRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void partialUpdateTALeaseInterestAccrualRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();

        // Update the tALeaseInterestAccrualRule using partial update
        TALeaseInterestAccrualRule partialUpdatedTALeaseInterestAccrualRule = new TALeaseInterestAccrualRule();
        partialUpdatedTALeaseInterestAccrualRule.setId(tALeaseInterestAccrualRule.getId());

        partialUpdatedTALeaseInterestAccrualRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseInterestAccrualRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseInterestAccrualRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseInterestAccrualRule testTALeaseInterestAccrualRule = tALeaseInterestAccrualRuleList.get(
            tALeaseInterestAccrualRuleList.size() - 1
        );
        assertThat(testTALeaseInterestAccrualRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseInterestAccrualRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTALeaseInterestAccrualRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();

        // Update the tALeaseInterestAccrualRule using partial update
        TALeaseInterestAccrualRule partialUpdatedTALeaseInterestAccrualRule = new TALeaseInterestAccrualRule();
        partialUpdatedTALeaseInterestAccrualRule.setId(tALeaseInterestAccrualRule.getId());

        partialUpdatedTALeaseInterestAccrualRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseInterestAccrualRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseInterestAccrualRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseInterestAccrualRule testTALeaseInterestAccrualRule = tALeaseInterestAccrualRuleList.get(
            tALeaseInterestAccrualRuleList.size() - 1
        );
        assertThat(testTALeaseInterestAccrualRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseInterestAccrualRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tALeaseInterestAccrualRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTALeaseInterestAccrualRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseInterestAccrualRuleRepository.findAll().size();
        tALeaseInterestAccrualRule.setId(count.incrementAndGet());

        // Create the TALeaseInterestAccrualRule
        TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleMapper.toDto(tALeaseInterestAccrualRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseInterestAccrualRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseInterestAccrualRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseInterestAccrualRule in the database
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(0)).save(tALeaseInterestAccrualRule);
    }

    @Test
    @Transactional
    void deleteTALeaseInterestAccrualRule() throws Exception {
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);

        int databaseSizeBeforeDelete = tALeaseInterestAccrualRuleRepository.findAll().size();

        // Delete the tALeaseInterestAccrualRule
        restTALeaseInterestAccrualRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tALeaseInterestAccrualRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TALeaseInterestAccrualRule> tALeaseInterestAccrualRuleList = tALeaseInterestAccrualRuleRepository.findAll();
        assertThat(tALeaseInterestAccrualRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TALeaseInterestAccrualRule in Elasticsearch
        verify(mockTALeaseInterestAccrualRuleSearchRepository, times(1)).deleteById(tALeaseInterestAccrualRule.getId());
    }

    @Test
    @Transactional
    void searchTALeaseInterestAccrualRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tALeaseInterestAccrualRuleRepository.saveAndFlush(tALeaseInterestAccrualRule);
        when(mockTALeaseInterestAccrualRuleSearchRepository.search("id:" + tALeaseInterestAccrualRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tALeaseInterestAccrualRule), PageRequest.of(0, 1), 1));

        // Search the tALeaseInterestAccrualRule
        restTALeaseInterestAccrualRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tALeaseInterestAccrualRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseInterestAccrualRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
