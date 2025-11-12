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
import io.github.erp.domain.*;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.repository.search.PrepaymentMarshallingSearchRepository;
import io.github.erp.service.PrepaymentMarshallingService;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import io.github.erp.service.mapper.PrepaymentMarshallingMapper;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the PrepaymentMarshallingResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
class PrepaymentMarshallingResourceIT {

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    private static final Integer DEFAULT_AMORTIZATION_PERIODS = 1;
    private static final Integer UPDATED_AMORTIZATION_PERIODS = 2;
    private static final Integer SMALLER_AMORTIZATION_PERIODS = 1 - 1;

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final UUID DEFAULT_COMPILATION_TOKEN = UUID.randomUUID();
    private static final UUID UPDATED_COMPILATION_TOKEN = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/prepayments/prepayment-marshallings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/prepayment-marshallings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentMarshallingRepository prepaymentMarshallingRepository;

    @Mock
    private PrepaymentMarshallingRepository prepaymentMarshallingRepositoryMock;

    @Autowired
    private PrepaymentMarshallingMapper prepaymentMarshallingMapper;

    @Mock
    private PrepaymentMarshallingService prepaymentMarshallingServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentMarshallingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentMarshallingSearchRepository mockPrepaymentMarshallingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentMarshallingMockMvc;

    private PrepaymentMarshalling prepaymentMarshalling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentMarshalling createEntity(EntityManager em) {
        PrepaymentMarshalling prepaymentMarshalling = new PrepaymentMarshalling()
            .inactive(DEFAULT_INACTIVE)
            .amortizationPeriods(DEFAULT_AMORTIZATION_PERIODS)
            .processed(DEFAULT_PROCESSED)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        prepaymentMarshalling.setPrepaymentAccount(prepaymentAccount);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        prepaymentMarshalling.setFirstFiscalMonth(fiscalMonth);
        // Add required entity
        prepaymentMarshalling.setLastFiscalMonth(fiscalMonth);
        // Add required entity
        AmortizationPeriod amortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            amortizationPeriod = AmortizationPeriodResourceIT.createEntity(em);
            em.persist(amortizationPeriod);
            em.flush();
        } else {
            amortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        prepaymentMarshalling.setFirstAmortizationPeriod(amortizationPeriod);
        return prepaymentMarshalling;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentMarshalling createUpdatedEntity(EntityManager em) {
        PrepaymentMarshalling prepaymentMarshalling = new PrepaymentMarshalling()
            .inactive(UPDATED_INACTIVE)
            .amortizationPeriods(UPDATED_AMORTIZATION_PERIODS)
            .processed(UPDATED_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createUpdatedEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        prepaymentMarshalling.setPrepaymentAccount(prepaymentAccount);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        prepaymentMarshalling.setFirstFiscalMonth(fiscalMonth);
        // Add required entity
        prepaymentMarshalling.setLastFiscalMonth(fiscalMonth);
        // Add required entity
        AmortizationPeriod amortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            amortizationPeriod = AmortizationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(amortizationPeriod);
            em.flush();
        } else {
            amortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        prepaymentMarshalling.setFirstAmortizationPeriod(amortizationPeriod);
        return prepaymentMarshalling;
    }

    @BeforeEach
    public void initTest() {
        prepaymentMarshalling = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeCreate = prepaymentMarshallingRepository.findAll().size();
        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);
        restPrepaymentMarshallingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentMarshalling testPrepaymentMarshalling = prepaymentMarshallingList.get(prepaymentMarshallingList.size() - 1);
        assertThat(testPrepaymentMarshalling.getInactive()).isEqualTo(DEFAULT_INACTIVE);
        assertThat(testPrepaymentMarshalling.getAmortizationPeriods()).isEqualTo(DEFAULT_AMORTIZATION_PERIODS);
        assertThat(testPrepaymentMarshalling.getProcessed()).isEqualTo(DEFAULT_PROCESSED);
        assertThat(testPrepaymentMarshalling.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(1)).save(testPrepaymentMarshalling);
    }

    @Test
    @Transactional
    void createPrepaymentMarshallingWithExistingId() throws Exception {
        // Create the PrepaymentMarshalling with an existing ID
        prepaymentMarshalling.setId(1L);
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        int databaseSizeBeforeCreate = prepaymentMarshallingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentMarshallingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallings() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMarshalling.getId().intValue())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationPeriods").value(hasItem(DEFAULT_AMORTIZATION_PERIODS)))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentMarshallingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prepaymentMarshallingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentMarshallingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentMarshallingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentMarshallingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prepaymentMarshallingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentMarshallingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentMarshallingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPrepaymentMarshalling() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get the prepaymentMarshalling
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentMarshalling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentMarshalling.getId().intValue()))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()))
            .andExpect(jsonPath("$.amortizationPeriods").value(DEFAULT_AMORTIZATION_PERIODS))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN.toString()));
    }

    @Test
    @Transactional
    void getPrepaymentMarshallingsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        Long id = prepaymentMarshalling.getId();

        defaultPrepaymentMarshallingShouldBeFound("id.equals=" + id);
        defaultPrepaymentMarshallingShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentMarshallingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentMarshallingShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentMarshallingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentMarshallingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByInactiveIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where inactive equals to DEFAULT_INACTIVE
        defaultPrepaymentMarshallingShouldBeFound("inactive.equals=" + DEFAULT_INACTIVE);

        // Get all the prepaymentMarshallingList where inactive equals to UPDATED_INACTIVE
        defaultPrepaymentMarshallingShouldNotBeFound("inactive.equals=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByInactiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where inactive not equals to DEFAULT_INACTIVE
        defaultPrepaymentMarshallingShouldNotBeFound("inactive.notEquals=" + DEFAULT_INACTIVE);

        // Get all the prepaymentMarshallingList where inactive not equals to UPDATED_INACTIVE
        defaultPrepaymentMarshallingShouldBeFound("inactive.notEquals=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByInactiveIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where inactive in DEFAULT_INACTIVE or UPDATED_INACTIVE
        defaultPrepaymentMarshallingShouldBeFound("inactive.in=" + DEFAULT_INACTIVE + "," + UPDATED_INACTIVE);

        // Get all the prepaymentMarshallingList where inactive equals to UPDATED_INACTIVE
        defaultPrepaymentMarshallingShouldNotBeFound("inactive.in=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByInactiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where inactive is not null
        defaultPrepaymentMarshallingShouldBeFound("inactive.specified=true");

        // Get all the prepaymentMarshallingList where inactive is null
        defaultPrepaymentMarshallingShouldNotBeFound("inactive.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods equals to DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.equals=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods equals to UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.equals=" + UPDATED_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods not equals to DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.notEquals=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods not equals to UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.notEquals=" + UPDATED_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods in DEFAULT_AMORTIZATION_PERIODS or UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound(
            "amortizationPeriods.in=" + DEFAULT_AMORTIZATION_PERIODS + "," + UPDATED_AMORTIZATION_PERIODS
        );

        // Get all the prepaymentMarshallingList where amortizationPeriods equals to UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.in=" + UPDATED_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods is not null
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.specified=true");

        // Get all the prepaymentMarshallingList where amortizationPeriods is null
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods is greater than or equal to DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.greaterThanOrEqual=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods is greater than or equal to UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.greaterThanOrEqual=" + UPDATED_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods is less than or equal to DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.lessThanOrEqual=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods is less than or equal to SMALLER_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.lessThanOrEqual=" + SMALLER_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods is less than DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.lessThan=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods is less than UPDATED_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.lessThan=" + UPDATED_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByAmortizationPeriodsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where amortizationPeriods is greater than DEFAULT_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldNotBeFound("amortizationPeriods.greaterThan=" + DEFAULT_AMORTIZATION_PERIODS);

        // Get all the prepaymentMarshallingList where amortizationPeriods is greater than SMALLER_AMORTIZATION_PERIODS
        defaultPrepaymentMarshallingShouldBeFound("amortizationPeriods.greaterThan=" + SMALLER_AMORTIZATION_PERIODS);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where processed equals to DEFAULT_PROCESSED
        defaultPrepaymentMarshallingShouldBeFound("processed.equals=" + DEFAULT_PROCESSED);

        // Get all the prepaymentMarshallingList where processed equals to UPDATED_PROCESSED
        defaultPrepaymentMarshallingShouldNotBeFound("processed.equals=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where processed not equals to DEFAULT_PROCESSED
        defaultPrepaymentMarshallingShouldNotBeFound("processed.notEquals=" + DEFAULT_PROCESSED);

        // Get all the prepaymentMarshallingList where processed not equals to UPDATED_PROCESSED
        defaultPrepaymentMarshallingShouldBeFound("processed.notEquals=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where processed in DEFAULT_PROCESSED or UPDATED_PROCESSED
        defaultPrepaymentMarshallingShouldBeFound("processed.in=" + DEFAULT_PROCESSED + "," + UPDATED_PROCESSED);

        // Get all the prepaymentMarshallingList where processed equals to UPDATED_PROCESSED
        defaultPrepaymentMarshallingShouldNotBeFound("processed.in=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where processed is not null
        defaultPrepaymentMarshallingShouldBeFound("processed.specified=true");

        // Get all the prepaymentMarshallingList where processed is null
        defaultPrepaymentMarshallingShouldNotBeFound("processed.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the prepaymentMarshallingList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the prepaymentMarshallingList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the prepaymentMarshallingList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPrepaymentMarshallingShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        // Get all the prepaymentMarshallingList where compilationToken is not null
        defaultPrepaymentMarshallingShouldBeFound("compilationToken.specified=true");

        // Get all the prepaymentMarshallingList where compilationToken is null
        defaultPrepaymentMarshallingShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByPrepaymentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        em.persist(prepaymentAccount);
        em.flush();
        prepaymentMarshalling.setPrepaymentAccount(prepaymentAccount);
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        Long prepaymentAccountId = prepaymentAccount.getId();

        // Get all the prepaymentMarshallingList where prepaymentAccount equals to prepaymentAccountId
        defaultPrepaymentMarshallingShouldBeFound("prepaymentAccountId.equals=" + prepaymentAccountId);

        // Get all the prepaymentMarshallingList where prepaymentAccount equals to (prepaymentAccountId + 1)
        defaultPrepaymentMarshallingShouldNotBeFound("prepaymentAccountId.equals=" + (prepaymentAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
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
        prepaymentMarshalling.addPlaceholder(placeholder);
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        Long placeholderId = placeholder.getId();

        // Get all the prepaymentMarshallingList where placeholder equals to placeholderId
        defaultPrepaymentMarshallingShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the prepaymentMarshallingList where placeholder equals to (placeholderId + 1)
        defaultPrepaymentMarshallingShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByFirstFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        FiscalMonth firstFiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            firstFiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(firstFiscalMonth);
            em.flush();
        } else {
            firstFiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(firstFiscalMonth);
        em.flush();
        prepaymentMarshalling.setFirstFiscalMonth(firstFiscalMonth);
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        Long firstFiscalMonthId = firstFiscalMonth.getId();

        // Get all the prepaymentMarshallingList where firstFiscalMonth equals to firstFiscalMonthId
        defaultPrepaymentMarshallingShouldBeFound("firstFiscalMonthId.equals=" + firstFiscalMonthId);

        // Get all the prepaymentMarshallingList where firstFiscalMonth equals to (firstFiscalMonthId + 1)
        defaultPrepaymentMarshallingShouldNotBeFound("firstFiscalMonthId.equals=" + (firstFiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByLastFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        FiscalMonth lastFiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            lastFiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(lastFiscalMonth);
            em.flush();
        } else {
            lastFiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(lastFiscalMonth);
        em.flush();
        prepaymentMarshalling.setLastFiscalMonth(lastFiscalMonth);
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        Long lastFiscalMonthId = lastFiscalMonth.getId();

        // Get all the prepaymentMarshallingList where lastFiscalMonth equals to lastFiscalMonthId
        defaultPrepaymentMarshallingShouldBeFound("lastFiscalMonthId.equals=" + lastFiscalMonthId);

        // Get all the prepaymentMarshallingList where lastFiscalMonth equals to (lastFiscalMonthId + 1)
        defaultPrepaymentMarshallingShouldNotBeFound("lastFiscalMonthId.equals=" + (lastFiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentMarshallingsByFirstAmortizationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        AmortizationPeriod firstAmortizationPeriod;
        if (TestUtil.findAll(em, AmortizationPeriod.class).isEmpty()) {
            firstAmortizationPeriod = AmortizationPeriodResourceIT.createEntity(em);
            em.persist(firstAmortizationPeriod);
            em.flush();
        } else {
            firstAmortizationPeriod = TestUtil.findAll(em, AmortizationPeriod.class).get(0);
        }
        em.persist(firstAmortizationPeriod);
        em.flush();
        prepaymentMarshalling.setFirstAmortizationPeriod(firstAmortizationPeriod);
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        Long firstAmortizationPeriodId = firstAmortizationPeriod.getId();

        // Get all the prepaymentMarshallingList where firstAmortizationPeriod equals to firstAmortizationPeriodId
        defaultPrepaymentMarshallingShouldBeFound("firstAmortizationPeriodId.equals=" + firstAmortizationPeriodId);

        // Get all the prepaymentMarshallingList where firstAmortizationPeriod equals to (firstAmortizationPeriodId + 1)
        defaultPrepaymentMarshallingShouldNotBeFound("firstAmortizationPeriodId.equals=" + (firstAmortizationPeriodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentMarshallingShouldBeFound(String filter) throws Exception {
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMarshalling.getId().intValue())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationPeriods").value(hasItem(DEFAULT_AMORTIZATION_PERIODS)))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));

        // Check, that the count call also returns 1
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentMarshallingShouldNotBeFound(String filter) throws Exception {
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentMarshalling() throws Exception {
        // Get the prepaymentMarshalling
        restPrepaymentMarshallingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentMarshalling() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();

        // Update the prepaymentMarshalling
        PrepaymentMarshalling updatedPrepaymentMarshalling = prepaymentMarshallingRepository.findById(prepaymentMarshalling.getId()).get();
        // Disconnect from session so that the updates on updatedPrepaymentMarshalling are not directly saved in db
        em.detach(updatedPrepaymentMarshalling);
        updatedPrepaymentMarshalling
            .inactive(UPDATED_INACTIVE)
            .amortizationPeriods(UPDATED_AMORTIZATION_PERIODS)
            .processed(UPDATED_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(updatedPrepaymentMarshalling);

        restPrepaymentMarshallingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentMarshallingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMarshalling testPrepaymentMarshalling = prepaymentMarshallingList.get(prepaymentMarshallingList.size() - 1);
        assertThat(testPrepaymentMarshalling.getInactive()).isEqualTo(UPDATED_INACTIVE);
        assertThat(testPrepaymentMarshalling.getAmortizationPeriods()).isEqualTo(UPDATED_AMORTIZATION_PERIODS);
        assertThat(testPrepaymentMarshalling.getProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testPrepaymentMarshalling.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository).save(testPrepaymentMarshalling);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentMarshallingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentMarshallingWithPatch() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();

        // Update the prepaymentMarshalling using partial update
        PrepaymentMarshalling partialUpdatedPrepaymentMarshalling = new PrepaymentMarshalling();
        partialUpdatedPrepaymentMarshalling.setId(prepaymentMarshalling.getId());

        partialUpdatedPrepaymentMarshalling
            .amortizationPeriods(UPDATED_AMORTIZATION_PERIODS)
            .processed(UPDATED_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restPrepaymentMarshallingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentMarshalling.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentMarshalling))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMarshalling testPrepaymentMarshalling = prepaymentMarshallingList.get(prepaymentMarshallingList.size() - 1);
        assertThat(testPrepaymentMarshalling.getInactive()).isEqualTo(DEFAULT_INACTIVE);
        assertThat(testPrepaymentMarshalling.getAmortizationPeriods()).isEqualTo(UPDATED_AMORTIZATION_PERIODS);
        assertThat(testPrepaymentMarshalling.getProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testPrepaymentMarshalling.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentMarshallingWithPatch() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();

        // Update the prepaymentMarshalling using partial update
        PrepaymentMarshalling partialUpdatedPrepaymentMarshalling = new PrepaymentMarshalling();
        partialUpdatedPrepaymentMarshalling.setId(prepaymentMarshalling.getId());

        partialUpdatedPrepaymentMarshalling
            .inactive(UPDATED_INACTIVE)
            .amortizationPeriods(UPDATED_AMORTIZATION_PERIODS)
            .processed(UPDATED_PROCESSED)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restPrepaymentMarshallingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentMarshalling.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentMarshalling))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMarshalling testPrepaymentMarshalling = prepaymentMarshallingList.get(prepaymentMarshallingList.size() - 1);
        assertThat(testPrepaymentMarshalling.getInactive()).isEqualTo(UPDATED_INACTIVE);
        assertThat(testPrepaymentMarshalling.getAmortizationPeriods()).isEqualTo(UPDATED_AMORTIZATION_PERIODS);
        assertThat(testPrepaymentMarshalling.getProcessed()).isEqualTo(UPDATED_PROCESSED);
        assertThat(testPrepaymentMarshalling.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentMarshallingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentMarshalling() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMarshallingRepository.findAll().size();
        prepaymentMarshalling.setId(count.incrementAndGet());

        // Create the PrepaymentMarshalling
        PrepaymentMarshallingDTO prepaymentMarshallingDTO = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMarshallingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMarshallingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentMarshalling in the database
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(0)).save(prepaymentMarshalling);
    }

    @Test
    @Transactional
    void deletePrepaymentMarshalling() throws Exception {
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);

        int databaseSizeBeforeDelete = prepaymentMarshallingRepository.findAll().size();

        // Delete the prepaymentMarshalling
        restPrepaymentMarshallingMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentMarshalling.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentMarshalling> prepaymentMarshallingList = prepaymentMarshallingRepository.findAll();
        assertThat(prepaymentMarshallingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentMarshalling in Elasticsearch
        verify(mockPrepaymentMarshallingSearchRepository, times(1)).deleteById(prepaymentMarshalling.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentMarshalling() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentMarshallingRepository.saveAndFlush(prepaymentMarshalling);
        when(mockPrepaymentMarshallingSearchRepository.search("id:" + prepaymentMarshalling.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentMarshalling), PageRequest.of(0, 1), 1));

        // Search the prepaymentMarshalling
        restPrepaymentMarshallingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentMarshalling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMarshalling.getId().intValue())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationPeriods").value(hasItem(DEFAULT_AMORTIZATION_PERIODS)))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN.toString())));
    }
}
