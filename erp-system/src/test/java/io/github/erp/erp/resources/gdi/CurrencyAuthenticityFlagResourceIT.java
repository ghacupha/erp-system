package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CurrencyAuthenticityFlag;
import io.github.erp.domain.enumeration.CurrencyAuthenticityFlags;
import io.github.erp.domain.enumeration.CurrencyAuthenticityTypes;
import io.github.erp.repository.CurrencyAuthenticityFlagRepository;
import io.github.erp.repository.search.CurrencyAuthenticityFlagSearchRepository;
import io.github.erp.service.dto.CurrencyAuthenticityFlagDTO;
import io.github.erp.service.mapper.CurrencyAuthenticityFlagMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CurrencyAuthenticityFlagResource;
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

/**
 * Integration tests for the {@link CurrencyAuthenticityFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CurrencyAuthenticityFlagResourceIT {

    private static final CurrencyAuthenticityFlags DEFAULT_CURRENCY_AUTHENTICITY_FLAG = CurrencyAuthenticityFlags.Y;
    private static final CurrencyAuthenticityFlags UPDATED_CURRENCY_AUTHENTICITY_FLAG = CurrencyAuthenticityFlags.N;

    private static final CurrencyAuthenticityTypes DEFAULT_CURRENCY_AUTHENTICITY_TYPE = CurrencyAuthenticityTypes.GENUINE;
    private static final CurrencyAuthenticityTypes UPDATED_CURRENCY_AUTHENTICITY_TYPE = CurrencyAuthenticityTypes.COUNTERFEIT;

    private static final String DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/currency-authenticity-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/currency-authenticity-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository;

    @Autowired
    private CurrencyAuthenticityFlagMapper currencyAuthenticityFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CurrencyAuthenticityFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private CurrencyAuthenticityFlagSearchRepository mockCurrencyAuthenticityFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyAuthenticityFlagMockMvc;

    private CurrencyAuthenticityFlag currencyAuthenticityFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyAuthenticityFlag createEntity(EntityManager em) {
        CurrencyAuthenticityFlag currencyAuthenticityFlag = new CurrencyAuthenticityFlag()
            .currencyAuthenticityFlag(DEFAULT_CURRENCY_AUTHENTICITY_FLAG)
            .currencyAuthenticityType(DEFAULT_CURRENCY_AUTHENTICITY_TYPE)
            .currencyAuthenticityTypeDetails(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS);
        return currencyAuthenticityFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyAuthenticityFlag createUpdatedEntity(EntityManager em) {
        CurrencyAuthenticityFlag currencyAuthenticityFlag = new CurrencyAuthenticityFlag()
            .currencyAuthenticityFlag(UPDATED_CURRENCY_AUTHENTICITY_FLAG)
            .currencyAuthenticityType(UPDATED_CURRENCY_AUTHENTICITY_TYPE)
            .currencyAuthenticityTypeDetails(UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS);
        return currencyAuthenticityFlag;
    }

    @BeforeEach
    public void initTest() {
        currencyAuthenticityFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeCreate = currencyAuthenticityFlagRepository.findAll().size();
        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyAuthenticityFlag testCurrencyAuthenticityFlag = currencyAuthenticityFlagList.get(currencyAuthenticityFlagList.size() - 1);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityFlag()).isEqualTo(DEFAULT_CURRENCY_AUTHENTICITY_FLAG);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityType()).isEqualTo(DEFAULT_CURRENCY_AUTHENTICITY_TYPE);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityTypeDetails()).isEqualTo(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(1)).save(testCurrencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void createCurrencyAuthenticityFlagWithExistingId() throws Exception {
        // Create the CurrencyAuthenticityFlag with an existing ID
        currencyAuthenticityFlag.setId(1L);
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        int databaseSizeBeforeCreate = currencyAuthenticityFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void checkCurrencyAuthenticityFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyAuthenticityFlagRepository.findAll().size();
        // set the field null
        currencyAuthenticityFlag.setCurrencyAuthenticityFlag(null);

        // Create the CurrencyAuthenticityFlag, which fails.
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        restCurrencyAuthenticityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyAuthenticityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyAuthenticityFlagRepository.findAll().size();
        // set the field null
        currencyAuthenticityFlag.setCurrencyAuthenticityType(null);

        // Create the CurrencyAuthenticityFlag, which fails.
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        restCurrencyAuthenticityFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlags() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyAuthenticityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityFlag").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityType").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].currencyAuthenticityTypeDetails").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getCurrencyAuthenticityFlag() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get the currencyAuthenticityFlag
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, currencyAuthenticityFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currencyAuthenticityFlag.getId().intValue()))
            .andExpect(jsonPath("$.currencyAuthenticityFlag").value(DEFAULT_CURRENCY_AUTHENTICITY_FLAG.toString()))
            .andExpect(jsonPath("$.currencyAuthenticityType").value(DEFAULT_CURRENCY_AUTHENTICITY_TYPE.toString()))
            .andExpect(jsonPath("$.currencyAuthenticityTypeDetails").value(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCurrencyAuthenticityFlagsByIdFiltering() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        Long id = currencyAuthenticityFlag.getId();

        defaultCurrencyAuthenticityFlagShouldBeFound("id.equals=" + id);
        defaultCurrencyAuthenticityFlagShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyAuthenticityFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyAuthenticityFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyAuthenticityFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyAuthenticityFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag equals to DEFAULT_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityFlag.equals=" + DEFAULT_CURRENCY_AUTHENTICITY_FLAG);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag equals to UPDATED_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityFlag.equals=" + UPDATED_CURRENCY_AUTHENTICITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag not equals to DEFAULT_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityFlag.notEquals=" + DEFAULT_CURRENCY_AUTHENTICITY_FLAG);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag not equals to UPDATED_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityFlag.notEquals=" + UPDATED_CURRENCY_AUTHENTICITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityFlagIsInShouldWork() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag in DEFAULT_CURRENCY_AUTHENTICITY_FLAG or UPDATED_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldBeFound(
            "currencyAuthenticityFlag.in=" + DEFAULT_CURRENCY_AUTHENTICITY_FLAG + "," + UPDATED_CURRENCY_AUTHENTICITY_FLAG
        );

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag equals to UPDATED_CURRENCY_AUTHENTICITY_FLAG
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityFlag.in=" + UPDATED_CURRENCY_AUTHENTICITY_FLAG);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag is not null
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityFlag.specified=true");

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityFlag is null
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType equals to DEFAULT_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityType.equals=" + DEFAULT_CURRENCY_AUTHENTICITY_TYPE);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType equals to UPDATED_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityType.equals=" + UPDATED_CURRENCY_AUTHENTICITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType not equals to DEFAULT_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityType.notEquals=" + DEFAULT_CURRENCY_AUTHENTICITY_TYPE);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType not equals to UPDATED_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityType.notEquals=" + UPDATED_CURRENCY_AUTHENTICITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType in DEFAULT_CURRENCY_AUTHENTICITY_TYPE or UPDATED_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldBeFound(
            "currencyAuthenticityType.in=" + DEFAULT_CURRENCY_AUTHENTICITY_TYPE + "," + UPDATED_CURRENCY_AUTHENTICITY_TYPE
        );

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType equals to UPDATED_CURRENCY_AUTHENTICITY_TYPE
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityType.in=" + UPDATED_CURRENCY_AUTHENTICITY_TYPE);
    }

    @Test
    @Transactional
    void getAllCurrencyAuthenticityFlagsByCurrencyAuthenticityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType is not null
        defaultCurrencyAuthenticityFlagShouldBeFound("currencyAuthenticityType.specified=true");

        // Get all the currencyAuthenticityFlagList where currencyAuthenticityType is null
        defaultCurrencyAuthenticityFlagShouldNotBeFound("currencyAuthenticityType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyAuthenticityFlagShouldBeFound(String filter) throws Exception {
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyAuthenticityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityFlag").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityType").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].currencyAuthenticityTypeDetails").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyAuthenticityFlagShouldNotBeFound(String filter) throws Exception {
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrencyAuthenticityFlag() throws Exception {
        // Get the currencyAuthenticityFlag
        restCurrencyAuthenticityFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCurrencyAuthenticityFlag() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();

        // Update the currencyAuthenticityFlag
        CurrencyAuthenticityFlag updatedCurrencyAuthenticityFlag = currencyAuthenticityFlagRepository
            .findById(currencyAuthenticityFlag.getId())
            .get();
        // Disconnect from session so that the updates on updatedCurrencyAuthenticityFlag are not directly saved in db
        em.detach(updatedCurrencyAuthenticityFlag);
        updatedCurrencyAuthenticityFlag
            .currencyAuthenticityFlag(UPDATED_CURRENCY_AUTHENTICITY_FLAG)
            .currencyAuthenticityType(UPDATED_CURRENCY_AUTHENTICITY_TYPE)
            .currencyAuthenticityTypeDetails(UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS);
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(updatedCurrencyAuthenticityFlag);

        restCurrencyAuthenticityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyAuthenticityFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyAuthenticityFlag testCurrencyAuthenticityFlag = currencyAuthenticityFlagList.get(currencyAuthenticityFlagList.size() - 1);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityFlag()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_FLAG);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityType()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_TYPE);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityTypeDetails()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository).save(testCurrencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void putNonExistingCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyAuthenticityFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyAuthenticityFlagWithPatch() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();

        // Update the currencyAuthenticityFlag using partial update
        CurrencyAuthenticityFlag partialUpdatedCurrencyAuthenticityFlag = new CurrencyAuthenticityFlag();
        partialUpdatedCurrencyAuthenticityFlag.setId(currencyAuthenticityFlag.getId());

        partialUpdatedCurrencyAuthenticityFlag.currencyAuthenticityType(UPDATED_CURRENCY_AUTHENTICITY_TYPE);

        restCurrencyAuthenticityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencyAuthenticityFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyAuthenticityFlag))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyAuthenticityFlag testCurrencyAuthenticityFlag = currencyAuthenticityFlagList.get(currencyAuthenticityFlagList.size() - 1);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityFlag()).isEqualTo(DEFAULT_CURRENCY_AUTHENTICITY_FLAG);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityType()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_TYPE);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityTypeDetails()).isEqualTo(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyAuthenticityFlagWithPatch() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();

        // Update the currencyAuthenticityFlag using partial update
        CurrencyAuthenticityFlag partialUpdatedCurrencyAuthenticityFlag = new CurrencyAuthenticityFlag();
        partialUpdatedCurrencyAuthenticityFlag.setId(currencyAuthenticityFlag.getId());

        partialUpdatedCurrencyAuthenticityFlag
            .currencyAuthenticityFlag(UPDATED_CURRENCY_AUTHENTICITY_FLAG)
            .currencyAuthenticityType(UPDATED_CURRENCY_AUTHENTICITY_TYPE)
            .currencyAuthenticityTypeDetails(UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS);

        restCurrencyAuthenticityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencyAuthenticityFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencyAuthenticityFlag))
            )
            .andExpect(status().isOk());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);
        CurrencyAuthenticityFlag testCurrencyAuthenticityFlag = currencyAuthenticityFlagList.get(currencyAuthenticityFlagList.size() - 1);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityFlag()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_FLAG);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityType()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_TYPE);
        assertThat(testCurrencyAuthenticityFlag.getCurrencyAuthenticityTypeDetails()).isEqualTo(UPDATED_CURRENCY_AUTHENTICITY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyAuthenticityFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrencyAuthenticityFlag() throws Exception {
        int databaseSizeBeforeUpdate = currencyAuthenticityFlagRepository.findAll().size();
        currencyAuthenticityFlag.setId(count.incrementAndGet());

        // Create the CurrencyAuthenticityFlag
        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyAuthenticityFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencyAuthenticityFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CurrencyAuthenticityFlag in the database
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(0)).save(currencyAuthenticityFlag);
    }

    @Test
    @Transactional
    void deleteCurrencyAuthenticityFlag() throws Exception {
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);

        int databaseSizeBeforeDelete = currencyAuthenticityFlagRepository.findAll().size();

        // Delete the currencyAuthenticityFlag
        restCurrencyAuthenticityFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, currencyAuthenticityFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CurrencyAuthenticityFlag> currencyAuthenticityFlagList = currencyAuthenticityFlagRepository.findAll();
        assertThat(currencyAuthenticityFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CurrencyAuthenticityFlag in Elasticsearch
        verify(mockCurrencyAuthenticityFlagSearchRepository, times(1)).deleteById(currencyAuthenticityFlag.getId());
    }

    @Test
    @Transactional
    void searchCurrencyAuthenticityFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        currencyAuthenticityFlagRepository.saveAndFlush(currencyAuthenticityFlag);
        when(mockCurrencyAuthenticityFlagSearchRepository.search("id:" + currencyAuthenticityFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(currencyAuthenticityFlag), PageRequest.of(0, 1), 1));

        // Search the currencyAuthenticityFlag
        restCurrencyAuthenticityFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + currencyAuthenticityFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyAuthenticityFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityFlag").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].currencyAuthenticityType").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE.toString())))
            .andExpect(
                jsonPath("$.[*].currencyAuthenticityTypeDetails").value(hasItem(DEFAULT_CURRENCY_AUTHENTICITY_TYPE_DETAILS.toString()))
            );
    }
}
