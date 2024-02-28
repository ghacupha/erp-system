package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.ProductType;
import io.github.erp.repository.ProductTypeRepository;
import io.github.erp.repository.search.ProductTypeSearchRepository;
import io.github.erp.service.dto.ProductTypeDTO;
import io.github.erp.service.mapper.ProductTypeMapper;
import io.github.erp.web.rest.ProductTypeResource;
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
 * Integration tests for the {@link ProductTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class ProductTypeResourceIT {

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/product-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/product-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ProductTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductTypeSearchRepository mockProductTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTypeMockMvc;

    private ProductType productType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createEntity(EntityManager em) {
        ProductType productType = new ProductType()
            .productCode(DEFAULT_PRODUCT_CODE)
            .productType(DEFAULT_PRODUCT_TYPE)
            .productTypeDescription(DEFAULT_PRODUCT_TYPE_DESCRIPTION);
        return productType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createUpdatedEntity(EntityManager em) {
        ProductType productType = new ProductType()
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);
        return productType;
    }

    @BeforeEach
    public void initTest() {
        productType = createEntity(em);
    }

    @Test
    @Transactional
    void createProductType() throws Exception {
        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();
        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);
        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProductType.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(DEFAULT_PRODUCT_TYPE_DESCRIPTION);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(1)).save(testProductType);
    }

    @Test
    @Transactional
    void createProductTypeWithExistingId() throws Exception {
        // Create the ProductType with an existing ID
        productType.setId(1L);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void checkProductCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTypeRepository.findAll().size();
        // set the field null
        productType.setProductCode(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductTypes() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productType.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE))
            .andExpect(jsonPath("$.productTypeDescription").value(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getProductTypesByIdFiltering() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        Long id = productType.getId();

        defaultProductTypeShouldBeFound("id.equals=" + id);
        defaultProductTypeShouldNotBeFound("id.notEquals=" + id);

        defaultProductTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultProductTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode equals to DEFAULT_PRODUCT_CODE
        defaultProductTypeShouldBeFound("productCode.equals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productTypeList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductTypeShouldNotBeFound("productCode.equals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode not equals to DEFAULT_PRODUCT_CODE
        defaultProductTypeShouldNotBeFound("productCode.notEquals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productTypeList where productCode not equals to UPDATED_PRODUCT_CODE
        defaultProductTypeShouldBeFound("productCode.notEquals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode in DEFAULT_PRODUCT_CODE or UPDATED_PRODUCT_CODE
        defaultProductTypeShouldBeFound("productCode.in=" + DEFAULT_PRODUCT_CODE + "," + UPDATED_PRODUCT_CODE);

        // Get all the productTypeList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductTypeShouldNotBeFound("productCode.in=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode is not null
        defaultProductTypeShouldBeFound("productCode.specified=true");

        // Get all the productTypeList where productCode is null
        defaultProductTypeShouldNotBeFound("productCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode contains DEFAULT_PRODUCT_CODE
        defaultProductTypeShouldBeFound("productCode.contains=" + DEFAULT_PRODUCT_CODE);

        // Get all the productTypeList where productCode contains UPDATED_PRODUCT_CODE
        defaultProductTypeShouldNotBeFound("productCode.contains=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productCode does not contain DEFAULT_PRODUCT_CODE
        defaultProductTypeShouldNotBeFound("productCode.doesNotContain=" + DEFAULT_PRODUCT_CODE);

        // Get all the productTypeList where productCode does not contain UPDATED_PRODUCT_CODE
        defaultProductTypeShouldBeFound("productCode.doesNotContain=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType equals to DEFAULT_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.equals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.equals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType not equals to DEFAULT_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.notEquals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productTypeList where productType not equals to UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.notEquals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeIsInShouldWork() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType in DEFAULT_PRODUCT_TYPE or UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.in=" + DEFAULT_PRODUCT_TYPE + "," + UPDATED_PRODUCT_TYPE);

        // Get all the productTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.in=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType is not null
        defaultProductTypeShouldBeFound("productType.specified=true");

        // Get all the productTypeList where productType is null
        defaultProductTypeShouldNotBeFound("productType.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType contains DEFAULT_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.contains=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productTypeList where productType contains UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.contains=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductTypesByProductTypeNotContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType does not contain DEFAULT_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.doesNotContain=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productTypeList where productType does not contain UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.doesNotContain=" + UPDATED_PRODUCT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductTypeShouldBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductTypeShouldNotBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductType() throws Exception {
        // Get the productType
        restProductTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType
        ProductType updatedProductType = productTypeRepository.findById(productType.getId()).get();
        // Disconnect from session so that the updates on updatedProductType are not directly saved in db
        em.detach(updatedProductType);
        updatedProductType
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(updatedProductType);

        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository).save(testProductType);
    }

    @Test
    @Transactional
    void putNonExistingProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void partialUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.productCode(UPDATED_PRODUCT_CODE);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProductType.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(DEFAULT_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType
            .productCode(UPDATED_PRODUCT_CODE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    void deleteProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeDelete = productTypeRepository.findAll().size();

        // Delete the productType
        restProductTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(1)).deleteById(productType.getId());
    }

    @Test
    @Transactional
    void searchProductType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);
        when(mockProductTypeSearchRepository.search("id:" + productType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(productType), PageRequest.of(0, 1), 1));

        // Search the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));
    }
}
