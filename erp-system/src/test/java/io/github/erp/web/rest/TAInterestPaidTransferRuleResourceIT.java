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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TAInterestPaidTransferRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TAInterestPaidTransferRuleRepository;
import io.github.erp.repository.search.TAInterestPaidTransferRuleSearchRepository;
import io.github.erp.service.TAInterestPaidTransferRuleService;
import io.github.erp.service.criteria.TAInterestPaidTransferRuleCriteria;
import io.github.erp.service.dto.TAInterestPaidTransferRuleDTO;
import io.github.erp.service.mapper.TAInterestPaidTransferRuleMapper;
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
 * Integration tests for the {@link TAInterestPaidTransferRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TAInterestPaidTransferRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ta-interest-paid-transfer-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-interest-paid-transfer-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepository;

    @Mock
    private TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepositoryMock;

    @Autowired
    private TAInterestPaidTransferRuleMapper tAInterestPaidTransferRuleMapper;

    @Mock
    private TAInterestPaidTransferRuleService tAInterestPaidTransferRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TAInterestPaidTransferRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TAInterestPaidTransferRuleSearchRepository mockTAInterestPaidTransferRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTAInterestPaidTransferRuleMockMvc;

    private TAInterestPaidTransferRule tAInterestPaidTransferRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TAInterestPaidTransferRule createEntity(EntityManager em) {
        TAInterestPaidTransferRule tAInterestPaidTransferRule = new TAInterestPaidTransferRule()
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
        tAInterestPaidTransferRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tAInterestPaidTransferRule.setDebit(transactionAccount);
        // Add required entity
        tAInterestPaidTransferRule.setCredit(transactionAccount);
        return tAInterestPaidTransferRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TAInterestPaidTransferRule createUpdatedEntity(EntityManager em) {
        TAInterestPaidTransferRule tAInterestPaidTransferRule = new TAInterestPaidTransferRule()
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
        tAInterestPaidTransferRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tAInterestPaidTransferRule.setDebit(transactionAccount);
        // Add required entity
        tAInterestPaidTransferRule.setCredit(transactionAccount);
        return tAInterestPaidTransferRule;
    }

    @BeforeEach
    public void initTest() {
        tAInterestPaidTransferRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeCreate = tAInterestPaidTransferRuleRepository.findAll().size();
        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TAInterestPaidTransferRule testTAInterestPaidTransferRule = tAInterestPaidTransferRuleList.get(
            tAInterestPaidTransferRuleList.size() - 1
        );
        assertThat(testTAInterestPaidTransferRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTAInterestPaidTransferRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(1)).save(testTAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void createTAInterestPaidTransferRuleWithExistingId() throws Exception {
        // Create the TAInterestPaidTransferRule with an existing ID
        tAInterestPaidTransferRule.setId(1L);
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        int databaseSizeBeforeCreate = tAInterestPaidTransferRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tAInterestPaidTransferRuleRepository.findAll().size();
        // set the field null
        tAInterestPaidTransferRule.setName(null);

        // Create the TAInterestPaidTransferRule, which fails.
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        restTAInterestPaidTransferRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tAInterestPaidTransferRuleRepository.findAll().size();
        // set the field null
        tAInterestPaidTransferRule.setIdentifier(null);

        // Create the TAInterestPaidTransferRule, which fails.
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        restTAInterestPaidTransferRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRules() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAInterestPaidTransferRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTAInterestPaidTransferRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tAInterestPaidTransferRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTAInterestPaidTransferRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tAInterestPaidTransferRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTAInterestPaidTransferRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tAInterestPaidTransferRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTAInterestPaidTransferRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tAInterestPaidTransferRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTAInterestPaidTransferRule() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get the tAInterestPaidTransferRule
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tAInterestPaidTransferRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tAInterestPaidTransferRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTAInterestPaidTransferRulesByIdFiltering() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        Long id = tAInterestPaidTransferRule.getId();

        defaultTAInterestPaidTransferRuleShouldBeFound("id.equals=" + id);
        defaultTAInterestPaidTransferRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTAInterestPaidTransferRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTAInterestPaidTransferRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTAInterestPaidTransferRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTAInterestPaidTransferRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name equals to DEFAULT_NAME
        defaultTAInterestPaidTransferRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tAInterestPaidTransferRuleList where name equals to UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name not equals to DEFAULT_NAME
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tAInterestPaidTransferRuleList where name not equals to UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tAInterestPaidTransferRuleList where name equals to UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name is not null
        defaultTAInterestPaidTransferRuleShouldBeFound("name.specified=true");

        // Get all the tAInterestPaidTransferRuleList where name is null
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name contains DEFAULT_NAME
        defaultTAInterestPaidTransferRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tAInterestPaidTransferRuleList where name contains UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where name does not contain DEFAULT_NAME
        defaultTAInterestPaidTransferRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tAInterestPaidTransferRuleList where name does not contain UPDATED_NAME
        defaultTAInterestPaidTransferRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tAInterestPaidTransferRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tAInterestPaidTransferRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tAInterestPaidTransferRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTAInterestPaidTransferRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        // Get all the tAInterestPaidTransferRuleList where identifier is not null
        defaultTAInterestPaidTransferRuleShouldBeFound("identifier.specified=true");

        // Get all the tAInterestPaidTransferRuleList where identifier is null
        defaultTAInterestPaidTransferRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tAInterestPaidTransferRule.getLeaseContract();
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tAInterestPaidTransferRuleList where leaseContract equals to leaseContractId
        defaultTAInterestPaidTransferRuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tAInterestPaidTransferRuleList where leaseContract equals to (leaseContractId + 1)
        defaultTAInterestPaidTransferRuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
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
        tAInterestPaidTransferRule.setDebit(debit);
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
        Long debitId = debit.getId();

        // Get all the tAInterestPaidTransferRuleList where debit equals to debitId
        defaultTAInterestPaidTransferRuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tAInterestPaidTransferRuleList where debit equals to (debitId + 1)
        defaultTAInterestPaidTransferRuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
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
        tAInterestPaidTransferRule.setCredit(credit);
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
        Long creditId = credit.getId();

        // Get all the tAInterestPaidTransferRuleList where credit equals to creditId
        defaultTAInterestPaidTransferRuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tAInterestPaidTransferRuleList where credit equals to (creditId + 1)
        defaultTAInterestPaidTransferRuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTAInterestPaidTransferRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
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
        tAInterestPaidTransferRule.addPlaceholder(placeholder);
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
        Long placeholderId = placeholder.getId();

        // Get all the tAInterestPaidTransferRuleList where placeholder equals to placeholderId
        defaultTAInterestPaidTransferRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tAInterestPaidTransferRuleList where placeholder equals to (placeholderId + 1)
        defaultTAInterestPaidTransferRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTAInterestPaidTransferRuleShouldBeFound(String filter) throws Exception {
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAInterestPaidTransferRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTAInterestPaidTransferRuleShouldNotBeFound(String filter) throws Exception {
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTAInterestPaidTransferRule() throws Exception {
        // Get the tAInterestPaidTransferRule
        restTAInterestPaidTransferRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTAInterestPaidTransferRule() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();

        // Update the tAInterestPaidTransferRule
        TAInterestPaidTransferRule updatedTAInterestPaidTransferRule = tAInterestPaidTransferRuleRepository
            .findById(tAInterestPaidTransferRule.getId())
            .get();
        // Disconnect from session so that the updates on updatedTAInterestPaidTransferRule are not directly saved in db
        em.detach(updatedTAInterestPaidTransferRule);
        updatedTAInterestPaidTransferRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(
            updatedTAInterestPaidTransferRule
        );

        restTAInterestPaidTransferRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tAInterestPaidTransferRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);
        TAInterestPaidTransferRule testTAInterestPaidTransferRule = tAInterestPaidTransferRuleList.get(
            tAInterestPaidTransferRuleList.size() - 1
        );
        assertThat(testTAInterestPaidTransferRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTAInterestPaidTransferRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository).save(testTAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void putNonExistingTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tAInterestPaidTransferRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void partialUpdateTAInterestPaidTransferRuleWithPatch() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();

        // Update the tAInterestPaidTransferRule using partial update
        TAInterestPaidTransferRule partialUpdatedTAInterestPaidTransferRule = new TAInterestPaidTransferRule();
        partialUpdatedTAInterestPaidTransferRule.setId(tAInterestPaidTransferRule.getId());

        restTAInterestPaidTransferRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTAInterestPaidTransferRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTAInterestPaidTransferRule))
            )
            .andExpect(status().isOk());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);
        TAInterestPaidTransferRule testTAInterestPaidTransferRule = tAInterestPaidTransferRuleList.get(
            tAInterestPaidTransferRuleList.size() - 1
        );
        assertThat(testTAInterestPaidTransferRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTAInterestPaidTransferRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTAInterestPaidTransferRuleWithPatch() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();

        // Update the tAInterestPaidTransferRule using partial update
        TAInterestPaidTransferRule partialUpdatedTAInterestPaidTransferRule = new TAInterestPaidTransferRule();
        partialUpdatedTAInterestPaidTransferRule.setId(tAInterestPaidTransferRule.getId());

        partialUpdatedTAInterestPaidTransferRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTAInterestPaidTransferRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTAInterestPaidTransferRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTAInterestPaidTransferRule))
            )
            .andExpect(status().isOk());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);
        TAInterestPaidTransferRule testTAInterestPaidTransferRule = tAInterestPaidTransferRuleList.get(
            tAInterestPaidTransferRuleList.size() - 1
        );
        assertThat(testTAInterestPaidTransferRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTAInterestPaidTransferRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tAInterestPaidTransferRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTAInterestPaidTransferRule() throws Exception {
        int databaseSizeBeforeUpdate = tAInterestPaidTransferRuleRepository.findAll().size();
        tAInterestPaidTransferRule.setId(count.incrementAndGet());

        // Create the TAInterestPaidTransferRule
        TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleMapper.toDto(tAInterestPaidTransferRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTAInterestPaidTransferRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tAInterestPaidTransferRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TAInterestPaidTransferRule in the database
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(0)).save(tAInterestPaidTransferRule);
    }

    @Test
    @Transactional
    void deleteTAInterestPaidTransferRule() throws Exception {
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);

        int databaseSizeBeforeDelete = tAInterestPaidTransferRuleRepository.findAll().size();

        // Delete the tAInterestPaidTransferRule
        restTAInterestPaidTransferRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tAInterestPaidTransferRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TAInterestPaidTransferRule> tAInterestPaidTransferRuleList = tAInterestPaidTransferRuleRepository.findAll();
        assertThat(tAInterestPaidTransferRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TAInterestPaidTransferRule in Elasticsearch
        verify(mockTAInterestPaidTransferRuleSearchRepository, times(1)).deleteById(tAInterestPaidTransferRule.getId());
    }

    @Test
    @Transactional
    void searchTAInterestPaidTransferRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tAInterestPaidTransferRuleRepository.saveAndFlush(tAInterestPaidTransferRule);
        when(mockTAInterestPaidTransferRuleSearchRepository.search("id:" + tAInterestPaidTransferRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tAInterestPaidTransferRule), PageRequest.of(0, 1), 1));

        // Search the tAInterestPaidTransferRule
        restTAInterestPaidTransferRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tAInterestPaidTransferRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tAInterestPaidTransferRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
