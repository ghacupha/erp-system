package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.enumeration.CollateralInsuredFlagTypes;
import io.github.erp.repository.CollateralInformationRepository;
import io.github.erp.repository.search.CollateralInformationSearchRepository;
import io.github.erp.service.dto.CollateralInformationDTO;
import io.github.erp.service.mapper.CollateralInformationMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CollateralInformationResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CollateralInformationResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COLLATERAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_COLLATERAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_CONTRACT_ID = "520340966401515";
    private static final String UPDATED_LOAN_CONTRACT_ID = "007288294904348";

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_PROPERTY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_PROPERTY_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COLLATERAL_OMV_IN_CCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_COLLATERAL_OMV_IN_CCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_COLLATERAL_OMV_IN_CCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_COLLATERAL_FSV_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_COLLATERAL_FSV_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_COLLATERAL_FSV_IN_LCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_COLLATERAL_DISCOUNTED_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_COLLATERAL_DISCOUNTED_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_COLLATERAL_DISCOUNTED_VALUE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_AMOUNT_CHARGED = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_CHARGED = new BigDecimal(1);
    private static final BigDecimal SMALLER_AMOUNT_CHARGED = new BigDecimal(0 - 1);

    private static final Double DEFAULT_COLLATERAL_DISCOUNT_RATE = 0D;
    private static final Double UPDATED_COLLATERAL_DISCOUNT_RATE = 1D;
    private static final Double SMALLER_COLLATERAL_DISCOUNT_RATE = 0D - 1D;

    private static final Double DEFAULT_LOAN_TO_VALUE_RATIO = 0D;
    private static final Double UPDATED_LOAN_TO_VALUE_RATIO = 1D;
    private static final Double SMALLER_LOAN_TO_VALUE_RATIO = 0D - 1D;

    private static final String DEFAULT_NAME_OF_PROPERTY_VALUER = "AAAAAAAAAA";
    private static final String UPDATED_NAME_OF_PROPERTY_VALUER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COLLATERAL_LAST_VALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COLLATERAL_LAST_VALUATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COLLATERAL_LAST_VALUATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final CollateralInsuredFlagTypes DEFAULT_INSURED_FLAG = CollateralInsuredFlagTypes.Y;
    private static final CollateralInsuredFlagTypes UPDATED_INSURED_FLAG = CollateralInsuredFlagTypes.N;

    private static final String DEFAULT_NAME_OF_INSURER = "AAAAAAAAAA";
    private static final String UPDATED_NAME_OF_INSURER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT_INSURED = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_INSURED = new BigDecimal(1);
    private static final BigDecimal SMALLER_AMOUNT_INSURED = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_INSURANCE_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSURANCE_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INSURANCE_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_GUARANTEE_INSURERS = "AAAAAAAAAA";
    private static final String UPDATED_GUARANTEE_INSURERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/collateral-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/collateral-informations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollateralInformationRepository collateralInformationRepository;

    @Autowired
    private CollateralInformationMapper collateralInformationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CollateralInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollateralInformationSearchRepository mockCollateralInformationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollateralInformationMockMvc;

    private CollateralInformation collateralInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollateralInformation createEntity(EntityManager em) {
        CollateralInformation collateralInformation = new CollateralInformation()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .collateralId(DEFAULT_COLLATERAL_ID)
            .loanContractId(DEFAULT_LOAN_CONTRACT_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .registrationPropertyNumber(DEFAULT_REGISTRATION_PROPERTY_NUMBER)
            .collateralOMVInCCY(DEFAULT_COLLATERAL_OMV_IN_CCY)
            .collateralFSVInLCY(DEFAULT_COLLATERAL_FSV_IN_LCY)
            .collateralDiscountedValue(DEFAULT_COLLATERAL_DISCOUNTED_VALUE)
            .amountCharged(DEFAULT_AMOUNT_CHARGED)
            .collateralDiscountRate(DEFAULT_COLLATERAL_DISCOUNT_RATE)
            .loanToValueRatio(DEFAULT_LOAN_TO_VALUE_RATIO)
            .nameOfPropertyValuer(DEFAULT_NAME_OF_PROPERTY_VALUER)
            .collateralLastValuationDate(DEFAULT_COLLATERAL_LAST_VALUATION_DATE)
            .insuredFlag(DEFAULT_INSURED_FLAG)
            .nameOfInsurer(DEFAULT_NAME_OF_INSURER)
            .amountInsured(DEFAULT_AMOUNT_INSURED)
            .insuranceExpiryDate(DEFAULT_INSURANCE_EXPIRY_DATE)
            .guaranteeInsurers(DEFAULT_GUARANTEE_INSURERS);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        collateralInformation.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        collateralInformation.setBranchCode(bankBranchCode);
        // Add required entity
        CollateralType collateralType;
        if (TestUtil.findAll(em, CollateralType.class).isEmpty()) {
            collateralType = CollateralTypeResourceIT.createEntity(em);
            em.persist(collateralType);
            em.flush();
        } else {
            collateralType = TestUtil.findAll(em, CollateralType.class).get(0);
        }
        collateralInformation.setCollateralType(collateralType);
        return collateralInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollateralInformation createUpdatedEntity(EntityManager em) {
        CollateralInformation collateralInformation = new CollateralInformation()
            .reportingDate(UPDATED_REPORTING_DATE)
            .collateralId(UPDATED_COLLATERAL_ID)
            .loanContractId(UPDATED_LOAN_CONTRACT_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .registrationPropertyNumber(UPDATED_REGISTRATION_PROPERTY_NUMBER)
            .collateralOMVInCCY(UPDATED_COLLATERAL_OMV_IN_CCY)
            .collateralFSVInLCY(UPDATED_COLLATERAL_FSV_IN_LCY)
            .collateralDiscountedValue(UPDATED_COLLATERAL_DISCOUNTED_VALUE)
            .amountCharged(UPDATED_AMOUNT_CHARGED)
            .collateralDiscountRate(UPDATED_COLLATERAL_DISCOUNT_RATE)
            .loanToValueRatio(UPDATED_LOAN_TO_VALUE_RATIO)
            .nameOfPropertyValuer(UPDATED_NAME_OF_PROPERTY_VALUER)
            .collateralLastValuationDate(UPDATED_COLLATERAL_LAST_VALUATION_DATE)
            .insuredFlag(UPDATED_INSURED_FLAG)
            .nameOfInsurer(UPDATED_NAME_OF_INSURER)
            .amountInsured(UPDATED_AMOUNT_INSURED)
            .insuranceExpiryDate(UPDATED_INSURANCE_EXPIRY_DATE)
            .guaranteeInsurers(UPDATED_GUARANTEE_INSURERS);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        collateralInformation.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        collateralInformation.setBranchCode(bankBranchCode);
        // Add required entity
        CollateralType collateralType;
        if (TestUtil.findAll(em, CollateralType.class).isEmpty()) {
            collateralType = CollateralTypeResourceIT.createUpdatedEntity(em);
            em.persist(collateralType);
            em.flush();
        } else {
            collateralType = TestUtil.findAll(em, CollateralType.class).get(0);
        }
        collateralInformation.setCollateralType(collateralType);
        return collateralInformation;
    }

    @BeforeEach
    public void initTest() {
        collateralInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createCollateralInformation() throws Exception {
        int databaseSizeBeforeCreate = collateralInformationRepository.findAll().size();
        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);
        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeCreate + 1);
        CollateralInformation testCollateralInformation = collateralInformationList.get(collateralInformationList.size() - 1);
        assertThat(testCollateralInformation.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCollateralInformation.getCollateralId()).isEqualTo(DEFAULT_COLLATERAL_ID);
        assertThat(testCollateralInformation.getLoanContractId()).isEqualTo(DEFAULT_LOAN_CONTRACT_ID);
        assertThat(testCollateralInformation.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCollateralInformation.getRegistrationPropertyNumber()).isEqualTo(DEFAULT_REGISTRATION_PROPERTY_NUMBER);
        assertThat(testCollateralInformation.getCollateralOMVInCCY()).isEqualByComparingTo(DEFAULT_COLLATERAL_OMV_IN_CCY);
        assertThat(testCollateralInformation.getCollateralFSVInLCY()).isEqualByComparingTo(DEFAULT_COLLATERAL_FSV_IN_LCY);
        assertThat(testCollateralInformation.getCollateralDiscountedValue()).isEqualByComparingTo(DEFAULT_COLLATERAL_DISCOUNTED_VALUE);
        assertThat(testCollateralInformation.getAmountCharged()).isEqualByComparingTo(DEFAULT_AMOUNT_CHARGED);
        assertThat(testCollateralInformation.getCollateralDiscountRate()).isEqualTo(DEFAULT_COLLATERAL_DISCOUNT_RATE);
        assertThat(testCollateralInformation.getLoanToValueRatio()).isEqualTo(DEFAULT_LOAN_TO_VALUE_RATIO);
        assertThat(testCollateralInformation.getNameOfPropertyValuer()).isEqualTo(DEFAULT_NAME_OF_PROPERTY_VALUER);
        assertThat(testCollateralInformation.getCollateralLastValuationDate()).isEqualTo(DEFAULT_COLLATERAL_LAST_VALUATION_DATE);
        assertThat(testCollateralInformation.getInsuredFlag()).isEqualTo(DEFAULT_INSURED_FLAG);
        assertThat(testCollateralInformation.getNameOfInsurer()).isEqualTo(DEFAULT_NAME_OF_INSURER);
        assertThat(testCollateralInformation.getAmountInsured()).isEqualByComparingTo(DEFAULT_AMOUNT_INSURED);
        assertThat(testCollateralInformation.getInsuranceExpiryDate()).isEqualTo(DEFAULT_INSURANCE_EXPIRY_DATE);
        assertThat(testCollateralInformation.getGuaranteeInsurers()).isEqualTo(DEFAULT_GUARANTEE_INSURERS);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(1)).save(testCollateralInformation);
    }

    @Test
    @Transactional
    void createCollateralInformationWithExistingId() throws Exception {
        // Create the CollateralInformation with an existing ID
        collateralInformation.setId(1L);
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        int databaseSizeBeforeCreate = collateralInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setReportingDate(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCollateralIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setCollateralId(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanContractIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setLoanContractId(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setCustomerId(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCollateralOMVInCCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setCollateralOMVInCCY(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCollateralFSVInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setCollateralFSVInLCY(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountChargedIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setAmountCharged(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuredFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = collateralInformationRepository.findAll().size();
        // set the field null
        collateralInformation.setInsuredFlag(null);

        // Create the CollateralInformation, which fails.
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        restCollateralInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollateralInformations() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].collateralId").value(hasItem(DEFAULT_COLLATERAL_ID)))
            .andExpect(jsonPath("$.[*].loanContractId").value(hasItem(DEFAULT_LOAN_CONTRACT_ID)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].registrationPropertyNumber").value(hasItem(DEFAULT_REGISTRATION_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].collateralOMVInCCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_OMV_IN_CCY))))
            .andExpect(jsonPath("$.[*].collateralFSVInLCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_FSV_IN_LCY))))
            .andExpect(jsonPath("$.[*].collateralDiscountedValue").value(hasItem(sameNumber(DEFAULT_COLLATERAL_DISCOUNTED_VALUE))))
            .andExpect(jsonPath("$.[*].amountCharged").value(hasItem(sameNumber(DEFAULT_AMOUNT_CHARGED))))
            .andExpect(jsonPath("$.[*].collateralDiscountRate").value(hasItem(DEFAULT_COLLATERAL_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanToValueRatio").value(hasItem(DEFAULT_LOAN_TO_VALUE_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].nameOfPropertyValuer").value(hasItem(DEFAULT_NAME_OF_PROPERTY_VALUER)))
            .andExpect(jsonPath("$.[*].collateralLastValuationDate").value(hasItem(DEFAULT_COLLATERAL_LAST_VALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].insuredFlag").value(hasItem(DEFAULT_INSURED_FLAG.toString())))
            .andExpect(jsonPath("$.[*].nameOfInsurer").value(hasItem(DEFAULT_NAME_OF_INSURER)))
            .andExpect(jsonPath("$.[*].amountInsured").value(hasItem(sameNumber(DEFAULT_AMOUNT_INSURED))))
            .andExpect(jsonPath("$.[*].insuranceExpiryDate").value(hasItem(DEFAULT_INSURANCE_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].guaranteeInsurers").value(hasItem(DEFAULT_GUARANTEE_INSURERS)));
    }

    @Test
    @Transactional
    void getCollateralInformation() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get the collateralInformation
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, collateralInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collateralInformation.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.collateralId").value(DEFAULT_COLLATERAL_ID))
            .andExpect(jsonPath("$.loanContractId").value(DEFAULT_LOAN_CONTRACT_ID))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.registrationPropertyNumber").value(DEFAULT_REGISTRATION_PROPERTY_NUMBER))
            .andExpect(jsonPath("$.collateralOMVInCCY").value(sameNumber(DEFAULT_COLLATERAL_OMV_IN_CCY)))
            .andExpect(jsonPath("$.collateralFSVInLCY").value(sameNumber(DEFAULT_COLLATERAL_FSV_IN_LCY)))
            .andExpect(jsonPath("$.collateralDiscountedValue").value(sameNumber(DEFAULT_COLLATERAL_DISCOUNTED_VALUE)))
            .andExpect(jsonPath("$.amountCharged").value(sameNumber(DEFAULT_AMOUNT_CHARGED)))
            .andExpect(jsonPath("$.collateralDiscountRate").value(DEFAULT_COLLATERAL_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.loanToValueRatio").value(DEFAULT_LOAN_TO_VALUE_RATIO.doubleValue()))
            .andExpect(jsonPath("$.nameOfPropertyValuer").value(DEFAULT_NAME_OF_PROPERTY_VALUER))
            .andExpect(jsonPath("$.collateralLastValuationDate").value(DEFAULT_COLLATERAL_LAST_VALUATION_DATE.toString()))
            .andExpect(jsonPath("$.insuredFlag").value(DEFAULT_INSURED_FLAG.toString()))
            .andExpect(jsonPath("$.nameOfInsurer").value(DEFAULT_NAME_OF_INSURER))
            .andExpect(jsonPath("$.amountInsured").value(sameNumber(DEFAULT_AMOUNT_INSURED)))
            .andExpect(jsonPath("$.insuranceExpiryDate").value(DEFAULT_INSURANCE_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.guaranteeInsurers").value(DEFAULT_GUARANTEE_INSURERS));
    }

    @Test
    @Transactional
    void getCollateralInformationsByIdFiltering() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        Long id = collateralInformation.getId();

        defaultCollateralInformationShouldBeFound("id.equals=" + id);
        defaultCollateralInformationShouldNotBeFound("id.notEquals=" + id);

        defaultCollateralInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollateralInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultCollateralInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollateralInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate is not null
        defaultCollateralInformationShouldBeFound("reportingDate.specified=true");

        // Get all the collateralInformationList where reportingDate is null
        defaultCollateralInformationShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCollateralInformationShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the collateralInformationList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCollateralInformationShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId equals to DEFAULT_COLLATERAL_ID
        defaultCollateralInformationShouldBeFound("collateralId.equals=" + DEFAULT_COLLATERAL_ID);

        // Get all the collateralInformationList where collateralId equals to UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldNotBeFound("collateralId.equals=" + UPDATED_COLLATERAL_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId not equals to DEFAULT_COLLATERAL_ID
        defaultCollateralInformationShouldNotBeFound("collateralId.notEquals=" + DEFAULT_COLLATERAL_ID);

        // Get all the collateralInformationList where collateralId not equals to UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldBeFound("collateralId.notEquals=" + UPDATED_COLLATERAL_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId in DEFAULT_COLLATERAL_ID or UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldBeFound("collateralId.in=" + DEFAULT_COLLATERAL_ID + "," + UPDATED_COLLATERAL_ID);

        // Get all the collateralInformationList where collateralId equals to UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldNotBeFound("collateralId.in=" + UPDATED_COLLATERAL_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId is not null
        defaultCollateralInformationShouldBeFound("collateralId.specified=true");

        // Get all the collateralInformationList where collateralId is null
        defaultCollateralInformationShouldNotBeFound("collateralId.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId contains DEFAULT_COLLATERAL_ID
        defaultCollateralInformationShouldBeFound("collateralId.contains=" + DEFAULT_COLLATERAL_ID);

        // Get all the collateralInformationList where collateralId contains UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldNotBeFound("collateralId.contains=" + UPDATED_COLLATERAL_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralIdNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralId does not contain DEFAULT_COLLATERAL_ID
        defaultCollateralInformationShouldNotBeFound("collateralId.doesNotContain=" + DEFAULT_COLLATERAL_ID);

        // Get all the collateralInformationList where collateralId does not contain UPDATED_COLLATERAL_ID
        defaultCollateralInformationShouldBeFound("collateralId.doesNotContain=" + UPDATED_COLLATERAL_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId equals to DEFAULT_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldBeFound("loanContractId.equals=" + DEFAULT_LOAN_CONTRACT_ID);

        // Get all the collateralInformationList where loanContractId equals to UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldNotBeFound("loanContractId.equals=" + UPDATED_LOAN_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId not equals to DEFAULT_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldNotBeFound("loanContractId.notEquals=" + DEFAULT_LOAN_CONTRACT_ID);

        // Get all the collateralInformationList where loanContractId not equals to UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldBeFound("loanContractId.notEquals=" + UPDATED_LOAN_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId in DEFAULT_LOAN_CONTRACT_ID or UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldBeFound("loanContractId.in=" + DEFAULT_LOAN_CONTRACT_ID + "," + UPDATED_LOAN_CONTRACT_ID);

        // Get all the collateralInformationList where loanContractId equals to UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldNotBeFound("loanContractId.in=" + UPDATED_LOAN_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId is not null
        defaultCollateralInformationShouldBeFound("loanContractId.specified=true");

        // Get all the collateralInformationList where loanContractId is null
        defaultCollateralInformationShouldNotBeFound("loanContractId.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId contains DEFAULT_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldBeFound("loanContractId.contains=" + DEFAULT_LOAN_CONTRACT_ID);

        // Get all the collateralInformationList where loanContractId contains UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldNotBeFound("loanContractId.contains=" + UPDATED_LOAN_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanContractIdNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanContractId does not contain DEFAULT_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldNotBeFound("loanContractId.doesNotContain=" + DEFAULT_LOAN_CONTRACT_ID);

        // Get all the collateralInformationList where loanContractId does not contain UPDATED_LOAN_CONTRACT_ID
        defaultCollateralInformationShouldBeFound("loanContractId.doesNotContain=" + UPDATED_LOAN_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultCollateralInformationShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the collateralInformationList where customerId equals to UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId not equals to DEFAULT_CUSTOMER_ID
        defaultCollateralInformationShouldNotBeFound("customerId.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the collateralInformationList where customerId not equals to UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldBeFound("customerId.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the collateralInformationList where customerId equals to UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId is not null
        defaultCollateralInformationShouldBeFound("customerId.specified=true");

        // Get all the collateralInformationList where customerId is null
        defaultCollateralInformationShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId contains DEFAULT_CUSTOMER_ID
        defaultCollateralInformationShouldBeFound("customerId.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the collateralInformationList where customerId contains UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldNotBeFound("customerId.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where customerId does not contain DEFAULT_CUSTOMER_ID
        defaultCollateralInformationShouldNotBeFound("customerId.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the collateralInformationList where customerId does not contain UPDATED_CUSTOMER_ID
        defaultCollateralInformationShouldBeFound("customerId.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber equals to DEFAULT_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldBeFound("registrationPropertyNumber.equals=" + DEFAULT_REGISTRATION_PROPERTY_NUMBER);

        // Get all the collateralInformationList where registrationPropertyNumber equals to UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.equals=" + UPDATED_REGISTRATION_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber not equals to DEFAULT_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.notEquals=" + DEFAULT_REGISTRATION_PROPERTY_NUMBER);

        // Get all the collateralInformationList where registrationPropertyNumber not equals to UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldBeFound("registrationPropertyNumber.notEquals=" + UPDATED_REGISTRATION_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber in DEFAULT_REGISTRATION_PROPERTY_NUMBER or UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldBeFound(
            "registrationPropertyNumber.in=" + DEFAULT_REGISTRATION_PROPERTY_NUMBER + "," + UPDATED_REGISTRATION_PROPERTY_NUMBER
        );

        // Get all the collateralInformationList where registrationPropertyNumber equals to UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.in=" + UPDATED_REGISTRATION_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber is not null
        defaultCollateralInformationShouldBeFound("registrationPropertyNumber.specified=true");

        // Get all the collateralInformationList where registrationPropertyNumber is null
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber contains DEFAULT_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldBeFound("registrationPropertyNumber.contains=" + DEFAULT_REGISTRATION_PROPERTY_NUMBER);

        // Get all the collateralInformationList where registrationPropertyNumber contains UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.contains=" + UPDATED_REGISTRATION_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByRegistrationPropertyNumberNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where registrationPropertyNumber does not contain DEFAULT_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldNotBeFound("registrationPropertyNumber.doesNotContain=" + DEFAULT_REGISTRATION_PROPERTY_NUMBER);

        // Get all the collateralInformationList where registrationPropertyNumber does not contain UPDATED_REGISTRATION_PROPERTY_NUMBER
        defaultCollateralInformationShouldBeFound("registrationPropertyNumber.doesNotContain=" + UPDATED_REGISTRATION_PROPERTY_NUMBER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY equals to DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.equals=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY equals to UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.equals=" + UPDATED_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY not equals to DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.notEquals=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY not equals to UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.notEquals=" + UPDATED_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY in DEFAULT_COLLATERAL_OMV_IN_CCY or UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound(
            "collateralOMVInCCY.in=" + DEFAULT_COLLATERAL_OMV_IN_CCY + "," + UPDATED_COLLATERAL_OMV_IN_CCY
        );

        // Get all the collateralInformationList where collateralOMVInCCY equals to UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.in=" + UPDATED_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY is not null
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.specified=true");

        // Get all the collateralInformationList where collateralOMVInCCY is null
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY is greater than or equal to DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.greaterThanOrEqual=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY is greater than or equal to UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.greaterThanOrEqual=" + UPDATED_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY is less than or equal to DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.lessThanOrEqual=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY is less than or equal to SMALLER_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.lessThanOrEqual=" + SMALLER_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY is less than DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.lessThan=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY is less than UPDATED_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.lessThan=" + UPDATED_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralOMVInCCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralOMVInCCY is greater than DEFAULT_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldNotBeFound("collateralOMVInCCY.greaterThan=" + DEFAULT_COLLATERAL_OMV_IN_CCY);

        // Get all the collateralInformationList where collateralOMVInCCY is greater than SMALLER_COLLATERAL_OMV_IN_CCY
        defaultCollateralInformationShouldBeFound("collateralOMVInCCY.greaterThan=" + SMALLER_COLLATERAL_OMV_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY equals to DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.equals=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY equals to UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.equals=" + UPDATED_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY not equals to DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.notEquals=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY not equals to UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.notEquals=" + UPDATED_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY in DEFAULT_COLLATERAL_FSV_IN_LCY or UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound(
            "collateralFSVInLCY.in=" + DEFAULT_COLLATERAL_FSV_IN_LCY + "," + UPDATED_COLLATERAL_FSV_IN_LCY
        );

        // Get all the collateralInformationList where collateralFSVInLCY equals to UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.in=" + UPDATED_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY is not null
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.specified=true");

        // Get all the collateralInformationList where collateralFSVInLCY is null
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY is greater than or equal to DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.greaterThanOrEqual=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY is greater than or equal to UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.greaterThanOrEqual=" + UPDATED_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY is less than or equal to DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.lessThanOrEqual=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY is less than or equal to SMALLER_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.lessThanOrEqual=" + SMALLER_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY is less than DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.lessThan=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY is less than UPDATED_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.lessThan=" + UPDATED_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralFSVInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralFSVInLCY is greater than DEFAULT_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldNotBeFound("collateralFSVInLCY.greaterThan=" + DEFAULT_COLLATERAL_FSV_IN_LCY);

        // Get all the collateralInformationList where collateralFSVInLCY is greater than SMALLER_COLLATERAL_FSV_IN_LCY
        defaultCollateralInformationShouldBeFound("collateralFSVInLCY.greaterThan=" + SMALLER_COLLATERAL_FSV_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue equals to DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.equals=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue equals to UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.equals=" + UPDATED_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue not equals to DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.notEquals=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue not equals to UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.notEquals=" + UPDATED_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue in DEFAULT_COLLATERAL_DISCOUNTED_VALUE or UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound(
            "collateralDiscountedValue.in=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE + "," + UPDATED_COLLATERAL_DISCOUNTED_VALUE
        );

        // Get all the collateralInformationList where collateralDiscountedValue equals to UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.in=" + UPDATED_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue is not null
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.specified=true");

        // Get all the collateralInformationList where collateralDiscountedValue is null
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue is greater than or equal to DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.greaterThanOrEqual=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue is greater than or equal to UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.greaterThanOrEqual=" + UPDATED_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue is less than or equal to DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.lessThanOrEqual=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue is less than or equal to SMALLER_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.lessThanOrEqual=" + SMALLER_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue is less than DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.lessThan=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue is less than UPDATED_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.lessThan=" + UPDATED_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountedValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountedValue is greater than DEFAULT_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountedValue.greaterThan=" + DEFAULT_COLLATERAL_DISCOUNTED_VALUE);

        // Get all the collateralInformationList where collateralDiscountedValue is greater than SMALLER_COLLATERAL_DISCOUNTED_VALUE
        defaultCollateralInformationShouldBeFound("collateralDiscountedValue.greaterThan=" + SMALLER_COLLATERAL_DISCOUNTED_VALUE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged equals to DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.equals=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged equals to UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.equals=" + UPDATED_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged not equals to DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.notEquals=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged not equals to UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.notEquals=" + UPDATED_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged in DEFAULT_AMOUNT_CHARGED or UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.in=" + DEFAULT_AMOUNT_CHARGED + "," + UPDATED_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged equals to UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.in=" + UPDATED_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged is not null
        defaultCollateralInformationShouldBeFound("amountCharged.specified=true");

        // Get all the collateralInformationList where amountCharged is null
        defaultCollateralInformationShouldNotBeFound("amountCharged.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged is greater than or equal to DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.greaterThanOrEqual=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged is greater than or equal to UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.greaterThanOrEqual=" + UPDATED_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged is less than or equal to DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.lessThanOrEqual=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged is less than or equal to SMALLER_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.lessThanOrEqual=" + SMALLER_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged is less than DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.lessThan=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged is less than UPDATED_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.lessThan=" + UPDATED_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountChargedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountCharged is greater than DEFAULT_AMOUNT_CHARGED
        defaultCollateralInformationShouldNotBeFound("amountCharged.greaterThan=" + DEFAULT_AMOUNT_CHARGED);

        // Get all the collateralInformationList where amountCharged is greater than SMALLER_AMOUNT_CHARGED
        defaultCollateralInformationShouldBeFound("amountCharged.greaterThan=" + SMALLER_AMOUNT_CHARGED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate equals to DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.equals=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate equals to UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.equals=" + UPDATED_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate not equals to DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.notEquals=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate not equals to UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.notEquals=" + UPDATED_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate in DEFAULT_COLLATERAL_DISCOUNT_RATE or UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound(
            "collateralDiscountRate.in=" + DEFAULT_COLLATERAL_DISCOUNT_RATE + "," + UPDATED_COLLATERAL_DISCOUNT_RATE
        );

        // Get all the collateralInformationList where collateralDiscountRate equals to UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.in=" + UPDATED_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate is not null
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.specified=true");

        // Get all the collateralInformationList where collateralDiscountRate is null
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate is greater than or equal to DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.greaterThanOrEqual=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate is greater than or equal to UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.greaterThanOrEqual=" + UPDATED_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate is less than or equal to DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.lessThanOrEqual=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate is less than or equal to SMALLER_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.lessThanOrEqual=" + SMALLER_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate is less than DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.lessThan=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate is less than UPDATED_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.lessThan=" + UPDATED_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralDiscountRate is greater than DEFAULT_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldNotBeFound("collateralDiscountRate.greaterThan=" + DEFAULT_COLLATERAL_DISCOUNT_RATE);

        // Get all the collateralInformationList where collateralDiscountRate is greater than SMALLER_COLLATERAL_DISCOUNT_RATE
        defaultCollateralInformationShouldBeFound("collateralDiscountRate.greaterThan=" + SMALLER_COLLATERAL_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio equals to DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.equals=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio equals to UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.equals=" + UPDATED_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio not equals to DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.notEquals=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio not equals to UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.notEquals=" + UPDATED_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio in DEFAULT_LOAN_TO_VALUE_RATIO or UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.in=" + DEFAULT_LOAN_TO_VALUE_RATIO + "," + UPDATED_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio equals to UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.in=" + UPDATED_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio is not null
        defaultCollateralInformationShouldBeFound("loanToValueRatio.specified=true");

        // Get all the collateralInformationList where loanToValueRatio is null
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio is greater than or equal to DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.greaterThanOrEqual=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio is greater than or equal to UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.greaterThanOrEqual=" + UPDATED_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio is less than or equal to DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.lessThanOrEqual=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio is less than or equal to SMALLER_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.lessThanOrEqual=" + SMALLER_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio is less than DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.lessThan=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio is less than UPDATED_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.lessThan=" + UPDATED_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByLoanToValueRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where loanToValueRatio is greater than DEFAULT_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldNotBeFound("loanToValueRatio.greaterThan=" + DEFAULT_LOAN_TO_VALUE_RATIO);

        // Get all the collateralInformationList where loanToValueRatio is greater than SMALLER_LOAN_TO_VALUE_RATIO
        defaultCollateralInformationShouldBeFound("loanToValueRatio.greaterThan=" + SMALLER_LOAN_TO_VALUE_RATIO);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer equals to DEFAULT_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldBeFound("nameOfPropertyValuer.equals=" + DEFAULT_NAME_OF_PROPERTY_VALUER);

        // Get all the collateralInformationList where nameOfPropertyValuer equals to UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.equals=" + UPDATED_NAME_OF_PROPERTY_VALUER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer not equals to DEFAULT_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.notEquals=" + DEFAULT_NAME_OF_PROPERTY_VALUER);

        // Get all the collateralInformationList where nameOfPropertyValuer not equals to UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldBeFound("nameOfPropertyValuer.notEquals=" + UPDATED_NAME_OF_PROPERTY_VALUER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer in DEFAULT_NAME_OF_PROPERTY_VALUER or UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldBeFound(
            "nameOfPropertyValuer.in=" + DEFAULT_NAME_OF_PROPERTY_VALUER + "," + UPDATED_NAME_OF_PROPERTY_VALUER
        );

        // Get all the collateralInformationList where nameOfPropertyValuer equals to UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.in=" + UPDATED_NAME_OF_PROPERTY_VALUER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer is not null
        defaultCollateralInformationShouldBeFound("nameOfPropertyValuer.specified=true");

        // Get all the collateralInformationList where nameOfPropertyValuer is null
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer contains DEFAULT_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldBeFound("nameOfPropertyValuer.contains=" + DEFAULT_NAME_OF_PROPERTY_VALUER);

        // Get all the collateralInformationList where nameOfPropertyValuer contains UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.contains=" + UPDATED_NAME_OF_PROPERTY_VALUER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfPropertyValuerNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfPropertyValuer does not contain DEFAULT_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldNotBeFound("nameOfPropertyValuer.doesNotContain=" + DEFAULT_NAME_OF_PROPERTY_VALUER);

        // Get all the collateralInformationList where nameOfPropertyValuer does not contain UPDATED_NAME_OF_PROPERTY_VALUER
        defaultCollateralInformationShouldBeFound("nameOfPropertyValuer.doesNotContain=" + UPDATED_NAME_OF_PROPERTY_VALUER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate equals to DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.equals=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE);

        // Get all the collateralInformationList where collateralLastValuationDate equals to UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.equals=" + UPDATED_COLLATERAL_LAST_VALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate not equals to DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.notEquals=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE);

        // Get all the collateralInformationList where collateralLastValuationDate not equals to UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.notEquals=" + UPDATED_COLLATERAL_LAST_VALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate in DEFAULT_COLLATERAL_LAST_VALUATION_DATE or UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound(
            "collateralLastValuationDate.in=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE + "," + UPDATED_COLLATERAL_LAST_VALUATION_DATE
        );

        // Get all the collateralInformationList where collateralLastValuationDate equals to UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.in=" + UPDATED_COLLATERAL_LAST_VALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate is not null
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.specified=true");

        // Get all the collateralInformationList where collateralLastValuationDate is null
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate is greater than or equal to DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound(
            "collateralLastValuationDate.greaterThanOrEqual=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        );

        // Get all the collateralInformationList where collateralLastValuationDate is greater than or equal to UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound(
            "collateralLastValuationDate.greaterThanOrEqual=" + UPDATED_COLLATERAL_LAST_VALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate is less than or equal to DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.lessThanOrEqual=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE);

        // Get all the collateralInformationList where collateralLastValuationDate is less than or equal to SMALLER_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound(
            "collateralLastValuationDate.lessThanOrEqual=" + SMALLER_COLLATERAL_LAST_VALUATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate is less than DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.lessThan=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE);

        // Get all the collateralInformationList where collateralLastValuationDate is less than UPDATED_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.lessThan=" + UPDATED_COLLATERAL_LAST_VALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralLastValuationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where collateralLastValuationDate is greater than DEFAULT_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldNotBeFound("collateralLastValuationDate.greaterThan=" + DEFAULT_COLLATERAL_LAST_VALUATION_DATE);

        // Get all the collateralInformationList where collateralLastValuationDate is greater than SMALLER_COLLATERAL_LAST_VALUATION_DATE
        defaultCollateralInformationShouldBeFound("collateralLastValuationDate.greaterThan=" + SMALLER_COLLATERAL_LAST_VALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuredFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuredFlag equals to DEFAULT_INSURED_FLAG
        defaultCollateralInformationShouldBeFound("insuredFlag.equals=" + DEFAULT_INSURED_FLAG);

        // Get all the collateralInformationList where insuredFlag equals to UPDATED_INSURED_FLAG
        defaultCollateralInformationShouldNotBeFound("insuredFlag.equals=" + UPDATED_INSURED_FLAG);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuredFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuredFlag not equals to DEFAULT_INSURED_FLAG
        defaultCollateralInformationShouldNotBeFound("insuredFlag.notEquals=" + DEFAULT_INSURED_FLAG);

        // Get all the collateralInformationList where insuredFlag not equals to UPDATED_INSURED_FLAG
        defaultCollateralInformationShouldBeFound("insuredFlag.notEquals=" + UPDATED_INSURED_FLAG);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuredFlagIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuredFlag in DEFAULT_INSURED_FLAG or UPDATED_INSURED_FLAG
        defaultCollateralInformationShouldBeFound("insuredFlag.in=" + DEFAULT_INSURED_FLAG + "," + UPDATED_INSURED_FLAG);

        // Get all the collateralInformationList where insuredFlag equals to UPDATED_INSURED_FLAG
        defaultCollateralInformationShouldNotBeFound("insuredFlag.in=" + UPDATED_INSURED_FLAG);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuredFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuredFlag is not null
        defaultCollateralInformationShouldBeFound("insuredFlag.specified=true");

        // Get all the collateralInformationList where insuredFlag is null
        defaultCollateralInformationShouldNotBeFound("insuredFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer equals to DEFAULT_NAME_OF_INSURER
        defaultCollateralInformationShouldBeFound("nameOfInsurer.equals=" + DEFAULT_NAME_OF_INSURER);

        // Get all the collateralInformationList where nameOfInsurer equals to UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.equals=" + UPDATED_NAME_OF_INSURER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer not equals to DEFAULT_NAME_OF_INSURER
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.notEquals=" + DEFAULT_NAME_OF_INSURER);

        // Get all the collateralInformationList where nameOfInsurer not equals to UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldBeFound("nameOfInsurer.notEquals=" + UPDATED_NAME_OF_INSURER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer in DEFAULT_NAME_OF_INSURER or UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldBeFound("nameOfInsurer.in=" + DEFAULT_NAME_OF_INSURER + "," + UPDATED_NAME_OF_INSURER);

        // Get all the collateralInformationList where nameOfInsurer equals to UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.in=" + UPDATED_NAME_OF_INSURER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer is not null
        defaultCollateralInformationShouldBeFound("nameOfInsurer.specified=true");

        // Get all the collateralInformationList where nameOfInsurer is null
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer contains DEFAULT_NAME_OF_INSURER
        defaultCollateralInformationShouldBeFound("nameOfInsurer.contains=" + DEFAULT_NAME_OF_INSURER);

        // Get all the collateralInformationList where nameOfInsurer contains UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.contains=" + UPDATED_NAME_OF_INSURER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByNameOfInsurerNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where nameOfInsurer does not contain DEFAULT_NAME_OF_INSURER
        defaultCollateralInformationShouldNotBeFound("nameOfInsurer.doesNotContain=" + DEFAULT_NAME_OF_INSURER);

        // Get all the collateralInformationList where nameOfInsurer does not contain UPDATED_NAME_OF_INSURER
        defaultCollateralInformationShouldBeFound("nameOfInsurer.doesNotContain=" + UPDATED_NAME_OF_INSURER);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured equals to DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.equals=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured equals to UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.equals=" + UPDATED_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured not equals to DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.notEquals=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured not equals to UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.notEquals=" + UPDATED_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured in DEFAULT_AMOUNT_INSURED or UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.in=" + DEFAULT_AMOUNT_INSURED + "," + UPDATED_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured equals to UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.in=" + UPDATED_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured is not null
        defaultCollateralInformationShouldBeFound("amountInsured.specified=true");

        // Get all the collateralInformationList where amountInsured is null
        defaultCollateralInformationShouldNotBeFound("amountInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured is greater than or equal to DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.greaterThanOrEqual=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured is greater than or equal to UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.greaterThanOrEqual=" + UPDATED_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured is less than or equal to DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.lessThanOrEqual=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured is less than or equal to SMALLER_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.lessThanOrEqual=" + SMALLER_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured is less than DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.lessThan=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured is less than UPDATED_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.lessThan=" + UPDATED_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByAmountInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where amountInsured is greater than DEFAULT_AMOUNT_INSURED
        defaultCollateralInformationShouldNotBeFound("amountInsured.greaterThan=" + DEFAULT_AMOUNT_INSURED);

        // Get all the collateralInformationList where amountInsured is greater than SMALLER_AMOUNT_INSURED
        defaultCollateralInformationShouldBeFound("amountInsured.greaterThan=" + SMALLER_AMOUNT_INSURED);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate equals to DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.equals=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate equals to UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.equals=" + UPDATED_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate not equals to DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.notEquals=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate not equals to UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.notEquals=" + UPDATED_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate in DEFAULT_INSURANCE_EXPIRY_DATE or UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound(
            "insuranceExpiryDate.in=" + DEFAULT_INSURANCE_EXPIRY_DATE + "," + UPDATED_INSURANCE_EXPIRY_DATE
        );

        // Get all the collateralInformationList where insuranceExpiryDate equals to UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.in=" + UPDATED_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate is not null
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.specified=true");

        // Get all the collateralInformationList where insuranceExpiryDate is null
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate is greater than or equal to DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.greaterThanOrEqual=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate is greater than or equal to UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.greaterThanOrEqual=" + UPDATED_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate is less than or equal to DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.lessThanOrEqual=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate is less than or equal to SMALLER_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.lessThanOrEqual=" + SMALLER_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate is less than DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.lessThan=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate is less than UPDATED_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.lessThan=" + UPDATED_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByInsuranceExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where insuranceExpiryDate is greater than DEFAULT_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldNotBeFound("insuranceExpiryDate.greaterThan=" + DEFAULT_INSURANCE_EXPIRY_DATE);

        // Get all the collateralInformationList where insuranceExpiryDate is greater than SMALLER_INSURANCE_EXPIRY_DATE
        defaultCollateralInformationShouldBeFound("insuranceExpiryDate.greaterThan=" + SMALLER_INSURANCE_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers equals to DEFAULT_GUARANTEE_INSURERS
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.equals=" + DEFAULT_GUARANTEE_INSURERS);

        // Get all the collateralInformationList where guaranteeInsurers equals to UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.equals=" + UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers not equals to DEFAULT_GUARANTEE_INSURERS
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.notEquals=" + DEFAULT_GUARANTEE_INSURERS);

        // Get all the collateralInformationList where guaranteeInsurers not equals to UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.notEquals=" + UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersIsInShouldWork() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers in DEFAULT_GUARANTEE_INSURERS or UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.in=" + DEFAULT_GUARANTEE_INSURERS + "," + UPDATED_GUARANTEE_INSURERS);

        // Get all the collateralInformationList where guaranteeInsurers equals to UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.in=" + UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersIsNullOrNotNull() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers is not null
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.specified=true");

        // Get all the collateralInformationList where guaranteeInsurers is null
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.specified=false");
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers contains DEFAULT_GUARANTEE_INSURERS
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.contains=" + DEFAULT_GUARANTEE_INSURERS);

        // Get all the collateralInformationList where guaranteeInsurers contains UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.contains=" + UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByGuaranteeInsurersNotContainsSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        // Get all the collateralInformationList where guaranteeInsurers does not contain DEFAULT_GUARANTEE_INSURERS
        defaultCollateralInformationShouldNotBeFound("guaranteeInsurers.doesNotContain=" + DEFAULT_GUARANTEE_INSURERS);

        // Get all the collateralInformationList where guaranteeInsurers does not contain UPDATED_GUARANTEE_INSURERS
        defaultCollateralInformationShouldBeFound("guaranteeInsurers.doesNotContain=" + UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);
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
        collateralInformation.setBankCode(bankCode);
        collateralInformationRepository.saveAndFlush(collateralInformation);
        Long bankCodeId = bankCode.getId();

        // Get all the collateralInformationList where bankCode equals to bankCodeId
        defaultCollateralInformationShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the collateralInformationList where bankCode equals to (bankCodeId + 1)
        defaultCollateralInformationShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);
        BankBranchCode branchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchCode);
            em.flush();
        } else {
            branchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchCode);
        em.flush();
        collateralInformation.setBranchCode(branchCode);
        collateralInformationRepository.saveAndFlush(collateralInformation);
        Long branchCodeId = branchCode.getId();

        // Get all the collateralInformationList where branchCode equals to branchCodeId
        defaultCollateralInformationShouldBeFound("branchCodeId.equals=" + branchCodeId);

        // Get all the collateralInformationList where branchCode equals to (branchCodeId + 1)
        defaultCollateralInformationShouldNotBeFound("branchCodeId.equals=" + (branchCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCollateralTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);
        CollateralType collateralType;
        if (TestUtil.findAll(em, CollateralType.class).isEmpty()) {
            collateralType = CollateralTypeResourceIT.createEntity(em);
            em.persist(collateralType);
            em.flush();
        } else {
            collateralType = TestUtil.findAll(em, CollateralType.class).get(0);
        }
        em.persist(collateralType);
        em.flush();
        collateralInformation.setCollateralType(collateralType);
        collateralInformationRepository.saveAndFlush(collateralInformation);
        Long collateralTypeId = collateralType.getId();

        // Get all the collateralInformationList where collateralType equals to collateralTypeId
        defaultCollateralInformationShouldBeFound("collateralTypeId.equals=" + collateralTypeId);

        // Get all the collateralInformationList where collateralType equals to (collateralTypeId + 1)
        defaultCollateralInformationShouldNotBeFound("collateralTypeId.equals=" + (collateralTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCollateralInformationsByCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);
        CountySubCountyCode countyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(countyCode);
            em.flush();
        } else {
            countyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        em.persist(countyCode);
        em.flush();
        collateralInformation.setCountyCode(countyCode);
        collateralInformationRepository.saveAndFlush(collateralInformation);
        Long countyCodeId = countyCode.getId();

        // Get all the collateralInformationList where countyCode equals to countyCodeId
        defaultCollateralInformationShouldBeFound("countyCodeId.equals=" + countyCodeId);

        // Get all the collateralInformationList where countyCode equals to (countyCodeId + 1)
        defaultCollateralInformationShouldNotBeFound("countyCodeId.equals=" + (countyCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollateralInformationShouldBeFound(String filter) throws Exception {
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].collateralId").value(hasItem(DEFAULT_COLLATERAL_ID)))
            .andExpect(jsonPath("$.[*].loanContractId").value(hasItem(DEFAULT_LOAN_CONTRACT_ID)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].registrationPropertyNumber").value(hasItem(DEFAULT_REGISTRATION_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].collateralOMVInCCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_OMV_IN_CCY))))
            .andExpect(jsonPath("$.[*].collateralFSVInLCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_FSV_IN_LCY))))
            .andExpect(jsonPath("$.[*].collateralDiscountedValue").value(hasItem(sameNumber(DEFAULT_COLLATERAL_DISCOUNTED_VALUE))))
            .andExpect(jsonPath("$.[*].amountCharged").value(hasItem(sameNumber(DEFAULT_AMOUNT_CHARGED))))
            .andExpect(jsonPath("$.[*].collateralDiscountRate").value(hasItem(DEFAULT_COLLATERAL_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanToValueRatio").value(hasItem(DEFAULT_LOAN_TO_VALUE_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].nameOfPropertyValuer").value(hasItem(DEFAULT_NAME_OF_PROPERTY_VALUER)))
            .andExpect(jsonPath("$.[*].collateralLastValuationDate").value(hasItem(DEFAULT_COLLATERAL_LAST_VALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].insuredFlag").value(hasItem(DEFAULT_INSURED_FLAG.toString())))
            .andExpect(jsonPath("$.[*].nameOfInsurer").value(hasItem(DEFAULT_NAME_OF_INSURER)))
            .andExpect(jsonPath("$.[*].amountInsured").value(hasItem(sameNumber(DEFAULT_AMOUNT_INSURED))))
            .andExpect(jsonPath("$.[*].insuranceExpiryDate").value(hasItem(DEFAULT_INSURANCE_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].guaranteeInsurers").value(hasItem(DEFAULT_GUARANTEE_INSURERS)));

        // Check, that the count call also returns 1
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollateralInformationShouldNotBeFound(String filter) throws Exception {
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollateralInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollateralInformation() throws Exception {
        // Get the collateralInformation
        restCollateralInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollateralInformation() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();

        // Update the collateralInformation
        CollateralInformation updatedCollateralInformation = collateralInformationRepository.findById(collateralInformation.getId()).get();
        // Disconnect from session so that the updates on updatedCollateralInformation are not directly saved in db
        em.detach(updatedCollateralInformation);
        updatedCollateralInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .collateralId(UPDATED_COLLATERAL_ID)
            .loanContractId(UPDATED_LOAN_CONTRACT_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .registrationPropertyNumber(UPDATED_REGISTRATION_PROPERTY_NUMBER)
            .collateralOMVInCCY(UPDATED_COLLATERAL_OMV_IN_CCY)
            .collateralFSVInLCY(UPDATED_COLLATERAL_FSV_IN_LCY)
            .collateralDiscountedValue(UPDATED_COLLATERAL_DISCOUNTED_VALUE)
            .amountCharged(UPDATED_AMOUNT_CHARGED)
            .collateralDiscountRate(UPDATED_COLLATERAL_DISCOUNT_RATE)
            .loanToValueRatio(UPDATED_LOAN_TO_VALUE_RATIO)
            .nameOfPropertyValuer(UPDATED_NAME_OF_PROPERTY_VALUER)
            .collateralLastValuationDate(UPDATED_COLLATERAL_LAST_VALUATION_DATE)
            .insuredFlag(UPDATED_INSURED_FLAG)
            .nameOfInsurer(UPDATED_NAME_OF_INSURER)
            .amountInsured(UPDATED_AMOUNT_INSURED)
            .insuranceExpiryDate(UPDATED_INSURANCE_EXPIRY_DATE)
            .guaranteeInsurers(UPDATED_GUARANTEE_INSURERS);
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(updatedCollateralInformation);

        restCollateralInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collateralInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);
        CollateralInformation testCollateralInformation = collateralInformationList.get(collateralInformationList.size() - 1);
        assertThat(testCollateralInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCollateralInformation.getCollateralId()).isEqualTo(UPDATED_COLLATERAL_ID);
        assertThat(testCollateralInformation.getLoanContractId()).isEqualTo(UPDATED_LOAN_CONTRACT_ID);
        assertThat(testCollateralInformation.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCollateralInformation.getRegistrationPropertyNumber()).isEqualTo(UPDATED_REGISTRATION_PROPERTY_NUMBER);
        assertThat(testCollateralInformation.getCollateralOMVInCCY()).isEqualTo(UPDATED_COLLATERAL_OMV_IN_CCY);
        assertThat(testCollateralInformation.getCollateralFSVInLCY()).isEqualTo(UPDATED_COLLATERAL_FSV_IN_LCY);
        assertThat(testCollateralInformation.getCollateralDiscountedValue()).isEqualTo(UPDATED_COLLATERAL_DISCOUNTED_VALUE);
        assertThat(testCollateralInformation.getAmountCharged()).isEqualTo(UPDATED_AMOUNT_CHARGED);
        assertThat(testCollateralInformation.getCollateralDiscountRate()).isEqualTo(UPDATED_COLLATERAL_DISCOUNT_RATE);
        assertThat(testCollateralInformation.getLoanToValueRatio()).isEqualTo(UPDATED_LOAN_TO_VALUE_RATIO);
        assertThat(testCollateralInformation.getNameOfPropertyValuer()).isEqualTo(UPDATED_NAME_OF_PROPERTY_VALUER);
        assertThat(testCollateralInformation.getCollateralLastValuationDate()).isEqualTo(UPDATED_COLLATERAL_LAST_VALUATION_DATE);
        assertThat(testCollateralInformation.getInsuredFlag()).isEqualTo(UPDATED_INSURED_FLAG);
        assertThat(testCollateralInformation.getNameOfInsurer()).isEqualTo(UPDATED_NAME_OF_INSURER);
        assertThat(testCollateralInformation.getAmountInsured()).isEqualTo(UPDATED_AMOUNT_INSURED);
        assertThat(testCollateralInformation.getInsuranceExpiryDate()).isEqualTo(UPDATED_INSURANCE_EXPIRY_DATE);
        assertThat(testCollateralInformation.getGuaranteeInsurers()).isEqualTo(UPDATED_GUARANTEE_INSURERS);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository).save(testCollateralInformation);
    }

    @Test
    @Transactional
    void putNonExistingCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collateralInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void partialUpdateCollateralInformationWithPatch() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();

        // Update the collateralInformation using partial update
        CollateralInformation partialUpdatedCollateralInformation = new CollateralInformation();
        partialUpdatedCollateralInformation.setId(collateralInformation.getId());

        partialUpdatedCollateralInformation
            .collateralId(UPDATED_COLLATERAL_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .registrationPropertyNumber(UPDATED_REGISTRATION_PROPERTY_NUMBER)
            .collateralOMVInCCY(UPDATED_COLLATERAL_OMV_IN_CCY)
            .collateralFSVInLCY(UPDATED_COLLATERAL_FSV_IN_LCY)
            .collateralDiscountedValue(UPDATED_COLLATERAL_DISCOUNTED_VALUE)
            .amountCharged(UPDATED_AMOUNT_CHARGED)
            .collateralDiscountRate(UPDATED_COLLATERAL_DISCOUNT_RATE)
            .insuredFlag(UPDATED_INSURED_FLAG)
            .amountInsured(UPDATED_AMOUNT_INSURED)
            .guaranteeInsurers(UPDATED_GUARANTEE_INSURERS);

        restCollateralInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollateralInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollateralInformation))
            )
            .andExpect(status().isOk());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);
        CollateralInformation testCollateralInformation = collateralInformationList.get(collateralInformationList.size() - 1);
        assertThat(testCollateralInformation.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCollateralInformation.getCollateralId()).isEqualTo(UPDATED_COLLATERAL_ID);
        assertThat(testCollateralInformation.getLoanContractId()).isEqualTo(DEFAULT_LOAN_CONTRACT_ID);
        assertThat(testCollateralInformation.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCollateralInformation.getRegistrationPropertyNumber()).isEqualTo(UPDATED_REGISTRATION_PROPERTY_NUMBER);
        assertThat(testCollateralInformation.getCollateralOMVInCCY()).isEqualByComparingTo(UPDATED_COLLATERAL_OMV_IN_CCY);
        assertThat(testCollateralInformation.getCollateralFSVInLCY()).isEqualByComparingTo(UPDATED_COLLATERAL_FSV_IN_LCY);
        assertThat(testCollateralInformation.getCollateralDiscountedValue()).isEqualByComparingTo(UPDATED_COLLATERAL_DISCOUNTED_VALUE);
        assertThat(testCollateralInformation.getAmountCharged()).isEqualByComparingTo(UPDATED_AMOUNT_CHARGED);
        assertThat(testCollateralInformation.getCollateralDiscountRate()).isEqualTo(UPDATED_COLLATERAL_DISCOUNT_RATE);
        assertThat(testCollateralInformation.getLoanToValueRatio()).isEqualTo(DEFAULT_LOAN_TO_VALUE_RATIO);
        assertThat(testCollateralInformation.getNameOfPropertyValuer()).isEqualTo(DEFAULT_NAME_OF_PROPERTY_VALUER);
        assertThat(testCollateralInformation.getCollateralLastValuationDate()).isEqualTo(DEFAULT_COLLATERAL_LAST_VALUATION_DATE);
        assertThat(testCollateralInformation.getInsuredFlag()).isEqualTo(UPDATED_INSURED_FLAG);
        assertThat(testCollateralInformation.getNameOfInsurer()).isEqualTo(DEFAULT_NAME_OF_INSURER);
        assertThat(testCollateralInformation.getAmountInsured()).isEqualByComparingTo(UPDATED_AMOUNT_INSURED);
        assertThat(testCollateralInformation.getInsuranceExpiryDate()).isEqualTo(DEFAULT_INSURANCE_EXPIRY_DATE);
        assertThat(testCollateralInformation.getGuaranteeInsurers()).isEqualTo(UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void fullUpdateCollateralInformationWithPatch() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();

        // Update the collateralInformation using partial update
        CollateralInformation partialUpdatedCollateralInformation = new CollateralInformation();
        partialUpdatedCollateralInformation.setId(collateralInformation.getId());

        partialUpdatedCollateralInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .collateralId(UPDATED_COLLATERAL_ID)
            .loanContractId(UPDATED_LOAN_CONTRACT_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .registrationPropertyNumber(UPDATED_REGISTRATION_PROPERTY_NUMBER)
            .collateralOMVInCCY(UPDATED_COLLATERAL_OMV_IN_CCY)
            .collateralFSVInLCY(UPDATED_COLLATERAL_FSV_IN_LCY)
            .collateralDiscountedValue(UPDATED_COLLATERAL_DISCOUNTED_VALUE)
            .amountCharged(UPDATED_AMOUNT_CHARGED)
            .collateralDiscountRate(UPDATED_COLLATERAL_DISCOUNT_RATE)
            .loanToValueRatio(UPDATED_LOAN_TO_VALUE_RATIO)
            .nameOfPropertyValuer(UPDATED_NAME_OF_PROPERTY_VALUER)
            .collateralLastValuationDate(UPDATED_COLLATERAL_LAST_VALUATION_DATE)
            .insuredFlag(UPDATED_INSURED_FLAG)
            .nameOfInsurer(UPDATED_NAME_OF_INSURER)
            .amountInsured(UPDATED_AMOUNT_INSURED)
            .insuranceExpiryDate(UPDATED_INSURANCE_EXPIRY_DATE)
            .guaranteeInsurers(UPDATED_GUARANTEE_INSURERS);

        restCollateralInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollateralInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollateralInformation))
            )
            .andExpect(status().isOk());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);
        CollateralInformation testCollateralInformation = collateralInformationList.get(collateralInformationList.size() - 1);
        assertThat(testCollateralInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCollateralInformation.getCollateralId()).isEqualTo(UPDATED_COLLATERAL_ID);
        assertThat(testCollateralInformation.getLoanContractId()).isEqualTo(UPDATED_LOAN_CONTRACT_ID);
        assertThat(testCollateralInformation.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCollateralInformation.getRegistrationPropertyNumber()).isEqualTo(UPDATED_REGISTRATION_PROPERTY_NUMBER);
        assertThat(testCollateralInformation.getCollateralOMVInCCY()).isEqualByComparingTo(UPDATED_COLLATERAL_OMV_IN_CCY);
        assertThat(testCollateralInformation.getCollateralFSVInLCY()).isEqualByComparingTo(UPDATED_COLLATERAL_FSV_IN_LCY);
        assertThat(testCollateralInformation.getCollateralDiscountedValue()).isEqualByComparingTo(UPDATED_COLLATERAL_DISCOUNTED_VALUE);
        assertThat(testCollateralInformation.getAmountCharged()).isEqualByComparingTo(UPDATED_AMOUNT_CHARGED);
        assertThat(testCollateralInformation.getCollateralDiscountRate()).isEqualTo(UPDATED_COLLATERAL_DISCOUNT_RATE);
        assertThat(testCollateralInformation.getLoanToValueRatio()).isEqualTo(UPDATED_LOAN_TO_VALUE_RATIO);
        assertThat(testCollateralInformation.getNameOfPropertyValuer()).isEqualTo(UPDATED_NAME_OF_PROPERTY_VALUER);
        assertThat(testCollateralInformation.getCollateralLastValuationDate()).isEqualTo(UPDATED_COLLATERAL_LAST_VALUATION_DATE);
        assertThat(testCollateralInformation.getInsuredFlag()).isEqualTo(UPDATED_INSURED_FLAG);
        assertThat(testCollateralInformation.getNameOfInsurer()).isEqualTo(UPDATED_NAME_OF_INSURER);
        assertThat(testCollateralInformation.getAmountInsured()).isEqualByComparingTo(UPDATED_AMOUNT_INSURED);
        assertThat(testCollateralInformation.getInsuranceExpiryDate()).isEqualTo(UPDATED_INSURANCE_EXPIRY_DATE);
        assertThat(testCollateralInformation.getGuaranteeInsurers()).isEqualTo(UPDATED_GUARANTEE_INSURERS);
    }

    @Test
    @Transactional
    void patchNonExistingCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collateralInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollateralInformation() throws Exception {
        int databaseSizeBeforeUpdate = collateralInformationRepository.findAll().size();
        collateralInformation.setId(count.incrementAndGet());

        // Create the CollateralInformation
        CollateralInformationDTO collateralInformationDTO = collateralInformationMapper.toDto(collateralInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollateralInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collateralInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollateralInformation in the database
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(0)).save(collateralInformation);
    }

    @Test
    @Transactional
    void deleteCollateralInformation() throws Exception {
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);

        int databaseSizeBeforeDelete = collateralInformationRepository.findAll().size();

        // Delete the collateralInformation
        restCollateralInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, collateralInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollateralInformation> collateralInformationList = collateralInformationRepository.findAll();
        assertThat(collateralInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CollateralInformation in Elasticsearch
        verify(mockCollateralInformationSearchRepository, times(1)).deleteById(collateralInformation.getId());
    }

    @Test
    @Transactional
    void searchCollateralInformation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        collateralInformationRepository.saveAndFlush(collateralInformation);
        when(mockCollateralInformationSearchRepository.search("id:" + collateralInformation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(collateralInformation), PageRequest.of(0, 1), 1));

        // Search the collateralInformation
        restCollateralInformationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + collateralInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collateralInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].collateralId").value(hasItem(DEFAULT_COLLATERAL_ID)))
            .andExpect(jsonPath("$.[*].loanContractId").value(hasItem(DEFAULT_LOAN_CONTRACT_ID)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].registrationPropertyNumber").value(hasItem(DEFAULT_REGISTRATION_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].collateralOMVInCCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_OMV_IN_CCY))))
            .andExpect(jsonPath("$.[*].collateralFSVInLCY").value(hasItem(sameNumber(DEFAULT_COLLATERAL_FSV_IN_LCY))))
            .andExpect(jsonPath("$.[*].collateralDiscountedValue").value(hasItem(sameNumber(DEFAULT_COLLATERAL_DISCOUNTED_VALUE))))
            .andExpect(jsonPath("$.[*].amountCharged").value(hasItem(sameNumber(DEFAULT_AMOUNT_CHARGED))))
            .andExpect(jsonPath("$.[*].collateralDiscountRate").value(hasItem(DEFAULT_COLLATERAL_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanToValueRatio").value(hasItem(DEFAULT_LOAN_TO_VALUE_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].nameOfPropertyValuer").value(hasItem(DEFAULT_NAME_OF_PROPERTY_VALUER)))
            .andExpect(jsonPath("$.[*].collateralLastValuationDate").value(hasItem(DEFAULT_COLLATERAL_LAST_VALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].insuredFlag").value(hasItem(DEFAULT_INSURED_FLAG.toString())))
            .andExpect(jsonPath("$.[*].nameOfInsurer").value(hasItem(DEFAULT_NAME_OF_INSURER)))
            .andExpect(jsonPath("$.[*].amountInsured").value(hasItem(sameNumber(DEFAULT_AMOUNT_INSURED))))
            .andExpect(jsonPath("$.[*].insuranceExpiryDate").value(hasItem(DEFAULT_INSURANCE_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].guaranteeInsurers").value(hasItem(DEFAULT_GUARANTEE_INSURERS)));
    }
}
