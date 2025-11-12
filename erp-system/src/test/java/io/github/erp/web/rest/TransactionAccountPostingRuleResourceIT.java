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
import io.github.erp.domain.TransactionAccountCategory;
import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepository;
import io.github.erp.service.criteria.TransactionAccountPostingRuleCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRuleDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRuleMapper;
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
 * Integration tests for the {@link TransactionAccountPostingRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountPostingRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/transaction-account-posting-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-posting-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository;

    @Autowired
    private TransactionAccountPostingRuleMapper transactionAccountPostingRuleMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountPostingRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountPostingRuleSearchRepository mockTransactionAccountPostingRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountPostingRuleMockMvc;

    private TransactionAccountPostingRule transactionAccountPostingRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingRule createEntity(EntityManager em) {
        TransactionAccountPostingRule transactionAccountPostingRule = new TransactionAccountPostingRule()
            .name(DEFAULT_NAME)
            .identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccountPostingRule.setDebitAccountType(transactionAccountCategory);
        // Add required entity
        transactionAccountPostingRule.setCreditAccountType(transactionAccountCategory);
        return transactionAccountPostingRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingRule createUpdatedEntity(EntityManager em) {
        TransactionAccountPostingRule transactionAccountPostingRule = new TransactionAccountPostingRule()
            .name(UPDATED_NAME)
            .identifier(UPDATED_IDENTIFIER);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccountPostingRule.setDebitAccountType(transactionAccountCategory);
        // Add required entity
        transactionAccountPostingRule.setCreditAccountType(transactionAccountCategory);
        return transactionAccountPostingRule;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountPostingRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountPostingRuleRepository.findAll().size();
        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );
        restTransactionAccountPostingRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccountPostingRule testTransactionAccountPostingRule = transactionAccountPostingRuleList.get(
            transactionAccountPostingRuleList.size() - 1
        );
        assertThat(testTransactionAccountPostingRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransactionAccountPostingRule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(1)).save(testTransactionAccountPostingRule);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingRuleWithExistingId() throws Exception {
        // Create the TransactionAccountPostingRule with an existing ID
        transactionAccountPostingRule.setId(1L);
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        int databaseSizeBeforeCreate = transactionAccountPostingRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountPostingRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountPostingRuleRepository.findAll().size();
        // set the field null
        transactionAccountPostingRule.setName(null);

        // Create the TransactionAccountPostingRule, which fails.
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        restTransactionAccountPostingRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRules() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getTransactionAccountPostingRule() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get the transactionAccountPostingRule
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountPostingRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountPostingRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTransactionAccountPostingRulesByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        Long id = transactionAccountPostingRule.getId();

        defaultTransactionAccountPostingRuleShouldBeFound("id.equals=" + id);
        defaultTransactionAccountPostingRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountPostingRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountPostingRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountPostingRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountPostingRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name equals to DEFAULT_NAME
        defaultTransactionAccountPostingRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRuleList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name not equals to DEFAULT_NAME
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRuleList where name not equals to UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the transactionAccountPostingRuleList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name is not null
        defaultTransactionAccountPostingRuleShouldBeFound("name.specified=true");

        // Get all the transactionAccountPostingRuleList where name is null
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name contains DEFAULT_NAME
        defaultTransactionAccountPostingRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRuleList where name contains UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where name does not contain DEFAULT_NAME
        defaultTransactionAccountPostingRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRuleList where name does not contain UPDATED_NAME
        defaultTransactionAccountPostingRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where identifier equals to DEFAULT_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the transactionAccountPostingRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the transactionAccountPostingRuleList where identifier not equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the transactionAccountPostingRuleList where identifier equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRuleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        // Get all the transactionAccountPostingRuleList where identifier is not null
        defaultTransactionAccountPostingRuleShouldBeFound("identifier.specified=true");

        // Get all the transactionAccountPostingRuleList where identifier is null
        defaultTransactionAccountPostingRuleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByDebitAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        TransactionAccountCategory debitAccountType;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            debitAccountType = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(debitAccountType);
            em.flush();
        } else {
            debitAccountType = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        em.persist(debitAccountType);
        em.flush();
        transactionAccountPostingRule.setDebitAccountType(debitAccountType);
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        Long debitAccountTypeId = debitAccountType.getId();

        // Get all the transactionAccountPostingRuleList where debitAccountType equals to debitAccountTypeId
        defaultTransactionAccountPostingRuleShouldBeFound("debitAccountTypeId.equals=" + debitAccountTypeId);

        // Get all the transactionAccountPostingRuleList where debitAccountType equals to (debitAccountTypeId + 1)
        defaultTransactionAccountPostingRuleShouldNotBeFound("debitAccountTypeId.equals=" + (debitAccountTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByCreditAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        TransactionAccountCategory creditAccountType;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            creditAccountType = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(creditAccountType);
            em.flush();
        } else {
            creditAccountType = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        em.persist(creditAccountType);
        em.flush();
        transactionAccountPostingRule.setCreditAccountType(creditAccountType);
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        Long creditAccountTypeId = creditAccountType.getId();

        // Get all the transactionAccountPostingRuleList where creditAccountType equals to creditAccountTypeId
        defaultTransactionAccountPostingRuleShouldBeFound("creditAccountTypeId.equals=" + creditAccountTypeId);

        // Get all the transactionAccountPostingRuleList where creditAccountType equals to (creditAccountTypeId + 1)
        defaultTransactionAccountPostingRuleShouldNotBeFound("creditAccountTypeId.equals=" + (creditAccountTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRulesByTransactionContextIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        Placeholder transactionContext;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            transactionContext = PlaceholderResourceIT.createEntity(em);
            em.persist(transactionContext);
            em.flush();
        } else {
            transactionContext = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(transactionContext);
        em.flush();
        transactionAccountPostingRule.setTransactionContext(transactionContext);
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        Long transactionContextId = transactionContext.getId();

        // Get all the transactionAccountPostingRuleList where transactionContext equals to transactionContextId
        defaultTransactionAccountPostingRuleShouldBeFound("transactionContextId.equals=" + transactionContextId);

        // Get all the transactionAccountPostingRuleList where transactionContext equals to (transactionContextId + 1)
        defaultTransactionAccountPostingRuleShouldNotBeFound("transactionContextId.equals=" + (transactionContextId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountPostingRuleShouldBeFound(String filter) throws Exception {
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountPostingRuleShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountPostingRule() throws Exception {
        // Get the transactionAccountPostingRule
        restTransactionAccountPostingRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccountPostingRule() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();

        // Update the transactionAccountPostingRule
        TransactionAccountPostingRule updatedTransactionAccountPostingRule = transactionAccountPostingRuleRepository
            .findById(transactionAccountPostingRule.getId())
            .get();
        // Disconnect from session so that the updates on updatedTransactionAccountPostingRule are not directly saved in db
        em.detach(updatedTransactionAccountPostingRule);
        updatedTransactionAccountPostingRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            updatedTransactionAccountPostingRule
        );

        restTransactionAccountPostingRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRule testTransactionAccountPostingRule = transactionAccountPostingRuleList.get(
            transactionAccountPostingRuleList.size() - 1
        );
        assertThat(testTransactionAccountPostingRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository).save(testTransactionAccountPostingRule);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountPostingRuleWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();

        // Update the transactionAccountPostingRule using partial update
        TransactionAccountPostingRule partialUpdatedTransactionAccountPostingRule = new TransactionAccountPostingRule();
        partialUpdatedTransactionAccountPostingRule.setId(transactionAccountPostingRule.getId());

        partialUpdatedTransactionAccountPostingRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTransactionAccountPostingRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingRule))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRule testTransactionAccountPostingRule = transactionAccountPostingRuleList.get(
            transactionAccountPostingRuleList.size() - 1
        );
        assertThat(testTransactionAccountPostingRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountPostingRuleWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();

        // Update the transactionAccountPostingRule using partial update
        TransactionAccountPostingRule partialUpdatedTransactionAccountPostingRule = new TransactionAccountPostingRule();
        partialUpdatedTransactionAccountPostingRule.setId(transactionAccountPostingRule.getId());

        partialUpdatedTransactionAccountPostingRule.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTransactionAccountPostingRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingRule))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRule testTransactionAccountPostingRule = transactionAccountPostingRuleList.get(
            transactionAccountPostingRuleList.size() - 1
        );
        assertThat(testTransactionAccountPostingRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountPostingRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccountPostingRule() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRuleRepository.findAll().size();
        transactionAccountPostingRule.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRule
        TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO = transactionAccountPostingRuleMapper.toDto(
            transactionAccountPostingRule
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingRule in the database
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(0)).save(transactionAccountPostingRule);
    }

    @Test
    @Transactional
    void deleteTransactionAccountPostingRule() throws Exception {
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);

        int databaseSizeBeforeDelete = transactionAccountPostingRuleRepository.findAll().size();

        // Delete the transactionAccountPostingRule
        restTransactionAccountPostingRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccountPostingRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccountPostingRule> transactionAccountPostingRuleList = transactionAccountPostingRuleRepository.findAll();
        assertThat(transactionAccountPostingRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccountPostingRule in Elasticsearch
        verify(mockTransactionAccountPostingRuleSearchRepository, times(1)).deleteById(transactionAccountPostingRule.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccountPostingRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountPostingRuleRepository.saveAndFlush(transactionAccountPostingRule);
        when(mockTransactionAccountPostingRuleSearchRepository.search("id:" + transactionAccountPostingRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountPostingRule), PageRequest.of(0, 1), 1));

        // Search the transactionAccountPostingRule
        restTransactionAccountPostingRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountPostingRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
