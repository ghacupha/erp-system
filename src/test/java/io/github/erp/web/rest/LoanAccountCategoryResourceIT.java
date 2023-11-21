package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.domain.LoanAccountCategory;
import io.github.erp.domain.enumeration.LoanAccountMutationTypes;
import io.github.erp.repository.LoanAccountCategoryRepository;
import io.github.erp.repository.search.LoanAccountCategorySearchRepository;
import io.github.erp.service.criteria.LoanAccountCategoryCriteria;
import io.github.erp.service.dto.LoanAccountCategoryDTO;
import io.github.erp.service.mapper.LoanAccountCategoryMapper;
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
 * Integration tests for the {@link LoanAccountCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoanAccountCategoryResourceIT {

    private static final String DEFAULT_LOAN_ACCOUNT_MUTATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_ACCOUNT_MUTATION_CODE = "BBBBBBBBBB";

    private static final LoanAccountMutationTypes DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE = LoanAccountMutationTypes.RESTRUCTURED;
    private static final LoanAccountMutationTypes UPDATED_LOAN_ACCOUNT_MUTATION_TYPE = LoanAccountMutationTypes.WRITTEN_OFF;

    private static final String DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loan-account-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loan-account-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanAccountCategoryRepository loanAccountCategoryRepository;

    @Autowired
    private LoanAccountCategoryMapper loanAccountCategoryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanAccountCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanAccountCategorySearchRepository mockLoanAccountCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanAccountCategoryMockMvc;

    private LoanAccountCategory loanAccountCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanAccountCategory createEntity(EntityManager em) {
        LoanAccountCategory loanAccountCategory = new LoanAccountCategory()
            .loanAccountMutationCode(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE)
            .loanAccountMutationType(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE)
            .loanAccountMutationDetails(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS)
            .loanAccountMutationDescription(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION);
        return loanAccountCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanAccountCategory createUpdatedEntity(EntityManager em) {
        LoanAccountCategory loanAccountCategory = new LoanAccountCategory()
            .loanAccountMutationCode(UPDATED_LOAN_ACCOUNT_MUTATION_CODE)
            .loanAccountMutationType(UPDATED_LOAN_ACCOUNT_MUTATION_TYPE)
            .loanAccountMutationDetails(UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS)
            .loanAccountMutationDescription(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);
        return loanAccountCategory;
    }

    @BeforeEach
    public void initTest() {
        loanAccountCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanAccountCategory() throws Exception {
        int databaseSizeBeforeCreate = loanAccountCategoryRepository.findAll().size();
        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);
        restLoanAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        LoanAccountCategory testLoanAccountCategory = loanAccountCategoryList.get(loanAccountCategoryList.size() - 1);
        assertThat(testLoanAccountCategory.getLoanAccountMutationCode()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationType()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDetails()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDescription()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(1)).save(testLoanAccountCategory);
    }

    @Test
    @Transactional
    void createLoanAccountCategoryWithExistingId() throws Exception {
        // Create the LoanAccountCategory with an existing ID
        loanAccountCategory.setId(1L);
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        int databaseSizeBeforeCreate = loanAccountCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void checkLoanAccountMutationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountCategoryRepository.findAll().size();
        // set the field null
        loanAccountCategory.setLoanAccountMutationCode(null);

        // Create the LoanAccountCategory, which fails.
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        restLoanAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanAccountMutationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountCategoryRepository.findAll().size();
        // set the field null
        loanAccountCategory.setLoanAccountMutationType(null);

        // Create the LoanAccountCategory, which fails.
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        restLoanAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanAccountMutationDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountCategoryRepository.findAll().size();
        // set the field null
        loanAccountCategory.setLoanAccountMutationDetails(null);

        // Create the LoanAccountCategory, which fails.
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        restLoanAccountCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategories() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanAccountMutationCode").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE)))
            .andExpect(jsonPath("$.[*].loanAccountMutationType").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].loanAccountMutationDetails").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS)))
            .andExpect(
                jsonPath("$.[*].loanAccountMutationDescription").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getLoanAccountCategory() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get the loanAccountCategory
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, loanAccountCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanAccountCategory.getId().intValue()))
            .andExpect(jsonPath("$.loanAccountMutationCode").value(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE))
            .andExpect(jsonPath("$.loanAccountMutationType").value(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE.toString()))
            .andExpect(jsonPath("$.loanAccountMutationDetails").value(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS))
            .andExpect(jsonPath("$.loanAccountMutationDescription").value(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getLoanAccountCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        Long id = loanAccountCategory.getId();

        defaultLoanAccountCategoryShouldBeFound("id.equals=" + id);
        defaultLoanAccountCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultLoanAccountCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanAccountCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanAccountCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanAccountCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode equals to DEFAULT_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationCode.equals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);

        // Get all the loanAccountCategoryList where loanAccountMutationCode equals to UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.equals=" + UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode not equals to DEFAULT_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.notEquals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);

        // Get all the loanAccountCategoryList where loanAccountMutationCode not equals to UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationCode.notEquals=" + UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode in DEFAULT_LOAN_ACCOUNT_MUTATION_CODE or UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldBeFound(
            "loanAccountMutationCode.in=" + DEFAULT_LOAN_ACCOUNT_MUTATION_CODE + "," + UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        );

        // Get all the loanAccountCategoryList where loanAccountMutationCode equals to UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.in=" + UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode is not null
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationCode.specified=true");

        // Get all the loanAccountCategoryList where loanAccountMutationCode is null
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeContainsSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode contains DEFAULT_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationCode.contains=" + DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);

        // Get all the loanAccountCategoryList where loanAccountMutationCode contains UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.contains=" + UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationCode does not contain DEFAULT_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationCode.doesNotContain=" + DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);

        // Get all the loanAccountCategoryList where loanAccountMutationCode does not contain UPDATED_LOAN_ACCOUNT_MUTATION_CODE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationCode.doesNotContain=" + UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationType equals to DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationType.equals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE);

        // Get all the loanAccountCategoryList where loanAccountMutationType equals to UPDATED_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationType.equals=" + UPDATED_LOAN_ACCOUNT_MUTATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationType not equals to DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationType.notEquals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE);

        // Get all the loanAccountCategoryList where loanAccountMutationType not equals to UPDATED_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationType.notEquals=" + UPDATED_LOAN_ACCOUNT_MUTATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationType in DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE or UPDATED_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldBeFound(
            "loanAccountMutationType.in=" + DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE + "," + UPDATED_LOAN_ACCOUNT_MUTATION_TYPE
        );

        // Get all the loanAccountCategoryList where loanAccountMutationType equals to UPDATED_LOAN_ACCOUNT_MUTATION_TYPE
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationType.in=" + UPDATED_LOAN_ACCOUNT_MUTATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationType is not null
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationType.specified=true");

        // Get all the loanAccountCategoryList where loanAccountMutationType is null
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails equals to DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationDetails.equals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails equals to UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.equals=" + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails not equals to DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.notEquals=" + DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails not equals to UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationDetails.notEquals=" + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails in DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS or UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldBeFound(
            "loanAccountMutationDetails.in=" + DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS + "," + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        );

        // Get all the loanAccountCategoryList where loanAccountMutationDetails equals to UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.in=" + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails is not null
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationDetails.specified=true");

        // Get all the loanAccountCategoryList where loanAccountMutationDetails is null
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsContainsSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails contains DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationDetails.contains=" + DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails contains UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.contains=" + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllLoanAccountCategoriesByLoanAccountMutationDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails does not contain DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldNotBeFound("loanAccountMutationDetails.doesNotContain=" + DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);

        // Get all the loanAccountCategoryList where loanAccountMutationDetails does not contain UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS
        defaultLoanAccountCategoryShouldBeFound("loanAccountMutationDetails.doesNotContain=" + UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanAccountCategoryShouldBeFound(String filter) throws Exception {
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanAccountMutationCode").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE)))
            .andExpect(jsonPath("$.[*].loanAccountMutationType").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].loanAccountMutationDetails").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS)))
            .andExpect(
                jsonPath("$.[*].loanAccountMutationDescription").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanAccountCategoryShouldNotBeFound(String filter) throws Exception {
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanAccountCategory() throws Exception {
        // Get the loanAccountCategory
        restLoanAccountCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanAccountCategory() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();

        // Update the loanAccountCategory
        LoanAccountCategory updatedLoanAccountCategory = loanAccountCategoryRepository.findById(loanAccountCategory.getId()).get();
        // Disconnect from session so that the updates on updatedLoanAccountCategory are not directly saved in db
        em.detach(updatedLoanAccountCategory);
        updatedLoanAccountCategory
            .loanAccountMutationCode(UPDATED_LOAN_ACCOUNT_MUTATION_CODE)
            .loanAccountMutationType(UPDATED_LOAN_ACCOUNT_MUTATION_TYPE)
            .loanAccountMutationDetails(UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS)
            .loanAccountMutationDescription(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(updatedLoanAccountCategory);

        restLoanAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanAccountCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        LoanAccountCategory testLoanAccountCategory = loanAccountCategoryList.get(loanAccountCategoryList.size() - 1);
        assertThat(testLoanAccountCategory.getLoanAccountMutationCode()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationType()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_TYPE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDetails()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDescription()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository).save(testLoanAccountCategory);
    }

    @Test
    @Transactional
    void putNonExistingLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanAccountCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void partialUpdateLoanAccountCategoryWithPatch() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();

        // Update the loanAccountCategory using partial update
        LoanAccountCategory partialUpdatedLoanAccountCategory = new LoanAccountCategory();
        partialUpdatedLoanAccountCategory.setId(loanAccountCategory.getId());

        partialUpdatedLoanAccountCategory.loanAccountMutationDescription(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);

        restLoanAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanAccountCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanAccountCategory))
            )
            .andExpect(status().isOk());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        LoanAccountCategory testLoanAccountCategory = loanAccountCategoryList.get(loanAccountCategoryList.size() - 1);
        assertThat(testLoanAccountCategory.getLoanAccountMutationCode()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationType()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDetails()).isEqualTo(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDescription()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLoanAccountCategoryWithPatch() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();

        // Update the loanAccountCategory using partial update
        LoanAccountCategory partialUpdatedLoanAccountCategory = new LoanAccountCategory();
        partialUpdatedLoanAccountCategory.setId(loanAccountCategory.getId());

        partialUpdatedLoanAccountCategory
            .loanAccountMutationCode(UPDATED_LOAN_ACCOUNT_MUTATION_CODE)
            .loanAccountMutationType(UPDATED_LOAN_ACCOUNT_MUTATION_TYPE)
            .loanAccountMutationDetails(UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS)
            .loanAccountMutationDescription(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);

        restLoanAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanAccountCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanAccountCategory))
            )
            .andExpect(status().isOk());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);
        LoanAccountCategory testLoanAccountCategory = loanAccountCategoryList.get(loanAccountCategoryList.size() - 1);
        assertThat(testLoanAccountCategory.getLoanAccountMutationCode()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_CODE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationType()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_TYPE);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDetails()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_DETAILS);
        assertThat(testLoanAccountCategory.getLoanAccountMutationDescription()).isEqualTo(UPDATED_LOAN_ACCOUNT_MUTATION_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanAccountCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanAccountCategory() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountCategoryRepository.findAll().size();
        loanAccountCategory.setId(count.incrementAndGet());

        // Create the LoanAccountCategory
        LoanAccountCategoryDTO loanAccountCategoryDTO = loanAccountCategoryMapper.toDto(loanAccountCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanAccountCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanAccountCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanAccountCategory in the database
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(0)).save(loanAccountCategory);
    }

    @Test
    @Transactional
    void deleteLoanAccountCategory() throws Exception {
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);

        int databaseSizeBeforeDelete = loanAccountCategoryRepository.findAll().size();

        // Delete the loanAccountCategory
        restLoanAccountCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanAccountCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanAccountCategory> loanAccountCategoryList = loanAccountCategoryRepository.findAll();
        assertThat(loanAccountCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanAccountCategory in Elasticsearch
        verify(mockLoanAccountCategorySearchRepository, times(1)).deleteById(loanAccountCategory.getId());
    }

    @Test
    @Transactional
    void searchLoanAccountCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanAccountCategoryRepository.saveAndFlush(loanAccountCategory);
        when(mockLoanAccountCategorySearchRepository.search("id:" + loanAccountCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanAccountCategory), PageRequest.of(0, 1), 1));

        // Search the loanAccountCategory
        restLoanAccountCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanAccountCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccountCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanAccountMutationCode").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_CODE)))
            .andExpect(jsonPath("$.[*].loanAccountMutationType").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].loanAccountMutationDetails").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DETAILS)))
            .andExpect(
                jsonPath("$.[*].loanAccountMutationDescription").value(hasItem(DEFAULT_LOAN_ACCOUNT_MUTATION_DESCRIPTION.toString()))
            );
    }
}
