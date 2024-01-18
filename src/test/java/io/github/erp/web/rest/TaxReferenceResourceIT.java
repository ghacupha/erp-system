package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TaxReference;
import io.github.erp.domain.enumeration.taxReferenceTypes;
import io.github.erp.repository.TaxReferenceRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.service.TaxReferenceService;
import io.github.erp.service.criteria.TaxReferenceCriteria;
import io.github.erp.service.dto.TaxReferenceDTO;
import io.github.erp.service.mapper.TaxReferenceMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link TaxReferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxReferenceResourceIT {

    private static final String DEFAULT_TAX_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TAX_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TAX_PERCENTAGE = 1D;
    private static final Double UPDATED_TAX_PERCENTAGE = 2D;
    private static final Double SMALLER_TAX_PERCENTAGE = 1D - 1D;

    private static final taxReferenceTypes DEFAULT_TAX_REFERENCE_TYPE = taxReferenceTypes.TELCO_EXCISE_DUTY;
    private static final taxReferenceTypes UPDATED_TAX_REFERENCE_TYPE = taxReferenceTypes.VALUE_ADDED_TAX;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tax-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/tax-references";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxReferenceRepository taxReferenceRepository;

    @Mock
    private TaxReferenceRepository taxReferenceRepositoryMock;

    @Autowired
    private TaxReferenceMapper taxReferenceMapper;

    @Mock
    private TaxReferenceService taxReferenceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TaxReferenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxReferenceSearchRepository mockTaxReferenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxReferenceMockMvc;

    private TaxReference taxReference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxReference createEntity(EntityManager em) {
        TaxReference taxReference = new TaxReference()
            .taxName(DEFAULT_TAX_NAME)
            .taxDescription(DEFAULT_TAX_DESCRIPTION)
            .taxPercentage(DEFAULT_TAX_PERCENTAGE)
            .taxReferenceType(DEFAULT_TAX_REFERENCE_TYPE)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return taxReference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxReference createUpdatedEntity(EntityManager em) {
        TaxReference taxReference = new TaxReference()
            .taxName(UPDATED_TAX_NAME)
            .taxDescription(UPDATED_TAX_DESCRIPTION)
            .taxPercentage(UPDATED_TAX_PERCENTAGE)
            .taxReferenceType(UPDATED_TAX_REFERENCE_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return taxReference;
    }

    @BeforeEach
    public void initTest() {
        taxReference = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxReference() throws Exception {
        int databaseSizeBeforeCreate = taxReferenceRepository.findAll().size();
        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);
        restTaxReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeCreate + 1);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(DEFAULT_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(DEFAULT_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(DEFAULT_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(DEFAULT_TAX_REFERENCE_TYPE);
        assertThat(testTaxReference.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testTaxReference.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(1)).save(testTaxReference);
    }

    @Test
    @Transactional
    void createTaxReferenceWithExistingId() throws Exception {
        // Create the TaxReference with an existing ID
        taxReference.setId(1L);
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        int databaseSizeBeforeCreate = taxReferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void checkTaxPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxReferenceRepository.findAll().size();
        // set the field null
        taxReference.setTaxPercentage(null);

        // Create the TaxReference, which fails.
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        restTaxReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxReferenceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxReferenceRepository.findAll().size();
        // set the field null
        taxReference.setTaxReferenceType(null);

        // Create the TaxReference, which fails.
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        restTaxReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxReferences() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxReferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxReferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxReferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxReferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxReferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxReferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxReferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxReferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTaxReference() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get the taxReference
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, taxReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxReference.getId().intValue()))
            .andExpect(jsonPath("$.taxName").value(DEFAULT_TAX_NAME))
            .andExpect(jsonPath("$.taxDescription").value(DEFAULT_TAX_DESCRIPTION))
            .andExpect(jsonPath("$.taxPercentage").value(DEFAULT_TAX_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.taxReferenceType").value(DEFAULT_TAX_REFERENCE_TYPE.toString()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getTaxReferencesByIdFiltering() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        Long id = taxReference.getId();

        defaultTaxReferenceShouldBeFound("id.equals=" + id);
        defaultTaxReferenceShouldNotBeFound("id.notEquals=" + id);

        defaultTaxReferenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaxReferenceShouldNotBeFound("id.greaterThan=" + id);

        defaultTaxReferenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaxReferenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName equals to DEFAULT_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.equals=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.equals=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName not equals to DEFAULT_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.notEquals=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName not equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.notEquals=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName in DEFAULT_TAX_NAME or UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.in=" + DEFAULT_TAX_NAME + "," + UPDATED_TAX_NAME);

        // Get all the taxReferenceList where taxName equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.in=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName is not null
        defaultTaxReferenceShouldBeFound("taxName.specified=true");

        // Get all the taxReferenceList where taxName is null
        defaultTaxReferenceShouldNotBeFound("taxName.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName contains DEFAULT_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.contains=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName contains UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.contains=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxNameNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName does not contain DEFAULT_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.doesNotContain=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName does not contain UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.doesNotContain=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription equals to DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.equals=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.equals=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription not equals to DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.notEquals=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription not equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.notEquals=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription in DEFAULT_TAX_DESCRIPTION or UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.in=" + DEFAULT_TAX_DESCRIPTION + "," + UPDATED_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.in=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription is not null
        defaultTaxReferenceShouldBeFound("taxDescription.specified=true");

        // Get all the taxReferenceList where taxDescription is null
        defaultTaxReferenceShouldNotBeFound("taxDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription contains DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.contains=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription contains UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.contains=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription does not contain DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.doesNotContain=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription does not contain UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.doesNotContain=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage equals to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.equals=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.equals=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage not equals to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.notEquals=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage not equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.notEquals=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage in DEFAULT_TAX_PERCENTAGE or UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.in=" + DEFAULT_TAX_PERCENTAGE + "," + UPDATED_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.in=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is not null
        defaultTaxReferenceShouldBeFound("taxPercentage.specified=true");

        // Get all the taxReferenceList where taxPercentage is null
        defaultTaxReferenceShouldNotBeFound("taxPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is greater than or equal to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.greaterThanOrEqual=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is greater than or equal to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.greaterThanOrEqual=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is less than or equal to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.lessThanOrEqual=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is less than or equal to SMALLER_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.lessThanOrEqual=" + SMALLER_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is less than DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.lessThan=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is less than UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.lessThan=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is greater than DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.greaterThan=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is greater than SMALLER_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.greaterThan=" + SMALLER_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxReferenceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType equals to DEFAULT_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.equals=" + DEFAULT_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.equals=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxReferenceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType not equals to DEFAULT_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.notEquals=" + DEFAULT_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType not equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.notEquals=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxReferenceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType in DEFAULT_TAX_REFERENCE_TYPE or UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.in=" + DEFAULT_TAX_REFERENCE_TYPE + "," + UPDATED_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.in=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByTaxReferenceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType is not null
        defaultTaxReferenceShouldBeFound("taxReferenceType.specified=true");

        // Get all the taxReferenceList where taxReferenceType is null
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxReferenceList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxReferenceList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the taxReferenceList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken is not null
        defaultTaxReferenceShouldBeFound("fileUploadToken.specified=true");

        // Get all the taxReferenceList where fileUploadToken is null
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxReferenceList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxReferenceList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxReferenceShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultTaxReferenceShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxReferenceList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultTaxReferenceShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxReferenceList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the taxReferenceList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken is not null
        defaultTaxReferenceShouldBeFound("compilationToken.specified=true");

        // Get all the taxReferenceList where compilationToken is null
        defaultTaxReferenceShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultTaxReferenceShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxReferenceList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultTaxReferenceShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxReferenceList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultTaxReferenceShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxReferencesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);
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
        taxReference.addPlaceholder(placeholder);
        taxReferenceRepository.saveAndFlush(taxReference);
        Long placeholderId = placeholder.getId();

        // Get all the taxReferenceList where placeholder equals to placeholderId
        defaultTaxReferenceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the taxReferenceList where placeholder equals to (placeholderId + 1)
        defaultTaxReferenceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxReferenceShouldBeFound(String filter) throws Exception {
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxReferenceShouldNotBeFound(String filter) throws Exception {
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxReferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxReference() throws Exception {
        // Get the taxReference
        restTaxReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaxReference() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();

        // Update the taxReference
        TaxReference updatedTaxReference = taxReferenceRepository.findById(taxReference.getId()).get();
        // Disconnect from session so that the updates on updatedTaxReference are not directly saved in db
        em.detach(updatedTaxReference);
        updatedTaxReference
            .taxName(UPDATED_TAX_NAME)
            .taxDescription(UPDATED_TAX_DESCRIPTION)
            .taxPercentage(UPDATED_TAX_PERCENTAGE)
            .taxReferenceType(UPDATED_TAX_REFERENCE_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(updatedTaxReference);

        restTaxReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxReferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(UPDATED_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(UPDATED_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(UPDATED_TAX_REFERENCE_TYPE);
        assertThat(testTaxReference.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testTaxReference.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository).save(testTaxReference);
    }

    @Test
    @Transactional
    void putNonExistingTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxReferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void partialUpdateTaxReferenceWithPatch() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();

        // Update the taxReference using partial update
        TaxReference partialUpdatedTaxReference = new TaxReference();
        partialUpdatedTaxReference.setId(taxReference.getId());

        partialUpdatedTaxReference
            .taxPercentage(UPDATED_TAX_PERCENTAGE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restTaxReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxReference))
            )
            .andExpect(status().isOk());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(DEFAULT_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(DEFAULT_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(DEFAULT_TAX_REFERENCE_TYPE);
        assertThat(testTaxReference.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testTaxReference.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateTaxReferenceWithPatch() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();

        // Update the taxReference using partial update
        TaxReference partialUpdatedTaxReference = new TaxReference();
        partialUpdatedTaxReference.setId(taxReference.getId());

        partialUpdatedTaxReference
            .taxName(UPDATED_TAX_NAME)
            .taxDescription(UPDATED_TAX_DESCRIPTION)
            .taxPercentage(UPDATED_TAX_PERCENTAGE)
            .taxReferenceType(UPDATED_TAX_REFERENCE_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restTaxReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxReference))
            )
            .andExpect(status().isOk());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(UPDATED_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(UPDATED_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(UPDATED_TAX_REFERENCE_TYPE);
        assertThat(testTaxReference.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testTaxReference.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxReferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();
        taxReference.setId(count.incrementAndGet());

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    void deleteTaxReference() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        int databaseSizeBeforeDelete = taxReferenceRepository.findAll().size();

        // Delete the taxReference
        restTaxReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxReference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(1)).deleteById(taxReference.getId());
    }

    @Test
    @Transactional
    void searchTaxReference() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);
        when(mockTaxReferenceSearchRepository.search("id:" + taxReference.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taxReference), PageRequest.of(0, 1), 1));

        // Search the taxReference
        restTaxReferenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + taxReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
