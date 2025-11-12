package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePeriod;
import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.domain.RouModelMetadata;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.service.criteria.RouDepreciationEntryCriteria;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link RouDepreciationEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouDepreciationEntryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(0 - 1);

    private static final UUID DEFAULT_ROU_ASSET_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_ROU_ASSET_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_ROU_DEPRECIATION_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_ROU_DEPRECIATION_IDENTIFIER = UUID.randomUUID();

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final UUID DEFAULT_BATCH_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_BATCH_JOB_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_COMPILATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPILATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COMPILATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_INVALIDATED = false;
    private static final Boolean UPDATED_INVALIDATED = true;

    private static final String ENTITY_API_URL = "/api/rou-depreciation-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-depreciation-entries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationEntryRepository rouDepreciationEntryRepository;

    @Autowired
    private RouDepreciationEntryMapper rouDepreciationEntryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationEntrySearchRepository mockRouDepreciationEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationEntryMockMvc;

    private RouDepreciationEntry rouDepreciationEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntry createEntity(EntityManager em) {
        RouDepreciationEntry rouDepreciationEntry = new RouDepreciationEntry()
            .description(DEFAULT_DESCRIPTION)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(DEFAULT_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(DEFAULT_ROU_DEPRECIATION_IDENTIFIER)
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .activated(DEFAULT_ACTIVATED)
            .isDeleted(DEFAULT_IS_DELETED)
            .batchJobIdentifier(DEFAULT_BATCH_JOB_IDENTIFIER)
            .depreciationAmountStepIdentifier(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER)
            .outstandingAmountStepIdentifier(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER)
            .flagAmortisedStepIdentifier(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER)
            .compilationTime(DEFAULT_COMPILATION_TIME)
            .invalidated(DEFAULT_INVALIDATED);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouDepreciationEntry.setDebitAccount(transactionAccount);
        // Add required entity
        rouDepreciationEntry.setCreditAccount(transactionAccount);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        rouDepreciationEntry.setAssetCategory(assetCategory);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouDepreciationEntry.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        RouModelMetadata rouModelMetadata;
        if (TestUtil.findAll(em, RouModelMetadata.class).isEmpty()) {
            rouModelMetadata = RouModelMetadataResourceIT.createEntity(em);
            em.persist(rouModelMetadata);
            em.flush();
        } else {
            rouModelMetadata = TestUtil.findAll(em, RouModelMetadata.class).get(0);
        }
        rouDepreciationEntry.setRouMetadata(rouModelMetadata);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        rouDepreciationEntry.setLeasePeriod(leasePeriod);
        return rouDepreciationEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntry createUpdatedEntity(EntityManager em) {
        RouDepreciationEntry rouDepreciationEntry = new RouDepreciationEntry()
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .activated(UPDATED_ACTIVATED)
            .isDeleted(UPDATED_IS_DELETED)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .depreciationAmountStepIdentifier(UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER)
            .outstandingAmountStepIdentifier(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER)
            .flagAmortisedStepIdentifier(UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouDepreciationEntry.setDebitAccount(transactionAccount);
        // Add required entity
        rouDepreciationEntry.setCreditAccount(transactionAccount);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createUpdatedEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        rouDepreciationEntry.setAssetCategory(assetCategory);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouDepreciationEntry.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        RouModelMetadata rouModelMetadata;
        if (TestUtil.findAll(em, RouModelMetadata.class).isEmpty()) {
            rouModelMetadata = RouModelMetadataResourceIT.createUpdatedEntity(em);
            em.persist(rouModelMetadata);
            em.flush();
        } else {
            rouModelMetadata = TestUtil.findAll(em, RouModelMetadata.class).get(0);
        }
        rouDepreciationEntry.setRouMetadata(rouModelMetadata);
        // Add required entity
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createUpdatedEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        rouDepreciationEntry.setLeasePeriod(leasePeriod);
        return rouDepreciationEntry;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationEntryRepository.findAll().size();
        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);
        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(DEFAULT_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(DEFAULT_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(DEFAULT_ROU_DEPRECIATION_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testRouDepreciationEntry.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testRouDepreciationEntry.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testRouDepreciationEntry.getBatchJobIdentifier()).isEqualTo(DEFAULT_BATCH_JOB_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getDepreciationAmountStepIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getOutstandingAmountStepIdentifier()).isEqualTo(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getFlagAmortisedStepIdentifier()).isEqualTo(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getCompilationTime()).isEqualTo(DEFAULT_COMPILATION_TIME);
        assertThat(testRouDepreciationEntry.getInvalidated()).isEqualTo(DEFAULT_INVALIDATED);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(1)).save(testRouDepreciationEntry);
    }

    @Test
    @Transactional
    void createRouDepreciationEntryWithExistingId() throws Exception {
        // Create the RouDepreciationEntry with an existing ID
        rouDepreciationEntry.setId(1L);
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        int databaseSizeBeforeCreate = rouDepreciationEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void checkDepreciationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setDepreciationAmount(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutstandingAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setOutstandingAmount(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRouDepreciationIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setRouDepreciationIdentifier(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntries() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(
                jsonPath("$.[*].depreciationAmountStepIdentifier").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(
                jsonPath("$.[*].outstandingAmountStepIdentifier").value(hasItem(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(jsonPath("$.[*].flagAmortisedStepIdentifier").value(hasItem(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));
    }

    @Test
    @Transactional
    void getRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationEntry.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)))
            .andExpect(jsonPath("$.rouAssetIdentifier").value(DEFAULT_ROU_ASSET_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.rouDepreciationIdentifier").value(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.batchJobIdentifier").value(DEFAULT_BATCH_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.depreciationAmountStepIdentifier").value(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.outstandingAmountStepIdentifier").value(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.flagAmortisedStepIdentifier").value(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationTime").value(sameInstant(DEFAULT_COMPILATION_TIME)))
            .andExpect(jsonPath("$.invalidated").value(DEFAULT_INVALIDATED.booleanValue()));
    }

    @Test
    @Transactional
    void getRouDepreciationEntriesByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        Long id = rouDepreciationEntry.getId();

        defaultRouDepreciationEntryShouldBeFound("id.equals=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description not equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description not equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description is not null
        defaultRouDepreciationEntryShouldBeFound("description.specified=true");

        // Get all the rouDepreciationEntryList where description is null
        defaultRouDepreciationEntryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description contains DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description contains UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description does not contain DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description does not contain UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the rouDepreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is not null
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.specified=true");

        // Get all the rouDepreciationEntryList where depreciationAmount is null
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is not null
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.specified=true");

        // Get all the rouDepreciationEntryList where outstandingAmount is null
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.equals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.equals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier not equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.notEquals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier not equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.notEquals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier in DEFAULT_ROU_ASSET_IDENTIFIER or UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "rouAssetIdentifier.in=" + DEFAULT_ROU_ASSET_IDENTIFIER + "," + UPDATED_ROU_ASSET_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.in=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where rouAssetIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to DEFAULT_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.equals=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.equals=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier not equals to DEFAULT_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.notEquals=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier not equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.notEquals=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier in DEFAULT_ROU_DEPRECIATION_IDENTIFIER or UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "rouDepreciationIdentifier.in=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER + "," + UPDATED_ROU_DEPRECIATION_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.in=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber is not null
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.specified=true");

        // Get all the rouDepreciationEntryList where sequenceNumber is null
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the rouDepreciationEntryList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultRouDepreciationEntryShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where activated equals to DEFAULT_ACTIVATED
        defaultRouDepreciationEntryShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the rouDepreciationEntryList where activated equals to UPDATED_ACTIVATED
        defaultRouDepreciationEntryShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where activated not equals to DEFAULT_ACTIVATED
        defaultRouDepreciationEntryShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the rouDepreciationEntryList where activated not equals to UPDATED_ACTIVATED
        defaultRouDepreciationEntryShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultRouDepreciationEntryShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the rouDepreciationEntryList where activated equals to UPDATED_ACTIVATED
        defaultRouDepreciationEntryShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where activated is not null
        defaultRouDepreciationEntryShouldBeFound("activated.specified=true");

        // Get all the rouDepreciationEntryList where activated is null
        defaultRouDepreciationEntryShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where isDeleted equals to DEFAULT_IS_DELETED
        defaultRouDepreciationEntryShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the rouDepreciationEntryList where isDeleted equals to UPDATED_IS_DELETED
        defaultRouDepreciationEntryShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByIsDeletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where isDeleted not equals to DEFAULT_IS_DELETED
        defaultRouDepreciationEntryShouldNotBeFound("isDeleted.notEquals=" + DEFAULT_IS_DELETED);

        // Get all the rouDepreciationEntryList where isDeleted not equals to UPDATED_IS_DELETED
        defaultRouDepreciationEntryShouldBeFound("isDeleted.notEquals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultRouDepreciationEntryShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the rouDepreciationEntryList where isDeleted equals to UPDATED_IS_DELETED
        defaultRouDepreciationEntryShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where isDeleted is not null
        defaultRouDepreciationEntryShouldBeFound("isDeleted.specified=true");

        // Get all the rouDepreciationEntryList where isDeleted is null
        defaultRouDepreciationEntryShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByBatchJobIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where batchJobIdentifier equals to DEFAULT_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("batchJobIdentifier.equals=" + DEFAULT_BATCH_JOB_IDENTIFIER);

        // Get all the rouDepreciationEntryList where batchJobIdentifier equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("batchJobIdentifier.equals=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByBatchJobIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where batchJobIdentifier not equals to DEFAULT_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("batchJobIdentifier.notEquals=" + DEFAULT_BATCH_JOB_IDENTIFIER);

        // Get all the rouDepreciationEntryList where batchJobIdentifier not equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("batchJobIdentifier.notEquals=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByBatchJobIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where batchJobIdentifier in DEFAULT_BATCH_JOB_IDENTIFIER or UPDATED_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "batchJobIdentifier.in=" + DEFAULT_BATCH_JOB_IDENTIFIER + "," + UPDATED_BATCH_JOB_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where batchJobIdentifier equals to UPDATED_BATCH_JOB_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("batchJobIdentifier.in=" + UPDATED_BATCH_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByBatchJobIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where batchJobIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("batchJobIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where batchJobIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("batchJobIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountStepIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier equals to DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("depreciationAmountStepIdentifier.equals=" + DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier equals to UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound(
            "depreciationAmountStepIdentifier.equals=" + UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountStepIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier not equals to DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound(
            "depreciationAmountStepIdentifier.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier not equals to UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "depreciationAmountStepIdentifier.notEquals=" + UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        );
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountStepIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier in DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER or UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "depreciationAmountStepIdentifier.in=" +
            DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER +
            "," +
            UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier equals to UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmountStepIdentifier.in=" + UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountStepIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("depreciationAmountStepIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where depreciationAmountStepIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmountStepIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountStepIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier equals to DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("outstandingAmountStepIdentifier.equals=" + DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier equals to UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmountStepIdentifier.equals=" + UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountStepIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier not equals to DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound(
            "outstandingAmountStepIdentifier.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier not equals to UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("outstandingAmountStepIdentifier.notEquals=" + UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountStepIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier in DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER or UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "outstandingAmountStepIdentifier.in=" +
            DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER +
            "," +
            UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier equals to UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmountStepIdentifier.in=" + UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountStepIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("outstandingAmountStepIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where outstandingAmountStepIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmountStepIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByFlagAmortisedStepIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier equals to DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("flagAmortisedStepIdentifier.equals=" + DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier equals to UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("flagAmortisedStepIdentifier.equals=" + UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByFlagAmortisedStepIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier not equals to DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("flagAmortisedStepIdentifier.notEquals=" + DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier not equals to UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("flagAmortisedStepIdentifier.notEquals=" + UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByFlagAmortisedStepIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier in DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER or UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "flagAmortisedStepIdentifier.in=" + DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER + "," + UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier equals to UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("flagAmortisedStepIdentifier.in=" + UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByFlagAmortisedStepIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("flagAmortisedStepIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where flagAmortisedStepIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("flagAmortisedStepIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime equals to DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.equals=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.equals=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime not equals to DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.notEquals=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime not equals to UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.notEquals=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime in DEFAULT_COMPILATION_TIME or UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.in=" + DEFAULT_COMPILATION_TIME + "," + UPDATED_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.in=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime is not null
        defaultRouDepreciationEntryShouldBeFound("compilationTime.specified=true");

        // Get all the rouDepreciationEntryList where compilationTime is null
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime is greater than or equal to DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.greaterThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime is greater than or equal to UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.greaterThanOrEqual=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime is less than or equal to DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.lessThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime is less than or equal to SMALLER_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.lessThanOrEqual=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime is less than DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.lessThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime is less than UPDATED_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.lessThan=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCompilationTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where compilationTime is greater than DEFAULT_COMPILATION_TIME
        defaultRouDepreciationEntryShouldNotBeFound("compilationTime.greaterThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the rouDepreciationEntryList where compilationTime is greater than SMALLER_COMPILATION_TIME
        defaultRouDepreciationEntryShouldBeFound("compilationTime.greaterThan=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByInvalidatedIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where invalidated equals to DEFAULT_INVALIDATED
        defaultRouDepreciationEntryShouldBeFound("invalidated.equals=" + DEFAULT_INVALIDATED);

        // Get all the rouDepreciationEntryList where invalidated equals to UPDATED_INVALIDATED
        defaultRouDepreciationEntryShouldNotBeFound("invalidated.equals=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByInvalidatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where invalidated not equals to DEFAULT_INVALIDATED
        defaultRouDepreciationEntryShouldNotBeFound("invalidated.notEquals=" + DEFAULT_INVALIDATED);

        // Get all the rouDepreciationEntryList where invalidated not equals to UPDATED_INVALIDATED
        defaultRouDepreciationEntryShouldBeFound("invalidated.notEquals=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByInvalidatedIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where invalidated in DEFAULT_INVALIDATED or UPDATED_INVALIDATED
        defaultRouDepreciationEntryShouldBeFound("invalidated.in=" + DEFAULT_INVALIDATED + "," + UPDATED_INVALIDATED);

        // Get all the rouDepreciationEntryList where invalidated equals to UPDATED_INVALIDATED
        defaultRouDepreciationEntryShouldNotBeFound("invalidated.in=" + UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByInvalidatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where invalidated is not null
        defaultRouDepreciationEntryShouldBeFound("invalidated.specified=true");

        // Get all the rouDepreciationEntryList where invalidated is null
        defaultRouDepreciationEntryShouldNotBeFound("invalidated.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDebitAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        TransactionAccount debitAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            debitAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(debitAccount);
            em.flush();
        } else {
            debitAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(debitAccount);
        em.flush();
        rouDepreciationEntry.setDebitAccount(debitAccount);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long debitAccountId = debitAccount.getId();

        // Get all the rouDepreciationEntryList where debitAccount equals to debitAccountId
        defaultRouDepreciationEntryShouldBeFound("debitAccountId.equals=" + debitAccountId);

        // Get all the rouDepreciationEntryList where debitAccount equals to (debitAccountId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("debitAccountId.equals=" + (debitAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByCreditAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        TransactionAccount creditAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            creditAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(creditAccount);
            em.flush();
        } else {
            creditAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(creditAccount);
        em.flush();
        rouDepreciationEntry.setCreditAccount(creditAccount);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long creditAccountId = creditAccount.getId();

        // Get all the rouDepreciationEntryList where creditAccount equals to creditAccountId
        defaultRouDepreciationEntryShouldBeFound("creditAccountId.equals=" + creditAccountId);

        // Get all the rouDepreciationEntryList where creditAccount equals to (creditAccountId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("creditAccountId.equals=" + (creditAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
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
        rouDepreciationEntry.setAssetCategory(assetCategory);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long assetCategoryId = assetCategory.getId();

        // Get all the rouDepreciationEntryList where assetCategory equals to assetCategoryId
        defaultRouDepreciationEntryShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the rouDepreciationEntryList where assetCategory equals to (assetCategoryId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByLeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        IFRS16LeaseContract leaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            leaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        em.persist(leaseContract);
        em.flush();
        rouDepreciationEntry.setLeaseContract(leaseContract);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long leaseContractId = leaseContract.getId();

        // Get all the rouDepreciationEntryList where leaseContract equals to leaseContractId
        defaultRouDepreciationEntryShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the rouDepreciationEntryList where leaseContract equals to (leaseContractId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        RouModelMetadata rouMetadata;
        if (TestUtil.findAll(em, RouModelMetadata.class).isEmpty()) {
            rouMetadata = RouModelMetadataResourceIT.createEntity(em);
            em.persist(rouMetadata);
            em.flush();
        } else {
            rouMetadata = TestUtil.findAll(em, RouModelMetadata.class).get(0);
        }
        em.persist(rouMetadata);
        em.flush();
        rouDepreciationEntry.setRouMetadata(rouMetadata);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long rouMetadataId = rouMetadata.getId();

        // Get all the rouDepreciationEntryList where rouMetadata equals to rouMetadataId
        defaultRouDepreciationEntryShouldBeFound("rouMetadataId.equals=" + rouMetadataId);

        // Get all the rouDepreciationEntryList where rouMetadata equals to (rouMetadataId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("rouMetadataId.equals=" + (rouMetadataId + 1));
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByLeasePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        LeasePeriod leasePeriod;
        if (TestUtil.findAll(em, LeasePeriod.class).isEmpty()) {
            leasePeriod = LeasePeriodResourceIT.createEntity(em);
            em.persist(leasePeriod);
            em.flush();
        } else {
            leasePeriod = TestUtil.findAll(em, LeasePeriod.class).get(0);
        }
        em.persist(leasePeriod);
        em.flush();
        rouDepreciationEntry.setLeasePeriod(leasePeriod);
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        Long leasePeriodId = leasePeriod.getId();

        // Get all the rouDepreciationEntryList where leasePeriod equals to leasePeriodId
        defaultRouDepreciationEntryShouldBeFound("leasePeriodId.equals=" + leasePeriodId);

        // Get all the rouDepreciationEntryList where leasePeriod equals to (leasePeriodId + 1)
        defaultRouDepreciationEntryShouldNotBeFound("leasePeriodId.equals=" + (leasePeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationEntryShouldBeFound(String filter) throws Exception {
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(
                jsonPath("$.[*].depreciationAmountStepIdentifier").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(
                jsonPath("$.[*].outstandingAmountStepIdentifier").value(hasItem(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(jsonPath("$.[*].flagAmortisedStepIdentifier").value(hasItem(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));

        // Check, that the count call also returns 1
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationEntryShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationEntry() throws Exception {
        // Get the rouDepreciationEntry
        restRouDepreciationEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry
        RouDepreciationEntry updatedRouDepreciationEntry = rouDepreciationEntryRepository.findById(rouDepreciationEntry.getId()).get();
        // Disconnect from session so that the updates on updatedRouDepreciationEntry are not directly saved in db
        em.detach(updatedRouDepreciationEntry);
        updatedRouDepreciationEntry
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .activated(UPDATED_ACTIVATED)
            .isDeleted(UPDATED_IS_DELETED)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .depreciationAmountStepIdentifier(UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER)
            .outstandingAmountStepIdentifier(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER)
            .flagAmortisedStepIdentifier(UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(updatedRouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualTo(UPDATED_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(UPDATED_ROU_DEPRECIATION_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testRouDepreciationEntry.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testRouDepreciationEntry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testRouDepreciationEntry.getBatchJobIdentifier()).isEqualTo(UPDATED_BATCH_JOB_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getDepreciationAmountStepIdentifier()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getOutstandingAmountStepIdentifier()).isEqualTo(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getFlagAmortisedStepIdentifier()).isEqualTo(UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testRouDepreciationEntry.getInvalidated()).isEqualTo(UPDATED_INVALIDATED);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository).save(testRouDepreciationEntry);
    }

    @Test
    @Transactional
    void putNonExistingRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void partialUpdateRouDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry using partial update
        RouDepreciationEntry partialUpdatedRouDepreciationEntry = new RouDepreciationEntry();
        partialUpdatedRouDepreciationEntry.setId(rouDepreciationEntry.getId());

        partialUpdatedRouDepreciationEntry
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .activated(UPDATED_ACTIVATED)
            .isDeleted(UPDATED_IS_DELETED)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .outstandingAmountStepIdentifier(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME);

        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(DEFAULT_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(DEFAULT_ROU_DEPRECIATION_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testRouDepreciationEntry.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testRouDepreciationEntry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testRouDepreciationEntry.getBatchJobIdentifier()).isEqualTo(UPDATED_BATCH_JOB_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getDepreciationAmountStepIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getOutstandingAmountStepIdentifier()).isEqualTo(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getFlagAmortisedStepIdentifier()).isEqualTo(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testRouDepreciationEntry.getInvalidated()).isEqualTo(DEFAULT_INVALIDATED);
    }

    @Test
    @Transactional
    void fullUpdateRouDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry using partial update
        RouDepreciationEntry partialUpdatedRouDepreciationEntry = new RouDepreciationEntry();
        partialUpdatedRouDepreciationEntry.setId(rouDepreciationEntry.getId());

        partialUpdatedRouDepreciationEntry
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .activated(UPDATED_ACTIVATED)
            .isDeleted(UPDATED_IS_DELETED)
            .batchJobIdentifier(UPDATED_BATCH_JOB_IDENTIFIER)
            .depreciationAmountStepIdentifier(UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER)
            .outstandingAmountStepIdentifier(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER)
            .flagAmortisedStepIdentifier(UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .invalidated(UPDATED_INVALIDATED);

        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(UPDATED_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(UPDATED_ROU_DEPRECIATION_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testRouDepreciationEntry.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testRouDepreciationEntry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testRouDepreciationEntry.getBatchJobIdentifier()).isEqualTo(UPDATED_BATCH_JOB_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getDepreciationAmountStepIdentifier()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getOutstandingAmountStepIdentifier()).isEqualTo(UPDATED_OUTSTANDING_AMOUNT_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getFlagAmortisedStepIdentifier()).isEqualTo(UPDATED_FLAG_AMORTISED_STEP_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testRouDepreciationEntry.getInvalidated()).isEqualTo(UPDATED_INVALIDATED);
    }

    @Test
    @Transactional
    void patchNonExistingRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void deleteRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeDelete = rouDepreciationEntryRepository.findAll().size();

        // Delete the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouDepreciationEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(1)).deleteById(rouDepreciationEntry.getId());
    }

    @Test
    @Transactional
    void searchRouDepreciationEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        when(mockRouDepreciationEntrySearchRepository.search("id:" + rouDepreciationEntry.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationEntry), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].batchJobIdentifier").value(hasItem(DEFAULT_BATCH_JOB_IDENTIFIER.toString())))
            .andExpect(
                jsonPath("$.[*].depreciationAmountStepIdentifier").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(
                jsonPath("$.[*].outstandingAmountStepIdentifier").value(hasItem(DEFAULT_OUTSTANDING_AMOUNT_STEP_IDENTIFIER.toString()))
            )
            .andExpect(jsonPath("$.[*].flagAmortisedStepIdentifier").value(hasItem(DEFAULT_FLAG_AMORTISED_STEP_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(sameInstant(DEFAULT_COMPILATION_TIME))))
            .andExpect(jsonPath("$.[*].invalidated").value(hasItem(DEFAULT_INVALIDATED.booleanValue())));
    }
}
