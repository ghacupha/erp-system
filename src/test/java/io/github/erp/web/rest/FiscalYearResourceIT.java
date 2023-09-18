package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.FiscalYearStatusType;
import io.github.erp.repository.FiscalYearRepository;
import io.github.erp.repository.search.FiscalYearSearchRepository;
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.criteria.FiscalYearCriteria;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.mapper.FiscalYearMapper;
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
 * Integration tests for the {@link FiscalYearResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalYearResourceIT {

    private static final String DEFAULT_FISCAL_YEAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_YEAR_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final FiscalYearStatusType DEFAULT_FISCAL_YEAR_STATUS = FiscalYearStatusType.OPEN;
    private static final FiscalYearStatusType UPDATED_FISCAL_YEAR_STATUS = FiscalYearStatusType.CLOSED;

    private static final String ENTITY_API_URL = "/api/fiscal-years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fiscal-years";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Mock
    private FiscalYearRepository fiscalYearRepositoryMock;

    @Autowired
    private FiscalYearMapper fiscalYearMapper;

    @Mock
    private FiscalYearService fiscalYearServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalYearSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalYearSearchRepository mockFiscalYearSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalYearMockMvc;

    private FiscalYear fiscalYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createEntity(EntityManager em) {
        FiscalYear fiscalYear = new FiscalYear()
            .fiscalYearCode(DEFAULT_FISCAL_YEAR_CODE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalYearStatus(DEFAULT_FISCAL_YEAR_STATUS);
        return fiscalYear;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createUpdatedEntity(EntityManager em) {
        FiscalYear fiscalYear = new FiscalYear()
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
        return fiscalYear;
    }

    @BeforeEach
    public void initTest() {
        fiscalYear = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalYear() throws Exception {
        int databaseSizeBeforeCreate = fiscalYearRepository.findAll().size();
        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);
        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isCreated());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(DEFAULT_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(DEFAULT_FISCAL_YEAR_STATUS);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(1)).save(testFiscalYear);
    }

    @Test
    @Transactional
    void createFiscalYearWithExistingId() throws Exception {
        // Create the FiscalYear with an existing ID
        fiscalYear.setId(1L);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        int databaseSizeBeforeCreate = fiscalYearRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void checkFiscalYearCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setFiscalYearCode(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setStartDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setEndDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalYears() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalYearServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalYearServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalYear.getId().intValue()))
            .andExpect(jsonPath("$.fiscalYearCode").value(DEFAULT_FISCAL_YEAR_CODE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalYearStatus").value(DEFAULT_FISCAL_YEAR_STATUS.toString()));
    }

    @Test
    @Transactional
    void getFiscalYearsByIdFiltering() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        Long id = fiscalYear.getId();

        defaultFiscalYearShouldBeFound("id.equals=" + id);
        defaultFiscalYearShouldNotBeFound("id.notEquals=" + id);

        defaultFiscalYearShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFiscalYearShouldNotBeFound("id.greaterThan=" + id);

        defaultFiscalYearShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFiscalYearShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode equals to DEFAULT_FISCAL_YEAR_CODE
        defaultFiscalYearShouldBeFound("fiscalYearCode.equals=" + DEFAULT_FISCAL_YEAR_CODE);

        // Get all the fiscalYearList where fiscalYearCode equals to UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.equals=" + UPDATED_FISCAL_YEAR_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode not equals to DEFAULT_FISCAL_YEAR_CODE
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.notEquals=" + DEFAULT_FISCAL_YEAR_CODE);

        // Get all the fiscalYearList where fiscalYearCode not equals to UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldBeFound("fiscalYearCode.notEquals=" + UPDATED_FISCAL_YEAR_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode in DEFAULT_FISCAL_YEAR_CODE or UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldBeFound("fiscalYearCode.in=" + DEFAULT_FISCAL_YEAR_CODE + "," + UPDATED_FISCAL_YEAR_CODE);

        // Get all the fiscalYearList where fiscalYearCode equals to UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.in=" + UPDATED_FISCAL_YEAR_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode is not null
        defaultFiscalYearShouldBeFound("fiscalYearCode.specified=true");

        // Get all the fiscalYearList where fiscalYearCode is null
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeContainsSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode contains DEFAULT_FISCAL_YEAR_CODE
        defaultFiscalYearShouldBeFound("fiscalYearCode.contains=" + DEFAULT_FISCAL_YEAR_CODE);

        // Get all the fiscalYearList where fiscalYearCode contains UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.contains=" + UPDATED_FISCAL_YEAR_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearCode does not contain DEFAULT_FISCAL_YEAR_CODE
        defaultFiscalYearShouldNotBeFound("fiscalYearCode.doesNotContain=" + DEFAULT_FISCAL_YEAR_CODE);

        // Get all the fiscalYearList where fiscalYearCode does not contain UPDATED_FISCAL_YEAR_CODE
        defaultFiscalYearShouldBeFound("fiscalYearCode.doesNotContain=" + UPDATED_FISCAL_YEAR_CODE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate equals to DEFAULT_START_DATE
        defaultFiscalYearShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate equals to UPDATED_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate not equals to DEFAULT_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate not equals to UPDATED_START_DATE
        defaultFiscalYearShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultFiscalYearShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the fiscalYearList where startDate equals to UPDATED_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is not null
        defaultFiscalYearShouldBeFound("startDate.specified=true");

        // Get all the fiscalYearList where startDate is null
        defaultFiscalYearShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultFiscalYearShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate is greater than or equal to UPDATED_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is less than or equal to DEFAULT_START_DATE
        defaultFiscalYearShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate is less than or equal to SMALLER_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is less than DEFAULT_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate is less than UPDATED_START_DATE
        defaultFiscalYearShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where startDate is greater than DEFAULT_START_DATE
        defaultFiscalYearShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the fiscalYearList where startDate is greater than SMALLER_START_DATE
        defaultFiscalYearShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate equals to DEFAULT_END_DATE
        defaultFiscalYearShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate equals to UPDATED_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate not equals to DEFAULT_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate not equals to UPDATED_END_DATE
        defaultFiscalYearShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultFiscalYearShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the fiscalYearList where endDate equals to UPDATED_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is not null
        defaultFiscalYearShouldBeFound("endDate.specified=true");

        // Get all the fiscalYearList where endDate is null
        defaultFiscalYearShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultFiscalYearShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate is greater than or equal to UPDATED_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is less than or equal to DEFAULT_END_DATE
        defaultFiscalYearShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate is less than or equal to SMALLER_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is less than DEFAULT_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate is less than UPDATED_END_DATE
        defaultFiscalYearShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where endDate is greater than DEFAULT_END_DATE
        defaultFiscalYearShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the fiscalYearList where endDate is greater than SMALLER_END_DATE
        defaultFiscalYearShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus equals to DEFAULT_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldBeFound("fiscalYearStatus.equals=" + DEFAULT_FISCAL_YEAR_STATUS);

        // Get all the fiscalYearList where fiscalYearStatus equals to UPDATED_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldNotBeFound("fiscalYearStatus.equals=" + UPDATED_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus not equals to DEFAULT_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldNotBeFound("fiscalYearStatus.notEquals=" + DEFAULT_FISCAL_YEAR_STATUS);

        // Get all the fiscalYearList where fiscalYearStatus not equals to UPDATED_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldBeFound("fiscalYearStatus.notEquals=" + UPDATED_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsInShouldWork() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus in DEFAULT_FISCAL_YEAR_STATUS or UPDATED_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldBeFound("fiscalYearStatus.in=" + DEFAULT_FISCAL_YEAR_STATUS + "," + UPDATED_FISCAL_YEAR_STATUS);

        // Get all the fiscalYearList where fiscalYearStatus equals to UPDATED_FISCAL_YEAR_STATUS
        defaultFiscalYearShouldNotBeFound("fiscalYearStatus.in=" + UPDATED_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void getAllFiscalYearsByFiscalYearStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList where fiscalYearStatus is not null
        defaultFiscalYearShouldBeFound("fiscalYearStatus.specified=true");

        // Get all the fiscalYearList where fiscalYearStatus is null
        defaultFiscalYearShouldNotBeFound("fiscalYearStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllFiscalYearsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
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
        fiscalYear.addPlaceholder(placeholder);
        fiscalYearRepository.saveAndFlush(fiscalYear);
        Long placeholderId = placeholder.getId();

        // Get all the fiscalYearList where placeholder equals to placeholderId
        defaultFiscalYearShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fiscalYearList where placeholder equals to (placeholderId + 1)
        defaultFiscalYearShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalYearsByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
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
        fiscalYear.addUniversallyUniqueMapping(universallyUniqueMapping);
        fiscalYearRepository.saveAndFlush(fiscalYear);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the fiscalYearList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultFiscalYearShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the fiscalYearList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultFiscalYearShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    @Test
    @Transactional
    void getAllFiscalYearsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
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
        fiscalYear.setCreatedBy(createdBy);
        fiscalYearRepository.saveAndFlush(fiscalYear);
        Long createdById = createdBy.getId();

        // Get all the fiscalYearList where createdBy equals to createdById
        defaultFiscalYearShouldBeFound("createdById.equals=" + createdById);

        // Get all the fiscalYearList where createdBy equals to (createdById + 1)
        defaultFiscalYearShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllFiscalYearsByLastUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
        ApplicationUser lastUpdatedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastUpdatedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastUpdatedBy);
            em.flush();
        } else {
            lastUpdatedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastUpdatedBy);
        em.flush();
        fiscalYear.setLastUpdatedBy(lastUpdatedBy);
        fiscalYearRepository.saveAndFlush(fiscalYear);
        Long lastUpdatedById = lastUpdatedBy.getId();

        // Get all the fiscalYearList where lastUpdatedBy equals to lastUpdatedById
        defaultFiscalYearShouldBeFound("lastUpdatedById.equals=" + lastUpdatedById);

        // Get all the fiscalYearList where lastUpdatedBy equals to (lastUpdatedById + 1)
        defaultFiscalYearShouldNotBeFound("lastUpdatedById.equals=" + (lastUpdatedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiscalYearShouldBeFound(String filter) throws Exception {
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));

        // Check, that the count call also returns 1
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiscalYearShouldNotBeFound(String filter) throws Exception {
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiscalYear() throws Exception {
        // Get the fiscalYear
        restFiscalYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear
        FiscalYear updatedFiscalYear = fiscalYearRepository.findById(fiscalYear.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalYear are not directly saved in db
        em.detach(updatedFiscalYear);
        updatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(updatedFiscalYear);

        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(UPDATED_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(UPDATED_FISCAL_YEAR_STATUS);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository).save(testFiscalYear);
    }

    @Test
    @Transactional
    void putNonExistingFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void partialUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear.startDate(UPDATED_START_DATE);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(DEFAULT_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(DEFAULT_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(UPDATED_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(UPDATED_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void deleteFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeDelete = fiscalYearRepository.findAll().size();

        // Delete the fiscalYear
        restFiscalYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalYear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(1)).deleteById(fiscalYear.getId());
    }

    @Test
    @Transactional
    void searchFiscalYear() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
        when(mockFiscalYearSearchRepository.search("id:" + fiscalYear.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalYear), PageRequest.of(0, 1), 1));

        // Search the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }
}
