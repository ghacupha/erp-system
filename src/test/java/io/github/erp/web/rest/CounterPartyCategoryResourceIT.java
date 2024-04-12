package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.CounterPartyCategory;
import io.github.erp.domain.enumeration.CounterpartyCategory;
import io.github.erp.repository.CounterPartyCategoryRepository;
import io.github.erp.repository.search.CounterPartyCategorySearchRepository;
import io.github.erp.service.criteria.CounterPartyCategoryCriteria;
import io.github.erp.service.dto.CounterPartyCategoryDTO;
import io.github.erp.service.mapper.CounterPartyCategoryMapper;
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
 * Integration tests for the {@link CounterPartyCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CounterPartyCategoryResourceIT {

    private static final String DEFAULT_COUNTERPARTY_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_CATEGORY_CODE = "BBBBBBBBBB";

    private static final CounterpartyCategory DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS = CounterpartyCategory.LOCAL;
    private static final CounterpartyCategory UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS = CounterpartyCategory.FOREIGN;

    private static final String DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/counter-party-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/counter-party-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CounterPartyCategoryRepository counterPartyCategoryRepository;

    @Autowired
    private CounterPartyCategoryMapper counterPartyCategoryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CounterPartyCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private CounterPartyCategorySearchRepository mockCounterPartyCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounterPartyCategoryMockMvc;

    private CounterPartyCategory counterPartyCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterPartyCategory createEntity(EntityManager em) {
        CounterPartyCategory counterPartyCategory = new CounterPartyCategory()
            .counterpartyCategoryCode(DEFAULT_COUNTERPARTY_CATEGORY_CODE)
            .counterpartyCategoryCodeDetails(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS)
            .counterpartyCategoryDescription(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION);
        return counterPartyCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterPartyCategory createUpdatedEntity(EntityManager em) {
        CounterPartyCategory counterPartyCategory = new CounterPartyCategory()
            .counterpartyCategoryCode(UPDATED_COUNTERPARTY_CATEGORY_CODE)
            .counterpartyCategoryCodeDetails(UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS)
            .counterpartyCategoryDescription(UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION);
        return counterPartyCategory;
    }

    @BeforeEach
    public void initTest() {
        counterPartyCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCounterPartyCategory() throws Exception {
        int databaseSizeBeforeCreate = counterPartyCategoryRepository.findAll().size();
        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);
        restCounterPartyCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CounterPartyCategory testCounterPartyCategory = counterPartyCategoryList.get(counterPartyCategoryList.size() - 1);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCode()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_CODE);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCodeDetails()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryDescription()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(1)).save(testCounterPartyCategory);
    }

    @Test
    @Transactional
    void createCounterPartyCategoryWithExistingId() throws Exception {
        // Create the CounterPartyCategory with an existing ID
        counterPartyCategory.setId(1L);
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        int databaseSizeBeforeCreate = counterPartyCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounterPartyCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void checkCounterpartyCategoryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterPartyCategoryRepository.findAll().size();
        // set the field null
        counterPartyCategory.setCounterpartyCategoryCode(null);

        // Create the CounterPartyCategory, which fails.
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        restCounterPartyCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCounterpartyCategoryCodeDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterPartyCategoryRepository.findAll().size();
        // set the field null
        counterPartyCategory.setCounterpartyCategoryCodeDetails(null);

        // Create the CounterPartyCategory, which fails.
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        restCounterPartyCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategories() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyCategoryCode").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryCodeDetails").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS.toString()))
            )
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryDescription").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getCounterPartyCategory() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get the counterPartyCategory
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, counterPartyCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(counterPartyCategory.getId().intValue()))
            .andExpect(jsonPath("$.counterpartyCategoryCode").value(DEFAULT_COUNTERPARTY_CATEGORY_CODE))
            .andExpect(jsonPath("$.counterpartyCategoryCodeDetails").value(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS.toString()))
            .andExpect(jsonPath("$.counterpartyCategoryDescription").value(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCounterPartyCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        Long id = counterPartyCategory.getId();

        defaultCounterPartyCategoryShouldBeFound("id.equals=" + id);
        defaultCounterPartyCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCounterPartyCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCounterPartyCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCounterPartyCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCounterPartyCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode equals to DEFAULT_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCode.equals=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode equals to UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.equals=" + UPDATED_COUNTERPARTY_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode not equals to DEFAULT_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.notEquals=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode not equals to UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCode.notEquals=" + UPDATED_COUNTERPARTY_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode in DEFAULT_COUNTERPARTY_CATEGORY_CODE or UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldBeFound(
            "counterpartyCategoryCode.in=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE + "," + UPDATED_COUNTERPARTY_CATEGORY_CODE
        );

        // Get all the counterPartyCategoryList where counterpartyCategoryCode equals to UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.in=" + UPDATED_COUNTERPARTY_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode is not null
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCode.specified=true");

        // Get all the counterPartyCategoryList where counterpartyCategoryCode is null
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode contains DEFAULT_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCode.contains=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode contains UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.contains=" + UPDATED_COUNTERPARTY_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode does not contain DEFAULT_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCode.doesNotContain=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE);

        // Get all the counterPartyCategoryList where counterpartyCategoryCode does not contain UPDATED_COUNTERPARTY_CATEGORY_CODE
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCode.doesNotContain=" + UPDATED_COUNTERPARTY_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails equals to DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCodeDetails.equals=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS);

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails equals to UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCodeDetails.equals=" + UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails not equals to DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldNotBeFound(
            "counterpartyCategoryCodeDetails.notEquals=" + DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS
        );

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails not equals to UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCodeDetails.notEquals=" + UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails in DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS or UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldBeFound(
            "counterpartyCategoryCodeDetails.in=" +
            DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS +
            "," +
            UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS
        );

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails equals to UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCodeDetails.in=" + UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyCategoriesByCounterpartyCategoryCodeDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails is not null
        defaultCounterPartyCategoryShouldBeFound("counterpartyCategoryCodeDetails.specified=true");

        // Get all the counterPartyCategoryList where counterpartyCategoryCodeDetails is null
        defaultCounterPartyCategoryShouldNotBeFound("counterpartyCategoryCodeDetails.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCounterPartyCategoryShouldBeFound(String filter) throws Exception {
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyCategoryCode").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryCodeDetails").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS.toString()))
            )
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryDescription").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCounterPartyCategoryShouldNotBeFound(String filter) throws Exception {
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounterPartyCategory() throws Exception {
        // Get the counterPartyCategory
        restCounterPartyCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCounterPartyCategory() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();

        // Update the counterPartyCategory
        CounterPartyCategory updatedCounterPartyCategory = counterPartyCategoryRepository.findById(counterPartyCategory.getId()).get();
        // Disconnect from session so that the updates on updatedCounterPartyCategory are not directly saved in db
        em.detach(updatedCounterPartyCategory);
        updatedCounterPartyCategory
            .counterpartyCategoryCode(UPDATED_COUNTERPARTY_CATEGORY_CODE)
            .counterpartyCategoryCodeDetails(UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS)
            .counterpartyCategoryDescription(UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION);
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(updatedCounterPartyCategory);

        restCounterPartyCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterPartyCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyCategory testCounterPartyCategory = counterPartyCategoryList.get(counterPartyCategoryList.size() - 1);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCode()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_CODE);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCodeDetails()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryDescription()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository).save(testCounterPartyCategory);
    }

    @Test
    @Transactional
    void putNonExistingCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterPartyCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void partialUpdateCounterPartyCategoryWithPatch() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();

        // Update the counterPartyCategory using partial update
        CounterPartyCategory partialUpdatedCounterPartyCategory = new CounterPartyCategory();
        partialUpdatedCounterPartyCategory.setId(counterPartyCategory.getId());

        restCounterPartyCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterPartyCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterPartyCategory))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyCategory testCounterPartyCategory = counterPartyCategoryList.get(counterPartyCategoryList.size() - 1);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCode()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_CODE);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCodeDetails()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryDescription()).isEqualTo(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCounterPartyCategoryWithPatch() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();

        // Update the counterPartyCategory using partial update
        CounterPartyCategory partialUpdatedCounterPartyCategory = new CounterPartyCategory();
        partialUpdatedCounterPartyCategory.setId(counterPartyCategory.getId());

        partialUpdatedCounterPartyCategory
            .counterpartyCategoryCode(UPDATED_COUNTERPARTY_CATEGORY_CODE)
            .counterpartyCategoryCodeDetails(UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS)
            .counterpartyCategoryDescription(UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION);

        restCounterPartyCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterPartyCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterPartyCategory))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyCategory testCounterPartyCategory = counterPartyCategoryList.get(counterPartyCategoryList.size() - 1);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCode()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_CODE);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryCodeDetails()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_CODE_DETAILS);
        assertThat(testCounterPartyCategory.getCounterpartyCategoryDescription()).isEqualTo(UPDATED_COUNTERPARTY_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counterPartyCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounterPartyCategory() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyCategoryRepository.findAll().size();
        counterPartyCategory.setId(count.incrementAndGet());

        // Create the CounterPartyCategory
        CounterPartyCategoryDTO counterPartyCategoryDTO = counterPartyCategoryMapper.toDto(counterPartyCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterPartyCategory in the database
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(0)).save(counterPartyCategory);
    }

    @Test
    @Transactional
    void deleteCounterPartyCategory() throws Exception {
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);

        int databaseSizeBeforeDelete = counterPartyCategoryRepository.findAll().size();

        // Delete the counterPartyCategory
        restCounterPartyCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, counterPartyCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CounterPartyCategory> counterPartyCategoryList = counterPartyCategoryRepository.findAll();
        assertThat(counterPartyCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CounterPartyCategory in Elasticsearch
        verify(mockCounterPartyCategorySearchRepository, times(1)).deleteById(counterPartyCategory.getId());
    }

    @Test
    @Transactional
    void searchCounterPartyCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        counterPartyCategoryRepository.saveAndFlush(counterPartyCategory);
        when(mockCounterPartyCategorySearchRepository.search("id:" + counterPartyCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(counterPartyCategory), PageRequest.of(0, 1), 1));

        // Search the counterPartyCategory
        restCounterPartyCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + counterPartyCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyCategoryCode").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryCodeDetails").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_CODE_DETAILS.toString()))
            )
            .andExpect(
                jsonPath("$.[*].counterpartyCategoryDescription").value(hasItem(DEFAULT_COUNTERPARTY_CATEGORY_DESCRIPTION.toString()))
            );
    }
}
