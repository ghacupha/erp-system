package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.SecurityClassificationType;
import io.github.erp.repository.SecurityClassificationTypeRepository;
import io.github.erp.repository.search.SecurityClassificationTypeSearchRepository;
import io.github.erp.service.dto.SecurityClassificationTypeDTO;
import io.github.erp.service.mapper.SecurityClassificationTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.SecurityClassificationTypeResource;
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
 * Integration tests for the {@link SecurityClassificationTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class SecurityClassificationTypeResourceIT {

    private static final String DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_CLASSIFICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_CLASSIFICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_CLASSIFICATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_CLASSIFICATION_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/security-classification-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/security-classification-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityClassificationTypeRepository securityClassificationTypeRepository;

    @Autowired
    private SecurityClassificationTypeMapper securityClassificationTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SecurityClassificationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecurityClassificationTypeSearchRepository mockSecurityClassificationTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityClassificationTypeMockMvc;

    private SecurityClassificationType securityClassificationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClassificationType createEntity(EntityManager em) {
        SecurityClassificationType securityClassificationType = new SecurityClassificationType()
            .securityClassificationTypeCode(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE)
            .securityClassificationType(DEFAULT_SECURITY_CLASSIFICATION_TYPE)
            .securityClassificationDetails(DEFAULT_SECURITY_CLASSIFICATION_DETAILS);
        return securityClassificationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClassificationType createUpdatedEntity(EntityManager em) {
        SecurityClassificationType securityClassificationType = new SecurityClassificationType()
            .securityClassificationTypeCode(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE)
            .securityClassificationType(UPDATED_SECURITY_CLASSIFICATION_TYPE)
            .securityClassificationDetails(UPDATED_SECURITY_CLASSIFICATION_DETAILS);
        return securityClassificationType;
    }

    @BeforeEach
    public void initTest() {
        securityClassificationType = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityClassificationType() throws Exception {
        int databaseSizeBeforeCreate = securityClassificationTypeRepository.findAll().size();
        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);
        restSecurityClassificationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityClassificationType testSecurityClassificationType = securityClassificationTypeList.get(
            securityClassificationTypeList.size() - 1
        );
        assertThat(testSecurityClassificationType.getSecurityClassificationTypeCode()).isEqualTo(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE);
        assertThat(testSecurityClassificationType.getSecurityClassificationType()).isEqualTo(DEFAULT_SECURITY_CLASSIFICATION_TYPE);
        assertThat(testSecurityClassificationType.getSecurityClassificationDetails()).isEqualTo(DEFAULT_SECURITY_CLASSIFICATION_DETAILS);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(1)).save(testSecurityClassificationType);
    }

    @Test
    @Transactional
    void createSecurityClassificationTypeWithExistingId() throws Exception {
        // Create the SecurityClassificationType with an existing ID
        securityClassificationType.setId(1L);
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        int databaseSizeBeforeCreate = securityClassificationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityClassificationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void checkSecurityClassificationTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityClassificationTypeRepository.findAll().size();
        // set the field null
        securityClassificationType.setSecurityClassificationTypeCode(null);

        // Create the SecurityClassificationType, which fails.
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        restSecurityClassificationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecurityClassificationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityClassificationTypeRepository.findAll().size();
        // set the field null
        securityClassificationType.setSecurityClassificationType(null);

        // Create the SecurityClassificationType, which fails.
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        restSecurityClassificationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypes() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClassificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityClassificationTypeCode").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityClassificationType").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].securityClassificationDetails").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getSecurityClassificationType() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get the securityClassificationType
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, securityClassificationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityClassificationType.getId().intValue()))
            .andExpect(jsonPath("$.securityClassificationTypeCode").value(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE))
            .andExpect(jsonPath("$.securityClassificationType").value(DEFAULT_SECURITY_CLASSIFICATION_TYPE))
            .andExpect(jsonPath("$.securityClassificationDetails").value(DEFAULT_SECURITY_CLASSIFICATION_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getSecurityClassificationTypesByIdFiltering() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        Long id = securityClassificationType.getId();

        defaultSecurityClassificationTypeShouldBeFound("id.equals=" + id);
        defaultSecurityClassificationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSecurityClassificationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSecurityClassificationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSecurityClassificationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSecurityClassificationTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode equals to DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationTypeCode.equals=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        );

        // Get all the securityClassificationTypeList where securityClassificationTypeCode equals to UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldNotBeFound(
            "securityClassificationTypeCode.equals=" + UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode not equals to DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldNotBeFound(
            "securityClassificationTypeCode.notEquals=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        );

        // Get all the securityClassificationTypeList where securityClassificationTypeCode not equals to UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationTypeCode.notEquals=" + UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode in DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE or UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationTypeCode.in=" +
            DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE +
            "," +
            UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        );

        // Get all the securityClassificationTypeList where securityClassificationTypeCode equals to UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationTypeCode.in=" + UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode is not null
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationTypeCode.specified=true");

        // Get all the securityClassificationTypeList where securityClassificationTypeCode is null
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode contains DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationTypeCode.contains=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        );

        // Get all the securityClassificationTypeList where securityClassificationTypeCode contains UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldNotBeFound(
            "securityClassificationTypeCode.contains=" + UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationTypeCode does not contain DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldNotBeFound(
            "securityClassificationTypeCode.doesNotContain=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE
        );

        // Get all the securityClassificationTypeList where securityClassificationTypeCode does not contain UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationTypeCode.doesNotContain=" + UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType equals to DEFAULT_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationType.equals=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE);

        // Get all the securityClassificationTypeList where securityClassificationType equals to UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationType.equals=" + UPDATED_SECURITY_CLASSIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType not equals to DEFAULT_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationType.notEquals=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE);

        // Get all the securityClassificationTypeList where securityClassificationType not equals to UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationType.notEquals=" + UPDATED_SECURITY_CLASSIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType in DEFAULT_SECURITY_CLASSIFICATION_TYPE or UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldBeFound(
            "securityClassificationType.in=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE + "," + UPDATED_SECURITY_CLASSIFICATION_TYPE
        );

        // Get all the securityClassificationTypeList where securityClassificationType equals to UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationType.in=" + UPDATED_SECURITY_CLASSIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType is not null
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationType.specified=true");

        // Get all the securityClassificationTypeList where securityClassificationType is null
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationType.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeContainsSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType contains DEFAULT_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationType.contains=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE);

        // Get all the securityClassificationTypeList where securityClassificationType contains UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldNotBeFound("securityClassificationType.contains=" + UPDATED_SECURITY_CLASSIFICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityClassificationTypesBySecurityClassificationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        // Get all the securityClassificationTypeList where securityClassificationType does not contain DEFAULT_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldNotBeFound(
            "securityClassificationType.doesNotContain=" + DEFAULT_SECURITY_CLASSIFICATION_TYPE
        );

        // Get all the securityClassificationTypeList where securityClassificationType does not contain UPDATED_SECURITY_CLASSIFICATION_TYPE
        defaultSecurityClassificationTypeShouldBeFound("securityClassificationType.doesNotContain=" + UPDATED_SECURITY_CLASSIFICATION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityClassificationTypeShouldBeFound(String filter) throws Exception {
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClassificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityClassificationTypeCode").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityClassificationType").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].securityClassificationDetails").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_DETAILS.toString())));

        // Check, that the count call also returns 1
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityClassificationTypeShouldNotBeFound(String filter) throws Exception {
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityClassificationType() throws Exception {
        // Get the securityClassificationType
        restSecurityClassificationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityClassificationType() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();

        // Update the securityClassificationType
        SecurityClassificationType updatedSecurityClassificationType = securityClassificationTypeRepository
            .findById(securityClassificationType.getId())
            .get();
        // Disconnect from session so that the updates on updatedSecurityClassificationType are not directly saved in db
        em.detach(updatedSecurityClassificationType);
        updatedSecurityClassificationType
            .securityClassificationTypeCode(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE)
            .securityClassificationType(UPDATED_SECURITY_CLASSIFICATION_TYPE)
            .securityClassificationDetails(UPDATED_SECURITY_CLASSIFICATION_DETAILS);
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(
            updatedSecurityClassificationType
        );

        restSecurityClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClassificationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityClassificationType testSecurityClassificationType = securityClassificationTypeList.get(
            securityClassificationTypeList.size() - 1
        );
        assertThat(testSecurityClassificationType.getSecurityClassificationTypeCode()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE);
        assertThat(testSecurityClassificationType.getSecurityClassificationType()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_TYPE);
        assertThat(testSecurityClassificationType.getSecurityClassificationDetails()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_DETAILS);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository).save(testSecurityClassificationType);
    }

    @Test
    @Transactional
    void putNonExistingSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClassificationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void partialUpdateSecurityClassificationTypeWithPatch() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();

        // Update the securityClassificationType using partial update
        SecurityClassificationType partialUpdatedSecurityClassificationType = new SecurityClassificationType();
        partialUpdatedSecurityClassificationType.setId(securityClassificationType.getId());

        partialUpdatedSecurityClassificationType
            .securityClassificationTypeCode(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE)
            .securityClassificationDetails(UPDATED_SECURITY_CLASSIFICATION_DETAILS);

        restSecurityClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClassificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClassificationType))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityClassificationType testSecurityClassificationType = securityClassificationTypeList.get(
            securityClassificationTypeList.size() - 1
        );
        assertThat(testSecurityClassificationType.getSecurityClassificationTypeCode()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE);
        assertThat(testSecurityClassificationType.getSecurityClassificationType()).isEqualTo(DEFAULT_SECURITY_CLASSIFICATION_TYPE);
        assertThat(testSecurityClassificationType.getSecurityClassificationDetails()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateSecurityClassificationTypeWithPatch() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();

        // Update the securityClassificationType using partial update
        SecurityClassificationType partialUpdatedSecurityClassificationType = new SecurityClassificationType();
        partialUpdatedSecurityClassificationType.setId(securityClassificationType.getId());

        partialUpdatedSecurityClassificationType
            .securityClassificationTypeCode(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE)
            .securityClassificationType(UPDATED_SECURITY_CLASSIFICATION_TYPE)
            .securityClassificationDetails(UPDATED_SECURITY_CLASSIFICATION_DETAILS);

        restSecurityClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClassificationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClassificationType))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityClassificationType testSecurityClassificationType = securityClassificationTypeList.get(
            securityClassificationTypeList.size() - 1
        );
        assertThat(testSecurityClassificationType.getSecurityClassificationTypeCode()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_TYPE_CODE);
        assertThat(testSecurityClassificationType.getSecurityClassificationType()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_TYPE);
        assertThat(testSecurityClassificationType.getSecurityClassificationDetails()).isEqualTo(UPDATED_SECURITY_CLASSIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityClassificationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityClassificationType() throws Exception {
        int databaseSizeBeforeUpdate = securityClassificationTypeRepository.findAll().size();
        securityClassificationType.setId(count.incrementAndGet());

        // Create the SecurityClassificationType
        SecurityClassificationTypeDTO securityClassificationTypeDTO = securityClassificationTypeMapper.toDto(securityClassificationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClassificationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClassificationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClassificationType in the database
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(0)).save(securityClassificationType);
    }

    @Test
    @Transactional
    void deleteSecurityClassificationType() throws Exception {
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);

        int databaseSizeBeforeDelete = securityClassificationTypeRepository.findAll().size();

        // Delete the securityClassificationType
        restSecurityClassificationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityClassificationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityClassificationType> securityClassificationTypeList = securityClassificationTypeRepository.findAll();
        assertThat(securityClassificationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SecurityClassificationType in Elasticsearch
        verify(mockSecurityClassificationTypeSearchRepository, times(1)).deleteById(securityClassificationType.getId());
    }

    @Test
    @Transactional
    void searchSecurityClassificationType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        securityClassificationTypeRepository.saveAndFlush(securityClassificationType);
        when(mockSecurityClassificationTypeSearchRepository.search("id:" + securityClassificationType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(securityClassificationType), PageRequest.of(0, 1), 1));

        // Search the securityClassificationType
        restSecurityClassificationTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityClassificationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClassificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityClassificationTypeCode").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityClassificationType").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].securityClassificationDetails").value(hasItem(DEFAULT_SECURITY_CLASSIFICATION_DETAILS.toString())));
    }
}
