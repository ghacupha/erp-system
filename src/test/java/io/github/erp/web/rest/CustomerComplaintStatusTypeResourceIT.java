package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.CustomerComplaintStatusType;
import io.github.erp.repository.CustomerComplaintStatusTypeRepository;
import io.github.erp.repository.search.CustomerComplaintStatusTypeSearchRepository;
import io.github.erp.service.criteria.CustomerComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CustomerComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CustomerComplaintStatusTypeMapper;
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
 * Integration tests for the {@link CustomerComplaintStatusTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerComplaintStatusTypeResourceIT {

    private static final String DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-complaint-status-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/customer-complaint-status-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository;

    @Autowired
    private CustomerComplaintStatusTypeMapper customerComplaintStatusTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CustomerComplaintStatusTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerComplaintStatusTypeSearchRepository mockCustomerComplaintStatusTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerComplaintStatusTypeMockMvc;

    private CustomerComplaintStatusType customerComplaintStatusType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerComplaintStatusType createEntity(EntityManager em) {
        CustomerComplaintStatusType customerComplaintStatusType = new CustomerComplaintStatusType()
            .customerComplaintStatusTypeCode(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)
            .customerComplaintStatusType(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE)
            .customerComplaintStatusTypeDetails(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);
        return customerComplaintStatusType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerComplaintStatusType createUpdatedEntity(EntityManager em) {
        CustomerComplaintStatusType customerComplaintStatusType = new CustomerComplaintStatusType()
            .customerComplaintStatusTypeCode(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)
            .customerComplaintStatusType(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE)
            .customerComplaintStatusTypeDetails(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);
        return customerComplaintStatusType;
    }

    @BeforeEach
    public void initTest() {
        customerComplaintStatusType = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeCreate = customerComplaintStatusTypeRepository.findAll().size();
        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerComplaintStatusType testCustomerComplaintStatusType = customerComplaintStatusTypeList.get(
            customerComplaintStatusTypeList.size() - 1
        );
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeCode())
            .isEqualTo(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusType()).isEqualTo(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeDetails())
            .isEqualTo(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(1)).save(testCustomerComplaintStatusType);
    }

    @Test
    @Transactional
    void createCustomerComplaintStatusTypeWithExistingId() throws Exception {
        // Create the CustomerComplaintStatusType with an existing ID
        customerComplaintStatusType.setId(1L);
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        int databaseSizeBeforeCreate = customerComplaintStatusTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void checkCustomerComplaintStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerComplaintStatusTypeRepository.findAll().size();
        // set the field null
        customerComplaintStatusType.setCustomerComplaintStatusTypeCode(null);

        // Create the CustomerComplaintStatusType, which fails.
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        restCustomerComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerComplaintStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerComplaintStatusTypeRepository.findAll().size();
        // set the field null
        customerComplaintStatusType.setCustomerComplaintStatusType(null);

        // Create the CustomerComplaintStatusType, which fails.
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        restCustomerComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypes() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerComplaintStatusTypeCode").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerComplaintStatusType").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].customerComplaintStatusTypeDetails")
                    .value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getCustomerComplaintStatusType() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get the customerComplaintStatusType
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, customerComplaintStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerComplaintStatusType.getId().intValue()))
            .andExpect(jsonPath("$.customerComplaintStatusTypeCode").value(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.customerComplaintStatusType").value(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE))
            .andExpect(jsonPath("$.customerComplaintStatusTypeDetails").value(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCustomerComplaintStatusTypesByIdFiltering() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        Long id = customerComplaintStatusType.getId();

        defaultCustomerComplaintStatusTypeShouldBeFound("id.equals=" + id);
        defaultCustomerComplaintStatusTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerComplaintStatusTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerComplaintStatusTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerComplaintStatusTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerComplaintStatusTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode equals to DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusTypeCode.equals=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusTypeCode.equals=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode not equals to DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusTypeCode.notEquals=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode not equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusTypeCode.notEquals=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode in DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE or UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusTypeCode.in=" +
            DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE +
            "," +
            UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusTypeCode.in=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode is not null
        defaultCustomerComplaintStatusTypeShouldBeFound("customerComplaintStatusTypeCode.specified=true");

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode is null
        defaultCustomerComplaintStatusTypeShouldNotBeFound("customerComplaintStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode contains DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusTypeCode.contains=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode contains UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusTypeCode.contains=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode does not contain DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusTypeCode.doesNotContain=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusTypeCode does not contain UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusTypeCode.doesNotContain=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType equals to DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldBeFound("customerComplaintStatusType.equals=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldNotBeFound("customerComplaintStatusType.equals=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType not equals to DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusType.notEquals=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType not equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldBeFound("customerComplaintStatusType.notEquals=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType in DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE or UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusType.in=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE + "," + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType equals to UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldNotBeFound("customerComplaintStatusType.in=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType is not null
        defaultCustomerComplaintStatusTypeShouldBeFound("customerComplaintStatusType.specified=true");

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType is null
        defaultCustomerComplaintStatusTypeShouldNotBeFound("customerComplaintStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType contains DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldBeFound("customerComplaintStatusType.contains=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType contains UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusType.contains=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCustomerComplaintStatusTypesByCustomerComplaintStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType does not contain DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldNotBeFound(
            "customerComplaintStatusType.doesNotContain=" + DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE
        );

        // Get all the customerComplaintStatusTypeList where customerComplaintStatusType does not contain UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        defaultCustomerComplaintStatusTypeShouldBeFound(
            "customerComplaintStatusType.doesNotContain=" + UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerComplaintStatusTypeShouldBeFound(String filter) throws Exception {
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerComplaintStatusTypeCode").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerComplaintStatusType").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].customerComplaintStatusTypeDetails")
                    .value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerComplaintStatusTypeShouldNotBeFound(String filter) throws Exception {
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerComplaintStatusType() throws Exception {
        // Get the customerComplaintStatusType
        restCustomerComplaintStatusTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomerComplaintStatusType() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();

        // Update the customerComplaintStatusType
        CustomerComplaintStatusType updatedCustomerComplaintStatusType = customerComplaintStatusTypeRepository
            .findById(customerComplaintStatusType.getId())
            .get();
        // Disconnect from session so that the updates on updatedCustomerComplaintStatusType are not directly saved in db
        em.detach(updatedCustomerComplaintStatusType);
        updatedCustomerComplaintStatusType
            .customerComplaintStatusTypeCode(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)
            .customerComplaintStatusType(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE)
            .customerComplaintStatusTypeDetails(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            updatedCustomerComplaintStatusType
        );

        restCustomerComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerComplaintStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerComplaintStatusType testCustomerComplaintStatusType = customerComplaintStatusTypeList.get(
            customerComplaintStatusTypeList.size() - 1
        );
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeCode())
            .isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusType()).isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeDetails())
            .isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository).save(testCustomerComplaintStatusType);
    }

    @Test
    @Transactional
    void putNonExistingCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerComplaintStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void partialUpdateCustomerComplaintStatusTypeWithPatch() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();

        // Update the customerComplaintStatusType using partial update
        CustomerComplaintStatusType partialUpdatedCustomerComplaintStatusType = new CustomerComplaintStatusType();
        partialUpdatedCustomerComplaintStatusType.setId(customerComplaintStatusType.getId());

        partialUpdatedCustomerComplaintStatusType
            .customerComplaintStatusTypeCode(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)
            .customerComplaintStatusType(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);

        restCustomerComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerComplaintStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerComplaintStatusType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerComplaintStatusType testCustomerComplaintStatusType = customerComplaintStatusTypeList.get(
            customerComplaintStatusTypeList.size() - 1
        );
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeCode())
            .isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusType()).isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeDetails())
            .isEqualTo(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCustomerComplaintStatusTypeWithPatch() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();

        // Update the customerComplaintStatusType using partial update
        CustomerComplaintStatusType partialUpdatedCustomerComplaintStatusType = new CustomerComplaintStatusType();
        partialUpdatedCustomerComplaintStatusType.setId(customerComplaintStatusType.getId());

        partialUpdatedCustomerComplaintStatusType
            .customerComplaintStatusTypeCode(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)
            .customerComplaintStatusType(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE)
            .customerComplaintStatusTypeDetails(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);

        restCustomerComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerComplaintStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerComplaintStatusType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerComplaintStatusType testCustomerComplaintStatusType = customerComplaintStatusTypeList.get(
            customerComplaintStatusTypeList.size() - 1
        );
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeCode())
            .isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusType()).isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE);
        assertThat(testCustomerComplaintStatusType.getCustomerComplaintStatusTypeDetails())
            .isEqualTo(UPDATED_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerComplaintStatusTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = customerComplaintStatusTypeRepository.findAll().size();
        customerComplaintStatusType.setId(count.incrementAndGet());

        // Create the CustomerComplaintStatusType
        CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO = customerComplaintStatusTypeMapper.toDto(
            customerComplaintStatusType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerComplaintStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerComplaintStatusType in the database
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(0)).save(customerComplaintStatusType);
    }

    @Test
    @Transactional
    void deleteCustomerComplaintStatusType() throws Exception {
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);

        int databaseSizeBeforeDelete = customerComplaintStatusTypeRepository.findAll().size();

        // Delete the customerComplaintStatusType
        restCustomerComplaintStatusTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerComplaintStatusType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerComplaintStatusType> customerComplaintStatusTypeList = customerComplaintStatusTypeRepository.findAll();
        assertThat(customerComplaintStatusTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerComplaintStatusType in Elasticsearch
        verify(mockCustomerComplaintStatusTypeSearchRepository, times(1)).deleteById(customerComplaintStatusType.getId());
    }

    @Test
    @Transactional
    void searchCustomerComplaintStatusType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        customerComplaintStatusTypeRepository.saveAndFlush(customerComplaintStatusType);
        when(mockCustomerComplaintStatusTypeSearchRepository.search("id:" + customerComplaintStatusType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerComplaintStatusType), PageRequest.of(0, 1), 1));

        // Search the customerComplaintStatusType
        restCustomerComplaintStatusTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + customerComplaintStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerComplaintStatusTypeCode").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].customerComplaintStatusType").value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].customerComplaintStatusTypeDetails")
                    .value(hasItem(DEFAULT_CUSTOMER_COMPLAINT_STATUS_TYPE_DETAILS.toString()))
            );
    }
}
