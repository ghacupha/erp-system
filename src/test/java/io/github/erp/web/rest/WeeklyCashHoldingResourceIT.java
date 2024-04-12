package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.CountySubCountyCode;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.KenyanCurrencyDenomination;
import io.github.erp.domain.WeeklyCashHolding;
import io.github.erp.repository.WeeklyCashHoldingRepository;
import io.github.erp.repository.search.WeeklyCashHoldingSearchRepository;
import io.github.erp.service.criteria.WeeklyCashHoldingCriteria;
import io.github.erp.service.dto.WeeklyCashHoldingDTO;
import io.github.erp.service.mapper.WeeklyCashHoldingMapper;
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
 * Integration tests for the {@link WeeklyCashHoldingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WeeklyCashHoldingResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_FIT_UNITS = 1;
    private static final Integer UPDATED_FIT_UNITS = 2;
    private static final Integer SMALLER_FIT_UNITS = 1 - 1;

    private static final Integer DEFAULT_UNFIT_UNITS = 1;
    private static final Integer UPDATED_UNFIT_UNITS = 2;
    private static final Integer SMALLER_UNFIT_UNITS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/weekly-cash-holdings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/weekly-cash-holdings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeeklyCashHoldingRepository weeklyCashHoldingRepository;

    @Autowired
    private WeeklyCashHoldingMapper weeklyCashHoldingMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WeeklyCashHoldingSearchRepositoryMockConfiguration
     */
    @Autowired
    private WeeklyCashHoldingSearchRepository mockWeeklyCashHoldingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeeklyCashHoldingMockMvc;

    private WeeklyCashHolding weeklyCashHolding;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyCashHolding createEntity(EntityManager em) {
        WeeklyCashHolding weeklyCashHolding = new WeeklyCashHolding()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .fitUnits(DEFAULT_FIT_UNITS)
            .unfitUnits(DEFAULT_UNFIT_UNITS);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        weeklyCashHolding.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        weeklyCashHolding.setBranchId(bankBranchCode);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        weeklyCashHolding.setSubCountyCode(countySubCountyCode);
        // Add required entity
        KenyanCurrencyDenomination kenyanCurrencyDenomination;
        if (TestUtil.findAll(em, KenyanCurrencyDenomination.class).isEmpty()) {
            kenyanCurrencyDenomination = KenyanCurrencyDenominationResourceIT.createEntity(em);
            em.persist(kenyanCurrencyDenomination);
            em.flush();
        } else {
            kenyanCurrencyDenomination = TestUtil.findAll(em, KenyanCurrencyDenomination.class).get(0);
        }
        weeklyCashHolding.setDenomination(kenyanCurrencyDenomination);
        return weeklyCashHolding;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyCashHolding createUpdatedEntity(EntityManager em) {
        WeeklyCashHolding weeklyCashHolding = new WeeklyCashHolding()
            .reportingDate(UPDATED_REPORTING_DATE)
            .fitUnits(UPDATED_FIT_UNITS)
            .unfitUnits(UPDATED_UNFIT_UNITS);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        weeklyCashHolding.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        weeklyCashHolding.setBranchId(bankBranchCode);
        // Add required entity
        CountySubCountyCode countySubCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            countySubCountyCode = CountySubCountyCodeResourceIT.createUpdatedEntity(em);
            em.persist(countySubCountyCode);
            em.flush();
        } else {
            countySubCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        weeklyCashHolding.setSubCountyCode(countySubCountyCode);
        // Add required entity
        KenyanCurrencyDenomination kenyanCurrencyDenomination;
        if (TestUtil.findAll(em, KenyanCurrencyDenomination.class).isEmpty()) {
            kenyanCurrencyDenomination = KenyanCurrencyDenominationResourceIT.createUpdatedEntity(em);
            em.persist(kenyanCurrencyDenomination);
            em.flush();
        } else {
            kenyanCurrencyDenomination = TestUtil.findAll(em, KenyanCurrencyDenomination.class).get(0);
        }
        weeklyCashHolding.setDenomination(kenyanCurrencyDenomination);
        return weeklyCashHolding;
    }

    @BeforeEach
    public void initTest() {
        weeklyCashHolding = createEntity(em);
    }

    @Test
    @Transactional
    void createWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeCreate = weeklyCashHoldingRepository.findAll().size();
        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);
        restWeeklyCashHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyCashHolding testWeeklyCashHolding = weeklyCashHoldingList.get(weeklyCashHoldingList.size() - 1);
        assertThat(testWeeklyCashHolding.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testWeeklyCashHolding.getFitUnits()).isEqualTo(DEFAULT_FIT_UNITS);
        assertThat(testWeeklyCashHolding.getUnfitUnits()).isEqualTo(DEFAULT_UNFIT_UNITS);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(1)).save(testWeeklyCashHolding);
    }

    @Test
    @Transactional
    void createWeeklyCashHoldingWithExistingId() throws Exception {
        // Create the WeeklyCashHolding with an existing ID
        weeklyCashHolding.setId(1L);
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        int databaseSizeBeforeCreate = weeklyCashHoldingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeeklyCashHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeCreate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCashHoldingRepository.findAll().size();
        // set the field null
        weeklyCashHolding.setReportingDate(null);

        // Create the WeeklyCashHolding, which fails.
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        restWeeklyCashHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFitUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCashHoldingRepository.findAll().size();
        // set the field null
        weeklyCashHolding.setFitUnits(null);

        // Create the WeeklyCashHolding, which fails.
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        restWeeklyCashHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnfitUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = weeklyCashHoldingRepository.findAll().size();
        // set the field null
        weeklyCashHolding.setUnfitUnits(null);

        // Create the WeeklyCashHolding, which fails.
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        restWeeklyCashHoldingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldings() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCashHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].fitUnits").value(hasItem(DEFAULT_FIT_UNITS)))
            .andExpect(jsonPath("$.[*].unfitUnits").value(hasItem(DEFAULT_UNFIT_UNITS)));
    }

    @Test
    @Transactional
    void getWeeklyCashHolding() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get the weeklyCashHolding
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL_ID, weeklyCashHolding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weeklyCashHolding.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.fitUnits").value(DEFAULT_FIT_UNITS))
            .andExpect(jsonPath("$.unfitUnits").value(DEFAULT_UNFIT_UNITS));
    }

    @Test
    @Transactional
    void getWeeklyCashHoldingsByIdFiltering() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        Long id = weeklyCashHolding.getId();

        defaultWeeklyCashHoldingShouldBeFound("id.equals=" + id);
        defaultWeeklyCashHoldingShouldNotBeFound("id.notEquals=" + id);

        defaultWeeklyCashHoldingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWeeklyCashHoldingShouldNotBeFound("id.greaterThan=" + id);

        defaultWeeklyCashHoldingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWeeklyCashHoldingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate is not null
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.specified=true");

        // Get all the weeklyCashHoldingList where reportingDate is null
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultWeeklyCashHoldingShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the weeklyCashHoldingList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultWeeklyCashHoldingShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits equals to DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.equals=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits equals to UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.equals=" + UPDATED_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits not equals to DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.notEquals=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits not equals to UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.notEquals=" + UPDATED_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits in DEFAULT_FIT_UNITS or UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.in=" + DEFAULT_FIT_UNITS + "," + UPDATED_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits equals to UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.in=" + UPDATED_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits is not null
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.specified=true");

        // Get all the weeklyCashHoldingList where fitUnits is null
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits is greater than or equal to DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.greaterThanOrEqual=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits is greater than or equal to UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.greaterThanOrEqual=" + UPDATED_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits is less than or equal to DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.lessThanOrEqual=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits is less than or equal to SMALLER_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.lessThanOrEqual=" + SMALLER_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits is less than DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.lessThan=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits is less than UPDATED_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.lessThan=" + UPDATED_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByFitUnitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where fitUnits is greater than DEFAULT_FIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("fitUnits.greaterThan=" + DEFAULT_FIT_UNITS);

        // Get all the weeklyCashHoldingList where fitUnits is greater than SMALLER_FIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("fitUnits.greaterThan=" + SMALLER_FIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits equals to DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.equals=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits equals to UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.equals=" + UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits not equals to DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.notEquals=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits not equals to UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.notEquals=" + UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits in DEFAULT_UNFIT_UNITS or UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.in=" + DEFAULT_UNFIT_UNITS + "," + UPDATED_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits equals to UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.in=" + UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits is not null
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.specified=true");

        // Get all the weeklyCashHoldingList where unfitUnits is null
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.specified=false");
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits is greater than or equal to DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.greaterThanOrEqual=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits is greater than or equal to UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.greaterThanOrEqual=" + UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits is less than or equal to DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.lessThanOrEqual=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits is less than or equal to SMALLER_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.lessThanOrEqual=" + SMALLER_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits is less than DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.lessThan=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits is less than UPDATED_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.lessThan=" + UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByUnfitUnitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        // Get all the weeklyCashHoldingList where unfitUnits is greater than DEFAULT_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldNotBeFound("unfitUnits.greaterThan=" + DEFAULT_UNFIT_UNITS);

        // Get all the weeklyCashHoldingList where unfitUnits is greater than SMALLER_UNFIT_UNITS
        defaultWeeklyCashHoldingShouldBeFound("unfitUnits.greaterThan=" + SMALLER_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        InstitutionCode bankCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        weeklyCashHolding.setBankCode(bankCode);
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        Long bankCodeId = bankCode.getId();

        // Get all the weeklyCashHoldingList where bankCode equals to bankCodeId
        defaultWeeklyCashHoldingShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the weeklyCashHoldingList where bankCode equals to (bankCodeId + 1)
        defaultWeeklyCashHoldingShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        BankBranchCode branchId;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchId = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchId);
            em.flush();
        } else {
            branchId = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchId);
        em.flush();
        weeklyCashHolding.setBranchId(branchId);
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        Long branchIdId = branchId.getId();

        // Get all the weeklyCashHoldingList where branchId equals to branchIdId
        defaultWeeklyCashHoldingShouldBeFound("branchIdId.equals=" + branchIdId);

        // Get all the weeklyCashHoldingList where branchId equals to (branchIdId + 1)
        defaultWeeklyCashHoldingShouldNotBeFound("branchIdId.equals=" + (branchIdId + 1));
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsBySubCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        CountySubCountyCode subCountyCode;
        if (TestUtil.findAll(em, CountySubCountyCode.class).isEmpty()) {
            subCountyCode = CountySubCountyCodeResourceIT.createEntity(em);
            em.persist(subCountyCode);
            em.flush();
        } else {
            subCountyCode = TestUtil.findAll(em, CountySubCountyCode.class).get(0);
        }
        em.persist(subCountyCode);
        em.flush();
        weeklyCashHolding.setSubCountyCode(subCountyCode);
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        Long subCountyCodeId = subCountyCode.getId();

        // Get all the weeklyCashHoldingList where subCountyCode equals to subCountyCodeId
        defaultWeeklyCashHoldingShouldBeFound("subCountyCodeId.equals=" + subCountyCodeId);

        // Get all the weeklyCashHoldingList where subCountyCode equals to (subCountyCodeId + 1)
        defaultWeeklyCashHoldingShouldNotBeFound("subCountyCodeId.equals=" + (subCountyCodeId + 1));
    }

    @Test
    @Transactional
    void getAllWeeklyCashHoldingsByDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        KenyanCurrencyDenomination denomination;
        if (TestUtil.findAll(em, KenyanCurrencyDenomination.class).isEmpty()) {
            denomination = KenyanCurrencyDenominationResourceIT.createEntity(em);
            em.persist(denomination);
            em.flush();
        } else {
            denomination = TestUtil.findAll(em, KenyanCurrencyDenomination.class).get(0);
        }
        em.persist(denomination);
        em.flush();
        weeklyCashHolding.setDenomination(denomination);
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        Long denominationId = denomination.getId();

        // Get all the weeklyCashHoldingList where denomination equals to denominationId
        defaultWeeklyCashHoldingShouldBeFound("denominationId.equals=" + denominationId);

        // Get all the weeklyCashHoldingList where denomination equals to (denominationId + 1)
        defaultWeeklyCashHoldingShouldNotBeFound("denominationId.equals=" + (denominationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWeeklyCashHoldingShouldBeFound(String filter) throws Exception {
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCashHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].fitUnits").value(hasItem(DEFAULT_FIT_UNITS)))
            .andExpect(jsonPath("$.[*].unfitUnits").value(hasItem(DEFAULT_UNFIT_UNITS)));

        // Check, that the count call also returns 1
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWeeklyCashHoldingShouldNotBeFound(String filter) throws Exception {
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWeeklyCashHolding() throws Exception {
        // Get the weeklyCashHolding
        restWeeklyCashHoldingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeeklyCashHolding() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();

        // Update the weeklyCashHolding
        WeeklyCashHolding updatedWeeklyCashHolding = weeklyCashHoldingRepository.findById(weeklyCashHolding.getId()).get();
        // Disconnect from session so that the updates on updatedWeeklyCashHolding are not directly saved in db
        em.detach(updatedWeeklyCashHolding);
        updatedWeeklyCashHolding.reportingDate(UPDATED_REPORTING_DATE).fitUnits(UPDATED_FIT_UNITS).unfitUnits(UPDATED_UNFIT_UNITS);
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(updatedWeeklyCashHolding);

        restWeeklyCashHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyCashHoldingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCashHolding testWeeklyCashHolding = weeklyCashHoldingList.get(weeklyCashHoldingList.size() - 1);
        assertThat(testWeeklyCashHolding.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testWeeklyCashHolding.getFitUnits()).isEqualTo(UPDATED_FIT_UNITS);
        assertThat(testWeeklyCashHolding.getUnfitUnits()).isEqualTo(UPDATED_UNFIT_UNITS);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository).save(testWeeklyCashHolding);
    }

    @Test
    @Transactional
    void putNonExistingWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyCashHoldingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void partialUpdateWeeklyCashHoldingWithPatch() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();

        // Update the weeklyCashHolding using partial update
        WeeklyCashHolding partialUpdatedWeeklyCashHolding = new WeeklyCashHolding();
        partialUpdatedWeeklyCashHolding.setId(weeklyCashHolding.getId());

        restWeeklyCashHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyCashHolding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeeklyCashHolding))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCashHolding testWeeklyCashHolding = weeklyCashHoldingList.get(weeklyCashHoldingList.size() - 1);
        assertThat(testWeeklyCashHolding.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testWeeklyCashHolding.getFitUnits()).isEqualTo(DEFAULT_FIT_UNITS);
        assertThat(testWeeklyCashHolding.getUnfitUnits()).isEqualTo(DEFAULT_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void fullUpdateWeeklyCashHoldingWithPatch() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();

        // Update the weeklyCashHolding using partial update
        WeeklyCashHolding partialUpdatedWeeklyCashHolding = new WeeklyCashHolding();
        partialUpdatedWeeklyCashHolding.setId(weeklyCashHolding.getId());

        partialUpdatedWeeklyCashHolding.reportingDate(UPDATED_REPORTING_DATE).fitUnits(UPDATED_FIT_UNITS).unfitUnits(UPDATED_UNFIT_UNITS);

        restWeeklyCashHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyCashHolding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeeklyCashHolding))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);
        WeeklyCashHolding testWeeklyCashHolding = weeklyCashHoldingList.get(weeklyCashHoldingList.size() - 1);
        assertThat(testWeeklyCashHolding.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testWeeklyCashHolding.getFitUnits()).isEqualTo(UPDATED_FIT_UNITS);
        assertThat(testWeeklyCashHolding.getUnfitUnits()).isEqualTo(UPDATED_UNFIT_UNITS);
    }

    @Test
    @Transactional
    void patchNonExistingWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weeklyCashHoldingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeeklyCashHolding() throws Exception {
        int databaseSizeBeforeUpdate = weeklyCashHoldingRepository.findAll().size();
        weeklyCashHolding.setId(count.incrementAndGet());

        // Create the WeeklyCashHolding
        WeeklyCashHoldingDTO weeklyCashHoldingDTO = weeklyCashHoldingMapper.toDto(weeklyCashHolding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyCashHoldingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weeklyCashHoldingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyCashHolding in the database
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(0)).save(weeklyCashHolding);
    }

    @Test
    @Transactional
    void deleteWeeklyCashHolding() throws Exception {
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);

        int databaseSizeBeforeDelete = weeklyCashHoldingRepository.findAll().size();

        // Delete the weeklyCashHolding
        restWeeklyCashHoldingMockMvc
            .perform(delete(ENTITY_API_URL_ID, weeklyCashHolding.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeeklyCashHolding> weeklyCashHoldingList = weeklyCashHoldingRepository.findAll();
        assertThat(weeklyCashHoldingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WeeklyCashHolding in Elasticsearch
        verify(mockWeeklyCashHoldingSearchRepository, times(1)).deleteById(weeklyCashHolding.getId());
    }

    @Test
    @Transactional
    void searchWeeklyCashHolding() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        weeklyCashHoldingRepository.saveAndFlush(weeklyCashHolding);
        when(mockWeeklyCashHoldingSearchRepository.search("id:" + weeklyCashHolding.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(weeklyCashHolding), PageRequest.of(0, 1), 1));

        // Search the weeklyCashHolding
        restWeeklyCashHoldingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + weeklyCashHolding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyCashHolding.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].fitUnits").value(hasItem(DEFAULT_FIT_UNITS)))
            .andExpect(jsonPath("$.[*].unfitUnits").value(hasItem(DEFAULT_UNFIT_UNITS)));
    }
}
