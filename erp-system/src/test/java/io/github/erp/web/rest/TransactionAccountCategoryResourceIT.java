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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TransactionAccountCategory;
import io.github.erp.domain.TransactionAccountLedger;
import io.github.erp.domain.enumeration.transactionAccountPostingTypes;
import io.github.erp.repository.TransactionAccountCategoryRepository;
import io.github.erp.repository.search.TransactionAccountCategorySearchRepository;
import io.github.erp.service.TransactionAccountCategoryService;
import io.github.erp.service.criteria.TransactionAccountCategoryCriteria;
import io.github.erp.service.dto.TransactionAccountCategoryDTO;
import io.github.erp.service.mapper.TransactionAccountCategoryMapper;
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
 * Integration tests for the {@link TransactionAccountCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final transactionAccountPostingTypes DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE = transactionAccountPostingTypes.DEBIT;
    private static final transactionAccountPostingTypes UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE = transactionAccountPostingTypes.CREDIT;

    private static final String ENTITY_API_URL = "/api/transaction-account-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountCategoryRepository transactionAccountCategoryRepository;

    @Mock
    private TransactionAccountCategoryRepository transactionAccountCategoryRepositoryMock;

    @Autowired
    private TransactionAccountCategoryMapper transactionAccountCategoryMapper;

    @Mock
    private TransactionAccountCategoryService transactionAccountCategoryServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountCategorySearchRepository mockTransactionAccountCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountCategoryMockMvc;

    private TransactionAccountCategory transactionAccountCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountCategory createEntity(EntityManager em) {
        TransactionAccountCategory transactionAccountCategory = new TransactionAccountCategory()
            .name(DEFAULT_NAME)
            .transactionAccountPostingType(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE);
        // Add required entity
        TransactionAccountLedger transactionAccountLedger;
        if (TestUtil.findAll(em, TransactionAccountLedger.class).isEmpty()) {
            transactionAccountLedger = TransactionAccountLedgerResourceIT.createEntity(em);
            em.persist(transactionAccountLedger);
            em.flush();
        } else {
            transactionAccountLedger = TestUtil.findAll(em, TransactionAccountLedger.class).get(0);
        }
        transactionAccountCategory.setAccountLedger(transactionAccountLedger);
        return transactionAccountCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountCategory createUpdatedEntity(EntityManager em) {
        TransactionAccountCategory transactionAccountCategory = new TransactionAccountCategory()
            .name(UPDATED_NAME)
            .transactionAccountPostingType(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);
        // Add required entity
        TransactionAccountLedger transactionAccountLedger;
        if (TestUtil.findAll(em, TransactionAccountLedger.class).isEmpty()) {
            transactionAccountLedger = TransactionAccountLedgerResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccountLedger);
            em.flush();
        } else {
            transactionAccountLedger = TestUtil.findAll(em, TransactionAccountLedger.class).get(0);
        }
        transactionAccountCategory.setAccountLedger(transactionAccountLedger);
        return transactionAccountCategory;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountCategoryRepository.findAll().size();
        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);
        restTransactionAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccountCategory testTransactionAccountCategory = transactionAccountCategoryList.get(
            transactionAccountCategoryList.size() - 1
        );
        assertThat(testTransactionAccountCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransactionAccountCategory.getTransactionAccountPostingType()).isEqualTo(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(1)).save(testTransactionAccountCategory);
    }

    @Test
    @Transactional
    void createTransactionAccountCategoryWithExistingId() throws Exception {
        // Create the TransactionAccountCategory with an existing ID
        transactionAccountCategory.setId(1L);
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        int databaseSizeBeforeCreate = transactionAccountCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountCategoryRepository.findAll().size();
        // set the field null
        transactionAccountCategory.setName(null);

        // Create the TransactionAccountCategory, which fails.
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        restTransactionAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAccountPostingTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountCategoryRepository.findAll().size();
        // set the field null
        transactionAccountCategory.setTransactionAccountPostingType(null);

        // Create the TransactionAccountCategory, which fails.
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        restTransactionAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategories() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].transactionAccountPostingType").value(hasItem(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionAccountCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionAccountCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionAccountCategory() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get the transactionAccountCategory
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.transactionAccountPostingType").value(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE.toString()));
    }

    @Test
    @Transactional
    void getTransactionAccountCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        Long id = transactionAccountCategory.getId();

        defaultTransactionAccountCategoryShouldBeFound("id.equals=" + id);
        defaultTransactionAccountCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name equals to DEFAULT_NAME
        defaultTransactionAccountCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the transactionAccountCategoryList where name equals to UPDATED_NAME
        defaultTransactionAccountCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name not equals to DEFAULT_NAME
        defaultTransactionAccountCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the transactionAccountCategoryList where name not equals to UPDATED_NAME
        defaultTransactionAccountCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTransactionAccountCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the transactionAccountCategoryList where name equals to UPDATED_NAME
        defaultTransactionAccountCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name is not null
        defaultTransactionAccountCategoryShouldBeFound("name.specified=true");

        // Get all the transactionAccountCategoryList where name is null
        defaultTransactionAccountCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name contains DEFAULT_NAME
        defaultTransactionAccountCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the transactionAccountCategoryList where name contains UPDATED_NAME
        defaultTransactionAccountCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where name does not contain DEFAULT_NAME
        defaultTransactionAccountCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the transactionAccountCategoryList where name does not contain UPDATED_NAME
        defaultTransactionAccountCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByTransactionAccountPostingTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where transactionAccountPostingType equals to DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldBeFound("transactionAccountPostingType.equals=" + DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE);

        // Get all the transactionAccountCategoryList where transactionAccountPostingType equals to UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldNotBeFound(
            "transactionAccountPostingType.equals=" + UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByTransactionAccountPostingTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where transactionAccountPostingType not equals to DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldNotBeFound(
            "transactionAccountPostingType.notEquals=" + DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE
        );

        // Get all the transactionAccountCategoryList where transactionAccountPostingType not equals to UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldBeFound(
            "transactionAccountPostingType.notEquals=" + UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByTransactionAccountPostingTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where transactionAccountPostingType in DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE or UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldBeFound(
            "transactionAccountPostingType.in=" + DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE + "," + UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        );

        // Get all the transactionAccountCategoryList where transactionAccountPostingType equals to UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE
        defaultTransactionAccountCategoryShouldNotBeFound("transactionAccountPostingType.in=" + UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByTransactionAccountPostingTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        // Get all the transactionAccountCategoryList where transactionAccountPostingType is not null
        defaultTransactionAccountCategoryShouldBeFound("transactionAccountPostingType.specified=true");

        // Get all the transactionAccountCategoryList where transactionAccountPostingType is null
        defaultTransactionAccountCategoryShouldNotBeFound("transactionAccountPostingType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);
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
        transactionAccountCategory.addPlaceholder(placeholder);
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);
        Long placeholderId = placeholder.getId();

        // Get all the transactionAccountCategoryList where placeholder equals to placeholderId
        defaultTransactionAccountCategoryShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionAccountCategoryList where placeholder equals to (placeholderId + 1)
        defaultTransactionAccountCategoryShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountCategoriesByAccountLedgerIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);
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
        transactionAccountCategory.setAccountLedger(accountLedger);
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);
        Long accountLedgerId = accountLedger.getId();

        // Get all the transactionAccountCategoryList where accountLedger equals to accountLedgerId
        defaultTransactionAccountCategoryShouldBeFound("accountLedgerId.equals=" + accountLedgerId);

        // Get all the transactionAccountCategoryList where accountLedger equals to (accountLedgerId + 1)
        defaultTransactionAccountCategoryShouldNotBeFound("accountLedgerId.equals=" + (accountLedgerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountCategoryShouldBeFound(String filter) throws Exception {
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].transactionAccountPostingType").value(hasItem(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE.toString())));

        // Check, that the count call also returns 1
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountCategoryShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountCategory() throws Exception {
        // Get the transactionAccountCategory
        restTransactionAccountCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccountCategory() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();

        // Update the transactionAccountCategory
        TransactionAccountCategory updatedTransactionAccountCategory = transactionAccountCategoryRepository
            .findById(transactionAccountCategory.getId())
            .get();
        // Disconnect from session so that the updates on updatedTransactionAccountCategory are not directly saved in db
        em.detach(updatedTransactionAccountCategory);
        updatedTransactionAccountCategory.name(UPDATED_NAME).transactionAccountPostingType(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(
            updatedTransactionAccountCategory
        );

        restTransactionAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountCategory testTransactionAccountCategory = transactionAccountCategoryList.get(
            transactionAccountCategoryList.size() - 1
        );
        assertThat(testTransactionAccountCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountCategory.getTransactionAccountPostingType()).isEqualTo(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository).save(testTransactionAccountCategory);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountCategoryWithPatch() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();

        // Update the transactionAccountCategory using partial update
        TransactionAccountCategory partialUpdatedTransactionAccountCategory = new TransactionAccountCategory();
        partialUpdatedTransactionAccountCategory.setId(transactionAccountCategory.getId());

        partialUpdatedTransactionAccountCategory.name(UPDATED_NAME).transactionAccountPostingType(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);

        restTransactionAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountCategory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountCategory testTransactionAccountCategory = transactionAccountCategoryList.get(
            transactionAccountCategoryList.size() - 1
        );
        assertThat(testTransactionAccountCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountCategory.getTransactionAccountPostingType()).isEqualTo(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountCategoryWithPatch() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();

        // Update the transactionAccountCategory using partial update
        TransactionAccountCategory partialUpdatedTransactionAccountCategory = new TransactionAccountCategory();
        partialUpdatedTransactionAccountCategory.setId(transactionAccountCategory.getId());

        partialUpdatedTransactionAccountCategory.name(UPDATED_NAME).transactionAccountPostingType(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);

        restTransactionAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountCategory))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountCategory testTransactionAccountCategory = transactionAccountCategoryList.get(
            transactionAccountCategoryList.size() - 1
        );
        assertThat(testTransactionAccountCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountCategory.getTransactionAccountPostingType()).isEqualTo(UPDATED_TRANSACTION_ACCOUNT_POSTING_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountCategoryRepository.findAll().size();
        transactionAccountCategory.setId(count.incrementAndGet());

        // Create the TransactionAccountCategory
        TransactionAccountCategoryDTO transactionAccountCategoryDTO = transactionAccountCategoryMapper.toDto(transactionAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountCategory in the database
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(0)).save(transactionAccountCategory);
    }

    @Test
    @Transactional
    void deleteTransactionAccountCategory() throws Exception {
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);

        int databaseSizeBeforeDelete = transactionAccountCategoryRepository.findAll().size();

        // Delete the transactionAccountCategory
        restTransactionAccountCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccountCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccountCategory> transactionAccountCategoryList = transactionAccountCategoryRepository.findAll();
        assertThat(transactionAccountCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccountCategory in Elasticsearch
        verify(mockTransactionAccountCategorySearchRepository, times(1)).deleteById(transactionAccountCategory.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccountCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountCategoryRepository.saveAndFlush(transactionAccountCategory);
        when(mockTransactionAccountCategorySearchRepository.search("id:" + transactionAccountCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountCategory), PageRequest.of(0, 1), 1));

        // Search the transactionAccountCategory
        restTransactionAccountCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].transactionAccountPostingType").value(hasItem(DEFAULT_TRANSACTION_ACCOUNT_POSTING_TYPE.toString())));
    }
}
