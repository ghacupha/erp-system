package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.AccountStatusType;
import io.github.erp.domain.enumeration.AccountStatusTypes;
import io.github.erp.repository.AccountStatusTypeRepository;
import io.github.erp.repository.search.AccountStatusTypeSearchRepository;
import io.github.erp.service.criteria.AccountStatusTypeCriteria;
import io.github.erp.service.dto.AccountStatusTypeDTO;
import io.github.erp.service.mapper.AccountStatusTypeMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AccountStatusTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountStatusTypeResourceIT {

    private static final String DEFAULT_ACCOUNT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS_CODE = "BBBBBBBBBB";

    private static final AccountStatusTypes DEFAULT_ACCOUNT_STATUS_TYPE = AccountStatusTypes.ACTIVE;
    private static final AccountStatusTypes UPDATED_ACCOUNT_STATUS_TYPE = AccountStatusTypes.INACTIVE;

    private static final String DEFAULT_ACCOUNT_STATUS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-status-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-status-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountStatusTypeRepository accountStatusTypeRepository;

    @Autowired
    private AccountStatusTypeMapper accountStatusTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountStatusTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountStatusTypeSearchRepository mockAccountStatusTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountStatusTypeMockMvc;

    private AccountStatusType accountStatusType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountStatusType createEntity(EntityManager em) {
        AccountStatusType accountStatusType = new AccountStatusType()
            .accountStatusCode(DEFAULT_ACCOUNT_STATUS_CODE)
            .accountStatusType(DEFAULT_ACCOUNT_STATUS_TYPE)
            .accountStatusDescription(DEFAULT_ACCOUNT_STATUS_DESCRIPTION);
        return accountStatusType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountStatusType createUpdatedEntity(EntityManager em) {
        AccountStatusType accountStatusType = new AccountStatusType()
            .accountStatusCode(UPDATED_ACCOUNT_STATUS_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusDescription(UPDATED_ACCOUNT_STATUS_DESCRIPTION);
        return accountStatusType;
    }

    @BeforeEach
    public void initTest() {
        accountStatusType = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountStatusType() throws Exception {
        int databaseSizeBeforeCreate = accountStatusTypeRepository.findAll().size();
        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);
        restAccountStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AccountStatusType testAccountStatusType = accountStatusTypeList.get(accountStatusTypeList.size() - 1);
        assertThat(testAccountStatusType.getAccountStatusCode()).isEqualTo(DEFAULT_ACCOUNT_STATUS_CODE);
        assertThat(testAccountStatusType.getAccountStatusType()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE);
        assertThat(testAccountStatusType.getAccountStatusDescription()).isEqualTo(DEFAULT_ACCOUNT_STATUS_DESCRIPTION);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(1)).save(testAccountStatusType);
    }

    @Test
    @Transactional
    void createAccountStatusTypeWithExistingId() throws Exception {
        // Create the AccountStatusType with an existing ID
        accountStatusType.setId(1L);
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        int databaseSizeBeforeCreate = accountStatusTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void checkAccountStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountStatusTypeRepository.findAll().size();
        // set the field null
        accountStatusType.setAccountStatusCode(null);

        // Create the AccountStatusType, which fails.
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        restAccountStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountStatusTypeRepository.findAll().size();
        // set the field null
        accountStatusType.setAccountStatusType(null);

        // Create the AccountStatusType, which fails.
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        restAccountStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypes() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountStatusDescription").value(hasItem(DEFAULT_ACCOUNT_STATUS_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getAccountStatusType() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get the accountStatusType
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, accountStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountStatusType.getId().intValue()))
            .andExpect(jsonPath("$.accountStatusCode").value(DEFAULT_ACCOUNT_STATUS_CODE))
            .andExpect(jsonPath("$.accountStatusType").value(DEFAULT_ACCOUNT_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.accountStatusDescription").value(DEFAULT_ACCOUNT_STATUS_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getAccountStatusTypesByIdFiltering() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        Long id = accountStatusType.getId();

        defaultAccountStatusTypeShouldBeFound("id.equals=" + id);
        defaultAccountStatusTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAccountStatusTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountStatusTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountStatusTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountStatusTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode equals to DEFAULT_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.equals=" + DEFAULT_ACCOUNT_STATUS_CODE);

        // Get all the accountStatusTypeList where accountStatusCode equals to UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.equals=" + UPDATED_ACCOUNT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode not equals to DEFAULT_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.notEquals=" + DEFAULT_ACCOUNT_STATUS_CODE);

        // Get all the accountStatusTypeList where accountStatusCode not equals to UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.notEquals=" + UPDATED_ACCOUNT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode in DEFAULT_ACCOUNT_STATUS_CODE or UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.in=" + DEFAULT_ACCOUNT_STATUS_CODE + "," + UPDATED_ACCOUNT_STATUS_CODE);

        // Get all the accountStatusTypeList where accountStatusCode equals to UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.in=" + UPDATED_ACCOUNT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode is not null
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.specified=true");

        // Get all the accountStatusTypeList where accountStatusCode is null
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode contains DEFAULT_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.contains=" + DEFAULT_ACCOUNT_STATUS_CODE);

        // Get all the accountStatusTypeList where accountStatusCode contains UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.contains=" + UPDATED_ACCOUNT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusCode does not contain DEFAULT_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusCode.doesNotContain=" + DEFAULT_ACCOUNT_STATUS_CODE);

        // Get all the accountStatusTypeList where accountStatusCode does not contain UPDATED_ACCOUNT_STATUS_CODE
        defaultAccountStatusTypeShouldBeFound("accountStatusCode.doesNotContain=" + UPDATED_ACCOUNT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusType equals to DEFAULT_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldBeFound("accountStatusType.equals=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the accountStatusTypeList where accountStatusType equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusType.equals=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusType not equals to DEFAULT_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusType.notEquals=" + DEFAULT_ACCOUNT_STATUS_TYPE);

        // Get all the accountStatusTypeList where accountStatusType not equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldBeFound("accountStatusType.notEquals=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusType in DEFAULT_ACCOUNT_STATUS_TYPE or UPDATED_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldBeFound("accountStatusType.in=" + DEFAULT_ACCOUNT_STATUS_TYPE + "," + UPDATED_ACCOUNT_STATUS_TYPE);

        // Get all the accountStatusTypeList where accountStatusType equals to UPDATED_ACCOUNT_STATUS_TYPE
        defaultAccountStatusTypeShouldNotBeFound("accountStatusType.in=" + UPDATED_ACCOUNT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountStatusTypesByAccountStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        // Get all the accountStatusTypeList where accountStatusType is not null
        defaultAccountStatusTypeShouldBeFound("accountStatusType.specified=true");

        // Get all the accountStatusTypeList where accountStatusType is null
        defaultAccountStatusTypeShouldNotBeFound("accountStatusType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountStatusTypeShouldBeFound(String filter) throws Exception {
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountStatusDescription").value(hasItem(DEFAULT_ACCOUNT_STATUS_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountStatusTypeShouldNotBeFound(String filter) throws Exception {
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountStatusType() throws Exception {
        // Get the accountStatusType
        restAccountStatusTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountStatusType() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();

        // Update the accountStatusType
        AccountStatusType updatedAccountStatusType = accountStatusTypeRepository.findById(accountStatusType.getId()).get();
        // Disconnect from session so that the updates on updatedAccountStatusType are not directly saved in db
        em.detach(updatedAccountStatusType);
        updatedAccountStatusType
            .accountStatusCode(UPDATED_ACCOUNT_STATUS_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusDescription(UPDATED_ACCOUNT_STATUS_DESCRIPTION);
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(updatedAccountStatusType);

        restAccountStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountStatusType testAccountStatusType = accountStatusTypeList.get(accountStatusTypeList.size() - 1);
        assertThat(testAccountStatusType.getAccountStatusCode()).isEqualTo(UPDATED_ACCOUNT_STATUS_CODE);
        assertThat(testAccountStatusType.getAccountStatusType()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE);
        assertThat(testAccountStatusType.getAccountStatusDescription()).isEqualTo(UPDATED_ACCOUNT_STATUS_DESCRIPTION);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository).save(testAccountStatusType);
    }

    @Test
    @Transactional
    void putNonExistingAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void partialUpdateAccountStatusTypeWithPatch() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();

        // Update the accountStatusType using partial update
        AccountStatusType partialUpdatedAccountStatusType = new AccountStatusType();
        partialUpdatedAccountStatusType.setId(accountStatusType.getId());

        partialUpdatedAccountStatusType.accountStatusDescription(UPDATED_ACCOUNT_STATUS_DESCRIPTION);

        restAccountStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountStatusType))
            )
            .andExpect(status().isOk());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountStatusType testAccountStatusType = accountStatusTypeList.get(accountStatusTypeList.size() - 1);
        assertThat(testAccountStatusType.getAccountStatusCode()).isEqualTo(DEFAULT_ACCOUNT_STATUS_CODE);
        assertThat(testAccountStatusType.getAccountStatusType()).isEqualTo(DEFAULT_ACCOUNT_STATUS_TYPE);
        assertThat(testAccountStatusType.getAccountStatusDescription()).isEqualTo(UPDATED_ACCOUNT_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAccountStatusTypeWithPatch() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();

        // Update the accountStatusType using partial update
        AccountStatusType partialUpdatedAccountStatusType = new AccountStatusType();
        partialUpdatedAccountStatusType.setId(accountStatusType.getId());

        partialUpdatedAccountStatusType
            .accountStatusCode(UPDATED_ACCOUNT_STATUS_CODE)
            .accountStatusType(UPDATED_ACCOUNT_STATUS_TYPE)
            .accountStatusDescription(UPDATED_ACCOUNT_STATUS_DESCRIPTION);

        restAccountStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountStatusType))
            )
            .andExpect(status().isOk());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        AccountStatusType testAccountStatusType = accountStatusTypeList.get(accountStatusTypeList.size() - 1);
        assertThat(testAccountStatusType.getAccountStatusCode()).isEqualTo(UPDATED_ACCOUNT_STATUS_CODE);
        assertThat(testAccountStatusType.getAccountStatusType()).isEqualTo(UPDATED_ACCOUNT_STATUS_TYPE);
        assertThat(testAccountStatusType.getAccountStatusDescription()).isEqualTo(UPDATED_ACCOUNT_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountStatusTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountStatusType() throws Exception {
        int databaseSizeBeforeUpdate = accountStatusTypeRepository.findAll().size();
        accountStatusType.setId(count.incrementAndGet());

        // Create the AccountStatusType
        AccountStatusTypeDTO accountStatusTypeDTO = accountStatusTypeMapper.toDto(accountStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountStatusType in the database
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(0)).save(accountStatusType);
    }

    @Test
    @Transactional
    void deleteAccountStatusType() throws Exception {
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);

        int databaseSizeBeforeDelete = accountStatusTypeRepository.findAll().size();

        // Delete the accountStatusType
        restAccountStatusTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountStatusType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountStatusType> accountStatusTypeList = accountStatusTypeRepository.findAll();
        assertThat(accountStatusTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountStatusType in Elasticsearch
        verify(mockAccountStatusTypeSearchRepository, times(1)).deleteById(accountStatusType.getId());
    }

    @Test
    @Transactional
    void searchAccountStatusType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountStatusTypeRepository.saveAndFlush(accountStatusType);
        when(mockAccountStatusTypeSearchRepository.search("id:" + accountStatusType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountStatusType), PageRequest.of(0, 1), 1));

        // Search the accountStatusType
        restAccountStatusTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatusCode").value(hasItem(DEFAULT_ACCOUNT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].accountStatusType").value(hasItem(DEFAULT_ACCOUNT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountStatusDescription").value(hasItem(DEFAULT_ACCOUNT_STATUS_DESCRIPTION.toString())));
    }
}
