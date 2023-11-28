package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.IsicEconomicActivity;
import io.github.erp.repository.IsicEconomicActivityRepository;
import io.github.erp.repository.search.IsicEconomicActivitySearchRepository;
import io.github.erp.service.criteria.IsicEconomicActivityCriteria;
import io.github.erp.service.dto.IsicEconomicActivityDTO;
import io.github.erp.service.mapper.IsicEconomicActivityMapper;
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
 * Integration tests for the {@link IsicEconomicActivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IsicEconomicActivityResourceIT {

    private static final String DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/isic-economic-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/isic-economic-activities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsicEconomicActivityRepository isicEconomicActivityRepository;

    @Autowired
    private IsicEconomicActivityMapper isicEconomicActivityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.IsicEconomicActivitySearchRepositoryMockConfiguration
     */
    @Autowired
    private IsicEconomicActivitySearchRepository mockIsicEconomicActivitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsicEconomicActivityMockMvc;

    private IsicEconomicActivity isicEconomicActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsicEconomicActivity createEntity(EntityManager em) {
        IsicEconomicActivity isicEconomicActivity = new IsicEconomicActivity()
            .businessEconomicActivityCode(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE)
            .section(DEFAULT_SECTION)
            .sectionLabel(DEFAULT_SECTION_LABEL)
            .division(DEFAULT_DIVISION)
            .divisionLabel(DEFAULT_DIVISION_LABEL)
            .groupCode(DEFAULT_GROUP_CODE)
            .groupLabel(DEFAULT_GROUP_LABEL)
            .classCode(DEFAULT_CLASS_CODE)
            .businessEconomicActivityType(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE)
            .businessEconomicActivityTypeDescription(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);
        return isicEconomicActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsicEconomicActivity createUpdatedEntity(EntityManager em) {
        IsicEconomicActivity isicEconomicActivity = new IsicEconomicActivity()
            .businessEconomicActivityCode(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE)
            .section(UPDATED_SECTION)
            .sectionLabel(UPDATED_SECTION_LABEL)
            .division(UPDATED_DIVISION)
            .divisionLabel(UPDATED_DIVISION_LABEL)
            .groupCode(UPDATED_GROUP_CODE)
            .groupLabel(UPDATED_GROUP_LABEL)
            .classCode(UPDATED_CLASS_CODE)
            .businessEconomicActivityType(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE)
            .businessEconomicActivityTypeDescription(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);
        return isicEconomicActivity;
    }

    @BeforeEach
    public void initTest() {
        isicEconomicActivity = createEntity(em);
    }

    @Test
    @Transactional
    void createIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeCreate = isicEconomicActivityRepository.findAll().size();
        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);
        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeCreate + 1);
        IsicEconomicActivity testIsicEconomicActivity = isicEconomicActivityList.get(isicEconomicActivityList.size() - 1);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityCode()).isEqualTo(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE);
        assertThat(testIsicEconomicActivity.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testIsicEconomicActivity.getSectionLabel()).isEqualTo(DEFAULT_SECTION_LABEL);
        assertThat(testIsicEconomicActivity.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testIsicEconomicActivity.getDivisionLabel()).isEqualTo(DEFAULT_DIVISION_LABEL);
        assertThat(testIsicEconomicActivity.getGroupCode()).isEqualTo(DEFAULT_GROUP_CODE);
        assertThat(testIsicEconomicActivity.getGroupLabel()).isEqualTo(DEFAULT_GROUP_LABEL);
        assertThat(testIsicEconomicActivity.getClassCode()).isEqualTo(DEFAULT_CLASS_CODE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityType()).isEqualTo(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityTypeDescription())
            .isEqualTo(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(1)).save(testIsicEconomicActivity);
    }

    @Test
    @Transactional
    void createIsicEconomicActivityWithExistingId() throws Exception {
        // Create the IsicEconomicActivity with an existing ID
        isicEconomicActivity.setId(1L);
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        int databaseSizeBeforeCreate = isicEconomicActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeCreate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void checkBusinessEconomicActivityCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setBusinessEconomicActivityCode(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setSection(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setSectionLabel(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDivisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setDivision(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDivisionLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setDivisionLabel(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGroupLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setGroupLabel(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = isicEconomicActivityRepository.findAll().size();
        // set the field null
        isicEconomicActivity.setClassCode(null);

        // Create the IsicEconomicActivity, which fails.
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivities() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isicEconomicActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicActivityCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].sectionLabel").value(hasItem(DEFAULT_SECTION_LABEL)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].divisionLabel").value(hasItem(DEFAULT_DIVISION_LABEL)))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE)))
            .andExpect(jsonPath("$.[*].groupLabel").value(hasItem(DEFAULT_GROUP_LABEL)))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicActivityType").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].businessEconomicActivityTypeDescription")
                    .value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getIsicEconomicActivity() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get the isicEconomicActivity
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, isicEconomicActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isicEconomicActivity.getId().intValue()))
            .andExpect(jsonPath("$.businessEconomicActivityCode").value(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.sectionLabel").value(DEFAULT_SECTION_LABEL))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION))
            .andExpect(jsonPath("$.divisionLabel").value(DEFAULT_DIVISION_LABEL))
            .andExpect(jsonPath("$.groupCode").value(DEFAULT_GROUP_CODE))
            .andExpect(jsonPath("$.groupLabel").value(DEFAULT_GROUP_LABEL))
            .andExpect(jsonPath("$.classCode").value(DEFAULT_CLASS_CODE))
            .andExpect(jsonPath("$.businessEconomicActivityType").value(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE))
            .andExpect(
                jsonPath("$.businessEconomicActivityTypeDescription").value(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION.toString())
            );
    }

    @Test
    @Transactional
    void getIsicEconomicActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        Long id = isicEconomicActivity.getId();

        defaultIsicEconomicActivityShouldBeFound("id.equals=" + id);
        defaultIsicEconomicActivityShouldNotBeFound("id.notEquals=" + id);

        defaultIsicEconomicActivityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIsicEconomicActivityShouldNotBeFound("id.greaterThan=" + id);

        defaultIsicEconomicActivityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIsicEconomicActivityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode equals to DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityCode.equals=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityCode.equals=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode not equals to DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityCode.notEquals=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode not equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityCode.notEquals=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode in DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE or UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldBeFound(
            "businessEconomicActivityCode.in=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE + "," + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        );

        // Get all the isicEconomicActivityList where businessEconomicActivityCode equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityCode.in=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode is not null
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityCode.specified=true");

        // Get all the isicEconomicActivityList where businessEconomicActivityCode is null
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode contains DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityCode.contains=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode contains UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityCode.contains=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityCode does not contain DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldNotBeFound(
            "businessEconomicActivityCode.doesNotContain=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE
        );

        // Get all the isicEconomicActivityList where businessEconomicActivityCode does not contain UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityCode.doesNotContain=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section equals to DEFAULT_SECTION
        defaultIsicEconomicActivityShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the isicEconomicActivityList where section equals to UPDATED_SECTION
        defaultIsicEconomicActivityShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section not equals to DEFAULT_SECTION
        defaultIsicEconomicActivityShouldNotBeFound("section.notEquals=" + DEFAULT_SECTION);

        // Get all the isicEconomicActivityList where section not equals to UPDATED_SECTION
        defaultIsicEconomicActivityShouldBeFound("section.notEquals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultIsicEconomicActivityShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the isicEconomicActivityList where section equals to UPDATED_SECTION
        defaultIsicEconomicActivityShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section is not null
        defaultIsicEconomicActivityShouldBeFound("section.specified=true");

        // Get all the isicEconomicActivityList where section is null
        defaultIsicEconomicActivityShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section contains DEFAULT_SECTION
        defaultIsicEconomicActivityShouldBeFound("section.contains=" + DEFAULT_SECTION);

        // Get all the isicEconomicActivityList where section contains UPDATED_SECTION
        defaultIsicEconomicActivityShouldNotBeFound("section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where section does not contain DEFAULT_SECTION
        defaultIsicEconomicActivityShouldNotBeFound("section.doesNotContain=" + DEFAULT_SECTION);

        // Get all the isicEconomicActivityList where section does not contain UPDATED_SECTION
        defaultIsicEconomicActivityShouldBeFound("section.doesNotContain=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel equals to DEFAULT_SECTION_LABEL
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.equals=" + DEFAULT_SECTION_LABEL);

        // Get all the isicEconomicActivityList where sectionLabel equals to UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.equals=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel not equals to DEFAULT_SECTION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.notEquals=" + DEFAULT_SECTION_LABEL);

        // Get all the isicEconomicActivityList where sectionLabel not equals to UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.notEquals=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel in DEFAULT_SECTION_LABEL or UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.in=" + DEFAULT_SECTION_LABEL + "," + UPDATED_SECTION_LABEL);

        // Get all the isicEconomicActivityList where sectionLabel equals to UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.in=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel is not null
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.specified=true");

        // Get all the isicEconomicActivityList where sectionLabel is null
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel contains DEFAULT_SECTION_LABEL
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.contains=" + DEFAULT_SECTION_LABEL);

        // Get all the isicEconomicActivityList where sectionLabel contains UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.contains=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesBySectionLabelNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where sectionLabel does not contain DEFAULT_SECTION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("sectionLabel.doesNotContain=" + DEFAULT_SECTION_LABEL);

        // Get all the isicEconomicActivityList where sectionLabel does not contain UPDATED_SECTION_LABEL
        defaultIsicEconomicActivityShouldBeFound("sectionLabel.doesNotContain=" + UPDATED_SECTION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division equals to DEFAULT_DIVISION
        defaultIsicEconomicActivityShouldBeFound("division.equals=" + DEFAULT_DIVISION);

        // Get all the isicEconomicActivityList where division equals to UPDATED_DIVISION
        defaultIsicEconomicActivityShouldNotBeFound("division.equals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division not equals to DEFAULT_DIVISION
        defaultIsicEconomicActivityShouldNotBeFound("division.notEquals=" + DEFAULT_DIVISION);

        // Get all the isicEconomicActivityList where division not equals to UPDATED_DIVISION
        defaultIsicEconomicActivityShouldBeFound("division.notEquals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division in DEFAULT_DIVISION or UPDATED_DIVISION
        defaultIsicEconomicActivityShouldBeFound("division.in=" + DEFAULT_DIVISION + "," + UPDATED_DIVISION);

        // Get all the isicEconomicActivityList where division equals to UPDATED_DIVISION
        defaultIsicEconomicActivityShouldNotBeFound("division.in=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division is not null
        defaultIsicEconomicActivityShouldBeFound("division.specified=true");

        // Get all the isicEconomicActivityList where division is null
        defaultIsicEconomicActivityShouldNotBeFound("division.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division contains DEFAULT_DIVISION
        defaultIsicEconomicActivityShouldBeFound("division.contains=" + DEFAULT_DIVISION);

        // Get all the isicEconomicActivityList where division contains UPDATED_DIVISION
        defaultIsicEconomicActivityShouldNotBeFound("division.contains=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where division does not contain DEFAULT_DIVISION
        defaultIsicEconomicActivityShouldNotBeFound("division.doesNotContain=" + DEFAULT_DIVISION);

        // Get all the isicEconomicActivityList where division does not contain UPDATED_DIVISION
        defaultIsicEconomicActivityShouldBeFound("division.doesNotContain=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel equals to DEFAULT_DIVISION_LABEL
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.equals=" + DEFAULT_DIVISION_LABEL);

        // Get all the isicEconomicActivityList where divisionLabel equals to UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.equals=" + UPDATED_DIVISION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel not equals to DEFAULT_DIVISION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.notEquals=" + DEFAULT_DIVISION_LABEL);

        // Get all the isicEconomicActivityList where divisionLabel not equals to UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.notEquals=" + UPDATED_DIVISION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel in DEFAULT_DIVISION_LABEL or UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.in=" + DEFAULT_DIVISION_LABEL + "," + UPDATED_DIVISION_LABEL);

        // Get all the isicEconomicActivityList where divisionLabel equals to UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.in=" + UPDATED_DIVISION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel is not null
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.specified=true");

        // Get all the isicEconomicActivityList where divisionLabel is null
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel contains DEFAULT_DIVISION_LABEL
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.contains=" + DEFAULT_DIVISION_LABEL);

        // Get all the isicEconomicActivityList where divisionLabel contains UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.contains=" + UPDATED_DIVISION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByDivisionLabelNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where divisionLabel does not contain DEFAULT_DIVISION_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("divisionLabel.doesNotContain=" + DEFAULT_DIVISION_LABEL);

        // Get all the isicEconomicActivityList where divisionLabel does not contain UPDATED_DIVISION_LABEL
        defaultIsicEconomicActivityShouldBeFound("divisionLabel.doesNotContain=" + UPDATED_DIVISION_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode equals to DEFAULT_GROUP_CODE
        defaultIsicEconomicActivityShouldBeFound("groupCode.equals=" + DEFAULT_GROUP_CODE);

        // Get all the isicEconomicActivityList where groupCode equals to UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.equals=" + UPDATED_GROUP_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode not equals to DEFAULT_GROUP_CODE
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.notEquals=" + DEFAULT_GROUP_CODE);

        // Get all the isicEconomicActivityList where groupCode not equals to UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldBeFound("groupCode.notEquals=" + UPDATED_GROUP_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode in DEFAULT_GROUP_CODE or UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldBeFound("groupCode.in=" + DEFAULT_GROUP_CODE + "," + UPDATED_GROUP_CODE);

        // Get all the isicEconomicActivityList where groupCode equals to UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.in=" + UPDATED_GROUP_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode is not null
        defaultIsicEconomicActivityShouldBeFound("groupCode.specified=true");

        // Get all the isicEconomicActivityList where groupCode is null
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode contains DEFAULT_GROUP_CODE
        defaultIsicEconomicActivityShouldBeFound("groupCode.contains=" + DEFAULT_GROUP_CODE);

        // Get all the isicEconomicActivityList where groupCode contains UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.contains=" + UPDATED_GROUP_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupCodeNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupCode does not contain DEFAULT_GROUP_CODE
        defaultIsicEconomicActivityShouldNotBeFound("groupCode.doesNotContain=" + DEFAULT_GROUP_CODE);

        // Get all the isicEconomicActivityList where groupCode does not contain UPDATED_GROUP_CODE
        defaultIsicEconomicActivityShouldBeFound("groupCode.doesNotContain=" + UPDATED_GROUP_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel equals to DEFAULT_GROUP_LABEL
        defaultIsicEconomicActivityShouldBeFound("groupLabel.equals=" + DEFAULT_GROUP_LABEL);

        // Get all the isicEconomicActivityList where groupLabel equals to UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.equals=" + UPDATED_GROUP_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel not equals to DEFAULT_GROUP_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.notEquals=" + DEFAULT_GROUP_LABEL);

        // Get all the isicEconomicActivityList where groupLabel not equals to UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldBeFound("groupLabel.notEquals=" + UPDATED_GROUP_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel in DEFAULT_GROUP_LABEL or UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldBeFound("groupLabel.in=" + DEFAULT_GROUP_LABEL + "," + UPDATED_GROUP_LABEL);

        // Get all the isicEconomicActivityList where groupLabel equals to UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.in=" + UPDATED_GROUP_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel is not null
        defaultIsicEconomicActivityShouldBeFound("groupLabel.specified=true");

        // Get all the isicEconomicActivityList where groupLabel is null
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel contains DEFAULT_GROUP_LABEL
        defaultIsicEconomicActivityShouldBeFound("groupLabel.contains=" + DEFAULT_GROUP_LABEL);

        // Get all the isicEconomicActivityList where groupLabel contains UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.contains=" + UPDATED_GROUP_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByGroupLabelNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where groupLabel does not contain DEFAULT_GROUP_LABEL
        defaultIsicEconomicActivityShouldNotBeFound("groupLabel.doesNotContain=" + DEFAULT_GROUP_LABEL);

        // Get all the isicEconomicActivityList where groupLabel does not contain UPDATED_GROUP_LABEL
        defaultIsicEconomicActivityShouldBeFound("groupLabel.doesNotContain=" + UPDATED_GROUP_LABEL);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode equals to DEFAULT_CLASS_CODE
        defaultIsicEconomicActivityShouldBeFound("classCode.equals=" + DEFAULT_CLASS_CODE);

        // Get all the isicEconomicActivityList where classCode equals to UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldNotBeFound("classCode.equals=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode not equals to DEFAULT_CLASS_CODE
        defaultIsicEconomicActivityShouldNotBeFound("classCode.notEquals=" + DEFAULT_CLASS_CODE);

        // Get all the isicEconomicActivityList where classCode not equals to UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldBeFound("classCode.notEquals=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode in DEFAULT_CLASS_CODE or UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldBeFound("classCode.in=" + DEFAULT_CLASS_CODE + "," + UPDATED_CLASS_CODE);

        // Get all the isicEconomicActivityList where classCode equals to UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldNotBeFound("classCode.in=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode is not null
        defaultIsicEconomicActivityShouldBeFound("classCode.specified=true");

        // Get all the isicEconomicActivityList where classCode is null
        defaultIsicEconomicActivityShouldNotBeFound("classCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode contains DEFAULT_CLASS_CODE
        defaultIsicEconomicActivityShouldBeFound("classCode.contains=" + DEFAULT_CLASS_CODE);

        // Get all the isicEconomicActivityList where classCode contains UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldNotBeFound("classCode.contains=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByClassCodeNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where classCode does not contain DEFAULT_CLASS_CODE
        defaultIsicEconomicActivityShouldNotBeFound("classCode.doesNotContain=" + DEFAULT_CLASS_CODE);

        // Get all the isicEconomicActivityList where classCode does not contain UPDATED_CLASS_CODE
        defaultIsicEconomicActivityShouldBeFound("classCode.doesNotContain=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType equals to DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityType.equals=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE);

        // Get all the isicEconomicActivityList where businessEconomicActivityType equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityType.equals=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType not equals to DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityType.notEquals=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE);

        // Get all the isicEconomicActivityList where businessEconomicActivityType not equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityType.notEquals=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType in DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE or UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldBeFound(
            "businessEconomicActivityType.in=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE + "," + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        );

        // Get all the isicEconomicActivityList where businessEconomicActivityType equals to UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityType.in=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType is not null
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityType.specified=true");

        // Get all the isicEconomicActivityList where businessEconomicActivityType is null
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityType.specified=false");
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType contains DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityType.contains=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE);

        // Get all the isicEconomicActivityList where businessEconomicActivityType contains UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldNotBeFound("businessEconomicActivityType.contains=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    void getAllIsicEconomicActivitiesByBusinessEconomicActivityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        // Get all the isicEconomicActivityList where businessEconomicActivityType does not contain DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldNotBeFound(
            "businessEconomicActivityType.doesNotContain=" + DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        );

        // Get all the isicEconomicActivityList where businessEconomicActivityType does not contain UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE
        defaultIsicEconomicActivityShouldBeFound("businessEconomicActivityType.doesNotContain=" + UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIsicEconomicActivityShouldBeFound(String filter) throws Exception {
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isicEconomicActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicActivityCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].sectionLabel").value(hasItem(DEFAULT_SECTION_LABEL)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].divisionLabel").value(hasItem(DEFAULT_DIVISION_LABEL)))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE)))
            .andExpect(jsonPath("$.[*].groupLabel").value(hasItem(DEFAULT_GROUP_LABEL)))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicActivityType").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].businessEconomicActivityTypeDescription")
                    .value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIsicEconomicActivityShouldNotBeFound(String filter) throws Exception {
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIsicEconomicActivity() throws Exception {
        // Get the isicEconomicActivity
        restIsicEconomicActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIsicEconomicActivity() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();

        // Update the isicEconomicActivity
        IsicEconomicActivity updatedIsicEconomicActivity = isicEconomicActivityRepository.findById(isicEconomicActivity.getId()).get();
        // Disconnect from session so that the updates on updatedIsicEconomicActivity are not directly saved in db
        em.detach(updatedIsicEconomicActivity);
        updatedIsicEconomicActivity
            .businessEconomicActivityCode(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE)
            .section(UPDATED_SECTION)
            .sectionLabel(UPDATED_SECTION_LABEL)
            .division(UPDATED_DIVISION)
            .divisionLabel(UPDATED_DIVISION_LABEL)
            .groupCode(UPDATED_GROUP_CODE)
            .groupLabel(UPDATED_GROUP_LABEL)
            .classCode(UPDATED_CLASS_CODE)
            .businessEconomicActivityType(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE)
            .businessEconomicActivityTypeDescription(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(updatedIsicEconomicActivity);

        restIsicEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isicEconomicActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);
        IsicEconomicActivity testIsicEconomicActivity = isicEconomicActivityList.get(isicEconomicActivityList.size() - 1);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityCode()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
        assertThat(testIsicEconomicActivity.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testIsicEconomicActivity.getSectionLabel()).isEqualTo(UPDATED_SECTION_LABEL);
        assertThat(testIsicEconomicActivity.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testIsicEconomicActivity.getDivisionLabel()).isEqualTo(UPDATED_DIVISION_LABEL);
        assertThat(testIsicEconomicActivity.getGroupCode()).isEqualTo(UPDATED_GROUP_CODE);
        assertThat(testIsicEconomicActivity.getGroupLabel()).isEqualTo(UPDATED_GROUP_LABEL);
        assertThat(testIsicEconomicActivity.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityType()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityTypeDescription())
            .isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository).save(testIsicEconomicActivity);
    }

    @Test
    @Transactional
    void putNonExistingIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isicEconomicActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void partialUpdateIsicEconomicActivityWithPatch() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();

        // Update the isicEconomicActivity using partial update
        IsicEconomicActivity partialUpdatedIsicEconomicActivity = new IsicEconomicActivity();
        partialUpdatedIsicEconomicActivity.setId(isicEconomicActivity.getId());

        partialUpdatedIsicEconomicActivity
            .businessEconomicActivityCode(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE)
            .section(UPDATED_SECTION)
            .sectionLabel(UPDATED_SECTION_LABEL)
            .divisionLabel(UPDATED_DIVISION_LABEL)
            .classCode(UPDATED_CLASS_CODE)
            .businessEconomicActivityType(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE)
            .businessEconomicActivityTypeDescription(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);

        restIsicEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsicEconomicActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsicEconomicActivity))
            )
            .andExpect(status().isOk());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);
        IsicEconomicActivity testIsicEconomicActivity = isicEconomicActivityList.get(isicEconomicActivityList.size() - 1);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityCode()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
        assertThat(testIsicEconomicActivity.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testIsicEconomicActivity.getSectionLabel()).isEqualTo(UPDATED_SECTION_LABEL);
        assertThat(testIsicEconomicActivity.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testIsicEconomicActivity.getDivisionLabel()).isEqualTo(UPDATED_DIVISION_LABEL);
        assertThat(testIsicEconomicActivity.getGroupCode()).isEqualTo(DEFAULT_GROUP_CODE);
        assertThat(testIsicEconomicActivity.getGroupLabel()).isEqualTo(DEFAULT_GROUP_LABEL);
        assertThat(testIsicEconomicActivity.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityType()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityTypeDescription())
            .isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateIsicEconomicActivityWithPatch() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();

        // Update the isicEconomicActivity using partial update
        IsicEconomicActivity partialUpdatedIsicEconomicActivity = new IsicEconomicActivity();
        partialUpdatedIsicEconomicActivity.setId(isicEconomicActivity.getId());

        partialUpdatedIsicEconomicActivity
            .businessEconomicActivityCode(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE)
            .section(UPDATED_SECTION)
            .sectionLabel(UPDATED_SECTION_LABEL)
            .division(UPDATED_DIVISION)
            .divisionLabel(UPDATED_DIVISION_LABEL)
            .groupCode(UPDATED_GROUP_CODE)
            .groupLabel(UPDATED_GROUP_LABEL)
            .classCode(UPDATED_CLASS_CODE)
            .businessEconomicActivityType(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE)
            .businessEconomicActivityTypeDescription(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);

        restIsicEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsicEconomicActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsicEconomicActivity))
            )
            .andExpect(status().isOk());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);
        IsicEconomicActivity testIsicEconomicActivity = isicEconomicActivityList.get(isicEconomicActivityList.size() - 1);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityCode()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_CODE);
        assertThat(testIsicEconomicActivity.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testIsicEconomicActivity.getSectionLabel()).isEqualTo(UPDATED_SECTION_LABEL);
        assertThat(testIsicEconomicActivity.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testIsicEconomicActivity.getDivisionLabel()).isEqualTo(UPDATED_DIVISION_LABEL);
        assertThat(testIsicEconomicActivity.getGroupCode()).isEqualTo(UPDATED_GROUP_CODE);
        assertThat(testIsicEconomicActivity.getGroupLabel()).isEqualTo(UPDATED_GROUP_LABEL);
        assertThat(testIsicEconomicActivity.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityType()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE);
        assertThat(testIsicEconomicActivity.getBusinessEconomicActivityTypeDescription())
            .isEqualTo(UPDATED_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isicEconomicActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsicEconomicActivity() throws Exception {
        int databaseSizeBeforeUpdate = isicEconomicActivityRepository.findAll().size();
        isicEconomicActivity.setId(count.incrementAndGet());

        // Create the IsicEconomicActivity
        IsicEconomicActivityDTO isicEconomicActivityDTO = isicEconomicActivityMapper.toDto(isicEconomicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsicEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isicEconomicActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsicEconomicActivity in the database
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(0)).save(isicEconomicActivity);
    }

    @Test
    @Transactional
    void deleteIsicEconomicActivity() throws Exception {
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);

        int databaseSizeBeforeDelete = isicEconomicActivityRepository.findAll().size();

        // Delete the isicEconomicActivity
        restIsicEconomicActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, isicEconomicActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IsicEconomicActivity> isicEconomicActivityList = isicEconomicActivityRepository.findAll();
        assertThat(isicEconomicActivityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IsicEconomicActivity in Elasticsearch
        verify(mockIsicEconomicActivitySearchRepository, times(1)).deleteById(isicEconomicActivity.getId());
    }

    @Test
    @Transactional
    void searchIsicEconomicActivity() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        isicEconomicActivityRepository.saveAndFlush(isicEconomicActivity);
        when(mockIsicEconomicActivitySearchRepository.search("id:" + isicEconomicActivity.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(isicEconomicActivity), PageRequest.of(0, 1), 1));

        // Search the isicEconomicActivity
        restIsicEconomicActivityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + isicEconomicActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isicEconomicActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicActivityCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_CODE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].sectionLabel").value(hasItem(DEFAULT_SECTION_LABEL)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].divisionLabel").value(hasItem(DEFAULT_DIVISION_LABEL)))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE)))
            .andExpect(jsonPath("$.[*].groupLabel").value(hasItem(DEFAULT_GROUP_LABEL)))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicActivityType").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE)))
            .andExpect(
                jsonPath("$.[*].businessEconomicActivityTypeDescription")
                    .value(hasItem(DEFAULT_BUSINESS_ECONOMIC_ACTIVITY_TYPE_DESCRIPTION.toString()))
            );
    }
}
