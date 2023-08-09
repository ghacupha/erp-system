package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.CountyCode;
import io.github.erp.domain.OutletStatus;
import io.github.erp.domain.OutletType;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.ServiceOutletRepository;
import io.github.erp.repository.search.ServiceOutletSearchRepository;
import io.github.erp.service.ServiceOutletService;
import io.github.erp.service.criteria.ServiceOutletCriteria;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.mapper.ServiceOutletMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link ServiceOutletResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceOutletResourceIT {

    private static final String DEFAULT_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OUTLET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final String DEFAULT_PARLIAMENTARY_CONSTITUENCY = "AAAAAAAAAA";
    private static final String UPDATED_PARLIAMENTARY_CONSTITUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_GPS_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_GPS_COORDINATES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_OUTLET_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OUTLET_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OUTLET_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_REGULATOR_APPROVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGULATOR_APPROVAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGULATOR_APPROVAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_OUTLET_CLOSURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OUTLET_CLOSURE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OUTLET_CLOSURE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LICENSE_FEE_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LICENSE_FEE_PAYABLE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LICENSE_FEE_PAYABLE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/service-outlets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/service-outlets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceOutletRepository serviceOutletRepository;

    @Mock
    private ServiceOutletRepository serviceOutletRepositoryMock;

    @Autowired
    private ServiceOutletMapper serviceOutletMapper;

    @Mock
    private ServiceOutletService serviceOutletServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ServiceOutletSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceOutletSearchRepository mockServiceOutletSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceOutletMockMvc;

    private ServiceOutlet serviceOutlet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOutlet createEntity(EntityManager em) {
        ServiceOutlet serviceOutlet = new ServiceOutlet()
            .outletCode(DEFAULT_OUTLET_CODE)
            .outletName(DEFAULT_OUTLET_NAME)
            .town(DEFAULT_TOWN)
            .parliamentaryConstituency(DEFAULT_PARLIAMENTARY_CONSTITUENCY)
            .gpsCoordinates(DEFAULT_GPS_COORDINATES)
            .outletOpeningDate(DEFAULT_OUTLET_OPENING_DATE)
            .regulatorApprovalDate(DEFAULT_REGULATOR_APPROVAL_DATE)
            .outletClosureDate(DEFAULT_OUTLET_CLOSURE_DATE)
            .dateLastModified(DEFAULT_DATE_LAST_MODIFIED)
            .licenseFeePayable(DEFAULT_LICENSE_FEE_PAYABLE);
        return serviceOutlet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOutlet createUpdatedEntity(EntityManager em) {
        ServiceOutlet serviceOutlet = new ServiceOutlet()
            .outletCode(UPDATED_OUTLET_CODE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .parliamentaryConstituency(UPDATED_PARLIAMENTARY_CONSTITUENCY)
            .gpsCoordinates(UPDATED_GPS_COORDINATES)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .regulatorApprovalDate(UPDATED_REGULATOR_APPROVAL_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);
        return serviceOutlet;
    }

    @BeforeEach
    public void initTest() {
        serviceOutlet = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceOutlet() throws Exception {
        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();
        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);
        restServiceOutletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getOutletCode()).isEqualTo(DEFAULT_OUTLET_CODE);
        assertThat(testServiceOutlet.getOutletName()).isEqualTo(DEFAULT_OUTLET_NAME);
        assertThat(testServiceOutlet.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testServiceOutlet.getParliamentaryConstituency()).isEqualTo(DEFAULT_PARLIAMENTARY_CONSTITUENCY);
        assertThat(testServiceOutlet.getGpsCoordinates()).isEqualTo(DEFAULT_GPS_COORDINATES);
        assertThat(testServiceOutlet.getOutletOpeningDate()).isEqualTo(DEFAULT_OUTLET_OPENING_DATE);
        assertThat(testServiceOutlet.getRegulatorApprovalDate()).isEqualTo(DEFAULT_REGULATOR_APPROVAL_DATE);
        assertThat(testServiceOutlet.getOutletClosureDate()).isEqualTo(DEFAULT_OUTLET_CLOSURE_DATE);
        assertThat(testServiceOutlet.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
        assertThat(testServiceOutlet.getLicenseFeePayable()).isEqualByComparingTo(DEFAULT_LICENSE_FEE_PAYABLE);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(1)).save(testServiceOutlet);
    }

    @Test
    @Transactional
    void createServiceOutletWithExistingId() throws Exception {
        // Create the ServiceOutlet with an existing ID
        serviceOutlet.setId(1L);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOutletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void checkOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setOutletCode(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutletNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setOutletName(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceOutlets() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].parliamentaryConstituency").value(hasItem(DEFAULT_PARLIAMENTARY_CONSTITUENCY)))
            .andExpect(jsonPath("$.[*].gpsCoordinates").value(hasItem(DEFAULT_GPS_COORDINATES)))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].regulatorApprovalDate").value(hasItem(DEFAULT_REGULATOR_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceOutletsWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceOutletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceOutletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceOutletServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceOutletsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceOutletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceOutletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceOutletServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get the serviceOutlet
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOutlet.getId().intValue()))
            .andExpect(jsonPath("$.outletCode").value(DEFAULT_OUTLET_CODE))
            .andExpect(jsonPath("$.outletName").value(DEFAULT_OUTLET_NAME))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN))
            .andExpect(jsonPath("$.parliamentaryConstituency").value(DEFAULT_PARLIAMENTARY_CONSTITUENCY))
            .andExpect(jsonPath("$.gpsCoordinates").value(DEFAULT_GPS_COORDINATES))
            .andExpect(jsonPath("$.outletOpeningDate").value(DEFAULT_OUTLET_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.regulatorApprovalDate").value(DEFAULT_REGULATOR_APPROVAL_DATE.toString()))
            .andExpect(jsonPath("$.outletClosureDate").value(DEFAULT_OUTLET_CLOSURE_DATE.toString()))
            .andExpect(jsonPath("$.dateLastModified").value(DEFAULT_DATE_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.licenseFeePayable").value(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE)));
    }

    @Test
    @Transactional
    void getServiceOutletsByIdFiltering() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        Long id = serviceOutlet.getId();

        defaultServiceOutletShouldBeFound("id.equals=" + id);
        defaultServiceOutletShouldNotBeFound("id.notEquals=" + id);

        defaultServiceOutletShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServiceOutletShouldNotBeFound("id.greaterThan=" + id);

        defaultServiceOutletShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServiceOutletShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode equals to DEFAULT_OUTLET_CODE
        defaultServiceOutletShouldBeFound("outletCode.equals=" + DEFAULT_OUTLET_CODE);

        // Get all the serviceOutletList where outletCode equals to UPDATED_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("outletCode.equals=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode not equals to DEFAULT_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("outletCode.notEquals=" + DEFAULT_OUTLET_CODE);

        // Get all the serviceOutletList where outletCode not equals to UPDATED_OUTLET_CODE
        defaultServiceOutletShouldBeFound("outletCode.notEquals=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode in DEFAULT_OUTLET_CODE or UPDATED_OUTLET_CODE
        defaultServiceOutletShouldBeFound("outletCode.in=" + DEFAULT_OUTLET_CODE + "," + UPDATED_OUTLET_CODE);

        // Get all the serviceOutletList where outletCode equals to UPDATED_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("outletCode.in=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode is not null
        defaultServiceOutletShouldBeFound("outletCode.specified=true");

        // Get all the serviceOutletList where outletCode is null
        defaultServiceOutletShouldNotBeFound("outletCode.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode contains DEFAULT_OUTLET_CODE
        defaultServiceOutletShouldBeFound("outletCode.contains=" + DEFAULT_OUTLET_CODE);

        // Get all the serviceOutletList where outletCode contains UPDATED_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("outletCode.contains=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletCode does not contain DEFAULT_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("outletCode.doesNotContain=" + DEFAULT_OUTLET_CODE);

        // Get all the serviceOutletList where outletCode does not contain UPDATED_OUTLET_CODE
        defaultServiceOutletShouldBeFound("outletCode.doesNotContain=" + UPDATED_OUTLET_CODE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName equals to DEFAULT_OUTLET_NAME
        defaultServiceOutletShouldBeFound("outletName.equals=" + DEFAULT_OUTLET_NAME);

        // Get all the serviceOutletList where outletName equals to UPDATED_OUTLET_NAME
        defaultServiceOutletShouldNotBeFound("outletName.equals=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName not equals to DEFAULT_OUTLET_NAME
        defaultServiceOutletShouldNotBeFound("outletName.notEquals=" + DEFAULT_OUTLET_NAME);

        // Get all the serviceOutletList where outletName not equals to UPDATED_OUTLET_NAME
        defaultServiceOutletShouldBeFound("outletName.notEquals=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName in DEFAULT_OUTLET_NAME or UPDATED_OUTLET_NAME
        defaultServiceOutletShouldBeFound("outletName.in=" + DEFAULT_OUTLET_NAME + "," + UPDATED_OUTLET_NAME);

        // Get all the serviceOutletList where outletName equals to UPDATED_OUTLET_NAME
        defaultServiceOutletShouldNotBeFound("outletName.in=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName is not null
        defaultServiceOutletShouldBeFound("outletName.specified=true");

        // Get all the serviceOutletList where outletName is null
        defaultServiceOutletShouldNotBeFound("outletName.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName contains DEFAULT_OUTLET_NAME
        defaultServiceOutletShouldBeFound("outletName.contains=" + DEFAULT_OUTLET_NAME);

        // Get all the serviceOutletList where outletName contains UPDATED_OUTLET_NAME
        defaultServiceOutletShouldNotBeFound("outletName.contains=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletNameNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletName does not contain DEFAULT_OUTLET_NAME
        defaultServiceOutletShouldNotBeFound("outletName.doesNotContain=" + DEFAULT_OUTLET_NAME);

        // Get all the serviceOutletList where outletName does not contain UPDATED_OUTLET_NAME
        defaultServiceOutletShouldBeFound("outletName.doesNotContain=" + UPDATED_OUTLET_NAME);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town equals to DEFAULT_TOWN
        defaultServiceOutletShouldBeFound("town.equals=" + DEFAULT_TOWN);

        // Get all the serviceOutletList where town equals to UPDATED_TOWN
        defaultServiceOutletShouldNotBeFound("town.equals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town not equals to DEFAULT_TOWN
        defaultServiceOutletShouldNotBeFound("town.notEquals=" + DEFAULT_TOWN);

        // Get all the serviceOutletList where town not equals to UPDATED_TOWN
        defaultServiceOutletShouldBeFound("town.notEquals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town in DEFAULT_TOWN or UPDATED_TOWN
        defaultServiceOutletShouldBeFound("town.in=" + DEFAULT_TOWN + "," + UPDATED_TOWN);

        // Get all the serviceOutletList where town equals to UPDATED_TOWN
        defaultServiceOutletShouldNotBeFound("town.in=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town is not null
        defaultServiceOutletShouldBeFound("town.specified=true");

        // Get all the serviceOutletList where town is null
        defaultServiceOutletShouldNotBeFound("town.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town contains DEFAULT_TOWN
        defaultServiceOutletShouldBeFound("town.contains=" + DEFAULT_TOWN);

        // Get all the serviceOutletList where town contains UPDATED_TOWN
        defaultServiceOutletShouldNotBeFound("town.contains=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByTownNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where town does not contain DEFAULT_TOWN
        defaultServiceOutletShouldNotBeFound("town.doesNotContain=" + DEFAULT_TOWN);

        // Get all the serviceOutletList where town does not contain UPDATED_TOWN
        defaultServiceOutletShouldBeFound("town.doesNotContain=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency equals to DEFAULT_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldBeFound("parliamentaryConstituency.equals=" + DEFAULT_PARLIAMENTARY_CONSTITUENCY);

        // Get all the serviceOutletList where parliamentaryConstituency equals to UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.equals=" + UPDATED_PARLIAMENTARY_CONSTITUENCY);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency not equals to DEFAULT_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.notEquals=" + DEFAULT_PARLIAMENTARY_CONSTITUENCY);

        // Get all the serviceOutletList where parliamentaryConstituency not equals to UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldBeFound("parliamentaryConstituency.notEquals=" + UPDATED_PARLIAMENTARY_CONSTITUENCY);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency in DEFAULT_PARLIAMENTARY_CONSTITUENCY or UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldBeFound(
            "parliamentaryConstituency.in=" + DEFAULT_PARLIAMENTARY_CONSTITUENCY + "," + UPDATED_PARLIAMENTARY_CONSTITUENCY
        );

        // Get all the serviceOutletList where parliamentaryConstituency equals to UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.in=" + UPDATED_PARLIAMENTARY_CONSTITUENCY);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency is not null
        defaultServiceOutletShouldBeFound("parliamentaryConstituency.specified=true");

        // Get all the serviceOutletList where parliamentaryConstituency is null
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency contains DEFAULT_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldBeFound("parliamentaryConstituency.contains=" + DEFAULT_PARLIAMENTARY_CONSTITUENCY);

        // Get all the serviceOutletList where parliamentaryConstituency contains UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.contains=" + UPDATED_PARLIAMENTARY_CONSTITUENCY);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByParliamentaryConstituencyNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where parliamentaryConstituency does not contain DEFAULT_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldNotBeFound("parliamentaryConstituency.doesNotContain=" + DEFAULT_PARLIAMENTARY_CONSTITUENCY);

        // Get all the serviceOutletList where parliamentaryConstituency does not contain UPDATED_PARLIAMENTARY_CONSTITUENCY
        defaultServiceOutletShouldBeFound("parliamentaryConstituency.doesNotContain=" + UPDATED_PARLIAMENTARY_CONSTITUENCY);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates equals to DEFAULT_GPS_COORDINATES
        defaultServiceOutletShouldBeFound("gpsCoordinates.equals=" + DEFAULT_GPS_COORDINATES);

        // Get all the serviceOutletList where gpsCoordinates equals to UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.equals=" + UPDATED_GPS_COORDINATES);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates not equals to DEFAULT_GPS_COORDINATES
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.notEquals=" + DEFAULT_GPS_COORDINATES);

        // Get all the serviceOutletList where gpsCoordinates not equals to UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldBeFound("gpsCoordinates.notEquals=" + UPDATED_GPS_COORDINATES);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates in DEFAULT_GPS_COORDINATES or UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldBeFound("gpsCoordinates.in=" + DEFAULT_GPS_COORDINATES + "," + UPDATED_GPS_COORDINATES);

        // Get all the serviceOutletList where gpsCoordinates equals to UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.in=" + UPDATED_GPS_COORDINATES);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates is not null
        defaultServiceOutletShouldBeFound("gpsCoordinates.specified=true");

        // Get all the serviceOutletList where gpsCoordinates is null
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates contains DEFAULT_GPS_COORDINATES
        defaultServiceOutletShouldBeFound("gpsCoordinates.contains=" + DEFAULT_GPS_COORDINATES);

        // Get all the serviceOutletList where gpsCoordinates contains UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.contains=" + UPDATED_GPS_COORDINATES);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByGpsCoordinatesNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where gpsCoordinates does not contain DEFAULT_GPS_COORDINATES
        defaultServiceOutletShouldNotBeFound("gpsCoordinates.doesNotContain=" + DEFAULT_GPS_COORDINATES);

        // Get all the serviceOutletList where gpsCoordinates does not contain UPDATED_GPS_COORDINATES
        defaultServiceOutletShouldBeFound("gpsCoordinates.doesNotContain=" + UPDATED_GPS_COORDINATES);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate equals to DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.equals=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate equals to UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.equals=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate not equals to DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.notEquals=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate not equals to UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.notEquals=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate in DEFAULT_OUTLET_OPENING_DATE or UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.in=" + DEFAULT_OUTLET_OPENING_DATE + "," + UPDATED_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate equals to UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.in=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate is not null
        defaultServiceOutletShouldBeFound("outletOpeningDate.specified=true");

        // Get all the serviceOutletList where outletOpeningDate is null
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate is greater than or equal to DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.greaterThanOrEqual=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate is greater than or equal to UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.greaterThanOrEqual=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate is less than or equal to DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.lessThanOrEqual=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate is less than or equal to SMALLER_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.lessThanOrEqual=" + SMALLER_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate is less than DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.lessThan=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate is less than UPDATED_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.lessThan=" + UPDATED_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletOpeningDate is greater than DEFAULT_OUTLET_OPENING_DATE
        defaultServiceOutletShouldNotBeFound("outletOpeningDate.greaterThan=" + DEFAULT_OUTLET_OPENING_DATE);

        // Get all the serviceOutletList where outletOpeningDate is greater than SMALLER_OUTLET_OPENING_DATE
        defaultServiceOutletShouldBeFound("outletOpeningDate.greaterThan=" + SMALLER_OUTLET_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate equals to DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.equals=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate equals to UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.equals=" + UPDATED_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate not equals to DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.notEquals=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate not equals to UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.notEquals=" + UPDATED_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate in DEFAULT_REGULATOR_APPROVAL_DATE or UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound(
            "regulatorApprovalDate.in=" + DEFAULT_REGULATOR_APPROVAL_DATE + "," + UPDATED_REGULATOR_APPROVAL_DATE
        );

        // Get all the serviceOutletList where regulatorApprovalDate equals to UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.in=" + UPDATED_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate is not null
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.specified=true");

        // Get all the serviceOutletList where regulatorApprovalDate is null
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate is greater than or equal to DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.greaterThanOrEqual=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate is greater than or equal to UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.greaterThanOrEqual=" + UPDATED_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate is less than or equal to DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.lessThanOrEqual=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate is less than or equal to SMALLER_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.lessThanOrEqual=" + SMALLER_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate is less than DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.lessThan=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate is less than UPDATED_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.lessThan=" + UPDATED_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByRegulatorApprovalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where regulatorApprovalDate is greater than DEFAULT_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldNotBeFound("regulatorApprovalDate.greaterThan=" + DEFAULT_REGULATOR_APPROVAL_DATE);

        // Get all the serviceOutletList where regulatorApprovalDate is greater than SMALLER_REGULATOR_APPROVAL_DATE
        defaultServiceOutletShouldBeFound("regulatorApprovalDate.greaterThan=" + SMALLER_REGULATOR_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate equals to DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.equals=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.equals=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate not equals to DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.notEquals=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate not equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.notEquals=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate in DEFAULT_OUTLET_CLOSURE_DATE or UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.in=" + DEFAULT_OUTLET_CLOSURE_DATE + "," + UPDATED_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate equals to UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.in=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate is not null
        defaultServiceOutletShouldBeFound("outletClosureDate.specified=true");

        // Get all the serviceOutletList where outletClosureDate is null
        defaultServiceOutletShouldNotBeFound("outletClosureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate is greater than or equal to DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.greaterThanOrEqual=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate is greater than or equal to UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.greaterThanOrEqual=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate is less than or equal to DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.lessThanOrEqual=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate is less than or equal to SMALLER_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.lessThanOrEqual=" + SMALLER_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate is less than DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.lessThan=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate is less than UPDATED_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.lessThan=" + UPDATED_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletClosureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where outletClosureDate is greater than DEFAULT_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldNotBeFound("outletClosureDate.greaterThan=" + DEFAULT_OUTLET_CLOSURE_DATE);

        // Get all the serviceOutletList where outletClosureDate is greater than SMALLER_OUTLET_CLOSURE_DATE
        defaultServiceOutletShouldBeFound("outletClosureDate.greaterThan=" + SMALLER_OUTLET_CLOSURE_DATE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified equals to DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.equals=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified equals to UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.equals=" + UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified not equals to DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.notEquals=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified not equals to UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.notEquals=" + UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified in DEFAULT_DATE_LAST_MODIFIED or UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.in=" + DEFAULT_DATE_LAST_MODIFIED + "," + UPDATED_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified equals to UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.in=" + UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified is not null
        defaultServiceOutletShouldBeFound("dateLastModified.specified=true");

        // Get all the serviceOutletList where dateLastModified is null
        defaultServiceOutletShouldNotBeFound("dateLastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified is greater than or equal to DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.greaterThanOrEqual=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified is greater than or equal to UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.greaterThanOrEqual=" + UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified is less than or equal to DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.lessThanOrEqual=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified is less than or equal to SMALLER_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.lessThanOrEqual=" + SMALLER_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified is less than DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.lessThan=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified is less than UPDATED_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.lessThan=" + UPDATED_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByDateLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where dateLastModified is greater than DEFAULT_DATE_LAST_MODIFIED
        defaultServiceOutletShouldNotBeFound("dateLastModified.greaterThan=" + DEFAULT_DATE_LAST_MODIFIED);

        // Get all the serviceOutletList where dateLastModified is greater than SMALLER_DATE_LAST_MODIFIED
        defaultServiceOutletShouldBeFound("dateLastModified.greaterThan=" + SMALLER_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable equals to DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.equals=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.equals=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable not equals to DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.notEquals=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable not equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.notEquals=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable in DEFAULT_LICENSE_FEE_PAYABLE or UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.in=" + DEFAULT_LICENSE_FEE_PAYABLE + "," + UPDATED_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable equals to UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.in=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable is not null
        defaultServiceOutletShouldBeFound("licenseFeePayable.specified=true");

        // Get all the serviceOutletList where licenseFeePayable is null
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable is greater than or equal to DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.greaterThanOrEqual=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable is greater than or equal to UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.greaterThanOrEqual=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable is less than or equal to DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.lessThanOrEqual=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable is less than or equal to SMALLER_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.lessThanOrEqual=" + SMALLER_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable is less than DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.lessThan=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable is less than UPDATED_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.lessThan=" + UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByLicenseFeePayableIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where licenseFeePayable is greater than DEFAULT_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldNotBeFound("licenseFeePayable.greaterThan=" + DEFAULT_LICENSE_FEE_PAYABLE);

        // Get all the serviceOutletList where licenseFeePayable is greater than SMALLER_LICENSE_FEE_PAYABLE
        defaultServiceOutletShouldBeFound("licenseFeePayable.greaterThan=" + SMALLER_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void getAllServiceOutletsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
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
        serviceOutlet.addPlaceholder(placeholder);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long placeholderId = placeholder.getId();

        // Get all the serviceOutletList where placeholder equals to placeholderId
        defaultServiceOutletShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the serviceOutletList where placeholder equals to (placeholderId + 1)
        defaultServiceOutletShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOutletsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        BankBranchCode bankCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        serviceOutlet.setBankCode(bankCode);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long bankCodeId = bankCode.getId();

        // Get all the serviceOutletList where bankCode equals to bankCodeId
        defaultServiceOutletShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the serviceOutletList where bankCode equals to (bankCodeId + 1)
        defaultServiceOutletShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        OutletType outletType;
        if (TestUtil.findAll(em, OutletType.class).isEmpty()) {
            outletType = OutletTypeResourceIT.createEntity(em);
            em.persist(outletType);
            em.flush();
        } else {
            outletType = TestUtil.findAll(em, OutletType.class).get(0);
        }
        em.persist(outletType);
        em.flush();
        serviceOutlet.setOutletType(outletType);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long outletTypeId = outletType.getId();

        // Get all the serviceOutletList where outletType equals to outletTypeId
        defaultServiceOutletShouldBeFound("outletTypeId.equals=" + outletTypeId);

        // Get all the serviceOutletList where outletType equals to (outletTypeId + 1)
        defaultServiceOutletShouldNotBeFound("outletTypeId.equals=" + (outletTypeId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOutletsByOutletStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
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
        serviceOutlet.setOutletStatus(outletStatus);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long outletStatusId = outletStatus.getId();

        // Get all the serviceOutletList where outletStatus equals to outletStatusId
        defaultServiceOutletShouldBeFound("outletStatusId.equals=" + outletStatusId);

        // Get all the serviceOutletList where outletStatus equals to (outletStatusId + 1)
        defaultServiceOutletShouldNotBeFound("outletStatusId.equals=" + (outletStatusId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOutletsByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        CountyCode countyName;
        if (TestUtil.findAll(em, CountyCode.class).isEmpty()) {
            countyName = CountyCodeResourceIT.createEntity(em);
            em.persist(countyName);
            em.flush();
        } else {
            countyName = TestUtil.findAll(em, CountyCode.class).get(0);
        }
        em.persist(countyName);
        em.flush();
        serviceOutlet.setCountyName(countyName);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long countyNameId = countyName.getId();

        // Get all the serviceOutletList where countyName equals to countyNameId
        defaultServiceOutletShouldBeFound("countyNameId.equals=" + countyNameId);

        // Get all the serviceOutletList where countyName equals to (countyNameId + 1)
        defaultServiceOutletShouldNotBeFound("countyNameId.equals=" + (countyNameId + 1));
    }

    @Test
    @Transactional
    void getAllServiceOutletsBySubCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        CountyCode subCountyName;
        if (TestUtil.findAll(em, CountyCode.class).isEmpty()) {
            subCountyName = CountyCodeResourceIT.createEntity(em);
            em.persist(subCountyName);
            em.flush();
        } else {
            subCountyName = TestUtil.findAll(em, CountyCode.class).get(0);
        }
        em.persist(subCountyName);
        em.flush();
        serviceOutlet.setSubCountyName(subCountyName);
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        Long subCountyNameId = subCountyName.getId();

        // Get all the serviceOutletList where subCountyName equals to subCountyNameId
        defaultServiceOutletShouldBeFound("subCountyNameId.equals=" + subCountyNameId);

        // Get all the serviceOutletList where subCountyName equals to (subCountyNameId + 1)
        defaultServiceOutletShouldNotBeFound("subCountyNameId.equals=" + (subCountyNameId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceOutletShouldBeFound(String filter) throws Exception {
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].parliamentaryConstituency").value(hasItem(DEFAULT_PARLIAMENTARY_CONSTITUENCY)))
            .andExpect(jsonPath("$.[*].gpsCoordinates").value(hasItem(DEFAULT_GPS_COORDINATES)))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].regulatorApprovalDate").value(hasItem(DEFAULT_REGULATOR_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));

        // Check, that the count call also returns 1
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceOutletShouldNotBeFound(String filter) throws Exception {
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceOutletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceOutlet() throws Exception {
        // Get the serviceOutlet
        restServiceOutletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Update the serviceOutlet
        ServiceOutlet updatedServiceOutlet = serviceOutletRepository.findById(serviceOutlet.getId()).get();
        // Disconnect from session so that the updates on updatedServiceOutlet are not directly saved in db
        em.detach(updatedServiceOutlet);
        updatedServiceOutlet
            .outletCode(UPDATED_OUTLET_CODE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .parliamentaryConstituency(UPDATED_PARLIAMENTARY_CONSTITUENCY)
            .gpsCoordinates(UPDATED_GPS_COORDINATES)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .regulatorApprovalDate(UPDATED_REGULATOR_APPROVAL_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(updatedServiceOutlet);

        restServiceOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOutletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getOutletCode()).isEqualTo(UPDATED_OUTLET_CODE);
        assertThat(testServiceOutlet.getOutletName()).isEqualTo(UPDATED_OUTLET_NAME);
        assertThat(testServiceOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testServiceOutlet.getParliamentaryConstituency()).isEqualTo(UPDATED_PARLIAMENTARY_CONSTITUENCY);
        assertThat(testServiceOutlet.getGpsCoordinates()).isEqualTo(UPDATED_GPS_COORDINATES);
        assertThat(testServiceOutlet.getOutletOpeningDate()).isEqualTo(UPDATED_OUTLET_OPENING_DATE);
        assertThat(testServiceOutlet.getRegulatorApprovalDate()).isEqualTo(UPDATED_REGULATOR_APPROVAL_DATE);
        assertThat(testServiceOutlet.getOutletClosureDate()).isEqualTo(UPDATED_OUTLET_CLOSURE_DATE);
        assertThat(testServiceOutlet.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testServiceOutlet.getLicenseFeePayable()).isEqualTo(UPDATED_LICENSE_FEE_PAYABLE);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository).save(testServiceOutlet);
    }

    @Test
    @Transactional
    void putNonExistingServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceOutletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void partialUpdateServiceOutletWithPatch() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Update the serviceOutlet using partial update
        ServiceOutlet partialUpdatedServiceOutlet = new ServiceOutlet();
        partialUpdatedServiceOutlet.setId(serviceOutlet.getId());

        partialUpdatedServiceOutlet
            .outletCode(UPDATED_OUTLET_CODE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .gpsCoordinates(UPDATED_GPS_COORDINATES)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .regulatorApprovalDate(UPDATED_REGULATOR_APPROVAL_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);

        restServiceOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOutlet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceOutlet))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getOutletCode()).isEqualTo(UPDATED_OUTLET_CODE);
        assertThat(testServiceOutlet.getOutletName()).isEqualTo(UPDATED_OUTLET_NAME);
        assertThat(testServiceOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testServiceOutlet.getParliamentaryConstituency()).isEqualTo(DEFAULT_PARLIAMENTARY_CONSTITUENCY);
        assertThat(testServiceOutlet.getGpsCoordinates()).isEqualTo(UPDATED_GPS_COORDINATES);
        assertThat(testServiceOutlet.getOutletOpeningDate()).isEqualTo(UPDATED_OUTLET_OPENING_DATE);
        assertThat(testServiceOutlet.getRegulatorApprovalDate()).isEqualTo(UPDATED_REGULATOR_APPROVAL_DATE);
        assertThat(testServiceOutlet.getOutletClosureDate()).isEqualTo(UPDATED_OUTLET_CLOSURE_DATE);
        assertThat(testServiceOutlet.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testServiceOutlet.getLicenseFeePayable()).isEqualByComparingTo(UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void fullUpdateServiceOutletWithPatch() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Update the serviceOutlet using partial update
        ServiceOutlet partialUpdatedServiceOutlet = new ServiceOutlet();
        partialUpdatedServiceOutlet.setId(serviceOutlet.getId());

        partialUpdatedServiceOutlet
            .outletCode(UPDATED_OUTLET_CODE)
            .outletName(UPDATED_OUTLET_NAME)
            .town(UPDATED_TOWN)
            .parliamentaryConstituency(UPDATED_PARLIAMENTARY_CONSTITUENCY)
            .gpsCoordinates(UPDATED_GPS_COORDINATES)
            .outletOpeningDate(UPDATED_OUTLET_OPENING_DATE)
            .regulatorApprovalDate(UPDATED_REGULATOR_APPROVAL_DATE)
            .outletClosureDate(UPDATED_OUTLET_CLOSURE_DATE)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .licenseFeePayable(UPDATED_LICENSE_FEE_PAYABLE);

        restServiceOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceOutlet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceOutlet))
            )
            .andExpect(status().isOk());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getOutletCode()).isEqualTo(UPDATED_OUTLET_CODE);
        assertThat(testServiceOutlet.getOutletName()).isEqualTo(UPDATED_OUTLET_NAME);
        assertThat(testServiceOutlet.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testServiceOutlet.getParliamentaryConstituency()).isEqualTo(UPDATED_PARLIAMENTARY_CONSTITUENCY);
        assertThat(testServiceOutlet.getGpsCoordinates()).isEqualTo(UPDATED_GPS_COORDINATES);
        assertThat(testServiceOutlet.getOutletOpeningDate()).isEqualTo(UPDATED_OUTLET_OPENING_DATE);
        assertThat(testServiceOutlet.getRegulatorApprovalDate()).isEqualTo(UPDATED_REGULATOR_APPROVAL_DATE);
        assertThat(testServiceOutlet.getOutletClosureDate()).isEqualTo(UPDATED_OUTLET_CLOSURE_DATE);
        assertThat(testServiceOutlet.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testServiceOutlet.getLicenseFeePayable()).isEqualByComparingTo(UPDATED_LICENSE_FEE_PAYABLE);
    }

    @Test
    @Transactional
    void patchNonExistingServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceOutletDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();
        serviceOutlet.setId(count.incrementAndGet());

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(0)).save(serviceOutlet);
    }

    @Test
    @Transactional
    void deleteServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeDelete = serviceOutletRepository.findAll().size();

        // Delete the serviceOutlet
        restServiceOutletMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceOutlet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceOutlet in Elasticsearch
        verify(mockServiceOutletSearchRepository, times(1)).deleteById(serviceOutlet.getId());
    }

    @Test
    @Transactional
    void searchServiceOutlet() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);
        when(mockServiceOutletSearchRepository.search("id:" + serviceOutlet.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceOutlet), PageRequest.of(0, 1), 1));

        // Search the serviceOutlet
        restServiceOutletMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + serviceOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletCode").value(hasItem(DEFAULT_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].outletName").value(hasItem(DEFAULT_OUTLET_NAME)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].parliamentaryConstituency").value(hasItem(DEFAULT_PARLIAMENTARY_CONSTITUENCY)))
            .andExpect(jsonPath("$.[*].gpsCoordinates").value(hasItem(DEFAULT_GPS_COORDINATES)))
            .andExpect(jsonPath("$.[*].outletOpeningDate").value(hasItem(DEFAULT_OUTLET_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].regulatorApprovalDate").value(hasItem(DEFAULT_REGULATOR_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].outletClosureDate").value(hasItem(DEFAULT_OUTLET_CLOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].licenseFeePayable").value(hasItem(sameNumber(DEFAULT_LICENSE_FEE_PAYABLE))));
    }
}
