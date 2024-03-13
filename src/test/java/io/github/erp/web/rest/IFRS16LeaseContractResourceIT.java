package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.search.IFRS16LeaseContractSearchRepository;
import io.github.erp.service.criteria.IFRS16LeaseContractCriteria;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link IFRS16LeaseContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IFRS16LeaseContractResourceIT {

    private static final String DEFAULT_BOOKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INCEPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INCEPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INCEPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final UUID DEFAULT_SERIAL_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_SERIAL_NUMBER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/ifrs-16-lease-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ifrs-16-lease-contracts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IFRS16LeaseContractRepository iFRS16LeaseContractRepository;

    @Autowired
    private IFRS16LeaseContractMapper iFRS16LeaseContractMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.IFRS16LeaseContractSearchRepositoryMockConfiguration
     */
    @Autowired
    private IFRS16LeaseContractSearchRepository mockIFRS16LeaseContractSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIFRS16LeaseContractMockMvc;

    private IFRS16LeaseContract iFRS16LeaseContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IFRS16LeaseContract createEntity(EntityManager em) {
        IFRS16LeaseContract iFRS16LeaseContract = new IFRS16LeaseContract()
            .bookingId(DEFAULT_BOOKING_ID)
            .leaseTitle(DEFAULT_LEASE_TITLE)
            .shortTitle(DEFAULT_SHORT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .inceptionDate(DEFAULT_INCEPTION_DATE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .serialNumber(DEFAULT_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        iFRS16LeaseContract.setSuperintendentServiceOutlet(serviceOutlet);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        iFRS16LeaseContract.setMainDealer(dealer);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        iFRS16LeaseContract.setFirstReportingPeriod(fiscalMonth);
        // Add required entity
        iFRS16LeaseContract.setLastReportingPeriod(fiscalMonth);
        return iFRS16LeaseContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IFRS16LeaseContract createUpdatedEntity(EntityManager em) {
        IFRS16LeaseContract iFRS16LeaseContract = new IFRS16LeaseContract()
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createUpdatedEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        iFRS16LeaseContract.setSuperintendentServiceOutlet(serviceOutlet);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        iFRS16LeaseContract.setMainDealer(dealer);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        iFRS16LeaseContract.setFirstReportingPeriod(fiscalMonth);
        // Add required entity
        iFRS16LeaseContract.setLastReportingPeriod(fiscalMonth);
        return iFRS16LeaseContract;
    }

    @BeforeEach
    public void initTest() {
        iFRS16LeaseContract = createEntity(em);
    }

    @Test
    @Transactional
    void createIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeCreate = iFRS16LeaseContractRepository.findAll().size();
        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);
        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeCreate + 1);
        IFRS16LeaseContract testIFRS16LeaseContract = iFRS16LeaseContractList.get(iFRS16LeaseContractList.size() - 1);
        assertThat(testIFRS16LeaseContract.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testIFRS16LeaseContract.getLeaseTitle()).isEqualTo(DEFAULT_LEASE_TITLE);
        assertThat(testIFRS16LeaseContract.getShortTitle()).isEqualTo(DEFAULT_SHORT_TITLE);
        assertThat(testIFRS16LeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIFRS16LeaseContract.getInceptionDate()).isEqualTo(DEFAULT_INCEPTION_DATE);
        assertThat(testIFRS16LeaseContract.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testIFRS16LeaseContract.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(1)).save(testIFRS16LeaseContract);
    }

    @Test
    @Transactional
    void createIFRS16LeaseContractWithExistingId() throws Exception {
        // Create the IFRS16LeaseContract with an existing ID
        iFRS16LeaseContract.setId(1L);
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        int databaseSizeBeforeCreate = iFRS16LeaseContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeCreate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void checkBookingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = iFRS16LeaseContractRepository.findAll().size();
        // set the field null
        iFRS16LeaseContract.setBookingId(null);

        // Create the IFRS16LeaseContract, which fails.
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaseTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = iFRS16LeaseContractRepository.findAll().size();
        // set the field null
        iFRS16LeaseContract.setLeaseTitle(null);

        // Create the IFRS16LeaseContract, which fails.
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInceptionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = iFRS16LeaseContractRepository.findAll().size();
        // set the field null
        iFRS16LeaseContract.setInceptionDate(null);

        // Create the IFRS16LeaseContract, which fails.
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = iFRS16LeaseContractRepository.findAll().size();
        // set the field null
        iFRS16LeaseContract.setCommencementDate(null);

        // Create the IFRS16LeaseContract, which fails.
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        restIFRS16LeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContracts() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iFRS16LeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));
    }

    @Test
    @Transactional
    void getIFRS16LeaseContract() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get the iFRS16LeaseContract
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL_ID, iFRS16LeaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iFRS16LeaseContract.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID))
            .andExpect(jsonPath("$.leaseTitle").value(DEFAULT_LEASE_TITLE))
            .andExpect(jsonPath("$.shortTitle").value(DEFAULT_SHORT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.inceptionDate").value(DEFAULT_INCEPTION_DATE.toString()))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()));
    }

    @Test
    @Transactional
    void getIFRS16LeaseContractsByIdFiltering() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        Long id = iFRS16LeaseContract.getId();

        defaultIFRS16LeaseContractShouldBeFound("id.equals=" + id);
        defaultIFRS16LeaseContractShouldNotBeFound("id.notEquals=" + id);

        defaultIFRS16LeaseContractShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIFRS16LeaseContractShouldNotBeFound("id.greaterThan=" + id);

        defaultIFRS16LeaseContractShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIFRS16LeaseContractShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId equals to DEFAULT_BOOKING_ID
        defaultIFRS16LeaseContractShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the iFRS16LeaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId not equals to DEFAULT_BOOKING_ID
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.notEquals=" + DEFAULT_BOOKING_ID);

        // Get all the iFRS16LeaseContractList where bookingId not equals to UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldBeFound("bookingId.notEquals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the iFRS16LeaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId is not null
        defaultIFRS16LeaseContractShouldBeFound("bookingId.specified=true");

        // Get all the iFRS16LeaseContractList where bookingId is null
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId contains DEFAULT_BOOKING_ID
        defaultIFRS16LeaseContractShouldBeFound("bookingId.contains=" + DEFAULT_BOOKING_ID);

        // Get all the iFRS16LeaseContractList where bookingId contains UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.contains=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByBookingIdNotContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where bookingId does not contain DEFAULT_BOOKING_ID
        defaultIFRS16LeaseContractShouldNotBeFound("bookingId.doesNotContain=" + DEFAULT_BOOKING_ID);

        // Get all the iFRS16LeaseContractList where bookingId does not contain UPDATED_BOOKING_ID
        defaultIFRS16LeaseContractShouldBeFound("bookingId.doesNotContain=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle equals to DEFAULT_LEASE_TITLE
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.equals=" + DEFAULT_LEASE_TITLE);

        // Get all the iFRS16LeaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.equals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle not equals to DEFAULT_LEASE_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.notEquals=" + DEFAULT_LEASE_TITLE);

        // Get all the iFRS16LeaseContractList where leaseTitle not equals to UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.notEquals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle in DEFAULT_LEASE_TITLE or UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.in=" + DEFAULT_LEASE_TITLE + "," + UPDATED_LEASE_TITLE);

        // Get all the iFRS16LeaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.in=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle is not null
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.specified=true");

        // Get all the iFRS16LeaseContractList where leaseTitle is null
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle contains DEFAULT_LEASE_TITLE
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.contains=" + DEFAULT_LEASE_TITLE);

        // Get all the iFRS16LeaseContractList where leaseTitle contains UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.contains=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseTitleNotContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where leaseTitle does not contain DEFAULT_LEASE_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("leaseTitle.doesNotContain=" + DEFAULT_LEASE_TITLE);

        // Get all the iFRS16LeaseContractList where leaseTitle does not contain UPDATED_LEASE_TITLE
        defaultIFRS16LeaseContractShouldBeFound("leaseTitle.doesNotContain=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle equals to DEFAULT_SHORT_TITLE
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.equals=" + DEFAULT_SHORT_TITLE);

        // Get all the iFRS16LeaseContractList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.equals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle not equals to DEFAULT_SHORT_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.notEquals=" + DEFAULT_SHORT_TITLE);

        // Get all the iFRS16LeaseContractList where shortTitle not equals to UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.notEquals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle in DEFAULT_SHORT_TITLE or UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.in=" + DEFAULT_SHORT_TITLE + "," + UPDATED_SHORT_TITLE);

        // Get all the iFRS16LeaseContractList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.in=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle is not null
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.specified=true");

        // Get all the iFRS16LeaseContractList where shortTitle is null
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle contains DEFAULT_SHORT_TITLE
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.contains=" + DEFAULT_SHORT_TITLE);

        // Get all the iFRS16LeaseContractList where shortTitle contains UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.contains=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByShortTitleNotContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where shortTitle does not contain DEFAULT_SHORT_TITLE
        defaultIFRS16LeaseContractShouldNotBeFound("shortTitle.doesNotContain=" + DEFAULT_SHORT_TITLE);

        // Get all the iFRS16LeaseContractList where shortTitle does not contain UPDATED_SHORT_TITLE
        defaultIFRS16LeaseContractShouldBeFound("shortTitle.doesNotContain=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description equals to DEFAULT_DESCRIPTION
        defaultIFRS16LeaseContractShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the iFRS16LeaseContractList where description equals to UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description not equals to DEFAULT_DESCRIPTION
        defaultIFRS16LeaseContractShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the iFRS16LeaseContractList where description not equals to UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the iFRS16LeaseContractList where description equals to UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description is not null
        defaultIFRS16LeaseContractShouldBeFound("description.specified=true");

        // Get all the iFRS16LeaseContractList where description is null
        defaultIFRS16LeaseContractShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description contains DEFAULT_DESCRIPTION
        defaultIFRS16LeaseContractShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the iFRS16LeaseContractList where description contains UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where description does not contain DEFAULT_DESCRIPTION
        defaultIFRS16LeaseContractShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the iFRS16LeaseContractList where description does not contain UPDATED_DESCRIPTION
        defaultIFRS16LeaseContractShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate equals to DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.equals=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate equals to UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.equals=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate not equals to DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.notEquals=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate not equals to UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.notEquals=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate in DEFAULT_INCEPTION_DATE or UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.in=" + DEFAULT_INCEPTION_DATE + "," + UPDATED_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate equals to UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.in=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate is not null
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.specified=true");

        // Get all the iFRS16LeaseContractList where inceptionDate is null
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate is greater than or equal to DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.greaterThanOrEqual=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate is greater than or equal to UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.greaterThanOrEqual=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate is less than or equal to DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.lessThanOrEqual=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate is less than or equal to SMALLER_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.lessThanOrEqual=" + SMALLER_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate is less than DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.lessThan=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate is less than UPDATED_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.lessThan=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByInceptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where inceptionDate is greater than DEFAULT_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("inceptionDate.greaterThan=" + DEFAULT_INCEPTION_DATE);

        // Get all the iFRS16LeaseContractList where inceptionDate is greater than SMALLER_INCEPTION_DATE
        defaultIFRS16LeaseContractShouldBeFound("inceptionDate.greaterThan=" + SMALLER_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate is not null
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.specified=true");

        // Get all the iFRS16LeaseContractList where commencementDate is null
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the iFRS16LeaseContractList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultIFRS16LeaseContractShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the iFRS16LeaseContractList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the iFRS16LeaseContractList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the iFRS16LeaseContractList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultIFRS16LeaseContractShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        // Get all the iFRS16LeaseContractList where serialNumber is not null
        defaultIFRS16LeaseContractShouldBeFound("serialNumber.specified=true");

        // Get all the iFRS16LeaseContractList where serialNumber is null
        defaultIFRS16LeaseContractShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsBySuperintendentServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        ServiceOutlet superintendentServiceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            superintendentServiceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(superintendentServiceOutlet);
            em.flush();
        } else {
            superintendentServiceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(superintendentServiceOutlet);
        em.flush();
        iFRS16LeaseContract.setSuperintendentServiceOutlet(superintendentServiceOutlet);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long superintendentServiceOutletId = superintendentServiceOutlet.getId();

        // Get all the iFRS16LeaseContractList where superintendentServiceOutlet equals to superintendentServiceOutletId
        defaultIFRS16LeaseContractShouldBeFound("superintendentServiceOutletId.equals=" + superintendentServiceOutletId);

        // Get all the iFRS16LeaseContractList where superintendentServiceOutlet equals to (superintendentServiceOutletId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("superintendentServiceOutletId.equals=" + (superintendentServiceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByMainDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Dealer mainDealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            mainDealer = DealerResourceIT.createEntity(em);
            em.persist(mainDealer);
            em.flush();
        } else {
            mainDealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(mainDealer);
        em.flush();
        iFRS16LeaseContract.setMainDealer(mainDealer);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long mainDealerId = mainDealer.getId();

        // Get all the iFRS16LeaseContractList where mainDealer equals to mainDealerId
        defaultIFRS16LeaseContractShouldBeFound("mainDealerId.equals=" + mainDealerId);

        // Get all the iFRS16LeaseContractList where mainDealer equals to (mainDealerId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("mainDealerId.equals=" + (mainDealerId + 1));
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByFirstReportingPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        FiscalMonth firstReportingPeriod;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            firstReportingPeriod = FiscalMonthResourceIT.createEntity(em);
            em.persist(firstReportingPeriod);
            em.flush();
        } else {
            firstReportingPeriod = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(firstReportingPeriod);
        em.flush();
        iFRS16LeaseContract.setFirstReportingPeriod(firstReportingPeriod);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long firstReportingPeriodId = firstReportingPeriod.getId();

        // Get all the iFRS16LeaseContractList where firstReportingPeriod equals to firstReportingPeriodId
        defaultIFRS16LeaseContractShouldBeFound("firstReportingPeriodId.equals=" + firstReportingPeriodId);

        // Get all the iFRS16LeaseContractList where firstReportingPeriod equals to (firstReportingPeriodId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("firstReportingPeriodId.equals=" + (firstReportingPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLastReportingPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        FiscalMonth lastReportingPeriod;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            lastReportingPeriod = FiscalMonthResourceIT.createEntity(em);
            em.persist(lastReportingPeriod);
            em.flush();
        } else {
            lastReportingPeriod = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(lastReportingPeriod);
        em.flush();
        iFRS16LeaseContract.setLastReportingPeriod(lastReportingPeriod);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long lastReportingPeriodId = lastReportingPeriod.getId();

        // Get all the iFRS16LeaseContractList where lastReportingPeriod equals to lastReportingPeriodId
        defaultIFRS16LeaseContractShouldBeFound("lastReportingPeriodId.equals=" + lastReportingPeriodId);

        // Get all the iFRS16LeaseContractList where lastReportingPeriod equals to (lastReportingPeriodId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("lastReportingPeriodId.equals=" + (lastReportingPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseContractDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        BusinessDocument leaseContractDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            leaseContractDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(leaseContractDocument);
            em.flush();
        } else {
            leaseContractDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(leaseContractDocument);
        em.flush();
        iFRS16LeaseContract.setLeaseContractDocument(leaseContractDocument);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long leaseContractDocumentId = leaseContractDocument.getId();

        // Get all the iFRS16LeaseContractList where leaseContractDocument equals to leaseContractDocumentId
        defaultIFRS16LeaseContractShouldBeFound("leaseContractDocumentId.equals=" + leaseContractDocumentId);

        // Get all the iFRS16LeaseContractList where leaseContractDocument equals to (leaseContractDocumentId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("leaseContractDocumentId.equals=" + (leaseContractDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllIFRS16LeaseContractsByLeaseContractCalculationsIsEqualToSomething() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        BusinessDocument leaseContractCalculations;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            leaseContractCalculations = BusinessDocumentResourceIT.createEntity(em);
            em.persist(leaseContractCalculations);
            em.flush();
        } else {
            leaseContractCalculations = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(leaseContractCalculations);
        em.flush();
        iFRS16LeaseContract.setLeaseContractCalculations(leaseContractCalculations);
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        Long leaseContractCalculationsId = leaseContractCalculations.getId();

        // Get all the iFRS16LeaseContractList where leaseContractCalculations equals to leaseContractCalculationsId
        defaultIFRS16LeaseContractShouldBeFound("leaseContractCalculationsId.equals=" + leaseContractCalculationsId);

        // Get all the iFRS16LeaseContractList where leaseContractCalculations equals to (leaseContractCalculationsId + 1)
        defaultIFRS16LeaseContractShouldNotBeFound("leaseContractCalculationsId.equals=" + (leaseContractCalculationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIFRS16LeaseContractShouldBeFound(String filter) throws Exception {
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iFRS16LeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));

        // Check, that the count call also returns 1
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIFRS16LeaseContractShouldNotBeFound(String filter) throws Exception {
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIFRS16LeaseContract() throws Exception {
        // Get the iFRS16LeaseContract
        restIFRS16LeaseContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIFRS16LeaseContract() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();

        // Update the iFRS16LeaseContract
        IFRS16LeaseContract updatedIFRS16LeaseContract = iFRS16LeaseContractRepository.findById(iFRS16LeaseContract.getId()).get();
        // Disconnect from session so that the updates on updatedIFRS16LeaseContract are not directly saved in db
        em.detach(updatedIFRS16LeaseContract);
        updatedIFRS16LeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(updatedIFRS16LeaseContract);

        restIFRS16LeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iFRS16LeaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isOk());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);
        IFRS16LeaseContract testIFRS16LeaseContract = iFRS16LeaseContractList.get(iFRS16LeaseContractList.size() - 1);
        assertThat(testIFRS16LeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testIFRS16LeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testIFRS16LeaseContract.getShortTitle()).isEqualTo(UPDATED_SHORT_TITLE);
        assertThat(testIFRS16LeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIFRS16LeaseContract.getInceptionDate()).isEqualTo(UPDATED_INCEPTION_DATE);
        assertThat(testIFRS16LeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testIFRS16LeaseContract.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository).save(testIFRS16LeaseContract);
    }

    @Test
    @Transactional
    void putNonExistingIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iFRS16LeaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void putWithIdMismatchIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void partialUpdateIFRS16LeaseContractWithPatch() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();

        // Update the iFRS16LeaseContract using partial update
        IFRS16LeaseContract partialUpdatedIFRS16LeaseContract = new IFRS16LeaseContract();
        partialUpdatedIFRS16LeaseContract.setId(iFRS16LeaseContract.getId());

        partialUpdatedIFRS16LeaseContract.bookingId(UPDATED_BOOKING_ID);

        restIFRS16LeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIFRS16LeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIFRS16LeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);
        IFRS16LeaseContract testIFRS16LeaseContract = iFRS16LeaseContractList.get(iFRS16LeaseContractList.size() - 1);
        assertThat(testIFRS16LeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testIFRS16LeaseContract.getLeaseTitle()).isEqualTo(DEFAULT_LEASE_TITLE);
        assertThat(testIFRS16LeaseContract.getShortTitle()).isEqualTo(DEFAULT_SHORT_TITLE);
        assertThat(testIFRS16LeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIFRS16LeaseContract.getInceptionDate()).isEqualTo(DEFAULT_INCEPTION_DATE);
        assertThat(testIFRS16LeaseContract.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testIFRS16LeaseContract.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateIFRS16LeaseContractWithPatch() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();

        // Update the iFRS16LeaseContract using partial update
        IFRS16LeaseContract partialUpdatedIFRS16LeaseContract = new IFRS16LeaseContract();
        partialUpdatedIFRS16LeaseContract.setId(iFRS16LeaseContract.getId());

        partialUpdatedIFRS16LeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restIFRS16LeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIFRS16LeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIFRS16LeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);
        IFRS16LeaseContract testIFRS16LeaseContract = iFRS16LeaseContractList.get(iFRS16LeaseContractList.size() - 1);
        assertThat(testIFRS16LeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testIFRS16LeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testIFRS16LeaseContract.getShortTitle()).isEqualTo(UPDATED_SHORT_TITLE);
        assertThat(testIFRS16LeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIFRS16LeaseContract.getInceptionDate()).isEqualTo(UPDATED_INCEPTION_DATE);
        assertThat(testIFRS16LeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testIFRS16LeaseContract.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iFRS16LeaseContractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIFRS16LeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = iFRS16LeaseContractRepository.findAll().size();
        iFRS16LeaseContract.setId(count.incrementAndGet());

        // Create the IFRS16LeaseContract
        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = iFRS16LeaseContractMapper.toDto(iFRS16LeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIFRS16LeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iFRS16LeaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IFRS16LeaseContract in the database
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(0)).save(iFRS16LeaseContract);
    }

    @Test
    @Transactional
    void deleteIFRS16LeaseContract() throws Exception {
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);

        int databaseSizeBeforeDelete = iFRS16LeaseContractRepository.findAll().size();

        // Delete the iFRS16LeaseContract
        restIFRS16LeaseContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, iFRS16LeaseContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IFRS16LeaseContract> iFRS16LeaseContractList = iFRS16LeaseContractRepository.findAll();
        assertThat(iFRS16LeaseContractList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockIFRS16LeaseContractSearchRepository, times(1)).deleteById(iFRS16LeaseContract.getId());
    }

    @Test
    @Transactional
    void searchIFRS16LeaseContract() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        iFRS16LeaseContractRepository.saveAndFlush(iFRS16LeaseContract);
        when(mockIFRS16LeaseContractSearchRepository.search("id:" + iFRS16LeaseContract.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(iFRS16LeaseContract), PageRequest.of(0, 1), 1));

        // Search the iFRS16LeaseContract
        restIFRS16LeaseContractMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + iFRS16LeaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iFRS16LeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));
    }
}
