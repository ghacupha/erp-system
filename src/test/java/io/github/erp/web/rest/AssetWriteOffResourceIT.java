package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.AssetWriteOff;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.AssetWriteOffRepository;
import io.github.erp.repository.search.AssetWriteOffSearchRepository;
import io.github.erp.service.AssetWriteOffService;
import io.github.erp.service.criteria.AssetWriteOffCriteria;
import io.github.erp.service.dto.AssetWriteOffDTO;
import io.github.erp.service.mapper.AssetWriteOffMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssetWriteOffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetWriteOffResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_WRITE_OFF_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_WRITE_OFF_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_WRITE_OFF_AMOUNT = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_WRITE_OFF_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WRITE_OFF_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_WRITE_OFF_DATE = LocalDate.ofEpochDay(-1L);

    private static final UUID DEFAULT_WRITE_OFF_REFERENCE_ID = UUID.randomUUID();
    private static final UUID UPDATED_WRITE_OFF_REFERENCE_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/asset-write-offs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/asset-write-offs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetWriteOffRepository assetWriteOffRepository;

    @Mock
    private AssetWriteOffRepository assetWriteOffRepositoryMock;

    @Autowired
    private AssetWriteOffMapper assetWriteOffMapper;

    @Mock
    private AssetWriteOffService assetWriteOffServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetWriteOffSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetWriteOffSearchRepository mockAssetWriteOffSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetWriteOffMockMvc;

    private AssetWriteOff assetWriteOff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetWriteOff createEntity(EntityManager em) {
        AssetWriteOff assetWriteOff = new AssetWriteOff()
            .description(DEFAULT_DESCRIPTION)
            .writeOffAmount(DEFAULT_WRITE_OFF_AMOUNT)
            .writeOffDate(DEFAULT_WRITE_OFF_DATE)
            .writeOffReferenceId(DEFAULT_WRITE_OFF_REFERENCE_ID);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetWriteOff.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetWriteOff.setAssetWrittenOff(assetRegistration);
        return assetWriteOff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetWriteOff createUpdatedEntity(EntityManager em) {
        AssetWriteOff assetWriteOff = new AssetWriteOff()
            .description(UPDATED_DESCRIPTION)
            .writeOffAmount(UPDATED_WRITE_OFF_AMOUNT)
            .writeOffDate(UPDATED_WRITE_OFF_DATE)
            .writeOffReferenceId(UPDATED_WRITE_OFF_REFERENCE_ID);
        // Add required entity
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        assetWriteOff.setEffectivePeriod(depreciationPeriod);
        // Add required entity
        AssetRegistration assetRegistration;
        if (TestUtil.findAll(em, AssetRegistration.class).isEmpty()) {
            assetRegistration = AssetRegistrationResourceIT.createUpdatedEntity(em);
            em.persist(assetRegistration);
            em.flush();
        } else {
            assetRegistration = TestUtil.findAll(em, AssetRegistration.class).get(0);
        }
        assetWriteOff.setAssetWrittenOff(assetRegistration);
        return assetWriteOff;
    }

    @BeforeEach
    public void initTest() {
        assetWriteOff = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetWriteOff() throws Exception {
        int databaseSizeBeforeCreate = assetWriteOffRepository.findAll().size();
        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);
        restAssetWriteOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeCreate + 1);
        AssetWriteOff testAssetWriteOff = assetWriteOffList.get(assetWriteOffList.size() - 1);
        assertThat(testAssetWriteOff.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetWriteOff.getWriteOffAmount()).isEqualByComparingTo(DEFAULT_WRITE_OFF_AMOUNT);
        assertThat(testAssetWriteOff.getWriteOffDate()).isEqualTo(DEFAULT_WRITE_OFF_DATE);
        assertThat(testAssetWriteOff.getWriteOffReferenceId()).isEqualTo(DEFAULT_WRITE_OFF_REFERENCE_ID);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(1)).save(testAssetWriteOff);
    }

    @Test
    @Transactional
    void createAssetWriteOffWithExistingId() throws Exception {
        // Create the AssetWriteOff with an existing ID
        assetWriteOff.setId(1L);
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        int databaseSizeBeforeCreate = assetWriteOffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetWriteOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void checkWriteOffAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetWriteOffRepository.findAll().size();
        // set the field null
        assetWriteOff.setWriteOffAmount(null);

        // Create the AssetWriteOff, which fails.
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        restAssetWriteOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWriteOffDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetWriteOffRepository.findAll().size();
        // set the field null
        assetWriteOff.setWriteOffDate(null);

        // Create the AssetWriteOff, which fails.
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        restAssetWriteOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffs() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWriteOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].writeOffAmount").value(hasItem(sameNumber(DEFAULT_WRITE_OFF_AMOUNT))))
            .andExpect(jsonPath("$.[*].writeOffDate").value(hasItem(DEFAULT_WRITE_OFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].writeOffReferenceId").value(hasItem(DEFAULT_WRITE_OFF_REFERENCE_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetWriteOffsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetWriteOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetWriteOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetWriteOffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetWriteOffsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetWriteOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetWriteOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetWriteOffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetWriteOff() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get the assetWriteOff
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL_ID, assetWriteOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetWriteOff.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.writeOffAmount").value(sameNumber(DEFAULT_WRITE_OFF_AMOUNT)))
            .andExpect(jsonPath("$.writeOffDate").value(DEFAULT_WRITE_OFF_DATE.toString()))
            .andExpect(jsonPath("$.writeOffReferenceId").value(DEFAULT_WRITE_OFF_REFERENCE_ID.toString()));
    }

    @Test
    @Transactional
    void getAssetWriteOffsByIdFiltering() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        Long id = assetWriteOff.getId();

        defaultAssetWriteOffShouldBeFound("id.equals=" + id);
        defaultAssetWriteOffShouldNotBeFound("id.notEquals=" + id);

        defaultAssetWriteOffShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetWriteOffShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetWriteOffShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetWriteOffShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description equals to DEFAULT_DESCRIPTION
        defaultAssetWriteOffShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetWriteOffList where description equals to UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetWriteOffShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetWriteOffList where description not equals to UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetWriteOffList where description equals to UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description is not null
        defaultAssetWriteOffShouldBeFound("description.specified=true");

        // Get all the assetWriteOffList where description is null
        defaultAssetWriteOffShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description contains DEFAULT_DESCRIPTION
        defaultAssetWriteOffShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetWriteOffList where description contains UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetWriteOffShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetWriteOffList where description does not contain UPDATED_DESCRIPTION
        defaultAssetWriteOffShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount equals to DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.equals=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount equals to UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.equals=" + UPDATED_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount not equals to DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.notEquals=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount not equals to UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.notEquals=" + UPDATED_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsInShouldWork() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount in DEFAULT_WRITE_OFF_AMOUNT or UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.in=" + DEFAULT_WRITE_OFF_AMOUNT + "," + UPDATED_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount equals to UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.in=" + UPDATED_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount is not null
        defaultAssetWriteOffShouldBeFound("writeOffAmount.specified=true");

        // Get all the assetWriteOffList where writeOffAmount is null
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount is greater than or equal to DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.greaterThanOrEqual=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount is greater than or equal to UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.greaterThanOrEqual=" + UPDATED_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount is less than or equal to DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.lessThanOrEqual=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount is less than or equal to SMALLER_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.lessThanOrEqual=" + SMALLER_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount is less than DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.lessThan=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount is less than UPDATED_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.lessThan=" + UPDATED_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffAmount is greater than DEFAULT_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldNotBeFound("writeOffAmount.greaterThan=" + DEFAULT_WRITE_OFF_AMOUNT);

        // Get all the assetWriteOffList where writeOffAmount is greater than SMALLER_WRITE_OFF_AMOUNT
        defaultAssetWriteOffShouldBeFound("writeOffAmount.greaterThan=" + SMALLER_WRITE_OFF_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate equals to DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.equals=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate equals to UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.equals=" + UPDATED_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate not equals to DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.notEquals=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate not equals to UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.notEquals=" + UPDATED_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate in DEFAULT_WRITE_OFF_DATE or UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.in=" + DEFAULT_WRITE_OFF_DATE + "," + UPDATED_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate equals to UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.in=" + UPDATED_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate is not null
        defaultAssetWriteOffShouldBeFound("writeOffDate.specified=true");

        // Get all the assetWriteOffList where writeOffDate is null
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate is greater than or equal to DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.greaterThanOrEqual=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate is greater than or equal to UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.greaterThanOrEqual=" + UPDATED_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate is less than or equal to DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.lessThanOrEqual=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate is less than or equal to SMALLER_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.lessThanOrEqual=" + SMALLER_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate is less than DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.lessThan=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate is less than UPDATED_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.lessThan=" + UPDATED_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffDate is greater than DEFAULT_WRITE_OFF_DATE
        defaultAssetWriteOffShouldNotBeFound("writeOffDate.greaterThan=" + DEFAULT_WRITE_OFF_DATE);

        // Get all the assetWriteOffList where writeOffDate is greater than SMALLER_WRITE_OFF_DATE
        defaultAssetWriteOffShouldBeFound("writeOffDate.greaterThan=" + SMALLER_WRITE_OFF_DATE);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffReferenceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffReferenceId equals to DEFAULT_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldBeFound("writeOffReferenceId.equals=" + DEFAULT_WRITE_OFF_REFERENCE_ID);

        // Get all the assetWriteOffList where writeOffReferenceId equals to UPDATED_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldNotBeFound("writeOffReferenceId.equals=" + UPDATED_WRITE_OFF_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffReferenceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffReferenceId not equals to DEFAULT_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldNotBeFound("writeOffReferenceId.notEquals=" + DEFAULT_WRITE_OFF_REFERENCE_ID);

        // Get all the assetWriteOffList where writeOffReferenceId not equals to UPDATED_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldBeFound("writeOffReferenceId.notEquals=" + UPDATED_WRITE_OFF_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffReferenceIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffReferenceId in DEFAULT_WRITE_OFF_REFERENCE_ID or UPDATED_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldBeFound(
            "writeOffReferenceId.in=" + DEFAULT_WRITE_OFF_REFERENCE_ID + "," + UPDATED_WRITE_OFF_REFERENCE_ID
        );

        // Get all the assetWriteOffList where writeOffReferenceId equals to UPDATED_WRITE_OFF_REFERENCE_ID
        defaultAssetWriteOffShouldNotBeFound("writeOffReferenceId.in=" + UPDATED_WRITE_OFF_REFERENCE_ID);
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByWriteOffReferenceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        // Get all the assetWriteOffList where writeOffReferenceId is not null
        defaultAssetWriteOffShouldBeFound("writeOffReferenceId.specified=true");

        // Get all the assetWriteOffList where writeOffReferenceId is null
        defaultAssetWriteOffShouldNotBeFound("writeOffReferenceId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
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
        assetWriteOff.setCreatedBy(createdBy);
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long createdById = createdBy.getId();

        // Get all the assetWriteOffList where createdBy equals to createdById
        defaultAssetWriteOffShouldBeFound("createdById.equals=" + createdById);

        // Get all the assetWriteOffList where createdBy equals to (createdById + 1)
        defaultAssetWriteOffShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        ApplicationUser modifiedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            modifiedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(modifiedBy);
            em.flush();
        } else {
            modifiedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(modifiedBy);
        em.flush();
        assetWriteOff.setModifiedBy(modifiedBy);
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long modifiedById = modifiedBy.getId();

        // Get all the assetWriteOffList where modifiedBy equals to modifiedById
        defaultAssetWriteOffShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the assetWriteOffList where modifiedBy equals to (modifiedById + 1)
        defaultAssetWriteOffShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByLastAccessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
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
        assetWriteOff.setLastAccessedBy(lastAccessedBy);
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long lastAccessedById = lastAccessedBy.getId();

        // Get all the assetWriteOffList where lastAccessedBy equals to lastAccessedById
        defaultAssetWriteOffShouldBeFound("lastAccessedById.equals=" + lastAccessedById);

        // Get all the assetWriteOffList where lastAccessedBy equals to (lastAccessedById + 1)
        defaultAssetWriteOffShouldNotBeFound("lastAccessedById.equals=" + (lastAccessedById + 1));
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByEffectivePeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
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
        assetWriteOff.setEffectivePeriod(effectivePeriod);
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long effectivePeriodId = effectivePeriod.getId();

        // Get all the assetWriteOffList where effectivePeriod equals to effectivePeriodId
        defaultAssetWriteOffShouldBeFound("effectivePeriodId.equals=" + effectivePeriodId);

        // Get all the assetWriteOffList where effectivePeriod equals to (effectivePeriodId + 1)
        defaultAssetWriteOffShouldNotBeFound("effectivePeriodId.equals=" + (effectivePeriodId + 1));
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
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
        assetWriteOff.addPlaceholder(placeholder);
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long placeholderId = placeholder.getId();

        // Get all the assetWriteOffList where placeholder equals to placeholderId
        defaultAssetWriteOffShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetWriteOffList where placeholder equals to (placeholderId + 1)
        defaultAssetWriteOffShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAssetWriteOffsByAssetWrittenOffIsEqualToSomething() throws Exception {
        // Get already existing entity
        AssetRegistration assetWrittenOff = assetWriteOff.getAssetWrittenOff();
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        Long assetWrittenOffId = assetWrittenOff.getId();

        // Get all the assetWriteOffList where assetWrittenOff equals to assetWrittenOffId
        defaultAssetWriteOffShouldBeFound("assetWrittenOffId.equals=" + assetWrittenOffId);

        // Get all the assetWriteOffList where assetWrittenOff equals to (assetWrittenOffId + 1)
        defaultAssetWriteOffShouldNotBeFound("assetWrittenOffId.equals=" + (assetWrittenOffId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetWriteOffShouldBeFound(String filter) throws Exception {
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWriteOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].writeOffAmount").value(hasItem(sameNumber(DEFAULT_WRITE_OFF_AMOUNT))))
            .andExpect(jsonPath("$.[*].writeOffDate").value(hasItem(DEFAULT_WRITE_OFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].writeOffReferenceId").value(hasItem(DEFAULT_WRITE_OFF_REFERENCE_ID.toString())));

        // Check, that the count call also returns 1
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetWriteOffShouldNotBeFound(String filter) throws Exception {
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetWriteOff() throws Exception {
        // Get the assetWriteOff
        restAssetWriteOffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetWriteOff() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();

        // Update the assetWriteOff
        AssetWriteOff updatedAssetWriteOff = assetWriteOffRepository.findById(assetWriteOff.getId()).get();
        // Disconnect from session so that the updates on updatedAssetWriteOff are not directly saved in db
        em.detach(updatedAssetWriteOff);
        updatedAssetWriteOff
            .description(UPDATED_DESCRIPTION)
            .writeOffAmount(UPDATED_WRITE_OFF_AMOUNT)
            .writeOffDate(UPDATED_WRITE_OFF_DATE)
            .writeOffReferenceId(UPDATED_WRITE_OFF_REFERENCE_ID);
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(updatedAssetWriteOff);

        restAssetWriteOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetWriteOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);
        AssetWriteOff testAssetWriteOff = assetWriteOffList.get(assetWriteOffList.size() - 1);
        assertThat(testAssetWriteOff.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetWriteOff.getWriteOffAmount()).isEqualTo(UPDATED_WRITE_OFF_AMOUNT);
        assertThat(testAssetWriteOff.getWriteOffDate()).isEqualTo(UPDATED_WRITE_OFF_DATE);
        assertThat(testAssetWriteOff.getWriteOffReferenceId()).isEqualTo(UPDATED_WRITE_OFF_REFERENCE_ID);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository).save(testAssetWriteOff);
    }

    @Test
    @Transactional
    void putNonExistingAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetWriteOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void partialUpdateAssetWriteOffWithPatch() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();

        // Update the assetWriteOff using partial update
        AssetWriteOff partialUpdatedAssetWriteOff = new AssetWriteOff();
        partialUpdatedAssetWriteOff.setId(assetWriteOff.getId());

        partialUpdatedAssetWriteOff.writeOffReferenceId(UPDATED_WRITE_OFF_REFERENCE_ID);

        restAssetWriteOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetWriteOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetWriteOff))
            )
            .andExpect(status().isOk());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);
        AssetWriteOff testAssetWriteOff = assetWriteOffList.get(assetWriteOffList.size() - 1);
        assertThat(testAssetWriteOff.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetWriteOff.getWriteOffAmount()).isEqualByComparingTo(DEFAULT_WRITE_OFF_AMOUNT);
        assertThat(testAssetWriteOff.getWriteOffDate()).isEqualTo(DEFAULT_WRITE_OFF_DATE);
        assertThat(testAssetWriteOff.getWriteOffReferenceId()).isEqualTo(UPDATED_WRITE_OFF_REFERENCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateAssetWriteOffWithPatch() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();

        // Update the assetWriteOff using partial update
        AssetWriteOff partialUpdatedAssetWriteOff = new AssetWriteOff();
        partialUpdatedAssetWriteOff.setId(assetWriteOff.getId());

        partialUpdatedAssetWriteOff
            .description(UPDATED_DESCRIPTION)
            .writeOffAmount(UPDATED_WRITE_OFF_AMOUNT)
            .writeOffDate(UPDATED_WRITE_OFF_DATE)
            .writeOffReferenceId(UPDATED_WRITE_OFF_REFERENCE_ID);

        restAssetWriteOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetWriteOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetWriteOff))
            )
            .andExpect(status().isOk());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);
        AssetWriteOff testAssetWriteOff = assetWriteOffList.get(assetWriteOffList.size() - 1);
        assertThat(testAssetWriteOff.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetWriteOff.getWriteOffAmount()).isEqualByComparingTo(UPDATED_WRITE_OFF_AMOUNT);
        assertThat(testAssetWriteOff.getWriteOffDate()).isEqualTo(UPDATED_WRITE_OFF_DATE);
        assertThat(testAssetWriteOff.getWriteOffReferenceId()).isEqualTo(UPDATED_WRITE_OFF_REFERENCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetWriteOffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetWriteOff() throws Exception {
        int databaseSizeBeforeUpdate = assetWriteOffRepository.findAll().size();
        assetWriteOff.setId(count.incrementAndGet());

        // Create the AssetWriteOff
        AssetWriteOffDTO assetWriteOffDTO = assetWriteOffMapper.toDto(assetWriteOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetWriteOffMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetWriteOffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetWriteOff in the database
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(0)).save(assetWriteOff);
    }

    @Test
    @Transactional
    void deleteAssetWriteOff() throws Exception {
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);

        int databaseSizeBeforeDelete = assetWriteOffRepository.findAll().size();

        // Delete the assetWriteOff
        restAssetWriteOffMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetWriteOff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetWriteOff> assetWriteOffList = assetWriteOffRepository.findAll();
        assertThat(assetWriteOffList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetWriteOff in Elasticsearch
        verify(mockAssetWriteOffSearchRepository, times(1)).deleteById(assetWriteOff.getId());
    }

    @Test
    @Transactional
    void searchAssetWriteOff() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetWriteOffRepository.saveAndFlush(assetWriteOff);
        when(mockAssetWriteOffSearchRepository.search("id:" + assetWriteOff.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetWriteOff), PageRequest.of(0, 1), 1));

        // Search the assetWriteOff
        restAssetWriteOffMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetWriteOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetWriteOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].writeOffAmount").value(hasItem(sameNumber(DEFAULT_WRITE_OFF_AMOUNT))))
            .andExpect(jsonPath("$.[*].writeOffDate").value(hasItem(DEFAULT_WRITE_OFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].writeOffReferenceId").value(hasItem(DEFAULT_WRITE_OFF_REFERENCE_ID.toString())));
    }
}
