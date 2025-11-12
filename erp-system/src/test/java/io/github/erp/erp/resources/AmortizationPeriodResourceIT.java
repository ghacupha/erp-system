package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.AmortizationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.repository.AmortizationPeriodRepository;
import io.github.erp.repository.search.AmortizationPeriodSearchRepository;
import io.github.erp.service.dto.AmortizationPeriodDTO;
import io.github.erp.service.mapper.AmortizationPeriodMapper;
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
 * Integration tests for the AmortizationPeriodResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
class AmortizationPeriodResourceIT {

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

    private static final String ENTITY_API_URL = "/api/prepayments/amortization-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/amortization-periods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmortizationPeriodRepository amortizationPeriodRepository;

    @Autowired
    private AmortizationPeriodMapper amortizationPeriodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AmortizationPeriodSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmortizationPeriodSearchRepository mockAmortizationPeriodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmortizationPeriodMockMvc;

    private AmortizationPeriod amortizationPeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPeriod createEntity(EntityManager em) {
        AmortizationPeriod amortizationPeriod = new AmortizationPeriod()
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
        amortizationPeriod.setFiscalMonth(fiscalMonth);
        return amortizationPeriod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPeriod createUpdatedEntity(EntityManager em) {
        AmortizationPeriod amortizationPeriod = new AmortizationPeriod()
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
        amortizationPeriod.setFiscalMonth(fiscalMonth);
        return amortizationPeriod;
    }

    @BeforeEach
    public void initTest() {
        amortizationPeriod = createEntity(em);
    }

    @Test
    @Transactional
    void createAmortizationPeriod() throws Exception {
        int databaseSizeBeforeCreate = amortizationPeriodRepository.findAll().size();
        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);
        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        AmortizationPeriod testAmortizationPeriod = amortizationPeriodList.get(amortizationPeriodList.size() - 1);
        assertThat(testAmortizationPeriod.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testAmortizationPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAmortizationPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAmortizationPeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(1)).save(testAmortizationPeriod);
    }

    @Test
    @Transactional
    void createAmortizationPeriodWithExistingId() throws Exception {
        // Create the AmortizationPeriod with an existing ID
        amortizationPeriod.setId(1L);
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        int databaseSizeBeforeCreate = amortizationPeriodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeCreate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPeriodRepository.findAll().size();
        // set the field null
        amortizationPeriod.setSequenceNumber(null);

        // Create the AmortizationPeriod, which fails.
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPeriodRepository.findAll().size();
        // set the field null
        amortizationPeriod.setStartDate(null);

        // Create the AmortizationPeriod, which fails.
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPeriodRepository.findAll().size();
        // set the field null
        amortizationPeriod.setEndDate(null);

        // Create the AmortizationPeriod, which fails.
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationPeriodRepository.findAll().size();
        // set the field null
        amortizationPeriod.setPeriodCode(null);

        // Create the AmortizationPeriod, which fails.
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        restAmortizationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriods() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }

    @Test
    @Transactional
    void getAmortizationPeriod() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get the amortizationPeriod
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, amortizationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amortizationPeriod.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.periodCode").value(DEFAULT_PERIOD_CODE));
    }

    @Test
    @Transactional
    void getAmortizationPeriodsByIdFiltering() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        Long id = amortizationPeriod.getId();

        defaultAmortizationPeriodShouldBeFound("id.equals=" + id);
        defaultAmortizationPeriodShouldNotBeFound("id.notEquals=" + id);

        defaultAmortizationPeriodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmortizationPeriodShouldNotBeFound("id.greaterThan=" + id);

        defaultAmortizationPeriodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmortizationPeriodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber is not null
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.specified=true");

        // Get all the amortizationPeriodList where sequenceNumber is null
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationPeriodList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultAmortizationPeriodShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate equals to DEFAULT_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate equals to UPDATED_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate not equals to DEFAULT_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate not equals to UPDATED_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the amortizationPeriodList where startDate equals to UPDATED_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate is not null
        defaultAmortizationPeriodShouldBeFound("startDate.specified=true");

        // Get all the amortizationPeriodList where startDate is null
        defaultAmortizationPeriodShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate is greater than or equal to UPDATED_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate is less than or equal to DEFAULT_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate is less than or equal to SMALLER_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate is less than DEFAULT_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate is less than UPDATED_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where startDate is greater than DEFAULT_START_DATE
        defaultAmortizationPeriodShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the amortizationPeriodList where startDate is greater than SMALLER_START_DATE
        defaultAmortizationPeriodShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate equals to DEFAULT_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate equals to UPDATED_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate not equals to DEFAULT_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate not equals to UPDATED_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the amortizationPeriodList where endDate equals to UPDATED_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate is not null
        defaultAmortizationPeriodShouldBeFound("endDate.specified=true");

        // Get all the amortizationPeriodList where endDate is null
        defaultAmortizationPeriodShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate is greater than or equal to UPDATED_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate is less than or equal to DEFAULT_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate is less than or equal to SMALLER_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate is less than DEFAULT_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate is less than UPDATED_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where endDate is greater than DEFAULT_END_DATE
        defaultAmortizationPeriodShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the amortizationPeriodList where endDate is greater than SMALLER_END_DATE
        defaultAmortizationPeriodShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode equals to DEFAULT_PERIOD_CODE
        defaultAmortizationPeriodShouldBeFound("periodCode.equals=" + DEFAULT_PERIOD_CODE);

        // Get all the amortizationPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldNotBeFound("periodCode.equals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode not equals to DEFAULT_PERIOD_CODE
        defaultAmortizationPeriodShouldNotBeFound("periodCode.notEquals=" + DEFAULT_PERIOD_CODE);

        // Get all the amortizationPeriodList where periodCode not equals to UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldBeFound("periodCode.notEquals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode in DEFAULT_PERIOD_CODE or UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldBeFound("periodCode.in=" + DEFAULT_PERIOD_CODE + "," + UPDATED_PERIOD_CODE);

        // Get all the amortizationPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldNotBeFound("periodCode.in=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode is not null
        defaultAmortizationPeriodShouldBeFound("periodCode.specified=true");

        // Get all the amortizationPeriodList where periodCode is null
        defaultAmortizationPeriodShouldNotBeFound("periodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeContainsSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode contains DEFAULT_PERIOD_CODE
        defaultAmortizationPeriodShouldBeFound("periodCode.contains=" + DEFAULT_PERIOD_CODE);

        // Get all the amortizationPeriodList where periodCode contains UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldNotBeFound("periodCode.contains=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByPeriodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        // Get all the amortizationPeriodList where periodCode does not contain DEFAULT_PERIOD_CODE
        defaultAmortizationPeriodShouldNotBeFound("periodCode.doesNotContain=" + DEFAULT_PERIOD_CODE);

        // Get all the amortizationPeriodList where periodCode does not contain UPDATED_PERIOD_CODE
        defaultAmortizationPeriodShouldBeFound("periodCode.doesNotContain=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllAmortizationPeriodsByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);
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
        amortizationPeriod.setFiscalMonth(fiscalMonth);
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the amortizationPeriodList where fiscalMonth equals to fiscalMonthId
        defaultAmortizationPeriodShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the amortizationPeriodList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultAmortizationPeriodShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmortizationPeriodShouldBeFound(String filter) throws Exception {
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));

        // Check, that the count call also returns 1
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmortizationPeriodShouldNotBeFound(String filter) throws Exception {
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmortizationPeriod() throws Exception {
        // Get the amortizationPeriod
        restAmortizationPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmortizationPeriod() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();

        // Update the amortizationPeriod
        AmortizationPeriod updatedAmortizationPeriod = amortizationPeriodRepository.findById(amortizationPeriod.getId()).get();
        // Disconnect from session so that the updates on updatedAmortizationPeriod are not directly saved in db
        em.detach(updatedAmortizationPeriod);
        updatedAmortizationPeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(updatedAmortizationPeriod);

        restAmortizationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPeriod testAmortizationPeriod = amortizationPeriodList.get(amortizationPeriodList.size() - 1);
        assertThat(testAmortizationPeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAmortizationPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAmortizationPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository).save(testAmortizationPeriod);
    }

    @Test
    @Transactional
    void putNonExistingAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void partialUpdateAmortizationPeriodWithPatch() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();

        // Update the amortizationPeriod using partial update
        AmortizationPeriod partialUpdatedAmortizationPeriod = new AmortizationPeriod();
        partialUpdatedAmortizationPeriod.setId(amortizationPeriod.getId());

        partialUpdatedAmortizationPeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .periodCode(UPDATED_PERIOD_CODE);

        restAmortizationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationPeriod))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPeriod testAmortizationPeriod = amortizationPeriodList.get(amortizationPeriodList.size() - 1);
        assertThat(testAmortizationPeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAmortizationPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAmortizationPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void fullUpdateAmortizationPeriodWithPatch() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();

        // Update the amortizationPeriod using partial update
        AmortizationPeriod partialUpdatedAmortizationPeriod = new AmortizationPeriod();
        partialUpdatedAmortizationPeriod.setId(amortizationPeriod.getId());

        partialUpdatedAmortizationPeriod
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .periodCode(UPDATED_PERIOD_CODE);

        restAmortizationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationPeriod))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);
        AmortizationPeriod testAmortizationPeriod = amortizationPeriodList.get(amortizationPeriodList.size() - 1);
        assertThat(testAmortizationPeriod.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAmortizationPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAmortizationPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amortizationPeriodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmortizationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = amortizationPeriodRepository.findAll().size();
        amortizationPeriod.setId(count.incrementAndGet());

        // Create the AmortizationPeriod
        AmortizationPeriodDTO amortizationPeriodDTO = amortizationPeriodMapper.toDto(amortizationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationPeriod in the database
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(0)).save(amortizationPeriod);
    }

    @Test
    @Transactional
    void deleteAmortizationPeriod() throws Exception {
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);

        int databaseSizeBeforeDelete = amortizationPeriodRepository.findAll().size();

        // Delete the amortizationPeriod
        restAmortizationPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, amortizationPeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AmortizationPeriod> amortizationPeriodList = amortizationPeriodRepository.findAll();
        assertThat(amortizationPeriodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AmortizationPeriod in Elasticsearch
        verify(mockAmortizationPeriodSearchRepository, times(1)).deleteById(amortizationPeriod.getId());
    }

    @Test
    @Transactional
    void searchAmortizationPeriod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        amortizationPeriodRepository.saveAndFlush(amortizationPeriod);
        when(mockAmortizationPeriodSearchRepository.search("id:" + amortizationPeriod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amortizationPeriod), PageRequest.of(0, 1), 1));

        // Search the amortizationPeriod
        restAmortizationPeriodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + amortizationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)));
    }
}
