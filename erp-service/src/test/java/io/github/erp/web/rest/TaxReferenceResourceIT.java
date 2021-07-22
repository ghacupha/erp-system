package io.github.erp.web.rest;

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.TaxReference;
import io.github.erp.repository.TaxReferenceRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.service.TaxReferenceService;
import io.github.erp.service.dto.TaxReferenceDTO;
import io.github.erp.service.mapper.TaxReferenceMapper;
import io.github.erp.service.dto.TaxReferenceCriteria;
import io.github.erp.service.TaxReferenceQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.domain.enumeration.taxReferenceTypes;
/**
 * Integration tests for the {@link TaxReferenceResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TaxReferenceResourceIT {

    private static final String DEFAULT_TAX_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TAX_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TAX_PERCENTAGE = 1D;
    private static final Double UPDATED_TAX_PERCENTAGE = 2D;
    private static final Double SMALLER_TAX_PERCENTAGE = 1D - 1D;

    private static final taxReferenceTypes DEFAULT_TAX_REFERENCE_TYPE = taxReferenceTypes.TELCO_EXCISE_DUTY;
    private static final taxReferenceTypes UPDATED_TAX_REFERENCE_TYPE = taxReferenceTypes.VALUE_ADDED_TAX;

    @Autowired
    private TaxReferenceRepository taxReferenceRepository;

    @Autowired
    private TaxReferenceMapper taxReferenceMapper;

    @Autowired
    private TaxReferenceService taxReferenceService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TaxReferenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxReferenceSearchRepository mockTaxReferenceSearchRepository;

    @Autowired
    private TaxReferenceQueryService taxReferenceQueryService;

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
            .taxReferenceType(DEFAULT_TAX_REFERENCE_TYPE);
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
            .taxReferenceType(UPDATED_TAX_REFERENCE_TYPE);
        return taxReference;
    }

    @BeforeEach
    public void initTest() {
        taxReference = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxReference() throws Exception {
        int databaseSizeBeforeCreate = taxReferenceRepository.findAll().size();
        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);
        restTaxReferenceMockMvc.perform(post("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeCreate + 1);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(DEFAULT_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(DEFAULT_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(DEFAULT_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(DEFAULT_TAX_REFERENCE_TYPE);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(1)).save(testTaxReference);
    }

    @Test
    @Transactional
    public void createTaxReferenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxReferenceRepository.findAll().size();

        // Create the TaxReference with an existing ID
        taxReference.setId(1L);
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxReferenceMockMvc.perform(post("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }


    @Test
    @Transactional
    public void checkTaxPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxReferenceRepository.findAll().size();
        // set the field null
        taxReference.setTaxPercentage(null);

        // Create the TaxReference, which fails.
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);


        restTaxReferenceMockMvc.perform(post("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isBadRequest());

        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxReferenceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxReferenceRepository.findAll().size();
        // set the field null
        taxReference.setTaxReferenceType(null);

        // Create the TaxReference, which fails.
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);


        restTaxReferenceMockMvc.perform(post("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isBadRequest());

        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaxReferences() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList
        restTaxReferenceMockMvc.perform(get("/api/tax-references?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getTaxReference() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get the taxReference
        restTaxReferenceMockMvc.perform(get("/api/tax-references/{id}", taxReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxReference.getId().intValue()))
            .andExpect(jsonPath("$.taxName").value(DEFAULT_TAX_NAME))
            .andExpect(jsonPath("$.taxDescription").value(DEFAULT_TAX_DESCRIPTION))
            .andExpect(jsonPath("$.taxPercentage").value(DEFAULT_TAX_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.taxReferenceType").value(DEFAULT_TAX_REFERENCE_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getTaxReferencesByIdFiltering() throws Exception {
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
    public void getAllTaxReferencesByTaxNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName equals to DEFAULT_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.equals=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.equals=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName not equals to DEFAULT_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.notEquals=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName not equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.notEquals=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxNameIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName in DEFAULT_TAX_NAME or UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.in=" + DEFAULT_TAX_NAME + "," + UPDATED_TAX_NAME);

        // Get all the taxReferenceList where taxName equals to UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.in=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName is not null
        defaultTaxReferenceShouldBeFound("taxName.specified=true");

        // Get all the taxReferenceList where taxName is null
        defaultTaxReferenceShouldNotBeFound("taxName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaxReferencesByTaxNameContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName contains DEFAULT_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.contains=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName contains UPDATED_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.contains=" + UPDATED_TAX_NAME);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxNameNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxName does not contain DEFAULT_TAX_NAME
        defaultTaxReferenceShouldNotBeFound("taxName.doesNotContain=" + DEFAULT_TAX_NAME);

        // Get all the taxReferenceList where taxName does not contain UPDATED_TAX_NAME
        defaultTaxReferenceShouldBeFound("taxName.doesNotContain=" + UPDATED_TAX_NAME);
    }


    @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription equals to DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.equals=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.equals=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription not equals to DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.notEquals=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription not equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.notEquals=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription in DEFAULT_TAX_DESCRIPTION or UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.in=" + DEFAULT_TAX_DESCRIPTION + "," + UPDATED_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription equals to UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.in=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription is not null
        defaultTaxReferenceShouldBeFound("taxDescription.specified=true");

        // Get all the taxReferenceList where taxDescription is null
        defaultTaxReferenceShouldNotBeFound("taxDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription contains DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.contains=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription contains UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.contains=" + UPDATED_TAX_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxDescription does not contain DEFAULT_TAX_DESCRIPTION
        defaultTaxReferenceShouldNotBeFound("taxDescription.doesNotContain=" + DEFAULT_TAX_DESCRIPTION);

        // Get all the taxReferenceList where taxDescription does not contain UPDATED_TAX_DESCRIPTION
        defaultTaxReferenceShouldBeFound("taxDescription.doesNotContain=" + UPDATED_TAX_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage equals to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.equals=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.equals=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage not equals to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.notEquals=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage not equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.notEquals=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage in DEFAULT_TAX_PERCENTAGE or UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.in=" + DEFAULT_TAX_PERCENTAGE + "," + UPDATED_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.in=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is not null
        defaultTaxReferenceShouldBeFound("taxPercentage.specified=true");

        // Get all the taxReferenceList where taxPercentage is null
        defaultTaxReferenceShouldNotBeFound("taxPercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is greater than or equal to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.greaterThanOrEqual=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is greater than or equal to UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.greaterThanOrEqual=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is less than or equal to DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.lessThanOrEqual=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is less than or equal to SMALLER_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.lessThanOrEqual=" + SMALLER_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is less than DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.lessThan=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is less than UPDATED_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.lessThan=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxPercentage is greater than DEFAULT_TAX_PERCENTAGE
        defaultTaxReferenceShouldNotBeFound("taxPercentage.greaterThan=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the taxReferenceList where taxPercentage is greater than SMALLER_TAX_PERCENTAGE
        defaultTaxReferenceShouldBeFound("taxPercentage.greaterThan=" + SMALLER_TAX_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllTaxReferencesByTaxReferenceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType equals to DEFAULT_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.equals=" + DEFAULT_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.equals=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxReferenceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType not equals to DEFAULT_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.notEquals=" + DEFAULT_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType not equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.notEquals=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxReferenceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType in DEFAULT_TAX_REFERENCE_TYPE or UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldBeFound("taxReferenceType.in=" + DEFAULT_TAX_REFERENCE_TYPE + "," + UPDATED_TAX_REFERENCE_TYPE);

        // Get all the taxReferenceList where taxReferenceType equals to UPDATED_TAX_REFERENCE_TYPE
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.in=" + UPDATED_TAX_REFERENCE_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaxReferencesByTaxReferenceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        // Get all the taxReferenceList where taxReferenceType is not null
        defaultTaxReferenceShouldBeFound("taxReferenceType.specified=true");

        // Get all the taxReferenceList where taxReferenceType is null
        defaultTaxReferenceShouldNotBeFound("taxReferenceType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxReferenceShouldBeFound(String filter) throws Exception {
        restTaxReferenceMockMvc.perform(get("/api/tax-references?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())));

        // Check, that the count call also returns 1
        restTaxReferenceMockMvc.perform(get("/api/tax-references/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxReferenceShouldNotBeFound(String filter) throws Exception {
        restTaxReferenceMockMvc.perform(get("/api/tax-references?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxReferenceMockMvc.perform(get("/api/tax-references/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTaxReference() throws Exception {
        // Get the taxReference
        restTaxReferenceMockMvc.perform(get("/api/tax-references/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxReference() throws Exception {
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
            .taxReferenceType(UPDATED_TAX_REFERENCE_TYPE);
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(updatedTaxReference);

        restTaxReferenceMockMvc.perform(put("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isOk());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);
        TaxReference testTaxReference = taxReferenceList.get(taxReferenceList.size() - 1);
        assertThat(testTaxReference.getTaxName()).isEqualTo(UPDATED_TAX_NAME);
        assertThat(testTaxReference.getTaxDescription()).isEqualTo(UPDATED_TAX_DESCRIPTION);
        assertThat(testTaxReference.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testTaxReference.getTaxReferenceType()).isEqualTo(UPDATED_TAX_REFERENCE_TYPE);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(1)).save(testTaxReference);
    }

    @Test
    @Transactional
    public void updateNonExistingTaxReference() throws Exception {
        int databaseSizeBeforeUpdate = taxReferenceRepository.findAll().size();

        // Create the TaxReference
        TaxReferenceDTO taxReferenceDTO = taxReferenceMapper.toDto(taxReference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxReferenceMockMvc.perform(put("/api/tax-references").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxReferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxReference in the database
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(0)).save(taxReference);
    }

    @Test
    @Transactional
    public void deleteTaxReference() throws Exception {
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);

        int databaseSizeBeforeDelete = taxReferenceRepository.findAll().size();

        // Delete the taxReference
        restTaxReferenceMockMvc.perform(delete("/api/tax-references/{id}", taxReference.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxReference> taxReferenceList = taxReferenceRepository.findAll();
        assertThat(taxReferenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaxReference in Elasticsearch
        verify(mockTaxReferenceSearchRepository, times(1)).deleteById(taxReference.getId());
    }

    @Test
    @Transactional
    public void searchTaxReference() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        taxReferenceRepository.saveAndFlush(taxReference);
        when(mockTaxReferenceSearchRepository.search(queryStringQuery("id:" + taxReference.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taxReference), PageRequest.of(0, 1), 1));

        // Search the taxReference
        restTaxReferenceMockMvc.perform(get("/api/_search/tax-references?query=id:" + taxReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxName").value(hasItem(DEFAULT_TAX_NAME)))
            .andExpect(jsonPath("$.[*].taxDescription").value(hasItem(DEFAULT_TAX_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxReferenceType").value(hasItem(DEFAULT_TAX_REFERENCE_TYPE.toString())));
    }
}
