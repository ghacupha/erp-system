package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.9.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.DeliveryNote;
import io.github.erp.domain.JobSheet;
import io.github.erp.domain.PaymentInvoice;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.PurchaseOrder;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.domain.Settlement;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.criteria.AssetRegistrationCriteria;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.mapper.AssetRegistrationMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AssetRegistrationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetRegistrationResourceIT {

    private static final String DEFAULT_ASSET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DETAILS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ASSET_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSET_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSET_COST = new BigDecimal(1 - 1);

    private static final byte[] DEFAULT_COMMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMMENTS_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/asset-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-registrations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetRegistrationRepository assetRegistrationRepository;

    @Mock
    private AssetRegistrationRepository assetRegistrationRepositoryMock;

    @Autowired
    private AssetRegistrationMapper assetRegistrationMapper;

    @Mock
    private AssetRegistrationService assetRegistrationServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetRegistrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetRegistrationSearchRepository mockAssetRegistrationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetRegistrationMockMvc;

    private AssetRegistration assetRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRegistration createEntity(EntityManager em) {
        AssetRegistration assetRegistration = new AssetRegistration()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .assetTag(DEFAULT_ASSET_TAG)
            .assetDetails(DEFAULT_ASSET_DETAILS)
            .assetCost(DEFAULT_ASSET_COST)
            .comments(DEFAULT_COMMENTS)
            .commentsContentType(DEFAULT_COMMENTS_CONTENT_TYPE);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        assetRegistration.getServiceOutlets().add(serviceOutlet);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        assetRegistration.getSettlements().add(settlement);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        assetRegistration.setAssetCategory(assetCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetRegistration.setDealer(dealer);
        return assetRegistration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRegistration createUpdatedEntity(EntityManager em) {
        AssetRegistration assetRegistration = new AssetRegistration()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .assetCost(UPDATED_ASSET_COST)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createUpdatedEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        assetRegistration.getServiceOutlets().add(serviceOutlet);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createUpdatedEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        assetRegistration.getSettlements().add(settlement);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createUpdatedEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        assetRegistration.setAssetCategory(assetCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetRegistration.setDealer(dealer);
        return assetRegistration;
    }

    @BeforeEach
    public void initTest() {
        assetRegistration = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetRegistration() throws Exception {
        int databaseSizeBeforeCreate = assetRegistrationRepository.findAll().size();
        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);
        restAssetRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        AssetRegistration testAssetRegistration = assetRegistrationList.get(assetRegistrationList.size() - 1);
        assertThat(testAssetRegistration.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testAssetRegistration.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAssetRegistration.getAssetDetails()).isEqualTo(DEFAULT_ASSET_DETAILS);
        assertThat(testAssetRegistration.getAssetCost()).isEqualByComparingTo(DEFAULT_ASSET_COST);
        assertThat(testAssetRegistration.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testAssetRegistration.getCommentsContentType()).isEqualTo(DEFAULT_COMMENTS_CONTENT_TYPE);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(1)).save(testAssetRegistration);
    }

    @Test
    @Transactional
    void createAssetRegistrationWithExistingId() throws Exception {
        // Create the AssetRegistration with an existing ID
        assetRegistration.setId(1L);
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        int databaseSizeBeforeCreate = assetRegistrationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void checkAssetNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRegistrationRepository.findAll().size();
        // set the field null
        assetRegistration.setAssetNumber(null);

        // Create the AssetRegistration, which fails.
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        restAssetRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssetTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRegistrationRepository.findAll().size();
        // set the field null
        assetRegistration.setAssetTag(null);

        // Create the AssetRegistration, which fails.
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        restAssetRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssetCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRegistrationRepository.findAll().size();
        // set the field null
        assetRegistration.setAssetCost(null);

        // Create the AssetRegistration, which fails.
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        restAssetRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetRegistrations() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetRegistrationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetRegistrationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetRegistrationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetRegistrationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetRegistration() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get the assetRegistration
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL_ID, assetRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetRegistration.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDetails").value(DEFAULT_ASSET_DETAILS))
            .andExpect(jsonPath("$.assetCost").value(sameNumber(DEFAULT_ASSET_COST)))
            .andExpect(jsonPath("$.commentsContentType").value(DEFAULT_COMMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.comments").value(Base64Utils.encodeToString(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getAssetRegistrationsByIdFiltering() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        Long id = assetRegistration.getId();

        defaultAssetRegistrationShouldBeFound("id.equals=" + id);
        defaultAssetRegistrationShouldNotBeFound("id.notEquals=" + id);

        defaultAssetRegistrationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetRegistrationShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetRegistrationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetRegistrationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultAssetRegistrationShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetRegistrationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultAssetRegistrationShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetRegistrationList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the assetRegistrationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber is not null
        defaultAssetRegistrationShouldBeFound("assetNumber.specified=true");

        // Get all the assetRegistrationList where assetNumber is null
        defaultAssetRegistrationShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber contains DEFAULT_ASSET_NUMBER
        defaultAssetRegistrationShouldBeFound("assetNumber.contains=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetRegistrationList where assetNumber contains UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldNotBeFound("assetNumber.contains=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetNumber does not contain DEFAULT_ASSET_NUMBER
        defaultAssetRegistrationShouldNotBeFound("assetNumber.doesNotContain=" + DEFAULT_ASSET_NUMBER);

        // Get all the assetRegistrationList where assetNumber does not contain UPDATED_ASSET_NUMBER
        defaultAssetRegistrationShouldBeFound("assetNumber.doesNotContain=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag equals to DEFAULT_ASSET_TAG
        defaultAssetRegistrationShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the assetRegistrationList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultAssetRegistrationShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the assetRegistrationList where assetTag not equals to UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the assetRegistrationList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag is not null
        defaultAssetRegistrationShouldBeFound("assetTag.specified=true");

        // Get all the assetRegistrationList where assetTag is null
        defaultAssetRegistrationShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag contains DEFAULT_ASSET_TAG
        defaultAssetRegistrationShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the assetRegistrationList where assetTag contains UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultAssetRegistrationShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the assetRegistrationList where assetTag does not contain UPDATED_ASSET_TAG
        defaultAssetRegistrationShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails equals to DEFAULT_ASSET_DETAILS
        defaultAssetRegistrationShouldBeFound("assetDetails.equals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetRegistrationList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldNotBeFound("assetDetails.equals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails not equals to DEFAULT_ASSET_DETAILS
        defaultAssetRegistrationShouldNotBeFound("assetDetails.notEquals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetRegistrationList where assetDetails not equals to UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldBeFound("assetDetails.notEquals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails in DEFAULT_ASSET_DETAILS or UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldBeFound("assetDetails.in=" + DEFAULT_ASSET_DETAILS + "," + UPDATED_ASSET_DETAILS);

        // Get all the assetRegistrationList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldNotBeFound("assetDetails.in=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails is not null
        defaultAssetRegistrationShouldBeFound("assetDetails.specified=true");

        // Get all the assetRegistrationList where assetDetails is null
        defaultAssetRegistrationShouldNotBeFound("assetDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails contains DEFAULT_ASSET_DETAILS
        defaultAssetRegistrationShouldBeFound("assetDetails.contains=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetRegistrationList where assetDetails contains UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldNotBeFound("assetDetails.contains=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetDetails does not contain DEFAULT_ASSET_DETAILS
        defaultAssetRegistrationShouldNotBeFound("assetDetails.doesNotContain=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetRegistrationList where assetDetails does not contain UPDATED_ASSET_DETAILS
        defaultAssetRegistrationShouldBeFound("assetDetails.doesNotContain=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost equals to DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.equals=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.equals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost not equals to DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.notEquals=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost not equals to UPDATED_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.notEquals=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsInShouldWork() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost in DEFAULT_ASSET_COST or UPDATED_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.in=" + DEFAULT_ASSET_COST + "," + UPDATED_ASSET_COST);

        // Get all the assetRegistrationList where assetCost equals to UPDATED_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.in=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost is not null
        defaultAssetRegistrationShouldBeFound("assetCost.specified=true");

        // Get all the assetRegistrationList where assetCost is null
        defaultAssetRegistrationShouldNotBeFound("assetCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost is greater than or equal to DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.greaterThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost is greater than or equal to UPDATED_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.greaterThanOrEqual=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost is less than or equal to DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.lessThanOrEqual=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost is less than or equal to SMALLER_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.lessThanOrEqual=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost is less than DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.lessThan=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost is less than UPDATED_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.lessThan=" + UPDATED_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        // Get all the assetRegistrationList where assetCost is greater than DEFAULT_ASSET_COST
        defaultAssetRegistrationShouldNotBeFound("assetCost.greaterThan=" + DEFAULT_ASSET_COST);

        // Get all the assetRegistrationList where assetCost is greater than SMALLER_ASSET_COST
        defaultAssetRegistrationShouldBeFound("assetCost.greaterThan=" + SMALLER_ASSET_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
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
        assetRegistration.addPlaceholder(placeholder);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long placeholderId = placeholder.getId();

        // Get all the assetRegistrationList where placeholder equals to placeholderId
        defaultAssetRegistrationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetRegistrationList where placeholder equals to (placeholderId + 1)
        defaultAssetRegistrationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByPaymentInvoicesIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        PaymentInvoice paymentInvoices;
        if (TestUtil.findAll(em, PaymentInvoice.class).isEmpty()) {
            paymentInvoices = PaymentInvoiceResourceIT.createEntity(em);
            em.persist(paymentInvoices);
            em.flush();
        } else {
            paymentInvoices = TestUtil.findAll(em, PaymentInvoice.class).get(0);
        }
        em.persist(paymentInvoices);
        em.flush();
        assetRegistration.addPaymentInvoices(paymentInvoices);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long paymentInvoicesId = paymentInvoices.getId();

        // Get all the assetRegistrationList where paymentInvoices equals to paymentInvoicesId
        defaultAssetRegistrationShouldBeFound("paymentInvoicesId.equals=" + paymentInvoicesId);

        // Get all the assetRegistrationList where paymentInvoices equals to (paymentInvoicesId + 1)
        defaultAssetRegistrationShouldNotBeFound("paymentInvoicesId.equals=" + (paymentInvoicesId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(serviceOutlet);
        em.flush();
        assetRegistration.addServiceOutlet(serviceOutlet);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the assetRegistrationList where serviceOutlet equals to serviceOutletId
        defaultAssetRegistrationShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the assetRegistrationList where serviceOutlet equals to (serviceOutletId + 1)
        defaultAssetRegistrationShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsBySettlementIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        em.persist(settlement);
        em.flush();
        assetRegistration.addSettlement(settlement);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long settlementId = settlement.getId();

        // Get all the assetRegistrationList where settlement equals to settlementId
        defaultAssetRegistrationShouldBeFound("settlementId.equals=" + settlementId);

        // Get all the assetRegistrationList where settlement equals to (settlementId + 1)
        defaultAssetRegistrationShouldNotBeFound("settlementId.equals=" + (settlementId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
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
        assetRegistration.setAssetCategory(assetCategory);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long assetCategoryId = assetCategory.getId();

        // Get all the assetRegistrationList where assetCategory equals to assetCategoryId
        defaultAssetRegistrationShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the assetRegistrationList where assetCategory equals to (assetCategoryId + 1)
        defaultAssetRegistrationShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        assetRegistration.addPurchaseOrder(purchaseOrder);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the assetRegistrationList where purchaseOrder equals to purchaseOrderId
        defaultAssetRegistrationShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the assetRegistrationList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultAssetRegistrationShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByDeliveryNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        DeliveryNote deliveryNote;
        if (TestUtil.findAll(em, DeliveryNote.class).isEmpty()) {
            deliveryNote = DeliveryNoteResourceIT.createEntity(em);
            em.persist(deliveryNote);
            em.flush();
        } else {
            deliveryNote = TestUtil.findAll(em, DeliveryNote.class).get(0);
        }
        em.persist(deliveryNote);
        em.flush();
        assetRegistration.addDeliveryNote(deliveryNote);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long deliveryNoteId = deliveryNote.getId();

        // Get all the assetRegistrationList where deliveryNote equals to deliveryNoteId
        defaultAssetRegistrationShouldBeFound("deliveryNoteId.equals=" + deliveryNoteId);

        // Get all the assetRegistrationList where deliveryNote equals to (deliveryNoteId + 1)
        defaultAssetRegistrationShouldNotBeFound("deliveryNoteId.equals=" + (deliveryNoteId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByJobSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        JobSheet jobSheet;
        if (TestUtil.findAll(em, JobSheet.class).isEmpty()) {
            jobSheet = JobSheetResourceIT.createEntity(em);
            em.persist(jobSheet);
            em.flush();
        } else {
            jobSheet = TestUtil.findAll(em, JobSheet.class).get(0);
        }
        em.persist(jobSheet);
        em.flush();
        assetRegistration.addJobSheet(jobSheet);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long jobSheetId = jobSheet.getId();

        // Get all the assetRegistrationList where jobSheet equals to jobSheetId
        defaultAssetRegistrationShouldBeFound("jobSheetId.equals=" + jobSheetId);

        // Get all the assetRegistrationList where jobSheet equals to (jobSheetId + 1)
        defaultAssetRegistrationShouldNotBeFound("jobSheetId.equals=" + (jobSheetId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        assetRegistration.setDealer(dealer);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long dealerId = dealer.getId();

        // Get all the assetRegistrationList where dealer equals to dealerId
        defaultAssetRegistrationShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the assetRegistrationList where dealer equals to (dealerId + 1)
        defaultAssetRegistrationShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByDesignatedUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Dealer designatedUsers;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            designatedUsers = DealerResourceIT.createEntity(em);
            em.persist(designatedUsers);
            em.flush();
        } else {
            designatedUsers = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(designatedUsers);
        em.flush();
        assetRegistration.addDesignatedUsers(designatedUsers);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long designatedUsersId = designatedUsers.getId();

        // Get all the assetRegistrationList where designatedUsers equals to designatedUsersId
        defaultAssetRegistrationShouldBeFound("designatedUsersId.equals=" + designatedUsersId);

        // Get all the assetRegistrationList where designatedUsers equals to (designatedUsersId + 1)
        defaultAssetRegistrationShouldNotBeFound("designatedUsersId.equals=" + (designatedUsersId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        assetRegistration.setSettlementCurrency(settlementCurrency);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the assetRegistrationList where settlementCurrency equals to settlementCurrencyId
        defaultAssetRegistrationShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the assetRegistrationList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultAssetRegistrationShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRegistrationsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        assetRegistration.addBusinessDocument(businessDocument);
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        Long businessDocumentId = businessDocument.getId();

        // Get all the assetRegistrationList where businessDocument equals to businessDocumentId
        defaultAssetRegistrationShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the assetRegistrationList where businessDocument equals to (businessDocumentId + 1)
        defaultAssetRegistrationShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetRegistrationShouldBeFound(String filter) throws Exception {
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))));

        // Check, that the count call also returns 1
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetRegistrationShouldNotBeFound(String filter) throws Exception {
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetRegistration() throws Exception {
        // Get the assetRegistration
        restAssetRegistrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetRegistration() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();

        // Update the assetRegistration
        AssetRegistration updatedAssetRegistration = assetRegistrationRepository.findById(assetRegistration.getId()).get();
        // Disconnect from session so that the updates on updatedAssetRegistration are not directly saved in db
        em.detach(updatedAssetRegistration);
        updatedAssetRegistration
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .assetCost(UPDATED_ASSET_COST)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE);
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(updatedAssetRegistration);

        restAssetRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);
        AssetRegistration testAssetRegistration = assetRegistrationList.get(assetRegistrationList.size() - 1);
        assertThat(testAssetRegistration.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testAssetRegistration.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetRegistration.getAssetDetails()).isEqualTo(UPDATED_ASSET_DETAILS);
        assertThat(testAssetRegistration.getAssetCost()).isEqualTo(UPDATED_ASSET_COST);
        assertThat(testAssetRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository).save(testAssetRegistration);
    }

    @Test
    @Transactional
    void putNonExistingAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void partialUpdateAssetRegistrationWithPatch() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();

        // Update the assetRegistration using partial update
        AssetRegistration partialUpdatedAssetRegistration = new AssetRegistration();
        partialUpdatedAssetRegistration.setId(assetRegistration.getId());

        partialUpdatedAssetRegistration
            .assetDetails(UPDATED_ASSET_DETAILS)
            .assetCost(UPDATED_ASSET_COST)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE);

        restAssetRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetRegistration))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);
        AssetRegistration testAssetRegistration = assetRegistrationList.get(assetRegistrationList.size() - 1);
        assertThat(testAssetRegistration.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testAssetRegistration.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAssetRegistration.getAssetDetails()).isEqualTo(UPDATED_ASSET_DETAILS);
        assertThat(testAssetRegistration.getAssetCost()).isEqualByComparingTo(UPDATED_ASSET_COST);
        assertThat(testAssetRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAssetRegistrationWithPatch() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();

        // Update the assetRegistration using partial update
        AssetRegistration partialUpdatedAssetRegistration = new AssetRegistration();
        partialUpdatedAssetRegistration.setId(assetRegistration.getId());

        partialUpdatedAssetRegistration
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .assetCost(UPDATED_ASSET_COST)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE);

        restAssetRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetRegistration))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);
        AssetRegistration testAssetRegistration = assetRegistrationList.get(assetRegistrationList.size() - 1);
        assertThat(testAssetRegistration.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testAssetRegistration.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetRegistration.getAssetDetails()).isEqualTo(UPDATED_ASSET_DETAILS);
        assertThat(testAssetRegistration.getAssetCost()).isEqualByComparingTo(UPDATED_ASSET_COST);
        assertThat(testAssetRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetRegistrationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetRegistration() throws Exception {
        int databaseSizeBeforeUpdate = assetRegistrationRepository.findAll().size();
        assetRegistration.setId(count.incrementAndGet());

        // Create the AssetRegistration
        AssetRegistrationDTO assetRegistrationDTO = assetRegistrationMapper.toDto(assetRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRegistration in the database
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(0)).save(assetRegistration);
    }

    @Test
    @Transactional
    void deleteAssetRegistration() throws Exception {
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);

        int databaseSizeBeforeDelete = assetRegistrationRepository.findAll().size();

        // Delete the assetRegistration
        restAssetRegistrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetRegistration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetRegistration> assetRegistrationList = assetRegistrationRepository.findAll();
        assertThat(assetRegistrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetRegistration in Elasticsearch
        verify(mockAssetRegistrationSearchRepository, times(1)).deleteById(assetRegistration.getId());
    }

    @Test
    @Transactional
    void searchAssetRegistration() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetRegistrationRepository.saveAndFlush(assetRegistration);
        when(mockAssetRegistrationSearchRepository.search("id:" + assetRegistration.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetRegistration), PageRequest.of(0, 1), 1));

        // Search the assetRegistration
        restAssetRegistrationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].assetCost").value(hasItem(sameNumber(DEFAULT_ASSET_COST))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))));
    }
}
