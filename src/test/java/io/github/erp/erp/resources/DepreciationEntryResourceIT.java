package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.math.BigDecimal;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the DepreciationEntryResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
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

    private static final UUID DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();

    private static final Integer DEFAULT_BATCH_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_BATCH_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_BATCH_SEQUENCE_NUMBER = 1 - 1;

    private static final String DEFAULT_PROCESSED_ITEMS = "AAAAAAAAAA";
    private static final String UPDATED_PROCESSED_ITEMS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_ITEMS_PROCESSED = 1;
    private static final Integer UPDATED_TOTAL_ITEMS_PROCESSED = 2;
    private static final Integer SMALLER_TOTAL_ITEMS_PROCESSED = 1 - 1;

    private static final Long DEFAULT_ELAPSED_MONTHS = 1L;
    private static final Long UPDATED_ELAPSED_MONTHS = 2L;
    private static final Long SMALLER_ELAPSED_MONTHS = 1L - 1L;

    private static final Long DEFAULT_PRIOR_MONTHS = 1L;
    private static final Long UPDATED_PRIOR_MONTHS = 2L;
    private static final Long SMALLER_PRIOR_MONTHS = 1L - 1L;

    private static final BigDecimal DEFAULT_USEFUL_LIFE_YEARS = new BigDecimal(1);
    private static final BigDecimal UPDATED_USEFUL_LIFE_YEARS = new BigDecimal(2);
    private static final BigDecimal SMALLER_USEFUL_LIFE_YEARS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PREVIOUS_NBV = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREVIOUS_NBV = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREVIOUS_NBV = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_BOOK_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_BOOK_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_BOOK_VALUE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DEPRECIATION_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_PERIOD_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DEPRECIATION_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_PERIOD_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CAPITALIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CAPITALIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CAPITALIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-entries";

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
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .depreciationPeriodIdentifier(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(DEFAULT_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(DEFAULT_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(DEFAULT_FISCAL_QUARTER_IDENTIFIER)
            .batchSequenceNumber(DEFAULT_BATCH_SEQUENCE_NUMBER)
            .processedItems(DEFAULT_PROCESSED_ITEMS)
            .totalItemsProcessed(DEFAULT_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(DEFAULT_ELAPSED_MONTHS)
            .priorMonths(DEFAULT_PRIOR_MONTHS)
            .usefulLifeYears(DEFAULT_USEFUL_LIFE_YEARS)
            .previousNBV(DEFAULT_PREVIOUS_NBV)
            .netBookValue(DEFAULT_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(DEFAULT_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(DEFAULT_DEPRECIATION_PERIOD_END_DATE)
            .capitalizationDate(DEFAULT_CAPITALIZATION_DATE);
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
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER)
            .batchSequenceNumber(UPDATED_BATCH_SEQUENCE_NUMBER)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .totalItemsProcessed(UPDATED_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .previousNBV(UPDATED_PREVIOUS_NBV)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(UPDATED_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(UPDATED_DEPRECIATION_PERIOD_END_DATE)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);
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
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(DEFAULT_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
        assertThat(testDepreciationEntry.getBatchSequenceNumber()).isEqualTo(DEFAULT_BATCH_SEQUENCE_NUMBER);
        assertThat(testDepreciationEntry.getProcessedItems()).isEqualTo(DEFAULT_PROCESSED_ITEMS);
        assertThat(testDepreciationEntry.getTotalItemsProcessed()).isEqualTo(DEFAULT_TOTAL_ITEMS_PROCESSED);
        assertThat(testDepreciationEntry.getElapsedMonths()).isEqualTo(DEFAULT_ELAPSED_MONTHS);
        assertThat(testDepreciationEntry.getPriorMonths()).isEqualTo(DEFAULT_PRIOR_MONTHS);
        assertThat(testDepreciationEntry.getUsefulLifeYears()).isEqualByComparingTo(DEFAULT_USEFUL_LIFE_YEARS);
        assertThat(testDepreciationEntry.getPreviousNBV()).isEqualByComparingTo(DEFAULT_PREVIOUS_NBV);
        assertThat(testDepreciationEntry.getNetBookValue()).isEqualByComparingTo(DEFAULT_NET_BOOK_VALUE);
        assertThat(testDepreciationEntry.getDepreciationPeriodStartDate()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_START_DATE);
        assertThat(testDepreciationEntry.getDepreciationPeriodEndDate()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_END_DATE);
        assertThat(testDepreciationEntry.getCapitalizationDate()).isEqualTo(DEFAULT_CAPITALIZATION_DATE);

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
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSequenceNumber").value(hasItem(DEFAULT_BATCH_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].totalItemsProcessed").value(hasItem(DEFAULT_TOTAL_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));
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
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.depreciationPeriodIdentifier").value(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.depreciationJobIdentifier").value(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalMonthIdentifier").value(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalQuarterIdentifier").value(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.batchSequenceNumber").value(DEFAULT_BATCH_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.processedItems").value(DEFAULT_PROCESSED_ITEMS))
            .andExpect(jsonPath("$.totalItemsProcessed").value(DEFAULT_TOTAL_ITEMS_PROCESSED))
            .andExpect(jsonPath("$.elapsedMonths").value(DEFAULT_ELAPSED_MONTHS.intValue()))
            .andExpect(jsonPath("$.priorMonths").value(DEFAULT_PRIOR_MONTHS.intValue()))
            .andExpect(jsonPath("$.usefulLifeYears").value(sameNumber(DEFAULT_USEFUL_LIFE_YEARS)))
            .andExpect(jsonPath("$.previousNBV").value(sameNumber(DEFAULT_PREVIOUS_NBV)))
            .andExpect(jsonPath("$.netBookValue").value(sameNumber(DEFAULT_NET_BOOK_VALUE)))
            .andExpect(jsonPath("$.depreciationPeriodStartDate").value(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.depreciationPeriodEndDate").value(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString()))
            .andExpect(jsonPath("$.capitalizationDate").value(DEFAULT_CAPITALIZATION_DATE.toString()));
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
    void getAllDepreciationEntriesByDepreciationPeriodIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier equals to DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("depreciationPeriodIdentifier.equals=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodIdentifier.equals=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier not equals to DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodIdentifier.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier not equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("depreciationPeriodIdentifier.notEquals=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier in DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER or UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldBeFound(
            "depreciationPeriodIdentifier.in=" + DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER + "," + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        );

        // Get all the depreciationEntryList where depreciationPeriodIdentifier equals to UPDATED_DEPRECIATION_PERIOD_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodIdentifier.in=" + UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodIdentifier is not null
        defaultDepreciationEntryShouldBeFound("depreciationPeriodIdentifier.specified=true");

        // Get all the depreciationEntryList where depreciationPeriodIdentifier is null
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationJobIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationJobIdentifier equals to DEFAULT_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("depreciationJobIdentifier.equals=" + DEFAULT_DEPRECIATION_JOB_IDENTIFIER);

        // Get all the depreciationEntryList where depreciationJobIdentifier equals to UPDATED_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationJobIdentifier.equals=" + UPDATED_DEPRECIATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationJobIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationJobIdentifier not equals to DEFAULT_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationJobIdentifier.notEquals=" + DEFAULT_DEPRECIATION_JOB_IDENTIFIER);

        // Get all the depreciationEntryList where depreciationJobIdentifier not equals to UPDATED_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("depreciationJobIdentifier.notEquals=" + UPDATED_DEPRECIATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationJobIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationJobIdentifier in DEFAULT_DEPRECIATION_JOB_IDENTIFIER or UPDATED_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldBeFound(
            "depreciationJobIdentifier.in=" + DEFAULT_DEPRECIATION_JOB_IDENTIFIER + "," + UPDATED_DEPRECIATION_JOB_IDENTIFIER
        );

        // Get all the depreciationEntryList where depreciationJobIdentifier equals to UPDATED_DEPRECIATION_JOB_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("depreciationJobIdentifier.in=" + UPDATED_DEPRECIATION_JOB_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationJobIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationJobIdentifier is not null
        defaultDepreciationEntryShouldBeFound("depreciationJobIdentifier.specified=true");

        // Get all the depreciationEntryList where depreciationJobIdentifier is null
        defaultDepreciationEntryShouldNotBeFound("depreciationJobIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalMonthIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalMonthIdentifier equals to DEFAULT_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("fiscalMonthIdentifier.equals=" + DEFAULT_FISCAL_MONTH_IDENTIFIER);

        // Get all the depreciationEntryList where fiscalMonthIdentifier equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalMonthIdentifier.equals=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalMonthIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalMonthIdentifier not equals to DEFAULT_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalMonthIdentifier.notEquals=" + DEFAULT_FISCAL_MONTH_IDENTIFIER);

        // Get all the depreciationEntryList where fiscalMonthIdentifier not equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("fiscalMonthIdentifier.notEquals=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalMonthIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalMonthIdentifier in DEFAULT_FISCAL_MONTH_IDENTIFIER or UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldBeFound(
            "fiscalMonthIdentifier.in=" + DEFAULT_FISCAL_MONTH_IDENTIFIER + "," + UPDATED_FISCAL_MONTH_IDENTIFIER
        );

        // Get all the depreciationEntryList where fiscalMonthIdentifier equals to UPDATED_FISCAL_MONTH_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalMonthIdentifier.in=" + UPDATED_FISCAL_MONTH_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalMonthIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalMonthIdentifier is not null
        defaultDepreciationEntryShouldBeFound("fiscalMonthIdentifier.specified=true");

        // Get all the depreciationEntryList where fiscalMonthIdentifier is null
        defaultDepreciationEntryShouldNotBeFound("fiscalMonthIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalQuarterIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier equals to DEFAULT_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("fiscalQuarterIdentifier.equals=" + DEFAULT_FISCAL_QUARTER_IDENTIFIER);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier equals to UPDATED_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalQuarterIdentifier.equals=" + UPDATED_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalQuarterIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier not equals to DEFAULT_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalQuarterIdentifier.notEquals=" + DEFAULT_FISCAL_QUARTER_IDENTIFIER);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier not equals to UPDATED_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldBeFound("fiscalQuarterIdentifier.notEquals=" + UPDATED_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalQuarterIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier in DEFAULT_FISCAL_QUARTER_IDENTIFIER or UPDATED_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldBeFound(
            "fiscalQuarterIdentifier.in=" + DEFAULT_FISCAL_QUARTER_IDENTIFIER + "," + UPDATED_FISCAL_QUARTER_IDENTIFIER
        );

        // Get all the depreciationEntryList where fiscalQuarterIdentifier equals to UPDATED_FISCAL_QUARTER_IDENTIFIER
        defaultDepreciationEntryShouldNotBeFound("fiscalQuarterIdentifier.in=" + UPDATED_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByFiscalQuarterIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where fiscalQuarterIdentifier is not null
        defaultDepreciationEntryShouldBeFound("fiscalQuarterIdentifier.specified=true");

        // Get all the depreciationEntryList where fiscalQuarterIdentifier is null
        defaultDepreciationEntryShouldNotBeFound("fiscalQuarterIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber equals to DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.equals=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber equals to UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.equals=" + UPDATED_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber not equals to DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.notEquals=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber not equals to UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.notEquals=" + UPDATED_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber in DEFAULT_BATCH_SEQUENCE_NUMBER or UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound(
            "batchSequenceNumber.in=" + DEFAULT_BATCH_SEQUENCE_NUMBER + "," + UPDATED_BATCH_SEQUENCE_NUMBER
        );

        // Get all the depreciationEntryList where batchSequenceNumber equals to UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.in=" + UPDATED_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber is not null
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.specified=true");

        // Get all the depreciationEntryList where batchSequenceNumber is null
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber is greater than or equal to DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.greaterThanOrEqual=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber is greater than or equal to UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.greaterThanOrEqual=" + UPDATED_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber is less than or equal to DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.lessThanOrEqual=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber is less than or equal to SMALLER_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.lessThanOrEqual=" + SMALLER_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber is less than DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.lessThan=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber is less than UPDATED_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.lessThan=" + UPDATED_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByBatchSequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where batchSequenceNumber is greater than DEFAULT_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldNotBeFound("batchSequenceNumber.greaterThan=" + DEFAULT_BATCH_SEQUENCE_NUMBER);

        // Get all the depreciationEntryList where batchSequenceNumber is greater than SMALLER_BATCH_SEQUENCE_NUMBER
        defaultDepreciationEntryShouldBeFound("batchSequenceNumber.greaterThan=" + SMALLER_BATCH_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems equals to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationEntryShouldBeFound("processedItems.equals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationEntryList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldNotBeFound("processedItems.equals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems not equals to DEFAULT_PROCESSED_ITEMS
        defaultDepreciationEntryShouldNotBeFound("processedItems.notEquals=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationEntryList where processedItems not equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldBeFound("processedItems.notEquals=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems in DEFAULT_PROCESSED_ITEMS or UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldBeFound("processedItems.in=" + DEFAULT_PROCESSED_ITEMS + "," + UPDATED_PROCESSED_ITEMS);

        // Get all the depreciationEntryList where processedItems equals to UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldNotBeFound("processedItems.in=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems is not null
        defaultDepreciationEntryShouldBeFound("processedItems.specified=true");

        // Get all the depreciationEntryList where processedItems is null
        defaultDepreciationEntryShouldNotBeFound("processedItems.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems contains DEFAULT_PROCESSED_ITEMS
        defaultDepreciationEntryShouldBeFound("processedItems.contains=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationEntryList where processedItems contains UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldNotBeFound("processedItems.contains=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByProcessedItemsNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where processedItems does not contain DEFAULT_PROCESSED_ITEMS
        defaultDepreciationEntryShouldNotBeFound("processedItems.doesNotContain=" + DEFAULT_PROCESSED_ITEMS);

        // Get all the depreciationEntryList where processedItems does not contain UPDATED_PROCESSED_ITEMS
        defaultDepreciationEntryShouldBeFound("processedItems.doesNotContain=" + UPDATED_PROCESSED_ITEMS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed equals to DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.equals=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed equals to UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.equals=" + UPDATED_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed not equals to DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.notEquals=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed not equals to UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.notEquals=" + UPDATED_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed in DEFAULT_TOTAL_ITEMS_PROCESSED or UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound(
            "totalItemsProcessed.in=" + DEFAULT_TOTAL_ITEMS_PROCESSED + "," + UPDATED_TOTAL_ITEMS_PROCESSED
        );

        // Get all the depreciationEntryList where totalItemsProcessed equals to UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.in=" + UPDATED_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed is not null
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.specified=true");

        // Get all the depreciationEntryList where totalItemsProcessed is null
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed is greater than or equal to DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed is greater than or equal to UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed is less than or equal to DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed is less than or equal to SMALLER_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed is less than DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.lessThan=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed is less than UPDATED_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.lessThan=" + UPDATED_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByTotalItemsProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where totalItemsProcessed is greater than DEFAULT_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldNotBeFound("totalItemsProcessed.greaterThan=" + DEFAULT_TOTAL_ITEMS_PROCESSED);

        // Get all the depreciationEntryList where totalItemsProcessed is greater than SMALLER_TOTAL_ITEMS_PROCESSED
        defaultDepreciationEntryShouldBeFound("totalItemsProcessed.greaterThan=" + SMALLER_TOTAL_ITEMS_PROCESSED);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths equals to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.equals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.equals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths not equals to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.notEquals=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths not equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.notEquals=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths in DEFAULT_ELAPSED_MONTHS or UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.in=" + DEFAULT_ELAPSED_MONTHS + "," + UPDATED_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths equals to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.in=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths is not null
        defaultDepreciationEntryShouldBeFound("elapsedMonths.specified=true");

        // Get all the depreciationEntryList where elapsedMonths is null
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths is greater than or equal to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.greaterThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths is greater than or equal to UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.greaterThanOrEqual=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths is less than or equal to DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.lessThanOrEqual=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths is less than or equal to SMALLER_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.lessThanOrEqual=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths is less than DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.lessThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths is less than UPDATED_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.lessThan=" + UPDATED_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByElapsedMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where elapsedMonths is greater than DEFAULT_ELAPSED_MONTHS
        defaultDepreciationEntryShouldNotBeFound("elapsedMonths.greaterThan=" + DEFAULT_ELAPSED_MONTHS);

        // Get all the depreciationEntryList where elapsedMonths is greater than SMALLER_ELAPSED_MONTHS
        defaultDepreciationEntryShouldBeFound("elapsedMonths.greaterThan=" + SMALLER_ELAPSED_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths equals to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.equals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.equals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths not equals to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.notEquals=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths not equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.notEquals=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths in DEFAULT_PRIOR_MONTHS or UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.in=" + DEFAULT_PRIOR_MONTHS + "," + UPDATED_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths equals to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.in=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths is not null
        defaultDepreciationEntryShouldBeFound("priorMonths.specified=true");

        // Get all the depreciationEntryList where priorMonths is null
        defaultDepreciationEntryShouldNotBeFound("priorMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths is greater than or equal to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.greaterThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths is greater than or equal to UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.greaterThanOrEqual=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths is less than or equal to DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.lessThanOrEqual=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths is less than or equal to SMALLER_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.lessThanOrEqual=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths is less than DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.lessThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths is less than UPDATED_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.lessThan=" + UPDATED_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPriorMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where priorMonths is greater than DEFAULT_PRIOR_MONTHS
        defaultDepreciationEntryShouldNotBeFound("priorMonths.greaterThan=" + DEFAULT_PRIOR_MONTHS);

        // Get all the depreciationEntryList where priorMonths is greater than SMALLER_PRIOR_MONTHS
        defaultDepreciationEntryShouldBeFound("priorMonths.greaterThan=" + SMALLER_PRIOR_MONTHS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.equals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.equals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears not equals to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.notEquals=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears not equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.notEquals=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears in DEFAULT_USEFUL_LIFE_YEARS or UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.in=" + DEFAULT_USEFUL_LIFE_YEARS + "," + UPDATED_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears equals to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.in=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears is not null
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.specified=true");

        // Get all the depreciationEntryList where usefulLifeYears is null
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears is greater than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.greaterThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears is greater than or equal to UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.greaterThanOrEqual=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears is less than or equal to DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.lessThanOrEqual=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears is less than or equal to SMALLER_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.lessThanOrEqual=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears is less than DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.lessThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears is less than UPDATED_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.lessThan=" + UPDATED_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByUsefulLifeYearsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where usefulLifeYears is greater than DEFAULT_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldNotBeFound("usefulLifeYears.greaterThan=" + DEFAULT_USEFUL_LIFE_YEARS);

        // Get all the depreciationEntryList where usefulLifeYears is greater than SMALLER_USEFUL_LIFE_YEARS
        defaultDepreciationEntryShouldBeFound("usefulLifeYears.greaterThan=" + SMALLER_USEFUL_LIFE_YEARS);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV equals to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.equals=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.equals=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV not equals to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.notEquals=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV not equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.notEquals=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV in DEFAULT_PREVIOUS_NBV or UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.in=" + DEFAULT_PREVIOUS_NBV + "," + UPDATED_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV equals to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.in=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV is not null
        defaultDepreciationEntryShouldBeFound("previousNBV.specified=true");

        // Get all the depreciationEntryList where previousNBV is null
        defaultDepreciationEntryShouldNotBeFound("previousNBV.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV is greater than or equal to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.greaterThanOrEqual=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV is greater than or equal to UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.greaterThanOrEqual=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV is less than or equal to DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.lessThanOrEqual=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV is less than or equal to SMALLER_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.lessThanOrEqual=" + SMALLER_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV is less than DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.lessThan=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV is less than UPDATED_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.lessThan=" + UPDATED_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByPreviousNBVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where previousNBV is greater than DEFAULT_PREVIOUS_NBV
        defaultDepreciationEntryShouldNotBeFound("previousNBV.greaterThan=" + DEFAULT_PREVIOUS_NBV);

        // Get all the depreciationEntryList where previousNBV is greater than SMALLER_PREVIOUS_NBV
        defaultDepreciationEntryShouldBeFound("previousNBV.greaterThan=" + SMALLER_PREVIOUS_NBV);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue equals to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.equals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.equals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue not equals to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.notEquals=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue not equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.notEquals=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue in DEFAULT_NET_BOOK_VALUE or UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.in=" + DEFAULT_NET_BOOK_VALUE + "," + UPDATED_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue equals to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.in=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue is not null
        defaultDepreciationEntryShouldBeFound("netBookValue.specified=true");

        // Get all the depreciationEntryList where netBookValue is null
        defaultDepreciationEntryShouldNotBeFound("netBookValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue is greater than or equal to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.greaterThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue is greater than or equal to UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.greaterThanOrEqual=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue is less than or equal to DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.lessThanOrEqual=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue is less than or equal to SMALLER_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.lessThanOrEqual=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue is less than DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.lessThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue is less than UPDATED_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.lessThan=" + UPDATED_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByNetBookValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where netBookValue is greater than DEFAULT_NET_BOOK_VALUE
        defaultDepreciationEntryShouldNotBeFound("netBookValue.greaterThan=" + DEFAULT_NET_BOOK_VALUE);

        // Get all the depreciationEntryList where netBookValue is greater than SMALLER_NET_BOOK_VALUE
        defaultDepreciationEntryShouldBeFound("netBookValue.greaterThan=" + SMALLER_NET_BOOK_VALUE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate equals to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.equals=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.equals=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate not equals to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate not equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.notEquals=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate in DEFAULT_DEPRECIATION_PERIOD_START_DATE or UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound(
            "depreciationPeriodStartDate.in=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE + "," + UPDATED_DEPRECIATION_PERIOD_START_DATE
        );

        // Get all the depreciationEntryList where depreciationPeriodStartDate equals to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.in=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is not null
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.specified=true");

        // Get all the depreciationEntryList where depreciationPeriodStartDate is null
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is greater than or equal to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is greater than or equal to UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound(
            "depreciationPeriodStartDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_PERIOD_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is less than or equal to DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is less than or equal to SMALLER_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is less than DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.lessThan=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is less than UPDATED_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.lessThan=" + UPDATED_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is greater than DEFAULT_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodStartDate.greaterThan=" + DEFAULT_DEPRECIATION_PERIOD_START_DATE);

        // Get all the depreciationEntryList where depreciationPeriodStartDate is greater than SMALLER_DEPRECIATION_PERIOD_START_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodStartDate.greaterThan=" + SMALLER_DEPRECIATION_PERIOD_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate equals to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.equals=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.equals=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate not equals to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate not equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.notEquals=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate in DEFAULT_DEPRECIATION_PERIOD_END_DATE or UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound(
            "depreciationPeriodEndDate.in=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE + "," + UPDATED_DEPRECIATION_PERIOD_END_DATE
        );

        // Get all the depreciationEntryList where depreciationPeriodEndDate equals to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.in=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is not null
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.specified=true");

        // Get all the depreciationEntryList where depreciationPeriodEndDate is null
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is greater than or equal to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is greater than or equal to UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is less than or equal to DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is less than or equal to SMALLER_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is less than DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.lessThan=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is less than UPDATED_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.lessThan=" + UPDATED_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationPeriodEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is greater than DEFAULT_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldNotBeFound("depreciationPeriodEndDate.greaterThan=" + DEFAULT_DEPRECIATION_PERIOD_END_DATE);

        // Get all the depreciationEntryList where depreciationPeriodEndDate is greater than SMALLER_DEPRECIATION_PERIOD_END_DATE
        defaultDepreciationEntryShouldBeFound("depreciationPeriodEndDate.greaterThan=" + SMALLER_DEPRECIATION_PERIOD_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate equals to DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.equals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.equals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate not equals to DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.notEquals=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate not equals to UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.notEquals=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate in DEFAULT_CAPITALIZATION_DATE or UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.in=" + DEFAULT_CAPITALIZATION_DATE + "," + UPDATED_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate equals to UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.in=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate is not null
        defaultDepreciationEntryShouldBeFound("capitalizationDate.specified=true");

        // Get all the depreciationEntryList where capitalizationDate is null
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate is greater than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.greaterThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate is greater than or equal to UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.greaterThanOrEqual=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate is less than or equal to DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.lessThanOrEqual=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate is less than or equal to SMALLER_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.lessThanOrEqual=" + SMALLER_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate is less than DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.lessThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate is less than UPDATED_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.lessThan=" + UPDATED_CAPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByCapitalizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList where capitalizationDate is greater than DEFAULT_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldNotBeFound("capitalizationDate.greaterThan=" + DEFAULT_CAPITALIZATION_DATE);

        // Get all the depreciationEntryList where capitalizationDate is greater than SMALLER_CAPITALIZATION_DATE
        defaultDepreciationEntryShouldBeFound("capitalizationDate.greaterThan=" + SMALLER_CAPITALIZATION_DATE);
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

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationJobIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        DepreciationJob depreciationJob;
        if (TestUtil.findAll(em, DepreciationJob.class).isEmpty()) {
            depreciationJob = DepreciationJobResourceIT.createEntity(em);
            em.persist(depreciationJob);
            em.flush();
        } else {
            depreciationJob = TestUtil.findAll(em, DepreciationJob.class).get(0);
        }
        em.persist(depreciationJob);
        em.flush();
        depreciationEntry.setDepreciationJob(depreciationJob);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long depreciationJobId = depreciationJob.getId();

        // Get all the depreciationEntryList where depreciationJob equals to depreciationJobId
        defaultDepreciationEntryShouldBeFound("depreciationJobId.equals=" + depreciationJobId);

        // Get all the depreciationEntryList where depreciationJob equals to (depreciationJobId + 1)
        defaultDepreciationEntryShouldNotBeFound("depreciationJobId.equals=" + (depreciationJobId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationEntriesByDepreciationBatchSequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        DepreciationBatchSequence depreciationBatchSequence;
        if (TestUtil.findAll(em, DepreciationBatchSequence.class).isEmpty()) {
            depreciationBatchSequence = DepreciationBatchSequenceResourceIT.createEntity(em);
            em.persist(depreciationBatchSequence);
            em.flush();
        } else {
            depreciationBatchSequence = TestUtil.findAll(em, DepreciationBatchSequence.class).get(0);
        }
        em.persist(depreciationBatchSequence);
        em.flush();
        depreciationEntry.setDepreciationBatchSequence(depreciationBatchSequence);
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        Long depreciationBatchSequenceId = depreciationBatchSequence.getId();

        // Get all the depreciationEntryList where depreciationBatchSequence equals to depreciationBatchSequenceId
        defaultDepreciationEntryShouldBeFound("depreciationBatchSequenceId.equals=" + depreciationBatchSequenceId);

        // Get all the depreciationEntryList where depreciationBatchSequence equals to (depreciationBatchSequenceId + 1)
        defaultDepreciationEntryShouldNotBeFound("depreciationBatchSequenceId.equals=" + (depreciationBatchSequenceId + 1));
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
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSequenceNumber").value(hasItem(DEFAULT_BATCH_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].totalItemsProcessed").value(hasItem(DEFAULT_TOTAL_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));

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
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER)
            .batchSequenceNumber(UPDATED_BATCH_SEQUENCE_NUMBER)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .totalItemsProcessed(UPDATED_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .previousNBV(UPDATED_PREVIOUS_NBV)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(UPDATED_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(UPDATED_DEPRECIATION_PERIOD_END_DATE)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);
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
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        assertThat(testDepreciationEntry.getBatchSequenceNumber()).isEqualTo(UPDATED_BATCH_SEQUENCE_NUMBER);
        assertThat(testDepreciationEntry.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationEntry.getTotalItemsProcessed()).isEqualTo(UPDATED_TOTAL_ITEMS_PROCESSED);
        assertThat(testDepreciationEntry.getElapsedMonths()).isEqualTo(UPDATED_ELAPSED_MONTHS);
        assertThat(testDepreciationEntry.getPriorMonths()).isEqualTo(UPDATED_PRIOR_MONTHS);
        assertThat(testDepreciationEntry.getUsefulLifeYears()).isEqualTo(UPDATED_USEFUL_LIFE_YEARS);
        assertThat(testDepreciationEntry.getPreviousNBV()).isEqualTo(UPDATED_PREVIOUS_NBV);
        assertThat(testDepreciationEntry.getNetBookValue()).isEqualTo(UPDATED_NET_BOOK_VALUE);
        assertThat(testDepreciationEntry.getDepreciationPeriodStartDate()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_START_DATE);
        assertThat(testDepreciationEntry.getDepreciationPeriodEndDate()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_END_DATE);
        assertThat(testDepreciationEntry.getCapitalizationDate()).isEqualTo(UPDATED_CAPITALIZATION_DATE);

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

        partialUpdatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .batchSequenceNumber(UPDATED_BATCH_SEQUENCE_NUMBER)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);

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
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
        assertThat(testDepreciationEntry.getBatchSequenceNumber()).isEqualTo(UPDATED_BATCH_SEQUENCE_NUMBER);
        assertThat(testDepreciationEntry.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationEntry.getTotalItemsProcessed()).isEqualTo(DEFAULT_TOTAL_ITEMS_PROCESSED);
        assertThat(testDepreciationEntry.getElapsedMonths()).isEqualTo(DEFAULT_ELAPSED_MONTHS);
        assertThat(testDepreciationEntry.getPriorMonths()).isEqualTo(DEFAULT_PRIOR_MONTHS);
        assertThat(testDepreciationEntry.getUsefulLifeYears()).isEqualByComparingTo(UPDATED_USEFUL_LIFE_YEARS);
        assertThat(testDepreciationEntry.getPreviousNBV()).isEqualByComparingTo(DEFAULT_PREVIOUS_NBV);
        assertThat(testDepreciationEntry.getNetBookValue()).isEqualByComparingTo(DEFAULT_NET_BOOK_VALUE);
        assertThat(testDepreciationEntry.getDepreciationPeriodStartDate()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_START_DATE);
        assertThat(testDepreciationEntry.getDepreciationPeriodEndDate()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_END_DATE);
        assertThat(testDepreciationEntry.getCapitalizationDate()).isEqualTo(UPDATED_CAPITALIZATION_DATE);
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
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER)
            .batchSequenceNumber(UPDATED_BATCH_SEQUENCE_NUMBER)
            .processedItems(UPDATED_PROCESSED_ITEMS)
            .totalItemsProcessed(UPDATED_TOTAL_ITEMS_PROCESSED)
            .elapsedMonths(UPDATED_ELAPSED_MONTHS)
            .priorMonths(UPDATED_PRIOR_MONTHS)
            .usefulLifeYears(UPDATED_USEFUL_LIFE_YEARS)
            .previousNBV(UPDATED_PREVIOUS_NBV)
            .netBookValue(UPDATED_NET_BOOK_VALUE)
            .depreciationPeriodStartDate(UPDATED_DEPRECIATION_PERIOD_START_DATE)
            .depreciationPeriodEndDate(UPDATED_DEPRECIATION_PERIOD_END_DATE)
            .capitalizationDate(UPDATED_CAPITALIZATION_DATE);

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
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        assertThat(testDepreciationEntry.getBatchSequenceNumber()).isEqualTo(UPDATED_BATCH_SEQUENCE_NUMBER);
        assertThat(testDepreciationEntry.getProcessedItems()).isEqualTo(UPDATED_PROCESSED_ITEMS);
        assertThat(testDepreciationEntry.getTotalItemsProcessed()).isEqualTo(UPDATED_TOTAL_ITEMS_PROCESSED);
        assertThat(testDepreciationEntry.getElapsedMonths()).isEqualTo(UPDATED_ELAPSED_MONTHS);
        assertThat(testDepreciationEntry.getPriorMonths()).isEqualTo(UPDATED_PRIOR_MONTHS);
        assertThat(testDepreciationEntry.getUsefulLifeYears()).isEqualByComparingTo(UPDATED_USEFUL_LIFE_YEARS);
        assertThat(testDepreciationEntry.getPreviousNBV()).isEqualByComparingTo(UPDATED_PREVIOUS_NBV);
        assertThat(testDepreciationEntry.getNetBookValue()).isEqualByComparingTo(UPDATED_NET_BOOK_VALUE);
        assertThat(testDepreciationEntry.getDepreciationPeriodStartDate()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_START_DATE);
        assertThat(testDepreciationEntry.getDepreciationPeriodEndDate()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_END_DATE);
        assertThat(testDepreciationEntry.getCapitalizationDate()).isEqualTo(UPDATED_CAPITALIZATION_DATE);
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
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].batchSequenceNumber").value(hasItem(DEFAULT_BATCH_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].processedItems").value(hasItem(DEFAULT_PROCESSED_ITEMS)))
            .andExpect(jsonPath("$.[*].totalItemsProcessed").value(hasItem(DEFAULT_TOTAL_ITEMS_PROCESSED)))
            .andExpect(jsonPath("$.[*].elapsedMonths").value(hasItem(DEFAULT_ELAPSED_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].priorMonths").value(hasItem(DEFAULT_PRIOR_MONTHS.intValue())))
            .andExpect(jsonPath("$.[*].usefulLifeYears").value(hasItem(sameNumber(DEFAULT_USEFUL_LIFE_YEARS))))
            .andExpect(jsonPath("$.[*].previousNBV").value(hasItem(sameNumber(DEFAULT_PREVIOUS_NBV))))
            .andExpect(jsonPath("$.[*].netBookValue").value(hasItem(sameNumber(DEFAULT_NET_BOOK_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationPeriodStartDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodEndDate").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].capitalizationDate").value(hasItem(DEFAULT_CAPITALIZATION_DATE.toString())));
    }
}
