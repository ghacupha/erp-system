package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.IsoCountryCode;
import io.github.erp.domain.PerformanceOfForeignSubsidiaries;
import io.github.erp.repository.PerformanceOfForeignSubsidiariesRepository;
import io.github.erp.repository.search.PerformanceOfForeignSubsidiariesSearchRepository;
import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
import io.github.erp.service.mapper.PerformanceOfForeignSubsidiariesMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import io.github.erp.web.rest.TestUtil;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PerformanceOfForeignSubsidiariesResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class PerformanceOfForeignSubsidiariesResourceIT {

    private static final String DEFAULT_SUBSIDIARY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBSIDIARY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SUBSIDIARY_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUBSIDIARY_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_GROSS_LOANS_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_LOANS_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_LOANS_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_NPA_LOAN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_NPA_LOAN_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_NPA_LOAN_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_ASSETS_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_ASSETS_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_ASSETS_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_DEPOSITS_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_DEPOSITS_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_DEPOSITS_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PROFIT_BEFORE_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROFIT_BEFORE_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_PROFIT_BEFORE_TAX = new BigDecimal(1 - 1);

    private static final Double DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO = 1D;
    private static final Double UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO = 2D;
    private static final Double SMALLER_TOTAL_CAPITAL_ADEQUACY_RATIO = 1D - 1D;

    private static final Double DEFAULT_LIQUIDITY_RATIO = 1D;
    private static final Double UPDATED_LIQUIDITY_RATIO = 2D;
    private static final Double SMALLER_LIQUIDITY_RATIO = 1D - 1D;

    private static final BigDecimal DEFAULT_GENERAL_PROVISIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_GENERAL_PROVISIONS = new BigDecimal(2);
    private static final BigDecimal SMALLER_GENERAL_PROVISIONS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SPECIFIC_PROVISIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_SPECIFIC_PROVISIONS = new BigDecimal(2);
    private static final BigDecimal SMALLER_SPECIFIC_PROVISIONS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_IN_SUSPENSE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_IN_SUSPENSE_AMOUNT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_TOTAL_NUMBER_OF_STAFF = 1;
    private static final Integer UPDATED_TOTAL_NUMBER_OF_STAFF = 2;
    private static final Integer SMALLER_TOTAL_NUMBER_OF_STAFF = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_BRANCHES = 1;
    private static final Integer UPDATED_NUMBER_OF_BRANCHES = 2;
    private static final Integer SMALLER_NUMBER_OF_BRANCHES = 1 - 1;

    private static final String ENTITY_API_URL = "/api/granular-data/performance-of-foreign-subsidiaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/performance-of-foreign-subsidiaries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository;

    @Autowired
    private PerformanceOfForeignSubsidiariesMapper performanceOfForeignSubsidiariesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PerformanceOfForeignSubsidiariesSearchRepositoryMockConfiguration
     */
    @Autowired
    private PerformanceOfForeignSubsidiariesSearchRepository mockPerformanceOfForeignSubsidiariesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerformanceOfForeignSubsidiariesMockMvc;

    private PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerformanceOfForeignSubsidiaries createEntity(EntityManager em) {
        PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries = new PerformanceOfForeignSubsidiaries()
            .subsidiaryName(DEFAULT_SUBSIDIARY_NAME)
            .reportingDate(DEFAULT_REPORTING_DATE)
            .subsidiaryId(DEFAULT_SUBSIDIARY_ID)
            .grossLoansAmount(DEFAULT_GROSS_LOANS_AMOUNT)
            .grossNPALoanAmount(DEFAULT_GROSS_NPA_LOAN_AMOUNT)
            .grossAssetsAmount(DEFAULT_GROSS_ASSETS_AMOUNT)
            .grossDepositsAmount(DEFAULT_GROSS_DEPOSITS_AMOUNT)
            .profitBeforeTax(DEFAULT_PROFIT_BEFORE_TAX)
            .totalCapitalAdequacyRatio(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO)
            .liquidityRatio(DEFAULT_LIQUIDITY_RATIO)
            .generalProvisions(DEFAULT_GENERAL_PROVISIONS)
            .specificProvisions(DEFAULT_SPECIFIC_PROVISIONS)
            .interestInSuspenseAmount(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT)
            .totalNumberOfStaff(DEFAULT_TOTAL_NUMBER_OF_STAFF)
            .numberOfBranches(DEFAULT_NUMBER_OF_BRANCHES);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        performanceOfForeignSubsidiaries.setBankCode(institutionCode);
        // Add required entity
        IsoCountryCode isoCountryCode;
        if (TestUtil.findAll(em, IsoCountryCode.class).isEmpty()) {
            isoCountryCode = IsoCountryCodeResourceIT.createEntity(em);
            em.persist(isoCountryCode);
            em.flush();
        } else {
            isoCountryCode = TestUtil.findAll(em, IsoCountryCode.class).get(0);
        }
        performanceOfForeignSubsidiaries.setSubsidiaryCountryCode(isoCountryCode);
        return performanceOfForeignSubsidiaries;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerformanceOfForeignSubsidiaries createUpdatedEntity(EntityManager em) {
        PerformanceOfForeignSubsidiaries performanceOfForeignSubsidiaries = new PerformanceOfForeignSubsidiaries()
            .subsidiaryName(UPDATED_SUBSIDIARY_NAME)
            .reportingDate(UPDATED_REPORTING_DATE)
            .subsidiaryId(UPDATED_SUBSIDIARY_ID)
            .grossLoansAmount(UPDATED_GROSS_LOANS_AMOUNT)
            .grossNPALoanAmount(UPDATED_GROSS_NPA_LOAN_AMOUNT)
            .grossAssetsAmount(UPDATED_GROSS_ASSETS_AMOUNT)
            .grossDepositsAmount(UPDATED_GROSS_DEPOSITS_AMOUNT)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .totalCapitalAdequacyRatio(UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO)
            .liquidityRatio(UPDATED_LIQUIDITY_RATIO)
            .generalProvisions(UPDATED_GENERAL_PROVISIONS)
            .specificProvisions(UPDATED_SPECIFIC_PROVISIONS)
            .interestInSuspenseAmount(UPDATED_INTEREST_IN_SUSPENSE_AMOUNT)
            .totalNumberOfStaff(UPDATED_TOTAL_NUMBER_OF_STAFF)
            .numberOfBranches(UPDATED_NUMBER_OF_BRANCHES);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        performanceOfForeignSubsidiaries.setBankCode(institutionCode);
        // Add required entity
        IsoCountryCode isoCountryCode;
        if (TestUtil.findAll(em, IsoCountryCode.class).isEmpty()) {
            isoCountryCode = IsoCountryCodeResourceIT.createUpdatedEntity(em);
            em.persist(isoCountryCode);
            em.flush();
        } else {
            isoCountryCode = TestUtil.findAll(em, IsoCountryCode.class).get(0);
        }
        performanceOfForeignSubsidiaries.setSubsidiaryCountryCode(isoCountryCode);
        return performanceOfForeignSubsidiaries;
    }

    @BeforeEach
    public void initTest() {
        performanceOfForeignSubsidiaries = createEntity(em);
    }

    @Test
    @Transactional
    void createPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeCreate = performanceOfForeignSubsidiariesRepository.findAll().size();
        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeCreate + 1);
        PerformanceOfForeignSubsidiaries testPerformanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesList.get(
            performanceOfForeignSubsidiariesList.size() - 1
        );
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryName()).isEqualTo(DEFAULT_SUBSIDIARY_NAME);
        assertThat(testPerformanceOfForeignSubsidiaries.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryId()).isEqualTo(DEFAULT_SUBSIDIARY_ID);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossLoansAmount()).isEqualByComparingTo(DEFAULT_GROSS_LOANS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossNPALoanAmount()).isEqualByComparingTo(DEFAULT_GROSS_NPA_LOAN_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossAssetsAmount()).isEqualByComparingTo(DEFAULT_GROSS_ASSETS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossDepositsAmount()).isEqualByComparingTo(DEFAULT_GROSS_DEPOSITS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getProfitBeforeTax()).isEqualByComparingTo(DEFAULT_PROFIT_BEFORE_TAX);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalCapitalAdequacyRatio()).isEqualTo(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getLiquidityRatio()).isEqualTo(DEFAULT_LIQUIDITY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getGeneralProvisions()).isEqualByComparingTo(DEFAULT_GENERAL_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getSpecificProvisions()).isEqualByComparingTo(DEFAULT_SPECIFIC_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getInterestInSuspenseAmount())
            .isEqualByComparingTo(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalNumberOfStaff()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_STAFF);
        assertThat(testPerformanceOfForeignSubsidiaries.getNumberOfBranches()).isEqualTo(DEFAULT_NUMBER_OF_BRANCHES);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(1)).save(testPerformanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void createPerformanceOfForeignSubsidiariesWithExistingId() throws Exception {
        // Create the PerformanceOfForeignSubsidiaries with an existing ID
        performanceOfForeignSubsidiaries.setId(1L);
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        int databaseSizeBeforeCreate = performanceOfForeignSubsidiariesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeCreate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void checkSubsidiaryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setSubsidiaryName(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setReportingDate(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubsidiaryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setSubsidiaryId(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrossLoansAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setGrossLoansAmount(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrossNPALoanAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setGrossNPALoanAmount(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrossAssetsAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setGrossAssetsAmount(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrossDepositsAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setGrossDepositsAmount(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfitBeforeTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setProfitBeforeTax(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCapitalAdequacyRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setTotalCapitalAdequacyRatio(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLiquidityRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setLiquidityRatio(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneralProvisionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setGeneralProvisions(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecificProvisionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setSpecificProvisions(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInterestInSuspenseAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setInterestInSuspenseAmount(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfStaffIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setTotalNumberOfStaff(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfBranchesIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceOfForeignSubsidiariesRepository.findAll().size();
        // set the field null
        performanceOfForeignSubsidiaries.setNumberOfBranches(null);

        // Create the PerformanceOfForeignSubsidiaries, which fails.
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiaries() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceOfForeignSubsidiaries.getId().intValue())))
            .andExpect(jsonPath("$.[*].subsidiaryName").value(hasItem(DEFAULT_SUBSIDIARY_NAME)))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].subsidiaryId").value(hasItem(DEFAULT_SUBSIDIARY_ID)))
            .andExpect(jsonPath("$.[*].grossLoansAmount").value(hasItem(sameNumber(DEFAULT_GROSS_LOANS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossNPALoanAmount").value(hasItem(sameNumber(DEFAULT_GROSS_NPA_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossAssetsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_ASSETS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossDepositsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_DEPOSITS_AMOUNT))))
            .andExpect(jsonPath("$.[*].profitBeforeTax").value(hasItem(sameNumber(DEFAULT_PROFIT_BEFORE_TAX))))
            .andExpect(jsonPath("$.[*].totalCapitalAdequacyRatio").value(hasItem(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].liquidityRatio").value(hasItem(DEFAULT_LIQUIDITY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].generalProvisions").value(hasItem(sameNumber(DEFAULT_GENERAL_PROVISIONS))))
            .andExpect(jsonPath("$.[*].specificProvisions").value(hasItem(sameNumber(DEFAULT_SPECIFIC_PROVISIONS))))
            .andExpect(jsonPath("$.[*].interestInSuspenseAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalNumberOfStaff").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_STAFF)))
            .andExpect(jsonPath("$.[*].numberOfBranches").value(hasItem(DEFAULT_NUMBER_OF_BRANCHES)));
    }

    @Test
    @Transactional
    void getPerformanceOfForeignSubsidiaries() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get the performanceOfForeignSubsidiaries
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL_ID, performanceOfForeignSubsidiaries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(performanceOfForeignSubsidiaries.getId().intValue()))
            .andExpect(jsonPath("$.subsidiaryName").value(DEFAULT_SUBSIDIARY_NAME))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.subsidiaryId").value(DEFAULT_SUBSIDIARY_ID))
            .andExpect(jsonPath("$.grossLoansAmount").value(sameNumber(DEFAULT_GROSS_LOANS_AMOUNT)))
            .andExpect(jsonPath("$.grossNPALoanAmount").value(sameNumber(DEFAULT_GROSS_NPA_LOAN_AMOUNT)))
            .andExpect(jsonPath("$.grossAssetsAmount").value(sameNumber(DEFAULT_GROSS_ASSETS_AMOUNT)))
            .andExpect(jsonPath("$.grossDepositsAmount").value(sameNumber(DEFAULT_GROSS_DEPOSITS_AMOUNT)))
            .andExpect(jsonPath("$.profitBeforeTax").value(sameNumber(DEFAULT_PROFIT_BEFORE_TAX)))
            .andExpect(jsonPath("$.totalCapitalAdequacyRatio").value(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO.doubleValue()))
            .andExpect(jsonPath("$.liquidityRatio").value(DEFAULT_LIQUIDITY_RATIO.doubleValue()))
            .andExpect(jsonPath("$.generalProvisions").value(sameNumber(DEFAULT_GENERAL_PROVISIONS)))
            .andExpect(jsonPath("$.specificProvisions").value(sameNumber(DEFAULT_SPECIFIC_PROVISIONS)))
            .andExpect(jsonPath("$.interestInSuspenseAmount").value(sameNumber(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT)))
            .andExpect(jsonPath("$.totalNumberOfStaff").value(DEFAULT_TOTAL_NUMBER_OF_STAFF))
            .andExpect(jsonPath("$.numberOfBranches").value(DEFAULT_NUMBER_OF_BRANCHES));
    }

    @Test
    @Transactional
    void getPerformanceOfForeignSubsidiariesByIdFiltering() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        Long id = performanceOfForeignSubsidiaries.getId();

        defaultPerformanceOfForeignSubsidiariesShouldBeFound("id.equals=" + id);
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("id.notEquals=" + id);

        defaultPerformanceOfForeignSubsidiariesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("id.greaterThan=" + id);

        defaultPerformanceOfForeignSubsidiariesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName equals to DEFAULT_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryName.equals=" + DEFAULT_SUBSIDIARY_NAME);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName equals to UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.equals=" + UPDATED_SUBSIDIARY_NAME);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName not equals to DEFAULT_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.notEquals=" + DEFAULT_SUBSIDIARY_NAME);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName not equals to UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryName.notEquals=" + UPDATED_SUBSIDIARY_NAME);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName in DEFAULT_SUBSIDIARY_NAME or UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "subsidiaryName.in=" + DEFAULT_SUBSIDIARY_NAME + "," + UPDATED_SUBSIDIARY_NAME
        );

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName equals to UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.in=" + UPDATED_SUBSIDIARY_NAME);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryName.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameContainsSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName contains DEFAULT_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryName.contains=" + DEFAULT_SUBSIDIARY_NAME);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName contains UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.contains=" + UPDATED_SUBSIDIARY_NAME);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryNameNotContainsSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName does not contain DEFAULT_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryName.doesNotContain=" + DEFAULT_SUBSIDIARY_NAME);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryName does not contain UPDATED_SUBSIDIARY_NAME
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryName.doesNotContain=" + UPDATED_SUBSIDIARY_NAME);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the performanceOfForeignSubsidiariesList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId equals to DEFAULT_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.equals=" + DEFAULT_SUBSIDIARY_ID);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId equals to UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.equals=" + UPDATED_SUBSIDIARY_ID);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId not equals to DEFAULT_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.notEquals=" + DEFAULT_SUBSIDIARY_ID);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId not equals to UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.notEquals=" + UPDATED_SUBSIDIARY_ID);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId in DEFAULT_SUBSIDIARY_ID or UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.in=" + DEFAULT_SUBSIDIARY_ID + "," + UPDATED_SUBSIDIARY_ID);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId equals to UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.in=" + UPDATED_SUBSIDIARY_ID);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdContainsSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId contains DEFAULT_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.contains=" + DEFAULT_SUBSIDIARY_ID);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId contains UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.contains=" + UPDATED_SUBSIDIARY_ID);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryIdNotContainsSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId does not contain DEFAULT_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryId.doesNotContain=" + DEFAULT_SUBSIDIARY_ID);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryId does not contain UPDATED_SUBSIDIARY_ID
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryId.doesNotContain=" + UPDATED_SUBSIDIARY_ID);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount equals to DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.equals=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount equals to UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.equals=" + UPDATED_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount not equals to DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.notEquals=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount not equals to UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.notEquals=" + UPDATED_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount in DEFAULT_GROSS_LOANS_AMOUNT or UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "grossLoansAmount.in=" + DEFAULT_GROSS_LOANS_AMOUNT + "," + UPDATED_GROSS_LOANS_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount equals to UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.in=" + UPDATED_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is greater than or equal to DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.greaterThanOrEqual=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is greater than or equal to UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.greaterThanOrEqual=" + UPDATED_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is less than or equal to DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.lessThanOrEqual=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is less than or equal to SMALLER_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.lessThanOrEqual=" + SMALLER_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is less than DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.lessThan=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is less than UPDATED_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.lessThan=" + UPDATED_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossLoansAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is greater than DEFAULT_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossLoansAmount.greaterThan=" + DEFAULT_GROSS_LOANS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossLoansAmount is greater than SMALLER_GROSS_LOANS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossLoansAmount.greaterThan=" + SMALLER_GROSS_LOANS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount equals to DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.equals=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount equals to UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.equals=" + UPDATED_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount not equals to DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.notEquals=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount not equals to UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.notEquals=" + UPDATED_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount in DEFAULT_GROSS_NPA_LOAN_AMOUNT or UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "grossNPALoanAmount.in=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT + "," + UPDATED_GROSS_NPA_LOAN_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount equals to UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.in=" + UPDATED_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is greater than or equal to DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.greaterThanOrEqual=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is greater than or equal to UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.greaterThanOrEqual=" + UPDATED_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is less than or equal to DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.lessThanOrEqual=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is less than or equal to SMALLER_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.lessThanOrEqual=" + SMALLER_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is less than DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.lessThan=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is less than UPDATED_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.lessThan=" + UPDATED_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossNPALoanAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is greater than DEFAULT_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossNPALoanAmount.greaterThan=" + DEFAULT_GROSS_NPA_LOAN_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossNPALoanAmount is greater than SMALLER_GROSS_NPA_LOAN_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossNPALoanAmount.greaterThan=" + SMALLER_GROSS_NPA_LOAN_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount equals to DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.equals=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount equals to UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.equals=" + UPDATED_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount not equals to DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.notEquals=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount not equals to UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.notEquals=" + UPDATED_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount in DEFAULT_GROSS_ASSETS_AMOUNT or UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "grossAssetsAmount.in=" + DEFAULT_GROSS_ASSETS_AMOUNT + "," + UPDATED_GROSS_ASSETS_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount equals to UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.in=" + UPDATED_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is greater than or equal to DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.greaterThanOrEqual=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is greater than or equal to UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.greaterThanOrEqual=" + UPDATED_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is less than or equal to DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.lessThanOrEqual=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is less than or equal to SMALLER_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.lessThanOrEqual=" + SMALLER_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is less than DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.lessThan=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is less than UPDATED_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.lessThan=" + UPDATED_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossAssetsAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is greater than DEFAULT_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossAssetsAmount.greaterThan=" + DEFAULT_GROSS_ASSETS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossAssetsAmount is greater than SMALLER_GROSS_ASSETS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossAssetsAmount.greaterThan=" + SMALLER_GROSS_ASSETS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount equals to DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.equals=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount equals to UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.equals=" + UPDATED_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount not equals to DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.notEquals=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount not equals to UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.notEquals=" + UPDATED_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount in DEFAULT_GROSS_DEPOSITS_AMOUNT or UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "grossDepositsAmount.in=" + DEFAULT_GROSS_DEPOSITS_AMOUNT + "," + UPDATED_GROSS_DEPOSITS_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount equals to UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.in=" + UPDATED_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is greater than or equal to DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.greaterThanOrEqual=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is greater than or equal to UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.greaterThanOrEqual=" + UPDATED_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is less than or equal to DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.lessThanOrEqual=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is less than or equal to SMALLER_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.lessThanOrEqual=" + SMALLER_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is less than DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.lessThan=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is less than UPDATED_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.lessThan=" + UPDATED_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGrossDepositsAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is greater than DEFAULT_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("grossDepositsAmount.greaterThan=" + DEFAULT_GROSS_DEPOSITS_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where grossDepositsAmount is greater than SMALLER_GROSS_DEPOSITS_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("grossDepositsAmount.greaterThan=" + SMALLER_GROSS_DEPOSITS_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax equals to DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.equals=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax equals to UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.equals=" + UPDATED_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax not equals to DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.notEquals=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax not equals to UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.notEquals=" + UPDATED_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax in DEFAULT_PROFIT_BEFORE_TAX or UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "profitBeforeTax.in=" + DEFAULT_PROFIT_BEFORE_TAX + "," + UPDATED_PROFIT_BEFORE_TAX
        );

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax equals to UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.in=" + UPDATED_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is greater than or equal to DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.greaterThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is greater than or equal to UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.greaterThanOrEqual=" + UPDATED_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is less than or equal to DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.lessThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is less than or equal to SMALLER_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.lessThanOrEqual=" + SMALLER_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is less than DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.lessThan=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is less than UPDATED_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.lessThan=" + UPDATED_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByProfitBeforeTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is greater than DEFAULT_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("profitBeforeTax.greaterThan=" + DEFAULT_PROFIT_BEFORE_TAX);

        // Get all the performanceOfForeignSubsidiariesList where profitBeforeTax is greater than SMALLER_PROFIT_BEFORE_TAX
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("profitBeforeTax.greaterThan=" + SMALLER_PROFIT_BEFORE_TAX);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio equals to DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalCapitalAdequacyRatio.equals=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio equals to UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalCapitalAdequacyRatio.equals=" + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio not equals to DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "totalCapitalAdequacyRatio.notEquals=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio not equals to UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalCapitalAdequacyRatio.notEquals=" + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio in DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO or UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "totalCapitalAdequacyRatio.in=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO + "," + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio equals to UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalCapitalAdequacyRatio.in=" + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalCapitalAdequacyRatio.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalCapitalAdequacyRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is greater than or equal to DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "totalCapitalAdequacyRatio.greaterThanOrEqual=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is greater than or equal to UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "totalCapitalAdequacyRatio.greaterThanOrEqual=" + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        );
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is less than or equal to DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "totalCapitalAdequacyRatio.lessThanOrEqual=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is less than or equal to SMALLER_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "totalCapitalAdequacyRatio.lessThanOrEqual=" + SMALLER_TOTAL_CAPITAL_ADEQUACY_RATIO
        );
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is less than DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "totalCapitalAdequacyRatio.lessThan=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is less than UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalCapitalAdequacyRatio.lessThan=" + UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalCapitalAdequacyRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is greater than DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "totalCapitalAdequacyRatio.greaterThan=" + DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where totalCapitalAdequacyRatio is greater than SMALLER_TOTAL_CAPITAL_ADEQUACY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "totalCapitalAdequacyRatio.greaterThan=" + SMALLER_TOTAL_CAPITAL_ADEQUACY_RATIO
        );
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio equals to DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.equals=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio equals to UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.equals=" + UPDATED_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio not equals to DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.notEquals=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio not equals to UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.notEquals=" + UPDATED_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio in DEFAULT_LIQUIDITY_RATIO or UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "liquidityRatio.in=" + DEFAULT_LIQUIDITY_RATIO + "," + UPDATED_LIQUIDITY_RATIO
        );

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio equals to UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.in=" + UPDATED_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is greater than or equal to DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.greaterThanOrEqual=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is greater than or equal to UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.greaterThanOrEqual=" + UPDATED_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is less than or equal to DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.lessThanOrEqual=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is less than or equal to SMALLER_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.lessThanOrEqual=" + SMALLER_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is less than DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.lessThan=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is less than UPDATED_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.lessThan=" + UPDATED_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByLiquidityRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is greater than DEFAULT_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("liquidityRatio.greaterThan=" + DEFAULT_LIQUIDITY_RATIO);

        // Get all the performanceOfForeignSubsidiariesList where liquidityRatio is greater than SMALLER_LIQUIDITY_RATIO
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("liquidityRatio.greaterThan=" + SMALLER_LIQUIDITY_RATIO);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions equals to DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.equals=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions equals to UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.equals=" + UPDATED_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions not equals to DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.notEquals=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions not equals to UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.notEquals=" + UPDATED_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions in DEFAULT_GENERAL_PROVISIONS or UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "generalProvisions.in=" + DEFAULT_GENERAL_PROVISIONS + "," + UPDATED_GENERAL_PROVISIONS
        );

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions equals to UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.in=" + UPDATED_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is greater than or equal to DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.greaterThanOrEqual=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is greater than or equal to UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.greaterThanOrEqual=" + UPDATED_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is less than or equal to DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.lessThanOrEqual=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is less than or equal to SMALLER_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.lessThanOrEqual=" + SMALLER_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is less than DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.lessThan=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is less than UPDATED_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.lessThan=" + UPDATED_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByGeneralProvisionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is greater than DEFAULT_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("generalProvisions.greaterThan=" + DEFAULT_GENERAL_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where generalProvisions is greater than SMALLER_GENERAL_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("generalProvisions.greaterThan=" + SMALLER_GENERAL_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions equals to DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.equals=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions equals to UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.equals=" + UPDATED_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions not equals to DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.notEquals=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions not equals to UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.notEquals=" + UPDATED_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions in DEFAULT_SPECIFIC_PROVISIONS or UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "specificProvisions.in=" + DEFAULT_SPECIFIC_PROVISIONS + "," + UPDATED_SPECIFIC_PROVISIONS
        );

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions equals to UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.in=" + UPDATED_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is greater than or equal to DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.greaterThanOrEqual=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is greater than or equal to UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.greaterThanOrEqual=" + UPDATED_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is less than or equal to DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.lessThanOrEqual=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is less than or equal to SMALLER_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.lessThanOrEqual=" + SMALLER_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is less than DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.lessThan=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is less than UPDATED_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.lessThan=" + UPDATED_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySpecificProvisionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is greater than DEFAULT_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("specificProvisions.greaterThan=" + DEFAULT_SPECIFIC_PROVISIONS);

        // Get all the performanceOfForeignSubsidiariesList where specificProvisions is greater than SMALLER_SPECIFIC_PROVISIONS
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("specificProvisions.greaterThan=" + SMALLER_SPECIFIC_PROVISIONS);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount equals to DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("interestInSuspenseAmount.equals=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount equals to UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("interestInSuspenseAmount.equals=" + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount not equals to DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "interestInSuspenseAmount.notEquals=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount not equals to UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("interestInSuspenseAmount.notEquals=" + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount in DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT or UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "interestInSuspenseAmount.in=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT + "," + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount equals to UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("interestInSuspenseAmount.in=" + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("interestInSuspenseAmount.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("interestInSuspenseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is greater than or equal to DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "interestInSuspenseAmount.greaterThanOrEqual=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is greater than or equal to UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "interestInSuspenseAmount.greaterThanOrEqual=" + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is less than or equal to DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "interestInSuspenseAmount.lessThanOrEqual=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is less than or equal to SMALLER_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "interestInSuspenseAmount.lessThanOrEqual=" + SMALLER_INTEREST_IN_SUSPENSE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is less than DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("interestInSuspenseAmount.lessThan=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is less than UPDATED_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("interestInSuspenseAmount.lessThan=" + UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByInterestInSuspenseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is greater than DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(
            "interestInSuspenseAmount.greaterThan=" + DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT
        );

        // Get all the performanceOfForeignSubsidiariesList where interestInSuspenseAmount is greater than SMALLER_INTEREST_IN_SUSPENSE_AMOUNT
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("interestInSuspenseAmount.greaterThan=" + SMALLER_INTEREST_IN_SUSPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff equals to DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.equals=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff equals to UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.equals=" + UPDATED_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff not equals to DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.notEquals=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff not equals to UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.notEquals=" + UPDATED_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff in DEFAULT_TOTAL_NUMBER_OF_STAFF or UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "totalNumberOfStaff.in=" + DEFAULT_TOTAL_NUMBER_OF_STAFF + "," + UPDATED_TOTAL_NUMBER_OF_STAFF
        );

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff equals to UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.in=" + UPDATED_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is greater than or equal to DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.greaterThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is greater than or equal to UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.greaterThanOrEqual=" + UPDATED_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is less than or equal to DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.lessThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is less than or equal to SMALLER_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.lessThanOrEqual=" + SMALLER_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is less than DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.lessThan=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is less than UPDATED_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.lessThan=" + UPDATED_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByTotalNumberOfStaffIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is greater than DEFAULT_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("totalNumberOfStaff.greaterThan=" + DEFAULT_TOTAL_NUMBER_OF_STAFF);

        // Get all the performanceOfForeignSubsidiariesList where totalNumberOfStaff is greater than SMALLER_TOTAL_NUMBER_OF_STAFF
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("totalNumberOfStaff.greaterThan=" + SMALLER_TOTAL_NUMBER_OF_STAFF);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches equals to DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.equals=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches equals to UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.equals=" + UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches not equals to DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.notEquals=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches not equals to UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.notEquals=" + UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsInShouldWork() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches in DEFAULT_NUMBER_OF_BRANCHES or UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound(
            "numberOfBranches.in=" + DEFAULT_NUMBER_OF_BRANCHES + "," + UPDATED_NUMBER_OF_BRANCHES
        );

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches equals to UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.in=" + UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsNullOrNotNull() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is not null
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.specified=true");

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is null
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.specified=false");
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is greater than or equal to DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is greater than or equal to UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.greaterThanOrEqual=" + UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is less than or equal to DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.lessThanOrEqual=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is less than or equal to SMALLER_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.lessThanOrEqual=" + SMALLER_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsLessThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is less than DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.lessThan=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is less than UPDATED_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.lessThan=" + UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByNumberOfBranchesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is greater than DEFAULT_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("numberOfBranches.greaterThan=" + DEFAULT_NUMBER_OF_BRANCHES);

        // Get all the performanceOfForeignSubsidiariesList where numberOfBranches is greater than SMALLER_NUMBER_OF_BRANCHES
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("numberOfBranches.greaterThan=" + SMALLER_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);
        InstitutionCode bankCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        performanceOfForeignSubsidiaries.setBankCode(bankCode);
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);
        Long bankCodeId = bankCode.getId();

        // Get all the performanceOfForeignSubsidiariesList where bankCode equals to bankCodeId
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the performanceOfForeignSubsidiariesList where bankCode equals to (bankCodeId + 1)
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllPerformanceOfForeignSubsidiariesBySubsidiaryCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);
        IsoCountryCode subsidiaryCountryCode;
        if (TestUtil.findAll(em, IsoCountryCode.class).isEmpty()) {
            subsidiaryCountryCode = IsoCountryCodeResourceIT.createEntity(em);
            em.persist(subsidiaryCountryCode);
            em.flush();
        } else {
            subsidiaryCountryCode = TestUtil.findAll(em, IsoCountryCode.class).get(0);
        }
        em.persist(subsidiaryCountryCode);
        em.flush();
        performanceOfForeignSubsidiaries.setSubsidiaryCountryCode(subsidiaryCountryCode);
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);
        Long subsidiaryCountryCodeId = subsidiaryCountryCode.getId();

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryCountryCode equals to subsidiaryCountryCodeId
        defaultPerformanceOfForeignSubsidiariesShouldBeFound("subsidiaryCountryCodeId.equals=" + subsidiaryCountryCodeId);

        // Get all the performanceOfForeignSubsidiariesList where subsidiaryCountryCode equals to (subsidiaryCountryCodeId + 1)
        defaultPerformanceOfForeignSubsidiariesShouldNotBeFound("subsidiaryCountryCodeId.equals=" + (subsidiaryCountryCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPerformanceOfForeignSubsidiariesShouldBeFound(String filter) throws Exception {
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceOfForeignSubsidiaries.getId().intValue())))
            .andExpect(jsonPath("$.[*].subsidiaryName").value(hasItem(DEFAULT_SUBSIDIARY_NAME)))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].subsidiaryId").value(hasItem(DEFAULT_SUBSIDIARY_ID)))
            .andExpect(jsonPath("$.[*].grossLoansAmount").value(hasItem(sameNumber(DEFAULT_GROSS_LOANS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossNPALoanAmount").value(hasItem(sameNumber(DEFAULT_GROSS_NPA_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossAssetsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_ASSETS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossDepositsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_DEPOSITS_AMOUNT))))
            .andExpect(jsonPath("$.[*].profitBeforeTax").value(hasItem(sameNumber(DEFAULT_PROFIT_BEFORE_TAX))))
            .andExpect(jsonPath("$.[*].totalCapitalAdequacyRatio").value(hasItem(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].liquidityRatio").value(hasItem(DEFAULT_LIQUIDITY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].generalProvisions").value(hasItem(sameNumber(DEFAULT_GENERAL_PROVISIONS))))
            .andExpect(jsonPath("$.[*].specificProvisions").value(hasItem(sameNumber(DEFAULT_SPECIFIC_PROVISIONS))))
            .andExpect(jsonPath("$.[*].interestInSuspenseAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalNumberOfStaff").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_STAFF)))
            .andExpect(jsonPath("$.[*].numberOfBranches").value(hasItem(DEFAULT_NUMBER_OF_BRANCHES)));

        // Check, that the count call also returns 1
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPerformanceOfForeignSubsidiariesShouldNotBeFound(String filter) throws Exception {
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerformanceOfForeignSubsidiaries() throws Exception {
        // Get the performanceOfForeignSubsidiaries
        restPerformanceOfForeignSubsidiariesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerformanceOfForeignSubsidiaries() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();

        // Update the performanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiaries updatedPerformanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesRepository
            .findById(performanceOfForeignSubsidiaries.getId())
            .get();
        // Disconnect from session so that the updates on updatedPerformanceOfForeignSubsidiaries are not directly saved in db
        em.detach(updatedPerformanceOfForeignSubsidiaries);
        updatedPerformanceOfForeignSubsidiaries
            .subsidiaryName(UPDATED_SUBSIDIARY_NAME)
            .reportingDate(UPDATED_REPORTING_DATE)
            .subsidiaryId(UPDATED_SUBSIDIARY_ID)
            .grossLoansAmount(UPDATED_GROSS_LOANS_AMOUNT)
            .grossNPALoanAmount(UPDATED_GROSS_NPA_LOAN_AMOUNT)
            .grossAssetsAmount(UPDATED_GROSS_ASSETS_AMOUNT)
            .grossDepositsAmount(UPDATED_GROSS_DEPOSITS_AMOUNT)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .totalCapitalAdequacyRatio(UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO)
            .liquidityRatio(UPDATED_LIQUIDITY_RATIO)
            .generalProvisions(UPDATED_GENERAL_PROVISIONS)
            .specificProvisions(UPDATED_SPECIFIC_PROVISIONS)
            .interestInSuspenseAmount(UPDATED_INTEREST_IN_SUSPENSE_AMOUNT)
            .totalNumberOfStaff(UPDATED_TOTAL_NUMBER_OF_STAFF)
            .numberOfBranches(UPDATED_NUMBER_OF_BRANCHES);
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            updatedPerformanceOfForeignSubsidiaries
        );

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, performanceOfForeignSubsidiariesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);
        PerformanceOfForeignSubsidiaries testPerformanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesList.get(
            performanceOfForeignSubsidiariesList.size() - 1
        );
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryName()).isEqualTo(UPDATED_SUBSIDIARY_NAME);
        assertThat(testPerformanceOfForeignSubsidiaries.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryId()).isEqualTo(UPDATED_SUBSIDIARY_ID);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossLoansAmount()).isEqualTo(UPDATED_GROSS_LOANS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossNPALoanAmount()).isEqualTo(UPDATED_GROSS_NPA_LOAN_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossAssetsAmount()).isEqualTo(UPDATED_GROSS_ASSETS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossDepositsAmount()).isEqualTo(UPDATED_GROSS_DEPOSITS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getProfitBeforeTax()).isEqualTo(UPDATED_PROFIT_BEFORE_TAX);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalCapitalAdequacyRatio()).isEqualTo(UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getLiquidityRatio()).isEqualTo(UPDATED_LIQUIDITY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getGeneralProvisions()).isEqualTo(UPDATED_GENERAL_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getSpecificProvisions()).isEqualTo(UPDATED_SPECIFIC_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getInterestInSuspenseAmount()).isEqualTo(UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalNumberOfStaff()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_STAFF);
        assertThat(testPerformanceOfForeignSubsidiaries.getNumberOfBranches()).isEqualTo(UPDATED_NUMBER_OF_BRANCHES);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository).save(testPerformanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void putNonExistingPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, performanceOfForeignSubsidiariesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void partialUpdatePerformanceOfForeignSubsidiariesWithPatch() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();

        // Update the performanceOfForeignSubsidiaries using partial update
        PerformanceOfForeignSubsidiaries partialUpdatedPerformanceOfForeignSubsidiaries = new PerformanceOfForeignSubsidiaries();
        partialUpdatedPerformanceOfForeignSubsidiaries.setId(performanceOfForeignSubsidiaries.getId());

        partialUpdatedPerformanceOfForeignSubsidiaries
            .grossAssetsAmount(UPDATED_GROSS_ASSETS_AMOUNT)
            .generalProvisions(UPDATED_GENERAL_PROVISIONS)
            .numberOfBranches(UPDATED_NUMBER_OF_BRANCHES);

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerformanceOfForeignSubsidiaries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerformanceOfForeignSubsidiaries))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);
        PerformanceOfForeignSubsidiaries testPerformanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesList.get(
            performanceOfForeignSubsidiariesList.size() - 1
        );
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryName()).isEqualTo(DEFAULT_SUBSIDIARY_NAME);
        assertThat(testPerformanceOfForeignSubsidiaries.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryId()).isEqualTo(DEFAULT_SUBSIDIARY_ID);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossLoansAmount()).isEqualByComparingTo(DEFAULT_GROSS_LOANS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossNPALoanAmount()).isEqualByComparingTo(DEFAULT_GROSS_NPA_LOAN_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossAssetsAmount()).isEqualByComparingTo(UPDATED_GROSS_ASSETS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossDepositsAmount()).isEqualByComparingTo(DEFAULT_GROSS_DEPOSITS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getProfitBeforeTax()).isEqualByComparingTo(DEFAULT_PROFIT_BEFORE_TAX);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalCapitalAdequacyRatio()).isEqualTo(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getLiquidityRatio()).isEqualTo(DEFAULT_LIQUIDITY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getGeneralProvisions()).isEqualByComparingTo(UPDATED_GENERAL_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getSpecificProvisions()).isEqualByComparingTo(DEFAULT_SPECIFIC_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getInterestInSuspenseAmount())
            .isEqualByComparingTo(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalNumberOfStaff()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_STAFF);
        assertThat(testPerformanceOfForeignSubsidiaries.getNumberOfBranches()).isEqualTo(UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void fullUpdatePerformanceOfForeignSubsidiariesWithPatch() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();

        // Update the performanceOfForeignSubsidiaries using partial update
        PerformanceOfForeignSubsidiaries partialUpdatedPerformanceOfForeignSubsidiaries = new PerformanceOfForeignSubsidiaries();
        partialUpdatedPerformanceOfForeignSubsidiaries.setId(performanceOfForeignSubsidiaries.getId());

        partialUpdatedPerformanceOfForeignSubsidiaries
            .subsidiaryName(UPDATED_SUBSIDIARY_NAME)
            .reportingDate(UPDATED_REPORTING_DATE)
            .subsidiaryId(UPDATED_SUBSIDIARY_ID)
            .grossLoansAmount(UPDATED_GROSS_LOANS_AMOUNT)
            .grossNPALoanAmount(UPDATED_GROSS_NPA_LOAN_AMOUNT)
            .grossAssetsAmount(UPDATED_GROSS_ASSETS_AMOUNT)
            .grossDepositsAmount(UPDATED_GROSS_DEPOSITS_AMOUNT)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .totalCapitalAdequacyRatio(UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO)
            .liquidityRatio(UPDATED_LIQUIDITY_RATIO)
            .generalProvisions(UPDATED_GENERAL_PROVISIONS)
            .specificProvisions(UPDATED_SPECIFIC_PROVISIONS)
            .interestInSuspenseAmount(UPDATED_INTEREST_IN_SUSPENSE_AMOUNT)
            .totalNumberOfStaff(UPDATED_TOTAL_NUMBER_OF_STAFF)
            .numberOfBranches(UPDATED_NUMBER_OF_BRANCHES);

        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerformanceOfForeignSubsidiaries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerformanceOfForeignSubsidiaries))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);
        PerformanceOfForeignSubsidiaries testPerformanceOfForeignSubsidiaries = performanceOfForeignSubsidiariesList.get(
            performanceOfForeignSubsidiariesList.size() - 1
        );
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryName()).isEqualTo(UPDATED_SUBSIDIARY_NAME);
        assertThat(testPerformanceOfForeignSubsidiaries.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testPerformanceOfForeignSubsidiaries.getSubsidiaryId()).isEqualTo(UPDATED_SUBSIDIARY_ID);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossLoansAmount()).isEqualByComparingTo(UPDATED_GROSS_LOANS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossNPALoanAmount()).isEqualByComparingTo(UPDATED_GROSS_NPA_LOAN_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossAssetsAmount()).isEqualByComparingTo(UPDATED_GROSS_ASSETS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getGrossDepositsAmount()).isEqualByComparingTo(UPDATED_GROSS_DEPOSITS_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getProfitBeforeTax()).isEqualByComparingTo(UPDATED_PROFIT_BEFORE_TAX);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalCapitalAdequacyRatio()).isEqualTo(UPDATED_TOTAL_CAPITAL_ADEQUACY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getLiquidityRatio()).isEqualTo(UPDATED_LIQUIDITY_RATIO);
        assertThat(testPerformanceOfForeignSubsidiaries.getGeneralProvisions()).isEqualByComparingTo(UPDATED_GENERAL_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getSpecificProvisions()).isEqualByComparingTo(UPDATED_SPECIFIC_PROVISIONS);
        assertThat(testPerformanceOfForeignSubsidiaries.getInterestInSuspenseAmount())
            .isEqualByComparingTo(UPDATED_INTEREST_IN_SUSPENSE_AMOUNT);
        assertThat(testPerformanceOfForeignSubsidiaries.getTotalNumberOfStaff()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_STAFF);
        assertThat(testPerformanceOfForeignSubsidiaries.getNumberOfBranches()).isEqualTo(UPDATED_NUMBER_OF_BRANCHES);
    }

    @Test
    @Transactional
    void patchNonExistingPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, performanceOfForeignSubsidiariesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerformanceOfForeignSubsidiaries() throws Exception {
        int databaseSizeBeforeUpdate = performanceOfForeignSubsidiariesRepository.findAll().size();
        performanceOfForeignSubsidiaries.setId(count.incrementAndGet());

        // Create the PerformanceOfForeignSubsidiaries
        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesMapper.toDto(
            performanceOfForeignSubsidiaries
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(performanceOfForeignSubsidiariesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerformanceOfForeignSubsidiaries in the database
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(0)).save(performanceOfForeignSubsidiaries);
    }

    @Test
    @Transactional
    void deletePerformanceOfForeignSubsidiaries() throws Exception {
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);

        int databaseSizeBeforeDelete = performanceOfForeignSubsidiariesRepository.findAll().size();

        // Delete the performanceOfForeignSubsidiaries
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(delete(ENTITY_API_URL_ID, performanceOfForeignSubsidiaries.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PerformanceOfForeignSubsidiaries> performanceOfForeignSubsidiariesList = performanceOfForeignSubsidiariesRepository.findAll();
        assertThat(performanceOfForeignSubsidiariesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PerformanceOfForeignSubsidiaries in Elasticsearch
        verify(mockPerformanceOfForeignSubsidiariesSearchRepository, times(1)).deleteById(performanceOfForeignSubsidiaries.getId());
    }

    @Test
    @Transactional
    void searchPerformanceOfForeignSubsidiaries() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        performanceOfForeignSubsidiariesRepository.saveAndFlush(performanceOfForeignSubsidiaries);
        when(
            mockPerformanceOfForeignSubsidiariesSearchRepository.search(
                "id:" + performanceOfForeignSubsidiaries.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(performanceOfForeignSubsidiaries), PageRequest.of(0, 1), 1));

        // Search the performanceOfForeignSubsidiaries
        restPerformanceOfForeignSubsidiariesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + performanceOfForeignSubsidiaries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceOfForeignSubsidiaries.getId().intValue())))
            .andExpect(jsonPath("$.[*].subsidiaryName").value(hasItem(DEFAULT_SUBSIDIARY_NAME)))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].subsidiaryId").value(hasItem(DEFAULT_SUBSIDIARY_ID)))
            .andExpect(jsonPath("$.[*].grossLoansAmount").value(hasItem(sameNumber(DEFAULT_GROSS_LOANS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossNPALoanAmount").value(hasItem(sameNumber(DEFAULT_GROSS_NPA_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossAssetsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_ASSETS_AMOUNT))))
            .andExpect(jsonPath("$.[*].grossDepositsAmount").value(hasItem(sameNumber(DEFAULT_GROSS_DEPOSITS_AMOUNT))))
            .andExpect(jsonPath("$.[*].profitBeforeTax").value(hasItem(sameNumber(DEFAULT_PROFIT_BEFORE_TAX))))
            .andExpect(jsonPath("$.[*].totalCapitalAdequacyRatio").value(hasItem(DEFAULT_TOTAL_CAPITAL_ADEQUACY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].liquidityRatio").value(hasItem(DEFAULT_LIQUIDITY_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].generalProvisions").value(hasItem(sameNumber(DEFAULT_GENERAL_PROVISIONS))))
            .andExpect(jsonPath("$.[*].specificProvisions").value(hasItem(sameNumber(DEFAULT_SPECIFIC_PROVISIONS))))
            .andExpect(jsonPath("$.[*].interestInSuspenseAmount").value(hasItem(sameNumber(DEFAULT_INTEREST_IN_SUSPENSE_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalNumberOfStaff").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_STAFF)))
            .andExpect(jsonPath("$.[*].numberOfBranches").value(hasItem(DEFAULT_NUMBER_OF_BRANCHES)));
    }
}
