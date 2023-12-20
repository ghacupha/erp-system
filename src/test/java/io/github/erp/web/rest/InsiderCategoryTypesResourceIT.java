package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import io.github.erp.domain.InsiderCategoryTypes;
import io.github.erp.repository.InsiderCategoryTypesRepository;
import io.github.erp.repository.search.InsiderCategoryTypesSearchRepository;
import io.github.erp.service.criteria.InsiderCategoryTypesCriteria;
import io.github.erp.service.dto.InsiderCategoryTypesDTO;
import io.github.erp.service.mapper.InsiderCategoryTypesMapper;
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
 * Integration tests for the {@link InsiderCategoryTypesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsiderCategoryTypesResourceIT {

    private static final String DEFAULT_INSIDER_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INSIDER_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_INSIDER_CATEGORY_TYPE_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_INSIDER_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_INSIDER_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insider-category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/insider-category-types";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsiderCategoryTypesRepository insiderCategoryTypesRepository;

    @Autowired
    private InsiderCategoryTypesMapper insiderCategoryTypesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InsiderCategoryTypesSearchRepositoryMockConfiguration
     */
    @Autowired
    private InsiderCategoryTypesSearchRepository mockInsiderCategoryTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsiderCategoryTypesMockMvc;

    private InsiderCategoryTypes insiderCategoryTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsiderCategoryTypes createEntity(EntityManager em) {
        InsiderCategoryTypes insiderCategoryTypes = new InsiderCategoryTypes()
            .insiderCategoryTypeCode(DEFAULT_INSIDER_CATEGORY_TYPE_CODE)
            .insiderCategoryTypeDetail(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL)
            .insiderCategoryDescription(DEFAULT_INSIDER_CATEGORY_DESCRIPTION);
        return insiderCategoryTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsiderCategoryTypes createUpdatedEntity(EntityManager em) {
        InsiderCategoryTypes insiderCategoryTypes = new InsiderCategoryTypes()
            .insiderCategoryTypeCode(UPDATED_INSIDER_CATEGORY_TYPE_CODE)
            .insiderCategoryTypeDetail(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL)
            .insiderCategoryDescription(UPDATED_INSIDER_CATEGORY_DESCRIPTION);
        return insiderCategoryTypes;
    }

    @BeforeEach
    public void initTest() {
        insiderCategoryTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeCreate = insiderCategoryTypesRepository.findAll().size();
        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);
        restInsiderCategoryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeCreate + 1);
        InsiderCategoryTypes testInsiderCategoryTypes = insiderCategoryTypesList.get(insiderCategoryTypesList.size() - 1);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeCode()).isEqualTo(DEFAULT_INSIDER_CATEGORY_TYPE_CODE);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeDetail()).isEqualTo(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryDescription()).isEqualTo(DEFAULT_INSIDER_CATEGORY_DESCRIPTION);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(1)).save(testInsiderCategoryTypes);
    }

    @Test
    @Transactional
    void createInsiderCategoryTypesWithExistingId() throws Exception {
        // Create the InsiderCategoryTypes with an existing ID
        insiderCategoryTypes.setId(1L);
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        int databaseSizeBeforeCreate = insiderCategoryTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsiderCategoryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeCreate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void checkInsiderCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = insiderCategoryTypesRepository.findAll().size();
        // set the field null
        insiderCategoryTypes.setInsiderCategoryTypeCode(null);

        // Create the InsiderCategoryTypes, which fails.
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        restInsiderCategoryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsiderCategoryTypeDetailIsRequired() throws Exception {
        int databaseSizeBeforeTest = insiderCategoryTypesRepository.findAll().size();
        // set the field null
        insiderCategoryTypes.setInsiderCategoryTypeDetail(null);

        // Create the InsiderCategoryTypes, which fails.
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        restInsiderCategoryTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypes() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insiderCategoryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeCode").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeDetail").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL)))
            .andExpect(jsonPath("$.[*].insiderCategoryDescription").value(hasItem(DEFAULT_INSIDER_CATEGORY_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getInsiderCategoryTypes() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get the insiderCategoryTypes
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, insiderCategoryTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insiderCategoryTypes.getId().intValue()))
            .andExpect(jsonPath("$.insiderCategoryTypeCode").value(DEFAULT_INSIDER_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.insiderCategoryTypeDetail").value(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL))
            .andExpect(jsonPath("$.insiderCategoryDescription").value(DEFAULT_INSIDER_CATEGORY_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInsiderCategoryTypesByIdFiltering() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        Long id = insiderCategoryTypes.getId();

        defaultInsiderCategoryTypesShouldBeFound("id.equals=" + id);
        defaultInsiderCategoryTypesShouldNotBeFound("id.notEquals=" + id);

        defaultInsiderCategoryTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInsiderCategoryTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultInsiderCategoryTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInsiderCategoryTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode equals to DEFAULT_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeCode.equals=" + DEFAULT_INSIDER_CATEGORY_TYPE_CODE);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode equals to UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.equals=" + UPDATED_INSIDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode not equals to DEFAULT_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.notEquals=" + DEFAULT_INSIDER_CATEGORY_TYPE_CODE);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode not equals to UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeCode.notEquals=" + UPDATED_INSIDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode in DEFAULT_INSIDER_CATEGORY_TYPE_CODE or UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldBeFound(
            "insiderCategoryTypeCode.in=" + DEFAULT_INSIDER_CATEGORY_TYPE_CODE + "," + UPDATED_INSIDER_CATEGORY_TYPE_CODE
        );

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode equals to UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.in=" + UPDATED_INSIDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode is not null
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeCode.specified=true");

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode is null
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode contains DEFAULT_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeCode.contains=" + DEFAULT_INSIDER_CATEGORY_TYPE_CODE);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode contains UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.contains=" + UPDATED_INSIDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode does not contain DEFAULT_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeCode.doesNotContain=" + DEFAULT_INSIDER_CATEGORY_TYPE_CODE);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeCode does not contain UPDATED_INSIDER_CATEGORY_TYPE_CODE
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeCode.doesNotContain=" + UPDATED_INSIDER_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail equals to DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeDetail.equals=" + DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail equals to UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.equals=" + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail not equals to DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.notEquals=" + DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail not equals to UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeDetail.notEquals=" + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailIsInShouldWork() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail in DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL or UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldBeFound(
            "insiderCategoryTypeDetail.in=" + DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL + "," + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        );

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail equals to UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.in=" + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail is not null
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeDetail.specified=true");

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail is null
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.specified=false");
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailContainsSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail contains DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeDetail.contains=" + DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail contains UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.contains=" + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
    }

    @Test
    @Transactional
    void getAllInsiderCategoryTypesByInsiderCategoryTypeDetailNotContainsSomething() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail does not contain DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldNotBeFound("insiderCategoryTypeDetail.doesNotContain=" + DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL);

        // Get all the insiderCategoryTypesList where insiderCategoryTypeDetail does not contain UPDATED_INSIDER_CATEGORY_TYPE_DETAIL
        defaultInsiderCategoryTypesShouldBeFound("insiderCategoryTypeDetail.doesNotContain=" + UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsiderCategoryTypesShouldBeFound(String filter) throws Exception {
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insiderCategoryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeCode").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeDetail").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL)))
            .andExpect(jsonPath("$.[*].insiderCategoryDescription").value(hasItem(DEFAULT_INSIDER_CATEGORY_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsiderCategoryTypesShouldNotBeFound(String filter) throws Exception {
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsiderCategoryTypes() throws Exception {
        // Get the insiderCategoryTypes
        restInsiderCategoryTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInsiderCategoryTypes() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();

        // Update the insiderCategoryTypes
        InsiderCategoryTypes updatedInsiderCategoryTypes = insiderCategoryTypesRepository.findById(insiderCategoryTypes.getId()).get();
        // Disconnect from session so that the updates on updatedInsiderCategoryTypes are not directly saved in db
        em.detach(updatedInsiderCategoryTypes);
        updatedInsiderCategoryTypes
            .insiderCategoryTypeCode(UPDATED_INSIDER_CATEGORY_TYPE_CODE)
            .insiderCategoryTypeDetail(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL)
            .insiderCategoryDescription(UPDATED_INSIDER_CATEGORY_DESCRIPTION);
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(updatedInsiderCategoryTypes);

        restInsiderCategoryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insiderCategoryTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);
        InsiderCategoryTypes testInsiderCategoryTypes = insiderCategoryTypesList.get(insiderCategoryTypesList.size() - 1);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeCode()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_CODE);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeDetail()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryDescription()).isEqualTo(UPDATED_INSIDER_CATEGORY_DESCRIPTION);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository).save(testInsiderCategoryTypes);
    }

    @Test
    @Transactional
    void putNonExistingInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insiderCategoryTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void partialUpdateInsiderCategoryTypesWithPatch() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();

        // Update the insiderCategoryTypes using partial update
        InsiderCategoryTypes partialUpdatedInsiderCategoryTypes = new InsiderCategoryTypes();
        partialUpdatedInsiderCategoryTypes.setId(insiderCategoryTypes.getId());

        partialUpdatedInsiderCategoryTypes
            .insiderCategoryTypeCode(UPDATED_INSIDER_CATEGORY_TYPE_CODE)
            .insiderCategoryTypeDetail(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);

        restInsiderCategoryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsiderCategoryTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsiderCategoryTypes))
            )
            .andExpect(status().isOk());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);
        InsiderCategoryTypes testInsiderCategoryTypes = insiderCategoryTypesList.get(insiderCategoryTypesList.size() - 1);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeCode()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_CODE);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeDetail()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryDescription()).isEqualTo(DEFAULT_INSIDER_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInsiderCategoryTypesWithPatch() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();

        // Update the insiderCategoryTypes using partial update
        InsiderCategoryTypes partialUpdatedInsiderCategoryTypes = new InsiderCategoryTypes();
        partialUpdatedInsiderCategoryTypes.setId(insiderCategoryTypes.getId());

        partialUpdatedInsiderCategoryTypes
            .insiderCategoryTypeCode(UPDATED_INSIDER_CATEGORY_TYPE_CODE)
            .insiderCategoryTypeDetail(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL)
            .insiderCategoryDescription(UPDATED_INSIDER_CATEGORY_DESCRIPTION);

        restInsiderCategoryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsiderCategoryTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsiderCategoryTypes))
            )
            .andExpect(status().isOk());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);
        InsiderCategoryTypes testInsiderCategoryTypes = insiderCategoryTypesList.get(insiderCategoryTypesList.size() - 1);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeCode()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_CODE);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryTypeDetail()).isEqualTo(UPDATED_INSIDER_CATEGORY_TYPE_DETAIL);
        assertThat(testInsiderCategoryTypes.getInsiderCategoryDescription()).isEqualTo(UPDATED_INSIDER_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insiderCategoryTypesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsiderCategoryTypes() throws Exception {
        int databaseSizeBeforeUpdate = insiderCategoryTypesRepository.findAll().size();
        insiderCategoryTypes.setId(count.incrementAndGet());

        // Create the InsiderCategoryTypes
        InsiderCategoryTypesDTO insiderCategoryTypesDTO = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsiderCategoryTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insiderCategoryTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsiderCategoryTypes in the database
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(0)).save(insiderCategoryTypes);
    }

    @Test
    @Transactional
    void deleteInsiderCategoryTypes() throws Exception {
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);

        int databaseSizeBeforeDelete = insiderCategoryTypesRepository.findAll().size();

        // Delete the insiderCategoryTypes
        restInsiderCategoryTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, insiderCategoryTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsiderCategoryTypes> insiderCategoryTypesList = insiderCategoryTypesRepository.findAll();
        assertThat(insiderCategoryTypesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InsiderCategoryTypes in Elasticsearch
        verify(mockInsiderCategoryTypesSearchRepository, times(1)).deleteById(insiderCategoryTypes.getId());
    }

    @Test
    @Transactional
    void searchInsiderCategoryTypes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        insiderCategoryTypesRepository.saveAndFlush(insiderCategoryTypes);
        when(mockInsiderCategoryTypesSearchRepository.search("id:" + insiderCategoryTypes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(insiderCategoryTypes), PageRequest.of(0, 1), 1));

        // Search the insiderCategoryTypes
        restInsiderCategoryTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + insiderCategoryTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insiderCategoryTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeCode").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].insiderCategoryTypeDetail").value(hasItem(DEFAULT_INSIDER_CATEGORY_TYPE_DETAIL)))
            .andExpect(jsonPath("$.[*].insiderCategoryDescription").value(hasItem(DEFAULT_INSIDER_CATEGORY_DESCRIPTION)));
    }
}
