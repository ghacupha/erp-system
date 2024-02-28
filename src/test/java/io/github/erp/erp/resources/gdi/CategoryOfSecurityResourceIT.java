package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.CategoryOfSecurity;
import io.github.erp.repository.CategoryOfSecurityRepository;
import io.github.erp.repository.search.CategoryOfSecuritySearchRepository;
import io.github.erp.service.dto.CategoryOfSecurityDTO;
import io.github.erp.service.mapper.CategoryOfSecurityMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CategoryOfSecurityResource;
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

/**
 * Integration tests for the {@link CategoryOfSecurityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CategoryOfSecurityResourceIT {

    private static final String DEFAULT_CATEGORY_OF_SECURITY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_OF_SECURITY = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_OF_SECURITY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_OF_SECURITY_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/category-of-securities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/category-of-securities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryOfSecurityRepository categoryOfSecurityRepository;

    @Autowired
    private CategoryOfSecurityMapper categoryOfSecurityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CategoryOfSecuritySearchRepositoryMockConfiguration
     */
    @Autowired
    private CategoryOfSecuritySearchRepository mockCategoryOfSecuritySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryOfSecurityMockMvc;

    private CategoryOfSecurity categoryOfSecurity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryOfSecurity createEntity(EntityManager em) {
        CategoryOfSecurity categoryOfSecurity = new CategoryOfSecurity()
            .categoryOfSecurity(DEFAULT_CATEGORY_OF_SECURITY)
            .categoryOfSecurityDetails(DEFAULT_CATEGORY_OF_SECURITY_DETAILS)
            .categoryOfSecurityDescription(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION);
        return categoryOfSecurity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryOfSecurity createUpdatedEntity(EntityManager em) {
        CategoryOfSecurity categoryOfSecurity = new CategoryOfSecurity()
            .categoryOfSecurity(UPDATED_CATEGORY_OF_SECURITY)
            .categoryOfSecurityDetails(UPDATED_CATEGORY_OF_SECURITY_DETAILS)
            .categoryOfSecurityDescription(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);
        return categoryOfSecurity;
    }

    @BeforeEach
    public void initTest() {
        categoryOfSecurity = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeCreate = categoryOfSecurityRepository.findAll().size();
        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);
        restCategoryOfSecurityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryOfSecurity testCategoryOfSecurity = categoryOfSecurityList.get(categoryOfSecurityList.size() - 1);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurity()).isEqualTo(DEFAULT_CATEGORY_OF_SECURITY);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDetails()).isEqualTo(DEFAULT_CATEGORY_OF_SECURITY_DETAILS);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDescription()).isEqualTo(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(1)).save(testCategoryOfSecurity);
    }

    @Test
    @Transactional
    void createCategoryOfSecurityWithExistingId() throws Exception {
        // Create the CategoryOfSecurity with an existing ID
        categoryOfSecurity.setId(1L);
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        int databaseSizeBeforeCreate = categoryOfSecurityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryOfSecurityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeCreate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void checkCategoryOfSecurityIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryOfSecurityRepository.findAll().size();
        // set the field null
        categoryOfSecurity.setCategoryOfSecurity(null);

        // Create the CategoryOfSecurity, which fails.
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        restCategoryOfSecurityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryOfSecurityDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryOfSecurityRepository.findAll().size();
        // set the field null
        categoryOfSecurity.setCategoryOfSecurityDetails(null);

        // Create the CategoryOfSecurity, which fails.
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        restCategoryOfSecurityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecurities() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryOfSecurity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryOfSecurity").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDetails").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DETAILS)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDescription").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCategoryOfSecurity() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get the categoryOfSecurity
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryOfSecurity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryOfSecurity.getId().intValue()))
            .andExpect(jsonPath("$.categoryOfSecurity").value(DEFAULT_CATEGORY_OF_SECURITY))
            .andExpect(jsonPath("$.categoryOfSecurityDetails").value(DEFAULT_CATEGORY_OF_SECURITY_DETAILS))
            .andExpect(jsonPath("$.categoryOfSecurityDescription").value(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCategoryOfSecuritiesByIdFiltering() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        Long id = categoryOfSecurity.getId();

        defaultCategoryOfSecurityShouldBeFound("id.equals=" + id);
        defaultCategoryOfSecurityShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryOfSecurityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryOfSecurityShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryOfSecurityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryOfSecurityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity equals to DEFAULT_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurity.equals=" + DEFAULT_CATEGORY_OF_SECURITY);

        // Get all the categoryOfSecurityList where categoryOfSecurity equals to UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.equals=" + UPDATED_CATEGORY_OF_SECURITY);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity not equals to DEFAULT_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.notEquals=" + DEFAULT_CATEGORY_OF_SECURITY);

        // Get all the categoryOfSecurityList where categoryOfSecurity not equals to UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurity.notEquals=" + UPDATED_CATEGORY_OF_SECURITY);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityIsInShouldWork() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity in DEFAULT_CATEGORY_OF_SECURITY or UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldBeFound(
            "categoryOfSecurity.in=" + DEFAULT_CATEGORY_OF_SECURITY + "," + UPDATED_CATEGORY_OF_SECURITY
        );

        // Get all the categoryOfSecurityList where categoryOfSecurity equals to UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.in=" + UPDATED_CATEGORY_OF_SECURITY);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity is not null
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurity.specified=true");

        // Get all the categoryOfSecurityList where categoryOfSecurity is null
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityContainsSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity contains DEFAULT_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurity.contains=" + DEFAULT_CATEGORY_OF_SECURITY);

        // Get all the categoryOfSecurityList where categoryOfSecurity contains UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.contains=" + UPDATED_CATEGORY_OF_SECURITY);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityNotContainsSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurity does not contain DEFAULT_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurity.doesNotContain=" + DEFAULT_CATEGORY_OF_SECURITY);

        // Get all the categoryOfSecurityList where categoryOfSecurity does not contain UPDATED_CATEGORY_OF_SECURITY
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurity.doesNotContain=" + UPDATED_CATEGORY_OF_SECURITY);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails equals to DEFAULT_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurityDetails.equals=" + DEFAULT_CATEGORY_OF_SECURITY_DETAILS);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails equals to UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.equals=" + UPDATED_CATEGORY_OF_SECURITY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails not equals to DEFAULT_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.notEquals=" + DEFAULT_CATEGORY_OF_SECURITY_DETAILS);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails not equals to UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurityDetails.notEquals=" + UPDATED_CATEGORY_OF_SECURITY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails in DEFAULT_CATEGORY_OF_SECURITY_DETAILS or UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldBeFound(
            "categoryOfSecurityDetails.in=" + DEFAULT_CATEGORY_OF_SECURITY_DETAILS + "," + UPDATED_CATEGORY_OF_SECURITY_DETAILS
        );

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails equals to UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.in=" + UPDATED_CATEGORY_OF_SECURITY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails is not null
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurityDetails.specified=true");

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails is null
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsContainsSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails contains DEFAULT_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurityDetails.contains=" + DEFAULT_CATEGORY_OF_SECURITY_DETAILS);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails contains UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.contains=" + UPDATED_CATEGORY_OF_SECURITY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCategoryOfSecuritiesByCategoryOfSecurityDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails does not contain DEFAULT_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldNotBeFound("categoryOfSecurityDetails.doesNotContain=" + DEFAULT_CATEGORY_OF_SECURITY_DETAILS);

        // Get all the categoryOfSecurityList where categoryOfSecurityDetails does not contain UPDATED_CATEGORY_OF_SECURITY_DETAILS
        defaultCategoryOfSecurityShouldBeFound("categoryOfSecurityDetails.doesNotContain=" + UPDATED_CATEGORY_OF_SECURITY_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryOfSecurityShouldBeFound(String filter) throws Exception {
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryOfSecurity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryOfSecurity").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDetails").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DETAILS)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDescription").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryOfSecurityShouldNotBeFound(String filter) throws Exception {
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoryOfSecurity() throws Exception {
        // Get the categoryOfSecurity
        restCategoryOfSecurityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategoryOfSecurity() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();

        // Update the categoryOfSecurity
        CategoryOfSecurity updatedCategoryOfSecurity = categoryOfSecurityRepository.findById(categoryOfSecurity.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryOfSecurity are not directly saved in db
        em.detach(updatedCategoryOfSecurity);
        updatedCategoryOfSecurity
            .categoryOfSecurity(UPDATED_CATEGORY_OF_SECURITY)
            .categoryOfSecurityDetails(UPDATED_CATEGORY_OF_SECURITY_DETAILS)
            .categoryOfSecurityDescription(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(updatedCategoryOfSecurity);

        restCategoryOfSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryOfSecurityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);
        CategoryOfSecurity testCategoryOfSecurity = categoryOfSecurityList.get(categoryOfSecurityList.size() - 1);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurity()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDetails()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DETAILS);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDescription()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository).save(testCategoryOfSecurity);
    }

    @Test
    @Transactional
    void putNonExistingCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryOfSecurityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void partialUpdateCategoryOfSecurityWithPatch() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();

        // Update the categoryOfSecurity using partial update
        CategoryOfSecurity partialUpdatedCategoryOfSecurity = new CategoryOfSecurity();
        partialUpdatedCategoryOfSecurity.setId(categoryOfSecurity.getId());

        partialUpdatedCategoryOfSecurity
            .categoryOfSecurity(UPDATED_CATEGORY_OF_SECURITY)
            .categoryOfSecurityDetails(UPDATED_CATEGORY_OF_SECURITY_DETAILS)
            .categoryOfSecurityDescription(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);

        restCategoryOfSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryOfSecurity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryOfSecurity))
            )
            .andExpect(status().isOk());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);
        CategoryOfSecurity testCategoryOfSecurity = categoryOfSecurityList.get(categoryOfSecurityList.size() - 1);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurity()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDetails()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DETAILS);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDescription()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCategoryOfSecurityWithPatch() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();

        // Update the categoryOfSecurity using partial update
        CategoryOfSecurity partialUpdatedCategoryOfSecurity = new CategoryOfSecurity();
        partialUpdatedCategoryOfSecurity.setId(categoryOfSecurity.getId());

        partialUpdatedCategoryOfSecurity
            .categoryOfSecurity(UPDATED_CATEGORY_OF_SECURITY)
            .categoryOfSecurityDetails(UPDATED_CATEGORY_OF_SECURITY_DETAILS)
            .categoryOfSecurityDescription(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);

        restCategoryOfSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryOfSecurity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryOfSecurity))
            )
            .andExpect(status().isOk());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);
        CategoryOfSecurity testCategoryOfSecurity = categoryOfSecurityList.get(categoryOfSecurityList.size() - 1);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurity()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDetails()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DETAILS);
        assertThat(testCategoryOfSecurity.getCategoryOfSecurityDescription()).isEqualTo(UPDATED_CATEGORY_OF_SECURITY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryOfSecurityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryOfSecurity() throws Exception {
        int databaseSizeBeforeUpdate = categoryOfSecurityRepository.findAll().size();
        categoryOfSecurity.setId(count.incrementAndGet());

        // Create the CategoryOfSecurity
        CategoryOfSecurityDTO categoryOfSecurityDTO = categoryOfSecurityMapper.toDto(categoryOfSecurity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryOfSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryOfSecurityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryOfSecurity in the database
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(0)).save(categoryOfSecurity);
    }

    @Test
    @Transactional
    void deleteCategoryOfSecurity() throws Exception {
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);

        int databaseSizeBeforeDelete = categoryOfSecurityRepository.findAll().size();

        // Delete the categoryOfSecurity
        restCategoryOfSecurityMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryOfSecurity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoryOfSecurity> categoryOfSecurityList = categoryOfSecurityRepository.findAll();
        assertThat(categoryOfSecurityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CategoryOfSecurity in Elasticsearch
        verify(mockCategoryOfSecuritySearchRepository, times(1)).deleteById(categoryOfSecurity.getId());
    }

    @Test
    @Transactional
    void searchCategoryOfSecurity() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        categoryOfSecurityRepository.saveAndFlush(categoryOfSecurity);
        when(mockCategoryOfSecuritySearchRepository.search("id:" + categoryOfSecurity.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(categoryOfSecurity), PageRequest.of(0, 1), 1));

        // Search the categoryOfSecurity
        restCategoryOfSecurityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + categoryOfSecurity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryOfSecurity.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryOfSecurity").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDetails").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DETAILS)))
            .andExpect(jsonPath("$.[*].categoryOfSecurityDescription").value(hasItem(DEFAULT_CATEGORY_OF_SECURITY_DESCRIPTION.toString())));
    }
}
