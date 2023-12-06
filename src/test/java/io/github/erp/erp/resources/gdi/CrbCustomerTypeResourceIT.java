package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.domain.CrbCustomerType;
import io.github.erp.repository.CrbCustomerTypeRepository;
import io.github.erp.repository.search.CrbCustomerTypeSearchRepository;
import io.github.erp.service.dto.CrbCustomerTypeDTO;
import io.github.erp.service.mapper.CrbCustomerTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CrbCustomerTypeResource;
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
 * Integration tests for the {@link CrbCustomerTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CrbCustomerTypeResourceIT {

    private static final String DEFAULT_CUSTOMER_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/crb-customer-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/crb-customer-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbCustomerTypeRepository crbCustomerTypeRepository;

    @Autowired
    private CrbCustomerTypeMapper crbCustomerTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbCustomerTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbCustomerTypeSearchRepository mockCrbCustomerTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbCustomerTypeMockMvc;

    private CrbCustomerType crbCustomerType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCustomerType createEntity(EntityManager em) {
        CrbCustomerType crbCustomerType = new CrbCustomerType()
            .customerTypeCode(DEFAULT_CUSTOMER_TYPE_CODE)
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return crbCustomerType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbCustomerType createUpdatedEntity(EntityManager em) {
        CrbCustomerType crbCustomerType = new CrbCustomerType()
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .description(UPDATED_DESCRIPTION);
        return crbCustomerType;
    }

    @BeforeEach
    public void initTest() {
        crbCustomerType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbCustomerType() throws Exception {
        int databaseSizeBeforeCreate = crbCustomerTypeRepository.findAll().size();
        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);
        restCrbCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbCustomerType testCrbCustomerType = crbCustomerTypeList.get(crbCustomerTypeList.size() - 1);
        assertThat(testCrbCustomerType.getCustomerTypeCode()).isEqualTo(DEFAULT_CUSTOMER_TYPE_CODE);
        assertThat(testCrbCustomerType.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCrbCustomerType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(1)).save(testCrbCustomerType);
    }

    @Test
    @Transactional
    void createCrbCustomerTypeWithExistingId() throws Exception {
        // Create the CrbCustomerType with an existing ID
        crbCustomerType.setId(1L);
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        int databaseSizeBeforeCreate = crbCustomerTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void checkCustomerTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCustomerTypeRepository.findAll().size();
        // set the field null
        crbCustomerType.setCustomerTypeCode(null);

        // Create the CrbCustomerType, which fails.
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        restCrbCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbCustomerTypeRepository.findAll().size();
        // set the field null
        crbCustomerType.setCustomerType(null);

        // Create the CrbCustomerType, which fails.
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        restCrbCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypes() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCrbCustomerType() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get the crbCustomerType
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbCustomerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbCustomerType.getId().intValue()))
            .andExpect(jsonPath("$.customerTypeCode").value(DEFAULT_CUSTOMER_TYPE_CODE))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCrbCustomerTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        Long id = crbCustomerType.getId();

        defaultCrbCustomerTypeShouldBeFound("id.equals=" + id);
        defaultCrbCustomerTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbCustomerTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbCustomerTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbCustomerTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbCustomerTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode equals to DEFAULT_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.equals=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the crbCustomerTypeList where customerTypeCode equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.equals=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode not equals to DEFAULT_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.notEquals=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the crbCustomerTypeList where customerTypeCode not equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.notEquals=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode in DEFAULT_CUSTOMER_TYPE_CODE or UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.in=" + DEFAULT_CUSTOMER_TYPE_CODE + "," + UPDATED_CUSTOMER_TYPE_CODE);

        // Get all the crbCustomerTypeList where customerTypeCode equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.in=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode is not null
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.specified=true");

        // Get all the crbCustomerTypeList where customerTypeCode is null
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode contains DEFAULT_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.contains=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the crbCustomerTypeList where customerTypeCode contains UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.contains=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerTypeCode does not contain DEFAULT_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldNotBeFound("customerTypeCode.doesNotContain=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the crbCustomerTypeList where customerTypeCode does not contain UPDATED_CUSTOMER_TYPE_CODE
        defaultCrbCustomerTypeShouldBeFound("customerTypeCode.doesNotContain=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType equals to DEFAULT_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldBeFound("customerType.equals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the crbCustomerTypeList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldNotBeFound("customerType.equals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType not equals to DEFAULT_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldNotBeFound("customerType.notEquals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the crbCustomerTypeList where customerType not equals to UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldBeFound("customerType.notEquals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType in DEFAULT_CUSTOMER_TYPE or UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldBeFound("customerType.in=" + DEFAULT_CUSTOMER_TYPE + "," + UPDATED_CUSTOMER_TYPE);

        // Get all the crbCustomerTypeList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldNotBeFound("customerType.in=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType is not null
        defaultCrbCustomerTypeShouldBeFound("customerType.specified=true");

        // Get all the crbCustomerTypeList where customerType is null
        defaultCrbCustomerTypeShouldNotBeFound("customerType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeContainsSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType contains DEFAULT_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldBeFound("customerType.contains=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the crbCustomerTypeList where customerType contains UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldNotBeFound("customerType.contains=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbCustomerTypesByCustomerTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        // Get all the crbCustomerTypeList where customerType does not contain DEFAULT_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldNotBeFound("customerType.doesNotContain=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the crbCustomerTypeList where customerType does not contain UPDATED_CUSTOMER_TYPE
        defaultCrbCustomerTypeShouldBeFound("customerType.doesNotContain=" + UPDATED_CUSTOMER_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbCustomerTypeShouldBeFound(String filter) throws Exception {
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbCustomerTypeShouldNotBeFound(String filter) throws Exception {
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbCustomerType() throws Exception {
        // Get the crbCustomerType
        restCrbCustomerTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbCustomerType() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();

        // Update the crbCustomerType
        CrbCustomerType updatedCrbCustomerType = crbCustomerTypeRepository.findById(crbCustomerType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbCustomerType are not directly saved in db
        em.detach(updatedCrbCustomerType);
        updatedCrbCustomerType
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .description(UPDATED_DESCRIPTION);
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(updatedCrbCustomerType);

        restCrbCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCustomerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCustomerType testCrbCustomerType = crbCustomerTypeList.get(crbCustomerTypeList.size() - 1);
        assertThat(testCrbCustomerType.getCustomerTypeCode()).isEqualTo(UPDATED_CUSTOMER_TYPE_CODE);
        assertThat(testCrbCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCrbCustomerType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository).save(testCrbCustomerType);
    }

    @Test
    @Transactional
    void putNonExistingCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbCustomerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void partialUpdateCrbCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();

        // Update the crbCustomerType using partial update
        CrbCustomerType partialUpdatedCrbCustomerType = new CrbCustomerType();
        partialUpdatedCrbCustomerType.setId(crbCustomerType.getId());

        partialUpdatedCrbCustomerType.customerType(UPDATED_CUSTOMER_TYPE);

        restCrbCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCustomerType testCrbCustomerType = crbCustomerTypeList.get(crbCustomerTypeList.size() - 1);
        assertThat(testCrbCustomerType.getCustomerTypeCode()).isEqualTo(DEFAULT_CUSTOMER_TYPE_CODE);
        assertThat(testCrbCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCrbCustomerType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();

        // Update the crbCustomerType using partial update
        CrbCustomerType partialUpdatedCrbCustomerType = new CrbCustomerType();
        partialUpdatedCrbCustomerType.setId(crbCustomerType.getId());

        partialUpdatedCrbCustomerType
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .description(UPDATED_DESCRIPTION);

        restCrbCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbCustomerType testCrbCustomerType = crbCustomerTypeList.get(crbCustomerTypeList.size() - 1);
        assertThat(testCrbCustomerType.getCustomerTypeCode()).isEqualTo(UPDATED_CUSTOMER_TYPE_CODE);
        assertThat(testCrbCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCrbCustomerType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbCustomerTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = crbCustomerTypeRepository.findAll().size();
        crbCustomerType.setId(count.incrementAndGet());

        // Create the CrbCustomerType
        CrbCustomerTypeDTO crbCustomerTypeDTO = crbCustomerTypeMapper.toDto(crbCustomerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbCustomerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbCustomerType in the database
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(0)).save(crbCustomerType);
    }

    @Test
    @Transactional
    void deleteCrbCustomerType() throws Exception {
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);

        int databaseSizeBeforeDelete = crbCustomerTypeRepository.findAll().size();

        // Delete the crbCustomerType
        restCrbCustomerTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbCustomerType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbCustomerType> crbCustomerTypeList = crbCustomerTypeRepository.findAll();
        assertThat(crbCustomerTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbCustomerType in Elasticsearch
        verify(mockCrbCustomerTypeSearchRepository, times(1)).deleteById(crbCustomerType.getId());
    }

    @Test
    @Transactional
    void searchCrbCustomerType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbCustomerTypeRepository.saveAndFlush(crbCustomerType);
        when(mockCrbCustomerTypeSearchRepository.search("id:" + crbCustomerType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbCustomerType), PageRequest.of(0, 1), 1));

        // Search the crbCustomerType
        restCrbCustomerTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbCustomerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbCustomerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
