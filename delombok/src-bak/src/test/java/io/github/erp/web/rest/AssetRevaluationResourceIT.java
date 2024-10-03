package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.AssetRevaluation;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.AssetRevaluationRepository;
import io.github.erp.repository.search.AssetRevaluationSearchRepository;
import io.github.erp.service.AssetRevaluationService;
import io.github.erp.service.criteria.AssetRevaluationCriteria;
import io.github.erp.service.dto.AssetRevaluationDTO;
import io.github.erp.service.mapper.AssetRevaluationMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link AssetRevaluationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetRevaluationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEVALUATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEVALUATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEVALUATION_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_REVALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVALUATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REVALUATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final UUID DEFAULT_REVALUATION_REFERENCE_ID = UUID.randomUUID();
    private static final UUID UPDATED_REVALUATION_REFERENCE_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/asset-revaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-revaluations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetRevaluationRepository assetRevaluationRepository;

    @Mock
    private AssetRevaluationRepository assetRevaluationRepositoryMock;

    @Autowired
    private AssetRevaluationMapper assetRevaluationMapper;

    @Mock
    private AssetRevaluationService assetRevaluationServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetRevaluationSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetRevaluationSearchRepository mockAssetRevaluationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetRevaluationMockMvc;

    private AssetRevaluation assetRevaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRevaluation createEntity(EntityManager em) {
        AssetRevaluation assetRevaluation = new AssetRevaluation()
            .description(DEFAULT_DESCRIPTION)
            .devaluationAmount(DEFAULT_DEVALUATION_AMOUNT)
            .revaluationDate(DEFAULT_REVALUATION_DATE)
            .revaluationReferenceId(DEFAULT_REVALUATION_REFERENCE_ID)
            .timeOfCreation(DEFAULT_TIME_OF_CREATION);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetRevaluation.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetRevaluation.setRevaluedAsset(assetRegistration);
        return assetRevaluation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRevaluation createUpdatedEntity(EntityManager em) {
        AssetRevaluation assetRevaluation = new AssetRevaluation()
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .revaluationDate(UPDATED_REVALUATION_DATE)
            .revaluationReferenceId(UPDATED_REVALUATION_REFERENCE_ID)
            .timeOfCreation(UPDATED_TIME_OF_CREATION);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetRevaluation.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createUpdatedEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetRevaluation.setRevaluedAsset(assetRegistration);
        return assetRevaluation;
    }

    @BeforeEach
    public void initTest() {
        assetRevaluation = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetRevaluation() throws Exception {
        int databaseSizeBeforeCreate = assetRevaluationRepository.findAll().size();
        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);
        restAssetRevaluationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeCreate + 1);
        AssetRevaluation testAssetRevaluation = assetRevaluationList.get(assetRevaluationList.size() - 1);
        assertThat(testAssetRevaluation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetRevaluation.getDevaluationAmount()).isEqualByComparingTo(DEFAULT_DEVALUATION_AMOUNT);
        assertThat(testAssetRevaluation.getRevaluationDate()).isEqualTo(DEFAULT_REVALUATION_DATE);
        assertThat(testAssetRevaluation.getRevaluationReferenceId()).isEqualTo(DEFAULT_REVALUATION_REFERENCE_ID);
        assertThat(testAssetRevaluation.getTimeOfCreation()).isEqualTo(DEFAULT_TIME_OF_CREATION);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(1)).save(testAssetRevaluation);
    }

    @Test
    @Transactional
    void createAssetRevaluationWithExistingId() throws Exception {
        // Create the AssetRevaluation with an existing ID
        assetRevaluation.setId(1L);
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        int databaseSizeBeforeCreate = assetRevaluationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetRevaluationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void checkDevaluationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRevaluationRepository.findAll().size();
        // set the field null
        assetRevaluation.setDevaluationAmount(null);

        // Create the AssetRevaluation, which fails.
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        restAssetRevaluationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRevaluationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRevaluationRepository.findAll().size();
        // set the field null
        assetRevaluation.setRevaluationDate(null);

        // Create the AssetRevaluation, which fails.
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        restAssetRevaluationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetRevaluations() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRevaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].revaluationDate").value(hasItem(DEFAULT_REVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].revaluationReferenceId").value(hasItem(DEFAULT_REVALUATION_REFERENCE_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetRevaluationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetRevaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetRevaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetRevaluationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetRevaluationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetRevaluationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetRevaluationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetRevaluationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetRevaluation() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get the assetRevaluation
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, assetRevaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetRevaluation.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.devaluationAmount").value(sameNumber(DEFAULT_DEVALUATION_AMOUNT)))
            .andExpect(jsonPath("$.revaluationDate").value(DEFAULT_REVALUATION_DATE.toString()))
            .andExpect(jsonPath("$.revaluationReferenceId").value(DEFAULT_REVALUATION_REFERENCE_ID.toString()))
            .andExpect(jsonPath("$.timeOfCreation").value(sameInstant(DEFAULT_TIME_OF_CREATION)));
    }

    @Test
    @Transactional
    void getAssetRevaluationsByIdFiltering() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        Long id = assetRevaluation.getId();

        defaultAssetRevaluationShouldBeFound("id.equals=" + id);
        defaultAssetRevaluationShouldNotBeFound("id.notEquals=" + id);

        defaultAssetRevaluationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetRevaluationShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetRevaluationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetRevaluationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description equals to DEFAULT_DESCRIPTION
        defaultAssetRevaluationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetRevaluationList where description equals to UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetRevaluationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetRevaluationList where description not equals to UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetRevaluationList where description equals to UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description is not null
        defaultAssetRevaluationShouldBeFound("description.specified=true");

        // Get all the assetRevaluationList where description is null
        defaultAssetRevaluationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description contains DEFAULT_DESCRIPTION
        defaultAssetRevaluationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetRevaluationList where description contains UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetRevaluationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetRevaluationList where description does not contain UPDATED_DESCRIPTION
        defaultAssetRevaluationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount equals to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.equals=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.equals=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount not equals to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.notEquals=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount not equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.notEquals=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount in DEFAULT_DEVALUATION_AMOUNT or UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.in=" + DEFAULT_DEVALUATION_AMOUNT + "," + UPDATED_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.in=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount is not null
        defaultAssetRevaluationShouldBeFound("devaluationAmount.specified=true");

        // Get all the assetRevaluationList where devaluationAmount is null
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount is greater than or equal to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.greaterThanOrEqual=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount is greater than or equal to UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.greaterThanOrEqual=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount is less than or equal to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.lessThanOrEqual=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount is less than or equal to SMALLER_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.lessThanOrEqual=" + SMALLER_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount is less than DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.lessThan=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount is less than UPDATED_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.lessThan=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByDevaluationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where devaluationAmount is greater than DEFAULT_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldNotBeFound("devaluationAmount.greaterThan=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetRevaluationList where devaluationAmount is greater than SMALLER_DEVALUATION_AMOUNT
        defaultAssetRevaluationShouldBeFound("devaluationAmount.greaterThan=" + SMALLER_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate equals to DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.equals=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate equals to UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.equals=" + UPDATED_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate not equals to DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.notEquals=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate not equals to UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.notEquals=" + UPDATED_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate in DEFAULT_REVALUATION_DATE or UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.in=" + DEFAULT_REVALUATION_DATE + "," + UPDATED_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate equals to UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.in=" + UPDATED_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate is not null
        defaultAssetRevaluationShouldBeFound("revaluationDate.specified=true");

        // Get all the assetRevaluationList where revaluationDate is null
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate is greater than or equal to DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.greaterThanOrEqual=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate is greater than or equal to UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.greaterThanOrEqual=" + UPDATED_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate is less than or equal to DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.lessThanOrEqual=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate is less than or equal to SMALLER_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.lessThanOrEqual=" + SMALLER_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate is less than DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.lessThan=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate is less than UPDATED_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.lessThan=" + UPDATED_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationDate is greater than DEFAULT_REVALUATION_DATE
        defaultAssetRevaluationShouldNotBeFound("revaluationDate.greaterThan=" + DEFAULT_REVALUATION_DATE);

        // Get all the assetRevaluationList where revaluationDate is greater than SMALLER_REVALUATION_DATE
        defaultAssetRevaluationShouldBeFound("revaluationDate.greaterThan=" + SMALLER_REVALUATION_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationReferenceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationReferenceId equals to DEFAULT_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldBeFound("revaluationReferenceId.equals=" + DEFAULT_REVALUATION_REFERENCE_ID);

        // Get all the assetRevaluationList where revaluationReferenceId equals to UPDATED_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldNotBeFound("revaluationReferenceId.equals=" + UPDATED_REVALUATION_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationReferenceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationReferenceId not equals to DEFAULT_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldNotBeFound("revaluationReferenceId.notEquals=" + DEFAULT_REVALUATION_REFERENCE_ID);

        // Get all the assetRevaluationList where revaluationReferenceId not equals to UPDATED_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldBeFound("revaluationReferenceId.notEquals=" + UPDATED_REVALUATION_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationReferenceIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationReferenceId in DEFAULT_REVALUATION_REFERENCE_ID or UPDATED_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldBeFound(
            "revaluationReferenceId.in=" + DEFAULT_REVALUATION_REFERENCE_ID + "," + UPDATED_REVALUATION_REFERENCE_ID
        );

        // Get all the assetRevaluationList where revaluationReferenceId equals to UPDATED_REVALUATION_REFERENCE_ID
        defaultAssetRevaluationShouldNotBeFound("revaluationReferenceId.in=" + UPDATED_REVALUATION_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluationReferenceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where revaluationReferenceId is not null
        defaultAssetRevaluationShouldBeFound("revaluationReferenceId.specified=true");

        // Get all the assetRevaluationList where revaluationReferenceId is null
        defaultAssetRevaluationShouldNotBeFound("revaluationReferenceId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation equals to DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.equals=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation equals to UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.equals=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation not equals to DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.notEquals=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation not equals to UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.notEquals=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsInShouldWork() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation in DEFAULT_TIME_OF_CREATION or UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.in=" + DEFAULT_TIME_OF_CREATION + "," + UPDATED_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation equals to UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.in=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation is not null
        defaultAssetRevaluationShouldBeFound("timeOfCreation.specified=true");

        // Get all the assetRevaluationList where timeOfCreation is null
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation is greater than or equal to DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.greaterThanOrEqual=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation is greater than or equal to UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.greaterThanOrEqual=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation is less than or equal to DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.lessThanOrEqual=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation is less than or equal to SMALLER_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.lessThanOrEqual=" + SMALLER_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation is less than DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.lessThan=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation is less than UPDATED_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.lessThan=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByTimeOfCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        // Get all the assetRevaluationList where timeOfCreation is greater than DEFAULT_TIME_OF_CREATION
        defaultAssetRevaluationShouldNotBeFound("timeOfCreation.greaterThan=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetRevaluationList where timeOfCreation is greater than SMALLER_TIME_OF_CREATION
        defaultAssetRevaluationShouldBeFound("timeOfCreation.greaterThan=" + SMALLER_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluerIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Dealer revaluer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            revaluer = DealerResourceIT.createEntity(em);
            em.persist(revaluer);
            em.flush();
        } else {
            revaluer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(revaluer);
        em.flush();
        assetRevaluation.setRevaluer(revaluer);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long revaluerId = revaluer.getId();

        // Get all the assetRevaluationList where revaluer equals to revaluerId
        defaultAssetRevaluationShouldBeFound("revaluerId.equals=" + revaluerId);

        // Get all the assetRevaluationList where revaluer equals to (revaluerId + 1)
        defaultAssetRevaluationShouldNotBeFound("revaluerId.equals=" + (revaluerId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
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
        assetRevaluation.setCreatedBy(createdBy);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long createdById = createdBy.getId();

        // Get all the assetRevaluationList where createdBy equals to createdById
        defaultAssetRevaluationShouldBeFound("createdById.equals=" + createdById);

        // Get all the assetRevaluationList where createdBy equals to (createdById + 1)
        defaultAssetRevaluationShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        ApplicationUser lastModifiedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastModifiedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastModifiedBy);
            em.flush();
        } else {
            lastModifiedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastModifiedBy);
        em.flush();
        assetRevaluation.setLastModifiedBy(lastModifiedBy);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long lastModifiedById = lastModifiedBy.getId();

        // Get all the assetRevaluationList where lastModifiedBy equals to lastModifiedById
        defaultAssetRevaluationShouldBeFound("lastModifiedById.equals=" + lastModifiedById);

        // Get all the assetRevaluationList where lastModifiedBy equals to (lastModifiedById + 1)
        defaultAssetRevaluationShouldNotBeFound("lastModifiedById.equals=" + (lastModifiedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        ApplicationUser lastAccessedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastAccessedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastAccessedBy);
            em.flush();
        } else {
            lastAccessedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastAccessedBy);
        em.flush();
        assetRevaluation.setLastAccessedBy(lastAccessedBy);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the assetRevaluationList where lastAccessedBy equals to lastAccessedById
        defaultAssetRevaluationShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the assetRevaluationList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultAssetRevaluationShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByEffectivePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        DepreciationPeriod effectivePeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            effectivePeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(effectivePeriod);
            em.flush();
        } else {
            effectivePeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(effectivePeriod);
        em.flush();
        assetRevaluation.setEffectivePeriod(effectivePeriod);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long effectivePeriodId = effectivePeriod.getId();

        // Get all the assetRevaluationList where effectivePeriod equals to effectivePeriodId
        defaultAssetRevaluationShouldBeFound("effectivePeriodId.equals=" + effectivePeriodId);

        // Get all the assetRevaluationList where effectivePeriod equals to (effectivePeriodId + 1)
        defaultAssetRevaluationShouldNotBeFound("effectivePeriodId.equals=" + (effectivePeriodId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByRevaluedAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        AssetRegistration revaluedAsset;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            revaluedAsset = AssetRegistrationResourceIT.createEntity(em);
            em.persist(revaluedAsset);
            em.flush();
        } else {
            revaluedAsset = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        em.persist(revaluedAsset);
        em.flush();
        assetRevaluation.setRevaluedAsset(revaluedAsset);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long revaluedAssetId = revaluedAsset.getId();

        // Get all the assetRevaluationList where revaluedAsset equals to revaluedAssetId
        defaultAssetRevaluationShouldBeFound("revaluedAssetId.equals=" + revaluedAssetId);

        // Get all the assetRevaluationList where revaluedAsset equals to (revaluedAssetId + 1)
        defaultAssetRevaluationShouldNotBeFound("revaluedAssetId.equals=" + (revaluedAssetId + 1));
    }

    @Test
    @Transactional
    void getAllAssetRevaluationsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
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
        assetRevaluation.addPlaceholder(placeholder);
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        Long placeholderId = placeholder.getId();

        // Get all the assetRevaluationList where placeholder equals to placeholderId
        defaultAssetRevaluationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetRevaluationList where placeholder equals to (placeholderId + 1)
        defaultAssetRevaluationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetRevaluationShouldBeFound(String filter) throws Exception {
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRevaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].revaluationDate").value(hasItem(DEFAULT_REVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].revaluationReferenceId").value(hasItem(DEFAULT_REVALUATION_REFERENCE_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))));

        // Check, that the count call also returns 1
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetRevaluationShouldNotBeFound(String filter) throws Exception {
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetRevaluation() throws Exception {
        // Get the assetRevaluation
        restAssetRevaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetRevaluation() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();

        // Update the assetRevaluation
        AssetRevaluation updatedAssetRevaluation = assetRevaluationRepository.findById(assetRevaluation.getId()).get();
        // Disconnect from session so that the updates on updatedAssetRevaluation are not directly saved in db
        em.detach(updatedAssetRevaluation);
        updatedAssetRevaluation
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .revaluationDate(UPDATED_REVALUATION_DATE)
            .revaluationReferenceId(UPDATED_REVALUATION_REFERENCE_ID)
            .timeOfCreation(UPDATED_TIME_OF_CREATION);
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(updatedAssetRevaluation);

        restAssetRevaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRevaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);
        AssetRevaluation testAssetRevaluation = assetRevaluationList.get(assetRevaluationList.size() - 1);
        assertThat(testAssetRevaluation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetRevaluation.getDevaluationAmount()).isEqualTo(UPDATED_DEVALUATION_AMOUNT);
        assertThat(testAssetRevaluation.getRevaluationDate()).isEqualTo(UPDATED_REVALUATION_DATE);
        assertThat(testAssetRevaluation.getRevaluationReferenceId()).isEqualTo(UPDATED_REVALUATION_REFERENCE_ID);
        assertThat(testAssetRevaluation.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository).save(testAssetRevaluation);
    }

    @Test
    @Transactional
    void putNonExistingAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRevaluationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void partialUpdateAssetRevaluationWithPatch() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();

        // Update the assetRevaluation using partial update
        AssetRevaluation partialUpdatedAssetRevaluation = new AssetRevaluation();
        partialUpdatedAssetRevaluation.setId(assetRevaluation.getId());

        partialUpdatedAssetRevaluation.revaluationDate(UPDATED_REVALUATION_DATE).timeOfCreation(UPDATED_TIME_OF_CREATION);

        restAssetRevaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRevaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetRevaluation))
            )
            .andExpect(status().isOk());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);
        AssetRevaluation testAssetRevaluation = assetRevaluationList.get(assetRevaluationList.size() - 1);
        assertThat(testAssetRevaluation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetRevaluation.getDevaluationAmount()).isEqualByComparingTo(DEFAULT_DEVALUATION_AMOUNT);
        assertThat(testAssetRevaluation.getRevaluationDate()).isEqualTo(UPDATED_REVALUATION_DATE);
        assertThat(testAssetRevaluation.getRevaluationReferenceId()).isEqualTo(DEFAULT_REVALUATION_REFERENCE_ID);
        assertThat(testAssetRevaluation.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateAssetRevaluationWithPatch() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();

        // Update the assetRevaluation using partial update
        AssetRevaluation partialUpdatedAssetRevaluation = new AssetRevaluation();
        partialUpdatedAssetRevaluation.setId(assetRevaluation.getId());

        partialUpdatedAssetRevaluation
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .revaluationDate(UPDATED_REVALUATION_DATE)
            .revaluationReferenceId(UPDATED_REVALUATION_REFERENCE_ID)
            .timeOfCreation(UPDATED_TIME_OF_CREATION);

        restAssetRevaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRevaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetRevaluation))
            )
            .andExpect(status().isOk());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);
        AssetRevaluation testAssetRevaluation = assetRevaluationList.get(assetRevaluationList.size() - 1);
        assertThat(testAssetRevaluation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetRevaluation.getDevaluationAmount()).isEqualByComparingTo(UPDATED_DEVALUATION_AMOUNT);
        assertThat(testAssetRevaluation.getRevaluationDate()).isEqualTo(UPDATED_REVALUATION_DATE);
        assertThat(testAssetRevaluation.getRevaluationReferenceId()).isEqualTo(UPDATED_REVALUATION_REFERENCE_ID);
        assertThat(testAssetRevaluation.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetRevaluationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetRevaluation() throws Exception {
        int databaseSizeBeforeUpdate = assetRevaluationRepository.findAll().size();
        assetRevaluation.setId(count.incrementAndGet());

        // Create the AssetRevaluation
        AssetRevaluationDTO assetRevaluationDTO = assetRevaluationMapper.toDto(assetRevaluation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRevaluationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetRevaluationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRevaluation in the database
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(0)).save(assetRevaluation);
    }

    @Test
    @Transactional
    void deleteAssetRevaluation() throws Exception {
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);

        int databaseSizeBeforeDelete = assetRevaluationRepository.findAll().size();

        // Delete the assetRevaluation
        restAssetRevaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetRevaluation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetRevaluation> assetRevaluationList = assetRevaluationRepository.findAll();
        assertThat(assetRevaluationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetRevaluation in Elasticsearch
        verify(mockAssetRevaluationSearchRepository, times(1)).deleteById(assetRevaluation.getId());
    }

    @Test
    @Transactional
    void searchAssetRevaluation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetRevaluationRepository.saveAndFlush(assetRevaluation);
        when(mockAssetRevaluationSearchRepository.search("id:" + assetRevaluation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetRevaluation), PageRequest.of(0, 1), 1));

        // Search the assetRevaluation
        restAssetRevaluationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetRevaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRevaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].revaluationDate").value(hasItem(DEFAULT_REVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].revaluationReferenceId").value(hasItem(DEFAULT_REVALUATION_REFERENCE_ID.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))));
    }
}
