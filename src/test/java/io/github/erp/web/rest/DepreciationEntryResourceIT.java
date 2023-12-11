package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.service.criteria.DepreciationEntryCriteria;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link DepreciationEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationEntryResourceIT {

    private static final ZonedDateTime DEFAULT_POSTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_POSTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_POSTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/depreciation-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-entries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationEntryRepository depreciationEntryRepository;

    @Autowired
    private DepreciationEntryMapper depreciationEntryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationEntrySearchRepository mockDepreciationEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationEntryMockMvc;

    private DepreciationEntry depreciationEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntry createEntity(EntityManager em) {
        DepreciationEntry depreciationEntry = new DepreciationEntry()
            .postedAt(DEFAULT_POSTED_AT)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .assetNumber(DEFAULT_ASSET_NUMBER);
        return depreciationEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntry createUpdatedEntity(EntityManager em) {
        DepreciationEntry depreciationEntry = new DepreciationEntry()
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER);
        return depreciationEntry;
    }

    @BeforeEach
    public void initTest() {
        depreciationEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationEntry() throws Exception {
        int databaseSizeBeforeCreate = depreciationEntryRepository.findAll().size();
        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);
        restDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(DEFAULT_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(1)).save(testDepreciationEntry);
    }

    @Test
    @Transactional
    void createDepreciationEntryWithExistingId() throws Exception {
        // Create the DepreciationEntry with an existing ID
        depreciationEntry.setId(1L);
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        int databaseSizeBeforeCreate = depreciationEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void getAllDepreciationEntries() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(sameInstant(DEFAULT_POSTED_AT))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationEntry.getId().intValue()))
            .andExpect(jsonPath("$.postedAt").value(sameInstant(DEFAULT_POSTED_AT)))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getDepreciationEntriesByIdFiltering() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        Long id = depreciationEntry.getId();

        defaultDepreciationEntryShouldBeFound("id.equals=" + id);
        defaultDepreciationEntryShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt equals to DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.equals=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt equals to UPDATED_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.equals=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt not equals to DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.notEquals=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt not equals to UPDATED_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.notEquals=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt in DEFAULT_POSTED_AT or UPDATED_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.in=" + DEFAULT_POSTED_AT + "," + UPDATED_POSTED_AT);

        // Get all the depreciationEntryList where postedAt equals to UPDATED_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.in=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt is not null
        defaultDepreciationEntryShouldBeFound("postedAt.specified=true");

        // Get all the depreciationEntryList where postedAt is null
        defaultDepreciationEntryShouldNotBeFound("postedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt is greater than or equal to DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.greaterThanOrEqual=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt is greater than or equal to UPDATED_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.greaterThanOrEqual=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt is less than or equal to DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.lessThanOrEqual=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt is less than or equal to SMALLER_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.lessThanOrEqual=" + SMALLER_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt is less than DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.lessThan=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt is less than UPDATED_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.lessThan=" + UPDATED_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPostedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where postedAt is greater than DEFAULT_POSTED_AT
        defaultDepreciationEntryShouldNotBeFound("postedAt.greaterThan=" + DEFAULT_POSTED_AT);

        // Get all the depreciationEntryList where postedAt is greater than SMALLER_POSTED_AT
        defaultDepreciationEntryShouldBeFound("postedAt.greaterThan=" + SMALLER_POSTED_AT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount is not null
        defaultDepreciationEntryShouldBeFound("depreciationAmount.specified=true");

        // Get all the depreciationEntryList where depreciationAmount is null
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the depreciationEntryList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultDepreciationEntryShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber is not null
        defaultDepreciationEntryShouldBeFound("assetNumber.specified=true");

        // Get all the depreciationEntryList where assetNumber is null
        defaultDepreciationEntryShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultDepreciationEntryShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the depreciationEntryList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultDepreciationEntryShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
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
        depreciationEntry.setServiceOutlet(serviceOutlet);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the depreciationEntryList where serviceOutlet equals to serviceOutletId
        defaultDepreciationEntryShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the depreciationEntryList where serviceOutlet equals to (serviceOutletId + 1)
        defaultDepreciationEntryShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
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
        depreciationEntry.setAssetCategory(assetCategory);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long assetCategoryId = assetCategory.getId();

        // Get all the depreciationEntryList where assetCategory equals to assetCategoryId
        defaultDepreciationEntryShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the depreciationEntryList where assetCategory equals to (assetCategoryId + 1)
        defaultDepreciationEntryShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        em.persist(depreciationMethod);
        em.flush();
        depreciationEntry.setDepreciationMethod(depreciationMethod);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long depreciationMethodId = depreciationMethod.getId();

        // Get all the depreciationEntryList where depreciationMethod equals to depreciationMethodId
        defaultDepreciationEntryShouldBeFound("depreciationMethodId.equals=" + depreciationMethodId);

        // Get all the depreciationEntryList where depreciationMethod equals to (depreciationMethodId + 1)
        defaultDepreciationEntryShouldNotBeFound("depreciationMethodId.equals=" + (depreciationMethodId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByAssetRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
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
        depreciationEntry.setAssetRegistration(assetRegistration);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long assetRegistrationId = assetRegistration.getId();

        // Get all the depreciationEntryList where assetRegistration equals to assetRegistrationId
        defaultDepreciationEntryShouldBeFound("assetRegistrationId.equals=" + assetRegistrationId);

        // Get all the depreciationEntryList where assetRegistration equals to (assetRegistrationId + 1)
        defaultDepreciationEntryShouldNotBeFound("assetRegistrationId.equals=" + (assetRegistrationId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(depreciationPeriod);
        em.flush();
        depreciationEntry.setDepreciationPeriod(depreciationPeriod);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the depreciationEntryList where depreciationPeriod equals to depreciationPeriodId
        defaultDepreciationEntryShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the depreciationEntryList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(fiscalMonth);
        em.flush();
        depreciationEntry.setFiscalMonth(fiscalMonth);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the depreciationEntryList where fiscalMonth equals to fiscalMonthId
        defaultDepreciationEntryShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the depreciationEntryList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultDepreciationEntryShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalQuarterIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        FiscalQuarter fiscalQuarter;
        if (TestUtil.findAll(em, FiscalQuarter.class).isEmpty()) {
            fiscalQuarter = FiscalQuarterResourceIT.createEntity(em);
            em.persist(fiscalQuarter);
            em.flush();
        } else {
            fiscalQuarter = TestUtil.findAll(em, FiscalQuarter.class).get(0);
        }
        em.persist(fiscalQuarter);
        em.flush();
        depreciationEntry.setFiscalQuarter(fiscalQuarter);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long fiscalQuarterId = fiscalQuarter.getId();

        // Get all the depreciationEntryList where fiscalQuarter equals to fiscalQuarterId
        defaultDepreciationEntryShouldBeFound("fiscalQuarterId.equals=" + fiscalQuarterId);

        // Get all the depreciationEntryList where fiscalQuarter equals to (fiscalQuarterId + 1)
        defaultDepreciationEntryShouldNotBeFound("fiscalQuarterId.equals=" + (fiscalQuarterId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalYearIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        depreciationEntry.setFiscalYear(fiscalYear);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long fiscalYearId = fiscalYear.getId();

        // Get all the depreciationEntryList where fiscalYear equals to fiscalYearId
        defaultDepreciationEntryShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the depreciationEntryList where fiscalYear equals to (fiscalYearId + 1)
        defaultDepreciationEntryShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationEntryShouldBeFound(String filter) throws Exception {
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(sameInstant(DEFAULT_POSTED_AT))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())));

        // Check, that the count call also returns 1
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationEntryShouldNotBeFound(String filter) throws Exception {
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationEntry() throws Exception {
        // Get the depreciationEntry
        restDepreciationEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry
        DepreciationEntry updatedDepreciationEntry = depreciationEntryRepository.findById(depreciationEntry.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationEntry are not directly saved in db
        em.detach(updatedDepreciationEntry);
        updatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER);
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(updatedDepreciationEntry);

        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository).save(testDepreciationEntry);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry using partial update
        DepreciationEntry partialUpdatedDepreciationEntry = new DepreciationEntry();
        partialUpdatedDepreciationEntry.setId(depreciationEntry.getId());

        partialUpdatedDepreciationEntry.postedAt(UPDATED_POSTED_AT);

        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry using partial update
        DepreciationEntry partialUpdatedDepreciationEntry = new DepreciationEntry();
        partialUpdatedDepreciationEntry.setId(depreciationEntry.getId());

        partialUpdatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER);

        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void deleteDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeDelete = depreciationEntryRepository.findAll().size();

        // Delete the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(1)).deleteById(depreciationEntry.getId());
    }

    @Test
    @Transactional
    void searchDepreciationEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        when(mockDepreciationEntrySearchRepository.search("id:" + depreciationEntry.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationEntry), PageRequest.of(0, 1), 1));

        // Search the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(sameInstant(DEFAULT_POSTED_AT))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())));
    }
}
