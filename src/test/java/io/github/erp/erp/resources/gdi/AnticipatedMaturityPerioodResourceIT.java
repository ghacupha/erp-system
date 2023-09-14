package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.AnticipatedMaturityPeriood;
import io.github.erp.repository.AnticipatedMaturityPerioodRepository;
import io.github.erp.repository.search.AnticipatedMaturityPerioodSearchRepository;
import io.github.erp.service.dto.AnticipatedMaturityPerioodDTO;
import io.github.erp.service.mapper.AnticipatedMaturityPerioodMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.AnticipatedMaturityPerioodResource;
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
 * Integration tests for the {@link AnticipatedMaturityPerioodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class AnticipatedMaturityPerioodResourceIT {

    private static final String DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ANTICIPATED_MATURITY_TENOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/anticipated-maturity-perioods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/anticipated-maturity-perioods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository;

    @Autowired
    private AnticipatedMaturityPerioodMapper anticipatedMaturityPerioodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AnticipatedMaturityPerioodSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnticipatedMaturityPerioodSearchRepository mockAnticipatedMaturityPerioodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnticipatedMaturityPerioodMockMvc;

    private AnticipatedMaturityPeriood anticipatedMaturityPeriood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnticipatedMaturityPeriood createEntity(EntityManager em) {
        AnticipatedMaturityPeriood anticipatedMaturityPeriood = new AnticipatedMaturityPeriood()
            .anticipatedMaturityTenorCode(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE)
            .aniticipatedMaturityTenorType(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE)
            .anticipatedMaturityTenorDetails(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS);
        return anticipatedMaturityPeriood;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnticipatedMaturityPeriood createUpdatedEntity(EntityManager em) {
        AnticipatedMaturityPeriood anticipatedMaturityPeriood = new AnticipatedMaturityPeriood()
            .anticipatedMaturityTenorCode(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE)
            .aniticipatedMaturityTenorType(UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE)
            .anticipatedMaturityTenorDetails(UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS);
        return anticipatedMaturityPeriood;
    }

    @BeforeEach
    public void initTest() {
        anticipatedMaturityPeriood = createEntity(em);
    }

    @Test
    @Transactional
    void createAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeCreate = anticipatedMaturityPerioodRepository.findAll().size();
        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeCreate + 1);
        AnticipatedMaturityPeriood testAnticipatedMaturityPeriood = anticipatedMaturityPerioodList.get(
            anticipatedMaturityPerioodList.size() - 1
        );
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorCode()).isEqualTo(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE);
        assertThat(testAnticipatedMaturityPeriood.getAniticipatedMaturityTenorType()).isEqualTo(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE);
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorDetails())
            .isEqualTo(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(1)).save(testAnticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void createAnticipatedMaturityPerioodWithExistingId() throws Exception {
        // Create the AnticipatedMaturityPeriood with an existing ID
        anticipatedMaturityPeriood.setId(1L);
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        int databaseSizeBeforeCreate = anticipatedMaturityPerioodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void checkAnticipatedMaturityTenorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = anticipatedMaturityPerioodRepository.findAll().size();
        // set the field null
        anticipatedMaturityPeriood.setAnticipatedMaturityTenorCode(null);

        // Create the AnticipatedMaturityPeriood, which fails.
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        restAnticipatedMaturityPerioodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAniticipatedMaturityTenorTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = anticipatedMaturityPerioodRepository.findAll().size();
        // set the field null
        anticipatedMaturityPeriood.setAniticipatedMaturityTenorType(null);

        // Create the AnticipatedMaturityPeriood, which fails.
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        restAnticipatedMaturityPerioodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioods() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anticipatedMaturityPeriood.getId().intValue())))
            .andExpect(jsonPath("$.[*].anticipatedMaturityTenorCode").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE)))
            .andExpect(jsonPath("$.[*].aniticipatedMaturityTenorType").value(hasItem(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE)))
            .andExpect(
                jsonPath("$.[*].anticipatedMaturityTenorDetails").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getAnticipatedMaturityPeriood() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get the anticipatedMaturityPeriood
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL_ID, anticipatedMaturityPeriood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anticipatedMaturityPeriood.getId().intValue()))
            .andExpect(jsonPath("$.anticipatedMaturityTenorCode").value(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE))
            .andExpect(jsonPath("$.aniticipatedMaturityTenorType").value(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE))
            .andExpect(jsonPath("$.anticipatedMaturityTenorDetails").value(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getAnticipatedMaturityPerioodsByIdFiltering() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        Long id = anticipatedMaturityPeriood.getId();

        defaultAnticipatedMaturityPerioodShouldBeFound("id.equals=" + id);
        defaultAnticipatedMaturityPerioodShouldNotBeFound("id.notEquals=" + id);

        defaultAnticipatedMaturityPerioodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnticipatedMaturityPerioodShouldNotBeFound("id.greaterThan=" + id);

        defaultAnticipatedMaturityPerioodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnticipatedMaturityPerioodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode equals to DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldBeFound("anticipatedMaturityTenorCode.equals=" + DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode equals to UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldNotBeFound("anticipatedMaturityTenorCode.equals=" + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode not equals to DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "anticipatedMaturityTenorCode.notEquals=" + DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        );

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode not equals to UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldBeFound("anticipatedMaturityTenorCode.notEquals=" + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode in DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE or UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "anticipatedMaturityTenorCode.in=" + DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE + "," + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        );

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode equals to UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldNotBeFound("anticipatedMaturityTenorCode.in=" + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode is not null
        defaultAnticipatedMaturityPerioodShouldBeFound("anticipatedMaturityTenorCode.specified=true");

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode is null
        defaultAnticipatedMaturityPerioodShouldNotBeFound("anticipatedMaturityTenorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeContainsSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode contains DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldBeFound("anticipatedMaturityTenorCode.contains=" + DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode contains UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "anticipatedMaturityTenorCode.contains=" + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        );
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAnticipatedMaturityTenorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode does not contain DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "anticipatedMaturityTenorCode.doesNotContain=" + DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE
        );

        // Get all the anticipatedMaturityPerioodList where anticipatedMaturityTenorCode does not contain UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "anticipatedMaturityTenorCode.doesNotContain=" + UPDATED_ANTICIPATED_MATURITY_TENOR_CODE
        );
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType equals to DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldBeFound("aniticipatedMaturityTenorType.equals=" + DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType equals to UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "aniticipatedMaturityTenorType.equals=" + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType not equals to DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "aniticipatedMaturityTenorType.notEquals=" + DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        );

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType not equals to UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "aniticipatedMaturityTenorType.notEquals=" + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeIsInShouldWork() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType in DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE or UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "aniticipatedMaturityTenorType.in=" + DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE + "," + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        );

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType equals to UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldNotBeFound("aniticipatedMaturityTenorType.in=" + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE);
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType is not null
        defaultAnticipatedMaturityPerioodShouldBeFound("aniticipatedMaturityTenorType.specified=true");

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType is null
        defaultAnticipatedMaturityPerioodShouldNotBeFound("aniticipatedMaturityTenorType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeContainsSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType contains DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "aniticipatedMaturityTenorType.contains=" + DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        );

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType contains UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "aniticipatedMaturityTenorType.contains=" + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAnticipatedMaturityPerioodsByAniticipatedMaturityTenorTypeNotContainsSomething() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType does not contain DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldNotBeFound(
            "aniticipatedMaturityTenorType.doesNotContain=" + DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE
        );

        // Get all the anticipatedMaturityPerioodList where aniticipatedMaturityTenorType does not contain UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        defaultAnticipatedMaturityPerioodShouldBeFound(
            "aniticipatedMaturityTenorType.doesNotContain=" + UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnticipatedMaturityPerioodShouldBeFound(String filter) throws Exception {
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anticipatedMaturityPeriood.getId().intValue())))
            .andExpect(jsonPath("$.[*].anticipatedMaturityTenorCode").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE)))
            .andExpect(jsonPath("$.[*].aniticipatedMaturityTenorType").value(hasItem(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE)))
            .andExpect(
                jsonPath("$.[*].anticipatedMaturityTenorDetails").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnticipatedMaturityPerioodShouldNotBeFound(String filter) throws Exception {
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnticipatedMaturityPeriood() throws Exception {
        // Get the anticipatedMaturityPeriood
        restAnticipatedMaturityPerioodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnticipatedMaturityPeriood() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();

        // Update the anticipatedMaturityPeriood
        AnticipatedMaturityPeriood updatedAnticipatedMaturityPeriood = anticipatedMaturityPerioodRepository
            .findById(anticipatedMaturityPeriood.getId())
            .get();
        // Disconnect from session so that the updates on updatedAnticipatedMaturityPeriood are not directly saved in db
        em.detach(updatedAnticipatedMaturityPeriood);
        updatedAnticipatedMaturityPeriood
            .anticipatedMaturityTenorCode(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE)
            .aniticipatedMaturityTenorType(UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE)
            .anticipatedMaturityTenorDetails(UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS);
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(
            updatedAnticipatedMaturityPeriood
        );

        restAnticipatedMaturityPerioodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anticipatedMaturityPerioodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);
        AnticipatedMaturityPeriood testAnticipatedMaturityPeriood = anticipatedMaturityPerioodList.get(
            anticipatedMaturityPerioodList.size() - 1
        );
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorCode()).isEqualTo(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
        assertThat(testAnticipatedMaturityPeriood.getAniticipatedMaturityTenorType()).isEqualTo(UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE);
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorDetails())
            .isEqualTo(UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository).save(testAnticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void putNonExistingAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anticipatedMaturityPerioodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void partialUpdateAnticipatedMaturityPerioodWithPatch() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();

        // Update the anticipatedMaturityPeriood using partial update
        AnticipatedMaturityPeriood partialUpdatedAnticipatedMaturityPeriood = new AnticipatedMaturityPeriood();
        partialUpdatedAnticipatedMaturityPeriood.setId(anticipatedMaturityPeriood.getId());

        partialUpdatedAnticipatedMaturityPeriood.anticipatedMaturityTenorCode(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);

        restAnticipatedMaturityPerioodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnticipatedMaturityPeriood.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnticipatedMaturityPeriood))
            )
            .andExpect(status().isOk());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);
        AnticipatedMaturityPeriood testAnticipatedMaturityPeriood = anticipatedMaturityPerioodList.get(
            anticipatedMaturityPerioodList.size() - 1
        );
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorCode()).isEqualTo(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
        assertThat(testAnticipatedMaturityPeriood.getAniticipatedMaturityTenorType()).isEqualTo(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE);
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorDetails())
            .isEqualTo(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateAnticipatedMaturityPerioodWithPatch() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();

        // Update the anticipatedMaturityPeriood using partial update
        AnticipatedMaturityPeriood partialUpdatedAnticipatedMaturityPeriood = new AnticipatedMaturityPeriood();
        partialUpdatedAnticipatedMaturityPeriood.setId(anticipatedMaturityPeriood.getId());

        partialUpdatedAnticipatedMaturityPeriood
            .anticipatedMaturityTenorCode(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE)
            .aniticipatedMaturityTenorType(UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE)
            .anticipatedMaturityTenorDetails(UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS);

        restAnticipatedMaturityPerioodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnticipatedMaturityPeriood.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnticipatedMaturityPeriood))
            )
            .andExpect(status().isOk());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);
        AnticipatedMaturityPeriood testAnticipatedMaturityPeriood = anticipatedMaturityPerioodList.get(
            anticipatedMaturityPerioodList.size() - 1
        );
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorCode()).isEqualTo(UPDATED_ANTICIPATED_MATURITY_TENOR_CODE);
        assertThat(testAnticipatedMaturityPeriood.getAniticipatedMaturityTenorType()).isEqualTo(UPDATED_ANITICIPATED_MATURITY_TENOR_TYPE);
        assertThat(testAnticipatedMaturityPeriood.getAnticipatedMaturityTenorDetails())
            .isEqualTo(UPDATED_ANTICIPATED_MATURITY_TENOR_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anticipatedMaturityPerioodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnticipatedMaturityPeriood() throws Exception {
        int databaseSizeBeforeUpdate = anticipatedMaturityPerioodRepository.findAll().size();
        anticipatedMaturityPeriood.setId(count.incrementAndGet());

        // Create the AnticipatedMaturityPeriood
        AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnticipatedMaturityPerioodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anticipatedMaturityPerioodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnticipatedMaturityPeriood in the database
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(0)).save(anticipatedMaturityPeriood);
    }

    @Test
    @Transactional
    void deleteAnticipatedMaturityPeriood() throws Exception {
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);

        int databaseSizeBeforeDelete = anticipatedMaturityPerioodRepository.findAll().size();

        // Delete the anticipatedMaturityPeriood
        restAnticipatedMaturityPerioodMockMvc
            .perform(delete(ENTITY_API_URL_ID, anticipatedMaturityPeriood.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnticipatedMaturityPeriood> anticipatedMaturityPerioodList = anticipatedMaturityPerioodRepository.findAll();
        assertThat(anticipatedMaturityPerioodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnticipatedMaturityPeriood in Elasticsearch
        verify(mockAnticipatedMaturityPerioodSearchRepository, times(1)).deleteById(anticipatedMaturityPeriood.getId());
    }

    @Test
    @Transactional
    void searchAnticipatedMaturityPeriood() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        anticipatedMaturityPerioodRepository.saveAndFlush(anticipatedMaturityPeriood);
        when(mockAnticipatedMaturityPerioodSearchRepository.search("id:" + anticipatedMaturityPeriood.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(anticipatedMaturityPeriood), PageRequest.of(0, 1), 1));

        // Search the anticipatedMaturityPeriood
        restAnticipatedMaturityPerioodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + anticipatedMaturityPeriood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anticipatedMaturityPeriood.getId().intValue())))
            .andExpect(jsonPath("$.[*].anticipatedMaturityTenorCode").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_CODE)))
            .andExpect(jsonPath("$.[*].aniticipatedMaturityTenorType").value(hasItem(DEFAULT_ANITICIPATED_MATURITY_TENOR_TYPE)))
            .andExpect(
                jsonPath("$.[*].anticipatedMaturityTenorDetails").value(hasItem(DEFAULT_ANTICIPATED_MATURITY_TENOR_DETAILS.toString()))
            );
    }
}
