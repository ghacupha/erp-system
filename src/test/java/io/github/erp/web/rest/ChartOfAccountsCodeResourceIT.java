package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.ChartOfAccountsCode;
import io.github.erp.repository.ChartOfAccountsCodeRepository;
import io.github.erp.repository.search.ChartOfAccountsCodeSearchRepository;
import io.github.erp.service.criteria.ChartOfAccountsCodeCriteria;
import io.github.erp.service.dto.ChartOfAccountsCodeDTO;
import io.github.erp.service.mapper.ChartOfAccountsCodeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ChartOfAccountsCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChartOfAccountsCodeResourceIT {

    private static final String DEFAULT_CHART_OF_ACCOUNTS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHART_OF_ACCOUNTS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHART_OF_ACCOUNTS_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_CHART_OF_ACCOUNTS_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chart-of-accounts-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/chart-of-accounts-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChartOfAccountsCodeRepository chartOfAccountsCodeRepository;

    @Autowired
    private ChartOfAccountsCodeMapper chartOfAccountsCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ChartOfAccountsCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChartOfAccountsCodeSearchRepository mockChartOfAccountsCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChartOfAccountsCodeMockMvc;

    private ChartOfAccountsCode chartOfAccountsCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccountsCode createEntity(EntityManager em) {
        ChartOfAccountsCode chartOfAccountsCode = new ChartOfAccountsCode()
            .chartOfAccountsCode(DEFAULT_CHART_OF_ACCOUNTS_CODE)
            .chartOfAccountsClass(DEFAULT_CHART_OF_ACCOUNTS_CLASS)
            .description(DEFAULT_DESCRIPTION);
        return chartOfAccountsCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccountsCode createUpdatedEntity(EntityManager em) {
        ChartOfAccountsCode chartOfAccountsCode = new ChartOfAccountsCode()
            .chartOfAccountsCode(UPDATED_CHART_OF_ACCOUNTS_CODE)
            .chartOfAccountsClass(UPDATED_CHART_OF_ACCOUNTS_CLASS)
            .description(UPDATED_DESCRIPTION);
        return chartOfAccountsCode;
    }

    @BeforeEach
    public void initTest() {
        chartOfAccountsCode = createEntity(em);
    }

    @Test
    @Transactional
    void createChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeCreate = chartOfAccountsCodeRepository.findAll().size();
        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);
        restChartOfAccountsCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeCreate + 1);
        ChartOfAccountsCode testChartOfAccountsCode = chartOfAccountsCodeList.get(chartOfAccountsCodeList.size() - 1);
        assertThat(testChartOfAccountsCode.getChartOfAccountsCode()).isEqualTo(DEFAULT_CHART_OF_ACCOUNTS_CODE);
        assertThat(testChartOfAccountsCode.getChartOfAccountsClass()).isEqualTo(DEFAULT_CHART_OF_ACCOUNTS_CLASS);
        assertThat(testChartOfAccountsCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(1)).save(testChartOfAccountsCode);
    }

    @Test
    @Transactional
    void createChartOfAccountsCodeWithExistingId() throws Exception {
        // Create the ChartOfAccountsCode with an existing ID
        chartOfAccountsCode.setId(1L);
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        int databaseSizeBeforeCreate = chartOfAccountsCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChartOfAccountsCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void checkChartOfAccountsCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chartOfAccountsCodeRepository.findAll().size();
        // set the field null
        chartOfAccountsCode.setChartOfAccountsCode(null);

        // Create the ChartOfAccountsCode, which fails.
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        restChartOfAccountsCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChartOfAccountsClassIsRequired() throws Exception {
        int databaseSizeBeforeTest = chartOfAccountsCodeRepository.findAll().size();
        // set the field null
        chartOfAccountsCode.setChartOfAccountsClass(null);

        // Create the ChartOfAccountsCode, which fails.
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        restChartOfAccountsCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodes() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccountsCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartOfAccountsCode").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CODE)))
            .andExpect(jsonPath("$.[*].chartOfAccountsClass").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CLASS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getChartOfAccountsCode() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get the chartOfAccountsCode
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, chartOfAccountsCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chartOfAccountsCode.getId().intValue()))
            .andExpect(jsonPath("$.chartOfAccountsCode").value(DEFAULT_CHART_OF_ACCOUNTS_CODE))
            .andExpect(jsonPath("$.chartOfAccountsClass").value(DEFAULT_CHART_OF_ACCOUNTS_CLASS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getChartOfAccountsCodesByIdFiltering() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        Long id = chartOfAccountsCode.getId();

        defaultChartOfAccountsCodeShouldBeFound("id.equals=" + id);
        defaultChartOfAccountsCodeShouldNotBeFound("id.notEquals=" + id);

        defaultChartOfAccountsCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChartOfAccountsCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultChartOfAccountsCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChartOfAccountsCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode equals to DEFAULT_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsCode.equals=" + DEFAULT_CHART_OF_ACCOUNTS_CODE);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode equals to UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.equals=" + UPDATED_CHART_OF_ACCOUNTS_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode not equals to DEFAULT_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.notEquals=" + DEFAULT_CHART_OF_ACCOUNTS_CODE);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode not equals to UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsCode.notEquals=" + UPDATED_CHART_OF_ACCOUNTS_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeIsInShouldWork() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode in DEFAULT_CHART_OF_ACCOUNTS_CODE or UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldBeFound(
            "chartOfAccountsCode.in=" + DEFAULT_CHART_OF_ACCOUNTS_CODE + "," + UPDATED_CHART_OF_ACCOUNTS_CODE
        );

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode equals to UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.in=" + UPDATED_CHART_OF_ACCOUNTS_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode is not null
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsCode.specified=true");

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode is null
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeContainsSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode contains DEFAULT_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsCode.contains=" + DEFAULT_CHART_OF_ACCOUNTS_CODE);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode contains UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.contains=" + UPDATED_CHART_OF_ACCOUNTS_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsCodeNotContainsSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode does not contain DEFAULT_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsCode.doesNotContain=" + DEFAULT_CHART_OF_ACCOUNTS_CODE);

        // Get all the chartOfAccountsCodeList where chartOfAccountsCode does not contain UPDATED_CHART_OF_ACCOUNTS_CODE
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsCode.doesNotContain=" + UPDATED_CHART_OF_ACCOUNTS_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassIsEqualToSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass equals to DEFAULT_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsClass.equals=" + DEFAULT_CHART_OF_ACCOUNTS_CLASS);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass equals to UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.equals=" + UPDATED_CHART_OF_ACCOUNTS_CLASS);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass not equals to DEFAULT_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.notEquals=" + DEFAULT_CHART_OF_ACCOUNTS_CLASS);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass not equals to UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsClass.notEquals=" + UPDATED_CHART_OF_ACCOUNTS_CLASS);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassIsInShouldWork() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass in DEFAULT_CHART_OF_ACCOUNTS_CLASS or UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldBeFound(
            "chartOfAccountsClass.in=" + DEFAULT_CHART_OF_ACCOUNTS_CLASS + "," + UPDATED_CHART_OF_ACCOUNTS_CLASS
        );

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass equals to UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.in=" + UPDATED_CHART_OF_ACCOUNTS_CLASS);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass is not null
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsClass.specified=true");

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass is null
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassContainsSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass contains DEFAULT_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsClass.contains=" + DEFAULT_CHART_OF_ACCOUNTS_CLASS);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass contains UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.contains=" + UPDATED_CHART_OF_ACCOUNTS_CLASS);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsCodesByChartOfAccountsClassNotContainsSomething() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass does not contain DEFAULT_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldNotBeFound("chartOfAccountsClass.doesNotContain=" + DEFAULT_CHART_OF_ACCOUNTS_CLASS);

        // Get all the chartOfAccountsCodeList where chartOfAccountsClass does not contain UPDATED_CHART_OF_ACCOUNTS_CLASS
        defaultChartOfAccountsCodeShouldBeFound("chartOfAccountsClass.doesNotContain=" + UPDATED_CHART_OF_ACCOUNTS_CLASS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChartOfAccountsCodeShouldBeFound(String filter) throws Exception {
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccountsCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartOfAccountsCode").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CODE)))
            .andExpect(jsonPath("$.[*].chartOfAccountsClass").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CLASS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChartOfAccountsCodeShouldNotBeFound(String filter) throws Exception {
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChartOfAccountsCode() throws Exception {
        // Get the chartOfAccountsCode
        restChartOfAccountsCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChartOfAccountsCode() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();

        // Update the chartOfAccountsCode
        ChartOfAccountsCode updatedChartOfAccountsCode = chartOfAccountsCodeRepository.findById(chartOfAccountsCode.getId()).get();
        // Disconnect from session so that the updates on updatedChartOfAccountsCode are not directly saved in db
        em.detach(updatedChartOfAccountsCode);
        updatedChartOfAccountsCode
            .chartOfAccountsCode(UPDATED_CHART_OF_ACCOUNTS_CODE)
            .chartOfAccountsClass(UPDATED_CHART_OF_ACCOUNTS_CLASS)
            .description(UPDATED_DESCRIPTION);
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(updatedChartOfAccountsCode);

        restChartOfAccountsCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chartOfAccountsCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccountsCode testChartOfAccountsCode = chartOfAccountsCodeList.get(chartOfAccountsCodeList.size() - 1);
        assertThat(testChartOfAccountsCode.getChartOfAccountsCode()).isEqualTo(UPDATED_CHART_OF_ACCOUNTS_CODE);
        assertThat(testChartOfAccountsCode.getChartOfAccountsClass()).isEqualTo(UPDATED_CHART_OF_ACCOUNTS_CLASS);
        assertThat(testChartOfAccountsCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository).save(testChartOfAccountsCode);
    }

    @Test
    @Transactional
    void putNonExistingChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chartOfAccountsCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void partialUpdateChartOfAccountsCodeWithPatch() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();

        // Update the chartOfAccountsCode using partial update
        ChartOfAccountsCode partialUpdatedChartOfAccountsCode = new ChartOfAccountsCode();
        partialUpdatedChartOfAccountsCode.setId(chartOfAccountsCode.getId());

        partialUpdatedChartOfAccountsCode.chartOfAccountsClass(UPDATED_CHART_OF_ACCOUNTS_CLASS).description(UPDATED_DESCRIPTION);

        restChartOfAccountsCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChartOfAccountsCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChartOfAccountsCode))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccountsCode testChartOfAccountsCode = chartOfAccountsCodeList.get(chartOfAccountsCodeList.size() - 1);
        assertThat(testChartOfAccountsCode.getChartOfAccountsCode()).isEqualTo(DEFAULT_CHART_OF_ACCOUNTS_CODE);
        assertThat(testChartOfAccountsCode.getChartOfAccountsClass()).isEqualTo(UPDATED_CHART_OF_ACCOUNTS_CLASS);
        assertThat(testChartOfAccountsCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateChartOfAccountsCodeWithPatch() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();

        // Update the chartOfAccountsCode using partial update
        ChartOfAccountsCode partialUpdatedChartOfAccountsCode = new ChartOfAccountsCode();
        partialUpdatedChartOfAccountsCode.setId(chartOfAccountsCode.getId());

        partialUpdatedChartOfAccountsCode
            .chartOfAccountsCode(UPDATED_CHART_OF_ACCOUNTS_CODE)
            .chartOfAccountsClass(UPDATED_CHART_OF_ACCOUNTS_CLASS)
            .description(UPDATED_DESCRIPTION);

        restChartOfAccountsCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChartOfAccountsCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChartOfAccountsCode))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);
        ChartOfAccountsCode testChartOfAccountsCode = chartOfAccountsCodeList.get(chartOfAccountsCodeList.size() - 1);
        assertThat(testChartOfAccountsCode.getChartOfAccountsCode()).isEqualTo(UPDATED_CHART_OF_ACCOUNTS_CODE);
        assertThat(testChartOfAccountsCode.getChartOfAccountsClass()).isEqualTo(UPDATED_CHART_OF_ACCOUNTS_CLASS);
        assertThat(testChartOfAccountsCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chartOfAccountsCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChartOfAccountsCode() throws Exception {
        int databaseSizeBeforeUpdate = chartOfAccountsCodeRepository.findAll().size();
        chartOfAccountsCode.setId(count.incrementAndGet());

        // Create the ChartOfAccountsCode
        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = chartOfAccountsCodeMapper.toDto(chartOfAccountsCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountsCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chartOfAccountsCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChartOfAccountsCode in the database
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(0)).save(chartOfAccountsCode);
    }

    @Test
    @Transactional
    void deleteChartOfAccountsCode() throws Exception {
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);

        int databaseSizeBeforeDelete = chartOfAccountsCodeRepository.findAll().size();

        // Delete the chartOfAccountsCode
        restChartOfAccountsCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, chartOfAccountsCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChartOfAccountsCode> chartOfAccountsCodeList = chartOfAccountsCodeRepository.findAll();
        assertThat(chartOfAccountsCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ChartOfAccountsCode in Elasticsearch
        verify(mockChartOfAccountsCodeSearchRepository, times(1)).deleteById(chartOfAccountsCode.getId());
    }

    @Test
    @Transactional
    void searchChartOfAccountsCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        chartOfAccountsCodeRepository.saveAndFlush(chartOfAccountsCode);
        when(mockChartOfAccountsCodeSearchRepository.search("id:" + chartOfAccountsCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(chartOfAccountsCode), PageRequest.of(0, 1), 1));

        // Search the chartOfAccountsCode
        restChartOfAccountsCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + chartOfAccountsCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccountsCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartOfAccountsCode").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CODE)))
            .andExpect(jsonPath("$.[*].chartOfAccountsClass").value(hasItem(DEFAULT_CHART_OF_ACCOUNTS_CLASS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
