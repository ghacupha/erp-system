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
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.FiscalQuarterRepository;
import io.github.erp.repository.search.FiscalQuarterSearchRepository;
import io.github.erp.service.FiscalQuarterService;
import io.github.erp.service.criteria.FiscalQuarterCriteria;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.mapper.FiscalQuarterMapper;
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
 * Integration tests for the {@link FiscalQuarterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalQuarterResourceIT {

    private static final Integer DEFAULT_QUARTER_NUMBER = 1;
    private static final Integer UPDATED_QUARTER_NUMBER = 2;
    private static final Integer SMALLER_QUARTER_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FISCAL_QUARTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_QUARTER_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fiscal-quarters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fiscal-quarters";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalQuarterRepository fiscalQuarterRepository;

    @Mock
    private FiscalQuarterRepository fiscalQuarterRepositoryMock;

    @Autowired
    private FiscalQuarterMapper fiscalQuarterMapper;

    @Mock
    private FiscalQuarterService fiscalQuarterServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalQuarterSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalQuarterSearchRepository mockFiscalQuarterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalQuarterMockMvc;

    private FiscalQuarter fiscalQuarter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createEntity(EntityManager em) {
        FiscalQuarter fiscalQuarter = new FiscalQuarter()
            .quarterNumber(DEFAULT_QUARTER_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalQuarterCode(DEFAULT_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalQuarter.setFiscalYear(fiscalYear);
        return fiscalQuarter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createUpdatedEntity(EntityManager em) {
        FiscalQuarter fiscalQuarter = new FiscalQuarter()
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalQuarter.setFiscalYear(fiscalYear);
        return fiscalQuarter;
    }

    @BeforeEach
    public void initTest() {
        fiscalQuarter = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalQuarter() throws Exception {
        int databaseSizeBeforeCreate = fiscalQuarterRepository.findAll().size();
        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);
        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(DEFAULT_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(DEFAULT_FISCAL_QUARTER_CODE);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(1)).save(testFiscalQuarter);
    }

    @Test
    @Transactional
    void createFiscalQuarterWithExistingId() throws Exception {
        // Create the FiscalQuarter with an existing ID
        fiscalQuarter.setId(1L);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        int databaseSizeBeforeCreate = fiscalQuarterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void checkQuarterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setQuarterNumber(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setStartDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setEndDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiscalQuarterCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setFiscalQuarterCode(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalQuarters() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalQuarterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalQuarterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalQuarter.getId().intValue()))
            .andExpect(jsonPath("$.quarterNumber").value(DEFAULT_QUARTER_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalQuarterCode").value(DEFAULT_FISCAL_QUARTER_CODE));
    }

    @Test
    @Transactional
    void getFiscalQuartersByIdFiltering() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        Long id = fiscalQuarter.getId();

        defaultFiscalQuarterShouldBeFound("id.equals=" + id);
        defaultFiscalQuarterShouldNotBeFound("id.notEquals=" + id);

        defaultFiscalQuarterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFiscalQuarterShouldNotBeFound("id.greaterThan=" + id);

        defaultFiscalQuarterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFiscalQuarterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber equals to DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.equals=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber equals to UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.equals=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber not equals to DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.notEquals=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber not equals to UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.notEquals=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber in DEFAULT_QUARTER_NUMBER or UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.in=" + DEFAULT_QUARTER_NUMBER + "," + UPDATED_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber equals to UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.in=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is not null
        defaultFiscalQuarterShouldBeFound("quarterNumber.specified=true");

        // Get all the fiscalQuarterList where quarterNumber is null
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is greater than or equal to DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.greaterThanOrEqual=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber is greater than or equal to UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.greaterThanOrEqual=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is less than or equal to DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.lessThanOrEqual=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber is less than or equal to SMALLER_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.lessThanOrEqual=" + SMALLER_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is less than DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.lessThan=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber is less than UPDATED_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.lessThan=" + UPDATED_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByQuarterNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where quarterNumber is greater than DEFAULT_QUARTER_NUMBER
        defaultFiscalQuarterShouldNotBeFound("quarterNumber.greaterThan=" + DEFAULT_QUARTER_NUMBER);

        // Get all the fiscalQuarterList where quarterNumber is greater than SMALLER_QUARTER_NUMBER
        defaultFiscalQuarterShouldBeFound("quarterNumber.greaterThan=" + SMALLER_QUARTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate equals to DEFAULT_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate equals to UPDATED_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate not equals to DEFAULT_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate not equals to UPDATED_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the fiscalQuarterList where startDate equals to UPDATED_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is not null
        defaultFiscalQuarterShouldBeFound("startDate.specified=true");

        // Get all the fiscalQuarterList where startDate is null
        defaultFiscalQuarterShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate is greater than or equal to UPDATED_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is less than or equal to DEFAULT_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate is less than or equal to SMALLER_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is less than DEFAULT_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate is less than UPDATED_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where startDate is greater than DEFAULT_START_DATE
        defaultFiscalQuarterShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the fiscalQuarterList where startDate is greater than SMALLER_START_DATE
        defaultFiscalQuarterShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate equals to DEFAULT_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate equals to UPDATED_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate not equals to DEFAULT_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate not equals to UPDATED_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the fiscalQuarterList where endDate equals to UPDATED_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is not null
        defaultFiscalQuarterShouldBeFound("endDate.specified=true");

        // Get all the fiscalQuarterList where endDate is null
        defaultFiscalQuarterShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate is greater than or equal to UPDATED_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is less than or equal to DEFAULT_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate is less than or equal to SMALLER_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is less than DEFAULT_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate is less than UPDATED_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where endDate is greater than DEFAULT_END_DATE
        defaultFiscalQuarterShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the fiscalQuarterList where endDate is greater than SMALLER_END_DATE
        defaultFiscalQuarterShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode equals to DEFAULT_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.equals=" + DEFAULT_FISCAL_QUARTER_CODE);

        // Get all the fiscalQuarterList where fiscalQuarterCode equals to UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.equals=" + UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode not equals to DEFAULT_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.notEquals=" + DEFAULT_FISCAL_QUARTER_CODE);

        // Get all the fiscalQuarterList where fiscalQuarterCode not equals to UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.notEquals=" + UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode in DEFAULT_FISCAL_QUARTER_CODE or UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.in=" + DEFAULT_FISCAL_QUARTER_CODE + "," + UPDATED_FISCAL_QUARTER_CODE);

        // Get all the fiscalQuarterList where fiscalQuarterCode equals to UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.in=" + UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode is not null
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.specified=true");

        // Get all the fiscalQuarterList where fiscalQuarterCode is null
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeContainsSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode contains DEFAULT_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.contains=" + DEFAULT_FISCAL_QUARTER_CODE);

        // Get all the fiscalQuarterList where fiscalQuarterCode contains UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.contains=" + UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalQuarterCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList where fiscalQuarterCode does not contain DEFAULT_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldNotBeFound("fiscalQuarterCode.doesNotContain=" + DEFAULT_FISCAL_QUARTER_CODE);

        // Get all the fiscalQuarterList where fiscalQuarterCode does not contain UPDATED_FISCAL_QUARTER_CODE
        defaultFiscalQuarterShouldBeFound("fiscalQuarterCode.doesNotContain=" + UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByFiscalYearIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
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
        fiscalQuarter.setFiscalYear(fiscalYear);
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        Long fiscalYearId = fiscalYear.getId();

        // Get all the fiscalQuarterList where fiscalYear equals to fiscalYearId
        defaultFiscalQuarterShouldBeFound("fiscalYearId.equals=" + fiscalYearId);

        // Get all the fiscalQuarterList where fiscalYear equals to (fiscalYearId + 1)
        defaultFiscalQuarterShouldNotBeFound("fiscalYearId.equals=" + (fiscalYearId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
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
        fiscalQuarter.addPlaceholder(placeholder);
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        Long placeholderId = placeholder.getId();

        // Get all the fiscalQuarterList where placeholder equals to placeholderId
        defaultFiscalQuarterShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalQuarterList where placeholder equals to (placeholderId + 1)
        defaultFiscalQuarterShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalQuartersByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
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
        fiscalQuarter.addUniversallyUniqueMapping(universallyUniqueMapping);
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the fiscalQuarterList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultFiscalQuarterShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the fiscalQuarterList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultFiscalQuarterShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalQuarterShouldBeFound(String filter) throws Exception {
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));

        // Check, that the count call also returns 1
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalQuarterShouldNotBeFound(String filter) throws Exception {
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalQuarter() throws Exception {
        // Get the fiscalQuarter
        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter
        FiscalQuarter updatedFiscalQuarter = fiscalQuarterRepository.findById(fiscalQuarter.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalQuarter are not directly saved in db
        em.detach(updatedFiscalQuarter);
        updatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(updatedFiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository).save(testFiscalQuarter);
    }

    @Test
    @Transactional
    void putNonExistingFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void partialUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void deleteFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeDelete = fiscalQuarterRepository.findAll().size();

        // Delete the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalQuarter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(1)).deleteById(fiscalQuarter.getId());
    }

    @Test
    @Transactional
    void searchFiscalQuarter() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        when(mockFiscalQuarterSearchRepository.search("id:" + fiscalQuarter.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalQuarter), PageRequest.of(0, 1), 1));

        // Search the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }
}
