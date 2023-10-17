package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.InterestCalcMethod;
import io.github.erp.repository.InterestCalcMethodRepository;
import io.github.erp.repository.search.InterestCalcMethodSearchRepository;
import io.github.erp.service.criteria.InterestCalcMethodCriteria;
import io.github.erp.service.dto.InterestCalcMethodDTO;
import io.github.erp.service.mapper.InterestCalcMethodMapper;
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
 * Integration tests for the {@link InterestCalcMethodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterestCalcMethodResourceIT {

    private static final String DEFAULT_INTEREST_CALCULATION_METHOD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_CALCULATION_METHOD_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_CALCULATION_MTHOD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_CALCULATION_METHOD_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interest-calc-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/interest-calc-methods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterestCalcMethodRepository interestCalcMethodRepository;

    @Autowired
    private InterestCalcMethodMapper interestCalcMethodMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InterestCalcMethodSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterestCalcMethodSearchRepository mockInterestCalcMethodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterestCalcMethodMockMvc;

    private InterestCalcMethod interestCalcMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterestCalcMethod createEntity(EntityManager em) {
        InterestCalcMethod interestCalcMethod = new InterestCalcMethod()
            .interestCalculationMethodCode(DEFAULT_INTEREST_CALCULATION_METHOD_CODE)
            .interestCalculationMthodType(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE)
            .interestCalculationMethodDetails(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS);
        return interestCalcMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterestCalcMethod createUpdatedEntity(EntityManager em) {
        InterestCalcMethod interestCalcMethod = new InterestCalcMethod()
            .interestCalculationMethodCode(UPDATED_INTEREST_CALCULATION_METHOD_CODE)
            .interestCalculationMthodType(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE)
            .interestCalculationMethodDetails(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);
        return interestCalcMethod;
    }

    @BeforeEach
    public void initTest() {
        interestCalcMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createInterestCalcMethod() throws Exception {
        int databaseSizeBeforeCreate = interestCalcMethodRepository.findAll().size();
        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);
        restInterestCalcMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeCreate + 1);
        InterestCalcMethod testInterestCalcMethod = interestCalcMethodList.get(interestCalcMethodList.size() - 1);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodCode()).isEqualTo(DEFAULT_INTEREST_CALCULATION_METHOD_CODE);
        assertThat(testInterestCalcMethod.getInterestCalculationMthodType()).isEqualTo(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodDetails()).isEqualTo(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(1)).save(testInterestCalcMethod);
    }

    @Test
    @Transactional
    void createInterestCalcMethodWithExistingId() throws Exception {
        // Create the InterestCalcMethod with an existing ID
        interestCalcMethod.setId(1L);
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        int databaseSizeBeforeCreate = interestCalcMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterestCalcMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeCreate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void checkInterestCalculationMethodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interestCalcMethodRepository.findAll().size();
        // set the field null
        interestCalcMethod.setInterestCalculationMethodCode(null);

        // Create the InterestCalcMethod, which fails.
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        restInterestCalcMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInterestCalculationMthodTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interestCalcMethodRepository.findAll().size();
        // set the field null
        interestCalcMethod.setInterestCalculationMthodType(null);

        // Create the InterestCalcMethod, which fails.
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        restInterestCalcMethodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethods() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interestCalcMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestCalculationMethodCode").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_CODE)))
            .andExpect(jsonPath("$.[*].interestCalculationMthodType").value(hasItem(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE)))
            .andExpect(
                jsonPath("$.[*].interestCalculationMethodDetails").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getInterestCalcMethod() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get the interestCalcMethod
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, interestCalcMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interestCalcMethod.getId().intValue()))
            .andExpect(jsonPath("$.interestCalculationMethodCode").value(DEFAULT_INTEREST_CALCULATION_METHOD_CODE))
            .andExpect(jsonPath("$.interestCalculationMthodType").value(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE))
            .andExpect(jsonPath("$.interestCalculationMethodDetails").value(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getInterestCalcMethodsByIdFiltering() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        Long id = interestCalcMethod.getId();

        defaultInterestCalcMethodShouldBeFound("id.equals=" + id);
        defaultInterestCalcMethodShouldNotBeFound("id.notEquals=" + id);

        defaultInterestCalcMethodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterestCalcMethodShouldNotBeFound("id.greaterThan=" + id);

        defaultInterestCalcMethodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterestCalcMethodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode equals to DEFAULT_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMethodCode.equals=" + DEFAULT_INTEREST_CALCULATION_METHOD_CODE);

        // Get all the interestCalcMethodList where interestCalculationMethodCode equals to UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMethodCode.equals=" + UPDATED_INTEREST_CALCULATION_METHOD_CODE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode not equals to DEFAULT_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMethodCode.notEquals=" + DEFAULT_INTEREST_CALCULATION_METHOD_CODE);

        // Get all the interestCalcMethodList where interestCalculationMethodCode not equals to UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMethodCode.notEquals=" + UPDATED_INTEREST_CALCULATION_METHOD_CODE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode in DEFAULT_INTEREST_CALCULATION_METHOD_CODE or UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldBeFound(
            "interestCalculationMethodCode.in=" + DEFAULT_INTEREST_CALCULATION_METHOD_CODE + "," + UPDATED_INTEREST_CALCULATION_METHOD_CODE
        );

        // Get all the interestCalcMethodList where interestCalculationMethodCode equals to UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMethodCode.in=" + UPDATED_INTEREST_CALCULATION_METHOD_CODE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode is not null
        defaultInterestCalcMethodShouldBeFound("interestCalculationMethodCode.specified=true");

        // Get all the interestCalcMethodList where interestCalculationMethodCode is null
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMethodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeContainsSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode contains DEFAULT_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMethodCode.contains=" + DEFAULT_INTEREST_CALCULATION_METHOD_CODE);

        // Get all the interestCalcMethodList where interestCalculationMethodCode contains UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMethodCode.contains=" + UPDATED_INTEREST_CALCULATION_METHOD_CODE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMethodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMethodCode does not contain DEFAULT_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldNotBeFound(
            "interestCalculationMethodCode.doesNotContain=" + DEFAULT_INTEREST_CALCULATION_METHOD_CODE
        );

        // Get all the interestCalcMethodList where interestCalculationMethodCode does not contain UPDATED_INTEREST_CALCULATION_METHOD_CODE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMethodCode.doesNotContain=" + UPDATED_INTEREST_CALCULATION_METHOD_CODE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType equals to DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMthodType.equals=" + DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE);

        // Get all the interestCalcMethodList where interestCalculationMthodType equals to UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.equals=" + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType not equals to DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.notEquals=" + DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE);

        // Get all the interestCalcMethodList where interestCalculationMthodType not equals to UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMthodType.notEquals=" + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeIsInShouldWork() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType in DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE or UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldBeFound(
            "interestCalculationMthodType.in=" + DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE + "," + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        );

        // Get all the interestCalcMethodList where interestCalculationMthodType equals to UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.in=" + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType is not null
        defaultInterestCalcMethodShouldBeFound("interestCalculationMthodType.specified=true");

        // Get all the interestCalcMethodList where interestCalculationMthodType is null
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.specified=false");
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeContainsSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType contains DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMthodType.contains=" + DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE);

        // Get all the interestCalcMethodList where interestCalculationMthodType contains UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.contains=" + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
    }

    @Test
    @Transactional
    void getAllInterestCalcMethodsByInterestCalculationMthodTypeNotContainsSomething() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        // Get all the interestCalcMethodList where interestCalculationMthodType does not contain DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldNotBeFound("interestCalculationMthodType.doesNotContain=" + DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE);

        // Get all the interestCalcMethodList where interestCalculationMthodType does not contain UPDATED_INTEREST_CALCULATION_MTHOD_TYPE
        defaultInterestCalcMethodShouldBeFound("interestCalculationMthodType.doesNotContain=" + UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterestCalcMethodShouldBeFound(String filter) throws Exception {
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interestCalcMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestCalculationMethodCode").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_CODE)))
            .andExpect(jsonPath("$.[*].interestCalculationMthodType").value(hasItem(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE)))
            .andExpect(
                jsonPath("$.[*].interestCalculationMethodDetails").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterestCalcMethodShouldNotBeFound(String filter) throws Exception {
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterestCalcMethod() throws Exception {
        // Get the interestCalcMethod
        restInterestCalcMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInterestCalcMethod() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();

        // Update the interestCalcMethod
        InterestCalcMethod updatedInterestCalcMethod = interestCalcMethodRepository.findById(interestCalcMethod.getId()).get();
        // Disconnect from session so that the updates on updatedInterestCalcMethod are not directly saved in db
        em.detach(updatedInterestCalcMethod);
        updatedInterestCalcMethod
            .interestCalculationMethodCode(UPDATED_INTEREST_CALCULATION_METHOD_CODE)
            .interestCalculationMthodType(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE)
            .interestCalculationMethodDetails(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(updatedInterestCalcMethod);

        restInterestCalcMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interestCalcMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);
        InterestCalcMethod testInterestCalcMethod = interestCalcMethodList.get(interestCalcMethodList.size() - 1);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodCode()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_CODE);
        assertThat(testInterestCalcMethod.getInterestCalculationMthodType()).isEqualTo(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodDetails()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository).save(testInterestCalcMethod);
    }

    @Test
    @Transactional
    void putNonExistingInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interestCalcMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void partialUpdateInterestCalcMethodWithPatch() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();

        // Update the interestCalcMethod using partial update
        InterestCalcMethod partialUpdatedInterestCalcMethod = new InterestCalcMethod();
        partialUpdatedInterestCalcMethod.setId(interestCalcMethod.getId());

        partialUpdatedInterestCalcMethod
            .interestCalculationMethodCode(UPDATED_INTEREST_CALCULATION_METHOD_CODE)
            .interestCalculationMthodType(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE)
            .interestCalculationMethodDetails(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);

        restInterestCalcMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterestCalcMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterestCalcMethod))
            )
            .andExpect(status().isOk());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);
        InterestCalcMethod testInterestCalcMethod = interestCalcMethodList.get(interestCalcMethodList.size() - 1);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodCode()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_CODE);
        assertThat(testInterestCalcMethod.getInterestCalculationMthodType()).isEqualTo(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodDetails()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateInterestCalcMethodWithPatch() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();

        // Update the interestCalcMethod using partial update
        InterestCalcMethod partialUpdatedInterestCalcMethod = new InterestCalcMethod();
        partialUpdatedInterestCalcMethod.setId(interestCalcMethod.getId());

        partialUpdatedInterestCalcMethod
            .interestCalculationMethodCode(UPDATED_INTEREST_CALCULATION_METHOD_CODE)
            .interestCalculationMthodType(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE)
            .interestCalculationMethodDetails(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);

        restInterestCalcMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterestCalcMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterestCalcMethod))
            )
            .andExpect(status().isOk());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);
        InterestCalcMethod testInterestCalcMethod = interestCalcMethodList.get(interestCalcMethodList.size() - 1);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodCode()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_CODE);
        assertThat(testInterestCalcMethod.getInterestCalculationMthodType()).isEqualTo(UPDATED_INTEREST_CALCULATION_MTHOD_TYPE);
        assertThat(testInterestCalcMethod.getInterestCalculationMethodDetails()).isEqualTo(UPDATED_INTEREST_CALCULATION_METHOD_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interestCalcMethodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterestCalcMethod() throws Exception {
        int databaseSizeBeforeUpdate = interestCalcMethodRepository.findAll().size();
        interestCalcMethod.setId(count.incrementAndGet());

        // Create the InterestCalcMethod
        InterestCalcMethodDTO interestCalcMethodDTO = interestCalcMethodMapper.toDto(interestCalcMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterestCalcMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interestCalcMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterestCalcMethod in the database
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(0)).save(interestCalcMethod);
    }

    @Test
    @Transactional
    void deleteInterestCalcMethod() throws Exception {
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);

        int databaseSizeBeforeDelete = interestCalcMethodRepository.findAll().size();

        // Delete the interestCalcMethod
        restInterestCalcMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, interestCalcMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterestCalcMethod> interestCalcMethodList = interestCalcMethodRepository.findAll();
        assertThat(interestCalcMethodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InterestCalcMethod in Elasticsearch
        verify(mockInterestCalcMethodSearchRepository, times(1)).deleteById(interestCalcMethod.getId());
    }

    @Test
    @Transactional
    void searchInterestCalcMethod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        interestCalcMethodRepository.saveAndFlush(interestCalcMethod);
        when(mockInterestCalcMethodSearchRepository.search("id:" + interestCalcMethod.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(interestCalcMethod), PageRequest.of(0, 1), 1));

        // Search the interestCalcMethod
        restInterestCalcMethodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + interestCalcMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interestCalcMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].interestCalculationMethodCode").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_CODE)))
            .andExpect(jsonPath("$.[*].interestCalculationMthodType").value(hasItem(DEFAULT_INTEREST_CALCULATION_MTHOD_TYPE)))
            .andExpect(
                jsonPath("$.[*].interestCalculationMethodDetails").value(hasItem(DEFAULT_INTEREST_CALCULATION_METHOD_DETAILS.toString()))
            );
    }
}
