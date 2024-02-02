package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.erp.resources.prepayments.PrepaymentAmortizationResourceProd;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.mapper.PrepaymentAmortizationMapper;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PrepaymentAmortizationResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
public class PrepaymentAmortizationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PREPAYMENT_PERIOD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREPAYMENT_PERIOD = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PREPAYMENT_PERIOD = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREPAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREPAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    private static final String ENTITY_API_URL = "/api/prepayments/prepayment-amortizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/prepayment-amortizations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentAmortizationRepository prepaymentAmortizationRepository;

    @Mock
    private PrepaymentAmortizationRepository prepaymentAmortizationRepositoryMock;

    @Autowired
    private PrepaymentAmortizationMapper prepaymentAmortizationMapper;

    @Mock
    private PrepaymentAmortizationService prepaymentAmortizationServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentAmortizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentAmortizationSearchRepository mockPrepaymentAmortizationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentAmortizationMockMvc;

    private PrepaymentAmortization prepaymentAmortization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAmortization createEntity(EntityManager em) {
        PrepaymentAmortization prepaymentAmortization = new PrepaymentAmortization()
            .description(DEFAULT_DESCRIPTION)
            .prepaymentPeriod(DEFAULT_PREPAYMENT_PERIOD)
            .prepaymentAmount(DEFAULT_PREPAYMENT_AMOUNT)
            .inactive(DEFAULT_INACTIVE);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        prepaymentAmortization.setFiscalMonth(fiscalMonth);
        // Add required entity
        PrepaymentCompilationRequest prepaymentCompilationRequest;
        if (TestUtil.findAll(em, PrepaymentCompilationRequest.class).isEmpty()) {
            prepaymentCompilationRequest = PrepaymentCompilationRequestResourceIT.createEntity(em);
            em.persist(prepaymentCompilationRequest);
            em.flush();
        } else {
            prepaymentCompilationRequest = TestUtil.findAll(em, PrepaymentCompilationRequest.class).get(0);
        }
        prepaymentAmortization.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
        return prepaymentAmortization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAmortization createUpdatedEntity(EntityManager em) {
        PrepaymentAmortization prepaymentAmortization = new PrepaymentAmortization()
            .description(UPDATED_DESCRIPTION)
            .prepaymentPeriod(UPDATED_PREPAYMENT_PERIOD)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .inactive(UPDATED_INACTIVE);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        prepaymentAmortization.setFiscalMonth(fiscalMonth);
        // Add required entity
        PrepaymentCompilationRequest prepaymentCompilationRequest;
        if (TestUtil.findAll(em, PrepaymentCompilationRequest.class).isEmpty()) {
            prepaymentCompilationRequest = PrepaymentCompilationRequestResourceIT.createUpdatedEntity(em);
            em.persist(prepaymentCompilationRequest);
            em.flush();
        } else {
            prepaymentCompilationRequest = TestUtil.findAll(em, PrepaymentCompilationRequest.class).get(0);
        }
        prepaymentAmortization.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
        return prepaymentAmortization;
    }

    @BeforeEach
    public void initTest() {
        prepaymentAmortization = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeCreate = prepaymentAmortizationRepository.findAll().size();
        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);
        restPrepaymentAmortizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentAmortization testPrepaymentAmortization = prepaymentAmortizationList.get(prepaymentAmortizationList.size() - 1);
        assertThat(testPrepaymentAmortization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrepaymentAmortization.getPrepaymentPeriod()).isEqualTo(DEFAULT_PREPAYMENT_PERIOD);
        assertThat(testPrepaymentAmortization.getPrepaymentAmount()).isEqualByComparingTo(DEFAULT_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAmortization.getInactive()).isEqualTo(DEFAULT_INACTIVE);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(1)).save(testPrepaymentAmortization);
    }

    @Test
    @Transactional
    void createPrepaymentAmortizationWithExistingId() throws Exception {
        // Create the PrepaymentAmortization with an existing ID
        prepaymentAmortization.setId(1L);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        int databaseSizeBeforeCreate = prepaymentAmortizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentAmortizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizations() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAmortization.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prepaymentPeriod").value(hasItem(DEFAULT_PREPAYMENT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentAmortizationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prepaymentAmortizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentAmortizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentAmortizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentAmortizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prepaymentAmortizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentAmortizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentAmortizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPrepaymentAmortization() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get the prepaymentAmortization
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentAmortization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentAmortization.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.prepaymentPeriod").value(DEFAULT_PREPAYMENT_PERIOD.toString()))
            .andExpect(jsonPath("$.prepaymentAmount").value(sameNumber(DEFAULT_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getPrepaymentAmortizationsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        Long id = prepaymentAmortization.getId();

        defaultPrepaymentAmortizationShouldBeFound("id.equals=" + id);
        defaultPrepaymentAmortizationShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentAmortizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentAmortizationShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentAmortizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentAmortizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description equals to DEFAULT_DESCRIPTION
        defaultPrepaymentAmortizationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the prepaymentAmortizationList where description equals to UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description not equals to DEFAULT_DESCRIPTION
        defaultPrepaymentAmortizationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the prepaymentAmortizationList where description not equals to UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the prepaymentAmortizationList where description equals to UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description is not null
        defaultPrepaymentAmortizationShouldBeFound("description.specified=true");

        // Get all the prepaymentAmortizationList where description is null
        defaultPrepaymentAmortizationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description contains DEFAULT_DESCRIPTION
        defaultPrepaymentAmortizationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the prepaymentAmortizationList where description contains UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where description does not contain DEFAULT_DESCRIPTION
        defaultPrepaymentAmortizationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the prepaymentAmortizationList where description does not contain UPDATED_DESCRIPTION
        defaultPrepaymentAmortizationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod equals to DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.equals=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod equals to UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.equals=" + UPDATED_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod not equals to DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.notEquals=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod not equals to UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.notEquals=" + UPDATED_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod in DEFAULT_PREPAYMENT_PERIOD or UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.in=" + DEFAULT_PREPAYMENT_PERIOD + "," + UPDATED_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod equals to UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.in=" + UPDATED_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is not null
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.specified=true");

        // Get all the prepaymentAmortizationList where prepaymentPeriod is null
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is greater than or equal to DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.greaterThanOrEqual=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is greater than or equal to UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.greaterThanOrEqual=" + UPDATED_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is less than or equal to DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.lessThanOrEqual=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is less than or equal to SMALLER_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.lessThanOrEqual=" + SMALLER_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is less than DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.lessThan=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is less than UPDATED_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.lessThan=" + UPDATED_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentPeriodIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is greater than DEFAULT_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentPeriod.greaterThan=" + DEFAULT_PREPAYMENT_PERIOD);

        // Get all the prepaymentAmortizationList where prepaymentPeriod is greater than SMALLER_PREPAYMENT_PERIOD
        defaultPrepaymentAmortizationShouldBeFound("prepaymentPeriod.greaterThan=" + SMALLER_PREPAYMENT_PERIOD);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.equals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.equals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount not equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.notEquals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount not equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.notEquals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount in DEFAULT_PREPAYMENT_AMOUNT or UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.in=" + DEFAULT_PREPAYMENT_AMOUNT + "," + UPDATED_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.in=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount is not null
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.specified=true");

        // Get all the prepaymentAmortizationList where prepaymentAmount is null
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount is greater than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.greaterThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount is greater than or equal to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.greaterThanOrEqual=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount is less than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.lessThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount is less than or equal to SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.lessThanOrEqual=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount is less than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.lessThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount is less than UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.lessThan=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where prepaymentAmount is greater than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAmount.greaterThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAmortizationList where prepaymentAmount is greater than SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAmount.greaterThan=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByInactiveIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where inactive equals to DEFAULT_INACTIVE
        defaultPrepaymentAmortizationShouldBeFound("inactive.equals=" + DEFAULT_INACTIVE);

        // Get all the prepaymentAmortizationList where inactive equals to UPDATED_INACTIVE
        defaultPrepaymentAmortizationShouldNotBeFound("inactive.equals=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByInactiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where inactive not equals to DEFAULT_INACTIVE
        defaultPrepaymentAmortizationShouldNotBeFound("inactive.notEquals=" + DEFAULT_INACTIVE);

        // Get all the prepaymentAmortizationList where inactive not equals to UPDATED_INACTIVE
        defaultPrepaymentAmortizationShouldBeFound("inactive.notEquals=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByInactiveIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where inactive in DEFAULT_INACTIVE or UPDATED_INACTIVE
        defaultPrepaymentAmortizationShouldBeFound("inactive.in=" + DEFAULT_INACTIVE + "," + UPDATED_INACTIVE);

        // Get all the prepaymentAmortizationList where inactive equals to UPDATED_INACTIVE
        defaultPrepaymentAmortizationShouldNotBeFound("inactive.in=" + UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByInactiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        // Get all the prepaymentAmortizationList where inactive is not null
        defaultPrepaymentAmortizationShouldBeFound("inactive.specified=true");

        // Get all the prepaymentAmortizationList where inactive is null
        defaultPrepaymentAmortizationShouldNotBeFound("inactive.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
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
        prepaymentAmortization.setPrepaymentAccount(prepaymentAccount);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long prepaymentAccountId = prepaymentAccount.getId();

        // Get all the prepaymentAmortizationList where prepaymentAccount equals to prepaymentAccountId
        defaultPrepaymentAmortizationShouldBeFound("prepaymentAccountId.equals=" + prepaymentAccountId);

        // Get all the prepaymentAmortizationList where prepaymentAccount equals to (prepaymentAccountId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentAccountId.equals=" + (prepaymentAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        prepaymentAmortization.setSettlementCurrency(settlementCurrency);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the prepaymentAmortizationList where settlementCurrency equals to settlementCurrencyId
        defaultPrepaymentAmortizationShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the prepaymentAmortizationList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByDebitAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        TransactionAccount debitAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            debitAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(debitAccount);
            em.flush();
        } else {
            debitAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(debitAccount);
        em.flush();
        prepaymentAmortization.setDebitAccount(debitAccount);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long debitAccountId = debitAccount.getId();

        // Get all the prepaymentAmortizationList where debitAccount equals to debitAccountId
        defaultPrepaymentAmortizationShouldBeFound("debitAccountId.equals=" + debitAccountId);

        // Get all the prepaymentAmortizationList where debitAccount equals to (debitAccountId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("debitAccountId.equals=" + (debitAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByCreditAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        TransactionAccount creditAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            creditAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(creditAccount);
            em.flush();
        } else {
            creditAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(creditAccount);
        em.flush();
        prepaymentAmortization.setCreditAccount(creditAccount);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long creditAccountId = creditAccount.getId();

        // Get all the prepaymentAmortizationList where creditAccount equals to creditAccountId
        defaultPrepaymentAmortizationShouldBeFound("creditAccountId.equals=" + creditAccountId);

        // Get all the prepaymentAmortizationList where creditAccount equals to (creditAccountId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("creditAccountId.equals=" + (creditAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
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
        prepaymentAmortization.addPlaceholder(placeholder);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long placeholderId = placeholder.getId();

        // Get all the prepaymentAmortizationList where placeholder equals to placeholderId
        defaultPrepaymentAmortizationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the prepaymentAmortizationList where placeholder equals to (placeholderId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByFiscalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
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
        prepaymentAmortization.setFiscalMonth(fiscalMonth);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long fiscalMonthId = fiscalMonth.getId();

        // Get all the prepaymentAmortizationList where fiscalMonth equals to fiscalMonthId
        defaultPrepaymentAmortizationShouldBeFound("fiscalMonthId.equals=" + fiscalMonthId);

        // Get all the prepaymentAmortizationList where fiscalMonth equals to (fiscalMonthId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("fiscalMonthId.equals=" + (fiscalMonthId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAmortizationsByPrepaymentCompilationRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        PrepaymentCompilationRequest prepaymentCompilationRequest;
        if (TestUtil.findAll(em, PrepaymentCompilationRequest.class).isEmpty()) {
            prepaymentCompilationRequest = PrepaymentCompilationRequestResourceIT.createEntity(em);
            em.persist(prepaymentCompilationRequest);
            em.flush();
        } else {
            prepaymentCompilationRequest = TestUtil.findAll(em, PrepaymentCompilationRequest.class).get(0);
        }
        em.persist(prepaymentCompilationRequest);
        em.flush();
        prepaymentAmortization.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        Long prepaymentCompilationRequestId = prepaymentCompilationRequest.getId();

        // Get all the prepaymentAmortizationList where prepaymentCompilationRequest equals to prepaymentCompilationRequestId
        defaultPrepaymentAmortizationShouldBeFound("prepaymentCompilationRequestId.equals=" + prepaymentCompilationRequestId);

        // Get all the prepaymentAmortizationList where prepaymentCompilationRequest equals to (prepaymentCompilationRequestId + 1)
        defaultPrepaymentAmortizationShouldNotBeFound("prepaymentCompilationRequestId.equals=" + (prepaymentCompilationRequestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentAmortizationShouldBeFound(String filter) throws Exception {
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAmortization.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prepaymentPeriod").value(hasItem(DEFAULT_PREPAYMENT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentAmortizationShouldNotBeFound(String filter) throws Exception {
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentAmortization() throws Exception {
        // Get the prepaymentAmortization
        restPrepaymentAmortizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentAmortization() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();

        // Update the prepaymentAmortization
        PrepaymentAmortization updatedPrepaymentAmortization = prepaymentAmortizationRepository
            .findById(prepaymentAmortization.getId())
            .get();
        // Disconnect from session so that the updates on updatedPrepaymentAmortization are not directly saved in db
        em.detach(updatedPrepaymentAmortization);
        updatedPrepaymentAmortization
            .description(UPDATED_DESCRIPTION)
            .prepaymentPeriod(UPDATED_PREPAYMENT_PERIOD)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .inactive(UPDATED_INACTIVE);
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(updatedPrepaymentAmortization);

        restPrepaymentAmortizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAmortizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAmortization testPrepaymentAmortization = prepaymentAmortizationList.get(prepaymentAmortizationList.size() - 1);
        assertThat(testPrepaymentAmortization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrepaymentAmortization.getPrepaymentPeriod()).isEqualTo(UPDATED_PREPAYMENT_PERIOD);
        assertThat(testPrepaymentAmortization.getPrepaymentAmount()).isEqualTo(UPDATED_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAmortization.getInactive()).isEqualTo(UPDATED_INACTIVE);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository).save(testPrepaymentAmortization);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAmortizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentAmortizationWithPatch() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();

        // Update the prepaymentAmortization using partial update
        PrepaymentAmortization partialUpdatedPrepaymentAmortization = new PrepaymentAmortization();
        partialUpdatedPrepaymentAmortization.setId(prepaymentAmortization.getId());

        restPrepaymentAmortizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAmortization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAmortization))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAmortization testPrepaymentAmortization = prepaymentAmortizationList.get(prepaymentAmortizationList.size() - 1);
        assertThat(testPrepaymentAmortization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrepaymentAmortization.getPrepaymentPeriod()).isEqualTo(DEFAULT_PREPAYMENT_PERIOD);
        assertThat(testPrepaymentAmortization.getPrepaymentAmount()).isEqualByComparingTo(DEFAULT_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAmortization.getInactive()).isEqualTo(DEFAULT_INACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentAmortizationWithPatch() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();

        // Update the prepaymentAmortization using partial update
        PrepaymentAmortization partialUpdatedPrepaymentAmortization = new PrepaymentAmortization();
        partialUpdatedPrepaymentAmortization.setId(prepaymentAmortization.getId());

        partialUpdatedPrepaymentAmortization
            .description(UPDATED_DESCRIPTION)
            .prepaymentPeriod(UPDATED_PREPAYMENT_PERIOD)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .inactive(UPDATED_INACTIVE);

        restPrepaymentAmortizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAmortization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAmortization))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAmortization testPrepaymentAmortization = prepaymentAmortizationList.get(prepaymentAmortizationList.size() - 1);
        assertThat(testPrepaymentAmortization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrepaymentAmortization.getPrepaymentPeriod()).isEqualTo(UPDATED_PREPAYMENT_PERIOD);
        assertThat(testPrepaymentAmortization.getPrepaymentAmount()).isEqualByComparingTo(UPDATED_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAmortization.getInactive()).isEqualTo(UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentAmortizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentAmortization() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAmortizationRepository.findAll().size();
        prepaymentAmortization.setId(count.incrementAndGet());

        // Create the PrepaymentAmortization
        PrepaymentAmortizationDTO prepaymentAmortizationDTO = prepaymentAmortizationMapper.toDto(prepaymentAmortization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAmortizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAmortizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAmortization in the database
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(0)).save(prepaymentAmortization);
    }

    @Test
    @Transactional
    void deletePrepaymentAmortization() throws Exception {
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);

        int databaseSizeBeforeDelete = prepaymentAmortizationRepository.findAll().size();

        // Delete the prepaymentAmortization
        restPrepaymentAmortizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentAmortization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentAmortization> prepaymentAmortizationList = prepaymentAmortizationRepository.findAll();
        assertThat(prepaymentAmortizationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentAmortization in Elasticsearch
        verify(mockPrepaymentAmortizationSearchRepository, times(1)).deleteById(prepaymentAmortization.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentAmortization() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentAmortizationRepository.saveAndFlush(prepaymentAmortization);
        when(mockPrepaymentAmortizationSearchRepository.search("id:" + prepaymentAmortization.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentAmortization), PageRequest.of(0, 1), 1));

        // Search the prepaymentAmortization
        restPrepaymentAmortizationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentAmortization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAmortization.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prepaymentPeriod").value(hasItem(DEFAULT_PREPAYMENT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }
}
