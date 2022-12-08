package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.CustomerIDDocumentType;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.CustomerIDDocumentTypeRepository;
import io.github.erp.repository.search.CustomerIDDocumentTypeSearchRepository;
import io.github.erp.service.CustomerIDDocumentTypeService;
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
import io.github.erp.service.mapper.CustomerIDDocumentTypeMapper;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
 * Integration tests for the {@link CustomerIDDocumentTypeResourceDev} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class CustomerIDDocumentTypeResourceIT {

    private static final String DEFAULT_DOCUMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/customer-id-document-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/customer-id-document-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository;

    @Mock
    private CustomerIDDocumentTypeRepository customerIDDocumentTypeRepositoryMock;

    @Autowired
    private CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper;

    @Mock
    private CustomerIDDocumentTypeService customerIDDocumentTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CustomerIDDocumentTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerIDDocumentTypeSearchRepository mockCustomerIDDocumentTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerIDDocumentTypeMockMvc;

    private CustomerIDDocumentType customerIDDocumentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerIDDocumentType createEntity(EntityManager em) {
        CustomerIDDocumentType customerIDDocumentType = new CustomerIDDocumentType()
            .documentCode(DEFAULT_DOCUMENT_CODE)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentTypeDescription(DEFAULT_DOCUMENT_TYPE_DESCRIPTION);
        return customerIDDocumentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerIDDocumentType createUpdatedEntity(EntityManager em) {
        CustomerIDDocumentType customerIDDocumentType = new CustomerIDDocumentType()
            .documentCode(UPDATED_DOCUMENT_CODE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentTypeDescription(UPDATED_DOCUMENT_TYPE_DESCRIPTION);
        return customerIDDocumentType;
    }

    @BeforeEach
    public void initTest() {
        customerIDDocumentType = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeCreate = customerIDDocumentTypeRepository.findAll().size();
        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);
        restCustomerIDDocumentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerIDDocumentType testCustomerIDDocumentType = customerIDDocumentTypeList.get(customerIDDocumentTypeList.size() - 1);
        assertThat(testCustomerIDDocumentType.getDocumentCode()).isEqualTo(DEFAULT_DOCUMENT_CODE);
        assertThat(testCustomerIDDocumentType.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testCustomerIDDocumentType.getDocumentTypeDescription()).isEqualTo(DEFAULT_DOCUMENT_TYPE_DESCRIPTION);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(1)).save(testCustomerIDDocumentType);
    }

    @Test
    @Transactional
    void createCustomerIDDocumentTypeWithExistingId() throws Exception {
        // Create the CustomerIDDocumentType with an existing ID
        customerIDDocumentType.setId(1L);
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        int databaseSizeBeforeCreate = customerIDDocumentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerIDDocumentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void checkDocumentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerIDDocumentTypeRepository.findAll().size();
        // set the field null
        customerIDDocumentType.setDocumentCode(null);

        // Create the CustomerIDDocumentType, which fails.
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        restCustomerIDDocumentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerIDDocumentTypeRepository.findAll().size();
        // set the field null
        customerIDDocumentType.setDocumentType(null);

        // Create the CustomerIDDocumentType, which fails.
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        restCustomerIDDocumentTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypes() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIDDocumentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentCode").value(hasItem(DEFAULT_DOCUMENT_CODE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentTypeDescription").value(hasItem(DEFAULT_DOCUMENT_TYPE_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerIDDocumentTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerIDDocumentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerIDDocumentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerIDDocumentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerIDDocumentTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerIDDocumentTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerIDDocumentTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerIDDocumentTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCustomerIDDocumentType() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get the customerIDDocumentType
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, customerIDDocumentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerIDDocumentType.getId().intValue()))
            .andExpect(jsonPath("$.documentCode").value(DEFAULT_DOCUMENT_CODE))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.documentTypeDescription").value(DEFAULT_DOCUMENT_TYPE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCustomerIDDocumentTypesByIdFiltering() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        Long id = customerIDDocumentType.getId();

        defaultCustomerIDDocumentTypeShouldBeFound("id.equals=" + id);
        defaultCustomerIDDocumentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerIDDocumentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerIDDocumentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerIDDocumentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerIDDocumentTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode equals to DEFAULT_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.equals=" + DEFAULT_DOCUMENT_CODE);

        // Get all the customerIDDocumentTypeList where documentCode equals to UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.equals=" + UPDATED_DOCUMENT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode not equals to DEFAULT_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.notEquals=" + DEFAULT_DOCUMENT_CODE);

        // Get all the customerIDDocumentTypeList where documentCode not equals to UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.notEquals=" + UPDATED_DOCUMENT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode in DEFAULT_DOCUMENT_CODE or UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.in=" + DEFAULT_DOCUMENT_CODE + "," + UPDATED_DOCUMENT_CODE);

        // Get all the customerIDDocumentTypeList where documentCode equals to UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.in=" + UPDATED_DOCUMENT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode is not null
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.specified=true");

        // Get all the customerIDDocumentTypeList where documentCode is null
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode contains DEFAULT_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.contains=" + DEFAULT_DOCUMENT_CODE);

        // Get all the customerIDDocumentTypeList where documentCode contains UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.contains=" + UPDATED_DOCUMENT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentCode does not contain DEFAULT_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentCode.doesNotContain=" + DEFAULT_DOCUMENT_CODE);

        // Get all the customerIDDocumentTypeList where documentCode does not contain UPDATED_DOCUMENT_CODE
        defaultCustomerIDDocumentTypeShouldBeFound("documentCode.doesNotContain=" + UPDATED_DOCUMENT_CODE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType equals to DEFAULT_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.equals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the customerIDDocumentTypeList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType not equals to DEFAULT_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.notEquals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the customerIDDocumentTypeList where documentType not equals to UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.notEquals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType in DEFAULT_DOCUMENT_TYPE or UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE);

        // Get all the customerIDDocumentTypeList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.in=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType is not null
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.specified=true");

        // Get all the customerIDDocumentTypeList where documentType is null
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType contains DEFAULT_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.contains=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the customerIDDocumentTypeList where documentType contains UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.contains=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentType does not contain DEFAULT_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentType.doesNotContain=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the customerIDDocumentTypeList where documentType does not contain UPDATED_DOCUMENT_TYPE
        defaultCustomerIDDocumentTypeShouldBeFound("documentType.doesNotContain=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription equals to DEFAULT_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldBeFound("documentTypeDescription.equals=" + DEFAULT_DOCUMENT_TYPE_DESCRIPTION);

        // Get all the customerIDDocumentTypeList where documentTypeDescription equals to UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.equals=" + UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription not equals to DEFAULT_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.notEquals=" + DEFAULT_DOCUMENT_TYPE_DESCRIPTION);

        // Get all the customerIDDocumentTypeList where documentTypeDescription not equals to UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldBeFound("documentTypeDescription.notEquals=" + UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription in DEFAULT_DOCUMENT_TYPE_DESCRIPTION or UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldBeFound(
            "documentTypeDescription.in=" + DEFAULT_DOCUMENT_TYPE_DESCRIPTION + "," + UPDATED_DOCUMENT_TYPE_DESCRIPTION
        );

        // Get all the customerIDDocumentTypeList where documentTypeDescription equals to UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.in=" + UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription is not null
        defaultCustomerIDDocumentTypeShouldBeFound("documentTypeDescription.specified=true");

        // Get all the customerIDDocumentTypeList where documentTypeDescription is null
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription contains DEFAULT_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldBeFound("documentTypeDescription.contains=" + DEFAULT_DOCUMENT_TYPE_DESCRIPTION);

        // Get all the customerIDDocumentTypeList where documentTypeDescription contains UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.contains=" + UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByDocumentTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        // Get all the customerIDDocumentTypeList where documentTypeDescription does not contain DEFAULT_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldNotBeFound("documentTypeDescription.doesNotContain=" + DEFAULT_DOCUMENT_TYPE_DESCRIPTION);

        // Get all the customerIDDocumentTypeList where documentTypeDescription does not contain UPDATED_DOCUMENT_TYPE_DESCRIPTION
        defaultCustomerIDDocumentTypeShouldBeFound("documentTypeDescription.doesNotContain=" + UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCustomerIDDocumentTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        customerIDDocumentType.addPlaceholder(placeholder);
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);
        Long placeholderId = placeholder.getId();

        // Get all the customerIDDocumentTypeList where placeholder equals to placeholderId
        defaultCustomerIDDocumentTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the customerIDDocumentTypeList where placeholder equals to (placeholderId + 1)
        defaultCustomerIDDocumentTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerIDDocumentTypeShouldBeFound(String filter) throws Exception {
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIDDocumentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentCode").value(hasItem(DEFAULT_DOCUMENT_CODE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentTypeDescription").value(hasItem(DEFAULT_DOCUMENT_TYPE_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerIDDocumentTypeShouldNotBeFound(String filter) throws Exception {
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerIDDocumentType() throws Exception {
        // Get the customerIDDocumentType
        restCustomerIDDocumentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomerIDDocumentType() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();

        // Update the customerIDDocumentType
        CustomerIDDocumentType updatedCustomerIDDocumentType = customerIDDocumentTypeRepository
            .findById(customerIDDocumentType.getId())
            .get();
        // Disconnect from session so that the updates on updatedCustomerIDDocumentType are not directly saved in db
        em.detach(updatedCustomerIDDocumentType);
        updatedCustomerIDDocumentType
            .documentCode(UPDATED_DOCUMENT_CODE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentTypeDescription(UPDATED_DOCUMENT_TYPE_DESCRIPTION);
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(updatedCustomerIDDocumentType);

        restCustomerIDDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerIDDocumentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerIDDocumentType testCustomerIDDocumentType = customerIDDocumentTypeList.get(customerIDDocumentTypeList.size() - 1);
        assertThat(testCustomerIDDocumentType.getDocumentCode()).isEqualTo(UPDATED_DOCUMENT_CODE);
        assertThat(testCustomerIDDocumentType.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testCustomerIDDocumentType.getDocumentTypeDescription()).isEqualTo(UPDATED_DOCUMENT_TYPE_DESCRIPTION);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository).save(testCustomerIDDocumentType);
    }

    @Test
    @Transactional
    void putNonExistingCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerIDDocumentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void partialUpdateCustomerIDDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();

        // Update the customerIDDocumentType using partial update
        CustomerIDDocumentType partialUpdatedCustomerIDDocumentType = new CustomerIDDocumentType();
        partialUpdatedCustomerIDDocumentType.setId(customerIDDocumentType.getId());

        partialUpdatedCustomerIDDocumentType.documentType(UPDATED_DOCUMENT_TYPE);

        restCustomerIDDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerIDDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerIDDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerIDDocumentType testCustomerIDDocumentType = customerIDDocumentTypeList.get(customerIDDocumentTypeList.size() - 1);
        assertThat(testCustomerIDDocumentType.getDocumentCode()).isEqualTo(DEFAULT_DOCUMENT_CODE);
        assertThat(testCustomerIDDocumentType.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testCustomerIDDocumentType.getDocumentTypeDescription()).isEqualTo(DEFAULT_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCustomerIDDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();

        // Update the customerIDDocumentType using partial update
        CustomerIDDocumentType partialUpdatedCustomerIDDocumentType = new CustomerIDDocumentType();
        partialUpdatedCustomerIDDocumentType.setId(customerIDDocumentType.getId());

        partialUpdatedCustomerIDDocumentType
            .documentCode(UPDATED_DOCUMENT_CODE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentTypeDescription(UPDATED_DOCUMENT_TYPE_DESCRIPTION);

        restCustomerIDDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerIDDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerIDDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomerIDDocumentType testCustomerIDDocumentType = customerIDDocumentTypeList.get(customerIDDocumentTypeList.size() - 1);
        assertThat(testCustomerIDDocumentType.getDocumentCode()).isEqualTo(UPDATED_DOCUMENT_CODE);
        assertThat(testCustomerIDDocumentType.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testCustomerIDDocumentType.getDocumentTypeDescription()).isEqualTo(UPDATED_DOCUMENT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerIDDocumentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerIDDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = customerIDDocumentTypeRepository.findAll().size();
        customerIDDocumentType.setId(count.incrementAndGet());

        // Create the CustomerIDDocumentType
        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = customerIDDocumentTypeMapper.toDto(customerIDDocumentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIDDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerIDDocumentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerIDDocumentType in the database
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(0)).save(customerIDDocumentType);
    }

    @Test
    @Transactional
    void deleteCustomerIDDocumentType() throws Exception {
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);

        int databaseSizeBeforeDelete = customerIDDocumentTypeRepository.findAll().size();

        // Delete the customerIDDocumentType
        restCustomerIDDocumentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerIDDocumentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerIDDocumentType> customerIDDocumentTypeList = customerIDDocumentTypeRepository.findAll();
        assertThat(customerIDDocumentTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerIDDocumentType in Elasticsearch
        verify(mockCustomerIDDocumentTypeSearchRepository, times(1)).deleteById(customerIDDocumentType.getId());
    }

    @Test
    @Transactional
    void searchCustomerIDDocumentType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        customerIDDocumentTypeRepository.saveAndFlush(customerIDDocumentType);
        when(mockCustomerIDDocumentTypeSearchRepository.search("id:" + customerIDDocumentType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerIDDocumentType), PageRequest.of(0, 1), 1));

        // Search the customerIDDocumentType
        restCustomerIDDocumentTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + customerIDDocumentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIDDocumentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentCode").value(hasItem(DEFAULT_DOCUMENT_CODE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentTypeDescription").value(hasItem(DEFAULT_DOCUMENT_TYPE_DESCRIPTION)));
    }
}
