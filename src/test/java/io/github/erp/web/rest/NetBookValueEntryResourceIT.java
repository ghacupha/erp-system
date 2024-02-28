package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.NetBookValueEntryRepository;
import io.github.erp.repository.search.NetBookValueEntrySearchRepository;
import io.github.erp.service.NetBookValueEntryService;
import io.github.erp.service.criteria.NetBookValueEntryCriteria;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
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
 * Integration tests for the {@link NetBookValueEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NetBookValueEntryResourceIT {

    private static final String DEFAULT_ASSET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final UUID DEFAULT_NBV_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_NBV_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_COMPILATION_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_JOB_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_COMPILATION_BATCH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_BATCH_IDENTIFIER = UUID.randomUUID();

    private static final Integer DEFAULT_ELAPSED_MONTHS = 1;
    private static final Integer UPDATED_ELAPSED_MONTHS = 2;
    private static final Integer SMALLER_ELAPSED_MONTHS = 1 - 1;

    private static final Integer DEFAULT_PRIOR_MONTHS = 1;
    private static final Integer UPDATED_PRIOR_MONTHS = 2;
    private static final Integer SMALLER_PRIOR_MONTHS = 1 - 1;

    private static final Double DEFAULT_USEFUL_LIFE_YEARS = 1D;
    private static final Double UPDATED_USEFUL_LIFE_YEARS = 2D;
    private static final Double SMALLER_USEFUL_LIFE_YEARS = 1D - 1D;

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREVIOUS_NET_BOOK_VALUE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_HISTORICAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_HISTORICAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_HISTORICAL_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_CAPITALIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CAPITALIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CAPITALIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/net-book-value-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/net-book-value-entries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NetBookValueEntryRepository netBookValueEntryRepository;

    @Mock
    private NetBookValueEntryRepository netBookValueEntryRepositoryMock;

    @Autowired
    private NetBookValueEntryMapper netBookValueEntryMapper;

    @Mock
    private NetBookValueEntryService netBookValueEntryServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NetBookValueEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private NetBookValueEntrySearchRepository mockNetBookValueEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetBookValueEntryMockMvc;

    private NetBookValueEntry netBookValueEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetBookValueEntry createEntity(EntityManager em) {
        NetBookValueEntry netBookValueEntry = new NetBookValueEntry()
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .assetTag(DEFAULT_ASSET_TAG)
            .assetDescription(DEFAULT_ASSET_DESCRIPTION)
            .nbvIdentifier(DEFAULT_NBV_IDENTIFIER)
            .compilationJobIdentifier(DEFAULT_COMPILATION_JOB_IDENTIFIER)
            .compilationBatchIdentifier(DEFAULT_COMPILATION_BATCH_IDENTIFIER)
            .elapsedMonths(DEFAULT_ELAPSED_MONTHS)
            .priorMonths(DEFAULT_PRIOR_MONTHS)
            .usefulLifeYears(DEFAULT_USEFUL_LIFE_YEARS)
            .netBookValueAmount(DEFAULT_NET_BOOK_VALUE_AMOUNT)
            .previousNetBookValueAmount(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT)
            .historicalCost(DEFAULT_HISTORICAL_COST)
            .capitalizationDate(DEFAULT_CAPITALIZATION_DATE);
        return netBookValueEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetBookValueEntry createUpdatedEntity(EntityManager em) {
        NetBookValueEntry netBookValueEntry = new NetBookValueEntry()
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .nbvIdentifier(UPDATED_NBV_IDENTIFIER)
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .netBookValueAmount(UPDATED_NET_BOOK_VALUE_AMOUNT)
            .previousNetBookValueAmount(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);
        return netBookValueEntry;
    }

    @BeforeEach
    public void initTest() {
        netBookValueEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createNetBookValueEntry() throws Exception {
        int databaseSizeBeforeCreate = netBookValueEntryRepository.findAll().size();
        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);
        restNetBookValueEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeCreate + 1);
        NetBookValueEntry testNetBookValueEntry = netBookValueEntryList.get(netBookValueEntryList.size() - 1);
        assertThat(testNetBookValueEntry.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testNetBookValueEntry.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testNetBookValueEntry.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testNetBookValueEntry.getNbvIdentifier()).isEqualTo(DEFAULT_NBV_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationJobIdentifier()).isEqualTo(DEFAULT_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationBatchIdentifier()).isEqualTo(DEFAULT_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNetBookValueEntry.getElapsedMonths()).isEqualTo(DEFAULT_ELAPSED_MONTHS);
        assertThat(testNetBookValueEntry.getPriorMonths()).isEqualTo(DEFAULT_PRIOR_MONTHS);
        assertThat(testNetBookValueEntry.getUsefulLifeYears()).isEqualTo(DEFAULT_USEFUL_LIFE_YEARS);
        assertThat(testNetBookValueEntry.getNetBookValueAmount()).isEqualByComparingTo(DEFAULT_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getPreviousNetBookValueAmount()).isEqualByComparingTo(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getHistoricalCost()).isEqualByComparingTo(DEFAULT_HISTORICAL_COST);
        assertThat(testNetBookValueEntry.getCapitalizationDate()).isEqualTo(DEFAULT_CAPITALIZATION_DATE);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(1)).save(testNetBookValueEntry);
    }

    @Test
    @Transactional
    void createNetBookValueEntryWithExistingId() throws Exception {
        // Create the NetBookValueEntry with an existing ID
        netBookValueEntry.setId(1L);
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        int databaseSizeBeforeCreate = netBookValueEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetBookValueEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void checkNbvIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = netBookValueEntryRepository.findAll().size();
        // set the field null
        netBookValueEntry.setNbvIdentifier(null);

        // Create the NetBookValueEntry, which fails.
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        restNetBookValueEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntries() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netBookValueEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nbvIdentifier").value(hasItem(DEFAULT_NBV_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS)))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS)))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(DEFAULT_USEFUL_LIFE_YEARS.doubleValue())))
            .andExpect(jsonPath("$.[*].netBookValueAmount").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].previousNetBookValueAmount").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNetBookValueEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(netBookValueEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNetBookValueEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(netBookValueEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNetBookValueEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(netBookValueEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNetBookValueEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(netBookValueEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getNetBookValueEntry() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get the netBookValueEntry
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, netBookValueEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(netBookValueEntry.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.nbvIdentifier").value(DEFAULT_NBV_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationJobIdentifier").value(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.compilationBatchIdentifier").value(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.elapsedMonths").value(DEFAULT_ELAPSED_MONTHS))
            .andExpect(jsonPath("$.priorMonths").value(DEFAULT_PRIOR_MONTHS))
            .andExpect(jsonPath("$.usefulLifeYears").value(DEFAULT_USEFUL_LIFE_YEARS.doubleValue()))
            .andExpect(jsonPath("$.netBookValueAmount").value(sameNumber(DEFAULT_NET_BOOK_VALUE_AMOUNT)))
            .andExpect(jsonPath("$.previousNetBookValueAmount").value(sameNumber(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT)))
            .andExpect(jsonPath("$.historicalCost").value(sameNumber(DEFAULT_HISTORICAL_COST)))
            .andExpect(jsonPath("$.capitalizationDate").value(DEFAULT_CAPITALIZATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNetBookValueEntriesByIdFiltering() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        Long id = netBookValueEntry.getId();

        defaultNetBookValueEntryShouldBeFound("id.equals=" + id);
        defaultNetBookValueEntryShouldNotBeFound("id.notEquals=" + id);

        defaultNetBookValueEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNetBookValueEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultNetBookValueEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNetBookValueEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultNetBookValueEntryShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the netBookValueEntryList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the netBookValueEntryList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the netBookValueEntryList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber is not null
        defaultNetBookValueEntryShouldBeFound("assetNumber.specified=true");

        // Get all the netBookValueEntryList where assetNumber is null
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber contains DEFAULT_ASSET_NUMBER
        defaultNetBookValueEntryShouldBeFound("assetNumber.contains=" + DEFAULT_ASSET_NUMBER);

        // Get all the netBookValueEntryList where assetNumber contains UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.contains=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetNumberNotContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetNumber does not contain DEFAULT_ASSET_NUMBER
        defaultNetBookValueEntryShouldNotBeFound("assetNumber.doesNotContain=" + DEFAULT_ASSET_NUMBER);

        // Get all the netBookValueEntryList where assetNumber does not contain UPDATED_ASSET_NUMBER
        defaultNetBookValueEntryShouldBeFound("assetNumber.doesNotContain=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag equals to DEFAULT_ASSET_TAG
        defaultNetBookValueEntryShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the netBookValueEntryList where assetTag equals to UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultNetBookValueEntryShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the netBookValueEntryList where assetTag not equals to UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the netBookValueEntryList where assetTag equals to UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag is not null
        defaultNetBookValueEntryShouldBeFound("assetTag.specified=true");

        // Get all the netBookValueEntryList where assetTag is null
        defaultNetBookValueEntryShouldNotBeFound("assetTag.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag contains DEFAULT_ASSET_TAG
        defaultNetBookValueEntryShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the netBookValueEntryList where assetTag contains UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultNetBookValueEntryShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the netBookValueEntryList where assetTag does not contain UPDATED_ASSET_TAG
        defaultNetBookValueEntryShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription equals to DEFAULT_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldBeFound("assetDescription.equals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the netBookValueEntryList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.equals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription not equals to DEFAULT_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.notEquals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the netBookValueEntryList where assetDescription not equals to UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldBeFound("assetDescription.notEquals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription in DEFAULT_ASSET_DESCRIPTION or UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldBeFound("assetDescription.in=" + DEFAULT_ASSET_DESCRIPTION + "," + UPDATED_ASSET_DESCRIPTION);

        // Get all the netBookValueEntryList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.in=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription is not null
        defaultNetBookValueEntryShouldBeFound("assetDescription.specified=true");

        // Get all the netBookValueEntryList where assetDescription is null
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription contains DEFAULT_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldBeFound("assetDescription.contains=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the netBookValueEntryList where assetDescription contains UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.contains=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where assetDescription does not contain DEFAULT_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldNotBeFound("assetDescription.doesNotContain=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the netBookValueEntryList where assetDescription does not contain UPDATED_ASSET_DESCRIPTION
        defaultNetBookValueEntryShouldBeFound("assetDescription.doesNotContain=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNbvIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where nbvIdentifier equals to DEFAULT_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("nbvIdentifier.equals=" + DEFAULT_NBV_IDENTIFIER);

        // Get all the netBookValueEntryList where nbvIdentifier equals to UPDATED_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("nbvIdentifier.equals=" + UPDATED_NBV_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNbvIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where nbvIdentifier not equals to DEFAULT_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("nbvIdentifier.notEquals=" + DEFAULT_NBV_IDENTIFIER);

        // Get all the netBookValueEntryList where nbvIdentifier not equals to UPDATED_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("nbvIdentifier.notEquals=" + UPDATED_NBV_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNbvIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where nbvIdentifier in DEFAULT_NBV_IDENTIFIER or UPDATED_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("nbvIdentifier.in=" + DEFAULT_NBV_IDENTIFIER + "," + UPDATED_NBV_IDENTIFIER);

        // Get all the netBookValueEntryList where nbvIdentifier equals to UPDATED_NBV_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("nbvIdentifier.in=" + UPDATED_NBV_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNbvIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where nbvIdentifier is not null
        defaultNetBookValueEntryShouldBeFound("nbvIdentifier.specified=true");

        // Get all the netBookValueEntryList where nbvIdentifier is null
        defaultNetBookValueEntryShouldNotBeFound("nbvIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationJobIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationJobIdentifier equals to DEFAULT_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("compilationJobIdentifier.equals=" + DEFAULT_COMPILATION_JOB_IDENTIFIER);

        // Get all the netBookValueEntryList where compilationJobIdentifier equals to UPDATED_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationJobIdentifier.equals=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationJobIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationJobIdentifier not equals to DEFAULT_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationJobIdentifier.notEquals=" + DEFAULT_COMPILATION_JOB_IDENTIFIER);

        // Get all the netBookValueEntryList where compilationJobIdentifier not equals to UPDATED_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("compilationJobIdentifier.notEquals=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationJobIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationJobIdentifier in DEFAULT_COMPILATION_JOB_IDENTIFIER or UPDATED_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound(
            "compilationJobIdentifier.in=" + DEFAULT_COMPILATION_JOB_IDENTIFIER + "," + UPDATED_COMPILATION_JOB_IDENTIFIER
        );

        // Get all the netBookValueEntryList where compilationJobIdentifier equals to UPDATED_COMPILATION_JOB_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationJobIdentifier.in=" + UPDATED_COMPILATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationJobIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationJobIdentifier is not null
        defaultNetBookValueEntryShouldBeFound("compilationJobIdentifier.specified=true");

        // Get all the netBookValueEntryList where compilationJobIdentifier is null
        defaultNetBookValueEntryShouldNotBeFound("compilationJobIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationBatchIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationBatchIdentifier equals to DEFAULT_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("compilationBatchIdentifier.equals=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER);

        // Get all the netBookValueEntryList where compilationBatchIdentifier equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationBatchIdentifier.equals=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationBatchIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationBatchIdentifier not equals to DEFAULT_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationBatchIdentifier.notEquals=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER);

        // Get all the netBookValueEntryList where compilationBatchIdentifier not equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound("compilationBatchIdentifier.notEquals=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationBatchIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationBatchIdentifier in DEFAULT_COMPILATION_BATCH_IDENTIFIER or UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldBeFound(
            "compilationBatchIdentifier.in=" + DEFAULT_COMPILATION_BATCH_IDENTIFIER + "," + UPDATED_COMPILATION_BATCH_IDENTIFIER
        );

        // Get all the netBookValueEntryList where compilationBatchIdentifier equals to UPDATED_COMPILATION_BATCH_IDENTIFIER
        defaultNetBookValueEntryShouldNotBeFound("compilationBatchIdentifier.in=" + UPDATED_COMPILATION_BATCH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCompilationBatchIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where compilationBatchIdentifier is not null
        defaultNetBookValueEntryShouldBeFound("compilationBatchIdentifier.specified=true");

        // Get all the netBookValueEntryList where compilationBatchIdentifier is null
        defaultNetBookValueEntryShouldNotBeFound("compilationBatchIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths equals to DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.equals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.equals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths not equals to DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.notEquals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths not equals to UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.notEquals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths in DEFAULT_ELAPSED_MONTHS or UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.in=" + DEFAULT_ELAPSED_MONTHS + "," + UPDATED_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.in=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths is not null
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.specified=true");

        // Get all the netBookValueEntryList where elapsedMonths is null
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths is greater than or equal to DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.greaterThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths is greater than or equal to UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.greaterThanOrEqual=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths is less than or equal to DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.lessThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths is less than or equal to SMALLER_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.lessThanOrEqual=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths is less than DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.lessThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths is less than UPDATED_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.lessThan=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByElapsedMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where elapsedMonths is greater than DEFAULT_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("elapsedMonths.greaterThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the netBookValueEntryList where elapsedMonths is greater than SMALLER_ELAPSED_MONTHS
        defaultNetBookValueEntryShouldBeFound("elapsedMonths.greaterThan=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths equals to DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.equals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.equals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths not equals to DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.notEquals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths not equals to UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.notEquals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths in DEFAULT_PRIOR_MONTHS or UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.in=" + DEFAULT_PRIOR_MONTHS + "," + UPDATED_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.in=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths is not null
        defaultNetBookValueEntryShouldBeFound("priorMonths.specified=true");

        // Get all the netBookValueEntryList where priorMonths is null
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths is greater than or equal to DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.greaterThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths is greater than or equal to UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.greaterThanOrEqual=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths is less than or equal to DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.lessThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths is less than or equal to SMALLER_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.lessThanOrEqual=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths is less than DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.lessThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths is less than UPDATED_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.lessThan=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPriorMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where priorMonths is greater than DEFAULT_PRIOR_MONTHS
        defaultNetBookValueEntryShouldNotBeFound("priorMonths.greaterThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the netBookValueEntryList where priorMonths is greater than SMALLER_PRIOR_MONTHS
        defaultNetBookValueEntryShouldBeFound("priorMonths.greaterThan=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.equals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.equals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears not equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.notEquals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears not equals to UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.notEquals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears in DEFAULT_USEFUL_LIFE_YEARS or UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.in=" + DEFAULT_USEFUL_LIFE_YEARS + "," + UPDATED_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.in=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears is not null
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.specified=true");

        // Get all the netBookValueEntryList where usefulLifeYears is null
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears is greater than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.greaterThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears is greater than or equal to UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.greaterThanOrEqual=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears is less than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.lessThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears is less than or equal to SMALLER_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.lessThanOrEqual=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears is less than DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.lessThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears is less than UPDATED_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.lessThan=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByUsefulLifeYearsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where usefulLifeYears is greater than DEFAULT_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldNotBeFound("usefulLifeYears.greaterThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the netBookValueEntryList where usefulLifeYears is greater than SMALLER_USEFUL_LIFE_YEARS
        defaultNetBookValueEntryShouldBeFound("usefulLifeYears.greaterThan=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount equals to DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.equals=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount equals to UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.equals=" + UPDATED_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount not equals to DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.notEquals=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount not equals to UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.notEquals=" + UPDATED_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount in DEFAULT_NET_BOOK_VALUE_AMOUNT or UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound(
            "netBookValueAmount.in=" + DEFAULT_NET_BOOK_VALUE_AMOUNT + "," + UPDATED_NET_BOOK_VALUE_AMOUNT
        );

        // Get all the netBookValueEntryList where netBookValueAmount equals to UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.in=" + UPDATED_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount is not null
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.specified=true");

        // Get all the netBookValueEntryList where netBookValueAmount is null
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount is greater than or equal to DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount is greater than or equal to UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount is less than or equal to DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount is less than or equal to SMALLER_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount is less than DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.lessThan=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount is less than UPDATED_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.lessThan=" + UPDATED_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByNetBookValueAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where netBookValueAmount is greater than DEFAULT_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("netBookValueAmount.greaterThan=" + DEFAULT_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where netBookValueAmount is greater than SMALLER_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("netBookValueAmount.greaterThan=" + SMALLER_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount equals to DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.equals=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount equals to UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.equals=" + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount not equals to DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.notEquals=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount not equals to UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.notEquals=" + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount in DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT or UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound(
            "previousNetBookValueAmount.in=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT + "," + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        );

        // Get all the netBookValueEntryList where previousNetBookValueAmount equals to UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.in=" + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is not null
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.specified=true");

        // Get all the netBookValueEntryList where previousNetBookValueAmount is null
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is greater than or equal to DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.greaterThanOrEqual=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is greater than or equal to UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.greaterThanOrEqual=" + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is less than or equal to DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.lessThanOrEqual=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is less than or equal to SMALLER_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.lessThanOrEqual=" + SMALLER_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is less than DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.lessThan=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is less than UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.lessThan=" + UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPreviousNetBookValueAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is greater than DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldNotBeFound("previousNetBookValueAmount.greaterThan=" + DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT);

        // Get all the netBookValueEntryList where previousNetBookValueAmount is greater than SMALLER_PREVIOUS_NET_BOOK_VALUE_AMOUNT
        defaultNetBookValueEntryShouldBeFound("previousNetBookValueAmount.greaterThan=" + SMALLER_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost equals to DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.equals=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.equals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost not equals to DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.notEquals=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost not equals to UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.notEquals=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost in DEFAULT_HISTORICAL_COST or UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.in=" + DEFAULT_HISTORICAL_COST + "," + UPDATED_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost equals to UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.in=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost is not null
        defaultNetBookValueEntryShouldBeFound("historicalCost.specified=true");

        // Get all the netBookValueEntryList where historicalCost is null
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost is greater than or equal to DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.greaterThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost is greater than or equal to UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.greaterThanOrEqual=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost is less than or equal to DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.lessThanOrEqual=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost is less than or equal to SMALLER_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.lessThanOrEqual=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost is less than DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.lessThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost is less than UPDATED_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.lessThan=" + UPDATED_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByHistoricalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where historicalCost is greater than DEFAULT_HISTORICAL_COST
        defaultNetBookValueEntryShouldNotBeFound("historicalCost.greaterThan=" + DEFAULT_HISTORICAL_COST);

        // Get all the netBookValueEntryList where historicalCost is greater than SMALLER_HISTORICAL_COST
        defaultNetBookValueEntryShouldBeFound("historicalCost.greaterThan=" + SMALLER_HISTORICAL_COST);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate equals to DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.equals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.equals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate not equals to DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.notEquals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate not equals to UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.notEquals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate in DEFAULT_CAPITALIZATION_DATE or UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.in=" + DEFAULT_CAPITALIZATION_DATE + "," + UPDATED_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.in=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate is not null
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.specified=true");

        // Get all the netBookValueEntryList where capitalizationDate is null
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate is greater than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.greaterThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate is greater than or equal to UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.greaterThanOrEqual=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate is less than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.lessThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate is less than or equal to SMALLER_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.lessThanOrEqual=" + SMALLER_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate is less than DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.lessThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate is less than UPDATED_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.lessThan=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByCapitalizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        // Get all the netBookValueEntryList where capitalizationDate is greater than DEFAULT_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldNotBeFound("capitalizationDate.greaterThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the netBookValueEntryList where capitalizationDate is greater than SMALLER_CAPITALIZATION_DATE
        defaultNetBookValueEntryShouldBeFound("capitalizationDate.greaterThan=" + SMALLER_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setServiceOutlet(serviceOutlet);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the netBookValueEntryList where serviceOutlet equals to serviceOutletId
        defaultNetBookValueEntryShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the netBookValueEntryList where serviceOutlet equals to (serviceOutletId + 1)
        defaultNetBookValueEntryShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setDepreciationPeriod(depreciationPeriod);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the netBookValueEntryList where depreciationPeriod equals to depreciationPeriodId
        defaultNetBookValueEntryShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the netBookValueEntryList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultNetBookValueEntryShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setFiscalMonth(fiscalMonth);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the netBookValueEntryList where fiscalMonth equals to fiscalMonthId
        defaultNetBookValueEntryShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the netBookValueEntryList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultNetBookValueEntryShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByDepreciationMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setDepreciationMethod(depreciationMethod);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long depreciationMethodId = depreciationMethod.getId();

        // Get all the netBookValueEntryList where depreciationMethod equals to depreciationMethodId
        defaultNetBookValueEntryShouldBeFound("depreciationMethodId.equals=" + depreciationMethodId);

        // Get all the netBookValueEntryList where depreciationMethod equals to (depreciationMethodId + 1)
        defaultNetBookValueEntryShouldNotBeFound("depreciationMethodId.equals=" + (depreciationMethodId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setAssetRegistration(assetRegistration);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long assetRegistrationId = assetRegistration.getId();

        // Get all the netBookValueEntryList where assetRegistration equals to assetRegistrationId
        defaultNetBookValueEntryShouldBeFound("assetRegistrationId.equals=" + assetRegistrationId);

        // Get all the netBookValueEntryList where assetRegistration equals to (assetRegistrationId + 1)
        defaultNetBookValueEntryShouldNotBeFound("assetRegistrationId.equals=" + (assetRegistrationId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.setAssetCategory(assetCategory);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long assetCategoryId = assetCategory.getId();

        // Get all the netBookValueEntryList where assetCategory equals to assetCategoryId
        defaultNetBookValueEntryShouldBeFound("assetCategoryId.equals=" + assetCategoryId);

        // Get all the netBookValueEntryList where assetCategory equals to (assetCategoryId + 1)
        defaultNetBookValueEntryShouldNotBeFound("assetCategoryId.equals=" + (assetCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllNetBookValueEntriesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
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
        netBookValueEntry.addPlaceholder(placeholder);
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        Long placeholderId = placeholder.getId();

        // Get all the netBookValueEntryList where placeholder equals to placeholderId
        defaultNetBookValueEntryShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the netBookValueEntryList where placeholder equals to (placeholderId + 1)
        defaultNetBookValueEntryShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNetBookValueEntryShouldBeFound(String filter) throws Exception {
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netBookValueEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nbvIdentifier").value(hasItem(DEFAULT_NBV_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS)))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS)))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(DEFAULT_USEFUL_LIFE_YEARS.doubleValue())))
            .andExpect(jsonPath("$.[*].netBookValueAmount").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].previousNetBookValueAmount").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));

        // Check, that the count call also returns 1
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNetBookValueEntryShouldNotBeFound(String filter) throws Exception {
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNetBookValueEntry() throws Exception {
        // Get the netBookValueEntry
        restNetBookValueEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNetBookValueEntry() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();

        // Update the netBookValueEntry
        NetBookValueEntry updatedNetBookValueEntry = netBookValueEntryRepository.findById(netBookValueEntry.getId()).get();
        // Disconnect from session so that the updates on updatedNetBookValueEntry are not directly saved in db
        em.detach(updatedNetBookValueEntry);
        updatedNetBookValueEntry
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .nbvIdentifier(UPDATED_NBV_IDENTIFIER)
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .netBookValueAmount(UPDATED_NET_BOOK_VALUE_AMOUNT)
            .previousNetBookValueAmount(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(updatedNetBookValueEntry);

        restNetBookValueEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, netBookValueEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);
        NetBookValueEntry testNetBookValueEntry = netBookValueEntryList.get(netBookValueEntryList.size() - 1);
        assertThat(testNetBookValueEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testNetBookValueEntry.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testNetBookValueEntry.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testNetBookValueEntry.getNbvIdentifier()).isEqualTo(UPDATED_NBV_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationJobIdentifier()).isEqualTo(UPDATED_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationBatchIdentifier()).isEqualTo(UPDATED_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNetBookValueEntry.getElapsedMonths()).isEqualTo(UPDATED_ELAPSED_MONTHS);
        assertThat(testNetBookValueEntry.getPriorMonths()).isEqualTo(UPDATED_PRIOR_MONTHS);
        assertThat(testNetBookValueEntry.getUsefulLifeYears()).isEqualTo(UPDATED_USEFUL_LIFE_YEARS);
        assertThat(testNetBookValueEntry.getNetBookValueAmount()).isEqualTo(UPDATED_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getPreviousNetBookValueAmount()).isEqualTo(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getHistoricalCost()).isEqualTo(UPDATED_HISTORICAL_COST);
        assertThat(testNetBookValueEntry.getCapitalizationDate()).isEqualTo(UPDATED_CAPITALIZATION_DATE);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository).save(testNetBookValueEntry);
    }

    @Test
    @Transactional
    void putNonExistingNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, netBookValueEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void putWithIdMismatchNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void partialUpdateNetBookValueEntryWithPatch() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();

        // Update the netBookValueEntry using partial update
        NetBookValueEntry partialUpdatedNetBookValueEntry = new NetBookValueEntry();
        partialUpdatedNetBookValueEntry.setId(netBookValueEntry.getId());

        partialUpdatedNetBookValueEntry
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .nbvIdentifier(UPDATED_NBV_IDENTIFIER)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .netBookValueAmount(UPDATED_NET_BOOK_VALUE_AMOUNT)
            .previousNetBookValueAmount(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT)
            .historicalCost(UPDATED_HISTORICAL_COST);

        restNetBookValueEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetBookValueEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetBookValueEntry))
            )
            .andExpect(status().isOk());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);
        NetBookValueEntry testNetBookValueEntry = netBookValueEntryList.get(netBookValueEntryList.size() - 1);
        assertThat(testNetBookValueEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testNetBookValueEntry.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testNetBookValueEntry.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testNetBookValueEntry.getNbvIdentifier()).isEqualTo(UPDATED_NBV_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationJobIdentifier()).isEqualTo(DEFAULT_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationBatchIdentifier()).isEqualTo(DEFAULT_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNetBookValueEntry.getElapsedMonths()).isEqualTo(UPDATED_ELAPSED_MONTHS);
        assertThat(testNetBookValueEntry.getPriorMonths()).isEqualTo(UPDATED_PRIOR_MONTHS);
        assertThat(testNetBookValueEntry.getUsefulLifeYears()).isEqualTo(DEFAULT_USEFUL_LIFE_YEARS);
        assertThat(testNetBookValueEntry.getNetBookValueAmount()).isEqualByComparingTo(UPDATED_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getPreviousNetBookValueAmount()).isEqualByComparingTo(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getHistoricalCost()).isEqualByComparingTo(UPDATED_HISTORICAL_COST);
        assertThat(testNetBookValueEntry.getCapitalizationDate()).isEqualTo(DEFAULT_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateNetBookValueEntryWithPatch() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();

        // Update the netBookValueEntry using partial update
        NetBookValueEntry partialUpdatedNetBookValueEntry = new NetBookValueEntry();
        partialUpdatedNetBookValueEntry.setId(netBookValueEntry.getId());

        partialUpdatedNetBookValueEntry
            .assetNumber(UPDATED_ASSET_NUMBER)
            .assetTag(UPDATED_ASSET_TAG)
            .assetDescription(UPDATED_ASSET_DESCRIPTION)
            .nbvIdentifier(UPDATED_NBV_IDENTIFIER)
            .compilationJobIdentifier(UPDATED_COMPILATION_JOB_IDENTIFIER)
            .compilationBatchIdentifier(UPDATED_COMPILATION_BATCH_IDENTIFIER)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .netBookValueAmount(UPDATED_NET_BOOK_VALUE_AMOUNT)
            .previousNetBookValueAmount(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT)
            .historicalCost(UPDATED_HISTORICAL_COST)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);

        restNetBookValueEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetBookValueEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetBookValueEntry))
            )
            .andExpect(status().isOk());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);
        NetBookValueEntry testNetBookValueEntry = netBookValueEntryList.get(netBookValueEntryList.size() - 1);
        assertThat(testNetBookValueEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testNetBookValueEntry.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testNetBookValueEntry.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testNetBookValueEntry.getNbvIdentifier()).isEqualTo(UPDATED_NBV_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationJobIdentifier()).isEqualTo(UPDATED_COMPILATION_JOB_IDENTIFIER);
        assertThat(testNetBookValueEntry.getCompilationBatchIdentifier()).isEqualTo(UPDATED_COMPILATION_BATCH_IDENTIFIER);
        assertThat(testNetBookValueEntry.getElapsedMonths()).isEqualTo(UPDATED_ELAPSED_MONTHS);
        assertThat(testNetBookValueEntry.getPriorMonths()).isEqualTo(UPDATED_PRIOR_MONTHS);
        assertThat(testNetBookValueEntry.getUsefulLifeYears()).isEqualTo(UPDATED_USEFUL_LIFE_YEARS);
        assertThat(testNetBookValueEntry.getNetBookValueAmount()).isEqualByComparingTo(UPDATED_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getPreviousNetBookValueAmount()).isEqualByComparingTo(UPDATED_PREVIOUS_NET_BOOK_VALUE_AMOUNT);
        assertThat(testNetBookValueEntry.getHistoricalCost()).isEqualByComparingTo(UPDATED_HISTORICAL_COST);
        assertThat(testNetBookValueEntry.getCapitalizationDate()).isEqualTo(UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, netBookValueEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNetBookValueEntry() throws Exception {
        int databaseSizeBeforeUpdate = netBookValueEntryRepository.findAll().size();
        netBookValueEntry.setId(count.incrementAndGet());

        // Create the NetBookValueEntry
        NetBookValueEntryDTO netBookValueEntryDTO = netBookValueEntryMapper.toDto(netBookValueEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetBookValueEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(netBookValueEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NetBookValueEntry in the database
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(0)).save(netBookValueEntry);
    }

    @Test
    @Transactional
    void deleteNetBookValueEntry() throws Exception {
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);

        int databaseSizeBeforeDelete = netBookValueEntryRepository.findAll().size();

        // Delete the netBookValueEntry
        restNetBookValueEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, netBookValueEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NetBookValueEntry> netBookValueEntryList = netBookValueEntryRepository.findAll();
        assertThat(netBookValueEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NetBookValueEntry in Elasticsearch
        verify(mockNetBookValueEntrySearchRepository, times(1)).deleteById(netBookValueEntry.getId());
    }

    @Test
    @Transactional
    void searchNetBookValueEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        netBookValueEntryRepository.saveAndFlush(netBookValueEntry);
        when(mockNetBookValueEntrySearchRepository.search("id:" + netBookValueEntry.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(netBookValueEntry), PageRequest.of(0, 1), 1));

        // Search the netBookValueEntry
        restNetBookValueEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + netBookValueEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netBookValueEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nbvIdentifier").value(hasItem(DEFAULT_NBV_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationJobIdentifier").value(hasItem(DEFAULT_COMPILATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].compilationBatchIdentifier").value(hasItem(DEFAULT_COMPILATION_BATCH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS)))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS)))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(DEFAULT_USEFUL_LIFE_YEARS.doubleValue())))
            .andExpect(jsonPath("$.[*].netBookValueAmount").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].previousNetBookValueAmount").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NET_BOOK_VALUE_AMOUNT))))
            .andExpect(jsonPath("$.[*].historicalCost").value(hasItem(sameNumber(DEFAULT_HISTORICAL_COST))))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));
    }
}
