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
import io.github.erp.domain.TALeaseRecognitionRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.TALeaseRecognitionRuleRepository;
import io.github.erp.repository.search.TALeaseRecognitionRuleSearchRepository;
import io.github.erp.service.TALeaseRecognitionRuleService;
import io.github.erp.service.criteria.TALeaseRecognitionRuleCriteria;
import io.github.erp.service.dto.TALeaseRecognitionRuleDTO;
import io.github.erp.service.mapper.TALeaseRecognitionRuleMapper;
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
 * Integration tests for the {@link TALeaseRecognitionRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TALeaseRecognitionRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ta-lease-recognition-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ta-lease-recognition-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepository;

    @Mock
    private TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepositoryMock;

    @Autowired
    private TALeaseRecognitionRuleMapper tALeaseRecognitionRuleMapper;

    @Mock
    private TALeaseRecognitionRuleService tALeaseRecognitionRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TALeaseRecognitionRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TALeaseRecognitionRuleSearchRepository mockTALeaseRecognitionRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTALeaseRecognitionRuleMockMvc;

    private TALeaseRecognitionRule tALeaseRecognitionRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseRecognitionRule createEntity(EntityManager em) {
        TALeaseRecognitionRule tALeaseRecognitionRule = new TALeaseRecognitionRule().name(DEFAULT_NAME).identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseRecognitionRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseRecognitionRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseRecognitionRule.setCredit(transactionAccount);
        return tALeaseRecognitionRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TALeaseRecognitionRule createUpdatedEntity(EntityManager em) {
        TALeaseRecognitionRule tALeaseRecognitionRule = new TALeaseRecognitionRule().name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        tALeaseRecognitionRule.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        tALeaseRecognitionRule.setDebit(transactionAccount);
        // Add required entity
        tALeaseRecognitionRule.setCredit(transactionAccount);
        return tALeaseRecognitionRule;
    }

    @BeforeEach
    public void initTest() {
        tALeaseRecognitionRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeCreate = tALeaseRecognitionRuleRepository.findAll().size();
        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);
        restTALeaseRecognitionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TALeaseRecognitionRule testTALeaseRecognitionRule = tALeaseRecognitionRuleList.get(tALeaseRecognitionRuleList.size() - 1);
        assertThat(testTALeaseRecognitionRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTALeaseRecognitionRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(1)).save(testTALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void createTALeaseRecognitionRuleWithExistingId() throws Exception {
        // Create the TALeaseRecognitionRule with an existing ID
        tALeaseRecognitionRule.setId(1L);
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        int databaseSizeBeforeCreate = tALeaseRecognitionRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTALeaseRecognitionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseRecognitionRuleRepository.findAll().size();
        // set the field null
        tALeaseRecognitionRule.setName(null);

        // Create the TALeaseRecognitionRule, which fails.
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        restTALeaseRecognitionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = tALeaseRecognitionRuleRepository.findAll().size();
        // set the field null
        tALeaseRecognitionRule.setIdentifier(null);

        // Create the TALeaseRecognitionRule, which fails.
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        restTALeaseRecognitionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRules() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRecognitionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseRecognitionRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(tALeaseRecognitionRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseRecognitionRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseRecognitionRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTALeaseRecognitionRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tALeaseRecognitionRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTALeaseRecognitionRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tALeaseRecognitionRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTALeaseRecognitionRule() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get the tALeaseRecognitionRule
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, tALeaseRecognitionRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tALeaseRecognitionRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTALeaseRecognitionRulesByIdFiltering() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        Long id = tALeaseRecognitionRule.getId();

        defaultTALeaseRecognitionRuleShouldBeFound("id.equals=" + id);
        defaultTALeaseRecognitionRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTALeaseRecognitionRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTALeaseRecognitionRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTALeaseRecognitionRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTALeaseRecognitionRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name equals to DEFAULT_NAME
        defaultTALeaseRecognitionRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tALeaseRecognitionRuleList where name equals to UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name not equals to DEFAULT_NAME
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tALeaseRecognitionRuleList where name not equals to UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tALeaseRecognitionRuleList where name equals to UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name is not null
        defaultTALeaseRecognitionRuleShouldBeFound("name.specified=true");

        // Get all the tALeaseRecognitionRuleList where name is null
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name contains DEFAULT_NAME
        defaultTALeaseRecognitionRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tALeaseRecognitionRuleList where name contains UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where name does not contain DEFAULT_NAME
        defaultTALeaseRecognitionRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tALeaseRecognitionRuleList where name does not contain UPDATED_NAME
        defaultTALeaseRecognitionRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseRecognitionRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the tALeaseRecognitionRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the tALeaseRecognitionRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTALeaseRecognitionRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        // Get all the tALeaseRecognitionRuleList where identifier is not null
        defaultTALeaseRecognitionRuleShouldBeFound("identifier.specified=true");

        // Get all the tALeaseRecognitionRuleList where identifier is null
        defaultTALeaseRecognitionRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = tALeaseRecognitionRule.getLeaseContract();
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
        Long leaseContractId = leaseContract.getId();

        // Get all the tALeaseRecognitionRuleList where leaseContract equals to leaseContractId
        defaultTALeaseRecognitionRuleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the tALeaseRecognitionRuleList where leaseContract equals to (leaseContractId + 1)
        defaultTALeaseRecognitionRuleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
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
        tALeaseRecognitionRule.setDebit(debit);
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
        Long debitId = debit.getId();

        // Get all the tALeaseRecognitionRuleList where debit equals to debitId
        defaultTALeaseRecognitionRuleShouldBeFound("debitId.equals=" + debitId);

        // Get all the tALeaseRecognitionRuleList where debit equals to (debitId + 1)
        defaultTALeaseRecognitionRuleShouldNotBeFound("debitId.equals=" + (debitId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
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
        tALeaseRecognitionRule.setCredit(credit);
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
        Long creditId = credit.getId();

        // Get all the tALeaseRecognitionRuleList where credit equals to creditId
        defaultTALeaseRecognitionRuleShouldBeFound("creditId.equals=" + creditId);

        // Get all the tALeaseRecognitionRuleList where credit equals to (creditId + 1)
        defaultTALeaseRecognitionRuleShouldNotBeFound("creditId.equals=" + (creditId + 1));
    }

    @Test
    @Transactional
    void getAllTALeaseRecognitionRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
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
        tALeaseRecognitionRule.addPlaceholder(placeholder);
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
        Long placeholderId = placeholder.getId();

        // Get all the tALeaseRecognitionRuleList where placeholder equals to placeholderId
        defaultTALeaseRecognitionRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the tALeaseRecognitionRuleList where placeholder equals to (placeholderId + 1)
        defaultTALeaseRecognitionRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTALeaseRecognitionRuleShouldBeFound(String filter) throws Exception {
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRecognitionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTALeaseRecognitionRuleShouldNotBeFound(String filter) throws Exception {
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTALeaseRecognitionRule() throws Exception {
        // Get the tALeaseRecognitionRule
        restTALeaseRecognitionRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTALeaseRecognitionRule() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();

        // Update the tALeaseRecognitionRule
        TALeaseRecognitionRule updatedTALeaseRecognitionRule = tALeaseRecognitionRuleRepository
            .findById(tALeaseRecognitionRule.getId())
            .get();
        // Disconnect from session so that the updates on updatedTALeaseRecognitionRule are not directly saved in db
        em.detach(updatedTALeaseRecognitionRule);
        updatedTALeaseRecognitionRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(updatedTALeaseRecognitionRule);

        restTALeaseRecognitionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseRecognitionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRecognitionRule testTALeaseRecognitionRule = tALeaseRecognitionRuleList.get(tALeaseRecognitionRuleList.size() - 1);
        assertThat(testTALeaseRecognitionRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseRecognitionRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository).save(testTALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void putNonExistingTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tALeaseRecognitionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void partialUpdateTALeaseRecognitionRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();

        // Update the tALeaseRecognitionRule using partial update
        TALeaseRecognitionRule partialUpdatedTALeaseRecognitionRule = new TALeaseRecognitionRule();
        partialUpdatedTALeaseRecognitionRule.setId(tALeaseRecognitionRule.getId());

        partialUpdatedTALeaseRecognitionRule.identifier(UPDATED_IDENTIFIER);

        restTALeaseRecognitionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseRecognitionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseRecognitionRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRecognitionRule testTALeaseRecognitionRule = tALeaseRecognitionRuleList.get(tALeaseRecognitionRuleList.size() - 1);
        assertThat(testTALeaseRecognitionRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTALeaseRecognitionRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTALeaseRecognitionRuleWithPatch() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();

        // Update the tALeaseRecognitionRule using partial update
        TALeaseRecognitionRule partialUpdatedTALeaseRecognitionRule = new TALeaseRecognitionRule();
        partialUpdatedTALeaseRecognitionRule.setId(tALeaseRecognitionRule.getId());

        partialUpdatedTALeaseRecognitionRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTALeaseRecognitionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTALeaseRecognitionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTALeaseRecognitionRule))
            )
            .andExpect(status().isOk());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);
        TALeaseRecognitionRule testTALeaseRecognitionRule = tALeaseRecognitionRuleList.get(tALeaseRecognitionRuleList.size() - 1);
        assertThat(testTALeaseRecognitionRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTALeaseRecognitionRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tALeaseRecognitionRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTALeaseRecognitionRule() throws Exception {
        int databaseSizeBeforeUpdate = tALeaseRecognitionRuleRepository.findAll().size();
        tALeaseRecognitionRule.setId(count.incrementAndGet());

        // Create the TALeaseRecognitionRule
        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleMapper.toDto(tALeaseRecognitionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTALeaseRecognitionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tALeaseRecognitionRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TALeaseRecognitionRule in the database
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(0)).save(tALeaseRecognitionRule);
    }

    @Test
    @Transactional
    void deleteTALeaseRecognitionRule() throws Exception {
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);

        int databaseSizeBeforeDelete = tALeaseRecognitionRuleRepository.findAll().size();

        // Delete the tALeaseRecognitionRule
        restTALeaseRecognitionRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, tALeaseRecognitionRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TALeaseRecognitionRule> tALeaseRecognitionRuleList = tALeaseRecognitionRuleRepository.findAll();
        assertThat(tALeaseRecognitionRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TALeaseRecognitionRule in Elasticsearch
        verify(mockTALeaseRecognitionRuleSearchRepository, times(1)).deleteById(tALeaseRecognitionRule.getId());
    }

    @Test
    @Transactional
    void searchTALeaseRecognitionRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tALeaseRecognitionRuleRepository.saveAndFlush(tALeaseRecognitionRule);
        when(mockTALeaseRecognitionRuleSearchRepository.search("id:" + tALeaseRecognitionRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tALeaseRecognitionRule), PageRequest.of(0, 1), 1));

        // Search the tALeaseRecognitionRule
        restTALeaseRecognitionRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tALeaseRecognitionRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tALeaseRecognitionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
