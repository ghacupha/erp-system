package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.ContractMetadata;
import io.github.erp.domain.LeaseContract;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.LeaseContractRepository;
import io.github.erp.repository.search.LeaseContractSearchRepository;
import io.github.erp.service.LeaseContractService;
import io.github.erp.service.criteria.LeaseContractCriteria;
import io.github.erp.service.dto.LeaseContractDTO;
import io.github.erp.service.mapper.LeaseContractMapper;
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
 * Integration tests for the {@link LeaseContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseContractResourceIT {

    private static final String DEFAULT_BOOKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TITLE = "BBBBBBBBBB";

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TERMINAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TERMINAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/lease-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-contracts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseContractRepository leaseContractRepository;

    @Mock
    private LeaseContractRepository leaseContractRepositoryMock;

    @Autowired
    private LeaseContractMapper leaseContractMapper;

    @Mock
    private LeaseContractService leaseContractServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseContractSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseContractSearchRepository mockLeaseContractSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseContractMockMvc;

    private LeaseContract leaseContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseContract createEntity(EntityManager em) {
        LeaseContract leaseContract = new LeaseContract()
            .bookingId(DEFAULT_BOOKING_ID)
            .leaseTitle(DEFAULT_LEASE_TITLE)
            .identifier(DEFAULT_IDENTIFIER)
            .description(DEFAULT_DESCRIPTION)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .terminalDate(DEFAULT_TERMINAL_DATE);
        return leaseContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseContract createUpdatedEntity(EntityManager em) {
        LeaseContract leaseContract = new LeaseContract()
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .identifier(UPDATED_IDENTIFIER)
            .description(UPDATED_DESCRIPTION)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE);
        return leaseContract;
    }

    @BeforeEach
    public void initTest() {
        leaseContract = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseContract() throws Exception {
        int databaseSizeBeforeCreate = leaseContractRepository.findAll().size();
        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);
        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseContract testLeaseContract = leaseContractList.get(leaseContractList.size() - 1);
        assertThat(testLeaseContract.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testLeaseContract.getLeaseTitle()).isEqualTo(DEFAULT_LEASE_TITLE);
        assertThat(testLeaseContract.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testLeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeaseContract.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testLeaseContract.getTerminalDate()).isEqualTo(DEFAULT_TERMINAL_DATE);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(1)).save(testLeaseContract);
    }

    @Test
    @Transactional
    void createLeaseContractWithExistingId() throws Exception {
        // Create the LeaseContract with an existing ID
        leaseContract.setId(1L);
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        int databaseSizeBeforeCreate = leaseContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void checkBookingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseContractRepository.findAll().size();
        // set the field null
        leaseContract.setBookingId(null);

        // Create the LeaseContract, which fails.
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaseTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseContractRepository.findAll().size();
        // set the field null
        leaseContract.setLeaseTitle(null);

        // Create the LeaseContract, which fails.
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseContractRepository.findAll().size();
        // set the field null
        leaseContract.setIdentifier(null);

        // Create the LeaseContract, which fails.
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseContractRepository.findAll().size();
        // set the field null
        leaseContract.setCommencementDate(null);

        // Create the LeaseContract, which fails.
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseContractRepository.findAll().size();
        // set the field null
        leaseContract.setTerminalDate(null);

        // Create the LeaseContract, which fails.
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        restLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseContracts() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(leaseContractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseContractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaseContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leaseContractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeaseContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leaseContractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLeaseContract() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get the leaseContract
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseContract.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID))
            .andExpect(jsonPath("$.leaseTitle").value(DEFAULT_LEASE_TITLE))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.terminalDate").value(DEFAULT_TERMINAL_DATE.toString()));
    }

    @Test
    @Transactional
    void getLeaseContractsByIdFiltering() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        Long id = leaseContract.getId();

        defaultLeaseContractShouldBeFound("id.equals=" + id);
        defaultLeaseContractShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseContractShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseContractShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseContractShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseContractShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId equals to DEFAULT_BOOKING_ID
        defaultLeaseContractShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseContractShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId not equals to DEFAULT_BOOKING_ID
        defaultLeaseContractShouldNotBeFound("bookingId.notEquals=" + DEFAULT_BOOKING_ID);

        // Get all the leaseContractList where bookingId not equals to UPDATED_BOOKING_ID
        defaultLeaseContractShouldBeFound("bookingId.notEquals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultLeaseContractShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the leaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultLeaseContractShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId is not null
        defaultLeaseContractShouldBeFound("bookingId.specified=true");

        // Get all the leaseContractList where bookingId is null
        defaultLeaseContractShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId contains DEFAULT_BOOKING_ID
        defaultLeaseContractShouldBeFound("bookingId.contains=" + DEFAULT_BOOKING_ID);

        // Get all the leaseContractList where bookingId contains UPDATED_BOOKING_ID
        defaultLeaseContractShouldNotBeFound("bookingId.contains=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBookingIdNotContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where bookingId does not contain DEFAULT_BOOKING_ID
        defaultLeaseContractShouldNotBeFound("bookingId.doesNotContain=" + DEFAULT_BOOKING_ID);

        // Get all the leaseContractList where bookingId does not contain UPDATED_BOOKING_ID
        defaultLeaseContractShouldBeFound("bookingId.doesNotContain=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle equals to DEFAULT_LEASE_TITLE
        defaultLeaseContractShouldBeFound("leaseTitle.equals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseContractShouldNotBeFound("leaseTitle.equals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle not equals to DEFAULT_LEASE_TITLE
        defaultLeaseContractShouldNotBeFound("leaseTitle.notEquals=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseContractList where leaseTitle not equals to UPDATED_LEASE_TITLE
        defaultLeaseContractShouldBeFound("leaseTitle.notEquals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle in DEFAULT_LEASE_TITLE or UPDATED_LEASE_TITLE
        defaultLeaseContractShouldBeFound("leaseTitle.in=" + DEFAULT_LEASE_TITLE + "," + UPDATED_LEASE_TITLE);

        // Get all the leaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultLeaseContractShouldNotBeFound("leaseTitle.in=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle is not null
        defaultLeaseContractShouldBeFound("leaseTitle.specified=true");

        // Get all the leaseContractList where leaseTitle is null
        defaultLeaseContractShouldNotBeFound("leaseTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle contains DEFAULT_LEASE_TITLE
        defaultLeaseContractShouldBeFound("leaseTitle.contains=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseContractList where leaseTitle contains UPDATED_LEASE_TITLE
        defaultLeaseContractShouldNotBeFound("leaseTitle.contains=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByLeaseTitleNotContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where leaseTitle does not contain DEFAULT_LEASE_TITLE
        defaultLeaseContractShouldNotBeFound("leaseTitle.doesNotContain=" + DEFAULT_LEASE_TITLE);

        // Get all the leaseContractList where leaseTitle does not contain UPDATED_LEASE_TITLE
        defaultLeaseContractShouldBeFound("leaseTitle.doesNotContain=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where identifier equals to DEFAULT_IDENTIFIER
        defaultLeaseContractShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the leaseContractList where identifier equals to UPDATED_IDENTIFIER
        defaultLeaseContractShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where identifier not equals to DEFAULT_IDENTIFIER
        defaultLeaseContractShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the leaseContractList where identifier not equals to UPDATED_IDENTIFIER
        defaultLeaseContractShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultLeaseContractShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the leaseContractList where identifier equals to UPDATED_IDENTIFIER
        defaultLeaseContractShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where identifier is not null
        defaultLeaseContractShouldBeFound("identifier.specified=true");

        // Get all the leaseContractList where identifier is null
        defaultLeaseContractShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description equals to DEFAULT_DESCRIPTION
        defaultLeaseContractShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseContractList where description equals to UPDATED_DESCRIPTION
        defaultLeaseContractShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description not equals to DEFAULT_DESCRIPTION
        defaultLeaseContractShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leaseContractList where description not equals to UPDATED_DESCRIPTION
        defaultLeaseContractShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeaseContractShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leaseContractList where description equals to UPDATED_DESCRIPTION
        defaultLeaseContractShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description is not null
        defaultLeaseContractShouldBeFound("description.specified=true");

        // Get all the leaseContractList where description is null
        defaultLeaseContractShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description contains DEFAULT_DESCRIPTION
        defaultLeaseContractShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leaseContractList where description contains UPDATED_DESCRIPTION
        defaultLeaseContractShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where description does not contain DEFAULT_DESCRIPTION
        defaultLeaseContractShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leaseContractList where description does not contain UPDATED_DESCRIPTION
        defaultLeaseContractShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate is not null
        defaultLeaseContractShouldBeFound("commencementDate.specified=true");

        // Get all the leaseContractList where commencementDate is null
        defaultLeaseContractShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseContractShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseContractList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultLeaseContractShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate equals to DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.equals=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate equals to UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.equals=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate not equals to DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.notEquals=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate not equals to UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.notEquals=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate in DEFAULT_TERMINAL_DATE or UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.in=" + DEFAULT_TERMINAL_DATE + "," + UPDATED_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate equals to UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.in=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate is not null
        defaultLeaseContractShouldBeFound("terminalDate.specified=true");

        // Get all the leaseContractList where terminalDate is null
        defaultLeaseContractShouldNotBeFound("terminalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate is greater than or equal to DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.greaterThanOrEqual=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate is greater than or equal to UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.greaterThanOrEqual=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate is less than or equal to DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.lessThanOrEqual=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate is less than or equal to SMALLER_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.lessThanOrEqual=" + SMALLER_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate is less than DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.lessThan=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate is less than UPDATED_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.lessThan=" + UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByTerminalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        // Get all the leaseContractList where terminalDate is greater than DEFAULT_TERMINAL_DATE
        defaultLeaseContractShouldNotBeFound("terminalDate.greaterThan=" + DEFAULT_TERMINAL_DATE);

        // Get all the leaseContractList where terminalDate is greater than SMALLER_TERMINAL_DATE
        defaultLeaseContractShouldBeFound("terminalDate.greaterThan=" + SMALLER_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseContractsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);
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
        leaseContract.addPlaceholder(placeholder);
        leaseContractRepository.saveAndFlush(leaseContract);
        Long placeholderId = placeholder.getId();

        // Get all the leaseContractList where placeholder equals to placeholderId
        defaultLeaseContractShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the leaseContractList where placeholder equals to (placeholderId + 1)
        defaultLeaseContractShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseContractsBySystemMappingsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);
        UniversallyUniqueMapping systemMappings;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            systemMappings = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(systemMappings);
            em.flush();
        } else {
            systemMappings = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(systemMappings);
        em.flush();
        leaseContract.addSystemMappings(systemMappings);
        leaseContractRepository.saveAndFlush(leaseContract);
        Long systemMappingsId = systemMappings.getId();

        // Get all the leaseContractList where systemMappings equals to systemMappingsId
        defaultLeaseContractShouldBeFound("systemMappingsId.equals=" + systemMappingsId);

        // Get all the leaseContractList where systemMappings equals to (systemMappingsId + 1)
        defaultLeaseContractShouldNotBeFound("systemMappingsId.equals=" + (systemMappingsId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseContractsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        leaseContract.addBusinessDocument(businessDocument);
        leaseContractRepository.saveAndFlush(leaseContract);
        Long businessDocumentId = businessDocument.getId();

        // Get all the leaseContractList where businessDocument equals to businessDocumentId
        defaultLeaseContractShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the leaseContractList where businessDocument equals to (businessDocumentId + 1)
        defaultLeaseContractShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseContractsByContractMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);
        ContractMetadata contractMetadata;
        if (TestUtil.findAll(em, ContractMetadata.class).isEmpty()) {
            contractMetadata = ContractMetadataResourceIT.createEntity(em);
            em.persist(contractMetadata);
            em.flush();
        } else {
            contractMetadata = TestUtil.findAll(em, ContractMetadata.class).get(0);
        }
        em.persist(contractMetadata);
        em.flush();
        leaseContract.addContractMetadata(contractMetadata);
        leaseContractRepository.saveAndFlush(leaseContract);
        Long contractMetadataId = contractMetadata.getId();

        // Get all the leaseContractList where contractMetadata equals to contractMetadataId
        defaultLeaseContractShouldBeFound("contractMetadataId.equals=" + contractMetadataId);

        // Get all the leaseContractList where contractMetadata equals to (contractMetadataId + 1)
        defaultLeaseContractShouldNotBeFound("contractMetadataId.equals=" + (contractMetadataId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseContractShouldBeFound(String filter) throws Exception {
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())));

        // Check, that the count call also returns 1
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseContractShouldNotBeFound(String filter) throws Exception {
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseContract() throws Exception {
        // Get the leaseContract
        restLeaseContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseContract() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();

        // Update the leaseContract
        LeaseContract updatedLeaseContract = leaseContractRepository.findById(leaseContract.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseContract are not directly saved in db
        em.detach(updatedLeaseContract);
        updatedLeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .identifier(UPDATED_IDENTIFIER)
            .description(UPDATED_DESCRIPTION)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE);
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(updatedLeaseContract);

        restLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);
        LeaseContract testLeaseContract = leaseContractList.get(leaseContractList.size() - 1);
        assertThat(testLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testLeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testLeaseContract.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testLeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseContract.getTerminalDate()).isEqualTo(UPDATED_TERMINAL_DATE);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository).save(testLeaseContract);
    }

    @Test
    @Transactional
    void putNonExistingLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void partialUpdateLeaseContractWithPatch() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();

        // Update the leaseContract using partial update
        LeaseContract partialUpdatedLeaseContract = new LeaseContract();
        partialUpdatedLeaseContract.setId(leaseContract.getId());

        partialUpdatedLeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .identifier(UPDATED_IDENTIFIER)
            .commencementDate(UPDATED_COMMENCEMENT_DATE);

        restLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);
        LeaseContract testLeaseContract = leaseContractList.get(leaseContractList.size() - 1);
        assertThat(testLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testLeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testLeaseContract.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testLeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseContract.getTerminalDate()).isEqualTo(DEFAULT_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseContractWithPatch() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();

        // Update the leaseContract using partial update
        LeaseContract partialUpdatedLeaseContract = new LeaseContract();
        partialUpdatedLeaseContract.setId(leaseContract.getId());

        partialUpdatedLeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .identifier(UPDATED_IDENTIFIER)
            .description(UPDATED_DESCRIPTION)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .terminalDate(UPDATED_TERMINAL_DATE);

        restLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);
        LeaseContract testLeaseContract = leaseContractList.get(leaseContractList.size() - 1);
        assertThat(testLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testLeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testLeaseContract.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testLeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseContract.getTerminalDate()).isEqualTo(UPDATED_TERMINAL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseContractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = leaseContractRepository.findAll().size();
        leaseContract.setId(count.incrementAndGet());

        // Create the LeaseContract
        LeaseContractDTO leaseContractDTO = leaseContractMapper.toDto(leaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseContract in the database
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(0)).save(leaseContract);
    }

    @Test
    @Transactional
    void deleteLeaseContract() throws Exception {
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);

        int databaseSizeBeforeDelete = leaseContractRepository.findAll().size();

        // Delete the leaseContract
        restLeaseContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseContract> leaseContractList = leaseContractRepository.findAll();
        assertThat(leaseContractList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseContract in Elasticsearch
        verify(mockLeaseContractSearchRepository, times(1)).deleteById(leaseContract.getId());
    }

    @Test
    @Transactional
    void searchLeaseContract() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseContractRepository.saveAndFlush(leaseContract);
        when(mockLeaseContractSearchRepository.search("id:" + leaseContract.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseContract), PageRequest.of(0, 1), 1));

        // Search the leaseContract
        restLeaseContractMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalDate").value(hasItem(DEFAULT_TERMINAL_DATE.toString())));
    }
}
