package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.domain.CrbSourceOfInformationType;
import io.github.erp.repository.CrbSourceOfInformationTypeRepository;
import io.github.erp.repository.search.CrbSourceOfInformationTypeSearchRepository;
import io.github.erp.service.criteria.CrbSourceOfInformationTypeCriteria;
import io.github.erp.service.dto.CrbSourceOfInformationTypeDTO;
import io.github.erp.service.mapper.CrbSourceOfInformationTypeMapper;
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
 * Integration tests for the {@link CrbSourceOfInformationTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbSourceOfInformationTypeResourceIT {

    private static final String DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-source-of-information-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-source-of-information-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository;

    @Autowired
    private CrbSourceOfInformationTypeMapper crbSourceOfInformationTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbSourceOfInformationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbSourceOfInformationTypeSearchRepository mockCrbSourceOfInformationTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbSourceOfInformationTypeMockMvc;

    private CrbSourceOfInformationType crbSourceOfInformationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSourceOfInformationType createEntity(EntityManager em) {
        CrbSourceOfInformationType crbSourceOfInformationType = new CrbSourceOfInformationType()
            .sourceOfInformationTypeCode(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE)
            .sourceOfInformationTypeDescription(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);
        return crbSourceOfInformationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSourceOfInformationType createUpdatedEntity(EntityManager em) {
        CrbSourceOfInformationType crbSourceOfInformationType = new CrbSourceOfInformationType()
            .sourceOfInformationTypeCode(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE)
            .sourceOfInformationTypeDescription(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);
        return crbSourceOfInformationType;
    }

    @BeforeEach
    public void initTest() {
        crbSourceOfInformationType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeCreate = crbSourceOfInformationTypeRepository.findAll().size();
        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbSourceOfInformationType testCrbSourceOfInformationType = crbSourceOfInformationTypeList.get(
            crbSourceOfInformationTypeList.size() - 1
        );
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeCode()).isEqualTo(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeDescription())
            .isEqualTo(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(1)).save(testCrbSourceOfInformationType);
    }

    @Test
    @Transactional
    void createCrbSourceOfInformationTypeWithExistingId() throws Exception {
        // Create the CrbSourceOfInformationType with an existing ID
        crbSourceOfInformationType.setId(1L);
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        int databaseSizeBeforeCreate = crbSourceOfInformationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void checkSourceOfInformationTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbSourceOfInformationTypeRepository.findAll().size();
        // set the field null
        crbSourceOfInformationType.setSourceOfInformationTypeCode(null);

        // Create the CrbSourceOfInformationType, which fails.
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        restCrbSourceOfInformationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypes() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSourceOfInformationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeCode").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeDescription").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCrbSourceOfInformationType() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get the crbSourceOfInformationType
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbSourceOfInformationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbSourceOfInformationType.getId().intValue()))
            .andExpect(jsonPath("$.sourceOfInformationTypeCode").value(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE))
            .andExpect(jsonPath("$.sourceOfInformationTypeDescription").value(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCrbSourceOfInformationTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        Long id = crbSourceOfInformationType.getId();

        defaultCrbSourceOfInformationTypeShouldBeFound("id.equals=" + id);
        defaultCrbSourceOfInformationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbSourceOfInformationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbSourceOfInformationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbSourceOfInformationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbSourceOfInformationTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode equals to DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldBeFound("sourceOfInformationTypeCode.equals=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldNotBeFound("sourceOfInformationTypeCode.equals=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode not equals to DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeCode.notEquals=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode not equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldBeFound("sourceOfInformationTypeCode.notEquals=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode in DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE or UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeCode.in=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE + "," + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldNotBeFound("sourceOfInformationTypeCode.in=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode is not null
        defaultCrbSourceOfInformationTypeShouldBeFound("sourceOfInformationTypeCode.specified=true");

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode is null
        defaultCrbSourceOfInformationTypeShouldNotBeFound("sourceOfInformationTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode contains DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldBeFound("sourceOfInformationTypeCode.contains=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode contains UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeCode.contains=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode does not contain DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeCode.doesNotContain=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeCode does not contain UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeCode.doesNotContain=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription equals to DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeDescription.equals=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeDescription.equals=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription not equals to DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeDescription.notEquals=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription not equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeDescription.notEquals=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription in DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION or UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeDescription.in=" +
            DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION +
            "," +
            UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription equals to UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeDescription.in=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription is not null
        defaultCrbSourceOfInformationTypeShouldBeFound("sourceOfInformationTypeDescription.specified=true");

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription is null
        defaultCrbSourceOfInformationTypeShouldNotBeFound("sourceOfInformationTypeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription contains DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeDescription.contains=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription contains UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeDescription.contains=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCrbSourceOfInformationTypesBySourceOfInformationTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription does not contain DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldNotBeFound(
            "sourceOfInformationTypeDescription.doesNotContain=" + DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );

        // Get all the crbSourceOfInformationTypeList where sourceOfInformationTypeDescription does not contain UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        defaultCrbSourceOfInformationTypeShouldBeFound(
            "sourceOfInformationTypeDescription.doesNotContain=" + UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbSourceOfInformationTypeShouldBeFound(String filter) throws Exception {
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSourceOfInformationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeCode").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeDescription").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbSourceOfInformationTypeShouldNotBeFound(String filter) throws Exception {
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbSourceOfInformationType() throws Exception {
        // Get the crbSourceOfInformationType
        restCrbSourceOfInformationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbSourceOfInformationType() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();

        // Update the crbSourceOfInformationType
        CrbSourceOfInformationType updatedCrbSourceOfInformationType = crbSourceOfInformationTypeRepository
            .findById(crbSourceOfInformationType.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbSourceOfInformationType are not directly saved in db
        em.detach(updatedCrbSourceOfInformationType);
        updatedCrbSourceOfInformationType
            .sourceOfInformationTypeCode(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE)
            .sourceOfInformationTypeDescription(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(
            updatedCrbSourceOfInformationType
        );

        restCrbSourceOfInformationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSourceOfInformationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbSourceOfInformationType testCrbSourceOfInformationType = crbSourceOfInformationTypeList.get(
            crbSourceOfInformationTypeList.size() - 1
        );
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeCode()).isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeDescription())
            .isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository).save(testCrbSourceOfInformationType);
    }

    @Test
    @Transactional
    void putNonExistingCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSourceOfInformationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void partialUpdateCrbSourceOfInformationTypeWithPatch() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();

        // Update the crbSourceOfInformationType using partial update
        CrbSourceOfInformationType partialUpdatedCrbSourceOfInformationType = new CrbSourceOfInformationType();
        partialUpdatedCrbSourceOfInformationType.setId(crbSourceOfInformationType.getId());

        partialUpdatedCrbSourceOfInformationType
            .sourceOfInformationTypeCode(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE)
            .sourceOfInformationTypeDescription(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);

        restCrbSourceOfInformationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSourceOfInformationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSourceOfInformationType))
            )
            .andExpect(status().isOk());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbSourceOfInformationType testCrbSourceOfInformationType = crbSourceOfInformationTypeList.get(
            crbSourceOfInformationTypeList.size() - 1
        );
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeCode()).isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeDescription())
            .isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbSourceOfInformationTypeWithPatch() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();

        // Update the crbSourceOfInformationType using partial update
        CrbSourceOfInformationType partialUpdatedCrbSourceOfInformationType = new CrbSourceOfInformationType();
        partialUpdatedCrbSourceOfInformationType.setId(crbSourceOfInformationType.getId());

        partialUpdatedCrbSourceOfInformationType
            .sourceOfInformationTypeCode(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE)
            .sourceOfInformationTypeDescription(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);

        restCrbSourceOfInformationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSourceOfInformationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSourceOfInformationType))
            )
            .andExpect(status().isOk());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbSourceOfInformationType testCrbSourceOfInformationType = crbSourceOfInformationTypeList.get(
            crbSourceOfInformationTypeList.size() - 1
        );
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeCode()).isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbSourceOfInformationType.getSourceOfInformationTypeDescription())
            .isEqualTo(UPDATED_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbSourceOfInformationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbSourceOfInformationType() throws Exception {
        int databaseSizeBeforeUpdate = crbSourceOfInformationTypeRepository.findAll().size();
        crbSourceOfInformationType.setId(count.incrementAndGet());

        // Create the CrbSourceOfInformationType
        CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSourceOfInformationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSourceOfInformationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSourceOfInformationType in the database
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(0)).save(crbSourceOfInformationType);
    }

    @Test
    @Transactional
    void deleteCrbSourceOfInformationType() throws Exception {
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);

        int databaseSizeBeforeDelete = crbSourceOfInformationTypeRepository.findAll().size();

        // Delete the crbSourceOfInformationType
        restCrbSourceOfInformationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbSourceOfInformationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbSourceOfInformationType> crbSourceOfInformationTypeList = crbSourceOfInformationTypeRepository.findAll();
        assertThat(crbSourceOfInformationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbSourceOfInformationType in Elasticsearch
        verify(mockCrbSourceOfInformationTypeSearchRepository, times(1)).deleteById(crbSourceOfInformationType.getId());
    }

    @Test
    @Transactional
    void searchCrbSourceOfInformationType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbSourceOfInformationTypeRepository.saveAndFlush(crbSourceOfInformationType);
        when(mockCrbSourceOfInformationTypeSearchRepository.search("id:" + crbSourceOfInformationType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbSourceOfInformationType), PageRequest.of(0, 1), 1));

        // Search the crbSourceOfInformationType
        restCrbSourceOfInformationTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbSourceOfInformationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSourceOfInformationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeCode").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfInformationTypeDescription").value(hasItem(DEFAULT_SOURCE_OF_INFORMATION_TYPE_DESCRIPTION)));
    }
}
