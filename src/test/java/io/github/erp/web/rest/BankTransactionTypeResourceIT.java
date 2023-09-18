package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.BankTransactionType;
import io.github.erp.repository.BankTransactionTypeRepository;
import io.github.erp.repository.search.BankTransactionTypeSearchRepository;
import io.github.erp.service.criteria.BankTransactionTypeCriteria;
import io.github.erp.service.dto.BankTransactionTypeDTO;
import io.github.erp.service.mapper.BankTransactionTypeMapper;
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
 * Integration tests for the {@link BankTransactionTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BankTransactionTypeResourceIT {

    private static final String DEFAULT_TRANSACTION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/bank-transaction-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BankTransactionTypeRepository bankTransactionTypeRepository;

    @Autowired
    private BankTransactionTypeMapper bankTransactionTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BankTransactionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private BankTransactionTypeSearchRepository mockBankTransactionTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankTransactionTypeMockMvc;

    private BankTransactionType bankTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransactionType createEntity(EntityManager em) {
        BankTransactionType bankTransactionType = new BankTransactionType()
            .transactionTypeCode(DEFAULT_TRANSACTION_TYPE_CODE)
            .transactionTypeDetails(DEFAULT_TRANSACTION_TYPE_DETAILS);
        return bankTransactionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransactionType createUpdatedEntity(EntityManager em) {
        BankTransactionType bankTransactionType = new BankTransactionType()
            .transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE)
            .transactionTypeDetails(UPDATED_TRANSACTION_TYPE_DETAILS);
        return bankTransactionType;
    }

    @BeforeEach
    public void initTest() {
        bankTransactionType = createEntity(em);
    }

    @Test
    @Transactional
    void createBankTransactionType() throws Exception {
        int databaseSizeBeforeCreate = bankTransactionTypeRepository.findAll().size();
        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);
        restBankTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BankTransactionType testBankTransactionType = bankTransactionTypeList.get(bankTransactionTypeList.size() - 1);
        assertThat(testBankTransactionType.getTransactionTypeCode()).isEqualTo(DEFAULT_TRANSACTION_TYPE_CODE);
        assertThat(testBankTransactionType.getTransactionTypeDetails()).isEqualTo(DEFAULT_TRANSACTION_TYPE_DETAILS);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(1)).save(testBankTransactionType);
    }

    @Test
    @Transactional
    void createBankTransactionTypeWithExistingId() throws Exception {
        // Create the BankTransactionType with an existing ID
        bankTransactionType.setId(1L);
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        int databaseSizeBeforeCreate = bankTransactionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void checkTransactionTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankTransactionTypeRepository.findAll().size();
        // set the field null
        bankTransactionType.setTransactionTypeCode(null);

        // Create the BankTransactionType, which fails.
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        restBankTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankTransactionTypeRepository.findAll().size();
        // set the field null
        bankTransactionType.setTransactionTypeDetails(null);

        // Create the BankTransactionType, which fails.
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        restBankTransactionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypes() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionTypeCode").value(hasItem(DEFAULT_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].transactionTypeDetails").value(hasItem(DEFAULT_TRANSACTION_TYPE_DETAILS)));
    }

    @Test
    @Transactional
    void getBankTransactionType() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get the bankTransactionType
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, bankTransactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankTransactionType.getId().intValue()))
            .andExpect(jsonPath("$.transactionTypeCode").value(DEFAULT_TRANSACTION_TYPE_CODE))
            .andExpect(jsonPath("$.transactionTypeDetails").value(DEFAULT_TRANSACTION_TYPE_DETAILS));
    }

    @Test
    @Transactional
    void getBankTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        Long id = bankTransactionType.getId();

        defaultBankTransactionTypeShouldBeFound("id.equals=" + id);
        defaultBankTransactionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultBankTransactionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankTransactionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultBankTransactionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankTransactionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode equals to DEFAULT_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldBeFound("transactionTypeCode.equals=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the bankTransactionTypeList where transactionTypeCode equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.equals=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode not equals to DEFAULT_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.notEquals=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the bankTransactionTypeList where transactionTypeCode not equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldBeFound("transactionTypeCode.notEquals=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode in DEFAULT_TRANSACTION_TYPE_CODE or UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldBeFound(
            "transactionTypeCode.in=" + DEFAULT_TRANSACTION_TYPE_CODE + "," + UPDATED_TRANSACTION_TYPE_CODE
        );

        // Get all the bankTransactionTypeList where transactionTypeCode equals to UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.in=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode is not null
        defaultBankTransactionTypeShouldBeFound("transactionTypeCode.specified=true");

        // Get all the bankTransactionTypeList where transactionTypeCode is null
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode contains DEFAULT_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldBeFound("transactionTypeCode.contains=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the bankTransactionTypeList where transactionTypeCode contains UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.contains=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeCode does not contain DEFAULT_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeCode.doesNotContain=" + DEFAULT_TRANSACTION_TYPE_CODE);

        // Get all the bankTransactionTypeList where transactionTypeCode does not contain UPDATED_TRANSACTION_TYPE_CODE
        defaultBankTransactionTypeShouldBeFound("transactionTypeCode.doesNotContain=" + UPDATED_TRANSACTION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails equals to DEFAULT_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldBeFound("transactionTypeDetails.equals=" + DEFAULT_TRANSACTION_TYPE_DETAILS);

        // Get all the bankTransactionTypeList where transactionTypeDetails equals to UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.equals=" + UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails not equals to DEFAULT_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.notEquals=" + DEFAULT_TRANSACTION_TYPE_DETAILS);

        // Get all the bankTransactionTypeList where transactionTypeDetails not equals to UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldBeFound("transactionTypeDetails.notEquals=" + UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails in DEFAULT_TRANSACTION_TYPE_DETAILS or UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldBeFound(
            "transactionTypeDetails.in=" + DEFAULT_TRANSACTION_TYPE_DETAILS + "," + UPDATED_TRANSACTION_TYPE_DETAILS
        );

        // Get all the bankTransactionTypeList where transactionTypeDetails equals to UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.in=" + UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails is not null
        defaultBankTransactionTypeShouldBeFound("transactionTypeDetails.specified=true");

        // Get all the bankTransactionTypeList where transactionTypeDetails is null
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsContainsSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails contains DEFAULT_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldBeFound("transactionTypeDetails.contains=" + DEFAULT_TRANSACTION_TYPE_DETAILS);

        // Get all the bankTransactionTypeList where transactionTypeDetails contains UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.contains=" + UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllBankTransactionTypesByTransactionTypeDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        // Get all the bankTransactionTypeList where transactionTypeDetails does not contain DEFAULT_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldNotBeFound("transactionTypeDetails.doesNotContain=" + DEFAULT_TRANSACTION_TYPE_DETAILS);

        // Get all the bankTransactionTypeList where transactionTypeDetails does not contain UPDATED_TRANSACTION_TYPE_DETAILS
        defaultBankTransactionTypeShouldBeFound("transactionTypeDetails.doesNotContain=" + UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankTransactionTypeShouldBeFound(String filter) throws Exception {
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionTypeCode").value(hasItem(DEFAULT_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].transactionTypeDetails").value(hasItem(DEFAULT_TRANSACTION_TYPE_DETAILS)));

        // Check, that the count call also returns 1
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankTransactionType() throws Exception {
        // Get the bankTransactionType
        restBankTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBankTransactionType() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();

        // Update the bankTransactionType
        BankTransactionType updatedBankTransactionType = bankTransactionTypeRepository.findById(bankTransactionType.getId()).get();
        // Disconnect from session so that the updates on updatedBankTransactionType are not directly saved in db
        em.detach(updatedBankTransactionType);
        updatedBankTransactionType
            .transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE)
            .transactionTypeDetails(UPDATED_TRANSACTION_TYPE_DETAILS);
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(updatedBankTransactionType);

        restBankTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        BankTransactionType testBankTransactionType = bankTransactionTypeList.get(bankTransactionTypeList.size() - 1);
        assertThat(testBankTransactionType.getTransactionTypeCode()).isEqualTo(UPDATED_TRANSACTION_TYPE_CODE);
        assertThat(testBankTransactionType.getTransactionTypeDetails()).isEqualTo(UPDATED_TRANSACTION_TYPE_DETAILS);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository).save(testBankTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransactionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void partialUpdateBankTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();

        // Update the bankTransactionType using partial update
        BankTransactionType partialUpdatedBankTransactionType = new BankTransactionType();
        partialUpdatedBankTransactionType.setId(bankTransactionType.getId());

        partialUpdatedBankTransactionType.transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE);

        restBankTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        BankTransactionType testBankTransactionType = bankTransactionTypeList.get(bankTransactionTypeList.size() - 1);
        assertThat(testBankTransactionType.getTransactionTypeCode()).isEqualTo(UPDATED_TRANSACTION_TYPE_CODE);
        assertThat(testBankTransactionType.getTransactionTypeDetails()).isEqualTo(DEFAULT_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateBankTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();

        // Update the bankTransactionType using partial update
        BankTransactionType partialUpdatedBankTransactionType = new BankTransactionType();
        partialUpdatedBankTransactionType.setId(bankTransactionType.getId());

        partialUpdatedBankTransactionType
            .transactionTypeCode(UPDATED_TRANSACTION_TYPE_CODE)
            .transactionTypeDetails(UPDATED_TRANSACTION_TYPE_DETAILS);

        restBankTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);
        BankTransactionType testBankTransactionType = bankTransactionTypeList.get(bankTransactionTypeList.size() - 1);
        assertThat(testBankTransactionType.getTransactionTypeCode()).isEqualTo(UPDATED_TRANSACTION_TYPE_CODE);
        assertThat(testBankTransactionType.getTransactionTypeDetails()).isEqualTo(UPDATED_TRANSACTION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankTransactionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankTransactionType() throws Exception {
        int databaseSizeBeforeUpdate = bankTransactionTypeRepository.findAll().size();
        bankTransactionType.setId(count.incrementAndGet());

        // Create the BankTransactionType
        BankTransactionTypeDTO bankTransactionTypeDTO = bankTransactionTypeMapper.toDto(bankTransactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankTransactionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransactionType in the database
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(0)).save(bankTransactionType);
    }

    @Test
    @Transactional
    void deleteBankTransactionType() throws Exception {
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);

        int databaseSizeBeforeDelete = bankTransactionTypeRepository.findAll().size();

        // Delete the bankTransactionType
        restBankTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankTransactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankTransactionType> bankTransactionTypeList = bankTransactionTypeRepository.findAll();
        assertThat(bankTransactionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BankTransactionType in Elasticsearch
        verify(mockBankTransactionTypeSearchRepository, times(1)).deleteById(bankTransactionType.getId());
    }

    @Test
    @Transactional
    void searchBankTransactionType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        bankTransactionTypeRepository.saveAndFlush(bankTransactionType);
        when(mockBankTransactionTypeSearchRepository.search("id:" + bankTransactionType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bankTransactionType), PageRequest.of(0, 1), 1));

        // Search the bankTransactionType
        restBankTransactionTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bankTransactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionTypeCode").value(hasItem(DEFAULT_TRANSACTION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].transactionTypeDetails").value(hasItem(DEFAULT_TRANSACTION_TYPE_DETAILS)));
    }
}
