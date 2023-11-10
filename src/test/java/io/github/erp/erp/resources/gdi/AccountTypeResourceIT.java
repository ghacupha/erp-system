package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.AccountType;
import io.github.erp.repository.AccountTypeRepository;
import io.github.erp.repository.search.AccountTypeSearchRepository;
import io.github.erp.service.dto.AccountTypeDTO;
import io.github.erp.service.mapper.AccountTypeMapper;
import io.github.erp.web.rest.AccountTypeResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccountTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class AccountTypeResourceIT {

    private static final String DEFAULT_ACCOUNT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/account-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/account-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private AccountTypeMapper accountTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountTypeSearchRepository mockAccountTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountTypeMockMvc;

    private AccountType accountType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountType createEntity(EntityManager em) {
        AccountType accountType = new AccountType()
            .accountTypeCode(DEFAULT_ACCOUNT_TYPE_CODE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return accountType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountType createUpdatedEntity(EntityManager em) {
        AccountType accountType = new AccountType()
            .accountTypeCode(UPDATED_ACCOUNT_TYPE_CODE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return accountType;
    }

    @BeforeEach
    public void initTest() {
        accountType = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountType() throws Exception {
        int databaseSizeBeforeCreate = accountTypeRepository.findAll().size();
        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);
        restAccountTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AccountType testAccountType = accountTypeList.get(accountTypeList.size() - 1);
        assertThat(testAccountType.getAccountTypeCode()).isEqualTo(DEFAULT_ACCOUNT_TYPE_CODE);
        assertThat(testAccountType.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(1)).save(testAccountType);
    }

    @Test
    @Transactional
    void createAccountTypeWithExistingId() throws Exception {
        // Create the AccountType with an existing ID
        accountType.setId(1L);
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        int databaseSizeBeforeCreate = accountTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void checkAccountTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountTypeRepository.findAll().size();
        // set the field null
        accountType.setAccountTypeCode(null);

        // Create the AccountType, which fails.
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        restAccountTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountTypes() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountTypeCode").value(hasItem(DEFAULT_ACCOUNT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getAccountType() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get the accountType
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, accountType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountType.getId().intValue()))
            .andExpect(jsonPath("$.accountTypeCode").value(DEFAULT_ACCOUNT_TYPE_CODE))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getAccountTypesByIdFiltering() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        Long id = accountType.getId();

        defaultAccountTypeShouldBeFound("id.equals=" + id);
        defaultAccountTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAccountTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode equals to DEFAULT_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldBeFound("accountTypeCode.equals=" + DEFAULT_ACCOUNT_TYPE_CODE);

        // Get all the accountTypeList where accountTypeCode equals to UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldNotBeFound("accountTypeCode.equals=" + UPDATED_ACCOUNT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode not equals to DEFAULT_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldNotBeFound("accountTypeCode.notEquals=" + DEFAULT_ACCOUNT_TYPE_CODE);

        // Get all the accountTypeList where accountTypeCode not equals to UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldBeFound("accountTypeCode.notEquals=" + UPDATED_ACCOUNT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode in DEFAULT_ACCOUNT_TYPE_CODE or UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldBeFound("accountTypeCode.in=" + DEFAULT_ACCOUNT_TYPE_CODE + "," + UPDATED_ACCOUNT_TYPE_CODE);

        // Get all the accountTypeList where accountTypeCode equals to UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldNotBeFound("accountTypeCode.in=" + UPDATED_ACCOUNT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode is not null
        defaultAccountTypeShouldBeFound("accountTypeCode.specified=true");

        // Get all the accountTypeList where accountTypeCode is null
        defaultAccountTypeShouldNotBeFound("accountTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode contains DEFAULT_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldBeFound("accountTypeCode.contains=" + DEFAULT_ACCOUNT_TYPE_CODE);

        // Get all the accountTypeList where accountTypeCode contains UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldNotBeFound("accountTypeCode.contains=" + UPDATED_ACCOUNT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountTypeCode does not contain DEFAULT_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldNotBeFound("accountTypeCode.doesNotContain=" + DEFAULT_ACCOUNT_TYPE_CODE);

        // Get all the accountTypeList where accountTypeCode does not contain UPDATED_ACCOUNT_TYPE_CODE
        defaultAccountTypeShouldBeFound("accountTypeCode.doesNotContain=" + UPDATED_ACCOUNT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType equals to DEFAULT_ACCOUNT_TYPE
        defaultAccountTypeShouldBeFound("accountType.equals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the accountTypeList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldNotBeFound("accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType not equals to DEFAULT_ACCOUNT_TYPE
        defaultAccountTypeShouldNotBeFound("accountType.notEquals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the accountTypeList where accountType not equals to UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldBeFound("accountType.notEquals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType in DEFAULT_ACCOUNT_TYPE or UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldBeFound("accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE);

        // Get all the accountTypeList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldNotBeFound("accountType.in=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType is not null
        defaultAccountTypeShouldBeFound("accountType.specified=true");

        // Get all the accountTypeList where accountType is null
        defaultAccountTypeShouldNotBeFound("accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeContainsSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType contains DEFAULT_ACCOUNT_TYPE
        defaultAccountTypeShouldBeFound("accountType.contains=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the accountTypeList where accountType contains UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldNotBeFound("accountType.contains=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountTypesByAccountTypeNotContainsSomething() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        // Get all the accountTypeList where accountType does not contain DEFAULT_ACCOUNT_TYPE
        defaultAccountTypeShouldNotBeFound("accountType.doesNotContain=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the accountTypeList where accountType does not contain UPDATED_ACCOUNT_TYPE
        defaultAccountTypeShouldBeFound("accountType.doesNotContain=" + UPDATED_ACCOUNT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountTypeShouldBeFound(String filter) throws Exception {
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountTypeCode").value(hasItem(DEFAULT_ACCOUNT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountTypeShouldNotBeFound(String filter) throws Exception {
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountType() throws Exception {
        // Get the accountType
        restAccountTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountType() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();

        // Update the accountType
        AccountType updatedAccountType = accountTypeRepository.findById(accountType.getId()).get();
        // Disconnect from session so that the updates on updatedAccountType are not directly saved in db
        em.detach(updatedAccountType);
        updatedAccountType.accountTypeCode(UPDATED_ACCOUNT_TYPE_CODE).accountType(UPDATED_ACCOUNT_TYPE).description(UPDATED_DESCRIPTION);
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(updatedAccountType);

        restAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountType testAccountType = accountTypeList.get(accountTypeList.size() - 1);
        assertThat(testAccountType.getAccountTypeCode()).isEqualTo(UPDATED_ACCOUNT_TYPE_CODE);
        assertThat(testAccountType.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository).save(testAccountType);
    }

    @Test
    @Transactional
    void putNonExistingAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void partialUpdateAccountTypeWithPatch() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();

        // Update the accountType using partial update
        AccountType partialUpdatedAccountType = new AccountType();
        partialUpdatedAccountType.setId(accountType.getId());

        partialUpdatedAccountType.accountType(UPDATED_ACCOUNT_TYPE).description(UPDATED_DESCRIPTION);

        restAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountType))
            )
            .andExpect(status().isOk());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountType testAccountType = accountTypeList.get(accountTypeList.size() - 1);
        assertThat(testAccountType.getAccountTypeCode()).isEqualTo(DEFAULT_ACCOUNT_TYPE_CODE);
        assertThat(testAccountType.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAccountTypeWithPatch() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();

        // Update the accountType using partial update
        AccountType partialUpdatedAccountType = new AccountType();
        partialUpdatedAccountType.setId(accountType.getId());

        partialUpdatedAccountType
            .accountTypeCode(UPDATED_ACCOUNT_TYPE_CODE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountType))
            )
            .andExpect(status().isOk());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountType testAccountType = accountTypeList.get(accountTypeList.size() - 1);
        assertThat(testAccountType.getAccountTypeCode()).isEqualTo(UPDATED_ACCOUNT_TYPE_CODE);
        assertThat(testAccountType.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountType() throws Exception {
        int databaseSizeBeforeUpdate = accountTypeRepository.findAll().size();
        accountType.setId(count.incrementAndGet());

        // Create the AccountType
        AccountTypeDTO accountTypeDTO = accountTypeMapper.toDto(accountType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountType in the database
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(0)).save(accountType);
    }

    @Test
    @Transactional
    void deleteAccountType() throws Exception {
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);

        int databaseSizeBeforeDelete = accountTypeRepository.findAll().size();

        // Delete the accountType
        restAccountTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        assertThat(accountTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountType in Elasticsearch
        verify(mockAccountTypeSearchRepository, times(1)).deleteById(accountType.getId());
    }

    @Test
    @Transactional
    void searchAccountType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountTypeRepository.saveAndFlush(accountType);
        when(mockAccountTypeSearchRepository.search("id:" + accountType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountType), PageRequest.of(0, 1), 1));

        // Search the accountType
        restAccountTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountTypeCode").value(hasItem(DEFAULT_ACCOUNT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
