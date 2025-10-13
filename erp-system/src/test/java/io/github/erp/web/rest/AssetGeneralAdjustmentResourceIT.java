package io.github.erp.web.rest;

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

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.AssetGeneralAdjustmentRepository;
import io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepository;
import io.github.erp.service.criteria.AssetGeneralAdjustmentCriteria;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import io.github.erp.service.mapper.AssetGeneralAdjustmentMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link AssetGeneralAdjustmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetGeneralAdjustmentResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEVALUATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEVALUATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEVALUATION_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_ADJUSTMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADJUSTMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ADJUSTMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_TIME_OF_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_ADJUSTMENT_REFERENCE_ID = UUID.randomUUID();
    private static final UUID UPDATED_ADJUSTMENT_REFERENCE_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/asset-general-adjustments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-general-adjustments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository;

    @Autowired
    private AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetGeneralAdjustmentSearchRepository mockAssetGeneralAdjustmentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetGeneralAdjustmentMockMvc;

    private AssetGeneralAdjustment assetGeneralAdjustment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetGeneralAdjustment createEntity(EntityManager em) {
        AssetGeneralAdjustment assetGeneralAdjustment = new AssetGeneralAdjustment()
            .description(DEFAULT_DESCRIPTION)
            .devaluationAmount(DEFAULT_DEVALUATION_AMOUNT)
            .adjustmentDate(DEFAULT_ADJUSTMENT_DATE)
            .timeOfCreation(DEFAULT_TIME_OF_CREATION)
            .adjustmentReferenceId(DEFAULT_ADJUSTMENT_REFERENCE_ID);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetGeneralAdjustment.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetGeneralAdjustment.setAssetRegistration(assetRegistration);
        return assetGeneralAdjustment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetGeneralAdjustment createUpdatedEntity(EntityManager em) {
        AssetGeneralAdjustment assetGeneralAdjustment = new AssetGeneralAdjustment()
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .adjustmentDate(UPDATED_ADJUSTMENT_DATE)
            .timeOfCreation(UPDATED_TIME_OF_CREATION)
            .adjustmentReferenceId(UPDATED_ADJUSTMENT_REFERENCE_ID);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetGeneralAdjustment.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createUpdatedEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetGeneralAdjustment.setAssetRegistration(assetRegistration);
        return assetGeneralAdjustment;
    }

    @BeforeEach
    public void initTest() {
        assetGeneralAdjustment = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeCreate = assetGeneralAdjustmentRepository.findAll().size();
        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);
        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeCreate + 1);
        AssetGeneralAdjustment testAssetGeneralAdjustment = assetGeneralAdjustmentList.get(assetGeneralAdjustmentList.size() - 1);
        assertThat(testAssetGeneralAdjustment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetGeneralAdjustment.getDevaluationAmount()).isEqualByComparingTo(DEFAULT_DEVALUATION_AMOUNT);
        assertThat(testAssetGeneralAdjustment.getAdjustmentDate()).isEqualTo(DEFAULT_ADJUSTMENT_DATE);
        assertThat(testAssetGeneralAdjustment.getTimeOfCreation()).isEqualTo(DEFAULT_TIME_OF_CREATION);
        assertThat(testAssetGeneralAdjustment.getAdjustmentReferenceId()).isEqualTo(DEFAULT_ADJUSTMENT_REFERENCE_ID);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(1)).save(testAssetGeneralAdjustment);
    }

    @Test
    @Transactional
    void createAssetGeneralAdjustmentWithExistingId() throws Exception {
        // Create the AssetGeneralAdjustment with an existing ID
        assetGeneralAdjustment.setId(1L);
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        int databaseSizeBeforeCreate = assetGeneralAdjustmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void checkDevaluationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetGeneralAdjustmentRepository.findAll().size();
        // set the field null
        assetGeneralAdjustment.setDevaluationAmount(null);

        // Create the AssetGeneralAdjustment, which fails.
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdjustmentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetGeneralAdjustmentRepository.findAll().size();
        // set the field null
        assetGeneralAdjustment.setAdjustmentDate(null);

        // Create the AssetGeneralAdjustment, which fails.
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetGeneralAdjustmentRepository.findAll().size();
        // set the field null
        assetGeneralAdjustment.setTimeOfCreation(null);

        // Create the AssetGeneralAdjustment, which fails.
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdjustmentReferenceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetGeneralAdjustmentRepository.findAll().size();
        // set the field null
        assetGeneralAdjustment.setAdjustmentReferenceId(null);

        // Create the AssetGeneralAdjustment, which fails.
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustments() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetGeneralAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].adjustmentDate").value(hasItem(DEFAULT_ADJUSTMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))))
            .andExpect(jsonPath("$.[*].adjustmentReferenceId").value(hasItem(DEFAULT_ADJUSTMENT_REFERENCE_ID.toString())));
    }

    @Test
    @Transactional
    void getAssetGeneralAdjustment() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get the assetGeneralAdjustment
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL_ID, assetGeneralAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetGeneralAdjustment.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.devaluationAmount").value(sameNumber(DEFAULT_DEVALUATION_AMOUNT)))
            .andExpect(jsonPath("$.adjustmentDate").value(DEFAULT_ADJUSTMENT_DATE.toString()))
            .andExpect(jsonPath("$.timeOfCreation").value(sameInstant(DEFAULT_TIME_OF_CREATION)))
            .andExpect(jsonPath("$.adjustmentReferenceId").value(DEFAULT_ADJUSTMENT_REFERENCE_ID.toString()));
    }

    @Test
    @Transactional
    void getAssetGeneralAdjustmentsByIdFiltering() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        Long id = assetGeneralAdjustment.getId();

        defaultAssetGeneralAdjustmentShouldBeFound("id.equals=" + id);
        defaultAssetGeneralAdjustmentShouldNotBeFound("id.notEquals=" + id);

        defaultAssetGeneralAdjustmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetGeneralAdjustmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetGeneralAdjustmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetGeneralAdjustmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description equals to DEFAULT_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetGeneralAdjustmentList where description equals to UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetGeneralAdjustmentList where description not equals to UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetGeneralAdjustmentList where description equals to UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description is not null
        defaultAssetGeneralAdjustmentShouldBeFound("description.specified=true");

        // Get all the assetGeneralAdjustmentList where description is null
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description contains DEFAULT_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetGeneralAdjustmentList where description contains UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetGeneralAdjustmentList where description does not contain UPDATED_DESCRIPTION
        defaultAssetGeneralAdjustmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount equals to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.equals=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.equals=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount not equals to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.notEquals=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount not equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.notEquals=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount in DEFAULT_DEVALUATION_AMOUNT or UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.in=" + DEFAULT_DEVALUATION_AMOUNT + "," + UPDATED_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount equals to UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.in=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is not null
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.specified=true");

        // Get all the assetGeneralAdjustmentList where devaluationAmount is null
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is greater than or equal to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.greaterThanOrEqual=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is greater than or equal to UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.greaterThanOrEqual=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is less than or equal to DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.lessThanOrEqual=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is less than or equal to SMALLER_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.lessThanOrEqual=" + SMALLER_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is less than DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.lessThan=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is less than UPDATED_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.lessThan=" + UPDATED_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByDevaluationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is greater than DEFAULT_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldNotBeFound("devaluationAmount.greaterThan=" + DEFAULT_DEVALUATION_AMOUNT);

        // Get all the assetGeneralAdjustmentList where devaluationAmount is greater than SMALLER_DEVALUATION_AMOUNT
        defaultAssetGeneralAdjustmentShouldBeFound("devaluationAmount.greaterThan=" + SMALLER_DEVALUATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate equals to DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.equals=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate equals to UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.equals=" + UPDATED_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate not equals to DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.notEquals=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate not equals to UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.notEquals=" + UPDATED_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate in DEFAULT_ADJUSTMENT_DATE or UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.in=" + DEFAULT_ADJUSTMENT_DATE + "," + UPDATED_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate equals to UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.in=" + UPDATED_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is not null
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.specified=true");

        // Get all the assetGeneralAdjustmentList where adjustmentDate is null
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is greater than or equal to DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.greaterThanOrEqual=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is greater than or equal to UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.greaterThanOrEqual=" + UPDATED_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is less than or equal to DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.lessThanOrEqual=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is less than or equal to SMALLER_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.lessThanOrEqual=" + SMALLER_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is less than DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.lessThan=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is less than UPDATED_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.lessThan=" + UPDATED_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is greater than DEFAULT_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentDate.greaterThan=" + DEFAULT_ADJUSTMENT_DATE);

        // Get all the assetGeneralAdjustmentList where adjustmentDate is greater than SMALLER_ADJUSTMENT_DATE
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentDate.greaterThan=" + SMALLER_ADJUSTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation equals to DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.equals=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation equals to UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.equals=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation not equals to DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.notEquals=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation not equals to UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.notEquals=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsInShouldWork() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation in DEFAULT_TIME_OF_CREATION or UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.in=" + DEFAULT_TIME_OF_CREATION + "," + UPDATED_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation equals to UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.in=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is not null
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.specified=true");

        // Get all the assetGeneralAdjustmentList where timeOfCreation is null
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is greater than or equal to DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.greaterThanOrEqual=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is greater than or equal to UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.greaterThanOrEqual=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is less than or equal to DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.lessThanOrEqual=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is less than or equal to SMALLER_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.lessThanOrEqual=" + SMALLER_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is less than DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.lessThan=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is less than UPDATED_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.lessThan=" + UPDATED_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByTimeOfCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is greater than DEFAULT_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldNotBeFound("timeOfCreation.greaterThan=" + DEFAULT_TIME_OF_CREATION);

        // Get all the assetGeneralAdjustmentList where timeOfCreation is greater than SMALLER_TIME_OF_CREATION
        defaultAssetGeneralAdjustmentShouldBeFound("timeOfCreation.greaterThan=" + SMALLER_TIME_OF_CREATION);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentReferenceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId equals to DEFAULT_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentReferenceId.equals=" + DEFAULT_ADJUSTMENT_REFERENCE_ID);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId equals to UPDATED_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentReferenceId.equals=" + UPDATED_ADJUSTMENT_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentReferenceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId not equals to DEFAULT_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentReferenceId.notEquals=" + DEFAULT_ADJUSTMENT_REFERENCE_ID);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId not equals to UPDATED_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentReferenceId.notEquals=" + UPDATED_ADJUSTMENT_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentReferenceIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId in DEFAULT_ADJUSTMENT_REFERENCE_ID or UPDATED_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldBeFound(
            "adjustmentReferenceId.in=" + DEFAULT_ADJUSTMENT_REFERENCE_ID + "," + UPDATED_ADJUSTMENT_REFERENCE_ID
        );

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId equals to UPDATED_ADJUSTMENT_REFERENCE_ID
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentReferenceId.in=" + UPDATED_ADJUSTMENT_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAdjustmentReferenceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId is not null
        defaultAssetGeneralAdjustmentShouldBeFound("adjustmentReferenceId.specified=true");

        // Get all the assetGeneralAdjustmentList where adjustmentReferenceId is null
        defaultAssetGeneralAdjustmentShouldNotBeFound("adjustmentReferenceId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByEffectivePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
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
        assetGeneralAdjustment.setEffectivePeriod(effectivePeriod);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long effectivePeriodId = effectivePeriod.getId();

        // Get all the assetGeneralAdjustmentList where effectivePeriod equals to effectivePeriodId
        defaultAssetGeneralAdjustmentShouldBeFound("effectivePeriodId.equals=" + effectivePeriodId);

        // Get all the assetGeneralAdjustmentList where effectivePeriod equals to (effectivePeriodId + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("effectivePeriodId.equals=" + (effectivePeriodId + 1));
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByAssetRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        em.persist(assetRegistration);
        em.flush();
        assetGeneralAdjustment.setAssetRegistration(assetRegistration);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long assetRegistrationId = assetRegistration.getId();

        // Get all the assetGeneralAdjustmentList where assetRegistration equals to assetRegistrationId
        defaultAssetGeneralAdjustmentShouldBeFound("assetRegistrationId.equals=" + assetRegistrationId);

        // Get all the assetGeneralAdjustmentList where assetRegistration equals to (assetRegistrationId + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("assetRegistrationId.equals=" + (assetRegistrationId + 1));
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
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
        assetGeneralAdjustment.setCreatedBy(createdBy);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long createdById = createdBy.getId();

        // Get all the assetGeneralAdjustmentList where createdBy equals to createdById
        defaultAssetGeneralAdjustmentShouldBeFound("createdById.equals=" + createdById);

        // Get all the assetGeneralAdjustmentList where createdBy equals to (createdById + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
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
        assetGeneralAdjustment.setLastModifiedBy(lastModifiedBy);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long lastModifiedById = lastModifiedBy.getId();

        // Get all the assetGeneralAdjustmentList where lastModifiedBy equals to lastModifiedById
        defaultAssetGeneralAdjustmentShouldBeFound("lastModifiedById.equals=" + lastModifiedById);

        // Get all the assetGeneralAdjustmentList where lastModifiedBy equals to (lastModifiedById + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("lastModifiedById.equals=" + (lastModifiedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
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
        assetGeneralAdjustment.setLastAccessedBy(lastAccessedBy);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the assetGeneralAdjustmentList where lastAccessedBy equals to lastAccessedById
        defaultAssetGeneralAdjustmentShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the assetGeneralAdjustmentList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetGeneralAdjustmentsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
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
        assetGeneralAdjustment.setPlaceholder(placeholder);
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        Long placeholderId = placeholder.getId();

        // Get all the assetGeneralAdjustmentList where placeholder equals to placeholderId
        defaultAssetGeneralAdjustmentShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetGeneralAdjustmentList where placeholder equals to (placeholderId + 1)
        defaultAssetGeneralAdjustmentShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetGeneralAdjustmentShouldBeFound(String filter) throws Exception {
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetGeneralAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].adjustmentDate").value(hasItem(DEFAULT_ADJUSTMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))))
            .andExpect(jsonPath("$.[*].adjustmentReferenceId").value(hasItem(DEFAULT_ADJUSTMENT_REFERENCE_ID.toString())));

        // Check, that the count call also returns 1
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetGeneralAdjustmentShouldNotBeFound(String filter) throws Exception {
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetGeneralAdjustment() throws Exception {
        // Get the assetGeneralAdjustment
        restAssetGeneralAdjustmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetGeneralAdjustment() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();

        // Update the assetGeneralAdjustment
        AssetGeneralAdjustment updatedAssetGeneralAdjustment = assetGeneralAdjustmentRepository
            .findById(assetGeneralAdjustment.getId())
            .get();
        // Disconnect from session so that the updates on updatedAssetGeneralAdjustment are not directly saved in db
        em.detach(updatedAssetGeneralAdjustment);
        updatedAssetGeneralAdjustment
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .adjustmentDate(UPDATED_ADJUSTMENT_DATE)
            .timeOfCreation(UPDATED_TIME_OF_CREATION)
            .adjustmentReferenceId(UPDATED_ADJUSTMENT_REFERENCE_ID);
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(updatedAssetGeneralAdjustment);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetGeneralAdjustmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);
        AssetGeneralAdjustment testAssetGeneralAdjustment = assetGeneralAdjustmentList.get(assetGeneralAdjustmentList.size() - 1);
        assertThat(testAssetGeneralAdjustment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetGeneralAdjustment.getDevaluationAmount()).isEqualTo(UPDATED_DEVALUATION_AMOUNT);
        assertThat(testAssetGeneralAdjustment.getAdjustmentDate()).isEqualTo(UPDATED_ADJUSTMENT_DATE);
        assertThat(testAssetGeneralAdjustment.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);
        assertThat(testAssetGeneralAdjustment.getAdjustmentReferenceId()).isEqualTo(UPDATED_ADJUSTMENT_REFERENCE_ID);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository).save(testAssetGeneralAdjustment);
    }

    @Test
    @Transactional
    void putNonExistingAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetGeneralAdjustmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void partialUpdateAssetGeneralAdjustmentWithPatch() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();

        // Update the assetGeneralAdjustment using partial update
        AssetGeneralAdjustment partialUpdatedAssetGeneralAdjustment = new AssetGeneralAdjustment();
        partialUpdatedAssetGeneralAdjustment.setId(assetGeneralAdjustment.getId());

        partialUpdatedAssetGeneralAdjustment.timeOfCreation(UPDATED_TIME_OF_CREATION);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetGeneralAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetGeneralAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);
        AssetGeneralAdjustment testAssetGeneralAdjustment = assetGeneralAdjustmentList.get(assetGeneralAdjustmentList.size() - 1);
        assertThat(testAssetGeneralAdjustment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetGeneralAdjustment.getDevaluationAmount()).isEqualByComparingTo(DEFAULT_DEVALUATION_AMOUNT);
        assertThat(testAssetGeneralAdjustment.getAdjustmentDate()).isEqualTo(DEFAULT_ADJUSTMENT_DATE);
        assertThat(testAssetGeneralAdjustment.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);
        assertThat(testAssetGeneralAdjustment.getAdjustmentReferenceId()).isEqualTo(DEFAULT_ADJUSTMENT_REFERENCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateAssetGeneralAdjustmentWithPatch() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();

        // Update the assetGeneralAdjustment using partial update
        AssetGeneralAdjustment partialUpdatedAssetGeneralAdjustment = new AssetGeneralAdjustment();
        partialUpdatedAssetGeneralAdjustment.setId(assetGeneralAdjustment.getId());

        partialUpdatedAssetGeneralAdjustment
            .description(UPDATED_DESCRIPTION)
            .devaluationAmount(UPDATED_DEVALUATION_AMOUNT)
            .adjustmentDate(UPDATED_ADJUSTMENT_DATE)
            .timeOfCreation(UPDATED_TIME_OF_CREATION)
            .adjustmentReferenceId(UPDATED_ADJUSTMENT_REFERENCE_ID);

        restAssetGeneralAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetGeneralAdjustment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetGeneralAdjustment))
            )
            .andExpect(status().isOk());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);
        AssetGeneralAdjustment testAssetGeneralAdjustment = assetGeneralAdjustmentList.get(assetGeneralAdjustmentList.size() - 1);
        assertThat(testAssetGeneralAdjustment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetGeneralAdjustment.getDevaluationAmount()).isEqualByComparingTo(UPDATED_DEVALUATION_AMOUNT);
        assertThat(testAssetGeneralAdjustment.getAdjustmentDate()).isEqualTo(UPDATED_ADJUSTMENT_DATE);
        assertThat(testAssetGeneralAdjustment.getTimeOfCreation()).isEqualTo(UPDATED_TIME_OF_CREATION);
        assertThat(testAssetGeneralAdjustment.getAdjustmentReferenceId()).isEqualTo(UPDATED_ADJUSTMENT_REFERENCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetGeneralAdjustmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetGeneralAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = assetGeneralAdjustmentRepository.findAll().size();
        assetGeneralAdjustment.setId(count.incrementAndGet());

        // Create the AssetGeneralAdjustment
        AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO = assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetGeneralAdjustmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetGeneralAdjustmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetGeneralAdjustment in the database
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(0)).save(assetGeneralAdjustment);
    }

    @Test
    @Transactional
    void deleteAssetGeneralAdjustment() throws Exception {
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);

        int databaseSizeBeforeDelete = assetGeneralAdjustmentRepository.findAll().size();

        // Delete the assetGeneralAdjustment
        restAssetGeneralAdjustmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetGeneralAdjustment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetGeneralAdjustment> assetGeneralAdjustmentList = assetGeneralAdjustmentRepository.findAll();
        assertThat(assetGeneralAdjustmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetGeneralAdjustment in Elasticsearch
        verify(mockAssetGeneralAdjustmentSearchRepository, times(1)).deleteById(assetGeneralAdjustment.getId());
    }

    @Test
    @Transactional
    void searchAssetGeneralAdjustment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetGeneralAdjustmentRepository.saveAndFlush(assetGeneralAdjustment);
        when(mockAssetGeneralAdjustmentSearchRepository.search("id:" + assetGeneralAdjustment.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetGeneralAdjustment), PageRequest.of(0, 1), 1));

        // Search the assetGeneralAdjustment
        restAssetGeneralAdjustmentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetGeneralAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetGeneralAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].devaluationAmount").value(hasItem(sameNumber(DEFAULT_DEVALUATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].adjustmentDate").value(hasItem(DEFAULT_ADJUSTMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].timeOfCreation").value(hasItem(sameInstant(DEFAULT_TIME_OF_CREATION))))
            .andExpect(jsonPath("$.[*].adjustmentReferenceId").value(hasItem(DEFAULT_ADJUSTMENT_REFERENCE_ID.toString())));
    }
}
