package io.github.erp.erp.resources;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.domain.Settlement;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.repository.RouInitialDirectCostRepository;
import io.github.erp.repository.search.RouInitialDirectCostSearchRepository;
import io.github.erp.service.RouInitialDirectCostService;
import io.github.erp.service.dto.RouInitialDirectCostDTO;
import io.github.erp.service.mapper.RouInitialDirectCostMapper;
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
 * Integration tests for the RouInitialDirectCostResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class RouInitialDirectCostResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST = new BigDecimal(1);
    private static final BigDecimal SMALLER_COST = new BigDecimal(0 - 1);

    private static final Long DEFAULT_REFERENCE_NUMBER = 1L;
    private static final Long UPDATED_REFERENCE_NUMBER = 2L;
    private static final Long SMALLER_REFERENCE_NUMBER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/leases/rou-initial-direct-costs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/rou-initial-direct-costs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouInitialDirectCostRepository rouInitialDirectCostRepository;

    @Mock
    private RouInitialDirectCostRepository rouInitialDirectCostRepositoryMock;

    @Autowired
    private RouInitialDirectCostMapper rouInitialDirectCostMapper;

    @Mock
    private RouInitialDirectCostService rouInitialDirectCostServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouInitialDirectCostSearchRepositoryMockConfiguration
     */
    @Autowired
    private RouInitialDirectCostSearchRepository mockRouInitialDirectCostSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouInitialDirectCostMockMvc;

    private RouInitialDirectCost rouInitialDirectCost;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouInitialDirectCost createEntity(EntityManager em) {
        RouInitialDirectCost rouInitialDirectCost = new RouInitialDirectCost()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouInitialDirectCost.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        rouInitialDirectCost.setSettlementDetails(settlement);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouInitialDirectCost.setTargetROUAccount(transactionAccount);
        // Add required entity
        rouInitialDirectCost.setTransferAccount(transactionAccount);
        return rouInitialDirectCost;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouInitialDirectCost createUpdatedEntity(EntityManager em) {
        RouInitialDirectCost rouInitialDirectCost = new RouInitialDirectCost()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .referenceNumber(UPDATED_REFERENCE_NUMBER);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        rouInitialDirectCost.setLeaseContract(iFRS16LeaseContract);
        // Add required entity
        Settlement settlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlement = SettlementResourceIT.createUpdatedEntity(em);
            em.persist(settlement);
            em.flush();
        } else {
            settlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        rouInitialDirectCost.setSettlementDetails(settlement);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        rouInitialDirectCost.setTargetROUAccount(transactionAccount);
        // Add required entity
        rouInitialDirectCost.setTransferAccount(transactionAccount);
        return rouInitialDirectCost;
    }

    @BeforeEach
    public void initTest() {
        rouInitialDirectCost = createEntity(em);
    }

    @Test
    @Transactional
    void createRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeCreate = rouInitialDirectCostRepository.findAll().size();
        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);
        restRouInitialDirectCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeCreate + 1);
        RouInitialDirectCost testRouInitialDirectCost = rouInitialDirectCostList.get(rouInitialDirectCostList.size() - 1);
        assertThat(testRouInitialDirectCost.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testRouInitialDirectCost.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouInitialDirectCost.getCost()).isEqualByComparingTo(DEFAULT_COST);
        assertThat(testRouInitialDirectCost.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(1)).save(testRouInitialDirectCost);
    }

    @Test
    @Transactional
    void createRouInitialDirectCostWithExistingId() throws Exception {
        // Create the RouInitialDirectCost with an existing ID
        rouInitialDirectCost.setId(1L);
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        int databaseSizeBeforeCreate = rouInitialDirectCostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouInitialDirectCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouInitialDirectCostRepository.findAll().size();
        // set the field null
        rouInitialDirectCost.setTransactionDate(null);

        // Create the RouInitialDirectCost, which fails.
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        restRouInitialDirectCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouInitialDirectCostRepository.findAll().size();
        // set the field null
        rouInitialDirectCost.setCost(null);

        // Create the RouInitialDirectCost, which fails.
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        restRouInitialDirectCostMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCosts() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouInitialDirectCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouInitialDirectCostsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rouInitialDirectCostServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restRouInitialDirectCostMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rouInitialDirectCostServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouInitialDirectCostsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rouInitialDirectCostServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restRouInitialDirectCostMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rouInitialDirectCostServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRouInitialDirectCost() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get the rouInitialDirectCost
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL_ID, rouInitialDirectCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouInitialDirectCost.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getRouInitialDirectCostsByIdFiltering() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        Long id = rouInitialDirectCost.getId();

        defaultRouInitialDirectCostShouldBeFound("id.equals=" + id);
        defaultRouInitialDirectCostShouldNotBeFound("id.notEquals=" + id);

        defaultRouInitialDirectCostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouInitialDirectCostShouldNotBeFound("id.greaterThan=" + id);

        defaultRouInitialDirectCostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouInitialDirectCostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate is not null
        defaultRouInitialDirectCostShouldBeFound("transactionDate.specified=true");

        // Get all the rouInitialDirectCostList where transactionDate is null
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the rouInitialDirectCostList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultRouInitialDirectCostShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description equals to DEFAULT_DESCRIPTION
        defaultRouInitialDirectCostShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouInitialDirectCostList where description equals to UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description not equals to DEFAULT_DESCRIPTION
        defaultRouInitialDirectCostShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouInitialDirectCostList where description not equals to UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouInitialDirectCostList where description equals to UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description is not null
        defaultRouInitialDirectCostShouldBeFound("description.specified=true");

        // Get all the rouInitialDirectCostList where description is null
        defaultRouInitialDirectCostShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description contains DEFAULT_DESCRIPTION
        defaultRouInitialDirectCostShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouInitialDirectCostList where description contains UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where description does not contain DEFAULT_DESCRIPTION
        defaultRouInitialDirectCostShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouInitialDirectCostList where description does not contain UPDATED_DESCRIPTION
        defaultRouInitialDirectCostShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost equals to DEFAULT_COST
        defaultRouInitialDirectCostShouldBeFound("cost.equals=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost equals to UPDATED_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost not equals to DEFAULT_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.notEquals=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost not equals to UPDATED_COST
        defaultRouInitialDirectCostShouldBeFound("cost.notEquals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsInShouldWork() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost in DEFAULT_COST or UPDATED_COST
        defaultRouInitialDirectCostShouldBeFound("cost.in=" + DEFAULT_COST + "," + UPDATED_COST);

        // Get all the rouInitialDirectCostList where cost equals to UPDATED_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost is not null
        defaultRouInitialDirectCostShouldBeFound("cost.specified=true");

        // Get all the rouInitialDirectCostList where cost is null
        defaultRouInitialDirectCostShouldNotBeFound("cost.specified=false");
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost is greater than or equal to DEFAULT_COST
        defaultRouInitialDirectCostShouldBeFound("cost.greaterThanOrEqual=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost is greater than or equal to UPDATED_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost is less than or equal to DEFAULT_COST
        defaultRouInitialDirectCostShouldBeFound("cost.lessThanOrEqual=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost is less than or equal to SMALLER_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost is less than DEFAULT_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.lessThan=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost is less than UPDATED_COST
        defaultRouInitialDirectCostShouldBeFound("cost.lessThan=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where cost is greater than DEFAULT_COST
        defaultRouInitialDirectCostShouldNotBeFound("cost.greaterThan=" + DEFAULT_COST);

        // Get all the rouInitialDirectCostList where cost is greater than SMALLER_COST
        defaultRouInitialDirectCostShouldBeFound("cost.greaterThan=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber equals to DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber not equals to DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.notEquals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber not equals to UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.notEquals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber in DEFAULT_REFERENCE_NUMBER or UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.in=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber is not null
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.specified=true");

        // Get all the rouInitialDirectCostList where referenceNumber is null
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber is greater than or equal to DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.greaterThanOrEqual=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber is greater than or equal to UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.greaterThanOrEqual=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber is less than or equal to DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.lessThanOrEqual=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber is less than or equal to SMALLER_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.lessThanOrEqual=" + SMALLER_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber is less than DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.lessThan=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber is less than UPDATED_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.lessThan=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByReferenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        // Get all the rouInitialDirectCostList where referenceNumber is greater than DEFAULT_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldNotBeFound("referenceNumber.greaterThan=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the rouInitialDirectCostList where referenceNumber is greater than SMALLER_REFERENCE_NUMBER
        defaultRouInitialDirectCostShouldBeFound("referenceNumber.greaterThan=" + SMALLER_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByLeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        IFRS16LeaseContract leaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            leaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        em.persist(leaseContract);
        em.flush();
        rouInitialDirectCost.setLeaseContract(leaseContract);
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Long leaseContractId = leaseContract.getId();

        // Get all the rouInitialDirectCostList where leaseContract equals to leaseContractId
        defaultRouInitialDirectCostShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the rouInitialDirectCostList where leaseContract equals to (leaseContractId + 1)
        defaultRouInitialDirectCostShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsBySettlementDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Settlement settlementDetails;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlementDetails = SettlementResourceIT.createEntity(em);
            em.persist(settlementDetails);
            em.flush();
        } else {
            settlementDetails = TestUtil.findAll(em, Settlement.class).get(0);
        }
        em.persist(settlementDetails);
        em.flush();
        rouInitialDirectCost.setSettlementDetails(settlementDetails);
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Long settlementDetailsId = settlementDetails.getId();

        // Get all the rouInitialDirectCostList where settlementDetails equals to settlementDetailsId
        defaultRouInitialDirectCostShouldBeFound("settlementDetailsId.equals=" + settlementDetailsId);

        // Get all the rouInitialDirectCostList where settlementDetails equals to (settlementDetailsId + 1)
        defaultRouInitialDirectCostShouldNotBeFound("settlementDetailsId.equals=" + (settlementDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTargetROUAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        TransactionAccount targetROUAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            targetROUAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(targetROUAccount);
            em.flush();
        } else {
            targetROUAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(targetROUAccount);
        em.flush();
        rouInitialDirectCost.setTargetROUAccount(targetROUAccount);
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Long targetROUAccountId = targetROUAccount.getId();

        // Get all the rouInitialDirectCostList where targetROUAccount equals to targetROUAccountId
        defaultRouInitialDirectCostShouldBeFound("targetROUAccountId.equals=" + targetROUAccountId);

        // Get all the rouInitialDirectCostList where targetROUAccount equals to (targetROUAccountId + 1)
        defaultRouInitialDirectCostShouldNotBeFound("targetROUAccountId.equals=" + (targetROUAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByTransferAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        TransactionAccount transferAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transferAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transferAccount);
            em.flush();
        } else {
            transferAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(transferAccount);
        em.flush();
        rouInitialDirectCost.setTransferAccount(transferAccount);
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Long transferAccountId = transferAccount.getId();

        // Get all the rouInitialDirectCostList where transferAccount equals to transferAccountId
        defaultRouInitialDirectCostShouldBeFound("transferAccountId.equals=" + transferAccountId);

        // Get all the rouInitialDirectCostList where transferAccount equals to (transferAccountId + 1)
        defaultRouInitialDirectCostShouldNotBeFound("transferAccountId.equals=" + (transferAccountId + 1));
    }

    @Test
    @Transactional
    void getAllRouInitialDirectCostsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
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
        rouInitialDirectCost.addPlaceholder(placeholder);
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        Long placeholderId = placeholder.getId();

        // Get all the rouInitialDirectCostList where placeholder equals to placeholderId
        defaultRouInitialDirectCostShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the rouInitialDirectCostList where placeholder equals to (placeholderId + 1)
        defaultRouInitialDirectCostShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouInitialDirectCostShouldBeFound(String filter) throws Exception {
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouInitialDirectCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.intValue())));

        // Check, that the count call also returns 1
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouInitialDirectCostShouldNotBeFound(String filter) throws Exception {
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouInitialDirectCost() throws Exception {
        // Get the rouInitialDirectCost
        restRouInitialDirectCostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    /*@Test
    @Transactional*/
    /*void putNewRouInitialDirectCost() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();

        if (rouInitialDirectCostRepository.findById(rouInitialDirectCost.getId()).isPresent()) {
            // Update the rouInitialDirectCost
            RouInitialDirectCost updatedRouInitialDirectCost = rouInitialDirectCostRepository.findById(rouInitialDirectCost.getId()).get();
            // Disconnect from session so that the updates on updatedRouInitialDirectCost are not directly saved in db
            em.detach(updatedRouInitialDirectCost);
            updatedRouInitialDirectCost
                .transactionDate(UPDATED_TRANSACTION_DATE)
                .description(UPDATED_DESCRIPTION)
                .cost(UPDATED_COST)
                .referenceNumber(UPDATED_REFERENCE_NUMBER);
            RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(updatedRouInitialDirectCost);

            restRouInitialDirectCostMockMvc
                .perform(
                    put(ENTITY_API_URL_ID, rouInitialDirectCostDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
                )
                .andExpect(status().isOk());

            // Validate the RouInitialDirectCost in the database
            List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
            assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);
            RouInitialDirectCost testRouInitialDirectCost = rouInitialDirectCostList.get(rouInitialDirectCostList.size() - 1);
            assertThat(testRouInitialDirectCost.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
            assertThat(testRouInitialDirectCost.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
            assertThat(testRouInitialDirectCost.getCost()).isEqualTo(UPDATED_COST);
            assertThat(testRouInitialDirectCost.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);

            // Validate the RouInitialDirectCost in Elasticsearch
            verify(mockRouInitialDirectCostSearchRepository).save(testRouInitialDirectCost);
        }
    }*/

    @Test
    @Transactional
    void putNonExistingRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouInitialDirectCostDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void partialUpdateRouInitialDirectCostWithPatch() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();

        // Update the rouInitialDirectCost using partial update
        RouInitialDirectCost partialUpdatedRouInitialDirectCost = new RouInitialDirectCost();
        partialUpdatedRouInitialDirectCost.setId(rouInitialDirectCost.getId());

        partialUpdatedRouInitialDirectCost.cost(UPDATED_COST);

        restRouInitialDirectCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouInitialDirectCost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouInitialDirectCost))
            )
            .andExpect(status().isOk());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);
        RouInitialDirectCost testRouInitialDirectCost = rouInitialDirectCostList.get(rouInitialDirectCostList.size() - 1);
        assertThat(testRouInitialDirectCost.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testRouInitialDirectCost.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouInitialDirectCost.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testRouInitialDirectCost.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateRouInitialDirectCostWithPatch() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();

        // Update the rouInitialDirectCost using partial update
        RouInitialDirectCost partialUpdatedRouInitialDirectCost = new RouInitialDirectCost();
        partialUpdatedRouInitialDirectCost.setId(rouInitialDirectCost.getId());

        partialUpdatedRouInitialDirectCost
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .referenceNumber(UPDATED_REFERENCE_NUMBER);

        restRouInitialDirectCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouInitialDirectCost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouInitialDirectCost))
            )
            .andExpect(status().isOk());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);
        RouInitialDirectCost testRouInitialDirectCost = rouInitialDirectCostList.get(rouInitialDirectCostList.size() - 1);
        assertThat(testRouInitialDirectCost.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testRouInitialDirectCost.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouInitialDirectCost.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testRouInitialDirectCost.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouInitialDirectCostDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouInitialDirectCost() throws Exception {
        int databaseSizeBeforeUpdate = rouInitialDirectCostRepository.findAll().size();
        rouInitialDirectCost.setId(count.incrementAndGet());

        // Create the RouInitialDirectCost
        RouInitialDirectCostDTO rouInitialDirectCostDTO = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouInitialDirectCostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouInitialDirectCostDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouInitialDirectCost in the database
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(0)).save(rouInitialDirectCost);
    }

    @Test
    @Transactional
    void deleteRouInitialDirectCost() throws Exception {
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);

        int databaseSizeBeforeDelete = rouInitialDirectCostRepository.findAll().size();

        // Delete the rouInitialDirectCost
        restRouInitialDirectCostMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouInitialDirectCost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouInitialDirectCost> rouInitialDirectCostList = rouInitialDirectCostRepository.findAll();
        assertThat(rouInitialDirectCostList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouInitialDirectCost in Elasticsearch
        verify(mockRouInitialDirectCostSearchRepository, times(1)).deleteById(rouInitialDirectCost.getId());
    }

    @Test
    @Transactional
    void searchRouInitialDirectCost() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouInitialDirectCostRepository.saveAndFlush(rouInitialDirectCost);
        when(mockRouInitialDirectCostSearchRepository.search("id:" + rouInitialDirectCost.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouInitialDirectCost), PageRequest.of(0, 1), 1));

        // Search the rouInitialDirectCost
        restRouInitialDirectCostMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouInitialDirectCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouInitialDirectCost.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.intValue())));
    }
}
