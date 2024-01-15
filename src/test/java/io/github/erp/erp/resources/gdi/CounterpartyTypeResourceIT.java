package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CounterpartyType;
import io.github.erp.repository.CounterpartyTypeRepository;
import io.github.erp.repository.search.CounterpartyTypeSearchRepository;
import io.github.erp.service.dto.CounterpartyTypeDTO;
import io.github.erp.service.mapper.CounterpartyTypeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CounterpartyTypeResource;
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
 * Integration tests for the {@link CounterpartyTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CounterpartyTypeResourceIT {

    private static final String DEFAULT_COUNTERPARTY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_PARTY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_PARTY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/counterparty-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/counterparty-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CounterpartyTypeRepository counterpartyTypeRepository;

    @Autowired
    private CounterpartyTypeMapper counterpartyTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CounterpartyTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CounterpartyTypeSearchRepository mockCounterpartyTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCounterpartyTypeMockMvc;

    private CounterpartyType counterpartyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterpartyType createEntity(EntityManager em) {
        CounterpartyType counterpartyType = new CounterpartyType()
            .counterpartyTypeCode(DEFAULT_COUNTERPARTY_TYPE_CODE)
            .counterPartyType(DEFAULT_COUNTER_PARTY_TYPE)
            .counterpartyTypeDescription(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION);
        return counterpartyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CounterpartyType createUpdatedEntity(EntityManager em) {
        CounterpartyType counterpartyType = new CounterpartyType()
            .counterpartyTypeCode(UPDATED_COUNTERPARTY_TYPE_CODE)
            .counterPartyType(UPDATED_COUNTER_PARTY_TYPE)
            .counterpartyTypeDescription(UPDATED_COUNTERPARTY_TYPE_DESCRIPTION);
        return counterpartyType;
    }

    @BeforeEach
    public void initTest() {
        counterpartyType = createEntity(em);
    }

    @Test
    @Transactional
    void createCounterpartyType() throws Exception {
        int databaseSizeBeforeCreate = counterpartyTypeRepository.findAll().size();
        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);
        restCounterpartyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CounterpartyType testCounterpartyType = counterpartyTypeList.get(counterpartyTypeList.size() - 1);
        assertThat(testCounterpartyType.getCounterpartyTypeCode()).isEqualTo(DEFAULT_COUNTERPARTY_TYPE_CODE);
        assertThat(testCounterpartyType.getCounterPartyType()).isEqualTo(DEFAULT_COUNTER_PARTY_TYPE);
        assertThat(testCounterpartyType.getCounterpartyTypeDescription()).isEqualTo(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(1)).save(testCounterpartyType);
    }

    @Test
    @Transactional
    void createCounterpartyTypeWithExistingId() throws Exception {
        // Create the CounterpartyType with an existing ID
        counterpartyType.setId(1L);
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        int databaseSizeBeforeCreate = counterpartyTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounterpartyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void checkCounterpartyTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterpartyTypeRepository.findAll().size();
        // set the field null
        counterpartyType.setCounterpartyTypeCode(null);

        // Create the CounterpartyType, which fails.
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        restCounterpartyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCounterPartyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = counterpartyTypeRepository.findAll().size();
        // set the field null
        counterpartyType.setCounterPartyType(null);

        // Create the CounterpartyType, which fails.
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        restCounterpartyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypes() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterpartyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyTypeCode").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].counterPartyType").value(hasItem(DEFAULT_COUNTER_PARTY_TYPE)))
            .andExpect(jsonPath("$.[*].counterpartyTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCounterpartyType() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get the counterpartyType
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, counterpartyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(counterpartyType.getId().intValue()))
            .andExpect(jsonPath("$.counterpartyTypeCode").value(DEFAULT_COUNTERPARTY_TYPE_CODE))
            .andExpect(jsonPath("$.counterPartyType").value(DEFAULT_COUNTER_PARTY_TYPE))
            .andExpect(jsonPath("$.counterpartyTypeDescription").value(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCounterpartyTypesByIdFiltering() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        Long id = counterpartyType.getId();

        defaultCounterpartyTypeShouldBeFound("id.equals=" + id);
        defaultCounterpartyTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCounterpartyTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCounterpartyTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCounterpartyTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCounterpartyTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode equals to DEFAULT_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldBeFound("counterpartyTypeCode.equals=" + DEFAULT_COUNTERPARTY_TYPE_CODE);

        // Get all the counterpartyTypeList where counterpartyTypeCode equals to UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.equals=" + UPDATED_COUNTERPARTY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode not equals to DEFAULT_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.notEquals=" + DEFAULT_COUNTERPARTY_TYPE_CODE);

        // Get all the counterpartyTypeList where counterpartyTypeCode not equals to UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldBeFound("counterpartyTypeCode.notEquals=" + UPDATED_COUNTERPARTY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode in DEFAULT_COUNTERPARTY_TYPE_CODE or UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldBeFound(
            "counterpartyTypeCode.in=" + DEFAULT_COUNTERPARTY_TYPE_CODE + "," + UPDATED_COUNTERPARTY_TYPE_CODE
        );

        // Get all the counterpartyTypeList where counterpartyTypeCode equals to UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.in=" + UPDATED_COUNTERPARTY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode is not null
        defaultCounterpartyTypeShouldBeFound("counterpartyTypeCode.specified=true");

        // Get all the counterpartyTypeList where counterpartyTypeCode is null
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode contains DEFAULT_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldBeFound("counterpartyTypeCode.contains=" + DEFAULT_COUNTERPARTY_TYPE_CODE);

        // Get all the counterpartyTypeList where counterpartyTypeCode contains UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.contains=" + UPDATED_COUNTERPARTY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterpartyTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterpartyTypeCode does not contain DEFAULT_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldNotBeFound("counterpartyTypeCode.doesNotContain=" + DEFAULT_COUNTERPARTY_TYPE_CODE);

        // Get all the counterpartyTypeList where counterpartyTypeCode does not contain UPDATED_COUNTERPARTY_TYPE_CODE
        defaultCounterpartyTypeShouldBeFound("counterpartyTypeCode.doesNotContain=" + UPDATED_COUNTERPARTY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType equals to DEFAULT_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldBeFound("counterPartyType.equals=" + DEFAULT_COUNTER_PARTY_TYPE);

        // Get all the counterpartyTypeList where counterPartyType equals to UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.equals=" + UPDATED_COUNTER_PARTY_TYPE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType not equals to DEFAULT_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.notEquals=" + DEFAULT_COUNTER_PARTY_TYPE);

        // Get all the counterpartyTypeList where counterPartyType not equals to UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldBeFound("counterPartyType.notEquals=" + UPDATED_COUNTER_PARTY_TYPE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType in DEFAULT_COUNTER_PARTY_TYPE or UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldBeFound("counterPartyType.in=" + DEFAULT_COUNTER_PARTY_TYPE + "," + UPDATED_COUNTER_PARTY_TYPE);

        // Get all the counterpartyTypeList where counterPartyType equals to UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.in=" + UPDATED_COUNTER_PARTY_TYPE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType is not null
        defaultCounterpartyTypeShouldBeFound("counterPartyType.specified=true");

        // Get all the counterpartyTypeList where counterPartyType is null
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.specified=false");
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeContainsSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType contains DEFAULT_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldBeFound("counterPartyType.contains=" + DEFAULT_COUNTER_PARTY_TYPE);

        // Get all the counterpartyTypeList where counterPartyType contains UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.contains=" + UPDATED_COUNTER_PARTY_TYPE);
    }

    @Test
    @Transactional
    void getAllCounterpartyTypesByCounterPartyTypeNotContainsSomething() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        // Get all the counterpartyTypeList where counterPartyType does not contain DEFAULT_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldNotBeFound("counterPartyType.doesNotContain=" + DEFAULT_COUNTER_PARTY_TYPE);

        // Get all the counterpartyTypeList where counterPartyType does not contain UPDATED_COUNTER_PARTY_TYPE
        defaultCounterpartyTypeShouldBeFound("counterPartyType.doesNotContain=" + UPDATED_COUNTER_PARTY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCounterpartyTypeShouldBeFound(String filter) throws Exception {
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterpartyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyTypeCode").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].counterPartyType").value(hasItem(DEFAULT_COUNTER_PARTY_TYPE)))
            .andExpect(jsonPath("$.[*].counterpartyTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCounterpartyTypeShouldNotBeFound(String filter) throws Exception {
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounterpartyType() throws Exception {
        // Get the counterpartyType
        restCounterpartyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCounterpartyType() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();

        // Update the counterpartyType
        CounterpartyType updatedCounterpartyType = counterpartyTypeRepository.findById(counterpartyType.getId()).get();
        // Disconnect from session so that the updates on updatedCounterpartyType are not directly saved in db
        em.detach(updatedCounterpartyType);
        updatedCounterpartyType
            .counterpartyTypeCode(UPDATED_COUNTERPARTY_TYPE_CODE)
            .counterPartyType(UPDATED_COUNTER_PARTY_TYPE)
            .counterpartyTypeDescription(UPDATED_COUNTERPARTY_TYPE_DESCRIPTION);
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(updatedCounterpartyType);

        restCounterpartyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterpartyTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterpartyType testCounterpartyType = counterpartyTypeList.get(counterpartyTypeList.size() - 1);
        assertThat(testCounterpartyType.getCounterpartyTypeCode()).isEqualTo(UPDATED_COUNTERPARTY_TYPE_CODE);
        assertThat(testCounterpartyType.getCounterPartyType()).isEqualTo(UPDATED_COUNTER_PARTY_TYPE);
        assertThat(testCounterpartyType.getCounterpartyTypeDescription()).isEqualTo(UPDATED_COUNTERPARTY_TYPE_DESCRIPTION);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository).save(testCounterpartyType);
    }

    @Test
    @Transactional
    void putNonExistingCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, counterpartyTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void partialUpdateCounterpartyTypeWithPatch() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();

        // Update the counterpartyType using partial update
        CounterpartyType partialUpdatedCounterpartyType = new CounterpartyType();
        partialUpdatedCounterpartyType.setId(counterpartyType.getId());

        restCounterpartyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterpartyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterpartyType))
            )
            .andExpect(status().isOk());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterpartyType testCounterpartyType = counterpartyTypeList.get(counterpartyTypeList.size() - 1);
        assertThat(testCounterpartyType.getCounterpartyTypeCode()).isEqualTo(DEFAULT_COUNTERPARTY_TYPE_CODE);
        assertThat(testCounterpartyType.getCounterPartyType()).isEqualTo(DEFAULT_COUNTER_PARTY_TYPE);
        assertThat(testCounterpartyType.getCounterpartyTypeDescription()).isEqualTo(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCounterpartyTypeWithPatch() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();

        // Update the counterpartyType using partial update
        CounterpartyType partialUpdatedCounterpartyType = new CounterpartyType();
        partialUpdatedCounterpartyType.setId(counterpartyType.getId());

        partialUpdatedCounterpartyType
            .counterpartyTypeCode(UPDATED_COUNTERPARTY_TYPE_CODE)
            .counterPartyType(UPDATED_COUNTER_PARTY_TYPE)
            .counterpartyTypeDescription(UPDATED_COUNTERPARTY_TYPE_DESCRIPTION);

        restCounterpartyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounterpartyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounterpartyType))
            )
            .andExpect(status().isOk());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);
        CounterpartyType testCounterpartyType = counterpartyTypeList.get(counterpartyTypeList.size() - 1);
        assertThat(testCounterpartyType.getCounterpartyTypeCode()).isEqualTo(UPDATED_COUNTERPARTY_TYPE_CODE);
        assertThat(testCounterpartyType.getCounterPartyType()).isEqualTo(UPDATED_COUNTER_PARTY_TYPE);
        assertThat(testCounterpartyType.getCounterpartyTypeDescription()).isEqualTo(UPDATED_COUNTERPARTY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, counterpartyTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounterpartyType() throws Exception {
        int databaseSizeBeforeUpdate = counterpartyTypeRepository.findAll().size();
        counterpartyType.setId(count.incrementAndGet());

        // Create the CounterpartyType
        CounterpartyTypeDTO counterpartyTypeDTO = counterpartyTypeMapper.toDto(counterpartyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCounterpartyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(counterpartyTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CounterpartyType in the database
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(0)).save(counterpartyType);
    }

    @Test
    @Transactional
    void deleteCounterpartyType() throws Exception {
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);

        int databaseSizeBeforeDelete = counterpartyTypeRepository.findAll().size();

        // Delete the counterpartyType
        restCounterpartyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, counterpartyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CounterpartyType> counterpartyTypeList = counterpartyTypeRepository.findAll();
        assertThat(counterpartyTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CounterpartyType in Elasticsearch
        verify(mockCounterpartyTypeSearchRepository, times(1)).deleteById(counterpartyType.getId());
    }

    @Test
    @Transactional
    void searchCounterpartyType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        counterpartyTypeRepository.saveAndFlush(counterpartyType);
        when(mockCounterpartyTypeSearchRepository.search("id:" + counterpartyType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(counterpartyType), PageRequest.of(0, 1), 1));

        // Search the counterpartyType
        restCounterpartyTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + counterpartyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counterpartyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterpartyTypeCode").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].counterPartyType").value(hasItem(DEFAULT_COUNTER_PARTY_TYPE)))
            .andExpect(jsonPath("$.[*].counterpartyTypeDescription").value(hasItem(DEFAULT_COUNTERPARTY_TYPE_DESCRIPTION.toString())));
    }
}
