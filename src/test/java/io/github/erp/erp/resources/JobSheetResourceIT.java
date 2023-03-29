package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
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
import io.github.erp.repository.JobSheetRepository;
import io.github.erp.repository.search.JobSheetSearchRepository;
import io.github.erp.service.JobSheetService;
import io.github.erp.service.dto.JobSheetDTO;
import io.github.erp.service.mapper.JobSheetMapper;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JobSheetResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PAYMENTS_USER", "FIXED_ASSETS_USER"})
public class JobSheetResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_JOB_SHEET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOB_SHEET_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_JOB_SHEET_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments/job-sheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search/job-sheets";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobSheetRepository jobSheetRepository;

    @Mock
    private JobSheetRepository jobSheetRepositoryMock;

    @Autowired
    private JobSheetMapper jobSheetMapper;

    @Mock
    private JobSheetService jobSheetServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.JobSheetSearchRepositoryMockConfiguration
     */
    @Autowired
    private JobSheetSearchRepository mockJobSheetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobSheetMockMvc;

    private JobSheet jobSheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSheet createEntity(EntityManager em) {
        JobSheet jobSheet = new JobSheet()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .jobSheetDate(DEFAULT_JOB_SHEET_DATE)
            .details(DEFAULT_DETAILS)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        jobSheet.setBiller(dealer);
        return jobSheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSheet createUpdatedEntity(EntityManager em) {
        JobSheet jobSheet = new JobSheet()
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .jobSheetDate(UPDATED_JOB_SHEET_DATE)
            .details(UPDATED_DETAILS)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        jobSheet.setBiller(dealer);
        return jobSheet;
    }

    @BeforeEach
    public void initTest() {
        jobSheet = createEntity(em);
    }

    @Test
    @Transactional
    void createJobSheet() throws Exception {
        int databaseSizeBeforeCreate = jobSheetRepository.findAll().size();
        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);
        restJobSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSheetDTO)))
            .andExpect(status().isCreated());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeCreate + 1);
        JobSheet testJobSheet = jobSheetList.get(jobSheetList.size() - 1);
        assertThat(testJobSheet.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testJobSheet.getJobSheetDate()).isEqualTo(DEFAULT_JOB_SHEET_DATE);
        assertThat(testJobSheet.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testJobSheet.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(1)).save(testJobSheet);
    }

    @Test
    @Transactional
    void createJobSheetWithExistingId() throws Exception {
        // Create the JobSheet with an existing ID
        jobSheet.setId(1L);
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        int databaseSizeBeforeCreate = jobSheetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeCreate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobSheetRepository.findAll().size();
        // set the field null
        jobSheet.setSerialNumber(null);

        // Create the JobSheet, which fails.
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        restJobSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSheetDTO)))
            .andExpect(status().isBadRequest());

        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobSheets() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].jobSheetDate").value(hasItem(DEFAULT_JOB_SHEET_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobSheetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobSheetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobSheetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobSheetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobSheetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobSheetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobSheetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobSheetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJobSheet() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get the jobSheet
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL_ID, jobSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobSheet.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.jobSheetDate").value(DEFAULT_JOB_SHEET_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getJobSheetsByIdFiltering() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        Long id = jobSheet.getId();

        defaultJobSheetShouldBeFound("id.equals=" + id);
        defaultJobSheetShouldNotBeFound("id.notEquals=" + id);

        defaultJobSheetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobSheetShouldNotBeFound("id.greaterThan=" + id);

        defaultJobSheetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobSheetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultJobSheetShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the jobSheetList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultJobSheetShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the jobSheetList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the jobSheetList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber is not null
        defaultJobSheetShouldBeFound("serialNumber.specified=true");

        // Get all the jobSheetList where serialNumber is null
        defaultJobSheetShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultJobSheetShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the jobSheetList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllJobSheetsBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultJobSheetShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the jobSheetList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultJobSheetShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate equals to DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.equals=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate equals to UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.equals=" + UPDATED_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate not equals to DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.notEquals=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate not equals to UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.notEquals=" + UPDATED_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsInShouldWork() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate in DEFAULT_JOB_SHEET_DATE or UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.in=" + DEFAULT_JOB_SHEET_DATE + "," + UPDATED_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate equals to UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.in=" + UPDATED_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate is not null
        defaultJobSheetShouldBeFound("jobSheetDate.specified=true");

        // Get all the jobSheetList where jobSheetDate is null
        defaultJobSheetShouldNotBeFound("jobSheetDate.specified=false");
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate is greater than or equal to DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.greaterThanOrEqual=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate is greater than or equal to UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.greaterThanOrEqual=" + UPDATED_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate is less than or equal to DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.lessThanOrEqual=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate is less than or equal to SMALLER_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.lessThanOrEqual=" + SMALLER_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsLessThanSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate is less than DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.lessThan=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate is less than UPDATED_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.lessThan=" + UPDATED_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByJobSheetDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where jobSheetDate is greater than DEFAULT_JOB_SHEET_DATE
        defaultJobSheetShouldNotBeFound("jobSheetDate.greaterThan=" + DEFAULT_JOB_SHEET_DATE);

        // Get all the jobSheetList where jobSheetDate is greater than SMALLER_JOB_SHEET_DATE
        defaultJobSheetShouldBeFound("jobSheetDate.greaterThan=" + SMALLER_JOB_SHEET_DATE);
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details equals to DEFAULT_DETAILS
        defaultJobSheetShouldBeFound("details.equals=" + DEFAULT_DETAILS);

        // Get all the jobSheetList where details equals to UPDATED_DETAILS
        defaultJobSheetShouldNotBeFound("details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details not equals to DEFAULT_DETAILS
        defaultJobSheetShouldNotBeFound("details.notEquals=" + DEFAULT_DETAILS);

        // Get all the jobSheetList where details not equals to UPDATED_DETAILS
        defaultJobSheetShouldBeFound("details.notEquals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details in DEFAULT_DETAILS or UPDATED_DETAILS
        defaultJobSheetShouldBeFound("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS);

        // Get all the jobSheetList where details equals to UPDATED_DETAILS
        defaultJobSheetShouldNotBeFound("details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details is not null
        defaultJobSheetShouldBeFound("details.specified=true");

        // Get all the jobSheetList where details is null
        defaultJobSheetShouldNotBeFound("details.specified=false");
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details contains DEFAULT_DETAILS
        defaultJobSheetShouldBeFound("details.contains=" + DEFAULT_DETAILS);

        // Get all the jobSheetList where details contains UPDATED_DETAILS
        defaultJobSheetShouldNotBeFound("details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllJobSheetsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        // Get all the jobSheetList where details does not contain DEFAULT_DETAILS
        defaultJobSheetShouldNotBeFound("details.doesNotContain=" + DEFAULT_DETAILS);

        // Get all the jobSheetList where details does not contain UPDATED_DETAILS
        defaultJobSheetShouldBeFound("details.doesNotContain=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllJobSheetsByBillerIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        Dealer biller;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            biller = DealerResourceIT.createEntity(em);
            em.persist(biller);
            em.flush();
        } else {
            biller = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(biller);
        em.flush();
        jobSheet.setBiller(biller);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long billerId = biller.getId();

        // Get all the jobSheetList where biller equals to billerId
        defaultJobSheetShouldBeFound("billerId.equals=" + billerId);

        // Get all the jobSheetList where biller equals to (billerId + 1)
        defaultJobSheetShouldNotBeFound("billerId.equals=" + (billerId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsBySignatoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        Dealer signatories;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            signatories = DealerResourceIT.createEntity(em);
            em.persist(signatories);
            em.flush();
        } else {
            signatories = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(signatories);
        em.flush();
        jobSheet.addSignatories(signatories);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long signatoriesId = signatories.getId();

        // Get all the jobSheetList where signatories equals to signatoriesId
        defaultJobSheetShouldBeFound("signatoriesId.equals=" + signatoriesId);

        // Get all the jobSheetList where signatories equals to (signatoriesId + 1)
        defaultJobSheetShouldNotBeFound("signatoriesId.equals=" + (signatoriesId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsByContactPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        Dealer contactPerson;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            contactPerson = DealerResourceIT.createEntity(em);
            em.persist(contactPerson);
            em.flush();
        } else {
            contactPerson = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(contactPerson);
        em.flush();
        jobSheet.setContactPerson(contactPerson);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long contactPersonId = contactPerson.getId();

        // Get all the jobSheetList where contactPerson equals to contactPersonId
        defaultJobSheetShouldBeFound("contactPersonId.equals=" + contactPersonId);

        // Get all the jobSheetList where contactPerson equals to (contactPersonId + 1)
        defaultJobSheetShouldNotBeFound("contactPersonId.equals=" + (contactPersonId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsByBusinessStampsIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        BusinessStamp businessStamps;
        if (TestUtil.findAll(em, BusinessStamp.class).isEmpty()) {
            businessStamps = BusinessStampResourceIT.createEntity(em);
            em.persist(businessStamps);
            em.flush();
        } else {
            businessStamps = TestUtil.findAll(em, BusinessStamp.class).get(0);
        }
        em.persist(businessStamps);
        em.flush();
        jobSheet.addBusinessStamps(businessStamps);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long businessStampsId = businessStamps.getId();

        // Get all the jobSheetList where businessStamps equals to businessStampsId
        defaultJobSheetShouldBeFound("businessStampsId.equals=" + businessStampsId);

        // Get all the jobSheetList where businessStamps equals to (businessStampsId + 1)
        defaultJobSheetShouldNotBeFound("businessStampsId.equals=" + (businessStampsId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
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
        jobSheet.addPlaceholder(placeholder);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long placeholderId = placeholder.getId();

        // Get all the jobSheetList where placeholder equals to placeholderId
        defaultJobSheetShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the jobSheetList where placeholder equals to (placeholderId + 1)
        defaultJobSheetShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        PaymentLabel paymentLabel;
        if (TestUtil.findAll(em, PaymentLabel.class).isEmpty()) {
            paymentLabel = PaymentLabelResourceIT.createEntity(em);
            em.persist(paymentLabel);
            em.flush();
        } else {
            paymentLabel = TestUtil.findAll(em, PaymentLabel.class).get(0);
        }
        em.persist(paymentLabel);
        em.flush();
        jobSheet.addPaymentLabel(paymentLabel);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the jobSheetList where paymentLabel equals to paymentLabelId
        defaultJobSheetShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the jobSheetList where paymentLabel equals to (paymentLabelId + 1)
        defaultJobSheetShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllJobSheetsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
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
        jobSheet.addBusinessDocument(businessDocument);
        jobSheetRepository.saveAndFlush(jobSheet);
        Long businessDocumentId = businessDocument.getId();

        // Get all the jobSheetList where businessDocument equals to businessDocumentId
        defaultJobSheetShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the jobSheetList where businessDocument equals to (businessDocumentId + 1)
        defaultJobSheetShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobSheetShouldBeFound(String filter) throws Exception {
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].jobSheetDate").value(hasItem(DEFAULT_JOB_SHEET_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobSheetShouldNotBeFound(String filter) throws Exception {
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobSheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJobSheet() throws Exception {
        // Get the jobSheet
        restJobSheetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobSheet() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();

        // Update the jobSheet
        JobSheet updatedJobSheet = jobSheetRepository.findById(jobSheet.getId()).get();
        // Disconnect from session so that the updates on updatedJobSheet are not directly saved in db
        em.detach(updatedJobSheet);
        updatedJobSheet
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .jobSheetDate(UPDATED_JOB_SHEET_DATE)
            .details(UPDATED_DETAILS)
            .remarks(UPDATED_REMARKS);
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(updatedJobSheet);

        restJobSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobSheetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);
        JobSheet testJobSheet = jobSheetList.get(jobSheetList.size() - 1);
        assertThat(testJobSheet.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testJobSheet.getJobSheetDate()).isEqualTo(UPDATED_JOB_SHEET_DATE);
        assertThat(testJobSheet.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testJobSheet.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository).save(testJobSheet);
    }

    @Test
    @Transactional
    void putNonExistingJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobSheetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    // @Test
    @Transactional
    void putWithMissingIdPathParamJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSheetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    @Test
    @Transactional
    void partialUpdateJobSheetWithPatch() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();

        // Update the jobSheet using partial update
        JobSheet partialUpdatedJobSheet = new JobSheet();
        partialUpdatedJobSheet.setId(jobSheet.getId());

        partialUpdatedJobSheet.details(UPDATED_DETAILS);

        restJobSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSheet))
            )
            .andExpect(status().isOk());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);
        JobSheet testJobSheet = jobSheetList.get(jobSheetList.size() - 1);
        assertThat(testJobSheet.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testJobSheet.getJobSheetDate()).isEqualTo(DEFAULT_JOB_SHEET_DATE);
        assertThat(testJobSheet.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testJobSheet.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateJobSheetWithPatch() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();

        // Update the jobSheet using partial update
        JobSheet partialUpdatedJobSheet = new JobSheet();
        partialUpdatedJobSheet.setId(jobSheet.getId());

        partialUpdatedJobSheet
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .jobSheetDate(UPDATED_JOB_SHEET_DATE)
            .details(UPDATED_DETAILS)
            .remarks(UPDATED_REMARKS);

        restJobSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSheet))
            )
            .andExpect(status().isOk());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);
        JobSheet testJobSheet = jobSheetList.get(jobSheetList.size() - 1);
        assertThat(testJobSheet.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testJobSheet.getJobSheetDate()).isEqualTo(UPDATED_JOB_SHEET_DATE);
        assertThat(testJobSheet.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testJobSheet.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobSheetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    // @Test
    @Transactional
    void patchWithMissingIdPathParamJobSheet() throws Exception {
        int databaseSizeBeforeUpdate = jobSheetRepository.findAll().size();
        jobSheet.setId(count.incrementAndGet());

        // Create the JobSheet
        JobSheetDTO jobSheetDTO = jobSheetMapper.toDto(jobSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSheetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobSheetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobSheet in the database
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(0)).save(jobSheet);
    }

    @Test
    @Transactional
    void deleteJobSheet() throws Exception {
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);

        int databaseSizeBeforeDelete = jobSheetRepository.findAll().size();

        // Delete the jobSheet
        restJobSheetMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobSheet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobSheet> jobSheetList = jobSheetRepository.findAll();
        assertThat(jobSheetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JobSheet in Elasticsearch
        verify(mockJobSheetSearchRepository, times(1)).deleteById(jobSheet.getId());
    }

    @Test
    @Transactional
    void searchJobSheet() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jobSheetRepository.saveAndFlush(jobSheet);
        when(mockJobSheetSearchRepository.search("id:" + jobSheet.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(jobSheet), PageRequest.of(0, 1), 1));

        // Search the jobSheet
        restJobSheetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + jobSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].jobSheetDate").value(hasItem(DEFAULT_JOB_SHEET_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
