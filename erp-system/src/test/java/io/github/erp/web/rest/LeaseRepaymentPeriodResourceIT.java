package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.repository.search.LeaseRepaymentPeriodSearchRepository;
import io.github.erp.service.criteria.LeaseRepaymentPeriodCriteria;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import io.github.erp.service.mapper.LeaseRepaymentPeriodMapper;
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
 * Integration tests for the {@link LeaseRepaymentPeriodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeaseRepaymentPeriodResourceIT {

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

    private static final String ENTITY_API_URL = "/api/lease-repayment-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lease-repayment-periods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;

    @Autowired
    private LeaseRepaymentPeriodMapper leaseRepaymentPeriodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseRepaymentPeriodSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseRepaymentPeriodSearchRepository mockLeaseRepaymentPeriodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseRepaymentPeriodMockMvc;

    private LeaseRepaymentPeriod leaseRepaymentPeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseRepaymentPeriod createEntity(EntityManager em) {
        LeaseRepaymentPeriod leaseRepaymentPeriod = new LeaseRepaymentPeriod()
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
        leaseRepaymentPeriod.setFiscalMonth(fiscalMonth);
        return leaseRepaymentPeriod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseRepaymentPeriod createUpdatedEntity(EntityManager em) {
        LeaseRepaymentPeriod leaseRepaymentPeriod = new LeaseRepaymentPeriod()
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
        leaseRepaymentPeriod.setFiscalMonth(fiscalMonth);
        return leaseRepaymentPeriod;
    }

    @BeforeEach
    public void initTest() {
        leaseRepaymentPeriod = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeCreate = leaseRepaymentPeriodRepository.findAll().size();
        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);
        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseRepaymentPeriod testLeaseRepaymentPeriod = leaseRepaymentPeriodList.get(leaseRepaymentPeriodList.size() - 1);
        assertThat(testLeaseRepaymentPeriod.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testLeaseRepaymentPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaseRepaymentPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeaseRepaymentPeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(1)).save(testLeaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void createLeaseRepaymentPeriodWithExistingId() throws Exception {
        // Create the LeaseRepaymentPeriod with an existing ID
        leaseRepaymentPeriod.setId(1L);
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        int databaseSizeBeforeCreate = leaseRepaymentPeriodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseRepaymentPeriodRepository.findAll().size();
        // set the field null
        leaseRepaymentPeriod.setSequenceNumber(null);

        // Create the LeaseRepaymentPeriod, which fails.
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseRepaymentPeriodRepository.findAll().size();
        // set the field null
        leaseRepaymentPeriod.setStartDate(null);

        // Create the LeaseRepaymentPeriod, which fails.
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseRepaymentPeriodRepository.findAll().size();
        // set the field null
        leaseRepaymentPeriod.setEndDate(null);

        // Create the LeaseRepaymentPeriod, which fails.
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseRepaymentPeriodRepository.findAll().size();
        // set the field null
        leaseRepaymentPeriod.setPeriodCode(null);

        // Create the LeaseRepaymentPeriod, which fails.
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriods() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseRepaymentPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }

    @Test
    @Transactional
    void getLeaseRepaymentPeriod() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get the leaseRepaymentPeriod
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseRepaymentPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseRepaymentPeriod.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.periodCode").value(DEFAULT_PERIOD_CODE));
    }

    @Test
    @Transactional
    void getLeaseRepaymentPeriodsByIdFiltering() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        Long id = leaseRepaymentPeriod.getId();

        defaultLeaseRepaymentPeriodShouldBeFound("id.equals=" + id);
        defaultLeaseRepaymentPeriodShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseRepaymentPeriodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseRepaymentPeriodShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseRepaymentPeriodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseRepaymentPeriodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is not null
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.specified=true");

        // Get all the leaseRepaymentPeriodList where sequenceNumber is null
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the leaseRepaymentPeriodList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultLeaseRepaymentPeriodShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate equals to DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate equals to UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate not equals to DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate not equals to UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate equals to UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate is not null
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.specified=true");

        // Get all the leaseRepaymentPeriodList where startDate is null
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate is greater than or equal to UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate is less than or equal to DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate is less than or equal to SMALLER_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate is less than DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate is less than UPDATED_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where startDate is greater than DEFAULT_START_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the leaseRepaymentPeriodList where startDate is greater than SMALLER_START_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate equals to DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate equals to UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate not equals to DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate not equals to UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate equals to UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate is not null
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.specified=true");

        // Get all the leaseRepaymentPeriodList where endDate is null
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate is greater than or equal to UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate is less than or equal to DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate is less than or equal to SMALLER_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate is less than DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate is less than UPDATED_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where endDate is greater than DEFAULT_END_DATE
        defaultLeaseRepaymentPeriodShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the leaseRepaymentPeriodList where endDate is greater than SMALLER_END_DATE
        defaultLeaseRepaymentPeriodShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode equals to DEFAULT_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.equals=" + DEFAULT_PERIOD_CODE);

        // Get all the leaseRepaymentPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.equals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode not equals to DEFAULT_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.notEquals=" + DEFAULT_PERIOD_CODE);

        // Get all the leaseRepaymentPeriodList where periodCode not equals to UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.notEquals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode in DEFAULT_PERIOD_CODE or UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.in=" + DEFAULT_PERIOD_CODE + "," + UPDATED_PERIOD_CODE);

        // Get all the leaseRepaymentPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.in=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode is not null
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.specified=true");

        // Get all the leaseRepaymentPeriodList where periodCode is null
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeContainsSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode contains DEFAULT_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.contains=" + DEFAULT_PERIOD_CODE);

        // Get all the leaseRepaymentPeriodList where periodCode contains UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.contains=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByPeriodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        // Get all the leaseRepaymentPeriodList where periodCode does not contain DEFAULT_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldNotBeFound("periodCode.doesNotContain=" + DEFAULT_PERIOD_CODE);

        // Get all the leaseRepaymentPeriodList where periodCode does not contain UPDATED_PERIOD_CODE
        defaultLeaseRepaymentPeriodShouldBeFound("periodCode.doesNotContain=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllLeaseRepaymentPeriodsByFiscalMonthIsEqualToSomething() throws Exception {
        // Get already existing entity
        FiscalMonth fiscalMonth = leaseRepaymentPeriod.getFiscalMonth();
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the leaseRepaymentPeriodList where fiscalMonth equals to fiscalMonthId
        defaultLeaseRepaymentPeriodShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the leaseRepaymentPeriodList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultLeaseRepaymentPeriodShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseRepaymentPeriodShouldBeFound(String filter) throws Exception {
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseRepaymentPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));

        // Check, that the count call also returns 1
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseRepaymentPeriodShouldNotBeFound(String filter) throws Exception {
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseRepaymentPeriod() throws Exception {
        // Get the leaseRepaymentPeriod
        restLeaseRepaymentPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseRepaymentPeriod() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();

        // Update the leaseRepaymentPeriod
        LeaseRepaymentPeriod updatedLeaseRepaymentPeriod = leaseRepaymentPeriodRepository.findById(leaseRepaymentPeriod.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseRepaymentPeriod are not directly saved in db
        em.detach(updatedLeaseRepaymentPeriod);
        updatedLeaseRepaymentPeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(updatedLeaseRepaymentPeriod);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseRepaymentPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);
        LeaseRepaymentPeriod testLeaseRepaymentPeriod = leaseRepaymentPeriodList.get(leaseRepaymentPeriodList.size() - 1);
        assertThat(testLeaseRepaymentPeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeaseRepaymentPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaseRepaymentPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaseRepaymentPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository).save(testLeaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void putNonExistingLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseRepaymentPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void partialUpdateLeaseRepaymentPeriodWithPatch() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();

        // Update the leaseRepaymentPeriod using partial update
        LeaseRepaymentPeriod partialUpdatedLeaseRepaymentPeriod = new LeaseRepaymentPeriod();
        partialUpdatedLeaseRepaymentPeriod.setId(leaseRepaymentPeriod.getId());

        partialUpdatedLeaseRepaymentPeriod.startDate(UPDATED_START_DATE).periodCode(UPDATED_PERIOD_CODE);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseRepaymentPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseRepaymentPeriod))
            )
            .andExpect(status().isOk());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);
        LeaseRepaymentPeriod testLeaseRepaymentPeriod = leaseRepaymentPeriodList.get(leaseRepaymentPeriodList.size() - 1);
        assertThat(testLeaseRepaymentPeriod.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testLeaseRepaymentPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaseRepaymentPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeaseRepaymentPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void fullUpdateLeaseRepaymentPeriodWithPatch() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();

        // Update the leaseRepaymentPeriod using partial update
        LeaseRepaymentPeriod partialUpdatedLeaseRepaymentPeriod = new LeaseRepaymentPeriod();
        partialUpdatedLeaseRepaymentPeriod.setId(leaseRepaymentPeriod.getId());

        partialUpdatedLeaseRepaymentPeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);

        restLeaseRepaymentPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseRepaymentPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseRepaymentPeriod))
            )
            .andExpect(status().isOk());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);
        LeaseRepaymentPeriod testLeaseRepaymentPeriod = leaseRepaymentPeriodList.get(leaseRepaymentPeriodList.size() - 1);
        assertThat(testLeaseRepaymentPeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testLeaseRepaymentPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaseRepaymentPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaseRepaymentPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseRepaymentPeriodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseRepaymentPeriod() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepaymentPeriodRepository.findAll().size();
        leaseRepaymentPeriod.setId(count.incrementAndGet());

        // Create the LeaseRepaymentPeriod
        LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO = leaseRepaymentPeriodMapper.toDto(leaseRepaymentPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseRepaymentPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseRepaymentPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseRepaymentPeriod in the database
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(0)).save(leaseRepaymentPeriod);
    }

    @Test
    @Transactional
    void deleteLeaseRepaymentPeriod() throws Exception {
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);

        int databaseSizeBeforeDelete = leaseRepaymentPeriodRepository.findAll().size();

        // Delete the leaseRepaymentPeriod
        restLeaseRepaymentPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseRepaymentPeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseRepaymentPeriod> leaseRepaymentPeriodList = leaseRepaymentPeriodRepository.findAll();
        assertThat(leaseRepaymentPeriodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseRepaymentPeriod in Elasticsearch
        verify(mockLeaseRepaymentPeriodSearchRepository, times(1)).deleteById(leaseRepaymentPeriod.getId());
    }

    @Test
    @Transactional
    void searchLeaseRepaymentPeriod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseRepaymentPeriodRepository.saveAndFlush(leaseRepaymentPeriod);
        when(mockLeaseRepaymentPeriodSearchRepository.search("id:" + leaseRepaymentPeriod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseRepaymentPeriod), PageRequest.of(0, 1), 1));

        // Search the leaseRepaymentPeriod
        restLeaseRepaymentPeriodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseRepaymentPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseRepaymentPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }
}
