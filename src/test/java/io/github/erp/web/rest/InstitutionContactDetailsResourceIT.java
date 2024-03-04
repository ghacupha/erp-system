package io.github.erp.web.rest;

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
import io.github.erp.domain.InstitutionContactDetails;
import io.github.erp.repository.InstitutionContactDetailsRepository;
import io.github.erp.repository.search.InstitutionContactDetailsSearchRepository;
import io.github.erp.service.criteria.InstitutionContactDetailsCriteria;
import io.github.erp.service.dto.InstitutionContactDetailsDTO;
import io.github.erp.service.mapper.InstitutionContactDetailsMapper;
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
 * Integration tests for the {@link InstitutionContactDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InstitutionContactDetailsResourceIT {

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DESIGNATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institution-contact-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/institution-contact-details";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstitutionContactDetailsRepository institutionContactDetailsRepository;

    @Autowired
    private InstitutionContactDetailsMapper institutionContactDetailsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InstitutionContactDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private InstitutionContactDetailsSearchRepository mockInstitutionContactDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstitutionContactDetailsMockMvc;

    private InstitutionContactDetails institutionContactDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionContactDetails createEntity(EntityManager em) {
        InstitutionContactDetails institutionContactDetails = new InstitutionContactDetails()
            .entityId(DEFAULT_ENTITY_ID)
            .entityName(DEFAULT_ENTITY_NAME)
            .contactType(DEFAULT_CONTACT_TYPE)
            .contactLevel(DEFAULT_CONTACT_LEVEL)
            .contactValue(DEFAULT_CONTACT_VALUE)
            .contactName(DEFAULT_CONTACT_NAME)
            .contactDesignation(DEFAULT_CONTACT_DESIGNATION);
        return institutionContactDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionContactDetails createUpdatedEntity(EntityManager em) {
        InstitutionContactDetails institutionContactDetails = new InstitutionContactDetails()
            .entityId(UPDATED_ENTITY_ID)
            .entityName(UPDATED_ENTITY_NAME)
            .contactType(UPDATED_CONTACT_TYPE)
            .contactLevel(UPDATED_CONTACT_LEVEL)
            .contactValue(UPDATED_CONTACT_VALUE)
            .contactName(UPDATED_CONTACT_NAME)
            .contactDesignation(UPDATED_CONTACT_DESIGNATION);
        return institutionContactDetails;
    }

    @BeforeEach
    public void initTest() {
        institutionContactDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeCreate = institutionContactDetailsRepository.findAll().size();
        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);
        restInstitutionContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionContactDetails testInstitutionContactDetails = institutionContactDetailsList.get(
            institutionContactDetailsList.size() - 1
        );
        assertThat(testInstitutionContactDetails.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testInstitutionContactDetails.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testInstitutionContactDetails.getContactType()).isEqualTo(DEFAULT_CONTACT_TYPE);
        assertThat(testInstitutionContactDetails.getContactLevel()).isEqualTo(DEFAULT_CONTACT_LEVEL);
        assertThat(testInstitutionContactDetails.getContactValue()).isEqualTo(DEFAULT_CONTACT_VALUE);
        assertThat(testInstitutionContactDetails.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testInstitutionContactDetails.getContactDesignation()).isEqualTo(DEFAULT_CONTACT_DESIGNATION);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(1)).save(testInstitutionContactDetails);
    }

    @Test
    @Transactional
    void createInstitutionContactDetailsWithExistingId() throws Exception {
        // Create the InstitutionContactDetails with an existing ID
        institutionContactDetails.setId(1L);
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        int databaseSizeBeforeCreate = institutionContactDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionContactDetailsRepository.findAll().size();
        // set the field null
        institutionContactDetails.setEntityId(null);

        // Create the InstitutionContactDetails, which fails.
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        restInstitutionContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionContactDetailsRepository.findAll().size();
        // set the field null
        institutionContactDetails.setEntityName(null);

        // Create the InstitutionContactDetails, which fails.
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        restInstitutionContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionContactDetailsRepository.findAll().size();
        // set the field null
        institutionContactDetails.setContactType(null);

        // Create the InstitutionContactDetails, which fails.
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        restInstitutionContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetails() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionContactDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE)))
            .andExpect(jsonPath("$.[*].contactLevel").value(hasItem(DEFAULT_CONTACT_LEVEL)))
            .andExpect(jsonPath("$.[*].contactValue").value(hasItem(DEFAULT_CONTACT_VALUE)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactDesignation").value(hasItem(DEFAULT_CONTACT_DESIGNATION)));
    }

    @Test
    @Transactional
    void getInstitutionContactDetails() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get the institutionContactDetails
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, institutionContactDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institutionContactDetails.getId().intValue()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.contactType").value(DEFAULT_CONTACT_TYPE))
            .andExpect(jsonPath("$.contactLevel").value(DEFAULT_CONTACT_LEVEL))
            .andExpect(jsonPath("$.contactValue").value(DEFAULT_CONTACT_VALUE))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.contactDesignation").value(DEFAULT_CONTACT_DESIGNATION));
    }

    @Test
    @Transactional
    void getInstitutionContactDetailsByIdFiltering() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        Long id = institutionContactDetails.getId();

        defaultInstitutionContactDetailsShouldBeFound("id.equals=" + id);
        defaultInstitutionContactDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultInstitutionContactDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInstitutionContactDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultInstitutionContactDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInstitutionContactDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId equals to DEFAULT_ENTITY_ID
        defaultInstitutionContactDetailsShouldBeFound("entityId.equals=" + DEFAULT_ENTITY_ID);

        // Get all the institutionContactDetailsList where entityId equals to UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId not equals to DEFAULT_ENTITY_ID
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.notEquals=" + DEFAULT_ENTITY_ID);

        // Get all the institutionContactDetailsList where entityId not equals to UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldBeFound("entityId.notEquals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId in DEFAULT_ENTITY_ID or UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldBeFound("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID);

        // Get all the institutionContactDetailsList where entityId equals to UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId is not null
        defaultInstitutionContactDetailsShouldBeFound("entityId.specified=true");

        // Get all the institutionContactDetailsList where entityId is null
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId contains DEFAULT_ENTITY_ID
        defaultInstitutionContactDetailsShouldBeFound("entityId.contains=" + DEFAULT_ENTITY_ID);

        // Get all the institutionContactDetailsList where entityId contains UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.contains=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityIdNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityId does not contain DEFAULT_ENTITY_ID
        defaultInstitutionContactDetailsShouldNotBeFound("entityId.doesNotContain=" + DEFAULT_ENTITY_ID);

        // Get all the institutionContactDetailsList where entityId does not contain UPDATED_ENTITY_ID
        defaultInstitutionContactDetailsShouldBeFound("entityId.doesNotContain=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName equals to DEFAULT_ENTITY_NAME
        defaultInstitutionContactDetailsShouldBeFound("entityName.equals=" + DEFAULT_ENTITY_NAME);

        // Get all the institutionContactDetailsList where entityName equals to UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName not equals to DEFAULT_ENTITY_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.notEquals=" + DEFAULT_ENTITY_NAME);

        // Get all the institutionContactDetailsList where entityName not equals to UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldBeFound("entityName.notEquals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName in DEFAULT_ENTITY_NAME or UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldBeFound("entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME);

        // Get all the institutionContactDetailsList where entityName equals to UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.in=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName is not null
        defaultInstitutionContactDetailsShouldBeFound("entityName.specified=true");

        // Get all the institutionContactDetailsList where entityName is null
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName contains DEFAULT_ENTITY_NAME
        defaultInstitutionContactDetailsShouldBeFound("entityName.contains=" + DEFAULT_ENTITY_NAME);

        // Get all the institutionContactDetailsList where entityName contains UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where entityName does not contain DEFAULT_ENTITY_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);

        // Get all the institutionContactDetailsList where entityName does not contain UPDATED_ENTITY_NAME
        defaultInstitutionContactDetailsShouldBeFound("entityName.doesNotContain=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType equals to DEFAULT_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldBeFound("contactType.equals=" + DEFAULT_CONTACT_TYPE);

        // Get all the institutionContactDetailsList where contactType equals to UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.equals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType not equals to DEFAULT_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.notEquals=" + DEFAULT_CONTACT_TYPE);

        // Get all the institutionContactDetailsList where contactType not equals to UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldBeFound("contactType.notEquals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType in DEFAULT_CONTACT_TYPE or UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldBeFound("contactType.in=" + DEFAULT_CONTACT_TYPE + "," + UPDATED_CONTACT_TYPE);

        // Get all the institutionContactDetailsList where contactType equals to UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.in=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType is not null
        defaultInstitutionContactDetailsShouldBeFound("contactType.specified=true");

        // Get all the institutionContactDetailsList where contactType is null
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType contains DEFAULT_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldBeFound("contactType.contains=" + DEFAULT_CONTACT_TYPE);

        // Get all the institutionContactDetailsList where contactType contains UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.contains=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactTypeNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactType does not contain DEFAULT_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldNotBeFound("contactType.doesNotContain=" + DEFAULT_CONTACT_TYPE);

        // Get all the institutionContactDetailsList where contactType does not contain UPDATED_CONTACT_TYPE
        defaultInstitutionContactDetailsShouldBeFound("contactType.doesNotContain=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel equals to DEFAULT_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.equals=" + DEFAULT_CONTACT_LEVEL);

        // Get all the institutionContactDetailsList where contactLevel equals to UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.equals=" + UPDATED_CONTACT_LEVEL);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel not equals to DEFAULT_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.notEquals=" + DEFAULT_CONTACT_LEVEL);

        // Get all the institutionContactDetailsList where contactLevel not equals to UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.notEquals=" + UPDATED_CONTACT_LEVEL);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel in DEFAULT_CONTACT_LEVEL or UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.in=" + DEFAULT_CONTACT_LEVEL + "," + UPDATED_CONTACT_LEVEL);

        // Get all the institutionContactDetailsList where contactLevel equals to UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.in=" + UPDATED_CONTACT_LEVEL);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel is not null
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.specified=true");

        // Get all the institutionContactDetailsList where contactLevel is null
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel contains DEFAULT_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.contains=" + DEFAULT_CONTACT_LEVEL);

        // Get all the institutionContactDetailsList where contactLevel contains UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.contains=" + UPDATED_CONTACT_LEVEL);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactLevelNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactLevel does not contain DEFAULT_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldNotBeFound("contactLevel.doesNotContain=" + DEFAULT_CONTACT_LEVEL);

        // Get all the institutionContactDetailsList where contactLevel does not contain UPDATED_CONTACT_LEVEL
        defaultInstitutionContactDetailsShouldBeFound("contactLevel.doesNotContain=" + UPDATED_CONTACT_LEVEL);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue equals to DEFAULT_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldBeFound("contactValue.equals=" + DEFAULT_CONTACT_VALUE);

        // Get all the institutionContactDetailsList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.equals=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue not equals to DEFAULT_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.notEquals=" + DEFAULT_CONTACT_VALUE);

        // Get all the institutionContactDetailsList where contactValue not equals to UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldBeFound("contactValue.notEquals=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue in DEFAULT_CONTACT_VALUE or UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldBeFound("contactValue.in=" + DEFAULT_CONTACT_VALUE + "," + UPDATED_CONTACT_VALUE);

        // Get all the institutionContactDetailsList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.in=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue is not null
        defaultInstitutionContactDetailsShouldBeFound("contactValue.specified=true");

        // Get all the institutionContactDetailsList where contactValue is null
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue contains DEFAULT_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldBeFound("contactValue.contains=" + DEFAULT_CONTACT_VALUE);

        // Get all the institutionContactDetailsList where contactValue contains UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.contains=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactValueNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactValue does not contain DEFAULT_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldNotBeFound("contactValue.doesNotContain=" + DEFAULT_CONTACT_VALUE);

        // Get all the institutionContactDetailsList where contactValue does not contain UPDATED_CONTACT_VALUE
        defaultInstitutionContactDetailsShouldBeFound("contactValue.doesNotContain=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName equals to DEFAULT_CONTACT_NAME
        defaultInstitutionContactDetailsShouldBeFound("contactName.equals=" + DEFAULT_CONTACT_NAME);

        // Get all the institutionContactDetailsList where contactName equals to UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName not equals to DEFAULT_CONTACT_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.notEquals=" + DEFAULT_CONTACT_NAME);

        // Get all the institutionContactDetailsList where contactName not equals to UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldBeFound("contactName.notEquals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName in DEFAULT_CONTACT_NAME or UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldBeFound("contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME);

        // Get all the institutionContactDetailsList where contactName equals to UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.in=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName is not null
        defaultInstitutionContactDetailsShouldBeFound("contactName.specified=true");

        // Get all the institutionContactDetailsList where contactName is null
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName contains DEFAULT_CONTACT_NAME
        defaultInstitutionContactDetailsShouldBeFound("contactName.contains=" + DEFAULT_CONTACT_NAME);

        // Get all the institutionContactDetailsList where contactName contains UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactName does not contain DEFAULT_CONTACT_NAME
        defaultInstitutionContactDetailsShouldNotBeFound("contactName.doesNotContain=" + DEFAULT_CONTACT_NAME);

        // Get all the institutionContactDetailsList where contactName does not contain UPDATED_CONTACT_NAME
        defaultInstitutionContactDetailsShouldBeFound("contactName.doesNotContain=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation equals to DEFAULT_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldBeFound("contactDesignation.equals=" + DEFAULT_CONTACT_DESIGNATION);

        // Get all the institutionContactDetailsList where contactDesignation equals to UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.equals=" + UPDATED_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation not equals to DEFAULT_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.notEquals=" + DEFAULT_CONTACT_DESIGNATION);

        // Get all the institutionContactDetailsList where contactDesignation not equals to UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldBeFound("contactDesignation.notEquals=" + UPDATED_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation in DEFAULT_CONTACT_DESIGNATION or UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldBeFound(
            "contactDesignation.in=" + DEFAULT_CONTACT_DESIGNATION + "," + UPDATED_CONTACT_DESIGNATION
        );

        // Get all the institutionContactDetailsList where contactDesignation equals to UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.in=" + UPDATED_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation is not null
        defaultInstitutionContactDetailsShouldBeFound("contactDesignation.specified=true");

        // Get all the institutionContactDetailsList where contactDesignation is null
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation contains DEFAULT_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldBeFound("contactDesignation.contains=" + DEFAULT_CONTACT_DESIGNATION);

        // Get all the institutionContactDetailsList where contactDesignation contains UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.contains=" + UPDATED_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllInstitutionContactDetailsByContactDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        // Get all the institutionContactDetailsList where contactDesignation does not contain DEFAULT_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldNotBeFound("contactDesignation.doesNotContain=" + DEFAULT_CONTACT_DESIGNATION);

        // Get all the institutionContactDetailsList where contactDesignation does not contain UPDATED_CONTACT_DESIGNATION
        defaultInstitutionContactDetailsShouldBeFound("contactDesignation.doesNotContain=" + UPDATED_CONTACT_DESIGNATION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInstitutionContactDetailsShouldBeFound(String filter) throws Exception {
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionContactDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE)))
            .andExpect(jsonPath("$.[*].contactLevel").value(hasItem(DEFAULT_CONTACT_LEVEL)))
            .andExpect(jsonPath("$.[*].contactValue").value(hasItem(DEFAULT_CONTACT_VALUE)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactDesignation").value(hasItem(DEFAULT_CONTACT_DESIGNATION)));

        // Check, that the count call also returns 1
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInstitutionContactDetailsShouldNotBeFound(String filter) throws Exception {
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInstitutionContactDetails() throws Exception {
        // Get the institutionContactDetails
        restInstitutionContactDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstitutionContactDetails() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();

        // Update the institutionContactDetails
        InstitutionContactDetails updatedInstitutionContactDetails = institutionContactDetailsRepository
            .findById(institutionContactDetails.getId())
            .get();
        // Disconnect from session so that the updates on updatedInstitutionContactDetails are not directly saved in db
        em.detach(updatedInstitutionContactDetails);
        updatedInstitutionContactDetails
            .entityId(UPDATED_ENTITY_ID)
            .entityName(UPDATED_ENTITY_NAME)
            .contactType(UPDATED_CONTACT_TYPE)
            .contactLevel(UPDATED_CONTACT_LEVEL)
            .contactValue(UPDATED_CONTACT_VALUE)
            .contactName(UPDATED_CONTACT_NAME)
            .contactDesignation(UPDATED_CONTACT_DESIGNATION);
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(updatedInstitutionContactDetails);

        restInstitutionContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionContactDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        InstitutionContactDetails testInstitutionContactDetails = institutionContactDetailsList.get(
            institutionContactDetailsList.size() - 1
        );
        assertThat(testInstitutionContactDetails.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testInstitutionContactDetails.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testInstitutionContactDetails.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testInstitutionContactDetails.getContactLevel()).isEqualTo(UPDATED_CONTACT_LEVEL);
        assertThat(testInstitutionContactDetails.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
        assertThat(testInstitutionContactDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInstitutionContactDetails.getContactDesignation()).isEqualTo(UPDATED_CONTACT_DESIGNATION);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository).save(testInstitutionContactDetails);
    }

    @Test
    @Transactional
    void putNonExistingInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionContactDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void partialUpdateInstitutionContactDetailsWithPatch() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();

        // Update the institutionContactDetails using partial update
        InstitutionContactDetails partialUpdatedInstitutionContactDetails = new InstitutionContactDetails();
        partialUpdatedInstitutionContactDetails.setId(institutionContactDetails.getId());

        partialUpdatedInstitutionContactDetails
            .entityName(UPDATED_ENTITY_NAME)
            .contactType(UPDATED_CONTACT_TYPE)
            .contactLevel(UPDATED_CONTACT_LEVEL)
            .contactValue(UPDATED_CONTACT_VALUE)
            .contactName(UPDATED_CONTACT_NAME);

        restInstitutionContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionContactDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionContactDetails))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        InstitutionContactDetails testInstitutionContactDetails = institutionContactDetailsList.get(
            institutionContactDetailsList.size() - 1
        );
        assertThat(testInstitutionContactDetails.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testInstitutionContactDetails.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testInstitutionContactDetails.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testInstitutionContactDetails.getContactLevel()).isEqualTo(UPDATED_CONTACT_LEVEL);
        assertThat(testInstitutionContactDetails.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
        assertThat(testInstitutionContactDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInstitutionContactDetails.getContactDesignation()).isEqualTo(DEFAULT_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void fullUpdateInstitutionContactDetailsWithPatch() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();

        // Update the institutionContactDetails using partial update
        InstitutionContactDetails partialUpdatedInstitutionContactDetails = new InstitutionContactDetails();
        partialUpdatedInstitutionContactDetails.setId(institutionContactDetails.getId());

        partialUpdatedInstitutionContactDetails
            .entityId(UPDATED_ENTITY_ID)
            .entityName(UPDATED_ENTITY_NAME)
            .contactType(UPDATED_CONTACT_TYPE)
            .contactLevel(UPDATED_CONTACT_LEVEL)
            .contactValue(UPDATED_CONTACT_VALUE)
            .contactName(UPDATED_CONTACT_NAME)
            .contactDesignation(UPDATED_CONTACT_DESIGNATION);

        restInstitutionContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionContactDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionContactDetails))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        InstitutionContactDetails testInstitutionContactDetails = institutionContactDetailsList.get(
            institutionContactDetailsList.size() - 1
        );
        assertThat(testInstitutionContactDetails.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testInstitutionContactDetails.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testInstitutionContactDetails.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testInstitutionContactDetails.getContactLevel()).isEqualTo(UPDATED_CONTACT_LEVEL);
        assertThat(testInstitutionContactDetails.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
        assertThat(testInstitutionContactDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testInstitutionContactDetails.getContactDesignation()).isEqualTo(UPDATED_CONTACT_DESIGNATION);
    }

    @Test
    @Transactional
    void patchNonExistingInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, institutionContactDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstitutionContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = institutionContactDetailsRepository.findAll().size();
        institutionContactDetails.setId(count.incrementAndGet());

        // Create the InstitutionContactDetails
        InstitutionContactDetailsDTO institutionContactDetailsDTO = institutionContactDetailsMapper.toDto(institutionContactDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionContactDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionContactDetails in the database
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(0)).save(institutionContactDetails);
    }

    @Test
    @Transactional
    void deleteInstitutionContactDetails() throws Exception {
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);

        int databaseSizeBeforeDelete = institutionContactDetailsRepository.findAll().size();

        // Delete the institutionContactDetails
        restInstitutionContactDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, institutionContactDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InstitutionContactDetails> institutionContactDetailsList = institutionContactDetailsRepository.findAll();
        assertThat(institutionContactDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InstitutionContactDetails in Elasticsearch
        verify(mockInstitutionContactDetailsSearchRepository, times(1)).deleteById(institutionContactDetails.getId());
    }

    @Test
    @Transactional
    void searchInstitutionContactDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        institutionContactDetailsRepository.saveAndFlush(institutionContactDetails);
        when(mockInstitutionContactDetailsSearchRepository.search("id:" + institutionContactDetails.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(institutionContactDetails), PageRequest.of(0, 1), 1));

        // Search the institutionContactDetails
        restInstitutionContactDetailsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + institutionContactDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionContactDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE)))
            .andExpect(jsonPath("$.[*].contactLevel").value(hasItem(DEFAULT_CONTACT_LEVEL)))
            .andExpect(jsonPath("$.[*].contactValue").value(hasItem(DEFAULT_CONTACT_VALUE)))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactDesignation").value(hasItem(DEFAULT_CONTACT_DESIGNATION)));
    }
}
