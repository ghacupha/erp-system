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

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.RouModelMetadata;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.RouModelMetadataRepository;
import io.github.erp.repository.search.RouModelMetadataSearchRepository;
import io.github.erp.service.RouModelMetadataService;
import io.github.erp.service.criteria.RouModelMetadataCriteria;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.RouModelMetadataMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RouModelMetadataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouModelMetadataResourceIT {

    private static final String DEFAULT_MODEL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MODEL_VERSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_MODEL_VERSION = new BigDecimal(2);
    private static final BigDecimal SMALLER_MODEL_VERSION = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEASE_TERM_PERIODS = 1;
    private static final Integer UPDATED_LEASE_TERM_PERIODS = 2;
    private static final Integer SMALLER_LEASE_TERM_PERIODS = 1 - 1;

    private static final BigDecimal DEFAULT_LEASE_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LEASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_LEASE_AMOUNT = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/rou-model-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-model-metadata";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouModelMetadataRepository rouModelMetadataRepository;

    @Mock
    private RouModelMetadataRepository rouModelMetadataRepositoryMock;

    @Autowired
    private RouModelMetadataMapper rouModelMetadataMapper;

    @Mock
    private RouModelMetadataService rouModelMetadataServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouModelMetadataSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouModelMetadataSearchRepository mockRouModelMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouModelMetadataMockMvc;

    private RouModelMetadata rouModelMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouModelMetadata createEntity(EntityManager em) {
        RouModelMetadata rouModelMetadata = new RouModelMetadata()
            .modelTitle(DEFAULT_MODEL_TITLE)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .description(DEFAULT_DESCRIPTION)
            .leaseTermPeriods(DEFAULT_LEASE_TERM_PERIODS)
            .leaseAmount(DEFAULT_LEASE_AMOUNT);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouModelMetadata.setIfrs16LeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouModelMetadata.setAssetAccount(transactionAccount);
        // Add required entity
        rouModelMetadata.setDepreciationAccount(transactionAccount);
        // Add required entity
        rouModelMetadata.setAccruedDepreciationAccount(transactionAccount);
        return rouModelMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouModelMetadata createUpdatedEntity(EntityManager em) {
        RouModelMetadata rouModelMetadata = new RouModelMetadata()
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .leaseTermPeriods(UPDATED_LEASE_TERM_PERIODS)
            .leaseAmount(UPDATED_LEASE_AMOUNT);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouModelMetadata.setIfrs16LeaseContract(iFRS16LeaseContract);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouModelMetadata.setAssetAccount(transactionAccount);
        // Add required entity
        rouModelMetadata.setDepreciationAccount(transactionAccount);
        // Add required entity
        rouModelMetadata.setAccruedDepreciationAccount(transactionAccount);
        return rouModelMetadata;
    }

    @BeforeEach
    public void initTest() {
        rouModelMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createRouModelMetadata() throws Exception {
        int databaseSizeBeforeCreate = rouModelMetadataRepository.findAll().size();
        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);
        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        RouModelMetadata testRouModelMetadata = rouModelMetadataList.get(rouModelMetadataList.size() - 1);
        assertThat(testRouModelMetadata.getModelTitle()).isEqualTo(DEFAULT_MODEL_TITLE);
        assertThat(testRouModelMetadata.getModelVersion()).isEqualByComparingTo(DEFAULT_MODEL_VERSION);
        assertThat(testRouModelMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouModelMetadata.getLeaseTermPeriods()).isEqualTo(DEFAULT_LEASE_TERM_PERIODS);
        assertThat(testRouModelMetadata.getLeaseAmount()).isEqualByComparingTo(DEFAULT_LEASE_AMOUNT);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(1)).save(testRouModelMetadata);
    }

    @Test
    @Transactional
    void createRouModelMetadataWithExistingId() throws Exception {
        // Create the RouModelMetadata with an existing ID
        rouModelMetadata.setId(1L);
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        int databaseSizeBeforeCreate = rouModelMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void checkModelTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouModelMetadataRepository.findAll().size();
        // set the field null
        rouModelMetadata.setModelTitle(null);

        // Create the RouModelMetadata, which fails.
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouModelMetadataRepository.findAll().size();
        // set the field null
        rouModelMetadata.setModelVersion(null);

        // Create the RouModelMetadata, which fails.
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaseTermPeriodsIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouModelMetadataRepository.findAll().size();
        // set the field null
        rouModelMetadata.setLeaseTermPeriods(null);

        // Create the RouModelMetadata, which fails.
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaseAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouModelMetadataRepository.findAll().size();
        // set the field null
        rouModelMetadata.setLeaseAmount(null);

        // Create the RouModelMetadata, which fails.
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        restRouModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouModelMetadata() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouModelMetadataWithEagerRelationshipsIsEnabled() throws Exception {
        when(rouModelMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRouModelMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rouModelMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouModelMetadataWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rouModelMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRouModelMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rouModelMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRouModelMetadata() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get the rouModelMetadata
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, rouModelMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouModelMetadata.getId().intValue()))
            .andExpect(jsonPath("$.modelTitle").value(DEFAULT_MODEL_TITLE))
            .andExpect(jsonPath("$.modelVersion").value(sameNumber(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.leaseTermPeriods").value(DEFAULT_LEASE_TERM_PERIODS))
            .andExpect(jsonPath("$.leaseAmount").value(sameNumber(DEFAULT_LEASE_AMOUNT)));
    }

    @Test
    @Transactional
    void getRouModelMetadataByIdFiltering() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        Long id = rouModelMetadata.getId();

        defaultRouModelMetadataShouldBeFound("id.equals=" + id);
        defaultRouModelMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultRouModelMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouModelMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultRouModelMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouModelMetadataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle equals to DEFAULT_MODEL_TITLE
        defaultRouModelMetadataShouldBeFound("modelTitle.equals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouModelMetadataList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldNotBeFound("modelTitle.equals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle not equals to DEFAULT_MODEL_TITLE
        defaultRouModelMetadataShouldNotBeFound("modelTitle.notEquals=" + DEFAULT_MODEL_TITLE);

        // Get all the rouModelMetadataList where modelTitle not equals to UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldBeFound("modelTitle.notEquals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleIsInShouldWork() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle in DEFAULT_MODEL_TITLE or UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldBeFound("modelTitle.in=" + DEFAULT_MODEL_TITLE + "," + UPDATED_MODEL_TITLE);

        // Get all the rouModelMetadataList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldNotBeFound("modelTitle.in=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle is not null
        defaultRouModelMetadataShouldBeFound("modelTitle.specified=true");

        // Get all the rouModelMetadataList where modelTitle is null
        defaultRouModelMetadataShouldNotBeFound("modelTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleContainsSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle contains DEFAULT_MODEL_TITLE
        defaultRouModelMetadataShouldBeFound("modelTitle.contains=" + DEFAULT_MODEL_TITLE);

        // Get all the rouModelMetadataList where modelTitle contains UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldNotBeFound("modelTitle.contains=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelTitleNotContainsSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelTitle does not contain DEFAULT_MODEL_TITLE
        defaultRouModelMetadataShouldNotBeFound("modelTitle.doesNotContain=" + DEFAULT_MODEL_TITLE);

        // Get all the rouModelMetadataList where modelTitle does not contain UPDATED_MODEL_TITLE
        defaultRouModelMetadataShouldBeFound("modelTitle.doesNotContain=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion equals to DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.equals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion not equals to DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.notEquals=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion not equals to UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.notEquals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion in DEFAULT_MODEL_VERSION or UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.in=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion is not null
        defaultRouModelMetadataShouldBeFound("modelVersion.specified=true");

        // Get all the rouModelMetadataList where modelVersion is null
        defaultRouModelMetadataShouldNotBeFound("modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion is greater than or equal to DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.greaterThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion is greater than or equal to UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.greaterThanOrEqual=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion is less than or equal to DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.lessThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion is less than or equal to SMALLER_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.lessThanOrEqual=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion is less than DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.lessThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion is less than UPDATED_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.lessThan=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByModelVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where modelVersion is greater than DEFAULT_MODEL_VERSION
        defaultRouModelMetadataShouldNotBeFound("modelVersion.greaterThan=" + DEFAULT_MODEL_VERSION);

        // Get all the rouModelMetadataList where modelVersion is greater than SMALLER_MODEL_VERSION
        defaultRouModelMetadataShouldBeFound("modelVersion.greaterThan=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description equals to DEFAULT_DESCRIPTION
        defaultRouModelMetadataShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouModelMetadataList where description equals to UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description not equals to DEFAULT_DESCRIPTION
        defaultRouModelMetadataShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouModelMetadataList where description not equals to UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouModelMetadataList where description equals to UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description is not null
        defaultRouModelMetadataShouldBeFound("description.specified=true");

        // Get all the rouModelMetadataList where description is null
        defaultRouModelMetadataShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description contains DEFAULT_DESCRIPTION
        defaultRouModelMetadataShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouModelMetadataList where description contains UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where description does not contain DEFAULT_DESCRIPTION
        defaultRouModelMetadataShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouModelMetadataList where description does not contain UPDATED_DESCRIPTION
        defaultRouModelMetadataShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods equals to DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.equals=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.equals=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods not equals to DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.notEquals=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods not equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.notEquals=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods in DEFAULT_LEASE_TERM_PERIODS or UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.in=" + DEFAULT_LEASE_TERM_PERIODS + "," + UPDATED_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods equals to UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.in=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods is not null
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.specified=true");

        // Get all the rouModelMetadataList where leaseTermPeriods is null
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods is greater than or equal to DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.greaterThanOrEqual=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods is greater than or equal to UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.greaterThanOrEqual=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods is less than or equal to DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.lessThanOrEqual=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods is less than or equal to SMALLER_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.lessThanOrEqual=" + SMALLER_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods is less than DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.lessThan=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods is less than UPDATED_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.lessThan=" + UPDATED_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseTermPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseTermPeriods is greater than DEFAULT_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldNotBeFound("leaseTermPeriods.greaterThan=" + DEFAULT_LEASE_TERM_PERIODS);

        // Get all the rouModelMetadataList where leaseTermPeriods is greater than SMALLER_LEASE_TERM_PERIODS
        defaultRouModelMetadataShouldBeFound("leaseTermPeriods.greaterThan=" + SMALLER_LEASE_TERM_PERIODS);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount equals to DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.equals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.equals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount not equals to DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.notEquals=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount not equals to UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.notEquals=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount in DEFAULT_LEASE_AMOUNT or UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.in=" + DEFAULT_LEASE_AMOUNT + "," + UPDATED_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount equals to UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.in=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount is not null
        defaultRouModelMetadataShouldBeFound("leaseAmount.specified=true");

        // Get all the rouModelMetadataList where leaseAmount is null
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount is greater than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.greaterThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount is greater than or equal to UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.greaterThanOrEqual=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount is less than or equal to DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.lessThanOrEqual=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount is less than or equal to SMALLER_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.lessThanOrEqual=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount is less than DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.lessThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount is less than UPDATED_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.lessThan=" + UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByLeaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        // Get all the rouModelMetadataList where leaseAmount is greater than DEFAULT_LEASE_AMOUNT
        defaultRouModelMetadataShouldNotBeFound("leaseAmount.greaterThan=" + DEFAULT_LEASE_AMOUNT);

        // Get all the rouModelMetadataList where leaseAmount is greater than SMALLER_LEASE_AMOUNT
        defaultRouModelMetadataShouldBeFound("leaseAmount.greaterThan=" + SMALLER_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByIfrs16LeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        IFRS16LeaseContract ifrs16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            ifrs16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(ifrs16LeaseContract);
            em.flush();
        } else {
            ifrs16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        em.persist(ifrs16LeaseContract);
        em.flush();
        rouModelMetadata.setIfrs16LeaseContract(ifrs16LeaseContract);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long ifrs16LeaseContractId = ifrs16LeaseContract.getId();

        // Get all the rouModelMetadataList where ifrs16LeaseContract equals to ifrs16LeaseContractId
        defaultRouModelMetadataShouldBeFound("ifrs16LeaseContractId.equals=" + ifrs16LeaseContractId);

        // Get all the rouModelMetadataList where ifrs16LeaseContract equals to (ifrs16LeaseContractId + 1)
        defaultRouModelMetadataShouldNotBeFound("ifrs16LeaseContractId.equals=" + (ifrs16LeaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByAssetAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        TransactionAccount assetAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            assetAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(assetAccount);
            em.flush();
        } else {
            assetAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(assetAccount);
        em.flush();
        rouModelMetadata.setAssetAccount(assetAccount);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long assetAccountId = assetAccount.getId();

        // Get all the rouModelMetadataList where assetAccount equals to assetAccountId
        defaultRouModelMetadataShouldBeFound("assetAccountId.equals=" + assetAccountId);

        // Get all the rouModelMetadataList where assetAccount equals to (assetAccountId + 1)
        defaultRouModelMetadataShouldNotBeFound("assetAccountId.equals=" + (assetAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDepreciationAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        TransactionAccount depreciationAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            depreciationAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(depreciationAccount);
            em.flush();
        } else {
            depreciationAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(depreciationAccount);
        em.flush();
        rouModelMetadata.setDepreciationAccount(depreciationAccount);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long depreciationAccountId = depreciationAccount.getId();

        // Get all the rouModelMetadataList where depreciationAccount equals to depreciationAccountId
        defaultRouModelMetadataShouldBeFound("depreciationAccountId.equals=" + depreciationAccountId);

        // Get all the rouModelMetadataList where depreciationAccount equals to (depreciationAccountId + 1)
        defaultRouModelMetadataShouldNotBeFound("depreciationAccountId.equals=" + (depreciationAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByAccruedDepreciationAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        TransactionAccount accruedDepreciationAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            accruedDepreciationAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(accruedDepreciationAccount);
            em.flush();
        } else {
            accruedDepreciationAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(accruedDepreciationAccount);
        em.flush();
        rouModelMetadata.setAccruedDepreciationAccount(accruedDepreciationAccount);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long accruedDepreciationAccountId = accruedDepreciationAccount.getId();

        // Get all the rouModelMetadataList where accruedDepreciationAccount equals to accruedDepreciationAccountId
        defaultRouModelMetadataShouldBeFound("accruedDepreciationAccountId.equals=" + accruedDepreciationAccountId);

        // Get all the rouModelMetadataList where accruedDepreciationAccount equals to (accruedDepreciationAccountId + 1)
        defaultRouModelMetadataShouldNotBeFound("accruedDepreciationAccountId.equals=" + (accruedDepreciationAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        em.persist(assetCategory);
        em.flush();
        rouModelMetadata.setAssetCategory(assetCategory);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long assetCategoryId = assetCategory.getId();

        // Get all the rouModelMetadataList where assetCategory equals to assetCategoryId
        defaultRouModelMetadataShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the rouModelMetadataList where assetCategory equals to (assetCategoryId + 1)
        defaultRouModelMetadataShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllRouModelMetadataByDocumentAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        BusinessDocument documentAttachments;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            documentAttachments = BusinessDocumentResourceIT.createEntity(em);
            em.persist(documentAttachments);
            em.flush();
        } else {
            documentAttachments = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(documentAttachments);
        em.flush();
        rouModelMetadata.addDocumentAttachments(documentAttachments);
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        Long documentAttachmentsId = documentAttachments.getId();

        // Get all the rouModelMetadataList where documentAttachments equals to documentAttachmentsId
        defaultRouModelMetadataShouldBeFound("documentAttachmentsId.equals=" + documentAttachmentsId);

        // Get all the rouModelMetadataList where documentAttachments equals to (documentAttachmentsId + 1)
        defaultRouModelMetadataShouldNotBeFound("documentAttachmentsId.equals=" + (documentAttachmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouModelMetadataShouldBeFound(String filter) throws Exception {
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))));

        // Check, that the count call also returns 1
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouModelMetadataShouldNotBeFound(String filter) throws Exception {
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouModelMetadata() throws Exception {
        // Get the rouModelMetadata
        restRouModelMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouModelMetadata() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();

        // Update the rouModelMetadata
        RouModelMetadata updatedRouModelMetadata = rouModelMetadataRepository.findById(rouModelMetadata.getId()).get();
        // Disconnect from session so that the updates on updatedRouModelMetadata are not directly saved in db
        em.detach(updatedRouModelMetadata);
        updatedRouModelMetadata
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .leaseTermPeriods(UPDATED_LEASE_TERM_PERIODS)
            .leaseAmount(UPDATED_LEASE_AMOUNT);
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(updatedRouModelMetadata);

        restRouModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouModelMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        RouModelMetadata testRouModelMetadata = rouModelMetadataList.get(rouModelMetadataList.size() - 1);
        assertThat(testRouModelMetadata.getModelTitle()).isEqualTo(UPDATED_MODEL_TITLE);
        assertThat(testRouModelMetadata.getModelVersion()).isEqualTo(UPDATED_MODEL_VERSION);
        assertThat(testRouModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouModelMetadata.getLeaseTermPeriods()).isEqualTo(UPDATED_LEASE_TERM_PERIODS);
        assertThat(testRouModelMetadata.getLeaseAmount()).isEqualTo(UPDATED_LEASE_AMOUNT);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository).save(testRouModelMetadata);
    }

    @Test
    @Transactional
    void putNonExistingRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouModelMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void partialUpdateRouModelMetadataWithPatch() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();

        // Update the rouModelMetadata using partial update
        RouModelMetadata partialUpdatedRouModelMetadata = new RouModelMetadata();
        partialUpdatedRouModelMetadata.setId(rouModelMetadata.getId());

        partialUpdatedRouModelMetadata
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .leaseTermPeriods(UPDATED_LEASE_TERM_PERIODS);

        restRouModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouModelMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouModelMetadata))
            )
            .andExpect(status().isOk());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        RouModelMetadata testRouModelMetadata = rouModelMetadataList.get(rouModelMetadataList.size() - 1);
        assertThat(testRouModelMetadata.getModelTitle()).isEqualTo(UPDATED_MODEL_TITLE);
        assertThat(testRouModelMetadata.getModelVersion()).isEqualByComparingTo(UPDATED_MODEL_VERSION);
        assertThat(testRouModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouModelMetadata.getLeaseTermPeriods()).isEqualTo(UPDATED_LEASE_TERM_PERIODS);
        assertThat(testRouModelMetadata.getLeaseAmount()).isEqualByComparingTo(DEFAULT_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateRouModelMetadataWithPatch() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();

        // Update the rouModelMetadata using partial update
        RouModelMetadata partialUpdatedRouModelMetadata = new RouModelMetadata();
        partialUpdatedRouModelMetadata.setId(rouModelMetadata.getId());

        partialUpdatedRouModelMetadata
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .leaseTermPeriods(UPDATED_LEASE_TERM_PERIODS)
            .leaseAmount(UPDATED_LEASE_AMOUNT);

        restRouModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouModelMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouModelMetadata))
            )
            .andExpect(status().isOk());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        RouModelMetadata testRouModelMetadata = rouModelMetadataList.get(rouModelMetadataList.size() - 1);
        assertThat(testRouModelMetadata.getModelTitle()).isEqualTo(UPDATED_MODEL_TITLE);
        assertThat(testRouModelMetadata.getModelVersion()).isEqualByComparingTo(UPDATED_MODEL_VERSION);
        assertThat(testRouModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouModelMetadata.getLeaseTermPeriods()).isEqualTo(UPDATED_LEASE_TERM_PERIODS);
        assertThat(testRouModelMetadata.getLeaseAmount()).isEqualByComparingTo(UPDATED_LEASE_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouModelMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = rouModelMetadataRepository.findAll().size();
        rouModelMetadata.setId(count.incrementAndGet());

        // Create the RouModelMetadata
        RouModelMetadataDTO rouModelMetadataDTO = rouModelMetadataMapper.toDto(rouModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouModelMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouModelMetadata in the database
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(0)).save(rouModelMetadata);
    }

    @Test
    @Transactional
    void deleteRouModelMetadata() throws Exception {
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);

        int databaseSizeBeforeDelete = rouModelMetadataRepository.findAll().size();

        // Delete the rouModelMetadata
        restRouModelMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouModelMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouModelMetadata> rouModelMetadataList = rouModelMetadataRepository.findAll();
        assertThat(rouModelMetadataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouModelMetadata in Elasticsearch
        verify(mockRouModelMetadataSearchRepository, times(1)).deleteById(rouModelMetadata.getId());
    }

    @Test
    @Transactional
    void searchRouModelMetadata() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouModelMetadataRepository.saveAndFlush(rouModelMetadata);
        when(mockRouModelMetadataSearchRepository.search("id:" + rouModelMetadata.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouModelMetadata), PageRequest.of(0, 1), 1));

        // Search the rouModelMetadata
        restRouModelMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouModelMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(sameNumber(DEFAULT_MODEL_VERSION))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leaseTermPeriods").value(hasItem(DEFAULT_LEASE_TERM_PERIODS)))
            .andExpect(jsonPath("$.[*].leaseAmount").value(hasItem(sameNumber(DEFAULT_LEASE_AMOUNT))));
    }
}
