package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.LoanProductType;
import io.github.erp.repository.LoanProductTypeRepository;
import io.github.erp.repository.search.LoanProductTypeSearchRepository;
import io.github.erp.service.criteria.LoanProductTypeCriteria;
import io.github.erp.service.dto.LoanProductTypeDTO;
import io.github.erp.service.mapper.LoanProductTypeMapper;
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
 * Integration tests for the {@link LoanProductTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoanProductTypeResourceIT {

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loan-product-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loan-product-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanProductTypeRepository loanProductTypeRepository;

    @Autowired
    private LoanProductTypeMapper loanProductTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanProductTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanProductTypeSearchRepository mockLoanProductTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanProductTypeMockMvc;

    private LoanProductType loanProductType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanProductType createEntity(EntityManager em) {
        LoanProductType loanProductType = new LoanProductType()
            .productCode(DEFAULT_PRODUCT_CODE)
            .productType(DEFAULT_PRODUCT_TYPE)
            .productTypeDescription(DEFAULT_PRODUCT_TYPE_DESCRIPTION);
        return loanProductType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanProductType createUpdatedEntity(EntityManager em) {
        LoanProductType loanProductType = new LoanProductType()
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);
        return loanProductType;
    }

    @BeforeEach
    public void initTest() {
        loanProductType = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanProductType() throws Exception {
        int databaseSizeBeforeCreate = loanProductTypeRepository.findAll().size();
        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);
        restLoanProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LoanProductType testLoanProductType = loanProductTypeList.get(loanProductTypeList.size() - 1);
        assertThat(testLoanProductType.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testLoanProductType.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testLoanProductType.getProductTypeDescription()).isEqualTo(DEFAULT_PRODUCT_TYPE_DESCRIPTION);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(1)).save(testLoanProductType);
    }

    @Test
    @Transactional
    void createLoanProductTypeWithExistingId() throws Exception {
        // Create the LoanProductType with an existing ID
        loanProductType.setId(1L);
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        int databaseSizeBeforeCreate = loanProductTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void checkProductCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanProductTypeRepository.findAll().size();
        // set the field null
        loanProductType.setProductCode(null);

        // Create the LoanProductType, which fails.
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        restLoanProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanProductTypeRepository.findAll().size();
        // set the field null
        loanProductType.setProductType(null);

        // Create the LoanProductType, which fails.
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        restLoanProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanProductTypes() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanProductType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getLoanProductType() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get the loanProductType
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, loanProductType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanProductType.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE))
            .andExpect(jsonPath("$.productTypeDescription").value(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getLoanProductTypesByIdFiltering() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        Long id = loanProductType.getId();

        defaultLoanProductTypeShouldBeFound("id.equals=" + id);
        defaultLoanProductTypeShouldNotBeFound("id.notEquals=" + id);

        defaultLoanProductTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanProductTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanProductTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanProductTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode equals to DEFAULT_PRODUCT_CODE
        defaultLoanProductTypeShouldBeFound("productCode.equals=" + DEFAULT_PRODUCT_CODE);

        // Get all the loanProductTypeList where productCode equals to UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldNotBeFound("productCode.equals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode not equals to DEFAULT_PRODUCT_CODE
        defaultLoanProductTypeShouldNotBeFound("productCode.notEquals=" + DEFAULT_PRODUCT_CODE);

        // Get all the loanProductTypeList where productCode not equals to UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldBeFound("productCode.notEquals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode in DEFAULT_PRODUCT_CODE or UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldBeFound("productCode.in=" + DEFAULT_PRODUCT_CODE + "," + UPDATED_PRODUCT_CODE);

        // Get all the loanProductTypeList where productCode equals to UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldNotBeFound("productCode.in=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode is not null
        defaultLoanProductTypeShouldBeFound("productCode.specified=true");

        // Get all the loanProductTypeList where productCode is null
        defaultLoanProductTypeShouldNotBeFound("productCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeContainsSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode contains DEFAULT_PRODUCT_CODE
        defaultLoanProductTypeShouldBeFound("productCode.contains=" + DEFAULT_PRODUCT_CODE);

        // Get all the loanProductTypeList where productCode contains UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldNotBeFound("productCode.contains=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productCode does not contain DEFAULT_PRODUCT_CODE
        defaultLoanProductTypeShouldNotBeFound("productCode.doesNotContain=" + DEFAULT_PRODUCT_CODE);

        // Get all the loanProductTypeList where productCode does not contain UPDATED_PRODUCT_CODE
        defaultLoanProductTypeShouldBeFound("productCode.doesNotContain=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType equals to DEFAULT_PRODUCT_TYPE
        defaultLoanProductTypeShouldBeFound("productType.equals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the loanProductTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldNotBeFound("productType.equals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType not equals to DEFAULT_PRODUCT_TYPE
        defaultLoanProductTypeShouldNotBeFound("productType.notEquals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the loanProductTypeList where productType not equals to UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldBeFound("productType.notEquals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType in DEFAULT_PRODUCT_TYPE or UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldBeFound("productType.in=" + DEFAULT_PRODUCT_TYPE + "," + UPDATED_PRODUCT_TYPE);

        // Get all the loanProductTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldNotBeFound("productType.in=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType is not null
        defaultLoanProductTypeShouldBeFound("productType.specified=true");

        // Get all the loanProductTypeList where productType is null
        defaultLoanProductTypeShouldNotBeFound("productType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeContainsSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType contains DEFAULT_PRODUCT_TYPE
        defaultLoanProductTypeShouldBeFound("productType.contains=" + DEFAULT_PRODUCT_TYPE);

        // Get all the loanProductTypeList where productType contains UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldNotBeFound("productType.contains=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanProductTypesByProductTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        // Get all the loanProductTypeList where productType does not contain DEFAULT_PRODUCT_TYPE
        defaultLoanProductTypeShouldNotBeFound("productType.doesNotContain=" + DEFAULT_PRODUCT_TYPE);

        // Get all the loanProductTypeList where productType does not contain UPDATED_PRODUCT_TYPE
        defaultLoanProductTypeShouldBeFound("productType.doesNotContain=" + UPDATED_PRODUCT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanProductTypeShouldBeFound(String filter) throws Exception {
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanProductType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanProductTypeShouldNotBeFound(String filter) throws Exception {
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanProductType() throws Exception {
        // Get the loanProductType
        restLoanProductTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanProductType() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();

        // Update the loanProductType
        LoanProductType updatedLoanProductType = loanProductTypeRepository.findById(loanProductType.getId()).get();
        // Disconnect from session so that the updates on updatedLoanProductType are not directly saved in db
        em.detach(updatedLoanProductType);
        updatedLoanProductType
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(updatedLoanProductType);

        restLoanProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanProductTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanProductType testLoanProductType = loanProductTypeList.get(loanProductTypeList.size() - 1);
        assertThat(testLoanProductType.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testLoanProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testLoanProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository).save(testLoanProductType);
    }

    @Test
    @Transactional
    void putNonExistingLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanProductTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void partialUpdateLoanProductTypeWithPatch() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();

        // Update the loanProductType using partial update
        LoanProductType partialUpdatedLoanProductType = new LoanProductType();
        partialUpdatedLoanProductType.setId(loanProductType.getId());

        partialUpdatedLoanProductType.productType(UPDATED_PRODUCT_TYPE).productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        restLoanProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanProductType))
            )
            .andExpect(status().isOk());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanProductType testLoanProductType = loanProductTypeList.get(loanProductTypeList.size() - 1);
        assertThat(testLoanProductType.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testLoanProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testLoanProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLoanProductTypeWithPatch() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();

        // Update the loanProductType using partial update
        LoanProductType partialUpdatedLoanProductType = new LoanProductType();
        partialUpdatedLoanProductType.setId(loanProductType.getId());

        partialUpdatedLoanProductType
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        restLoanProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanProductType))
            )
            .andExpect(status().isOk());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanProductType testLoanProductType = loanProductTypeList.get(loanProductTypeList.size() - 1);
        assertThat(testLoanProductType.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testLoanProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testLoanProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanProductTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanProductType() throws Exception {
        int databaseSizeBeforeUpdate = loanProductTypeRepository.findAll().size();
        loanProductType.setId(count.incrementAndGet());

        // Create the LoanProductType
        LoanProductTypeDTO loanProductTypeDTO = loanProductTypeMapper.toDto(loanProductType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanProductTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanProductType in the database
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(0)).save(loanProductType);
    }

    @Test
    @Transactional
    void deleteLoanProductType() throws Exception {
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);

        int databaseSizeBeforeDelete = loanProductTypeRepository.findAll().size();

        // Delete the loanProductType
        restLoanProductTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanProductType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanProductType> loanProductTypeList = loanProductTypeRepository.findAll();
        assertThat(loanProductTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanProductType in Elasticsearch
        verify(mockLoanProductTypeSearchRepository, times(1)).deleteById(loanProductType.getId());
    }

    @Test
    @Transactional
    void searchLoanProductType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanProductTypeRepository.saveAndFlush(loanProductType);
        when(mockLoanProductTypeSearchRepository.search("id:" + loanProductType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanProductType), PageRequest.of(0, 1), 1));

        // Search the loanProductType
        restLoanProductTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanProductType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanProductType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));
    }
}
