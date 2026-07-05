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
import io.github.erp.domain.TransactionAccountPostingProcessType;
import io.github.erp.repository.TransactionAccountPostingProcessTypeRepository;
import io.github.erp.repository.search.TransactionAccountPostingProcessTypeSearchRepository;
import io.github.erp.service.TransactionAccountPostingProcessTypeService;
import io.github.erp.service.criteria.TransactionAccountPostingProcessTypeCriteria;
import io.github.erp.service.dto.TransactionAccountPostingProcessTypeDTO;
import io.github.erp.service.mapper.TransactionAccountPostingProcessTypeMapper;
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
 * Integration tests for the {@link TransactionAccountPostingProcessTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountPostingProcessTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-account-posting-process-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-posting-process-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository;

    @Mock
    private TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepositoryMock;

    @Autowired
    private TransactionAccountPostingProcessTypeMapper transactionAccountPostingProcessTypeMapper;

    @Mock
    private TransactionAccountPostingProcessTypeService transactionAccountPostingProcessTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountPostingProcessTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountPostingProcessTypeSearchRepository mockTransactionAccountPostingProcessTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountPostingProcessTypeMockMvc;

    private TransactionAccountPostingProcessType transactionAccountPostingProcessType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingProcessType createEntity(EntityManager em) {
        TransactionAccountPostingProcessType transactionAccountPostingProcessType = new TransactionAccountPostingProcessType()
            .name(DEFAULT_NAME);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccountPostingProcessType.setDebitAccountType(transactionAccountCategory);
        // Add required entity
        transactionAccountPostingProcessType.setCreditAccountType(transactionAccountCategory);
        return transactionAccountPostingProcessType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingProcessType createUpdatedEntity(EntityManager em) {
        TransactionAccountPostingProcessType transactionAccountPostingProcessType = new TransactionAccountPostingProcessType()
            .name(UPDATED_NAME);
        // Add required entity
        TransactionAccountCategory transactionAccountCategory;
        if (TestUtil.findAll(em, TransactionAccountCategory.class).isEmpty()) {
            transactionAccountCategory = TransactionAccountCategoryResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccountCategory);
            em.flush();
        } else {
            transactionAccountCategory = TestUtil.findAll(em, TransactionAccountCategory.class).get(0);
        }
        transactionAccountPostingProcessType.setDebitAccountType(transactionAccountCategory);
        // Add required entity
        transactionAccountPostingProcessType.setCreditAccountType(transactionAccountCategory);
        return transactionAccountPostingProcessType;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountPostingProcessType = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountPostingProcessTypeRepository.findAll().size();
        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccountPostingProcessType testTransactionAccountPostingProcessType = transactionAccountPostingProcessTypeList.get(
            transactionAccountPostingProcessTypeList.size() - 1
        );
        assertThat(testTransactionAccountPostingProcessType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(1)).save(testTransactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingProcessTypeWithExistingId() throws Exception {
        // Create the TransactionAccountPostingProcessType with an existing ID
        transactionAccountPostingProcessType.setId(1L);
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        int databaseSizeBeforeCreate = transactionAccountPostingProcessTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountPostingProcessTypeRepository.findAll().size();
        // set the field null
        transactionAccountPostingProcessType.setName(null);

        // Create the TransactionAccountPostingProcessType, which fails.
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypes() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountPostingProcessTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionAccountPostingProcessTypeServiceMock.findAllWithEagerRelationships(any()))
            .thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountPostingProcessTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountPostingProcessTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountPostingProcessTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionAccountPostingProcessTypeServiceMock.findAllWithEagerRelationships(any()))
            .thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountPostingProcessTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountPostingProcessTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionAccountPostingProcessType() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get the transactionAccountPostingProcessType
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountPostingProcessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountPostingProcessType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTransactionAccountPostingProcessTypesByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        Long id = transactionAccountPostingProcessType.getId();

        defaultTransactionAccountPostingProcessTypeShouldBeFound("id.equals=" + id);
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountPostingProcessTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountPostingProcessTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name equals to DEFAULT_NAME
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingProcessTypeList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name not equals to DEFAULT_NAME
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingProcessTypeList where name not equals to UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the transactionAccountPostingProcessTypeList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name is not null
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.specified=true");

        // Get all the transactionAccountPostingProcessTypeList where name is null
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name contains DEFAULT_NAME
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingProcessTypeList where name contains UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        // Get all the transactionAccountPostingProcessTypeList where name does not contain DEFAULT_NAME
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingProcessTypeList where name does not contain UPDATED_NAME
        defaultTransactionAccountPostingProcessTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByDebitAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
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
        transactionAccountPostingProcessType.setDebitAccountType(debitAccountType);
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
        Long debitAccountTypeId = debitAccountType.getId();

        // Get all the transactionAccountPostingProcessTypeList where debitAccountType equals to debitAccountTypeId
        defaultTransactionAccountPostingProcessTypeShouldBeFound("debitAccountTypeId.equals=" + debitAccountTypeId);

        // Get all the transactionAccountPostingProcessTypeList where debitAccountType equals to (debitAccountTypeId + 1)
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("debitAccountTypeId.equals=" + (debitAccountTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByCreditAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
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
        transactionAccountPostingProcessType.setCreditAccountType(creditAccountType);
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
        Long creditAccountTypeId = creditAccountType.getId();

        // Get all the transactionAccountPostingProcessTypeList where creditAccountType equals to creditAccountTypeId
        defaultTransactionAccountPostingProcessTypeShouldBeFound("creditAccountTypeId.equals=" + creditAccountTypeId);

        // Get all the transactionAccountPostingProcessTypeList where creditAccountType equals to (creditAccountTypeId + 1)
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("creditAccountTypeId.equals=" + (creditAccountTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingProcessTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
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
        transactionAccountPostingProcessType.addPlaceholder(placeholder);
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
        Long placeholderId = placeholder.getId();

        // Get all the transactionAccountPostingProcessTypeList where placeholder equals to placeholderId
        defaultTransactionAccountPostingProcessTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionAccountPostingProcessTypeList where placeholder equals to (placeholderId + 1)
        defaultTransactionAccountPostingProcessTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountPostingProcessTypeShouldBeFound(String filter) throws Exception {
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountPostingProcessTypeShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountPostingProcessType() throws Exception {
        // Get the transactionAccountPostingProcessType
        restTransactionAccountPostingProcessTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccountPostingProcessType() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();

        // Update the transactionAccountPostingProcessType
        TransactionAccountPostingProcessType updatedTransactionAccountPostingProcessType = transactionAccountPostingProcessTypeRepository
            .findById(transactionAccountPostingProcessType.getId())
            .get();
        // Disconnect from session so that the updates on updatedTransactionAccountPostingProcessType are not directly saved in db
        em.detach(updatedTransactionAccountPostingProcessType);
        updatedTransactionAccountPostingProcessType.name(UPDATED_NAME);
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            updatedTransactionAccountPostingProcessType
        );

        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingProcessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingProcessType testTransactionAccountPostingProcessType = transactionAccountPostingProcessTypeList.get(
            transactionAccountPostingProcessTypeList.size() - 1
        );
        assertThat(testTransactionAccountPostingProcessType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository).save(testTransactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingProcessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountPostingProcessTypeWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();

        // Update the transactionAccountPostingProcessType using partial update
        TransactionAccountPostingProcessType partialUpdatedTransactionAccountPostingProcessType = new TransactionAccountPostingProcessType();
        partialUpdatedTransactionAccountPostingProcessType.setId(transactionAccountPostingProcessType.getId());

        partialUpdatedTransactionAccountPostingProcessType.name(UPDATED_NAME);

        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingProcessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingProcessType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingProcessType testTransactionAccountPostingProcessType = transactionAccountPostingProcessTypeList.get(
            transactionAccountPostingProcessTypeList.size() - 1
        );
        assertThat(testTransactionAccountPostingProcessType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountPostingProcessTypeWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();

        // Update the transactionAccountPostingProcessType using partial update
        TransactionAccountPostingProcessType partialUpdatedTransactionAccountPostingProcessType = new TransactionAccountPostingProcessType();
        partialUpdatedTransactionAccountPostingProcessType.setId(transactionAccountPostingProcessType.getId());

        partialUpdatedTransactionAccountPostingProcessType.name(UPDATED_NAME);

        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingProcessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingProcessType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingProcessType testTransactionAccountPostingProcessType = transactionAccountPostingProcessTypeList.get(
            transactionAccountPostingProcessTypeList.size() - 1
        );
        assertThat(testTransactionAccountPostingProcessType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountPostingProcessTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccountPostingProcessType() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingProcessTypeRepository.findAll().size();
        transactionAccountPostingProcessType.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingProcessType
        TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeMapper.toDto(
            transactionAccountPostingProcessType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingProcessTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingProcessType in the database
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(0)).save(transactionAccountPostingProcessType);
    }

    @Test
    @Transactional
    void deleteTransactionAccountPostingProcessType() throws Exception {
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);

        int databaseSizeBeforeDelete = transactionAccountPostingProcessTypeRepository.findAll().size();

        // Delete the transactionAccountPostingProcessType
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccountPostingProcessType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccountPostingProcessType> transactionAccountPostingProcessTypeList = transactionAccountPostingProcessTypeRepository.findAll();
        assertThat(transactionAccountPostingProcessTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccountPostingProcessType in Elasticsearch
        verify(mockTransactionAccountPostingProcessTypeSearchRepository, times(1)).deleteById(transactionAccountPostingProcessType.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccountPostingProcessType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountPostingProcessTypeRepository.saveAndFlush(transactionAccountPostingProcessType);
        when(
            mockTransactionAccountPostingProcessTypeSearchRepository.search(
                "id:" + transactionAccountPostingProcessType.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountPostingProcessType), PageRequest.of(0, 1), 1));

        // Search the transactionAccountPostingProcessType
        restTransactionAccountPostingProcessTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountPostingProcessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
