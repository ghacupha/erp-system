package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.FiscalMonthRepository;
import io.github.erp.repository.search.FiscalMonthSearchRepository;
import io.github.erp.service.FiscalMonthService;
import io.github.erp.service.criteria.FiscalMonthCriteria;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.mapper.FiscalMonthMapper;
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
 * Integration tests for the {@link FiscalMonthResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalMonthResourceIT {

    private static final Integer DEFAULT_MONTH_NUMBER = 1;
    private static final Integer UPDATED_MONTH_NUMBER = 2;
    private static final Integer SMALLER_MONTH_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FISCAL_MONTH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_MONTH_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fiscal-months";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fiscal-months";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalMonthRepository fiscalMonthRepository;

    @Mock
    private FiscalMonthRepository fiscalMonthRepositoryMock;

    @Autowired
    private FiscalMonthMapper fiscalMonthMapper;

    @Mock
    private FiscalMonthService fiscalMonthServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalMonthSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalMonthSearchRepository mockFiscalMonthSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalMonthMockMvc;

    private FiscalMonth fiscalMonth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createEntity(EntityManager em) {
        FiscalMonth fiscalMonth = new FiscalMonth()
            .monthNumber(DEFAULT_MONTH_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalMonthCode(DEFAULT_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalMonth.setFiscalYear(fiscalYear);
        return fiscalMonth;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createUpdatedEntity(EntityManager em) {
        FiscalMonth fiscalMonth = new FiscalMonth()
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalMonth.setFiscalYear(fiscalYear);
        return fiscalMonth;
    }

    @BeforeEach
    public void initTest() {
        fiscalMonth = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalMonth() throws Exception {
        int databaseSizeBeforeCreate = fiscalMonthRepository.findAll().size();
        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);
        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(DEFAULT_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(DEFAULT_FISCAL_MONTH_CODE);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(1)).save(testFiscalMonth);
    }

    @Test
    @Transactional
    void createFiscalMonthWithExistingId() throws Exception {
        // Create the FiscalMonth with an existing ID
        fiscalMonth.setId(1L);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        int databaseSizeBeforeCreate = fiscalMonthRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void checkMonthNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setMonthNumber(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setStartDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setEndDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiscalMonthCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setFiscalMonthCode(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalMonths() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalMonthServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalMonthServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalMonth.getId().intValue()))
            .andExpect(jsonPath("$.monthNumber").value(DEFAULT_MONTH_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalMonthCode").value(DEFAULT_FISCAL_MONTH_CODE));
    }

    @Test
    @Transactional
    void getFiscalMonthsByIdFiltering() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        Long id = fiscalMonth.getId();

        defaultFiscalMonthShouldBeFound("id.equals=" + id);
        defaultFiscalMonthShouldNotBeFound("id.notEquals=" + id);

        defaultFiscalMonthShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFiscalMonthShouldNotBeFound("id.greaterThan=" + id);

        defaultFiscalMonthShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFiscalMonthShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber equals to DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.equals=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber equals to UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.equals=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber not equals to DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.notEquals=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber not equals to UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.notEquals=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber in DEFAULT_MONTH_NUMBER or UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.in=" + DEFAULT_MONTH_NUMBER + "," + UPDATED_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber equals to UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.in=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is not null
        defaultFiscalMonthShouldBeFound("monthNumber.specified=true");

        // Get all the fiscalMonthList where monthNumber is null
        defaultFiscalMonthShouldNotBeFound("monthNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is greater than or equal to DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.greaterThanOrEqual=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber is greater than or equal to UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.greaterThanOrEqual=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is less than or equal to DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.lessThanOrEqual=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber is less than or equal to SMALLER_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.lessThanOrEqual=" + SMALLER_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is less than DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.lessThan=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber is less than UPDATED_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.lessThan=" + UPDATED_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByMonthNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where monthNumber is greater than DEFAULT_MONTH_NUMBER
        defaultFiscalMonthShouldNotBeFound("monthNumber.greaterThan=" + DEFAULT_MONTH_NUMBER);

        // Get all the fiscalMonthList where monthNumber is greater than SMALLER_MONTH_NUMBER
        defaultFiscalMonthShouldBeFound("monthNumber.greaterThan=" + SMALLER_MONTH_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate equals to DEFAULT_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate equals to UPDATED_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate not equals to DEFAULT_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate not equals to UPDATED_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the fiscalMonthList where startDate equals to UPDATED_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is not null
        defaultFiscalMonthShouldBeFound("startDate.specified=true");

        // Get all the fiscalMonthList where startDate is null
        defaultFiscalMonthShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate is greater than or equal to UPDATED_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is less than or equal to DEFAULT_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate is less than or equal to SMALLER_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is less than DEFAULT_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate is less than UPDATED_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where startDate is greater than DEFAULT_START_DATE
        defaultFiscalMonthShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the fiscalMonthList where startDate is greater than SMALLER_START_DATE
        defaultFiscalMonthShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate equals to DEFAULT_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate equals to UPDATED_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate not equals to DEFAULT_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate not equals to UPDATED_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the fiscalMonthList where endDate equals to UPDATED_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is not null
        defaultFiscalMonthShouldBeFound("endDate.specified=true");

        // Get all the fiscalMonthList where endDate is null
        defaultFiscalMonthShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate is greater than or equal to UPDATED_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is less than or equal to DEFAULT_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate is less than or equal to SMALLER_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is less than DEFAULT_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate is less than UPDATED_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where endDate is greater than DEFAULT_END_DATE
        defaultFiscalMonthShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the fiscalMonthList where endDate is greater than SMALLER_END_DATE
        defaultFiscalMonthShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode equals to DEFAULT_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.equals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the fiscalMonthList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.equals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode not equals to DEFAULT_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.notEquals=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the fiscalMonthList where fiscalMonthCode not equals to UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.notEquals=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode in DEFAULT_FISCAL_MONTH_CODE or UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.in=" + DEFAULT_FISCAL_MONTH_CODE + "," + UPDATED_FISCAL_MONTH_CODE);

        // Get all the fiscalMonthList where fiscalMonthCode equals to UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.in=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode is not null
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.specified=true");

        // Get all the fiscalMonthList where fiscalMonthCode is null
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeContainsSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode contains DEFAULT_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.contains=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the fiscalMonthList where fiscalMonthCode contains UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.contains=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalMonthCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList where fiscalMonthCode does not contain DEFAULT_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldNotBeFound("fiscalMonthCode.doesNotContain=" + DEFAULT_FISCAL_MONTH_CODE);

        // Get all the fiscalMonthList where fiscalMonthCode does not contain UPDATED_FISCAL_MONTH_CODE
        defaultFiscalMonthShouldBeFound("fiscalMonthCode.doesNotContain=" + UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalYearIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        em.persist(fiscalYear);
        em.flush();
        fiscalMonth.setFiscalYear(fiscalYear);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long fiscalYearId = fiscalYear.getId();

        // Get all the fiscalMonthList where fiscalYear equals to fiscalYearId
        defaultFiscalMonthShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the fiscalMonthList where fiscalYear equals to (fiscalYearId + 1)
        defaultFiscalMonthShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
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
        fiscalMonth.addPlaceholder(placeholder);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long placeholderId = placeholder.getId();

        // Get all the fiscalMonthList where placeholder equals to placeholderId
        defaultFiscalMonthShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalMonthList where placeholder equals to (placeholderId + 1)
        defaultFiscalMonthShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        UniversallyUniqueMapping universallyUniqueMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            universallyUniqueMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(universallyUniqueMapping);
            em.flush();
        } else {
            universallyUniqueMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(universallyUniqueMapping);
        em.flush();
        fiscalMonth.addUniversallyUniqueMapping(universallyUniqueMapping);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the fiscalMonthList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultFiscalMonthShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the fiscalMonthList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultFiscalMonthShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalMonthsByFiscalQuarterIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        FiscalQuarter fiscalQuarter;
        if (TestUtil.findAll(em, FiscalQuarter.class).isEmpty()) {
            fiscalQuarter = FiscalQuarterResourceIT.createEntity(em);
            em.persist(fiscalQuarter);
            em.flush();
        } else {
            fiscalQuarter = TestUtil.findAll(em, FiscalQuarter.class).get(0);
        }
        em.persist(fiscalQuarter);
        em.flush();
        fiscalMonth.setFiscalQuarter(fiscalQuarter);
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        Long fiscalQuarterId = fiscalQuarter.getId();

        // Get all the fiscalMonthList where fiscalQuarter equals to fiscalQuarterId
        defaultFiscalMonthShouldBeFound("fiscalQuarterId.equals=" + fiscalQuarterId);

        // Get all the fiscalMonthList where fiscalQuarter equals to (fiscalQuarterId + 1)
        defaultFiscalMonthShouldNotBeFound("fiscalQuarterId.equals=" + (fiscalQuarterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalMonthShouldBeFound(String filter) throws Exception {
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));

        // Check, that the count call also returns 1
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalMonthShouldNotBeFound(String filter) throws Exception {
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalMonth() throws Exception {
        // Get the fiscalMonth
        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth
        FiscalMonth updatedFiscalMonth = fiscalMonthRepository.findById(fiscalMonth.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalMonth are not directly saved in db
        em.detach(updatedFiscalMonth);
        updatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(updatedFiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(UPDATED_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(UPDATED_FISCAL_MONTH_CODE);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository).save(testFiscalMonth);
    }

    @Test
    @Transactional
    void putNonExistingFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void partialUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth.endDate(UPDATED_END_DATE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(DEFAULT_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(DEFAULT_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(UPDATED_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void deleteFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeDelete = fiscalMonthRepository.findAll().size();

        // Delete the fiscalMonth
        restFiscalMonthMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalMonth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(1)).deleteById(fiscalMonth.getId());
    }

    @Test
    @Transactional
    void searchFiscalMonth() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        when(mockFiscalMonthSearchRepository.search("id:" + fiscalMonth.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalMonth), PageRequest.of(0, 1), 1));

        // Search the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }
}
