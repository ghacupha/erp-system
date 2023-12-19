package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.ParticularsOfOutletRepository;
import io.github.erp.repository.search.ParticularsOfOutletSearchRepository;
import io.github.erp.service.dto.ParticularsOfOutletDTO;
import io.github.erp.service.mapper.ParticularsOfOutletMapper;
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
 * Integration tests for the {@link ParticularsOfOutletResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class ParticularsOfOutletResourceIT {

    private static final LocalDate DEFAULT_BUSINESS_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BUSINESS_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BUSINESS_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OUTLET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final Double DEFAULT_ISO_6709_LATITUTE = 1D;
    private static final Double UPDATED_ISO_6709_LATITUTE = 2D;
    private static final Double SMALLER_ISO_6709_LATITUTE = 1D - 1D;

    private static final Double DEFAULT_ISO_6709_LONGITUDE = 1D;
    private static final Double UPDATED_ISO_6709_LONGITUDE = 2D;
    private static final Double SMALLER_ISO_6709_LONGITUDE = 1D - 1D;

    private static final LocalDate DEFAULT_CBK_APPROVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CBK_APPROVAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CBK_APPROVAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_OUTLET_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OUTLET_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OUTLET_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_OUTLET_CLOSURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OUTLET_CLOSURE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OUTLET_CLOSURE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LICENSE_FEE_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LICENSE_FEE_PAYABLE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LICENSE_FEE_PAYABLE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/granular-data/particulars-of-outlets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/particulars-of-outlets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParticularsOfOutletRepository particularsOfOutletRepository;

    @Autowired
    private ParticularsOfOutletMapper particularsOfOutletMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ParticularsOfOutletSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParticularsOfOutletSearchRepository mockParticularsOfOutletSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticularsOfOutletMockMvc;

    private ParticularsOfOutlet particularsOfOutlet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticularsOfOutlet createEntity(EntityManager em) {
        ParticularsOfOutlet particularsOfOutlet = new ParticularsOfOutlet()
            .businessReportingDate(DEFAULT_BUSINESS_REPORTING_DATE)
            .outletName(DEFAULT_OUTLET_NAME)
            .town(DEFAULT_TOWN)
            .iso6709Latitute(DEFAULT_ISO_6709_LATITUTE)
            .iso6709Longitude(DEFAULT_ISO_6709_LONGITUDE)
            .cbkApprovalDate(DEFAULT_CBK_APPROVAL_DATE)
            .outletOpeningDate(DEFAULT_OUTLET_OPENING_DATE)
            .outletClosureDate(DEFAULT_OUTLET_CLOSURE_DATE)
            .licenseFeePayable(DEFAULT_LICENSE_FEE_PAYABLE);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        particularsOfOutlet.setSubCountyCode(countySubCountyCode);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        particularsOfOutlet.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        particularsOfOutlet.setOutletId(bankBranchCode);
        // Add required entity
        OutletType outletType;
        if (TestUtil.findAll(em, OutletType.class).isEmpty()) {
            outletType = OutletTypeResourceIT.createEntity(em);
            em.persist(outletType);
            em.flush();
        } else {
            outletType = TestUtil.findAll(em, OutletType.class).get(0);
        }
        particularsOfOutlet.setTypeOfOutlet(outletType);
        // Add required entity
        OutletStatus outletStatus;
        if (TestUtil.findAll(em, OutletStatus.class).isEmpty()) {
            outletStatus = OutletStatusResourceIT.createEntity(em);
            em.persist(outletStatus);
            em.flush();
        } else {
            outletStatus = TestUtil.findAll(em, OutletStatus.class).get(0);
        }
        particularsOfOutlet.setOutletStatus(outletStatus);
        return particularsOfOutlet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParticularsOfOutlet createUpdatedEntity(EntityManager em) {
        ParticularsOfOutlet particularsOfOutlet = new ParticularsOfOutlet()
            .businessReportingDate(UPDATED_BUSINESS_REPORTING_DATE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .cbkApprovalDate(UPDATED_CBK_APPROVAL_DATE)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createUpdatedEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        particularsOfOutlet.setSubCountyCode(countySubCountyCode);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        particularsOfOutlet.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        particularsOfOutlet.setOutletId(bankBranchCode);
        // Add required entity
        OutletType outletType;
        if (TestUtil.findAll(em, OutletType.class).isEmpty()) {
            outletType = OutletTypeResourceIT.createUpdatedEntity(em);
            em.persist(outletType);
            em.flush();
        } else {
            outletType = TestUtil.findAll(em, OutletType.class).get(0);
        }
        particularsOfOutlet.setTypeOfOutlet(outletType);
        // Add required entity
        OutletStatus outletStatus;
        if (TestUtil.findAll(em, OutletStatus.class).isEmpty()) {
            outletStatus = OutletStatusResourceIT.createUpdatedEntity(em);
            em.persist(outletStatus);
            em.flush();
        } else {
            outletStatus = TestUtil.findAll(em, OutletStatus.class).get(0);
        }
        particularsOfOutlet.setOutletStatus(outletStatus);
        return particularsOfOutlet;
    }

    @BeforeEach
    public void initTest() {
        particularsOfOutlet = createEntity(em);
    }

    @Test
    @Transactional
    void createParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeCreate = particularsOfOutletRepository.findAll().size();
        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);
        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeCreate + 1);
        ParticularsOfOutlet testParticularsOfOutlet = particularsOfOutletList.get(particularsOfOutletList.size() - 1);
        assertThat(testParticularsOfOutlet.getBusinessReportingDate()).isEqualTo(DEFAULT_BUSINESS_REPORTING_DATE);
        assertThat(testParticularsOfOutlet.getOutletName()).isEqualTo(DEFAULT_OUTLET_NAME);
        assertThat(testParticularsOfOutlet.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testParticularsOfOutlet.getIso6709Latitute()).isEqualTo(DEFAULT_ISO_6709_LATITUTE);
        assertThat(testParticularsOfOutlet.getIso6709Longitude()).isEqualTo(DEFAULT_ISO_6709_LONGITUDE);
        assertThat(testParticularsOfOutlet.getCbkApprovalDate()).isEqualTo(DEFAULT_CBK_APPROVAL_DATE);
        assertThat(testParticularsOfOutlet.getOutletOpeningDate()).isEqualTo(DEFAULT_OUTLET_OPENING_DATE);
        assertThat(testParticularsOfOutlet.getOutletClosureDate()).isEqualTo(DEFAULT_OUTLET_CLOSURE_DATE);
        assertThat(testParticularsOfOutlet.getLicenseFeePayable()).isEqualByComparingTo(DEFAULT_LICENSE_FEE_PAYABLE);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(1)).save(testParticularsOfOutlet);
    }

    @Test
    @Transactional
    void createParticularsOfOutletWithExistingId() throws Exception {
        // Create the ParticularsOfOutlet with an existing ID
        particularsOfOutlet.setId(1L);
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        int databaseSizeBeforeCreate = particularsOfOutletRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeCreate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void checkBusinessReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setBusinessReportingDate(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutletNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setOutletName(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTownIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setTown(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIso6709LatituteIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setIso6709Latitute(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIso6709LongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setIso6709Longitude(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCbkApprovalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setCbkApprovalDate(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutletOpeningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setOutletOpeningDate(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseFeePayableIsRequired() throws Exception {
        int databaseSizeBeforeTest = particularsOfOutletRepository.findAll().size();
        // set the field null
        particularsOfOutlet.setLicenseFeePayable(null);

        // Create the ParticularsOfOutlet, which fails.
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutlets() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(particularsOfOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDate").value(hasItem(DEFAULT_BUSINESS_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].cbkApprovalDate").value(hasItem(DEFAULT_CBK_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));
    }

    @Test
    @Transactional
    void getParticularsOfOutlet() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get the particularsOfOutlet
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL_ID, particularsOfOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(particularsOfOutlet.getId().intValue()))
            .andExpect(jsonPath("$.businessReportingDate").value(DEFAULT_BUSINESS_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.outletName").value(DEFAULT_OUTLET_NAME))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN))
            .andExpect(jsonPath("$.iso6709Latitute").value(DEFAULT_ISO_6709_LATITUTE.doubleValue()))
            .andExpect(jsonPath("$.iso6709Longitude").value(DEFAULT_ISO_6709_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.cbkApprovalDate").value(DEFAULT_CBK_APPROVAL_DATE.toString()))
            .andExpect(jsonPath("$.outletOpeningDate").value(DEFAULT_OUTLET_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.outletClosureDate").value(DEFAULT_OUTLET_CLOSURE_DATE.toString()))
            .andExpect(jsonPath("$.licenseFeePayable").value(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE)));
    }

    @Test
    @Transactional
    void getParticularsOfOutletsByIdFiltering() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        Long id = particularsOfOutlet.getId();

        defaultParticularsOfOutletShouldBeFound("id.equals=" + id);
        defaultParticularsOfOutletShouldNotBeFound("id.notEquals=" + id);

        defaultParticularsOfOutletShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParticularsOfOutletShouldNotBeFound("id.greaterThan=" + id);

        defaultParticularsOfOutletShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParticularsOfOutletShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate equals to DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.equals=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate equals to UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.equals=" + UPDATED_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate not equals to DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.notEquals=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate not equals to UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.notEquals=" + UPDATED_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate in DEFAULT_BUSINESS_REPORTING_DATE or UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound(
            "businessReportingDate.in=" + DEFAULT_BUSINESS_REPORTING_DATE + "," + UPDATED_BUSINESS_REPORTING_DATE
        );

        // Get all the particularsOfOutletList where businessReportingDate equals to UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.in=" + UPDATED_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate is not null
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.specified=true");

        // Get all the particularsOfOutletList where businessReportingDate is null
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate is greater than or equal to DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.greaterThanOrEqual=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate is greater than or equal to UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.greaterThanOrEqual=" + UPDATED_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate is less than or equal to DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.lessThanOrEqual=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate is less than or equal to SMALLER_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.lessThanOrEqual=" + SMALLER_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate is less than DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.lessThan=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate is less than UPDATED_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.lessThan=" + UPDATED_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBusinessReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where businessReportingDate is greater than DEFAULT_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldNotBeFound("businessReportingDate.greaterThan=" + DEFAULT_BUSINESS_REPORTING_DATE);

        // Get all the particularsOfOutletList where businessReportingDate is greater than SMALLER_BUSINESS_REPORTING_DATE
        defaultParticularsOfOutletShouldBeFound("businessReportingDate.greaterThan=" + SMALLER_BUSINESS_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName equals to DEFAULT_OUTLET_NAME
        defaultParticularsOfOutletShouldBeFound("outletName.equals=" + DEFAULT_OUTLET_NAME);

        // Get all the particularsOfOutletList where outletName equals to UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldNotBeFound("outletName.equals=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName not equals to DEFAULT_OUTLET_NAME
        defaultParticularsOfOutletShouldNotBeFound("outletName.notEquals=" + DEFAULT_OUTLET_NAME);

        // Get all the particularsOfOutletList where outletName not equals to UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldBeFound("outletName.notEquals=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName in DEFAULT_OUTLET_NAME or UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldBeFound("outletName.in=" + DEFAULT_OUTLET_NAME + "," + UPDATED_OUTLET_NAME);

        // Get all the particularsOfOutletList where outletName equals to UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldNotBeFound("outletName.in=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName is not null
        defaultParticularsOfOutletShouldBeFound("outletName.specified=true");

        // Get all the particularsOfOutletList where outletName is null
        defaultParticularsOfOutletShouldNotBeFound("outletName.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameContainsSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName contains DEFAULT_OUTLET_NAME
        defaultParticularsOfOutletShouldBeFound("outletName.contains=" + DEFAULT_OUTLET_NAME);

        // Get all the particularsOfOutletList where outletName contains UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldNotBeFound("outletName.contains=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletNameNotContainsSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletName does not contain DEFAULT_OUTLET_NAME
        defaultParticularsOfOutletShouldNotBeFound("outletName.doesNotContain=" + DEFAULT_OUTLET_NAME);

        // Get all the particularsOfOutletList where outletName does not contain UPDATED_OUTLET_NAME
        defaultParticularsOfOutletShouldBeFound("outletName.doesNotContain=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town equals to DEFAULT_TOWN
        defaultParticularsOfOutletShouldBeFound("town.equals=" + DEFAULT_TOWN);

        // Get all the particularsOfOutletList where town equals to UPDATED_TOWN
        defaultParticularsOfOutletShouldNotBeFound("town.equals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town not equals to DEFAULT_TOWN
        defaultParticularsOfOutletShouldNotBeFound("town.notEquals=" + DEFAULT_TOWN);

        // Get all the particularsOfOutletList where town not equals to UPDATED_TOWN
        defaultParticularsOfOutletShouldBeFound("town.notEquals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town in DEFAULT_TOWN or UPDATED_TOWN
        defaultParticularsOfOutletShouldBeFound("town.in=" + DEFAULT_TOWN + "," + UPDATED_TOWN);

        // Get all the particularsOfOutletList where town equals to UPDATED_TOWN
        defaultParticularsOfOutletShouldNotBeFound("town.in=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town is not null
        defaultParticularsOfOutletShouldBeFound("town.specified=true");

        // Get all the particularsOfOutletList where town is null
        defaultParticularsOfOutletShouldNotBeFound("town.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownContainsSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town contains DEFAULT_TOWN
        defaultParticularsOfOutletShouldBeFound("town.contains=" + DEFAULT_TOWN);

        // Get all the particularsOfOutletList where town contains UPDATED_TOWN
        defaultParticularsOfOutletShouldNotBeFound("town.contains=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTownNotContainsSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where town does not contain DEFAULT_TOWN
        defaultParticularsOfOutletShouldNotBeFound("town.doesNotContain=" + DEFAULT_TOWN);

        // Get all the particularsOfOutletList where town does not contain UPDATED_TOWN
        defaultParticularsOfOutletShouldBeFound("town.doesNotContain=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute equals to DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.equals=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute equals to UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.equals=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute not equals to DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.notEquals=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute not equals to UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.notEquals=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute in DEFAULT_ISO_6709_LATITUTE or UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.in=" + DEFAULT_ISO_6709_LATITUTE + "," + UPDATED_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute equals to UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.in=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute is not null
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.specified=true");

        // Get all the particularsOfOutletList where iso6709Latitute is null
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute is greater than or equal to DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.greaterThanOrEqual=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute is greater than or equal to UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.greaterThanOrEqual=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute is less than or equal to DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.lessThanOrEqual=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute is less than or equal to SMALLER_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.lessThanOrEqual=" + SMALLER_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute is less than DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.lessThan=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute is less than UPDATED_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.lessThan=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LatituteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Latitute is greater than DEFAULT_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Latitute.greaterThan=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the particularsOfOutletList where iso6709Latitute is greater than SMALLER_ISO_6709_LATITUTE
        defaultParticularsOfOutletShouldBeFound("iso6709Latitute.greaterThan=" + SMALLER_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude equals to DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.equals=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude equals to UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.equals=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude not equals to DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.notEquals=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude not equals to UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.notEquals=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude in DEFAULT_ISO_6709_LONGITUDE or UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.in=" + DEFAULT_ISO_6709_LONGITUDE + "," + UPDATED_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude equals to UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.in=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude is not null
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.specified=true");

        // Get all the particularsOfOutletList where iso6709Longitude is null
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude is greater than or equal to DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.greaterThanOrEqual=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude is greater than or equal to UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.greaterThanOrEqual=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude is less than or equal to DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.lessThanOrEqual=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude is less than or equal to SMALLER_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.lessThanOrEqual=" + SMALLER_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude is less than DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.lessThan=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude is less than UPDATED_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.lessThan=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByIso6709LongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where iso6709Longitude is greater than DEFAULT_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldNotBeFound("iso6709Longitude.greaterThan=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the particularsOfOutletList where iso6709Longitude is greater than SMALLER_ISO_6709_LONGITUDE
        defaultParticularsOfOutletShouldBeFound("iso6709Longitude.greaterThan=" + SMALLER_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate equals to DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.equals=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate equals to UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.equals=" + UPDATED_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate not equals to DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.notEquals=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate not equals to UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.notEquals=" + UPDATED_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate in DEFAULT_CBK_APPROVAL_DATE or UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.in=" + DEFAULT_CBK_APPROVAL_DATE + "," + UPDATED_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate equals to UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.in=" + UPDATED_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate is not null
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.specified=true");

        // Get all the particularsOfOutletList where cbkApprovalDate is null
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate is greater than or equal to DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.greaterThanOrEqual=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate is greater than or equal to UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.greaterThanOrEqual=" + UPDATED_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate is less than or equal to DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.lessThanOrEqual=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate is less than or equal to SMALLER_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.lessThanOrEqual=" + SMALLER_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate is less than DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.lessThan=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate is less than UPDATED_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.lessThan=" + UPDATED_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByCbkApprovalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where cbkApprovalDate is greater than DEFAULT_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldNotBeFound("cbkApprovalDate.greaterThan=" + DEFAULT_CBK_APPROVAL_DATE);

        // Get all the particularsOfOutletList where cbkApprovalDate is greater than SMALLER_CBK_APPROVAL_DATE
        defaultParticularsOfOutletShouldBeFound("cbkApprovalDate.greaterThan=" + SMALLER_CBK_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate equals to DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.equals=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate equals to UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.equals=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate not equals to DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.notEquals=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate not equals to UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.notEquals=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate in DEFAULT_OUTLET_OPENING_DATE or UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.in=" + DEFAULT_OUTLET_OPENING_DATE + "," + UPDATED_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate equals to UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.in=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate is not null
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.specified=true");

        // Get all the particularsOfOutletList where outletOpeningDate is null
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate is greater than or equal to DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.greaterThanOrEqual=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate is greater than or equal to UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.greaterThanOrEqual=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate is less than or equal to DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.lessThanOrEqual=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate is less than or equal to SMALLER_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.lessThanOrEqual=" + SMALLER_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate is less than DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.lessThan=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate is less than UPDATED_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.lessThan=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletOpeningDate is greater than DEFAULT_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletOpeningDate.greaterThan=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the particularsOfOutletList where outletOpeningDate is greater than SMALLER_OUTLET_OPENING_DATE
        defaultParticularsOfOutletShouldBeFound("outletOpeningDate.greaterThan=" + SMALLER_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate equals to DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.equals=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.equals=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate not equals to DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.notEquals=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate not equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.notEquals=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate in DEFAULT_OUTLET_CLOSURE_DATE or UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.in=" + DEFAULT_OUTLET_CLOSURE_DATE + "," + UPDATED_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.in=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate is not null
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.specified=true");

        // Get all the particularsOfOutletList where outletClosureDate is null
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate is greater than or equal to DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.greaterThanOrEqual=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate is greater than or equal to UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.greaterThanOrEqual=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate is less than or equal to DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.lessThanOrEqual=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate is less than or equal to SMALLER_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.lessThanOrEqual=" + SMALLER_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate is less than DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.lessThan=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate is less than UPDATED_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.lessThan=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletClosureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where outletClosureDate is greater than DEFAULT_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldNotBeFound("outletClosureDate.greaterThan=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the particularsOfOutletList where outletClosureDate is greater than SMALLER_OUTLET_CLOSURE_DATE
        defaultParticularsOfOutletShouldBeFound("outletClosureDate.greaterThan=" + SMALLER_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable equals to DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.equals=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.equals=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable not equals to DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.notEquals=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable not equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.notEquals=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsInShouldWork() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable in DEFAULT_LICENSE_FEE_PAYABLE or UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.in=" + DEFAULT_LICENSE_FEE_PAYABLE + "," + UPDATED_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.in=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsNullOrNotNull() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable is not null
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.specified=true");

        // Get all the particularsOfOutletList where licenseFeePayable is null
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.specified=false");
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable is greater than or equal to DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.greaterThanOrEqual=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable is greater than or equal to UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.greaterThanOrEqual=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable is less than or equal to DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.lessThanOrEqual=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable is less than or equal to SMALLER_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.lessThanOrEqual=" + SMALLER_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsLessThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable is less than DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.lessThan=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable is less than UPDATED_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.lessThan=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByLicenseFeePayableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        // Get all the particularsOfOutletList where licenseFeePayable is greater than DEFAULT_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldNotBeFound("licenseFeePayable.greaterThan=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the particularsOfOutletList where licenseFeePayable is greater than SMALLER_LICENSE_FEE_PAYABLE
        defaultParticularsOfOutletShouldBeFound("licenseFeePayable.greaterThan=" + SMALLER_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsBySubCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        CountySubCountyCode subCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            subCountyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(subCountyCode);
            em.flush();
        } else {
            subCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        em.persist(subCountyCode);
        em.flush();
        particularsOfOutlet.setSubCountyCode(subCountyCode);
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        Long subCountyCodeId = subCountyCode.getId();

        // Get all the particularsOfOutletList where subCountyCode equals to subCountyCodeId
        defaultParticularsOfOutletShouldBeFound("subCountyCodeId.equals=" + subCountyCodeId);

        // Get all the particularsOfOutletList where subCountyCode equals to (subCountyCodeId + 1)
        defaultParticularsOfOutletShouldNotBeFound("subCountyCodeId.equals=" + (subCountyCodeId + 1));
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
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
        particularsOfOutlet.setBankCode(bankCode);
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        Long bankCodeId = bankCode.getId();

        // Get all the particularsOfOutletList where bankCode equals to bankCodeId
        defaultParticularsOfOutletShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the particularsOfOutletList where bankCode equals to (bankCodeId + 1)
        defaultParticularsOfOutletShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletIdIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        BankBranchCode outletId;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            outletId = BankBranchCodeResourceIT.createEntity(em);
            em.persist(outletId);
            em.flush();
        } else {
            outletId = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(outletId);
        em.flush();
        particularsOfOutlet.setOutletId(outletId);
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        Long outletIdId = outletId.getId();

        // Get all the particularsOfOutletList where outletId equals to outletIdId
        defaultParticularsOfOutletShouldBeFound("outletIdId.equals=" + outletIdId);

        // Get all the particularsOfOutletList where outletId equals to (outletIdId + 1)
        defaultParticularsOfOutletShouldNotBeFound("outletIdId.equals=" + (outletIdId + 1));
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByTypeOfOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        OutletType typeOfOutlet;
        if (TestUtil.findAll(em, OutletType.class).isEmpty()) {
            typeOfOutlet = OutletTypeResourceIT.createEntity(em);
            em.persist(typeOfOutlet);
            em.flush();
        } else {
            typeOfOutlet = TestUtil.findAll(em, OutletType.class).get(0);
        }
        em.persist(typeOfOutlet);
        em.flush();
        particularsOfOutlet.setTypeOfOutlet(typeOfOutlet);
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        Long typeOfOutletId = typeOfOutlet.getId();

        // Get all the particularsOfOutletList where typeOfOutlet equals to typeOfOutletId
        defaultParticularsOfOutletShouldBeFound("typeOfOutletId.equals=" + typeOfOutletId);

        // Get all the particularsOfOutletList where typeOfOutlet equals to (typeOfOutletId + 1)
        defaultParticularsOfOutletShouldNotBeFound("typeOfOutletId.equals=" + (typeOfOutletId + 1));
    }

    @Test
    @Transactional
    void getAllParticularsOfOutletsByOutletStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        OutletStatus outletStatus;
        if (TestUtil.findAll(em, OutletStatus.class).isEmpty()) {
            outletStatus = OutletStatusResourceIT.createEntity(em);
            em.persist(outletStatus);
            em.flush();
        } else {
            outletStatus = TestUtil.findAll(em, OutletStatus.class).get(0);
        }
        em.persist(outletStatus);
        em.flush();
        particularsOfOutlet.setOutletStatus(outletStatus);
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        Long outletStatusId = outletStatus.getId();

        // Get all the particularsOfOutletList where outletStatus equals to outletStatusId
        defaultParticularsOfOutletShouldBeFound("outletStatusId.equals=" + outletStatusId);

        // Get all the particularsOfOutletList where outletStatus equals to (outletStatusId + 1)
        defaultParticularsOfOutletShouldNotBeFound("outletStatusId.equals=" + (outletStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParticularsOfOutletShouldBeFound(String filter) throws Exception {
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(particularsOfOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDate").value(hasItem(DEFAULT_BUSINESS_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].cbkApprovalDate").value(hasItem(DEFAULT_CBK_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));

        // Check, that the count call also returns 1
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParticularsOfOutletShouldNotBeFound(String filter) throws Exception {
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParticularsOfOutlet() throws Exception {
        // Get the particularsOfOutlet
        restParticularsOfOutletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParticularsOfOutlet() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();

        // Update the particularsOfOutlet
        ParticularsOfOutlet updatedParticularsOfOutlet = particularsOfOutletRepository.findById(particularsOfOutlet.getId()).get();
        // Disconnect from session so that the updates on updatedParticularsOfOutlet are not directly saved in db
        em.detach(updatedParticularsOfOutlet);
        updatedParticularsOfOutlet
            .businessReportingDate(UPDATED_BUSINESS_REPORTING_DATE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .cbkApprovalDate(UPDATED_CBK_APPROVAL_DATE)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(updatedParticularsOfOutlet);

        restParticularsOfOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, particularsOfOutletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isOk());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);
        ParticularsOfOutlet testParticularsOfOutlet = particularsOfOutletList.get(particularsOfOutletList.size() - 1);
        assertThat(testParticularsOfOutlet.getBusinessReportingDate()).isEqualTo(UPDATED_BUSINESS_REPORTING_DATE);
        assertThat(testParticularsOfOutlet.getOutletName()).isEqualTo(UPDATED_OUTLET_NAME);
        assertThat(testParticularsOfOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testParticularsOfOutlet.getIso6709Latitute()).isEqualTo(UPDATED_ISO_6709_LATITUTE);
        assertThat(testParticularsOfOutlet.getIso6709Longitude()).isEqualTo(UPDATED_ISO_6709_LONGITUDE);
        assertThat(testParticularsOfOutlet.getCbkApprovalDate()).isEqualTo(UPDATED_CBK_APPROVAL_DATE);
        assertThat(testParticularsOfOutlet.getOutletOpeningDate()).isEqualTo(UPDATED_OUTLET_OPENING_DATE);
        assertThat(testParticularsOfOutlet.getOutletClosureDate()).isEqualTo(UPDATED_OUTLET_CLOSURE_DATE);
        assertThat(testParticularsOfOutlet.getLicenseFeePayable()).isEqualTo(UPDATED_LICENSE_FEE_PAYABLE);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository).save(testParticularsOfOutlet);
    }

    @Test
    @Transactional
    void putNonExistingParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, particularsOfOutletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void putWithIdMismatchParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void partialUpdateParticularsOfOutletWithPatch() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();

        // Update the particularsOfOutlet using partial update
        ParticularsOfOutlet partialUpdatedParticularsOfOutlet = new ParticularsOfOutlet();
        partialUpdatedParticularsOfOutlet.setId(particularsOfOutlet.getId());

        partialUpdatedParticularsOfOutlet
            .businessReportingDate(UPDATED_BUSINESS_REPORTING_DATE)
            .town(UPDATED_TOWN)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .cbkApprovalDate(UPDATED_CBK_APPROVAL_DATE)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);

        restParticularsOfOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticularsOfOutlet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticularsOfOutlet))
            )
            .andExpect(status().isOk());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);
        ParticularsOfOutlet testParticularsOfOutlet = particularsOfOutletList.get(particularsOfOutletList.size() - 1);
        assertThat(testParticularsOfOutlet.getBusinessReportingDate()).isEqualTo(UPDATED_BUSINESS_REPORTING_DATE);
        assertThat(testParticularsOfOutlet.getOutletName()).isEqualTo(DEFAULT_OUTLET_NAME);
        assertThat(testParticularsOfOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testParticularsOfOutlet.getIso6709Latitute()).isEqualTo(UPDATED_ISO_6709_LATITUTE);
        assertThat(testParticularsOfOutlet.getIso6709Longitude()).isEqualTo(DEFAULT_ISO_6709_LONGITUDE);
        assertThat(testParticularsOfOutlet.getCbkApprovalDate()).isEqualTo(UPDATED_CBK_APPROVAL_DATE);
        assertThat(testParticularsOfOutlet.getOutletOpeningDate()).isEqualTo(DEFAULT_OUTLET_OPENING_DATE);
        assertThat(testParticularsOfOutlet.getOutletClosureDate()).isEqualTo(DEFAULT_OUTLET_CLOSURE_DATE);
        assertThat(testParticularsOfOutlet.getLicenseFeePayable()).isEqualByComparingTo(UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void fullUpdateParticularsOfOutletWithPatch() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();

        // Update the particularsOfOutlet using partial update
        ParticularsOfOutlet partialUpdatedParticularsOfOutlet = new ParticularsOfOutlet();
        partialUpdatedParticularsOfOutlet.setId(particularsOfOutlet.getId());

        partialUpdatedParticularsOfOutlet
            .businessReportingDate(UPDATED_BUSINESS_REPORTING_DATE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .cbkApprovalDate(UPDATED_CBK_APPROVAL_DATE)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);

        restParticularsOfOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticularsOfOutlet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticularsOfOutlet))
            )
            .andExpect(status().isOk());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);
        ParticularsOfOutlet testParticularsOfOutlet = particularsOfOutletList.get(particularsOfOutletList.size() - 1);
        assertThat(testParticularsOfOutlet.getBusinessReportingDate()).isEqualTo(UPDATED_BUSINESS_REPORTING_DATE);
        assertThat(testParticularsOfOutlet.getOutletName()).isEqualTo(UPDATED_OUTLET_NAME);
        assertThat(testParticularsOfOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testParticularsOfOutlet.getIso6709Latitute()).isEqualTo(UPDATED_ISO_6709_LATITUTE);
        assertThat(testParticularsOfOutlet.getIso6709Longitude()).isEqualTo(UPDATED_ISO_6709_LONGITUDE);
        assertThat(testParticularsOfOutlet.getCbkApprovalDate()).isEqualTo(UPDATED_CBK_APPROVAL_DATE);
        assertThat(testParticularsOfOutlet.getOutletOpeningDate()).isEqualTo(UPDATED_OUTLET_OPENING_DATE);
        assertThat(testParticularsOfOutlet.getOutletClosureDate()).isEqualTo(UPDATED_OUTLET_CLOSURE_DATE);
        assertThat(testParticularsOfOutlet.getLicenseFeePayable()).isEqualByComparingTo(UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void patchNonExistingParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, particularsOfOutletDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParticularsOfOutlet() throws Exception {
        int databaseSizeBeforeUpdate = particularsOfOutletRepository.findAll().size();
        particularsOfOutlet.setId(count.incrementAndGet());

        // Create the ParticularsOfOutlet
        ParticularsOfOutletDTO particularsOfOutletDTO = particularsOfOutletMapper.toDto(particularsOfOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticularsOfOutletMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(particularsOfOutletDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParticularsOfOutlet in the database
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(0)).save(particularsOfOutlet);
    }

    @Test
    @Transactional
    void deleteParticularsOfOutlet() throws Exception {
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);

        int databaseSizeBeforeDelete = particularsOfOutletRepository.findAll().size();

        // Delete the particularsOfOutlet
        restParticularsOfOutletMockMvc
            .perform(delete(ENTITY_API_URL_ID, particularsOfOutlet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParticularsOfOutlet> particularsOfOutletList = particularsOfOutletRepository.findAll();
        assertThat(particularsOfOutletList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ParticularsOfOutlet in Elasticsearch
        verify(mockParticularsOfOutletSearchRepository, times(1)).deleteById(particularsOfOutlet.getId());
    }

    @Test
    @Transactional
    void searchParticularsOfOutlet() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        particularsOfOutletRepository.saveAndFlush(particularsOfOutlet);
        when(mockParticularsOfOutletSearchRepository.search("id:" + particularsOfOutlet.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(particularsOfOutlet), PageRequest.of(0, 1), 1));

        // Search the particularsOfOutlet
        restParticularsOfOutletMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + particularsOfOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(particularsOfOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDate").value(hasItem(DEFAULT_BUSINESS_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].cbkApprovalDate").value(hasItem(DEFAULT_CBK_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));
    }
}
