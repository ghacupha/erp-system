package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.repository.search.AssetWarrantySearchRepository;
import io.github.erp.service.AssetWarrantyService;
import io.github.erp.service.dto.AssetWarrantyDTO;
import io.github.erp.service.mapper.AssetWarrantyMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the AssetWarrantyResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
public class AssetWarrantyResourceIT {

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/fixed-asset/asset-warranties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/asset-warranties";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetWarrantyRepository assetWarrantyRepository;

    @Mock
    private AssetWarrantyRepository assetWarrantyRepositoryMock;

    @Autowired
    private AssetWarrantyMapper assetWarrantyMapper;

    @Mock
    private AssetWarrantyService assetWarrantyServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetWarrantySearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetWarrantySearchRepository mockAssetWarrantySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetWarrantyMockMvc;

    private AssetWarranty assetWarranty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetWarranty createEntity(EntityManager em) {
        AssetWarranty assetWarranty = new AssetWarranty()
            .assetTag(DEFAULT_ASSET_TAG)
            .description(DEFAULT_DESCRIPTION)
            .modelNumber(DEFAULT_MODEL_NUMBER)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .expiryDate(DEFAULT_EXPIRY_DATE);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetWarranty.setDealer(dealer);
        return assetWarranty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetWarranty createUpdatedEntity(EntityManager em) {
        AssetWarranty assetWarranty = new AssetWarranty()
            .assetTag(UPDATED_ASSET_TAG)
            .description(UPDATED_DESCRIPTION)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        assetWarranty.setDealer(dealer);
        return assetWarranty;
    }

    @BeforeEach
    public void initTest() {
        assetWarranty = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetWarranty() throws Exception {
        int databaseSizeBeforeCreate = assetWarrantyRepository.findAll().size();
        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);
        restAssetWarrantyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeCreate + 1);
        AssetWarranty testAssetWarranty = assetWarrantyList.get(assetWarrantyList.size() - 1);
        assertThat(testAssetWarranty.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAssetWarranty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetWarranty.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testAssetWarranty.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testAssetWarranty.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(1)).save(testAssetWarranty);
    }

    @Test
    @Transactional
    void createAssetWarrantyWithExistingId() throws Exception {
        // Create the AssetWarranty with an existing ID
        assetWarranty.setId(1L);
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        int databaseSizeBeforeCreate = assetWarrantyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetWarrantyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void getAllAssetWarranties() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWarranty.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetWarrantiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetWarrantyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetWarrantyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetWarrantyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetWarrantiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetWarrantyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetWarrantyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetWarrantyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetWarranty() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get the assetWarranty
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL_ID, assetWarranty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetWarranty.getId().intValue()))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getAssetWarrantiesByIdFiltering() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        Long id = assetWarranty.getId();

        defaultAssetWarrantyShouldBeFound("id.equals=" + id);
        defaultAssetWarrantyShouldNotBeFound("id.notEquals=" + id);

        defaultAssetWarrantyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetWarrantyShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetWarrantyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetWarrantyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag equals to DEFAULT_ASSET_TAG
        defaultAssetWarrantyShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the assetWarrantyList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultAssetWarrantyShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the assetWarrantyList where assetTag not equals to UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the assetWarrantyList where assetTag equals to UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag is not null
        defaultAssetWarrantyShouldBeFound("assetTag.specified=true");

        // Get all the assetWarrantyList where assetTag is null
        defaultAssetWarrantyShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag contains DEFAULT_ASSET_TAG
        defaultAssetWarrantyShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the assetWarrantyList where assetTag contains UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultAssetWarrantyShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the assetWarrantyList where assetTag does not contain UPDATED_ASSET_TAG
        defaultAssetWarrantyShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description equals to DEFAULT_DESCRIPTION
        defaultAssetWarrantyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetWarrantyList where description equals to UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetWarrantyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetWarrantyList where description not equals to UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetWarrantyList where description equals to UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description is not null
        defaultAssetWarrantyShouldBeFound("description.specified=true");

        // Get all the assetWarrantyList where description is null
        defaultAssetWarrantyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description contains DEFAULT_DESCRIPTION
        defaultAssetWarrantyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetWarrantyList where description contains UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetWarrantyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetWarrantyList where description does not contain UPDATED_DESCRIPTION
        defaultAssetWarrantyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber equals to DEFAULT_MODEL_NUMBER
        defaultAssetWarrantyShouldBeFound("modelNumber.equals=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetWarrantyList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("modelNumber.equals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber not equals to DEFAULT_MODEL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("modelNumber.notEquals=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetWarrantyList where modelNumber not equals to UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldBeFound("modelNumber.notEquals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber in DEFAULT_MODEL_NUMBER or UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldBeFound("modelNumber.in=" + DEFAULT_MODEL_NUMBER + "," + UPDATED_MODEL_NUMBER);

        // Get all the assetWarrantyList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("modelNumber.in=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber is not null
        defaultAssetWarrantyShouldBeFound("modelNumber.specified=true");

        // Get all the assetWarrantyList where modelNumber is null
        defaultAssetWarrantyShouldNotBeFound("modelNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber contains DEFAULT_MODEL_NUMBER
        defaultAssetWarrantyShouldBeFound("modelNumber.contains=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetWarrantyList where modelNumber contains UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("modelNumber.contains=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByModelNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where modelNumber does not contain DEFAULT_MODEL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("modelNumber.doesNotContain=" + DEFAULT_MODEL_NUMBER);

        // Get all the assetWarrantyList where modelNumber does not contain UPDATED_MODEL_NUMBER
        defaultAssetWarrantyShouldBeFound("modelNumber.doesNotContain=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultAssetWarrantyShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetWarrantyList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetWarrantyList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the assetWarrantyList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber is not null
        defaultAssetWarrantyShouldBeFound("serialNumber.specified=true");

        // Get all the assetWarrantyList where serialNumber is null
        defaultAssetWarrantyShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultAssetWarrantyShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetWarrantyList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultAssetWarrantyShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the assetWarrantyList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultAssetWarrantyShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate equals to DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.equals=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate not equals to DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.notEquals=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate not equals to UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.notEquals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate in DEFAULT_EXPIRY_DATE or UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate is not null
        defaultAssetWarrantyShouldBeFound("expiryDate.specified=true");

        // Get all the assetWarrantyList where expiryDate is null
        defaultAssetWarrantyShouldNotBeFound("expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate is greater than or equal to DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.greaterThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate is greater than or equal to UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.greaterThanOrEqual=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate is less than or equal to DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.lessThanOrEqual=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate is less than or equal to SMALLER_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.lessThanOrEqual=" + SMALLER_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate is less than DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate is less than UPDATED_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        // Get all the assetWarrantyList where expiryDate is greater than DEFAULT_EXPIRY_DATE
        defaultAssetWarrantyShouldNotBeFound("expiryDate.greaterThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the assetWarrantyList where expiryDate is greater than SMALLER_EXPIRY_DATE
        defaultAssetWarrantyShouldBeFound("expiryDate.greaterThan=" + SMALLER_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);
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
        assetWarranty.addPlaceholder(placeholder);
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        Long placeholderId = placeholder.getId();

        // Get all the assetWarrantyList where placeholder equals to placeholderId
        defaultAssetWarrantyShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetWarrantyList where placeholder equals to (placeholderId + 1)
        defaultAssetWarrantyShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);
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
        assetWarranty.addUniversallyUniqueMapping(universallyUniqueMapping);
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the assetWarrantyList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultAssetWarrantyShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the assetWarrantyList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultAssetWarrantyShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);
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
        assetWarranty.setDealer(dealer);
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        Long dealerId = dealer.getId();

        // Get all the assetWarrantyList where dealer equals to dealerId
        defaultAssetWarrantyShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the assetWarrantyList where dealer equals to (dealerId + 1)
        defaultAssetWarrantyShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllAssetWarrantiesByWarrantyAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        BusinessDocument warrantyAttachment;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            warrantyAttachment = BusinessDocumentResourceIT.createEntity(em);
            em.persist(warrantyAttachment);
            em.flush();
        } else {
            warrantyAttachment = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(warrantyAttachment);
        em.flush();
        assetWarranty.addWarrantyAttachment(warrantyAttachment);
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        Long warrantyAttachmentId = warrantyAttachment.getId();

        // Get all the assetWarrantyList where warrantyAttachment equals to warrantyAttachmentId
        defaultAssetWarrantyShouldBeFound("warrantyAttachmentId.equals=" + warrantyAttachmentId);

        // Get all the assetWarrantyList where warrantyAttachment equals to (warrantyAttachmentId + 1)
        defaultAssetWarrantyShouldNotBeFound("warrantyAttachmentId.equals=" + (warrantyAttachmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetWarrantyShouldBeFound(String filter) throws Exception {
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWarranty.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));

        // Check, that the count call also returns 1
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetWarrantyShouldNotBeFound(String filter) throws Exception {
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetWarranty() throws Exception {
        // Get the assetWarranty
        restAssetWarrantyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetWarranty() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();

        // Update the assetWarranty
        AssetWarranty updatedAssetWarranty = assetWarrantyRepository.findById(assetWarranty.getId()).get();
        // Disconnect from session so that the updates on updatedAssetWarranty are not directly saved in db
        em.detach(updatedAssetWarranty);
        updatedAssetWarranty
            .assetTag(UPDATED_ASSET_TAG)
            .description(UPDATED_DESCRIPTION)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE);
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(updatedAssetWarranty);

        restAssetWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetWarrantyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);
        AssetWarranty testAssetWarranty = assetWarrantyList.get(assetWarrantyList.size() - 1);
        assertThat(testAssetWarranty.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetWarranty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetWarranty.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testAssetWarranty.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testAssetWarranty.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository).save(testAssetWarranty);
    }

    @Test
    @Transactional
    void putNonExistingAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetWarrantyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void partialUpdateAssetWarrantyWithPatch() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();

        // Update the assetWarranty using partial update
        AssetWarranty partialUpdatedAssetWarranty = new AssetWarranty();
        partialUpdatedAssetWarranty.setId(assetWarranty.getId());

        partialUpdatedAssetWarranty.expiryDate(UPDATED_EXPIRY_DATE);

        restAssetWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetWarranty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetWarranty))
            )
            .andExpect(status().isOk());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);
        AssetWarranty testAssetWarranty = assetWarrantyList.get(assetWarrantyList.size() - 1);
        assertThat(testAssetWarranty.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testAssetWarranty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetWarranty.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testAssetWarranty.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testAssetWarranty.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAssetWarrantyWithPatch() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();

        // Update the assetWarranty using partial update
        AssetWarranty partialUpdatedAssetWarranty = new AssetWarranty();
        partialUpdatedAssetWarranty.setId(assetWarranty.getId());

        partialUpdatedAssetWarranty
            .assetTag(UPDATED_ASSET_TAG)
            .description(UPDATED_DESCRIPTION)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE);

        restAssetWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetWarranty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetWarranty))
            )
            .andExpect(status().isOk());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);
        AssetWarranty testAssetWarranty = assetWarrantyList.get(assetWarrantyList.size() - 1);
        assertThat(testAssetWarranty.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testAssetWarranty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetWarranty.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testAssetWarranty.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testAssetWarranty.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetWarrantyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetWarranty() throws Exception {
        int databaseSizeBeforeUpdate = assetWarrantyRepository.findAll().size();
        assetWarranty.setId(count.incrementAndGet());

        // Create the AssetWarranty
        AssetWarrantyDTO assetWarrantyDTO = assetWarrantyMapper.toDto(assetWarranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWarrantyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetWarranty in the database
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(0)).save(assetWarranty);
    }

    @Test
    @Transactional
    void deleteAssetWarranty() throws Exception {
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);

        int databaseSizeBeforeDelete = assetWarrantyRepository.findAll().size();

        // Delete the assetWarranty
        restAssetWarrantyMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetWarranty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetWarranty> assetWarrantyList = assetWarrantyRepository.findAll();
        assertThat(assetWarrantyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetWarranty in Elasticsearch
        verify(mockAssetWarrantySearchRepository, times(1)).deleteById(assetWarranty.getId());
    }

    @Test
    @Transactional
    void searchAssetWarranty() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetWarrantyRepository.saveAndFlush(assetWarranty);
        when(mockAssetWarrantySearchRepository.search("id:" + assetWarranty.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetWarranty), PageRequest.of(0, 1), 1));

        // Search the assetWarranty
        restAssetWarrantyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetWarranty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWarranty.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }
}
