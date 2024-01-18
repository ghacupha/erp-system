package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.LoanRestructureFlag;
import io.github.erp.domain.enumeration.FlagCodes;
import io.github.erp.repository.LoanRestructureFlagRepository;
import io.github.erp.repository.search.LoanRestructureFlagSearchRepository;
import io.github.erp.service.criteria.LoanRestructureFlagCriteria;
import io.github.erp.service.dto.LoanRestructureFlagDTO;
import io.github.erp.service.mapper.LoanRestructureFlagMapper;
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
 * Integration tests for the {@link LoanRestructureFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoanRestructureFlagResourceIT {

    private static final FlagCodes DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE = FlagCodes.Y;
    private static final FlagCodes UPDATED_LOAN_RESTRUCTURE_FLAG_CODE = FlagCodes.N;

    private static final String DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loan-restructure-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loan-restructure-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanRestructureFlagRepository loanRestructureFlagRepository;

    @Autowired
    private LoanRestructureFlagMapper loanRestructureFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanRestructureFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanRestructureFlagSearchRepository mockLoanRestructureFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanRestructureFlagMockMvc;

    private LoanRestructureFlag loanRestructureFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRestructureFlag createEntity(EntityManager em) {
        LoanRestructureFlag loanRestructureFlag = new LoanRestructureFlag()
            .loanRestructureFlagCode(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE)
            .loanRestructureFlagType(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE)
            .loanRestructureFlagDetails(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS);
        return loanRestructureFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRestructureFlag createUpdatedEntity(EntityManager em) {
        LoanRestructureFlag loanRestructureFlag = new LoanRestructureFlag()
            .loanRestructureFlagCode(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE)
            .loanRestructureFlagType(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE)
            .loanRestructureFlagDetails(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);
        return loanRestructureFlag;
    }

    @BeforeEach
    public void initTest() {
        loanRestructureFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeCreate = loanRestructureFlagRepository.findAll().size();
        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);
        restLoanRestructureFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeCreate + 1);
        LoanRestructureFlag testLoanRestructureFlag = loanRestructureFlagList.get(loanRestructureFlagList.size() - 1);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagCode()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagType()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagDetails()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(1)).save(testLoanRestructureFlag);
    }

    @Test
    @Transactional
    void createLoanRestructureFlagWithExistingId() throws Exception {
        // Create the LoanRestructureFlag with an existing ID
        loanRestructureFlag.setId(1L);
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        int databaseSizeBeforeCreate = loanRestructureFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanRestructureFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void checkLoanRestructureFlagCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRestructureFlagRepository.findAll().size();
        // set the field null
        loanRestructureFlag.setLoanRestructureFlagCode(null);

        // Create the LoanRestructureFlag, which fails.
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        restLoanRestructureFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanRestructureFlagTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRestructureFlagRepository.findAll().size();
        // set the field null
        loanRestructureFlag.setLoanRestructureFlagType(null);

        // Create the LoanRestructureFlag, which fails.
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        restLoanRestructureFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlags() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE.toString())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureFlagDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanRestructureFlag() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get the loanRestructureFlag
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, loanRestructureFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanRestructureFlag.getId().intValue()))
            .andExpect(jsonPath("$.loanRestructureFlagCode").value(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE.toString()))
            .andExpect(jsonPath("$.loanRestructureFlagType").value(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE))
            .andExpect(jsonPath("$.loanRestructureFlagDetails").value(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanRestructureFlagsByIdFiltering() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        Long id = loanRestructureFlag.getId();

        defaultLoanRestructureFlagShouldBeFound("id.equals=" + id);
        defaultLoanRestructureFlagShouldNotBeFound("id.notEquals=" + id);

        defaultLoanRestructureFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanRestructureFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanRestructureFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanRestructureFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode equals to DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagCode.equals=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode equals to UPDATED_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagCode.equals=" + UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode not equals to DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagCode.notEquals=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode not equals to UPDATED_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagCode.notEquals=" + UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode in DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE or UPDATED_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldBeFound(
            "loanRestructureFlagCode.in=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE + "," + UPDATED_LOAN_RESTRUCTURE_FLAG_CODE
        );

        // Get all the loanRestructureFlagList where loanRestructureFlagCode equals to UPDATED_LOAN_RESTRUCTURE_FLAG_CODE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagCode.in=" + UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagCode is not null
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagCode.specified=true");

        // Get all the loanRestructureFlagList where loanRestructureFlagCode is null
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType equals to DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagType.equals=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE);

        // Get all the loanRestructureFlagList where loanRestructureFlagType equals to UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.equals=" + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType not equals to DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.notEquals=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE);

        // Get all the loanRestructureFlagList where loanRestructureFlagType not equals to UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagType.notEquals=" + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType in DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE or UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldBeFound(
            "loanRestructureFlagType.in=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE + "," + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        );

        // Get all the loanRestructureFlagList where loanRestructureFlagType equals to UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.in=" + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType is not null
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagType.specified=true");

        // Get all the loanRestructureFlagList where loanRestructureFlagType is null
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType contains DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagType.contains=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE);

        // Get all the loanRestructureFlagList where loanRestructureFlagType contains UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.contains=" + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureFlagsByLoanRestructureFlagTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        // Get all the loanRestructureFlagList where loanRestructureFlagType does not contain DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldNotBeFound("loanRestructureFlagType.doesNotContain=" + DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE);

        // Get all the loanRestructureFlagList where loanRestructureFlagType does not contain UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE
        defaultLoanRestructureFlagShouldBeFound("loanRestructureFlagType.doesNotContain=" + UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanRestructureFlagShouldBeFound(String filter) throws Exception {
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE.toString())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureFlagDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanRestructureFlagShouldNotBeFound(String filter) throws Exception {
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanRestructureFlag() throws Exception {
        // Get the loanRestructureFlag
        restLoanRestructureFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanRestructureFlag() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();

        // Update the loanRestructureFlag
        LoanRestructureFlag updatedLoanRestructureFlag = loanRestructureFlagRepository.findById(loanRestructureFlag.getId()).get();
        // Disconnect from session so that the updates on updatedLoanRestructureFlag are not directly saved in db
        em.detach(updatedLoanRestructureFlag);
        updatedLoanRestructureFlag
            .loanRestructureFlagCode(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE)
            .loanRestructureFlagType(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE)
            .loanRestructureFlagDetails(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(updatedLoanRestructureFlag);

        restLoanRestructureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRestructureFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureFlag testLoanRestructureFlag = loanRestructureFlagList.get(loanRestructureFlagList.size() - 1);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagType()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagDetails()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository).save(testLoanRestructureFlag);
    }

    @Test
    @Transactional
    void putNonExistingLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRestructureFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void partialUpdateLoanRestructureFlagWithPatch() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();

        // Update the loanRestructureFlag using partial update
        LoanRestructureFlag partialUpdatedLoanRestructureFlag = new LoanRestructureFlag();
        partialUpdatedLoanRestructureFlag.setId(loanRestructureFlag.getId());

        partialUpdatedLoanRestructureFlag
            .loanRestructureFlagCode(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE)
            .loanRestructureFlagType(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE)
            .loanRestructureFlagDetails(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);

        restLoanRestructureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRestructureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRestructureFlag))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureFlag testLoanRestructureFlag = loanRestructureFlagList.get(loanRestructureFlagList.size() - 1);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagType()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagDetails()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanRestructureFlagWithPatch() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();

        // Update the loanRestructureFlag using partial update
        LoanRestructureFlag partialUpdatedLoanRestructureFlag = new LoanRestructureFlag();
        partialUpdatedLoanRestructureFlag.setId(loanRestructureFlag.getId());

        partialUpdatedLoanRestructureFlag
            .loanRestructureFlagCode(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE)
            .loanRestructureFlagType(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE)
            .loanRestructureFlagDetails(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);

        restLoanRestructureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRestructureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRestructureFlag))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureFlag testLoanRestructureFlag = loanRestructureFlagList.get(loanRestructureFlagList.size() - 1);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_CODE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagType()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_TYPE);
        assertThat(testLoanRestructureFlag.getLoanRestructureFlagDetails()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanRestructureFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanRestructureFlag() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureFlagRepository.findAll().size();
        loanRestructureFlag.setId(count.incrementAndGet());

        // Create the LoanRestructureFlag
        LoanRestructureFlagDTO loanRestructureFlagDTO = loanRestructureFlagMapper.toDto(loanRestructureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRestructureFlag in the database
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(0)).save(loanRestructureFlag);
    }

    @Test
    @Transactional
    void deleteLoanRestructureFlag() throws Exception {
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);

        int databaseSizeBeforeDelete = loanRestructureFlagRepository.findAll().size();

        // Delete the loanRestructureFlag
        restLoanRestructureFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanRestructureFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanRestructureFlag> loanRestructureFlagList = loanRestructureFlagRepository.findAll();
        assertThat(loanRestructureFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanRestructureFlag in Elasticsearch
        verify(mockLoanRestructureFlagSearchRepository, times(1)).deleteById(loanRestructureFlag.getId());
    }

    @Test
    @Transactional
    void searchLoanRestructureFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanRestructureFlagRepository.saveAndFlush(loanRestructureFlag);
        when(mockLoanRestructureFlagSearchRepository.search("id:" + loanRestructureFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanRestructureFlag), PageRequest.of(0, 1), 1));

        // Search the loanRestructureFlag
        restLoanRestructureFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanRestructureFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_CODE.toString())))
            .andExpect(jsonPath("$.[*].loanRestructureFlagType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureFlagDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_FLAG_DETAILS.toString())));
    }
}
