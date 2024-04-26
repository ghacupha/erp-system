package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.AssetDisposal;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.AssetDisposalRepository;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.service.AssetDisposalService;
import io.github.erp.service.criteria.AssetDisposalCriteria;
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.mapper.AssetDisposalMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link AssetDisposalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetDisposalResourceIT {

    private static final UUID DEFAULT_ASSET_DISPOSAL_REFERENCE = UUID.randomUUID();
    private static final UUID UPDATED_ASSET_DISPOSAL_REFERENCE = UUID.randomUUID();

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ASSET_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSET_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSET_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_HISTORICAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_HISTORICAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_HISTORICAL_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCRUED_DEPRECIATION = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACCRUED_DEPRECIATION = new BigDecimal(1);
    private static final BigDecimal SMALLER_ACCRUED_DEPRECIATION = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_DECOMMISSIONING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DECOMMISSIONING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DECOMMISSIONING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DISPOSAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISPOSAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DISPOSAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_DORMANT = false;
    private static final Boolean UPDATED_DORMANT = true;

    private static final String ENTITY_API_URL = "/api/asset-disposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-disposals";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetDisposalRepository assetDisposalRepository;

    @Mock
    private AssetDisposalRepository assetDisposalRepositoryMock;

    @Autowired
    private AssetDisposalMapper assetDisposalMapper;

    @Mock
    private AssetDisposalService assetDisposalServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetDisposalSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetDisposalSearchRepository mockAssetDisposalSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetDisposalMockMvc;

    private AssetDisposal assetDisposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDisposal createEntity(EntityManager em) {
        AssetDisposal assetDisposal = new AssetDisposal()
            .assetDisposalReference(DEFAULT_ASSET_DISPOSAL_REFERENCE)
            .description(DEFAULT_DESCRIPTION)
            .assetCost(DEFAULT_ASSET_COST)
            .historicalCost(DEFAULT_HISTORICAL_COST)
            .accruedDepreciation(DEFAULT_ACCRUED_DEPRECIATION)
            .netBookValue(DEFAULT_NET_BOOK_VALUE)
            .decommissioningDate(DEFAULT_DECOMMISSIONING_DATE)
            .disposalDate(DEFAULT_DISPOSAL_DATE)
            .dormant(DEFAULT_DORMANT);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetDisposal.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetDisposal.setAssetDisposed(assetRegistration);
        return assetDisposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDisposal createUpdatedEntity(EntityManager em) {
        AssetDisposal assetDisposal = new AssetDisposal()
            .assetDisposalReference(UPDATED_ASSET_DISPOSAL_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .assetCost(UPDATED_ASSET_COST)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .accruedDepreciation(UPDATED_ACCRUED_DEPRECIATION)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .decommissioningDate(UPDATED_DECOMMISSIONING_DATE)
            .disposalDate(UPDATED_DISPOSAL_DATE)
            .dormant(UPDATED_DORMANT);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetDisposal.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createUpdatedEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetDisposal.setAssetDisposed(assetRegistration);
        return assetDisposal;
    }

    @BeforeEach
    public void initTest() {
        assetDisposal = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetDisposal() throws Exception {
        int databaseSizeBeforeCreate = assetDisposalRepository.findAll().size();
        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);
        restAssetDisposalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeCreate + 1);
        AssetDisposal testAssetDisposal = assetDisposalList.get(assetDisposalList.size() - 1);
        assertThat(testAssetDisposal.getAssetDisposalReference()).isEqualTo(DEFAULT_ASSET_DISPOSAL_REFERENCE);
        assertThat(testAssetDisposal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetDisposal.getAssetCost()).isEqualByComparingTo(DEFAULT_ASSET_COST);
        assertThat(testAssetDisposal.getHistoricalCost()).isEqualByComparingTo(DEFAULT_HISTORICAL_COST);
        assertThat(testAssetDisposal.getAccruedDepreciation()).isEqualByComparingTo(DEFAULT_ACCRUED_DEPRECIATION);
        assertThat(testAssetDisposal.getNetBookValue()).isEqualByComparingTo(DEFAULT_NET_BOOK_VALUE);
        assertThat(testAssetDisposal.getDecommissioningDate()).isEqualTo(DEFAULT_DECOMMISSIONING_DATE);
        assertThat(testAssetDisposal.getDisposalDate()).isEqualTo(DEFAULT_DISPOSAL_DATE);
        assertThat(testAssetDisposal.getDormant()).isEqualTo(DEFAULT_DORMANT);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(1)).save(testAssetDisposal);
    }

    @Test
    @Transactional
    void createAssetDisposalWithExistingId() throws Exception {
        // Create the AssetDisposal with an existing ID
        assetDisposal.setId(1L);
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        int databaseSizeBeforeCreate = assetDisposalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetDisposalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void checkAccruedDepreciationIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetDisposalRepository.findAll().size();
        // set the field null
        assetDisposal.setAccruedDepreciation(null);

        // Create the AssetDisposal, which fails.
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        restAssetDisposalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNetBookValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetDisposalRepository.findAll().size();
        // set the field null
        assetDisposal.setNetBookValue(null);

        // Create the AssetDisposal, which fails.
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        restAssetDisposalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisposalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetDisposalRepository.findAll().size();
        // set the field null
        assetDisposal.setDisposalDate(null);

        // Create the AssetDisposal, which fails.
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        restAssetDisposalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetDisposals() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDisposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetDisposalReference").value(hasItem(DEFAULT_ASSET_DISPOSAL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].accruedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].decommissioningDate").value(hasItem(DEFAULT_DECOMMISSIONING_DATE.toString())))
            .andExpect(jsonPath("$.[*].disposalDate").value(hasItem(DEFAULT_DISPOSAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].dormant").value(hasItem(DEFAULT_DORMANT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetDisposalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetDisposalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetDisposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetDisposalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetDisposalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetDisposalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetDisposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetDisposalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetDisposal() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get the assetDisposal
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL_ID, assetDisposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetDisposal.getId().intValue()))
            .andExpect(jsonPath("$.assetDisposalReference").value(DEFAULT_ASSET_DISPOSAL_REFERENCE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.assetCost").value(sameNumber(DEFAULT_ASSET_COST)))
            .andExpect(jsonPath("$.historicalCost").value(sameNumber(DEFAULT_HISTORICAL_COST)))
            .andExpect(jsonPath("$.accruedDepreciation").value(sameNumber(DEFAULT_ACCRUED_DEPRECIATION)))
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)))
            .andExpect(jsonPath("$.decommissioningDate").value(DEFAULT_DECOMMISSIONING_DATE.toString()))
            .andExpect(jsonPath("$.disposalDate").value(DEFAULT_DISPOSAL_DATE.toString()))
            .andExpect(jsonPath("$.dormant").value(DEFAULT_DORMANT.booleanValue()));
    }

    @Test
    @Transactional
    void getAssetDisposalsByIdFiltering() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        Long id = assetDisposal.getId();

        defaultAssetDisposalShouldBeFound("id.equals=" + id);
        defaultAssetDisposalShouldNotBeFound("id.notEquals=" + id);

        defaultAssetDisposalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetDisposalShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetDisposalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetDisposalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetDisposalReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetDisposalReference equals to DEFAULT_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldBeFound("assetDisposalReference.equals=" + DEFAULT_ASSET_DISPOSAL_REFERENCE);

        // Get all the assetDisposalList where assetDisposalReference equals to UPDATED_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldNotBeFound("assetDisposalReference.equals=" + UPDATED_ASSET_DISPOSAL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetDisposalReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetDisposalReference not equals to DEFAULT_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldNotBeFound("assetDisposalReference.notEquals=" + DEFAULT_ASSET_DISPOSAL_REFERENCE);

        // Get all the assetDisposalList where assetDisposalReference not equals to UPDATED_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldBeFound("assetDisposalReference.notEquals=" + UPDATED_ASSET_DISPOSAL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetDisposalReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetDisposalReference in DEFAULT_ASSET_DISPOSAL_REFERENCE or UPDATED_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldBeFound(
            "assetDisposalReference.in=" + DEFAULT_ASSET_DISPOSAL_REFERENCE + "," + UPDATED_ASSET_DISPOSAL_REFERENCE
        );

        // Get all the assetDisposalList where assetDisposalReference equals to UPDATED_ASSET_DISPOSAL_REFERENCE
        defaultAssetDisposalShouldNotBeFound("assetDisposalReference.in=" + UPDATED_ASSET_DISPOSAL_REFERENCE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetDisposalReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetDisposalReference is not null
        defaultAssetDisposalShouldBeFound("assetDisposalReference.specified=true");

        // Get all the assetDisposalList where assetDisposalReference is null
        defaultAssetDisposalShouldNotBeFound("assetDisposalReference.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description equals to DEFAULT_DESCRIPTION
        defaultAssetDisposalShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetDisposalList where description equals to UPDATED_DESCRIPTION
        defaultAssetDisposalShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetDisposalShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetDisposalList where description not equals to UPDATED_DESCRIPTION
        defaultAssetDisposalShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetDisposalShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetDisposalList where description equals to UPDATED_DESCRIPTION
        defaultAssetDisposalShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description is not null
        defaultAssetDisposalShouldBeFound("description.specified=true");

        // Get all the assetDisposalList where description is null
        defaultAssetDisposalShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description contains DEFAULT_DESCRIPTION
        defaultAssetDisposalShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetDisposalList where description contains UPDATED_DESCRIPTION
        defaultAssetDisposalShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetDisposalShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetDisposalList where description does not contain UPDATED_DESCRIPTION
        defaultAssetDisposalShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost equals to DEFAULT_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.equals=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.equals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost not equals to DEFAULT_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.notEquals=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost not equals to UPDATED_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.notEquals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost in DEFAULT_ASSET_COST or UPDATED_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.in=" + DEFAULT_ASSET_COST + "," + UPDATED_ASSET_COST);

        // Get all the assetDisposalList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.in=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost is not null
        defaultAssetDisposalShouldBeFound("assetCost.specified=true");

        // Get all the assetDisposalList where assetCost is null
        defaultAssetDisposalShouldNotBeFound("assetCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost is greater than or equal to DEFAULT_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.greaterThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost is greater than or equal to UPDATED_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.greaterThanOrEqual=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost is less than or equal to DEFAULT_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.lessThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost is less than or equal to SMALLER_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.lessThanOrEqual=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost is less than DEFAULT_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.lessThan=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost is less than UPDATED_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.lessThan=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where assetCost is greater than DEFAULT_ASSET_COST
        defaultAssetDisposalShouldNotBeFound("assetCost.greaterThan=" + DEFAULT_ASSET_COST);

        // Get all the assetDisposalList where assetCost is greater than SMALLER_ASSET_COST
        defaultAssetDisposalShouldBeFound("assetCost.greaterThan=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost equals to DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.equals=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.equals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost not equals to DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.notEquals=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost not equals to UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.notEquals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost in DEFAULT_HISTORICAL_COST or UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.in=" + DEFAULT_HISTORICAL_COST + "," + UPDATED_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.in=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost is not null
        defaultAssetDisposalShouldBeFound("historicalCost.specified=true");

        // Get all the assetDisposalList where historicalCost is null
        defaultAssetDisposalShouldNotBeFound("historicalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost is greater than or equal to DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.greaterThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost is greater than or equal to UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.greaterThanOrEqual=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost is less than or equal to DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.lessThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost is less than or equal to SMALLER_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.lessThanOrEqual=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost is less than DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.lessThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost is less than UPDATED_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.lessThan=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByHistoricalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where historicalCost is greater than DEFAULT_HISTORICAL_COST
        defaultAssetDisposalShouldNotBeFound("historicalCost.greaterThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the assetDisposalList where historicalCost is greater than SMALLER_HISTORICAL_COST
        defaultAssetDisposalShouldBeFound("historicalCost.greaterThan=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation equals to DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.equals=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation equals to UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.equals=" + UPDATED_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation not equals to DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.notEquals=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation not equals to UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.notEquals=" + UPDATED_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation in DEFAULT_ACCRUED_DEPRECIATION or UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.in=" + DEFAULT_ACCRUED_DEPRECIATION + "," + UPDATED_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation equals to UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.in=" + UPDATED_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation is not null
        defaultAssetDisposalShouldBeFound("accruedDepreciation.specified=true");

        // Get all the assetDisposalList where accruedDepreciation is null
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation is greater than or equal to DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.greaterThanOrEqual=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation is greater than or equal to UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.greaterThanOrEqual=" + UPDATED_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation is less than or equal to DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.lessThanOrEqual=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation is less than or equal to SMALLER_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.lessThanOrEqual=" + SMALLER_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation is less than DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.lessThan=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation is less than UPDATED_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.lessThan=" + UPDATED_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAccruedDepreciationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where accruedDepreciation is greater than DEFAULT_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldNotBeFound("accruedDepreciation.greaterThan=" + DEFAULT_ACCRUED_DEPRECIATION);

        // Get all the assetDisposalList where accruedDepreciation is greater than SMALLER_ACCRUED_DEPRECIATION
        defaultAssetDisposalShouldBeFound("accruedDepreciation.greaterThan=" + SMALLER_ACCRUED_DEPRECIATION);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue is not null
        defaultAssetDisposalShouldBeFound("netBookValue.specified=true");

        // Get all the assetDisposalList where netBookValue is null
        defaultAssetDisposalShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultAssetDisposalShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the assetDisposalList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultAssetDisposalShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate equals to DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.equals=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate equals to UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.equals=" + UPDATED_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate not equals to DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.notEquals=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate not equals to UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.notEquals=" + UPDATED_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate in DEFAULT_DECOMMISSIONING_DATE or UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.in=" + DEFAULT_DECOMMISSIONING_DATE + "," + UPDATED_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate equals to UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.in=" + UPDATED_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate is not null
        defaultAssetDisposalShouldBeFound("decommissioningDate.specified=true");

        // Get all the assetDisposalList where decommissioningDate is null
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate is greater than or equal to DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.greaterThanOrEqual=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate is greater than or equal to UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.greaterThanOrEqual=" + UPDATED_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate is less than or equal to DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.lessThanOrEqual=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate is less than or equal to SMALLER_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.lessThanOrEqual=" + SMALLER_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate is less than DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.lessThan=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate is less than UPDATED_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.lessThan=" + UPDATED_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDecommissioningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where decommissioningDate is greater than DEFAULT_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldNotBeFound("decommissioningDate.greaterThan=" + DEFAULT_DECOMMISSIONING_DATE);

        // Get all the assetDisposalList where decommissioningDate is greater than SMALLER_DECOMMISSIONING_DATE
        defaultAssetDisposalShouldBeFound("decommissioningDate.greaterThan=" + SMALLER_DECOMMISSIONING_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate equals to DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.equals=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate equals to UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.equals=" + UPDATED_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate not equals to DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.notEquals=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate not equals to UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.notEquals=" + UPDATED_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate in DEFAULT_DISPOSAL_DATE or UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.in=" + DEFAULT_DISPOSAL_DATE + "," + UPDATED_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate equals to UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.in=" + UPDATED_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate is not null
        defaultAssetDisposalShouldBeFound("disposalDate.specified=true");

        // Get all the assetDisposalList where disposalDate is null
        defaultAssetDisposalShouldNotBeFound("disposalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate is greater than or equal to DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.greaterThanOrEqual=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate is greater than or equal to UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.greaterThanOrEqual=" + UPDATED_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate is less than or equal to DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.lessThanOrEqual=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate is less than or equal to SMALLER_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.lessThanOrEqual=" + SMALLER_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate is less than DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.lessThan=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate is less than UPDATED_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.lessThan=" + UPDATED_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDisposalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where disposalDate is greater than DEFAULT_DISPOSAL_DATE
        defaultAssetDisposalShouldNotBeFound("disposalDate.greaterThan=" + DEFAULT_DISPOSAL_DATE);

        // Get all the assetDisposalList where disposalDate is greater than SMALLER_DISPOSAL_DATE
        defaultAssetDisposalShouldBeFound("disposalDate.greaterThan=" + SMALLER_DISPOSAL_DATE);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDormantIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where dormant equals to DEFAULT_DORMANT
        defaultAssetDisposalShouldBeFound("dormant.equals=" + DEFAULT_DORMANT);

        // Get all the assetDisposalList where dormant equals to UPDATED_DORMANT
        defaultAssetDisposalShouldNotBeFound("dormant.equals=" + UPDATED_DORMANT);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDormantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where dormant not equals to DEFAULT_DORMANT
        defaultAssetDisposalShouldNotBeFound("dormant.notEquals=" + DEFAULT_DORMANT);

        // Get all the assetDisposalList where dormant not equals to UPDATED_DORMANT
        defaultAssetDisposalShouldBeFound("dormant.notEquals=" + UPDATED_DORMANT);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDormantIsInShouldWork() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where dormant in DEFAULT_DORMANT or UPDATED_DORMANT
        defaultAssetDisposalShouldBeFound("dormant.in=" + DEFAULT_DORMANT + "," + UPDATED_DORMANT);

        // Get all the assetDisposalList where dormant equals to UPDATED_DORMANT
        defaultAssetDisposalShouldNotBeFound("dormant.in=" + UPDATED_DORMANT);
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByDormantIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        // Get all the assetDisposalList where dormant is not null
        defaultAssetDisposalShouldBeFound("dormant.specified=true");

        // Get all the assetDisposalList where dormant is null
        defaultAssetDisposalShouldNotBeFound("dormant.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
        ApplicationUser createdBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            createdBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(createdBy);
            em.flush();
        } else {
            createdBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        assetDisposal.setCreatedBy(createdBy);
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long createdById = createdBy.getId();

        // Get all the assetDisposalList where createdBy equals to createdById
        defaultAssetDisposalShouldBeFound("createdById.equals=" + createdById);

        // Get all the assetDisposalList where createdBy equals to (createdById + 1)
        defaultAssetDisposalShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
        ApplicationUser modifiedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            modifiedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(modifiedBy);
            em.flush();
        } else {
            modifiedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(modifiedBy);
        em.flush();
        assetDisposal.setModifiedBy(modifiedBy);
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long modifiedById = modifiedBy.getId();

        // Get all the assetDisposalList where modifiedBy equals to modifiedById
        defaultAssetDisposalShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the assetDisposalList where modifiedBy equals to (modifiedById + 1)
        defaultAssetDisposalShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
        ApplicationUser lastAccessedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastAccessedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastAccessedBy);
            em.flush();
        } else {
            lastAccessedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastAccessedBy);
        em.flush();
        assetDisposal.setLastAccessedBy(lastAccessedBy);
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the assetDisposalList where lastAccessedBy equals to lastAccessedById
        defaultAssetDisposalShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the assetDisposalList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultAssetDisposalShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByEffectivePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
        DepreciationPeriod effectivePeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            effectivePeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(effectivePeriod);
            em.flush();
        } else {
            effectivePeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(effectivePeriod);
        em.flush();
        assetDisposal.setEffectivePeriod(effectivePeriod);
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long effectivePeriodId = effectivePeriod.getId();

        // Get all the assetDisposalList where effectivePeriod equals to effectivePeriodId
        defaultAssetDisposalShouldBeFound("effectivePeriodId.equals=" + effectivePeriodId);

        // Get all the assetDisposalList where effectivePeriod equals to (effectivePeriodId + 1)
        defaultAssetDisposalShouldNotBeFound("effectivePeriodId.equals=" + (effectivePeriodId + 1));
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
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
        assetDisposal.addPlaceholder(placeholder);
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long placeholderId = placeholder.getId();

        // Get all the assetDisposalList where placeholder equals to placeholderId
        defaultAssetDisposalShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetDisposalList where placeholder equals to (placeholderId + 1)
        defaultAssetDisposalShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetDisposalsByAssetDisposedIsEqualToSomething() throws Exception {
        // Get already existing entity
        AssetRegistration assetDisposed = assetDisposal.getAssetDisposed();
        assetDisposalRepository.saveAndFlush(assetDisposal);
        Long assetDisposedId = assetDisposed.getId();

        // Get all the assetDisposalList where assetDisposed equals to assetDisposedId
        defaultAssetDisposalShouldBeFound("assetDisposedId.equals=" + assetDisposedId);

        // Get all the assetDisposalList where assetDisposed equals to (assetDisposedId + 1)
        defaultAssetDisposalShouldNotBeFound("assetDisposedId.equals=" + (assetDisposedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetDisposalShouldBeFound(String filter) throws Exception {
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDisposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetDisposalReference").value(hasItem(DEFAULT_ASSET_DISPOSAL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].accruedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].decommissioningDate").value(hasItem(DEFAULT_DECOMMISSIONING_DATE.toString())))
            .andExpect(jsonPath("$.[*].disposalDate").value(hasItem(DEFAULT_DISPOSAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].dormant").value(hasItem(DEFAULT_DORMANT.booleanValue())));

        // Check, that the count call also returns 1
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetDisposalShouldNotBeFound(String filter) throws Exception {
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetDisposalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetDisposal() throws Exception {
        // Get the assetDisposal
        restAssetDisposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetDisposal() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();

        // Update the assetDisposal
        AssetDisposal updatedAssetDisposal = assetDisposalRepository.findById(assetDisposal.getId()).get();
        // Disconnect from session so that the updates on updatedAssetDisposal are not directly saved in db
        em.detach(updatedAssetDisposal);
        updatedAssetDisposal
            .assetDisposalReference(UPDATED_ASSET_DISPOSAL_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .assetCost(UPDATED_ASSET_COST)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .accruedDepreciation(UPDATED_ACCRUED_DEPRECIATION)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .decommissioningDate(UPDATED_DECOMMISSIONING_DATE)
            .disposalDate(UPDATED_DISPOSAL_DATE)
            .dormant(UPDATED_DORMANT);
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(updatedAssetDisposal);

        restAssetDisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDisposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);
        AssetDisposal testAssetDisposal = assetDisposalList.get(assetDisposalList.size() - 1);
        assertThat(testAssetDisposal.getAssetDisposalReference()).isEqualTo(UPDATED_ASSET_DISPOSAL_REFERENCE);
        assertThat(testAssetDisposal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetDisposal.getAssetCost()).isEqualTo(UPDATED_ASSET_COST);
        assertThat(testAssetDisposal.getHistoricalCost()).isEqualTo(UPDATED_HISTORICAL_COST);
        assertThat(testAssetDisposal.getAccruedDepreciation()).isEqualTo(UPDATED_ACCRUED_DEPRECIATION);
        assertThat(testAssetDisposal.getNetBookValue()).isEqualTo(UPDATED_NET_BOOK_VALUE);
        assertThat(testAssetDisposal.getDecommissioningDate()).isEqualTo(UPDATED_DECOMMISSIONING_DATE);
        assertThat(testAssetDisposal.getDisposalDate()).isEqualTo(UPDATED_DISPOSAL_DATE);
        assertThat(testAssetDisposal.getDormant()).isEqualTo(UPDATED_DORMANT);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository).save(testAssetDisposal);
    }

    @Test
    @Transactional
    void putNonExistingAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDisposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void partialUpdateAssetDisposalWithPatch() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();

        // Update the assetDisposal using partial update
        AssetDisposal partialUpdatedAssetDisposal = new AssetDisposal();
        partialUpdatedAssetDisposal.setId(assetDisposal.getId());

        partialUpdatedAssetDisposal
            .assetDisposalReference(UPDATED_ASSET_DISPOSAL_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .accruedDepreciation(UPDATED_ACCRUED_DEPRECIATION)
            .disposalDate(UPDATED_DISPOSAL_DATE);

        restAssetDisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDisposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDisposal))
            )
            .andExpect(status().isOk());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);
        AssetDisposal testAssetDisposal = assetDisposalList.get(assetDisposalList.size() - 1);
        assertThat(testAssetDisposal.getAssetDisposalReference()).isEqualTo(UPDATED_ASSET_DISPOSAL_REFERENCE);
        assertThat(testAssetDisposal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetDisposal.getAssetCost()).isEqualByComparingTo(DEFAULT_ASSET_COST);
        assertThat(testAssetDisposal.getHistoricalCost()).isEqualByComparingTo(UPDATED_HISTORICAL_COST);
        assertThat(testAssetDisposal.getAccruedDepreciation()).isEqualByComparingTo(UPDATED_ACCRUED_DEPRECIATION);
        assertThat(testAssetDisposal.getNetBookValue()).isEqualByComparingTo(DEFAULT_NET_BOOK_VALUE);
        assertThat(testAssetDisposal.getDecommissioningDate()).isEqualTo(DEFAULT_DECOMMISSIONING_DATE);
        assertThat(testAssetDisposal.getDisposalDate()).isEqualTo(UPDATED_DISPOSAL_DATE);
        assertThat(testAssetDisposal.getDormant()).isEqualTo(DEFAULT_DORMANT);
    }

    @Test
    @Transactional
    void fullUpdateAssetDisposalWithPatch() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();

        // Update the assetDisposal using partial update
        AssetDisposal partialUpdatedAssetDisposal = new AssetDisposal();
        partialUpdatedAssetDisposal.setId(assetDisposal.getId());

        partialUpdatedAssetDisposal
            .assetDisposalReference(UPDATED_ASSET_DISPOSAL_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .assetCost(UPDATED_ASSET_COST)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .accruedDepreciation(UPDATED_ACCRUED_DEPRECIATION)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .decommissioningDate(UPDATED_DECOMMISSIONING_DATE)
            .disposalDate(UPDATED_DISPOSAL_DATE)
            .dormant(UPDATED_DORMANT);

        restAssetDisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDisposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetDisposal))
            )
            .andExpect(status().isOk());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);
        AssetDisposal testAssetDisposal = assetDisposalList.get(assetDisposalList.size() - 1);
        assertThat(testAssetDisposal.getAssetDisposalReference()).isEqualTo(UPDATED_ASSET_DISPOSAL_REFERENCE);
        assertThat(testAssetDisposal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetDisposal.getAssetCost()).isEqualByComparingTo(UPDATED_ASSET_COST);
        assertThat(testAssetDisposal.getHistoricalCost()).isEqualByComparingTo(UPDATED_HISTORICAL_COST);
        assertThat(testAssetDisposal.getAccruedDepreciation()).isEqualByComparingTo(UPDATED_ACCRUED_DEPRECIATION);
        assertThat(testAssetDisposal.getNetBookValue()).isEqualByComparingTo(UPDATED_NET_BOOK_VALUE);
        assertThat(testAssetDisposal.getDecommissioningDate()).isEqualTo(UPDATED_DECOMMISSIONING_DATE);
        assertThat(testAssetDisposal.getDisposalDate()).isEqualTo(UPDATED_DISPOSAL_DATE);
        assertThat(testAssetDisposal.getDormant()).isEqualTo(UPDATED_DORMANT);
    }

    @Test
    @Transactional
    void patchNonExistingAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDisposalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetDisposal() throws Exception {
        int databaseSizeBeforeUpdate = assetDisposalRepository.findAll().size();
        assetDisposal.setId(count.incrementAndGet());

        // Create the AssetDisposal
        AssetDisposalDTO assetDisposalDTO = assetDisposalMapper.toDto(assetDisposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDisposalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDisposalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDisposal in the database
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(0)).save(assetDisposal);
    }

    @Test
    @Transactional
    void deleteAssetDisposal() throws Exception {
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);

        int databaseSizeBeforeDelete = assetDisposalRepository.findAll().size();

        // Delete the assetDisposal
        restAssetDisposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetDisposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetDisposal> assetDisposalList = assetDisposalRepository.findAll();
        assertThat(assetDisposalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetDisposal in Elasticsearch
        verify(mockAssetDisposalSearchRepository, times(1)).deleteById(assetDisposal.getId());
    }

    @Test
    @Transactional
    void searchAssetDisposal() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetDisposalRepository.saveAndFlush(assetDisposal);
        when(mockAssetDisposalSearchRepository.search("id:" + assetDisposal.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetDisposal), PageRequest.of(0, 1), 1));

        // Search the assetDisposal
        restAssetDisposalMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetDisposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDisposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetDisposalReference").value(hasItem(DEFAULT_ASSET_DISPOSAL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].accruedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCRUED_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].decommissioningDate").value(hasItem(DEFAULT_DECOMMISSIONING_DATE.toString())))
            .andExpect(jsonPath("$.[*].disposalDate").value(hasItem(DEFAULT_DISPOSAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].dormant").value(hasItem(DEFAULT_DORMANT.booleanValue())));
    }
}
