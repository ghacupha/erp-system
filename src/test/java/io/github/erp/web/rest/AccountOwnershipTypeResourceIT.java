package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.AccountOwnershipType;
import io.github.erp.repository.AccountOwnershipTypeRepository;
import io.github.erp.repository.search.AccountOwnershipTypeSearchRepository;
import io.github.erp.service.criteria.AccountOwnershipTypeCriteria;
import io.github.erp.service.dto.AccountOwnershipTypeDTO;
import io.github.erp.service.mapper.AccountOwnershipTypeMapper;
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
 * Integration tests for the {@link AccountOwnershipTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountOwnershipTypeResourceIT {

    private static final String DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_OWNERSHIP_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_OWNERSHIP_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-ownership-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-ownership-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountOwnershipTypeRepository accountOwnershipTypeRepository;

    @Autowired
    private AccountOwnershipTypeMapper accountOwnershipTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountOwnershipTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountOwnershipTypeSearchRepository mockAccountOwnershipTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountOwnershipTypeMockMvc;

    private AccountOwnershipType accountOwnershipType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountOwnershipType createEntity(EntityManager em) {
        AccountOwnershipType accountOwnershipType = new AccountOwnershipType()
            .accountOwnershipTypeCode(DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE)
            .accountOwnershipType(DEFAULT_ACCOUNT_OWNERSHIP_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return accountOwnershipType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountOwnershipType createUpdatedEntity(EntityManager em) {
        AccountOwnershipType accountOwnershipType = new AccountOwnershipType()
            .accountOwnershipTypeCode(UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE)
            .accountOwnershipType(UPDATED_ACCOUNT_OWNERSHIP_TYPE)
            .description(UPDATED_DESCRIPTION);
        return accountOwnershipType;
    }

    @BeforeEach
    public void initTest() {
        accountOwnershipType = createEntity(em);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypes() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountOwnershipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountOwnershipTypeCode").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountOwnershipType").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getAccountOwnershipType() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get the accountOwnershipType
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, accountOwnershipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountOwnershipType.getId().intValue()))
            .andExpect(jsonPath("$.accountOwnershipTypeCode").value(DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE))
            .andExpect(jsonPath("$.accountOwnershipType").value(DEFAULT_ACCOUNT_OWNERSHIP_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getAccountOwnershipTypesByIdFiltering() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        Long id = accountOwnershipType.getId();

        defaultAccountOwnershipTypeShouldBeFound("id.equals=" + id);
        defaultAccountOwnershipTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAccountOwnershipTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountOwnershipTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountOwnershipTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountOwnershipTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode equals to DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipTypeCode.equals=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.equals=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode not equals to DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.notEquals=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode not equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipTypeCode.notEquals=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode in DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE or UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldBeFound(
            "accountOwnershipTypeCode.in=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE + "," + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        );

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.in=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode is not null
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipTypeCode.specified=true");

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode is null
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode contains DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipTypeCode.contains=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode contains UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.contains=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode does not contain DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipTypeCode.doesNotContain=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE);

        // Get all the accountOwnershipTypeList where accountOwnershipTypeCode does not contain UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipTypeCode.doesNotContain=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType equals to DEFAULT_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipType.equals=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE);

        // Get all the accountOwnershipTypeList where accountOwnershipType equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.equals=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType not equals to DEFAULT_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.notEquals=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE);

        // Get all the accountOwnershipTypeList where accountOwnershipType not equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipType.notEquals=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType in DEFAULT_ACCOUNT_OWNERSHIP_TYPE or UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldBeFound(
            "accountOwnershipType.in=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE + "," + UPDATED_ACCOUNT_OWNERSHIP_TYPE
        );

        // Get all the accountOwnershipTypeList where accountOwnershipType equals to UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.in=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType is not null
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipType.specified=true");

        // Get all the accountOwnershipTypeList where accountOwnershipType is null
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeContainsSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType contains DEFAULT_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipType.contains=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE);

        // Get all the accountOwnershipTypeList where accountOwnershipType contains UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.contains=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountOwnershipTypesByAccountOwnershipTypeNotContainsSomething() throws Exception {
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);

        // Get all the accountOwnershipTypeList where accountOwnershipType does not contain DEFAULT_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldNotBeFound("accountOwnershipType.doesNotContain=" + DEFAULT_ACCOUNT_OWNERSHIP_TYPE);

        // Get all the accountOwnershipTypeList where accountOwnershipType does not contain UPDATED_ACCOUNT_OWNERSHIP_TYPE
        defaultAccountOwnershipTypeShouldBeFound("accountOwnershipType.doesNotContain=" + UPDATED_ACCOUNT_OWNERSHIP_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountOwnershipTypeShouldBeFound(String filter) throws Exception {
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountOwnershipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountOwnershipTypeCode").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountOwnershipType").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountOwnershipTypeShouldNotBeFound(String filter) throws Exception {
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountOwnershipType() throws Exception {
        // Get the accountOwnershipType
        restAccountOwnershipTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchAccountOwnershipType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountOwnershipTypeRepository.saveAndFlush(accountOwnershipType);
        when(mockAccountOwnershipTypeSearchRepository.search("id:" + accountOwnershipType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountOwnershipType), PageRequest.of(0, 1), 1));

        // Search the accountOwnershipType
        restAccountOwnershipTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountOwnershipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountOwnershipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountOwnershipTypeCode").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].accountOwnershipType").value(hasItem(DEFAULT_ACCOUNT_OWNERSHIP_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
