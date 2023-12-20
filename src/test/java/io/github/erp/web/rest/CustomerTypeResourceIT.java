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
import io.github.erp.domain.CustomerType;
import io.github.erp.repository.CustomerTypeRepository;
import io.github.erp.repository.search.CustomerTypeSearchRepository;
import io.github.erp.service.criteria.CustomerTypeCriteria;
import io.github.erp.service.dto.CustomerTypeDTO;
import io.github.erp.service.mapper.CustomerTypeMapper;
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
 * Integration tests for the {@link CustomerTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerTypeResourceIT {

    private static final String DEFAULT_CUSTOMER_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/customer-types";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    @Autowired
    private CustomerTypeMapper customerTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CustomerTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerTypeSearchRepository mockCustomerTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerTypeMockMvc;

    private CustomerType customerType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerType createEntity(EntityManager em) {
        CustomerType customerType = new CustomerType()
            .customerTypeCode(DEFAULT_CUSTOMER_TYPE_CODE)
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .customerTypeDescription(DEFAULT_CUSTOMER_TYPE_DESCRIPTION);
        return customerType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerType createUpdatedEntity(EntityManager em) {
        CustomerType customerType = new CustomerType()
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .customerTypeDescription(UPDATED_CUSTOMER_TYPE_DESCRIPTION);
        return customerType;
    }

    @BeforeEach
    public void initTest() {
        customerType = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerType() throws Exception {
        int databaseSizeBeforeCreate = customerTypeRepository.findAll().size();
        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);
        restCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerType testCustomerType = customerTypeList.get(customerTypeList.size() - 1);
        assertThat(testCustomerType.getCustomerTypeCode()).isEqualTo(DEFAULT_CUSTOMER_TYPE_CODE);
        assertThat(testCustomerType.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCustomerType.getCustomerTypeDescription()).isEqualTo(DEFAULT_CUSTOMER_TYPE_DESCRIPTION);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(1)).save(testCustomerType);
    }

    @Test
    @Transactional
    void createCustomerTypeWithExistingId() throws Exception {
        // Create the CustomerType with an existing ID
        customerType.setId(1L);
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        int databaseSizeBeforeCreate = customerTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void getAllCustomerTypes() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].customerTypeDescription").value(hasItem(DEFAULT_CUSTOMER_TYPE_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCustomerType() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get the customerType
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, customerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerType.getId().intValue()))
            .andExpect(jsonPath("$.customerTypeCode").value(DEFAULT_CUSTOMER_TYPE_CODE))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE))
            .andExpect(jsonPath("$.customerTypeDescription").value(DEFAULT_CUSTOMER_TYPE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCustomerTypesByIdFiltering() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        Long id = customerType.getId();

        defaultCustomerTypeShouldBeFound("id.equals=" + id);
        defaultCustomerTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode equals to DEFAULT_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldBeFound("customerTypeCode.equals=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the customerTypeList where customerTypeCode equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.equals=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode not equals to DEFAULT_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.notEquals=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the customerTypeList where customerTypeCode not equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldBeFound("customerTypeCode.notEquals=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode in DEFAULT_CUSTOMER_TYPE_CODE or UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldBeFound("customerTypeCode.in=" + DEFAULT_CUSTOMER_TYPE_CODE + "," + UPDATED_CUSTOMER_TYPE_CODE);

        // Get all the customerTypeList where customerTypeCode equals to UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.in=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode is not null
        defaultCustomerTypeShouldBeFound("customerTypeCode.specified=true");

        // Get all the customerTypeList where customerTypeCode is null
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode contains DEFAULT_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldBeFound("customerTypeCode.contains=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the customerTypeList where customerTypeCode contains UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.contains=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerTypeCode does not contain DEFAULT_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldNotBeFound("customerTypeCode.doesNotContain=" + DEFAULT_CUSTOMER_TYPE_CODE);

        // Get all the customerTypeList where customerTypeCode does not contain UPDATED_CUSTOMER_TYPE_CODE
        defaultCustomerTypeShouldBeFound("customerTypeCode.doesNotContain=" + UPDATED_CUSTOMER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType equals to DEFAULT_CUSTOMER_TYPE
        defaultCustomerTypeShouldBeFound("customerType.equals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerTypeList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldNotBeFound("customerType.equals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType not equals to DEFAULT_CUSTOMER_TYPE
        defaultCustomerTypeShouldNotBeFound("customerType.notEquals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerTypeList where customerType not equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldBeFound("customerType.notEquals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType in DEFAULT_CUSTOMER_TYPE or UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldBeFound("customerType.in=" + DEFAULT_CUSTOMER_TYPE + "," + UPDATED_CUSTOMER_TYPE);

        // Get all the customerTypeList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldNotBeFound("customerType.in=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType is not null
        defaultCustomerTypeShouldBeFound("customerType.specified=true");

        // Get all the customerTypeList where customerType is null
        defaultCustomerTypeShouldNotBeFound("customerType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeContainsSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType contains DEFAULT_CUSTOMER_TYPE
        defaultCustomerTypeShouldBeFound("customerType.contains=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerTypeList where customerType contains UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldNotBeFound("customerType.contains=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerTypesByCustomerTypeNotContainsSomething() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        // Get all the customerTypeList where customerType does not contain DEFAULT_CUSTOMER_TYPE
        defaultCustomerTypeShouldNotBeFound("customerType.doesNotContain=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerTypeList where customerType does not contain UPDATED_CUSTOMER_TYPE
        defaultCustomerTypeShouldBeFound("customerType.doesNotContain=" + UPDATED_CUSTOMER_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerTypeShouldBeFound(String filter) throws Exception {
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].customerTypeDescription").value(hasItem(DEFAULT_CUSTOMER_TYPE_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerTypeShouldNotBeFound(String filter) throws Exception {
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerType() throws Exception {
        // Get the customerType
        restCustomerTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomerType() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();

        // Update the customerType
        CustomerType updatedCustomerType = customerTypeRepository.findById(customerType.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerType are not directly saved in db
        em.detach(updatedCustomerType);
        updatedCustomerType
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .customerTypeDescription(UPDATED_CUSTOMER_TYPE_DESCRIPTION);
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(updatedCustomerType);

        restCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerType testCustomerType = customerTypeList.get(customerTypeList.size() - 1);
        assertThat(testCustomerType.getCustomerTypeCode()).isEqualTo(UPDATED_CUSTOMER_TYPE_CODE);
        assertThat(testCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomerType.getCustomerTypeDescription()).isEqualTo(UPDATED_CUSTOMER_TYPE_DESCRIPTION);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository).save(testCustomerType);
    }

    @Test
    @Transactional
    void putNonExistingCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void partialUpdateCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();

        // Update the customerType using partial update
        CustomerType partialUpdatedCustomerType = new CustomerType();
        partialUpdatedCustomerType.setId(customerType.getId());

        partialUpdatedCustomerType.customerType(UPDATED_CUSTOMER_TYPE);

        restCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerType testCustomerType = customerTypeList.get(customerTypeList.size() - 1);
        assertThat(testCustomerType.getCustomerTypeCode()).isEqualTo(DEFAULT_CUSTOMER_TYPE_CODE);
        assertThat(testCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomerType.getCustomerTypeDescription()).isEqualTo(DEFAULT_CUSTOMER_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCustomerTypeWithPatch() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();

        // Update the customerType using partial update
        CustomerType partialUpdatedCustomerType = new CustomerType();
        partialUpdatedCustomerType.setId(customerType.getId());

        partialUpdatedCustomerType
            .customerTypeCode(UPDATED_CUSTOMER_TYPE_CODE)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .customerTypeDescription(UPDATED_CUSTOMER_TYPE_DESCRIPTION);

        restCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerType testCustomerType = customerTypeList.get(customerTypeList.size() - 1);
        assertThat(testCustomerType.getCustomerTypeCode()).isEqualTo(UPDATED_CUSTOMER_TYPE_CODE);
        assertThat(testCustomerType.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomerType.getCustomerTypeDescription()).isEqualTo(UPDATED_CUSTOMER_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerType() throws Exception {
        int databaseSizeBeforeUpdate = customerTypeRepository.findAll().size();
        customerType.setId(count.incrementAndGet());

        // Create the CustomerType
        CustomerTypeDTO customerTypeDTO = customerTypeMapper.toDto(customerType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerType in the database
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(0)).save(customerType);
    }

    @Test
    @Transactional
    void deleteCustomerType() throws Exception {
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);

        int databaseSizeBeforeDelete = customerTypeRepository.findAll().size();

        // Delete the customerType
        restCustomerTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        assertThat(customerTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerType in Elasticsearch
        verify(mockCustomerTypeSearchRepository, times(1)).deleteById(customerType.getId());
    }

    @Test
    @Transactional
    void searchCustomerType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        customerTypeRepository.saveAndFlush(customerType);
        when(mockCustomerTypeSearchRepository.search("id:" + customerType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerType), PageRequest.of(0, 1), 1));

        // Search the customerType
        restCustomerTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + customerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerTypeCode").value(hasItem(DEFAULT_CUSTOMER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE)))
            .andExpect(jsonPath("$.[*].customerTypeDescription").value(hasItem(DEFAULT_CUSTOMER_TYPE_DESCRIPTION)));
    }
}
