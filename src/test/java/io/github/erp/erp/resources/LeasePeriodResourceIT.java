package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeasePeriodRepository;
import io.github.erp.repository.search.LeasePeriodSearchRepository;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.mapper.LeasePeriodMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the LeasePeriodResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeasePeriodResourceIT {

    private static final Long DEFAULT_SEQUENCE_NUMBER = 1L;
    private static final Long UPDATED_SEQUENCE_NUMBER = 2L;
    private static final Long SMALLER_SEQUENCE_NUMBER = 1L - 1L;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PERIOD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leases/lease-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-periods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeasePeriodRepository leasePeriodRepository;

    @Autowired
    private LeasePeriodMapper leasePeriodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeasePeriodSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeasePeriodSearchRepository mockLeasePeriodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeasePeriodMockMvc;

    private LeasePeriod leasePeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasePeriod createEntity(EntityManager em) {
        LeasePeriod leasePeriod = new LeasePeriod()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .periodCode(DEFAULT_PERIOD_CODE);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        leasePeriod.setFiscalMonth(fiscalMonth);
        return leasePeriod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasePeriod createUpdatedEntity(EntityManager em) {
        LeasePeriod leasePeriod = new LeasePeriod()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        leasePeriod.setFiscalMonth(fiscalMonth);
        return leasePeriod;
    }

    @BeforeEach
    public void initTest() {
        leasePeriod = createEntity(em);
    }

    @Test
    @Transactional
    void createLeasePeriod() throws Exception {
        int databaseSizeBeforeCreate = leasePeriodRepository.findAll().size();
        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);
        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeCreate + 1);
        LeasePeriod testLeasePeriod = leasePeriodList.get(leasePeriodList.size() - 1);
        assertThat(testLeasePeriod.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testLeasePeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeasePeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeasePeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(1)).save(testLeasePeriod);
    }

    @Test
    @Transactional
    void createLeasePeriodWithExistingId() throws Exception {
        // Create the LeasePeriod with an existing ID
        leasePeriod.setId(1L);
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        int databaseSizeBeforeCreate = leasePeriodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasePeriodRepository.findAll().size();
        // set the field null
        leasePeriod.setSequenceNumber(null);

        // Create the LeasePeriod, which fails.
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasePeriodRepository.findAll().size();
        // set the field null
        leasePeriod.setStartDate(null);

        // Create the LeasePeriod, which fails.
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasePeriodRepository.findAll().size();
        // set the field null
        leasePeriod.setEndDate(null);

        // Create the LeasePeriod, which fails.
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasePeriodRepository.findAll().size();
        // set the field null
        leasePeriod.setPeriodCode(null);

        // Create the LeasePeriod, which fails.
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        restLeasePeriodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeasePeriods() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }

    @Test
    @Transactional
    void getLeasePeriod() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get the leasePeriod
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, leasePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leasePeriod.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.periodCode").value(DEFAULT_PERIOD_CODE));
    }

    @Test
    @Transactional
    void getLeasePeriodsByIdFiltering() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        Long id = leasePeriod.getId();

        defaultLeasePeriodShouldBeFound("id.equals=" + id);
        defaultLeasePeriodShouldNotBeFound("id.notEquals=" + id);

        defaultLeasePeriodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeasePeriodShouldNotBeFound("id.greaterThan=" + id);

        defaultLeasePeriodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeasePeriodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber is not null
        defaultLeasePeriodShouldBeFound("sequenceNumber.specified=true");

        // Get all the leasePeriodList where sequenceNumber is null
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultLeasePeriodShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leasePeriodList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultLeasePeriodShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate equals to DEFAULT_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate equals to UPDATED_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate not equals to DEFAULT_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate not equals to UPDATED_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the leasePeriodList where startDate equals to UPDATED_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate is not null
        defaultLeasePeriodShouldBeFound("startDate.specified=true");

        // Get all the leasePeriodList where startDate is null
        defaultLeasePeriodShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate is greater than or equal to UPDATED_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate is less than or equal to DEFAULT_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate is less than or equal to SMALLER_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate is less than DEFAULT_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate is less than UPDATED_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where startDate is greater than DEFAULT_START_DATE
        defaultLeasePeriodShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the leasePeriodList where startDate is greater than SMALLER_START_DATE
        defaultLeasePeriodShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate equals to DEFAULT_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate equals to UPDATED_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate not equals to DEFAULT_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate not equals to UPDATED_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leasePeriodList where endDate equals to UPDATED_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate is not null
        defaultLeasePeriodShouldBeFound("endDate.specified=true");

        // Get all the leasePeriodList where endDate is null
        defaultLeasePeriodShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate is greater than or equal to UPDATED_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate is less than or equal to DEFAULT_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate is less than or equal to SMALLER_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate is less than DEFAULT_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate is less than UPDATED_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where endDate is greater than DEFAULT_END_DATE
        defaultLeasePeriodShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the leasePeriodList where endDate is greater than SMALLER_END_DATE
        defaultLeasePeriodShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode equals to DEFAULT_PERIOD_CODE
        defaultLeasePeriodShouldBeFound("periodCode.equals=" + DEFAULT_PERIOD_CODE);

        // Get all the leasePeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldNotBeFound("periodCode.equals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode not equals to DEFAULT_PERIOD_CODE
        defaultLeasePeriodShouldNotBeFound("periodCode.notEquals=" + DEFAULT_PERIOD_CODE);

        // Get all the leasePeriodList where periodCode not equals to UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldBeFound("periodCode.notEquals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode in DEFAULT_PERIOD_CODE or UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldBeFound("periodCode.in=" + DEFAULT_PERIOD_CODE + "," + UPDATED_PERIOD_CODE);

        // Get all the leasePeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldNotBeFound("periodCode.in=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode is not null
        defaultLeasePeriodShouldBeFound("periodCode.specified=true");

        // Get all the leasePeriodList where periodCode is null
        defaultLeasePeriodShouldNotBeFound("periodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeContainsSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode contains DEFAULT_PERIOD_CODE
        defaultLeasePeriodShouldBeFound("periodCode.contains=" + DEFAULT_PERIOD_CODE);

        // Get all the leasePeriodList where periodCode contains UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldNotBeFound("periodCode.contains=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByPeriodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        // Get all the leasePeriodList where periodCode does not contain DEFAULT_PERIOD_CODE
        defaultLeasePeriodShouldNotBeFound("periodCode.doesNotContain=" + DEFAULT_PERIOD_CODE);

        // Get all the leasePeriodList where periodCode does not contain UPDATED_PERIOD_CODE
        defaultLeasePeriodShouldBeFound("periodCode.doesNotContain=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeasePeriodsByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);
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
        leasePeriod.setFiscalMonth(fiscalMonth);
        leasePeriodRepository.saveAndFlush(leasePeriod);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the leasePeriodList where fiscalMonth equals to fiscalMonthId
        defaultLeasePeriodShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the leasePeriodList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultLeasePeriodShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeasePeriodShouldBeFound(String filter) throws Exception {
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));

        // Check, that the count call also returns 1
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeasePeriodShouldNotBeFound(String filter) throws Exception {
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeasePeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeasePeriod() throws Exception {
        // Get the leasePeriod
        restLeasePeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeasePeriod() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();

        // Update the leasePeriod
        LeasePeriod updatedLeasePeriod = leasePeriodRepository.findById(leasePeriod.getId()).get();
        // Disconnect from session so that the updates on updatedLeasePeriod are not directly saved in db
        em.detach(updatedLeasePeriod);
        updatedLeasePeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(updatedLeasePeriod);

        restLeasePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leasePeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);
        LeasePeriod testLeasePeriod = leasePeriodList.get(leasePeriodList.size() - 1);
        assertThat(testLeasePeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeasePeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeasePeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeasePeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository).save(testLeasePeriod);
    }

    @Test
    @Transactional
    void putNonExistingLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leasePeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void partialUpdateLeasePeriodWithPatch() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();

        // Update the leasePeriod using partial update
        LeasePeriod partialUpdatedLeasePeriod = new LeasePeriod();
        partialUpdatedLeasePeriod.setId(leasePeriod.getId());

        partialUpdatedLeasePeriod.sequenceNumber(UPDATED_SEQUENCE_NUMBER).startDate(UPDATED_START_DATE);

        restLeasePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeasePeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeasePeriod))
            )
            .andExpect(status().isOk());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);
        LeasePeriod testLeasePeriod = leasePeriodList.get(leasePeriodList.size() - 1);
        assertThat(testLeasePeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeasePeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeasePeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeasePeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);
    }

    @Test
    @Transactional
    void fullUpdateLeasePeriodWithPatch() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();

        // Update the leasePeriod using partial update
        LeasePeriod partialUpdatedLeasePeriod = new LeasePeriod();
        partialUpdatedLeasePeriod.setId(leasePeriod.getId());

        partialUpdatedLeasePeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);

        restLeasePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeasePeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeasePeriod))
            )
            .andExpect(status().isOk());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);
        LeasePeriod testLeasePeriod = leasePeriodList.get(leasePeriodList.size() - 1);
        assertThat(testLeasePeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeasePeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeasePeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeasePeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leasePeriodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeasePeriod() throws Exception {
        int databaseSizeBeforeUpdate = leasePeriodRepository.findAll().size();
        leasePeriod.setId(count.incrementAndGet());

        // Create the LeasePeriod
        LeasePeriodDTO leasePeriodDTO = leasePeriodMapper.toDto(leasePeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePeriodMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leasePeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeasePeriod in the database
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(0)).save(leasePeriod);
    }

    @Test
    @Transactional
    void deleteLeasePeriod() throws Exception {
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);

        int databaseSizeBeforeDelete = leasePeriodRepository.findAll().size();

        // Delete the leasePeriod
        restLeasePeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, leasePeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeasePeriod> leasePeriodList = leasePeriodRepository.findAll();
        assertThat(leasePeriodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeasePeriod in Elasticsearch
        verify(mockLeasePeriodSearchRepository, times(1)).deleteById(leasePeriod.getId());
    }

    @Test
    @Transactional
    void searchLeasePeriod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leasePeriodRepository.saveAndFlush(leasePeriod);
        when(mockLeasePeriodSearchRepository.search("id:" + leasePeriod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leasePeriod), PageRequest.of(0, 1), 1));

        // Search the leasePeriod
        restLeasePeriodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leasePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }
}
