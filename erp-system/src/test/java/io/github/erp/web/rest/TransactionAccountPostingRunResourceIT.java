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
import io.github.erp.domain.TransactionAccountPostingRun;
import io.github.erp.repository.TransactionAccountPostingRunRepository;
import io.github.erp.repository.search.TransactionAccountPostingRunSearchRepository;
import io.github.erp.service.TransactionAccountPostingRunService;
import io.github.erp.service.criteria.TransactionAccountPostingRunCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRunDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRunMapper;
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
 * Integration tests for the {@link TransactionAccountPostingRunResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountPostingRunResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/transaction-account-posting-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-account-posting-runs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionAccountPostingRunRepository transactionAccountPostingRunRepository;

    @Mock
    private TransactionAccountPostingRunRepository transactionAccountPostingRunRepositoryMock;

    @Autowired
    private TransactionAccountPostingRunMapper transactionAccountPostingRunMapper;

    @Mock
    private TransactionAccountPostingRunService transactionAccountPostingRunServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionAccountPostingRunSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionAccountPostingRunSearchRepository mockTransactionAccountPostingRunSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountPostingRunMockMvc;

    private TransactionAccountPostingRun transactionAccountPostingRun;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingRun createEntity(EntityManager em) {
        TransactionAccountPostingRun transactionAccountPostingRun = new TransactionAccountPostingRun()
            .name(DEFAULT_NAME)
            .identifier(DEFAULT_IDENTIFIER);
        return transactionAccountPostingRun;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccountPostingRun createUpdatedEntity(EntityManager em) {
        TransactionAccountPostingRun transactionAccountPostingRun = new TransactionAccountPostingRun()
            .name(UPDATED_NAME)
            .identifier(UPDATED_IDENTIFIER);
        return transactionAccountPostingRun;
    }

    @BeforeEach
    public void initTest() {
        transactionAccountPostingRun = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeCreate = transactionAccountPostingRunRepository.findAll().size();
        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );
        restTransactionAccountPostingRunMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionAccountPostingRun testTransactionAccountPostingRun = transactionAccountPostingRunList.get(
            transactionAccountPostingRunList.size() - 1
        );
        assertThat(testTransactionAccountPostingRun.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransactionAccountPostingRun.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(1)).save(testTransactionAccountPostingRun);
    }

    @Test
    @Transactional
    void createTransactionAccountPostingRunWithExistingId() throws Exception {
        // Create the TransactionAccountPostingRun with an existing ID
        transactionAccountPostingRun.setId(1L);
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        int databaseSizeBeforeCreate = transactionAccountPostingRunRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountPostingRunMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionAccountPostingRunRepository.findAll().size();
        // set the field null
        transactionAccountPostingRun.setName(null);

        // Create the TransactionAccountPostingRun, which fails.
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        restTransactionAccountPostingRunMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRuns() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountPostingRunsWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionAccountPostingRunServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountPostingRunMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountPostingRunServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionAccountPostingRunsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionAccountPostingRunServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionAccountPostingRunMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionAccountPostingRunServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionAccountPostingRun() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get the transactionAccountPostingRun
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccountPostingRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccountPostingRun.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getTransactionAccountPostingRunsByIdFiltering() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        Long id = transactionAccountPostingRun.getId();

        defaultTransactionAccountPostingRunShouldBeFound("id.equals=" + id);
        defaultTransactionAccountPostingRunShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionAccountPostingRunShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionAccountPostingRunShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionAccountPostingRunShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionAccountPostingRunShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name equals to DEFAULT_NAME
        defaultTransactionAccountPostingRunShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRunList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingRunShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name not equals to DEFAULT_NAME
        defaultTransactionAccountPostingRunShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRunList where name not equals to UPDATED_NAME
        defaultTransactionAccountPostingRunShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTransactionAccountPostingRunShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the transactionAccountPostingRunList where name equals to UPDATED_NAME
        defaultTransactionAccountPostingRunShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name is not null
        defaultTransactionAccountPostingRunShouldBeFound("name.specified=true");

        // Get all the transactionAccountPostingRunList where name is null
        defaultTransactionAccountPostingRunShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name contains DEFAULT_NAME
        defaultTransactionAccountPostingRunShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRunList where name contains UPDATED_NAME
        defaultTransactionAccountPostingRunShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where name does not contain DEFAULT_NAME
        defaultTransactionAccountPostingRunShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the transactionAccountPostingRunList where name does not contain UPDATED_NAME
        defaultTransactionAccountPostingRunShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where identifier equals to DEFAULT_IDENTIFIER
        defaultTransactionAccountPostingRunShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the transactionAccountPostingRunList where identifier equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRunShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTransactionAccountPostingRunShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the transactionAccountPostingRunList where identifier not equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRunShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRunShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the transactionAccountPostingRunList where identifier equals to UPDATED_IDENTIFIER
        defaultTransactionAccountPostingRunShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        // Get all the transactionAccountPostingRunList where identifier is not null
        defaultTransactionAccountPostingRunShouldBeFound("identifier.specified=true");

        // Get all the transactionAccountPostingRunList where identifier is null
        defaultTransactionAccountPostingRunShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionAccountPostingRunsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);
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
        transactionAccountPostingRun.addPlaceholder(placeholder);
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);
        Long placeholderId = placeholder.getId();

        // Get all the transactionAccountPostingRunList where placeholder equals to placeholderId
        defaultTransactionAccountPostingRunShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionAccountPostingRunList where placeholder equals to (placeholderId + 1)
        defaultTransactionAccountPostingRunShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionAccountPostingRunShouldBeFound(String filter) throws Exception {
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionAccountPostingRunShouldNotBeFound(String filter) throws Exception {
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccountPostingRun() throws Exception {
        // Get the transactionAccountPostingRun
        restTransactionAccountPostingRunMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionAccountPostingRun() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();

        // Update the transactionAccountPostingRun
        TransactionAccountPostingRun updatedTransactionAccountPostingRun = transactionAccountPostingRunRepository
            .findById(transactionAccountPostingRun.getId())
            .get();
        // Disconnect from session so that the updates on updatedTransactionAccountPostingRun are not directly saved in db
        em.detach(updatedTransactionAccountPostingRun);
        updatedTransactionAccountPostingRun.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            updatedTransactionAccountPostingRun
        );

        restTransactionAccountPostingRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRun testTransactionAccountPostingRun = transactionAccountPostingRunList.get(
            transactionAccountPostingRunList.size() - 1
        );
        assertThat(testTransactionAccountPostingRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRun.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository).save(testTransactionAccountPostingRun);
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccountPostingRunDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountPostingRunWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();

        // Update the transactionAccountPostingRun using partial update
        TransactionAccountPostingRun partialUpdatedTransactionAccountPostingRun = new TransactionAccountPostingRun();
        partialUpdatedTransactionAccountPostingRun.setId(transactionAccountPostingRun.getId());

        partialUpdatedTransactionAccountPostingRun.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTransactionAccountPostingRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingRun))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRun testTransactionAccountPostingRun = transactionAccountPostingRunList.get(
            transactionAccountPostingRunList.size() - 1
        );
        assertThat(testTransactionAccountPostingRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRun.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountPostingRunWithPatch() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();

        // Update the transactionAccountPostingRun using partial update
        TransactionAccountPostingRun partialUpdatedTransactionAccountPostingRun = new TransactionAccountPostingRun();
        partialUpdatedTransactionAccountPostingRun.setId(transactionAccountPostingRun.getId());

        partialUpdatedTransactionAccountPostingRun.name(UPDATED_NAME).identifier(UPDATED_IDENTIFIER);

        restTransactionAccountPostingRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccountPostingRun.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionAccountPostingRun))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);
        TransactionAccountPostingRun testTransactionAccountPostingRun = transactionAccountPostingRunList.get(
            transactionAccountPostingRunList.size() - 1
        );
        assertThat(testTransactionAccountPostingRun.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransactionAccountPostingRun.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccountPostingRunDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccountPostingRun() throws Exception {
        int databaseSizeBeforeUpdate = transactionAccountPostingRunRepository.findAll().size();
        transactionAccountPostingRun.setId(count.incrementAndGet());

        // Create the TransactionAccountPostingRun
        TransactionAccountPostingRunDTO transactionAccountPostingRunDTO = transactionAccountPostingRunMapper.toDto(
            transactionAccountPostingRun
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountPostingRunMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionAccountPostingRunDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccountPostingRun in the database
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(0)).save(transactionAccountPostingRun);
    }

    @Test
    @Transactional
    void deleteTransactionAccountPostingRun() throws Exception {
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);

        int databaseSizeBeforeDelete = transactionAccountPostingRunRepository.findAll().size();

        // Delete the transactionAccountPostingRun
        restTransactionAccountPostingRunMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccountPostingRun.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionAccountPostingRun> transactionAccountPostingRunList = transactionAccountPostingRunRepository.findAll();
        assertThat(transactionAccountPostingRunList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionAccountPostingRun in Elasticsearch
        verify(mockTransactionAccountPostingRunSearchRepository, times(1)).deleteById(transactionAccountPostingRun.getId());
    }

    @Test
    @Transactional
    void searchTransactionAccountPostingRun() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionAccountPostingRunRepository.saveAndFlush(transactionAccountPostingRun);
        when(mockTransactionAccountPostingRunSearchRepository.search("id:" + transactionAccountPostingRun.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionAccountPostingRun), PageRequest.of(0, 1), 1));

        // Search the transactionAccountPostingRun
        restTransactionAccountPostingRunMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccountPostingRun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccountPostingRun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
