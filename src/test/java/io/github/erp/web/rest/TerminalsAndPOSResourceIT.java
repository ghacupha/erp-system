package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.CountySubCountyCode;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.TerminalFunctions;
import io.github.erp.domain.TerminalTypes;
import io.github.erp.domain.TerminalsAndPOS;
import io.github.erp.repository.TerminalsAndPOSRepository;
import io.github.erp.repository.search.TerminalsAndPOSSearchRepository;
import io.github.erp.service.criteria.TerminalsAndPOSCriteria;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
import io.github.erp.service.mapper.TerminalsAndPOSMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TerminalsAndPOSResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TerminalsAndPOSResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TERMINAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MERCHANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TERMINAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TERMINAL_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_LOCATION = "BBBBBBBBBB";

    private static final Double DEFAULT_ISO_6709_LATITUTE = 1D;
    private static final Double UPDATED_ISO_6709_LATITUTE = 2D;
    private static final Double SMALLER_ISO_6709_LATITUTE = 1D - 1D;

    private static final Double DEFAULT_ISO_6709_LONGITUDE = 1D;
    private static final Double UPDATED_ISO_6709_LONGITUDE = 2D;
    private static final Double SMALLER_ISO_6709_LONGITUDE = 1D - 1D;

    private static final LocalDate DEFAULT_TERMINAL_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINAL_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TERMINAL_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TERMINAL_CLOSURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINAL_CLOSURE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TERMINAL_CLOSURE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/terminals-and-pos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/terminals-and-pos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalsAndPOSRepository terminalsAndPOSRepository;

    @Autowired
    private TerminalsAndPOSMapper terminalsAndPOSMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TerminalsAndPOSSearchRepositoryMockConfiguration
     */
    @Autowired
    private TerminalsAndPOSSearchRepository mockTerminalsAndPOSSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalsAndPOSMockMvc;

    private TerminalsAndPOS terminalsAndPOS;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalsAndPOS createEntity(EntityManager em) {
        TerminalsAndPOS terminalsAndPOS = new TerminalsAndPOS()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .terminalId(DEFAULT_TERMINAL_ID)
            .merchantId(DEFAULT_MERCHANT_ID)
            .terminalName(DEFAULT_TERMINAL_NAME)
            .terminalLocation(DEFAULT_TERMINAL_LOCATION)
            .iso6709Latitute(DEFAULT_ISO_6709_LATITUTE)
            .iso6709Longitude(DEFAULT_ISO_6709_LONGITUDE)
            .terminalOpeningDate(DEFAULT_TERMINAL_OPENING_DATE)
            .terminalClosureDate(DEFAULT_TERMINAL_CLOSURE_DATE);
        // Add required entity
        TerminalTypes terminalTypes;
        if (TestUtil.findAll(em, TerminalTypes.class).isEmpty()) {
            terminalTypes = TerminalTypesResourceIT.createEntity(em);
            em.persist(terminalTypes);
            em.flush();
        } else {
            terminalTypes = TestUtil.findAll(em, TerminalTypes.class).get(0);
        }
        terminalsAndPOS.setTerminalType(terminalTypes);
        // Add required entity
        TerminalFunctions terminalFunctions;
        if (TestUtil.findAll(em, TerminalFunctions.class).isEmpty()) {
            terminalFunctions = TerminalFunctionsResourceIT.createEntity(em);
            em.persist(terminalFunctions);
            em.flush();
        } else {
            terminalFunctions = TestUtil.findAll(em, TerminalFunctions.class).get(0);
        }
        terminalsAndPOS.setTerminalFunctionality(terminalFunctions);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        terminalsAndPOS.setPhysicalLocation(countySubCountyCode);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        terminalsAndPOS.setBankId(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        terminalsAndPOS.setBranchId(bankBranchCode);
        return terminalsAndPOS;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalsAndPOS createUpdatedEntity(EntityManager em) {
        TerminalsAndPOS terminalsAndPOS = new TerminalsAndPOS()
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .merchantId(UPDATED_MERCHANT_ID)
            .terminalName(UPDATED_TERMINAL_NAME)
            .terminalLocation(UPDATED_TERMINAL_LOCATION)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .terminalOpeningDate(UPDATED_TERMINAL_OPENING_DATE)
            .terminalClosureDate(UPDATED_TERMINAL_CLOSURE_DATE);
        // Add required entity
        TerminalTypes terminalTypes;
        if (TestUtil.findAll(em, TerminalTypes.class).isEmpty()) {
            terminalTypes = TerminalTypesResourceIT.createUpdatedEntity(em);
            em.persist(terminalTypes);
            em.flush();
        } else {
            terminalTypes = TestUtil.findAll(em, TerminalTypes.class).get(0);
        }
        terminalsAndPOS.setTerminalType(terminalTypes);
        // Add required entity
        TerminalFunctions terminalFunctions;
        if (TestUtil.findAll(em, TerminalFunctions.class).isEmpty()) {
            terminalFunctions = TerminalFunctionsResourceIT.createUpdatedEntity(em);
            em.persist(terminalFunctions);
            em.flush();
        } else {
            terminalFunctions = TestUtil.findAll(em, TerminalFunctions.class).get(0);
        }
        terminalsAndPOS.setTerminalFunctionality(terminalFunctions);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createUpdatedEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        terminalsAndPOS.setPhysicalLocation(countySubCountyCode);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        terminalsAndPOS.setBankId(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        terminalsAndPOS.setBranchId(bankBranchCode);
        return terminalsAndPOS;
    }

    @BeforeEach
    public void initTest() {
        terminalsAndPOS = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeCreate = terminalsAndPOSRepository.findAll().size();
        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);
        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeCreate + 1);
        TerminalsAndPOS testTerminalsAndPOS = terminalsAndPOSList.get(terminalsAndPOSList.size() - 1);
        assertThat(testTerminalsAndPOS.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalId()).isEqualTo(DEFAULT_TERMINAL_ID);
        assertThat(testTerminalsAndPOS.getMerchantId()).isEqualTo(DEFAULT_MERCHANT_ID);
        assertThat(testTerminalsAndPOS.getTerminalName()).isEqualTo(DEFAULT_TERMINAL_NAME);
        assertThat(testTerminalsAndPOS.getTerminalLocation()).isEqualTo(DEFAULT_TERMINAL_LOCATION);
        assertThat(testTerminalsAndPOS.getIso6709Latitute()).isEqualTo(DEFAULT_ISO_6709_LATITUTE);
        assertThat(testTerminalsAndPOS.getIso6709Longitude()).isEqualTo(DEFAULT_ISO_6709_LONGITUDE);
        assertThat(testTerminalsAndPOS.getTerminalOpeningDate()).isEqualTo(DEFAULT_TERMINAL_OPENING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalClosureDate()).isEqualTo(DEFAULT_TERMINAL_CLOSURE_DATE);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(1)).save(testTerminalsAndPOS);
    }

    @Test
    @Transactional
    void createTerminalsAndPOSWithExistingId() throws Exception {
        // Create the TerminalsAndPOS with an existing ID
        terminalsAndPOS.setId(1L);
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        int databaseSizeBeforeCreate = terminalsAndPOSRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeCreate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setReportingDate(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setTerminalId(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMerchantIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setMerchantId(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setTerminalName(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setTerminalLocation(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIso6709LatituteIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setIso6709Latitute(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIso6709LongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setIso6709Longitude(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalOpeningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalsAndPOSRepository.findAll().size();
        // set the field null
        terminalsAndPOS.setTerminalOpeningDate(null);

        // Create the TerminalsAndPOS, which fails.
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOS() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalsAndPOS.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID)))
            .andExpect(jsonPath("$.[*].terminalName").value(hasItem(DEFAULT_TERMINAL_NAME)))
            .andExpect(jsonPath("$.[*].terminalLocation").value(hasItem(DEFAULT_TERMINAL_LOCATION)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].terminalOpeningDate").value(hasItem(DEFAULT_TERMINAL_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalClosureDate").value(hasItem(DEFAULT_TERMINAL_CLOSURE_DATE.toString())));
    }

    @Test
    @Transactional
    void getTerminalsAndPOS() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get the terminalsAndPOS
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL_ID, terminalsAndPOS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminalsAndPOS.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.terminalId").value(DEFAULT_TERMINAL_ID))
            .andExpect(jsonPath("$.merchantId").value(DEFAULT_MERCHANT_ID))
            .andExpect(jsonPath("$.terminalName").value(DEFAULT_TERMINAL_NAME))
            .andExpect(jsonPath("$.terminalLocation").value(DEFAULT_TERMINAL_LOCATION))
            .andExpect(jsonPath("$.iso6709Latitute").value(DEFAULT_ISO_6709_LATITUTE.doubleValue()))
            .andExpect(jsonPath("$.iso6709Longitude").value(DEFAULT_ISO_6709_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.terminalOpeningDate").value(DEFAULT_TERMINAL_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.terminalClosureDate").value(DEFAULT_TERMINAL_CLOSURE_DATE.toString()));
    }

    @Test
    @Transactional
    void getTerminalsAndPOSByIdFiltering() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        Long id = terminalsAndPOS.getId();

        defaultTerminalsAndPOSShouldBeFound("id.equals=" + id);
        defaultTerminalsAndPOSShouldNotBeFound("id.notEquals=" + id);

        defaultTerminalsAndPOSShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTerminalsAndPOSShouldNotBeFound("id.greaterThan=" + id);

        defaultTerminalsAndPOSShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTerminalsAndPOSShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate is not null
        defaultTerminalsAndPOSShouldBeFound("reportingDate.specified=true");

        // Get all the terminalsAndPOSList where reportingDate is null
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the terminalsAndPOSList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultTerminalsAndPOSShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId equals to DEFAULT_TERMINAL_ID
        defaultTerminalsAndPOSShouldBeFound("terminalId.equals=" + DEFAULT_TERMINAL_ID);

        // Get all the terminalsAndPOSList where terminalId equals to UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.equals=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId not equals to DEFAULT_TERMINAL_ID
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.notEquals=" + DEFAULT_TERMINAL_ID);

        // Get all the terminalsAndPOSList where terminalId not equals to UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldBeFound("terminalId.notEquals=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId in DEFAULT_TERMINAL_ID or UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldBeFound("terminalId.in=" + DEFAULT_TERMINAL_ID + "," + UPDATED_TERMINAL_ID);

        // Get all the terminalsAndPOSList where terminalId equals to UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.in=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId is not null
        defaultTerminalsAndPOSShouldBeFound("terminalId.specified=true");

        // Get all the terminalsAndPOSList where terminalId is null
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId contains DEFAULT_TERMINAL_ID
        defaultTerminalsAndPOSShouldBeFound("terminalId.contains=" + DEFAULT_TERMINAL_ID);

        // Get all the terminalsAndPOSList where terminalId contains UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.contains=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalIdNotContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalId does not contain DEFAULT_TERMINAL_ID
        defaultTerminalsAndPOSShouldNotBeFound("terminalId.doesNotContain=" + DEFAULT_TERMINAL_ID);

        // Get all the terminalsAndPOSList where terminalId does not contain UPDATED_TERMINAL_ID
        defaultTerminalsAndPOSShouldBeFound("terminalId.doesNotContain=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId equals to DEFAULT_MERCHANT_ID
        defaultTerminalsAndPOSShouldBeFound("merchantId.equals=" + DEFAULT_MERCHANT_ID);

        // Get all the terminalsAndPOSList where merchantId equals to UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.equals=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId not equals to DEFAULT_MERCHANT_ID
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.notEquals=" + DEFAULT_MERCHANT_ID);

        // Get all the terminalsAndPOSList where merchantId not equals to UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldBeFound("merchantId.notEquals=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId in DEFAULT_MERCHANT_ID or UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldBeFound("merchantId.in=" + DEFAULT_MERCHANT_ID + "," + UPDATED_MERCHANT_ID);

        // Get all the terminalsAndPOSList where merchantId equals to UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.in=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId is not null
        defaultTerminalsAndPOSShouldBeFound("merchantId.specified=true");

        // Get all the terminalsAndPOSList where merchantId is null
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId contains DEFAULT_MERCHANT_ID
        defaultTerminalsAndPOSShouldBeFound("merchantId.contains=" + DEFAULT_MERCHANT_ID);

        // Get all the terminalsAndPOSList where merchantId contains UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.contains=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByMerchantIdNotContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where merchantId does not contain DEFAULT_MERCHANT_ID
        defaultTerminalsAndPOSShouldNotBeFound("merchantId.doesNotContain=" + DEFAULT_MERCHANT_ID);

        // Get all the terminalsAndPOSList where merchantId does not contain UPDATED_MERCHANT_ID
        defaultTerminalsAndPOSShouldBeFound("merchantId.doesNotContain=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName equals to DEFAULT_TERMINAL_NAME
        defaultTerminalsAndPOSShouldBeFound("terminalName.equals=" + DEFAULT_TERMINAL_NAME);

        // Get all the terminalsAndPOSList where terminalName equals to UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.equals=" + UPDATED_TERMINAL_NAME);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName not equals to DEFAULT_TERMINAL_NAME
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.notEquals=" + DEFAULT_TERMINAL_NAME);

        // Get all the terminalsAndPOSList where terminalName not equals to UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldBeFound("terminalName.notEquals=" + UPDATED_TERMINAL_NAME);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName in DEFAULT_TERMINAL_NAME or UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldBeFound("terminalName.in=" + DEFAULT_TERMINAL_NAME + "," + UPDATED_TERMINAL_NAME);

        // Get all the terminalsAndPOSList where terminalName equals to UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.in=" + UPDATED_TERMINAL_NAME);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName is not null
        defaultTerminalsAndPOSShouldBeFound("terminalName.specified=true");

        // Get all the terminalsAndPOSList where terminalName is null
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName contains DEFAULT_TERMINAL_NAME
        defaultTerminalsAndPOSShouldBeFound("terminalName.contains=" + DEFAULT_TERMINAL_NAME);

        // Get all the terminalsAndPOSList where terminalName contains UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.contains=" + UPDATED_TERMINAL_NAME);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalNameNotContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalName does not contain DEFAULT_TERMINAL_NAME
        defaultTerminalsAndPOSShouldNotBeFound("terminalName.doesNotContain=" + DEFAULT_TERMINAL_NAME);

        // Get all the terminalsAndPOSList where terminalName does not contain UPDATED_TERMINAL_NAME
        defaultTerminalsAndPOSShouldBeFound("terminalName.doesNotContain=" + UPDATED_TERMINAL_NAME);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation equals to DEFAULT_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.equals=" + DEFAULT_TERMINAL_LOCATION);

        // Get all the terminalsAndPOSList where terminalLocation equals to UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.equals=" + UPDATED_TERMINAL_LOCATION);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation not equals to DEFAULT_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.notEquals=" + DEFAULT_TERMINAL_LOCATION);

        // Get all the terminalsAndPOSList where terminalLocation not equals to UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.notEquals=" + UPDATED_TERMINAL_LOCATION);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation in DEFAULT_TERMINAL_LOCATION or UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.in=" + DEFAULT_TERMINAL_LOCATION + "," + UPDATED_TERMINAL_LOCATION);

        // Get all the terminalsAndPOSList where terminalLocation equals to UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.in=" + UPDATED_TERMINAL_LOCATION);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation is not null
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.specified=true");

        // Get all the terminalsAndPOSList where terminalLocation is null
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation contains DEFAULT_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.contains=" + DEFAULT_TERMINAL_LOCATION);

        // Get all the terminalsAndPOSList where terminalLocation contains UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.contains=" + UPDATED_TERMINAL_LOCATION);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalLocationNotContainsSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalLocation does not contain DEFAULT_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldNotBeFound("terminalLocation.doesNotContain=" + DEFAULT_TERMINAL_LOCATION);

        // Get all the terminalsAndPOSList where terminalLocation does not contain UPDATED_TERMINAL_LOCATION
        defaultTerminalsAndPOSShouldBeFound("terminalLocation.doesNotContain=" + UPDATED_TERMINAL_LOCATION);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute equals to DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.equals=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute equals to UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.equals=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute not equals to DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.notEquals=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute not equals to UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.notEquals=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute in DEFAULT_ISO_6709_LATITUTE or UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.in=" + DEFAULT_ISO_6709_LATITUTE + "," + UPDATED_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute equals to UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.in=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute is not null
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.specified=true");

        // Get all the terminalsAndPOSList where iso6709Latitute is null
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute is greater than or equal to DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.greaterThanOrEqual=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute is greater than or equal to UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.greaterThanOrEqual=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute is less than or equal to DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.lessThanOrEqual=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute is less than or equal to SMALLER_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.lessThanOrEqual=" + SMALLER_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsLessThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute is less than DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.lessThan=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute is less than UPDATED_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.lessThan=" + UPDATED_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LatituteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Latitute is greater than DEFAULT_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Latitute.greaterThan=" + DEFAULT_ISO_6709_LATITUTE);

        // Get all the terminalsAndPOSList where iso6709Latitute is greater than SMALLER_ISO_6709_LATITUTE
        defaultTerminalsAndPOSShouldBeFound("iso6709Latitute.greaterThan=" + SMALLER_ISO_6709_LATITUTE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude equals to DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.equals=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude equals to UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.equals=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude not equals to DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.notEquals=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude not equals to UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.notEquals=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude in DEFAULT_ISO_6709_LONGITUDE or UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.in=" + DEFAULT_ISO_6709_LONGITUDE + "," + UPDATED_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude equals to UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.in=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude is not null
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.specified=true");

        // Get all the terminalsAndPOSList where iso6709Longitude is null
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude is greater than or equal to DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.greaterThanOrEqual=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude is greater than or equal to UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.greaterThanOrEqual=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude is less than or equal to DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.lessThanOrEqual=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude is less than or equal to SMALLER_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.lessThanOrEqual=" + SMALLER_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude is less than DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.lessThan=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude is less than UPDATED_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.lessThan=" + UPDATED_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByIso6709LongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where iso6709Longitude is greater than DEFAULT_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldNotBeFound("iso6709Longitude.greaterThan=" + DEFAULT_ISO_6709_LONGITUDE);

        // Get all the terminalsAndPOSList where iso6709Longitude is greater than SMALLER_ISO_6709_LONGITUDE
        defaultTerminalsAndPOSShouldBeFound("iso6709Longitude.greaterThan=" + SMALLER_ISO_6709_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate equals to DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.equals=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate equals to UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.equals=" + UPDATED_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate not equals to DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.notEquals=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate not equals to UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.notEquals=" + UPDATED_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate in DEFAULT_TERMINAL_OPENING_DATE or UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound(
            "terminalOpeningDate.in=" + DEFAULT_TERMINAL_OPENING_DATE + "," + UPDATED_TERMINAL_OPENING_DATE
        );

        // Get all the terminalsAndPOSList where terminalOpeningDate equals to UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.in=" + UPDATED_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate is not null
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.specified=true");

        // Get all the terminalsAndPOSList where terminalOpeningDate is null
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate is greater than or equal to DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.greaterThanOrEqual=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate is greater than or equal to UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.greaterThanOrEqual=" + UPDATED_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate is less than or equal to DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.lessThanOrEqual=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate is less than or equal to SMALLER_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.lessThanOrEqual=" + SMALLER_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate is less than DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.lessThan=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate is less than UPDATED_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.lessThan=" + UPDATED_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalOpeningDate is greater than DEFAULT_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalOpeningDate.greaterThan=" + DEFAULT_TERMINAL_OPENING_DATE);

        // Get all the terminalsAndPOSList where terminalOpeningDate is greater than SMALLER_TERMINAL_OPENING_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalOpeningDate.greaterThan=" + SMALLER_TERMINAL_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate equals to DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.equals=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate equals to UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.equals=" + UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate not equals to DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.notEquals=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate not equals to UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.notEquals=" + UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsInShouldWork() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate in DEFAULT_TERMINAL_CLOSURE_DATE or UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound(
            "terminalClosureDate.in=" + DEFAULT_TERMINAL_CLOSURE_DATE + "," + UPDATED_TERMINAL_CLOSURE_DATE
        );

        // Get all the terminalsAndPOSList where terminalClosureDate equals to UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.in=" + UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate is not null
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.specified=true");

        // Get all the terminalsAndPOSList where terminalClosureDate is null
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate is greater than or equal to DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.greaterThanOrEqual=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate is greater than or equal to UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.greaterThanOrEqual=" + UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate is less than or equal to DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.lessThanOrEqual=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate is less than or equal to SMALLER_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.lessThanOrEqual=" + SMALLER_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate is less than DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.lessThan=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate is less than UPDATED_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.lessThan=" + UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalClosureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        // Get all the terminalsAndPOSList where terminalClosureDate is greater than DEFAULT_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldNotBeFound("terminalClosureDate.greaterThan=" + DEFAULT_TERMINAL_CLOSURE_DATE);

        // Get all the terminalsAndPOSList where terminalClosureDate is greater than SMALLER_TERMINAL_CLOSURE_DATE
        defaultTerminalsAndPOSShouldBeFound("terminalClosureDate.greaterThan=" + SMALLER_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        TerminalTypes terminalType;
        if (TestUtil.findAll(em, TerminalTypes.class).isEmpty()) {
            terminalType = TerminalTypesResourceIT.createEntity(em);
            em.persist(terminalType);
            em.flush();
        } else {
            terminalType = TestUtil.findAll(em, TerminalTypes.class).get(0);
        }
        em.persist(terminalType);
        em.flush();
        terminalsAndPOS.setTerminalType(terminalType);
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        Long terminalTypeId = terminalType.getId();

        // Get all the terminalsAndPOSList where terminalType equals to terminalTypeId
        defaultTerminalsAndPOSShouldBeFound("terminalTypeId.equals=" + terminalTypeId);

        // Get all the terminalsAndPOSList where terminalType equals to (terminalTypeId + 1)
        defaultTerminalsAndPOSShouldNotBeFound("terminalTypeId.equals=" + (terminalTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByTerminalFunctionalityIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        TerminalFunctions terminalFunctionality;
        if (TestUtil.findAll(em, TerminalFunctions.class).isEmpty()) {
            terminalFunctionality = TerminalFunctionsResourceIT.createEntity(em);
            em.persist(terminalFunctionality);
            em.flush();
        } else {
            terminalFunctionality = TestUtil.findAll(em, TerminalFunctions.class).get(0);
        }
        em.persist(terminalFunctionality);
        em.flush();
        terminalsAndPOS.setTerminalFunctionality(terminalFunctionality);
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        Long terminalFunctionalityId = terminalFunctionality.getId();

        // Get all the terminalsAndPOSList where terminalFunctionality equals to terminalFunctionalityId
        defaultTerminalsAndPOSShouldBeFound("terminalFunctionalityId.equals=" + terminalFunctionalityId);

        // Get all the terminalsAndPOSList where terminalFunctionality equals to (terminalFunctionalityId + 1)
        defaultTerminalsAndPOSShouldNotBeFound("terminalFunctionalityId.equals=" + (terminalFunctionalityId + 1));
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByPhysicalLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        CountySubCountyCode physicalLocation;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            physicalLocation = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(physicalLocation);
            em.flush();
        } else {
            physicalLocation = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        em.persist(physicalLocation);
        em.flush();
        terminalsAndPOS.setPhysicalLocation(physicalLocation);
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        Long physicalLocationId = physicalLocation.getId();

        // Get all the terminalsAndPOSList where physicalLocation equals to physicalLocationId
        defaultTerminalsAndPOSShouldBeFound("physicalLocationId.equals=" + physicalLocationId);

        // Get all the terminalsAndPOSList where physicalLocation equals to (physicalLocationId + 1)
        defaultTerminalsAndPOSShouldNotBeFound("physicalLocationId.equals=" + (physicalLocationId + 1));
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByBankIdIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        InstitutionCode bankId;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankId = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankId);
            em.flush();
        } else {
            bankId = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankId);
        em.flush();
        terminalsAndPOS.setBankId(bankId);
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        Long bankIdId = bankId.getId();

        // Get all the terminalsAndPOSList where bankId equals to bankIdId
        defaultTerminalsAndPOSShouldBeFound("bankIdId.equals=" + bankIdId);

        // Get all the terminalsAndPOSList where bankId equals to (bankIdId + 1)
        defaultTerminalsAndPOSShouldNotBeFound("bankIdId.equals=" + (bankIdId + 1));
    }

    @Test
    @Transactional
    void getAllTerminalsAndPOSByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        BankBranchCode branchId;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchId = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchId);
            em.flush();
        } else {
            branchId = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchId);
        em.flush();
        terminalsAndPOS.setBranchId(branchId);
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        Long branchIdId = branchId.getId();

        // Get all the terminalsAndPOSList where branchId equals to branchIdId
        defaultTerminalsAndPOSShouldBeFound("branchIdId.equals=" + branchIdId);

        // Get all the terminalsAndPOSList where branchId equals to (branchIdId + 1)
        defaultTerminalsAndPOSShouldNotBeFound("branchIdId.equals=" + (branchIdId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTerminalsAndPOSShouldBeFound(String filter) throws Exception {
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalsAndPOS.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID)))
            .andExpect(jsonPath("$.[*].terminalName").value(hasItem(DEFAULT_TERMINAL_NAME)))
            .andExpect(jsonPath("$.[*].terminalLocation").value(hasItem(DEFAULT_TERMINAL_LOCATION)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].terminalOpeningDate").value(hasItem(DEFAULT_TERMINAL_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalClosureDate").value(hasItem(DEFAULT_TERMINAL_CLOSURE_DATE.toString())));

        // Check, that the count call also returns 1
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTerminalsAndPOSShouldNotBeFound(String filter) throws Exception {
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTerminalsAndPOS() throws Exception {
        // Get the terminalsAndPOS
        restTerminalsAndPOSMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTerminalsAndPOS() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();

        // Update the terminalsAndPOS
        TerminalsAndPOS updatedTerminalsAndPOS = terminalsAndPOSRepository.findById(terminalsAndPOS.getId()).get();
        // Disconnect from session so that the updates on updatedTerminalsAndPOS are not directly saved in db
        em.detach(updatedTerminalsAndPOS);
        updatedTerminalsAndPOS
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .merchantId(UPDATED_MERCHANT_ID)
            .terminalName(UPDATED_TERMINAL_NAME)
            .terminalLocation(UPDATED_TERMINAL_LOCATION)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .terminalOpeningDate(UPDATED_TERMINAL_OPENING_DATE)
            .terminalClosureDate(UPDATED_TERMINAL_CLOSURE_DATE);
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(updatedTerminalsAndPOS);

        restTerminalsAndPOSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalsAndPOSDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isOk());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);
        TerminalsAndPOS testTerminalsAndPOS = terminalsAndPOSList.get(terminalsAndPOSList.size() - 1);
        assertThat(testTerminalsAndPOS.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testTerminalsAndPOS.getMerchantId()).isEqualTo(UPDATED_MERCHANT_ID);
        assertThat(testTerminalsAndPOS.getTerminalName()).isEqualTo(UPDATED_TERMINAL_NAME);
        assertThat(testTerminalsAndPOS.getTerminalLocation()).isEqualTo(UPDATED_TERMINAL_LOCATION);
        assertThat(testTerminalsAndPOS.getIso6709Latitute()).isEqualTo(UPDATED_ISO_6709_LATITUTE);
        assertThat(testTerminalsAndPOS.getIso6709Longitude()).isEqualTo(UPDATED_ISO_6709_LONGITUDE);
        assertThat(testTerminalsAndPOS.getTerminalOpeningDate()).isEqualTo(UPDATED_TERMINAL_OPENING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalClosureDate()).isEqualTo(UPDATED_TERMINAL_CLOSURE_DATE);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository).save(testTerminalsAndPOS);
    }

    @Test
    @Transactional
    void putNonExistingTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalsAndPOSDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void partialUpdateTerminalsAndPOSWithPatch() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();

        // Update the terminalsAndPOS using partial update
        TerminalsAndPOS partialUpdatedTerminalsAndPOS = new TerminalsAndPOS();
        partialUpdatedTerminalsAndPOS.setId(terminalsAndPOS.getId());

        partialUpdatedTerminalsAndPOS
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .terminalOpeningDate(UPDATED_TERMINAL_OPENING_DATE);

        restTerminalsAndPOSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalsAndPOS.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalsAndPOS))
            )
            .andExpect(status().isOk());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);
        TerminalsAndPOS testTerminalsAndPOS = terminalsAndPOSList.get(terminalsAndPOSList.size() - 1);
        assertThat(testTerminalsAndPOS.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testTerminalsAndPOS.getMerchantId()).isEqualTo(DEFAULT_MERCHANT_ID);
        assertThat(testTerminalsAndPOS.getTerminalName()).isEqualTo(DEFAULT_TERMINAL_NAME);
        assertThat(testTerminalsAndPOS.getTerminalLocation()).isEqualTo(DEFAULT_TERMINAL_LOCATION);
        assertThat(testTerminalsAndPOS.getIso6709Latitute()).isEqualTo(DEFAULT_ISO_6709_LATITUTE);
        assertThat(testTerminalsAndPOS.getIso6709Longitude()).isEqualTo(DEFAULT_ISO_6709_LONGITUDE);
        assertThat(testTerminalsAndPOS.getTerminalOpeningDate()).isEqualTo(UPDATED_TERMINAL_OPENING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalClosureDate()).isEqualTo(DEFAULT_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTerminalsAndPOSWithPatch() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();

        // Update the terminalsAndPOS using partial update
        TerminalsAndPOS partialUpdatedTerminalsAndPOS = new TerminalsAndPOS();
        partialUpdatedTerminalsAndPOS.setId(terminalsAndPOS.getId());

        partialUpdatedTerminalsAndPOS
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .merchantId(UPDATED_MERCHANT_ID)
            .terminalName(UPDATED_TERMINAL_NAME)
            .terminalLocation(UPDATED_TERMINAL_LOCATION)
            .iso6709Latitute(UPDATED_ISO_6709_LATITUTE)
            .iso6709Longitude(UPDATED_ISO_6709_LONGITUDE)
            .terminalOpeningDate(UPDATED_TERMINAL_OPENING_DATE)
            .terminalClosureDate(UPDATED_TERMINAL_CLOSURE_DATE);

        restTerminalsAndPOSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalsAndPOS.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalsAndPOS))
            )
            .andExpect(status().isOk());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);
        TerminalsAndPOS testTerminalsAndPOS = terminalsAndPOSList.get(terminalsAndPOSList.size() - 1);
        assertThat(testTerminalsAndPOS.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testTerminalsAndPOS.getMerchantId()).isEqualTo(UPDATED_MERCHANT_ID);
        assertThat(testTerminalsAndPOS.getTerminalName()).isEqualTo(UPDATED_TERMINAL_NAME);
        assertThat(testTerminalsAndPOS.getTerminalLocation()).isEqualTo(UPDATED_TERMINAL_LOCATION);
        assertThat(testTerminalsAndPOS.getIso6709Latitute()).isEqualTo(UPDATED_ISO_6709_LATITUTE);
        assertThat(testTerminalsAndPOS.getIso6709Longitude()).isEqualTo(UPDATED_ISO_6709_LONGITUDE);
        assertThat(testTerminalsAndPOS.getTerminalOpeningDate()).isEqualTo(UPDATED_TERMINAL_OPENING_DATE);
        assertThat(testTerminalsAndPOS.getTerminalClosureDate()).isEqualTo(UPDATED_TERMINAL_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalsAndPOSDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminalsAndPOS() throws Exception {
        int databaseSizeBeforeUpdate = terminalsAndPOSRepository.findAll().size();
        terminalsAndPOS.setId(count.incrementAndGet());

        // Create the TerminalsAndPOS
        TerminalsAndPOSDTO terminalsAndPOSDTO = terminalsAndPOSMapper.toDto(terminalsAndPOS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalsAndPOSMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalsAndPOSDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalsAndPOS in the database
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(0)).save(terminalsAndPOS);
    }

    @Test
    @Transactional
    void deleteTerminalsAndPOS() throws Exception {
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);

        int databaseSizeBeforeDelete = terminalsAndPOSRepository.findAll().size();

        // Delete the terminalsAndPOS
        restTerminalsAndPOSMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminalsAndPOS.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TerminalsAndPOS> terminalsAndPOSList = terminalsAndPOSRepository.findAll();
        assertThat(terminalsAndPOSList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TerminalsAndPOS in Elasticsearch
        verify(mockTerminalsAndPOSSearchRepository, times(1)).deleteById(terminalsAndPOS.getId());
    }

    @Test
    @Transactional
    void searchTerminalsAndPOS() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        terminalsAndPOSRepository.saveAndFlush(terminalsAndPOS);
        when(mockTerminalsAndPOSSearchRepository.search("id:" + terminalsAndPOS.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(terminalsAndPOS), PageRequest.of(0, 1), 1));

        // Search the terminalsAndPOS
        restTerminalsAndPOSMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + terminalsAndPOS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalsAndPOS.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID)))
            .andExpect(jsonPath("$.[*].terminalName").value(hasItem(DEFAULT_TERMINAL_NAME)))
            .andExpect(jsonPath("$.[*].terminalLocation").value(hasItem(DEFAULT_TERMINAL_LOCATION)))
            .andExpect(jsonPath("$.[*].iso6709Latitute").value(hasItem(DEFAULT_ISO_6709_LATITUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].iso6709Longitude").value(hasItem(DEFAULT_ISO_6709_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].terminalOpeningDate").value(hasItem(DEFAULT_TERMINAL_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalClosureDate").value(hasItem(DEFAULT_TERMINAL_CLOSURE_DATE.toString())));
    }
}
