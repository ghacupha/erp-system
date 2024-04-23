package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.WeeklyCounterfeitHolding;
import io.github.erp.repository.WeeklyCounterfeitHoldingRepository;
import io.github.erp.repository.search.WeeklyCounterfeitHoldingSearchRepository;
import io.github.erp.service.dto.WeeklyCounterfeitHoldingDTO;
import io.github.erp.service.mapper.WeeklyCounterfeitHoldingMapper;
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
 * Integration tests for the {@link WeeklyCounterfeitHoldingResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class WeeklyCounterfeitHoldingResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_CONFISCATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CONFISCATED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CONFISCATED = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPOSITORS_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_DEPOSITORS_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_TELLERS_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_TELLERS_NAMES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_SUBMITTED_TO_CBK = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SUBMITTED_TO_CBK = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_SUBMITTED_TO_CBK = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/weekly-counterfeit-holdings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/weekly-counterfeit-holdings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository;

    @Autowired
    private WeeklyCounterfeitHoldingMapper weeklyCounterfeitHoldingMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WeeklyCounterfeitHoldingSearchRepositoryMockConfiguration
     */
    @Autowired
    private WeeklyCounterfeitHoldingSearchRepository mockWeeklyCounterfeitHoldingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeeklyCounterfeitHoldingMockMvc;

    private WeeklyCounterfeitHolding weeklyCounterfeitHolding;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyCounterfeitHolding createEntity(EntityManager em) {
        WeeklyCounterfeitHolding weeklyCounterfeitHolding = new WeeklyCounterfeitHolding()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .dateConfiscated(DEFAULT_DATE_CONFISCATED)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .depositorsNames(DEFAULT_DEPOSITORS_NAMES)
            .tellersNames(DEFAULT_TELLERS_NAMES)
            .dateSubmittedToCBK(DEFAULT_DATE_SUBMITTED_TO_CBK)
            .remarks(DEFAULT_REMARKS);
        return weeklyCounterfeitHolding;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyCounterfeitHolding createUpdatedEntity(EntityManager em) {
        WeeklyCounterfeitHolding weeklyCounterfeitHolding = new WeeklyCounterfeitHolding()
            .reportingDate(UPDATED_REPORTING_DATE)
            .dateConfiscated(UPDATED_DATE_CONFISCATED)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .depositorsNames(UPDATED_DEPOSITORS_NAMES)
            .tellersNames(UPDATED_TELLERS_NAMES)
            .dateSubmittedToCBK(UPDATED_DATE_SUBMITTED_TO_CBK)
            .remarks(UPDATED_REMARKS);
        return weeklyCounterfeitHolding;
    }

    @BeforeEach
    public void initTest() {
        weeklyCounterfeitHolding = createEntity(em);
    }

    @Test
    @Transactional
    void createWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeCreate = weeklyCounterfeitHoldingRepository.findAll().size();
        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyCounterfeitHolding testWeeklyCounterfeitHolding = weeklyCounterfeitHoldingList.get(weeklyCounterfeitHoldingList.size() - 1);
        assertThat(testWeeklyCounterfeitHolding.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testWeeklyCounterfeitHolding.getDateConfiscated()).isEqualTo(DEFAULT_DATE_CONFISCATED);
        assertThat(testWeeklyCounterfeitHolding.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testWeeklyCounterfeitHolding.getDepositorsNames()).isEqualTo(DEFAULT_DEPOSITORS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getTellersNames()).isEqualTo(DEFAULT_TELLERS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getDateSubmittedToCBK()).isEqualTo(DEFAULT_DATE_SUBMITTED_TO_CBK);
        assertThat(testWeeklyCounterfeitHolding.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(1)).save(testWeeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void createWeeklyCounterfeitHoldingWithExistingId() throws Exception {
        // Create the WeeklyCounterfeitHolding with an existing ID
        weeklyCounterfeitHolding.setId(1L);
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        int databaseSizeBeforeCreate = weeklyCounterfeitHoldingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeCreate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setReportingDate(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateConfiscatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setDateConfiscated(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setSerialNumber(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepositorsNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setDepositorsNames(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTellersNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setTellersNames(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSubmittedToCBKIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCounterfeitHoldingRepository.findAll().size();
        // set the field null
        weeklyCounterfeitHolding.setDateSubmittedToCBK(null);

        // Create the WeeklyCounterfeitHolding, which fails.
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldings() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCounterfeitHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateConfiscated").value(hasItem(DEFAULT_DATE_CONFISCATED.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].depositorsNames").value(hasItem(DEFAULT_DEPOSITORS_NAMES)))
            .andExpect(jsonPath("$.[*].tellersNames").value(hasItem(DEFAULT_TELLERS_NAMES)))
            .andExpect(jsonPath("$.[*].dateSubmittedToCBK").value(hasItem(DEFAULT_DATE_SUBMITTED_TO_CBK.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @Test
    @Transactional
    void getWeeklyCounterfeitHolding() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get the weeklyCounterfeitHolding
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL_ID, weeklyCounterfeitHolding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weeklyCounterfeitHolding.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.dateConfiscated").value(DEFAULT_DATE_CONFISCATED.toString()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.depositorsNames").value(DEFAULT_DEPOSITORS_NAMES))
            .andExpect(jsonPath("$.tellersNames").value(DEFAULT_TELLERS_NAMES))
            .andExpect(jsonPath("$.dateSubmittedToCBK").value(DEFAULT_DATE_SUBMITTED_TO_CBK.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getWeeklyCounterfeitHoldingsByIdFiltering() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        Long id = weeklyCounterfeitHolding.getId();

        defaultWeeklyCounterfeitHoldingShouldBeFound("id.equals=" + id);
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("id.notEquals=" + id);

        defaultWeeklyCounterfeitHoldingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("id.greaterThan=" + id);

        defaultWeeklyCounterfeitHoldingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.specified=true");

        // Get all the weeklyCounterfeitHoldingList where reportingDate is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCounterfeitHoldingList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultWeeklyCounterfeitHoldingShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated equals to DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.equals=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated equals to UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.equals=" + UPDATED_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated not equals to DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.notEquals=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated not equals to UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.notEquals=" + UPDATED_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated in DEFAULT_DATE_CONFISCATED or UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.in=" + DEFAULT_DATE_CONFISCATED + "," + UPDATED_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated equals to UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.in=" + UPDATED_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.specified=true");

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is greater than or equal to DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.greaterThanOrEqual=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is greater than or equal to UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.greaterThanOrEqual=" + UPDATED_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is less than or equal to DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.lessThanOrEqual=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is less than or equal to SMALLER_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.lessThanOrEqual=" + SMALLER_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is less than DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.lessThan=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is less than UPDATED_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.lessThan=" + UPDATED_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateConfiscatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is greater than DEFAULT_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateConfiscated.greaterThan=" + DEFAULT_DATE_CONFISCATED);

        // Get all the weeklyCounterfeitHoldingList where dateConfiscated is greater than SMALLER_DATE_CONFISCATED
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateConfiscated.greaterThan=" + SMALLER_DATE_CONFISCATED);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the weeklyCounterfeitHoldingList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the weeklyCounterfeitHoldingList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the weeklyCounterfeitHoldingList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.specified=true");

        // Get all the weeklyCounterfeitHoldingList where serialNumber is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the weeklyCounterfeitHoldingList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the weeklyCounterfeitHoldingList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultWeeklyCounterfeitHoldingShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames equals to DEFAULT_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.equals=" + DEFAULT_DEPOSITORS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames equals to UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.equals=" + UPDATED_DEPOSITORS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames not equals to DEFAULT_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.notEquals=" + DEFAULT_DEPOSITORS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames not equals to UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.notEquals=" + UPDATED_DEPOSITORS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames in DEFAULT_DEPOSITORS_NAMES or UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.in=" + DEFAULT_DEPOSITORS_NAMES + "," + UPDATED_DEPOSITORS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames equals to UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.in=" + UPDATED_DEPOSITORS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.specified=true");

        // Get all the weeklyCounterfeitHoldingList where depositorsNames is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames contains DEFAULT_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.contains=" + DEFAULT_DEPOSITORS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames contains UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.contains=" + UPDATED_DEPOSITORS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDepositorsNamesNotContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames does not contain DEFAULT_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("depositorsNames.doesNotContain=" + DEFAULT_DEPOSITORS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where depositorsNames does not contain UPDATED_DEPOSITORS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("depositorsNames.doesNotContain=" + UPDATED_DEPOSITORS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames equals to DEFAULT_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.equals=" + DEFAULT_TELLERS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where tellersNames equals to UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.equals=" + UPDATED_TELLERS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames not equals to DEFAULT_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.notEquals=" + DEFAULT_TELLERS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where tellersNames not equals to UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.notEquals=" + UPDATED_TELLERS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames in DEFAULT_TELLERS_NAMES or UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.in=" + DEFAULT_TELLERS_NAMES + "," + UPDATED_TELLERS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where tellersNames equals to UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.in=" + UPDATED_TELLERS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.specified=true");

        // Get all the weeklyCounterfeitHoldingList where tellersNames is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames contains DEFAULT_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.contains=" + DEFAULT_TELLERS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where tellersNames contains UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.contains=" + UPDATED_TELLERS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByTellersNamesNotContainsSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where tellersNames does not contain DEFAULT_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("tellersNames.doesNotContain=" + DEFAULT_TELLERS_NAMES);

        // Get all the weeklyCounterfeitHoldingList where tellersNames does not contain UPDATED_TELLERS_NAMES
        defaultWeeklyCounterfeitHoldingShouldBeFound("tellersNames.doesNotContain=" + UPDATED_TELLERS_NAMES);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK equals to DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.equals=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK equals to UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.equals=" + UPDATED_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK not equals to DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.notEquals=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK not equals to UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.notEquals=" + UPDATED_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK in DEFAULT_DATE_SUBMITTED_TO_CBK or UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound(
            "dateSubmittedToCBK.in=" + DEFAULT_DATE_SUBMITTED_TO_CBK + "," + UPDATED_DATE_SUBMITTED_TO_CBK
        );

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK equals to UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.in=" + UPDATED_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is not null
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.specified=true");

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is null
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is greater than or equal to DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.greaterThanOrEqual=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is greater than or equal to UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.greaterThanOrEqual=" + UPDATED_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is less than or equal to DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.lessThanOrEqual=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is less than or equal to SMALLER_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.lessThanOrEqual=" + SMALLER_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is less than DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.lessThan=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is less than UPDATED_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.lessThan=" + UPDATED_DATE_SUBMITTED_TO_CBK);
    }

    @Test
    @Transactional
    void getAllWeeklyCounterfeitHoldingsByDateSubmittedToCBKIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is greater than DEFAULT_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldNotBeFound("dateSubmittedToCBK.greaterThan=" + DEFAULT_DATE_SUBMITTED_TO_CBK);

        // Get all the weeklyCounterfeitHoldingList where dateSubmittedToCBK is greater than SMALLER_DATE_SUBMITTED_TO_CBK
        defaultWeeklyCounterfeitHoldingShouldBeFound("dateSubmittedToCBK.greaterThan=" + SMALLER_DATE_SUBMITTED_TO_CBK);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWeeklyCounterfeitHoldingShouldBeFound(String filter) throws Exception {
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCounterfeitHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateConfiscated").value(hasItem(DEFAULT_DATE_CONFISCATED.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].depositorsNames").value(hasItem(DEFAULT_DEPOSITORS_NAMES)))
            .andExpect(jsonPath("$.[*].tellersNames").value(hasItem(DEFAULT_TELLERS_NAMES)))
            .andExpect(jsonPath("$.[*].dateSubmittedToCBK").value(hasItem(DEFAULT_DATE_SUBMITTED_TO_CBK.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWeeklyCounterfeitHoldingShouldNotBeFound(String filter) throws Exception {
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWeeklyCounterfeitHolding() throws Exception {
        // Get the weeklyCounterfeitHolding
        restWeeklyCounterfeitHoldingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeeklyCounterfeitHolding() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();

        // Update the weeklyCounterfeitHolding
        WeeklyCounterfeitHolding updatedWeeklyCounterfeitHolding = weeklyCounterfeitHoldingRepository
            .findById(weeklyCounterfeitHolding.getId())
            .get();
        // Disconnect from session so that the updates on updatedWeeklyCounterfeitHolding are not directly saved in db
        em.detach(updatedWeeklyCounterfeitHolding);
        updatedWeeklyCounterfeitHolding
            .reportingDate(UPDATED_REPORTING_DATE)
            .dateConfiscated(UPDATED_DATE_CONFISCATED)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .depositorsNames(UPDATED_DEPOSITORS_NAMES)
            .tellersNames(UPDATED_TELLERS_NAMES)
            .dateSubmittedToCBK(UPDATED_DATE_SUBMITTED_TO_CBK)
            .remarks(UPDATED_REMARKS);
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(updatedWeeklyCounterfeitHolding);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyCounterfeitHoldingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCounterfeitHolding testWeeklyCounterfeitHolding = weeklyCounterfeitHoldingList.get(weeklyCounterfeitHoldingList.size() - 1);
        assertThat(testWeeklyCounterfeitHolding.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testWeeklyCounterfeitHolding.getDateConfiscated()).isEqualTo(UPDATED_DATE_CONFISCATED);
        assertThat(testWeeklyCounterfeitHolding.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testWeeklyCounterfeitHolding.getDepositorsNames()).isEqualTo(UPDATED_DEPOSITORS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getTellersNames()).isEqualTo(UPDATED_TELLERS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getDateSubmittedToCBK()).isEqualTo(UPDATED_DATE_SUBMITTED_TO_CBK);
        assertThat(testWeeklyCounterfeitHolding.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository).save(testWeeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void putNonExistingWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyCounterfeitHoldingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void partialUpdateWeeklyCounterfeitHoldingWithPatch() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();

        // Update the weeklyCounterfeitHolding using partial update
        WeeklyCounterfeitHolding partialUpdatedWeeklyCounterfeitHolding = new WeeklyCounterfeitHolding();
        partialUpdatedWeeklyCounterfeitHolding.setId(weeklyCounterfeitHolding.getId());

        partialUpdatedWeeklyCounterfeitHolding
            .reportingDate(UPDATED_REPORTING_DATE)
            .dateConfiscated(UPDATED_DATE_CONFISCATED)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .tellersNames(UPDATED_TELLERS_NAMES);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyCounterfeitHolding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeeklyCounterfeitHolding))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCounterfeitHolding testWeeklyCounterfeitHolding = weeklyCounterfeitHoldingList.get(weeklyCounterfeitHoldingList.size() - 1);
        assertThat(testWeeklyCounterfeitHolding.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testWeeklyCounterfeitHolding.getDateConfiscated()).isEqualTo(UPDATED_DATE_CONFISCATED);
        assertThat(testWeeklyCounterfeitHolding.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testWeeklyCounterfeitHolding.getDepositorsNames()).isEqualTo(DEFAULT_DEPOSITORS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getTellersNames()).isEqualTo(UPDATED_TELLERS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getDateSubmittedToCBK()).isEqualTo(DEFAULT_DATE_SUBMITTED_TO_CBK);
        assertThat(testWeeklyCounterfeitHolding.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateWeeklyCounterfeitHoldingWithPatch() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();

        // Update the weeklyCounterfeitHolding using partial update
        WeeklyCounterfeitHolding partialUpdatedWeeklyCounterfeitHolding = new WeeklyCounterfeitHolding();
        partialUpdatedWeeklyCounterfeitHolding.setId(weeklyCounterfeitHolding.getId());

        partialUpdatedWeeklyCounterfeitHolding
            .reportingDate(UPDATED_REPORTING_DATE)
            .dateConfiscated(UPDATED_DATE_CONFISCATED)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .depositorsNames(UPDATED_DEPOSITORS_NAMES)
            .tellersNames(UPDATED_TELLERS_NAMES)
            .dateSubmittedToCBK(UPDATED_DATE_SUBMITTED_TO_CBK)
            .remarks(UPDATED_REMARKS);

        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyCounterfeitHolding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeeklyCounterfeitHolding))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCounterfeitHolding testWeeklyCounterfeitHolding = weeklyCounterfeitHoldingList.get(weeklyCounterfeitHoldingList.size() - 1);
        assertThat(testWeeklyCounterfeitHolding.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testWeeklyCounterfeitHolding.getDateConfiscated()).isEqualTo(UPDATED_DATE_CONFISCATED);
        assertThat(testWeeklyCounterfeitHolding.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testWeeklyCounterfeitHolding.getDepositorsNames()).isEqualTo(UPDATED_DEPOSITORS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getTellersNames()).isEqualTo(UPDATED_TELLERS_NAMES);
        assertThat(testWeeklyCounterfeitHolding.getDateSubmittedToCBK()).isEqualTo(UPDATED_DATE_SUBMITTED_TO_CBK);
        assertThat(testWeeklyCounterfeitHolding.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weeklyCounterfeitHoldingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeeklyCounterfeitHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCounterfeitHoldingRepository.findAll().size();
        weeklyCounterfeitHolding.setId(count.incrementAndGet());

        // Create the WeeklyCounterfeitHolding
        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCounterfeitHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCounterfeitHoldingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyCounterfeitHolding in the database
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(0)).save(weeklyCounterfeitHolding);
    }

    @Test
    @Transactional
    void deleteWeeklyCounterfeitHolding() throws Exception {
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);

        int databaseSizeBeforeDelete = weeklyCounterfeitHoldingRepository.findAll().size();

        // Delete the weeklyCounterfeitHolding
        restWeeklyCounterfeitHoldingMockMvc
            .perform(delete(ENTITY_API_URL_ID, weeklyCounterfeitHolding.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeeklyCounterfeitHolding> weeklyCounterfeitHoldingList = weeklyCounterfeitHoldingRepository.findAll();
        assertThat(weeklyCounterfeitHoldingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WeeklyCounterfeitHolding in Elasticsearch
        verify(mockWeeklyCounterfeitHoldingSearchRepository, times(1)).deleteById(weeklyCounterfeitHolding.getId());
    }

    @Test
    @Transactional
    void searchWeeklyCounterfeitHolding() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        weeklyCounterfeitHoldingRepository.saveAndFlush(weeklyCounterfeitHolding);
        when(mockWeeklyCounterfeitHoldingSearchRepository.search("id:" + weeklyCounterfeitHolding.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(weeklyCounterfeitHolding), PageRequest.of(0, 1), 1));

        // Search the weeklyCounterfeitHolding
        restWeeklyCounterfeitHoldingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + weeklyCounterfeitHolding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCounterfeitHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateConfiscated").value(hasItem(DEFAULT_DATE_CONFISCATED.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].depositorsNames").value(hasItem(DEFAULT_DEPOSITORS_NAMES)))
            .andExpect(jsonPath("$.[*].tellersNames").value(hasItem(DEFAULT_TELLERS_NAMES)))
            .andExpect(jsonPath("$.[*].dateSubmittedToCBK").value(hasItem(DEFAULT_DATE_SUBMITTED_TO_CBK.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
