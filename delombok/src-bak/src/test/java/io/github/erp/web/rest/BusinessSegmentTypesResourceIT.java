package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.BusinessSegmentTypes;
import io.github.erp.repository.BusinessSegmentTypesRepository;
import io.github.erp.repository.search.BusinessSegmentTypesSearchRepository;
import io.github.erp.service.criteria.BusinessSegmentTypesCriteria;
import io.github.erp.service.dto.BusinessSegmentTypesDTO;
import io.github.erp.service.mapper.BusinessSegmentTypesMapper;
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
 * Integration tests for the {@link BusinessSegmentTypesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BusinessSegmentTypesResourceIT {

    private static final String DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_ECONOMIC_SEGMENT = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_ECONOMIC_SEGMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-segment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/business-segment-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessSegmentTypesRepository businessSegmentTypesRepository;

    @Autowired
    private BusinessSegmentTypesMapper businessSegmentTypesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BusinessSegmentTypesSearchRepositoryMockConfiguration
     */
    @Autowired
    private BusinessSegmentTypesSearchRepository mockBusinessSegmentTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessSegmentTypesMockMvc;

    private BusinessSegmentTypes businessSegmentTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessSegmentTypes createEntity(EntityManager em) {
        BusinessSegmentTypes businessSegmentTypes = new BusinessSegmentTypes()
            .businessEconomicSegmentCode(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE)
            .businessEconomicSegment(DEFAULT_BUSINESS_ECONOMIC_SEGMENT)
            .details(DEFAULT_DETAILS);
        return businessSegmentTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessSegmentTypes createUpdatedEntity(EntityManager em) {
        BusinessSegmentTypes businessSegmentTypes = new BusinessSegmentTypes()
            .businessEconomicSegmentCode(UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE)
            .businessEconomicSegment(UPDATED_BUSINESS_ECONOMIC_SEGMENT)
            .details(UPDATED_DETAILS);
        return businessSegmentTypes;
    }

    @BeforeEach
    public void initTest() {
        businessSegmentTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeCreate = businessSegmentTypesRepository.findAll().size();
        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);
        restBusinessSegmentTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessSegmentTypes testBusinessSegmentTypes = businessSegmentTypesList.get(businessSegmentTypesList.size() - 1);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegmentCode()).isEqualTo(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegment()).isEqualTo(DEFAULT_BUSINESS_ECONOMIC_SEGMENT);
        assertThat(testBusinessSegmentTypes.getDetails()).isEqualTo(DEFAULT_DETAILS);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(1)).save(testBusinessSegmentTypes);
    }

    @Test
    @Transactional
    void createBusinessSegmentTypesWithExistingId() throws Exception {
        // Create the BusinessSegmentTypes with an existing ID
        businessSegmentTypes.setId(1L);
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        int databaseSizeBeforeCreate = businessSegmentTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessSegmentTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeCreate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void checkBusinessEconomicSegmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessSegmentTypesRepository.findAll().size();
        // set the field null
        businessSegmentTypes.setBusinessEconomicSegmentCode(null);

        // Create the BusinessSegmentTypes, which fails.
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        restBusinessSegmentTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBusinessEconomicSegmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessSegmentTypesRepository.findAll().size();
        // set the field null
        businessSegmentTypes.setBusinessEconomicSegment(null);

        // Create the BusinessSegmentTypes, which fails.
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        restBusinessSegmentTypesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypes() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessSegmentTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicSegmentCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicSegment").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getBusinessSegmentTypes() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get the businessSegmentTypes
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, businessSegmentTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessSegmentTypes.getId().intValue()))
            .andExpect(jsonPath("$.businessEconomicSegmentCode").value(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE))
            .andExpect(jsonPath("$.businessEconomicSegment").value(DEFAULT_BUSINESS_ECONOMIC_SEGMENT))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getBusinessSegmentTypesByIdFiltering() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        Long id = businessSegmentTypes.getId();

        defaultBusinessSegmentTypesShouldBeFound("id.equals=" + id);
        defaultBusinessSegmentTypesShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessSegmentTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessSegmentTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessSegmentTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessSegmentTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode equals to DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegmentCode.equals=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.equals=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode not equals to DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.notEquals=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode not equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegmentCode.notEquals=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode in DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE or UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldBeFound(
            "businessEconomicSegmentCode.in=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE + "," + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        );

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.in=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode is not null
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegmentCode.specified=true");

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode is null
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeContainsSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode contains DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegmentCode.contains=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode contains UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.contains=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentCodeNotContainsSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode does not contain DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegmentCode.doesNotContain=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);

        // Get all the businessSegmentTypesList where businessEconomicSegmentCode does not contain UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegmentCode.doesNotContain=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentIsEqualToSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment equals to DEFAULT_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegment.equals=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT);

        // Get all the businessSegmentTypesList where businessEconomicSegment equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.equals=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment not equals to DEFAULT_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.notEquals=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT);

        // Get all the businessSegmentTypesList where businessEconomicSegment not equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegment.notEquals=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentIsInShouldWork() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment in DEFAULT_BUSINESS_ECONOMIC_SEGMENT or UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldBeFound(
            "businessEconomicSegment.in=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT + "," + UPDATED_BUSINESS_ECONOMIC_SEGMENT
        );

        // Get all the businessSegmentTypesList where businessEconomicSegment equals to UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.in=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment is not null
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegment.specified=true");

        // Get all the businessSegmentTypesList where businessEconomicSegment is null
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentContainsSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment contains DEFAULT_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegment.contains=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT);

        // Get all the businessSegmentTypesList where businessEconomicSegment contains UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.contains=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT);
    }

    @Test
    @Transactional
    void getAllBusinessSegmentTypesByBusinessEconomicSegmentNotContainsSomething() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        // Get all the businessSegmentTypesList where businessEconomicSegment does not contain DEFAULT_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldNotBeFound("businessEconomicSegment.doesNotContain=" + DEFAULT_BUSINESS_ECONOMIC_SEGMENT);

        // Get all the businessSegmentTypesList where businessEconomicSegment does not contain UPDATED_BUSINESS_ECONOMIC_SEGMENT
        defaultBusinessSegmentTypesShouldBeFound("businessEconomicSegment.doesNotContain=" + UPDATED_BUSINESS_ECONOMIC_SEGMENT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessSegmentTypesShouldBeFound(String filter) throws Exception {
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessSegmentTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicSegmentCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicSegment").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));

        // Check, that the count call also returns 1
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessSegmentTypesShouldNotBeFound(String filter) throws Exception {
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusinessSegmentTypes() throws Exception {
        // Get the businessSegmentTypes
        restBusinessSegmentTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessSegmentTypes() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();

        // Update the businessSegmentTypes
        BusinessSegmentTypes updatedBusinessSegmentTypes = businessSegmentTypesRepository.findById(businessSegmentTypes.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessSegmentTypes are not directly saved in db
        em.detach(updatedBusinessSegmentTypes);
        updatedBusinessSegmentTypes
            .businessEconomicSegmentCode(UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE)
            .businessEconomicSegment(UPDATED_BUSINESS_ECONOMIC_SEGMENT)
            .details(UPDATED_DETAILS);
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(updatedBusinessSegmentTypes);

        restBusinessSegmentTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessSegmentTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);
        BusinessSegmentTypes testBusinessSegmentTypes = businessSegmentTypesList.get(businessSegmentTypesList.size() - 1);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegmentCode()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegment()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_SEGMENT);
        assertThat(testBusinessSegmentTypes.getDetails()).isEqualTo(UPDATED_DETAILS);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository).save(testBusinessSegmentTypes);
    }

    @Test
    @Transactional
    void putNonExistingBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessSegmentTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void partialUpdateBusinessSegmentTypesWithPatch() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();

        // Update the businessSegmentTypes using partial update
        BusinessSegmentTypes partialUpdatedBusinessSegmentTypes = new BusinessSegmentTypes();
        partialUpdatedBusinessSegmentTypes.setId(businessSegmentTypes.getId());

        partialUpdatedBusinessSegmentTypes.businessEconomicSegment(UPDATED_BUSINESS_ECONOMIC_SEGMENT).details(UPDATED_DETAILS);

        restBusinessSegmentTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessSegmentTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessSegmentTypes))
            )
            .andExpect(status().isOk());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);
        BusinessSegmentTypes testBusinessSegmentTypes = businessSegmentTypesList.get(businessSegmentTypesList.size() - 1);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegmentCode()).isEqualTo(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegment()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_SEGMENT);
        assertThat(testBusinessSegmentTypes.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateBusinessSegmentTypesWithPatch() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();

        // Update the businessSegmentTypes using partial update
        BusinessSegmentTypes partialUpdatedBusinessSegmentTypes = new BusinessSegmentTypes();
        partialUpdatedBusinessSegmentTypes.setId(businessSegmentTypes.getId());

        partialUpdatedBusinessSegmentTypes
            .businessEconomicSegmentCode(UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE)
            .businessEconomicSegment(UPDATED_BUSINESS_ECONOMIC_SEGMENT)
            .details(UPDATED_DETAILS);

        restBusinessSegmentTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessSegmentTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessSegmentTypes))
            )
            .andExpect(status().isOk());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);
        BusinessSegmentTypes testBusinessSegmentTypes = businessSegmentTypesList.get(businessSegmentTypesList.size() - 1);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegmentCode()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_SEGMENT_CODE);
        assertThat(testBusinessSegmentTypes.getBusinessEconomicSegment()).isEqualTo(UPDATED_BUSINESS_ECONOMIC_SEGMENT);
        assertThat(testBusinessSegmentTypes.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessSegmentTypesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessSegmentTypes() throws Exception {
        int databaseSizeBeforeUpdate = businessSegmentTypesRepository.findAll().size();
        businessSegmentTypes.setId(count.incrementAndGet());

        // Create the BusinessSegmentTypes
        BusinessSegmentTypesDTO businessSegmentTypesDTO = businessSegmentTypesMapper.toDto(businessSegmentTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessSegmentTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessSegmentTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessSegmentTypes in the database
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(0)).save(businessSegmentTypes);
    }

    @Test
    @Transactional
    void deleteBusinessSegmentTypes() throws Exception {
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);

        int databaseSizeBeforeDelete = businessSegmentTypesRepository.findAll().size();

        // Delete the businessSegmentTypes
        restBusinessSegmentTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessSegmentTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessSegmentTypes> businessSegmentTypesList = businessSegmentTypesRepository.findAll();
        assertThat(businessSegmentTypesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BusinessSegmentTypes in Elasticsearch
        verify(mockBusinessSegmentTypesSearchRepository, times(1)).deleteById(businessSegmentTypes.getId());
    }

    @Test
    @Transactional
    void searchBusinessSegmentTypes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        businessSegmentTypesRepository.saveAndFlush(businessSegmentTypes);
        when(mockBusinessSegmentTypesSearchRepository.search("id:" + businessSegmentTypes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(businessSegmentTypes), PageRequest.of(0, 1), 1));

        // Search the businessSegmentTypes
        restBusinessSegmentTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + businessSegmentTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessSegmentTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessEconomicSegmentCode").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT_CODE)))
            .andExpect(jsonPath("$.[*].businessEconomicSegment").value(hasItem(DEFAULT_BUSINESS_ECONOMIC_SEGMENT)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }
}
