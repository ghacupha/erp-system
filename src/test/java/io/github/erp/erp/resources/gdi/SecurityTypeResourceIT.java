package io.github.erp.erp.resources.gdi;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.IntegrationTest;
import io.github.erp.domain.SecurityType;
import io.github.erp.repository.SecurityTypeRepository;
import io.github.erp.repository.search.SecurityTypeSearchRepository;
import io.github.erp.service.dto.SecurityTypeDTO;
import io.github.erp.service.mapper.SecurityTypeMapper;
import io.github.erp.web.rest.SecurityTypeResource;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SecurityTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class SecurityTypeResourceIT {

    private static final String DEFAULT_SECURITY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/security-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/security-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityTypeRepository securityTypeRepository;

    @Autowired
    private SecurityTypeMapper securityTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SecurityTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecurityTypeSearchRepository mockSecurityTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityTypeMockMvc;

    private SecurityType securityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityType createEntity(EntityManager em) {
        SecurityType securityType = new SecurityType()
            .securityTypeCode(DEFAULT_SECURITY_TYPE_CODE)
            .securityType(DEFAULT_SECURITY_TYPE)
            .securityTypeDetails(DEFAULT_SECURITY_TYPE_DETAILS);
        return securityType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityType createUpdatedEntity(EntityManager em) {
        SecurityType securityType = new SecurityType()
            .securityTypeCode(UPDATED_SECURITY_TYPE_CODE)
            .securityType(UPDATED_SECURITY_TYPE)
            .securityTypeDetails(UPDATED_SECURITY_TYPE_DETAILS);
        return securityType;
    }

    @BeforeEach
    public void initTest() {
        securityType = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityType() throws Exception {
        int databaseSizeBeforeCreate = securityTypeRepository.findAll().size();
        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);
        restSecurityTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityType testSecurityType = securityTypeList.get(securityTypeList.size() - 1);
        assertThat(testSecurityType.getSecurityTypeCode()).isEqualTo(DEFAULT_SECURITY_TYPE_CODE);
        assertThat(testSecurityType.getSecurityType()).isEqualTo(DEFAULT_SECURITY_TYPE);
        assertThat(testSecurityType.getSecurityTypeDetails()).isEqualTo(DEFAULT_SECURITY_TYPE_DETAILS);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(1)).save(testSecurityType);
    }

    @Test
    @Transactional
    void createSecurityTypeWithExistingId() throws Exception {
        // Create the SecurityType with an existing ID
        securityType.setId(1L);
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        int databaseSizeBeforeCreate = securityTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void checkSecurityTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityTypeRepository.findAll().size();
        // set the field null
        securityType.setSecurityTypeCode(null);

        // Create the SecurityType, which fails.
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        restSecurityTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecurityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityTypeRepository.findAll().size();
        // set the field null
        securityType.setSecurityType(null);

        // Create the SecurityType, which fails.
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        restSecurityTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityTypes() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTypeCode").value(hasItem(DEFAULT_SECURITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityType").value(hasItem(DEFAULT_SECURITY_TYPE)))
            .andExpect(jsonPath("$.[*].securityTypeDetails").value(hasItem(DEFAULT_SECURITY_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getSecurityType() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get the securityType
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, securityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityType.getId().intValue()))
            .andExpect(jsonPath("$.securityTypeCode").value(DEFAULT_SECURITY_TYPE_CODE))
            .andExpect(jsonPath("$.securityType").value(DEFAULT_SECURITY_TYPE))
            .andExpect(jsonPath("$.securityTypeDetails").value(DEFAULT_SECURITY_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getSecurityTypesByIdFiltering() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        Long id = securityType.getId();

        defaultSecurityTypeShouldBeFound("id.equals=" + id);
        defaultSecurityTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSecurityTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSecurityTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSecurityTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSecurityTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode equals to DEFAULT_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldBeFound("securityTypeCode.equals=" + DEFAULT_SECURITY_TYPE_CODE);

        // Get all the securityTypeList where securityTypeCode equals to UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.equals=" + UPDATED_SECURITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode not equals to DEFAULT_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.notEquals=" + DEFAULT_SECURITY_TYPE_CODE);

        // Get all the securityTypeList where securityTypeCode not equals to UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldBeFound("securityTypeCode.notEquals=" + UPDATED_SECURITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode in DEFAULT_SECURITY_TYPE_CODE or UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldBeFound("securityTypeCode.in=" + DEFAULT_SECURITY_TYPE_CODE + "," + UPDATED_SECURITY_TYPE_CODE);

        // Get all the securityTypeList where securityTypeCode equals to UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.in=" + UPDATED_SECURITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode is not null
        defaultSecurityTypeShouldBeFound("securityTypeCode.specified=true");

        // Get all the securityTypeList where securityTypeCode is null
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode contains DEFAULT_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldBeFound("securityTypeCode.contains=" + DEFAULT_SECURITY_TYPE_CODE);

        // Get all the securityTypeList where securityTypeCode contains UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.contains=" + UPDATED_SECURITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityTypeCode does not contain DEFAULT_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldNotBeFound("securityTypeCode.doesNotContain=" + DEFAULT_SECURITY_TYPE_CODE);

        // Get all the securityTypeList where securityTypeCode does not contain UPDATED_SECURITY_TYPE_CODE
        defaultSecurityTypeShouldBeFound("securityTypeCode.doesNotContain=" + UPDATED_SECURITY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType equals to DEFAULT_SECURITY_TYPE
        defaultSecurityTypeShouldBeFound("securityType.equals=" + DEFAULT_SECURITY_TYPE);

        // Get all the securityTypeList where securityType equals to UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldNotBeFound("securityType.equals=" + UPDATED_SECURITY_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType not equals to DEFAULT_SECURITY_TYPE
        defaultSecurityTypeShouldNotBeFound("securityType.notEquals=" + DEFAULT_SECURITY_TYPE);

        // Get all the securityTypeList where securityType not equals to UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldBeFound("securityType.notEquals=" + UPDATED_SECURITY_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType in DEFAULT_SECURITY_TYPE or UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldBeFound("securityType.in=" + DEFAULT_SECURITY_TYPE + "," + UPDATED_SECURITY_TYPE);

        // Get all the securityTypeList where securityType equals to UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldNotBeFound("securityType.in=" + UPDATED_SECURITY_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType is not null
        defaultSecurityTypeShouldBeFound("securityType.specified=true");

        // Get all the securityTypeList where securityType is null
        defaultSecurityTypeShouldNotBeFound("securityType.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeContainsSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType contains DEFAULT_SECURITY_TYPE
        defaultSecurityTypeShouldBeFound("securityType.contains=" + DEFAULT_SECURITY_TYPE);

        // Get all the securityTypeList where securityType contains UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldNotBeFound("securityType.contains=" + UPDATED_SECURITY_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTypesBySecurityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        // Get all the securityTypeList where securityType does not contain DEFAULT_SECURITY_TYPE
        defaultSecurityTypeShouldNotBeFound("securityType.doesNotContain=" + DEFAULT_SECURITY_TYPE);

        // Get all the securityTypeList where securityType does not contain UPDATED_SECURITY_TYPE
        defaultSecurityTypeShouldBeFound("securityType.doesNotContain=" + UPDATED_SECURITY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityTypeShouldBeFound(String filter) throws Exception {
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTypeCode").value(hasItem(DEFAULT_SECURITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityType").value(hasItem(DEFAULT_SECURITY_TYPE)))
            .andExpect(jsonPath("$.[*].securityTypeDetails").value(hasItem(DEFAULT_SECURITY_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityTypeShouldNotBeFound(String filter) throws Exception {
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityType() throws Exception {
        // Get the securityType
        restSecurityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityType() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();

        // Update the securityType
        SecurityType updatedSecurityType = securityTypeRepository.findById(securityType.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityType are not directly saved in db
        em.detach(updatedSecurityType);
        updatedSecurityType
            .securityTypeCode(UPDATED_SECURITY_TYPE_CODE)
            .securityType(UPDATED_SECURITY_TYPE)
            .securityTypeDetails(UPDATED_SECURITY_TYPE_DETAILS);
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(updatedSecurityType);

        restSecurityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityType testSecurityType = securityTypeList.get(securityTypeList.size() - 1);
        assertThat(testSecurityType.getSecurityTypeCode()).isEqualTo(UPDATED_SECURITY_TYPE_CODE);
        assertThat(testSecurityType.getSecurityType()).isEqualTo(UPDATED_SECURITY_TYPE);
        assertThat(testSecurityType.getSecurityTypeDetails()).isEqualTo(UPDATED_SECURITY_TYPE_DETAILS);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository).save(testSecurityType);
    }

    @Test
    @Transactional
    void putNonExistingSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void partialUpdateSecurityTypeWithPatch() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();

        // Update the securityType using partial update
        SecurityType partialUpdatedSecurityType = new SecurityType();
        partialUpdatedSecurityType.setId(securityType.getId());

        partialUpdatedSecurityType.securityTypeCode(UPDATED_SECURITY_TYPE_CODE).securityTypeDetails(UPDATED_SECURITY_TYPE_DETAILS);

        restSecurityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityType))
            )
            .andExpect(status().isOk());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityType testSecurityType = securityTypeList.get(securityTypeList.size() - 1);
        assertThat(testSecurityType.getSecurityTypeCode()).isEqualTo(UPDATED_SECURITY_TYPE_CODE);
        assertThat(testSecurityType.getSecurityType()).isEqualTo(DEFAULT_SECURITY_TYPE);
        assertThat(testSecurityType.getSecurityTypeDetails()).isEqualTo(UPDATED_SECURITY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateSecurityTypeWithPatch() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();

        // Update the securityType using partial update
        SecurityType partialUpdatedSecurityType = new SecurityType();
        partialUpdatedSecurityType.setId(securityType.getId());

        partialUpdatedSecurityType
            .securityTypeCode(UPDATED_SECURITY_TYPE_CODE)
            .securityType(UPDATED_SECURITY_TYPE)
            .securityTypeDetails(UPDATED_SECURITY_TYPE_DETAILS);

        restSecurityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityType))
            )
            .andExpect(status().isOk());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);
        SecurityType testSecurityType = securityTypeList.get(securityTypeList.size() - 1);
        assertThat(testSecurityType.getSecurityTypeCode()).isEqualTo(UPDATED_SECURITY_TYPE_CODE);
        assertThat(testSecurityType.getSecurityType()).isEqualTo(UPDATED_SECURITY_TYPE);
        assertThat(testSecurityType.getSecurityTypeDetails()).isEqualTo(UPDATED_SECURITY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityType() throws Exception {
        int databaseSizeBeforeUpdate = securityTypeRepository.findAll().size();
        securityType.setId(count.incrementAndGet());

        // Create the SecurityType
        SecurityTypeDTO securityTypeDTO = securityTypeMapper.toDto(securityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityType in the database
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(0)).save(securityType);
    }

    @Test
    @Transactional
    void deleteSecurityType() throws Exception {
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);

        int databaseSizeBeforeDelete = securityTypeRepository.findAll().size();

        // Delete the securityType
        restSecurityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityType> securityTypeList = securityTypeRepository.findAll();
        assertThat(securityTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SecurityType in Elasticsearch
        verify(mockSecurityTypeSearchRepository, times(1)).deleteById(securityType.getId());
    }

    @Test
    @Transactional
    void searchSecurityType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        securityTypeRepository.saveAndFlush(securityType);
        when(mockSecurityTypeSearchRepository.search("id:" + securityType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(securityType), PageRequest.of(0, 1), 1));

        // Search the securityType
        restSecurityTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTypeCode").value(hasItem(DEFAULT_SECURITY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].securityType").value(hasItem(DEFAULT_SECURITY_TYPE)))
            .andExpect(jsonPath("$.[*].securityTypeDetails").value(hasItem(DEFAULT_SECURITY_TYPE_DETAILS.toString())));
    }
}
