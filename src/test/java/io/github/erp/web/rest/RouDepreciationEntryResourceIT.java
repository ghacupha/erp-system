package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.service.criteria.RouDepreciationEntryCriteria;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RouDepreciationEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouDepreciationEntryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_OUTSTANDING_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_OUTSTANDING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_OUTSTANDING_AMOUNT = new BigDecimal(0 - 1);

    private static final UUID DEFAULT_ROU_ASSET_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_ROU_ASSET_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_ROU_DEPRECIATION_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_ROU_DEPRECIATION_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/rou-depreciation-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/rou-depreciation-entries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RouDepreciationEntryRepository rouDepreciationEntryRepository;

    @Autowired
    private RouDepreciationEntryMapper rouDepreciationEntryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RouDepreciationEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private RouDepreciationEntrySearchRepository mockRouDepreciationEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouDepreciationEntryMockMvc;

    private RouDepreciationEntry rouDepreciationEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntry createEntity(EntityManager em) {
        RouDepreciationEntry rouDepreciationEntry = new RouDepreciationEntry()
            .description(DEFAULT_DESCRIPTION)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .outstandingAmount(DEFAULT_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(DEFAULT_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(DEFAULT_ROU_DEPRECIATION_IDENTIFIER);
        return rouDepreciationEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouDepreciationEntry createUpdatedEntity(EntityManager em) {
        RouDepreciationEntry rouDepreciationEntry = new RouDepreciationEntry()
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER);
        return rouDepreciationEntry;
    }

    @BeforeEach
    public void initTest() {
        rouDepreciationEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeCreate = rouDepreciationEntryRepository.findAll().size();
        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);
        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeCreate + 1);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(DEFAULT_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(DEFAULT_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(DEFAULT_ROU_DEPRECIATION_IDENTIFIER);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(1)).save(testRouDepreciationEntry);
    }

    @Test
    @Transactional
    void createRouDepreciationEntryWithExistingId() throws Exception {
        // Create the RouDepreciationEntry with an existing ID
        rouDepreciationEntry.setId(1L);
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        int databaseSizeBeforeCreate = rouDepreciationEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void checkDepreciationAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setDepreciationAmount(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutstandingAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setOutstandingAmount(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRouDepreciationIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = rouDepreciationEntryRepository.findAll().size();
        // set the field null
        rouDepreciationEntry.setRouDepreciationIdentifier(null);

        // Create the RouDepreciationEntry, which fails.
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntries() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, rouDepreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rouDepreciationEntry.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.outstandingAmount").value(sameNumber(DEFAULT_OUTSTANDING_AMOUNT)))
            .andExpect(jsonPath("$.rouAssetIdentifier").value(DEFAULT_ROU_ASSET_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.rouDepreciationIdentifier").value(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getRouDepreciationEntriesByIdFiltering() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        Long id = rouDepreciationEntry.getId();

        defaultRouDepreciationEntryShouldBeFound("id.equals=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.notEquals=" + id);

        defaultRouDepreciationEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultRouDepreciationEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRouDepreciationEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description not equals to DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description not equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description equals to UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description is not null
        defaultRouDepreciationEntryShouldBeFound("description.specified=true");

        // Get all the rouDepreciationEntryList where description is null
        defaultRouDepreciationEntryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description contains DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description contains UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where description does not contain DEFAULT_DESCRIPTION
        defaultRouDepreciationEntryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the rouDepreciationEntryList where description does not contain UPDATED_DESCRIPTION
        defaultRouDepreciationEntryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT
        );

        // Get all the rouDepreciationEntryList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is not null
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.specified=true");

        // Get all the rouDepreciationEntryList where depreciationAmount is null
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the rouDepreciationEntryList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.equals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.equals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount not equals to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.notEquals=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount not equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.notEquals=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount in DEFAULT_OUTSTANDING_AMOUNT or UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.in=" + DEFAULT_OUTSTANDING_AMOUNT + "," + UPDATED_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount equals to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.in=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is not null
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.specified=true");

        // Get all the rouDepreciationEntryList where outstandingAmount is null
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.greaterThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than or equal to UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.greaterThanOrEqual=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than or equal to DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.lessThanOrEqual=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than or equal to SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.lessThanOrEqual=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.lessThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is less than UPDATED_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.lessThan=" + UPDATED_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByOutstandingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than DEFAULT_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldNotBeFound("outstandingAmount.greaterThan=" + DEFAULT_OUTSTANDING_AMOUNT);

        // Get all the rouDepreciationEntryList where outstandingAmount is greater than SMALLER_OUTSTANDING_AMOUNT
        defaultRouDepreciationEntryShouldBeFound("outstandingAmount.greaterThan=" + SMALLER_OUTSTANDING_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.equals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.equals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier not equals to DEFAULT_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.notEquals=" + DEFAULT_ROU_ASSET_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier not equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.notEquals=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier in DEFAULT_ROU_ASSET_IDENTIFIER or UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "rouAssetIdentifier.in=" + DEFAULT_ROU_ASSET_IDENTIFIER + "," + UPDATED_ROU_ASSET_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where rouAssetIdentifier equals to UPDATED_ROU_ASSET_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.in=" + UPDATED_ROU_ASSET_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouAssetIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouAssetIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("rouAssetIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where rouAssetIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("rouAssetIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to DEFAULT_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.equals=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.equals=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier not equals to DEFAULT_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.notEquals=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier not equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.notEquals=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier in DEFAULT_ROU_DEPRECIATION_IDENTIFIER or UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldBeFound(
            "rouDepreciationIdentifier.in=" + DEFAULT_ROU_DEPRECIATION_IDENTIFIER + "," + UPDATED_ROU_DEPRECIATION_IDENTIFIER
        );

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier equals to UPDATED_ROU_DEPRECIATION_IDENTIFIER
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.in=" + UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRouDepreciationEntriesByRouDepreciationIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier is not null
        defaultRouDepreciationEntryShouldBeFound("rouDepreciationIdentifier.specified=true");

        // Get all the rouDepreciationEntryList where rouDepreciationIdentifier is null
        defaultRouDepreciationEntryShouldNotBeFound("rouDepreciationIdentifier.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRouDepreciationEntryShouldBeFound(String filter) throws Exception {
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRouDepreciationEntryShouldNotBeFound(String filter) throws Exception {
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRouDepreciationEntry() throws Exception {
        // Get the rouDepreciationEntry
        restRouDepreciationEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry
        RouDepreciationEntry updatedRouDepreciationEntry = rouDepreciationEntryRepository.findById(rouDepreciationEntry.getId()).get();
        // Disconnect from session so that the updates on updatedRouDepreciationEntry are not directly saved in db
        em.detach(updatedRouDepreciationEntry);
        updatedRouDepreciationEntry
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER);
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(updatedRouDepreciationEntry);

        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualTo(UPDATED_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(UPDATED_ROU_DEPRECIATION_IDENTIFIER);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository).save(testRouDepreciationEntry);
    }

    @Test
    @Transactional
    void putNonExistingRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void partialUpdateRouDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry using partial update
        RouDepreciationEntry partialUpdatedRouDepreciationEntry = new RouDepreciationEntry();
        partialUpdatedRouDepreciationEntry.setId(rouDepreciationEntry.getId());

        partialUpdatedRouDepreciationEntry.rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER);

        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(DEFAULT_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(DEFAULT_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateRouDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();

        // Update the rouDepreciationEntry using partial update
        RouDepreciationEntry partialUpdatedRouDepreciationEntry = new RouDepreciationEntry();
        partialUpdatedRouDepreciationEntry.setId(rouDepreciationEntry.getId());

        partialUpdatedRouDepreciationEntry
            .description(UPDATED_DESCRIPTION)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .outstandingAmount(UPDATED_OUTSTANDING_AMOUNT)
            .rouAssetIdentifier(UPDATED_ROU_ASSET_IDENTIFIER)
            .rouDepreciationIdentifier(UPDATED_ROU_DEPRECIATION_IDENTIFIER);

        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRouDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        RouDepreciationEntry testRouDepreciationEntry = rouDepreciationEntryList.get(rouDepreciationEntryList.size() - 1);
        assertThat(testRouDepreciationEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRouDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testRouDepreciationEntry.getOutstandingAmount()).isEqualByComparingTo(UPDATED_OUTSTANDING_AMOUNT);
        assertThat(testRouDepreciationEntry.getRouAssetIdentifier()).isEqualTo(UPDATED_ROU_ASSET_IDENTIFIER);
        assertThat(testRouDepreciationEntry.getRouDepreciationIdentifier()).isEqualTo(UPDATED_ROU_DEPRECIATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rouDepreciationEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = rouDepreciationEntryRepository.findAll().size();
        rouDepreciationEntry.setId(count.incrementAndGet());

        // Create the RouDepreciationEntry
        RouDepreciationEntryDTO rouDepreciationEntryDTO = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rouDepreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouDepreciationEntry in the database
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(0)).save(rouDepreciationEntry);
    }

    @Test
    @Transactional
    void deleteRouDepreciationEntry() throws Exception {
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);

        int databaseSizeBeforeDelete = rouDepreciationEntryRepository.findAll().size();

        // Delete the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, rouDepreciationEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RouDepreciationEntry> rouDepreciationEntryList = rouDepreciationEntryRepository.findAll();
        assertThat(rouDepreciationEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RouDepreciationEntry in Elasticsearch
        verify(mockRouDepreciationEntrySearchRepository, times(1)).deleteById(rouDepreciationEntry.getId());
    }

    @Test
    @Transactional
    void searchRouDepreciationEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        rouDepreciationEntryRepository.saveAndFlush(rouDepreciationEntry);
        when(mockRouDepreciationEntrySearchRepository.search("id:" + rouDepreciationEntry.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rouDepreciationEntry), PageRequest.of(0, 1), 1));

        // Search the rouDepreciationEntry
        restRouDepreciationEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + rouDepreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rouDepreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].outstandingAmount").value(hasItem(sameNumber(DEFAULT_OUTSTANDING_AMOUNT))))
            .andExpect(jsonPath("$.[*].rouAssetIdentifier").value(hasItem(DEFAULT_ROU_ASSET_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].rouDepreciationIdentifier").value(hasItem(DEFAULT_ROU_DEPRECIATION_IDENTIFIER.toString())));
    }
}
