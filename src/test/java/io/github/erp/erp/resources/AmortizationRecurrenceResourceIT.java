package io.github.erp.erp.resources;

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
import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.recurrenceFrequency;
import io.github.erp.repository.AmortizationRecurrenceRepository;
import io.github.erp.repository.search.AmortizationRecurrenceSearchRepository;
import io.github.erp.service.AmortizationRecurrenceService;
import io.github.erp.service.dto.AmortizationRecurrenceDTO;
import io.github.erp.service.mapper.AmortizationRecurrenceMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AmortizationRecurrenceResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PREPAYMENTS_MODULE_USER"})
public class AmortizationRecurrenceResourceIT {

    private static final LocalDate DEFAULT_FIRST_AMORTIZATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_AMORTIZATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FIRST_AMORTIZATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final recurrenceFrequency DEFAULT_AMORTIZATION_FREQUENCY = recurrenceFrequency.MONTHLY;
    private static final recurrenceFrequency UPDATED_AMORTIZATION_FREQUENCY = recurrenceFrequency.BI_MONTHLY;

    private static final Integer DEFAULT_NUMBER_OF_RECURRENCES = 1;
    private static final Integer UPDATED_NUMBER_OF_RECURRENCES = 2;
    private static final Integer SMALLER_NUMBER_OF_RECURRENCES = 1 - 1;

    private static final byte[] DEFAULT_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NOTES_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_OVER_WRITTEN = false;
    private static final Boolean UPDATED_IS_OVER_WRITTEN = true;

    private static final ZonedDateTime DEFAULT_TIME_OF_INSTALLATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_INSTALLATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_INSTALLATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_RECURRENCE_GUID = UUID.randomUUID();
    private static final UUID UPDATED_RECURRENCE_GUID = UUID.randomUUID();

    private static final UUID DEFAULT_PREPAYMENT_ACCOUNT_GUID = UUID.randomUUID();
    private static final UUID UPDATED_PREPAYMENT_ACCOUNT_GUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/prepayments/amortization-recurrences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/prepayments/_search/amortization-recurrences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmortizationRecurrenceRepository amortizationRecurrenceRepository;

    @Mock
    private AmortizationRecurrenceRepository amortizationRecurrenceRepositoryMock;

    @Autowired
    private AmortizationRecurrenceMapper amortizationRecurrenceMapper;

    @Mock
    private AmortizationRecurrenceService amortizationRecurrenceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AmortizationRecurrenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmortizationRecurrenceSearchRepository mockAmortizationRecurrenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmortizationRecurrenceMockMvc;

    private AmortizationRecurrence amortizationRecurrence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationRecurrence createEntity(EntityManager em) {
        AmortizationRecurrence amortizationRecurrence = new AmortizationRecurrence()
            .firstAmortizationDate(DEFAULT_FIRST_AMORTIZATION_DATE)
            .amortizationFrequency(DEFAULT_AMORTIZATION_FREQUENCY)
            .numberOfRecurrences(DEFAULT_NUMBER_OF_RECURRENCES)
            .notes(DEFAULT_NOTES)
            .notesContentType(DEFAULT_NOTES_CONTENT_TYPE)
            .particulars(DEFAULT_PARTICULARS)
            .isActive(DEFAULT_IS_ACTIVE)
            .isOverWritten(DEFAULT_IS_OVER_WRITTEN)
            .timeOfInstallation(DEFAULT_TIME_OF_INSTALLATION)
            .recurrenceGuid(DEFAULT_RECURRENCE_GUID)
            .prepaymentAccountGuid(DEFAULT_PREPAYMENT_ACCOUNT_GUID);
        // Add required entity
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        amortizationRecurrence.setDepreciationMethod(depreciationMethod);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        amortizationRecurrence.setPrepaymentAccount(prepaymentAccount);
        return amortizationRecurrence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationRecurrence createUpdatedEntity(EntityManager em) {
        AmortizationRecurrence amortizationRecurrence = new AmortizationRecurrence()
            .firstAmortizationDate(UPDATED_FIRST_AMORTIZATION_DATE)
            .amortizationFrequency(UPDATED_AMORTIZATION_FREQUENCY)
            .numberOfRecurrences(UPDATED_NUMBER_OF_RECURRENCES)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .particulars(UPDATED_PARTICULARS)
            .isActive(UPDATED_IS_ACTIVE)
            .isOverWritten(UPDATED_IS_OVER_WRITTEN)
            .timeOfInstallation(UPDATED_TIME_OF_INSTALLATION)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID);
        // Add required entity
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        amortizationRecurrence.setDepreciationMethod(depreciationMethod);
        // Add required entity
        PrepaymentAccount prepaymentAccount;
        if (TestUtil.findAll(em, PrepaymentAccount.class).isEmpty()) {
            prepaymentAccount = PrepaymentAccountResourceIT.createUpdatedEntity(em);
            em.persist(prepaymentAccount);
            em.flush();
        } else {
            prepaymentAccount = TestUtil.findAll(em, PrepaymentAccount.class).get(0);
        }
        amortizationRecurrence.setPrepaymentAccount(prepaymentAccount);
        return amortizationRecurrence;
    }

    @BeforeEach
    public void initTest() {
        amortizationRecurrence = createEntity(em);
    }

    @Test
    @Transactional
    void createAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeCreate = amortizationRecurrenceRepository.findAll().size();
        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);
        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeCreate + 1);
        AmortizationRecurrence testAmortizationRecurrence = amortizationRecurrenceList.get(amortizationRecurrenceList.size() - 1);
        assertThat(testAmortizationRecurrence.getFirstAmortizationDate()).isEqualTo(DEFAULT_FIRST_AMORTIZATION_DATE);
        assertThat(testAmortizationRecurrence.getAmortizationFrequency()).isEqualTo(DEFAULT_AMORTIZATION_FREQUENCY);
        assertThat(testAmortizationRecurrence.getNumberOfRecurrences()).isEqualTo(DEFAULT_NUMBER_OF_RECURRENCES);
        assertThat(testAmortizationRecurrence.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAmortizationRecurrence.getNotesContentType()).isEqualTo(DEFAULT_NOTES_CONTENT_TYPE);
        assertThat(testAmortizationRecurrence.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testAmortizationRecurrence.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAmortizationRecurrence.getIsOverWritten()).isEqualTo(DEFAULT_IS_OVER_WRITTEN);
        assertThat(testAmortizationRecurrence.getTimeOfInstallation()).isEqualTo(DEFAULT_TIME_OF_INSTALLATION);
        assertThat(testAmortizationRecurrence.getRecurrenceGuid()).isEqualTo(DEFAULT_RECURRENCE_GUID);
        assertThat(testAmortizationRecurrence.getPrepaymentAccountGuid()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT_GUID);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(1)).save(testAmortizationRecurrence);
    }

    @Test
    @Transactional
    void createAmortizationRecurrenceWithExistingId() throws Exception {
        // Create the AmortizationRecurrence with an existing ID
        amortizationRecurrence.setId(1L);
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        int databaseSizeBeforeCreate = amortizationRecurrenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void checkFirstAmortizationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setFirstAmortizationDate(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmortizationFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setAmortizationFrequency(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfRecurrencesIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setNumberOfRecurrences(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfInstallationIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setTimeOfInstallation(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecurrenceGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setRecurrenceGuid(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrepaymentAccountGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = amortizationRecurrenceRepository.findAll().size();
        // set the field null
        amortizationRecurrence.setPrepaymentAccountGuid(null);

        // Create the AmortizationRecurrence, which fails.
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrences() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationRecurrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstAmortizationDate").value(hasItem(DEFAULT_FIRST_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amortizationFrequency").value(hasItem(DEFAULT_AMORTIZATION_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].numberOfRecurrences").value(hasItem(DEFAULT_NUMBER_OF_RECURRENCES)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isOverWritten").value(hasItem(DEFAULT_IS_OVER_WRITTEN.booleanValue())))
            .andExpect(jsonPath("$.[*].timeOfInstallation").value(hasItem(sameInstant(DEFAULT_TIME_OF_INSTALLATION))))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmortizationRecurrencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(amortizationRecurrenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmortizationRecurrenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amortizationRecurrenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAmortizationRecurrencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(amortizationRecurrenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAmortizationRecurrenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(amortizationRecurrenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAmortizationRecurrence() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get the amortizationRecurrence
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL_ID, amortizationRecurrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amortizationRecurrence.getId().intValue()))
            .andExpect(jsonPath("$.firstAmortizationDate").value(DEFAULT_FIRST_AMORTIZATION_DATE.toString()))
            .andExpect(jsonPath("$.amortizationFrequency").value(DEFAULT_AMORTIZATION_FREQUENCY.toString()))
            .andExpect(jsonPath("$.numberOfRecurrences").value(DEFAULT_NUMBER_OF_RECURRENCES))
            .andExpect(jsonPath("$.notesContentType").value(DEFAULT_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.notes").value(Base64Utils.encodeToString(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isOverWritten").value(DEFAULT_IS_OVER_WRITTEN.booleanValue()))
            .andExpect(jsonPath("$.timeOfInstallation").value(sameInstant(DEFAULT_TIME_OF_INSTALLATION)))
            .andExpect(jsonPath("$.recurrenceGuid").value(DEFAULT_RECURRENCE_GUID.toString()))
            .andExpect(jsonPath("$.prepaymentAccountGuid").value(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString()));
    }

    @Test
    @Transactional
    void getAmortizationRecurrencesByIdFiltering() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        Long id = amortizationRecurrence.getId();

        defaultAmortizationRecurrenceShouldBeFound("id.equals=" + id);
        defaultAmortizationRecurrenceShouldNotBeFound("id.notEquals=" + id);

        defaultAmortizationRecurrenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmortizationRecurrenceShouldNotBeFound("id.greaterThan=" + id);

        defaultAmortizationRecurrenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmortizationRecurrenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate equals to DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.equals=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate equals to UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.equals=" + UPDATED_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate not equals to DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.notEquals=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate not equals to UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.notEquals=" + UPDATED_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate in DEFAULT_FIRST_AMORTIZATION_DATE or UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound(
            "firstAmortizationDate.in=" + DEFAULT_FIRST_AMORTIZATION_DATE + "," + UPDATED_FIRST_AMORTIZATION_DATE
        );

        // Get all the amortizationRecurrenceList where firstAmortizationDate equals to UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.in=" + UPDATED_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is not null
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.specified=true");

        // Get all the amortizationRecurrenceList where firstAmortizationDate is null
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is greater than or equal to DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.greaterThanOrEqual=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is greater than or equal to UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.greaterThanOrEqual=" + UPDATED_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is less than or equal to DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.lessThanOrEqual=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is less than or equal to SMALLER_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.lessThanOrEqual=" + SMALLER_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is less than DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.lessThan=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is less than UPDATED_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.lessThan=" + UPDATED_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByFirstAmortizationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is greater than DEFAULT_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldNotBeFound("firstAmortizationDate.greaterThan=" + DEFAULT_FIRST_AMORTIZATION_DATE);

        // Get all the amortizationRecurrenceList where firstAmortizationDate is greater than SMALLER_FIRST_AMORTIZATION_DATE
        defaultAmortizationRecurrenceShouldBeFound("firstAmortizationDate.greaterThan=" + SMALLER_FIRST_AMORTIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByAmortizationFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where amortizationFrequency equals to DEFAULT_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldBeFound("amortizationFrequency.equals=" + DEFAULT_AMORTIZATION_FREQUENCY);

        // Get all the amortizationRecurrenceList where amortizationFrequency equals to UPDATED_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldNotBeFound("amortizationFrequency.equals=" + UPDATED_AMORTIZATION_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByAmortizationFrequencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where amortizationFrequency not equals to DEFAULT_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldNotBeFound("amortizationFrequency.notEquals=" + DEFAULT_AMORTIZATION_FREQUENCY);

        // Get all the amortizationRecurrenceList where amortizationFrequency not equals to UPDATED_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldBeFound("amortizationFrequency.notEquals=" + UPDATED_AMORTIZATION_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByAmortizationFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where amortizationFrequency in DEFAULT_AMORTIZATION_FREQUENCY or UPDATED_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldBeFound(
            "amortizationFrequency.in=" + DEFAULT_AMORTIZATION_FREQUENCY + "," + UPDATED_AMORTIZATION_FREQUENCY
        );

        // Get all the amortizationRecurrenceList where amortizationFrequency equals to UPDATED_AMORTIZATION_FREQUENCY
        defaultAmortizationRecurrenceShouldNotBeFound("amortizationFrequency.in=" + UPDATED_AMORTIZATION_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByAmortizationFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where amortizationFrequency is not null
        defaultAmortizationRecurrenceShouldBeFound("amortizationFrequency.specified=true");

        // Get all the amortizationRecurrenceList where amortizationFrequency is null
        defaultAmortizationRecurrenceShouldNotBeFound("amortizationFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences equals to DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.equals=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences equals to UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.equals=" + UPDATED_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences not equals to DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.notEquals=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences not equals to UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.notEquals=" + UPDATED_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences in DEFAULT_NUMBER_OF_RECURRENCES or UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound(
            "numberOfRecurrences.in=" + DEFAULT_NUMBER_OF_RECURRENCES + "," + UPDATED_NUMBER_OF_RECURRENCES
        );

        // Get all the amortizationRecurrenceList where numberOfRecurrences equals to UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.in=" + UPDATED_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is not null
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.specified=true");

        // Get all the amortizationRecurrenceList where numberOfRecurrences is null
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is greater than or equal to DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is greater than or equal to UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.greaterThanOrEqual=" + UPDATED_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is less than or equal to DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.lessThanOrEqual=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is less than or equal to SMALLER_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.lessThanOrEqual=" + SMALLER_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is less than DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.lessThan=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is less than UPDATED_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.lessThan=" + UPDATED_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByNumberOfRecurrencesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is greater than DEFAULT_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldNotBeFound("numberOfRecurrences.greaterThan=" + DEFAULT_NUMBER_OF_RECURRENCES);

        // Get all the amortizationRecurrenceList where numberOfRecurrences is greater than SMALLER_NUMBER_OF_RECURRENCES
        defaultAmortizationRecurrenceShouldBeFound("numberOfRecurrences.greaterThan=" + SMALLER_NUMBER_OF_RECURRENCES);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars equals to DEFAULT_PARTICULARS
        defaultAmortizationRecurrenceShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the amortizationRecurrenceList where particulars equals to UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars not equals to DEFAULT_PARTICULARS
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the amortizationRecurrenceList where particulars not equals to UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the amortizationRecurrenceList where particulars equals to UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars is not null
        defaultAmortizationRecurrenceShouldBeFound("particulars.specified=true");

        // Get all the amortizationRecurrenceList where particulars is null
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsContainsSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars contains DEFAULT_PARTICULARS
        defaultAmortizationRecurrenceShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the amortizationRecurrenceList where particulars contains UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where particulars does not contain DEFAULT_PARTICULARS
        defaultAmortizationRecurrenceShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the amortizationRecurrenceList where particulars does not contain UPDATED_PARTICULARS
        defaultAmortizationRecurrenceShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isActive equals to DEFAULT_IS_ACTIVE
        defaultAmortizationRecurrenceShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the amortizationRecurrenceList where isActive equals to UPDATED_IS_ACTIVE
        defaultAmortizationRecurrenceShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultAmortizationRecurrenceShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the amortizationRecurrenceList where isActive not equals to UPDATED_IS_ACTIVE
        defaultAmortizationRecurrenceShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultAmortizationRecurrenceShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the amortizationRecurrenceList where isActive equals to UPDATED_IS_ACTIVE
        defaultAmortizationRecurrenceShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isActive is not null
        defaultAmortizationRecurrenceShouldBeFound("isActive.specified=true");

        // Get all the amortizationRecurrenceList where isActive is null
        defaultAmortizationRecurrenceShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsOverWrittenIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isOverWritten equals to DEFAULT_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldBeFound("isOverWritten.equals=" + DEFAULT_IS_OVER_WRITTEN);

        // Get all the amortizationRecurrenceList where isOverWritten equals to UPDATED_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldNotBeFound("isOverWritten.equals=" + UPDATED_IS_OVER_WRITTEN);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsOverWrittenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isOverWritten not equals to DEFAULT_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldNotBeFound("isOverWritten.notEquals=" + DEFAULT_IS_OVER_WRITTEN);

        // Get all the amortizationRecurrenceList where isOverWritten not equals to UPDATED_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldBeFound("isOverWritten.notEquals=" + UPDATED_IS_OVER_WRITTEN);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsOverWrittenIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isOverWritten in DEFAULT_IS_OVER_WRITTEN or UPDATED_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldBeFound("isOverWritten.in=" + DEFAULT_IS_OVER_WRITTEN + "," + UPDATED_IS_OVER_WRITTEN);

        // Get all the amortizationRecurrenceList where isOverWritten equals to UPDATED_IS_OVER_WRITTEN
        defaultAmortizationRecurrenceShouldNotBeFound("isOverWritten.in=" + UPDATED_IS_OVER_WRITTEN);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByIsOverWrittenIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where isOverWritten is not null
        defaultAmortizationRecurrenceShouldBeFound("isOverWritten.specified=true");

        // Get all the amortizationRecurrenceList where isOverWritten is null
        defaultAmortizationRecurrenceShouldNotBeFound("isOverWritten.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation equals to DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.equals=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation equals to UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.equals=" + UPDATED_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation not equals to DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.notEquals=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation not equals to UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.notEquals=" + UPDATED_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation in DEFAULT_TIME_OF_INSTALLATION or UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound(
            "timeOfInstallation.in=" + DEFAULT_TIME_OF_INSTALLATION + "," + UPDATED_TIME_OF_INSTALLATION
        );

        // Get all the amortizationRecurrenceList where timeOfInstallation equals to UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.in=" + UPDATED_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation is not null
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.specified=true");

        // Get all the amortizationRecurrenceList where timeOfInstallation is null
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation is greater than or equal to DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.greaterThanOrEqual=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation is greater than or equal to UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.greaterThanOrEqual=" + UPDATED_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation is less than or equal to DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.lessThanOrEqual=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation is less than or equal to SMALLER_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.lessThanOrEqual=" + SMALLER_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation is less than DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.lessThan=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation is less than UPDATED_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.lessThan=" + UPDATED_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByTimeOfInstallationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where timeOfInstallation is greater than DEFAULT_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldNotBeFound("timeOfInstallation.greaterThan=" + DEFAULT_TIME_OF_INSTALLATION);

        // Get all the amortizationRecurrenceList where timeOfInstallation is greater than SMALLER_TIME_OF_INSTALLATION
        defaultAmortizationRecurrenceShouldBeFound("timeOfInstallation.greaterThan=" + SMALLER_TIME_OF_INSTALLATION);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByRecurrenceGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where recurrenceGuid equals to DEFAULT_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldBeFound("recurrenceGuid.equals=" + DEFAULT_RECURRENCE_GUID);

        // Get all the amortizationRecurrenceList where recurrenceGuid equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("recurrenceGuid.equals=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByRecurrenceGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where recurrenceGuid not equals to DEFAULT_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("recurrenceGuid.notEquals=" + DEFAULT_RECURRENCE_GUID);

        // Get all the amortizationRecurrenceList where recurrenceGuid not equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldBeFound("recurrenceGuid.notEquals=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByRecurrenceGuidIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where recurrenceGuid in DEFAULT_RECURRENCE_GUID or UPDATED_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldBeFound("recurrenceGuid.in=" + DEFAULT_RECURRENCE_GUID + "," + UPDATED_RECURRENCE_GUID);

        // Get all the amortizationRecurrenceList where recurrenceGuid equals to UPDATED_RECURRENCE_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("recurrenceGuid.in=" + UPDATED_RECURRENCE_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByRecurrenceGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where recurrenceGuid is not null
        defaultAmortizationRecurrenceShouldBeFound("recurrenceGuid.specified=true");

        // Get all the amortizationRecurrenceList where recurrenceGuid is null
        defaultAmortizationRecurrenceShouldNotBeFound("recurrenceGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPrepaymentAccountGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid equals to DEFAULT_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldBeFound("prepaymentAccountGuid.equals=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("prepaymentAccountGuid.equals=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPrepaymentAccountGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid not equals to DEFAULT_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("prepaymentAccountGuid.notEquals=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid not equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldBeFound("prepaymentAccountGuid.notEquals=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPrepaymentAccountGuidIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid in DEFAULT_PREPAYMENT_ACCOUNT_GUID or UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldBeFound(
            "prepaymentAccountGuid.in=" + DEFAULT_PREPAYMENT_ACCOUNT_GUID + "," + UPDATED_PREPAYMENT_ACCOUNT_GUID
        );

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid equals to UPDATED_PREPAYMENT_ACCOUNT_GUID
        defaultAmortizationRecurrenceShouldNotBeFound("prepaymentAccountGuid.in=" + UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPrepaymentAccountGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid is not null
        defaultAmortizationRecurrenceShouldBeFound("prepaymentAccountGuid.specified=true");

        // Get all the amortizationRecurrenceList where prepaymentAccountGuid is null
        defaultAmortizationRecurrenceShouldNotBeFound("prepaymentAccountGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
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
        amortizationRecurrence.addPlaceholder(placeholder);
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        Long placeholderId = placeholder.getId();

        // Get all the amortizationRecurrenceList where placeholder equals to placeholderId
        defaultAmortizationRecurrenceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the amortizationRecurrenceList where placeholder equals to (placeholderId + 1)
        defaultAmortizationRecurrenceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        PrepaymentMapping parameters;
        if (TestUtil.findAll(em, PrepaymentMapping.class).isEmpty()) {
            parameters = PrepaymentMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, PrepaymentMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        amortizationRecurrence.addParameters(parameters);
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        Long parametersId = parameters.getId();

        // Get all the amortizationRecurrenceList where parameters equals to parametersId
        defaultAmortizationRecurrenceShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the amortizationRecurrenceList where parameters equals to (parametersId + 1)
        defaultAmortizationRecurrenceShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByApplicationParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
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
        amortizationRecurrence.addApplicationParameters(applicationParameters);
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        Long applicationParametersId = applicationParameters.getId();

        // Get all the amortizationRecurrenceList where applicationParameters equals to applicationParametersId
        defaultAmortizationRecurrenceShouldBeFound("applicationParametersId.equals=" + applicationParametersId);

        // Get all the amortizationRecurrenceList where applicationParameters equals to (applicationParametersId + 1)
        defaultAmortizationRecurrenceShouldNotBeFound("applicationParametersId.equals=" + (applicationParametersId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByDepreciationMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        em.persist(depreciationMethod);
        em.flush();
        amortizationRecurrence.setDepreciationMethod(depreciationMethod);
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        Long depreciationMethodId = depreciationMethod.getId();

        // Get all the amortizationRecurrenceList where depreciationMethod equals to depreciationMethodId
        defaultAmortizationRecurrenceShouldBeFound("depreciationMethodId.equals=" + depreciationMethodId);

        // Get all the amortizationRecurrenceList where depreciationMethod equals to (depreciationMethodId + 1)
        defaultAmortizationRecurrenceShouldNotBeFound("depreciationMethodId.equals=" + (depreciationMethodId + 1));
    }

    @Test
    @Transactional
    void getAllAmortizationRecurrencesByPrepaymentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
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
        amortizationRecurrence.setPrepaymentAccount(prepaymentAccount);
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        Long prepaymentAccountId = prepaymentAccount.getId();

        // Get all the amortizationRecurrenceList where prepaymentAccount equals to prepaymentAccountId
        defaultAmortizationRecurrenceShouldBeFound("prepaymentAccountId.equals=" + prepaymentAccountId);

        // Get all the amortizationRecurrenceList where prepaymentAccount equals to (prepaymentAccountId + 1)
        defaultAmortizationRecurrenceShouldNotBeFound("prepaymentAccountId.equals=" + (prepaymentAccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmortizationRecurrenceShouldBeFound(String filter) throws Exception {
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationRecurrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstAmortizationDate").value(hasItem(DEFAULT_FIRST_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amortizationFrequency").value(hasItem(DEFAULT_AMORTIZATION_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].numberOfRecurrences").value(hasItem(DEFAULT_NUMBER_OF_RECURRENCES)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isOverWritten").value(hasItem(DEFAULT_IS_OVER_WRITTEN.booleanValue())))
            .andExpect(jsonPath("$.[*].timeOfInstallation").value(hasItem(sameInstant(DEFAULT_TIME_OF_INSTALLATION))))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())));

        // Check, that the count call also returns 1
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmortizationRecurrenceShouldNotBeFound(String filter) throws Exception {
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmortizationRecurrence() throws Exception {
        // Get the amortizationRecurrence
        restAmortizationRecurrenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmortizationRecurrence() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();

        // Update the amortizationRecurrence
        AmortizationRecurrence updatedAmortizationRecurrence = amortizationRecurrenceRepository
            .findById(amortizationRecurrence.getId())
            .get();
        // Disconnect from session so that the updates on updatedAmortizationRecurrence are not directly saved in db
        em.detach(updatedAmortizationRecurrence);
        updatedAmortizationRecurrence
            .firstAmortizationDate(UPDATED_FIRST_AMORTIZATION_DATE)
            .amortizationFrequency(UPDATED_AMORTIZATION_FREQUENCY)
            .numberOfRecurrences(UPDATED_NUMBER_OF_RECURRENCES)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .particulars(UPDATED_PARTICULARS)
            .isActive(UPDATED_IS_ACTIVE)
            .isOverWritten(UPDATED_IS_OVER_WRITTEN)
            .timeOfInstallation(UPDATED_TIME_OF_INSTALLATION)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID);
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(updatedAmortizationRecurrence);

        restAmortizationRecurrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationRecurrenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationRecurrence testAmortizationRecurrence = amortizationRecurrenceList.get(amortizationRecurrenceList.size() - 1);
        assertThat(testAmortizationRecurrence.getFirstAmortizationDate()).isEqualTo(UPDATED_FIRST_AMORTIZATION_DATE);
        assertThat(testAmortizationRecurrence.getAmortizationFrequency()).isEqualTo(UPDATED_AMORTIZATION_FREQUENCY);
        assertThat(testAmortizationRecurrence.getNumberOfRecurrences()).isEqualTo(UPDATED_NUMBER_OF_RECURRENCES);
        assertThat(testAmortizationRecurrence.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAmortizationRecurrence.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testAmortizationRecurrence.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testAmortizationRecurrence.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAmortizationRecurrence.getIsOverWritten()).isEqualTo(UPDATED_IS_OVER_WRITTEN);
        assertThat(testAmortizationRecurrence.getTimeOfInstallation()).isEqualTo(UPDATED_TIME_OF_INSTALLATION);
        assertThat(testAmortizationRecurrence.getRecurrenceGuid()).isEqualTo(UPDATED_RECURRENCE_GUID);
        assertThat(testAmortizationRecurrence.getPrepaymentAccountGuid()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT_GUID);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository).save(testAmortizationRecurrence);
    }

    @Test
    @Transactional
    void putNonExistingAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amortizationRecurrenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void partialUpdateAmortizationRecurrenceWithPatch() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();

        // Update the amortizationRecurrence using partial update
        AmortizationRecurrence partialUpdatedAmortizationRecurrence = new AmortizationRecurrence();
        partialUpdatedAmortizationRecurrence.setId(amortizationRecurrence.getId());

        partialUpdatedAmortizationRecurrence
            .numberOfRecurrences(UPDATED_NUMBER_OF_RECURRENCES)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restAmortizationRecurrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationRecurrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationRecurrence))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationRecurrence testAmortizationRecurrence = amortizationRecurrenceList.get(amortizationRecurrenceList.size() - 1);
        assertThat(testAmortizationRecurrence.getFirstAmortizationDate()).isEqualTo(DEFAULT_FIRST_AMORTIZATION_DATE);
        assertThat(testAmortizationRecurrence.getAmortizationFrequency()).isEqualTo(DEFAULT_AMORTIZATION_FREQUENCY);
        assertThat(testAmortizationRecurrence.getNumberOfRecurrences()).isEqualTo(UPDATED_NUMBER_OF_RECURRENCES);
        assertThat(testAmortizationRecurrence.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAmortizationRecurrence.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testAmortizationRecurrence.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testAmortizationRecurrence.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAmortizationRecurrence.getIsOverWritten()).isEqualTo(DEFAULT_IS_OVER_WRITTEN);
        assertThat(testAmortizationRecurrence.getTimeOfInstallation()).isEqualTo(DEFAULT_TIME_OF_INSTALLATION);
        assertThat(testAmortizationRecurrence.getRecurrenceGuid()).isEqualTo(DEFAULT_RECURRENCE_GUID);
        assertThat(testAmortizationRecurrence.getPrepaymentAccountGuid()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void fullUpdateAmortizationRecurrenceWithPatch() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();

        // Update the amortizationRecurrence using partial update
        AmortizationRecurrence partialUpdatedAmortizationRecurrence = new AmortizationRecurrence();
        partialUpdatedAmortizationRecurrence.setId(amortizationRecurrence.getId());

        partialUpdatedAmortizationRecurrence
            .firstAmortizationDate(UPDATED_FIRST_AMORTIZATION_DATE)
            .amortizationFrequency(UPDATED_AMORTIZATION_FREQUENCY)
            .numberOfRecurrences(UPDATED_NUMBER_OF_RECURRENCES)
            .notes(UPDATED_NOTES)
            .notesContentType(UPDATED_NOTES_CONTENT_TYPE)
            .particulars(UPDATED_PARTICULARS)
            .isActive(UPDATED_IS_ACTIVE)
            .isOverWritten(UPDATED_IS_OVER_WRITTEN)
            .timeOfInstallation(UPDATED_TIME_OF_INSTALLATION)
            .recurrenceGuid(UPDATED_RECURRENCE_GUID)
            .prepaymentAccountGuid(UPDATED_PREPAYMENT_ACCOUNT_GUID);

        restAmortizationRecurrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmortizationRecurrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmortizationRecurrence))
            )
            .andExpect(status().isOk());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);
        AmortizationRecurrence testAmortizationRecurrence = amortizationRecurrenceList.get(amortizationRecurrenceList.size() - 1);
        assertThat(testAmortizationRecurrence.getFirstAmortizationDate()).isEqualTo(UPDATED_FIRST_AMORTIZATION_DATE);
        assertThat(testAmortizationRecurrence.getAmortizationFrequency()).isEqualTo(UPDATED_AMORTIZATION_FREQUENCY);
        assertThat(testAmortizationRecurrence.getNumberOfRecurrences()).isEqualTo(UPDATED_NUMBER_OF_RECURRENCES);
        assertThat(testAmortizationRecurrence.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAmortizationRecurrence.getNotesContentType()).isEqualTo(UPDATED_NOTES_CONTENT_TYPE);
        assertThat(testAmortizationRecurrence.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testAmortizationRecurrence.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAmortizationRecurrence.getIsOverWritten()).isEqualTo(UPDATED_IS_OVER_WRITTEN);
        assertThat(testAmortizationRecurrence.getTimeOfInstallation()).isEqualTo(UPDATED_TIME_OF_INSTALLATION);
        assertThat(testAmortizationRecurrence.getRecurrenceGuid()).isEqualTo(UPDATED_RECURRENCE_GUID);
        assertThat(testAmortizationRecurrence.getPrepaymentAccountGuid()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT_GUID);
    }

    @Test
    @Transactional
    void patchNonExistingAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amortizationRecurrenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmortizationRecurrence() throws Exception {
        int databaseSizeBeforeUpdate = amortizationRecurrenceRepository.findAll().size();
        amortizationRecurrence.setId(count.incrementAndGet());

        // Create the AmortizationRecurrence
        AmortizationRecurrenceDTO amortizationRecurrenceDTO = amortizationRecurrenceMapper.toDto(amortizationRecurrence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmortizationRecurrenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amortizationRecurrenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AmortizationRecurrence in the database
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(0)).save(amortizationRecurrence);
    }

    @Test
    @Transactional
    void deleteAmortizationRecurrence() throws Exception {
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);

        int databaseSizeBeforeDelete = amortizationRecurrenceRepository.findAll().size();

        // Delete the amortizationRecurrence
        restAmortizationRecurrenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, amortizationRecurrence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AmortizationRecurrence> amortizationRecurrenceList = amortizationRecurrenceRepository.findAll();
        assertThat(amortizationRecurrenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AmortizationRecurrence in Elasticsearch
        verify(mockAmortizationRecurrenceSearchRepository, times(1)).deleteById(amortizationRecurrence.getId());
    }

    @Test
    @Transactional
    void searchAmortizationRecurrence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        amortizationRecurrenceRepository.saveAndFlush(amortizationRecurrence);
        when(mockAmortizationRecurrenceSearchRepository.search("id:" + amortizationRecurrence.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amortizationRecurrence), PageRequest.of(0, 1), 1));

        // Search the amortizationRecurrence
        restAmortizationRecurrenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + amortizationRecurrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationRecurrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstAmortizationDate").value(hasItem(DEFAULT_FIRST_AMORTIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amortizationFrequency").value(hasItem(DEFAULT_AMORTIZATION_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].numberOfRecurrences").value(hasItem(DEFAULT_NUMBER_OF_RECURRENCES)))
            .andExpect(jsonPath("$.[*].notesContentType").value(hasItem(DEFAULT_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTES))))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isOverWritten").value(hasItem(DEFAULT_IS_OVER_WRITTEN.booleanValue())))
            .andExpect(jsonPath("$.[*].timeOfInstallation").value(hasItem(sameInstant(DEFAULT_TIME_OF_INSTALLATION))))
            .andExpect(jsonPath("$.[*].recurrenceGuid").value(hasItem(DEFAULT_RECURRENCE_GUID.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAccountGuid").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_GUID.toString())));
    }
}
