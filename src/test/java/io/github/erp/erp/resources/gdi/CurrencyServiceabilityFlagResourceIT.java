package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.CurrencyServiceabilityFlag;
import io.github.erp.domain.enumeration.CurrencyServiceability;
import io.github.erp.domain.enumeration.CurrencyServiceabilityFlagTypes;
import io.github.erp.repository.CurrencyServiceabilityFlagRepository;
import io.github.erp.repository.search.CurrencyServiceabilityFlagSearchRepository;
import io.github.erp.service.dto.CurrencyServiceabilityFlagDTO;
import io.github.erp.service.mapper.CurrencyServiceabilityFlagMapper;
import io.github.erp.web.rest.CurrencyServiceabilityFlagResource;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CurrencyServiceabilityFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CurrencyServiceabilityFlagResourceIT {

    private static final CurrencyServiceabilityFlagTypes DEFAULT_CURRENCY_SERVICEABILITY_FLAG = CurrencyServiceabilityFlagTypes.Y;
    private static final CurrencyServiceabilityFlagTypes UPDATED_CURRENCY_SERVICEABILITY_FLAG = CurrencyServiceabilityFlagTypes.N;

    private static final CurrencyServiceability DEFAULT_CURRENCY_SERVICEABILITY = CurrencyServiceability.FIT;
    private static final CurrencyServiceability UPDATED_CURRENCY_SERVICEABILITY = CurrencyServiceability.UNFIT;

    private static final String DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/currency-serviceability-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/currency-serviceability-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository;

    @Autowired
    private CurrencyServiceabilityFlagMapper currencyServiceabilityFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CurrencyServiceabilityFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private CurrencyServiceabilityFlagSearchRepository mockCurrencyServiceabilityFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyServiceabilityFlagMockMvc;

    private CurrencyServiceabilityFlag currencyServiceabilityFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyServiceabilityFlag createEntity(EntityManager em) {
        CurrencyServiceabilityFlag currencyServiceabilityFlag = new CurrencyServiceabilityFlag()
            .currencyServiceabilityFlag(DEFAULT_CURRENCY_SERVICEABILITY_FLAG)
            .currencyServiceability(DEFAULT_CURRENCY_SERVICEABILITY)
            .currencyServiceabilityFlagDetails(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS);
        return currencyServiceabilityFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyServiceabilityFlag createUpdatedEntity(EntityManager em) {
        CurrencyServiceabilityFlag currencyServiceabilityFlag = new CurrencyServiceabilityFlag()
            .currencyServiceabilityFlag(UPDATED_CURRENCY_SERVICEABILITY_FLAG)
            .currencyServiceability(UPDATED_CURRENCY_SERVICEABILITY)
            .currencyServiceabilityFlagDetails(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);
        return currencyServiceabilityFlag;
    }

    @BeforeEach
    public void initTest() {
        currencyServiceabilityFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeCreate = currencyServiceabilityFlagRepository.findAll().size();
        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyServiceabilityFlag testCurrencyServiceabilityFlag = currencyServiceabilityFlagList.get(
            currencyServiceabilityFlagList.size() - 1
        );
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlag()).isEqualTo(DEFAULT_CURRENCY_SERVICEABILITY_FLAG);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceability()).isEqualTo(DEFAULT_CURRENCY_SERVICEABILITY);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlagDetails())
            .isEqualTo(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(1)).save(testCurrencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void createCurrencyServiceabilityFlagWithExistingId() throws Exception {
        // Create the CurrencyServiceabilityFlag with an existing ID
        currencyServiceabilityFlag.setId(1L);
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        int databaseSizeBeforeCreate = currencyServiceabilityFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void checkCurrencyServiceabilityFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyServiceabilityFlagRepository.findAll().size();
        // set the field null
        currencyServiceabilityFlag.setCurrencyServiceabilityFlag(null);

        // Create the CurrencyServiceabilityFlag, which fails.
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        restCurrencyServiceabilityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyServiceabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyServiceabilityFlagRepository.findAll().size();
        // set the field null
        currencyServiceabilityFlag.setCurrencyServiceability(null);

        // Create the CurrencyServiceabilityFlag, which fails.
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        restCurrencyServiceabilityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlags() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyServiceabilityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyServiceabilityFlag").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyServiceability").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY.toString())))
            .andExpect(
                jsonPath("$.[*].currencyServiceabilityFlagDetails").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getCurrencyServiceabilityFlag() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get the currencyServiceabilityFlag
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, currencyServiceabilityFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currencyServiceabilityFlag.getId().intValue()))
            .andExpect(jsonPath("$.currencyServiceabilityFlag").value(DEFAULT_CURRENCY_SERVICEABILITY_FLAG.toString()))
            .andExpect(jsonPath("$.currencyServiceability").value(DEFAULT_CURRENCY_SERVICEABILITY.toString()))
            .andExpect(jsonPath("$.currencyServiceabilityFlagDetails").value(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCurrencyServiceabilityFlagsByIdFiltering() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        Long id = currencyServiceabilityFlag.getId();

        defaultCurrencyServiceabilityFlagShouldBeFound("id.equals=" + id);
        defaultCurrencyServiceabilityFlagShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyServiceabilityFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyServiceabilityFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyServiceabilityFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyServiceabilityFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag equals to DEFAULT_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceabilityFlag.equals=" + DEFAULT_CURRENCY_SERVICEABILITY_FLAG);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag equals to UPDATED_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceabilityFlag.equals=" + UPDATED_CURRENCY_SERVICEABILITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag not equals to DEFAULT_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceabilityFlag.notEquals=" + DEFAULT_CURRENCY_SERVICEABILITY_FLAG);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag not equals to UPDATED_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceabilityFlag.notEquals=" + UPDATED_CURRENCY_SERVICEABILITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityFlagIsInShouldWork() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag in DEFAULT_CURRENCY_SERVICEABILITY_FLAG or UPDATED_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldBeFound(
            "currencyServiceabilityFlag.in=" + DEFAULT_CURRENCY_SERVICEABILITY_FLAG + "," + UPDATED_CURRENCY_SERVICEABILITY_FLAG
        );

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag equals to UPDATED_CURRENCY_SERVICEABILITY_FLAG
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceabilityFlag.in=" + UPDATED_CURRENCY_SERVICEABILITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag is not null
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceabilityFlag.specified=true");

        // Get all the currencyServiceabilityFlagList where currencyServiceabilityFlag is null
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceabilityFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceability equals to DEFAULT_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceability.equals=" + DEFAULT_CURRENCY_SERVICEABILITY);

        // Get all the currencyServiceabilityFlagList where currencyServiceability equals to UPDATED_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceability.equals=" + UPDATED_CURRENCY_SERVICEABILITY);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceability not equals to DEFAULT_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceability.notEquals=" + DEFAULT_CURRENCY_SERVICEABILITY);

        // Get all the currencyServiceabilityFlagList where currencyServiceability not equals to UPDATED_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceability.notEquals=" + UPDATED_CURRENCY_SERVICEABILITY);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityIsInShouldWork() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceability in DEFAULT_CURRENCY_SERVICEABILITY or UPDATED_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldBeFound(
            "currencyServiceability.in=" + DEFAULT_CURRENCY_SERVICEABILITY + "," + UPDATED_CURRENCY_SERVICEABILITY
        );

        // Get all the currencyServiceabilityFlagList where currencyServiceability equals to UPDATED_CURRENCY_SERVICEABILITY
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceability.in=" + UPDATED_CURRENCY_SERVICEABILITY);
    }

    @Test
    @Transactional
    void getAllCurrencyServiceabilityFlagsByCurrencyServiceabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        // Get all the currencyServiceabilityFlagList where currencyServiceability is not null
        defaultCurrencyServiceabilityFlagShouldBeFound("currencyServiceability.specified=true");

        // Get all the currencyServiceabilityFlagList where currencyServiceability is null
        defaultCurrencyServiceabilityFlagShouldNotBeFound("currencyServiceability.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyServiceabilityFlagShouldBeFound(String filter) throws Exception {
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyServiceabilityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyServiceabilityFlag").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyServiceability").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY.toString())))
            .andExpect(
                jsonPath("$.[*].currencyServiceabilityFlagDetails").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyServiceabilityFlagShouldNotBeFound(String filter) throws Exception {
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrencyServiceabilityFlag() throws Exception {
        // Get the currencyServiceabilityFlag
        restCurrencyServiceabilityFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCurrencyServiceabilityFlag() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();

        // Update the currencyServiceabilityFlag
        CurrencyServiceabilityFlag updatedCurrencyServiceabilityFlag = currencyServiceabilityFlagRepository
            .findById(currencyServiceabilityFlag.getId())
            .get();
        // Disconnect from session so that the updates on updatedCurrencyServiceabilityFlag are not directly saved in db
        em.detach(updatedCurrencyServiceabilityFlag);
        updatedCurrencyServiceabilityFlag
            .currencyServiceabilityFlag(UPDATED_CURRENCY_SERVICEABILITY_FLAG)
            .currencyServiceability(UPDATED_CURRENCY_SERVICEABILITY)
            .currencyServiceabilityFlagDetails(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(
            updatedCurrencyServiceabilityFlag
        );

        restCurrencyServiceabilityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyServiceabilityFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyServiceabilityFlag testCurrencyServiceabilityFlag = currencyServiceabilityFlagList.get(
            currencyServiceabilityFlagList.size() - 1
        );
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlag()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceability()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlagDetails())
            .isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository).save(testCurrencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void putNonExistingCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyServiceabilityFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyServiceabilityFlagWithPatch() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();

        // Update the currencyServiceabilityFlag using partial update
        CurrencyServiceabilityFlag partialUpdatedCurrencyServiceabilityFlag = new CurrencyServiceabilityFlag();
        partialUpdatedCurrencyServiceabilityFlag.setId(currencyServiceabilityFlag.getId());

        partialUpdatedCurrencyServiceabilityFlag
            .currencyServiceabilityFlag(UPDATED_CURRENCY_SERVICEABILITY_FLAG)
            .currencyServiceability(UPDATED_CURRENCY_SERVICEABILITY)
            .currencyServiceabilityFlagDetails(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);

        restCurrencyServiceabilityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencyServiceabilityFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyServiceabilityFlag))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyServiceabilityFlag testCurrencyServiceabilityFlag = currencyServiceabilityFlagList.get(
            currencyServiceabilityFlagList.size() - 1
        );
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlag()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceability()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlagDetails())
            .isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyServiceabilityFlagWithPatch() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();

        // Update the currencyServiceabilityFlag using partial update
        CurrencyServiceabilityFlag partialUpdatedCurrencyServiceabilityFlag = new CurrencyServiceabilityFlag();
        partialUpdatedCurrencyServiceabilityFlag.setId(currencyServiceabilityFlag.getId());

        partialUpdatedCurrencyServiceabilityFlag
            .currencyServiceabilityFlag(UPDATED_CURRENCY_SERVICEABILITY_FLAG)
            .currencyServiceability(UPDATED_CURRENCY_SERVICEABILITY)
            .currencyServiceabilityFlagDetails(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);

        restCurrencyServiceabilityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencyServiceabilityFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyServiceabilityFlag))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyServiceabilityFlag testCurrencyServiceabilityFlag = currencyServiceabilityFlagList.get(
            currencyServiceabilityFlagList.size() - 1
        );
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlag()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceability()).isEqualTo(UPDATED_CURRENCY_SERVICEABILITY);
        assertThat(testCurrencyServiceabilityFlag.getCurrencyServiceabilityFlagDetails())
            .isEqualTo(UPDATED_CURRENCY_SERVICEABILITY_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyServiceabilityFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrencyServiceabilityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyServiceabilityFlagRepository.findAll().size();
        currencyServiceabilityFlag.setId(count.incrementAndGet());

        // Create the CurrencyServiceabilityFlag
        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyServiceabilityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyServiceabilityFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CurrencyServiceabilityFlag in the database
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(0)).save(currencyServiceabilityFlag);
    }

    @Test
    @Transactional
    void deleteCurrencyServiceabilityFlag() throws Exception {
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);

        int databaseSizeBeforeDelete = currencyServiceabilityFlagRepository.findAll().size();

        // Delete the currencyServiceabilityFlag
        restCurrencyServiceabilityFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, currencyServiceabilityFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CurrencyServiceabilityFlag> currencyServiceabilityFlagList = currencyServiceabilityFlagRepository.findAll();
        assertThat(currencyServiceabilityFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CurrencyServiceabilityFlag in Elasticsearch
        verify(mockCurrencyServiceabilityFlagSearchRepository, times(1)).deleteById(currencyServiceabilityFlag.getId());
    }

    @Test
    @Transactional
    void searchCurrencyServiceabilityFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        currencyServiceabilityFlagRepository.saveAndFlush(currencyServiceabilityFlag);
        when(mockCurrencyServiceabilityFlagSearchRepository.search("id:" + currencyServiceabilityFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(currencyServiceabilityFlag), PageRequest.of(0, 1), 1));

        // Search the currencyServiceabilityFlag
        restCurrencyServiceabilityFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + currencyServiceabilityFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyServiceabilityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyServiceabilityFlag").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyServiceability").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY.toString())))
            .andExpect(
                jsonPath("$.[*].currencyServiceabilityFlagDetails").value(hasItem(DEFAULT_CURRENCY_SERVICEABILITY_FLAG_DETAILS.toString()))
            );
    }
}
