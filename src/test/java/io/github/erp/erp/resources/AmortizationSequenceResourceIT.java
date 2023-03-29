package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
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
import io.github.erp.domain.*;
import io.github.erp.repository.AmortizationSequenceRepository;
import io.github.erp.repository.search.AmortizationSequenceSearchRepository;
import io.github.erp.service.AmortizationSequenceService;
import io.github.erp.service.dto.AmortizationSequenceDTO;
import io.github.erp.service.mapper.AmortizationSequenceMapper;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AmortizationSequenceResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
public class AmortizationSequenceResourceIT {

    private static final UUID DEFAULT_PREPAYMENT_ACCOUNT_GUID = UUID.randomUUID();
    private static final UUID UPDATED_PREPAYMENT_ACCOUNT_GUID = UUID.randomUUID();

    private static final UUID DEFAULT_RECURRENCE_GUID = UUID.randomUUID();
    private static final UUID UPDATED_RECURRENCE_GUID = UUID.randomUUID();

    private static final Integer DEFAULT_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_SEQUENCE_NUMBER = 1 - 1;

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CURRENT_AMORTIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CURRENT_AMORTIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CURRENT_AMORTIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PREVIOUS_AMORTIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREVIOUS_AMORTIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PREVIOUS_AMORTIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_NEXT_AMORTIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_AMORTIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NEXT_AMORTIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_COMMENCEMENT_SEQUENCE = false;
    private static final Boolean UPDATED_IS_COMMENCEMENT_SEQUENCE = true;

    private static final Boolean DEFAULT_IS_TERMINAL_SEQUENCE = false;
    private static final Boolean UPDATED_IS_TERMINAL_SEQUENCE = true;

    private static final BigDecimal DEFAULT_AMORTIZATION_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMORTIZATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_AMORTIZATION_AMOUNT = new BigDecimal(0 - 1);

    private static final UUID DEFAULT_SEQUENCE_GUID = UUID.randomUUID();
    private static final UUID UPDATED_SEQUENCE_GUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/prepayments/amortization-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/amortization-sequences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmortizationSequenceRepository amortizationSequenceRepository;

    @Mock
    private AmortizationSequenceRepository amortizationSequenceRepositoryMock;

    @Autowired
    private AmortizationSequenceMapper amortizationSequenceMapper;

    @Mock
    private AmortizationSequenceService amortizationSequenceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AmortizationSequenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmortizationSequenceSearchRepository mockAmortizationSequenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmortizationSequenceMockMvc;

    private AmortizationSequence amortizationSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationSequence createEntity(EntityManager em) {
        AmortizationSequence amortizationSequence = new AmortizationSequence()
            .prepaymentAccountGuid(DEFAULT_PREPAYMENT_ACCOUNT_GUID)
            .recurrenceGuid(DEFAULT_RECURRENCE_GUID)
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .currentAmortizationDate(DEFAULT_CURRENT_AMORTIZATION_DATE)
            .previousAmortizationDate(DEFAULT_PREVIOUS_AMORTIZATION_DATE)
            .nextAmortizationDate(DEFAULT_NEXT_AMORTIZATION_DATE)
            .isCommencementSequence(DEFAULT_IS_COMMENCEMENT_SEQUENCE)
            .isTerminalSequence(DEFAULT_IS_TERMINAL_SEQUENCE)
            .amortizationAmount(DEFAULT_AMORTIZATION_AMOUNT)
            .sequenceGuid(DEFAULT_SEQUENCE_GUID);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        amortizationSequence.setPrepaymentAccount(prepaymentAccount);
        // Add required entity
        AmortizationRecurrence amortizationRecurrence;
        if (TestUtil.findAll(em, AmortizationRecurrence.class).isEmpty()) {
            amortizationRecurrence = AmortizationRecurrenceResourceIT.createEntity(em);
            em.persist(amortizationRecurrence);
            em.flush();
        } else {
            amortizationRecurrence = TestUtil.findAll(em, AmortizationRecurrence.class).get(0);
        }
        amortizationSequence.setAmortizationRecurrence(amortizationRecurrence);
        return amortizationSequence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationSequence createUpdatedEntity(EntityManager em) {
        AmortizationSequence amortizationSequence = new AmortizationSequence()
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .currentAmortizationDate(UPDATED_CURRENT_AMORTIZATION_DATE)
            .previousAmortizationDate(UPDATED_PREVIOUS_AMORTIZATION_DATE)
            .nextAmortizationDate(UPDATED_NEXT_AMORTIZATION_DATE)
            .isCommencementSequence(UPDATED_IS_COMMENCEMENT_SEQUENCE)
            .isTerminalSequence(UPDATED_IS_TERMINAL_SEQUENCE)
            .amortizationAmount(UPDATED_AMORTIZATION_AMOUNT)
            .sequenceGuid(UPDATED_SEQUENCE_GUID);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createUpdatedEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        amortizationSequence.setPrepaymentAccount(prepaymentAccount);
        // Add required entity
        AmortizationRecurrence amortizationRecurrence;
        if (TestUtil.findAll(em, AmortizationRecurrence.class).isEmpty()) {
            amortizationRecurrence = AmortizationRecurrenceResourceIT.createUpdatedEntity(em);
            em.persist(amortizationRecurrence);
            em.flush();
        } else {
            amortizationRecurrence = TestUtil.findAll(em, AmortizationRecurrence.class).get(0);
        }
        amortizationSequence.setAmortizationRecurrence(amortizationRecurrence);
        return amortizationSequence;
    }

    @BeforeEach
    public void initTest() {
        amortizationSequence = createEntity(em);
    }

    @Test
    @Transactional
    void createAmortizationSequence() throws Exception {
        int databaseSizeBeforeCreate = amortizationSequenceRepository.findAll().size();
        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);
        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeCreate + 1);
        AmortizationSequence testAmortizationSequence = amortizationSequenceList.get(amortizationSequenceList.size() - 1);
        assertThat(testAmortizationSequence.getPrepaymentAccountGuid()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT_GUID);
        assertThat(testAmortizationSequence.getRecurrenceGuid()).isEqualTo(DEFAULT_RECURRENCE_GUID);
        assertThat(testAmortizationSequence.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testAmortizationSequence.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testAmortizationSequence.getCurrentAmortizationDate()).isEqualTo(DEFAULT_CURRENT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getPreviousAmortizationDate()).isEqualTo(DEFAULT_PREVIOUS_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getNextAmortizationDate()).isEqualTo(DEFAULT_NEXT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getIsCommencementSequence()).isEqualTo(DEFAULT_IS_COMMENCEMENT_SEQUENCE);
        assertThat(testAmortizationSequence.getIsTerminalSequence()).isEqualTo(DEFAULT_IS_TERMINAL_SEQUENCE);
        assertThat(testAmortizationSequence.getAmortizationAmount()).isEqualByComparingTo(DEFAULT_AMORTIZATION_AMOUNT);
        assertThat(testAmortizationSequence.getSequenceGuid()).isEqualTo(DEFAULT_SEQUENCE_GUID);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(1)).save(testAmortizationSequence);
    }

    @Test
    @Transactional
    void createAmortizationSequenceWithExistingId() throws Exception {
        // Create the AmortizationSequence with an existing ID
        amortizationSequence.setId(1L);
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        int databaseSizeBeforeCreate = amortizationSequenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void checkPrepaymentAccountGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setPrepaymentAccountGuid(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecurrenceGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setRecurrenceGuid(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setSequenceNumber(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentAmortizationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setCurrentAmortizationDate(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsCommencementSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setIsCommencementSequence(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTerminalSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setIsTerminalSequence(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmortizationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setAmortizationAmount(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationSequenceRepository.findAll().size();
        // set the field null
        amortizationSequence.setSequenceGuid(null);

        // Create the AmortizationSequence, which fails.
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmortizationSequences() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].currentAmortizationDate").value(hasItem(DEFAULT_CURRENT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousAmortizationDate").value(hasItem(DEFAULT_PREVIOUS_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextAmortizationDate").value(hasItem(DEFAULT_NEXT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCommencementSequence").value(hasItem(DEFAULT_IS_COMMENCEMENT_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].isTerminalSequence").value(hasItem(DEFAULT_IS_TERMINAL_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].sequenceGuid").value(hasItem(DEFAULT_SEQUENCE_GUID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmortizationSequencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(amortizationSequenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmortizationSequenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amortizationSequenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmortizationSequencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(amortizationSequenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmortizationSequenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amortizationSequenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAmortizationSequence() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get the amortizationSequence
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, amortizationSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amortizationSequence.getId().intValue()))
            .andExpect(jsonPath("$.prepaymentAccountGuid").value(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString()))
            .andExpect(jsonPath("$.recurrenceGuid").value(DEFAULT_RECURRENCE_GUID.toString()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.currentAmortizationDate").value(DEFAULT_CURRENT_AMORTIZATION_DATE.toString()))
            .andExpect(jsonPath("$.previousAmortizationDate").value(DEFAULT_PREVIOUS_AMORTIZATION_DATE.toString()))
            .andExpect(jsonPath("$.nextAmortizationDate").value(DEFAULT_NEXT_AMORTIZATION_DATE.toString()))
            .andExpect(jsonPath("$.isCommencementSequence").value(DEFAULT_IS_COMMENCEMENT_SEQUENCE.booleanValue()))
            .andExpect(jsonPath("$.isTerminalSequence").value(DEFAULT_IS_TERMINAL_SEQUENCE.booleanValue()))
            .andExpect(jsonPath("$.amortizationAmount").value(sameNumber(DEFAULT_AMORTIZATION_AMOUNT)))
            .andExpect(jsonPath("$.sequenceGuid").value(DEFAULT_SEQUENCE_GUID.toString()));
    }

    @Test
    @Transactional
    void getAmortizationSequencesByIdFiltering() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        Long id = amortizationSequence.getId();

        defaultAmortizationSequenceShouldBeFound("id.equals=" + id);
        defaultAmortizationSequenceShouldNotBeFound("id.notEquals=" + id);

        defaultAmortizationSequenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmortizationSequenceShouldNotBeFound("id.greaterThan=" + id);

        defaultAmortizationSequenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmortizationSequenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentAccountGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where prepaymentAccountGuid equals to DEFAULT_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldBeFound("prepaymentAccountGuid.equals=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID);

        // Get all the amortizationSequenceList where prepaymentAccountGuid equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldNotBeFound("prepaymentAccountGuid.equals=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentAccountGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where prepaymentAccountGuid not equals to DEFAULT_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldNotBeFound("prepaymentAccountGuid.notEquals=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID);

        // Get all the amortizationSequenceList where prepaymentAccountGuid not equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldBeFound("prepaymentAccountGuid.notEquals=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentAccountGuidIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where prepaymentAccountGuid in DEFAULT_PREPAYMENT_ACCOUNT_GUID or UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldBeFound(
            "prepaymentAccountGuid.in=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID + "," + UPDATED_PREPAYMENT_ACCOUNT_GUID
        );

        // Get all the amortizationSequenceList where prepaymentAccountGuid equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationSequenceShouldNotBeFound("prepaymentAccountGuid.in=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentAccountGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where prepaymentAccountGuid is not null
        defaultAmortizationSequenceShouldBeFound("prepaymentAccountGuid.specified=true");

        // Get all the amortizationSequenceList where prepaymentAccountGuid is null
        defaultAmortizationSequenceShouldNotBeFound("prepaymentAccountGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByRecurrenceGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where recurrenceGuid equals to DEFAULT_RECURRENCE_GUID
        defaultAmortizationSequenceShouldBeFound("recurrenceGuid.equals=" + DEFAULT_RECURRENCE_GUID);

        // Get all the amortizationSequenceList where recurrenceGuid equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("recurrenceGuid.equals=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByRecurrenceGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where recurrenceGuid not equals to DEFAULT_RECURRENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("recurrenceGuid.notEquals=" + DEFAULT_RECURRENCE_GUID);

        // Get all the amortizationSequenceList where recurrenceGuid not equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationSequenceShouldBeFound("recurrenceGuid.notEquals=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByRecurrenceGuidIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where recurrenceGuid in DEFAULT_RECURRENCE_GUID or UPDATED_RECURRENCE_GUID
        defaultAmortizationSequenceShouldBeFound("recurrenceGuid.in=" + DEFAULT_RECURRENCE_GUID + "," + UPDATED_RECURRENCE_GUID);

        // Get all the amortizationSequenceList where recurrenceGuid equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("recurrenceGuid.in=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByRecurrenceGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where recurrenceGuid is not null
        defaultAmortizationSequenceShouldBeFound("recurrenceGuid.specified=true");

        // Get all the amortizationSequenceList where recurrenceGuid is null
        defaultAmortizationSequenceShouldNotBeFound("recurrenceGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber is not null
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.specified=true");

        // Get all the amortizationSequenceList where sequenceNumber is null
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber is greater than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.greaterThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber is greater than or equal to UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.greaterThanOrEqual=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber is less than or equal to DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.lessThanOrEqual=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber is less than or equal to SMALLER_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.lessThanOrEqual=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber is less than DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.lessThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber is less than UPDATED_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.lessThan=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceNumber is greater than DEFAULT_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldNotBeFound("sequenceNumber.greaterThan=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the amortizationSequenceList where sequenceNumber is greater than SMALLER_SEQUENCE_NUMBER
        defaultAmortizationSequenceShouldBeFound("sequenceNumber.greaterThan=" + SMALLER_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars equals to DEFAULT_PARTICULARS
        defaultAmortizationSequenceShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the amortizationSequenceList where particulars equals to UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars not equals to DEFAULT_PARTICULARS
        defaultAmortizationSequenceShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the amortizationSequenceList where particulars not equals to UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the amortizationSequenceList where particulars equals to UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars is not null
        defaultAmortizationSequenceShouldBeFound("particulars.specified=true");

        // Get all the amortizationSequenceList where particulars is null
        defaultAmortizationSequenceShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsContainsSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars contains DEFAULT_PARTICULARS
        defaultAmortizationSequenceShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the amortizationSequenceList where particulars contains UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where particulars does not contain DEFAULT_PARTICULARS
        defaultAmortizationSequenceShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the amortizationSequenceList where particulars does not contain UPDATED_PARTICULARS
        defaultAmortizationSequenceShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate equals to DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.equals=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate equals to UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.equals=" + UPDATED_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate not equals to DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.notEquals=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate not equals to UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.notEquals=" + UPDATED_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate in DEFAULT_CURRENT_AMORTIZATION_DATE or UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound(
            "currentAmortizationDate.in=" + DEFAULT_CURRENT_AMORTIZATION_DATE + "," + UPDATED_CURRENT_AMORTIZATION_DATE
        );

        // Get all the amortizationSequenceList where currentAmortizationDate equals to UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.in=" + UPDATED_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate is not null
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.specified=true");

        // Get all the amortizationSequenceList where currentAmortizationDate is null
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate is greater than or equal to DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.greaterThanOrEqual=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate is greater than or equal to UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.greaterThanOrEqual=" + UPDATED_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate is less than or equal to DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.lessThanOrEqual=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate is less than or equal to SMALLER_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.lessThanOrEqual=" + SMALLER_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate is less than DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.lessThan=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate is less than UPDATED_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.lessThan=" + UPDATED_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByCurrentAmortizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where currentAmortizationDate is greater than DEFAULT_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("currentAmortizationDate.greaterThan=" + DEFAULT_CURRENT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where currentAmortizationDate is greater than SMALLER_CURRENT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("currentAmortizationDate.greaterThan=" + SMALLER_CURRENT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate equals to DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.equals=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate equals to UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.equals=" + UPDATED_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate not equals to DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.notEquals=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate not equals to UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.notEquals=" + UPDATED_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate in DEFAULT_PREVIOUS_AMORTIZATION_DATE or UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound(
            "previousAmortizationDate.in=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE + "," + UPDATED_PREVIOUS_AMORTIZATION_DATE
        );

        // Get all the amortizationSequenceList where previousAmortizationDate equals to UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.in=" + UPDATED_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate is not null
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.specified=true");

        // Get all the amortizationSequenceList where previousAmortizationDate is null
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate is greater than or equal to DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.greaterThanOrEqual=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate is greater than or equal to UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.greaterThanOrEqual=" + UPDATED_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate is less than or equal to DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.lessThanOrEqual=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate is less than or equal to SMALLER_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.lessThanOrEqual=" + SMALLER_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate is less than DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.lessThan=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate is less than UPDATED_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.lessThan=" + UPDATED_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPreviousAmortizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where previousAmortizationDate is greater than DEFAULT_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("previousAmortizationDate.greaterThan=" + DEFAULT_PREVIOUS_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where previousAmortizationDate is greater than SMALLER_PREVIOUS_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("previousAmortizationDate.greaterThan=" + SMALLER_PREVIOUS_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate equals to DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.equals=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate equals to UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.equals=" + UPDATED_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate not equals to DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.notEquals=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate not equals to UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.notEquals=" + UPDATED_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate in DEFAULT_NEXT_AMORTIZATION_DATE or UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound(
            "nextAmortizationDate.in=" + DEFAULT_NEXT_AMORTIZATION_DATE + "," + UPDATED_NEXT_AMORTIZATION_DATE
        );

        // Get all the amortizationSequenceList where nextAmortizationDate equals to UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.in=" + UPDATED_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate is not null
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.specified=true");

        // Get all the amortizationSequenceList where nextAmortizationDate is null
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate is greater than or equal to DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.greaterThanOrEqual=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate is greater than or equal to UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.greaterThanOrEqual=" + UPDATED_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate is less than or equal to DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.lessThanOrEqual=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate is less than or equal to SMALLER_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.lessThanOrEqual=" + SMALLER_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate is less than DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.lessThan=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate is less than UPDATED_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.lessThan=" + UPDATED_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByNextAmortizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where nextAmortizationDate is greater than DEFAULT_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldNotBeFound("nextAmortizationDate.greaterThan=" + DEFAULT_NEXT_AMORTIZATION_DATE);

        // Get all the amortizationSequenceList where nextAmortizationDate is greater than SMALLER_NEXT_AMORTIZATION_DATE
        defaultAmortizationSequenceShouldBeFound("nextAmortizationDate.greaterThan=" + SMALLER_NEXT_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsCommencementSequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isCommencementSequence equals to DEFAULT_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldBeFound("isCommencementSequence.equals=" + DEFAULT_IS_COMMENCEMENT_SEQUENCE);

        // Get all the amortizationSequenceList where isCommencementSequence equals to UPDATED_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isCommencementSequence.equals=" + UPDATED_IS_COMMENCEMENT_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsCommencementSequenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isCommencementSequence not equals to DEFAULT_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isCommencementSequence.notEquals=" + DEFAULT_IS_COMMENCEMENT_SEQUENCE);

        // Get all the amortizationSequenceList where isCommencementSequence not equals to UPDATED_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldBeFound("isCommencementSequence.notEquals=" + UPDATED_IS_COMMENCEMENT_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsCommencementSequenceIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isCommencementSequence in DEFAULT_IS_COMMENCEMENT_SEQUENCE or UPDATED_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldBeFound(
            "isCommencementSequence.in=" + DEFAULT_IS_COMMENCEMENT_SEQUENCE + "," + UPDATED_IS_COMMENCEMENT_SEQUENCE
        );

        // Get all the amortizationSequenceList where isCommencementSequence equals to UPDATED_IS_COMMENCEMENT_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isCommencementSequence.in=" + UPDATED_IS_COMMENCEMENT_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsCommencementSequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isCommencementSequence is not null
        defaultAmortizationSequenceShouldBeFound("isCommencementSequence.specified=true");

        // Get all the amortizationSequenceList where isCommencementSequence is null
        defaultAmortizationSequenceShouldNotBeFound("isCommencementSequence.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsTerminalSequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isTerminalSequence equals to DEFAULT_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldBeFound("isTerminalSequence.equals=" + DEFAULT_IS_TERMINAL_SEQUENCE);

        // Get all the amortizationSequenceList where isTerminalSequence equals to UPDATED_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isTerminalSequence.equals=" + UPDATED_IS_TERMINAL_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsTerminalSequenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isTerminalSequence not equals to DEFAULT_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isTerminalSequence.notEquals=" + DEFAULT_IS_TERMINAL_SEQUENCE);

        // Get all the amortizationSequenceList where isTerminalSequence not equals to UPDATED_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldBeFound("isTerminalSequence.notEquals=" + UPDATED_IS_TERMINAL_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsTerminalSequenceIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isTerminalSequence in DEFAULT_IS_TERMINAL_SEQUENCE or UPDATED_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldBeFound(
            "isTerminalSequence.in=" + DEFAULT_IS_TERMINAL_SEQUENCE + "," + UPDATED_IS_TERMINAL_SEQUENCE
        );

        // Get all the amortizationSequenceList where isTerminalSequence equals to UPDATED_IS_TERMINAL_SEQUENCE
        defaultAmortizationSequenceShouldNotBeFound("isTerminalSequence.in=" + UPDATED_IS_TERMINAL_SEQUENCE);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByIsTerminalSequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where isTerminalSequence is not null
        defaultAmortizationSequenceShouldBeFound("isTerminalSequence.specified=true");

        // Get all the amortizationSequenceList where isTerminalSequence is null
        defaultAmortizationSequenceShouldNotBeFound("isTerminalSequence.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount equals to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.equals=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.equals=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount not equals to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.notEquals=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount not equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.notEquals=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount in DEFAULT_AMORTIZATION_AMOUNT or UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound(
            "amortizationAmount.in=" + DEFAULT_AMORTIZATION_AMOUNT + "," + UPDATED_AMORTIZATION_AMOUNT
        );

        // Get all the amortizationSequenceList where amortizationAmount equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.in=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount is not null
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.specified=true");

        // Get all the amortizationSequenceList where amortizationAmount is null
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount is greater than or equal to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.greaterThanOrEqual=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount is greater than or equal to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.greaterThanOrEqual=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount is less than or equal to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.lessThanOrEqual=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount is less than or equal to SMALLER_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.lessThanOrEqual=" + SMALLER_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount is less than DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.lessThan=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount is less than UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.lessThan=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where amortizationAmount is greater than DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldNotBeFound("amortizationAmount.greaterThan=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationSequenceList where amortizationAmount is greater than SMALLER_AMORTIZATION_AMOUNT
        defaultAmortizationSequenceShouldBeFound("amortizationAmount.greaterThan=" + SMALLER_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceGuid equals to DEFAULT_SEQUENCE_GUID
        defaultAmortizationSequenceShouldBeFound("sequenceGuid.equals=" + DEFAULT_SEQUENCE_GUID);

        // Get all the amortizationSequenceList where sequenceGuid equals to UPDATED_SEQUENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("sequenceGuid.equals=" + UPDATED_SEQUENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceGuid not equals to DEFAULT_SEQUENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("sequenceGuid.notEquals=" + DEFAULT_SEQUENCE_GUID);

        // Get all the amortizationSequenceList where sequenceGuid not equals to UPDATED_SEQUENCE_GUID
        defaultAmortizationSequenceShouldBeFound("sequenceGuid.notEquals=" + UPDATED_SEQUENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceGuidIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceGuid in DEFAULT_SEQUENCE_GUID or UPDATED_SEQUENCE_GUID
        defaultAmortizationSequenceShouldBeFound("sequenceGuid.in=" + DEFAULT_SEQUENCE_GUID + "," + UPDATED_SEQUENCE_GUID);

        // Get all the amortizationSequenceList where sequenceGuid equals to UPDATED_SEQUENCE_GUID
        defaultAmortizationSequenceShouldNotBeFound("sequenceGuid.in=" + UPDATED_SEQUENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesBySequenceGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        // Get all the amortizationSequenceList where sequenceGuid is not null
        defaultAmortizationSequenceShouldBeFound("sequenceGuid.specified=true");

        // Get all the amortizationSequenceList where sequenceGuid is null
        defaultAmortizationSequenceShouldNotBeFound("sequenceGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
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
        amortizationSequence.setPrepaymentAccount(prepaymentAccount);
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        Long prepaymentAccountId = prepaymentAccount.getId();

        // Get all the amortizationSequenceList where prepaymentAccount equals to prepaymentAccountId
        defaultAmortizationSequenceShouldBeFound("prepaymentAccountId.equals=" + prepaymentAccountId);

        // Get all the amortizationSequenceList where prepaymentAccount equals to (prepaymentAccountId + 1)
        defaultAmortizationSequenceShouldNotBeFound("prepaymentAccountId.equals=" + (prepaymentAccountId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByAmortizationRecurrenceIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        AmortizationRecurrence amortizationRecurrence;
        if (TestUtil.findAll(em, AmortizationRecurrence.class).isEmpty()) {
            amortizationRecurrence = AmortizationRecurrenceResourceIT.createEntity(em);
            em.persist(amortizationRecurrence);
            em.flush();
        } else {
            amortizationRecurrence = TestUtil.findAll(em, AmortizationRecurrence.class).get(0);
        }
        em.persist(amortizationRecurrence);
        em.flush();
        amortizationSequence.setAmortizationRecurrence(amortizationRecurrence);
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        Long amortizationRecurrenceId = amortizationRecurrence.getId();

        // Get all the amortizationSequenceList where amortizationRecurrence equals to amortizationRecurrenceId
        defaultAmortizationSequenceShouldBeFound("amortizationRecurrenceId.equals=" + amortizationRecurrenceId);

        // Get all the amortizationSequenceList where amortizationRecurrence equals to (amortizationRecurrenceId + 1)
        defaultAmortizationSequenceShouldNotBeFound("amortizationRecurrenceId.equals=" + (amortizationRecurrenceId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
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
        amortizationSequence.addPlaceholder(placeholder);
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        Long placeholderId = placeholder.getId();

        // Get all the amortizationSequenceList where placeholder equals to placeholderId
        defaultAmortizationSequenceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the amortizationSequenceList where placeholder equals to (placeholderId + 1)
        defaultAmortizationSequenceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByPrepaymentMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        PrepaymentMapping prepaymentMapping;
        if (TestUtil.findAll(em, PrepaymentMapping.class).isEmpty()) {
            prepaymentMapping = PrepaymentMappingResourceIT.createEntity(em);
            em.persist(prepaymentMapping);
            em.flush();
        } else {
            prepaymentMapping = TestUtil.findAll(em, PrepaymentMapping.class).get(0);
        }
        em.persist(prepaymentMapping);
        em.flush();
        amortizationSequence.addPrepaymentMapping(prepaymentMapping);
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        Long prepaymentMappingId = prepaymentMapping.getId();

        // Get all the amortizationSequenceList where prepaymentMapping equals to prepaymentMappingId
        defaultAmortizationSequenceShouldBeFound("prepaymentMappingId.equals=" + prepaymentMappingId);

        // Get all the amortizationSequenceList where prepaymentMapping equals to (prepaymentMappingId + 1)
        defaultAmortizationSequenceShouldNotBeFound("prepaymentMappingId.equals=" + (prepaymentMappingId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationSequencesByApplicationParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        UniversallyUniqueMapping applicationParameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            applicationParameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(applicationParameters);
            em.flush();
        } else {
            applicationParameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(applicationParameters);
        em.flush();
        amortizationSequence.addApplicationParameters(applicationParameters);
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        Long applicationParametersId = applicationParameters.getId();

        // Get all the amortizationSequenceList where applicationParameters equals to applicationParametersId
        defaultAmortizationSequenceShouldBeFound("applicationParametersId.equals=" + applicationParametersId);

        // Get all the amortizationSequenceList where applicationParameters equals to (applicationParametersId + 1)
        defaultAmortizationSequenceShouldNotBeFound("applicationParametersId.equals=" + (applicationParametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmortizationSequenceShouldBeFound(String filter) throws Exception {
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].currentAmortizationDate").value(hasItem(DEFAULT_CURRENT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousAmortizationDate").value(hasItem(DEFAULT_PREVIOUS_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextAmortizationDate").value(hasItem(DEFAULT_NEXT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCommencementSequence").value(hasItem(DEFAULT_IS_COMMENCEMENT_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].isTerminalSequence").value(hasItem(DEFAULT_IS_TERMINAL_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].sequenceGuid").value(hasItem(DEFAULT_SEQUENCE_GUID.toString())));

        // Check, that the count call also returns 1
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmortizationSequenceShouldNotBeFound(String filter) throws Exception {
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmortizationSequence() throws Exception {
        // Get the amortizationSequence
        restAmortizationSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmortizationSequence() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();

        // Update the amortizationSequence
        AmortizationSequence updatedAmortizationSequence = amortizationSequenceRepository.findById(amortizationSequence.getId()).get();
        // Disconnect from session so that the updates on updatedAmortizationSequence are not directly saved in db
        em.detach(updatedAmortizationSequence);
        updatedAmortizationSequence
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .currentAmortizationDate(UPDATED_CURRENT_AMORTIZATION_DATE)
            .previousAmortizationDate(UPDATED_PREVIOUS_AMORTIZATION_DATE)
            .nextAmortizationDate(UPDATED_NEXT_AMORTIZATION_DATE)
            .isCommencementSequence(UPDATED_IS_COMMENCEMENT_SEQUENCE)
            .isTerminalSequence(UPDATED_IS_TERMINAL_SEQUENCE)
            .amortizationAmount(UPDATED_AMORTIZATION_AMOUNT)
            .sequenceGuid(UPDATED_SEQUENCE_GUID);
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(updatedAmortizationSequence);

        restAmortizationSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationSequence testAmortizationSequence = amortizationSequenceList.get(amortizationSequenceList.size() - 1);
        assertThat(testAmortizationSequence.getPrepaymentAccountGuid()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT_GUID);
        assertThat(testAmortizationSequence.getRecurrenceGuid()).isEqualTo(UPDATED_RECURRENCE_GUID);
        assertThat(testAmortizationSequence.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationSequence.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testAmortizationSequence.getCurrentAmortizationDate()).isEqualTo(UPDATED_CURRENT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getPreviousAmortizationDate()).isEqualTo(UPDATED_PREVIOUS_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getNextAmortizationDate()).isEqualTo(UPDATED_NEXT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getIsCommencementSequence()).isEqualTo(UPDATED_IS_COMMENCEMENT_SEQUENCE);
        assertThat(testAmortizationSequence.getIsTerminalSequence()).isEqualTo(UPDATED_IS_TERMINAL_SEQUENCE);
        assertThat(testAmortizationSequence.getAmortizationAmount()).isEqualTo(UPDATED_AMORTIZATION_AMOUNT);
        assertThat(testAmortizationSequence.getSequenceGuid()).isEqualTo(UPDATED_SEQUENCE_GUID);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository).save(testAmortizationSequence);
    }

    @Test
    @Transactional
    void putNonExistingAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void partialUpdateAmortizationSequenceWithPatch() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();

        // Update the amortizationSequence using partial update
        AmortizationSequence partialUpdatedAmortizationSequence = new AmortizationSequence();
        partialUpdatedAmortizationSequence.setId(amortizationSequence.getId());

        partialUpdatedAmortizationSequence
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .currentAmortizationDate(UPDATED_CURRENT_AMORTIZATION_DATE)
            .isCommencementSequence(UPDATED_IS_COMMENCEMENT_SEQUENCE)
            .isTerminalSequence(UPDATED_IS_TERMINAL_SEQUENCE)
            .sequenceGuid(UPDATED_SEQUENCE_GUID);

        restAmortizationSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationSequence))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationSequence testAmortizationSequence = amortizationSequenceList.get(amortizationSequenceList.size() - 1);
        assertThat(testAmortizationSequence.getPrepaymentAccountGuid()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT_GUID);
        assertThat(testAmortizationSequence.getRecurrenceGuid()).isEqualTo(DEFAULT_RECURRENCE_GUID);
        assertThat(testAmortizationSequence.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationSequence.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testAmortizationSequence.getCurrentAmortizationDate()).isEqualTo(UPDATED_CURRENT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getPreviousAmortizationDate()).isEqualTo(DEFAULT_PREVIOUS_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getNextAmortizationDate()).isEqualTo(DEFAULT_NEXT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getIsCommencementSequence()).isEqualTo(UPDATED_IS_COMMENCEMENT_SEQUENCE);
        assertThat(testAmortizationSequence.getIsTerminalSequence()).isEqualTo(UPDATED_IS_TERMINAL_SEQUENCE);
        assertThat(testAmortizationSequence.getAmortizationAmount()).isEqualByComparingTo(DEFAULT_AMORTIZATION_AMOUNT);
        assertThat(testAmortizationSequence.getSequenceGuid()).isEqualTo(UPDATED_SEQUENCE_GUID);
    }

    @Test
    @Transactional
    void fullUpdateAmortizationSequenceWithPatch() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();

        // Update the amortizationSequence using partial update
        AmortizationSequence partialUpdatedAmortizationSequence = new AmortizationSequence();
        partialUpdatedAmortizationSequence.setId(amortizationSequence.getId());

        partialUpdatedAmortizationSequence
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .currentAmortizationDate(UPDATED_CURRENT_AMORTIZATION_DATE)
            .previousAmortizationDate(UPDATED_PREVIOUS_AMORTIZATION_DATE)
            .nextAmortizationDate(UPDATED_NEXT_AMORTIZATION_DATE)
            .isCommencementSequence(UPDATED_IS_COMMENCEMENT_SEQUENCE)
            .isTerminalSequence(UPDATED_IS_TERMINAL_SEQUENCE)
            .amortizationAmount(UPDATED_AMORTIZATION_AMOUNT)
            .sequenceGuid(UPDATED_SEQUENCE_GUID);

        restAmortizationSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationSequence))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationSequence testAmortizationSequence = amortizationSequenceList.get(amortizationSequenceList.size() - 1);
        assertThat(testAmortizationSequence.getPrepaymentAccountGuid()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT_GUID);
        assertThat(testAmortizationSequence.getRecurrenceGuid()).isEqualTo(UPDATED_RECURRENCE_GUID);
        assertThat(testAmortizationSequence.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testAmortizationSequence.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testAmortizationSequence.getCurrentAmortizationDate()).isEqualTo(UPDATED_CURRENT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getPreviousAmortizationDate()).isEqualTo(UPDATED_PREVIOUS_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getNextAmortizationDate()).isEqualTo(UPDATED_NEXT_AMORTIZATION_DATE);
        assertThat(testAmortizationSequence.getIsCommencementSequence()).isEqualTo(UPDATED_IS_COMMENCEMENT_SEQUENCE);
        assertThat(testAmortizationSequence.getIsTerminalSequence()).isEqualTo(UPDATED_IS_TERMINAL_SEQUENCE);
        assertThat(testAmortizationSequence.getAmortizationAmount()).isEqualByComparingTo(UPDATED_AMORTIZATION_AMOUNT);
        assertThat(testAmortizationSequence.getSequenceGuid()).isEqualTo(UPDATED_SEQUENCE_GUID);
    }

    @Test
    @Transactional
    void patchNonExistingAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amortizationSequenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmortizationSequence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationSequenceRepository.findAll().size();
        amortizationSequence.setId(count.incrementAndGet());

        // Create the AmortizationSequence
        AmortizationSequenceDTO amortizationSequenceDTO = amortizationSequenceMapper.toDto(amortizationSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationSequence in the database
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(0)).save(amortizationSequence);
    }

    @Test
    @Transactional
    void deleteAmortizationSequence() throws Exception {
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);

        int databaseSizeBeforeDelete = amortizationSequenceRepository.findAll().size();

        // Delete the amortizationSequence
        restAmortizationSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, amortizationSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AmortizationSequence> amortizationSequenceList = amortizationSequenceRepository.findAll();
        assertThat(amortizationSequenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AmortizationSequence in Elasticsearch
        verify(mockAmortizationSequenceSearchRepository, times(1)).deleteById(amortizationSequence.getId());
    }

    @Test
    @Transactional
    void searchAmortizationSequence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        amortizationSequenceRepository.saveAndFlush(amortizationSequence);
        when(mockAmortizationSequenceSearchRepository.search("id:" + amortizationSequence.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amortizationSequence), PageRequest.of(0, 1), 1));

        // Search the amortizationSequence
        restAmortizationSequenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + amortizationSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].currentAmortizationDate").value(hasItem(DEFAULT_CURRENT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousAmortizationDate").value(hasItem(DEFAULT_PREVIOUS_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextAmortizationDate").value(hasItem(DEFAULT_NEXT_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCommencementSequence").value(hasItem(DEFAULT_IS_COMMENCEMENT_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].isTerminalSequence").value(hasItem(DEFAULT_IS_TERMINAL_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].sequenceGuid").value(hasItem(DEFAULT_SEQUENCE_GUID.toString())));
    }
}
