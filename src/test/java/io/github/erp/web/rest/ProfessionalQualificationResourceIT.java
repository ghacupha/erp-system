package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.ProfessionalQualification;
import io.github.erp.repository.ProfessionalQualificationRepository;
import io.github.erp.repository.search.ProfessionalQualificationSearchRepository;
import io.github.erp.service.criteria.ProfessionalQualificationCriteria;
import io.github.erp.service.dto.ProfessionalQualificationDTO;
import io.github.erp.service.mapper.ProfessionalQualificationMapper;
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
 * Integration tests for the {@link ProfessionalQualificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalQualificationResourceIT {

    private static final String DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professional-qualifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/professional-qualifications";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessionalQualificationRepository professionalQualificationRepository;

    @Autowired
    private ProfessionalQualificationMapper professionalQualificationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ProfessionalQualificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfessionalQualificationSearchRepository mockProfessionalQualificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalQualificationMockMvc;

    private ProfessionalQualification professionalQualification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalQualification createEntity(EntityManager em) {
        ProfessionalQualification professionalQualification = new ProfessionalQualification()
            .professionalQualificationsCode(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE)
            .professionalQualificationsType(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE)
            .professionalQualificationsDetails(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS);
        return professionalQualification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalQualification createUpdatedEntity(EntityManager em) {
        ProfessionalQualification professionalQualification = new ProfessionalQualification()
            .professionalQualificationsCode(UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE)
            .professionalQualificationsType(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE)
            .professionalQualificationsDetails(UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS);
        return professionalQualification;
    }

    @BeforeEach
    public void initTest() {
        professionalQualification = createEntity(em);
    }

    @Test
    @Transactional
    void createProfessionalQualification() throws Exception {
        int databaseSizeBeforeCreate = professionalQualificationRepository.findAll().size();
        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);
        restProfessionalQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeCreate + 1);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getProfessionalQualificationsCode()).isEqualTo(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsType()).isEqualTo(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsDetails())
            .isEqualTo(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(1)).save(testProfessionalQualification);
    }

    @Test
    @Transactional
    void createProfessionalQualificationWithExistingId() throws Exception {
        // Create the ProfessionalQualification with an existing ID
        professionalQualification.setId(1L);
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        int databaseSizeBeforeCreate = professionalQualificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void checkProfessionalQualificationsCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionalQualificationRepository.findAll().size();
        // set the field null
        professionalQualification.setProfessionalQualificationsCode(null);

        // Create the ProfessionalQualification, which fails.
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        restProfessionalQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalQualificationsTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionalQualificationRepository.findAll().size();
        // set the field null
        professionalQualification.setProfessionalQualificationsType(null);

        // Create the ProfessionalQualification, which fails.
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        restProfessionalQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalQualifications() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].professionalQualificationsCode").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].professionalQualificationsType").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE)))
            .andExpect(
                jsonPath("$.[*].professionalQualificationsDetails").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getProfessionalQualification() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get the professionalQualification
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalQualification.getId().intValue()))
            .andExpect(jsonPath("$.professionalQualificationsCode").value(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE))
            .andExpect(jsonPath("$.professionalQualificationsType").value(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE))
            .andExpect(jsonPath("$.professionalQualificationsDetails").value(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getProfessionalQualificationsByIdFiltering() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        Long id = professionalQualification.getId();

        defaultProfessionalQualificationShouldBeFound("id.equals=" + id);
        defaultProfessionalQualificationShouldNotBeFound("id.notEquals=" + id);

        defaultProfessionalQualificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfessionalQualificationShouldNotBeFound("id.greaterThan=" + id);

        defaultProfessionalQualificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfessionalQualificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode equals to DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldBeFound("professionalQualificationsCode.equals=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE);

        // Get all the professionalQualificationList where professionalQualificationsCode equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsCode.equals=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode not equals to DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsCode.notEquals=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        );

        // Get all the professionalQualificationList where professionalQualificationsCode not equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsCode.notEquals=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeIsInShouldWork() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode in DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE or UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsCode.in=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE + "," + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        );

        // Get all the professionalQualificationList where professionalQualificationsCode equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldNotBeFound("professionalQualificationsCode.in=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode is not null
        defaultProfessionalQualificationShouldBeFound("professionalQualificationsCode.specified=true");

        // Get all the professionalQualificationList where professionalQualificationsCode is null
        defaultProfessionalQualificationShouldNotBeFound("professionalQualificationsCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeContainsSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode contains DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsCode.contains=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        );

        // Get all the professionalQualificationList where professionalQualificationsCode contains UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsCode.contains=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsCodeNotContainsSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsCode does not contain DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsCode.doesNotContain=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE
        );

        // Get all the professionalQualificationList where professionalQualificationsCode does not contain UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsCode.doesNotContain=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType equals to DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldBeFound("professionalQualificationsType.equals=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE);

        // Get all the professionalQualificationList where professionalQualificationsType equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsType.equals=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType not equals to DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsType.notEquals=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        );

        // Get all the professionalQualificationList where professionalQualificationsType not equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsType.notEquals=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeIsInShouldWork() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType in DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE or UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsType.in=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE + "," + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        );

        // Get all the professionalQualificationList where professionalQualificationsType equals to UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldNotBeFound("professionalQualificationsType.in=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE);
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType is not null
        defaultProfessionalQualificationShouldBeFound("professionalQualificationsType.specified=true");

        // Get all the professionalQualificationList where professionalQualificationsType is null
        defaultProfessionalQualificationShouldNotBeFound("professionalQualificationsType.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeContainsSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType contains DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsType.contains=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        );

        // Get all the professionalQualificationList where professionalQualificationsType contains UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsType.contains=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalQualificationsByProfessionalQualificationsTypeNotContainsSomething() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        // Get all the professionalQualificationList where professionalQualificationsType does not contain DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldNotBeFound(
            "professionalQualificationsType.doesNotContain=" + DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE
        );

        // Get all the professionalQualificationList where professionalQualificationsType does not contain UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        defaultProfessionalQualificationShouldBeFound(
            "professionalQualificationsType.doesNotContain=" + UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfessionalQualificationShouldBeFound(String filter) throws Exception {
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].professionalQualificationsCode").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].professionalQualificationsType").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE)))
            .andExpect(
                jsonPath("$.[*].professionalQualificationsDetails").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfessionalQualificationShouldNotBeFound(String filter) throws Exception {
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalQualification() throws Exception {
        // Get the professionalQualification
        restProfessionalQualificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfessionalQualification() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();

        // Update the professionalQualification
        ProfessionalQualification updatedProfessionalQualification = professionalQualificationRepository
            .findById(professionalQualification.getId())
            .get();
        // Disconnect from session so that the updates on updatedProfessionalQualification are not directly saved in db
        em.detach(updatedProfessionalQualification);
        updatedProfessionalQualification
            .professionalQualificationsCode(UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE)
            .professionalQualificationsType(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE)
            .professionalQualificationsDetails(UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS);
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(updatedProfessionalQualification);

        restProfessionalQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getProfessionalQualificationsCode()).isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsType()).isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsDetails())
            .isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository).save(testProfessionalQualification);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalQualificationWithPatch() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();

        // Update the professionalQualification using partial update
        ProfessionalQualification partialUpdatedProfessionalQualification = new ProfessionalQualification();
        partialUpdatedProfessionalQualification.setId(professionalQualification.getId());

        partialUpdatedProfessionalQualification.professionalQualificationsType(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE);

        restProfessionalQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalQualification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionalQualification))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getProfessionalQualificationsCode()).isEqualTo(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsType()).isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsDetails())
            .isEqualTo(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateProfessionalQualificationWithPatch() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();

        // Update the professionalQualification using partial update
        ProfessionalQualification partialUpdatedProfessionalQualification = new ProfessionalQualification();
        partialUpdatedProfessionalQualification.setId(professionalQualification.getId());

        partialUpdatedProfessionalQualification
            .professionalQualificationsCode(UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE)
            .professionalQualificationsType(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE)
            .professionalQualificationsDetails(UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS);

        restProfessionalQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalQualification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionalQualification))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getProfessionalQualificationsCode()).isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_CODE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsType()).isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_TYPE);
        assertThat(testProfessionalQualification.getProfessionalQualificationsDetails())
            .isEqualTo(UPDATED_PROFESSIONAL_QUALIFICATIONS_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(0)).save(professionalQualification);
    }

    @Test
    @Transactional
    void deleteProfessionalQualification() throws Exception {
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);

        int databaseSizeBeforeDelete = professionalQualificationRepository.findAll().size();

        // Delete the professionalQualification
        restProfessionalQualificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalQualification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfessionalQualification in Elasticsearch
        verify(mockProfessionalQualificationSearchRepository, times(1)).deleteById(professionalQualification.getId());
    }

    @Test
    @Transactional
    void searchProfessionalQualification() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        professionalQualificationRepository.saveAndFlush(professionalQualification);
        when(mockProfessionalQualificationSearchRepository.search("id:" + professionalQualification.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(professionalQualification), PageRequest.of(0, 1), 1));

        // Search the professionalQualification
        restProfessionalQualificationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + professionalQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].professionalQualificationsCode").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].professionalQualificationsType").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_TYPE)))
            .andExpect(
                jsonPath("$.[*].professionalQualificationsDetails").value(hasItem(DEFAULT_PROFESSIONAL_QUALIFICATIONS_DETAILS.toString()))
            );
    }
}
