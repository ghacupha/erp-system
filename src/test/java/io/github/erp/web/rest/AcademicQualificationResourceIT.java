package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.AcademicQualification;
import io.github.erp.repository.AcademicQualificationRepository;
import io.github.erp.repository.search.AcademicQualificationSearchRepository;
import io.github.erp.service.criteria.AcademicQualificationCriteria;
import io.github.erp.service.dto.AcademicQualificationDTO;
import io.github.erp.service.mapper.AcademicQualificationMapper;
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
 * Integration tests for the {@link AcademicQualificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AcademicQualificationResourceIT {

    private static final String DEFAULT_ACADEMIC_QUALIFICATIONS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACADEMIC_QUALIFICATIONS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACADEMIC_QUALIFICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACADEMIC_QUALIFICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/academic-qualifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/academic-qualifications";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcademicQualificationRepository academicQualificationRepository;

    @Autowired
    private AcademicQualificationMapper academicQualificationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AcademicQualificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private AcademicQualificationSearchRepository mockAcademicQualificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcademicQualificationMockMvc;

    private AcademicQualification academicQualification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicQualification createEntity(EntityManager em) {
        AcademicQualification academicQualification = new AcademicQualification()
            .academicQualificationsCode(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE)
            .academicQualificationType(DEFAULT_ACADEMIC_QUALIFICATION_TYPE)
            .academicQualificationTypeDetail(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL);
        return academicQualification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicQualification createUpdatedEntity(EntityManager em) {
        AcademicQualification academicQualification = new AcademicQualification()
            .academicQualificationsCode(UPDATED_ACADEMIC_QUALIFICATIONS_CODE)
            .academicQualificationType(UPDATED_ACADEMIC_QUALIFICATION_TYPE)
            .academicQualificationTypeDetail(UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL);
        return academicQualification;
    }

    @BeforeEach
    public void initTest() {
        academicQualification = createEntity(em);
    }

    @Test
    @Transactional
    void createAcademicQualification() throws Exception {
        int databaseSizeBeforeCreate = academicQualificationRepository.findAll().size();
        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);
        restAcademicQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeCreate + 1);
        AcademicQualification testAcademicQualification = academicQualificationList.get(academicQualificationList.size() - 1);
        assertThat(testAcademicQualification.getAcademicQualificationsCode()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);
        assertThat(testAcademicQualification.getAcademicQualificationType()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATION_TYPE);
        assertThat(testAcademicQualification.getAcademicQualificationTypeDetail()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(1)).save(testAcademicQualification);
    }

    @Test
    @Transactional
    void createAcademicQualificationWithExistingId() throws Exception {
        // Create the AcademicQualification with an existing ID
        academicQualification.setId(1L);
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        int databaseSizeBeforeCreate = academicQualificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademicQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void checkAcademicQualificationsCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = academicQualificationRepository.findAll().size();
        // set the field null
        academicQualification.setAcademicQualificationsCode(null);

        // Create the AcademicQualification, which fails.
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        restAcademicQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcademicQualificationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = academicQualificationRepository.findAll().size();
        // set the field null
        academicQualification.setAcademicQualificationType(null);

        // Create the AcademicQualification, which fails.
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        restAcademicQualificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcademicQualifications() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].academicQualificationsCode").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].academicQualificationType").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].academicQualificationTypeDetail").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL.toString()))
            );
    }

    @Test
    @Transactional
    void getAcademicQualification() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get the academicQualification
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL_ID, academicQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(academicQualification.getId().intValue()))
            .andExpect(jsonPath("$.academicQualificationsCode").value(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE))
            .andExpect(jsonPath("$.academicQualificationType").value(DEFAULT_ACADEMIC_QUALIFICATION_TYPE))
            .andExpect(jsonPath("$.academicQualificationTypeDetail").value(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL.toString()));
    }

    @Test
    @Transactional
    void getAcademicQualificationsByIdFiltering() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        Long id = academicQualification.getId();

        defaultAcademicQualificationShouldBeFound("id.equals=" + id);
        defaultAcademicQualificationShouldNotBeFound("id.notEquals=" + id);

        defaultAcademicQualificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAcademicQualificationShouldNotBeFound("id.greaterThan=" + id);

        defaultAcademicQualificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAcademicQualificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode equals to DEFAULT_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldBeFound("academicQualificationsCode.equals=" + DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);

        // Get all the academicQualificationList where academicQualificationsCode equals to UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.equals=" + UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode not equals to DEFAULT_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.notEquals=" + DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);

        // Get all the academicQualificationList where academicQualificationsCode not equals to UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldBeFound("academicQualificationsCode.notEquals=" + UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeIsInShouldWork() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode in DEFAULT_ACADEMIC_QUALIFICATIONS_CODE or UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldBeFound(
            "academicQualificationsCode.in=" + DEFAULT_ACADEMIC_QUALIFICATIONS_CODE + "," + UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        );

        // Get all the academicQualificationList where academicQualificationsCode equals to UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.in=" + UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode is not null
        defaultAcademicQualificationShouldBeFound("academicQualificationsCode.specified=true");

        // Get all the academicQualificationList where academicQualificationsCode is null
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeContainsSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode contains DEFAULT_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldBeFound("academicQualificationsCode.contains=" + DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);

        // Get all the academicQualificationList where academicQualificationsCode contains UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.contains=" + UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationsCodeNotContainsSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationsCode does not contain DEFAULT_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationsCode.doesNotContain=" + DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);

        // Get all the academicQualificationList where academicQualificationsCode does not contain UPDATED_ACADEMIC_QUALIFICATIONS_CODE
        defaultAcademicQualificationShouldBeFound("academicQualificationsCode.doesNotContain=" + UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType equals to DEFAULT_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldBeFound("academicQualificationType.equals=" + DEFAULT_ACADEMIC_QUALIFICATION_TYPE);

        // Get all the academicQualificationList where academicQualificationType equals to UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.equals=" + UPDATED_ACADEMIC_QUALIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType not equals to DEFAULT_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.notEquals=" + DEFAULT_ACADEMIC_QUALIFICATION_TYPE);

        // Get all the academicQualificationList where academicQualificationType not equals to UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldBeFound("academicQualificationType.notEquals=" + UPDATED_ACADEMIC_QUALIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType in DEFAULT_ACADEMIC_QUALIFICATION_TYPE or UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldBeFound(
            "academicQualificationType.in=" + DEFAULT_ACADEMIC_QUALIFICATION_TYPE + "," + UPDATED_ACADEMIC_QUALIFICATION_TYPE
        );

        // Get all the academicQualificationList where academicQualificationType equals to UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.in=" + UPDATED_ACADEMIC_QUALIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType is not null
        defaultAcademicQualificationShouldBeFound("academicQualificationType.specified=true");

        // Get all the academicQualificationList where academicQualificationType is null
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.specified=false");
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeContainsSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType contains DEFAULT_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldBeFound("academicQualificationType.contains=" + DEFAULT_ACADEMIC_QUALIFICATION_TYPE);

        // Get all the academicQualificationList where academicQualificationType contains UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.contains=" + UPDATED_ACADEMIC_QUALIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllAcademicQualificationsByAcademicQualificationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        // Get all the academicQualificationList where academicQualificationType does not contain DEFAULT_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldNotBeFound("academicQualificationType.doesNotContain=" + DEFAULT_ACADEMIC_QUALIFICATION_TYPE);

        // Get all the academicQualificationList where academicQualificationType does not contain UPDATED_ACADEMIC_QUALIFICATION_TYPE
        defaultAcademicQualificationShouldBeFound("academicQualificationType.doesNotContain=" + UPDATED_ACADEMIC_QUALIFICATION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAcademicQualificationShouldBeFound(String filter) throws Exception {
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].academicQualificationsCode").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].academicQualificationType").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].academicQualificationTypeDetail").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL.toString()))
            );

        // Check, that the count call also returns 1
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAcademicQualificationShouldNotBeFound(String filter) throws Exception {
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAcademicQualification() throws Exception {
        // Get the academicQualification
        restAcademicQualificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcademicQualification() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();

        // Update the academicQualification
        AcademicQualification updatedAcademicQualification = academicQualificationRepository.findById(academicQualification.getId()).get();
        // Disconnect from session so that the updates on updatedAcademicQualification are not directly saved in db
        em.detach(updatedAcademicQualification);
        updatedAcademicQualification
            .academicQualificationsCode(UPDATED_ACADEMIC_QUALIFICATIONS_CODE)
            .academicQualificationType(UPDATED_ACADEMIC_QUALIFICATION_TYPE)
            .academicQualificationTypeDetail(UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL);
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(updatedAcademicQualification);

        restAcademicQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academicQualificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);
        AcademicQualification testAcademicQualification = academicQualificationList.get(academicQualificationList.size() - 1);
        assertThat(testAcademicQualification.getAcademicQualificationsCode()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
        assertThat(testAcademicQualification.getAcademicQualificationType()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATION_TYPE);
        assertThat(testAcademicQualification.getAcademicQualificationTypeDetail()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository).save(testAcademicQualification);
    }

    @Test
    @Transactional
    void putNonExistingAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academicQualificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void partialUpdateAcademicQualificationWithPatch() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();

        // Update the academicQualification using partial update
        AcademicQualification partialUpdatedAcademicQualification = new AcademicQualification();
        partialUpdatedAcademicQualification.setId(academicQualification.getId());

        restAcademicQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicQualification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademicQualification))
            )
            .andExpect(status().isOk());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);
        AcademicQualification testAcademicQualification = academicQualificationList.get(academicQualificationList.size() - 1);
        assertThat(testAcademicQualification.getAcademicQualificationsCode()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE);
        assertThat(testAcademicQualification.getAcademicQualificationType()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATION_TYPE);
        assertThat(testAcademicQualification.getAcademicQualificationTypeDetail()).isEqualTo(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void fullUpdateAcademicQualificationWithPatch() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();

        // Update the academicQualification using partial update
        AcademicQualification partialUpdatedAcademicQualification = new AcademicQualification();
        partialUpdatedAcademicQualification.setId(academicQualification.getId());

        partialUpdatedAcademicQualification
            .academicQualificationsCode(UPDATED_ACADEMIC_QUALIFICATIONS_CODE)
            .academicQualificationType(UPDATED_ACADEMIC_QUALIFICATION_TYPE)
            .academicQualificationTypeDetail(UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL);

        restAcademicQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicQualification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademicQualification))
            )
            .andExpect(status().isOk());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);
        AcademicQualification testAcademicQualification = academicQualificationList.get(academicQualificationList.size() - 1);
        assertThat(testAcademicQualification.getAcademicQualificationsCode()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATIONS_CODE);
        assertThat(testAcademicQualification.getAcademicQualificationType()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATION_TYPE);
        assertThat(testAcademicQualification.getAcademicQualificationTypeDetail()).isEqualTo(UPDATED_ACADEMIC_QUALIFICATION_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void patchNonExistingAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, academicQualificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcademicQualification() throws Exception {
        int databaseSizeBeforeUpdate = academicQualificationRepository.findAll().size();
        academicQualification.setId(count.incrementAndGet());

        // Create the AcademicQualification
        AcademicQualificationDTO academicQualificationDTO = academicQualificationMapper.toDto(academicQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicQualificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicQualificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicQualification in the database
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(0)).save(academicQualification);
    }

    @Test
    @Transactional
    void deleteAcademicQualification() throws Exception {
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);

        int databaseSizeBeforeDelete = academicQualificationRepository.findAll().size();

        // Delete the academicQualification
        restAcademicQualificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, academicQualification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcademicQualification> academicQualificationList = academicQualificationRepository.findAll();
        assertThat(academicQualificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AcademicQualification in Elasticsearch
        verify(mockAcademicQualificationSearchRepository, times(1)).deleteById(academicQualification.getId());
    }

    @Test
    @Transactional
    void searchAcademicQualification() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        academicQualificationRepository.saveAndFlush(academicQualification);
        when(mockAcademicQualificationSearchRepository.search("id:" + academicQualification.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(academicQualification), PageRequest.of(0, 1), 1));

        // Search the academicQualification
        restAcademicQualificationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + academicQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].academicQualificationsCode").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATIONS_CODE)))
            .andExpect(jsonPath("$.[*].academicQualificationType").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].academicQualificationTypeDetail").value(hasItem(DEFAULT_ACADEMIC_QUALIFICATION_TYPE_DETAIL.toString()))
            );
    }
}
