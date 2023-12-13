package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.InstitutionStatusType;
import io.github.erp.repository.InstitutionStatusTypeRepository;
import io.github.erp.repository.search.InstitutionStatusTypeSearchRepository;
import io.github.erp.service.criteria.InstitutionStatusTypeCriteria;
import io.github.erp.service.dto.InstitutionStatusTypeDTO;
import io.github.erp.service.mapper.InstitutionStatusTypeMapper;
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
 * Integration tests for the {@link InstitutionStatusTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InstitutionStatusTypeResourceIT {

    private static final String DEFAULT_INSTITUTION_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institution-status-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/institution-status-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstitutionStatusTypeRepository institutionStatusTypeRepository;

    @Autowired
    private InstitutionStatusTypeMapper institutionStatusTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InstitutionStatusTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InstitutionStatusTypeSearchRepository mockInstitutionStatusTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstitutionStatusTypeMockMvc;

    private InstitutionStatusType institutionStatusType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionStatusType createEntity(EntityManager em) {
        InstitutionStatusType institutionStatusType = new InstitutionStatusType()
            .institutionStatusCode(DEFAULT_INSTITUTION_STATUS_CODE)
            .institutionStatusType(DEFAULT_INSTITUTION_STATUS_TYPE)
            .insitutionStatusTypeDescription(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION);
        return institutionStatusType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionStatusType createUpdatedEntity(EntityManager em) {
        InstitutionStatusType institutionStatusType = new InstitutionStatusType()
            .institutionStatusCode(UPDATED_INSTITUTION_STATUS_CODE)
            .institutionStatusType(UPDATED_INSTITUTION_STATUS_TYPE)
            .insitutionStatusTypeDescription(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);
        return institutionStatusType;
    }

    @BeforeEach
    public void initTest() {
        institutionStatusType = createEntity(em);
    }

    @Test
    @Transactional
    void createInstitutionStatusType() throws Exception {
        int databaseSizeBeforeCreate = institutionStatusTypeRepository.findAll().size();
        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);
        restInstitutionStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionStatusType testInstitutionStatusType = institutionStatusTypeList.get(institutionStatusTypeList.size() - 1);
        assertThat(testInstitutionStatusType.getInstitutionStatusCode()).isEqualTo(DEFAULT_INSTITUTION_STATUS_CODE);
        assertThat(testInstitutionStatusType.getInstitutionStatusType()).isEqualTo(DEFAULT_INSTITUTION_STATUS_TYPE);
        assertThat(testInstitutionStatusType.getInsitutionStatusTypeDescription()).isEqualTo(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(1)).save(testInstitutionStatusType);
    }

    @Test
    @Transactional
    void createInstitutionStatusTypeWithExistingId() throws Exception {
        // Create the InstitutionStatusType with an existing ID
        institutionStatusType.setId(1L);
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        int databaseSizeBeforeCreate = institutionStatusTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void checkInstitutionStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionStatusTypeRepository.findAll().size();
        // set the field null
        institutionStatusType.setInstitutionStatusCode(null);

        // Create the InstitutionStatusType, which fails.
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        restInstitutionStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypes() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionStatusCode").value(hasItem(DEFAULT_INSTITUTION_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].institutionStatusType").value(hasItem(DEFAULT_INSTITUTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].insitutionStatusTypeDescription").value(hasItem(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getInstitutionStatusType() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get the institutionStatusType
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, institutionStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institutionStatusType.getId().intValue()))
            .andExpect(jsonPath("$.institutionStatusCode").value(DEFAULT_INSTITUTION_STATUS_CODE))
            .andExpect(jsonPath("$.institutionStatusType").value(DEFAULT_INSTITUTION_STATUS_TYPE))
            .andExpect(jsonPath("$.insitutionStatusTypeDescription").value(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getInstitutionStatusTypesByIdFiltering() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        Long id = institutionStatusType.getId();

        defaultInstitutionStatusTypeShouldBeFound("id.equals=" + id);
        defaultInstitutionStatusTypeShouldNotBeFound("id.notEquals=" + id);

        defaultInstitutionStatusTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInstitutionStatusTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultInstitutionStatusTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInstitutionStatusTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode equals to DEFAULT_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusCode.equals=" + DEFAULT_INSTITUTION_STATUS_CODE);

        // Get all the institutionStatusTypeList where institutionStatusCode equals to UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.equals=" + UPDATED_INSTITUTION_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode not equals to DEFAULT_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.notEquals=" + DEFAULT_INSTITUTION_STATUS_CODE);

        // Get all the institutionStatusTypeList where institutionStatusCode not equals to UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusCode.notEquals=" + UPDATED_INSTITUTION_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode in DEFAULT_INSTITUTION_STATUS_CODE or UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldBeFound(
            "institutionStatusCode.in=" + DEFAULT_INSTITUTION_STATUS_CODE + "," + UPDATED_INSTITUTION_STATUS_CODE
        );

        // Get all the institutionStatusTypeList where institutionStatusCode equals to UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.in=" + UPDATED_INSTITUTION_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode is not null
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusCode.specified=true");

        // Get all the institutionStatusTypeList where institutionStatusCode is null
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode contains DEFAULT_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusCode.contains=" + DEFAULT_INSTITUTION_STATUS_CODE);

        // Get all the institutionStatusTypeList where institutionStatusCode contains UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.contains=" + UPDATED_INSTITUTION_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusCode does not contain DEFAULT_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusCode.doesNotContain=" + DEFAULT_INSTITUTION_STATUS_CODE);

        // Get all the institutionStatusTypeList where institutionStatusCode does not contain UPDATED_INSTITUTION_STATUS_CODE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusCode.doesNotContain=" + UPDATED_INSTITUTION_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType equals to DEFAULT_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusType.equals=" + DEFAULT_INSTITUTION_STATUS_TYPE);

        // Get all the institutionStatusTypeList where institutionStatusType equals to UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.equals=" + UPDATED_INSTITUTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType not equals to DEFAULT_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.notEquals=" + DEFAULT_INSTITUTION_STATUS_TYPE);

        // Get all the institutionStatusTypeList where institutionStatusType not equals to UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusType.notEquals=" + UPDATED_INSTITUTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType in DEFAULT_INSTITUTION_STATUS_TYPE or UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldBeFound(
            "institutionStatusType.in=" + DEFAULT_INSTITUTION_STATUS_TYPE + "," + UPDATED_INSTITUTION_STATUS_TYPE
        );

        // Get all the institutionStatusTypeList where institutionStatusType equals to UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.in=" + UPDATED_INSTITUTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType is not null
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusType.specified=true");

        // Get all the institutionStatusTypeList where institutionStatusType is null
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType contains DEFAULT_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusType.contains=" + DEFAULT_INSTITUTION_STATUS_TYPE);

        // Get all the institutionStatusTypeList where institutionStatusType contains UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.contains=" + UPDATED_INSTITUTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllInstitutionStatusTypesByInstitutionStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        // Get all the institutionStatusTypeList where institutionStatusType does not contain DEFAULT_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldNotBeFound("institutionStatusType.doesNotContain=" + DEFAULT_INSTITUTION_STATUS_TYPE);

        // Get all the institutionStatusTypeList where institutionStatusType does not contain UPDATED_INSTITUTION_STATUS_TYPE
        defaultInstitutionStatusTypeShouldBeFound("institutionStatusType.doesNotContain=" + UPDATED_INSTITUTION_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInstitutionStatusTypeShouldBeFound(String filter) throws Exception {
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionStatusCode").value(hasItem(DEFAULT_INSTITUTION_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].institutionStatusType").value(hasItem(DEFAULT_INSTITUTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].insitutionStatusTypeDescription").value(hasItem(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInstitutionStatusTypeShouldNotBeFound(String filter) throws Exception {
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInstitutionStatusType() throws Exception {
        // Get the institutionStatusType
        restInstitutionStatusTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstitutionStatusType() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();

        // Update the institutionStatusType
        InstitutionStatusType updatedInstitutionStatusType = institutionStatusTypeRepository.findById(institutionStatusType.getId()).get();
        // Disconnect from session so that the updates on updatedInstitutionStatusType are not directly saved in db
        em.detach(updatedInstitutionStatusType);
        updatedInstitutionStatusType
            .institutionStatusCode(UPDATED_INSTITUTION_STATUS_CODE)
            .institutionStatusType(UPDATED_INSTITUTION_STATUS_TYPE)
            .insitutionStatusTypeDescription(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(updatedInstitutionStatusType);

        restInstitutionStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionStatusType testInstitutionStatusType = institutionStatusTypeList.get(institutionStatusTypeList.size() - 1);
        assertThat(testInstitutionStatusType.getInstitutionStatusCode()).isEqualTo(UPDATED_INSTITUTION_STATUS_CODE);
        assertThat(testInstitutionStatusType.getInstitutionStatusType()).isEqualTo(UPDATED_INSTITUTION_STATUS_TYPE);
        assertThat(testInstitutionStatusType.getInsitutionStatusTypeDescription()).isEqualTo(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository).save(testInstitutionStatusType);
    }

    @Test
    @Transactional
    void putNonExistingInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void partialUpdateInstitutionStatusTypeWithPatch() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();

        // Update the institutionStatusType using partial update
        InstitutionStatusType partialUpdatedInstitutionStatusType = new InstitutionStatusType();
        partialUpdatedInstitutionStatusType.setId(institutionStatusType.getId());

        partialUpdatedInstitutionStatusType
            .institutionStatusType(UPDATED_INSTITUTION_STATUS_TYPE)
            .insitutionStatusTypeDescription(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);

        restInstitutionStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionStatusType))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionStatusType testInstitutionStatusType = institutionStatusTypeList.get(institutionStatusTypeList.size() - 1);
        assertThat(testInstitutionStatusType.getInstitutionStatusCode()).isEqualTo(DEFAULT_INSTITUTION_STATUS_CODE);
        assertThat(testInstitutionStatusType.getInstitutionStatusType()).isEqualTo(UPDATED_INSTITUTION_STATUS_TYPE);
        assertThat(testInstitutionStatusType.getInsitutionStatusTypeDescription()).isEqualTo(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInstitutionStatusTypeWithPatch() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();

        // Update the institutionStatusType using partial update
        InstitutionStatusType partialUpdatedInstitutionStatusType = new InstitutionStatusType();
        partialUpdatedInstitutionStatusType.setId(institutionStatusType.getId());

        partialUpdatedInstitutionStatusType
            .institutionStatusCode(UPDATED_INSTITUTION_STATUS_CODE)
            .institutionStatusType(UPDATED_INSTITUTION_STATUS_TYPE)
            .insitutionStatusTypeDescription(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);

        restInstitutionStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionStatusType))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionStatusType testInstitutionStatusType = institutionStatusTypeList.get(institutionStatusTypeList.size() - 1);
        assertThat(testInstitutionStatusType.getInstitutionStatusCode()).isEqualTo(UPDATED_INSTITUTION_STATUS_CODE);
        assertThat(testInstitutionStatusType.getInstitutionStatusType()).isEqualTo(UPDATED_INSTITUTION_STATUS_TYPE);
        assertThat(testInstitutionStatusType.getInsitutionStatusTypeDescription()).isEqualTo(UPDATED_INSITUTION_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, institutionStatusTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstitutionStatusType() throws Exception {
        int databaseSizeBeforeUpdate = institutionStatusTypeRepository.findAll().size();
        institutionStatusType.setId(count.incrementAndGet());

        // Create the InstitutionStatusType
        InstitutionStatusTypeDTO institutionStatusTypeDTO = institutionStatusTypeMapper.toDto(institutionStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionStatusType in the database
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(0)).save(institutionStatusType);
    }

    @Test
    @Transactional
    void deleteInstitutionStatusType() throws Exception {
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);

        int databaseSizeBeforeDelete = institutionStatusTypeRepository.findAll().size();

        // Delete the institutionStatusType
        restInstitutionStatusTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, institutionStatusType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InstitutionStatusType> institutionStatusTypeList = institutionStatusTypeRepository.findAll();
        assertThat(institutionStatusTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InstitutionStatusType in Elasticsearch
        verify(mockInstitutionStatusTypeSearchRepository, times(1)).deleteById(institutionStatusType.getId());
    }

    @Test
    @Transactional
    void searchInstitutionStatusType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        institutionStatusTypeRepository.saveAndFlush(institutionStatusType);
        when(mockInstitutionStatusTypeSearchRepository.search("id:" + institutionStatusType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(institutionStatusType), PageRequest.of(0, 1), 1));

        // Search the institutionStatusType
        restInstitutionStatusTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + institutionStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionStatusCode").value(hasItem(DEFAULT_INSTITUTION_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].institutionStatusType").value(hasItem(DEFAULT_INSTITUTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].insitutionStatusTypeDescription").value(hasItem(DEFAULT_INSITUTION_STATUS_TYPE_DESCRIPTION.toString()))
            );
    }
}
