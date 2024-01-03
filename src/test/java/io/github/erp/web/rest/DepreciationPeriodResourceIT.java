package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.search.DepreciationPeriodSearchRepository;
import io.github.erp.service.criteria.DepreciationPeriodCriteria;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.mapper.DepreciationPeriodMapper;
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
 * Integration tests for the {@link DepreciationPeriodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationPeriodResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final DepreciationPeriodStatusTypes DEFAULT_DEPRECIATION_PERIOD_STATUS = DepreciationPeriodStatusTypes.OPEN;
    private static final DepreciationPeriodStatusTypes UPDATED_DEPRECIATION_PERIOD_STATUS = DepreciationPeriodStatusTypes.CLOSED;

    private static final String DEFAULT_PERIOD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PROCESS_LOCKED = false;
    private static final Boolean UPDATED_PROCESS_LOCKED = true;

    private static final String ENTITY_API_URL = "/api/depreciation-periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-periods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationPeriodRepository depreciationPeriodRepository;

    @Autowired
    private DepreciationPeriodMapper depreciationPeriodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationPeriodSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationPeriodSearchRepository mockDepreciationPeriodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationPeriodMockMvc;

    private DepreciationPeriod depreciationPeriod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationPeriod createEntity(EntityManager em) {
        DepreciationPeriod depreciationPeriod = new DepreciationPeriod()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .depreciationPeriodStatus(DEFAULT_DEPRECIATION_PERIOD_STATUS)
            .periodCode(DEFAULT_PERIOD_CODE)
            .processLocked(DEFAULT_PROCESS_LOCKED);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        depreciationPeriod.setFiscalMonth(fiscalMonth);
        return depreciationPeriod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationPeriod createUpdatedEntity(EntityManager em) {
        DepreciationPeriod depreciationPeriod = new DepreciationPeriod()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .depreciationPeriodStatus(UPDATED_DEPRECIATION_PERIOD_STATUS)
            .periodCode(UPDATED_PERIOD_CODE)
            .processLocked(UPDATED_PROCESS_LOCKED);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        depreciationPeriod.setFiscalMonth(fiscalMonth);
        return depreciationPeriod;
    }

    @BeforeEach
    public void initTest() {
        depreciationPeriod = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationPeriod() throws Exception {
        int databaseSizeBeforeCreate = depreciationPeriodRepository.findAll().size();
        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);
        restDepreciationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationPeriod testDepreciationPeriod = depreciationPeriodList.get(depreciationPeriodList.size() - 1);
        assertThat(testDepreciationPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDepreciationPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDepreciationPeriod.getDepreciationPeriodStatus()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_STATUS);
        assertThat(testDepreciationPeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);
        assertThat(testDepreciationPeriod.getProcessLocked()).isEqualTo(DEFAULT_PROCESS_LOCKED);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(1)).save(testDepreciationPeriod);
    }

    @Test
    @Transactional
    void createDepreciationPeriodWithExistingId() throws Exception {
        // Create the DepreciationPeriod with an existing ID
        depreciationPeriod.setId(1L);
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        int databaseSizeBeforeCreate = depreciationPeriodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationPeriodRepository.findAll().size();
        // set the field null
        depreciationPeriod.setStartDate(null);

        // Create the DepreciationPeriod, which fails.
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        restDepreciationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationPeriodRepository.findAll().size();
        // set the field null
        depreciationPeriod.setEndDate(null);

        // Create the DepreciationPeriod, which fails.
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        restDepreciationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationPeriodRepository.findAll().size();
        // set the field null
        depreciationPeriod.setPeriodCode(null);

        // Create the DepreciationPeriod, which fails.
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        restDepreciationPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriods() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodStatus").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].processLocked").value(hasItem(DEFAULT_PROCESS_LOCKED.booleanValue())));
    }

    @Test
    @Transactional
    void getDepreciationPeriod() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get the depreciationPeriod
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationPeriod.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.depreciationPeriodStatus").value(DEFAULT_DEPRECIATION_PERIOD_STATUS.toString()))
            .andExpect(jsonPath("$.periodCode").value(DEFAULT_PERIOD_CODE))
            .andExpect(jsonPath("$.processLocked").value(DEFAULT_PROCESS_LOCKED.booleanValue()));
    }

    @Test
    @Transactional
    void getDepreciationPeriodsByIdFiltering() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        Long id = depreciationPeriod.getId();

        defaultDepreciationPeriodShouldBeFound("id.equals=" + id);
        defaultDepreciationPeriodShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationPeriodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationPeriodShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationPeriodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationPeriodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate equals to DEFAULT_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate equals to UPDATED_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate not equals to DEFAULT_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate not equals to UPDATED_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the depreciationPeriodList where startDate equals to UPDATED_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate is not null
        defaultDepreciationPeriodShouldBeFound("startDate.specified=true");

        // Get all the depreciationPeriodList where startDate is null
        defaultDepreciationPeriodShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate is greater than or equal to UPDATED_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate is less than or equal to DEFAULT_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate is less than or equal to SMALLER_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate is less than DEFAULT_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate is less than UPDATED_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where startDate is greater than DEFAULT_START_DATE
        defaultDepreciationPeriodShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the depreciationPeriodList where startDate is greater than SMALLER_START_DATE
        defaultDepreciationPeriodShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate equals to DEFAULT_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate equals to UPDATED_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate not equals to DEFAULT_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate not equals to UPDATED_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the depreciationPeriodList where endDate equals to UPDATED_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate is not null
        defaultDepreciationPeriodShouldBeFound("endDate.specified=true");

        // Get all the depreciationPeriodList where endDate is null
        defaultDepreciationPeriodShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate is greater than or equal to UPDATED_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate is less than or equal to DEFAULT_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate is less than or equal to SMALLER_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate is less than DEFAULT_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate is less than UPDATED_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where endDate is greater than DEFAULT_END_DATE
        defaultDepreciationPeriodShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the depreciationPeriodList where endDate is greater than SMALLER_END_DATE
        defaultDepreciationPeriodShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByDepreciationPeriodStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where depreciationPeriodStatus equals to DEFAULT_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldBeFound("depreciationPeriodStatus.equals=" + DEFAULT_DEPRECIATION_PERIOD_STATUS);

        // Get all the depreciationPeriodList where depreciationPeriodStatus equals to UPDATED_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldNotBeFound("depreciationPeriodStatus.equals=" + UPDATED_DEPRECIATION_PERIOD_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByDepreciationPeriodStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where depreciationPeriodStatus not equals to DEFAULT_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldNotBeFound("depreciationPeriodStatus.notEquals=" + DEFAULT_DEPRECIATION_PERIOD_STATUS);

        // Get all the depreciationPeriodList where depreciationPeriodStatus not equals to UPDATED_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldBeFound("depreciationPeriodStatus.notEquals=" + UPDATED_DEPRECIATION_PERIOD_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByDepreciationPeriodStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where depreciationPeriodStatus in DEFAULT_DEPRECIATION_PERIOD_STATUS or UPDATED_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldBeFound(
            "depreciationPeriodStatus.in=" + DEFAULT_DEPRECIATION_PERIOD_STATUS + "," + UPDATED_DEPRECIATION_PERIOD_STATUS
        );

        // Get all the depreciationPeriodList where depreciationPeriodStatus equals to UPDATED_DEPRECIATION_PERIOD_STATUS
        defaultDepreciationPeriodShouldNotBeFound("depreciationPeriodStatus.in=" + UPDATED_DEPRECIATION_PERIOD_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByDepreciationPeriodStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where depreciationPeriodStatus is not null
        defaultDepreciationPeriodShouldBeFound("depreciationPeriodStatus.specified=true");

        // Get all the depreciationPeriodList where depreciationPeriodStatus is null
        defaultDepreciationPeriodShouldNotBeFound("depreciationPeriodStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode equals to DEFAULT_PERIOD_CODE
        defaultDepreciationPeriodShouldBeFound("periodCode.equals=" + DEFAULT_PERIOD_CODE);

        // Get all the depreciationPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldNotBeFound("periodCode.equals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode not equals to DEFAULT_PERIOD_CODE
        defaultDepreciationPeriodShouldNotBeFound("periodCode.notEquals=" + DEFAULT_PERIOD_CODE);

        // Get all the depreciationPeriodList where periodCode not equals to UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldBeFound("periodCode.notEquals=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode in DEFAULT_PERIOD_CODE or UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldBeFound("periodCode.in=" + DEFAULT_PERIOD_CODE + "," + UPDATED_PERIOD_CODE);

        // Get all the depreciationPeriodList where periodCode equals to UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldNotBeFound("periodCode.in=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode is not null
        defaultDepreciationPeriodShouldBeFound("periodCode.specified=true");

        // Get all the depreciationPeriodList where periodCode is null
        defaultDepreciationPeriodShouldNotBeFound("periodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeContainsSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode contains DEFAULT_PERIOD_CODE
        defaultDepreciationPeriodShouldBeFound("periodCode.contains=" + DEFAULT_PERIOD_CODE);

        // Get all the depreciationPeriodList where periodCode contains UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldNotBeFound("periodCode.contains=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPeriodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where periodCode does not contain DEFAULT_PERIOD_CODE
        defaultDepreciationPeriodShouldNotBeFound("periodCode.doesNotContain=" + DEFAULT_PERIOD_CODE);

        // Get all the depreciationPeriodList where periodCode does not contain UPDATED_PERIOD_CODE
        defaultDepreciationPeriodShouldBeFound("periodCode.doesNotContain=" + UPDATED_PERIOD_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByProcessLockedIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where processLocked equals to DEFAULT_PROCESS_LOCKED
        defaultDepreciationPeriodShouldBeFound("processLocked.equals=" + DEFAULT_PROCESS_LOCKED);

        // Get all the depreciationPeriodList where processLocked equals to UPDATED_PROCESS_LOCKED
        defaultDepreciationPeriodShouldNotBeFound("processLocked.equals=" + UPDATED_PROCESS_LOCKED);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByProcessLockedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where processLocked not equals to DEFAULT_PROCESS_LOCKED
        defaultDepreciationPeriodShouldNotBeFound("processLocked.notEquals=" + DEFAULT_PROCESS_LOCKED);

        // Get all the depreciationPeriodList where processLocked not equals to UPDATED_PROCESS_LOCKED
        defaultDepreciationPeriodShouldBeFound("processLocked.notEquals=" + UPDATED_PROCESS_LOCKED);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByProcessLockedIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where processLocked in DEFAULT_PROCESS_LOCKED or UPDATED_PROCESS_LOCKED
        defaultDepreciationPeriodShouldBeFound("processLocked.in=" + DEFAULT_PROCESS_LOCKED + "," + UPDATED_PROCESS_LOCKED);

        // Get all the depreciationPeriodList where processLocked equals to UPDATED_PROCESS_LOCKED
        defaultDepreciationPeriodShouldNotBeFound("processLocked.in=" + UPDATED_PROCESS_LOCKED);
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByProcessLockedIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        // Get all the depreciationPeriodList where processLocked is not null
        defaultDepreciationPeriodShouldBeFound("processLocked.specified=true");

        // Get all the depreciationPeriodList where processLocked is null
        defaultDepreciationPeriodShouldNotBeFound("processLocked.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByPreviousPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        DepreciationPeriod previousPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            previousPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(previousPeriod);
            em.flush();
        } else {
            previousPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(previousPeriod);
        em.flush();
        depreciationPeriod.setPreviousPeriod(previousPeriod);
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        Long previousPeriodId = previousPeriod.getId();

        // Get all the depreciationPeriodList where previousPeriod equals to previousPeriodId
        defaultDepreciationPeriodShouldBeFound("previousPeriodId.equals=" + previousPeriodId);

        // Get all the depreciationPeriodList where previousPeriod equals to (previousPeriodId + 1)
        defaultDepreciationPeriodShouldNotBeFound("previousPeriodId.equals=" + (previousPeriodId + 1));
    }

    // @Test
    @Transactional
    void getAllDepreciationPeriodsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        ApplicationUser createdBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            createdBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(createdBy);
            em.flush();
        } else {
            createdBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        depreciationPeriod.setCreatedBy(createdBy);
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        Long createdById = createdBy.getId();

        // Get all the depreciationPeriodList where createdBy equals to createdById
        defaultDepreciationPeriodShouldBeFound("createdById.equals=" + createdById);

        // Get all the depreciationPeriodList where createdBy equals to (createdById + 1)
        defaultDepreciationPeriodShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationPeriodsByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
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
        depreciationPeriod.setFiscalMonth(fiscalMonth);
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the depreciationPeriodList where fiscalMonth equals to fiscalMonthId
        defaultDepreciationPeriodShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the depreciationPeriodList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultDepreciationPeriodShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationPeriodShouldBeFound(String filter) throws Exception {
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodStatus").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].processLocked").value(hasItem(DEFAULT_PROCESS_LOCKED.booleanValue())));

        // Check, that the count call also returns 1
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationPeriodShouldNotBeFound(String filter) throws Exception {
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationPeriod() throws Exception {
        // Get the depreciationPeriod
        restDepreciationPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationPeriod() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();

        // Update the depreciationPeriod
        DepreciationPeriod updatedDepreciationPeriod = depreciationPeriodRepository.findById(depreciationPeriod.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationPeriod are not directly saved in db
        em.detach(updatedDepreciationPeriod);
        updatedDepreciationPeriod
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .depreciationPeriodStatus(UPDATED_DEPRECIATION_PERIOD_STATUS)
            .periodCode(UPDATED_PERIOD_CODE)
            .processLocked(UPDATED_PROCESS_LOCKED);
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(updatedDepreciationPeriod);

        restDepreciationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationPeriod testDepreciationPeriod = depreciationPeriodList.get(depreciationPeriodList.size() - 1);
        assertThat(testDepreciationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDepreciationPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDepreciationPeriod.getDepreciationPeriodStatus()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_STATUS);
        assertThat(testDepreciationPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
        assertThat(testDepreciationPeriod.getProcessLocked()).isEqualTo(UPDATED_PROCESS_LOCKED);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository).save(testDepreciationPeriod);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationPeriodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationPeriodWithPatch() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();

        // Update the depreciationPeriod using partial update
        DepreciationPeriod partialUpdatedDepreciationPeriod = new DepreciationPeriod();
        partialUpdatedDepreciationPeriod.setId(depreciationPeriod.getId());

        partialUpdatedDepreciationPeriod.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).processLocked(UPDATED_PROCESS_LOCKED);

        restDepreciationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationPeriod))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationPeriod testDepreciationPeriod = depreciationPeriodList.get(depreciationPeriodList.size() - 1);
        assertThat(testDepreciationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDepreciationPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDepreciationPeriod.getDepreciationPeriodStatus()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_STATUS);
        assertThat(testDepreciationPeriod.getPeriodCode()).isEqualTo(DEFAULT_PERIOD_CODE);
        assertThat(testDepreciationPeriod.getProcessLocked()).isEqualTo(UPDATED_PROCESS_LOCKED);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationPeriodWithPatch() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();

        // Update the depreciationPeriod using partial update
        DepreciationPeriod partialUpdatedDepreciationPeriod = new DepreciationPeriod();
        partialUpdatedDepreciationPeriod.setId(depreciationPeriod.getId());

        partialUpdatedDepreciationPeriod
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .depreciationPeriodStatus(UPDATED_DEPRECIATION_PERIOD_STATUS)
            .periodCode(UPDATED_PERIOD_CODE)
            .processLocked(UPDATED_PROCESS_LOCKED);

        restDepreciationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationPeriod))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);
        DepreciationPeriod testDepreciationPeriod = depreciationPeriodList.get(depreciationPeriodList.size() - 1);
        assertThat(testDepreciationPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDepreciationPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDepreciationPeriod.getDepreciationPeriodStatus()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_STATUS);
        assertThat(testDepreciationPeriod.getPeriodCode()).isEqualTo(UPDATED_PERIOD_CODE);
        assertThat(testDepreciationPeriod.getProcessLocked()).isEqualTo(UPDATED_PROCESS_LOCKED);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationPeriodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = depreciationPeriodRepository.findAll().size();
        depreciationPeriod.setId(count.incrementAndGet());

        // Create the DepreciationPeriod
        DepreciationPeriodDTO depreciationPeriodDTO = depreciationPeriodMapper.toDto(depreciationPeriod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationPeriodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationPeriod in the database
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(0)).save(depreciationPeriod);
    }

    @Test
    @Transactional
    void deleteDepreciationPeriod() throws Exception {
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);

        int databaseSizeBeforeDelete = depreciationPeriodRepository.findAll().size();

        // Delete the depreciationPeriod
        restDepreciationPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationPeriod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationPeriod> depreciationPeriodList = depreciationPeriodRepository.findAll();
        assertThat(depreciationPeriodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationPeriod in Elasticsearch
        verify(mockDepreciationPeriodSearchRepository, times(1)).deleteById(depreciationPeriod.getId());
    }

    @Test
    @Transactional
    void searchDepreciationPeriod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationPeriodRepository.saveAndFlush(depreciationPeriod);
        when(mockDepreciationPeriodSearchRepository.search("id:" + depreciationPeriod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationPeriod), PageRequest.of(0, 1), 1));

        // Search the depreciationPeriod
        restDepreciationPeriodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodStatus").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].periodCode").value(hasItem(DEFAULT_PERIOD_CODE)))
            .andExpect(jsonPath("$.[*].processLocked").value(hasItem(DEFAULT_PROCESS_LOCKED.booleanValue())));
    }
}
