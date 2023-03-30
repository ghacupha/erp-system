package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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
import io.github.erp.repository.LeaseModelMetadataRepository;
import io.github.erp.repository.search.LeaseModelMetadataSearchRepository;
import io.github.erp.service.LeaseModelMetadataService;
import io.github.erp.service.dto.LeaseModelMetadataDTO;
import io.github.erp.service.mapper.LeaseModelMetadataMapper;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LeaseModelMetadataResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseModelMetadataResourceIT {

    private static final String DEFAULT_MODEL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MODEL_VERSION = 1D;
    private static final Double UPDATED_MODEL_VERSION = 2D;
    private static final Double SMALLER_MODEL_VERSION = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_MODEL_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MODEL_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MODEL_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MODEL_NOTES_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_ANNUAL_DISCOUNTING_RATE = 1D;
    private static final Double UPDATED_ANNUAL_DISCOUNTING_RATE = 2D;
    private static final Double SMALLER_ANNUAL_DISCOUNTING_RATE = 1D - 1D;

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TERMINAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TERMINAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_TOTAL_REPORTING_PERIODS = 1D;
    private static final Double UPDATED_TOTAL_REPORTING_PERIODS = 2D;
    private static final Double SMALLER_TOTAL_REPORTING_PERIODS = 1D - 1D;

    private static final Double DEFAULT_REPORTING_PERIODS_PER_YEAR = 1D;
    private static final Double UPDATED_REPORTING_PERIODS_PER_YEAR = 2D;
    private static final Double SMALLER_REPORTING_PERIODS_PER_YEAR = 1D - 1D;

    private static final Double DEFAULT_SETTLEMENT_PERIODS_PER_YEAR = 1D;
    private static final Double UPDATED_SETTLEMENT_PERIODS_PER_YEAR = 2D;
    private static final Double SMALLER_SETTLEMENT_PERIODS_PER_YEAR = 1D - 1D;

    private static final BigDecimal DEFAULT_INITIAL_LIABILITY_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INITIAL_LIABILITY_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INITIAL_LIABILITY_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INITIAL_ROU_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INITIAL_ROU_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INITIAL_ROU_AMOUNT = new BigDecimal(1 - 1);

    private static final Double DEFAULT_TOTAL_DEPRECIATION_PERIODS = 1D;
    private static final Double UPDATED_TOTAL_DEPRECIATION_PERIODS = 2D;
    private static final Double SMALLER_TOTAL_DEPRECIATION_PERIODS = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/leases/lease-model-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-model-metadata";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseModelMetadataRepository leaseModelMetadataRepository;

    @Mock
    private LeaseModelMetadataRepository leaseModelMetadataRepositoryMock;

    @Autowired
    private LeaseModelMetadataMapper leaseModelMetadataMapper;

    @Mock
    private LeaseModelMetadataService leaseModelMetadataServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseModelMetadataSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseModelMetadataSearchRepository mockLeaseModelMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseModelMetadataMockMvc;

    private LeaseModelMetadata leaseModelMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseModelMetadata createEntity(EntityManager em) {
        LeaseModelMetadata leaseModelMetadata = new LeaseModelMetadata()
            .modelTitle(DEFAULT_MODEL_TITLE)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .description(DEFAULT_DESCRIPTION)
            .modelNotes(DEFAULT_MODEL_NOTES)
            .modelNotesContentType(DEFAULT_MODEL_NOTES_CONTENT_TYPE)
            .annualDiscountingRate(DEFAULT_ANNUAL_DISCOUNTING_RATE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .terminalDate(DEFAULT_TERMINAL_DATE)
            .totalReportingPeriods(DEFAULT_TOTAL_REPORTING_PERIODS)
            .reportingPeriodsPerYear(DEFAULT_REPORTING_PERIODS_PER_YEAR)
            .settlementPeriodsPerYear(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR)
            .initialLiabilityAmount(DEFAULT_INITIAL_LIABILITY_AMOUNT)
            .initialROUAmount(DEFAULT_INITIAL_ROU_AMOUNT)
            .totalDepreciationPeriods(DEFAULT_TOTAL_DEPRECIATION_PERIODS);
        // Add required entity
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        leaseModelMetadata.setLeaseContract(leaseContract);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        leaseModelMetadata.setLiabilityCurrency(settlementCurrency);
        // Add required entity
        leaseModelMetadata.setRouAssetCurrency(settlementCurrency);
        return leaseModelMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseModelMetadata createUpdatedEntity(EntityManager em) {
        LeaseModelMetadata leaseModelMetadata = new LeaseModelMetadata()
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .modelNotes(UPDATED_MODEL_NOTES)
            .modelNotesContentType(UPDATED_MODEL_NOTES_CONTENT_TYPE)
            .annualDiscountingRate(UPDATED_ANNUAL_DISCOUNTING_RATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE)
            .totalReportingPeriods(UPDATED_TOTAL_REPORTING_PERIODS)
            .reportingPeriodsPerYear(UPDATED_REPORTING_PERIODS_PER_YEAR)
            .settlementPeriodsPerYear(UPDATED_SETTLEMENT_PERIODS_PER_YEAR)
            .initialLiabilityAmount(UPDATED_INITIAL_LIABILITY_AMOUNT)
            .initialROUAmount(UPDATED_INITIAL_ROU_AMOUNT)
            .totalDepreciationPeriods(UPDATED_TOTAL_DEPRECIATION_PERIODS);
        // Add required entity
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        leaseModelMetadata.setLeaseContract(leaseContract);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createUpdatedEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        leaseModelMetadata.setLiabilityCurrency(settlementCurrency);
        // Add required entity
        leaseModelMetadata.setRouAssetCurrency(settlementCurrency);
        return leaseModelMetadata;
    }

    @BeforeEach
    public void initTest() {
        leaseModelMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeCreate = leaseModelMetadataRepository.findAll().size();
        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);
        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseModelMetadata testLeaseModelMetadata = leaseModelMetadataList.get(leaseModelMetadataList.size() - 1);
        assertThat(testLeaseModelMetadata.getModelTitle()).isEqualTo(DEFAULT_MODEL_TITLE);
        assertThat(testLeaseModelMetadata.getModelVersion()).isEqualTo(DEFAULT_MODEL_VERSION);
        assertThat(testLeaseModelMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeaseModelMetadata.getModelNotes()).isEqualTo(DEFAULT_MODEL_NOTES);
        assertThat(testLeaseModelMetadata.getModelNotesContentType()).isEqualTo(DEFAULT_MODEL_NOTES_CONTENT_TYPE);
        assertThat(testLeaseModelMetadata.getAnnualDiscountingRate()).isEqualTo(DEFAULT_ANNUAL_DISCOUNTING_RATE);
        assertThat(testLeaseModelMetadata.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testLeaseModelMetadata.getTerminalDate()).isEqualTo(DEFAULT_TERMINAL_DATE);
        assertThat(testLeaseModelMetadata.getTotalReportingPeriods()).isEqualTo(DEFAULT_TOTAL_REPORTING_PERIODS);
        assertThat(testLeaseModelMetadata.getReportingPeriodsPerYear()).isEqualTo(DEFAULT_REPORTING_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getSettlementPeriodsPerYear()).isEqualTo(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getInitialLiabilityAmount()).isEqualByComparingTo(DEFAULT_INITIAL_LIABILITY_AMOUNT);
        assertThat(testLeaseModelMetadata.getInitialROUAmount()).isEqualByComparingTo(DEFAULT_INITIAL_ROU_AMOUNT);
        assertThat(testLeaseModelMetadata.getTotalDepreciationPeriods()).isEqualTo(DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(1)).save(testLeaseModelMetadata);
    }

    @Test
    @Transactional
    void createLeaseModelMetadataWithExistingId() throws Exception {
        // Create the LeaseModelMetadata with an existing ID
        leaseModelMetadata.setId(1L);
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        int databaseSizeBeforeCreate = leaseModelMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void checkModelTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseModelMetadataRepository.findAll().size();
        // set the field null
        leaseModelMetadata.setModelTitle(null);

        // Create the LeaseModelMetadata, which fails.
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseModelMetadataRepository.findAll().size();
        // set the field null
        leaseModelMetadata.setModelVersion(null);

        // Create the LeaseModelMetadata, which fails.
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnnualDiscountingRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseModelMetadataRepository.findAll().size();
        // set the field null
        leaseModelMetadata.setAnnualDiscountingRate(null);

        // Create the LeaseModelMetadata, which fails.
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseModelMetadataRepository.findAll().size();
        // set the field null
        leaseModelMetadata.setCommencementDate(null);

        // Create the LeaseModelMetadata, which fails.
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseModelMetadataRepository.findAll().size();
        // set the field null
        leaseModelMetadata.setTerminalDate(null);

        // Create the LeaseModelMetadata, which fails.
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadata() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNotesContentType").value(hasItem(DEFAULT_MODEL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].modelNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_MODEL_NOTES))))
            .andExpect(jsonPath("$.[*].annualDiscountingRate").value(hasItem(DEFAULT_ANNUAL_DISCOUNTING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalReportingPeriods").value(hasItem(DEFAULT_TOTAL_REPORTING_PERIODS.doubleValue())))
            .andExpect(jsonPath("$.[*].reportingPeriodsPerYear").value(hasItem(DEFAULT_REPORTING_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].settlementPeriodsPerYear").value(hasItem(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].initialLiabilityAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].initialROUAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_ROU_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalDepreciationPeriods").value(hasItem(DEFAULT_TOTAL_DEPRECIATION_PERIODS.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseModelMetadataWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaseModelMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseModelMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseModelMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseModelMetadataWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaseModelMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseModelMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseModelMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLeaseModelMetadata() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get the leaseModelMetadata
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseModelMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseModelMetadata.getId().intValue()))
            .andExpect(jsonPath("$.modelTitle").value(DEFAULT_MODEL_TITLE))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.modelNotesContentType").value(DEFAULT_MODEL_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.modelNotes").value(Base64Utils.encodeToString(DEFAULT_MODEL_NOTES)))
            .andExpect(jsonPath("$.annualDiscountingRate").value(DEFAULT_ANNUAL_DISCOUNTING_RATE.doubleValue()))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.terminalDate").value(DEFAULT_TERMINAL_DATE.toString()))
            .andExpect(jsonPath("$.totalReportingPeriods").value(DEFAULT_TOTAL_REPORTING_PERIODS.doubleValue()))
            .andExpect(jsonPath("$.reportingPeriodsPerYear").value(DEFAULT_REPORTING_PERIODS_PER_YEAR.doubleValue()))
            .andExpect(jsonPath("$.settlementPeriodsPerYear").value(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR.doubleValue()))
            .andExpect(jsonPath("$.initialLiabilityAmount").value(sameNumber(DEFAULT_INITIAL_LIABILITY_AMOUNT)))
            .andExpect(jsonPath("$.initialROUAmount").value(sameNumber(DEFAULT_INITIAL_ROU_AMOUNT)))
            .andExpect(jsonPath("$.totalDepreciationPeriods").value(DEFAULT_TOTAL_DEPRECIATION_PERIODS.doubleValue()));
    }

    @Test
    @Transactional
    void getLeaseModelMetadataByIdFiltering() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        Long id = leaseModelMetadata.getId();

        defaultLeaseModelMetadataShouldBeFound("id.equals=" + id);
        defaultLeaseModelMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseModelMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseModelMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseModelMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseModelMetadataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle equals to DEFAULT_MODEL_TITLE
        defaultLeaseModelMetadataShouldBeFound("modelTitle.equals=" + DEFAULT_MODEL_TITLE);

        // Get all the leaseModelMetadataList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.equals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle not equals to DEFAULT_MODEL_TITLE
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.notEquals=" + DEFAULT_MODEL_TITLE);

        // Get all the leaseModelMetadataList where modelTitle not equals to UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldBeFound("modelTitle.notEquals=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle in DEFAULT_MODEL_TITLE or UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldBeFound("modelTitle.in=" + DEFAULT_MODEL_TITLE + "," + UPDATED_MODEL_TITLE);

        // Get all the leaseModelMetadataList where modelTitle equals to UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.in=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle is not null
        defaultLeaseModelMetadataShouldBeFound("modelTitle.specified=true");

        // Get all the leaseModelMetadataList where modelTitle is null
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleContainsSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle contains DEFAULT_MODEL_TITLE
        defaultLeaseModelMetadataShouldBeFound("modelTitle.contains=" + DEFAULT_MODEL_TITLE);

        // Get all the leaseModelMetadataList where modelTitle contains UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.contains=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelTitleNotContainsSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelTitle does not contain DEFAULT_MODEL_TITLE
        defaultLeaseModelMetadataShouldNotBeFound("modelTitle.doesNotContain=" + DEFAULT_MODEL_TITLE);

        // Get all the leaseModelMetadataList where modelTitle does not contain UPDATED_MODEL_TITLE
        defaultLeaseModelMetadataShouldBeFound("modelTitle.doesNotContain=" + UPDATED_MODEL_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion equals to DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.equals=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion not equals to DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.notEquals=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion not equals to UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.notEquals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion in DEFAULT_MODEL_VERSION or UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion equals to UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.in=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion is not null
        defaultLeaseModelMetadataShouldBeFound("modelVersion.specified=true");

        // Get all the leaseModelMetadataList where modelVersion is null
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion is greater than or equal to DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.greaterThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion is greater than or equal to UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.greaterThanOrEqual=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion is less than or equal to DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.lessThanOrEqual=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion is less than or equal to SMALLER_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.lessThanOrEqual=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion is less than DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.lessThan=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion is less than UPDATED_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.lessThan=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where modelVersion is greater than DEFAULT_MODEL_VERSION
        defaultLeaseModelMetadataShouldNotBeFound("modelVersion.greaterThan=" + DEFAULT_MODEL_VERSION);

        // Get all the leaseModelMetadataList where modelVersion is greater than SMALLER_MODEL_VERSION
        defaultLeaseModelMetadataShouldBeFound("modelVersion.greaterThan=" + SMALLER_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description equals to DEFAULT_DESCRIPTION
        defaultLeaseModelMetadataShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseModelMetadataList where description equals to UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description not equals to DEFAULT_DESCRIPTION
        defaultLeaseModelMetadataShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseModelMetadataList where description not equals to UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leaseModelMetadataList where description equals to UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description is not null
        defaultLeaseModelMetadataShouldBeFound("description.specified=true");

        // Get all the leaseModelMetadataList where description is null
        defaultLeaseModelMetadataShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description contains DEFAULT_DESCRIPTION
        defaultLeaseModelMetadataShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leaseModelMetadataList where description contains UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where description does not contain DEFAULT_DESCRIPTION
        defaultLeaseModelMetadataShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leaseModelMetadataList where description does not contain UPDATED_DESCRIPTION
        defaultLeaseModelMetadataShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate equals to DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.equals=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate equals to UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.equals=" + UPDATED_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate not equals to DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.notEquals=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate not equals to UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.notEquals=" + UPDATED_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate in DEFAULT_ANNUAL_DISCOUNTING_RATE or UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound(
            "annualDiscountingRate.in=" + DEFAULT_ANNUAL_DISCOUNTING_RATE + "," + UPDATED_ANNUAL_DISCOUNTING_RATE
        );

        // Get all the leaseModelMetadataList where annualDiscountingRate equals to UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.in=" + UPDATED_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate is not null
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.specified=true");

        // Get all the leaseModelMetadataList where annualDiscountingRate is null
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate is greater than or equal to DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.greaterThanOrEqual=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate is greater than or equal to UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.greaterThanOrEqual=" + UPDATED_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate is less than or equal to DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.lessThanOrEqual=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate is less than or equal to SMALLER_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.lessThanOrEqual=" + SMALLER_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate is less than DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.lessThan=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate is less than UPDATED_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.lessThan=" + UPDATED_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAnnualDiscountingRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where annualDiscountingRate is greater than DEFAULT_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldNotBeFound("annualDiscountingRate.greaterThan=" + DEFAULT_ANNUAL_DISCOUNTING_RATE);

        // Get all the leaseModelMetadataList where annualDiscountingRate is greater than SMALLER_ANNUAL_DISCOUNTING_RATE
        defaultLeaseModelMetadataShouldBeFound("annualDiscountingRate.greaterThan=" + SMALLER_ANNUAL_DISCOUNTING_RATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate is not null
        defaultLeaseModelMetadataShouldBeFound("commencementDate.specified=true");

        // Get all the leaseModelMetadataList where commencementDate is null
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseModelMetadataList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultLeaseModelMetadataShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate equals to DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.equals=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate equals to UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.equals=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate not equals to DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.notEquals=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate not equals to UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.notEquals=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate in DEFAULT_TERMINAL_DATE or UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.in=" + DEFAULT_TERMINAL_DATE + "," + UPDATED_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate equals to UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.in=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate is not null
        defaultLeaseModelMetadataShouldBeFound("terminalDate.specified=true");

        // Get all the leaseModelMetadataList where terminalDate is null
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate is greater than or equal to DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.greaterThanOrEqual=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate is greater than or equal to UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.greaterThanOrEqual=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate is less than or equal to DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.lessThanOrEqual=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate is less than or equal to SMALLER_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.lessThanOrEqual=" + SMALLER_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate is less than DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.lessThan=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate is less than UPDATED_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.lessThan=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTerminalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where terminalDate is greater than DEFAULT_TERMINAL_DATE
        defaultLeaseModelMetadataShouldNotBeFound("terminalDate.greaterThan=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseModelMetadataList where terminalDate is greater than SMALLER_TERMINAL_DATE
        defaultLeaseModelMetadataShouldBeFound("terminalDate.greaterThan=" + SMALLER_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods equals to DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.equals=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods equals to UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.equals=" + UPDATED_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods not equals to DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.notEquals=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods not equals to UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.notEquals=" + UPDATED_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods in DEFAULT_TOTAL_REPORTING_PERIODS or UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound(
            "totalReportingPeriods.in=" + DEFAULT_TOTAL_REPORTING_PERIODS + "," + UPDATED_TOTAL_REPORTING_PERIODS
        );

        // Get all the leaseModelMetadataList where totalReportingPeriods equals to UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.in=" + UPDATED_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods is not null
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.specified=true");

        // Get all the leaseModelMetadataList where totalReportingPeriods is null
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods is greater than or equal to DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.greaterThanOrEqual=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods is greater than or equal to UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.greaterThanOrEqual=" + UPDATED_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods is less than or equal to DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.lessThanOrEqual=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods is less than or equal to SMALLER_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.lessThanOrEqual=" + SMALLER_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods is less than DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.lessThan=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods is less than UPDATED_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.lessThan=" + UPDATED_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalReportingPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalReportingPeriods is greater than DEFAULT_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalReportingPeriods.greaterThan=" + DEFAULT_TOTAL_REPORTING_PERIODS);

        // Get all the leaseModelMetadataList where totalReportingPeriods is greater than SMALLER_TOTAL_REPORTING_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalReportingPeriods.greaterThan=" + SMALLER_TOTAL_REPORTING_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear equals to DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.equals=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear equals to UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.equals=" + UPDATED_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear not equals to DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.notEquals=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear not equals to UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.notEquals=" + UPDATED_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear in DEFAULT_REPORTING_PERIODS_PER_YEAR or UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound(
            "reportingPeriodsPerYear.in=" + DEFAULT_REPORTING_PERIODS_PER_YEAR + "," + UPDATED_REPORTING_PERIODS_PER_YEAR
        );

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear equals to UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.in=" + UPDATED_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is not null
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.specified=true");

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is null
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is greater than or equal to DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.greaterThanOrEqual=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is greater than or equal to UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.greaterThanOrEqual=" + UPDATED_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is less than or equal to DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.lessThanOrEqual=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is less than or equal to SMALLER_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.lessThanOrEqual=" + SMALLER_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is less than DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.lessThan=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is less than UPDATED_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.lessThan=" + UPDATED_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByReportingPeriodsPerYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is greater than DEFAULT_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("reportingPeriodsPerYear.greaterThan=" + DEFAULT_REPORTING_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where reportingPeriodsPerYear is greater than SMALLER_REPORTING_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("reportingPeriodsPerYear.greaterThan=" + SMALLER_REPORTING_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear equals to DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.equals=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear equals to UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.equals=" + UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear not equals to DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.notEquals=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear not equals to UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.notEquals=" + UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear in DEFAULT_SETTLEMENT_PERIODS_PER_YEAR or UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound(
            "settlementPeriodsPerYear.in=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR + "," + UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        );

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear equals to UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.in=" + UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is not null
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.specified=true");

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is null
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is greater than or equal to DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.greaterThanOrEqual=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is greater than or equal to UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.greaterThanOrEqual=" + UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is less than or equal to DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.lessThanOrEqual=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is less than or equal to SMALLER_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.lessThanOrEqual=" + SMALLER_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is less than DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.lessThan=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is less than UPDATED_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.lessThan=" + UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySettlementPeriodsPerYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is greater than DEFAULT_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldNotBeFound("settlementPeriodsPerYear.greaterThan=" + DEFAULT_SETTLEMENT_PERIODS_PER_YEAR);

        // Get all the leaseModelMetadataList where settlementPeriodsPerYear is greater than SMALLER_SETTLEMENT_PERIODS_PER_YEAR
        defaultLeaseModelMetadataShouldBeFound("settlementPeriodsPerYear.greaterThan=" + SMALLER_SETTLEMENT_PERIODS_PER_YEAR);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount equals to DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.equals=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount equals to UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.equals=" + UPDATED_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount not equals to DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.notEquals=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount not equals to UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.notEquals=" + UPDATED_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount in DEFAULT_INITIAL_LIABILITY_AMOUNT or UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound(
            "initialLiabilityAmount.in=" + DEFAULT_INITIAL_LIABILITY_AMOUNT + "," + UPDATED_INITIAL_LIABILITY_AMOUNT
        );

        // Get all the leaseModelMetadataList where initialLiabilityAmount equals to UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.in=" + UPDATED_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is not null
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.specified=true");

        // Get all the leaseModelMetadataList where initialLiabilityAmount is null
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is greater than or equal to DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.greaterThanOrEqual=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is greater than or equal to UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.greaterThanOrEqual=" + UPDATED_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is less than or equal to DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.lessThanOrEqual=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is less than or equal to SMALLER_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.lessThanOrEqual=" + SMALLER_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is less than DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.lessThan=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is less than UPDATED_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.lessThan=" + UPDATED_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialLiabilityAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is greater than DEFAULT_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialLiabilityAmount.greaterThan=" + DEFAULT_INITIAL_LIABILITY_AMOUNT);

        // Get all the leaseModelMetadataList where initialLiabilityAmount is greater than SMALLER_INITIAL_LIABILITY_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialLiabilityAmount.greaterThan=" + SMALLER_INITIAL_LIABILITY_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount equals to DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.equals=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount equals to UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.equals=" + UPDATED_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount not equals to DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.notEquals=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount not equals to UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.notEquals=" + UPDATED_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount in DEFAULT_INITIAL_ROU_AMOUNT or UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.in=" + DEFAULT_INITIAL_ROU_AMOUNT + "," + UPDATED_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount equals to UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.in=" + UPDATED_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount is not null
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.specified=true");

        // Get all the leaseModelMetadataList where initialROUAmount is null
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount is greater than or equal to DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.greaterThanOrEqual=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount is greater than or equal to UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.greaterThanOrEqual=" + UPDATED_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount is less than or equal to DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.lessThanOrEqual=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount is less than or equal to SMALLER_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.lessThanOrEqual=" + SMALLER_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount is less than DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.lessThan=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount is less than UPDATED_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.lessThan=" + UPDATED_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInitialROUAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where initialROUAmount is greater than DEFAULT_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldNotBeFound("initialROUAmount.greaterThan=" + DEFAULT_INITIAL_ROU_AMOUNT);

        // Get all the leaseModelMetadataList where initialROUAmount is greater than SMALLER_INITIAL_ROU_AMOUNT
        defaultLeaseModelMetadataShouldBeFound("initialROUAmount.greaterThan=" + SMALLER_INITIAL_ROU_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods equals to DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.equals=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods equals to UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.equals=" + UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods not equals to DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.notEquals=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods not equals to UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.notEquals=" + UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods in DEFAULT_TOTAL_DEPRECIATION_PERIODS or UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound(
            "totalDepreciationPeriods.in=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS + "," + UPDATED_TOTAL_DEPRECIATION_PERIODS
        );

        // Get all the leaseModelMetadataList where totalDepreciationPeriods equals to UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.in=" + UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is not null
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.specified=true");

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is null
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is greater than or equal to DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.greaterThanOrEqual=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is greater than or equal to UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.greaterThanOrEqual=" + UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is less than or equal to DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.lessThanOrEqual=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is less than or equal to SMALLER_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.lessThanOrEqual=" + SMALLER_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is less than DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.lessThan=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is less than UPDATED_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.lessThan=" + UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByTotalDepreciationPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is greater than DEFAULT_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldNotBeFound("totalDepreciationPeriods.greaterThan=" + DEFAULT_TOTAL_DEPRECIATION_PERIODS);

        // Get all the leaseModelMetadataList where totalDepreciationPeriods is greater than SMALLER_TOTAL_DEPRECIATION_PERIODS
        defaultLeaseModelMetadataShouldBeFound("totalDepreciationPeriods.greaterThan=" + SMALLER_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
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
        leaseModelMetadata.addPlaceholder(placeholder);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long placeholderId = placeholder.getId();

        // Get all the leaseModelMetadataList where placeholder equals to placeholderId
        defaultLeaseModelMetadataShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the leaseModelMetadataList where placeholder equals to (placeholderId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByLeaseMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        UniversallyUniqueMapping leaseMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            leaseMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(leaseMapping);
            em.flush();
        } else {
            leaseMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(leaseMapping);
        em.flush();
        leaseModelMetadata.addLeaseMapping(leaseMapping);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long leaseMappingId = leaseMapping.getId();

        // Get all the leaseModelMetadataList where leaseMapping equals to leaseMappingId
        defaultLeaseModelMetadataShouldBeFound("leaseMappingId.equals=" + leaseMappingId);

        // Get all the leaseModelMetadataList where leaseMapping equals to (leaseMappingId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("leaseMappingId.equals=" + (leaseMappingId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByLeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        LeaseContract leaseContract;
        if (TestUtil.findAll(em, LeaseContract.class).isEmpty()) {
            leaseContract = LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, LeaseContract.class).get(0);
        }
        em.persist(leaseContract);
        em.flush();
        leaseModelMetadata.setLeaseContract(leaseContract);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long leaseContractId = leaseContract.getId();

        // Get all the leaseModelMetadataList where leaseContract equals to leaseContractId
        defaultLeaseModelMetadataShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the leaseModelMetadataList where leaseContract equals to (leaseContractId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByPredecessorIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        LeaseModelMetadata predecessor;
        if (TestUtil.findAll(em, LeaseModelMetadata.class).isEmpty()) {
            predecessor = LeaseModelMetadataResourceIT.createEntity(em);
            em.persist(predecessor);
            em.flush();
        } else {
            predecessor = TestUtil.findAll(em, LeaseModelMetadata.class).get(0);
        }
        em.persist(predecessor);
        em.flush();
        leaseModelMetadata.setPredecessor(predecessor);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long predecessorId = predecessor.getId();

        // Get all the leaseModelMetadataList where predecessor equals to predecessorId
        defaultLeaseModelMetadataShouldBeFound("predecessorId.equals=" + predecessorId);

        // Get all the leaseModelMetadataList where predecessor equals to (predecessorId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("predecessorId.equals=" + (predecessorId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByLiabilityCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        SettlementCurrency liabilityCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            liabilityCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(liabilityCurrency);
            em.flush();
        } else {
            liabilityCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(liabilityCurrency);
        em.flush();
        leaseModelMetadata.setLiabilityCurrency(liabilityCurrency);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long liabilityCurrencyId = liabilityCurrency.getId();

        // Get all the leaseModelMetadataList where liabilityCurrency equals to liabilityCurrencyId
        defaultLeaseModelMetadataShouldBeFound("liabilityCurrencyId.equals=" + liabilityCurrencyId);

        // Get all the leaseModelMetadataList where liabilityCurrency equals to (liabilityCurrencyId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("liabilityCurrencyId.equals=" + (liabilityCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByRouAssetCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        SettlementCurrency rouAssetCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            rouAssetCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(rouAssetCurrency);
            em.flush();
        } else {
            rouAssetCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(rouAssetCurrency);
        em.flush();
        leaseModelMetadata.setRouAssetCurrency(rouAssetCurrency);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long rouAssetCurrencyId = rouAssetCurrency.getId();

        // Get all the leaseModelMetadataList where rouAssetCurrency equals to rouAssetCurrencyId
        defaultLeaseModelMetadataShouldBeFound("rouAssetCurrencyId.equals=" + rouAssetCurrencyId);

        // Get all the leaseModelMetadataList where rouAssetCurrency equals to (rouAssetCurrencyId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("rouAssetCurrencyId.equals=" + (rouAssetCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByModelAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        BusinessDocument modelAttachments;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            modelAttachments = BusinessDocumentResourceIT.createEntity(em);
            em.persist(modelAttachments);
            em.flush();
        } else {
            modelAttachments = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(modelAttachments);
        em.flush();
        leaseModelMetadata.setModelAttachments(modelAttachments);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long modelAttachmentsId = modelAttachments.getId();

        // Get all the leaseModelMetadataList where modelAttachments equals to modelAttachmentsId
        defaultLeaseModelMetadataShouldBeFound("modelAttachmentsId.equals=" + modelAttachmentsId);

        // Get all the leaseModelMetadataList where modelAttachments equals to (modelAttachmentsId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("modelAttachmentsId.equals=" + (modelAttachmentsId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        leaseModelMetadata.setSecurityClearance(securityClearance);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long securityClearanceId = securityClearance.getId();

        // Get all the leaseModelMetadataList where securityClearance equals to securityClearanceId
        defaultLeaseModelMetadataShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the leaseModelMetadataList where securityClearance equals to (securityClearanceId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByLeaseLiabilityAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        TransactionAccount leaseLiabilityAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            leaseLiabilityAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(leaseLiabilityAccount);
            em.flush();
        } else {
            leaseLiabilityAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(leaseLiabilityAccount);
        em.flush();
        leaseModelMetadata.setLeaseLiabilityAccount(leaseLiabilityAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long leaseLiabilityAccountId = leaseLiabilityAccount.getId();

        // Get all the leaseModelMetadataList where leaseLiabilityAccount equals to leaseLiabilityAccountId
        defaultLeaseModelMetadataShouldBeFound("leaseLiabilityAccountId.equals=" + leaseLiabilityAccountId);

        // Get all the leaseModelMetadataList where leaseLiabilityAccount equals to (leaseLiabilityAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("leaseLiabilityAccountId.equals=" + (leaseLiabilityAccountId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInterestPayableAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        TransactionAccount interestPayableAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            interestPayableAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(interestPayableAccount);
            em.flush();
        } else {
            interestPayableAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(interestPayableAccount);
        em.flush();
        leaseModelMetadata.setInterestPayableAccount(interestPayableAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long interestPayableAccountId = interestPayableAccount.getId();

        // Get all the leaseModelMetadataList where interestPayableAccount equals to interestPayableAccountId
        defaultLeaseModelMetadataShouldBeFound("interestPayableAccountId.equals=" + interestPayableAccountId);

        // Get all the leaseModelMetadataList where interestPayableAccount equals to (interestPayableAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("interestPayableAccountId.equals=" + (interestPayableAccountId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByInterestExpenseAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        TransactionAccount interestExpenseAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            interestExpenseAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(interestExpenseAccount);
            em.flush();
        } else {
            interestExpenseAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(interestExpenseAccount);
        em.flush();
        leaseModelMetadata.setInterestExpenseAccount(interestExpenseAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long interestExpenseAccountId = interestExpenseAccount.getId();

        // Get all the leaseModelMetadataList where interestExpenseAccount equals to interestExpenseAccountId
        defaultLeaseModelMetadataShouldBeFound("interestExpenseAccountId.equals=" + interestExpenseAccountId);

        // Get all the leaseModelMetadataList where interestExpenseAccount equals to (interestExpenseAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("interestExpenseAccountId.equals=" + (interestExpenseAccountId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByRouAssetAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        TransactionAccount rouAssetAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            rouAssetAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(rouAssetAccount);
            em.flush();
        } else {
            rouAssetAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(rouAssetAccount);
        em.flush();
        leaseModelMetadata.setRouAssetAccount(rouAssetAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long rouAssetAccountId = rouAssetAccount.getId();

        // Get all the leaseModelMetadataList where rouAssetAccount equals to rouAssetAccountId
        defaultLeaseModelMetadataShouldBeFound("rouAssetAccountId.equals=" + rouAssetAccountId);

        // Get all the leaseModelMetadataList where rouAssetAccount equals to (rouAssetAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("rouAssetAccountId.equals=" + (rouAssetAccountId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByRouDepreciationAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        TransactionAccount rouDepreciationAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            rouDepreciationAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(rouDepreciationAccount);
            em.flush();
        } else {
            rouDepreciationAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(rouDepreciationAccount);
        em.flush();
        leaseModelMetadata.setRouDepreciationAccount(rouDepreciationAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long rouDepreciationAccountId = rouDepreciationAccount.getId();

        // Get all the leaseModelMetadataList where rouDepreciationAccount equals to rouDepreciationAccountId
        defaultLeaseModelMetadataShouldBeFound("rouDepreciationAccountId.equals=" + rouDepreciationAccountId);

        // Get all the leaseModelMetadataList where rouDepreciationAccount equals to (rouDepreciationAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("rouDepreciationAccountId.equals=" + (rouDepreciationAccountId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseModelMetadataByAccruedDepreciationAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
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
        leaseModelMetadata.setAccruedDepreciationAccount(accruedDepreciationAccount);
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        Long accruedDepreciationAccountId = accruedDepreciationAccount.getId();

        // Get all the leaseModelMetadataList where accruedDepreciationAccount equals to accruedDepreciationAccountId
        defaultLeaseModelMetadataShouldBeFound("accruedDepreciationAccountId.equals=" + accruedDepreciationAccountId);

        // Get all the leaseModelMetadataList where accruedDepreciationAccount equals to (accruedDepreciationAccountId + 1)
        defaultLeaseModelMetadataShouldNotBeFound("accruedDepreciationAccountId.equals=" + (accruedDepreciationAccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseModelMetadataShouldBeFound(String filter) throws Exception {
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNotesContentType").value(hasItem(DEFAULT_MODEL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].modelNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_MODEL_NOTES))))
            .andExpect(jsonPath("$.[*].annualDiscountingRate").value(hasItem(DEFAULT_ANNUAL_DISCOUNTING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalReportingPeriods").value(hasItem(DEFAULT_TOTAL_REPORTING_PERIODS.doubleValue())))
            .andExpect(jsonPath("$.[*].reportingPeriodsPerYear").value(hasItem(DEFAULT_REPORTING_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].settlementPeriodsPerYear").value(hasItem(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].initialLiabilityAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].initialROUAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_ROU_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalDepreciationPeriods").value(hasItem(DEFAULT_TOTAL_DEPRECIATION_PERIODS.doubleValue())));

        // Check, that the count call also returns 1
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseModelMetadataShouldNotBeFound(String filter) throws Exception {
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseModelMetadata() throws Exception {
        // Get the leaseModelMetadata
        restLeaseModelMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseModelMetadata() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();

        // Update the leaseModelMetadata
        LeaseModelMetadata updatedLeaseModelMetadata = leaseModelMetadataRepository.findById(leaseModelMetadata.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseModelMetadata are not directly saved in db
        em.detach(updatedLeaseModelMetadata);
        updatedLeaseModelMetadata
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .modelNotes(UPDATED_MODEL_NOTES)
            .modelNotesContentType(UPDATED_MODEL_NOTES_CONTENT_TYPE)
            .annualDiscountingRate(UPDATED_ANNUAL_DISCOUNTING_RATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE)
            .totalReportingPeriods(UPDATED_TOTAL_REPORTING_PERIODS)
            .reportingPeriodsPerYear(UPDATED_REPORTING_PERIODS_PER_YEAR)
            .settlementPeriodsPerYear(UPDATED_SETTLEMENT_PERIODS_PER_YEAR)
            .initialLiabilityAmount(UPDATED_INITIAL_LIABILITY_AMOUNT)
            .initialROUAmount(UPDATED_INITIAL_ROU_AMOUNT)
            .totalDepreciationPeriods(UPDATED_TOTAL_DEPRECIATION_PERIODS);
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(updatedLeaseModelMetadata);

        restLeaseModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseModelMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        LeaseModelMetadata testLeaseModelMetadata = leaseModelMetadataList.get(leaseModelMetadataList.size() - 1);
        assertThat(testLeaseModelMetadata.getModelTitle()).isEqualTo(UPDATED_MODEL_TITLE);
        assertThat(testLeaseModelMetadata.getModelVersion()).isEqualTo(UPDATED_MODEL_VERSION);
        assertThat(testLeaseModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaseModelMetadata.getModelNotes()).isEqualTo(UPDATED_MODEL_NOTES);
        assertThat(testLeaseModelMetadata.getModelNotesContentType()).isEqualTo(UPDATED_MODEL_NOTES_CONTENT_TYPE);
        assertThat(testLeaseModelMetadata.getAnnualDiscountingRate()).isEqualTo(UPDATED_ANNUAL_DISCOUNTING_RATE);
        assertThat(testLeaseModelMetadata.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseModelMetadata.getTerminalDate()).isEqualTo(UPDATED_TERMINAL_DATE);
        assertThat(testLeaseModelMetadata.getTotalReportingPeriods()).isEqualTo(UPDATED_TOTAL_REPORTING_PERIODS);
        assertThat(testLeaseModelMetadata.getReportingPeriodsPerYear()).isEqualTo(UPDATED_REPORTING_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getSettlementPeriodsPerYear()).isEqualTo(UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getInitialLiabilityAmount()).isEqualTo(UPDATED_INITIAL_LIABILITY_AMOUNT);
        assertThat(testLeaseModelMetadata.getInitialROUAmount()).isEqualTo(UPDATED_INITIAL_ROU_AMOUNT);
        assertThat(testLeaseModelMetadata.getTotalDepreciationPeriods()).isEqualTo(UPDATED_TOTAL_DEPRECIATION_PERIODS);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository).save(testLeaseModelMetadata);
    }

    @Test
    @Transactional
    void putNonExistingLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseModelMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void partialUpdateLeaseModelMetadataWithPatch() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();

        // Update the leaseModelMetadata using partial update
        LeaseModelMetadata partialUpdatedLeaseModelMetadata = new LeaseModelMetadata();
        partialUpdatedLeaseModelMetadata.setId(leaseModelMetadata.getId());

        partialUpdatedLeaseModelMetadata
            .description(UPDATED_DESCRIPTION)
            .totalReportingPeriods(UPDATED_TOTAL_REPORTING_PERIODS)
            .reportingPeriodsPerYear(UPDATED_REPORTING_PERIODS_PER_YEAR)
            .settlementPeriodsPerYear(UPDATED_SETTLEMENT_PERIODS_PER_YEAR)
            .totalDepreciationPeriods(UPDATED_TOTAL_DEPRECIATION_PERIODS);

        restLeaseModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseModelMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseModelMetadata))
            )
            .andExpect(status().isOk());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        LeaseModelMetadata testLeaseModelMetadata = leaseModelMetadataList.get(leaseModelMetadataList.size() - 1);
        assertThat(testLeaseModelMetadata.getModelTitle()).isEqualTo(DEFAULT_MODEL_TITLE);
        assertThat(testLeaseModelMetadata.getModelVersion()).isEqualTo(DEFAULT_MODEL_VERSION);
        assertThat(testLeaseModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaseModelMetadata.getModelNotes()).isEqualTo(DEFAULT_MODEL_NOTES);
        assertThat(testLeaseModelMetadata.getModelNotesContentType()).isEqualTo(DEFAULT_MODEL_NOTES_CONTENT_TYPE);
        assertThat(testLeaseModelMetadata.getAnnualDiscountingRate()).isEqualTo(DEFAULT_ANNUAL_DISCOUNTING_RATE);
        assertThat(testLeaseModelMetadata.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testLeaseModelMetadata.getTerminalDate()).isEqualTo(DEFAULT_TERMINAL_DATE);
        assertThat(testLeaseModelMetadata.getTotalReportingPeriods()).isEqualTo(UPDATED_TOTAL_REPORTING_PERIODS);
        assertThat(testLeaseModelMetadata.getReportingPeriodsPerYear()).isEqualTo(UPDATED_REPORTING_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getSettlementPeriodsPerYear()).isEqualTo(UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getInitialLiabilityAmount()).isEqualByComparingTo(DEFAULT_INITIAL_LIABILITY_AMOUNT);
        assertThat(testLeaseModelMetadata.getInitialROUAmount()).isEqualByComparingTo(DEFAULT_INITIAL_ROU_AMOUNT);
        assertThat(testLeaseModelMetadata.getTotalDepreciationPeriods()).isEqualTo(UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void fullUpdateLeaseModelMetadataWithPatch() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();

        // Update the leaseModelMetadata using partial update
        LeaseModelMetadata partialUpdatedLeaseModelMetadata = new LeaseModelMetadata();
        partialUpdatedLeaseModelMetadata.setId(leaseModelMetadata.getId());

        partialUpdatedLeaseModelMetadata
            .modelTitle(UPDATED_MODEL_TITLE)
            .modelVersion(UPDATED_MODEL_VERSION)
            .description(UPDATED_DESCRIPTION)
            .modelNotes(UPDATED_MODEL_NOTES)
            .modelNotesContentType(UPDATED_MODEL_NOTES_CONTENT_TYPE)
            .annualDiscountingRate(UPDATED_ANNUAL_DISCOUNTING_RATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE)
            .totalReportingPeriods(UPDATED_TOTAL_REPORTING_PERIODS)
            .reportingPeriodsPerYear(UPDATED_REPORTING_PERIODS_PER_YEAR)
            .settlementPeriodsPerYear(UPDATED_SETTLEMENT_PERIODS_PER_YEAR)
            .initialLiabilityAmount(UPDATED_INITIAL_LIABILITY_AMOUNT)
            .initialROUAmount(UPDATED_INITIAL_ROU_AMOUNT)
            .totalDepreciationPeriods(UPDATED_TOTAL_DEPRECIATION_PERIODS);

        restLeaseModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseModelMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseModelMetadata))
            )
            .andExpect(status().isOk());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);
        LeaseModelMetadata testLeaseModelMetadata = leaseModelMetadataList.get(leaseModelMetadataList.size() - 1);
        assertThat(testLeaseModelMetadata.getModelTitle()).isEqualTo(UPDATED_MODEL_TITLE);
        assertThat(testLeaseModelMetadata.getModelVersion()).isEqualTo(UPDATED_MODEL_VERSION);
        assertThat(testLeaseModelMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaseModelMetadata.getModelNotes()).isEqualTo(UPDATED_MODEL_NOTES);
        assertThat(testLeaseModelMetadata.getModelNotesContentType()).isEqualTo(UPDATED_MODEL_NOTES_CONTENT_TYPE);
        assertThat(testLeaseModelMetadata.getAnnualDiscountingRate()).isEqualTo(UPDATED_ANNUAL_DISCOUNTING_RATE);
        assertThat(testLeaseModelMetadata.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseModelMetadata.getTerminalDate()).isEqualTo(UPDATED_TERMINAL_DATE);
        assertThat(testLeaseModelMetadata.getTotalReportingPeriods()).isEqualTo(UPDATED_TOTAL_REPORTING_PERIODS);
        assertThat(testLeaseModelMetadata.getReportingPeriodsPerYear()).isEqualTo(UPDATED_REPORTING_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getSettlementPeriodsPerYear()).isEqualTo(UPDATED_SETTLEMENT_PERIODS_PER_YEAR);
        assertThat(testLeaseModelMetadata.getInitialLiabilityAmount()).isEqualByComparingTo(UPDATED_INITIAL_LIABILITY_AMOUNT);
        assertThat(testLeaseModelMetadata.getInitialROUAmount()).isEqualByComparingTo(UPDATED_INITIAL_ROU_AMOUNT);
        assertThat(testLeaseModelMetadata.getTotalDepreciationPeriods()).isEqualTo(UPDATED_TOTAL_DEPRECIATION_PERIODS);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseModelMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseModelMetadata() throws Exception {
        int databaseSizeBeforeUpdate = leaseModelMetadataRepository.findAll().size();
        leaseModelMetadata.setId(count.incrementAndGet());

        // Create the LeaseModelMetadata
        LeaseModelMetadataDTO leaseModelMetadataDTO = leaseModelMetadataMapper.toDto(leaseModelMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseModelMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseModelMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseModelMetadata in the database
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(0)).save(leaseModelMetadata);
    }

    @Test
    @Transactional
    void deleteLeaseModelMetadata() throws Exception {
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);

        int databaseSizeBeforeDelete = leaseModelMetadataRepository.findAll().size();

        // Delete the leaseModelMetadata
        restLeaseModelMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseModelMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseModelMetadata> leaseModelMetadataList = leaseModelMetadataRepository.findAll();
        assertThat(leaseModelMetadataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseModelMetadata in Elasticsearch
        verify(mockLeaseModelMetadataSearchRepository, times(1)).deleteById(leaseModelMetadata.getId());
    }

    @Test
    @Transactional
    void searchLeaseModelMetadata() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseModelMetadataRepository.saveAndFlush(leaseModelMetadata);
        when(mockLeaseModelMetadataSearchRepository.search("id:" + leaseModelMetadata.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseModelMetadata), PageRequest.of(0, 1), 1));

        // Search the leaseModelMetadata
        restLeaseModelMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseModelMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseModelMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelTitle").value(hasItem(DEFAULT_MODEL_TITLE)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelNotesContentType").value(hasItem(DEFAULT_MODEL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].modelNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_MODEL_NOTES))))
            .andExpect(jsonPath("$.[*].annualDiscountingRate").value(hasItem(DEFAULT_ANNUAL_DISCOUNTING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalReportingPeriods").value(hasItem(DEFAULT_TOTAL_REPORTING_PERIODS.doubleValue())))
            .andExpect(jsonPath("$.[*].reportingPeriodsPerYear").value(hasItem(DEFAULT_REPORTING_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].settlementPeriodsPerYear").value(hasItem(DEFAULT_SETTLEMENT_PERIODS_PER_YEAR.doubleValue())))
            .andExpect(jsonPath("$.[*].initialLiabilityAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_LIABILITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].initialROUAmount").value(hasItem(sameNumber(DEFAULT_INITIAL_ROU_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalDepreciationPeriods").value(hasItem(DEFAULT_TOTAL_DEPRECIATION_PERIODS.doubleValue())));
    }
}
