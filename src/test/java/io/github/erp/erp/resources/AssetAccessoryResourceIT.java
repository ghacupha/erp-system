package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessorySearchRepository;
import io.github.erp.service.AssetAccessoryService;
import io.github.erp.service.dto.AssetAccessoryDTO;
import io.github.erp.service.mapper.AssetAccessoryMapper;
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
import org.springframework.util.Base64Utils;

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
 * Integration tests for the AssetAccessoryResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
public class AssetAccessoryResourceIT {

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DETAILS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COMMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMMENTS_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fixed-asset/asset-accessories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/asset-accessories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetAccessoryRepository assetAccessoryRepository;

    @Mock
    private AssetAccessoryRepository assetAccessoryRepositoryMock;

    @Autowired
    private AssetAccessoryMapper assetAccessoryMapper;

    @Mock
    private AssetAccessoryService assetAccessoryServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetAccessorySearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetAccessorySearchRepository mockAssetAccessorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetAccessoryMockMvc;

    private AssetAccessory assetAccessory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAccessory createEntity(EntityManager em) {
        AssetAccessory assetAccessory = new AssetAccessory()
            .assetTag(DEFAULT_ASSET_TAG)
            .assetDetails(DEFAULT_ASSET_DETAILS)
            .comments(DEFAULT_COMMENTS)
            .commentsContentType(DEFAULT_COMMENTS_CONTENT_TYPE)
            .modelNumber(DEFAULT_MODEL_NUMBER)
            .serialNumber(DEFAULT_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        assetAccessory.getServiceOutlets().add(serviceOutlet);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        assetAccessory.getSettlements().add(settlement);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        assetAccessory.setAssetCategory(assetCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetAccessory.setDealer(dealer);
        return assetAccessory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAccessory createUpdatedEntity(EntityManager em) {
        AssetAccessory assetAccessory = new AssetAccessory()
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createUpdatedEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        assetAccessory.getServiceOutlets().add(serviceOutlet);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createUpdatedEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        assetAccessory.getSettlements().add(settlement);
        // Add required entity
        AssetCategory assetCategory;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetCategory = AssetCategoryResourceIT.createUpdatedEntity(em);
            em.persist(assetCategory);
            em.flush();
        } else {
            assetCategory = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        assetAccessory.setAssetCategory(assetCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetAccessory.setDealer(dealer);
        return assetAccessory;
    }

    @BeforeEach
    public void initTest() {
        assetAccessory = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetAccessory() throws Exception {
        int databaseSizeBeforeCreate = assetAccessoryRepository.findAll().size();
        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);
        restAssetAccessoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeCreate + 1);
        AssetAccessory testAssetAccessory = assetAccessoryList.get(assetAccessoryList.size() - 1);
        assertThat(testAssetAccessory.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAssetAccessory.getAssetDetails()).isEqualTo(DEFAULT_ASSET_DETAILS);
        assertThat(testAssetAccessory.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testAssetAccessory.getCommentsContentType()).isEqualTo(DEFAULT_COMMENTS_CONTENT_TYPE);
        assertThat(testAssetAccessory.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testAssetAccessory.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(1)).save(testAssetAccessory);
    }

    @Test
    @Transactional
    void createAssetAccessoryWithExistingId() throws Exception {
        // Create the AssetAccessory with an existing ID
        assetAccessory.setId(1L);
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        int databaseSizeBeforeCreate = assetAccessoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetAccessoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void getAllAssetAccessories() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAccessory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetAccessoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetAccessoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetAccessoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetAccessoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetAccessoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetAccessoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetAccessoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetAccessoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetAccessory() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get the assetAccessory
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetAccessory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetAccessory.getId().intValue()))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDetails").value(DEFAULT_ASSET_DETAILS))
            .andExpect(jsonPath("$.commentsContentType").value(DEFAULT_COMMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.comments").value(Base64Utils.encodeToString(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER));
    }

    @Test
    @Transactional
    void getAssetAccessoriesByIdFiltering() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        Long id = assetAccessory.getId();

        defaultAssetAccessoryShouldBeFound("id.equals=" + id);
        defaultAssetAccessoryShouldNotBeFound("id.notEquals=" + id);

        defaultAssetAccessoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetAccessoryShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetAccessoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetAccessoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag equals to DEFAULT_ASSET_TAG
        defaultAssetAccessoryShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the assetAccessoryList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultAssetAccessoryShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the assetAccessoryList where assetTag not equals to UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the assetAccessoryList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag is not null
        defaultAssetAccessoryShouldBeFound("assetTag.specified=true");

        // Get all the assetAccessoryList where assetTag is null
        defaultAssetAccessoryShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag contains DEFAULT_ASSET_TAG
        defaultAssetAccessoryShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the assetAccessoryList where assetTag contains UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultAssetAccessoryShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the assetAccessoryList where assetTag does not contain UPDATED_ASSET_TAG
        defaultAssetAccessoryShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails equals to DEFAULT_ASSET_DETAILS
        defaultAssetAccessoryShouldBeFound("assetDetails.equals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAccessoryList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldNotBeFound("assetDetails.equals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails not equals to DEFAULT_ASSET_DETAILS
        defaultAssetAccessoryShouldNotBeFound("assetDetails.notEquals=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAccessoryList where assetDetails not equals to UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldBeFound("assetDetails.notEquals=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails in DEFAULT_ASSET_DETAILS or UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldBeFound("assetDetails.in=" + DEFAULT_ASSET_DETAILS + "," + UPDATED_ASSET_DETAILS);

        // Get all the assetAccessoryList where assetDetails equals to UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldNotBeFound("assetDetails.in=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails is not null
        defaultAssetAccessoryShouldBeFound("assetDetails.specified=true");

        // Get all the assetAccessoryList where assetDetails is null
        defaultAssetAccessoryShouldNotBeFound("assetDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails contains DEFAULT_ASSET_DETAILS
        defaultAssetAccessoryShouldBeFound("assetDetails.contains=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAccessoryList where assetDetails contains UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldNotBeFound("assetDetails.contains=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where assetDetails does not contain DEFAULT_ASSET_DETAILS
        defaultAssetAccessoryShouldNotBeFound("assetDetails.doesNotContain=" + DEFAULT_ASSET_DETAILS);

        // Get all the assetAccessoryList where assetDetails does not contain UPDATED_ASSET_DETAILS
        defaultAssetAccessoryShouldBeFound("assetDetails.doesNotContain=" + UPDATED_ASSET_DETAILS);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber equals to DEFAULT_MODEL_NUMBER
        defaultAssetAccessoryShouldBeFound("modelNumber.equals=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetAccessoryList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("modelNumber.equals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber not equals to DEFAULT_MODEL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("modelNumber.notEquals=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetAccessoryList where modelNumber not equals to UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldBeFound("modelNumber.notEquals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber in DEFAULT_MODEL_NUMBER or UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldBeFound("modelNumber.in=" + DEFAULT_MODEL_NUMBER + "," + UPDATED_MODEL_NUMBER);

        // Get all the assetAccessoryList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("modelNumber.in=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber is not null
        defaultAssetAccessoryShouldBeFound("modelNumber.specified=true");

        // Get all the assetAccessoryList where modelNumber is null
        defaultAssetAccessoryShouldNotBeFound("modelNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber contains DEFAULT_MODEL_NUMBER
        defaultAssetAccessoryShouldBeFound("modelNumber.contains=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetAccessoryList where modelNumber contains UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("modelNumber.contains=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByModelNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where modelNumber does not contain DEFAULT_MODEL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("modelNumber.doesNotContain=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetAccessoryList where modelNumber does not contain UPDATED_MODEL_NUMBER
        defaultAssetAccessoryShouldBeFound("modelNumber.doesNotContain=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultAssetAccessoryShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetAccessoryList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetAccessoryList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the assetAccessoryList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber is not null
        defaultAssetAccessoryShouldBeFound("serialNumber.specified=true");

        // Get all the assetAccessoryList where serialNumber is null
        defaultAssetAccessoryShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultAssetAccessoryShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetAccessoryList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        // Get all the assetAccessoryList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultAssetAccessoryShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetAccessoryList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultAssetAccessoryShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        em.persist(assetRegistration);
        em.flush();
        assetAccessory.setAssetRegistration(assetRegistration);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long assetRegistrationId = assetRegistration.getId();

        // Get all the assetAccessoryList where assetRegistration equals to assetRegistrationId
        defaultAssetAccessoryShouldBeFound("assetRegistrationId.equals=" + assetRegistrationId);

        // Get all the assetAccessoryList where assetRegistration equals to (assetRegistrationId + 1)
        defaultAssetAccessoryShouldNotBeFound("assetRegistrationId.equals=" + (assetRegistrationId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetWarrantyIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        AssetWarranty assetWarranty;
        if (TestUtil.findAll(em, AssetWarranty.class).isEmpty()) {
            assetWarranty = AssetWarrantyResourceIT.createEntity(em);
            em.persist(assetWarranty);
            em.flush();
        } else {
            assetWarranty = TestUtil.findAll(em, AssetWarranty.class).get(0);
        }
        em.persist(assetWarranty);
        em.flush();
        assetAccessory.addAssetWarranty(assetWarranty);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long assetWarrantyId = assetWarranty.getId();

        // Get all the assetAccessoryList where assetWarranty equals to assetWarrantyId
        defaultAssetAccessoryShouldBeFound("assetWarrantyId.equals=" + assetWarrantyId);

        // Get all the assetAccessoryList where assetWarranty equals to (assetWarrantyId + 1)
        defaultAssetAccessoryShouldNotBeFound("assetWarrantyId.equals=" + (assetWarrantyId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addPlaceholder(placeholder);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long placeholderId = placeholder.getId();

        // Get all the assetAccessoryList where placeholder equals to placeholderId
        defaultAssetAccessoryShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetAccessoryList where placeholder equals to (placeholderId + 1)
        defaultAssetAccessoryShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByPaymentInvoicesIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addPaymentInvoices(paymentInvoices);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long paymentInvoicesId = paymentInvoices.getId();

        // Get all the assetAccessoryList where paymentInvoices equals to paymentInvoicesId
        defaultAssetAccessoryShouldBeFound("paymentInvoicesId.equals=" + paymentInvoicesId);

        // Get all the assetAccessoryList where paymentInvoices equals to (paymentInvoicesId + 1)
        defaultAssetAccessoryShouldNotBeFound("paymentInvoicesId.equals=" + (paymentInvoicesId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addServiceOutlet(serviceOutlet);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the assetAccessoryList where serviceOutlet equals to serviceOutletId
        defaultAssetAccessoryShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the assetAccessoryList where serviceOutlet equals to (serviceOutletId + 1)
        defaultAssetAccessoryShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySettlementIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addSettlement(settlement);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long settlementId = settlement.getId();

        // Get all the assetAccessoryList where settlement equals to settlementId
        defaultAssetAccessoryShouldBeFound("settlementId.equals=" + settlementId);

        // Get all the assetAccessoryList where settlement equals to (settlementId + 1)
        defaultAssetAccessoryShouldNotBeFound("settlementId.equals=" + (settlementId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.setAssetCategory(assetCategory);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long assetCategoryId = assetCategory.getId();

        // Get all the assetAccessoryList where assetCategory equals to assetCategoryId
        defaultAssetAccessoryShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the assetAccessoryList where assetCategory equals to (assetCategoryId + 1)
        defaultAssetAccessoryShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addPurchaseOrder(purchaseOrder);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the assetAccessoryList where purchaseOrder equals to purchaseOrderId
        defaultAssetAccessoryShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the assetAccessoryList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultAssetAccessoryShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByDeliveryNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addDeliveryNote(deliveryNote);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long deliveryNoteId = deliveryNote.getId();

        // Get all the assetAccessoryList where deliveryNote equals to deliveryNoteId
        defaultAssetAccessoryShouldBeFound("deliveryNoteId.equals=" + deliveryNoteId);

        // Get all the assetAccessoryList where deliveryNote equals to (deliveryNoteId + 1)
        defaultAssetAccessoryShouldNotBeFound("deliveryNoteId.equals=" + (deliveryNoteId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByJobSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addJobSheet(jobSheet);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long jobSheetId = jobSheet.getId();

        // Get all the assetAccessoryList where jobSheet equals to jobSheetId
        defaultAssetAccessoryShouldBeFound("jobSheetId.equals=" + jobSheetId);

        // Get all the assetAccessoryList where jobSheet equals to (jobSheetId + 1)
        defaultAssetAccessoryShouldNotBeFound("jobSheetId.equals=" + (jobSheetId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.setDealer(dealer);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long dealerId = dealer.getId();

        // Get all the assetAccessoryList where dealer equals to dealerId
        defaultAssetAccessoryShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the assetAccessoryList where dealer equals to (dealerId + 1)
        defaultAssetAccessoryShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByDesignatedUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addDesignatedUsers(designatedUsers);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long designatedUsersId = designatedUsers.getId();

        // Get all the assetAccessoryList where designatedUsers equals to designatedUsersId
        defaultAssetAccessoryShouldBeFound("designatedUsersId.equals=" + designatedUsersId);

        // Get all the assetAccessoryList where designatedUsers equals to (designatedUsersId + 1)
        defaultAssetAccessoryShouldNotBeFound("designatedUsersId.equals=" + (designatedUsersId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.setSettlementCurrency(settlementCurrency);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the assetAccessoryList where settlementCurrency equals to settlementCurrencyId
        defaultAssetAccessoryShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the assetAccessoryList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultAssetAccessoryShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
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
        assetAccessory.addBusinessDocument(businessDocument);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long businessDocumentId = businessDocument.getId();

        // Get all the assetAccessoryList where businessDocument equals to businessDocumentId
        defaultAssetAccessoryShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the assetAccessoryList where businessDocument equals to (businessDocumentId + 1)
        defaultAssetAccessoryShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllAssetAccessoriesByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        UniversallyUniqueMapping universallyUniqueMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            universallyUniqueMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(universallyUniqueMapping);
            em.flush();
        } else {
            universallyUniqueMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(universallyUniqueMapping);
        em.flush();
        assetAccessory.addUniversallyUniqueMapping(universallyUniqueMapping);
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the assetAccessoryList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultAssetAccessoryShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the assetAccessoryList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultAssetAccessoryShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetAccessoryShouldBeFound(String filter) throws Exception {
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAccessory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)));

        // Check, that the count call also returns 1
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetAccessoryShouldNotBeFound(String filter) throws Exception {
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetAccessory() throws Exception {
        // Get the assetAccessory
        restAssetAccessoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetAccessory() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();

        // Update the assetAccessory
        AssetAccessory updatedAssetAccessory = assetAccessoryRepository.findById(assetAccessory.getId()).get();
        // Disconnect from session so that the updates on updatedAssetAccessory are not directly saved in db
        em.detach(updatedAssetAccessory);
        updatedAssetAccessory
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(updatedAssetAccessory);

        restAssetAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAccessoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);
        AssetAccessory testAssetAccessory = assetAccessoryList.get(assetAccessoryList.size() - 1);
        assertThat(testAssetAccessory.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetAccessory.getAssetDetails()).isEqualTo(UPDATED_ASSET_DETAILS);
        assertThat(testAssetAccessory.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetAccessory.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testAssetAccessory.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testAssetAccessory.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository).save(testAssetAccessory);
    }

    @Test
    @Transactional
    void putNonExistingAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetAccessoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void partialUpdateAssetAccessoryWithPatch() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();

        // Update the assetAccessory using partial update
        AssetAccessory partialUpdatedAssetAccessory = new AssetAccessory();
        partialUpdatedAssetAccessory.setId(assetAccessory.getId());

        partialUpdatedAssetAccessory
            .assetTag(UPDATED_ASSET_TAG)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .modelNumber(UPDATED_MODEL_NUMBER);

        restAssetAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAccessory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAccessory))
            )
            .andExpect(status().isOk());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);
        AssetAccessory testAssetAccessory = assetAccessoryList.get(assetAccessoryList.size() - 1);
        assertThat(testAssetAccessory.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetAccessory.getAssetDetails()).isEqualTo(DEFAULT_ASSET_DETAILS);
        assertThat(testAssetAccessory.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetAccessory.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testAssetAccessory.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testAssetAccessory.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateAssetAccessoryWithPatch() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();

        // Update the assetAccessory using partial update
        AssetAccessory partialUpdatedAssetAccessory = new AssetAccessory();
        partialUpdatedAssetAccessory.setId(assetAccessory.getId());

        partialUpdatedAssetAccessory
            .assetTag(UPDATED_ASSET_TAG)
            .assetDetails(UPDATED_ASSET_DETAILS)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restAssetAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetAccessory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetAccessory))
            )
            .andExpect(status().isOk());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);
        AssetAccessory testAssetAccessory = assetAccessoryList.get(assetAccessoryList.size() - 1);
        assertThat(testAssetAccessory.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetAccessory.getAssetDetails()).isEqualTo(UPDATED_ASSET_DETAILS);
        assertThat(testAssetAccessory.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssetAccessory.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testAssetAccessory.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testAssetAccessory.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetAccessoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetAccessory() throws Exception {
        int databaseSizeBeforeUpdate = assetAccessoryRepository.findAll().size();
        assetAccessory.setId(count.incrementAndGet());

        // Create the AssetAccessory
        AssetAccessoryDTO assetAccessoryDTO = assetAccessoryMapper.toDto(assetAccessory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetAccessoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetAccessoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetAccessory in the database
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(0)).save(assetAccessory);
    }

    @Test
    @Transactional
    void deleteAssetAccessory() throws Exception {
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);

        int databaseSizeBeforeDelete = assetAccessoryRepository.findAll().size();

        // Delete the assetAccessory
        restAssetAccessoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetAccessory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetAccessory> assetAccessoryList = assetAccessoryRepository.findAll();
        assertThat(assetAccessoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetAccessory in Elasticsearch
        verify(mockAssetAccessorySearchRepository, times(1)).deleteById(assetAccessory.getId());
    }

    @Test
    @Transactional
    void searchAssetAccessory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetAccessoryRepository.saveAndFlush(assetAccessory);
        when(mockAssetAccessorySearchRepository.search("id:" + assetAccessory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetAccessory), PageRequest.of(0, 1), 1));

        // Search the assetAccessory
        restAssetAccessoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetAccessory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAccessory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDetails").value(hasItem(DEFAULT_ASSET_DETAILS)))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)));
    }
}
