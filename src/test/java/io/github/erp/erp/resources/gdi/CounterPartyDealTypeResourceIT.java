package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.CounterPartyDealType;
import io.github.erp.repository.CounterPartyDealTypeRepository;
import io.github.erp.repository.search.CounterPartyDealTypeSearchRepository;
import io.github.erp.service.dto.CounterPartyDealTypeDTO;
import io.github.erp.service.mapper.CounterPartyDealTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CounterPartyDealTypeResource;
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
 * Integration tests for the {@link CounterPartyDealTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CounterPartyDealTypeResourceIT {

    private static final String DEFAULT_COUNTERPARTY_DEAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_DEAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/counter-party-deal-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/counter-party-deal-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CounterPartyDealTypeRepository counterPartyDealTypeRepository;

    @Autowired
    private CounterPartyDealTypeMapper counterPartyDealTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CounterPartyDealTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CounterPartyDealTypeSearchRepository mockCounterPartyDealTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounterPartyDealTypeMockMvc;

    private CounterPartyDealType counterPartyDealType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterPartyDealType createEntity(EntityManager em) {
        CounterPartyDealType counterPartyDealType = new CounterPartyDealType()
            .counterpartyDealCode(DEFAULT_COUNTERPARTY_DEAL_CODE)
            .counterpartyDealTypeDetails(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS)
            .counterpartyDealTypeDescription(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);
        return counterPartyDealType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterPartyDealType createUpdatedEntity(EntityManager em) {
        CounterPartyDealType counterPartyDealType = new CounterPartyDealType()
            .counterpartyDealCode(UPDATED_COUNTERPARTY_DEAL_CODE)
            .counterpartyDealTypeDetails(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS)
            .counterpartyDealTypeDescription(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);
        return counterPartyDealType;
    }

    @BeforeEach
    public void initTest() {
        counterPartyDealType = createEntity(em);
    }

    @Test
    @Transactional
    void createCounterPartyDealType() throws Exception {
        int databaseSizeBeforeCreate = counterPartyDealTypeRepository.findAll().size();
        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);
        restCounterPartyDealTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CounterPartyDealType testCounterPartyDealType = counterPartyDealTypeList.get(counterPartyDealTypeList.size() - 1);
        assertThat(testCounterPartyDealType.getCounterpartyDealCode()).isEqualTo(DEFAULT_COUNTERPARTY_DEAL_CODE);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDetails()).isEqualTo(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDescription()).isEqualTo(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(1)).save(testCounterPartyDealType);
    }

    @Test
    @Transactional
    void createCounterPartyDealTypeWithExistingId() throws Exception {
        // Create the CounterPartyDealType with an existing ID
        counterPartyDealType.setId(1L);
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        int databaseSizeBeforeCreate = counterPartyDealTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounterPartyDealTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void checkCounterpartyDealCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterPartyDealTypeRepository.findAll().size();
        // set the field null
        counterPartyDealType.setCounterpartyDealCode(null);

        // Create the CounterPartyDealType, which fails.
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        restCounterPartyDealTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCounterpartyDealTypeDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterPartyDealTypeRepository.findAll().size();
        // set the field null
        counterPartyDealType.setCounterpartyDealTypeDetails(null);

        // Create the CounterPartyDealType, which fails.
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        restCounterPartyDealTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypes() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyDealType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyDealCode").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_CODE)))
            .andExpect(jsonPath("$.[*].counterpartyDealTypeDetails").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS)))
            .andExpect(
                jsonPath("$.[*].counterpartyDealTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getCounterPartyDealType() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get the counterPartyDealType
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, counterPartyDealType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(counterPartyDealType.getId().intValue()))
            .andExpect(jsonPath("$.counterpartyDealCode").value(DEFAULT_COUNTERPARTY_DEAL_CODE))
            .andExpect(jsonPath("$.counterpartyDealTypeDetails").value(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS))
            .andExpect(jsonPath("$.counterpartyDealTypeDescription").value(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCounterPartyDealTypesByIdFiltering() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        Long id = counterPartyDealType.getId();

        defaultCounterPartyDealTypeShouldBeFound("id.equals=" + id);
        defaultCounterPartyDealTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCounterPartyDealTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCounterPartyDealTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCounterPartyDealTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCounterPartyDealTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode equals to DEFAULT_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealCode.equals=" + DEFAULT_COUNTERPARTY_DEAL_CODE);

        // Get all the counterPartyDealTypeList where counterpartyDealCode equals to UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.equals=" + UPDATED_COUNTERPARTY_DEAL_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode not equals to DEFAULT_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.notEquals=" + DEFAULT_COUNTERPARTY_DEAL_CODE);

        // Get all the counterPartyDealTypeList where counterpartyDealCode not equals to UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealCode.notEquals=" + UPDATED_COUNTERPARTY_DEAL_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeIsInShouldWork() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode in DEFAULT_COUNTERPARTY_DEAL_CODE or UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldBeFound(
            "counterpartyDealCode.in=" + DEFAULT_COUNTERPARTY_DEAL_CODE + "," + UPDATED_COUNTERPARTY_DEAL_CODE
        );

        // Get all the counterPartyDealTypeList where counterpartyDealCode equals to UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.in=" + UPDATED_COUNTERPARTY_DEAL_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode is not null
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealCode.specified=true");

        // Get all the counterPartyDealTypeList where counterpartyDealCode is null
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeContainsSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode contains DEFAULT_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealCode.contains=" + DEFAULT_COUNTERPARTY_DEAL_CODE);

        // Get all the counterPartyDealTypeList where counterpartyDealCode contains UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.contains=" + UPDATED_COUNTERPARTY_DEAL_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealCodeNotContainsSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealCode does not contain DEFAULT_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealCode.doesNotContain=" + DEFAULT_COUNTERPARTY_DEAL_CODE);

        // Get all the counterPartyDealTypeList where counterpartyDealCode does not contain UPDATED_COUNTERPARTY_DEAL_CODE
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealCode.doesNotContain=" + UPDATED_COUNTERPARTY_DEAL_CODE);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails equals to DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealTypeDetails.equals=" + DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails equals to UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.equals=" + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails not equals to DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.notEquals=" + DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails not equals to UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealTypeDetails.notEquals=" + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails in DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS or UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldBeFound(
            "counterpartyDealTypeDetails.in=" + DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS + "," + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        );

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails equals to UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.in=" + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails is not null
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealTypeDetails.specified=true");

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails is null
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsContainsSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails contains DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealTypeDetails.contains=" + DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails contains UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.contains=" + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllCounterPartyDealTypesByCounterpartyDealTypeDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails does not contain DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldNotBeFound("counterpartyDealTypeDetails.doesNotContain=" + DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS);

        // Get all the counterPartyDealTypeList where counterpartyDealTypeDetails does not contain UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS
        defaultCounterPartyDealTypeShouldBeFound("counterpartyDealTypeDetails.doesNotContain=" + UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCounterPartyDealTypeShouldBeFound(String filter) throws Exception {
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyDealType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyDealCode").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_CODE)))
            .andExpect(jsonPath("$.[*].counterpartyDealTypeDetails").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS)))
            .andExpect(
                jsonPath("$.[*].counterpartyDealTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCounterPartyDealTypeShouldNotBeFound(String filter) throws Exception {
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounterPartyDealType() throws Exception {
        // Get the counterPartyDealType
        restCounterPartyDealTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCounterPartyDealType() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();

        // Update the counterPartyDealType
        CounterPartyDealType updatedCounterPartyDealType = counterPartyDealTypeRepository.findById(counterPartyDealType.getId()).get();
        // Disconnect from session so that the updates on updatedCounterPartyDealType are not directly saved in db
        em.detach(updatedCounterPartyDealType);
        updatedCounterPartyDealType
            .counterpartyDealCode(UPDATED_COUNTERPARTY_DEAL_CODE)
            .counterpartyDealTypeDetails(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS)
            .counterpartyDealTypeDescription(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(updatedCounterPartyDealType);

        restCounterPartyDealTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterPartyDealTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyDealType testCounterPartyDealType = counterPartyDealTypeList.get(counterPartyDealTypeList.size() - 1);
        assertThat(testCounterPartyDealType.getCounterpartyDealCode()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_CODE);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDetails()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDescription()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository).save(testCounterPartyDealType);
    }

    @Test
    @Transactional
    void putNonExistingCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterPartyDealTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void partialUpdateCounterPartyDealTypeWithPatch() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();

        // Update the counterPartyDealType using partial update
        CounterPartyDealType partialUpdatedCounterPartyDealType = new CounterPartyDealType();
        partialUpdatedCounterPartyDealType.setId(counterPartyDealType.getId());

        partialUpdatedCounterPartyDealType
            .counterpartyDealCode(UPDATED_COUNTERPARTY_DEAL_CODE)
            .counterpartyDealTypeDetails(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS)
            .counterpartyDealTypeDescription(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);

        restCounterPartyDealTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterPartyDealType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterPartyDealType))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyDealType testCounterPartyDealType = counterPartyDealTypeList.get(counterPartyDealTypeList.size() - 1);
        assertThat(testCounterPartyDealType.getCounterpartyDealCode()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_CODE);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDetails()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDescription()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCounterPartyDealTypeWithPatch() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();

        // Update the counterPartyDealType using partial update
        CounterPartyDealType partialUpdatedCounterPartyDealType = new CounterPartyDealType();
        partialUpdatedCounterPartyDealType.setId(counterPartyDealType.getId());

        partialUpdatedCounterPartyDealType
            .counterpartyDealCode(UPDATED_COUNTERPARTY_DEAL_CODE)
            .counterpartyDealTypeDetails(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS)
            .counterpartyDealTypeDescription(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);

        restCounterPartyDealTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterPartyDealType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterPartyDealType))
            )
            .andExpect(status().isOk());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterPartyDealType testCounterPartyDealType = counterPartyDealTypeList.get(counterPartyDealTypeList.size() - 1);
        assertThat(testCounterPartyDealType.getCounterpartyDealCode()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_CODE);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDetails()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DETAILS);
        assertThat(testCounterPartyDealType.getCounterpartyDealTypeDescription()).isEqualTo(UPDATED_COUNTERPARTY_DEAL_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counterPartyDealTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounterPartyDealType() throws Exception {
        int databaseSizeBeforeUpdate = counterPartyDealTypeRepository.findAll().size();
        counterPartyDealType.setId(count.incrementAndGet());

        // Create the CounterPartyDealType
        CounterPartyDealTypeDTO counterPartyDealTypeDTO = counterPartyDealTypeMapper.toDto(counterPartyDealType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterPartyDealTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterPartyDealTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterPartyDealType in the database
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(0)).save(counterPartyDealType);
    }

    @Test
    @Transactional
    void deleteCounterPartyDealType() throws Exception {
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);

        int databaseSizeBeforeDelete = counterPartyDealTypeRepository.findAll().size();

        // Delete the counterPartyDealType
        restCounterPartyDealTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, counterPartyDealType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CounterPartyDealType> counterPartyDealTypeList = counterPartyDealTypeRepository.findAll();
        assertThat(counterPartyDealTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CounterPartyDealType in Elasticsearch
        verify(mockCounterPartyDealTypeSearchRepository, times(1)).deleteById(counterPartyDealType.getId());
    }

    @Test
    @Transactional
    void searchCounterPartyDealType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        counterPartyDealTypeRepository.saveAndFlush(counterPartyDealType);
        when(mockCounterPartyDealTypeSearchRepository.search("id:" + counterPartyDealType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(counterPartyDealType), PageRequest.of(0, 1), 1));

        // Search the counterPartyDealType
        restCounterPartyDealTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + counterPartyDealType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterPartyDealType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyDealCode").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_CODE)))
            .andExpect(jsonPath("$.[*].counterpartyDealTypeDetails").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DETAILS)))
            .andExpect(
                jsonPath("$.[*].counterpartyDealTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_DEAL_TYPE_DESCRIPTION.toString()))
            );
    }
}
