package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.BouncedChequeCategories;
import io.github.erp.repository.BouncedChequeCategoriesRepository;
import io.github.erp.repository.search.BouncedChequeCategoriesSearchRepository;
import io.github.erp.service.criteria.BouncedChequeCategoriesCriteria;
import io.github.erp.service.dto.BouncedChequeCategoriesDTO;
import io.github.erp.service.mapper.BouncedChequeCategoriesMapper;
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
 * Integration tests for the {@link BouncedChequeCategoriesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BouncedChequeCategoriesResourceIT {

    private static final String DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bounced-cheque-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/bounced-cheque-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository;

    @Autowired
    private BouncedChequeCategoriesMapper bouncedChequeCategoriesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BouncedChequeCategoriesSearchRepositoryMockConfiguration
     */
    @Autowired
    private BouncedChequeCategoriesSearchRepository mockBouncedChequeCategoriesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBouncedChequeCategoriesMockMvc;

    private BouncedChequeCategories bouncedChequeCategories;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BouncedChequeCategories createEntity(EntityManager em) {
        BouncedChequeCategories bouncedChequeCategories = new BouncedChequeCategories()
            .bouncedChequeCategoryTypeCode(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)
            .bouncedChequeCategoryType(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);
        return bouncedChequeCategories;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BouncedChequeCategories createUpdatedEntity(EntityManager em) {
        BouncedChequeCategories bouncedChequeCategories = new BouncedChequeCategories()
            .bouncedChequeCategoryTypeCode(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)
            .bouncedChequeCategoryType(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
        return bouncedChequeCategories;
    }

    @BeforeEach
    public void initTest() {
        bouncedChequeCategories = createEntity(em);
    }

    @Test
    @Transactional
    void createBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeCreate = bouncedChequeCategoriesRepository.findAll().size();
        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);
        restBouncedChequeCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeCreate + 1);
        BouncedChequeCategories testBouncedChequeCategories = bouncedChequeCategoriesList.get(bouncedChequeCategoriesList.size() - 1);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryTypeCode()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryType()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(1)).save(testBouncedChequeCategories);
    }

    @Test
    @Transactional
    void createBouncedChequeCategoriesWithExistingId() throws Exception {
        // Create the BouncedChequeCategories with an existing ID
        bouncedChequeCategories.setId(1L);
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        int databaseSizeBeforeCreate = bouncedChequeCategoriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBouncedChequeCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeCreate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void checkBouncedChequeCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bouncedChequeCategoriesRepository.findAll().size();
        // set the field null
        bouncedChequeCategories.setBouncedChequeCategoryTypeCode(null);

        // Create the BouncedChequeCategories, which fails.
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        restBouncedChequeCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBouncedChequeCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bouncedChequeCategoriesRepository.findAll().size();
        // set the field null
        bouncedChequeCategories.setBouncedChequeCategoryType(null);

        // Create the BouncedChequeCategories, which fails.
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        restBouncedChequeCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategories() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bouncedChequeCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE)));
    }

    @Test
    @Transactional
    void getBouncedChequeCategories() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get the bouncedChequeCategories
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL_ID, bouncedChequeCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bouncedChequeCategories.getId().intValue()))
            .andExpect(jsonPath("$.bouncedChequeCategoryTypeCode").value(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.bouncedChequeCategoryType").value(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE));
    }

    @Test
    @Transactional
    void getBouncedChequeCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        Long id = bouncedChequeCategories.getId();

        defaultBouncedChequeCategoriesShouldBeFound("id.equals=" + id);
        defaultBouncedChequeCategoriesShouldNotBeFound("id.notEquals=" + id);

        defaultBouncedChequeCategoriesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBouncedChequeCategoriesShouldNotBeFound("id.greaterThan=" + id);

        defaultBouncedChequeCategoriesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBouncedChequeCategoriesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode equals to DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryTypeCode.equals=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryTypeCode.equals=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode not equals to DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldNotBeFound(
            "bouncedChequeCategoryTypeCode.notEquals=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        );

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode not equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryTypeCode.notEquals=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode in DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE or UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldBeFound(
            "bouncedChequeCategoryTypeCode.in=" +
            DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE +
            "," +
            UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        );

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryTypeCode.in=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode is not null
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryTypeCode.specified=true");

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode is null
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode contains DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryTypeCode.contains=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode contains UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldNotBeFound(
            "bouncedChequeCategoryTypeCode.contains=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode does not contain DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldNotBeFound(
            "bouncedChequeCategoryTypeCode.doesNotContain=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        );

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryTypeCode does not contain UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        defaultBouncedChequeCategoriesShouldBeFound(
            "bouncedChequeCategoryTypeCode.doesNotContain=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType equals to DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryType.equals=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.equals=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType not equals to DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.notEquals=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType not equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryType.notEquals=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType in DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE or UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldBeFound(
            "bouncedChequeCategoryType.in=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE + "," + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        );

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType equals to UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.in=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType is not null
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryType.specified=true");

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType is null
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.specified=false");
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeContainsSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType contains DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryType.contains=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType contains UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.contains=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllBouncedChequeCategoriesByBouncedChequeCategoryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType does not contain DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldNotBeFound("bouncedChequeCategoryType.doesNotContain=" + DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Get all the bouncedChequeCategoriesList where bouncedChequeCategoryType does not contain UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE
        defaultBouncedChequeCategoriesShouldBeFound("bouncedChequeCategoryType.doesNotContain=" + UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBouncedChequeCategoriesShouldBeFound(String filter) throws Exception {
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bouncedChequeCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE)));

        // Check, that the count call also returns 1
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBouncedChequeCategoriesShouldNotBeFound(String filter) throws Exception {
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBouncedChequeCategories() throws Exception {
        // Get the bouncedChequeCategories
        restBouncedChequeCategoriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBouncedChequeCategories() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();

        // Update the bouncedChequeCategories
        BouncedChequeCategories updatedBouncedChequeCategories = bouncedChequeCategoriesRepository
            .findById(bouncedChequeCategories.getId())
            .get();
        // Disconnect from session so that the updates on updatedBouncedChequeCategories are not directly saved in db
        em.detach(updatedBouncedChequeCategories);
        updatedBouncedChequeCategories
            .bouncedChequeCategoryTypeCode(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)
            .bouncedChequeCategoryType(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(updatedBouncedChequeCategories);

        restBouncedChequeCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bouncedChequeCategoriesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isOk());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);
        BouncedChequeCategories testBouncedChequeCategories = bouncedChequeCategoriesList.get(bouncedChequeCategoriesList.size() - 1);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryTypeCode()).isEqualTo(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryType()).isEqualTo(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository).save(testBouncedChequeCategories);
    }

    @Test
    @Transactional
    void putNonExistingBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bouncedChequeCategoriesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void putWithIdMismatchBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void partialUpdateBouncedChequeCategoriesWithPatch() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();

        // Update the bouncedChequeCategories using partial update
        BouncedChequeCategories partialUpdatedBouncedChequeCategories = new BouncedChequeCategories();
        partialUpdatedBouncedChequeCategories.setId(bouncedChequeCategories.getId());

        partialUpdatedBouncedChequeCategories.bouncedChequeCategoryTypeCode(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);

        restBouncedChequeCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBouncedChequeCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBouncedChequeCategories))
            )
            .andExpect(status().isOk());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);
        BouncedChequeCategories testBouncedChequeCategories = bouncedChequeCategoriesList.get(bouncedChequeCategoriesList.size() - 1);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryTypeCode()).isEqualTo(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryType()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBouncedChequeCategoriesWithPatch() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();

        // Update the bouncedChequeCategories using partial update
        BouncedChequeCategories partialUpdatedBouncedChequeCategories = new BouncedChequeCategories();
        partialUpdatedBouncedChequeCategories.setId(bouncedChequeCategories.getId());

        partialUpdatedBouncedChequeCategories
            .bouncedChequeCategoryTypeCode(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)
            .bouncedChequeCategoryType(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);

        restBouncedChequeCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBouncedChequeCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBouncedChequeCategories))
            )
            .andExpect(status().isOk());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);
        BouncedChequeCategories testBouncedChequeCategories = bouncedChequeCategoriesList.get(bouncedChequeCategoriesList.size() - 1);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryTypeCode()).isEqualTo(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE);
        assertThat(testBouncedChequeCategories.getBouncedChequeCategoryType()).isEqualTo(UPDATED_BOUNCED_CHEQUE_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bouncedChequeCategoriesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBouncedChequeCategories() throws Exception {
        int databaseSizeBeforeUpdate = bouncedChequeCategoriesRepository.findAll().size();
        bouncedChequeCategories.setId(count.incrementAndGet());

        // Create the BouncedChequeCategories
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO = bouncedChequeCategoriesMapper.toDto(bouncedChequeCategories);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBouncedChequeCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bouncedChequeCategoriesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BouncedChequeCategories in the database
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(0)).save(bouncedChequeCategories);
    }

    @Test
    @Transactional
    void deleteBouncedChequeCategories() throws Exception {
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);

        int databaseSizeBeforeDelete = bouncedChequeCategoriesRepository.findAll().size();

        // Delete the bouncedChequeCategories
        restBouncedChequeCategoriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, bouncedChequeCategories.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BouncedChequeCategories> bouncedChequeCategoriesList = bouncedChequeCategoriesRepository.findAll();
        assertThat(bouncedChequeCategoriesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BouncedChequeCategories in Elasticsearch
        verify(mockBouncedChequeCategoriesSearchRepository, times(1)).deleteById(bouncedChequeCategories.getId());
    }

    @Test
    @Transactional
    void searchBouncedChequeCategories() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        bouncedChequeCategoriesRepository.saveAndFlush(bouncedChequeCategories);
        when(mockBouncedChequeCategoriesSearchRepository.search("id:" + bouncedChequeCategories.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bouncedChequeCategories), PageRequest.of(0, 1), 1));

        // Search the bouncedChequeCategories
        restBouncedChequeCategoriesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bouncedChequeCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bouncedChequeCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeCategoryType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_CATEGORY_TYPE)));
    }
}
