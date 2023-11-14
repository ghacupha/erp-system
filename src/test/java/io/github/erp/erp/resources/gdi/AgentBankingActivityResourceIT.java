package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.AgentBankingActivity;
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.BankTransactionType;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.repository.AgentBankingActivityRepository;
import io.github.erp.repository.search.AgentBankingActivitySearchRepository;
import io.github.erp.service.dto.AgentBankingActivityDTO;
import io.github.erp.service.mapper.AgentBankingActivityMapper;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the AgentBankingActivityResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class AgentBankingActivityResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_AGENT_UNIQUE_ID = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_UNIQUE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TERMINAL_UNIQUE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_UNIQUE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS = 0;
    private static final Integer UPDATED_TOTAL_COUNT_OF_TRANSACTIONS = 1;
    private static final Integer SMALLER_TOTAL_COUNT_OF_TRANSACTIONS = 0 - 1;

    private static final BigDecimal DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/granular-data/agent-banking-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/agent-banking-activities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentBankingActivityRepository agentBankingActivityRepository;

    @Autowired
    private AgentBankingActivityMapper agentBankingActivityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AgentBankingActivitySearchRepositoryMockConfiguration
     */
    @Autowired
    private AgentBankingActivitySearchRepository mockAgentBankingActivitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgentBankingActivityMockMvc;

    private AgentBankingActivity agentBankingActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentBankingActivity createEntity(EntityManager em) {
        AgentBankingActivity agentBankingActivity = new AgentBankingActivity()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .agentUniqueId(DEFAULT_AGENT_UNIQUE_ID)
            .terminalUniqueId(DEFAULT_TERMINAL_UNIQUE_ID)
            .totalCountOfTransactions(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS)
            .totalValueOfTransactionsInLCY(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        agentBankingActivity.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        agentBankingActivity.setBranchCode(bankBranchCode);
        // Add required entity
        BankTransactionType bankTransactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            bankTransactionType = BankTransactionTypeResourceIT.createEntity(em);
            em.persist(bankTransactionType);
            em.flush();
        } else {
            bankTransactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        agentBankingActivity.setTransactionType(bankTransactionType);
        return agentBankingActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentBankingActivity createUpdatedEntity(EntityManager em) {
        AgentBankingActivity agentBankingActivity = new AgentBankingActivity()
            .reportingDate(UPDATED_REPORTING_DATE)
            .agentUniqueId(UPDATED_AGENT_UNIQUE_ID)
            .terminalUniqueId(UPDATED_TERMINAL_UNIQUE_ID)
            .totalCountOfTransactions(UPDATED_TOTAL_COUNT_OF_TRANSACTIONS)
            .totalValueOfTransactionsInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        agentBankingActivity.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        agentBankingActivity.setBranchCode(bankBranchCode);
        // Add required entity
        BankTransactionType bankTransactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            bankTransactionType = BankTransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(bankTransactionType);
            em.flush();
        } else {
            bankTransactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        agentBankingActivity.setTransactionType(bankTransactionType);
        return agentBankingActivity;
    }

    @BeforeEach
    public void initTest() {
        agentBankingActivity = createEntity(em);
    }

    @Test
    @Transactional
    void createAgentBankingActivity() throws Exception {
        int databaseSizeBeforeCreate = agentBankingActivityRepository.findAll().size();
        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);
        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeCreate + 1);
        AgentBankingActivity testAgentBankingActivity = agentBankingActivityList.get(agentBankingActivityList.size() - 1);
        assertThat(testAgentBankingActivity.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testAgentBankingActivity.getAgentUniqueId()).isEqualTo(DEFAULT_AGENT_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTerminalUniqueId()).isEqualTo(DEFAULT_TERMINAL_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTotalCountOfTransactions()).isEqualTo(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);
        assertThat(testAgentBankingActivity.getTotalValueOfTransactionsInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(1)).save(testAgentBankingActivity);
    }

    @Test
    @Transactional
    void createAgentBankingActivityWithExistingId() throws Exception {
        // Create the AgentBankingActivity with an existing ID
        agentBankingActivity.setId(1L);
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        int databaseSizeBeforeCreate = agentBankingActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeCreate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentBankingActivityRepository.findAll().size();
        // set the field null
        agentBankingActivity.setReportingDate(null);

        // Create the AgentBankingActivity, which fails.
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgentUniqueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentBankingActivityRepository.findAll().size();
        // set the field null
        agentBankingActivity.setAgentUniqueId(null);

        // Create the AgentBankingActivity, which fails.
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalUniqueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentBankingActivityRepository.findAll().size();
        // set the field null
        agentBankingActivity.setTerminalUniqueId(null);

        // Create the AgentBankingActivity, which fails.
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCountOfTransactionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentBankingActivityRepository.findAll().size();
        // set the field null
        agentBankingActivity.setTotalCountOfTransactions(null);

        // Create the AgentBankingActivity, which fails.
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalValueOfTransactionsInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentBankingActivityRepository.findAll().size();
        // set the field null
        agentBankingActivity.setTotalValueOfTransactionsInLCY(null);

        // Create the AgentBankingActivity, which fails.
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivities() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentBankingActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].agentUniqueId").value(hasItem(DEFAULT_AGENT_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].terminalUniqueId").value(hasItem(DEFAULT_TERMINAL_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].totalCountOfTransactions").value(hasItem(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY)))
            );
    }

    @Test
    @Transactional
    void getAgentBankingActivity() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get the agentBankingActivity
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, agentBankingActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agentBankingActivity.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.agentUniqueId").value(DEFAULT_AGENT_UNIQUE_ID))
            .andExpect(jsonPath("$.terminalUniqueId").value(DEFAULT_TERMINAL_UNIQUE_ID))
            .andExpect(jsonPath("$.totalCountOfTransactions").value(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS))
            .andExpect(jsonPath("$.totalValueOfTransactionsInLCY").value(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY)));
    }

    @Test
    @Transactional
    void getAgentBankingActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        Long id = agentBankingActivity.getId();

        defaultAgentBankingActivityShouldBeFound("id.equals=" + id);
        defaultAgentBankingActivityShouldNotBeFound("id.notEquals=" + id);

        defaultAgentBankingActivityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgentBankingActivityShouldNotBeFound("id.greaterThan=" + id);

        defaultAgentBankingActivityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgentBankingActivityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate is not null
        defaultAgentBankingActivityShouldBeFound("reportingDate.specified=true");

        // Get all the agentBankingActivityList where reportingDate is null
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultAgentBankingActivityShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the agentBankingActivityList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultAgentBankingActivityShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId equals to DEFAULT_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.equals=" + DEFAULT_AGENT_UNIQUE_ID);

        // Get all the agentBankingActivityList where agentUniqueId equals to UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.equals=" + UPDATED_AGENT_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId not equals to DEFAULT_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.notEquals=" + DEFAULT_AGENT_UNIQUE_ID);

        // Get all the agentBankingActivityList where agentUniqueId not equals to UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.notEquals=" + UPDATED_AGENT_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdIsInShouldWork() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId in DEFAULT_AGENT_UNIQUE_ID or UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.in=" + DEFAULT_AGENT_UNIQUE_ID + "," + UPDATED_AGENT_UNIQUE_ID);

        // Get all the agentBankingActivityList where agentUniqueId equals to UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.in=" + UPDATED_AGENT_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId is not null
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.specified=true");

        // Get all the agentBankingActivityList where agentUniqueId is null
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdContainsSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId contains DEFAULT_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.contains=" + DEFAULT_AGENT_UNIQUE_ID);

        // Get all the agentBankingActivityList where agentUniqueId contains UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.contains=" + UPDATED_AGENT_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByAgentUniqueIdNotContainsSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where agentUniqueId does not contain DEFAULT_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("agentUniqueId.doesNotContain=" + DEFAULT_AGENT_UNIQUE_ID);

        // Get all the agentBankingActivityList where agentUniqueId does not contain UPDATED_AGENT_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("agentUniqueId.doesNotContain=" + UPDATED_AGENT_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId equals to DEFAULT_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.equals=" + DEFAULT_TERMINAL_UNIQUE_ID);

        // Get all the agentBankingActivityList where terminalUniqueId equals to UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.equals=" + UPDATED_TERMINAL_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId not equals to DEFAULT_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.notEquals=" + DEFAULT_TERMINAL_UNIQUE_ID);

        // Get all the agentBankingActivityList where terminalUniqueId not equals to UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.notEquals=" + UPDATED_TERMINAL_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdIsInShouldWork() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId in DEFAULT_TERMINAL_UNIQUE_ID or UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.in=" + DEFAULT_TERMINAL_UNIQUE_ID + "," + UPDATED_TERMINAL_UNIQUE_ID);

        // Get all the agentBankingActivityList where terminalUniqueId equals to UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.in=" + UPDATED_TERMINAL_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId is not null
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.specified=true");

        // Get all the agentBankingActivityList where terminalUniqueId is null
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdContainsSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId contains DEFAULT_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.contains=" + DEFAULT_TERMINAL_UNIQUE_ID);

        // Get all the agentBankingActivityList where terminalUniqueId contains UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.contains=" + UPDATED_TERMINAL_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTerminalUniqueIdNotContainsSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where terminalUniqueId does not contain DEFAULT_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldNotBeFound("terminalUniqueId.doesNotContain=" + DEFAULT_TERMINAL_UNIQUE_ID);

        // Get all the agentBankingActivityList where terminalUniqueId does not contain UPDATED_TERMINAL_UNIQUE_ID
        defaultAgentBankingActivityShouldBeFound("terminalUniqueId.doesNotContain=" + UPDATED_TERMINAL_UNIQUE_ID);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions equals to DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.equals=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions equals to UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.equals=" + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions not equals to DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.notEquals=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions not equals to UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.notEquals=" + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsInShouldWork() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions in DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS or UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound(
            "totalCountOfTransactions.in=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS + "," + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        );

        // Get all the agentBankingActivityList where totalCountOfTransactions equals to UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.in=" + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions is not null
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.specified=true");

        // Get all the agentBankingActivityList where totalCountOfTransactions is null
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions is greater than or equal to DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.greaterThanOrEqual=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions is greater than or equal to UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.greaterThanOrEqual=" + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions is less than or equal to DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.lessThanOrEqual=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions is less than or equal to SMALLER_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.lessThanOrEqual=" + SMALLER_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsLessThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions is less than DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.lessThan=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions is less than UPDATED_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.lessThan=" + UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalCountOfTransactionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalCountOfTransactions is greater than DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldNotBeFound("totalCountOfTransactions.greaterThan=" + DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);

        // Get all the agentBankingActivityList where totalCountOfTransactions is greater than SMALLER_TOTAL_COUNT_OF_TRANSACTIONS
        defaultAgentBankingActivityShouldBeFound("totalCountOfTransactions.greaterThan=" + SMALLER_TOTAL_COUNT_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY equals to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound("totalValueOfTransactionsInLCY.equals=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound("totalValueOfTransactionsInLCY.equals=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY not equals to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound(
            "totalValueOfTransactionsInLCY.notEquals=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY not equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound("totalValueOfTransactionsInLCY.notEquals=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY in DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY or UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound(
            "totalValueOfTransactionsInLCY.in=" +
            DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY +
            "," +
            UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound("totalValueOfTransactionsInLCY.in=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is not null
        defaultAgentBankingActivityShouldBeFound("totalValueOfTransactionsInLCY.specified=true");

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is null
        defaultAgentBankingActivityShouldNotBeFound("totalValueOfTransactionsInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is greater than or equal to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound(
            "totalValueOfTransactionsInLCY.greaterThanOrEqual=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is greater than or equal to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound(
            "totalValueOfTransactionsInLCY.greaterThanOrEqual=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is less than or equal to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound(
            "totalValueOfTransactionsInLCY.lessThanOrEqual=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is less than or equal to SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound(
            "totalValueOfTransactionsInLCY.lessThanOrEqual=" + SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is less than DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound("totalValueOfTransactionsInLCY.lessThan=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is less than UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound("totalValueOfTransactionsInLCY.lessThan=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTotalValueOfTransactionsInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is greater than DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldNotBeFound(
            "totalValueOfTransactionsInLCY.greaterThan=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the agentBankingActivityList where totalValueOfTransactionsInLCY is greater than SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultAgentBankingActivityShouldBeFound("totalValueOfTransactionsInLCY.greaterThan=" + SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
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
        agentBankingActivity.setBankCode(bankCode);
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        Long bankCodeId = bankCode.getId();

        // Get all the agentBankingActivityList where bankCode equals to bankCodeId
        defaultAgentBankingActivityShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the agentBankingActivityList where bankCode equals to (bankCodeId + 1)
        defaultAgentBankingActivityShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        BankBranchCode branchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchCode);
            em.flush();
        } else {
            branchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchCode);
        em.flush();
        agentBankingActivity.setBranchCode(branchCode);
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        Long branchCodeId = branchCode.getId();

        // Get all the agentBankingActivityList where branchCode equals to branchCodeId
        defaultAgentBankingActivityShouldBeFound("branchCodeId.equals=" + branchCodeId);

        // Get all the agentBankingActivityList where branchCode equals to (branchCodeId + 1)
        defaultAgentBankingActivityShouldNotBeFound("branchCodeId.equals=" + (branchCodeId + 1));
    }

    @Test
    @Transactional
    void getAllAgentBankingActivitiesByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        BankTransactionType transactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            transactionType = BankTransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        agentBankingActivity.setTransactionType(transactionType);
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        Long transactionTypeId = transactionType.getId();

        // Get all the agentBankingActivityList where transactionType equals to transactionTypeId
        defaultAgentBankingActivityShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the agentBankingActivityList where transactionType equals to (transactionTypeId + 1)
        defaultAgentBankingActivityShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgentBankingActivityShouldBeFound(String filter) throws Exception {
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentBankingActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].agentUniqueId").value(hasItem(DEFAULT_AGENT_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].terminalUniqueId").value(hasItem(DEFAULT_TERMINAL_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].totalCountOfTransactions").value(hasItem(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY)))
            );

        // Check, that the count call also returns 1
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgentBankingActivityShouldNotBeFound(String filter) throws Exception {
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgentBankingActivity() throws Exception {
        // Get the agentBankingActivity
        restAgentBankingActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgentBankingActivity() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();

        // Update the agentBankingActivity
        AgentBankingActivity updatedAgentBankingActivity = agentBankingActivityRepository.findById(agentBankingActivity.getId()).get();
        // Disconnect from session so that the updates on updatedAgentBankingActivity are not directly saved in db
        em.detach(updatedAgentBankingActivity);
        updatedAgentBankingActivity
            .reportingDate(UPDATED_REPORTING_DATE)
            .agentUniqueId(UPDATED_AGENT_UNIQUE_ID)
            .terminalUniqueId(UPDATED_TERMINAL_UNIQUE_ID)
            .totalCountOfTransactions(UPDATED_TOTAL_COUNT_OF_TRANSACTIONS)
            .totalValueOfTransactionsInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(updatedAgentBankingActivity);

        restAgentBankingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentBankingActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);
        AgentBankingActivity testAgentBankingActivity = agentBankingActivityList.get(agentBankingActivityList.size() - 1);
        assertThat(testAgentBankingActivity.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAgentBankingActivity.getAgentUniqueId()).isEqualTo(UPDATED_AGENT_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTerminalUniqueId()).isEqualTo(UPDATED_TERMINAL_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTotalCountOfTransactions()).isEqualTo(UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
        assertThat(testAgentBankingActivity.getTotalValueOfTransactionsInLCY()).isEqualTo(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository).save(testAgentBankingActivity);
    }

    @Test
    @Transactional
    void putNonExistingAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentBankingActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void partialUpdateAgentBankingActivityWithPatch() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();

        // Update the agentBankingActivity using partial update
        AgentBankingActivity partialUpdatedAgentBankingActivity = new AgentBankingActivity();
        partialUpdatedAgentBankingActivity.setId(agentBankingActivity.getId());

        partialUpdatedAgentBankingActivity.reportingDate(UPDATED_REPORTING_DATE).terminalUniqueId(UPDATED_TERMINAL_UNIQUE_ID);

        restAgentBankingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentBankingActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentBankingActivity))
            )
            .andExpect(status().isOk());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);
        AgentBankingActivity testAgentBankingActivity = agentBankingActivityList.get(agentBankingActivityList.size() - 1);
        assertThat(testAgentBankingActivity.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAgentBankingActivity.getAgentUniqueId()).isEqualTo(DEFAULT_AGENT_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTerminalUniqueId()).isEqualTo(UPDATED_TERMINAL_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTotalCountOfTransactions()).isEqualTo(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS);
        assertThat(testAgentBankingActivity.getTotalValueOfTransactionsInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateAgentBankingActivityWithPatch() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();

        // Update the agentBankingActivity using partial update
        AgentBankingActivity partialUpdatedAgentBankingActivity = new AgentBankingActivity();
        partialUpdatedAgentBankingActivity.setId(agentBankingActivity.getId());

        partialUpdatedAgentBankingActivity
            .reportingDate(UPDATED_REPORTING_DATE)
            .agentUniqueId(UPDATED_AGENT_UNIQUE_ID)
            .terminalUniqueId(UPDATED_TERMINAL_UNIQUE_ID)
            .totalCountOfTransactions(UPDATED_TOTAL_COUNT_OF_TRANSACTIONS)
            .totalValueOfTransactionsInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);

        restAgentBankingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgentBankingActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgentBankingActivity))
            )
            .andExpect(status().isOk());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);
        AgentBankingActivity testAgentBankingActivity = agentBankingActivityList.get(agentBankingActivityList.size() - 1);
        assertThat(testAgentBankingActivity.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testAgentBankingActivity.getAgentUniqueId()).isEqualTo(UPDATED_AGENT_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTerminalUniqueId()).isEqualTo(UPDATED_TERMINAL_UNIQUE_ID);
        assertThat(testAgentBankingActivity.getTotalCountOfTransactions()).isEqualTo(UPDATED_TOTAL_COUNT_OF_TRANSACTIONS);
        assertThat(testAgentBankingActivity.getTotalValueOfTransactionsInLCY())
            .isEqualByComparingTo(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agentBankingActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgentBankingActivity() throws Exception {
        int databaseSizeBeforeUpdate = agentBankingActivityRepository.findAll().size();
        agentBankingActivity.setId(count.incrementAndGet());

        // Create the AgentBankingActivity
        AgentBankingActivityDTO agentBankingActivityDTO = agentBankingActivityMapper.toDto(agentBankingActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentBankingActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentBankingActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgentBankingActivity in the database
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(0)).save(agentBankingActivity);
    }

    @Test
    @Transactional
    void deleteAgentBankingActivity() throws Exception {
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);

        int databaseSizeBeforeDelete = agentBankingActivityRepository.findAll().size();

        // Delete the agentBankingActivity
        restAgentBankingActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, agentBankingActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentBankingActivity> agentBankingActivityList = agentBankingActivityRepository.findAll();
        assertThat(agentBankingActivityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AgentBankingActivity in Elasticsearch
        verify(mockAgentBankingActivitySearchRepository, times(1)).deleteById(agentBankingActivity.getId());
    }

    @Test
    @Transactional
    void searchAgentBankingActivity() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        agentBankingActivityRepository.saveAndFlush(agentBankingActivity);
        when(mockAgentBankingActivitySearchRepository.search("id:" + agentBankingActivity.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(agentBankingActivity), PageRequest.of(0, 1), 1));

        // Search the agentBankingActivity
        restAgentBankingActivityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + agentBankingActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentBankingActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].agentUniqueId").value(hasItem(DEFAULT_AGENT_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].terminalUniqueId").value(hasItem(DEFAULT_TERMINAL_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].totalCountOfTransactions").value(hasItem(DEFAULT_TOTAL_COUNT_OF_TRANSACTIONS)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_IN_LCY)))
            );
    }
}
